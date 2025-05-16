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

public class agregar_chat extends AppCompatActivity {
    FloatingActionButton fab;
    Button btn;
    DB db;
    Bundle parametros = new Bundle();
    String accion = "nuevo";
    String idChat = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_chat);
        db = new DB(this);
        btn = findViewById(R.id.btnGuardarChat);
        btn.setOnClickListener(view->guardarChat());

        fab = findViewById(R.id.fabListaChat);
        fab.setOnClickListener(view->abrirVentana());
    }
    private void abrirVentana() {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(parametros);
        startActivity(intent);

    }
    private void guardarChat() {

    }
}