package com.example.david.proyectowms;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class TipoCarroInforme extends Activity {
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
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_carro_informe);

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
    }

    public void carro(View v){
        Intent intent = new Intent((Context) TipoCarroInforme.this, (Class) Informe.class);


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
        bundle2.putString("vehiculo", "A");
        bundle2.putString("proceso", "4");
        intent.putExtras(bundle2);
        startActivity(intent);
    }

    public void moto(View v){
        Intent intent = new Intent((Context) TipoCarroInforme.this, (Class) Informe.class);


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
        bundle2.putString("vehiculo", "M");
        bundle2.putString("proceso", "4");
        intent.putExtras(bundle2);
        startActivity(intent);
    }
}
