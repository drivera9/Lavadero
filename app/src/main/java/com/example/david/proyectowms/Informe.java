package com.example.david.proyectowms;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.opencsv.CSVWriter;

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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Informe extends Activity {

    String[] rombos;
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
    double total = 0;
    String vehiculo = "";
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informe);

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
        titulo = bundle.getString("titulo");
        vehiculo = bundle.getString("vehiculo");


        mProgressDialog= new ProgressDialog(Informe.this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Espere...");

        String url = "http://" + ip + "/consultarDetalleLavadero.php";


        List<NameValuePair> params = new ArrayList<NameValuePair>();

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());


        params.add(new BasicNameValuePair("sTipoConsulta", "informe"));
        params.add(new BasicNameValuePair("sAno", String.valueOf(calendar.get(Calendar.YEAR))));
        params.add(new BasicNameValuePair("sMes", String.valueOf(calendar.get(Calendar.MONTH)+1)));
        params.add(new BasicNameValuePair("sDia", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))));
        params.add(new BasicNameValuePair("sIp", ip));
        params.add(new BasicNameValuePair("sUser", user));
        params.add(new BasicNameValuePair("sPass", pass));
        params.add(new BasicNameValuePair("sBd", "openbravo"));
        params.add(new BasicNameValuePair("sCodProceso", "1"));
        params.add(new BasicNameValuePair("sCodEmpresa", empresa));
        params.add(new BasicNameValuePair("sCodSucursal", Sucursal));
        params.add(new BasicNameValuePair("sVehiculo", vehiculo));
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

        String resultServer = getHttpPost(url, params);
        System.out.println(resultServer);
        try {
            JSONArray jArray = new JSONArray(resultServer);
            final ArrayList<String> array = new ArrayList<>();
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                array.add(json.getString("cod_placa"));
                array.add(json.getString("nom_codigo"));
                total += Double.parseDouble(json.getString("val_unitario"));
                array.add(json.getString("val_unitario"));
                array.add(json.getString("fec_inicial"));
                array.add(json.getString("fec_final"));

                if (json.getString("fec_final").trim().equals("null")) {


                    Date hora1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(json.getString("fec_inicial").replace("-", "/"));
                    Date hora2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(json.getString("fec_inicial").replace("-", "/"));
                    long lantes = hora1.getTime();
                    long lahora = hora2.getTime();
                    long diferencia = (lahora - lantes);
                    array.add(String.valueOf(diferencia / (1000 * 60)));
                }else{


                    Date hora1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(json.getString("fec_inicial").replace("-", "/"));
                    Date hora2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(json.getString("fec_final").replace("-", "/"));
                    long lantes = hora1.getTime();
                    long lahora = hora2.getTime();
                    long diferencia = (lahora - lantes);
                    array.add(String.valueOf(diferencia / (1000 * 60)));
                }
            }

            rombos = new String[(array.size())];

            for (int i = 0; i < array.size(); i++) {
                rombos[i] = array.get(i).trim();
            }



            final GridView gridview = (GridView) findViewById(R.id.gridView7);// crear el
            // gridview a partir del elemento del xml gridview

            gridview.setAdapter(new CustomGridViewAdapter(getApplicationContext(), rombos, "informe"));// con setAdapter se llena


            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View v,
                                        final int position, long id) {

                }
            });

            String[] titulos = new String[]{"PACA","LAVADO","VALOR","HORA INI","HORA FIN","DIF"};

            final GridView gridviewTitulos = (GridView) findViewById(R.id.gridView10);// crear el
            // gridview a partir del elemento del xml gridview

            gridviewTitulos.setAdapter(new CustomGridViewAdapter(getApplicationContext(), titulos, "titulosInforme"));

            String[] totales = new String[]{"TOTAL","",String.valueOf(total),"","",""};

            final GridView gridviewTotales = (GridView) findViewById(R.id.gridView11);// crear el
            // gridview a partir del elemento del xml gridview

            gridviewTotales.setAdapter(new CustomGridViewAdapter(getApplicationContext(), totales, "titulosInforme"));

            /*

            String [] total = new String [] {Integer.toString(count)};

            final GridView gridviewTotal = (GridView) findViewById(R.id.gridView13);// crear el
            // gridview a partir del elemento del xml gridview

            gridviewTotal.setAdapter(new CustomGridViewAdapter(getApplicationContext(), total, "grd"));
*/


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void cancelar (View v){
        finish();

    }

    public void exportar (View v){
        CSVWriter writer = null;
        try
        {
            writer = new CSVWriter(new FileWriter(Environment.getExternalStorageDirectory().getPath() + "/myfile.csv"), ',');

            String[] entries = ("PLACA" + "#" + "LAVADO" + "#" + "VALOR" + "#" +
                    "HORA INICIAL" + "#" +  "HORA FINAL" + "#DIFERENCIA (MINS)").split("#");
            writer.writeNext(entries);
            int j = 0;
            for (int i = 0;i<(rombos.length/6);i++){

                if (rombos[j+4].trim().trim().equals("null")){
                    rombos[j+4] = rombos[j+3];
                }

                Date hora1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(rombos[j+3].replace("-","/"));
                Date hora2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(rombos[j+4].replace("-","/"));
                long lantes = hora1.getTime();
                long lahora = hora2.getTime();
                long diferencia = (lahora - lantes);

                entries = (rombos[j] + "#" + rombos[j+1] + "#" + rombos[j+2] + "#" +
                rombos[j+3] + "#" +  rombos[j+4] + "#" + String.valueOf(diferencia /(1000*60))).split("#");

                writer.writeNext(entries);
                j = j+6;
            }
            // array of your values

            writer.close();

            Toast.makeText(Informe.this, "Se ha exportado exitosamente!", Toast.LENGTH_SHORT).show();

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {""});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "INFORME");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "INFORME");
            File root = Environment.getExternalStorageDirectory();
            String pathToMyAttachedFile = "myfile.csv";
            File file = new File(root, pathToMyAttachedFile);
            if (!file.exists() || !file.canRead()) {
                return;
            }
            Uri uri = Uri.fromFile(file);
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        } catch (ParseException e) {
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
