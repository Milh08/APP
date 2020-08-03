package com.analisis.appesi.Actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.analisis.appesi.API.ClienteRetrofit;
import com.analisis.appesi.Modelo.A_Iniciar_Sesion_Response;
import com.analisis.appesi.Modelo.Post_Localizacion;
import com.analisis.appesi.Modelo.User;
import com.analisis.appesi.R;
import com.analisis.appesi.SQL.Administrador;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class A_Iniciar_Sesion extends AppCompatActivity {
    EditText CORREO, PASS;
    CardView ENTRAR;

    private static final String STRING_PREFERENCES = "login";
    public static User Datos_De_Usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_iniciar__sesion);
        EnableRuntimePermissionToAccessCamera();

        CORREO = (EditText) findViewById(R.id.Correo);
        PASS   = (EditText) findViewById(R.id.Pass);
        ENTRAR = (CardView) findViewById(R.id.Entrar);

        ENTRAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Iniciar_Sesion(
                        CORREO.getText().toString(),
                        PASS.getText().toString()
                );
            }
        });
    }

    public void onResume(){
        super.onResume();
        Estado_De_CONEXION();
    }
    private void Estado_De_CONEXION(){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){
            if (Obtener_Estado_Sesion() == 1){
                mostrar_Login();
                Subir_Localizaciones_Guardadas_Localmente();
            }
        } else {
            if (Obtener_Estado_Sesion() == 1){
                Abrir_Ventana_Menu();
                finish();
            } else {
                Toast.makeText(
                        this,
                        "No esta conectado",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }
    private void mostrar_Login(){
        SharedPreferences preferences = getSharedPreferences(
                STRING_PREFERENCES, MODE_PRIVATE
        );
        Iniciar_Sesion(
                preferences.getString("Correo", ""),
                preferences.getString("Contraseña", "")
        );
    }
    private void Iniciar_Sesion(String email, String pass){
        String correo     = email;
        String contraseña = pass;

        if (correo.isEmpty()){
            Toast.makeText(this,
                    "El campo del correo esta vacio",
                    Toast.LENGTH_LONG
            ).show();
        }
        if (contraseña.isEmpty()){
            Toast.makeText(
                    this,
                    "El campo de contraseña esta vacio",
                    Toast.LENGTH_LONG
            ).show();
        }
        if (!Validar_Correo(correo)){
            Toast.makeText(
                    this,
                    "Correo invalido",
                    Toast.LENGTH_LONG
            ).show();
        }
        else if (!contraseña.isEmpty()){
            Call<A_Iniciar_Sesion_Response> call = ClienteRetrofit
                    .getInstance().getApi()
                    .Datos_Login(correo, contraseña);
            call.enqueue(new Callback<A_Iniciar_Sesion_Response>() {
                @Override
                public void onResponse(Call<A_Iniciar_Sesion_Response> call, Response<A_Iniciar_Sesion_Response> response) {
                    if (!response.isSuccessful()){
                        if (response.code() == 401){
                            Toast.makeText(
                                    A_Iniciar_Sesion.this,
                                    "Datos Incorrectos VERIFICA TU INFORMACIÓN",
                                    Toast.LENGTH_LONG
                            ).show();
                        } else {
                            Toast.makeText(
                                    A_Iniciar_Sesion.this,
                                    "" + response.code(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                        return;
                    }
                    Datos_De_Usuario = response.body().getUser();
                    if (Obtener_Estado_Sesion() == 0) Guardar_Estado_Sesion();
                    Registrar();
                    Abrir_Ventana_Menu();
                    finish();
                }

                @Override
                public void onFailure(Call<A_Iniciar_Sesion_Response> call, Throwable t) {
                    Toast.makeText(
                            A_Iniciar_Sesion.this,
                            "Error en la conexión",
                            Toast.LENGTH_LONG
                    ).show();
                }
            });
        }
    }
    private void Borrar_InfoTabla(String Nombre_Tabla){
        Administrador admin = new Administrador(
                this,
                "administracion",
                null,
                1
        );
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        BaseDeDatos.execSQL("DELETE FROM " + Nombre_Tabla);
        BaseDeDatos.close();
    }
    private void Subir_Localizaciones_Guardadas_Localmente(){
        Administrador admin = new Administrador(
                this,
                "administracion",
                null,
                1
        );
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        Cursor info = BaseDeDatos.rawQuery(
                "SELECT * FROM postlocalizacion",
                null
        );

        if (info.moveToFirst()){
            try {
                while (info.moveToNext()){
                    Guardar_Informacion(
                            info.getString(0),
                            info.getString(1),
                            info.getInt(2),
                            info.getInt(3),
                            info.getInt(4)
                    );
                }
                Borrar_InfoTabla("postlocalizacion");
                BaseDeDatos.close();
                Toast.makeText(
                        this,
                        "Se cargaron las coordenadas guardadas",
                        Toast.LENGTH_SHORT
                ).show();
            } catch (Exception e){}
        }
    }
    private void Guardar_Informacion(
            String latitud, String longitud, int Lote_id,
            int produndidad, int User_id
    ){
        Call<Post_Localizacion> call = ClienteRetrofit
                .getInstance()
                .getApi()
                .Datos_De_Muestas(
                        latitud, longitud, Lote_id, produndidad, User_id
                );
        call.enqueue(new Callback<Post_Localizacion>() {
            @Override
            public void onResponse(Call<Post_Localizacion> call, Response<Post_Localizacion> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(
                            A_Iniciar_Sesion.this,
                            "Error al guardar los datos",
                            Toast.LENGTH_LONG
                    ).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<Post_Localizacion> call, Throwable t) {
                Toast.makeText(
                        A_Iniciar_Sesion.this,
                        "Error en la conexion",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
    private void Registrar(){
        Administrador admin = new Administrador(
                this,
                "administracion",
                null,
                1
        );
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        String correo, pass;
        correo = CORREO.getText().toString();
        pass   = PASS.getText().toString();

        ContentValues registroLogin = new ContentValues();
        registroLogin.put("correo",     correo);
        registroLogin.put("contrasena", pass);

        ContentValues registroUsuario = new ContentValues();
        registroUsuario.put("id",           Datos_De_Usuario.getId());
        registroUsuario.put("municipio_id",Datos_De_Usuario.getMunicipio_id());
        registroUsuario.put("nombre",       Datos_De_Usuario.getNombre());
        registroUsuario.put("ape_p",        Datos_De_Usuario.getApe_p());
        registroUsuario.put("ape_m",        Datos_De_Usuario.getApe_m());
        registroUsuario.put("email",        Datos_De_Usuario.getEmail());
        registroUsuario.put("password",     Datos_De_Usuario.getPassword());
        registroUsuario.put("direccion",    Datos_De_Usuario.getDireccion());
        registroUsuario.put("telefono",     Datos_De_Usuario.getTelefono());

        BaseDeDatos.insert("login", null, registroLogin);
        BaseDeDatos.insert("usuario", null, registroUsuario);
        BaseDeDatos.close();
    }
    private int Obtener_Estado_Sesion(){
        SharedPreferences preferences = getSharedPreferences(
                STRING_PREFERENCES, MODE_PRIVATE
        );
        return preferences.getInt("estado", 0);
    }
    private void Guardar_Estado_Sesion(){
        SharedPreferences preferences = getSharedPreferences(
                STRING_PREFERENCES, MODE_PRIVATE
        );
        preferences.edit().putString(
                "Correo",
                CORREO.getText().toString()
        ).apply();
        preferences.edit().putString(
                "Contraseña",
                PASS.getText().toString()
        ).apply();
        preferences.edit().putInt("estado", 1).apply();
    }
    private boolean Validar_Correo(String correo){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(correo).matches();
    }
    public void EnableRuntimePermissionToAccessCamera(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(
                A_Iniciar_Sesion.this,
                Manifest.permission.CAMERA
        )){
            Toast.makeText(
                    A_Iniciar_Sesion.this,
                    "CAMERA permission allows us to Access CAMERA app",
                    Toast.LENGTH_LONG
            ).show();
        }else {
            ActivityCompat.requestPermissions(
                    A_Iniciar_Sesion.this,
                    new String[]{Manifest.permission.CAMERA},
                    1
            );
        }
    }
    public void Abrir_Ventana_Menu(){
        Intent pantalla_Menu =new Intent(this, B_Menu.class);
        startActivity(pantalla_Menu);
    }
}