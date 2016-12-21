package com.example.david.proyectowms;

/*
 * Decompiled with CFR 0_92.
 *
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.app.Activity
 *  android.app.AlertDialog
 *  android.app.AlertDialog$Builder
 *  android.content.Context
 *  android.content.DialogInterface
 *  android.content.DialogInterface$OnClickListener
 *  android.content.Intent
 *  android.os.Bundle
 *  android.text.Editable
 *  android.view.KeyEvent
 *  android.view.MenuItem
 *  android.view.View
 *  android.view.View$OnKeyListener
 *  android.widget.AdapterView
 *  android.widget.AdapterView$OnItemClickListener
 *  android.widget.AdapterView$OnItemSelectedListener
 *  android.widget.ArrayAdapter
 *  android.widget.EditText
 *  android.widget.GridView
 *  android.widget.ListAdapter
 *  android.widget.Spinner
 *  android.widget.SpinnerAdapter
 *  android.widget.TextView
 *  android.widget.Toast
 *  java.io.IOException
 *  java.io.PrintStream
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.sql.SQLException
 *  java.util.ArrayList
 */

        import android.app.Activity;
        import android.app.AlertDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.database.Cursor;
        import android.os.Bundle;
        import android.view.KeyEvent;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.EditText;
        import android.widget.GridView;
        import android.widget.ListAdapter;
        import android.widget.RelativeLayout;
        import android.widget.Spinner;
        import android.widget.SpinnerAdapter;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.example.david.proyectowms.DataBaseHelper;

        import java.io.IOException;
        import java.sql.SQLException;
        import java.util.ArrayList;

/*
 * Failed to analyse overrides
 */
public class InventarioFisico
        extends Activity {
    GridView grid;
    GridView gridTitulos;
    private final String[] items = new String[]{"NUM" ,"UBIC" ,"REF" ,"PLU" ,"DESC"};

    public void borrarPLU() throws IOException, SQLException {
        try {
            final DataBaseHelper dataBaseHelper = new DataBaseHelper((Context)this);
            AlertDialog.Builder builder = new AlertDialog.Builder((Context)this);
            builder.setMessage((CharSequence)"\u00bfDesea borrar el registro?").setTitle((CharSequence)"Advertencia").setCancelable(false).setNegativeButton((CharSequence)"No", (DialogInterface.OnClickListener)new DialogInterface.OnClickListener(){

                public void onClick(DialogInterface dialogInterface, int n) {
                    dialogInterface.cancel();
                }
            }).setPositiveButton((CharSequence)"Si", (DialogInterface.OnClickListener)new DialogInterface.OnClickListener(){

                /*
                 * Enabled aggressive block sorting
                 * Enabled unnecessary exception pruning
                 */
                public void onClick(DialogInterface dialogInterface, int n) {
                    String string = ((EditText)InventarioFisico.this.findViewById(R.id.editText_plu)).getText().toString();
                    try {
                        dataBaseHelper.createDataBase();
                    }
                    catch (IOException var4_4) {
                        var4_4.printStackTrace();
                    }
                    try {
                        dataBaseHelper.openDataBase();
                    }
                    catch (SQLException var5_5) {
                        var5_5.printStackTrace();
                    }
                    dataBaseHelper.borrarPLU(string);
                    dataBaseHelper.close();
                    try {
                        consultarTodo();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
            builder.create().show();
            Object[] arrobject = new String[]{""};
            this.grid = (GridView)this.findViewById(R.id.gridView);
            ArrayAdapter arrayAdapter = new ArrayAdapter((Context)this, android.R.layout.simple_list_item_1, arrobject);
            this.grid.setAdapter((ListAdapter) arrayAdapter);

            return;
        }
        catch (Exception var3_5) {
            Toast.makeText((Context)this, (CharSequence)"No hay registros solicitados!!", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void borrarUbicacion() throws IOException, SQLException {
        try {
            final DataBaseHelper dataBaseHelper = new DataBaseHelper((Context)this);
            AlertDialog.Builder builder = new AlertDialog.Builder((Context)this);
            builder.setMessage((CharSequence)"\u00bfDesea borrar el registro?").setTitle((CharSequence)"Advertencia").setCancelable(false).setNegativeButton((CharSequence)"No", (DialogInterface.OnClickListener)new DialogInterface.OnClickListener(){

                public void onClick(DialogInterface dialogInterface, int n) {
                    dialogInterface.cancel();
                }
            }).setPositiveButton((CharSequence)"Si", (DialogInterface.OnClickListener)new DialogInterface.OnClickListener(){

                /*
                 * Enabled aggressive block sorting
                 * Enabled unnecessary exception pruning
                 */
                public void onClick(DialogInterface dialogInterface, int n) {
                    String string = ((EditText)InventarioFisico.this.findViewById(R.id.editText_ubic)).getText().toString();
                    try {
                        dataBaseHelper.createDataBase();
                    }

                    catch (IOException var4_4) {
                        var4_4.printStackTrace();
                    }
                    try {
                        dataBaseHelper.openDataBase();
                    }
                    catch (SQLException var5_5) {
                        var5_5.printStackTrace();
                    }
                    dataBaseHelper.borrarUbic(string);
                    dataBaseHelper.close();
                    try {
                        consultarTodo();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
            });
            builder.create().show();
            Object[] arrobject = new String[]{""};
            this.grid = (GridView)this.findViewById(R.id.gridView);
            ArrayAdapter arrayAdapter = new ArrayAdapter((Context)this, android.R.layout.simple_list_item_1, arrobject);
            this.grid.setAdapter((ListAdapter)arrayAdapter);
            consultarTodo();
            return;
        }
        catch (Exception var3_5) {
            Toast.makeText((Context)this, (CharSequence)"No hay registros solicitados!!", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void consultarPLU() throws IOException, SQLException {
        DataBaseHelper dataBaseHelper = new DataBaseHelper((Context)this);
        dataBaseHelper.createDataBase();
        dataBaseHelper.openDataBase();
        ArrayList arrayList = dataBaseHelper.consultarPLU(((EditText)this.findViewById(R.id.editText_plu)).getText().toString());
        Cursor ubiccursor = dataBaseHelper.contarPLU("des_invfisico_tmp.cod_ubicacion", ((EditText) this.findViewById(R.id.editText_plu)).getText().toString());
        Cursor desccursor = dataBaseHelper.contarPLU("plu.descripcion", ((EditText) this.findViewById(R.id.editText_plu)).getText().toString());
        Cursor refcursor = dataBaseHelper.contarPLU("des_invfisico_tmp.cod_referencia", ((EditText) this.findViewById(R.id.editText_plu)).getText().toString());
        Cursor plucursor = dataBaseHelper.contarPLU("des_invfisico_tmp.cod_plu", ((EditText) this.findViewById(R.id.editText_plu)).getText().toString());

        String[] totales = new String[]{"totales" ,ubiccursor.getCount() + "" ,refcursor.getCount() + "" ,plucursor.getCount() + "" ,desccursor.getCount() + ""};

        dataBaseHelper.close();

        GridView totalesgrid = (GridView) findViewById(R.id.gridView3);
        CustomGridViewAdapter gridViewAdapter2 = new CustomGridViewAdapter(InventarioFisico.this, totales,"grid");
        // ArrayAdapter arrayAdapter = new ArrayAdapter((Context)this, R.layout.row_grid, arrobject);
        totalesgrid.setAdapter(gridViewAdapter2);
        Object[] arrobject = new String[arrayList.size()];
        int n = 0;
        do {
            if (n >= arrayList.size()) break;
            arrobject[n] = (String)arrayList.get(n);
            ++n;
        } while (true);
        try {
            this.grid = (GridView)this.findViewById(R.id.gridView);
            CustomGridViewAdapter gridViewAdapter = new CustomGridViewAdapter(InventarioFisico.this, (String[]) arrobject,"grid");
            // ArrayAdapter arrayAdapter = new ArrayAdapter((Context)this, R.layout.row_grid, arrobject);
            this.grid.setAdapter(gridViewAdapter);
            this.grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    try {

                    EditText editText = (EditText)InventarioFisico.this.findViewById(R.id.editText_plu);
                    if (id == 3) {
                        editText.setText((CharSequence)((TextView)view).getText().toString());
                    }
                    EditText editText2 = (EditText)InventarioFisico.this.findViewById(R.id.editText_ubic);
                    if (id == 0) {
                        editText2.setText((CharSequence)((TextView)view).getText().toString());
                    }
                    Toast.makeText((Context)InventarioFisico.this.getApplicationContext(), (CharSequence)((TextView)view).getText(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();

                    }}
            });
            return;
        }
        catch (Exception var2_6) {
            Object[] arrobject2 = new String[]{""};
            this.grid = (GridView)this.findViewById(R.id.gridView);
            ArrayAdapter arrayAdapter = new ArrayAdapter((Context)this, android.R.layout.simple_list_item_1, arrobject2);
            this.grid.setAdapter((ListAdapter)arrayAdapter);
            Toast.makeText((Context)this, (CharSequence)"No hay registros solicitados!!", Toast.LENGTH_SHORT).show();
            return;
        }
    }
    public void consultarTodo() throws IOException, SQLException {
        DataBaseHelper dataBaseHelper = new DataBaseHelper((Context)this);
        dataBaseHelper.createDataBase();
        dataBaseHelper.openDataBase();
        final ArrayList arrayList = dataBaseHelper.consultarTodo();
        Cursor ubiccursor = dataBaseHelper.contar("des_invfisico_tmp.cod_ubicacion");
        Cursor desccursor = dataBaseHelper.contar("plu.descripcion");
        Cursor refcursor = dataBaseHelper.contar("des_invfisico_tmp.cod_referencia");
        Cursor plucursor = dataBaseHelper.contar("des_invfisico_tmp.cod_plu");

        String[] totales = new String[]{"totales" ,ubiccursor.getCount() + "" ,refcursor.getCount() + "" ,plucursor.getCount() + "" ,desccursor.getCount() + ""};
        dataBaseHelper.close();

        GridView totalesgrid = (GridView) findViewById(R.id.gridView3);
        CustomGridViewAdapter gridViewAdapter2 = new CustomGridViewAdapter(InventarioFisico.this, totales,"grid");
        // ArrayAdapter arrayAdapter = new ArrayAdapter((Context)this, R.layout.row_grid, arrobject);
        totalesgrid.setAdapter(gridViewAdapter2);
        final Object[] arrobject = new String[arrayList.size()];
        final ArrayList arrayList2 = new ArrayList();
        final ArrayList arrayList3 = new ArrayList();
        for (int i = 0; i < arrayList.size(); ++i) {
            arrobject[i] = (String)arrayList.get(i);
        }
        /*for (int i = 0;i<arrobject.length;i++){
            System.out.println(arrobject[i]);
        }*/
        int n = 0;
        int n2 = 0;
        int n3 = 1;
        while (n < 2*  arrobject.length) {
            for (int j = 0; j <= 4; ++j) {
                arrayList3.add(n, (Object)("" + n3 + ""));
                arrayList3.add(n + 1, arrobject[n2]);
                arrayList2.add(n, (Object)("" + j + ""));
                arrayList2.add(n + 1, arrobject[n2]);
                ++n2;
                n+=2;
            }
            ++n3;
        }
        /*for (int j = 0; j < arrobject.length; ++j) {
            System.out.println(" - " + arrobject[j]);
        }*/

            this.grid = (GridView) this.findViewById(R.id.gridView);
            CustomGridViewAdapter gridViewAdapter = new CustomGridViewAdapter(InventarioFisico.this, (String[]) arrobject,"grid");
            // ArrayAdapter arrayAdapter = new ArrayAdapter((Context)this, R.layout.row_grid, arrobject);
            this.grid.setAdapter(gridViewAdapter);
            this.grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    if (arrobject[position].equals("")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder((Context) InventarioFisico.this);
                        builder.setMessage((CharSequence) "\u00bfDesea Agregar el registro?").setTitle((CharSequence) "Advertencia").setCancelable(false).setNegativeButton((CharSequence) "No", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int n) {
                                dialogInterface.cancel();
                            }
                        }).setPositiveButton((CharSequence) "Si", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int n2) {
                                System.out.println("---------------");
                                for (int i = 0; i < arrayList3.size(); i++) {
                                    System.out.println("" + i + arrayList3.get(i));
                                }
                                System.out.println(arrobject[position - 1] + "######");
                                new CustomDialogClass(InventarioFisico.this, Integer.parseInt((String) (arrayList3.get(position + position))), Integer.parseInt((String) ((String) arrayList2.get(position + position))), arrobject[position - 1].toString(), arrobject[position - 2].toString(), arrobject[position - 3].toString()).show();
                                try {
                                    consultarTodo();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        builder.create().show();
                    } else {
                        new AlertDialog.Builder(InventarioFisico.this)
                                .setTitle("Cast Recording")
                                .setMessage(" " + arrobject[position])
                                .show();
                    }
                }
            });
        }

            public void consultarUbic() throws IOException, SQLException {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(InventarioFisico.this);
                dataBaseHelper.createDataBase();
                dataBaseHelper.openDataBase();
                ArrayList arrayList = dataBaseHelper.consultarUbic(((EditText) findViewById(R.id.editText_ubic)).getText().toString());
                Toast.makeText(InventarioFisico.this, (CharSequence) ((CharSequence) arrayList.get(0)), Toast.LENGTH_SHORT).show();
                Cursor ubiccursor = dataBaseHelper.contarUbic("des_invfisico_tmp.cod_ubicacion", ((EditText) findViewById(R.id.editText_ubic)).getText().toString());
                Cursor desccursor = dataBaseHelper.contarUbic("plu.descripcion", ((EditText) findViewById(R.id.editText_ubic)).getText().toString());
                Cursor refcursor = dataBaseHelper.contarUbic("des_invfisico_tmp.cod_referencia", ((EditText) findViewById(R.id.editText_ubic)).getText().toString());
                Cursor plucursor = dataBaseHelper.contarUbic("des_invfisico_tmp.cod_plu", ((EditText) findViewById(R.id.editText_ubic)).getText().toString());

                String[] totales = new String[]{"totales" ,ubiccursor.getCount() + "" ,refcursor.getCount() + "" ,plucursor.getCount() + "" ,desccursor.getCount() + ""};

                dataBaseHelper.close();

                GridView totalesgrid = (GridView) findViewById(R.id.gridView3);
                CustomGridViewAdapter gridViewAdapter2 = new CustomGridViewAdapter(InventarioFisico.this, totales,"grid");
                // ArrayAdapter arrayAdapter = new ArrayAdapter((Context)this, R.layout.row_grid, arrobject);
                totalesgrid.setAdapter(gridViewAdapter2);

                Object[] arrobject = new String[arrayList.size()];
                int n = 0;
                do {
                    if (n >= arrayList.size()) break;
                    arrobject[n] = (String) arrayList.get(n);
                    ++n;
                } while (true);
                try {
                    this.grid = (GridView)this.findViewById(R.id.gridView);
                    CustomGridViewAdapter gridViewAdapter = new CustomGridViewAdapter(InventarioFisico.this, (String[]) arrobject,"grid");
                    // ArrayAdapter arrayAdapter = new ArrayAdapter((Context)this, R.layout.row_grid, arrobject);
                    this.grid.setAdapter(gridViewAdapter);
                    this.grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            try {

                            EditText editText = (EditText) InventarioFisico.this.findViewById(R.id.editText_plu);
                            if (id == 3) {
                                editText.setText((CharSequence) "");
                                editText.setText((CharSequence) ((TextView) view).getText().toString());
                            }
                            EditText editText2 = (EditText) InventarioFisico.this.findViewById(R.id.editText_ubic);
                            if (id == 0) {
                                editText2.setText((CharSequence) "");
                                editText2.setText((CharSequence) ((TextView) view).getText().toString());
                            }
                            Toast.makeText((Context) InventarioFisico.this.getApplicationContext(), (CharSequence) ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    return;
                } catch (Exception var2_6) {
                    Toast.makeText(InventarioFisico.this, (CharSequence) "No hay registros solicitados!!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            public void guardarProducto() {
                Object[] arrobject = new String[]{""};
                grid = (GridView) findViewById(R.id.gridView);
                ArrayAdapter arrayAdapter = new ArrayAdapter(InventarioFisico.this, android.R.layout.simple_list_item_1, arrobject);
                grid.setAdapter((ListAdapter) arrayAdapter);
                EditText editText = (EditText) findViewById(R.id.editText_ubic);
                EditText editText2 = (EditText) findViewById(R.id.editText_plu);
                Bundle bundle = new Bundle();
                Intent intent = new Intent(InventarioFisico.this, (Class) GuardarProducto.class);
                bundle.putString("plu", editText2.getText().toString());
                bundle.putString("ubic", editText.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }

            public void guardarProducto2(String n, int n2, String string, int n3) throws IOException, SQLException {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(InventarioFisico.this);
                dataBaseHelper.createDataBase();
                dataBaseHelper.openDataBase();
                dataBaseHelper.guardarProducto(n, n2, 1, n3, string);
                dataBaseHelper.close();
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             */
            public void onCreate(Bundle bundle) {
                super.onCreate(bundle);
                setContentView(R.layout.inventario_fisico);
                RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);
                gridTitulos = (GridView) findViewById(R.id.gridView2);
                CustomGridViewAdapter gridViewAdapter = new CustomGridViewAdapter(InventarioFisico.this, items,"titulos");
                // ArrayAdapter arrayAdapter = new ArrayAdapter((Context)this, R.layout.row_grid, arrobject);
                gridTitulos.setAdapter(gridViewAdapter);
                //String string = getIntent().getExtras().getString("usuario");
                DataBaseHelper dataBaseHelper = new DataBaseHelper(InventarioFisico.this);
                try {
                    dataBaseHelper.createDataBase();
                } catch (IOException var4_4) {
                    var4_4.printStackTrace();
                }
                try {
                    dataBaseHelper.openDataBase();
                } catch (SQLException var5_5) {
                    var5_5.printStackTrace();
                }
                //ArrayList arrayList = dataBaseHelper.getConsultas(string);
                dataBaseHelper.close();
                //for (int i = 0; i < arrayList.size(); ++i) {
                //    System.out.println((String)arrayList.get(i));
                //}
                final EditText editText = (EditText) findViewById(R.id.editText_plu);
                final EditText editText2 = (EditText) findViewById(R.id.editText_ubic);
                final EditText editText3 = (EditText) findViewById(R.id.editText_cant);
                final Spinner spinner = (Spinner) findViewById(R.id.spinner_opciones);
                final Spinner spinner2 = (Spinner) findViewById(R.id.spinner_opcionesvisibles);
                ((TextView) findViewById(R.id.textView_cant)).setVisibility(View.INVISIBLE);
                ((TextView) findViewById(R.id.textView_grp)).setVisibility(View.INVISIBLE);
                ((EditText) findViewById(R.id.editText_cant)).setVisibility(View.INVISIBLE);
                ((EditText) findViewById(R.id.editText_grp)).setVisibility(View.INVISIBLE);
                ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(InventarioFisico.this, R.array.plu_array, android.R.layout.simple_list_item_1);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter((SpinnerAdapter) arrayAdapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int n, long id) {
                        if (parent.getItemAtPosition(n).toString().equals((Object) "PLU")) {
                            ((TextView) InventarioFisico.this.findViewById(R.id.textView_cant)).setVisibility(View.INVISIBLE);
                            ((EditText) InventarioFisico.this.findViewById(R.id.editText_cant)).setVisibility(View.INVISIBLE);
                            ((TextView) InventarioFisico.this.findViewById(R.id.textView_grp)).setVisibility(View.INVISIBLE);
                            ((EditText) InventarioFisico.this.findViewById(R.id.editText_grp)).setVisibility(View.INVISIBLE);
                        }
                        if (parent.getItemAtPosition(n).toString().equals((Object) "PLU + CANT")) {
                            ((TextView) InventarioFisico.this.findViewById(R.id.textView_cant)).setVisibility(View.VISIBLE);
                            ((EditText) InventarioFisico.this.findViewById(R.id.editText_cant)).setVisibility(View.VISIBLE);
                            ((TextView) InventarioFisico.this.findViewById(R.id.textView_grp)).setVisibility(View.INVISIBLE);
                            ((EditText) InventarioFisico.this.findViewById(R.id.editText_grp)).setVisibility(View.INVISIBLE);
                        }
                        if (parent.getItemAtPosition(n).toString().equals((Object) "PLU + CANT + GRP")) {
                            ((TextView) InventarioFisico.this.findViewById(R.id.textView_cant)).setVisibility(View.VISIBLE);
                            ((EditText) InventarioFisico.this.findViewById(R.id.editText_cant)).setVisibility(View.VISIBLE);
                            ((TextView) InventarioFisico.this.findViewById(R.id.textView_grp)).setVisibility(View.VISIBLE);
                            ((EditText) InventarioFisico.this.findViewById(R.id.editText_grp)).setVisibility(View.VISIBLE);
                        }
                    }

                    public void onNothingSelected(AdapterView adapterView) {
                    }
                });
                ArrayAdapter arrayAdapter2 = ArrayAdapter.createFromResource(InventarioFisico.this, R.array.opciones_array, android.R.layout.simple_list_item_1);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter((SpinnerAdapter) arrayAdapter2);
                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int n, long id) {

                        if (parent.getItemAtPosition(n).toString().equals((Object) "Consultar todo")) {
                            try {
                                consultarTodo();
                            } catch (IOException var15_5) {
                                var15_5.printStackTrace();
                            } catch (SQLException var14_6) {
                                var14_6.printStackTrace();
                            }
                            spinner2.setSelection(0);
                        }
                        if (parent.getItemAtPosition(n).toString().equals((Object) "Consultar PLU")) {
                            try {
                                InventarioFisico.this.consultarPLU();
                            } catch (IOException var13_7) {
                                var13_7.printStackTrace();
                            } catch (SQLException var12_8) {
                                var12_8.printStackTrace();
                            }
                            spinner2.setSelection(0);
                        }
                        if (parent.getItemAtPosition(n).toString().equals((Object) "Consultar ubicacion")) {
                            try {
                                consultarUbic();
                            } catch (IOException var11_9) {
                                var11_9.printStackTrace();
                            } catch (SQLException var10_10) {
                                var10_10.printStackTrace();
                            }
                            spinner2.setSelection(0);
                        }
                        if (parent.getItemAtPosition(n).toString().equals((Object) "Borrar PLU")) {
                            try {
                                InventarioFisico.this.borrarPLU();
                            } catch (IOException var9_11) {
                                var9_11.printStackTrace();
                            } catch (SQLException var8_12) {
                                var8_12.printStackTrace();
                            }
                            spinner2.setSelection(0);
                        }
                        if (parent.getItemAtPosition(n).toString().equals((Object) "Borrar ubicacion")) {
                            try {
                                InventarioFisico.this.borrarUbicacion();
                            } catch (IOException var7_13) {
                                var7_13.printStackTrace();
                            } catch (SQLException var6_14) {
                                var6_14.printStackTrace();
                            }
                            spinner2.setSelection(0);
                        }
                        if (parent.getItemAtPosition(n).toString().equals((Object) "Nuevo producto")) {
                            guardarProducto();
                        }
                    }

                    public void onNothingSelected(AdapterView adapterView) {
                    }
                });
                try {
                    editText.setOnKeyListener(new View.OnKeyListener() {
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                                try {
                                    guardarProducto2(editText.getText().toString(), Integer.parseInt((String) editText2.getText().toString()), "", 1);
                                    Toast.makeText((Context) InventarioFisico.this, (CharSequence) "se guardo correctamente!", Toast.LENGTH_SHORT).show();
                                    //consultarTodo();
                                    editText.setText((CharSequence) "");
                                    return true;
                                } catch (IOException var6_4) {
                                    var6_4.printStackTrace();
                                    return true;
                                } catch (SQLException var5_5) {
                                    var5_5.printStackTrace();
                                    return true;
                                } catch (Exception var4_6) {
                                    Toast.makeText((Context) InventarioFisico.this, (CharSequence) "Hace faltan datos!", Toast.LENGTH_SHORT).show();
                                    return true;
                                }
                            }
                            return false;
                        }
                    });
                    editText3.setOnKeyListener(new View.OnKeyListener() {
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            int n2 = event.getAction();
                            boolean bl = false;
                            if (n2 != 0) return bl;
                            bl = false;
                            if (keyCode != 66) return bl;
                            try {
                                guardarProducto2(editText.getText().toString(), Integer.parseInt((String) editText2.getText().toString()), " ", Integer.parseInt((String) editText3.getText().toString()));
                                Toast.makeText((Context) InventarioFisico.this, (CharSequence) "se guardo correctamente!", Toast.LENGTH_SHORT).show();
                                //consultarTodo();
                                editText.setText((CharSequence) "");
                                editText3.setText((CharSequence) "");
                                do {
                                    return true;
                                } while (true);
                            } catch (IOException var8_6) {
                                var8_6.printStackTrace();
                                return true;
                            } catch (SQLException var7_7) {
                                var7_7.printStackTrace();
                                return true;
                            } catch (Exception var6_8) {
                                Toast.makeText((Context) InventarioFisico.this, (CharSequence) "Hace faltan datos!", Toast.LENGTH_SHORT).show();
                                return true;
                            }
                        }
                    });
                    return;
                } catch (Exception var18_19) {
                    Toast.makeText(InventarioFisico.this, (CharSequence) "faltan datos!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            public boolean onOptionsItemSelected(MenuItem menuItem) {
                if (menuItem.getItemId() == 2131296441) {
                    return true;
                }
                return onOptionsItemSelected(menuItem);
            }
        }


