package com.example.david.proyectowms;

import android.app.Activity;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class MostrarDatosNit extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_datos);
        Bundle bundle = this.getIntent().getExtras();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        final String cedula = bundle.getString("cedula");
        final String nombre1 = bundle.getString("nombre1");
        final String nombre2 = bundle.getString("nombre2");
        final String apellido1 = bundle.getString("apellido1");
        final String apellido2 = bundle.getString("apellido2");

        TextView Cedula = (TextView) findViewById(R.id.textView_cedula);
        TextView Nombre1 = (TextView) findViewById(R.id.textView_nombre1);
        TextView Nombre2 = (TextView) findViewById(R.id.textView_nombre2);
        TextView Apellido1 = (TextView) findViewById(R.id.textView_apellido1);
        TextView Apellido2 = (TextView) findViewById(R.id.textView_apellido2);

        Cedula.setText(cedula);
        Nombre1.setText(nombre1);
        Nombre2.setText(nombre2);
        Apellido1.setText(apellido1);
        Apellido2.setText(apellido2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mostrar_datos, menu);
        return true;
    }

    public void salir(View v){
        finish();
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
