package com.alexis.petcare20;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login extends AppCompatActivity {

    Button btn;
    ImageButton btnIcon, btnIcon2;
    EditText editText;
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

        botonOjos();

    }

    private void botonOjos() {
        //Para ver o no el password
        btnIcon = findViewById(R.id.btnOcultar);
        btnIcon.setTag("oculto");
        btnIcon.setOnClickListener(view -> {
            //Lo pongo alreves por si btnIcon.getTag() = null
            if("oculto".equals(btnIcon.getTag())) {
                btnIcon.setImageResource(R.drawable.ojo_ver);
                btnIcon.setTag("visible");
                //Toast.makeText(this, "Ya puedes ver", Toast.LENGTH_SHORT).show();
                ver();
            }else{
                btnIcon.setImageResource(R.drawable.ojo_no_ver);
                btnIcon.setTag("oculto");
                ocultar();
                //Toast.makeText(this, "No puedes ver", Toast.LENGTH_SHORT).show();
            }

        });
    }
    private void ver() {
        editText = findViewById(R.id.txtPassword);
        editText.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD | android.text.InputType.TYPE_CLASS_TEXT);
    }
    private void ocultar() {
        editText = findViewById(R.id.txtPassword);
        editText.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD | android.text.InputType.TYPE_CLASS_TEXT);
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