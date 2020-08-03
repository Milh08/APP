package com.analisis.appesi.Actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.analisis.appesi.API.ClienteRetrofit;
import com.analisis.appesi.Modelo.RespuestaAI;
import com.analisis.appesi.Modelo.Respuesta_Croma;
import com.analisis.appesi.Modelo.Respuesta_Muestra;
import com.analisis.appesi.Modelo.Respuesta_Municipio;
import com.analisis.appesi.Modelo.UserLote;
import com.analisis.appesi.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class D_Generar_Muestra extends AppCompatActivity {
    public static final String TEMA_PREFRERENTE = "MODE.tema";
    public static final int REQUEST_TAKE_PHOTO = 1;

    public static UserLote userLote;
    public static Respuesta_Croma LM;
    public static Bitmap bitmap;
    public static Uri photoURI;
    public static File photoFile;
    public static RespuestaAI respuestaAI;

    Respuesta_Municipio Region;
    List<UserLote> Lista_De_Lotes;
    List<Respuesta_Croma> Cromann;
    String ruta_Imagen;

    TextView Pais, Estado, Municipio, Nombre, Num_Muesta, Latitud, Longitud, Profundidad;
    Spinner Lista_Lotes, Lista_Muestras;
    Button BtnAnalizar, BtnFotografia;
    ImageView foto_img;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d__generar__muestra);
        getSupportActionBar().setTitle("Crear Nueva Muestra");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Pais           = findViewById(R.id.Pais);
        Estado         = findViewById(R.id.Estado);
        Municipio      = findViewById(R.id.Municipio);
        Nombre         = findViewById(R.id.Nombre_Lote);
        Num_Muesta     = findViewById(R.id.id_Muestra);
        Latitud        = findViewById(R.id.Latitud_CC);
        Longitud       = findViewById(R.id.Longitud_CC);
        Profundidad    = findViewById(R.id.Profundidad_CC);
        Lista_Lotes    = findViewById(R.id.Localizaciones);
        Lista_Muestras = findViewById(R.id.Lista_Muestras);
        BtnAnalizar    = findViewById(R.id.Analizar);
        BtnFotografia  = findViewById(R.id.Camara);
        foto_img       = findViewById(R.id.foto_img);

        Cambiar_Tema();
        Listar_Lotes();

        BtnFotografia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tomar_Fotografia_Intent();
            }
        });

        BtnAnalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalizarImagen();
            }
        });
    }
    private void AnalizarImagen(){
        RequestBody fileReqBody = RequestBody.create(
                MediaType.parse("multipart/form-data"),
                photoFile
        );
        MultipartBody.Part requestImage = MultipartBody.Part.createFormData(
                "ourfile",
                photoFile.getName(),
                fileReqBody
        );
        Call<RespuestaAI> call = ClienteRetrofit.getInstance2().getApi()
                .uploads(requestImage);
        call.enqueue(new Callback<RespuestaAI>() {
            @Override
            public void onResponse(Call<RespuestaAI> call, Response<RespuestaAI> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(
                            D_Generar_Muestra.this,
                            "Error al subir imagen IA " + response.code(),
                            Toast.LENGTH_LONG
                    ).show();
                    return;
                }
                try {
                    respuestaAI = response.body();
                    if (response.isSuccessful())
                        Abrir_Ventana_Resultados();
                }
                catch (Exception e){
                    Toast.makeText(
                            D_Generar_Muestra.this,
                            "Entro al error de la excepcion",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<RespuestaAI> call, Throwable t) {

            }
        });
    }
    private void Tomar_Fotografia_Intent(){
        Intent Tomar_Foto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (Tomar_Foto.resolveActivity(getPackageManager()) != null){
            try{
                photoFile = Crear_Archivo_Imagen();
            }
            catch (IOException ex){
                ex.printStackTrace();
            }

            if (photoFile != null){
                photoURI = FileProvider.getUriForFile(
                        this,
                        "com.example.appesi",
                        photoFile
                );
                Tomar_Foto.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(Tomar_Foto, REQUEST_TAKE_PHOTO);
                BtnAnalizar.setEnabled(true);
            }
        }
    }
    private File Crear_Archivo_Imagen() throws IOException{
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat(
                "yyyyMMdd_HHmmss"
        ).format(new Date());

        String NombreArchivoImagen = "JPEG_" + timeStamp + "_";

        File storageDir = getExternalFilesDir(
                Environment.DIRECTORY_PICTURES
        );

        File Archivo_Imagen = File.createTempFile(
                NombreArchivoImagen,
                ".jpg",
                storageDir
        );
        ruta_Imagen = Archivo_Imagen.getAbsolutePath();
        return Archivo_Imagen;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK){
            Uri uri = Uri.parse(ruta_Imagen);
            Log.e("photoUri on activiti.->", uri+"");
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
                Log.e("bitmap on activiti.->", bitmap+"");
                foto_img.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void Listar_Muestras(int id){
        Call<List<Respuesta_Croma>> call = ClienteRetrofit.getInstance()
                .getApi().Datos_Croma(id);
        call.enqueue(new Callback<List<Respuesta_Croma>>() {
            @Override
            public void onResponse(Call<List<Respuesta_Croma>> call, Response<List<Respuesta_Croma>> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(
                            D_Generar_Muestra.this,
                            "" + response.code(),
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                Cromann = response.body();
                ArrayAdapter<Respuesta_Croma> adapter = new ArrayAdapter<Respuesta_Croma>(
                        D_Generar_Muestra.this,
                        R.layout.spinner_modify, Cromann
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                Lista_Muestras.setAdapter(adapter);

                Lista_Muestras.setOnItemSelectedListener(
                        new AdapterView.OnItemSelectedListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                LM = (Respuesta_Croma) parent.getSelectedItem();
                                Num_Muesta.setText("");
                                Num_Muesta.setText(""
                                        + LM.getCromann().get(0).getMuestraId());
                                Informacion_De_Muestra(
                                        LM.getCromann().get(0).getId()
                                );
                                Profundidad.setText("");
                                Profundidad.setText(LM.getProfundidad());
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        }
                );
            }

            @Override
            public void onFailure(Call<List<Respuesta_Croma>> call, Throwable t) {
                Toast.makeText(
                        D_Generar_Muestra.this,
                        "Error al cargar lista de muestras",
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
    private void Informacion_De_Muestra(int id){
        Call<Respuesta_Muestra> call = ClienteRetrofit.getInstance().getApi()
                .informacion_Croma(id);
        call.enqueue(new Callback<Respuesta_Muestra>() {
            @Override
            public void onResponse(Call<Respuesta_Muestra> call, Response<Respuesta_Muestra> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(
                            D_Generar_Muestra.this,
                            "" + response.code(),
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                Respuesta_Muestra res = response.body();
                Latitud.setText("");
                Latitud.setText(Formato_Decimal(
                        res.getMuestra().getLocalizacion().getLatitud()
                ));
                Longitud.setText("");
                Longitud.setText(Formato_Decimal(
                        res.getMuestra().getLocalizacion().getLongitud()
                ));
            }

            @Override
            public void onFailure(Call<Respuesta_Muestra> call, Throwable t) {
                Toast.makeText(
                        D_Generar_Muestra.this,
                        "Error al cargar información de la muestra",
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
    private void Listar_Lotes(){
        Call<List<UserLote>> call = ClienteRetrofit.getInstance()
                .getApi()
                .Informacion_Lotes(A_Iniciar_Sesion.Datos_De_Usuario.getId());
        call.enqueue(new Callback<List<UserLote>>() {
            @Override
            public void onResponse(Call<List<UserLote>> call, Response<List<UserLote>> response) {
                if (!response.isSuccessful()){
                   Toast.makeText(
                            D_Generar_Muestra.this,
                            "" + response.code(),
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                Lista_De_Lotes = response.body();

                ArrayAdapter<UserLote> adapter = new ArrayAdapter<UserLote>(
                        D_Generar_Muestra.this,
                        R.layout.spinner_modify,
                        Lista_De_Lotes
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                Lista_Lotes.setAdapter(adapter);

                Lista_Lotes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        userLote = (UserLote) parent.getSelectedItem();
                        Listar_Muestras(userLote.getId());
                        DisplayUserData(userLote);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onFailure(Call<List<UserLote>> call, Throwable t) {

            }
        });
    }
    public String Formato_Decimal(String num){
        String formato          = "";
        int Numero_De_Decimales = 6;
        int x                   = 0;

        for (int i=0; i<num.length(); i++){
            if (num.charAt(i) == '.')
                x = 1;
            formato = formato + num.charAt(i);
            if (x == 1)
                Numero_De_Decimales--;
            if (Numero_De_Decimales == 0)
                break;
        }
        return formato;
    }
    private void DisplayUserData(UserLote userLote){
        Nombre.setText("");
        Nombre.setText(userLote.getNombre());
        Informacion_De_Region();
    }
    private void Informacion_De_Region(){
        Call<Respuesta_Municipio> call = ClienteRetrofit.getInstance()
                .getApi().Obtener_Region(A_Iniciar_Sesion.Datos_De_Usuario.getMunicipio_id());
        call.enqueue(new Callback<Respuesta_Municipio>() {
            @Override
            public void onResponse(Call<Respuesta_Municipio> call, Response<Respuesta_Municipio> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(
                            D_Generar_Muestra.this,
                            "" + response.code(),
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                Region = response.body();
                Pais.setText("");
                Pais.setText(Region.getPais().getNombre());
                Estado.setText("");
                Estado.setText(Region.getMunicipioestado().getNombre());
                Municipio.setText("");
                Municipio.setText(Region.getMunicipio().getNombre());
            }

            @Override
            public void onFailure(Call<Respuesta_Municipio> call, Throwable t) {
                Toast.makeText(
                        D_Generar_Muestra.this,
                        "Error al cargar la información de region",
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
    public void Cambiar_Tema(){
        if (Obtener_Modo_Visualizacion()) getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
    public boolean Obtener_Modo_Visualizacion(){
        SharedPreferences preferences = getSharedPreferences(TEMA_PREFRERENTE, MODE_PRIVATE);
        return preferences.getBoolean("Modo_Oscuro", false);
    }
    public void Abrir_Ventana_Resultados(){
        Intent resultados = new Intent(this, E_Mostrar_Resultados.class);
        startActivity(resultados);
    }
}