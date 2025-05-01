package com.alexis.petcare20;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login extends AppCompatActivity {

    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        btn = findViewById(R.id.btnCrearCuenta);
        btn.setOnClickListener(view -> {
            abrirCrearCuenta();
        });
        btn = findViewById(R.id.btnIniciarSesion);
        btn.setOnClickListener(view -> {
            abrirPantallaPrincipal();
        });


    }
    private void abrirCrearCuenta() {
        // Aquí puedes abrir la ventana de agregar citas
        // Por ejemplo, puedes iniciar una nueva actividad
        Intent intent = new Intent(this, crear_cuenta.class);
        startActivity(intent);
    }

    private void abrirPantallaPrincipal() {
        // Aquí puedes abrir la ventana de agregar citas
        // Por ejemplo, puedes iniciar una nueva actividad
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}