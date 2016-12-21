package com.example.david.proyectowms;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ExampleTableRow extends Activity {

    TableLayout tabla;
    TableLayout cabecera;
    TableRow.LayoutParams layoutFila;
    TableRow.LayoutParams layoutId;
    TableRow.LayoutParams layoutTexto;
    TableRow.LayoutParams layoutApellido;

    private int MAX_FILAS = 10;

    Resources rs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_table_row);
        rs = this.getResources();
        tabla = (TableLayout)findViewById(R.id.tabla);
        cabecera = (TableLayout)findViewById(R.id.cabecera);
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        layoutId = new TableRow.LayoutParams(70,TableRow.LayoutParams.WRAP_CONTENT);
        layoutTexto = new TableRow.LayoutParams(160,TableRow.LayoutParams.WRAP_CONTENT);
        layoutApellido = new TableRow.LayoutParams(160,TableRow.LayoutParams.WRAP_CONTENT);
        agregarCabecera();
        agregarFilasTabla();
    }

    public void agregarCabecera(){
        TableRow fila;
        TextView txtId;
        TextView txtNombre;
        TextView txtApellido;

        fila = new TableRow(this);
        fila.setLayoutParams(layoutFila);

        txtId = new TextView(this);
        txtNombre = new TextView(this);
        txtApellido = new TextView(this);



        fila.addView(txtId);
        fila.addView(txtNombre);
        fila.addView(txtApellido);
        cabecera.addView(fila);
    }

    public void agregarFilasTabla(){

        TableRow fila;
        TextView txtId;
        TextView txtNombre;
        TextView txtApellido;

        for(int i = 0;i<MAX_FILAS;i++){
            int posicion = i + 1;
            fila = new TableRow(this);
            fila.setLayoutParams(layoutFila);

            txtId = new TextView(this);
            txtNombre = new TextView(this);
            txtApellido = new TextView(this);

            txtId.setText(String.valueOf(posicion));
            txtId.setGravity(Gravity.CENTER_HORIZONTAL);
            txtId.setLayoutParams(layoutId);

            txtNombre.setText("Texto "+posicion);
            txtNombre.setGravity(Gravity.CENTER_HORIZONTAL);
            txtNombre.setLayoutParams(layoutTexto);
            txtNombre.setText("Texto " + posicion);
            txtNombre.setGravity(Gravity.CENTER_HORIZONTAL);
            txtNombre.setLayoutParams(layoutTexto);

            txtApellido.setText(String.valueOf(posicion));
            txtApellido.setGravity(Gravity.CENTER_HORIZONTAL);
            txtApellido.setLayoutParams(layoutApellido);

            fila.addView(txtId);
            fila.addView(txtNombre);
            fila.addView(txtApellido);

            tabla.addView(fila);
        }
    }
}
