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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        String[] datos = {idCitas, nombreMascota, fecha, clinica, nota, cuentaID};
        //Toast.makeText(getApplicationContext(), "Datos: " + datos[5], Toast.LENGTH_LONG).show();
        String mensaje = db.administrar_Citas(accion, datos);
       // db.administrar_Citas(accion, datos);

/*        AlertDialog.Builder confirmacion = new AlertDialog.Builder(this);
        confirmacion.setTitle("ERROR");
        confirmacion.setMessage("Este es el error: "+ mensaje.toString());
        confirmacion.create().show();*/

        Toast.makeText(getApplicationContext(), "Registro guardado con exito. "+  mensaje , Toast.LENGTH_LONG).show();
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
}