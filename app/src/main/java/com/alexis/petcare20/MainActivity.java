package com.alexis.petcare20;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

   // private LinearLayout layout_mascotas, layout_chat, layout_citas, layout_veterinarios, layout_cuenta;
    LinearLayout layout_mascotas, layout_chat, layout_citas, layout_veterinarios, layout_cuenta;
    BottomNavigationView bottomNav;
    FloatingActionButton fab;
    Button btn;
    JSONArray jsonArray;
    final ArrayList<mascotas> alMascotas = new ArrayList<mascotas>();
    final ArrayList<mascotas> alMascotasCopia = new ArrayList<mascotas>();
    ListView ltsMascotas;
    Cursor cMascotas;
    JSONObject jsonObject;
    mascotas misMascotas;
    int posicion = 0;
    Bundle parametros = new Bundle();
    DB db;

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

        //Listo por defecto
        fab = findViewById(R.id.fabAgregarMascotas);
        fab.setOnClickListener(view->abrirAgregarMascotas());

        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.mascotasMenu) {
                layout_mascotas.setVisibility(View.VISIBLE);
                layout_chat.setVisibility(View.GONE);
                layout_citas.setVisibility(View.GONE);
                layout_veterinarios.setVisibility(View.GONE);
                layout_cuenta.setVisibility(View.GONE);

                fab = findViewById(R.id.fabAgregarMascotas);
                fab.setOnClickListener(view->abrirAgregarMascotas());

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

                fab = findViewById(R.id.fabAgregarCitasMascotas);
                fab.setOnClickListener(view->abrirVentana());

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

                    btn = findViewById(R.id.btnCerrarSesion);
                    btn.setOnClickListener(view -> {
                        Intent intent = new Intent(this, Login.class);
                        startActivity(intent);
                    });
                    return true;
            }
            return false;
        });
        obtenerDatosMascotas();
        buscarMascotas();

    }
    private void abrirVentana(){
        Intent intent = new Intent(this, agregar_citas.class);
        startActivity(intent);
    }
    private void abrirAgregarMascotas(){
        Intent intent = new Intent(this, agregar_mascotas.class);
        intent.putExtras(parametros);
        startActivity(intent);
    }





    private void obtenerDatosMascotas(){
        try{
            db = new DB(this);

            cMascotas = db.lista_mascotas();
            if(cMascotas.moveToFirst()){
                jsonArray = new JSONArray();
                do{


                    jsonObject = new JSONObject();
                    jsonObject.put("idMascota", cMascotas.getString(0));
                    jsonObject.put("dueño", cMascotas.getString(1));
                    jsonObject.put("nombre", cMascotas.getString(2));
                    jsonObject.put("edad", cMascotas.getString(3));
                    jsonObject.put("raza", cMascotas.getString(4));
                    jsonObject.put("problemas_medicos", cMascotas.getString(5));
                    jsonObject.put("foto", cMascotas.getString(6));
                    jsonArray.put(jsonObject);
                }while(cMascotas.moveToNext());

                mostrarMascotas();

            }else {
                mostrarMsg("No hay mascotas registrados.");
            
            }
        }catch (Exception e){
            mostrarMsg("Error: " + e.getMessage());
        }
    }

    private void mostrarMascotas() {

        try{

            if(jsonArray.length()>0){

                ltsMascotas = findViewById(R.id.ltsMascotas);
                alMascotas.clear();

                alMascotasCopia.clear();

                for (int i=0; i<jsonArray.length(); i++){
                    jsonObject = jsonArray.getJSONObject(i);
                    misMascotas = new mascotas(
                            jsonObject.getString("idMascota"),
                            jsonObject.getString("dueño"),
                            jsonObject.getString("nombre"),
                            jsonObject.getString("edad"),
                            jsonObject.getString("raza"),
                            jsonObject.getString("problemas_medicos"),
                            jsonObject.getString("foto")

                    );


                    alMascotas.add(misMascotas);
                }
                alMascotasCopia.addAll(alMascotas);
                ltsMascotas.setAdapter(new adaptadorMascotas(this,alMascotas));

                registerForContextMenu(ltsMascotas);

            }else{
                mostrarMsg("No hay amigos registrados.");
                
            }
        }catch (Exception e){
            mostrarMsg("Error: " + e.getMessage());
        }
        
    }

    private void buscarMascotas(){
        TextView tempVal = findViewById(R.id.txtBuscarMascotas);

        tempVal.addTextChangedListener(new TextWatcher() {
                     @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                alMascotas.clear();
                String buscar = tempVal.getText().toString().trim().toLowerCase();
                if( buscar.length()<=0){
                    alMascotas.addAll(alMascotasCopia);
                }else{
                    for (mascotas item: alMascotasCopia){
                        if(item.getNombre().toLowerCase().contains(buscar) ||
                                item.getEdad().toLowerCase().contains(buscar) ||
                                item.getDueño().toLowerCase().contains(buscar)){
                            alMascotas.add(item);

                        }
                    }
                    ltsMascotas.setAdapter(new adaptadorMascotas(getApplicationContext(), alMascotas));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mi_menu, menu);
        try {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            posicion = info.position;

                menu.setHeaderTitle(jsonArray.getJSONObject(posicion).getString("nombre"));


        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        try {
            if (item.getItemId() == R.id.mnxNuevo) {
                parametros.putString("accion", "nuevo");
                abrirAgregarMascotas();
            } else if (item.getItemId() == R.id.mnxModificar) {

                    parametros.putString("accion", "modificar");
                    parametros.putString("mascotas", jsonArray.getJSONObject(posicion).toString());

                abrirAgregarMascotas();

            } else if (item.getItemId() == R.id.mnxEliminar) {
                eliminarMascota();
                obtenerDatosMascotas();
                buscarMascotas();
            }
            return true;
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
            return super.onContextItemSelected(item);
        }
    }

    private  void eliminarMascota(){
        try {
            db = new DB(this);
            String idMascota = jsonArray.getJSONObject(posicion).getString("idMascota");
            String respuesta = db.administrar_Mascota("eliminar", new String[]{idMascota});
            if (respuesta.equals("ok")) {
                mostrarMsg("Registro eliminada.");
                obtenerDatosMascotas();
            } else {
                mostrarMsg("Error: " + respuesta);
            }
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }

    private void mostrarMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

}
