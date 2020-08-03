package com.analisis.appesi.Actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.analisis.appesi.API.ClienteRetrofit;
import com.analisis.appesi.Modelo.Caractere;
import com.analisis.appesi.Modelo.Carga_De_Datos_IA;
import com.analisis.appesi.Modelo.Cromann;
import com.analisis.appesi.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class E_Mostrar_Resultados extends AppCompatActivity {
    public static final String TEMA_PREFRERENTE = "MODE.tema";

    ImageView Croma_Img;
    TextView Oxigeno, Mat_Organica, Minerales, Nitrogeno, Rompimiento, Mat_Viva,
    Act_Biologica, Nitro_Organico;
    PieChart GRAFICA;
    PieChartView G;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e__mostrar__resultados);
        getSupportActionBar().setTitle("Resultados del Análisis");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Croma_Img      = findViewById(R.id.Croma_Img);
        Oxigeno        = findViewById(R.id.Oxigeno);
        Mat_Organica   = findViewById(R.id.Mat_Organica);
        Minerales      = findViewById(R.id.Minerales);
        Nitrogeno      = findViewById(R.id.Nitrogeno);
        Rompimiento    = findViewById(R.id.Rompimiento);
        Mat_Viva       = findViewById(R.id.Mat_Viva);
        Act_Biologica  = findViewById(R.id.Act_Biologica);
        Nitro_Organico = findViewById(R.id.Nitro_Organico);
        GRAFICA        = findViewById(R.id.GRAFICA);
        G              = findViewById(R.id.chart);

        Cambiar_Tema();
        Croma_Img.setImageBitmap(D_Generar_Muestra.bitmap);
        Subir_Imagen_Al_Servidor();
        Caracteristicas_Presentes();

        Grafica();
        //g2();
    }
    public void g2(){
        List<SliceValue> pieData = new ArrayList<>();
        pieData.add(new SliceValue(50, Color.parseColor("#121212")).setLabel("valor 1"));
        pieData.add(new SliceValue(50, Color.parseColor("#00F260")).setLabel("Valor 2"));

        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true);
        pieChartData.setHasCenterCircle(true).setCenterText1("Croma").setCenterText1FontSize(15).setCenterText1Color
                (getResources().getColor(R.color.TextoTema));
        G.setPieChartData(pieChartData);
        G.animate();
    }
    public void Grafica(){
        int [] x = Colores();
        ArrayList<PieEntry> visitors = new ArrayList<>();
        visitors = Informacion_Grafica();

        PieDataSet pieDataSet = new PieDataSet(visitors, "Visitors");
        pieDataSet.setColors(x);
        pieDataSet.setValueTextColors(Collections.singletonList(getResources().getColor(R.color.TextoTema)));
        pieDataSet.setValueTextSize(16);

        PieData pieData = new PieData(pieDataSet);

        GRAFICA.setData(pieData);
        GRAFICA.getDescription().setEnabled(false);
        GRAFICA.setCenterText("Cromas");
        GRAFICA.animate();

    }
    public ArrayList<PieEntry> Informacion_Grafica(){
        ArrayList<PieEntry> visitors = new ArrayList<>();
        int tamaño = D_Generar_Muestra.respuestaAI.coloresCodigos.size();

        for (int i=0; i<tamaño; i++){
            Double x = D_Generar_Muestra.respuestaAI.porcentajes.get(i)
                    .getCantidad();
            visitors.add(
                    new PieEntry(
                            Math.round(x),
                            D_Generar_Muestra.respuestaAI.coloresCodigos.get(i)
                            .getCodigo()
                    )
            );
        }
        return visitors;
    }
    public int[] Colores(){
        int tamaño = D_Generar_Muestra.respuestaAI.coloresCodigos.size();
        int[] colores = new int[tamaño];
        String codigo = "";

        for (int i=0; i<tamaño; i++){
            codigo = D_Generar_Muestra.respuestaAI.coloresCodigos
                    .get(i).getCodigo();
            colores[i] = Color.parseColor(codigo);
        }

        return colores;
    }
    public void Caracteristicas_Presentes(){
        List<Caractere> caracteres = D_Generar_Muestra.respuestaAI.getCaracteres();

        if (caracteres.get(0).getValor() == 1){
            Oxigeno.setBackgroundResource(R.drawable.a_fondo_presencia);
            D_Generar_Muestra.LM.getCromann().get(0).setIndOxg(true);
        }
        if (caracteres.get(1).getValor() == 1){
            Mat_Organica.setBackgroundResource(R.drawable.a_fondo_presencia);
            D_Generar_Muestra.LM.getCromann().get(0).setIndMatOrg(true);
        }
        if (caracteres.get(2).getValor() == 1){
            Minerales.setBackgroundResource(R.drawable.a_fondo_presencia);
            D_Generar_Muestra.LM.getCromann().get(0).setIndTransSist(true);
        }
        if (caracteres.get(3).getValor() == 1){
            Nitrogeno.setBackgroundResource(R.drawable.a_fondo_presencia);
            D_Generar_Muestra.LM.getCromann().get(0).setIndNElem(true);
        }
        if (caracteres.get(4).getValor() == 1){
            Rompimiento.setBackgroundResource(R.drawable.a_fondo_presencia);
            D_Generar_Muestra.LM.getCromann().get(0).setIndRomp(true);
        }
        if (caracteres.get(5).getValor() == 1){
            Mat_Viva.setBackgroundResource(R.drawable.a_fondo_presencia);
            D_Generar_Muestra.LM.getCromann().get(0).setIndMatViva(true);
        }
        if (caracteres.get(6).getValor() == 1){
            Act_Biologica.setBackgroundResource(R.drawable.a_fondo_presencia);
            D_Generar_Muestra.LM.getCromann().get(0).setIndBio(true);
        }
        if (caracteres.get(7).getValor() == 1){
            Nitro_Organico.setBackgroundResource(R.drawable.a_fondo_presencia);
            D_Generar_Muestra.LM.getCromann().get(0).setIndProN(true);
        }

        Actualizar_Muestra(
                D_Generar_Muestra.LM.getCromann().get(0).getId(),
                D_Generar_Muestra.LM.getCromann().get(0)
        );
    }
    public void Actualizar_Muestra(int id, Cromann cromann){
        Call<Carga_De_Datos_IA> call = ClienteRetrofit.getInstance().getApi()
                .ia(id, cromann);
        call.enqueue(new Callback<Carga_De_Datos_IA>() {
            @Override
            public void onResponse(Call<Carga_De_Datos_IA> call, Response<Carga_De_Datos_IA> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(
                            E_Mostrar_Resultados.this,
                            "Error al actualizar Muestra",
                            Toast.LENGTH_LONG
                    ).show();
                    return;
                }
                Toast.makeText(
                        E_Mostrar_Resultados.this,
                        "Se actualizo la muestra Corectamente",
                        Toast.LENGTH_LONG
                ).show();
            }

            @Override
            public void onFailure(Call<Carga_De_Datos_IA> call, Throwable t) {

            }
        });
    }
    public void Subir_Imagen_Al_Servidor(){
        RequestBody filep = RequestBody.create(
                MediaType.parse(getContentResolver()
                        .getType(D_Generar_Muestra.photoURI)),
                D_Generar_Muestra.photoFile
        );
        MultipartBody.Part file = MultipartBody.Part.createFormData(
                "img",
                D_Generar_Muestra.photoFile.getName(),
                filep
        );
        Call<ResponseBody> call = ClienteRetrofit.getInstance3().getApi()
                .Subir_Img(
                        file,
                        D_Generar_Muestra.LM.getCromann().get(0).getId() + ""
                );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(
                            E_Mostrar_Resultados.this,
                            "La Imagen tomada no se pudo cargar al servidor",
                            Toast.LENGTH_LONG
                    ).show();
                    return;
                }
                System.out.println("RESPUESTA " + response.body().toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
        /*Call<ResponseBody> call = ClienteRetrofit.getInstance().getApi()
                .cargar_Imagen(
                        D_Generar_Muestra.LM.getCromann().get(0).getId(),
                        file
                );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(
                            E_Mostrar_Resultados.this,
                            "La Imagen tomada no se pudo cargar al servidor",
                            Toast.LENGTH_LONG
                    ).show();
                    return;
                }
                Toast.makeText(
                        E_Mostrar_Resultados.this,
                        "Imagen cargada al servidor exitosamente",
                        Toast.LENGTH_LONG
                ).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });*/
    }
    public void Cambiar_Tema(){
        if (Obtener_Modo_Visualizacion()) getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
    public boolean Obtener_Modo_Visualizacion(){
        SharedPreferences preferences = getSharedPreferences(TEMA_PREFRERENTE, MODE_PRIVATE);
        return preferences.getBoolean("Modo_Oscuro", false);
    }
}