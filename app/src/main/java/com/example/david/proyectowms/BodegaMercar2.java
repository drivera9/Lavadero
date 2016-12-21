package com.example.david.proyectowms;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import com.example.david.proyectowms.ListaExpandible;

public class BodegaMercar2 extends Activity {

    String Codigo = "";
    TableLayout tabla;
    TableLayout cabecera;
    TableRow.LayoutParams layoutFila;
    TableRow.LayoutParams layoutId;
    TableRow.LayoutParams layoutTexto;
    TableRow.LayoutParams layoutApellido;

    TableRow.LayoutParams layoutInd_estado;
    TableRow.LayoutParams layoutNum_item;
    TableRow.LayoutParams layoutCod_ubicacion;
    TableRow.LayoutParams layoutCan_pedida;
    TableRow.LayoutParams layoutCan_despachada;
    TableRow.LayoutParams layoutCod_codigo;
    TableRow.LayoutParams layoutNom_codigo;
    TableRow.LayoutParams layoutCan_existencia;
    TableRow.LayoutParams layoutCuenta;


    String procesoAnterior;
    String conexion = "";
    String user = "";
    String pass = "";
    String ip = "";
    String rombo = "";
    String proceso = "";
    String procesoAux = "";
    String procesOriginal = "";
    String sucursal = "";
    String tipo= "";
    String procesoSiguiente;
    ArrayList<String> mercado = new ArrayList();
    ExpandableListView lista ;
    ListaExpandible listaProductos;
    List<String>titulos;
    LinkedHashMap<String,ArrayList<String>> datos;
    String Titulo;

    private LinearLayout layout;
    private View Progress;

    private int MAX_FILAS = 10;
    String Id = "";
    Resources rs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_table_row);


        final Bundle bundle = this.getIntent().getExtras();
        user = bundle.getString("user");
        procesOriginal = bundle.getString("procesOriginal");
        pass = bundle.getString("pass");
        conexion = bundle.getString("conexion");
        procesoAnterior = bundle.getString("procesoAnterior");
        ip = bundle.getString("ip");
        proceso= bundle.getString("proceso");
        sucursal = bundle.getString("sucursal");
        rombo = bundle.getString("rombo");
        tipo = bundle.getString("tipo");
        Progress = findViewById(R.id.progressbar);
        layout = (LinearLayout) findViewById(R.id.LayoutLinearMercar);
        System.out.println(procesOriginal + " PROCESORIGINAL ");
        final EditText plu =(EditText) findViewById(R.id.PLU);

        plu.requestFocus();
        plu.setText(rombo.trim());
        lista = (ExpandableListView) findViewById(R.id.listaMercado);


        if (tipo.equals("mercar")){
            Titulo ="Mercar";
        }else{
            Titulo = "Entregar";
        }
        setTitle(Titulo);
        TextView titulo = (TextView) findViewById(R.id.textView_titulo);

        titulo.setText(Titulo);

        plu.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    insert(plu.getText().toString().trim());
                    //plu.setText("");
                    plu.requestFocus();
                    plu.setFocusable(true);
                    actualizar();
                }
                return false;
            }

        });

        actualizar();



        lista.setOnChildClickListener(new ExpandableListView.OnChildClickListener(){
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        titulos.get(groupPosition)
                                + " -> "
                                + datos.get(
                                titulos.get(groupPosition)).get(childPosition)
                        , Toast.LENGTH_SHORT
                )
                        .show();
                return false;
            }
        });


    }

    public void enter(View v){

        final EditText plu =(EditText) findViewById(R.id.PLU);

        plu.requestFocus();
        plu.setText(rombo.trim());
        insert(plu.getText().toString().trim());
        //plu.setText("");
        plu.requestFocus();
        plu.setFocusable(true);
        actualizar();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        showProgress(show, this, Progress, layout);
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

    public void actualizar(){

         mercado = new ArrayList();

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
        params.add(new BasicNameValuePair("sTipo", "mercar"));

        String resultServer  = getHttpPost(url,params);
        datos = new LinkedHashMap<>();

        try {
            JSONArray jArray = new JSONArray(resultServer);
            ArrayList<String> array = new ArrayList<>();
            array.add("num_rombo");
            array.add("ind_estado");
            array.add("num_item");
            array.add("cod_ubicacion");
            array.add("can_pedida");
            array.add("can_despachada");
            array.add("cod_codigo");
            array.add("nom_codigo");
            array.add("can_existencia");
            array.add("cuenta");
            for (int i = 0; i < jArray.length(); i++) {
                ArrayList<String> rowList = new ArrayList<>();
                JSONObject json = jArray.getJSONObject(i);
                rowList.add("Rombo : " + json.getString("num_rombo"));
                rowList.add("Estado : " + json.getString("ind_estado"));
                rowList.add("Item : " + json.getString("num_item"));
                rowList.add("Ubicacion : " + json.getString("cod_ubicacion_bodega"));
                rowList.add("Cant Pedida : " + json.getString("can_pedida"));
                rowList.add("Cant Despachada : " + json.getString("can_despachada"));
                rowList.add("Codigo : " + json.getString("cod_codigo"));
                Codigo =json.getString("cod_codigo");
                rowList.add("Desc : " + json.getString("nom_codigo"));
                rowList.add("Cant Existencia : " + json.getString("can_existencia"));
                rowList.add("Cuenta : " + json.getString("cuenta"));
                datos.put(  json.getString("cod_ubicacion_bodega").trim()   + " - " + "CODIGO : " + json.getString("cod_codigo").trim() + " - " + "R : " +  json.getString("num_rombo").trim() + "\n" + "________________________________________" + "\n" + json.getString("nom_codigo").trim(), rowList);
            }

            rs = this.getResources();
            tabla = (TableLayout)findViewById(R.id.tabla);
            cabecera = (TableLayout)findViewById(R.id.cabecera);
            //tabla.removeAllViews();
            //cabecera.removeAllViews();
            layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            layoutInd_estado = new TableRow.LayoutParams(200,100);
            layoutNum_item = new TableRow.LayoutParams(200,100);
            layoutCod_ubicacion = new TableRow.LayoutParams(200,100);
            layoutCan_pedida = new TableRow.LayoutParams(200,100);
            layoutCan_despachada = new TableRow.LayoutParams(200,100);
            layoutCod_codigo = new TableRow.LayoutParams(200,100);
            layoutNom_codigo = new TableRow.LayoutParams(200,100);
            layoutCan_existencia = new TableRow.LayoutParams(200,100);
            layoutCuenta = new TableRow.LayoutParams(200,100);
            titulos = new ArrayList<>(datos.keySet());

            listaProductos = new ListaExpandible(this,titulos , datos );
            lista.setAdapter(listaProductos);

        }catch (JSONException e ) {
            e.printStackTrace();
            finish();
        } catch (Exception e ){
            e.printStackTrace();
            finish();
        }
    }

    public void update(String codigo){
        String url = "http://" + ip + "/updateEstado.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();


        params.add(new BasicNameValuePair("sCodCodigo", codigo));
        params.add(new BasicNameValuePair("sCodProceso",proceso));

        String resultServer = getHttpPost(url, params);
        System.out.println(resultServer);
        JSONObject c;
        try {
            actualizar();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void insert(String codigo){
        EditText plu =(EditText) findViewById(R.id.PLU);
        if (tipo.equals("mercar")) {


            showProgress(true);
            String url2 = "http://" + ip + "/procesoSiguiente.php";

            List<NameValuePair> params2 = new ArrayList<NameValuePair>();
            params2.add(new BasicNameValuePair("sCodProceso",proceso));

            String resultServer2  = getHttpPost(url2,params2);
            System.out.println(resultServer2);
            ArrayList<String> array2 = new ArrayList<String>();
            ArrayList<String>  procesosantsigui = new ArrayList<>();
            try {

                JSONArray jArray2 = new JSONArray(resultServer2);

                for (int i = 0; i < jArray2.length(); i++) {
                    JSONObject json2 = jArray2.getJSONObject(i);
                    array2.add(json2.getString("cod_proceso_siguiente"));
                    array2.add(json2.getString("cod_proceso_anterior"));
                    procesoSiguiente = array2.get(0);
                }

                for (int i = 0;i<array2.size();i++){
                    procesosantsigui.add(array2.get(i));
                }

            }catch (JSONException e ){
                e.printStackTrace();
            }

            if (procesOriginal.equals("6")){
                procesoSiguiente = "6";
            }
            System.out.println(tipo);

            String url = "http://" + ip + "/insertEstado.php";

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            //proceso = procesosantsigui.get(0);
            params.add(new BasicNameValuePair("sCodCodigo", codigo.trim()));
            params.add(new BasicNameValuePair("sCodProcesoAnterior", procesoAnterior));
            params.add(new BasicNameValuePair("sNumRombo", rombo));
            params.add(new BasicNameValuePair("sCodProceso", procesoSiguiente));
            params.add(new BasicNameValuePair("sCodSucursal", sucursal));


            String resultServer = getHttpPost(url, params);
            System.out.println(resultServer);
            JSONObject c;
            try {
                //actualizar();
                showProgress(false);
                //plu.requestFocus();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                finish();
            }
        }else{
            if (tipo.equals("entrega")) {

                showProgress(true);
                String url2 = "http://" + ip + "/procesoSiguiente.php";

                List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                params2.add(new BasicNameValuePair("sCodProceso", proceso));

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
                    }

                    for (int i = 0; i < array2.size(); i++) {
                        procesosantsigui.add(array2.get(i));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                procesoAux = array2.get(0);
                String url = "http://" + ip + "/insertEstado.php";

                List<NameValuePair> params = new ArrayList<NameValuePair>();


                params.add(new BasicNameValuePair("sCodCodigo", codigo.trim()));
                params.add(new BasicNameValuePair("sCodProcesoAnterior", procesoAnterior));
                params.add(new BasicNameValuePair("sCodProceso", procesoAux.trim()));
                params.add(new BasicNameValuePair("sNumRombo", rombo));
                params.add(new BasicNameValuePair("sCodSucursal", sucursal));

                String resultServer = getHttpPost(url, params);
                System.out.println(resultServer);
                JSONObject c;
                try {
                    //actualizar();
                    showProgress(false);
                    //plu.requestFocus();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    finish();
                }
            }
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



    public void agregarFilasTabla(){

        TableRow fila;
        TextView txtInd_estado;
        TextView txtNum_item;
        TextView txtCod_ubicacion;
        TextView txtCan_pedida;
        TextView txtCan_despachada;
        TextView txtCod_codigo;
        TextView txtNom_codigo;
        TextView txtCan_existencia;
        TextView txtCuenta;


        //System.out.println(mercado.get(9) + "MERCADOOOOOOOOOOOOO");
        /*if (mercado.get(9).equals("")){
            finish();
        }*/
        for(int i = 0;i<mercado.size();i= i+9){


            fila = new TableRow(this);
            fila.setLayoutParams(layoutFila);

            txtInd_estado = new TextView(this);
            txtNum_item = new TextView(this);
            txtCod_ubicacion = new TextView(this);
            txtCan_pedida = new TextView(this);
            txtCan_despachada = new TextView(this);
            txtCod_codigo = new TextView(this);
            txtNom_codigo = new TextView(this);
            txtCan_existencia = new TextView(this);
            txtCuenta= new TextView(this);

            txtInd_estado.setText(mercado.get(i));
            txtInd_estado.setGravity(Gravity.CENTER_HORIZONTAL);
            txtInd_estado.setBackgroundResource(R.drawable.cell_shape);
            txtInd_estado.setLayoutParams(layoutInd_estado);
            txtInd_estado.setTextColor(getResources().getColor(R.color.METRO_blue));

            txtNum_item.setText(mercado.get(i + 1));
            txtNum_item.setGravity(Gravity.CENTER_HORIZONTAL);
            txtNum_item.setBackgroundResource(R.drawable.cell_shape);
            txtNum_item.setLayoutParams(layoutNum_item);
            txtNum_item.setTextColor(getResources().getColor(R.color.METRO_blue));

            txtCod_ubicacion.setText(mercado.get(i + 2));
            txtCod_ubicacion.setGravity(Gravity.CENTER_HORIZONTAL);
            txtCod_ubicacion.setBackgroundResource(R.drawable.cell_shape);
            txtCod_ubicacion.setLayoutParams(layoutCod_ubicacion);
            txtCod_ubicacion.setTextColor(getResources().getColor(R.color.METRO_blue));

            txtCan_pedida.setText(mercado.get(i + 3));
            txtCan_pedida.setGravity(Gravity.CENTER_HORIZONTAL);
            txtCan_pedida.setBackgroundResource(R.drawable.cell_shape);
            txtCan_pedida.setLayoutParams(layoutCan_pedida);
            txtCan_pedida.setTextColor(getResources().getColor(R.color.METRO_blue));

            txtCan_despachada.setText(mercado.get(i + 4));
            txtCan_despachada.setGravity(Gravity.CENTER_HORIZONTAL);
            txtCan_despachada.setBackgroundResource(R.drawable.cell_shape);
            txtCan_despachada.setLayoutParams(layoutCan_despachada);
            txtCan_despachada.setTextColor(getResources().getColor(R.color.METRO_blue));

            txtCod_codigo.setText(mercado.get(i + 5));
            txtCod_codigo.setGravity(Gravity.CENTER_HORIZONTAL);
            txtCod_codigo.setBackgroundResource(R.drawable.cell_shape);
            txtCod_codigo.setLayoutParams(layoutCod_codigo);
            txtCod_codigo.setTextColor(getResources().getColor(R.color.METRO_blue));

            txtNom_codigo.setText(mercado.get(i + 6));
            txtNom_codigo.setGravity(Gravity.CENTER_HORIZONTAL);
            txtNom_codigo.setBackgroundResource(R.drawable.cell_shape);
            txtNom_codigo.setLayoutParams(layoutNom_codigo);
            txtNom_codigo.setTextColor(getResources().getColor(R.color.METRO_blue));

            txtCan_existencia.setText(mercado.get(i + 7));
            txtCan_existencia.setGravity(Gravity.CENTER_HORIZONTAL);
            txtCan_existencia.setBackgroundResource(R.drawable.cell_shape);
            txtCan_existencia.setLayoutParams(layoutCan_existencia);
            txtCan_existencia.setTextColor(getResources().getColor(R.color.METRO_blue));

            txtCuenta.setText(mercado.get(i + 8));
            txtCuenta.setGravity(Gravity.CENTER_HORIZONTAL);
            txtCuenta.setBackgroundResource(R.drawable.cell_shape);
            txtCuenta.setLayoutParams(layoutCuenta);
            txtCuenta.setTextColor(getResources().getColor(R.color.METRO_blue));


            if(i<9){
                txtInd_estado.setBackgroundResource(R.drawable.cell_shape_titulo);
                txtNum_item.setBackgroundResource(R.drawable.cell_shape_titulo);
                txtCod_ubicacion.setBackgroundResource(R.drawable.cell_shape_titulo);
                txtCan_pedida.setBackgroundResource(R.drawable.cell_shape_titulo);
                txtCan_despachada.setBackgroundResource(R.drawable.cell_shape_titulo);
                txtCod_codigo.setBackgroundResource(R.drawable.cell_shape_titulo);
                txtNom_codigo.setBackgroundResource(R.drawable.cell_shape_titulo);
                txtCan_existencia.setBackgroundResource(R.drawable.cell_shape_titulo);
                txtCuenta.setBackgroundResource(R.drawable.cell_shape_titulo);

            }


            fila.addView(txtInd_estado);
            fila.addView(txtNum_item);
            fila.addView(txtCod_ubicacion);
            fila.addView(txtCan_pedida);
            fila.addView(txtCan_despachada);
            fila.addView(txtCod_codigo);
            fila.addView(txtNom_codigo);
            fila.addView(txtCan_existencia);
            fila.addView(txtCuenta);
            tabla.addView(fila);


        }
    }

    public void salir(View v){
        finish();
    }
}
