package com.example.david.proyectowms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
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


public class BodegaRomboTerminar extends Activity {
    String conexion = "";
    String user = "";
    String pass = "";
    String ip = "";
    String sucursal = "";
    String titulo = "";
    String proceso = "";
    String tipo = "";
    String procesoAnterior;
    String empresa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bodega_rombo);

        final Bundle bundle = this.getIntent().getExtras();
        user = bundle.getString("user");
        pass = bundle.getString("pass");
        proceso = bundle.getString("proceso");
        System.out.println("PROCESOOOoOoOoOoo" + proceso);
        conexion = bundle.getString("conexion");
        ip = bundle.getString("ip");
        tipo = bundle.getString("tipo");
        sucursal = bundle.getString("sucursal");
        empresa = bundle.getString("empresa");
        procesoAnterior = bundle.getString("procesoAnterior");
        titulo = bundle.getString("titulo");
        setTitle(titulo);


        String url = "http://" + ip + "/consultarDetalle.php";


        List<NameValuePair> params = new ArrayList<NameValuePair>();

       /* if (proceso.equals("3")){
            params.add(new BasicNameValuePair("sTipoConsulta", "validacion"));
        }else{*/
            params.add(new BasicNameValuePair("sTipoConsulta", "rombo"));
        //}
        params.add(new BasicNameValuePair("sIp", ip));
        params.add(new BasicNameValuePair("sUser", user));
        params.add(new BasicNameValuePair("sPass", pass));
        params.add(new BasicNameValuePair("sBd", "openbravo"));
        params.add(new BasicNameValuePair("sCodProceso", proceso));
        params.add(new BasicNameValuePair("sCodEmpresa", empresa));
        params.add(new BasicNameValuePair("sCodSucursal",sucursal));
        params.add(new BasicNameValuePair("sTipo", "mercar"));

        System.out.println("--------DETALLE------------");
        System.out.println(user);
        System.out.println(pass);
        System.out.println(proceso.trim());

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
            final ArrayList<String> array = new ArrayList<>();
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                array.add(json.getString("cod_empresa"));
                array.add(json.getString("cod_sucursal"));
                array.add(json.getString("num_rombo"));
                array.add(json.getString("cuenta"));
            }

            final String [] rombos = new String [array.size()-(array.size()/2)];

            int j = 0;
            int count = 0;
            for (int i = 0;i<array.size();i= i+4){
                rombos[j] = array.get(i+2);
                rombos[j+1] = array.get(i+3);
                count = count + Integer.parseInt(array.get(i+3));
                System.out.println(rombos[j]);
                System.out.println(rombos[j+1]);
                j=j+2;
            }





            final GridView gridview = (GridView) findViewById(R.id.gridView7);// crear el
            // gridview a partir del elemento del xml gridview

            gridview.setAdapter(new CustomGridViewAdapter(getApplicationContext(), rombos, "grid"));// con setAdapter se llena


            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View v,
                                        final int position, long id) {

                    System.out.println("ROMBO "+ rombos[position]);
                    AlertDialog.Builder builder = new AlertDialog.Builder(BodegaRomboTerminar.this);
                    builder.setMessage("Realmente desea continuar? esta terminando un carro!! ")
                            .setTitle("Advertencia")
                            .setCancelable(false)
                            .setNegativeButton("NO",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    })
                            .setPositiveButton("SI",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            String url = "http://" + ip + "/insertEstado.php";

                                            List<NameValuePair> params = new ArrayList<NameValuePair>();


                                            //proceso = procesosantsigui.get(0);
                                            params.add(new BasicNameValuePair("sCodCodigo", ""));
                                            params.add(new BasicNameValuePair("sCodProceso", "91"));
                                            params.add(new BasicNameValuePair("sCodProcesoAnterior", procesoAnterior));
                                            params.add(new BasicNameValuePair("sNumRombo", rombos[position]));
                                            String resultServer = getHttpPost(url, params);
                                            System.out.println(resultServer);
                                            JSONObject c;
                                            try {
                                                Toast.makeText(getApplicationContext(),"SE ACTUALIZO CORRECTAMENTE", Toast.LENGTH_LONG).show();
                                                finish();
                                            } catch (Exception e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                                finish();
                                            }
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }

            });

            String [] titulos = new String [] {"rombo","cantidad"};

            final GridView gridviewTitulos = (GridView) findViewById(R.id.gridView10);// crear el
            // gridview a partir del elemento del xml gridview

            gridviewTitulos.setAdapter(new CustomGridViewAdapter(getApplicationContext(), titulos, "grd"));

            String [] total = new String [] {"TOTAL",Integer.toString(count)};

            final GridView gridviewTotal = (GridView) findViewById(R.id.gridView13);// crear el
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
        getMenuInflater().inflate(R.menu.menu_bodega_rombo, menu);
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
