package com.example.david.proyectowms;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MostrarModelo extends Activity {

    AutoCompleteTextView txtSearch;
    AutoCompleteTextView txtSearch1;
    AutoCompleteTextView txtSearch2;
    PeopleAdapter adapter;
    PeopleAdapter adapter1;
    PeopleAdapter adapter2;
    String url = "";
    String ip = "";
    List<Objeto> mList;
    List<Objeto> mList1;
    List<Objeto> mList2;
    TextView seleccionado ;
    String marca = "";
    String marca1 = "";
    String marca2 = "";
    ArrayList<String> general;
    String placa = "";
    String descripcion = "";
    List<Objeto> list = new ArrayList<Objeto>();
    ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_modelo);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mProgressDialog= new ProgressDialog(MostrarModelo.this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Consultando datos...");

        Bundle bundle = this.getIntent().getExtras();
        placa= bundle.getString("placa");
        ip = bundle.getString("ip");
        seleccionado = (TextView) findViewById(R.id.seleccionado);
        new retrievePeople().execute("");




        txtSearch1 = (AutoCompleteTextView) findViewById(R.id.txt_search1);
        txtSearch1.setThreshold(1);
        txtSearch2 = (AutoCompleteTextView) findViewById(R.id.txt_search2);
        txtSearch2.setThreshold(1);





    }

    public void Marca1(){
        mList1 = retrievePeople1();
        adapter1 = new PeopleAdapter(this, R.layout.activity_recepcion, R.id.lbl_name, mList1);
        txtSearch1.setAdapter(adapter1);
        txtSearch1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                marca1 = mList1.get(position).getConcepto();
                seleccionado.setText(marca1);
                descripcion +=  " " + txtSearch1.getText().toString();
                Marca2();
                txtSearch2.requestFocus();
            }
        });
    }

    public void Marca2(){
        mList2 = retrievePeople2();
        adapter2 = new PeopleAdapter(this, R.layout.activity_recepcion, R.id.lbl_name, mList2);
        txtSearch2.setAdapter(adapter2);
        txtSearch2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                marca2 = mList2.get(position).getCodigo();
                descripcion +=  " " + txtSearch2.getText().toString();
                seleccionado.setText(descripcion);

            }
        });
    }

    class retrievePeople extends AsyncTask<String, String, String> {

        boolean red = true;
        String resultado;
        String user;
        @Override
        protected void onPreExecute(){
            mProgressDialog.show();

            list = new ArrayList<Objeto>();

        }
        @Override
        protected String doInBackground(String... f_url) {


            url = "http://" + ip + "/consultarRecurso.php";
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("sParametro", "MODELO"));
            params.add(new BasicNameValuePair("sCodEmpresa", "METRO"));
            params.add(new BasicNameValuePair("sCodSucursal", "1"));

            String resultServer = getHttpPost(url, params);
            System.out.println(resultServer);
            general = new ArrayList<>();

            try {
                JSONArray jArray = new JSONArray(resultServer);
                ArrayList<String> array = new ArrayList<String>();
                int j = 0;
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json = jArray.getJSONObject(i);
                    array.add(json.getString("marca"));
                    array.add(json.getString("descripcion"));
                    list.add(new Objeto(array.get(j+1), array.get(j),i+1));
                    general.add(array.get(j));
                    j=j+2;
                }

            } catch (Throwable e) {

                e.printStackTrace();

            }


            return null;
        }

        // Once Music File is downloaded
        @Override
        protected void onPostExecute(String file_url) {

            mProgressDialog.dismiss();
            mList = list;
            txtSearch = (AutoCompleteTextView) findViewById(R.id.txt_search);
            txtSearch.setThreshold(1);
            adapter = new PeopleAdapter(MostrarModelo.this, R.layout.activity_recepcion, R.id.lbl_name, mList);

            txtSearch.setAdapter(adapter);
            txtSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                    marca = mList.get(position).getConcepto();
                    seleccionado.setText(marca);
                    descripcion += txtSearch.getText().toString();
                    Marca1();
                    txtSearch1.requestFocus();
                }
            });
        }
    }



    private List<Objeto> retrievePeople1() {
        List<Objeto> list = new ArrayList<Objeto>();

        url = "http://" + ip + "/consultarRecurso.php";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sParametro", "MODELO1"));
        params.add(new BasicNameValuePair("sCodEmpresa", "METRO"));
        params.add(new BasicNameValuePair("sMarca", marca));
        params.add(new BasicNameValuePair("sCodSucursal", "1"));

        String resultServer = getHttpPost(url, params);
        System.out.println(resultServer);
        general = new ArrayList<>();

        try {
            JSONArray jArray = new JSONArray(resultServer);
            ArrayList<String> array = new ArrayList<String>();
            int j = 0;
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                array.add(json.getString("modelo"));
                array.add(json.getString("descripcion"));
                list.add(new Objeto(array.get(j+1) ,array.get(j), i+1));
                general.add(array.get(j));
                j = j+2;
            }

        } catch (Throwable e) {

            e.printStackTrace();

        }
        finally {

        }

        return list;
    }

    private List<Objeto> retrievePeople2() {
        List<Objeto> list = new ArrayList<Objeto>();

        url = "http://" + ip + "/consultarRecurso.php";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sParametro", "MODELO2"));
        params.add(new BasicNameValuePair("sCodEmpresa", "METRO"));
        params.add(new BasicNameValuePair("sMarca", marca1));
        params.add(new BasicNameValuePair("sCodSucursal", "1"));

        String resultServer = getHttpPost(url, params);
        System.out.println(resultServer);

        try {
            JSONArray jArray = new JSONArray(resultServer);
            ArrayList<String> array = new ArrayList<String>();
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                array.add(json.getString("ano"));
                list.add(new Objeto(array.get(i) ,"", i+1));
            }

        } catch (Throwable e) {

            e.printStackTrace();

        }
        finally {
        }

        return list;
    }

    public void aceptar(View v){


        List<Objeto> list = new ArrayList<Objeto>();

        url = "http://" + ip + "/consultarRecurso.php";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sParametro", "MODELO3"));
        params.add(new BasicNameValuePair("sMarca", marca1));
        params.add(new BasicNameValuePair("sMarca1", marca2));
        params.add(new BasicNameValuePair("sCodSucursal", "1"));
        params.add(new BasicNameValuePair("sCodEmpresa", "METRO"));

        String resultServer = getHttpPost(url, params);
        System.out.println(resultServer);
        String id_modano = "";

        try {
            JSONArray jArray = new JSONArray(resultServer);
            ArrayList<String> array = new ArrayList<String>();
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                array.add(json.getString("id_modano"));
                id_modano = array.get(0);
            }

            url = "http://" + ip + "/guardarMovimiento.php";
            List<NameValuePair> params2 = new ArrayList<NameValuePair>();
            params2.add(new BasicNameValuePair("sParametro", "modelo"));
            params2.add(new BasicNameValuePair("sPlaca", placa));
            params2.add(new BasicNameValuePair("sModelo",id_modano ));

            String resultServer2 = getHttpPost(url, params2);
            System.out.println(resultServer2);

            final String finalId_modano = id_modano;
            new AlertDialog.Builder(MostrarModelo.this)
                    .setTitle("ACTUALIZACION")
                    .setMessage("ACTUALIZACION EXITOSA!" )
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent data = new Intent();
                            data.putExtra("parametro", "guardarModelo");
                            data.putExtra("valor", "bien");
                            data.putExtra("valor", finalId_modano);
                            data.putExtra("desc", descripcion);
                            setResult(RESULT_OK, data);
                            finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        } catch (Throwable e) {

            e.printStackTrace();

        }
        finally {
        }




    }

    public void cancelar(View v){
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
        finally {
        }
        return str.toString();
    }


}
