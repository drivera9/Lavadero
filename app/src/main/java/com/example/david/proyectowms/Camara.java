package com.example.david.proyectowms;

import java.io.File;
 import java.io.IOException;
 import java.text.SimpleDateFormat;
 import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Bundle;
import android.os.Environment;
 import android.provider.MediaStore;
 import android.annotation.SuppressLint;
 import android.app.Activity;
 import android.content.Intent;
 import android.util.Log;
 import android.view.Menu;
 import android.view.View;
 import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Camara extends Activity {

              private final String ruta_fotos = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/misfotos/";
         private File file = new File(ruta_fotos);
         private Button boton;

              @Override
     protected void onCreate(Bundle savedInstanceState) {
                  super.onCreate(savedInstanceState);
                  setContentView(R.layout.activity_camara);
                  //======== codigo nuevo ========
                  boton = (Button) findViewById(R.id.btnTomaFoto);
                  //Si no existe crea la carpeta donde se guardaran las fotos
                  file.mkdirs();
                  //accion para el boton
                  boton.setOnClickListener(new View.OnClickListener() {

                      @Override
                      public void onClick(View v) {
                          String file = ruta_fotos + getCode() + ".jpg";
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
                          } catch (IOException ex) {
                              Log.e("ERROR ", "Error:" + ex);
                          }
                      }

                  });
              }
    public void mostrar(View v) {
        if (getCode() != null) {
            File imgFile = new  File(ruta_fotos + getCode() + ".jpg");
            try {
                if (imgFile.exists()) {
                    ImageView myImage = (ImageView) findViewById(R.id.imageView2);
                    myImage.setImageURI(Uri.fromFile(imgFile));

                }
            }catch (Exception e){
                Toast.makeText(Camara.this,e.getMessage(),Toast.LENGTH_LONG);
            }
        }
              }


              /**
     60  * Metodo privado que genera un codigo unico segun la hora y fecha del sistema
     61  * @return photoCode
     62  * */
              @SuppressLint("SimpleDateFormat")
              private String getCode()
      {
           SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
           String date = dateFormat.format(new Date() );
           String photoCode = "pic_123";
           return photoCode;
          }

              @Override
      public boolean onCreateOptionsMenu(Menu menu) {
           // Inflate the menu; this adds items to the action bar if it is present.
           getMenuInflater().inflate(R.menu.menu_main, menu);
           return true;
          }
             }
