package com.example.david.proyectowms;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class PlacaCalificacion extends Activity {

    float ratingMotor = 0;
    float ratingExt = 0;
    float ratingInt = 0;
    float ratingBaul = 0;
    float ratingChasisPn = 0;
    String codAtributo = "";
    String email = "";
    String costo = "";
    String fpago = "";
    String marca = "";
    String notas = "";
    String descPrecio = "";
    String precio = "";
    String tipoVehiculo = "";
    int indice =0;
    private final String ruta_fotos = "/storage/sdcard0/METRO/" ;
    private File file = new File(ruta_fotos);
    private Button boton;
    static String conexion = "";
    String tecnico = "";
    String encuentraPlaca = "";
    String encuentraNit = "";
    String nombreTabla = "";
    String ip = "" ;
    String placa = "";
    String vin = "";
    String Cedula = "";
    String nombre1 = "";
    String nombre2 = "";
    String apellido1 = "";
    String apellido2 = "";
    String nombre = "";
    String cod_placa = "";
    String num_rombo= "";
    String cod_ubicacion = "";
    String fec_proceso = "";
    String num_nit = "";
    String promesa = "";
    String solicitud = "";
    String sucursal;
    int request_code = 1;
    ArrayList atributos ;
    String titulo = "";
    String empresa;
    EditText texto;
    String [][] generalAux;
    String [][] general;
    String proceso = "";
    int date ;
    String fechaFoto;
    String dia ;
    String dirFoto;
    int longitud;
    String tiempoPromesa;
    String kilometraje = "";
    String sInformativo = "";
    String parametro = "";
    String pass;
    String user;
    String km = "";
    ProgressDialog mProgressDialog;
    String km_anterior = "";
    String razon = "";
    String descripcion = "";
    String soat = "";
    String tecno = "";
    String modelo = "";
    String cita = "";
    String horacita = "";
    String descripcionModelo = "";
    String motivo;
    String eventoCita = "";
    String eventoRazon = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placa_calificacion);

        Bundle bundle = this.getIntent().getExtras();
        codAtributo = bundle.getString("codAtributo");
        user = bundle.getString("user");
        pass = bundle.getString("pass");
        conexion = bundle.getString("conexion");
        titulo = bundle.getString("titulo");
        ip = bundle.getString("ip");
        proceso = bundle.getString("proceso");

        num_rombo = bundle.getString("num_rombo");

        nombreTabla = bundle.getString("nombreTabla");
        sucursal = bundle.getString("sucursal");
        empresa = bundle.getString("empresa");


    }

    public void continuar(View v){
        EditText editPlaca = (EditText) findViewById(R.id.editPlaca);

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        String url3 = "http://" + ip + "/consultarGeneralLavadero.php";

        List<NameValuePair> params3 = new ArrayList<NameValuePair>();
        params3.add(new BasicNameValuePair("sPlaca", editPlaca.getText().toString()));
        params3.add(new BasicNameValuePair("sAno", String.valueOf(calendar.get(Calendar.YEAR))));
        params3.add(new BasicNameValuePair("sMes", String.valueOf(calendar.get(Calendar.MONTH)+1)));
        params3.add(new BasicNameValuePair("sDia", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))));
        params3.add(new BasicNameValuePair("sParametro", "placaCalificacion"));
        String resultServer3 = getHttpPost(url3, params3);
        System.out.println("---------------------------------resultserver----------------");
        System.out.println(resultServer3);
        String placa = "";

        try {

            JSONArray jArray2 = new JSONArray(resultServer3);
            ArrayList<String> array2 = new ArrayList<String>();
            for (int j = 0; j < jArray2.length(); j++) {
                JSONObject json2 = jArray2.getJSONObject(j);
                array2.add(json2.getString("cod_placa"));
                placa = array2.get(0).trim();
                //System.out.println("NOMBRE PROCESOS - "+ nomProcesos.get(i));
            }
        } catch (JSONException e) {

            e.printStackTrace();
        }


        if (placa.length()>2) {

            Intent intent = new Intent((Context) PlacaCalificacion.this, (Class) Calificacion.class);
            Bundle bundle2 = new Bundle();
            bundle2.putString("nombreTabla", "");
            bundle2.putString("codAtributo", "");
            bundle2.putString("user", user);
            bundle2.putString("pass", pass);
            bundle2.putString("sucursal", sucursal);
            bundle2.putString("empresa", empresa);
            bundle2.putString("conexion", conexion);
            bundle2.putString("sucursal", sucursal);
            bundle2.putString("placa", placa);
            bundle2.putString("ip", ip);
            bundle2.putString("titulo", titulo + "/Rec");
            bundle2.putString("proceso", "4");
            intent.putExtras(bundle2);
            startActivity(intent);
        }else{
            Toast.makeText(PlacaCalificacion.this, "Digite una placa valida!", Toast.LENGTH_SHORT).show();
        }
    }

    public String getHttpPost(String url,List<NameValuePair> params) {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) { // Status OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("Log", "Failed to download result..");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }
}
