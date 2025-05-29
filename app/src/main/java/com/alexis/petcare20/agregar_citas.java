package com.alexis.petcare20;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class agregar_citas extends AppCompatActivity {
/*    private MainActivity mainActivity; // Referencia a MainActivity

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }*/
    FloatingActionButton fab;
    Button btn;
//    TextView tempVal;
    DB db;
    Bundle parametros = new Bundle();
    String accion = "nuevo";
    String idCitas = "";
    String cuentaID, user;
    String miToken = "";

    String miKey = "";
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_citas);

        db = new DB(this);
        //usuario = getIntent().getStringExtra("usuarioCuenta");

        btn = findViewById(R.id.btnGuardarCita);
        btn.setOnClickListener(view->guardarCita());

        fab = findViewById(R.id.fabListaCitasMascotas);
        fab.setOnClickListener(view->abrirVentana());
        cuentaID = datosCuentaEnUso.getIdCuenta();
       // mostrarMsg("Este es el usuario: " + cuentaID);
        mostrarDatos();
    }

    private void mostrarDatos(){
        try {
            Bundle parametros = getIntent().getExtras();
            accion = parametros.getString("accion");


            if (accion.equals("modificar")) {
                JSONObject datos = new JSONObject(parametros.getString("citas"));

                idCitas = datos.getString("idCitas");
                miKey = datos.getString("llave");
                TextView tempVal = findViewById(R.id.txtNombreCitaMascota);
                mostrarMsg(datos.getString("nombre") + "sfsdf");

                tempVal.setText(datos.getString("nombre"));


                tempVal = findViewById(R.id.txtFecha);
                tempVal.setText(datos.getString("fecha"));

                tempVal = findViewById(R.id.txtNombreClinica);
                tempVal.setText(datos.getString("clinica"));

                tempVal = findViewById(R.id.txtNota);
                tempVal.setText(datos.getString("nota"));
            }

        }catch (Exception e){
            AlertDialog.Builder confirmacion = new AlertDialog.Builder(this);
            confirmacion.setTitle("ERROR");
            confirmacion.setMessage("Este es el error: "+ e.getMessage());
            confirmacion.create().show();

            //mostrarMsg("Error 1: "+e.getMessage());
        }
    }
    private void abrirVentana() {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("cargar_layout","citas");
        intent.putExtras(parametros);
        startActivity(intent);

    }
    private void guardarCita(){

        TextView  tempVal = findViewById(R.id.txtNombreCitaMascota);
        String nombreMascota = tempVal.getText().toString();

        tempVal = findViewById(R.id.txtFecha);
        String fecha = tempVal.getText().toString();

        tempVal = findViewById(R.id.txtNombreClinica);
        String clinica = tempVal.getText().toString();

        tempVal = findViewById(R.id.txtNota);
        String nota = tempVal.getText().toString();

        //String usuario = getIntent().getStringExtra("usuarioCuenta");


        if (nombreMascota.isEmpty() || fecha.isEmpty() || clinica.isEmpty() || nota.isEmpty()) {
            mostrarMsg("Error: Todos los campos son obligatorios.");
            return;
        }
        cuentaID = datosCuentaEnUso.getIdCuenta();
        if (cuentaID == null || cuentaID.isEmpty()) {
            mostrarMsg("Error: Usuario no encontrado.");
            return;
        }

        //Toast.makeText(getApplicationContext(), "Datos: " + datos[5], Toast.LENGTH_LONG).show();


        if (accion == "modificar") {
            try {


            String[] datos = {idCitas, nombreMascota, fecha, clinica, nota, cuentaID,miKey};
            String mensaje = db.administrar_Citas(accion, datos);
            mostrarMsg("Estado de la cita: " + mensaje);
            //comienzo de actualizacion en fireBase

            try {
                Map<String, Object> updates = new HashMap<>();
                updates.put("idCitas",idCitas );
                updates.put("nombreMascota", nombreMascota);
                updates.put("fecha", fecha);
                updates.put("clinica", clinica);
                updates.put("nota", nota);
                updates.put("usuario", cuentaID);
                updates.put("llave", miKey);

                if( miKey!= null || miKey == ""){
                    databaseReference = FirebaseDatabase.getInstance().getReference("citas");
                    databaseReference.child(miKey).updateChildren(updates).addOnSuccessListener(success->{
                        mostrarMsg("Registro actualizado con exito.");
                    }).addOnFailureListener(failure->{
                        mostrarMsg("Error al actualizar datos: "+failure.getMessage());
                    });
                } else {
                    mostrarMsg("Error al guardar en firebase.");
                }
            } catch (Exception e) {
                mostrarMsg("ERROR al actualizar en Firebase: " + e.getMessage());
            }

            } catch (Exception e) {
                mostrarMsg("Error al actualizar: " + e.getMessage());
            }
        }else{
            if( miToken.equals("") || miToken==null ){
                obtenerToken();
            }
            try{

                databaseReference = FirebaseDatabase.getInstance().getReference("citas");
                String key = databaseReference.push().getKey();

                if (key == null) {
                    key = "";
                }

                String[] datos = {idCitas, nombreMascota, fecha, clinica, nota, cuentaID,key};
                String mensaje = db.administrar_Citas(accion, datos);

                citas cita = new citas(idCitas, nombreMascota, fecha, clinica, nota, cuentaID,key);
                if( key!= null ){
                    databaseReference.child(key).setValue(cita).addOnSuccessListener(success->{
                        mostrarMsg("Registro guardado con exito.");
                    }).addOnFailureListener(failure->{
                        mostrarMsg("Error al registrar datos: "+failure.getMessage());
                    });
                } else {
                    mostrarMsg("Error al guardar en firebase.");
                }
                mostrarMsg("Estado de la cita: " + mensaje);
            } catch (Exception e) {
                mostrarMsg("Error: " + e.getMessage());
            }
        }

       // db.administrar_Citas(accion, datos);

/*        AlertDialog.Builder confirmacion = new AlertDialog.Builder(this);
        confirmacion.setTitle("ERROR");
        confirmacion.setMessage("Este es el error: "+ mensaje.toString());
        confirmacion.create().show();*/

        abrirVentana();

    }
    private void mostrarAlertDialog(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage(msg);
        builder.setPositiveButton("Aceptar", null);
        builder.create().show();
    }
    private void mostrarMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
    private void obtenerToken(){
        try{
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(tarea->{
                if(!tarea.isSuccessful()){
                    mostrarMsg("Error al obtener token: "+tarea.getException().getMessage());
                }else{
                    miToken = tarea.getResult();
                }
            });
        }catch (Exception e){
            mostrarMsg("Error al obtener token: "+e.getMessage());
        }
    }

}