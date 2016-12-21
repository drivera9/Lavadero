package com.example.david.proyectowms;

import android.app.Activity;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

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

public class MostrarLista extends Activity {
    String cod_empresa = "";
    String cod_sucursal = "";
    String[] procesos;
    String tipoVehiculo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_lista);

        Bundle bundle = this.getIntent().getExtras();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        TextView titulo = (TextView) findViewById(R.id.titulo);
        String Titulo = "";

        final GridView gridview = (GridView) findViewById(R.id.gridView4);

        final String conexion = bundle.getString("conexion");
        final String ip = bundle.getString("ip");
        final String atributo = bundle.getString("atributo");
        cod_empresa = bundle.getString("empresa");
        cod_sucursal = bundle.getString("sucursal");
        tipoVehiculo = bundle.getString("tipoVehiculo");

        if (tipoVehiculo.trim().equals("A")){
            Titulo = "Automovil";
        }else{
            if (tipoVehiculo.trim().equals("B")){
                Titulo = "Bus";
            }else{
                if (tipoVehiculo.trim().equals("M")){
                    Titulo = "Moto";
                }else{
                    if (tipoVehiculo.trim().equals("K")){
                        Titulo = "Camion";
                    }else{
                        if (tipoVehiculo.trim().equals("C")){
                            Titulo = "Camioneta";
                        }
                    }
                }
            }
        }

        titulo.setText(Titulo.trim());

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String url = "http://" + ip + "/consultarRecursoLavadero.php";
        params.add(new BasicNameValuePair("sParametro", "LISTA"));
        params.add(new BasicNameValuePair("sTipoVehiculo", tipoVehiculo));
        String resultServer = getHttpPost(url, params);
        System.out.println("---------------------------------");
        System.out.println(resultServer);


        try {
            JSONArray jArray = new JSONArray(resultServer);
            ArrayList<String> array = new ArrayList<String>();
            //Toast.makeText(getApplicationContext()," proceso " + array.get(0),Toast.LENGTH_LONG).show();
            int j = 0;

            ArrayList valores = new ArrayList();
            ArrayList desc = new ArrayList();
            final ArrayList costos = new ArrayList();
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                desc.add(json.getString("cod_descripcion").trim());
                valores.add(json.getString("val_valor1").trim());
                costos.add(json.getString("val_costo").trim());
            }

            final String[] arrayValores = new String[valores.size()];
            final String[] arrayDesc = new String[desc.size()];

            procesos = new String[valores.size()+desc.size()];



            int k = 0;
            for (int i = 0;i<desc.size();i++){
                arrayDesc[i] = desc.get(i).toString();
                procesos[k] = desc.get(i).toString();
                arrayValores[i] = valores.get(i).toString();
                procesos[k+1] = valores.get(i).toString();
                k = k+2;
            }



            gridview.setAdapter(new CustomGridViewAdapter(getApplicationContext(),procesos, "grid"));// con setAdapter se llena


            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    if (position%2==1) {

                        Intent data = new Intent();
                        data.putExtra("parametro", "lista");
                        data.putExtra("valor", procesos[position].trim());
                        data.putExtra("valor2", procesos[position-1].trim());
                        double d = (position/2)+0.5;
                        data.putExtra("valor3", costos.get((int) d).toString());
                        setResult(RESULT_OK, data);
                        finish();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
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
