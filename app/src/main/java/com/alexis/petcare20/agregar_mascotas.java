package com.alexis.petcare20;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
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

public class agregar_mascotas extends AppCompatActivity {
    FloatingActionButton fab;
    DB db;
    ImageView img;
    String idMascota = "";
    String urlCompletaFoto = "";
    String accion = "nuevo";
    Button btnGuardarMascota;
    Intent tomarFotoMascotaIntent;
    String cuentaID;

    TextView temval;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_mascotas);
        db = new DB(this);
        img = findViewById(R.id.imgFotoMascotaCita);

        fab = findViewById(R.id.fabListaMascotas);
        fab.setOnClickListener(view -> {
            abrirListaMascotas();
        });

        btnGuardarMascota = findViewById(R.id.btnGuardarMascota);
        btnGuardarMascota.setOnClickListener(view -> {
            guardarMascota();
        });
        cuentaID = datosCuentaEnUso.getIdCuenta();
        //mostrarMsg("Este es el usuario: " + cuentaID);
        mostarDatosMascotaModificar();
        tomarFoto();
    }

    private void mostarDatosMascotaModificar() {

        try {

            Bundle parameters = getIntent().getExtras();

            accion = parameters.getString("accion");


            if (accion.equals("modificar")) {
                JSONObject datos = new JSONObject(parameters.getString("mascotas"));
                TextView temval = findViewById(R.id.txtNombreMascota);
                temval.setText(datos.getString("nombre"));
                temval = findViewById(R.id.txtEdad);
                temval.setText(datos.getString("edad"));
                temval = findViewById(R.id.txtRaza);
                temval.setText(datos.getString("raza"));
                temval = findViewById(R.id.txtProblemasMedicos);
                temval.setText(datos.getString("problemas_medicos"));
                temval = findViewById(R.id.txtDueño);
                temval.setText(datos.getString("dueño"));

                idMascota = datos.getString("idMascota");

            }
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }

    }

    private void guardarMascota() {
    try {
        temval = findViewById(R.id.txtNombreMascota);
        String nombre = temval.getText().toString();
        temval = findViewById(R.id.txtEdad);
        String edad = temval.getText().toString();
        temval = findViewById(R.id.txtRaza);
        String raza = temval.getText().toString();
        temval = findViewById(R.id.txtProblemasMedicos);
        String problemasMedicos = temval.getText().toString();
        temval = findViewById(R.id.txtDueño);
        String dueño = temval.getText().toString();

        if (nombre.isEmpty() || edad.isEmpty() || raza.isEmpty() || problemasMedicos.isEmpty() || dueño.isEmpty()) {
            mostrarMsg("Por favor, completa todos los campos.");
            return;
        }
        cuentaID = datosCuentaEnUso.getIdCuenta();
        String datosMascota[] = {idMascota, dueño, nombre, edad, raza, problemasMedicos, urlCompletaFoto, cuentaID};
        //Toast.makeText(getApplicationContext(), "Datos: " + datosMascota[7], Toast.LENGTH_LONG).show();
        db = new DB(this);



        if (accion.equals("modificar")) {

          String respuesta =  db.administrar_Mascota("modificar", datosMascota);
            mostrarMsg("Estado del registro: " + respuesta );
            comprovacion(respuesta);
        } else {

            String respuesta =   db.administrar_Mascota("nuevo", datosMascota);
            mostrarMsg("Estado del registro: " + respuesta );
            comprovacion(respuesta);
        }

    } catch (Exception e) {
        mostrarMsg("Error: " + e.getMessage());
    }
    }

    private void comprovacion(String respuesta){
        if (respuesta.equals("ok")){
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("cargar_layout","mascotas");
                startActivity(intent);
        }
    }


    private void tomarFoto(){
        img.setOnClickListener(view->{
            tomarFotoMascotaIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File fotoMascota = null;
            try{
                fotoMascota = crearImagenMascota();
                if( fotoMascota!=null ){
                    Uri uriFotoMascota = FileProvider.getUriForFile(agregar_mascotas.this,
                            "com.alexis.petcare20.fileprovider", fotoMascota);
                    tomarFotoMascotaIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriFotoMascota);
                    startActivityForResult(tomarFotoMascotaIntent, 1);
                }else{
                    mostrarMsg("No fue posible tomar la fotografia..");
                }
            }catch (Exception e){
                mostrarMsg("Error: "+e.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if( requestCode==1 && resultCode==RESULT_OK ){
                        img.setImageURI(Uri.parse(urlCompletaFoto));
            }else{
                mostrarMsg("No fue posible tomar la fotografia.");
            }
        }catch (Exception e){
            mostrarMsg("Error: "+e.getMessage());
        }
    }

    private File crearImagenMascota() throws Exception{
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
    private void mostrarMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    private void abrirListaMascotas() {
        // Aquí puedes abrir la ventana de agregar citas
        // Por ejemplo, puedes iniciar una nueva actividad
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }




}