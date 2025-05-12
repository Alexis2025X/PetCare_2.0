package com.alexis.petcare20;

import android.app.AlertDialog;
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
    Bundle parametros = new Bundle();
    ListView ltsCitas;
    Cursor cCitas;
    DB db;
    int posicion = 0;
    final ArrayList<citas> alCitas = new ArrayList<citas>();
    final ArrayList<citas> alCitasCopia = new ArrayList<citas>();
    JSONArray jsonArray;
    JSONObject jsonObject;
    citas misCitas;

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

        parametros.putString("accion", "nuevo");

        db = new DB(this);
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
        obtenerDatosCitas();
        buscarCitas();

    }
    private void abrirVentana(){
        Intent intent = new Intent(this, agregar_citas.class);
        intent.putExtras(parametros);
        startActivity(intent);
    }
    private void abrirAgregarMascotas(){
        Intent intent = new Intent(this, agregar_mascotas.class);
        startActivity(intent);
    }
    /////////////////////////////////////////
    private void mostrarMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
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
        try{
            if( item.getItemId()==R.id.mnxNuevo){
                abrirVentana();
            }else if( item.getItemId()==R.id.mnxModificar){
                parametros.putString("accion", "modificar");
                parametros.putString("citas", jsonArray.getJSONObject(posicion).toString());
                abrirVentana();
            } else if (item.getItemId()==R.id.mnxEliminar) {
                eliminarCita();
                obtenerDatosCitas();
                buscarCitas();
            }
            return true;
        }catch (Exception e){
            mostrarMsg("Error: " + e.getMessage());
            return super.onContextItemSelected(item);
        }
    }
    private void eliminarCita(){
        try{
            String nombre = jsonArray.getJSONObject(posicion).getString("nombre");
            AlertDialog.Builder confirmacion = new AlertDialog.Builder(this);
            confirmacion.setTitle("Esta seguro de eliminar a: ");
            confirmacion.setMessage(nombre);
            confirmacion.setPositiveButton("Si", (dialog, which) -> {
                try {
                    String respuesta = db.administrar_Citas("eliminar", new String[]{jsonArray.getJSONObject(posicion).getString("idCitas")});
                    if(respuesta.equals("ok")) {
                        obtenerDatosCitas();
                        mostrarMsg("Registro eliminado con exito");
                    }else{
                        mostrarMsg("Error: " + respuesta);
                    }
                }catch (Exception e){
                    mostrarMsg("Error: " + e.getMessage());
                }
            });
            confirmacion.setNegativeButton("No", (dialog, which) -> {
                dialog.dismiss();
            });
            confirmacion.create().show();
        }catch (Exception e){
            mostrarMsg("Error: " + e.getMessage());
        }
    }
    /*private void listarDatos(){
        try{
                jsonObject = new JSONObject(respuesta);
                jsonArray = jsonObject.getJSONArray("rows");
                mostrarDatosCitas();
            }else{//offline
                obtenerDatosCitas();
            }
        }catch (Exception e){
            mostrarMsg("Error: " + e.getMessage());
        }
    }*/
    private void obtenerDatosCitas(){
        try{
            cCitas = db.lista_Citas();
            //mostrarMsg(cCitas.toString() + "buyftyf");
            if(cCitas.moveToFirst()){
                jsonArray = new JSONArray();
                do{
                    jsonObject = new JSONObject();
                    jsonObject.put("idCitas", cCitas.getString(0));
                    jsonObject.put("nombre", cCitas.getString(1));
                    jsonObject.put("fecha", cCitas.getString(2));
                    jsonObject.put("clinica", cCitas.getString(3));
                    jsonObject.put("nota", cCitas.getString(4));
                    jsonArray.put(jsonObject);

                    //mostrarMsg(cCitas.getString(4) + "buyftyf");

                }while(cCitas.moveToNext());
                mostrarDatosCitas();
            }else{
                mostrarMsg("No hay citas registrados.");
                abrirVentana();
            }
        }catch (Exception e){
            mostrarMsg("Error: " + e.getMessage());
        }
    }
    private void mostrarDatosCitas(){
        try{
            if(jsonArray.length()>0){
                ltsCitas = findViewById(R.id.ltsCitas);
                alCitas.clear();
                //alCitasCopia.clear();

                for (int i=0; i<jsonArray.length(); i++){
                    jsonObject = jsonArray.getJSONObject(i);
                    misCitas = new citas(
                            jsonObject.getString("idCitas"),
                            jsonObject.getString("nombre"),
                            jsonObject.getString("fecha"),
                            jsonObject.getString("clinica"),
                            jsonObject.getString("nota")
                            //jsonObject.getString("foto")
                    );
                    alCitas.add(misCitas);
                }
                alCitasCopia.addAll(alCitas);
                ltsCitas.setAdapter(new AdaptadorCitas(this, alCitas));
                registerForContextMenu(ltsCitas);
            }else{
                mostrarMsg("No hay citas registrados.");
                abrirVentana();
            }
        }catch (Exception e){
            mostrarMsg("Error: " + e.getMessage());
        }
    }
    private void buscarCitas(){
        TextView tempVal = findViewById(R.id.txtBuscarCitas);
        tempVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                alCitas.clear();
                String buscar = tempVal.getText().toString().trim().toLowerCase();
                if( buscar.length()<=0){
                    alCitas.addAll(alCitasCopia);
                }else{
                    for (citas item: alCitasCopia){
                        if(item.getnombreMascota().toLowerCase().contains(buscar) ||
                                item.getfecha().toLowerCase().contains(buscar) ||
                                item.getclinica().toLowerCase().contains(buscar)){
                            alCitas.add(item);
                        }
                    }
                    ltsCitas.setAdapter(new AdaptadorCitas(getApplicationContext(), alCitas));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


}
