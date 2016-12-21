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
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Procesos extends Activity {
String dirIP = "";
    ArrayList<String> columnas;
    ArrayList<String> ArrayDatos;
    int longitud = 0;
    String [][] generalAux;
    String [][] general;
    String titulo = "";
    String user = "";
    String pass = "";
    String proceso = "";
    String sucursal;
    String empresa;
    String num_rombo = "";
    private LinearLayout layout;
    private View Progress;
    ArrayList<String> procesos = new ArrayList<>();
    ArrayList<String> nomProceso = new ArrayList<>();
    ProgressDialog mProgressDialog;
    String ip = "";
    String conexion = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procesos);
        Bundle bundle = this.getIntent().getExtras();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        layout = (LinearLayout) findViewById(R.id.LayoutRelativeProcesos);
        Progress = findViewById(R.id.progressbar);
        empresa = bundle.getString("empresa");
        user = bundle.getString("user");
        pass = bundle.getString("pass");
        sucursal = bundle.getString("sucursal");
        procesos = bundle.getStringArrayList("procesos");
        nomProceso = bundle.getStringArrayList("nomProcesos");
        System.out.println("sucursal ------------>" + sucursal);
        conexion = bundle.getString("conexion");
         ip = bundle.getString("ip");

        for (int i = 0;i<procesos.size();i++){
            System.out.println("proceso - " + procesos.get(i));
            //System.out.println("nombre - " + nomProceso.get(i));
        }

        titulo = "Sucursal " + sucursal;
        setTitle(titulo);

        ArrayList<Lista_entrada> datos = new ArrayList<Lista_entrada>();
        ListView lista = (ListView) findViewById(R.id.listView2);

        Button sincronizar = (Button) findViewById(R.id.sincronizar);


        if (conexion.equals("local")) {


            sincronizar.setVisibility(View.INVISIBLE);


            for (int i = 0;i<procesos.size();i++) {
                try {

                    if (procesos.get(i).trim().equals("1")) {
                        datos.add(new Lista_entrada(R.drawable.recepcion, "   " + nomProceso.get(i)));
                        continue;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                }
            }
                    lista.setAdapter(new Lista_adaptador(this, R.layout.entrada, datos){
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

                    lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {
                            Lista_entrada elegido = (Lista_entrada) pariente.getItemAtPosition(posicion);

                            mProgressDialog = new ProgressDialog(Procesos.this);
                            mProgressDialog.setIndeterminate(false);
                            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            mProgressDialog.setMessage("Espere...");

                            mProgressDialog.show();

                            if (elegido.get_textoEncima().trim().equals("ELIMINAR")) {

                                Intent intent = new Intent((Context) Procesos.this, (Class) MostrarPlaca.class);
                                Bundle bundle2 = new Bundle();

                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("procesOriginal", "4");
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Eliminar");
                                bundle2.putString("proceso", "4");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("RECEPCION")) {
                                Intent intent = new Intent((Context) Procesos.this, (Class) Recepcion.class);
                                Bundle bundle2 = new Bundle();
                                bundle2.putString("nombreTabla", "");
                                bundle2.putString("codAtributo", "");
                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Rec");
                                bundle2.putString("proceso", "1");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }
                        }
                    });
        }else{

                    for (int i = 0;i<procesos.size();i++){
                        try {

                            if (procesos.get(i).trim().equals("1")) {
                                datos.add(new Lista_entrada(R.drawable.recepcion, "   " + nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }

                        try {

                            if (procesos.get(i).trim().equals("2")) {
                                datos.add(new Lista_entrada(R.drawable.recepcion, "   " + nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }

                        try {

                            if (procesos.get(i).trim().equals("3")) {
                                datos.add(new Lista_entrada(R.drawable.recepcion, "   " + nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }

                        try {

                            if (procesos.get(i).trim().equals("4")) {
                                datos.add(new Lista_entrada(R.drawable.recepcion, "   " + nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }

                        try {

                            if (procesos.get(i).trim().equals("5")) {
                                datos.add(new Lista_entrada(R.drawable.recepcion, "   " + nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }

                        try {

                            if (procesos.get(i).trim().equals("6")) {
                                datos.add(new Lista_entrada(R.drawable.recepcion, "   " + nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }


                        try{
                            if (procesos.get(i).trim().equals("7")) {

                                datos.add(new Lista_entrada(R.drawable.recepcion, nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }
                        try{
                            if (procesos.get(i).trim().equals("8")) {

                                datos.add(new Lista_entrada(R.drawable.recepcion, nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }
                        try {

                            if (procesos.get(i).trim().equals("10")) {
                                datos.add(new Lista_entrada(R.drawable.mano_de_obra, "   " + nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }

                        try{
                            if (procesos.get(i).trim().equals("11")) {

                                datos.add(new Lista_entrada(R.drawable.validacion, nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }
                        try{
                            if (procesos.get(i).trim().equals("12")) {

                                datos.add(new Lista_entrada(R.drawable.facturacion, nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }

                        try{
                            if (procesos.get(i).trim().equals("13")) {

                                datos.add(new Lista_entrada(R.drawable.validacion, nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }
                        try{
                            if (procesos.get(i).trim().equals("14")) {

                                datos.add(new Lista_entrada(R.drawable.validacion, nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }
                        try{
                            if (procesos.get(i).trim().equals("15")) {

                                datos.add(new Lista_entrada(R.drawable.validacion, nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }
                        try{
                            if (procesos.get(i).trim().equals("16")) {

                                datos.add(new Lista_entrada(R.drawable.validacion, nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }
                        try{
                            if (procesos.get(i).trim().equals("17")) {

                                datos.add(new Lista_entrada(R.drawable.validacion, nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }
                        try{
                            if (procesos.get(i).trim().equals("18")) {
                                datos.add(new Lista_entrada(R.drawable.cambiarfecha, nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }
                        try{
                            if (procesos.get(i).trim().equals("19")) {

                                datos.add(new Lista_entrada(R.drawable.validacion, nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }
                        try{
                            if (procesos.get(i).trim().equals("20")) {

                                datos.add(new Lista_entrada(R.drawable.validacion, nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }
                        try{
                            if (procesos.get(i).trim().equals("99")) {

                                datos.add(new Lista_entrada(R.drawable.warning, nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }


                    }


                    lista.setAdapter(new Lista_adaptador(this, R.layout.entrada, datos){
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

                    lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {
                            Lista_entrada elegido = (Lista_entrada) pariente.getItemAtPosition(posicion);

                            mProgressDialog= new ProgressDialog(Procesos.this);
                            mProgressDialog.setIndeterminate(false);
                            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            mProgressDialog.setMessage("Espere...");

                            mProgressDialog.show();

                            if (elegido.get_textoEncima().trim().equals("ELIMINAR")) {



                                Intent intent = new Intent((Context) Procesos.this, (Class) BodegaRombo.class);


                                Bundle bundle2 = new Bundle();
                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("procesOriginal", "2");
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Rec");
                                bundle2.putString("proceso", "2");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("CAMBIAR LAVADOR")) {

                                Intent intent = new Intent((Context) Procesos.this, (Class) CambiarLavador.class);
                                Bundle bundle2 = new Bundle();

                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("procesOriginal", "4");
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Eliminar");
                                bundle2.putString("proceso", "4");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("CAMBIAR PRECIO")) {

                                Intent intent = new Intent((Context) Procesos.this, (Class) CambiarPrecio.class);
                                Bundle bundle2 = new Bundle();

                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("procesOriginal", "6");
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Eliminar");
                                bundle2.putString("proceso", "6");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("RECEPCION")) {
                                new ConsultarGeneral().execute("");
                            }

                            if (elegido.get_textoEncima().trim().equals("CALIFICACION")) {
                                Intent intent = new Intent((Context) Procesos.this, (Class) BodegaRombo.class);


                                Bundle bundle2 = new Bundle();
                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("procesOriginal", "4");
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Rec");
                                bundle2.putString("proceso", "4");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("INFORME")) {


                                Intent intent = new Intent((Context) Procesos.this, (Class) TipoCarroInforme.class);


                                Bundle bundle2 = new Bundle();
                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("procesOriginal", "4");
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Rec");
                                bundle2.putString("proceso", "4");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }


                            if (elegido.get_textoEncima().trim().equals("LLAMAR")) {

                                Intent intent = new Intent((Context) Procesos.this, (Class) BodegaRombo.class);
                                Bundle bundle2 = new Bundle();

                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("procesOriginal", "17");
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Cotizacion");
                                bundle2.putString("proceso", "5");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("CAMBIAR PROMESA")) {

                                Intent intent = new Intent((Context) Procesos.this, (Class) BodegaRombo.class);
                                Bundle bundle2 = new Bundle();

                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("procesOriginal", "15");
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Cambiar Promesa");
                                bundle2.putString("proceso", "15");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("CAMBIAR SEDE")) {

                                Intent intent = new Intent((Context) Procesos.this, (Class) CambiarSede.class);
                                Bundle bundle2 = new Bundle();

                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Cambiar Sede");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("EVENTO")) {

                                Intent intent = new Intent((Context) Procesos.this, (Class) RomboEvento.class);
                                Bundle bundle2 = new Bundle();

                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("procesOriginal", "16");
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Evento recepcion");
                                bundle2.putString("proceso", "16");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("CAMBIAR FECHA")) {

                                Intent intent = new Intent((Context) Procesos.this, (Class) CambiarFecha.class);
                                Bundle bundle2 = new Bundle();

                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("procesOriginal", "18");
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Cambiar Fecha");
                                bundle2.putString("proceso", "18");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("TRASLADO")) {

                                Intent intent = new Intent((Context) Procesos.this, (Class) BodegaGeneral.class);
                                Bundle bundle2 = new Bundle();

                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("procesOriginal", "19");
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Traslado");
                                bundle2.putString("proceso", "19");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("BODEGA")) {


                                Intent intent = new Intent((Context) Procesos.this, (Class) BodegaGeneral.class);
                                Bundle bundle2 = new Bundle();
                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Bodega");
                                bundle2.putString("proceso", "4");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("FACTURACION")) {


                                Intent intent = new Intent((Context) Procesos.this, (Class) BodegaGeneral.class);
                                Bundle bundle2 = new Bundle();
                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Bodega");
                                bundle2.putString("proceso", "12");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("MANO OBRA")) {


                                Intent intent = new Intent((Context) Procesos.this, (Class) BodegaGeneral.class);
                                Bundle bundle2 = new Bundle();
                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Bodega");
                                bundle2.putString("proceso", "10");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("DESPACHO")) {

                                Intent intent = new Intent((Context) Procesos.this, (Class) Despacho.class);
                                Bundle bundle2 = new Bundle();
                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Bodega");
                                bundle2.putString("proceso", "7");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("TRANSITO")) {
                                Intent intent = new Intent((Context) Procesos.this, (Class) BodegaGeneral.class);
                                Bundle bundle2 = new Bundle();
                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Bodega");
                                bundle2.putString("proceso", "8");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("SC")) {
                                Intent intent = new Intent((Context) Procesos.this, (Class) BodegaGeneral.class);
                                Bundle bundle2 = new Bundle();
                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/SC");
                                bundle2.putString("proceso", "6");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }


                            if (elegido.get_textoEncima().trim().equals("VALIDAR REC")) {
                                Intent intent = new Intent((Context) Procesos.this, (Class) BodegaRombo.class);
                                Bundle bundle2 = new Bundle();

                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("procesOriginal", "13");
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Bodega");
                                bundle2.putString("proceso", "13");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("VALIDACION")) {
                                Intent intent = new Intent((Context) Procesos.this, (Class) BodegaRombo.class);
                                Bundle bundle2 = new Bundle();
                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("procesOriginal", "7");
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Rec");
                                bundle2.putString("proceso", "7");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("VALIDAR BOD")) {
                                Intent intent = new Intent((Context) Procesos.this, (Class) BodegaRombo.class);
                                Bundle bundle2 = new Bundle();

                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("procesOriginal", "14");
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Bodega");
                                bundle2.putString("proceso", "14");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }



                            if (elegido.get_textoEncima().trim().equals("ENTREGAR")) {


                                Intent intent = new Intent((Context) Procesos.this, (Class) BodegaTerminar.class);
                                Bundle bundle2 = new Bundle();

                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Bodega");
                                bundle2.putString("proceso", "99");
                                bundle2.putString("tipo", "mercar");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }


                        }
                    });
            }

    }

    class ConsultarGeneral extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute(){
            mProgressDialog.show();

            columnas = new ArrayList<>();
            ArrayDatos = new ArrayList<>();


        }
        @Override
        protected String doInBackground(String... f_url) {

            String url11 = "http://" + ip + "/consultarRecursoLavadero.php";
            List<NameValuePair> params11 = new ArrayList<NameValuePair>();


            params11.add(new BasicNameValuePair("sCodEmpresa", empresa.trim()));
            params11.add(new BasicNameValuePair("sCodSucursal", sucursal.trim()));
            params11.add(new BasicNameValuePair("sParametro", "ROMBO"));


            String resultServer11 = getHttpPost(url11, params11);
            System.out.println("---------------------------------");
            System.out.println(resultServer11);
            try {
                JSONArray jArray = new JSONArray(resultServer11);
                ArrayList<String> array = new ArrayList<String>();
                //Toast.makeText(getApplicationContext()," proceso " + array.get(0),Toast.LENGTH_LONG).show();
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json = jArray.getJSONObject(i);
                    array.add((json.getString("val_recurso")));
                }

                num_rombo = array.get(0).trim();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String url = "http://" + ip + "/consultarGeneralLavadero.php";

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("sCodUser", pass));
            params.add(new BasicNameValuePair("sCodEmpresa", empresa));
            params.add(new BasicNameValuePair("sCodSucursal", sucursal));
            params.add(new BasicNameValuePair("sIndEstado", "A"));
            params.add(new BasicNameValuePair("sCodProceso", "1"));
            params.add(new BasicNameValuePair("sParametro", "consultarProceso"));

            String resultServer  = getHttpPost(url,params);
            try {
                JSONArray jArray = new JSONArray(resultServer);
                generalAux = new String[jArray.length()][21];

                int contador = 0;
                int j =0;
                longitud = jArray.length();
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json = jArray.getJSONObject(i);
                    generalAux[i][j] = json.getString("cod_empresa");
                    generalAux[i][j+1] = json.getString("cod_sucursal");
                    generalAux[i][j+2] = json.getString("cod_usuario");
                    generalAux[i][j+3] = json.getString("cod_proceso");
                    generalAux[i][j+4] = json.getString("cod_atributo");
                    generalAux[i][j+5] = json.getString("cod_valor");
                    generalAux[i][j+6] = json.getString("num_secuencia");
                    generalAux[i][j+7] = json.getString("num_orden");
                    generalAux[i][j+8] = json.getString("ind_requerido");
                    generalAux[i][j+9] = json.getString("ind_tipo");
                    generalAux[i][j+10] = json.getString("val_minimo");
                    generalAux[i][j+11] = json.getString("val_maximo");
                    generalAux[i][j+12] = json.getString("num_longitud");
                    generalAux[i][j+13] = json.getString("nom_ruta");
                    generalAux[i][j+14] = json.getString("val_defecto");
                    generalAux[i][j+15] = json.getString("cod_proceso_padre");
                    generalAux[i][j+16] = json.getString("ind_confirmacion");
                    generalAux[i][j+17] = json.getString("ind_estado");
                    if (generalAux[i][j+17].equals("I")){
                        contador++;
                    }
                    generalAux[i][j+18] = json.getString("nom_columna");
                    generalAux[i][j+19] = json.getString("nom_tabla");
                    generalAux[i][j+20] = json.getString("idx_foto");
                    j=0;
                }

                for (int i = 0; i < generalAux.length; i++) {
                    for (j = 0; j < generalAux[i].length; j++) {
                        System.out.println(" AUXILIAR  " + i + " " + generalAux[i][j]);
                    }
                }

                if (contador == 0 ){
                    general = new String[generalAux.length][21];

                    for (int i = 0; i < generalAux.length; i++) {
                        for (j = 0; j < generalAux[i].length; j++) {
                            System.out.println(" AUXILIAR  " + i + " " + generalAux[i][j]);
                            if (generalAux[i][j].equals("null")){
                                generalAux[i][j] = "";
                            }

                            general[i][j] = generalAux[i][j];

                        }


                        if (generalAux[i][17].equals("I") && generalAux[i][15].equals(generalAux[i][3])){
                            columnas.add(generalAux[i][18]);
                            ArrayDatos.add(generalAux[i][5]);
                        }
                    }
                }else{
                    general = new String[generalAux.length-contador][21];
                    System.out.println("LONGITUD ->" + (generalAux.length-contador));

                    int k=0;
                    for (int i = 0; i < general.length; i++) {
                        for (j = 0; j < generalAux[i].length; j++) {
                            if (generalAux[k][j].equals("null")) {
                                generalAux[k][j] = "";
                            }

                            if (!generalAux[k][17].equals("I")) {
                                general[i][j] = generalAux[k][j];
                            } else {
                                if (generalAux[k][15].equals(generalAux[k][3])) {
                                    columnas.add(generalAux[k][18].trim());
                                    ArrayDatos.add(generalAux[k][5].trim());
                                }
                                k++;
                                general[i][j] = generalAux[k][j];
                            }
                        }
                        k++;
                    }
                }

            }catch (JSONException e ){
                e.printStackTrace();
            }

            return null;

        }

        // Once Music File is downloaded
        @Override
        protected void onPostExecute(String file_url) {

            Intent intent = new Intent((Context) Procesos.this, (Class) Recepcion.class);
            Bundle bundle2 = new Bundle();
            bundle2.putString("nombreTabla", "");
            bundle2.putString("codAtributo", "");
            bundle2.putString("user", user);
            bundle2.putString("pass", pass);
            bundle2.putString("sucursal", sucursal);
            bundle2.putString("empresa", empresa);
            bundle2.putString("conexion", conexion);
            bundle2.putString("sucursal", sucursal);
            bundle2.putSerializable("general" , general);
            bundle2.putSerializable("generalAux" , generalAux);
            bundle2.putStringArrayList("columnas" , columnas);
            bundle2.putStringArrayList("datos" , ArrayDatos);
            bundle2.putString("rombo" , num_rombo);
            bundle2.putSerializable("general" , general);
            bundle2.putString("ip", ip);
            bundle2.putString("titulo", titulo + "/Rec");
            bundle2.putString("proceso", "1");
            intent.putExtras(bundle2);
            startActivity(intent);
            mProgressDialog.dismiss();
        }
    }

    public void sincronizar(View v){
        final DataBaseHelper dataBaseHelper = new DataBaseHelper((Context) this);
        try {
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList traDetalle = dataBaseHelper.consultarLocal(pass, empresa, sucursal);

        for (int i = 0;i<traDetalle.size();i++){
            System.out.println("DETALLE - " + i + " " + traDetalle.get(i));
        }

        dataBaseHelper.close();

        System.out.println("SIZE - " + traDetalle.size()/45);
        for (int i = 0;i<traDetalle.size()/45;i++){
            String url2 = "http://" + ip + "/guardarMovimientoLavadero.php";

            List<NameValuePair> params2 = new ArrayList<NameValuePair>();

            params2.add(new BasicNameValuePair("sCodEmpresa", traDetalle.get(i).toString()));
            params2.add(new BasicNameValuePair("sCodSucursal", traDetalle.get(i+1).toString()));
            params2.add(new BasicNameValuePair("sCodProceso", traDetalle.get(i+2).toString()));
            params2.add(new BasicNameValuePair("sCodPlaca", traDetalle.get(i+3).toString()));
            params2.add(new BasicNameValuePair("sNumRombo", traDetalle.get(i+4).toString()));
            params2.add(new BasicNameValuePair("sCodUsuario", traDetalle.get(i+5).toString()));
            params2.add(new BasicNameValuePair("sCodUbicacion", traDetalle.get(i+6).toString()));
            params2.add(new BasicNameValuePair("sFecProceso", traDetalle.get(i+7).toString()));
            params2.add(new BasicNameValuePair("sNumNit", traDetalle.get(i+8).toString()));
            params2.add(new BasicNameValuePair("sIndEstado", traDetalle.get(i+9).toString()));
            params2.add(new BasicNameValuePair("sNumItem", traDetalle.get(i+10).toString()));
            params2.add(new BasicNameValuePair("sNumTotal", traDetalle.get(i+11).toString()));
            params2.add(new BasicNameValuePair("sCodCodigo", traDetalle.get(i+12).toString()));
            params2.add(new BasicNameValuePair("sNomCodigo", traDetalle.get(i+13).toString()));
            params2.add(new BasicNameValuePair("sCanPedida", traDetalle.get(i+14).toString()));
            params2.add(new BasicNameValuePair("sCanDespachada", traDetalle.get(i+15).toString()));
            params2.add(new BasicNameValuePair("sCanExistencia", traDetalle.get(i+16).toString()));
            params2.add(new BasicNameValuePair("sCodGrupo", traDetalle.get(i+17).toString()));
            params2.add(new BasicNameValuePair("sCodSubgrupo", traDetalle.get(i+18).toString()));
            params2.add(new BasicNameValuePair("sCodUbicacionBodega", traDetalle.get(i+19).toString()));
            params2.add(new BasicNameValuePair("sCodEvento", traDetalle.get(i+20).toString()));
            params2.add(new BasicNameValuePair("sNumTiempoProceso", traDetalle.get(i+21).toString()));
            params2.add(new BasicNameValuePair("sNumTiempoTotal", traDetalle.get(i+22).toString()));
            params2.add(new BasicNameValuePair("sCodConcepto", traDetalle.get(i+23).toString()));
            params2.add(new BasicNameValuePair("sNumConcepto", traDetalle.get(i+24).toString()));
            params2.add(new BasicNameValuePair("sFecInicial", traDetalle.get(i+25).toString()));
            params2.add(new BasicNameValuePair("sFecFinal", traDetalle.get(i+26).toString()));
            params2.add(new BasicNameValuePair("sIndProcesoV", traDetalle.get(i+27).toString()));
            params2.add(new BasicNameValuePair("sIndProceso", traDetalle.get(i+28).toString()));
            params2.add(new BasicNameValuePair("sCodTecnico", traDetalle.get(i+29).toString()));
            params2.add(new BasicNameValuePair("sFecLanzamiento", traDetalle.get(i+30).toString()));
            params2.add(new BasicNameValuePair("sValUnitario", traDetalle.get(i+31).toString()));
            params2.add(new BasicNameValuePair("sValTotal", traDetalle.get(i+32).toString()));
            params2.add(new BasicNameValuePair("sValPIva", traDetalle.get(i+33).toString()));
            params2.add(new BasicNameValuePair("sValPDescuento", traDetalle.get(i+34).toString()));
            params2.add(new BasicNameValuePair("sValDescuento", traDetalle.get(i+35).toString()));
            params2.add(new BasicNameValuePair("sValIva", traDetalle.get(i+36).toString()));
            params2.add(new BasicNameValuePair("sNomObs", traDetalle.get(i+37).toString()));
            params2.add(new BasicNameValuePair("sAno", traDetalle.get(i+38).toString()));
            params2.add(new BasicNameValuePair("sMes", traDetalle.get(i+39).toString()));
            params2.add(new BasicNameValuePair("sDia", traDetalle.get(i+40).toString()));
            params2.add(new BasicNameValuePair("sCodReferencia", traDetalle.get(i+41).toString()));
            params2.add(new BasicNameValuePair("sNumModelo", traDetalle.get(i+42).toString()));
            params2.add(new BasicNameValuePair("sCodMarca", traDetalle.get(i+43).toString()));
            params2.add(new BasicNameValuePair("sIndFPago", traDetalle.get(i+44).toString()));
            params2.add(new BasicNameValuePair("sValCosto", traDetalle.get(i+45).toString()));
            params2.add(new BasicNameValuePair("sParametro", "sincronizar"));

            String resultServer2 = getHttpPost(url2, params2);
            System.out.println(resultServer2);
        }
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        showProgress(show, this,Progress ,layout);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_procesos, menu);
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
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Saliendo...")
                .setMessage("Esta Seguro que desea salir?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DataBaseHelper dataBaseHelper = new DataBaseHelper(Procesos.this);
                        try {
                            dataBaseHelper.createDataBase();
                            dataBaseHelper.openDataBase();
                            dataBaseHelper.borrarMovimiento();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }

                        dataBaseHelper.close();
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}
