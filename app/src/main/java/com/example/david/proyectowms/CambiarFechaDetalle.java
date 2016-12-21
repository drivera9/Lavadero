package com.example.david.proyectowms;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CambiarFechaDetalle extends Activity {

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
    String codEvento;
    String proceso;
    String Fecha;
    String Placa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_fecha_detalle);

        user = getIntent().getExtras().getString("user");
        pass = getIntent().getExtras().getString("pass");
        ip = getIntent().getExtras().getString("ip");
        sucursal = getIntent().getExtras().getString("sucursal");
        empresa = getIntent().getExtras().getString("empresa");
        num_rombo = getIntent().getExtras().getString("num_rombo");
        Fecha = getIntent().getExtras().getString("fecha");
        Placa = getIntent().getExtras().getString("placa");

        TextView rombo = (TextView) findViewById(R.id.rombo);

        TextView fecha = (TextView) findViewById(R.id.fecha);

        TextView placa = (TextView) findViewById(R.id.placa);

        fecha.setText(Fecha);

        placa.setText(Placa);

        rombo.setText(num_rombo );
    }

    public void cambiarFecha(View v){


        try {

            String url2 = "http://" + ip + "/guardarMovimiento.php";

            List<NameValuePair> params2 = new ArrayList<NameValuePair>();

            params2.add(new BasicNameValuePair("sNumRombo",num_rombo));
            params2.add(new BasicNameValuePair("sCodSucursal", sucursal));
            params2.add(new BasicNameValuePair("sCodEmpresa", empresa));
            params2.add(new BasicNameValuePair("sCodPlaca", Placa));
            params2.add(new BasicNameValuePair("sParametro", "cambiarFecha"));

            String resultServer2 = getHttpPost(url2, params2);
            System.out.println(resultServer2);

            Toast.makeText(CambiarFechaDetalle.this, "Haz actualizado la fecha!", Toast.LENGTH_SHORT).show();
            finish();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(CambiarFechaDetalle.this, "Ha odurrido un error , intentalo de nuevo!", Toast.LENGTH_SHORT).show();
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
