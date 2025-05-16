package com.alexis.petcare20;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.stream.Stream;

public class crear_cuenta extends AppCompatActivity {

    FloatingActionButton fab;
    Button btn;
    DB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_cuenta);

        fab = findViewById(R.id.fabLogin);
        fab.setOnClickListener(view -> {
            abrirLogin();
        });
        btn = findViewById(R.id.btnCrearCuenta);
        btn.setOnClickListener(view -> {
            crearCuenta();
            abrirLogin();
        });
    }

    private void crearCuenta() {
        TextView temval = findViewById(R.id.txtUsuario);
        String usuario = temval.getText().toString();
         temval = findViewById(R.id.txtPassword);
        String contraseña = temval.getText().toString();
         temval = findViewById(R.id.txtCorreo);
        String correo = temval.getText().toString();
        temval = findViewById(R.id.txtNombreUser);
        String nombre = temval.getText().toString();


        if (nombre.isEmpty() || usuario.isEmpty() || contraseña.isEmpty() || correo.isEmpty()) {
            mostrarMsg("Por favor, completa todos los campos.");
            return;
        }
        db = new DB(this);
        String[] datos = {"",nombre,usuario, contraseña, correo};
      String respuesta =  db.administrar_cuentas("nuevo", datos);
      mostrarMsg("Estado de la cuenta: "  + respuesta);
    }


    private void abrirLogin() {
        // Aquí puedes abrir la ventana de agregar citas
        // Por ejemplo, puedes iniciar una nueva actividad
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
    private void mostrarMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

}