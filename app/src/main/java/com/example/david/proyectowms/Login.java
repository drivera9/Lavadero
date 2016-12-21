package com.example.david.proyectowms;

/*
 * Decompiled with CFR 0_92.
 *
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Bundle
 *  android.text.Editable
 *  android.view.Menu
 *  android.view.MenuInflater
 *  android.view.MenuItem
 *  android.view.View
 *  android.widget.EditText
 *  android.widget.Toast
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 */
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 * Failed to analyse overrides
 */
public class Login
        extends Activity  {
    private ProgressDialog pd = null;
    String dirIP = "";
    String cod_sucursal;
    String cod_empresa;
    String Ip= "";
    String pass = "";
    ArrayList<String> procesos = new ArrayList<>();
    ArrayList<String> nomProcesos = new ArrayList<>();
    private View Progress;
    private ScrollView Scroll;
    ProgressDialog mProgressDialog;
    String conexion;
    EditText ip ;


    public void Imprimir (View v){
        Intent intent = new Intent((Context) this, (Class) Imprimir.class);
        this.startActivity(intent);
        this.finish();
    }

    class ActualizarFechas extends AsyncTask<String, String, String> {

        String resultado;
        String user;
        @Override
        protected void onPreExecute(){
            mProgressDialog.show();
            ip = (EditText) findViewById(R.id.editText_ip);
            Ip = ip.getText().toString();

        }
        @Override
        protected String doInBackground(String... f_url) {

            String url = "http://" + Ip + "/consultarGeneral2.php";

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("sParametro", "fechas"));


            String resultServer = getHttpPost(url, params);
            System.out.println("---------------------------------resultserver----------------");

            ArrayList ids = new ArrayList();
            ArrayList fecInicial = new ArrayList();
            ArrayList fecFinal = new ArrayList();
            try {

                JSONArray jArray = new JSONArray(resultServer);
                ArrayList<String> array = new ArrayList<String>();
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json = jArray.getJSONObject(i);
                    ids.add(json.getString("id"));
                    array.add(json.getString("cod_placa"));
                    fecInicial.add(json.getString("fec_inicial"));
                    fecFinal.add(json.getString("fec_final"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            for(int i = 0;i<ids.size();i++){
                System.out.println("id -> " + ids.get(i));
                System.out.println("fecinicial -> " + fecInicial.get(i));
                System.out.println("fecfinal -> " + fecFinal.get(i));
            }
            for (int i = 0;i<ids.size()-1;i++){
                String url2 = "http://" + Ip + "/guardarPrueba.php";

                List<NameValuePair> params2 = new ArrayList<NameValuePair>();

                params2.add(new BasicNameValuePair("sId", ids.get(i).toString()));
                params2.add(new BasicNameValuePair("sHora", fecInicial.get(i+1).toString().substring(11,16)));
                params2.add(new BasicNameValuePair("sfecha", fecInicial.get(i+1).toString()));
                params2.add(new BasicNameValuePair("sParametro", "fechasReportes"));

                String resultServer2 = getHttpPost(url2, params2);
                System.out.println(resultServer2);
            }

            return null;
        }

        // Once Music File is downloaded
        @Override
        protected void onPostExecute(String file_url) {

            mProgressDialog.dismiss();

        }
    }

    public void fechas(View v){
        new ActualizarFechas().execute("");

    }

    public void matriz(View v){
        String url = "http://10.0.10.194:8090/Matrix32x32.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String resultServer  = getHttpPost(url,params);
        System.out.println(resultServer);
        String matriz = "T";
        try {

            JSONArray jArray = new JSONArray(resultServer);
            ArrayList<String> array = new ArrayList<String>();
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                array.add(json.getString("prueba_tabla"));
                matriz = array.get(0);
            }

        }catch (JSONException e ){
            e.printStackTrace();
        }

        String url1 = "http://10.0.10.194:8090/puertoSerie.php";

        List<NameValuePair> params1 = new ArrayList<NameValuePair>();
        params1.add(new BasicNameValuePair("sString", matriz + "."));
        String resultServer1  = getHttpPost(url1,params1);
        System.out.println("---------------------------------resultserver----------------");


    }

    public void procedimiento(View v){
        String url = "http://" + Ip + "/procedimiento.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        String resultServer  = getHttpPost(url,params);
        System.out.println("---------------------------------resultserver----------------");
        System.out.println(resultServer);
    }

    public void texto51(View v){
        EditText User = (EditText) findViewById(R.id.editText);
        User.setText("juan51");
    }

    public void texto33(View v){
        EditText User = (EditText) findViewById(R.id.editText);
        User.setText("juan33");
    }

    public void texto10(View v){
        EditText User = (EditText) findViewById(R.id.editText);
        User.setText("juan10");
    }


    class ConsultarDatos extends AsyncTask<String, String, String> {

        boolean red = true;
        String resultado;
        String user;
        @Override
        protected void onPreExecute(){
            mProgressDialog.show();

            EditText User = (EditText) findViewById(R.id.editText);

            user = User.getText().toString().toUpperCase();
            Ip = ip.getText().toString().trim();

        }
        @Override
        protected String doInBackground(String... f_url) {


            InputStream isr = null;
            String url = "http://" + Ip + "/consultarGeneral.php";

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("sUser", user));
            params.add(new BasicNameValuePair("sParametro", "login"));


            String resultServer = getHttpPost(url, params);
            System.out.println("---------------------------------resultserver----------------");
            try {

                JSONArray jArray = new JSONArray(resultServer);
                ArrayList<String> array = new ArrayList<String>();
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json = jArray.getJSONObject(i);
                    array.add(json.getString("cod_clave"));
                    array.add(json.getString("cod_sucursal"));
                    array.add(json.getString("cod_empresa"));
                    cod_sucursal = array.get(1);
                    cod_empresa = array.get(2);
                    pass = array.get(0).trim();
                }

            } catch (JSONException e) {
                //Toast.makeText((Context)getApplicationContext(), (CharSequence) "usuario no existe !",
                //  Toast.LENGTH_SHORT).show();
                //showProgress(false);
                red = false;
                e.printStackTrace();
            }


            String url1 = "http://" + Ip + "/consultarGeneral.php";

            List<NameValuePair> params1 = new ArrayList<NameValuePair>();
            params1.add(new BasicNameValuePair("sUser", user));
            params1.add(new BasicNameValuePair("sCodEmpresa", cod_empresa));
            params1.add(new BasicNameValuePair("sCodSucursal", cod_sucursal));
            params1.add(new BasicNameValuePair("sParametro", "procesos"));

            String resultServer1 = getHttpPost(url1, params1);
            System.out.println(resultServer1 + " COD PROCESO ");

            try {
                JSONArray jArray = new JSONArray(resultServer1);
                final ArrayList<Integer> array = new ArrayList<Integer>();
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json = jArray.getJSONObject(i);
                    array.add((json.getInt("cod_proceso")));
                    procesos.add(array.get(i).toString());
                    System.out.println("ESTOS SON LOS PROCESOS ->" + procesos.get(i));
                }
            } catch (JSONException e) {
                red = false;
                e.printStackTrace();

            }

            for (int i = 0; i < procesos.size(); i++) {

                String url3 = "http://" + Ip + "/consultarGeneral.php";

                List<NameValuePair> params3 = new ArrayList<NameValuePair>();
                params3.add(new BasicNameValuePair("sCodProceso", procesos.get(i)));
                params3.add(new BasicNameValuePair("sParametro", "nombreProcesos"));
                String resultServer3 = getHttpPost(url3, params3);
                System.out.println("---------------------------------resultserver----------------");
                System.out.println(resultServer3);

                try {

                    JSONArray jArray2 = new JSONArray(resultServer3);
                    ArrayList<String> array2 = new ArrayList<String>();
                    for (int j = 0; j < jArray2.length(); j++) {
                        JSONObject json2 = jArray2.getJSONObject(j);
                        array2.add(json2.getString("nom_proceso"));
                        nomProcesos.add(array2.get(j));
                        //System.out.println("NOMBRE PROCESOS - "+ nomProcesos.get(i));
                    }
                } catch (JSONException e) {
                    red = false;
                    e.printStackTrace();
                }
            }


            return null;
        }

        // Once Music File is downloaded
        @Override
        protected void onPostExecute(String file_url) {
            EditText Pass = (EditText) findViewById(R.id.editText2);
            if(pass.equals(Pass.getText().toString())){
                Bundle bundle = new Bundle();
                Intent intent = new Intent((Context) getApplicationContext(), (Class) Procesos.class);
                bundle.putString("sucursal", cod_sucursal);
                bundle.putString("empresa",cod_empresa );
                bundle.putString("user", user);
                bundle.putString("pass", pass);
                bundle.putString("conexion", conexion);
                bundle.putStringArrayList("procesos", procesos);
                bundle.putStringArrayList("nomProcesos",nomProcesos);
                bundle.putString("ip",Ip);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                return;
            }else{
                Toast.makeText((Context) getApplicationContext(), (CharSequence) "Ocurrio un error! verifique red y " +
                                "que los datos esten correctos!",
                        Toast.LENGTH_SHORT).show();


            }
            mProgressDialog.dismiss();
        }
    }

    class ConsultarDatosLocal extends AsyncTask<String, String, String> {

        boolean red = true;
        String resultado;
        String user;

        @Override
        protected void onPreExecute(){
            mProgressDialog.show();

            EditText User = (EditText) findViewById(R.id.editText);

            user = User.getText().toString().toUpperCase();
            Ip = ip.getText().toString().trim();

        }

        @Override
        protected String doInBackground(String... f_url) {




            return null;
        }

        // Once Music File is downloaded
        @Override
        protected void onPostExecute(String file_url) {

            mProgressDialog.dismiss();

        }
    }

    public void LoginProcesos(View v) {
        EditText User = (EditText) this.findViewById(R.id.editText);
        EditText Pass = (EditText) this.findViewById(R.id.editText2);

        User.requestFocus();

        String user = User.getText().toString().toUpperCase();
        String pass = Pass.getText().toString().toUpperCase();



        conexion = "";
        CheckBox conexionlocal = (CheckBox) findViewById(R.id.checkBox_local);
        CheckBox conexionremoto = (CheckBox) findViewById(R.id.checkBox_remoto);

        if (conexionlocal.isChecked()){
            conexion = "local";
        }

        if (conexionremoto.isChecked()){
            conexion = "remoto";
        }
        Bundle bundle = new Bundle();


        if (conexion.equals("local")) {

            DataBaseHelper dataBaseHelper = new DataBaseHelper(Login.this);


            try {
                dataBaseHelper.createDataBase();
                dataBaseHelper.openDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            ArrayList resultado = dataBaseHelper.getContrasenasProcesos(user);



            if (resultado.get(0).toString().equals(pass)) {


                ArrayList procesos = dataBaseHelper.getProcesos(resultado.get(3).toString(),resultado.get(1).toString(),resultado.get(2).toString());

                cod_sucursal = resultado.get(1).toString();
                cod_empresa = resultado.get(2).toString();

                ArrayList nomProcesos = new ArrayList();


                for (int i = 0;i<procesos.size();i++){
                    nomProcesos = dataBaseHelper.getNomProcesos(procesos.get(i).toString());
                }

                    bundle = new Bundle();
                    Intent intent = new Intent((Context) getApplicationContext(), (Class) Procesos.class);
                    bundle.putString("sucursal", cod_sucursal);
                    bundle.putString("empresa",cod_empresa );
                    bundle.putString("user", user);
                    bundle.putString("pass", pass);
                    bundle.putString("conexion", conexion);
                    bundle.putStringArrayList("procesos", procesos);
                    bundle.putStringArrayList("nomProcesos",nomProcesos);
                    bundle.putString("ip",Ip);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();



                }else{
                        Toast.makeText((Context) getApplicationContext(), (CharSequence) "Ocurrio un error! verifique red y " +
                            "que los datos esten correctos!",
                                    Toast.LENGTH_SHORT).show();


                }

            dataBaseHelper.close();

        }else{


            if (conexion.equals("remoto")){
                new ConsultarDatos().execute("");
            }


        }
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

    public void cancelar(View v){
        finish();
    }
    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        showProgress(show, this, Progress, Scroll);
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.requestWindowFeature(1);
        this.setContentView(R.layout.login);
        setTitle("Login");
        ip = (EditText) findViewById(R.id.editText_ip);
        Ip = ip.getText().toString();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Scroll =(ScrollView) findViewById(R.id.ScrollLogin);
        Progress = findViewById(R.id.progressbar);
        final EditText plu =(EditText) findViewById(R.id.editText_BD);

        mProgressDialog= new ProgressDialog(Login.this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Consultando datos...");

        plu.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Toast.makeText(getApplicationContext(), plu.getText(), Toast.LENGTH_LONG).show();
                    plu.setText("");
                }
                return false;
            }

        });

        final CheckBox checkBoxremoto = (CheckBox) findViewById(R.id.checkBox_remoto);
        final CheckBox checkBoxlocal = (CheckBox) findViewById(R.id.checkBox_local);


        checkBoxremoto.setChecked(true);


        checkBoxremoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkBoxlocal.setChecked(false);

            }
        });
        checkBoxlocal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkBoxremoto.setChecked(false);

            }
        });



    }


    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 2131296441) {
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
