package com.example.david.proyectowms;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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


public class BodegaMercar extends ActionBarActivity {
    String conexion = "";
    String user = "";
    String pass = "";
    String ip = "";
    String rombo = "";
    String proceso = "";
    String sucursal = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bodega_mercar);
        final EditText plu = (EditText) findViewById(R.id.editText_plu);
        //plu.setInputType(InputType.TYPE_NULL);

        final Bundle bundle = this.getIntent().getExtras();
        user = bundle.getString("user");
        pass = bundle.getString("pass");
        conexion = bundle.getString("conexion");
        ip = bundle.getString("ip");
        proceso= bundle.getString("proceso");
        sucursal = bundle.getString("sucursal");
        rombo = bundle.getString("rombo");

        actualizar();
        final ViewGroup vg = (ViewGroup) findViewById (R.id.bodegamercar);



        final GridView gridview = (GridView) findViewById(R.id.gridView8);
        plu.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    vg.invalidate();
                   update(plu.getText().toString().trim());
                    actualizar();
                    plu.setText("");
                }
                return false;
            }

        });

    }

    public void actualizar(){
        String url = "http://" + ip + "/consultarDetalle.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sIp", ip));
        params.add(new BasicNameValuePair("sUser", user));
        params.add(new BasicNameValuePair("sPass", pass));
        params.add(new BasicNameValuePair("sBd", "openbravo"));
        params.add(new BasicNameValuePair("sCodProceso",proceso ));
        params.add(new BasicNameValuePair("sCodEmpresa","METRO" ));
        params.add(new BasicNameValuePair("sCodSucursal", sucursal));
        params.add(new BasicNameValuePair("sNumRombo", rombo));
        params.add(new BasicNameValuePair("sTipoConsulta", "mercar"));

        System.out.println("------------DETALLEEESSS----------");
        System.out.println(ip);
        System.out.println(user);
        System.out.println(pass);
        System.out.println(proceso);
        System.out.println(sucursal);
        System.out.println(rombo);

        /** Get result from Server (Return the JSON Code)
         * StatusID = ? [0=Failed,1=Complete]
         * Error	= ?	[On case error return custom error message]
         *
         * Eg Save Failed = {"StatusID":"0","Error":"Email Exists!"}
         * Eg Save Complete = {"StatusID":"1","Error":""}
         */

        String resultServer  = getHttpPost(url,params);
        try {
            JSONArray jArray = new JSONArray(resultServer);
            ArrayList<String> array = new ArrayList<>();
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                array.add(json.getString("ind_estado"));
                array.add(json.getString("num_item"));
                array.add(json.getString("cod_ubicacion"));
                array.add(json.getString("can_pedida"));
                array.add(json.getString("can_despachada"));
                array.add(json.getString("cod_codigo"));
                array.add(json.getString("nom_codigo"));
                array.add(json.getString("can_existencia"));
                array.add(json.getString("cuenta"));
            }


            final GridView gridview = (GridView) findViewById(R.id.gridView8);
            final String[] mercado = new String[array.size()];
            if (!array.get(0).equals("")) {
                for (int i = 0; i < array.size(); i++) {
                    mercado[i] = array.get(i);
                }
            }else {
                finish();
            }

            String [] titulos = new String [] {"AAAAAAAAAAAAAAAAAAAAAAAAA","NIIIIIIIIIIIIIIII","CU","CP","CD","CC","NC","CE","C"};

            final GridView gridviewTotal = (GridView) findViewById(R.id.gridView14);// crear el
            // gridview a partir del elemento del xml gridview

            gridviewTotal.setAdapter(new CustomGridViewAdapter(getApplicationContext(), titulos, "grid"));


            // crear el
            // gridview a partir del elemento del xml gridview

            gridview.setAdapter(new CustomGridViewAdapter(getApplicationContext(), mercado, "grid"));// con setAdapter se llena


            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {


                }
            });






        }catch (JSONException e ){
            e.printStackTrace();
            finish();
        }
    }

    public void update(String codigo){
        String url = "http://" + ip + "/updateEstado.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();


        params.add(new BasicNameValuePair("sCodCodigo", codigo));

        String resultServer = getHttpPost(url, params);

        String strStatusID = "0";
        String strError = "Unknow Status!";

        JSONObject c;
        try {
            c = new JSONObject(resultServer);
            strStatusID = c.getString("StatusID");
            strError = c.getString("Error");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        actualizar();
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
        getMenuInflater().inflate(R.menu.menu_bodega_mercar, menu);
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
