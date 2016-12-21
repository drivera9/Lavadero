package com.example.david.proyectowms;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.qoppa.pdf.PDFException;
import com.qoppa.pdf.PrintSettings;
import com.qoppa.pdfPrint.PDFPrint;

import java.io.File;
import java.io.FileOutputStream;



public class Imprimir extends Activity {

    private final static String NOMBRE_DOCUMENTO = "prueba2.pdf";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imprimir);

// Creamos el documento.
        com.itextpdf.text.Document documento = new com.itextpdf.text.Document();
// Creamos el fichero con el nombre que deseemos.
        File f = null;
        try {
            f = crearFichero(NOMBRE_DOCUMENTO);
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
// Creamos el flujo de datos de salida para el fichero donde guardaremos el pdf.
        FileOutputStream ficheroPdf = new FileOutputStream(f.getAbsolutePath());

// Asociamos el flujo que acabamos de crear al documento.

            PdfWriter.getInstance(documento, ficheroPdf);
        } catch (DocumentException e ) {
            e.printStackTrace();
        } catch (IOException e){

        }



// Abrimos el documento.
        documento.open();
        // Añadimos un título con la fuente por defecto.
        try {
            documento.add(new Paragraph("Título 1"));
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.placa6);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        Image imagen = null;
        try {
            imagen = Image.getInstance(stream.toByteArray());

        documento.add(imagen);
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e){
            e.printStackTrace();
        }
        documento.close();


        
    }

    public static File crearFichero(String nombreFichero) throws IOException {
        File ruta = getRuta();
        File fichero = null;
        if (ruta != null)
            fichero = new File(ruta, nombreFichero);
        return fichero;
    }

    public static File getRuta() {

        // El fichero será almacenado en un directorio dentro del directorio
        // Descargas
        File ruta = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            ruta = new File(
                    Environment
                            .getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_DOWNLOADS),
                    "hola");

            if (ruta != null) {
                if (!ruta.mkdirs()) {
                    if (!ruta.exists()) {
                        return null;
                    }
                }
            }
        } else {
        }

        return ruta;
    }
}
