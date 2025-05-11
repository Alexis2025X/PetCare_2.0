package com.alexis.petcare20;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class crear_cuenta extends AppCompatActivity {

    FloatingActionButton fab;
    Button btn;
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
            abrirLogin();
        });
    }
    private void abrirLogin() {
        // Aquí puedes abrir la ventana de agregar citas
        // Por ejemplo, puedes iniciar una nueva actividad
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}