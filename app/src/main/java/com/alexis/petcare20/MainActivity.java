package com.alexis.petcare20;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

   // private LinearLayout layout_mascotas, layout_chat, layout_citas, layout_veterinarios, layout_cuenta;
    LinearLayout layout_mascotas, layout_chat, layout_citas, layout_veterinarios, layout_cuenta;
    BottomNavigationView bottomNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout_mascotas = findViewById(R.id.layout_mascotas);
        layout_chat = findViewById(R.id.layout_chat);
        layout_citas = findViewById(R.id.layout_citas);
        layout_veterinarios = findViewById(R.id.layout_veterinarios);
        layout_cuenta = findViewById(R.id.layout_cuenta);

        bottomNav = findViewById(R.id.bottom_nav);
        //linerLayout = findViewById(R.id.layout_mascotas);
        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.mascotasMenu) {
                layout_mascotas.setVisibility(View.VISIBLE);
                layout_chat.setVisibility(View.GONE);
                layout_citas.setVisibility(View.GONE);
                layout_veterinarios.setVisibility(View.GONE);
                layout_cuenta.setVisibility(View.GONE);

                return true;
            }else if (item.getItemId() == R.id.chatMenu) {
                layout_mascotas.setVisibility(View.GONE);
                layout_chat.setVisibility(View.VISIBLE);
                layout_citas.setVisibility(View.GONE);
                layout_veterinarios.setVisibility(View.GONE);
                layout_cuenta.setVisibility(View.GONE);

                return true;
            }else if (item.getItemId() == R.id.citasMenu) {
                layout_mascotas.setVisibility(View.GONE);
                layout_chat.setVisibility(View.GONE);
                layout_citas.setVisibility(View.VISIBLE);
                layout_veterinarios.setVisibility(View.GONE);
                layout_cuenta.setVisibility(View.GONE);
                return true;
            } else if (item.getItemId() == R.id.veterinariosMenu) {
                layout_mascotas.setVisibility(View.GONE);
                layout_chat.setVisibility(View.GONE);
                layout_citas.setVisibility(View.GONE);
                layout_veterinarios.setVisibility(View.VISIBLE);
                layout_cuenta.setVisibility(View.GONE);
                return true;
            } else if (item.getItemId() == R.id.cuentaMenu){
                    layout_mascotas.setVisibility(View.GONE);
                    layout_chat.setVisibility(View.GONE);
                    layout_citas.setVisibility(View.GONE);
                    layout_veterinarios.setVisibility(View.GONE);
                    layout_cuenta.setVisibility(View.VISIBLE);
                    return true;
            }
            return false;
        });

    }
}
