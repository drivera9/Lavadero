package com.example.david.proyectowms;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CambiarPromesa extends Activity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_promesa);

        user = getIntent().getExtras().getString("user");
        pass = getIntent().getExtras().getString("pass");
        ip = getIntent().getExtras().getString("ip");
        sucursal = getIntent().getExtras().getString("sucursal");
        empresa = getIntent().getExtras().getString("empresa");
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
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url4 = "http://" + ip + "/consultarGeneral.php";

        List<NameValuePair> params4 = new ArrayList<NameValuePair>();
        params4.add(new BasicNameValuePair("sParametro", "consultarFecha"));
        params4.add(new BasicNameValuePair("sSucursal", sucursal.trim()));
        params4.add(new BasicNameValuePair("sNumRombo", num_rombo.trim()));
        params4.add(new BasicNameValuePair("sCodPlaca", cod_placa.trim()));
        params4.add(new BasicNameValuePair("sBodega", bodega));
        params4.add(new BasicNameValuePair("sNit", nit.trim()));
        String resultServer4 = getHttpPost(url4, params4);
        System.out.println(resultServer4);

        try {

            JSONArray jArray4 = new JSONArray(resultServer4);
            for (int j = 0; j < jArray4.length(); j++) {
                JSONObject json4 = jArray4.getJSONObject(j);
                entrada = json4.getString("entrada");
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }


        final EditText promesa = (EditText) findViewById(R.id.editPromesa);

        promesa.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                TextView fecha = (TextView) findViewById(R.id.textFecha);

                if (!promesa.getText().toString().equals("")) {

                    String dateInString = entrada;  // Start date
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H:m:s");
                    Calendar c = Calendar.getInstance();
                    try {
                        c.setTime(sdf.parse(dateInString));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    c.add(Calendar.HOUR, Integer.parseInt(promesa.getText().toString()));
                    sdf = new SimpleDateFormat("yyyy-MM-dd H:m:s");
                    Date resultdate = new Date(c.getTimeInMillis());
                    dateInString = sdf.format(resultdate);
                    fecha.setText(dateInString);
                }else{
                    fecha.setText("");
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    public void guardar(View v){

        EditText promesa = (EditText) findViewById(R.id.editPromesa);


        try {

            String url = "http://" + ip + "/guardarFecha.php";

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("sSucursal", sucursal.trim()));
            params.add(new BasicNameValuePair("sNumRombo", num_rombo.trim()));
            params.add(new BasicNameValuePair("sCodPlaca", cod_placa.trim()));
            params.add(new BasicNameValuePair("sBodega", bodega));
            params.add(new BasicNameValuePair("sNit", nit.trim()));
            params.add(new BasicNameValuePair("sEntrada", entrada));
            params.add(new BasicNameValuePair("sPromesa", promesa.getText().toString().trim()));

            String resultServer = getHttpPost(url, params);
            System.out.println(resultServer);

            Toast.makeText(CambiarPromesa.this, "Haz actualizado la promesa!", Toast.LENGTH_SHORT).show();
            finish();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(CambiarPromesa.this, "Ha odurrido un error , intentalo de nuevo!", Toast.LENGTH_SHORT).show();
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
