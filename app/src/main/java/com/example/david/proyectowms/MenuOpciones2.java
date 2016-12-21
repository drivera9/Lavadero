package com.example.david.proyectowms;

/*
 * Decompiled with CFR 0_92.
 *
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Bundle
 *  android.view.Menu
 *  android.view.MenuInflater
 *  android.view.MenuItem
 *  android.view.View
 *  android.widget.AdapterView
 *  android.widget.AdapterView$OnItemClickListener
 *  android.widget.ImageView
 *  android.widget.ListAdapter
 *  android.widget.ListView
 *  android.widget.TextView
 *  android.widget.Toast
 *  java.io.IOException
 *  java.io.PrintStream
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Error
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.sql.SQLException
 *  java.util.ArrayList
 */

        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ImageView;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.example.david.proyectowms.InventarioFisico;

        import java.io.IOException;
        import java.sql.SQLException;
        import java.util.ArrayList;

/*
 * Failed to analyse overrides
 */
public class MenuOpciones2
        extends Activity {
    ArrayList datos = new ArrayList();
    private ListView lista;
    ArrayList modulos = new ArrayList();
    Intent[] newActivity = new Intent[0];
    ArrayList opciones = new ArrayList();
    public void actualizar(View view) {
    }
    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.requestWindowFeature(1);
        this.setContentView(R.layout.menu_opciones2);
        Bundle bundle2 = this.getIntent().getExtras();
        bundle2.getString("opciones");
        bundle2.getString("vista");
        String string = bundle2.getString("usuario");
        DataBaseHelper dataBaseHelper = new DataBaseHelper((Context)this);
        try {
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();
            this.modulos = dataBaseHelper.getVista2(string);
            dataBaseHelper.close();
        }
        catch (IOException var27_7) {
            throw new Error("Unable to create database");
        }
        catch (SQLException var8_5) {
            var8_5.printStackTrace();
        }
        this.opciones.add((Object)"Cambiar ubicacion");
        this.opciones.add((Object)"Consulta PLU");
        this.opciones.add((Object)"Entradas inventario");
        this.opciones.add((Object)"Impresion rotulo");
        this.opciones.add((Object)"Inventario fisico");
        if (this.modulos.size() == 0) {
            Toast.makeText((Context)this, (CharSequence)"  El usuario no existe! ", Toast.LENGTH_SHORT).show();
            return;
        }
        for (int i = 0; i < this.modulos.size(); ++i) {
            System.out.println((String)this.modulos.get(i));
        }

        for (int i = 0; i < this.modulos.size(); ++i) {
            //this.datos.add((Object)new Lista_entrada(R.drawable.abc_ab_share_pack_mtrl_alpha, (String)this.modulos.get(i)));
        }

                 this.lista = (ListView)this.findViewById(R.id.listView_opciones2);
                 this.lista.setAdapter(new Lista_adaptador((Context)this, R.layout.entrada, this.datos){
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
                final Intent intent = new Intent((Context)this, (Class)InventarioFisico.class);
                Bundle bundle3 = new Bundle();
                bundle3.putString("usuario", string);
                intent.putExtras(bundle3);
                ListView listView2 = this.lista;
                listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int posicion, long id) {

                            MenuOpciones2.this.startActivity(intent);
                    }
                });
            }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 2131296441) {
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}