package com.example.david.proyectowms;
/*
 * Decompiled with CFR 0_92.
 *
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Bundle
 *  android.view.View
 *  android.widget.AdapterView
 *  android.widget.AdapterView$OnItemClickListener
 *  android.widget.ImageView
 *  android.widget.ListAdapter
 *  android.widget.ListView
 *  android.widget.TextView
 *  android.widget.Toast
 *  java.io.IOException
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Error
 *  java.lang.Object
 *  java.lang.String
 *  java.sql.SQLException
 *  java.util.ArrayList
 */

        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ImageView;
        import android.widget.ListAdapter;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;
        import com.example.david.proyectowms.DataBaseHelper;

        import java.io.IOException;
        import java.sql.SQLException;
        import java.util.ArrayList;

/*
 * Failed to analyse overrides
 */
public class MainActivity
        extends Activity {
    final ArrayList datos = new ArrayList();
    private ListView lista;
    ArrayList modulos = new ArrayList();
    ArrayList resultado = new ArrayList();
    ArrayList resultado2 = new ArrayList();
    ArrayList resultadoOpciones = new ArrayList();
    ArrayList resultadoVista = new ArrayList();
    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.requestWindowFeature(1);
        this.setContentView(R.layout.main);
        DataBaseHelper dataBaseHelper = new DataBaseHelper((Context)this);
        try {
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();
            this.resultado = dataBaseHelper.getUsuarios();
            for (int i = 0; i < this.resultado.size(); ++i) {
                //this.datos.add((Object)new Lista_entrada(R.drawable.abc_ab_share_pack_mtrl_alpha, (String)this.resultado.get(i)));
            }
            this.resultado2 = dataBaseHelper.getContraseñas();
            this.resultadoOpciones = dataBaseHelper.getOpciones();
            this.resultadoVista = dataBaseHelper.getVista();
            dataBaseHelper.close();
        }
        catch (IOException var6_5) {
            throw new Error("Unable to create database");
        }
        catch (SQLException var4_6) {
            var4_6.printStackTrace();
        }
        this.lista = (ListView)this.findViewById(R.id.listView);
        this.lista.setAdapter((ListAdapter)new Lista_adaptador((Context)this, R.layout.entrada, this.datos){
            public void onEntrada(Object object, View view) {
                if (object != null) {
                    ImageView imageView;
                    TextView textView = (TextView)view.findViewById(R.id.textView_superior);
                    if (textView != null) {
                        textView.setText((CharSequence)((Lista_entrada)object).get_textoEncima());
                    }
                    if ((imageView = (ImageView)view.findViewById(R.id.imageView_imagen)) != null) {
                        imageView.setImageResource(((Lista_entrada)object).get_idImagen());
                    }
                }
            }
        });
        final Intent intent = new Intent((Context)this, (Class)Login.class);
        this.lista.setOnItemClickListener((AdapterView.OnItemClickListener)new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView adapterView, View view, int n, long l) {
                Bundle bundle = new Bundle();
                for (int i = 0; i < MainActivity.this.datos.size(); ++i) {
                    if (n != i) continue;
                    bundle.putString("pass", (String)MainActivity.this.resultado2.get(n));
                    bundle.putString("opciones", (String)MainActivity.this.resultadoOpciones.get(n));
                    bundle.putString("usuario", (String)MainActivity.this.resultado.get(n));
                    Toast.makeText((Context)MainActivity.this, (CharSequence)((CharSequence)MainActivity.this.resultado.get(n)), Toast.LENGTH_SHORT).show();
                    bundle.putString("vista", (String)MainActivity.this.resultadoVista.get(n));
                    intent.putExtras(bundle);
                    MainActivity.this.startActivity(intent);
                }
            }
        });
    }
}
