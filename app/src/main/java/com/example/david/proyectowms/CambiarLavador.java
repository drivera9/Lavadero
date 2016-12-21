package com.example.david.proyectowms;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class CambiarLavador extends Activity {

    String conexion = "";
    String user = "";
    String pass = "";
    String ip = "";
    String sucursal = "";
    String titulo = "";
    String proceso = "";
    String tipo = "";
    String procesOriginal = "";
    String empresa;
    String Sucursal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_lavador);

        final Bundle bundle = this.getIntent().getExtras();
        user = bundle.getString("user");
        empresa = bundle.getString("empresa");
        Sucursal = bundle.getString("sucursal");
        procesOriginal = bundle.getString("procesOriginal");
        pass = bundle.getString("pass");
        proceso = bundle.getString("proceso");
        System.out.println("PROCESOOOoOoOoOoo" + proceso);
        conexion = bundle.getString("conexion");
        ip = bundle.getString("ip");
        tipo = bundle.getString("tipo");
        sucursal = bundle.getString("sucursal");

    }

    public void cambiar(View v){

        final EditText placa = (EditText) findViewById(R.id.editPlaca);

        final EditText rombo = (EditText) findViewById(R.id.editRombo);

        final TextView lavador = (TextView) findViewById(R.id.textLavador);

        String nombres = "";

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        String url = "http://" + ip + "/consultarGeneralLavadero.php";


        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sAno", String.valueOf(calendar.get(Calendar.YEAR))));
        params.add(new BasicNameValuePair("sMes", String.valueOf(calendar.get(Calendar.MONTH)+1)));
        params.add(new BasicNameValuePair("sDia", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))));
        params.add(new BasicNameValuePair("sCodPlaca", placa.getText().toString().trim()));
        params.add(new BasicNameValuePair("sNumRombo", rombo.getText().toString().trim()));
        params.add(new BasicNameValuePair("sParametro", "consultarCambioLavador"));



        String resultServer = getHttpPost(url, params);
        System.out.println("---------------------------------resultserver----------------");
        try {

            JSONArray jArray = new JSONArray(resultServer);
            ArrayList<String> array = new ArrayList<String>();
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                array.add(json.getString("cod_tecnico"));
                nombres = array.get(0);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (nombres.trim().equals("LAVA")) {

            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("CAMBIAR LAVADOR");
            dialogo1.setMessage("Placa : " + placa.getText().toString().trim() + "\n\n" +
                    "Rombo : " + rombo.getText().toString().trim() + "\n\n" + "Anterior lavador : " + nombres + "\n\n" +
                    "Nuevo lavador : " + lavador.getText().toString() + "\n\n");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {

                    String url = "http://" + ip + "/guardarMovimientoLavadero.php";


                    Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("sAno", String.valueOf(calendar.get(Calendar.YEAR))));
                    params.add(new BasicNameValuePair("sMes", String.valueOf(calendar.get(Calendar.MONTH)+1)));
                    params.add(new BasicNameValuePair("sDia", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))));
                    params.add(new BasicNameValuePair("sCodPlaca", placa.getText().toString()));
                    params.add(new BasicNameValuePair("sNumRombo", rombo.getText().toString()));
                    params.add(new BasicNameValuePair("sCodTecnico", lavador.getText().toString()));
                    params.add(new BasicNameValuePair("sParametro", "cambiarLavador"));


                    String resultServer = getHttpPost(url, params);
                    System.out.println(resultServer);

                    Toast.makeText(CambiarLavador.this, "Haz actualizado correctamente!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
            dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                }
            });
            dialogo1.show();
        }else{
            Toast.makeText(CambiarLavador.this, "Verifique los datos!", Toast.LENGTH_SHORT).show();
        }
    }

    public void buscarLavador(View v){
        Intent intent = new Intent((Context) CambiarLavador.this, (Class) MostrarRombUbic.class);
        Bundle bundle2 = new Bundle();
        bundle2.putString("conexion", conexion);
        bundle2.putString("ip", ip);
        bundle2.putString("atributo", "LAVADOR");
        bundle2.putString("empresa", empresa);
        bundle2.putString("sucursal", sucursal);
        intent.putExtras(bundle2);
        CambiarLavador.this.startActivityForResult(intent, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        try {


            String parametro = data.getStringExtra("parametro");
            String valor = data.getStringExtra("valor");

            if (requestCode == 1) {
                if (resultCode == Activity.RESULT_OK) {

                    TextView lavador = (TextView) findViewById(R.id.textLavador);
                    lavador.setText(valor.trim());

                }
            }
        }catch (Throwable e){
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
