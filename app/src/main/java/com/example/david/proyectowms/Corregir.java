package com.example.david.proyectowms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.InputType;
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class Corregir extends Activity {
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
    String nombre = "";
    String cod_placa = "";
    String num_rombo= "";
    String cod_ubicacion = "";
    String fec_proceso = "";
    String num_nit = "";
    String sucursal;
    int request_code = 1;
    ArrayList  atributos ;
    String titulo = "";
    String empresa;
    EditText texto;
    String [][] generalAux;
    String [][] general;
    String proceso = "";
    int date ;
    String fechaFoto;
    String dia ;
    String dirFoto;
    int longitud;
    String tiempoPromesa;
    String kilometraje = "";
    String sInformativo = "";
    String parametro = "";
    String pass;
    String user;
    String km = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recepcion);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Bundle bundle = this.getIntent().getExtras();
        user = bundle.getString("user");
        pass = bundle.getString("pass");
        conexion = bundle.getString("conexion");
        titulo = bundle.getString("titulo");
        ip = bundle.getString("ip");
        proceso = bundle.getString("proceso");
        num_rombo = bundle.getString("num_rombo");
        sucursal = bundle.getString("sucursal");
        empresa = bundle.getString("empresa");
        System.out.println("sucursal y empresa ------------>" + sucursal + empresa);
        String url1 = "http://"+  ip + "/delete.php";

        atributos = new ArrayList();
        fechaFoto = getCode();

        Button buscar = (Button) findViewById(R.id.button_buscar);
        Button añadir = (Button) findViewById(R.id.button_anadir);


        buscar.setVisibility(View.INVISIBLE);
        añadir.setVisibility(View.INVISIBLE);


        List<NameValuePair> params1 = new ArrayList<NameValuePair>();

        String resultServer1 = getHttpPost(url1, params1);

        JSONObject c2;


        setTitle(titulo);

        texto = (EditText) findViewById(R.id.editText_texto);
        TextView atributo = (TextView) findViewById(R.id.textView_atributo);

        if (atributo.getText().equals("ROMBO")) {
            texto.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        texto.requestFocus();

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
            general = dataBaseHelper.consultarProceso("123", "METRO", sucursal, "01");
            actualizar(general, indice,conexion);
            texto.requestFocus();



            final ImageButton button = (ImageButton) findViewById(R.id.imageButton8);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (indice == general.length - 1) {
                    } else {
                        indice++;
                        actualizar(general, indice,conexion);
                        texto.requestFocus();
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
                        texto.requestFocus();
                    }
                }
            });

            final ImageButton button3 = (ImageButton) findViewById(R.id.imageButton6);
            button3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    indice = 0;
                    actualizar(general, indice,conexion);
                    texto.requestFocus();
                }
            });

            final ImageButton button4 = (ImageButton) findViewById(R.id.imageButton9);
            button4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    indice = general.length - 1;
                    actualizar(general, indice,conexion);
                    texto.requestFocus();
                }
            });
        }else{

            String url = "http://" + ip + "/consultarGeneral.php";

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("sCodUser", pass));
            params.add(new BasicNameValuePair("sCodEmpresa", empresa));
            params.add(new BasicNameValuePair("sCodSucursal", sucursal));
            params.add(new BasicNameValuePair("sIndEstado", "A"));
            params.add(new BasicNameValuePair("sCodProceso", proceso));
            params.add(new BasicNameValuePair("sParametro", "consultarProceso"));

            String resultServer  = getHttpPost(url,params);
            try {
                JSONArray jArray = new JSONArray(resultServer);
                generalAux = new String[jArray.length()][18];

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
                    //generalAux[i][j+18] = json.getString("nom_tabla");
                }

                for (int i = 0; i < generalAux.length; i++) {
                    for (j = 0; j < generalAux[i].length; j++) {

                        //System.out.println("GENERALAUX->" + generalAux[i][j]);
                        //System.out.println("-------------------");

                    }
                }


                int contador = 0;

                for (int i = 0; i < generalAux.length; i++) {
                    for (j = 0; j < generalAux[i].length; j++) {
                        if (generalAux[i][j].equals("null")){
                            generalAux[i][j] = "";
                        }

                        //System.out.println("GENERAL->" + general[i][j]);
                    }
                    if (generalAux[i][17].equals("I")){
                        contador++;
                    }
                }

                System.out.println(longitud + " LONGITUD ");
                System.out.println(contador + " CONTADOR ");


                general = new String[longitud-contador][18];

                for (int i = 0; i < general.length; i++) {
                    for (j = 0; j < general[i].length; j++) {
                        general[i][j] = generalAux[i][j];
                        System.out.println("GENERAL->" + general[i][j]);
                        System.out.println("----------------------------");
                        System.out.println("GENERALAUX->" + generalAux[i][j]);
                    }
                }

                String url2 = "http://" + ip + "/consultarGeneral.php";

                List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                params2.add(new BasicNameValuePair("sNumRombo", num_rombo));
                params2.add(new BasicNameValuePair("sParametro", "consultarCorregir"));

                String resultServer2  = getHttpPost(url2,params2);
                try {
                    JSONArray jArray2 = new JSONArray(resultServer2);
                    longitud = jArray2.length();
                    for (int i = 0; i < jArray2.length(); i++) {
                        JSONObject json = jArray2.getJSONObject(i);

                        if (general[indice][4].trim().equals("ROMBO")) {
                            general[indice][5] = num_rombo;
                        }
                        if (general[indice][4].trim().equals("PLACA")) {
                            general[indice][5] = json.getString("cod_placa");
                        }
                        if (general[indice][4].trim().equals("UBICACION")) {
                            general[indice][5] = json.getString("cod_ubicacion");
                        }
                        if (general[indice][4].trim().equals("NIT")) {
                            general[indice][5] = json.getString("num_nit");
                        }
                        if (general[indice][4].trim().equals("KM")) {
                            general[indice][5] = json.getString("kilometraje");
                        }


                        //generalAux[i][j+18] = json.getString("nom_tabla");
                    }
                }catch (Exception e){

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
                        ok(cod_empresa,cod_sucursal,cod_proceso,cod_usuario,ind_estado,general);
                    }
                });

                final ImageButton button = (ImageButton) findViewById(R.id.imageButton8);
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (indice == general.length - 1) {
                        } else {
                            indice++;
                            actualizar(general, indice, conexion);
                            texto.requestFocus();
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
                            texto.requestFocus();
                        }
                    }
                });

                final ImageButton button3 = (ImageButton) findViewById(R.id.imageButton6);
                button3.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        indice = 0;
                        actualizar(general, indice,conexion);
                        texto.requestFocus();
                    }
                });

                final ImageButton button4 = (ImageButton) findViewById(R.id.imageButton9);
                button4.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        indice = general.length - 1;
                        actualizar(general, indice, conexion);
                        texto.requestFocus();
                    }
                });
            }catch (JSONException e ){
                e.printStackTrace();
            }
        }

        texto.requestFocus();

    }



    public void camara(View v) {

        //Si no existe crea la carpeta donde se guardaran las fotos
        //file.mkdirs();
        //String file = ruta_fotos + getCode() + ".jpg";
        TextView texto = (TextView) findViewById(R.id.textView_atributo);


        for (int i = 0; i < general.length; i++) {
            for (int j = 0; j < general[i].length; j++) {
                if (general[i][j].equals("null")){
                    general[i][j] = "";
                }
                System.out.println("GENERAL->" + general[i][j]);
            }
        }

        nombre = new String();

        nombre = general[indice][4].trim() + "_" + fechaFoto;


        System.out.println("ESTE ES EL NOMBRE -> " + nombre + " general -> " +general[indice][4].trim());



        dirFoto =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)  + "/METRO/" + getDia() + "/";

        //String file = "/storage/E6E7-2A20/METRO/" + nombre;

        System.out.println(dirFoto);

        File fecha = new File(dirFoto);
        fecha.mkdirs();

        String file = dirFoto + nombre +  ".jpg";


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
            String strPath = dirFoto + nombre +  ".jpg";
            //String strPath = "/storage/E6E7-2A20/METRO/" + nombre;
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;


            Bitmap bm = BitmapFactory.decodeFile(strPath,options);

            myImage.setImageBitmap(bm);
            myImage.invalidate();

            general[indice][13] = file;
            mostrarImagen();
        } catch (IOException ex) {
            Log.e("ERROR ", "Error:" + ex);
        }
    }

    private String getCode() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String date = dateFormat.format(new Date());
        return date;
    }

    private String getDia() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateFormat.format(new Date());
        return date;
    }

    public void mostrarImagen(){
        ImageView myImage = (ImageView) findViewById(R.id.imageView);
        nombre = new String();

        nombre = general[indice][4].trim() + "_" + fechaFoto;
        //File imgFile = new  File(ruta_fotos + getCode() + ".jpg");
        String strPath = dirFoto + nombre +  ".jpg";
        System.out.println(strPath);
        //String strPath = "/storage/E6E7-2A20/METRO/" + nombre;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;

        Bitmap bm = BitmapFactory.decodeFile(strPath, options);


        if (bm==null) {
            myImage.setImageResource(R.drawable.camera);
        }else{
            myImage.setImageBitmap(bm);
        }
        /*try {
            if (imgFile.exists()) {
                ImageView myImage = (ImageView) findViewById(R.id.imageView);
                myImage.setImageURI(Uri.fromFile(imgFile));

            }
        }catch (Exception e){
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

        general = dataBaseHelper.consultarProceso("123", "METRO", sucursal, "1");
        if (indice==general.length-1){

        }else {
            indice++;
            actualizar(general, indice,conexion);
        }
    }





    public void cancelar(View v){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Saliendo...")
                .setMessage("Esta Seguro que desea dejar de recibir?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
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
        general = dataBaseHelper.consultarProceso("123", "METRO", sucursal, "01");
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
        Toast.makeText(Corregir.this, (CharSequence) "se guardo correctamente!", Toast.LENGTH_SHORT).show();
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
        general = dataBaseHelper.consultarProceso("123", "METRO", sucursal, "01");
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
        general = dataBaseHelper.consultarProceso("123", "METRO", sucursal, "01");
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
        general = dataBaseHelper.consultarProceso("123", "METRO", sucursal, "01");
        indice= 0;
        actualizar(general, indice, conexion);
    }


    public void actualizar(final String[][] general, final int indice, final String conexion){



        final TextView error = (TextView) findViewById(R.id.textView_error);
        //final TextView informativo = (TextView) findViewById(R.id.textView_informativo);
        //informativo.setTextColor(Color.rgb(21,130,125));
        error.setText("");
        //informativo.setText("");

        final ArrayList resultado = new ArrayList();
        final TextView atributo = (TextView) findViewById(R.id.textView_atributo);
        final ImageView imagen = (ImageView) findViewById(R.id.imageView);



        texto.setText(general[indice][5].trim());

        Button buscar = (Button) findViewById(R.id.button_buscar);
        Button anadir = (Button) findViewById(R.id.button_anadir);

        if ( general[indice][15].trim().equals(general[indice][3].trim())) {
            buscar.setVisibility(View.INVISIBLE);
            anadir.setVisibility(View.INVISIBLE);
        }else{
            imagen.setImageResource(R.drawable.add);
            buscar.setVisibility(View.VISIBLE);
            anadir.setVisibility(View.VISIBLE);
        }


        //for(int j = 0;j<general[0].length;j++){
        //    resultado.add(general[indice][j]);
        //}


       /* String url = "http://" + ip + "/consultarGeneral.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sCodUser", resultado.get(2).toString().trim()));
        params.add(new BasicNameValuePair("sCodEmpresa", resultado.get(0).toString().trim()));
        params.add(new BasicNameValuePair("sCodAtributo", resultado.get(4).toString().trim()));
        params.add(new BasicNameValuePair("sCodSucursal",sucursal));
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
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                array.add((json.getString("cod_valor")));
            }
            texto.setText(array.get(0));
        }catch (JSONException e){
            e.printStackTrace();
        }*/

        //texto.setText(general[indice][5].trim());


        atributo.setText(general[indice][4].trim());
        //atributo.setText(resultado.get(7) + ". " + resultado.get(4).toString().trim());

        if(general[indice][5].trim().equals("ROMBO")) imagen.setImageResource(R.drawable.rombo);
        if(general[indice][5].trim().equals("PLACA")) imagen.setImageResource(R.drawable.placa6);
        if(general[indice][5].trim().equals("UBICACION")) imagen.setImageResource(R.drawable.ubicacion);
        //if (resultado.get(13).equals("ROMBO.JPG")) imagen.setImageResource(R.drawable.rombo1);
        //if (resultado.get(13).equals("PLACA.JPG")) imagen.setImageResource(R.drawable.placa6);
        //if (resultado.get(13).equals("NIT.JPG")) imagen.setImageResource(R.drawable.cedula1);
        /*if (Integer.parseInt(resultado.get(0).toString()) > 0){
            int cont = 0;

        }*/

        mostrarImagen();

        texto.requestFocus();

        final String cod_empresa = general[indice][0];
        final String cod_sucursal =general[indice][1];
        final String cod_usuario = general[indice][2];
        final String cod_proceso = general[indice][3];
        final String[] cod_atributo = {general[indice][4]};
        final String cod_valor = general[indice][5];
        final String num_orden = general[indice][7];

        /*final String cod_empresa = resultado.get(0).toString();
        final String cod_sucursal = resultado.get(1).toString();
        final String cod_usuario = resultado.get(2).toString();
        final String cod_proceso = resultado.get(3).toString();
        final String cod_atributo = resultado.get(4).toString();
        final String cod_valor = resultado.get(5).toString();
        final String num_orden = resultado.get(7).toString();*/


        if (conexion.equals("local")) texto.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    /*if (texto.getText().length() == 0 && (cod_atributo.equals("ROMBO") || (cod_atributo.equals("UBICACION")) )) {
                        Intent intent = new Intent((Context) Recepcion.this, (Class) MostrarRombUbic.class);
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("conexion", conexion);
                        Recepcion.this.startActivityForResult(intent,request_code);

                    } else {*/

                    final DataBaseHelper dataBaseHelper = new DataBaseHelper((Context) Corregir.this);
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
                        dataBaseHelper.guardarProceso(cod_empresa, cod_sucursal, cod_usuario, cod_proceso, cod_atributo[0], texto.getText().toString(), num_orden);
                        dataBaseHelper.close();
                        pasar(conexion, general);
                        return true;
                        //actualizar(general,indice);
                    }
                    //}
                }
                return false;
            }
        });
        else {


            final String finalIp = ip;

            final GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
                public boolean onDoubleTap(MotionEvent e) {
                    if (texto.getText().toString().matches("") && (cod_atributo[0].trim().equals("ROMBO") || cod_atributo[0].trim().equals("UBICACION") || cod_atributo[0].trim().equals("TECNICO") || cod_atributo[0].trim().equals("NUMERO OT")) || cod_atributo[0].trim().equals("PROMESA") || cod_atributo[0].trim().equals("RAZON2")) {
                        Intent intent = new Intent((Context) Corregir.this, (Class) MostrarRombUbic.class);
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("conexion", conexion);
                        bundle2.putString("ip", ip);
                        bundle2.putString("atributo", cod_atributo[0].trim());
                        bundle2.putString("empresa", cod_empresa);
                        bundle2.putString("sucursal", cod_sucursal);
                        intent.putExtras(bundle2);
                        Corregir.this.startActivityForResult(intent, request_code);
                        Toast.makeText(getApplicationContext(), general[indice][4] + "  ATRIBUTO ", Toast.LENGTH_SHORT).show();
                    }//else{
                       /* if (general[indice][9].toString().equals("F")){
                            Intent intent = new Intent((Context) Recepcion.this, (Class) Calendario.class);
                            Recepcion.this.startActivityForResult(intent,request_code);
                        }
                    }*/

                        /*String url2 = "http://" + ip + "/consultarGeneral.php";

                        List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                        params2.add(new BasicNameValuePair("sCodUser", resultado.get(2).toString()));
                        params2.add(new BasicNameValuePair("sCodEmpresa", resultado.get(0).toString()));
                        params2.add(new BasicNameValuePair("sCodAtributo", resultado.get(4).toString()));
                        params2.add(new BasicNameValuePair("sCodSucursal",sucursal));
                        params2.add(new BasicNameValuePair("sParametro", "actualizar"));


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

                                Toast.makeText(Recepcion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
                                pasar(conexion, general);
                            }catch(Exception e1){
                                Toast.makeText(Recepcion.this, "Dont Update Data Successfully", Toast.LENGTH_SHORT).show();
                            }
                        } else {





                            final AlertDialog.Builder ad = new AlertDialog.Builder(Recepcion.this);

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

                            */

                    // params.add(new BasicNameValuePair("sNumOrden", num_orden));


                            /*if (resultado.get(4).equals("NIT")) {
                                String cedula = texto.getText().toString().replaceAll("\n", "");
                                String cedulafinal = "";

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
                                        Toast.makeText(Recepcion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(Recepcion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
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


                                Character  numAux = cedula.charAt(50);
                                String num = numAux.toString();
                                while (num.charAt(0)>='0' && num.charAt(0)<='9'){
                                    cedulafinal = cedulafinal + num.charAt(0);
                                }

                                    try {
                                        placa = matricula.substring(55, 61);
                                        vin = matricula.substring(61, 78);

                                        texto.setText("");

                                        Intent intent = new Intent((Context) Recepcion.this, (Class) MostrarDatosMatricula.class);
                                        Bundle bundle2 = new Bundle();
                                        bundle2.putString("placa", placa);
                                        bundle2.putString("vin", vin);
                                        intent.putExtras(bundle2);
                                        Recepcion.this.startActivity(intent);
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
                                            Toast.makeText(Recepcion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
                                            pasar(conexion, general);
                                        }catch(Exception e1){
                                            Toast.makeText(Recepcion.this, "Dont Update Data Successfully", Toast.LENGTH_SHORT).show();

                                        }
                                    }

                                } else {
                                    String resultServer = getHttpPost(url, params);
                                    System.out.println(resultServer);

                                    String strStatusID = "0";
                                    String strError = "Unknow Status!";

                                    JSONObject c;
                                    try {

                                        Toast.makeText(Recepcion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();

                                        pasar(conexion, general);
                                    }catch(Exception e1){
                                        Toast.makeText(Recepcion.this, "Dont Update Data Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }*/


                            /*String file = "storage/sdcard0/METRO/" + nombre;
                            File mi_foto = new File(file);
                            String file2 = "storage/sdcard0/METRO/";
                            File mi_foto2 = new File(file2);
                            mi_foto.renameTo(mi_foto2);*/
                    //else{
                    //    Toast.makeText(getApplicationContext()," Este registro ya esta en la base de datos !" , Toast.LENGTH_SHORT).show();
                    //}
                    // }

                    return true;
                }
            });

            /*if ( !(general[indice][18].trim().equals(general[indice][3].trim()))) {




                Button BotonBuscar = (Button) findViewById(R.id.button_buscar);
                BotonBuscar.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        if (general[indice][15].trim().equals("2")){
                            cod_atributo[0] = "CL";
                        }


                        Intent intent = new Intent((Context) Recepcion.this, (Class) MostrarRombUbic.class);
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("conexion", conexion);
                        bundle2.putString("ip", ip);
                        bundle2.putString("atributo", cod_atributo[0]);
                        bundle2.putString("empresa", cod_empresa);
                        bundle2.putString("sucursal", sucursal);

                        bundle2.putString("parametro", parametro);
                        intent.putExtras(bundle2);
                        Recepcion.this.startActivityForResult(intent, request_code);
                        general[indice][5] = texto.getText().toString().trim();
                        //pasar(conexion, general);
                        error.setText("");
                    }
                });

                Button Botonanadir = (Button) findViewById(R.id.button_anadir);
                Botonanadir.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        proceso = general[indice][15].trim();
                        if (general[indice][15].trim().equals("1")){
                            titulo = "CLIENTES";
                        }
                        if (general[indice][15].trim().equals("2")){
                            titulo = "CARGOS";
                        }
                        if (general[indice][15].trim().equals("3")){
                            titulo = "CIUDADES";
                        }
                        if (general[indice][15].trim().equals("4")){
                            titulo = "IMPUESTOS";
                        }

                        System.out.println(user + " " + pass + " " + proceso);
                        Intent intent = new Intent((Context) Recepcion.this, (Class) Recepcion.class);
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("user", user);
                        bundle2.putString("pass", pass);
                        bundle2.putString("sucursal", sucursal);
                        bundle2.putString("empresa", empresa);
                        bundle2.putString("conexion", conexion);
                        bundle2.putString("sucursal", sucursal);
                        bundle2.putString("ip", ip);
                        bundle2.putString("titulo", titulo);
                        bundle2.putString("proceso", proceso);
                        intent.putExtras(bundle2);
                        startActivity(intent);
                    }
                });



            }else {
            }*/

            texto.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
                }
            });
            texto.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {


                        /*if (texto.getText().toString().matches("") && (general[indice][5].equals("ROMBO") || general[indice][5].equals("UBICACION") || general[indice][5].equals("MECANICO"))) {
                            Intent intent = new Intent((Context) Recepcion.this, (Class) MostrarRombUbic.class);
                            Bundle bundle2 = new Bundle();
                            bundle2.putString("conexion", conexion);
                            bundle2.putString("ip", ip);
                            bundle2.putString("atributo", cod_atributo);
                            intent.putExtras(bundle2);
                            Recepcion.this.startActivityForResult(intent, request_code);
                            Toast.makeText(getApplicationContext(), resultado.get(4) + "  ATRIBUTO ", Toast.LENGTH_SHORT).show();

                        } else {*/
                            /*String url2 = "http://" + ip + "/consultarGeneral.php";

                            List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                            params2.add(new BasicNameValuePair("sCodUser", general[indice][2].toString()));
                            params2.add(new BasicNameValuePair("sCodEmpresa", general[indice][0].toString()));
                            params2.add(new BasicNameValuePair("sCodAtributo", general[indice][4].toString()));
                            params2.add(new BasicNameValuePair("sCodSucursal",sucursal));
                            params2.add(new BasicNameValuePair("sParametro", "actualizar"));


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
                                    Toast.makeText(Recepcion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
                                    pasar(conexion, general);
                                }catch (Exception e){
                                    Toast.makeText(Recepcion.this, "Dont Update Data Successfully", Toast.LENGTH_SHORT).show();
                                }
                            } else {*/





                                /*final AlertDialog.Builder ad = new AlertDialog.Builder(Recepcion.this);

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

                                String resultServer = getHttpPost(url, params);
                                System.out.println(resultServer);


                                JSONObject c;
                                try {
                                    Toast.makeText(Recepcion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
                                    pasar(conexion, general);
                                }catch(Exception e){
                                    Toast.makeText(Recepcion.this, "Dont Update Data Successfully", Toast.LENGTH_SHORT).show();
                                }*/

                        String atributo = texto.getText().toString().trim();
                        error.setTextColor(Color.RED);

                        String file = "storage/sdcard0/METRO/" + nombre;
                        File mi_foto = new File(file);
                        String file2 = "storage/sdcard0/METRO/";
                        File mi_foto2 = new File(file2);
                        mi_foto.renameTo(mi_foto2);

                        EditText texto1 = (EditText) findViewById(R.id.editText_texto);

                        if (general[indice][4].trim().equals("ROMBO")) {
                            num_rombo = general[indice][5].trim();
                        }
                        if (general[indice][4].trim().equals("PLACA")) {
                            cod_placa = texto.getText().toString().trim();
                        }
                        if (general[indice][4].trim().equals("UBICACION")) {
                            cod_ubicacion = general[indice][5].trim();
                        }
                        if (general[indice][4].trim().equals("NIT")) {
                            num_nit = texto.getText().toString().trim();
                        }
                        if (general[indice][4].trim().equals("KM")) {
                            kilometraje = general[indice][5].trim();
                        }


                        general[indice][5] = texto.getText().toString().trim();
                        pasar(conexion, general);
                        error.setText("");

                            /*if (general[indice][4].trim().equals("CEDULA")) {
                                String cedula = texto.getText().toString().replaceAll("\n", "");


                                try {
                                    Cedula = cedula.substring(47, 58);
                                    nombre1 = cedula.substring(95, 116);
                                    nombre2 = cedula.substring(117, 140);
                                    apellido1 = cedula.substring(58, 71);
                                    apellido2 = cedula.substring(72, 94);
                                    if (Cedula.substring(1, 2).equals("00")) {
                                        Cedula = cedula.substring(50, 58);
                                    }


                                    if (general[indice + 1][4].trim().equals("NOMBRES")) {
                                        general[indice + 1][5] = nombre1 + " " + nombre2 + " " + apellido1 + " " + apellido2;

                                    }

                                    texto.setText(Cedula);
                                    general[indice][5] = texto.getText().toString().trim();
                                    pasar(conexion, general);
                                    error.setText("");
                                } catch (Exception e) {
                                    general[indice][5] = texto.getText().toString().trim();
                                    pasar(conexion, general);
                                    error.setText("");
                                    e.printStackTrace();
                                }
                            } else {

                                if (general[indice][4].trim().equals("NOMBRES")) {
                                    String cedula = texto.getText().toString().replaceAll("\n", "");


                                    try {
                                        Cedula = cedula.substring(47, 58);
                                        nombre1 = cedula.substring(95, 116);
                                        nombre2 = cedula.substring(117, 140);
                                        apellido1 = cedula.substring(58, 71);
                                        apellido2 = cedula.substring(72, 94);
                                        Toast.makeText(getApplicationContext(),Cedula.substring(1, 3),Toast.LENGTH_LONG).show();
                                        if (Cedula.substring(1, 3).equals("00")) {
                                            Cedula = cedula.substring(50, 58);
                                        }

                                        if (general[indice + 1][4].trim().equals("CEDULA")) {
                                            general[indice + 1][5] = Cedula;
                                        }


                                        texto.setText(remove(nombre1) + " " + remove(nombre2) + " " + remove(apellido1) + " " + remove(apellido2));
                                        general[indice][5] = remove(nombre1) + " " + remove(nombre2) + " " + remove(apellido1) + " " + remove(apellido2);
                                        pasar(conexion, general);
                                        error.setText("");
                                    } catch (Exception e) {
                                        general[indice][5] = texto.getText().toString().trim();
                                        pasar(conexion, general);
                                        error.setText("");
                                        e.printStackTrace();
                                    }
                                } else {


                                    if (general[indice][4].trim().equals("KM")) {

                                        String url = "http://" + ip + "/consultarGeneral.php";

                                        List<NameValuePair> params = new ArrayList<NameValuePair>();

                                        params.add(new BasicNameValuePair("sParametro", "KM"));
                                        params.add(new BasicNameValuePair("sPlaca", cod_placa));

                                        String resultServer = getHttpPost(url, params);
                                        System.out.println("---------------------------------resultserver----------------");
                                        System.out.println(resultServer);
                                        String km_anterior = "";
                                        try {

                                            JSONArray jArray = new JSONArray(resultServer);
                                            ArrayList<String> array = new ArrayList<String>();
                                            for (int i = 0; i < jArray.length(); i++) {
                                                JSONObject json = jArray.getJSONObject(i);
                                                array.add(json.getString("km_anterior"));
                                                km_anterior = array.get(0);
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        if (km_anterior.equals("null")) km_anterior = "1";

                                        if (Integer.parseInt(km_anterior.toString()) < Integer.parseInt(texto.getText().toString())) {

                                            general[indice][5] = texto.getText().toString().trim();
                                            pasar(conexion, general);
                                            error.setText("");
                                        } else {
                                            informativo.setText("Km Anterior : " + km_anterior);
                                            //informativo.setTextColor(Color.BLUE);
                                            error.setText("No es un km permitido! es menor que el anterior");
                                        }
                                    } else {

                                        if (general[indice][4].trim().equals("NIT")) {
                                            String url = "http://" + ip + "/consultarGeneral.php";

                                            List<NameValuePair> params = new ArrayList<NameValuePair>();

                                            params.add(new BasicNameValuePair("sParametro", "NIT"));
                                            params.add(new BasicNameValuePair("sNit", num_nit));

                                            String resultServer = getHttpPost(url, params);
                                            System.out.println("---------------------------------resultserver----------------");
                                            System.out.println(resultServer);
                                            String nombre = "";
                                            try {

                                                JSONArray jArray = new JSONArray(resultServer);
                                                ArrayList<String> array = new ArrayList<String>();
                                                for (int i = 0; i < jArray.length(); i++) {
                                                    JSONObject json = jArray.getJSONObject(i);
                                                    array.add(json.getString("nombres"));
                                                    sInformativo = array.get(0);
                                                }

                                            } catch (JSONException e) {
                                                informativo.setText("No existe el nombre!");
                                                e.printStackTrace();
                                            }

                                            if (sInformativo.equals("null") || (sInformativo.equals(""))) {
                                                nombre = "";
                                                informativo.setText("No existe el nombre!");

                                            } else {
                                                informativo.setText(sInformativo);
                                                //informativo.setTextColor(Color.BLUE);
                                                general[indice][5] = texto.getText().toString().trim();
                                                pasar(conexion, general);
                                                error.setText("");
                                            }

                                        } else {
                                            if (general[indice][4].trim().equals("PLACA")) {
                                                String url = "http://" + ip + "/consultarGeneral.php";

                                                List<NameValuePair> params = new ArrayList<NameValuePair>();

                                                params.add(new BasicNameValuePair("sParametro", "PLACA"));
                                                params.add(new BasicNameValuePair("sPlaca", cod_placa));

                                                String resultServer = getHttpPost(url, params);
                                                System.out.println("---------------------------------resultserver----------------");
                                                System.out.println(resultServer);
                                                String nombre = "";
                                                try {

                                                    JSONArray jArray = new JSONArray(resultServer);
                                                    ArrayList<String> array = new ArrayList<String>();
                                                    for (int i = 0; i < jArray.length(); i++) {
                                                        JSONObject json = jArray.getJSONObject(i);
                                                        array.add(json.getString("nombre"));
                                                        sInformativo = sInformativo + "\n" + array.get(0);
                                                    }

                                                } catch (JSONException e) {

                                                    informativo.setText("");
                                                    informativo.setText("No existe la placa!");
                                                    //informativo.setTextColor(Color.BLUE);
                                                    e.printStackTrace();
                                                }


                                                if (sInformativo.equals("null") || sInformativo.equals("")) {

                                                    informativo.setText("No existe la placa!");
                                                } else {
                                                    informativo.setText(sInformativo);
                                                    //informativo.setTextColor(Color.BLUE);
                                                    general[indice][5] = texto.getText().toString().trim();
                                                    pasar(conexion, general);
                                                    error.setText("");
                                                }
                                            } else {*/
                                                /*if (general[indice][9].trim().equals("E")) {
                                                    if (isNumeric(atributo)) {
                                                        if (Integer.parseInt(atributo) >= Double.parseDouble(general[indice][10]) && Integer.parseInt(atributo) <= Double.parseDouble(general[indice][11])) {
                                                            if (general[indice][4].trim().equals("ROMBO")) {

                                                                String url = "";
                                                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                                                boolean ocupado = false;

                                                                params.add(new BasicNameValuePair("sCodEmpresa", cod_empresa.trim()));
                                                                params.add(new BasicNameValuePair("sCodSucursal", cod_sucursal.trim()));
                                                                url = "http://" + ip + "/consultarRecurso.php";
                                                                params.add(new BasicNameValuePair("sParametro", "R"));
                                                                String resultServer = getHttpPost(url, params);
                                                                System.out.println("---------------------------------");
                                                                System.out.println(resultServer);
                                                                try {
                                                                    JSONArray jArray = new JSONArray(resultServer);
                                                                    ArrayList<String> array = new ArrayList<String>();
                                                                    for (int i = 0; i < jArray.length(); i++) {
                                                                        JSONObject json = jArray.getJSONObject(i);
                                                                        array.add((json.getString("val_recurso")));
                                                                        if (texto.getText().toString().equals(array.get(i).trim())) {
                                                                            ocupado = false;
                                                                            break;
                                                                        } else {
                                                                            ocupado = true;
                                                                        }
                                                                    }


                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }


                                                                if (ocupado) {
                                                                    error.setText("El rombo no esta disponible");
                                                                } else {
                                                                    general[indice][5] = texto.getText().toString().trim();
                                                                    pasar(conexion, general);
                                                                    error.setText("");
                                                                }
                                                            } else {
                                                                general[indice][5] = texto.getText().toString().trim();
                                                                pasar(conexion, general);
                                                                error.setText("");
                                                            }

                                                        } else {
                                                            error.setText("No esta en el rango!");
                                                        }
                                                    } else {
                                                        error.setText("No es numero!");
                                                    }
                                                } else {
                                                    if (general[indice][9].trim().equals("C")) {
                                                        if ((texto.getText().toString().trim().length() >= Double.parseDouble(general[indice][10])) && (texto.getText().toString().trim().length() <= Double.parseDouble(general[indice][11]))) {
                                                            general[indice][5] = texto.getText().toString().trim();
                                                            pasar(conexion, general);
                                                            error.setText("");

                                                        } else {
                                                            if (Double.parseDouble(general[indice][10]) == Double.parseDouble(general[indice][11]) && texto.getText().toString().trim().length() == Double.parseDouble(general[indice][10])) {
                                                                general[indice][5] = texto.getText().toString().trim();
                                                                pasar(conexion, general);
                                                                error.setText("");
                                                            } else {
                                                                error.setText("No esta en el rango!");
                                                            }
                                                            error.setText("No esta en el rango!");
                                                        }
                                                    } else {
                                                        if (general[indice][9].trim().equals("F")) {
                                                            if (general[indice][18].trim().equals("PROMESA_ENTREGA")) {

                                                                date = getHora();
                                                                general[indice][5] = texto.getText().toString().trim();
                                                                pasar(conexion, general);
                                                                error.setText("");
                                                            } else {
                                                                general[indice][5] = texto.getText().toString().trim();
                                                                pasar(conexion, general);
                                                                error.setText("");
                                                            }
                                                        }
                                                    }
                                                }*/


                        // params.add(new BasicNameValuePair("sNumOrden", num_orden));


                                /*if (resultado.get(4).equals("NIT")) {
                                    String cedula = texto.getText().toString().replaceAll("\n", "");
                                    String cedulafinal = "";


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
                                            Toast.makeText(Recepcion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(Recepcion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
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


                                        try {
                                            placa = matricula.substring(55, 61);
                                            vin = matricula.substring(61, 78);

                                            texto.setText("");

                                            Intent intent = new Intent((Context) Recepcion.this, (Class) MostrarDatosMatricula.class);
                                            Bundle bundle2 = new Bundle();
                                            bundle2.putString("placa", placa);
                                            bundle2.putString("vin", vin);
                                            intent.putExtras(bundle2);
                                            Recepcion.this.startActivity(intent);
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
                                                Toast.makeText(Recepcion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
                                                pasar(conexion, general);
                                            }catch(Exception e1){
                                                Toast.makeText(Recepcion.this, "Dont Update Data Successfully", Toast.LENGTH_SHORT).show();

                                            }
                                        }

                                    } else {
                                        String resultServer = getHttpPost(url, params);
                                        System.out.println(resultServer);


                                        JSONObject c;
                                        try {
                                            Toast.makeText(Recepcion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
                                            pasar(conexion, general);
                                        }catch(Exception e){
                                            Toast.makeText(Recepcion.this, "Dont Update Data Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }*/


                        //TALL_ENCABEZA_ORDEN
                                                /*)}
                                        }


                                    }
                                }
                            }*/

                    }
                    return false;
                }

            });


        }
    }

    public void ver (View v){
        Intent intent = new Intent((Context) Corregir.this, (Class) MostrarDatosNit.class);
        Bundle bundle2 = new Bundle();
        bundle2.putString("cedula", Cedula);
        bundle2.putString("nombre1", nombre1);
        bundle2.putString("nombre2", nombre2);
        bundle2.putString("apellido1",apellido1);
        bundle2.putString("apellido2", apellido2);
        intent.putExtras(bundle2);
        Corregir.this.startActivity(intent);
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
            general2 = dataBaseHelper.consultarProceso("123", "METRO", sucursal, "01");

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
                   String ind_estado, String[][] general){

        //atributos = new ArrayList();

        ArrayList<String> columnas = new ArrayList<>();
        ArrayList<String> datos = new ArrayList<>();
        atributos = new ArrayList<>();

        if (cod_proceso.trim().equals("13")||cod_proceso.trim().equals("14")){
            atributos.add("ROMBO" + " :");
            atributos.add(num_rombo);
        }

        for (int i = 0;i<general.length;i++) {
            atributos.add(general[i][4].trim() + " :");
            atributos.add(general[i][5].trim());
            //}
            /*if(general[i][16].trim().equals("S")) {
                if (general[i][18].trim().equals("PROMESA_ENTREGA")) {
                    columnas.add(general[i][18].trim());
                    datos.add("GETDATE()");
                    Toast.makeText(getApplicationContext(),String.valueOf(date),Toast.LENGTH_LONG).show();
                }else{
                    columnas.add(general[i][18].trim());
                    datos.add(general[i][5].trim());
                }
            }*/
        }

        /*String url1 = "http://" + ip + "/consultarGeneral.php";

        List<NameValuePair> params1 = new ArrayList<NameValuePair>();
        params1.add(new BasicNameValuePair("sParametro", "consecutivo"));


        String resultServer1  = getHttpPost(url1,params1);
        System.out.println("---------------------------------resultserver----------------");
        try {

            JSONArray jArray = new JSONArray(resultServer1);
            ArrayList<String> array = new ArrayList<String>();
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                array.add(json.getString("siguiente"));
                //datos.add(array.get(0));
            }

        }catch (JSONException e ){
            e.printStackTrace();
        }*/


        //TALL_ENCABEZA_ORDEN_DATOS_POR_DEFECTO
        /*columnas.add("NUMERO");

        columnas.add("ROMBO_USADO");
        datos.add(num_rombo);

        columnas.add("PROMESA_ENTREGA");
        datos.add("GETDATE()");*/


        for (int i = 0;i<(generalAux.length-general.length);i++) {
            columnas.add(generalAux[general.length + i][18]);
        }

        for (int i = 0;i<(generalAux.length-general.length);i++) {
            datos.add(generalAux[general.length + i][5]);
        }




        /*atributos.add("ROMBO :");
        atributos.add(num_rombo);
        atributos.add("PLACA : ");
        atributos.add(cod_placa);
        atributos.add("UBIC : ");
        atributos.add(cod_ubicacion);*/
        String faltanDatos = "false";

        for (int i =0;i<atributos.size();i=i+2){
            if (atributos.get(i+1).equals("")){
                faltanDatos ="true";
                break;
            }
        }

        if (proceso.equals("13")||proceso.equals("14")||proceso.equals("15")){

            Bundle bundle = new Bundle();
            Intent intent = new Intent((Context) this, (Class) GuardarValidacion.class);
            bundle.putString("cod_empresa", cod_empresa);
            bundle.putString("cod_sucursal", cod_sucursal);
            bundle.putString("cod_proceso", cod_proceso);
            bundle.putString("cod_placa", cod_placa);
            bundle.putString("num_rombo", num_rombo);
            bundle.putString("cod_usuario", cod_usuario);
            bundle.putString("cod_ubicacion", cod_ubicacion);
            bundle.putString("fec_proceso", fec_proceso);
            bundle.putString("num_nit", num_nit);
            bundle.putString("ind_estado", ind_estado);
            bundle.putStringArrayList("atributos", atributos);
            bundle.putStringArrayList("columnas", columnas);
            bundle.putStringArrayList("datos", datos);
            bundle.putString("ip", ip);
            bundle.putString("km", km);
            bundle.putString("faltanDatos", faltanDatos);
            intent.putExtras(bundle);
            this.startActivityForResult(intent, request_code);
        }else {

            Bundle bundle = new Bundle();
            Intent intent = new Intent((Context) this, (Class) Guardar.class);
            bundle.putString("cod_empresa", cod_empresa);
            bundle.putString("cod_sucursal", cod_sucursal);
            bundle.putString("cod_proceso", cod_proceso);
            bundle.putString("cod_placa", cod_placa);
            bundle.putString("num_rombo", num_rombo);
            bundle.putString("cod_usuario", cod_usuario);
            bundle.putString("cod_ubicacion", cod_ubicacion);
            bundle.putString("fec_proceso", fec_proceso);
            bundle.putString("num_nit", num_nit);
            bundle.putString("ind_estado", ind_estado);
            bundle.putStringArrayList("atributos", atributos);
            bundle.putStringArrayList("columnas", columnas);
            bundle.putStringArrayList("datos", datos);
            bundle.putString("ip", ip);
            bundle.putString("faltanDatos", faltanDatos);
            intent.putExtras(bundle);
            this.startActivityForResult(intent, request_code);
        }
        //finish();
        /*for (int i = 0; i < general.length; i++) {
                general[i][5] = "";
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

    private static boolean isNumeric(String cadena){
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe){
            return false;
        }
    }

    public void refrescar(View v){
        mostrarImagen();
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
        try {
            if (data.getStringExtra("bien").equals("bien")) {
                finish();
            } else {
                if (data.getStringExtra("regresar").equals("regresar")) {

                } else {
                    km = data.getStringExtra("codigo");
                    Toast.makeText(getApplicationContext(), km, Toast.LENGTH_LONG).show();
                    texto.setText(data.getStringExtra("km"));

                    general[indice][5] = texto.getText().toString().trim();
                    pasar(conexion, general);

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void ampliar(View v){
        nombre = new String();

        nombre = general[indice][4].trim() + "_" + fechaFoto;
        //File imgFile = new  File(ruta_fotos + getCode() + ".jpg");
        String strPath = dirFoto + nombre +  ".jpg";

        Bundle bundle = new Bundle();
        Intent intent = new Intent((Context) this, (Class) AmpliarImagen.class);
        bundle.putString("nombre", nombre);
        bundle.putString("path", strPath);
        intent.putExtras(bundle);
        this.startActivity(intent);
    }

    public int getHora() {
        Calendar calendar = Calendar.getInstance();
        Calendar c = new GregorianCalendar();
        int mMinute = c.get(Calendar.MINUTE);
        return mMinute; // Devuelve el objeto Date con las nuevas horas añadidas
    }

    public static String remove(String input) {
        String output = "";

        for (int i =0;i<input.length()-1;i++){
            if (Character.isLetter(input.charAt(i))){
                output += input.charAt(i);
            }
        }
        return output;
    }

    public void onDestroy() {
        super.onDestroy();

    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Saliendo...")
                .setMessage("Esta Seguro que desea dejar de recibir?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}

