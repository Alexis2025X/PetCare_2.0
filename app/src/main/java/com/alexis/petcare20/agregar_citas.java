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
    ImageView img;
    String urlCompletaFoto = "";
    Intent tomarFotoIntent;
    String miToken = "";

    String miKey = "";
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_citas);
        img = findViewById(R.id.imgFotoMascotaCita);

        db = new DB(this);
        //usuario = getIntent().getStringExtra("usuarioCuenta");

        btn = findViewById(R.id.btnGuardarCita);
        btn.setOnClickListener(view->guardarCita());

        fab = findViewById(R.id.fabListaCitasMascotas);
        fab.setOnClickListener(view->abrirVentana());
        cuentaID = datosCuentaEnUso.getIdCuenta();
       // mostrarMsg("Este es el usuario: " + cuentaID);
        mostrarDatos();
        try {
            tomarFoto();
        }catch (Exception e){
            mostrarMsg("Error al tomar foto: "+e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if( requestCode==1 && resultCode==RESULT_OK ){
                img.setImageURI(Uri.parse(urlCompletaFoto));
            }else{
                mostrarMsg("No se tomo la foto.");
            }
        }catch (Exception e){
            mostrarMsg("Error al tomar la foto: "+e.getMessage());
        }
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

                urlCompletaFoto = datos.getString("urlFoto");
                img.setImageURI(Uri.parse(urlCompletaFoto));
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
                updates.put("urlFoto", urlCompletaFoto);
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

                String[] datos = {idCitas, nombreMascota, fecha, clinica, nota, urlCompletaFoto, cuentaID,key};
                String mensaje = db.administrar_Citas(accion, datos);

                citas cita = new citas(idCitas, nombreMascota, fecha, clinica, nota, urlCompletaFoto, cuentaID,key);
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
    private void tomarFoto(){
        img.setOnClickListener(view->{
            tomarFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File fotoChat = null;
            try{
                fotoChat = crearImagenCita();
                if( fotoChat!=null ){
                    Uri uriFotoAimgo = FileProvider.getUriForFile(agregar_citas.this,
                            "com.alexis.petcare20.fileprovider", fotoChat);
                    tomarFotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriFotoAimgo);
                    startActivityForResult(tomarFotoIntent, 1);
                }else{
                    mostrarMsg("No se pudo crear la imagen.");
                }
            }catch (Exception e){
                mostrarMsg("Error al tomar foto: "+e.getMessage());
            }
        });
    }
    private File crearImagenCita() throws Exception{
        String fechaHoraMs = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()),
                fileName = "imagen_"+ fechaHoraMs+"_";
        File dirAlmacenamiento = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        if( dirAlmacenamiento.exists()==false ){
            dirAlmacenamiento.mkdir();
        }
        File image = File.createTempFile(fileName, ".jpg", dirAlmacenamiento);
        urlCompletaFoto = image.getAbsolutePath();
        return image;
    }

}