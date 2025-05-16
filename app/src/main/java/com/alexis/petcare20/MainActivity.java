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
    FloatingActionButton fabAgregarMascotas, fabAgregarCitas, fabAgregarChat;
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
//Para mascotas

    //JSONArray jsonArray;
    final ArrayList<mascotas> alMascotas = new ArrayList<mascotas>();
    final ArrayList<mascotas> alMascotasCopia = new ArrayList<mascotas>();
    ListView ltsMascotas;
    Cursor cMascotas;
    //JSONObject jsonObject;
    mascotas misMascotas;
    //int posicion = 0;
    //Bundle parametros = new Bundle();
    //DB db;

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

        fabAgregarCitas = findViewById(R.id.fabAgregarCitasMascotas);
        fabAgregarChat = findViewById(R.id.fabAgregarChat);

        //Listo por defecto
        fabAgregarMascotas = findViewById(R.id.fabAgregarMascotas);
        fabAgregarMascotas.setOnClickListener(view->abrirAgregarMascotas());


        parametros.putString("accion", "nuevo");

        db = new DB(this);
        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.mascotasMenu) {
                layout_mascotas.setVisibility(View.VISIBLE);
                layout_chat.setVisibility(View.GONE);
                layout_citas.setVisibility(View.GONE);
                layout_veterinarios.setVisibility(View.GONE);
                layout_cuenta.setVisibility(View.GONE);

/*                fab = findViewById(R.id.fabAgregarMascotas);
                //fab.setVisibility(View.VISIBLE);
                fab.setOnClickListener(view->abrirAgregarMascotas());*/
                fabAgregarMascotas.setVisibility(View.VISIBLE);
                fabAgregarMascotas.setOnClickListener(view->abrirAgregarMascotas());
                fabAgregarCitas.setVisibility(View.GONE);
                fabAgregarChat.setVisibility(View.GONE);


                return true;
            }else if (item.getItemId() == R.id.chatMenu) {
                layout_mascotas.setVisibility(View.GONE);
                layout_chat.setVisibility(View.VISIBLE);
                layout_citas.setVisibility(View.GONE);
                layout_veterinarios.setVisibility(View.GONE);
                layout_cuenta.setVisibility(View.GONE);

                //Los fabs desaparecen
                fabAgregarChat.setVisibility(View.VISIBLE);
                fabAgregarChat.setOnClickListener(view->abrirAgregarChat());
                fabAgregarMascotas.setVisibility(View.GONE);
                fabAgregarCitas.setVisibility(View.GONE);

                return true;
            }else if (item.getItemId() == R.id.citasMenu) {
                layout_mascotas.setVisibility(View.GONE);
                layout_chat.setVisibility(View.GONE);
                layout_citas.setVisibility(View.VISIBLE);
                layout_veterinarios.setVisibility(View.GONE);
                layout_cuenta.setVisibility(View.GONE);

/*                fab = findViewById(R.id.fabAgregarCitasMascotas);
                fab.setOnClickListener(view->abrirVentana());*/
                fabAgregarChat.setVisibility(View.GONE);
                fabAgregarCitas.setVisibility(View.VISIBLE);
                fabAgregarCitas.setOnClickListener(view->abrirVentana());
                fabAgregarMascotas.setVisibility(View.GONE);

                return true;
            } else if (item.getItemId() == R.id.veterinariosMenu) {
                layout_mascotas.setVisibility(View.GONE);
                layout_chat.setVisibility(View.GONE);
                layout_citas.setVisibility(View.GONE);
                layout_veterinarios.setVisibility(View.VISIBLE);
                layout_cuenta.setVisibility(View.GONE);
                //Los fabs desaparecen
                fabAgregarChat.setVisibility(View.GONE);
                fabAgregarMascotas.setVisibility(View.GONE);
                fabAgregarCitas.setVisibility(View.GONE);

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
                //Los fabs desaparecen
                fabAgregarChat.setVisibility(View.GONE);
                fabAgregarMascotas.setVisibility(View.GONE);
                fabAgregarCitas.setVisibility(View.GONE);
                    return true;
            }
            return false;
        });
        obtenerDatosCitas();
        buscarCitas();
        //para mascotas
        obtenerDatosMascotas();
        buscarMascotas();

    }
    private void abrirVentana(){
        Intent intent = new Intent(this, agregar_citas.class);
        intent.putExtras(parametros);
        startActivity(intent);
    }
    private void abrirAgregarMascotas(){
        Intent intent = new Intent(this, agregar_mascotas.class);
        intent.putExtras(parametros);
        startActivity(intent);
    }
    private void abrirAgregarChat(){
        Intent intent = new Intent(this, agregar_chat.class);
        intent.putExtras(parametros);
        startActivity(intent);
    }
    /////////////////////////////////////////
    //LA FUNCIÓN MSJ SIRVE PARA AMBOS
    private void mostrarMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
    //La función de menu sirve para mascotas y citas
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
    //La función de menu sirve para mascotas y citas SI LO PUEDES REFACTORIZAR HAZLO
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        
        if(layout_mascotas.getVisibility() == View.VISIBLE){
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
                //return true;
            } catch (Exception e) {
                mostrarMsg("Error: " + e.getMessage());
                return super.onContextItemSelected(item);
            }
        }else if(layout_citas.getVisibility() == View.VISIBLE){
            //menu de citas
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
                //return true;
            }catch (Exception e){
                mostrarMsg("Error: " + e.getMessage());
                return super.onContextItemSelected(item);
            }
        }
        return true;
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
                if(layout_citas.getVisibility() == View.VISIBLE){
                    mostrarMsg("No hay citas registrados.");
                    abrirVentana();
                }

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

    //AQUI COMIENZA PARA MASCOTAS
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
                if(layout_mascotas.getVisibility() == View.VISIBLE){
                    mostrarMsg("No hay mascotas registrados.");
                    abrirAgregarMascotas();
                }

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
    /*@Override
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
    }*/
//LA COMENTE TEMPORALMENTE
 /*   @Override
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
    }*/

    private  void eliminarMascota(){
        try {
            db = new DB(this);
            String idMascota = jsonArray.getJSONObject(posicion).getString("idMascota");
            String respuesta = db.administrar_Mascota("eliminar", new String[]{idMascota});
            if (respuesta.equals("ok")) {
                obtenerDatosMascotas();
                mostrarMsg("Registro eliminada.");
            } else {
                mostrarMsg("Error: " + respuesta);
            }
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }


}

