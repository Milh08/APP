package com.analisis.appesi.Actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.analisis.appesi.Modelo.User;
import com.analisis.appesi.R;
import com.analisis.appesi.SQL.Administrador;

public class B_Menu extends AppCompatActivity {
    private static final String TEMA_PREFRERENTE   = "MODE.tema";
    private static final String STRING_PREFERENCES = "login";

    Switch Tema;
    TextView Nombre_Completo, CerrarSesion;
    CardView BotonMuestra, BotonLocalizacion;
    public static User Datos_De_Usuario = A_Iniciar_Sesion.Datos_De_Usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b__menu);

        Tema              = findViewById(R.id.Tema);
        Nombre_Completo   = findViewById(R.id.Nombre_Completo);
        CerrarSesion      = findViewById(R.id.CerrarSesion);
        BotonMuestra      = findViewById(R.id.BotonMuestra);
        BotonLocalizacion = findViewById(R.id.BotonLocalizacion);

        Cambiar_Tema();
        Estado_De_CONEXION();

        Nombre_Completo.setText(
                Datos_De_Usuario.getNombre() + " " +
                Datos_De_Usuario.getApe_p()  + " " +
                Datos_De_Usuario.getApe_m()
        );

        Tema.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    Guardar_Modo_Visualizacion(true);
                }
                else{
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    Guardar_Modo_Visualizacion(false);
                }
            }
        });

        BotonLocalizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { abrir_ventana_Localizacion();
            }
        });
        BotonMuestra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrir_ventana_muestra();
            }
        });
        CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Volver_A_Login();
                finish();
            }
        });
    }
    private void Estado_De_CONEXION(){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){}
        else {
            BotonMuestra.setEnabled(false);
            if (!BotonMuestra.isEnabled()){
                Toast.makeText(
                        B_Menu.this,
                        "No tiene acceso a Internet para crear muestras",
                        Toast.LENGTH_LONG
                ).show();
            }
            Dar_Informacion();
        }
    }
    private void Dar_Informacion(){
        Administrador admin = new Administrador(
                this, "administracion", null, 1
        );
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        SharedPreferences preferences = getSharedPreferences(
                STRING_PREFERENCES, MODE_PRIVATE
        );
        String CORREO = preferences.getString("Correo", "");

        User datos_De_Usuario = new User();

        Cursor fila = BaseDeDatos.rawQuery(
                "SELECT * FROM usuario WHERE email ='" + CORREO + "'",
                null
        );

        if (fila.moveToFirst()){
            int id                  = fila.getInt(0);
            int municipio_id        = fila.getInt(1);
            String nombre           = fila.getString(2);
            String ape_p            = fila.getString(3);
            String ape_m            = fila.getString(4);
            String email            = fila.getString(5);
            String password         = fila.getString(6);
            String direccion        = fila.getString(7);
            String telefono         = fila.getString(8);
            int rol                 = 0;
            String token_activacion = "si";
            String imgperfil        = "si";
            String created_at       = "si";
            String updated_at       = "si";

            datos_De_Usuario.setId(id);
            datos_De_Usuario.setMunicipio_id(municipio_id);
            datos_De_Usuario.setNombre(nombre);
            datos_De_Usuario.setApe_p(ape_p);
            datos_De_Usuario.setApe_m(ape_m);
            datos_De_Usuario.setEmail(email);
            datos_De_Usuario.setPassword(password);
            datos_De_Usuario.setDireccion(direccion);
            datos_De_Usuario.setTelefono(telefono);
            datos_De_Usuario.setRol(rol);
            datos_De_Usuario.setActivo(true);
            datos_De_Usuario.setEliminado(false);
            datos_De_Usuario.setConf_correo(true);
            datos_De_Usuario.setToken_activacion(token_activacion);
            datos_De_Usuario.setImgperfil(imgperfil);
            datos_De_Usuario.setCreated_at(created_at);
            datos_De_Usuario.setUpdated_at(updated_at);

            Datos_De_Usuario = datos_De_Usuario;

            BaseDeDatos.close();
        }
        else {
            Toast.makeText(
                    this,
                    "No existe informacion para dar asignar",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
    private void Volver_A_Login(){
        Reestablecer_Login();
        Intent Ventana_Login = new Intent(this, A_Iniciar_Sesion.class);
        startActivity(Ventana_Login);
    }
    private void Reestablecer_Login(){
        SharedPreferences preferences = getSharedPreferences(
                STRING_PREFERENCES, MODE_PRIVATE
        );
        preferences.edit().putString("Correo", "").apply();
        preferences.edit().putString("Contraseña", "").apply();
        preferences.edit().putInt("estado", 0).apply();
    }
    public void Cambiar_Tema(){
        if (Obtener_Modo_Visualizacion()){
            Tema.setChecked(Obtener_Modo_Visualizacion());
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
    public void Guardar_Modo_Visualizacion(Boolean valor){
        SharedPreferences preferences = getSharedPreferences(TEMA_PREFRERENTE, MODE_PRIVATE);
        preferences.edit().putBoolean("Modo_Oscuro", valor).apply();
    }
    public boolean Obtener_Modo_Visualizacion(){
        SharedPreferences preferences = getSharedPreferences(TEMA_PREFRERENTE, MODE_PRIVATE);
        return preferences.getBoolean("Modo_Oscuro", false);
    }
    public void abrir_ventana_Localizacion(){
        Intent ventana_Localizacion = new Intent(this, C_Localizacion.class);
        ventana_Localizacion.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(ventana_Localizacion);
    }
    public void abrir_ventana_muestra(){
        Intent ventana_muestra = new Intent(this, D_Generar_Muestra.class);
        ventana_muestra.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(ventana_muestra);
    }
}