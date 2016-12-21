package com.example.david.proyectowms;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CambiarSede extends AppCompatActivity {

    String conexion = "";
    String user = "";
    String pass = "";
    String ip = "";
    String sucursal = "";
    String titulo = "";
    String empresa = "";
    EditText codigo;
    String sede;
    String nombres;
    String bodega;
    String nit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_sede);

        final Bundle bundle = this.getIntent().getExtras();
        user = bundle.getString("user");
        empresa = bundle.getString("empresa");
        pass = bundle.getString("pass");
        conexion = bundle.getString("conexion");
        ip = bundle.getString("ip");
        sucursal = bundle.getString("sucursal");
        titulo = bundle.getString("titulo");
        setTitle(titulo);

        codigo = (EditText) findViewById(R.id.editText);

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


    }

    public void ver(View v){

        try {

            String url = "http://" + ip + "/consultarGeneral2.php";

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("sUser", codigo.getText().toString().trim()));
            params.add(new BasicNameValuePair("sParametro", "sede"));
            String resultServer = getHttpPost(url, params);
            System.out.println("---------------------------------resultserver----------------");
            try {

                JSONArray jArray = new JSONArray(resultServer);
                ArrayList<String> array = new ArrayList<String>();
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json = jArray.getJSONObject(i);
                    array.add(json.getString("nombres"));
                    array.add(json.getString("bodega"));
                    array.add(json.getString("nit"));
                }

                nombres = array.get(0).trim();
                sede = array.get(1).trim();
                nit = array.get(2).trim();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (sede.trim().equals("1")) {
                sede = "51";
            } else {
                if (sede.trim().equals("2")) {
                    sede = "33";
                } else {
                    if (sede.trim().equals("3")) {
                        sede = "10";
                    } else {
                        if (sede.trim().equals("11")) {
                            sede = "99";
                        }
                    }
                }
            }


            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Cambiar sede");
            dialogo1.setMessage("De : " + sede + "\n\nA : " + sucursal + "\n" +
                    "\n" + nombres + "\n\n" + "Codigo : " + codigo.getText().toString());
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {

                    String url7 = "http://" + ip + "/guardarMovimiento.php";

                    List<NameValuePair> params7 = new ArrayList<NameValuePair>();
                    params7.add(new BasicNameValuePair("sBodega", bodega));
                    params7.add(new BasicNameValuePair("sNit", nit));
                    params7.add(new BasicNameValuePair("sParametro", "cambiarSede"));

                    String resultServer7 = getHttpPost(url7, params7);
                    System.out.println(resultServer7);

                    Toast.makeText(CambiarSede.this, "Se ha actualizado la bodega!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
            dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                }
            });
            dialogo1.show();

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(CambiarSede.this, "Verifique el codigo !", Toast.LENGTH_SHORT).show();
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
