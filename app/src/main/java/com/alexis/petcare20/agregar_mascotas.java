package com.alexis.petcare20;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class agregar_mascotas extends AppCompatActivity {
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_mascotas);


        fab = findViewById(R.id.fabListaMascotas);
        fab.setOnClickListener(view -> {
            abrirListaMascotas();
        });

    }
    private void abrirListaMascotas() {
        // Aquí puedes abrir la ventana de agregar citas
        // Por ejemplo, puedes iniciar una nueva actividad
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);


    }
}