package com.example.david.proyectowms;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Evento extends Activity {

    String user;
    String pass;
    String ip;
    String sucursal;
    String empresa;
    String num_rombo;
    String cod_placa;
    String nit;
    String bodega;
    String entrada;
    String url;
    String placa;
    String codEvento;
    String proceso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);

        user = getIntent().getExtras().getString("user");
        pass = getIntent().getExtras().getString("pass");
        ip = getIntent().getExtras().getString("ip");
        sucursal = getIntent().getExtras().getString("sucursal");
        empresa = getIntent().getExtras().getString("empresa");
        num_rombo = getIntent().getExtras().getString("num_rombo");

        final GridView gridview = (GridView) findViewById(R.id.gridView);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        url = "http://" + ip + "/consultarRecurso.php";
        params.add(new BasicNameValuePair("sParametro", "evento"));
        params.add(new BasicNameValuePair("sCodEmpresa", empresa.trim()));
        params.add(new BasicNameValuePair("sCodSucursal", sucursal.trim()));
        String resultServer = getHttpPost(url, params);
        System.out.println("---------------------------------");
        System.out.println(resultServer);

        String url3 = "http://" + ip + "/consultarGeneral.php";

        List<NameValuePair> params3 = new ArrayList<NameValuePair>();
        params3.add(new BasicNameValuePair("sNumRombo", num_rombo));
        params3.add(new BasicNameValuePair("sCodSucursal", sucursal));
        params3.add(new BasicNameValuePair("sParametro", "cambiarPromesaTraDetalle"));
        String resultServer3 = getHttpPost(url3, params3);
        System.out.println(resultServer3);

        try {

            JSONArray jArray2 = new JSONArray(resultServer3);
            ArrayList<String> array2 = new ArrayList<String>();
            for (int j = 0; j < jArray2.length(); j++) {
                JSONObject json2 = jArray2.getJSONObject(j);
                cod_placa = json2.getString("cod_placa");
                nit = json2.getString("nit");
                proceso = json2.getString("proceso");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            JSONArray jArray = new JSONArray(resultServer);
            ArrayList<String> array = new ArrayList<String>();
            //Toast.makeText(getApplicationContext()," proceso " + array.get(0),Toast.LENGTH_LONG).show();
            int j = 0;
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                array.add((json.getString("cod_evento")));
                array.add((json.getString("nom_evento")));
            }

            final String[] cod_eventos = new String[array.size() / 2];
            final String[] nom_eventos = new String[array.size() / 2];
            int k = 0;
            for (int i = 0; i < array.size(); i = i + 2) {
                cod_eventos[k] = array.get(i);
                System.out.println(cod_eventos[k]);
                k = k + 1;
            }
            k = 0;
            for (int i = 0; i < array.size() / 2; i++) {
                nom_eventos[i] = array.get(k + 1);
                k = k + 2;
            }


            gridview.setAdapter(new CustomGridViewAdapter(getApplicationContext(), nom_eventos, "grid"));// con setAdapter se llena


            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    TextView nombreCodigo = (TextView) findViewById(R.id.textNombreCodigo);
                    nombreCodigo.setText(nom_eventos[position].trim());
                    codEvento = cod_eventos[position].trim();

                }
            });
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void guardar(View v){
        try {

            String url7 = "http://" + ip + "/guardarMovimiento.php";

            List<NameValuePair> params7 = new ArrayList<NameValuePair>();
            params7.add(new BasicNameValuePair("sPlaca", cod_placa.trim()));
            params7.add(new BasicNameValuePair("sNumRombo", num_rombo));
            params7.add(new BasicNameValuePair("sNit", nit.trim()));
            params7.add(new BasicNameValuePair("sCodEvento", codEvento));
            params7.add(new BasicNameValuePair("sCodProceso",proceso ));
            params7.add(new BasicNameValuePair("sParametro", "guardarEvento"));

            String resultServer7 = getHttpPost(url7, params7);
            System.out.println(resultServer7);

            Toast.makeText(Evento.this, "Haz guardado el evento !", Toast.LENGTH_SHORT).show();
            finish();
        }catch (Exception e){
            Toast.makeText(Evento.this, "No se pudo guardar !", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void cancelar(View v){
        finish();
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
