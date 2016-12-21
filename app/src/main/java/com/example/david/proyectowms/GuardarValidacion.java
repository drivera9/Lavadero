package com.example.david.proyectowms;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ScrollView;
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


public class GuardarValidacion extends Activity {
    String procesoOriginal = "";
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
    ArrayList<String> columnas ;
    ArrayList<String> datos ;
    String cod_tecnico = "";
    String procesoSiguiente = "";
    private View Progress;
    private ScrollView Scroll;
    ProgressDialog mProgressDialog;
    String faltanDatos;
    String km = "";
    String num_rombo = "";
    String procesoAnterior = "";
    String equivalenciaBodega = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardar);
        Button guardar = (Button) findViewById(R.id.button_guardar);
       // guardar.setVisibility(View.INVISIBLE);
        arrayGrid = new String[1];

        columnas = new ArrayList<>();

        Scroll =(ScrollView) findViewById(R.id.ScrollLogin);
        Progress = findViewById(R.id.progressbar);
        Bundle bundle = this.getIntent().getExtras();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        mProgressDialog= new ProgressDialog(GuardarValidacion.this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Guardando datos...");

        System.out.println("-------------DATOSSSSS-----------");
        cod_empresa = bundle.getString("cod_empresa");
        km = bundle.getString("km");
        System.out.println(cod_empresa);
        cod_sucursal = bundle.getString("cod_sucursal");
        if (cod_sucursal.trim().equals("51")){
            equivalenciaBodega = "1";
        }else{
            if (cod_sucursal.trim().equals("33")){
                equivalenciaBodega = "2";
            }else{
                if (cod_sucursal.trim().equals("10")){
                    equivalenciaBodega = "3";
                }else{
                    if (cod_sucursal.trim().equals("11")) {
                        equivalenciaBodega = "99";
                    }
                }
            }
        }
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
        faltanDatos = bundle.getString("faltanDatos");
        ArrayList atributos = bundle.getStringArrayList("atributos");
        columnas = bundle.getStringArrayList("columnas");
        datos = bundle.getStringArrayList("datos");
        cod_tecnico = bundle.getString("cod_tecnico");
        num_rombo= bundle.getString("num_rombo");


        for (int i = 0;i<columnas.size();i++){
            System.out.println("COLUMNA " + i +  " " + columnas.get(i));
        }

        for (int i = 0;i<datos.size();i++){
            System.out.println("DATO " + i +  " " + datos.get(i));
        }




        /*String url = "http://" + ip + "/atributos.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
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
        }*/

        arrayGrid = new String [atributos.size()];
        for(int i = 0;i<atributos.size();i++){
            arrayGrid[i] = (atributos.get(i).toString());
            System.out.println(arrayGrid[i]);
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


    public void prueba2(View v){




        String url = "http://" + ip + "/guardarMovimiento.php";
        String nombreTabla = "Terceros1";

        String sql = "INSERT INTO " + nombreTabla + " (";



        for (int i = 0;i<columnas.size()-1;i++){
                sql += columnas.get(i) + ",";
        }

        sql+= columnas.get(columnas.size()-1);
        sql+= ") VALUES (";

        for (int i = 0;i<datos.size()-1;i++){
            if (datos.get(i).equals("GETDATE()")){
                sql +=  datos.get(i) +  ",";
            }else {
                    sql += "'" + datos.get(i) + "'" + ",";

            }
        }
        sql+=  "'"  + datos.get(datos.size()-1) + "'";
        sql+= ");";


        System.out.println("ESTE ES EL SQL -> " + sql);



                    cod_proceso = "1";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sSQL", sql));
        params.add(new BasicNameValuePair("sParametro", "1"));

        String resultServer = getHttpPost(url, params);
        System.out.println(resultServer);


        try {

            String url2 = "http://" + ip + "/aumentarConsecutivo.php";

            List<NameValuePair> params2 = new ArrayList<NameValuePair>();

            String resultServer2 = getHttpPost(url2, params2);
            System.out.println(resultServer2);


            try {

            } catch (Exception ex) {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }
            //finish();
            //arrayGrid = new String[10];
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),"No se guardo el encabezado" ,Toast.LENGTH_LONG).show();
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
        //guardar();
    }

    public void prueba(View v){
        if (faltanDatos.equals("false")) {
            new GuardarMovimiento().execute("");
        }else{
            Toast.makeText(getApplicationContext(),"Hace faltan datos!",Toast.LENGTH_LONG).show();
        }
    }

    class GuardarMovimiento extends AsyncTask<String, String, String> {

        String resultado;
        String user;
        @Override
        protected void onPreExecute(){
            mProgressDialog.show();

        }
        @Override
        protected String doInBackground(String... f_url) {

            String url3 = "http://" + ip + "/consultarGeneral.php";

            List<NameValuePair> params3 = new ArrayList<NameValuePair>();
            params3.add(new BasicNameValuePair("sNumRombo", num_rombo));
            params3.add(new BasicNameValuePair("sCodSucursal", cod_sucursal));
            params3.add(new BasicNameValuePair("sParametro", "validarBod"));


            String resultServer3 = getHttpPost(url3, params3);
            System.out.println("---------------------------------resultserver----------------");
            System.out.println(resultServer3);
            String cuenta = "";
            try {

                JSONArray jArray = new JSONArray(resultServer3);
                ArrayList<String> array = new ArrayList<String>();
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json = jArray.getJSONObject(i);
                    array.add(json.getString("cuenta"));
                }
                cuenta = array.get(0);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            String url6 = "http://" + ip + "/consultarGeneral.php";

            List<NameValuePair> params6 = new ArrayList<NameValuePair>();
            params6.add(new BasicNameValuePair("sNumRombo", num_rombo));
            params6.add(new BasicNameValuePair("sCodSucursal", cod_sucursal));
            params6.add(new BasicNameValuePair("sParametro", "validarPlaca"));

            String placa = "";
            String resultServer6 = getHttpPost(url6, params6);
            System.out.println(resultServer6);
            try {

                JSONArray jArray = new JSONArray(resultServer6);
                ArrayList<String> array = new ArrayList<String>();
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json = jArray.getJSONObject(i);
                    array.add(json.getString("placa"));
                    array.add(json.getString("cod_proceso"));
                }
                placa = array.get(0).trim();
                procesoOriginal = array.get(1).trim();
            } catch (JSONException e) {
                e.printStackTrace();
            }



                String url = "http://" + ip + "/guardarValidacion.php";


                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("skm", km));
                params.add(new BasicNameValuePair("sPlaca", placa));
                params.add(new BasicNameValuePair("sNumRombo", num_rombo));
                params.add(new BasicNameValuePair("sBodega", equivalenciaBodega));


                String resultServer = getHttpPost(url, params);
                System.out.println(resultServer);


                try {


                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), "No se guardo el detalle", Toast.LENGTH_LONG).show();
                    // TODO Auto-generated catch block
                    ex.printStackTrace();
                }

                String url2 = "http://" + ip + "/procesoSiguiente.php";

                List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                params2.add(new BasicNameValuePair("sCodProceso", cod_proceso));

                String resultServer2 = getHttpPost(url2, params2);
                System.out.println(resultServer2);
                ArrayList<String> array2 = new ArrayList<String>();
                ArrayList<String> procesosantsigui = new ArrayList<>();
                try {

                    JSONArray jArray2 = new JSONArray(resultServer2);

                    for (int i = 0; i < jArray2.length(); i++) {
                        JSONObject json2 = jArray2.getJSONObject(i);
                        array2.add(json2.getString("cod_proceso_siguiente"));
                        array2.add(json2.getString("cod_proceso_anterior"));
                        procesoSiguiente = array2.get(0);
                        procesoAnterior = array2.get(1);
                    }

                    for (int i = 0; i < array2.size(); i++) {
                        procesosantsigui.add(array2.get(i));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String url1 = "http://" + ip + "/insertEstado.php";

                List<NameValuePair> params1 = new ArrayList<NameValuePair>();

                //proceso = procesosantsigui.get(0);
                params1.add(new BasicNameValuePair("sCodCodigo", num_rombo));
                params1.add(new BasicNameValuePair("sCodProcesoAnterior", procesoOriginal));
                params1.add(new BasicNameValuePair("sNumRombo", num_rombo));
                params1.add(new BasicNameValuePair("sCodProceso", procesoSiguiente));
                params1.add(new BasicNameValuePair("sCodSucursal", cod_sucursal));

                String resultServer1 = getHttpPost(url1, params1);
                System.out.println(resultServer1);
                JSONObject c;
                try {

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                }

            if (Integer.parseInt(cuenta) > 1){
                String url5 = "http://" + ip + "/guardarMovimiento.php";


                List<NameValuePair> params5 = new ArrayList<NameValuePair>();
                params5.add(new BasicNameValuePair("sNumRombo", num_rombo));
                params5.add(new BasicNameValuePair("sCodSucursal", cod_sucursal));
                params5.add(new BasicNameValuePair("sParametro", "validarBod"));

                String resultServer5 = getHttpPost(url5, params5);
                System.out.println(resultServer5);
            }

            return null;
        }

        // Once Music File is downloaded
        @Override
        protected void onPostExecute(String file_url) {
            mProgressDialog.dismiss();
            finish();
        }
    }



    public void guardar (View v) {

        String num_rombo =" rombo ";
        ArrayList<Boolean> atributosBool = new ArrayList<>();
        for (int i = 0;i<arrayGrid.length-1;i++){
            if (arrayGrid[i].equals("ROMBO :")){
                num_rombo = arrayGrid[i+1];
            }
            if (arrayGrid[i].equals("MECANICO :")){
                cod_tecnico = arrayGrid[i+1];
            }
            if (arrayGrid[i].equals("PLACA :")){
                cod_placa = arrayGrid[i+1];
            }
            if (arrayGrid[i].equals("UBIC :")){
                cod_ubicacion = arrayGrid[i+1];
            }

            if (arrayGrid[i].equals("NIT :")){
                num_nit = arrayGrid[i+1];
            }

        }

        for (int i =0;i<arrayGrid.length-1;i++) {

            System.out.println("ARRAYYYYYYYYYYYYYYYYYY->" + arrayGrid[i]);
        }

        /*String url4 = "http://" + ip + "/consultarPlaca.php";

        List<NameValuePair> params4 = new ArrayList<NameValuePair>();

        ArrayList<String> array = new ArrayList<String>();

        String resultServer4 = getHttpPost(url4, params4);
        try {

            JSONArray jArray = new JSONArray(resultServer4);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                array.add(json.getString("cod_placa"));
                array.add(json.getString("num_rombo"));
            }

        }catch (JSONException e ){
            e.printStackTrace();
        }

        boolean placa;

        for (int i = 0;i<array.size();i++){
            if (array.equals(num))
        }
*/

        ArrayList<String> rutas = new ArrayList<>();
        rutas.add(num_rombo);
        rutas.add(cod_ubicacion);
        rutas.add(cod_tecnico);

        /*for (int i = 0;i<rutas.size();i++) {

            //showProgress(true);

            String url3 = "http://" + ip + "/desactivarRomUbic.php";

            List<NameValuePair> params3 = new ArrayList<NameValuePair>();

            params3.add(new BasicNameValuePair("sValRecurso",rutas.get(i)));
            params3.add(new BasicNameValuePair("sCodSucursal", cod_sucursal));


            String resultServer3 = getHttpPost(url3, params3);
            System.out.println(resultServer3);


            try {

            } catch (Exception ex) {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }
        }*/

        String url1 = "http://" + ip + "/actualizarCita.php";

        List<NameValuePair> params1 = new ArrayList<NameValuePair>();

        params1.add(new BasicNameValuePair("sNumRombo", num_rombo));
        params1.add(new BasicNameValuePair("sCodUbicacion", cod_ubicacion));
        params1.add(new BasicNameValuePair("sCodPlaca", cod_placa));


        /** Get result from Server (Return the JSON Code)
         * StatusID = ? [0=Failed,1=Complete]
         * Error	= ?	[On case error return custom error message]
         *
         * Eg Save Failed = {"StatusID":"0","Error":"Email Exists!"}
         * Eg Save Complete = {"StatusID":"1","Error":""}
         */
        String resultServer1 = getHttpPost(url1, params1);
        System.out.println(resultServer1);


        try {

        } catch (Exception ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }

        boolean atributo = false;

       /* String url3 = "http://" + ip + "/consultarGeneral.php";

        List<NameValuePair> params3 = new ArrayList<NameValuePair>();
        params3.add(new BasicNameValuePair("sCodEmpresa", cod_empresa));
        params3.add(new BasicNameValuePair("sCodSucursal", cod_sucursal));
        params3.add(new BasicNameValuePair("sCodPlaca", cod_placa.trim()));
        params3.add(new BasicNameValuePair("sNumRombo", num_rombo.trim()));
        params3.add(new BasicNameValuePair("sParametro", "detalle"));


        //showProgress(true);

        String resultServer3  = getHttpPost(url3,params3);
        System.out.println("---------------------------------resultserver----------------");
        System.out.println(resultServer3);
        try {

            JSONArray jArray = new JSONArray(resultServer3);
            ArrayList<String> array = new ArrayList<String>();
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                array.add(json.getString("num_rombo").trim());
                array.add(json.getString("cod_placa").trim());
            }

            for (int i = 0; i < array.size(); i= i+2) {
                if (array.get(i).equals(num_rombo) && array.get(i).equals(cod_placa)){
                    Toast.makeText(getApplicationContext(), "o se puede insertar, ya hay una placa igual!", Toast.LENGTH_SHORT).show();
                }else{*/
                    String url = "http://" + ip + "/guardarMovimiento.php";



                    cod_proceso = "1";
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("sCodEmpresa", cod_empresa));
                    params.add(new BasicNameValuePair("sCodSucursal", cod_sucursal));
                    params.add(new BasicNameValuePair("sCodUsuario", cod_usuario));
                    params.add(new BasicNameValuePair("sCodProceso", cod_proceso));
                    params.add(new BasicNameValuePair("sCodUbicacion", cod_ubicacion));
                    params.add(new BasicNameValuePair("sFecProceso", "2"));
                    params.add(new BasicNameValuePair("sNumNit", num_nit));
                    params.add(new BasicNameValuePair("sIndEstado", ind_estado));
                    params.add(new BasicNameValuePair("sNumRombo", num_rombo));
                    params.add(new BasicNameValuePair("sCodPlaca", cod_placa));
                    params.add(new BasicNameValuePair("sCodTecnico", cod_tecnico));
                    params.add(new BasicNameValuePair("sNumItem", "0"));
                    params.add(new BasicNameValuePair("sNumTotal", "0"));
                    params.add(new BasicNameValuePair("sCodCodigo", ""));
                    params.add(new BasicNameValuePair("sCodUbicacionBodega", "1"));
                    params.add(new BasicNameValuePair("sParametro", "2"));




                    JSONObject c3;

                    String resultServer = getHttpPost(url, params);
                    System.out.println(resultServer);


                    try {
            /*String url2 = "http://" + ip + "/aumentarConsecutivo.php";

            List<NameValuePair> params2 = new ArrayList<NameValuePair>();

            String resultServer2 = getHttpPost(url2, params2);
            System.out.println(resultServer2);


            try {

            } catch (Exception ex) {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }*/
                        Intent data = new Intent();
                        data.setData(Uri.parse("bien"));
                        setResult(RESULT_OK, data);
                        finish();
                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(),"No se guardo el detalle" ,Toast.LENGTH_LONG).show();
                        // TODO Auto-generated catch block
                        ex.printStackTrace();
                    }
               // }
            //}

        /*}catch (JSONException e ){
            e.printStackTrace();
        }*/

        /*String url2 = "http://192.168.1.190:8080/guardarPrueba.php";



        cod_proceso = "1";
        List<NameValuePair> params2 = new ArrayList<NameValuePair>();

        String resultServer2 = getHttpPost(url2, params2);
        System.out.println(resultServer2);
        try {
            finish();
        } catch (Exception ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }*/



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

        Intent data = new Intent();
        data.putExtra("parametro", "regresar");
        data.putExtra("valor", "si");
        setResult(RESULT_OK, data);
        finish();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        showProgress(show, this, Progress, Scroll);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static void showProgress(final boolean show, Context ctx,
                                    final View mProgressView, final View mFormView) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = ctx.getResources()
                    .getInteger(android.R.integer.config_shortAnimTime);

            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {

            Intent data = new Intent();
            data.putExtra("parametro", "regresar");
            data.putExtra("valor", "si");
            setResult(RESULT_OK, data);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
