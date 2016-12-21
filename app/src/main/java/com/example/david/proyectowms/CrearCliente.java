package com.example.david.proyectowms;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CrearCliente extends Activity {

    String ip = "";
    String Nit = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cliente);
        Bundle bundle = this.getIntent().getExtras();
        ip = bundle.getString("ip");
        Nit = bundle.getString("nit");
        EditText nit = (EditText) findViewById(R.id.editNit);
        nit.setText(Nit.trim());

        EditText nombre = (EditText) findViewById(R.id.editNombre);

        nombre.requestFocus();
    }


    public void guardar(View v ){

        EditText nit = (EditText) findViewById(R.id.editNit);
        EditText nombre = (EditText) findViewById(R.id.editNombre);
        EditText tel = (EditText) findViewById(R.id.editTel);
        EditText email = (EditText) findViewById(R.id.editEmail);
        EditText sector = (EditText) findViewById(R.id.editSector);

        String url = "http://" + ip + "/guardarMovimientoLavadero.php";


        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sNit", nit.getText().toString()));
        params.add(new BasicNameValuePair("sNombre", nombre.getText().toString()));
        params.add(new BasicNameValuePair("sTel", tel.getText().toString()));
        params.add(new BasicNameValuePair("sEmail", email.getText().toString()));
        params.add(new BasicNameValuePair("sSector", sector.getText().toString()));
        params.add(new BasicNameValuePair("sParametro", "nit"));

        JSONObject c3;

        String resultServer = getHttpPost(url, params);
        System.out.println(resultServer);

        Toast.makeText(CrearCliente.this, "Se guardo correctamente!", Toast.LENGTH_SHORT).show();
        Intent data = new Intent();
        data.putExtra("parametro", "clienteNuevo");
        data.putExtra("valor", nit.getText().toString());
        setResult(RESULT_OK, data);
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
