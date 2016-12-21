package com.example.david.proyectowms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.List;

public class BodegaGeneral extends Activity {
    String conexion = "";
    String user = "";
    String pass = "";
    String ip = "";
    String titulo = "";
    String proceso = "";
    Boolean mercar = false;
    String empresa;
    String sucursal;
    String procesoAnterior = "";
    String procesOriginal = "";
    ArrayList<String> bodegaSucursal = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bodega_general);
        final Bundle bundle = this.getIntent().getExtras();
        user = bundle.getString("user");
        pass = bundle.getString("pass");
        sucursal = bundle.getString("sucursal");
        empresa = bundle.getString("empresa");

        proceso = bundle.getString("proceso");
        System.out.println("PROCESO---------------" + proceso);
        System.out.println("PASS---------------" + pass);
        conexion = bundle.getString("conexion");
        ip = bundle.getString("ip");
        titulo = bundle.getString("titulo");
        setTitle(titulo.trim());


        ArrayList datos = new ArrayList();
        datos.add(new Lista_entrada(R.drawable.recoger, "Recoger"));
        datos.add(new Lista_entrada(R.drawable.entregar, "Entregar"));


        ListView lista = (ListView) findViewById(R.id.listView3);
        lista.setAdapter(new Lista_adaptador(this, R.layout.entrada, datos) {
            @Override
            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {
                    TextView texto_superior_entrada = (TextView) view.findViewById(R.id.textView_superior);
                    if (texto_superior_entrada != null)
                        texto_superior_entrada.setText(((Lista_entrada) entrada).get_textoEncima());


                    ImageView imagen_entrada = (ImageView) view.findViewById(R.id.imageView_imagen);
                    if (imagen_entrada != null)
                        imagen_entrada.setImageResource(((Lista_entrada) entrada).get_idImagen());
                }
            }
        });

        String url1 = "http://" + ip + "/procesoSiguiente.php";

        List<NameValuePair> params1 = new ArrayList<NameValuePair>();
        params1.add(new BasicNameValuePair("sCodProceso", proceso));


        String resultServer1  = getHttpPost(url1,params1);
        System.out.println("---------------------------------resultserver----------------");
        System.out.println(" PRUEBA PROCESOS" + resultServer1 );
        try {

            JSONArray jArray1 = new JSONArray(resultServer1);
            ArrayList<String> array1 = new ArrayList<String>();
            for (int i = 0; i < jArray1.length(); i++) {
                JSONObject json = jArray1.getJSONObject(i);
                array1.add(json.getString("cod_proceso_siguiente"));
                array1.add(json.getString("cod_proceso_anterior"));
                procesoAnterior = array1.get(1);
            }

            System.out.println("PROCESO ANTERIOR " +procesoAnterior);
        }catch (JSONException e ){
            e.printStackTrace();
        }

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {
                Lista_entrada elegido = (Lista_entrada) pariente.getItemAtPosition(posicion);

                if (elegido.get_textoEncima().equals("Recoger")) {
                    procesOriginal = proceso;
                    System.out.println("procesoooooooooooooooooooooooo original" + procesOriginal);
                    proceso = procesoAnterior;

                    String url = "http://" + ip + "/consultarDetalle.php";

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("sTipo", "1"));
                    params.add(new BasicNameValuePair("sIp", ip));
                    params.add(new BasicNameValuePair("sUser", user));
                    params.add(new BasicNameValuePair("sPass", pass));
                    params.add(new BasicNameValuePair("sCodSucursal", sucursal));
                    params.add(new BasicNameValuePair("sCodEmpresa", empresa.trim()));
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
                        JSONArray jArray = new JSONArray(resultServer.trim());
                        ArrayList<String> array = new ArrayList<String>();
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject json = jArray.getJSONObject(i);
                            array.add(json.getString("cod_empresa").trim());
                            array.add(json.getString("cod_sucursal").trim());
                            array.add(json.getString("cuenta").trim());
                        }

                        for (int i = 0;i< array.size();i++){
                            bodegaSucursal.add(array.get(i));
                            System.out.println("SUCURSAL EN GENERAL - " + bodegaSucursal.get(i));
                        }


                    }catch (JSONException e ){
                        e.printStackTrace();
                    }
                    Intent intent = new Intent((Context) BodegaGeneral.this, (Class) Bodega.class);
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("user", user);
                    bundle2.putString("empresa", empresa );
                    bundle2.putString("sucursal", sucursal );
                    bundle2.putString("pass", pass);
                    bundle2.putString("conexion", conexion);
                    bundle2.putString("ip", ip);
                    bundle2.putString("titulo", titulo + "/");
                    bundle2.putString("proceso", proceso);
                    bundle2.putString("procesoAnterior", procesoAnterior);
                    bundle2.putString("procesOriginal", procesOriginal);
                    bundle2.putStringArrayList("bodegaSucursal",bodegaSucursal);
                    bundle2.putString("tipo", "mercar");
                    intent.putExtras(bundle2);
                    startActivity(intent);
                    finish();
                }

                if (elegido.get_textoEncima().equals("Entregar")) {
                    procesOriginal = " ";

                    String url = "http://" + ip + "/consultarDetalle.php";

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("sTipo", "1"));
                    params.add(new BasicNameValuePair("sIp", ip));
                    params.add(new BasicNameValuePair("sUser", user));
                    params.add(new BasicNameValuePair("sPass", pass));
                    params.add(new BasicNameValuePair("sCodSucursal", sucursal));
                    params.add(new BasicNameValuePair("sCodEmpresa", empresa.trim()));
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
                        JSONArray jArray = new JSONArray(resultServer.trim());
                        ArrayList<String> array = new ArrayList<String>();
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject json = jArray.getJSONObject(i);
                            array.add(json.getString("cod_empresa").trim());
                            array.add(json.getString("cod_sucursal").trim());
                            array.add(json.getString("cuenta").trim());
                        }

                        for (int i = 0;i< array.size();i++){
                            bodegaSucursal.add(array.get(i));
                            System.out.println("SUCURSAL EN GENERAL - " + bodegaSucursal.get(i));
                        }


                    }catch (JSONException e ){
                        e.printStackTrace();
                    }
                    Intent intent = new Intent((Context) BodegaGeneral.this, (Class) Bodega.class);
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("user", user);
                    bundle2.putString("pass", pass);
                    bundle2.putString("conexion", conexion);
                    bundle2.putString("ip", ip);
                    bundle2.putString("titulo", titulo + "/");
                    bundle2.putString("proceso", proceso);
                    bundle2.putString("procesoAnterior", procesoAnterior);
                    bundle2.putString("procesOriginal", procesOriginal);
                    bundle2.putString("empresa", empresa.trim() );
                    bundle2.putString("sucursal", sucursal );
                    bundle2.putStringArrayList("bodegaSucursal",bodegaSucursal);
                    bundle2.putString("tipo", "entrega");
                    intent.putExtras(bundle2);
                    startActivity(intent);
                    finish();
                }
            }
        });

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

    public void regresar(View v){
        finish();
    }
}