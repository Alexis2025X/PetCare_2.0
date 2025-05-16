package com.alexis.petcare20;


import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

   // private LinearLayout layout_mascotas, layout_chat, layout_citas, layout_veterinarios, layout_cuenta;
    LinearLayout layout_mascotas, layout_chat, layout_citas, layout_veterinarios, layout_cuenta;
    BottomNavigationView bottomNav;
    FloatingActionButton fabAgregarMascotas;
    FloatingActionButton fabAgregarCitas;
    FloatingActionButton fabAgregarChat;
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
    final ArrayList<mascotas> alMascotas = new ArrayList<mascotas>();
    final ArrayList<mascotas> alMascotasCopia = new ArrayList<mascotas>();
    ListView ltsMascotas;
    Cursor cMascotas;
    mascotas misMascotas;
//Para el mapa
    TextView tempVal;
    TextView lblUbicacion, lblLatitud, lblLongitud;
    GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    //Location currentLocation;
    private Marker marcadorUbicacion;
    double latitud, longitud;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Para las vistas
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
                //Se muestra el mapa;
                mostrarMsg("Obteniendo ubicación...");
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

        //para mascotas
        obtenerDatosMascotas();
        buscarMascotas();
        //Para citas
        obtenerDatosCitas();
        buscarCitas();
        //Para el mapa

        lblUbicacion = findViewById(R.id.lblUbicacion);
/*        lblLatitud = findViewById(R.id.lblLatitud);
        lblLongitud = findViewById(R.id.lblLongitud);*/
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);

        obtenerPosicion();

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
    //Para obtener ubicación
    void obtenerPosicion(){
        try{
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != getPackageManager().PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != getPackageManager().PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                tempVal.setText("Solicitando permisos de ubicación...");
            }
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    try{
                        LatLng miUbicacion = new LatLng(location.getLatitude(), location.getLongitude());//Obtengo la ubicación
                        if(miUbicacion == null){
                            mostrarMsg("Error: No se pudo obtener la ubicación.");
                            LatLng ElSalvador = new LatLng(13.3432943,-88.4530736);
                            marcadorUbicacion = mMap.addMarker(new MarkerOptions().position(ElSalvador).title("El Salvador").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                        }
                        //Creamos el marcados solo la primera vez
                        if(marcadorUbicacion == null){
                            marcadorUbicacion = mMap.addMarker(new MarkerOptions().position(miUbicacion).title("Mi ubicación").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                        } else {
                            // Actualizar posición del marcador existente
                            marcadorUbicacion.setPosition(miUbicacion);
                        }
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(miUbicacion));
                        //Circulo alrededor de la ubicación
/*                    Circle circle = mMap.addCircle(new CircleOptions()
                            .center(miUbicacion)
                            .radius(1000*5) //mt->km
                            .strokeColor(Color.BLUE) // Color del borde
                            .strokeWidth(2f) // Ancho del borde
                            .fillColor(Color.argb(20, 0, 100, 255)) // Color de relleno (transparente)
                    );*/


                    }
                    catch (Exception e){
                        tempVal.setText("Error al obtener la ubicación: "+ e.getMessage());
                    }
                    //mMap.addMarker(new MarkerOptions().position(miUbicacion).title("Mi ubicación"));
                    mostrarUbicacion(location);

                }
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    tempVal.setText("Estado del proveedor: "+ status);
                }
                @Override
                public void onProviderEnabled(String provider) {
                    tempVal.setText("Proveedor habilitado: "+ provider);
                }
                @Override
                public void onProviderDisabled(String provider) {
                    //Tengo que ver como pedir que se habilite la ubicación
                    try{
                    tempVal.setText("Proveedor deshabilitado: "+ provider);
                    }
                    catch (Exception e){
                        //mostrarMsg("Proveedor deshabilitado: "+ e.getMessage());
                        mostrarMsg("Ubicación deshabilitada");
                    }
                }
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }catch (SecurityException e){
            tempVal.setText("Error al obtener la ubicación: "+ e.getMessage());
        }
    }
    void mostrarUbicacion(Location location){

        lblUbicacion.setText("Mi ubicación: "+"\nLatitud: "+ location.getLatitude() + "\nLongitud: "+ location.getLongitude() + "\nAltitud: "+ location.getAltitude());
/*        lblUbicacion.setText("Mi ubicación: "+"\nLatitud: "+ latitud + "\nLongitud: "+ longitud);*/
        latitud = location.getLatitude();
        longitud = location.getLongitude();

    }

    //Parte del mapa
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
            mMap = googleMap;
            this.mMap.setOnMapClickListener(this);
            this.mMap.setOnMapLongClickListener(this);
            //Para mostrar la ubicación actual
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            obtenerPosicion();
            //LatLng ElSalvador = new LatLng(13.3432943,-88.4530736);
            //mMap.addMarker(new MarkerOptions().position(ElSalvador).title("Ubicación de El Salvador"));
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(ElSalvador));//actualiza el mapa a ubicación indicada
        }
    //Por si solo hay un click corto

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
/*        lblLatitud.setText("Latitud: " +""+ latLng.latitude);
        lblLongitud.setText("Longitud: "+""+ latLng.longitude);*/

        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title("Ubicación seleccionada").snippet("Latitud: " + latLng.latitude + " Longitud: " + latLng.longitude));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

    }
    //Por si hay un click largo
    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
/*        lblLatitud.setText(""+ latLng.latitude);
        lblLongitud.setText(""+ latLng.longitude);*/
            mMap.addMarker(new MarkerOptions().position(latLng).title("Ubicación seleccionada").snippet("Latitud: " + latLng.latitude + " Longitud: " + latLng.longitude));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

    }

    //Para lugares cercanos



}

