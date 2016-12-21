package com.example.david.proyectowms;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class GuardarTerminar extends Activity {
    String cod_empresa = "";
    String cod_sucursal = "";
    String cod_proceso = "";
    String cod_placa = "";
    String cod_usuario = "";
    String cod_ubicacion = "";
    String fec_proceso = "";
    String num_nit = "";
    String ind_estado = "O";
    String ip ;
    String[] arrayGrid ;
    String mecanico = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardar_terminar);
        Button guardar = (Button) findViewById(R.id.button_guardar);
       // guardar.setVisibility(View.INVISIBLE);
        arrayGrid = new String[1];

        Bundle bundle = this.getIntent().getExtras();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        System.out.println("-------------DATOSSSSS-----------");
        cod_empresa = bundle.getString("cod_empresa");
        System.out.println(cod_empresa);
        cod_sucursal = bundle.getString("cod_sucursal");
        System.out.println(cod_sucursal);
        cod_proceso = bundle.getString("cod_proceso");
        System.out.println(cod_proceso);
        cod_placa = bundle.getString("cod_placa");
        System.out.println(cod_placa);
        cod_usuario = bundle.getString("cod_usuario");
        System.out.println(cod_usuario);
        cod_ubicacion = bundle.getString("cod_ubicacion");
        System.out.println(cod_ubicacion);
        fec_proceso = bundle.getString("fec_proceso");
        System.out.println(fec_proceso);
        num_nit = bundle.getString("num_nit");
        System.out.println(num_nit);
        ind_estado = bundle.getString("ind_estado");
        System.out.println(ind_estado);
        ip = bundle.getString("ip");
        ArrayList atributos = bundle.getStringArrayList("atributos");

        String url = "http://" + ip + "/atributos.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        /** Get result from Server (Return the JSON Code)
         * StatusID = ? [0=Failed,1=Complete]
         * Error	= ?	[On case error return custom error message]
         *
         * Eg Save Failed = {"StatusID":"0","Error":"Email Exists!"}
         * Eg Save Complete = {"StatusID":"1","Error":""}
         */


        String resultServer  = getHttpPost(url, params);
        System.out.println("---------------------------------");
        System.out.println(resultServer);
        try {
            JSONArray jArray = new JSONArray(resultServer);
            ArrayList<String> array = new ArrayList<String>();
            //Toast.makeText(getApplicationContext()," proceso " + array.get(0),Toast.LENGTH_LONG).show();
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                array.add(json.getString("cod_atributo").trim() + " :");
                array.add((json.getString("cod_valor")));
            }
            arrayGrid = new String [array.size()];
            for(int i = 0;i<array.size();i++){
                arrayGrid[i] = (array.get(i));
            }
        }catch (JSONException e){
            arrayGrid = new String[1];
            e.printStackTrace();
        }



        GridView gridview = (GridView) findViewById(R.id.gridView5);// crear el
        // gridview a partir del elemento del xml gridview

        gridview.setAdapter(new CustomGridViewAdapter(getApplicationContext(), arrayGrid, "grd"));// con setAdapter se llena


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // Toast para mostrar un mensaje. Escribe el nombre de tu clase
                // si no la llamaste MainActivity.


            }
        });

        for (int i = 0;i<arrayGrid.length-1;i++){
            if (arrayGrid[i].equals("")){

            }else{
                guardar.setVisibility(View.VISIBLE);
            }
        }


                fec_proceso = getCode();






    }

    private String getCode()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String date = dateFormat.format(new Date() );
        return date;
    }




    public void guardar (View v) {

        String num_rombo =" rombo ";
        ArrayList<Boolean> atributosBool = new ArrayList<>();
        for (int i = 0;i<arrayGrid.length-1;i++){
            if (arrayGrid[i].equals("ROMBO :")){
                num_rombo = arrayGrid[i+1];
            }
            if (arrayGrid[i].equals("PLACA :")){
                cod_placa = arrayGrid[i+1];
            }
            if (arrayGrid[i].equals("UBICACION :")){
                cod_ubicacion = arrayGrid[i+1];
            }

            if (arrayGrid[i].equals("NIT :")){
                num_nit = arrayGrid[i+1];
            }

        }

        String url2 = "http://" + ip + "/procesoSiguiente.php";

        List<NameValuePair> params2 = new ArrayList<NameValuePair>();
        params2.add(new BasicNameValuePair("sCodProceso",cod_proceso));

        String resultServer2  = getHttpPost(url2,params2);
        System.out.println(resultServer2);
        ArrayList<String> array2 = new ArrayList<String>();
        ArrayList<String>  procesosantsigui = new ArrayList<>();
        String procesoSiguiente = "";
        try {

            JSONArray jArray2 = new JSONArray(resultServer2);

            for (int i = 0; i < jArray2.length(); i++) {
                JSONObject json2 = jArray2.getJSONObject(i);
                array2.add(json2.getString("cod_proceso_siguiente"));
                procesoSiguiente = array2.get(0);
                array2.add(json2.getString("cod_proceso_anterior"));
            }

            for (int i = 0;i<array2.size();i++){
                procesosantsigui.add(array2.get(i));
            }

        }catch (JSONException e ){
            e.printStackTrace();
        }


        String url = "http://" + ip + "/insertEstado.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();

            /*if(proceso.equals("3")){
                proceso = "4";
            }else{
                if(proceso.equals("7")){
                    proceso = "8";
                }else{
                    if (proceso.equals("9")){
                        proceso = "6";
                    }
                }
            }*/

        //proceso = procesosantsigui.get(0);
        params.add(new BasicNameValuePair("sCodCodigo", ""));
        params.add(new BasicNameValuePair("sCodProceso", procesoSiguiente));


        String resultServer = getHttpPost(url, params);
        System.out.println(resultServer);
        JSONObject c;
        try {
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            finish();
        }
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

    public void regresar(View v){
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_guardar, menu);
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
