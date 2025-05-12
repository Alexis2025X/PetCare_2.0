package com.alexis.petcare20;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
    FloatingActionButton fab;
    Button btn;
//    TextView tempVal;
    DB db;
    Bundle parametros = new Bundle();
    String accion = "nuevo";
    String idCitas = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_citas);

        db = new DB(this);

        btn = findViewById(R.id.btnGuardarCita);
        btn.setOnClickListener(view->guardarCita());

        fab = findViewById(R.id.fabListaCitasMascotas);
        fab.setOnClickListener(view->abrirVentana());

        mostrarDatos();
    }

    private void mostrarDatos(){
        try {
            Bundle parametros = getIntent().getExtras();
            accion = parametros.getString("accion");

//            jsonObject.put("idCitas", cCitas.getString(0));
//            jsonObject.put("nombre", cCitas.getString(1));
//            jsonObject.put("fecha", cCitas.getString(2));
//            jsonObject.put("clinica", cCitas.getString(3));
//            jsonObject.put("nota", cCitas.getString(4));
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

        if (nombreMascota.isEmpty() || fecha.isEmpty() || clinica.isEmpty() || nota.isEmpty()) {
            mostrarMsg("Error: Todos los campos son obligatorios.");
            return;
        }
        String[] datos = {idCitas, nombreMascota, fecha, clinica, nota};
        String mensaje = db.administrar_Citas(accion, datos);
       // db.administrar_Citas(accion, datos);

        Toast.makeText(getApplicationContext(), "Registro guardado con exito. "+  mensaje , Toast.LENGTH_LONG).show();
        abrirVentana();

    }

    private void mostrarMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}