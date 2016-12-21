package com.example.david.proyectowms;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import com.example.david.proyectowms.ListaExpandible;

public class Traslado extends Activity {

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
    String numero;
    String cantidad;
    ProgressDialog mProgressDialog;
    String codigo;
    String cant;
    TextView error;

    private LinearLayout layout;
    private View Progress;

    private int MAX_FILAS = 10;
    String Id = "";
    Resources rs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_table_row);

        error = (TextView) findViewById(R.id.textError);

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
        numero = bundle.getString("numero");
        Progress = findViewById(R.id.progressbar);
        layout = (LinearLayout) findViewById(R.id.LayoutLinearMercar);
        System.out.println(procesOriginal + " PROCESORIGINAL ");
        final EditText plu =(EditText) findViewById(R.id.PLU);

        mProgressDialog= new ProgressDialog(Traslado.this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Guardando datos...");

        plu.requestFocus();
        plu.setText(rombo.trim());
        lista = (ExpandableListView) findViewById(R.id.listaMercado);

        setTitle("Traslado");
        final TextView titulo = (TextView) findViewById(R.id.textView_titulo);

        titulo.setText("Traslado");

        plu.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String Cant = null;

                    for (int i = 0;i<titulos.size();i++) {
                        if (datos.get(titulos.get(i)).get(6).trim().equals("Codigo : " + plu.getText().toString().trim())){
                            Cant = datos.get(titulos.get(i)).get(9).trim();
                        }
                    }

                    error.setText("");

                    try {
                        codigo = plu.getText().toString().trim();
                        cant = Cant.substring(Cant.length()-1);
                        new GuardarDocumento().execute("");

                    }catch (Exception e){
                        e.printStackTrace();
                        error.setText("Verifique el codigo !");
                    }

                    plu.setText("");
                    plu.requestFocus();
                    plu.setFocusable(true);
                    actualizar(tipo);
                }
                return false;
            }

        });

        actualizar(tipo);



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

    public void actualizar(String tipo){

        if (tipo.trim().equals("mercar")) {

            mercado = new ArrayList();

            String url = "http://" + ip + "/consultarDetalle.php";

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("sIp", ip));
            params.add(new BasicNameValuePair("sUser", user));
            params.add(new BasicNameValuePair("sPass", pass));
            params.add(new BasicNameValuePair("sBd", "openbravo"));
            params.add(new BasicNameValuePair("sCodProceso", proceso));
            params.add(new BasicNameValuePair("sCodEmpresa", "METRO"));
            params.add(new BasicNameValuePair("sCodSucursal", sucursal));
            params.add(new BasicNameValuePair("sNumRombo", rombo));
            params.add(new BasicNameValuePair("sTipoConsulta", "mercar"));
            params.add(new BasicNameValuePair("sTipo", "mercar"));

            String resultServer = getHttpPost(url, params);
            System.out.println(resultServer);
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
                    cantidad = json.getString("can_pedida");
                    rowList.add("Cant Despachada : " + json.getString("can_despachada"));
                    rowList.add("Codigo : " + json.getString("cod_codigo"));
                    Codigo = json.getString("cod_codigo");
                    rowList.add("Desc : " + json.getString("nom_codigo"));
                    rowList.add("Cant Existencia : " + json.getString("can_existencia"));
                    rowList.add("Cuenta : " + json.getString("cuenta"));
                    datos.put(json.getString("cod_ubicacion_bodega").trim() + " - " + "CODIGO : " + json.getString("cod_codigo").trim() + " - " + "R : " + json.getString("num_rombo").trim() + "\n" + "________________________________________" + "\n" + json.getString("nom_codigo").trim(), rowList);
                }

                rs = this.getResources();
                tabla = (TableLayout) findViewById(R.id.tabla);
                cabecera = (TableLayout) findViewById(R.id.cabecera);
                //tabla.removeAllViews();
                //cabecera.removeAllViews();
                layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                layoutInd_estado = new TableRow.LayoutParams(200, 100);
                layoutNum_item = new TableRow.LayoutParams(200, 100);
                layoutCod_ubicacion = new TableRow.LayoutParams(200, 100);
                layoutCan_pedida = new TableRow.LayoutParams(200, 100);
                layoutCan_despachada = new TableRow.LayoutParams(200, 100);
                layoutCod_codigo = new TableRow.LayoutParams(200, 100);
                layoutNom_codigo = new TableRow.LayoutParams(200, 100);
                layoutCan_existencia = new TableRow.LayoutParams(200, 100);
                layoutCuenta = new TableRow.LayoutParams(200, 100);
                titulos = new ArrayList<>(datos.keySet());

                listaProductos = new ListaExpandible(this, titulos, datos);
                lista.setAdapter(listaProductos);

            } catch (JSONException e) {
                e.printStackTrace();
                String url11 = "http://" + ip + "/guardarMovimiento.php";

                List<NameValuePair> params11 = new ArrayList<NameValuePair>();

                params11.add(new BasicNameValuePair("sParametro","aumentarTrotConsecutivo"));

                String resultServer11 = getHttpPost(url11, params11);
                System.out.println(resultServer11);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
                finish();
            }
        }else{
            mercado = new ArrayList();

            String url = "http://" + ip + "/consultarDetalle.php";

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("sIp", ip));
            params.add(new BasicNameValuePair("sUser", user));
            params.add(new BasicNameValuePair("sPass", pass));
            params.add(new BasicNameValuePair("sBd", "openbravo"));
            params.add(new BasicNameValuePair("sCodProceso", proceso));
            params.add(new BasicNameValuePair("sCodEmpresa", "METRO"));
            params.add(new BasicNameValuePair("sCodSucursal", sucursal));
            params.add(new BasicNameValuePair("sNumRombo", rombo));
            params.add(new BasicNameValuePair("sTipoConsulta", "mercar"));
            params.add(new BasicNameValuePair("sTipo", "mercar"));

            String resultServer = getHttpPost(url, params);
            System.out.println(resultServer);
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
                    cantidad = json.getString("can_pedida");
                    rowList.add("Cant Despachada : " + json.getString("can_despachada"));
                    rowList.add("Codigo : " + json.getString("cod_codigo"));
                    Codigo = json.getString("cod_codigo");
                    rowList.add("Desc : " + json.getString("nom_codigo"));
                    rowList.add("Cant Existencia : " + json.getString("can_existencia"));
                    rowList.add("Cuenta : " + json.getString("cuenta"));
                    datos.put(json.getString("cod_ubicacion_bodega").trim() + " - " + "CODIGO : " + json.getString("cod_codigo").trim() + " - " + "R : " + json.getString("num_rombo").trim() + "\n" + "________________________________________" + "\n" + json.getString("nom_codigo").trim(), rowList);
                }

                rs = this.getResources();
                tabla = (TableLayout) findViewById(R.id.tabla);
                cabecera = (TableLayout) findViewById(R.id.cabecera);
                //tabla.removeAllViews();
                //cabecera.removeAllViews();
                layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                layoutInd_estado = new TableRow.LayoutParams(200, 100);
                layoutNum_item = new TableRow.LayoutParams(200, 100);
                layoutCod_ubicacion = new TableRow.LayoutParams(200, 100);
                layoutCan_pedida = new TableRow.LayoutParams(200, 100);
                layoutCan_despachada = new TableRow.LayoutParams(200, 100);
                layoutCod_codigo = new TableRow.LayoutParams(200, 100);
                layoutNom_codigo = new TableRow.LayoutParams(200, 100);
                layoutCan_existencia = new TableRow.LayoutParams(200, 100);
                layoutCuenta = new TableRow.LayoutParams(200, 100);
                titulos = new ArrayList<>(datos.keySet());

                listaProductos = new ListaExpandible(this, titulos, datos);
                lista.setAdapter(listaProductos);

            } catch (JSONException e) {
                e.printStackTrace();
                finish();
            } catch (Exception e) {
                e.printStackTrace();
                finish();
            }
        }
    }

    class GuardarDocumento extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute(){
            mProgressDialog.show();

        }
        @Override
        protected String doInBackground(String... f_url) {

            String consecutivo = "";

            String url6 = "http://" + ip + "/consultarGeneral.php";

            List<NameValuePair> params6 = new ArrayList<NameValuePair>();
            params6.add(new BasicNameValuePair("sConsecutivo", "TROT"));
            params6.add(new BasicNameValuePair("sParametro", "TROT"));

            String resultServer6  = getHttpPost(url6,params6);
            System.out.println("---------------------------------resultserver----------------");

            try {

                JSONArray jArray = new JSONArray(resultServer6);
                ArrayList<String> array = new ArrayList<String>();
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json = jArray.getJSONObject(i);
                    array.add(json.getString("siguiente"));
                }

                consecutivo = array.get(0);

            }catch (JSONException e ){
                e.printStackTrace();
            }

            String bodega = "";

            if (sucursal.trim().equals("51")){
                bodega = "1";
            }else{
                if (sucursal.trim().equals("33")){
                    bodega = "2";
                }else{
                    if (sucursal.trim().equals("10")){
                        bodega = "3";
                    }else{
                        if (sucursal.trim().equals("99")){
                            bodega = "11";
                        }
                    }
                }
            }

            String url5 = "http://" + ip + "/consultarGeneral.php";

            List<NameValuePair> params5 = new ArrayList<NameValuePair>();
            params5.add(new BasicNameValuePair("sNumero",numero ));
            params5.add(new BasicNameValuePair("sBodega",bodega ));
            params5.add(new BasicNameValuePair("sParametro", "trasladoNumeroOrden"));

            String resultServer5  = getHttpPost(url5,params5);
            System.out.println("---------------------------------resultserver----------------");

            String nit = "";
            String vendedor = "";
            String usuario = "";

            try {

                JSONArray jArray = new JSONArray(resultServer5);
                ArrayList<String> array = new ArrayList<String>();
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json = jArray.getJSONObject(i);
                    nit = json.getString("nit");
                    vendedor = json.getString("vendedor");
                    usuario = json.getString("usuario");
                }

            }catch (JSONException e ){
                e.printStackTrace();
            }

            if (tipo.equals("mercar")) {

                String url12 = "http://" + ip + "/consultarGeneral.php";

                List<NameValuePair> params12 = new ArrayList<NameValuePair>();
                params12.add(new BasicNameValuePair("sConsecutivo", consecutivo));
                params12.add(new BasicNameValuePair("sParametro", "trasladoSeq"));

                String resultServer12  = getHttpPost(url12,params12);
                System.out.println("---------------------------------resultserver----------------");

                String max = "0";

                try {

                    JSONArray jArray = new JSONArray(resultServer12);
                    ArrayList<String> array = new ArrayList<String>();
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json = jArray.getJSONObject(i);
                        max = json.getString("seq");
                    }

                }catch (JSONException e ){
                    e.printStackTrace();
                }

                if (max.equals("null")){
                    max = "0";
                }

                String url7 = "http://" + ip + "/guardarMovimiento.php";

                List<NameValuePair> params7 = new ArrayList<NameValuePair>();
                params7.add(new BasicNameValuePair("sSw","16" ));
                params7.add(new BasicNameValuePair("sTipo", "TROT"));
                params7.add(new BasicNameValuePair("sNumero",consecutivo ));
                params7.add(new BasicNameValuePair("sNit", nit));
                params7.add(new BasicNameValuePair("sFecha", "GETDATE()"));
                params7.add(new BasicNameValuePair("sCondicion", "0"));//?
                params7.add(new BasicNameValuePair("sVencimiento", "GETDATE()"));
                params7.add(new BasicNameValuePair("sValorTotal","0"));//?
                params7.add(new BasicNameValuePair("sIva", "0"));//?
                params7.add(new BasicNameValuePair("sRetencion","0" ));
                params7.add(new BasicNameValuePair("sRetencionCausada", "0" ));
                params7.add(new BasicNameValuePair("sRetencionIva","0"  ));
                params7.add(new BasicNameValuePair("sRetencionIca","0"  ));
                params7.add(new BasicNameValuePair("sDescuentoPie", "0"));//?
                params7.add(new BasicNameValuePair("sFletes", "0" ));
                params7.add(new BasicNameValuePair("sIvaFletes", "0" ));
                params7.add(new BasicNameValuePair("sCosto", "" ));
                params7.add(new BasicNameValuePair("sVendedor",vendedor ));
                params7.add(new BasicNameValuePair("sValorAplicado","0" ));//?
                params7.add(new BasicNameValuePair("sAnulado","0" ));
                params7.add(new BasicNameValuePair("sModelo","*" ));
                params7.add(new BasicNameValuePair("sDocumento","0" ));//?
                params7.add(new BasicNameValuePair("sNotas", "0"));//?
                params7.add(new BasicNameValuePair("sUsuario", usuario));
                params7.add(new BasicNameValuePair("sFechaHora","GETDATE()" ));
                params7.add(new BasicNameValuePair("sPc", "0"));//?
                params7.add(new BasicNameValuePair("sRetencion2","0" ));
                params7.add(new BasicNameValuePair("sRetencion3","0" ));
                params7.add(new BasicNameValuePair("sBodega", "5"));
                params7.add(new BasicNameValuePair("sImpoConsumo","0" ));
                params7.add(new BasicNameValuePair("sDescuento2", "0" ));
                params7.add(new BasicNameValuePair("sDuracion","0" ));//?
                params7.add(new BasicNameValuePair("sConcepto","0" ));
                params7.add(new BasicNameValuePair("sVencimientoPresup","" ));
                params7.add(new BasicNameValuePair("sExportado","" ));
                params7.add(new BasicNameValuePair("sImpuestoDeporte", "0" ));
                params7.add(new BasicNameValuePair("sPrefijo","" ));
                params7.add(new BasicNameValuePair("sMoneda", ""));
                params7.add(new BasicNameValuePair("sTasa", ""));
                params7.add(new BasicNameValuePair("sValorMercancia","0" ));//?
                params7.add(new BasicNameValuePair("sNumeroCuotas","0" ));
                params7.add(new BasicNameValuePair("sCentroDoc","" ));
                params7.add(new BasicNameValuePair("sCodigoDireccion","0" ));
                params7.add(new BasicNameValuePair("sDescuento1","" ));
                params7.add(new BasicNameValuePair("sDescuento2", ""));
                params7.add(new BasicNameValuePair("sDescuento3","" ));
                params7.add(new BasicNameValuePair("sAbono","0" ));
                params7.add(new BasicNameValuePair("sParametro", "traslado"));

                String resultServer7 = getHttpPost(url7, params7);
                System.out.println(resultServer7);

                String url4 = "http://" + ip + "/guardarMovimiento.php";

                List<NameValuePair> params4 = new ArrayList<NameValuePair>();
                params4.add(new BasicNameValuePair("sSw","16" ));
                params4.add(new BasicNameValuePair("sTipo", "TROT"));
                params4.add(new BasicNameValuePair("sNumero",consecutivo ));
                params4.add(new BasicNameValuePair("sCodigo", codigo.trim()));
                params4.add(new BasicNameValuePair("sSeq", String.valueOf(Integer.parseInt(max.trim())+1)));
                params4.add(new BasicNameValuePair("sFec", "GETDATE()"));
                params4.add(new BasicNameValuePair("sNit", nit));
                params4.add(new BasicNameValuePair("sCantidad",cant));
                params4.add(new BasicNameValuePair("sPorcentajeIva", "16"));
                params4.add(new BasicNameValuePair("sValorUnitario","0" ));//?
                params4.add(new BasicNameValuePair("sPorcentajeDescuento", "0" ));
                params4.add(new BasicNameValuePair("sCostoUnitario","0"  ));//?
                params4.add(new BasicNameValuePair("sCostoUnitarioNiif","0"  ));
                params4.add(new BasicNameValuePair("sAdicional", ""));
                params4.add(new BasicNameValuePair("sVendedor", nit ));
                params4.add(new BasicNameValuePair("sBodega", "5" ));
                params4.add(new BasicNameValuePair("sUnd", "UND" ));
                params4.add(new BasicNameValuePair("sCantidadUnd","1" ));
                params4.add(new BasicNameValuePair("sCantidadPedida","1" ));
                params4.add(new BasicNameValuePair("sManejaInventario","S" ));
                params4.add(new BasicNameValuePair("sCostoUnitarioSin","0" ));
                params4.add(new BasicNameValuePair("sPedido","0" ));//?
                params4.add(new BasicNameValuePair("sLote", ""));
                params4.add(new BasicNameValuePair("sCantidadOtraUnd", "0"));
                params4.add(new BasicNameValuePair("sTelefono","" ));
                params4.add(new BasicNameValuePair("sTipoOp", ""));
                params4.add(new BasicNameValuePair("sNumeroOp","" ));
                params4.add(new BasicNameValuePair("sLoteVcmto","" ));
                params4.add(new BasicNameValuePair("sTipoLink", ""));
                params4.add(new BasicNameValuePair("sNumeroLink","" ));
                params4.add(new BasicNameValuePair("sSeqLink", "" ));
                params4.add(new BasicNameValuePair("sCantidadDos","" ));
                params4.add(new BasicNameValuePair("sPorcDcto2","0" ));
                params4.add(new BasicNameValuePair("sPorcDcto3","0" ));
                params4.add(new BasicNameValuePair("sParametro", "trasladoDetalle"));
                String resultServer4 = getHttpPost(url4, params4);
                System.out.println(resultServer4);

                String url3 = "http://" + ip + "/guardarMovimiento.php";



                List<NameValuePair> params3 = new ArrayList<NameValuePair>();
                params3.add(new BasicNameValuePair("sSw","12" ));
                params3.add(new BasicNameValuePair("sTipo", "TROT"));
                params3.add(new BasicNameValuePair("sNumero",consecutivo ));
                params3.add(new BasicNameValuePair("sCodigo", codigo.trim()));
                params3.add(new BasicNameValuePair("sSeq", String.valueOf(Integer.parseInt(max.trim())+2)));
                params3.add(new BasicNameValuePair("sFec", "GETDATE()"));
                params3.add(new BasicNameValuePair("sNit", nit));
                params3.add(new BasicNameValuePair("sCantidad",cant));
                params3.add(new BasicNameValuePair("sPorcentajeIva", "16"));
                params3.add(new BasicNameValuePair("sValorUnitario","0" ));//?
                params3.add(new BasicNameValuePair("sPorcentajeDescuento", "0" ));
                params3.add(new BasicNameValuePair("sCostoUnitario","0"  ));//?
                params3.add(new BasicNameValuePair("sCostoUnitarioNiif","0"  ));
                params3.add(new BasicNameValuePair("sAdicional", ""));
                params3.add(new BasicNameValuePair("sVendedor", nit ));
                params3.add(new BasicNameValuePair("sBodega", "99" ));
                params3.add(new BasicNameValuePair("sUnd", "UND" ));
                params3.add(new BasicNameValuePair("sCantidadUnd","1" ));
                params3.add(new BasicNameValuePair("sCantidadPedida","1" ));
                params3.add(new BasicNameValuePair("sManejaInventario","S" ));
                params3.add(new BasicNameValuePair("sCostoUnitarioSin","0" ));
                params3.add(new BasicNameValuePair("sPedido","0" ));//?
                params3.add(new BasicNameValuePair("sLote", ""));
                params3.add(new BasicNameValuePair("sCantidadOtraUnd", "0"));
                params3.add(new BasicNameValuePair("sTelefono","" ));
                params3.add(new BasicNameValuePair("sTipoOp", ""));
                params3.add(new BasicNameValuePair("sNumeroOp","" ));
                params3.add(new BasicNameValuePair("sLoteVcmto","" ));
                params3.add(new BasicNameValuePair("sTipoLink", ""));
                params3.add(new BasicNameValuePair("sNumeroLink","" ));
                params3.add(new BasicNameValuePair("sSeqLink", "" ));
                params3.add(new BasicNameValuePair("sCantidadDos","" ));
                params3.add(new BasicNameValuePair("sPorcDcto2","0" ));
                params3.add(new BasicNameValuePair("sPorcDcto3","0" ));
                params3.add(new BasicNameValuePair("sParametro", "trasladoDetalle"));
                String resultServer3 = getHttpPost(url3, params3);
                System.out.println(resultServer3);

                Calendar c1 = Calendar.getInstance();

                String url8 = "http://" + ip + "/consultarGeneral.php";

                List<NameValuePair> params8 = new ArrayList<NameValuePair>();
                params8.add(new BasicNameValuePair("sCodigo",codigo.trim() ));
                params8.add(new BasicNameValuePair("sAno", Integer.toString(c1.get(Calendar.YEAR))));
                params8.add(new BasicNameValuePair("sMes",Integer.toString(c1.get(Calendar.MONTH)+1)));
                //params8.add(new BasicNameValuePair("sMes","7"));
                //params8.add(new BasicNameValuePair("sAno","2016"));
                params8.add(new BasicNameValuePair("sBodega", "5"));
                params8.add(new BasicNameValuePair("sParametro", "trasladoRefStoConsultar"));

                String resultServer8  = getHttpPost(url8,params8);

                System.out.println("---------------------------------resultserver----------------");
                System.out.println(resultServer8);
                String cuenta = "0";
                String valorCodigo = "";
                try {

                    JSONArray jArray = new JSONArray(resultServer8);
                    ArrayList<String> array = new ArrayList<String>();
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json = jArray.getJSONObject(i);
                        cuenta = json.getString("cuenta");
                        valorCodigo = json.getString("valor");
                    }

                }catch (JSONException e ){
                    e.printStackTrace();
                }

                //if (Integer.parseInt(cuenta) > 1){


                    String url10 = "http://" + ip + "/guardarMovimiento.php";

                    List<NameValuePair> params10 = new ArrayList<NameValuePair>();
                    params10.add(new BasicNameValuePair("sParametro","trasladoDetalleStoSal" ));
                    params10.add(new BasicNameValuePair("sCant","1" ));
                    params10.add(new BasicNameValuePair("sCost",  valorCodigo));
                    params10.add(new BasicNameValuePair("sBodega","5" ));
                    params10.add(new BasicNameValuePair("sCodigo", codigo.trim()));
                    params10.add(new BasicNameValuePair("sAno", Integer.toString(c1.get(Calendar.YEAR))));
                    params10.add(new BasicNameValuePair("sMes",Integer.toString(c1.get(Calendar.MONTH)+1)));
                    String resultServer10 = getHttpPost(url10, params10);
                    System.out.println(resultServer10);


                    String url11 = "http://" + ip + "/guardarMovimiento.php";

                    List<NameValuePair> params11 = new ArrayList<NameValuePair>();
                    params11.add(new BasicNameValuePair("sParametro","trasladoDetalleStoEnt" ));
                    params11.add(new BasicNameValuePair("sCant","1" ));
                    params11.add(new BasicNameValuePair("sCost", valorCodigo));
                    params11.add(new BasicNameValuePair("sBodega","5" ));
                    params11.add(new BasicNameValuePair("sCodigo", codigo.trim()));
                    params11.add(new BasicNameValuePair("sAno", Integer.toString(c1.get(Calendar.YEAR))));
                    params11.add(new BasicNameValuePair("sMes",Integer.toString(c1.get(Calendar.MONTH)+1)));
                    String resultServer11 = getHttpPost(url11, params11);
                    System.out.println(resultServer11);

                    /*String url12 = "http://" + ip + "/guardarMovimiento.php";

                    List<NameValuePair> params12 = new ArrayList<NameValuePair>();
                    params12.add(new BasicNameValuePair("sParametro","trasladoDetalleStoInsert" ));
                    params12.add(new BasicNameValuePair("sCant",cant ));
                    params12.add(new BasicNameValuePair("sCost",valorCodigo ));
                    params12.add(new BasicNameValuePair("sBodega","5" ));
                    params12.add(new BasicNameValuePair("sCodigo", codigo.trim()));
                    params12.add(new BasicNameValuePair("sAno", Integer.toString(c1.get(Calendar.YEAR))));
                    params12.add(new BasicNameValuePair("sMes",Integer.toString(c1.get(Calendar.MONTH))));
                    String resultServer12 = getHttpPost(url12, params12);
                    System.out.println(resultServer12);*/


                //}

                //Toast.makeText(Traslado.this, "Año " + c1.get(Calendar.YEAR) + "Mes " + c1.get(Calendar.MONTH), Toast.LENGTH_SHORT).show();

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

                    //showProgress(true);
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

            return null;

        }

        // Once Music File is downloaded
        @Override
        protected void onPostExecute(String file_url) {

            mProgressDialog.dismiss();
            actualizar(tipo);
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
            actualizar(tipo);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void insert(String codigo, String cant){
        EditText plu =(EditText) findViewById(R.id.PLU);

        String consecutivo = "";

        String url6 = "http://" + ip + "/consultarGeneral.php";

        List<NameValuePair> params6 = new ArrayList<NameValuePair>();
        params6.add(new BasicNameValuePair("sConsecutivo", "TROT"));
        params6.add(new BasicNameValuePair("sParametro", "TROT"));

        String resultServer6  = getHttpPost(url6,params6);
        System.out.println("---------------------------------resultserver----------------");

        try {

            JSONArray jArray = new JSONArray(resultServer6);
            ArrayList<String> array = new ArrayList<String>();
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                array.add(json.getString("siguiente"));
            }

            consecutivo = array.get(0);

        }catch (JSONException e ){
            e.printStackTrace();
        }

        String bodega = "";

        if (sucursal.trim().equals("51")){
            bodega = "1";
        }else{
            if (sucursal.trim().equals("33")){
                bodega = "2";
            }else{
                if (sucursal.trim().equals("10")){
                    bodega = "3";
                }else{
                    if (sucursal.trim().equals("99")){
                        bodega = "11";
                    }
                }
            }
        }

        String url5 = "http://" + ip + "/consultarGeneral.php";

        List<NameValuePair> params5 = new ArrayList<NameValuePair>();
        params5.add(new BasicNameValuePair("sNumero",numero ));
        params5.add(new BasicNameValuePair("sBodega",bodega ));
        params5.add(new BasicNameValuePair("sParametro", "trasladoNumeroOrden"));

        String resultServer5  = getHttpPost(url5,params5);
        System.out.println("---------------------------------resultserver----------------");

        String nit = "";
        String vendedor = "";
        String usuario = "";

        try {

            JSONArray jArray = new JSONArray(resultServer5);
            ArrayList<String> array = new ArrayList<String>();
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                nit = json.getString("nit");
                vendedor = json.getString("vendedor");
                usuario = json.getString("usuario");
            }

        }catch (JSONException e ){
            e.printStackTrace();
        }

        if (tipo.equals("mercar")) {

            String url7 = "http://" + ip + "/guardarMovimiento.php";

            List<NameValuePair> params7 = new ArrayList<NameValuePair>();
            params7.add(new BasicNameValuePair("sSw","16" ));
            params7.add(new BasicNameValuePair("sTipo", "TROT"));
            params7.add(new BasicNameValuePair("sNumero",consecutivo ));
            params7.add(new BasicNameValuePair("sNit", nit));
            params7.add(new BasicNameValuePair("sFecha", "GETDATE()"));
            params7.add(new BasicNameValuePair("sCondicion", "0"));//?
            params7.add(new BasicNameValuePair("sVencimiento", "GETDATE()"));
            params7.add(new BasicNameValuePair("sValorTotal","0"));//?
            params7.add(new BasicNameValuePair("sIva", "0"));//?
            params7.add(new BasicNameValuePair("sRetencion","0" ));
            params7.add(new BasicNameValuePair("sRetencionCausada", "0" ));
            params7.add(new BasicNameValuePair("sRetencionIva","0"  ));
            params7.add(new BasicNameValuePair("sRetencionIca","0"  ));
            params7.add(new BasicNameValuePair("sDescuentoPie", "0"));//?
            params7.add(new BasicNameValuePair("sFletes", "0" ));
            params7.add(new BasicNameValuePair("sIvaFletes", "0" ));
            params7.add(new BasicNameValuePair("sCosto", "" ));
            params7.add(new BasicNameValuePair("sVendedor",vendedor ));
            params7.add(new BasicNameValuePair("sValorAplicado","0" ));//?
            params7.add(new BasicNameValuePair("sAnulado","0" ));
            params7.add(new BasicNameValuePair("sModelo","*" ));
            params7.add(new BasicNameValuePair("sDocumento","0" ));//?
            params7.add(new BasicNameValuePair("sNotas", "0"));//?
            params7.add(new BasicNameValuePair("sUsuario", usuario));
            params7.add(new BasicNameValuePair("sFechaHora","GETDATE()" ));
            params7.add(new BasicNameValuePair("sPc", "0"));//?
            params7.add(new BasicNameValuePair("sRetencion2","0" ));
            params7.add(new BasicNameValuePair("sRetencion3","0" ));
            params7.add(new BasicNameValuePair("sBodega", "5"));
            params7.add(new BasicNameValuePair("sImpoConsumo","0" ));
            params7.add(new BasicNameValuePair("sDescuento2", "0" ));
            params7.add(new BasicNameValuePair("sDuracion","0" ));//?
            params7.add(new BasicNameValuePair("sConcepto","0" ));
            params7.add(new BasicNameValuePair("sVencimientoPresup","" ));
            params7.add(new BasicNameValuePair("sExportado","" ));
            params7.add(new BasicNameValuePair("sImpuestoDeporte", "0" ));
            params7.add(new BasicNameValuePair("sPrefijo","" ));
            params7.add(new BasicNameValuePair("sMoneda", ""));
            params7.add(new BasicNameValuePair("sTasa", ""));
            params7.add(new BasicNameValuePair("sValorMercancia","0" ));//?
            params7.add(new BasicNameValuePair("sNumeroCuotas","0" ));
            params7.add(new BasicNameValuePair("sCentroDoc","" ));
            params7.add(new BasicNameValuePair("sCodigoDireccion","0" ));
            params7.add(new BasicNameValuePair("sDescuento1","" ));
            params7.add(new BasicNameValuePair("sDescuento2", ""));
            params7.add(new BasicNameValuePair("sDescuento3","" ));
            params7.add(new BasicNameValuePair("sAbono","0" ));
            params7.add(new BasicNameValuePair("sParametro", "traslado"));

            String resultServer7 = getHttpPost(url7, params7);
            System.out.println(resultServer7);

            String url4 = "http://" + ip + "/guardarMovimiento.php";

            List<NameValuePair> params4 = new ArrayList<NameValuePair>();
            params4.add(new BasicNameValuePair("sSw","16" ));
            params4.add(new BasicNameValuePair("sTipo", "TROT"));
            params4.add(new BasicNameValuePair("sNumero",consecutivo ));
            params4.add(new BasicNameValuePair("sCodigo", codigo.trim()));
            params4.add(new BasicNameValuePair("sSeq", "1"));
            params4.add(new BasicNameValuePair("sFec", "GETDATE()"));
            params4.add(new BasicNameValuePair("sNit", nit));
            params4.add(new BasicNameValuePair("sCantidad",cant));
            params4.add(new BasicNameValuePair("sPorcentajeIva", "16"));
            params4.add(new BasicNameValuePair("sValorUnitario","0" ));//?
            params4.add(new BasicNameValuePair("sPorcentajeDescuento", "0" ));
            params4.add(new BasicNameValuePair("sCostoUnitario","0"  ));//?
            params4.add(new BasicNameValuePair("sCostoUnitarioNiif","0"  ));
            params4.add(new BasicNameValuePair("sAdicional", ""));
            params4.add(new BasicNameValuePair("sVendedor", nit ));
            params4.add(new BasicNameValuePair("sBodega", "5" ));
            params4.add(new BasicNameValuePair("sUnd", "UND" ));
            params4.add(new BasicNameValuePair("sCantidadUnd","1" ));
            params4.add(new BasicNameValuePair("sCantidadPedida","1" ));
            params4.add(new BasicNameValuePair("sManejaInventario","S" ));
            params4.add(new BasicNameValuePair("sCostoUnitarioSin","0" ));
            params4.add(new BasicNameValuePair("sPedido","0" ));//?
            params4.add(new BasicNameValuePair("sLote", ""));
            params4.add(new BasicNameValuePair("sCantidadOtraUnd", "0"));
            params4.add(new BasicNameValuePair("sTelefono","" ));
            params4.add(new BasicNameValuePair("sTipoOp", ""));
            params4.add(new BasicNameValuePair("sNumeroOp","" ));
            params4.add(new BasicNameValuePair("sLoteVcmto","" ));
            params4.add(new BasicNameValuePair("sTipoLink", ""));
            params4.add(new BasicNameValuePair("sNumeroLink","" ));
            params4.add(new BasicNameValuePair("sSeqLink", "" ));
            params4.add(new BasicNameValuePair("sCantidadDos","" ));
            params4.add(new BasicNameValuePair("sPorcDcto2","0" ));
            params4.add(new BasicNameValuePair("sPorcDcto3","0" ));
            params4.add(new BasicNameValuePair("sParametro", "trasladoDetalle"));
            String resultServer4 = getHttpPost(url4, params4);
            System.out.println(resultServer4);

            String url3 = "http://" + ip + "/guardarMovimiento.php";

            List<NameValuePair> params3 = new ArrayList<NameValuePair>();
            params3.add(new BasicNameValuePair("sSw","12" ));
            params3.add(new BasicNameValuePair("sTipo", "TROT"));
            params3.add(new BasicNameValuePair("sNumero",consecutivo ));
            params3.add(new BasicNameValuePair("sCodigo", codigo.trim()));
            params3.add(new BasicNameValuePair("sSeq", "2"));
            params3.add(new BasicNameValuePair("sFec", "GETDATE()"));
            params3.add(new BasicNameValuePair("sNit", nit));
            params3.add(new BasicNameValuePair("sCantidad",cant));
            params3.add(new BasicNameValuePair("sPorcentajeIva", "16"));
            params3.add(new BasicNameValuePair("sValorUnitario","0" ));//?
            params3.add(new BasicNameValuePair("sPorcentajeDescuento", "0" ));
            params3.add(new BasicNameValuePair("sCostoUnitario","0"  ));//?
            params3.add(new BasicNameValuePair("sCostoUnitarioNiif","0"  ));
            params3.add(new BasicNameValuePair("sAdicional", ""));
            params3.add(new BasicNameValuePair("sVendedor", nit ));
            params3.add(new BasicNameValuePair("sBodega", "99" ));
            params3.add(new BasicNameValuePair("sUnd", "UND" ));
            params3.add(new BasicNameValuePair("sCantidadUnd","1" ));
            params3.add(new BasicNameValuePair("sCantidadPedida","1" ));
            params3.add(new BasicNameValuePair("sManejaInventario","S" ));
            params3.add(new BasicNameValuePair("sCostoUnitarioSin","0" ));
            params3.add(new BasicNameValuePair("sPedido","0" ));//?
            params3.add(new BasicNameValuePair("sLote", ""));
            params3.add(new BasicNameValuePair("sCantidadOtraUnd", "0"));
            params3.add(new BasicNameValuePair("sTelefono","" ));
            params3.add(new BasicNameValuePair("sTipoOp", ""));
            params3.add(new BasicNameValuePair("sNumeroOp","" ));
            params3.add(new BasicNameValuePair("sLoteVcmto","" ));
            params3.add(new BasicNameValuePair("sTipoLink", ""));
            params3.add(new BasicNameValuePair("sNumeroLink","" ));
            params3.add(new BasicNameValuePair("sSeqLink", "" ));
            params3.add(new BasicNameValuePair("sCantidadDos","" ));
            params3.add(new BasicNameValuePair("sPorcDcto2","0" ));
            params3.add(new BasicNameValuePair("sPorcDcto3","0" ));
            params3.add(new BasicNameValuePair("sParametro", "trasladoDetalle"));
            String resultServer3 = getHttpPost(url3, params3);
            System.out.println(resultServer3);

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
