package com.example.david.proyectowms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

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


public class Bodega extends Activity {
String conexion = "";
    String user = "";
    String pass = "";
    String ip = "";
    String titulo = "";
    String proceso = "";
    String tipo = "";
    String procesoAnterior;
    String procesOriginal;
    String empresa;
    String Sucursal;
    ArrayList<String> bodegaSucursal = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bodega);

        final Bundle bundle = this.getIntent().getExtras();
        user = bundle.getString("user");
        pass = bundle.getString("pass");
        empresa = bundle.getString("empresa");
        Sucursal = bundle.getString("sucursal");
        System.out.println("PASS---------------" + pass);
        proceso = bundle.getString("proceso");
        System.out.println("procesoOoOoO---------------" + proceso);
        conexion = bundle.getString("conexion");
        ip = bundle.getString("ip");
        titulo = bundle.getString("titulo");
        tipo = bundle.getString("tipo");
        bodegaSucursal = bundle.getStringArrayList("bodegaSucursal");
        setTitle(titulo +    tipo);

        for (int i = 0;i<bodegaSucursal.size();i++){
            System.out.println("SUCURSAL EN SUCURSAL -" + bodegaSucursal.get(i));
        }

        procesoAnterior = bundle.getString("procesoAnterior");
        procesOriginal = bundle.getString("procesOriginal");

        System.out.println("PROCESO DESPUESSSS" + proceso);
        //String url = "http://" + ip + "/consultarSucursal.php";


            final String [] sucursal = new String [(bodegaSucursal.size()-(bodegaSucursal.size()/3))];
            int j = 0;
            int count = 0;
            for (int i = 0;i<bodegaSucursal.size();i= i+3){
                sucursal[j] = bodegaSucursal.get(i+1);
                sucursal[j+1] = bodegaSucursal.get(i+2);
                count = count + Integer.parseInt(bodegaSucursal.get(i+2));
                j=j+2;
            }





            final GridView gridview = (GridView) findViewById(R.id.gridView6);// crear el
            // gridview a partir del elemento del xml gridview

            gridview.setAdapter(new CustomGridViewAdapter(getApplicationContext(), sucursal, "grid"));// con setAdapter se llena


            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    if (position % 2 == 0) {
                        if (sucursal[position].equals("TOTAL")) {

                        } else {
                            Intent intent = new Intent((Context) Bodega.this, (Class) BodegaTraslado.class);
                            Bundle bundle2 = new Bundle();
                            bundle2.putString("sucursal", sucursal[Integer.parseInt(String.valueOf(id))]);
                            bundle2.putString("conexion", conexion);
                            bundle2.putString("ip", ip);
                            bundle2.putString("proceso", proceso);
                            bundle2.putString("user", user);
                            bundle2.putString("pass", pass);
                            bundle2.putString("tipo", tipo);
                            bundle2.putString("empresa", empresa);
                            bundle2.putString("sucursal", Sucursal);
                            bundle2.putString("procesOriginal", procesOriginal);
                            bundle2.putString("titulo", titulo + "/Suc" + sucursal[Integer.parseInt(String.valueOf(id))]);
                            intent.putExtras(bundle2);
                            startActivity(intent);
                            finish();
                        }
                    } else {


                    }


                }
            });

            String [] titulos = new String [] {"sucursal","cantidad"};

            final GridView gridviewTitulos = (GridView) findViewById(R.id.gridView9);// crear el
            // gridview a partir del elemento del xml gridview

            gridviewTitulos.setAdapter(new CustomGridViewAdapter(getApplicationContext(), titulos, "grd"));// con setAdapter se llena


            String [] total = new String [] {"TOTAL",Integer.toString(count)};

            final GridView gridviewTotal = (GridView) findViewById(R.id.gridView12);// crear el
            // gridview a partir del elemento del xml gridview

            gridviewTotal.setAdapter(new CustomGridViewAdapter(getApplicationContext(), total, "grd"));






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
        System.out.println("GETTTTTTT" + str.toString());
        return str.toString();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bodega, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
