package com.example.david.proyectowms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Validacion extends Activity {
    int indice =0;
    private final String ruta_fotos = "/storage/sdcard0/METRO/" ;
    private File file = new File(ruta_fotos);
    private Button boton;
    static String conexion = "";
    String ip = "" ;
    String placa = "";
    String vin = "";
    String Cedula = "";
    String nombre1 = "";
    String nombre2 = "";
    String apellido1 = "";
    String apellido2 = "";
    String nombre = "NOMBRE.jpg";
    String cod_placa = "";
    String num_rombo= "";
    String cod_ubicacion = "";
    String fec_proceso = "";
    String num_nit = "";
    int request_code = 1;
    ArrayList  atributos ;
    String titulo = "";
    String proceso = "";
    String cod_sucursal = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validacion);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Bundle bundle = this.getIntent().getExtras();
        String user = bundle.getString("user");
        proceso = bundle.getString("proceso");
        String pass = bundle.getString("pass");
        conexion = bundle.getString("conexion");
        titulo = bundle.getString("titulo");
        num_rombo = bundle.getString("rombo");
        cod_sucursal = bundle.getString("cod_sucursal");
        System.out.println("ROMBOO" + num_rombo);
        ip = bundle.getString("ip");

        String url1 = "http://"+  ip + "/delete.php";


        List<NameValuePair> params1 = new ArrayList<NameValuePair>();

        /** Get result from Server (Return the JSON Code)
         * StatusID = ? [0=Failed,1=Complete]
         * Error    = ? [On case error return custom error message]
         *
         * Eg Save Failed = {"StatusID":"0","Error":"Email Exists!"}
         * Eg Save Complete = {"StatusID":"1","Error":""}
         */

        String resultServer1 = getHttpPost(url1, params1);

        JSONObject c2;


        setTitle(titulo);

        EditText texto = (EditText) findViewById(R.id.editText_texto);
        //num_rombo =  texto.getText().toString();



        if (conexion.equals("local")) {
            final DataBaseHelper dataBaseHelper = new DataBaseHelper((Context) this);
            try {
                dataBaseHelper.createDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                dataBaseHelper.openDataBase();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            final String[][] general;
            general = dataBaseHelper.consultarProceso("123", "METRO", "10", "01");
            actualizar(general, indice,conexion);


            final ImageButton button = (ImageButton) findViewById(R.id.imageButton8);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (indice == general.length - 1) {
                    } else {
                        indice++;
                        actualizar(general, indice,conexion);
                    }
                }
            });


            final ImageButton button2 = (ImageButton) findViewById(R.id.imageButton7);
            button2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (indice == 0) {
                    } else {
                        indice--;
                        actualizar(general, indice,conexion);
                    }
                }
            });

            final ImageButton button3 = (ImageButton) findViewById(R.id.imageButton6);
            button3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    indice = 0;
                    actualizar(general, indice,conexion);
                }
            });

            final ImageButton button4 = (ImageButton) findViewById(R.id.imageButton9);
            button4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    indice = general.length - 1;
                    actualizar(general, indice,conexion);
                }
            });
        }else{


            InputStream isr = null;
            String url = "http://" + ip + "/consultarGeneral.php";

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("sCodUser", pass));
            params.add(new BasicNameValuePair("sCodSucursal", cod_sucursal));
            params.add(new BasicNameValuePair("sCodEmpresa", "METRO"));
            params.add(new BasicNameValuePair("sIndEstado", "A"));
            params.add(new BasicNameValuePair("sCodProceso", "11"));
            params.add(new BasicNameValuePair("sParametro", "consultarProceso"));

            String resultServer  = getHttpPost(url,params);
            System.out.println(resultServer);
            try {
                JSONArray jArray = new JSONArray(resultServer);
                final String[][] general = new String[jArray.length()][18];
                int j =0;
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json = jArray.getJSONObject(i);
                    general[i][j] = json.getString("cod_empresa");
                    general[i][j+1] = json.getString("cod_sucursal");
                    general[i][j+2] = json.getString("cod_usuario");
                    general[i][j+3] = json.getString("cod_proceso");
                    general[i][j+4] = json.getString("cod_atributo");
                    general[i][j+5] = json.getString("cod_valor");
                    general[i][j+6] = json.getString("num_secuencia");
                    general[i][j+7] = json.getString("num_orden");
                    general[i][j+8] = json.getString("ind_requerido");
                    general[i][j+9] = json.getString("ind_tipo");
                    general[i][j+10] = json.getString("val_minimo");
                    general[i][j+11] = json.getString("val_maximo");
                    general[i][j+12] = json.getString("num_longitud");
                    general[i][j+13] = json.getString("nom_ruta");
                    general[i][j+14] = json.getString("val_defecto");
                    general[i][j+15] = json.getString("ind_autonext");
                    general[i][j+16] = json.getString("ind_confirmacion");
                    general[i][j+17] = json.getString("ind_estado");
                }


                actualizar(general, indice, conexion);

                final String cod_empresa = general[0][0];
                final String cod_sucursal = general[0][1];
                final String cod_proceso = general[0][3];
                final String cod_usuario = general[0][2];
                final String ind_estado = general[0][17];

                final Button buttonOk = (Button) findViewById(R.id.button_OK);
                buttonOk.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        ok(cod_empresa,cod_sucursal,cod_proceso,cod_usuario,ind_estado);
                    }
                });

                final ImageButton button = (ImageButton) findViewById(R.id.imageButton8);
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (indice == general.length - 1) {
                        } else {
                            indice++;
                            actualizar(general, indice, conexion);
                        }
                    }
                });


                final ImageButton button2 = (ImageButton) findViewById(R.id.imageButton7);
                button2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (indice == 0) {
                        } else {
                            indice--;
                            actualizar(general, indice,conexion);
                        }
                    }
                });

                final ImageButton button3 = (ImageButton) findViewById(R.id.imageButton6);
                button3.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        indice = 0;
                        actualizar(general, indice,conexion);
                    }
                });

                final ImageButton button4 = (ImageButton) findViewById(R.id.imageButton9);
                button4.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        indice = general.length - 1;
                        actualizar(general, indice, conexion);
                    }
                });
            }catch (JSONException e ){
                e.printStackTrace();
            }
        }

    }



    public void camara(View v) {

        //Si no existe crea la carpeta donde se guardaran las fotos
        file.mkdirs();
        //String file = ruta_fotos + getCode() + ".jpg";
        TextView texto = (TextView) findViewById(R.id.textView_atributo);

        String atributo = texto.getText().toString();
        if (atributo.substring(3, texto.length()).equals("NIT")) {
            nombre = "NIT.jpg";
        }
        if (atributo.substring(3, texto.length()).equals("MATRICULA")) {
            nombre = "MATRICULA.jpg";
        }


        Toast.makeText(getApplicationContext(), "ATRIBUTO : " + atributo.substring(3, texto.length()), Toast.LENGTH_SHORT).show();

        String file = "storage/sdcard0/METRO/" + nombre;
        File mi_foto = new File(file);
        try {
            mi_foto.createNewFile();

            //
            Uri uri = Uri.fromFile(mi_foto);
            //Abre la camara para tomar la foto
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //Guarda imagen
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            //Retorna a la actividad
            startActivityForResult(cameraIntent, 0);
            ImageView myImage = (ImageView) findViewById(R.id.imageView);
            //File imgFile = new  File(ruta_fotos + getCode() + ".jpg");
            String strPath = "/storage/sdcard0/METRO/" + nombre ;

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;

            Bitmap bm = BitmapFactory.decodeFile(strPath,options);

            myImage.setImageBitmap(bm);
            myImage.invalidate();
        } catch (IOException ex) {
            Log.e("ERROR ", "Error:" + ex);
        }
    }


    public void mostrarImagen(){
        ImageView myImage = (ImageView) findViewById(R.id.imageView);
        //File imgFile = new  File(ruta_fotos + getCode() + ".jpg");
        String strPath = "/storage/sdcard0/METRO/" + nombre ;

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;

        Bitmap bm = BitmapFactory.decodeFile(strPath,options);

        myImage.setImageBitmap(bm);
        /*try {
            if (imgFile.exists()) {
                ImageView myImage = (ImageView) findViewById(R.id.imageView);
                myImage.setImageURI(Uri.fromFile(imgFile));

            }
        }catch (Exception e){
            Toast.makeText(Validacion.this, e.getMessage(), Toast.LENGTH_LONG);
        }*/
    }



    public void siguiente(View v){


        final DataBaseHelper dataBaseHelper = new DataBaseHelper((Context)this);
        try {
            dataBaseHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dataBaseHelper.openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        final String [][] general;
        general = dataBaseHelper.consultarProceso("123", "METRO", "10", "1");
        if (indice==general.length-1){

        }else {
            indice++;
            actualizar(general, indice,conexion);
        }
    }





    public void cancelar(View v){
        finish();
    }

    public void guardar(View v){
        final DataBaseHelper dataBaseHelper = new DataBaseHelper((Context)this);
        try {
            dataBaseHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dataBaseHelper.openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        final String [][] general;
        general = dataBaseHelper.consultarProceso("123", "METRO", "10", "01");
       /* for (int j = 0;j<Integer.parseInt(resultado.get(0).toString());j++) {
            for (int i = 0; i < resultado.size()-18; i++) {
                general[j][i] = resultado.get(i).toString();
            }
        }*/

        final ArrayList resultado = new ArrayList();



        /*if (Integer.parseInt(resultado.get(0).toString()) > 0){
            int cont = 0;

        }*/

        final String cod_empresa = resultado.get(0).toString();
        final String cod_sucursal = resultado.get(1).toString();
        final String cod_usuario = resultado.get(2).toString();
        final String cod_proceso = resultado.get(3).toString();
        final String cod_atributo = resultado.get(4).toString();
        final String cod_valor = resultado.get(5).toString();
        final String num_orden = resultado.get(7).toString();

        EditText texto = (EditText) findViewById(R.id.editText_texto);
        try {
            dataBaseHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dataBaseHelper.openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //System.out.println(Integer.parseInt(resultado.get(6).toString()));
        dataBaseHelper.guardarProceso(cod_empresa, cod_sucursal, cod_usuario, cod_proceso, cod_atributo, texto.getText().toString(), num_orden);
        Toast.makeText(Validacion.this, (CharSequence) "se guardo correctamente!", Toast.LENGTH_SHORT).show();
        dataBaseHelper.close();
        texto.setText("");
    }

    public void atras(View v){

        final DataBaseHelper dataBaseHelper = new DataBaseHelper((Context)this);
        try {
            dataBaseHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dataBaseHelper.openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        final String [][] general;
        general = dataBaseHelper.consultarProceso("123", "METRO", "10", "01");
        if(indice == 0){

        }else{
            indice--;
            actualizar(general,indice,conexion);
        }
    }

    public void anteriorUltimo(View v){
        final DataBaseHelper dataBaseHelper = new DataBaseHelper((Context)this);
        try {
            dataBaseHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dataBaseHelper.openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        final String [][] general;
        general = dataBaseHelper.consultarProceso("123", "METRO", "10", "01");
        indice = general.length-1;
        actualizar(general, indice, conexion);
    }

    public void prueba (View v){
        TextView atributo = (TextView) findViewById(R.id.textView_atributo);
        atributo.setText("cambio");
    }

    public void anteriorPrimero(View v){
        final DataBaseHelper dataBaseHelper = new DataBaseHelper((Context)this);
        try {
            dataBaseHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dataBaseHelper.openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        final String [][] general;
        general = dataBaseHelper.consultarProceso("123", "METRO", "10", "01");
        indice= 0;
        actualizar(general, indice, conexion);
    }


    public void actualizar(final String[][] general, final int indice, final String conexion){

        mostrarImagen();

        final ArrayList resultado = new ArrayList();
        final TextView atributo = (TextView) findViewById(R.id.textView_atributo);
        final ImageView imagen = (ImageView) findViewById(R.id.imageView);
        final EditText texto = (EditText) findViewById(R.id.editText_texto);

        texto.setText("");



        for(int j = 0;j<general[0].length;j++){
            resultado.add(general[indice][j]);
        }


        String url = "http://" + ip + "/consultarGeneral.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sCodUser", resultado.get(2).toString().trim()));
        params.add(new BasicNameValuePair("sCodEmpresa", resultado.get(0).toString().trim()));
        params.add(new BasicNameValuePair("sCodAtributo", resultado.get(4).toString().trim()));
        params.add(new BasicNameValuePair("sParametro", "actualizar"));

        final String resultServer  = getHttpPost(url, params);
        System.out.println("-----------ACTUALIZAR------------");
        System.out.println("coduser - " + resultado.get(2).toString());
        System.out.println("codempresa - " + resultado.get(0).toString());
        System.out.println("codatributo + " + resultado.get(4).toString());
        System.out.println(resultServer);
        try {
            JSONArray jArray = new JSONArray(resultServer);
            ArrayList<String> array = new ArrayList<String>();
            //Toast.makeText(getApplicationContext()," proceso " + array.get(0),Toast.LENGTH_LONG).show();
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                array.add((json.getString("cod_valor")));
            }
            texto.setText(array.get(0));
        }catch (JSONException e){
            e.printStackTrace();
        }



        atributo.setText(resultado.get(7) + ". " + resultado.get(4).toString().trim());


        if (resultado.get(13).equals("ROMBO.JPG")) imagen.setImageResource(R.drawable.rombo1);
        if (resultado.get(13).equals("PLACA.JPG")) imagen.setImageResource(R.drawable.placa6);
        if (resultado.get(13).equals("NIT.JPG")) imagen.setImageResource(R.drawable.cedula1);
        /*if (Integer.parseInt(resultado.get(0).toString()) > 0){
            int cont = 0;

        }*/

        texto.requestFocus();
        final String cod_empresa = resultado.get(0).toString();
        final String cod_sucursal = resultado.get(1).toString();
        final String cod_usuario = resultado.get(2).toString();
        final String cod_proceso = resultado.get(3).toString();
        final String cod_atributo = resultado.get(4).toString();
        final String cod_valor = resultado.get(5).toString();
        final String num_orden = resultado.get(7).toString();


        if (conexion.equals("local")) texto.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (texto.getText().length() == 0 && (cod_atributo.equals("ROMBO") || (cod_atributo.equals("UBICACION")) )) {
                        Intent intent = new Intent((Context) Validacion.this, (Class) MostrarRombUbic.class);
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("conexion", conexion);
                        Validacion.this.startActivityForResult(intent,request_code);
                        Toast.makeText(getApplicationContext(),resultado.get(4) + "  ATRIBUTO ",Toast.LENGTH_SHORT).show();

                    } else {

                        final DataBaseHelper dataBaseHelper = new DataBaseHelper((Context) Validacion.this);
                        try {
                            dataBaseHelper.createDataBase();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            dataBaseHelper.openDataBase();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        if (resultado.get(9).equals("D")) {
                            String cedula = texto.getText().toString().replaceAll("\n", "");
                            String cedulafinal = "";

                                /*
                                Character  numAux = cedula.charAt(50);
                                String num = numAux.toString();
                                while (num.charAt(0)>='0' && num.charAt(0)<='9'){
                                    cedulafinal = cedulafinal + num.charAt(0);
                                }
                                /*for (int i = 50;i<58;i++){
                                    cedulafinal = cedulafinal+cedula.charAt(i);
                                }*/


                            Cedula = cedula.substring(47, 58);
                            nombre1 = cedula.substring(95, 116);
                            nombre2 = cedula.substring(117, 140);
                            apellido1 = cedula.substring(58, 71);
                            apellido2 = cedula.substring(72, 94);


                            if (Cedula.substring(1, 2).equals("00")) {
                                Cedula = cedula.substring(50, 58);
                            }
                            dataBaseHelper.updateNit(Cedula, nombre1, nombre2, apellido1, apellido2);
                            dataBaseHelper.close();
                            pasar(conexion, general);
                            return true;

                        } else {


                            //System.out.println(Integer.parseInt(resultado.get(6).toString()));
                            dataBaseHelper.guardarProceso(cod_empresa, cod_sucursal, cod_usuario, cod_proceso, cod_atributo, texto.getText().toString(), num_orden);
                            dataBaseHelper.close();
                            pasar(conexion, general);
                            return true;
                            //actualizar(general,indice);
                        }
                    }
                }
                return false;
            }
        });
        else {
            final String finalIp = ip;

            final GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
                public boolean onDoubleTap(MotionEvent e) {
                    if (texto.getText().toString().matches("") && (cod_atributo.trim().equals("ROMBO") || cod_atributo.trim().equals("UBICACION") || cod_atributo.trim().equals("MECANICO"))) {
                        Intent intent = new Intent((Context) Validacion.this, (Class) MostrarRombUbic.class);
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("conexion", conexion);
                        bundle2.putString("ip", ip);
                        bundle2.putString("atributo", cod_atributo);
                        intent.putExtras(bundle2);
                        Validacion.this.startActivityForResult(intent, request_code);
                        Toast.makeText(getApplicationContext(), resultado.get(4) + "  ATRIBUTO ", Toast.LENGTH_SHORT).show();

                    } else {
                        String url2 = "http://" + ip + "/consultarGeneral.php";

                        List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                        params2.add(new BasicNameValuePair("sCodUser", resultado.get(2).toString()));
                        params2.add(new BasicNameValuePair("sCodEmpresa", resultado.get(0).toString()));
                        params2.add(new BasicNameValuePair("sCodAtributo", resultado.get(4).toString()));
                        params2.add(new BasicNameValuePair("sParametro", "actualizar"));
                        /** Get result from Server (Return the JSON Code)
                         * StatusID = ? [0=Failed,1=Complete]
                         * Error    = ? [On case error return custom error message]
                         *
                         * Eg Save Failed = {"StatusID":"0","Error":"Email Exists!"}
                         * Eg Save Complete = {"StatusID":"1","Error":""}
                         */

                        String resultServer2 = getHttpPost(url2, params2);
                        System.out.println("---------------------------------");
                        System.out.println(resultServer2);
                        ArrayList<String> array = new ArrayList<String>();
                        try {
                            JSONArray jArray = new JSONArray(resultServer2);

                            //Toast.makeText(getApplicationContext()," proceso " + array.get(0),Toast.LENGTH_LONG).show();
                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject json = jArray.getJSONObject(i);
                                array.add((json.getString("cod_valor")));
                            }
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }

                        if (array.size()>0) {
                            String url4 = "http://" + ip + "/updateGeneral.php";

                            List<NameValuePair> params4 = new ArrayList<NameValuePair>();


                            params4.add(new BasicNameValuePair("sCodEmpresa", cod_empresa));
                            params4.add(new BasicNameValuePair("sCodUsuario", cod_usuario));
                            params4.add(new BasicNameValuePair("sCodAtributo", cod_atributo));
                            params4.add(new BasicNameValuePair("sCodValor", texto.getText().toString()));
                            params4.add(new BasicNameValuePair("sParametro", "update3"));

                            String resultServer4 = getHttpPost(url4, params4);

                            JSONObject c4;
                            try {

                                Toast.makeText(Validacion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
                                pasar(conexion, general);
                            }catch(Exception e1){
                                Toast.makeText(Validacion.this, "Dont Update Data Successfully", Toast.LENGTH_SHORT).show();
                            }
                        } else {





                            final AlertDialog.Builder ad = new AlertDialog.Builder(Validacion.this);

                            ad.setTitle("Error! ");
                            ad.setIcon(android.R.drawable.btn_star_big_on);
                            ad.setPositiveButton("Close", null);
                            InputStream isr = null;
                            String url = "http://" + finalIp + "/update.php";

                            List<NameValuePair> params = new ArrayList<NameValuePair>();


                            params.add(new BasicNameValuePair("sCodEmpresa", cod_empresa));
                            params.add(new BasicNameValuePair("sCodSucursal", cod_sucursal));
                            params.add(new BasicNameValuePair("sCodUsuario", cod_usuario));
                            params.add(new BasicNameValuePair("sCodProceso", cod_proceso));
                            params.add(new BasicNameValuePair("sCodAtributo", cod_atributo));
                            params.add(new BasicNameValuePair("sCodValor", texto.getText().toString()));
                            params.add(new BasicNameValuePair("sNumOrden", num_orden));



                            // params.add(new BasicNameValuePair("sNumOrden", num_orden));


                            if (resultado.get(4).equals("NIT")) {
                                String cedula = texto.getText().toString().replaceAll("\n", "");
                                String cedulafinal = "";

                                /*
                                Character  numAux = cedula.charAt(50);
                                String num = numAux.toString();
                                while (num.charAt(0)>='0' && num.charAt(0)<='9'){
                                    cedulafinal = cedulafinal + num.charAt(0);
                                }
                                /*for (int i = 50;i<58;i++){
                                    cedulafinal = cedulafinal+cedula.charAt(i);
                                }*/

                                String url3 = "http://" + finalIp + ":8080/update2.php";
                                String resultServer3;
                                try {

                                    Cedula = cedula.substring(47, 58);
                                    nombre1 = cedula.substring(95, 116);
                                    nombre2 = cedula.substring(117, 140);
                                    apellido1 = cedula.substring(58, 71);
                                    apellido2 = cedula.substring(72, 94);
                                    if (Cedula.substring(1, 2).equals("00")) {
                                        Cedula = cedula.substring(50, 58);
                                    }


                                    List<NameValuePair> params3 = new ArrayList<NameValuePair>();
                                    params3.add(new BasicNameValuePair("sNit", Cedula));
                                    params3.add(new BasicNameValuePair("sApellido1", apellido1));
                                    params3.add(new BasicNameValuePair("sApellido2", apellido2));
                                    params3.add(new BasicNameValuePair("sNombre1", nombre1));
                                    params3.add(new BasicNameValuePair("sNombre2", nombre2));
                                    resultServer3 = getHttpPost(url3, params3);
                                    System.out.println(resultServer3);

                                    String strStatusID = "0";
                                    String strError = "Unknow Status! 3";

                                    JSONObject c3;
                                    try {
                                        c3 = new JSONObject(resultServer3);
                                        strStatusID = c3.getString("StatusID");
                                        strError = c3.getString("Error");
                                    } catch (JSONException ex) {
                                        // TODO Auto-generated catch block
                                        ex.printStackTrace();
                                    }
                                    if (strStatusID.equals("0")) {
                                        ad.setMessage(strError);
                                        ad.show();
                                        return false;
                                    } else {
                                        Toast.makeText(Validacion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
                                        pasar(conexion, general);
                                    }
                                } catch (Exception ec) {
                                    url3 = "http://" + finalIp + "/update.php";
                                    List<NameValuePair> params3 = new ArrayList<NameValuePair>();
                                    params3.add(new BasicNameValuePair("sCodEmpresa", cod_empresa));
                                    params3.add(new BasicNameValuePair("sCodSucursal", cod_sucursal));
                                    params3.add(new BasicNameValuePair("sCodUsuario", cod_usuario));
                                    params3.add(new BasicNameValuePair("sCodProceso", cod_proceso));
                                    params3.add(new BasicNameValuePair("sCodAtributo", cod_atributo));
                                    params3.add(new BasicNameValuePair("sCodValor", texto.getText().toString()));
                                    params3.add(new BasicNameValuePair("sNumOrden", num_orden));
                                    resultServer3 = getHttpPost(url3, params3);
                                    String strStatusID = "0";
                                    String strError = "Unknow Status! 3";

                                    JSONObject c3;
                                    try {
                                        c3 = new JSONObject(resultServer3);
                                        strStatusID = c3.getString("StatusID");
                                        strError = c3.getString("Error");
                                    } catch (JSONException ex) {
                                        // TODO Auto-generated catch block
                                        ex.printStackTrace();
                                    }
                                    if (strStatusID.equals("0")) {
                                        ad.setMessage(strError);
                                        ad.show();
                                        return false;
                                    } else {
                                        Toast.makeText(Validacion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
                                        pasar(conexion, general);
                                    }
                                }


                            } else {
                                if (resultado.get(4).equals("MATRICULA")) {
                                    String matricula = texto.getText().toString().replaceAll("\n", "");
                                    matricula = matricula.replaceAll(" ", "");
                                    System.out.println("LONGITUD : " + matricula.length());
                                    System.out.println(texto.getText().toString());
                                    String matriculafinal = "";

                                /*
                                Character  numAux = cedula.charAt(50);
                                String num = numAux.toString();
                                while (num.charAt(0)>='0' && num.charAt(0)<='9'){
                                    cedulafinal = cedulafinal + num.charAt(0);
                                }
                                /*for (int i = 50;i<58;i++){
                                    cedulafinal = cedulafinal+cedula.charAt(i);
                                }*/
                                    try {
                                        placa = matricula.substring(55, 61);
                                        vin = matricula.substring(61, 78);

                                        texto.setText("");

                                        Intent intent = new Intent((Context) Validacion.this, (Class) MostrarDatosMatricula.class);
                                        Bundle bundle2 = new Bundle();
                                        bundle2.putString("placa", placa);
                                        bundle2.putString("vin", vin);
                                        intent.putExtras(bundle2);
                                        Validacion.this.startActivity(intent);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();

                                        ad.setTitle("Error! ");
                                        ad.setIcon(android.R.drawable.btn_star_big_on);
                                        ad.setPositiveButton("Close", null);
                                        String url5 = "http://" + finalIp + "/update.php";

                                        List<NameValuePair> params5 = new ArrayList<NameValuePair>();


                                        params5.add(new BasicNameValuePair("sCodEmpresa", cod_empresa));
                                        params5.add(new BasicNameValuePair("sCodSucursal", cod_sucursal));
                                        params5.add(new BasicNameValuePair("sCodUsuario", cod_usuario));
                                        params5.add(new BasicNameValuePair("sCodProceso", cod_proceso));
                                        params5.add(new BasicNameValuePair("sCodAtributo", cod_atributo));
                                        params5.add(new BasicNameValuePair("sCodValor", texto.getText().toString()));
                                        params5.add(new BasicNameValuePair("sNumOrden", num_orden));
                                        String resultServer = getHttpPost(url, params);
                                        System.out.println(resultServer);


                                        JSONObject c;
                                        try {
                                            Toast.makeText(Validacion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
                                            pasar(conexion, general);
                                        }catch(Exception e1){
                                            Toast.makeText(Validacion.this, "Dont Update Data Successfully", Toast.LENGTH_SHORT).show();

                                        }
                                    }

                                } else {
                                    String resultServer = getHttpPost(url, params);
                                    System.out.println(resultServer);

                                    String strStatusID = "0";
                                    String strError = "Unknow Status!";

                                    JSONObject c;
                                    try {

                                        Toast.makeText(Validacion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();

                                        pasar(conexion, general);
                                    }catch(Exception e1){
                                        Toast.makeText(Validacion.this, "Dont Update Data Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }


                            String file = "storage/sdcard0/METRO/" + nombre;
                            File mi_foto = new File(file);
                            String file2 = "storage/sdcard0/METRO/";
                            File mi_foto2 = new File(file2);
                            mi_foto.renameTo(mi_foto2);

                        }//else{
                        //    Toast.makeText(getApplicationContext()," Este registro ya esta en la base de datos !" , Toast.LENGTH_SHORT).show();
                        //}
                        // }
                    }
                    if (cod_atributo.equals("ROMBO")){
                        num_rombo = texto.getText().toString();
                    }
                    if (cod_atributo.equals("PLACA")){
                        cod_placa = texto.getText().toString();
                    }
                    if(cod_atributo.equals("UBICACION")){
                        cod_ubicacion = texto.getText().toString();
                    }
                    if(cod_atributo.equals("NIT")){
                        num_nit = texto.getText().toString();
                    }
                    return true;
                }
            });
            EditText tv = (EditText) findViewById(R.id.editText_texto);
            tv.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
                }
            });

            texto.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        if (texto.getText().toString().matches("") && (cod_atributo.equals("ROMBO") || cod_atributo.equals("UBICACION") || cod_atributo.equals("MECANICO"))) {
                            Intent intent = new Intent((Context) Validacion.this, (Class) MostrarRombUbic.class);
                            Bundle bundle2 = new Bundle();
                            bundle2.putString("conexion", conexion);
                            bundle2.putString("ip", ip);
                            bundle2.putString("atributo", cod_atributo);
                            intent.putExtras(bundle2);
                            Validacion.this.startActivityForResult(intent, request_code);
                            Toast.makeText(getApplicationContext(), resultado.get(4) + "  ATRIBUTO ", Toast.LENGTH_SHORT).show();

                        } else {
                            String url2 = "http://" + ip + "/consultarGeneral.php";

                            List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                            params2.add(new BasicNameValuePair("sCodUser", resultado.get(2).toString()));
                            params2.add(new BasicNameValuePair("sCodEmpresa", resultado.get(0).toString()));
                            params2.add(new BasicNameValuePair("sCodAtributo", resultado.get(4).toString()));
                            params2.add(new BasicNameValuePair("sParametro", "actualizar"));
                            /** Get result from Server (Return the JSON Code)
                             * StatusID = ? [0=Failed,1=Complete]
                             * Error    = ? [On case error return custom error message]
                             *
                             * Eg Save Failed = {"StatusID":"0","Error":"Email Exists!"}
                             * Eg Save Complete = {"StatusID":"1","Error":""}
                             */

                            String resultServer2 = getHttpPost(url2, params2);
                            System.out.println("---------------------------------");
                            System.out.println(resultServer2);
                            ArrayList<String> array = new ArrayList<String>();
                            try {
                                JSONArray jArray = new JSONArray(resultServer2);

                                //Toast.makeText(getApplicationContext()," proceso " + array.get(0),Toast.LENGTH_LONG).show();
                                for (int i = 0; i < jArray.length(); i++) {
                                    JSONObject json = jArray.getJSONObject(i);
                                    array.add((json.getString("cod_valor")));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (array.size()>0) {
                                String url4 = "http://" + ip + "/updateGeneral.php";

                                List<NameValuePair> params4 = new ArrayList<NameValuePair>();


                                params4.add(new BasicNameValuePair("sCodEmpresa", cod_empresa));
                                params4.add(new BasicNameValuePair("sCodUsuario", cod_usuario));
                                params4.add(new BasicNameValuePair("sCodAtributo", cod_atributo));
                                params4.add(new BasicNameValuePair("sCodValor", texto.getText().toString()));
                                params4.add(new BasicNameValuePair("sParametro", "update3"));

                                String resultServer4 = getHttpPost(url4, params4);
                                System.out.println(resultServer4);


                                try {
                                    Toast.makeText(Validacion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
                                    pasar(conexion, general);
                                }catch (Exception e){
                                    Toast.makeText(Validacion.this, "Dont Update Data Successfully", Toast.LENGTH_SHORT).show();
                                }
                            } else {





                                final AlertDialog.Builder ad = new AlertDialog.Builder(Validacion.this);

                                ad.setTitle("Error! ");
                                ad.setIcon(android.R.drawable.btn_star_big_on);
                                ad.setPositiveButton("Close", null);
                                InputStream isr = null;
                                String url = "http://" + finalIp + "/update.php";

                                List<NameValuePair> params = new ArrayList<NameValuePair>();


                                params.add(new BasicNameValuePair("sCodEmpresa", cod_empresa));
                                params.add(new BasicNameValuePair("sCodSucursal", cod_sucursal));
                                params.add(new BasicNameValuePair("sCodUsuario", cod_usuario));
                                params.add(new BasicNameValuePair("sCodProceso", cod_proceso));
                                params.add(new BasicNameValuePair("sCodAtributo", cod_atributo));
                                params.add(new BasicNameValuePair("sCodValor", texto.getText().toString()));
                                params.add(new BasicNameValuePair("sNumOrden", num_orden));



                                // params.add(new BasicNameValuePair("sNumOrden", num_orden));


                                if (resultado.get(4).equals("NIT")) {
                                    String cedula = texto.getText().toString().replaceAll("\n", "");
                                    String cedulafinal = "";

                                /*
                                Character  numAux = cedula.charAt(50);
                                String num = numAux.toString();
                                while (num.charAt(0)>='0' && num.charAt(0)<='9'){
                                    cedulafinal = cedulafinal + num.charAt(0);
                                }
                                /*for (int i = 50;i<58;i++){
                                    cedulafinal = cedulafinal+cedula.charAt(i);
                                }*/

                                    String url3 = "http://" + finalIp + ":8080/update2.php";
                                    String resultServer3;
                                    try {

                                        Cedula = cedula.substring(47, 58);
                                        nombre1 = cedula.substring(95, 116);
                                        nombre2 = cedula.substring(117, 140);
                                        apellido1 = cedula.substring(58, 71);
                                        apellido2 = cedula.substring(72, 94);
                                        if (Cedula.substring(1, 2).equals("00")) {
                                            Cedula = cedula.substring(50, 58);
                                        }


                                        List<NameValuePair> params3 = new ArrayList<NameValuePair>();
                                        params3.add(new BasicNameValuePair("sNit", Cedula));
                                        params3.add(new BasicNameValuePair("sApellido1", apellido1));
                                        params3.add(new BasicNameValuePair("sApellido2", apellido2));
                                        params3.add(new BasicNameValuePair("sNombre1", nombre1));
                                        params3.add(new BasicNameValuePair("sNombre2", nombre2));
                                        resultServer3 = getHttpPost(url3, params3);
                                        System.out.println(resultServer3);

                                        String strStatusID = "0";
                                        String strError = "Unknow Status! 3";

                                        JSONObject c3;
                                        try {
                                            c3 = new JSONObject(resultServer3);
                                            strStatusID = c3.getString("StatusID");
                                            strError = c3.getString("Error");
                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                        if (strStatusID.equals("0")) {
                                            ad.setMessage(strError);
                                            ad.show();
                                            return false;
                                        } else {
                                            Toast.makeText(Validacion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
                                            pasar(conexion, general);
                                        }
                                    } catch (Exception e) {
                                        url3 = "http://" + finalIp + "/update.php";
                                        List<NameValuePair> params3 = new ArrayList<NameValuePair>();
                                        params3.add(new BasicNameValuePair("sCodEmpresa", cod_empresa));
                                        params3.add(new BasicNameValuePair("sCodSucursal", cod_sucursal));
                                        params3.add(new BasicNameValuePair("sCodUsuario", cod_usuario));
                                        params3.add(new BasicNameValuePair("sCodProceso", cod_proceso));
                                        params3.add(new BasicNameValuePair("sCodAtributo", cod_atributo));
                                        params3.add(new BasicNameValuePair("sCodValor", texto.getText().toString()));
                                        params3.add(new BasicNameValuePair("sNumOrden", num_orden));
                                        resultServer3 = getHttpPost(url3, params3);
                                        String strStatusID = "0";
                                        String strError = "Unknow Status! 3";

                                        JSONObject c3;
                                        try {
                                            c3 = new JSONObject(resultServer3);
                                            strStatusID = c3.getString("StatusID");
                                            strError = c3.getString("Error");
                                        } catch (JSONException ex) {
                                            // TODO Auto-generated catch block
                                            ex.printStackTrace();
                                        }
                                        if (strStatusID.equals("0")) {
                                            ad.setMessage(strError);
                                            ad.show();
                                            return false;
                                        } else {
                                            Toast.makeText(Validacion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
                                            pasar(conexion, general);
                                        }
                                    }


                                } else {
                                    if (resultado.get(4).equals("MATRICULA")) {
                                        String matricula = texto.getText().toString().replaceAll("\n", "");
                                        matricula = matricula.replaceAll(" ", "");
                                        System.out.println("LONGITUD : " + matricula.length());
                                        System.out.println(texto.getText().toString());
                                        String matriculafinal = "";

                                /*
                                Character  numAux = cedula.charAt(50);
                                String num = numAux.toString();
                                while (num.charAt(0)>='0' && num.charAt(0)<='9'){
                                    cedulafinal = cedulafinal + num.charAt(0);
                                }
                                /*for (int i = 50;i<58;i++){
                                    cedulafinal = cedulafinal+cedula.charAt(i);
                                }*/
                                        try {
                                            placa = matricula.substring(55, 61);
                                            vin = matricula.substring(61, 78);

                                            texto.setText("");

                                            Intent intent = new Intent((Context) Validacion.this, (Class) MostrarDatosMatricula.class);
                                            Bundle bundle2 = new Bundle();
                                            bundle2.putString("placa", placa);
                                            bundle2.putString("vin", vin);
                                            intent.putExtras(bundle2);
                                            Validacion.this.startActivity(intent);
                                        } catch (Exception e) {
                                            e.printStackTrace();

                                            ad.setTitle("Error! ");
                                            ad.setIcon(android.R.drawable.btn_star_big_on);
                                            ad.setPositiveButton("Close", null);
                                            String url5 = "http://" + finalIp + "/update.php";

                                            List<NameValuePair> params5 = new ArrayList<NameValuePair>();


                                            params5.add(new BasicNameValuePair("sCodEmpresa", cod_empresa));
                                            params5.add(new BasicNameValuePair("sCodSucursal", cod_sucursal));
                                            params5.add(new BasicNameValuePair("sCodUsuario", cod_usuario));
                                            params5.add(new BasicNameValuePair("sCodProceso", cod_proceso));
                                            params5.add(new BasicNameValuePair("sCodAtributo", cod_atributo));
                                            params5.add(new BasicNameValuePair("sCodValor", texto.getText().toString()));
                                            params5.add(new BasicNameValuePair("sNumOrden", num_orden));
                                            String resultServer = getHttpPost(url, params);
                                            System.out.println(resultServer);

                                            String strStatusID = "0";
                                            String strError = "Unknow Status!";

                                            JSONObject c;
                                            try {
                                                Toast.makeText(Validacion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
                                                pasar(conexion, general);
                                            }catch(Exception e1){
                                                Toast.makeText(Validacion.this, "Dont Update Data Successfully", Toast.LENGTH_SHORT).show();

                                            }
                                        }

                                    } else {
                                        String resultServer = getHttpPost(url, params);
                                        System.out.println(resultServer);


                                        JSONObject c;
                                        try {
                                            Toast.makeText(Validacion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
                                            pasar(conexion, general);
                                        }catch(Exception e){
                                            Toast.makeText(Validacion.this, "Dont Update Data Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }


                                String file = "storage/sdcard0/METRO/" + nombre;
                                File mi_foto = new File(file);
                                String file2 = "storage/sdcard0/METRO/";
                                File mi_foto2 = new File(file2);
                                mi_foto.renameTo(mi_foto2);

                            }//else{
                            //    Toast.makeText(getApplicationContext()," Este registro ya esta en la base de datos !" , Toast.LENGTH_SHORT).show();
                            //}
                            // }
                        }
                        if (cod_atributo.equals("ROMBO")){
                            num_rombo = texto.getText().toString();
                        }
                        if (cod_atributo.equals("PLACA")){
                            cod_placa = texto.getText().toString();
                        }
                        if(cod_atributo.equals("UBICACION")){
                            cod_ubicacion = texto.getText().toString();
                        }
                        if(cod_atributo.equals("NIT")){
                            num_nit = texto.getText().toString();
                        }
                    }
                    return false;
                }

            });



        }
    }

    public void ver (View v){
        Intent intent = new Intent((Context) Validacion.this, (Class) MostrarDatosNit.class);
        Bundle bundle2 = new Bundle();
        bundle2.putString("cedula", Cedula);
        bundle2.putString("nombre1", nombre1);
        bundle2.putString("nombre2", nombre2);
        bundle2.putString("apellido1",apellido1);
        bundle2.putString("apellido2", apellido2);
        intent.putExtras(bundle2);
        Validacion.this.startActivity(intent);
    }

    public void pasar(String conexion,final String[][] general){
        if (conexion.equals("local")) {
            final DataBaseHelper dataBaseHelper = new DataBaseHelper((Context) this);
            try {
                dataBaseHelper.createDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                dataBaseHelper.openDataBase();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            final String[][] general2;
            general2 = dataBaseHelper.consultarProceso("123", "METRO", "10", "01");

            if (indice == general2.length - 1) {
                indice = 0;
                actualizar(general2, indice, conexion);
            } else {
                indice++;
                actualizar(general2, indice, conexion);
            }
        }else{
            if (indice == general.length - 1) {
                indice = 0;
                actualizar(general, indice, conexion);
            } else {
                indice++;
                actualizar(general, indice, conexion);
            }
        }
    }

    public void ok(String cod_empresa,String cod_sucursal,String cod_proceso
            ,String cod_usuario,
                   String ind_estado){
        Bundle bundle = new Bundle();
        Intent intent = new Intent((Context) this, (Class)GuardarValidacion.class);
        bundle.putString("cod_empresa", cod_empresa);
        bundle.putString("cod_sucursal", cod_sucursal);
        bundle.putString("proceso", proceso);
        bundle.putString("cod_placa", cod_placa);
        System.out.println(" AQUI ES EL ERROR " + num_rombo);
        bundle.putString("num_rombo", num_rombo);
        bundle.putString("cod_usuario", cod_usuario);
        bundle.putString("cod_ubicacion", cod_ubicacion);
        bundle.putString("fec_proceso", fec_proceso);
        bundle.putString("num_nit", num_nit);
        bundle.putString("ind_estado", ind_estado);
        bundle.putStringArrayList("atributos", atributos);
        bundle.putString("ip",ip);
        intent.putExtras(bundle);
        this.startActivity(intent);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if ((requestCode == request_code) && (resultCode == RESULT_OK)){
            EditText texto = (EditText) findViewById(R.id.editText_texto);
            texto.setText(data.getDataString());
        }
    }

    public void onDestroy() {
        super.onDestroy();

    }
}