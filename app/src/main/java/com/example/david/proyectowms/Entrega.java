package com.example.david.proyectowms;

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


public class Entrega extends ActionBarActivity {
    String conexion = "";
    String user = "";
    String pass = "";
    String ip = "";
    String titulo = "";
    String proceso = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bodega);

        final Bundle bundle = this.getIntent().getExtras();
        user = bundle.getString("user");
        pass = bundle.getString("pass");
        proceso = bundle.getString("proceso");
        conexion = bundle.getString("conexion");
        ip = bundle.getString("ip");
        titulo = bundle.getString("titulo");
        setTitle(titulo);

        //String url = "http://" + ip + "/consultarSucursal.php";
        String url = "http://" + ip + "/consultarDetalle.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sIp", ip));
        params.add(new BasicNameValuePair("sUser", user));
        params.add(new BasicNameValuePair("sPass", pass));
        params.add(new BasicNameValuePair("sBd", "openbravo"));
        params.add(new BasicNameValuePair("sCodProceso",proceso));
        params.add(new BasicNameValuePair("sTipoConsulta", "sucursal"));

        /** Get result from Server (Return the JSON Code)
         * StatusID = ? [0=Failed,1=Complete]
         * Error	= ?	[On case error return custom error message]
         *
         * Eg Save Failed = {"StatusID":"0","Error":"Email Exists!"}
         * Eg Save Complete = {"StatusID":"1","Error":""}
         */

        String resultServer  = getHttpPost(url,params);
        System.out.println(resultServer);
        try {
            JSONArray jArray = new JSONArray(resultServer);
            ArrayList<String> array = new ArrayList<String>();
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                array.add(json.getString("cod_empresa"));
                array.add(json.getString("cod_sucursal"));
                array.add(Integer.toString(json.getInt("cuenta")));
            }

            final String [] sucursal = new String [(array.size()-(array.size()/3))];
            int j = 0;
            int count = 0;
            for (int i = 0;i<array.size();i= i+3){
                sucursal[j] = array.get(i+1);
                sucursal[j+1] = array.get(i+2);
                count = count + Integer.parseInt(array.get(i+2));
                System.out.println(sucursal[j]);
                System.out.println(sucursal[j+1]);
                j=j+2;
            }





            final GridView gridview = (GridView) findViewById(R.id.gridView6);// crear el
            // gridview a partir del elemento del xml gridview

            gridview.setAdapter(new CustomGridViewAdapter(getApplicationContext(), sucursal, "grd"));// con setAdapter se llena


            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    if (position%2==0){
                        if (sucursal[position].equals("TOTAL")){

                        }else {
                            Intent intent = new Intent((Context) Entrega.this, (Class) BodegaRombo.class);
                            Bundle bundle2 = new Bundle();
                            bundle2.putString("sucursal", sucursal[Integer.parseInt(String.valueOf(id))]);
                            bundle2.putString("conexion", conexion);
                            bundle2.putString("ip", ip);
                            bundle2.putString("proceso", proceso);
                            bundle2.putString("titulo", titulo + "/Suc" + sucursal[Integer.parseInt(String.valueOf(id))]);
                            intent.putExtras(bundle2);
                            startActivity(intent);
                        }
                    }else {


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




        }catch (JSONException e ){
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
