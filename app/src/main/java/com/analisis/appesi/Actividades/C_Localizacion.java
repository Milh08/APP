package com.analisis.appesi.Actividades;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.analisis.appesi.API.ClienteRetrofit;
import com.analisis.appesi.Modelo.Lista_Lotes;
import com.analisis.appesi.Modelo.Localizacion;
import com.analisis.appesi.Modelo.Post_Localizacion;
import com.analisis.appesi.Modelo.UserLote;
import com.analisis.appesi.R;
import com.analisis.appesi.SQL.Administrador;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class C_Localizacion extends AppCompatActivity {
    private static final String TEMA_PREFRERENTE = "MODE.tema";
    private static final String InfoTabla        = "Tabla.Lotes";

    Post_Localizacion post_localizacion = new Post_Localizacion();

    Spinner Lotes_Spinner, medidas;
    CardView GuardarUbicacion;

    List<UserLote> lista_lotes;
    LocationManager ubicacion;
    Double Latitud_D, Longitud_D;
    Integer cm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c__localizacion);
        getSupportActionBar().setTitle("Guardar ubicación");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Cambiar_Tema();
        Permiso_De_Localizacion();

        Lotes_Spinner = findViewById(R.id.Lotes_Spinner);
        medidas = findViewById(R.id.medidas);
        GuardarUbicacion = findViewById(R.id.GuardarUbicacion);

        Lista_De_Medidas();
        if (ESTADO_DE_CONEXION()) Listar_Lotes();
        else Consultar_Lotes_En_SQL();

        GuardarUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Obtener_Coordenadas();
                if (ESTADO_DE_CONEXION()) Guardar_Informacion();
                else Guardar_Informacion_SQL();
            }
        });

    }
    private void Guardar_Informacion(){
        post_localizacion.setUser_id(A_Iniciar_Sesion.Datos_De_Usuario.getId());

        Call<Post_Localizacion> call = ClienteRetrofit
                .getInstance().getApi().Datos_De_Muestas(
                        post_localizacion.getLatitud(),
                        post_localizacion.getLongitud(),
                        post_localizacion.getLote_id(),
                        post_localizacion.getProfundidad(),
                        post_localizacion.getUser_id()
                );
        call.enqueue(new Callback<Post_Localizacion>() {
            @Override
            public void onResponse(Call<Post_Localizacion> call, Response<Post_Localizacion> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(
                            C_Localizacion.this,
                            "Error al guardar los datos",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                if(response.isSuccessful())
                    Toast.makeText(
                            C_Localizacion.this,
                            "Ubicacion Almacenada",
                            Toast.LENGTH_SHORT
                    ).show();
            }

            @Override
            public void onFailure(Call<Post_Localizacion> call, Throwable t) {
                Toast.makeText(
                        C_Localizacion.this,
                        "Error en la conexion",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void Obtener_Coordenadas(){
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
        checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
        PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION},
                    1
            );
        }
        ubicacion = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location loc = ubicacion.getLastKnownLocation(
                LocationManager.GPS_PROVIDER
        );

        if (ubicacion != null){
            Latitud_D = loc.getLatitude();
            Longitud_D = loc.getLongitude();
            post_localizacion.setLatitud(Latitud_D.toString());
            post_localizacion.setLongitud(Longitud_D.toString());
            Toast.makeText(
                    C_Localizacion.this,
                    "Ubicación guardado con exito",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
    private void Listar_Lotes(){
        Call<List<UserLote>> call = ClienteRetrofit.getInstance()
                .getApi().Informacion_Lotes(
                        A_Iniciar_Sesion.Datos_De_Usuario.getId()
                );
        call.enqueue(new Callback<List<UserLote>>() {
            @Override
            public void onResponse(Call<List<UserLote>> call, Response<List<UserLote>> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(
                            C_Localizacion.this,
                            "" + response.code(),
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                lista_lotes = response.body();

                ArrayAdapter<UserLote> adapter = new ArrayAdapter<UserLote>(
                        C_Localizacion.this,
                        R.layout.spinner_modify,
                        lista_lotes
                );
                adapter.setDropDownViewResource(
                        android.R.layout.simple_spinner_item
                );
                Lotes_Spinner.setAdapter(adapter);

                Lotes_Spinner.setOnItemSelectedListener(
                        new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                UserLote userLote = (UserLote) parent
                                        .getSelectedItem();
                                if (lista_lotes.size() != Obtener_El_Tamaño_De_Filas()){
                                    Guardar_El_Tamaño_De_Filas(lista_lotes.size());
                                    Borrar_Info_Tabla("lotes");
                                    Registrar_Lotes_En_SQL(lista_lotes);
                                }
                                post_localizacion.setLote_id(userLote.getId());
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        }
                );
            }

            @Override
            public void onFailure(Call<List<UserLote>> call, Throwable t) {
                Toast.makeText(
                        C_Localizacion.this,
                        "Error al obtener la lista de lotes",
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
    private void Borrar_Info_Tabla(String Nombre_Tabla){
        Administrador admin = new Administrador(
                this, "administracion", null, 1
        );
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        BaseDeDatos.execSQL("DELETE FROM " + Nombre_Tabla);
        BaseDeDatos.close();
    }
    private void Consultar_Lotes_En_SQL(){
        List<UserLote> LOTES_SQL = new ArrayList<>();
        Administrador admin = new Administrador(
                this, "administracion", null, 1
        );
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        Cursor filaLT = BaseDeDatos.rawQuery(
                "SELECT * FROM lotes", null
        );
        Cursor filaLC = BaseDeDatos.rawQuery(
                "SELECT * FROM localizacion",
                null
        );

        while (filaLT.moveToNext() && filaLC.moveToNext()){
            UserLote userLote = new UserLote();
            Localizacion localizacion = new Localizacion();

            userLote.setId(filaLT.getInt(0));
            userLote.setUserId(filaLT.getInt(1));
            userLote.setMunicipioId(filaLT.getInt(2));
            userLote.setLocalizacioneId(filaLT.getInt(3));
            userLote.setNombre(filaLT.getString(4));
            userLote.setUsoSuelo(filaLT.getString(5));
            userLote.setEliminado(false);
            userLote.setCreatedAt("");
            userLote.setUpdatedAt("");

            localizacion.setId(filaLC.getInt(0));
            localizacion.setLatitud(filaLC.getString(1));
            localizacion.setLatitud(filaLC.getString(2));
            localizacion.setCreatedAt("");
            localizacion.setUpdatedAt("");

            userLote.setLocalizacion(localizacion);

            LOTES_SQL.add(userLote);
        }
        lista_lotes = LOTES_SQL;

        ArrayAdapter<UserLote> adapter = new ArrayAdapter<UserLote>(
                C_Localizacion.this,
                R.layout.spinner_modify, lista_lotes
        );
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_item
        );
        Lotes_Spinner.setAdapter(adapter);

        Lotes_Spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        UserLote userLote = (UserLote) parent.getSelectedItem();
                        post_localizacion.setLote_id(userLote.getId());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );
    }
    private void Guardar_Informacion_SQL(){
        post_localizacion.setUser_id(B_Menu.Datos_De_Usuario.getId());

        Administrador admin = new Administrador(
                this, "administracion", null, 1
        );
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        ContentValues postLocalizacion = new ContentValues();

        postLocalizacion.put("latitud",     post_localizacion.getLatitud());
        postLocalizacion.put("longitud",    post_localizacion.getLongitud());
        postLocalizacion.put("lote_id",     post_localizacion.getLote_id());
        postLocalizacion.put("profundidad", post_localizacion.getProfundidad());
        postLocalizacion.put("user_id",     post_localizacion.getUser_id());

        BaseDeDatos.insert(
                "postlocalizacion", null, postLocalizacion
        );

        BaseDeDatos.close();
    }
    private void Registrar_Lotes_En_SQL(List<UserLote> lista_lotes){
        Administrador admin = new Administrador(
                this, "administracion", null, 1
        );
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        int tamaño_Lista = lista_lotes.size();

        try {
            for (int i = 0; i < tamaño_Lista; i++){
                ContentValues registro_de_lotes = new ContentValues();
                ContentValues registro_de_localizacion = new ContentValues();

                // INFORMACION DE LOTES
                registro_de_lotes.put("id",             lista_lotes.get(i).getId());
                registro_de_lotes.put("userId",         lista_lotes.get(i).getUserId());
                registro_de_lotes.put("municipioId",    lista_lotes.get(i).getMunicipioId());
                registro_de_lotes.put("localizacionId", lista_lotes.get(i).getLocalizacioneId());
                registro_de_lotes.put("nombre",         lista_lotes.get(i).getNombre());
                registro_de_lotes.put("usoSuelo",       lista_lotes.get(i).getUsoSuelo());
                registro_de_lotes.put("eliminado",      lista_lotes.get(i).getEliminado());
                registro_de_lotes.put("createdAt",      lista_lotes.get(i).getCreatedAt());
                registro_de_lotes.put("updatedAt",      lista_lotes.get(i).getUpdatedAt());

                // INFORMACION DE LOCALIZACION DE LOS LOTES
                registro_de_localizacion.put("id",        lista_lotes.get(i).getLocalizacion().getId());
                registro_de_localizacion.put("latitud",   lista_lotes.get(i).getLocalizacion().getLatitud());
                registro_de_localizacion.put("longitud",  lista_lotes.get(i).getLocalizacion().getLongitud());
                registro_de_localizacion.put("createdAt", lista_lotes.get(i).getLocalizacion().getCreatedAt());
                registro_de_localizacion.put("updatedAt", lista_lotes.get(i).getLocalizacion().getUpdatedAt());


                BaseDeDatos.insert("lotes", null, registro_de_lotes);
                BaseDeDatos.insert("localizacion", null, registro_de_localizacion);
            }
        }
        catch (Exception e){
            Toast.makeText(
                    this,
                    "Error al almacenar lotes en BASE DE DATOS LOCAL",
                    Toast.LENGTH_LONG
            ).show();
        }
        BaseDeDatos.close();
    }
    private int Obtener_El_Tamaño_De_Filas(){
        SharedPreferences preferences = getSharedPreferences(
                InfoTabla, MODE_PRIVATE
        );
        return preferences.getInt("filas", 0);
    }
    private void Guardar_El_Tamaño_De_Filas(int Numero_Filas){
        SharedPreferences preferences = getSharedPreferences(
                InfoTabla, MODE_PRIVATE
        );
        preferences.edit().putInt("filas", Numero_Filas).apply();
    }
    public void Lista_De_Medidas(){
        Integer[] centimetros = {15, 25, 50, 100};

        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(
                C_Localizacion.this,
                R.layout.spinner_modify,
                centimetros
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        medidas.setAdapter(adapter);

        medidas.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        cm = (Integer) parent.getSelectedItem();
                        post_localizacion.setProfundidad(cm);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );
    }
    public Boolean ESTADO_DE_CONEXION(){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        Boolean conexionEstablecida = false;
        if (networkInfo != null && networkInfo.isConnected()){
            conexionEstablecida = true;
            return conexionEstablecida;
        }
        return conexionEstablecida;
    }
    public void Cambiar_Tema(){
        if (Obtener_Modo_Visualizacion()) getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
    public boolean Obtener_Modo_Visualizacion(){
        SharedPreferences preferences = getSharedPreferences(TEMA_PREFRERENTE, MODE_PRIVATE);
        return preferences.getBoolean("Modo_Oscuro", false);
    }
    public void Permiso_De_Localizacion(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                C_Localizacion.this,
                Manifest.permission.ACCESS_FINE_LOCATION
        )){
            Toast.makeText(
                    C_Localizacion.this,
                    "GPS permission allows us to Access GPS app",
                    Toast.LENGTH_LONG
            ).show();
        }
        else {
            ActivityCompat.requestPermissions(
                    C_Localizacion.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1
            );
        }
    }
}