package com.example.david.proyectowms;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import android.content.Intent;
import android.net.Uri;

public class Cotizacion extends Activity {

    String user;
    String pass;
    String sucursal;
    String empresa;
    String ip;
    String num_rombo;
    String bodega;
    String placa;
    String nit;
    String contacto2;
    String nombres;

    EditText editRombo;
    EditText editNombre;
    EditText editPlaca;
    TextView textContacto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cotizacion);

        user = getIntent().getExtras().getString("user");
        pass = getIntent().getExtras().getString("pass");
        sucursal = getIntent().getExtras().getString("sucursal");
        empresa = getIntent().getExtras().getString("empresa");
        ip = getIntent().getExtras().getString("ip");
        num_rombo = getIntent().getExtras().getString("num_rombo");

        if (sucursal.trim().equals("51")){
            bodega = "1";
        }else{
            if (sucursal.trim().equals("33")){
                bodega = "2";
            }else{
                if (sucursal.trim().equals("10")){
                    bodega = "3";
                }else{
                    if (sucursal.trim().equals("99")){
                        bodega = "11";
                    }
                }
            }
        }

        String url = "http://" + ip + "/consultarGeneralLavadero.php";

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sAno", String.valueOf(calendar.get(Calendar.YEAR))));
        params.add(new BasicNameValuePair("sMes", String.valueOf(calendar.get(Calendar.MONTH)+1)));
        params.add(new BasicNameValuePair("sDia", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))));
        params.add(new BasicNameValuePair("sBodega", bodega));
        params.add(new BasicNameValuePair("sRombo", num_rombo));
        params.add(new BasicNameValuePair("sParametro", "llamada"));


        String resultServer = getHttpPost(url, params);
        System.out.println(resultServer);
        try {

            JSONArray jArray = new JSONArray(resultServer);
            ArrayList<String> array = new ArrayList<String>();
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                array.add(json.getString("cod_placa"));
                array.add(json.getString("num_nit"));
            }
            placa = array.get(0);
            nit = array.get(1);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        String url2 = "http://" + ip + "/consultarGeneralLavadero.php";

        List<NameValuePair> params2 = new ArrayList<NameValuePair>();
        params2.add(new BasicNameValuePair("sNit", nit));
        params2.add(new BasicNameValuePair("sParametro", "nitLlamada"));


        String resultServer2 = getHttpPost(url2, params2);
        try {

            JSONArray jArray = new JSONArray(resultServer2);
            ArrayList<String> array = new ArrayList<String>();
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                array.add(json.getString("contacto_2"));
                array.add(json.getString("nombres"));
            }

            contacto2 = array.get(0);
            nombres = array.get(1);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        FindViewById();

        editRombo.setText(num_rombo.trim());
        editNombre.setText(nombres.trim());
        editPlaca.setText(placa.trim());
        textContacto.setText(contacto2.trim());
    }

    public void FindViewById(){
        editRombo = (EditText) findViewById(R.id.rombo);
        editPlaca = (EditText) findViewById(R.id.placa);
        editNombre = (EditText) findViewById(R.id.nombre);
        textContacto = (TextView) findViewById(R.id.telefono);
    }

    public void llamar(View v){
        try {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + textContacto.getText().toString().trim()));
            startActivity(intent);

            String url7 = "http://" + ip + "/guardarMovimiento.php";

            List<NameValuePair> params7 = new ArrayList<NameValuePair>();
            params7.add(new BasicNameValuePair("sPlaca", placa));
            params7.add(new BasicNameValuePair("sNumRombo", num_rombo));
            params7.add(new BasicNameValuePair("sParametro", "guardarLlamada"));

            String resultServer7 = getHttpPost(url7, params7);
            System.out.println(resultServer7);
        }catch (Exception e){
            Toast.makeText(Cotizacion.this, "No se pudo llamar, verifique el telefono o la señal de su telefono!", Toast.LENGTH_SHORT).show();
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
