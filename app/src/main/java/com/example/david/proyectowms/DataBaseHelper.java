package com.example.david.proyectowms;

/*
 * Decompiled with CFR 0_92.
 *
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.content.Context
 *  android.content.res.AssetManager
 *  android.database.Cursor
 *  android.database.sqlite.SQLiteDatabase
 *  android.database.sqlite.SQLiteDatabase$CursorFactory
 *  android.database.sqlite.SQLiteException
 *  android.database.sqlite.SQLiteOpenHelper
 *  java.io.FileOutputStream
 *  java.io.IOException
 *  java.io.InputStream
 *  java.io.PrintStream
 *  java.lang.Error
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.sql.SQLException
 *  java.util.ArrayList
 */
        import android.content.*;
        import android.database.Cursor;
        import android.database.sqlite.*;

        import org.apache.http.message.BasicNameValuePair;

        import java.io.*;
        import java.sql.*;
        import java.text.DateFormat;
        import java.text.SimpleDateFormat;
        import java.util.*;
        import java.util.Date;

/*
 * Failed to analyse overrides
 */
public class DataBaseHelper
        extends SQLiteOpenHelper {
    private static String DB_NAME;
    private static String DB_PATH;
    protected static final String TAG = "DataAdapter";
    private Context myContext;
    private SQLiteDatabase myDataBase;

    static {
        DB_PATH = "/data/data/com.example.david.lavadero/databases/";
        DB_NAME = "trizio";
    }

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int n, int n2) {
    }

    private void copyDataBase() throws IOException {
        int n;
        InputStream inputStream = this.myContext.getAssets().open(DB_NAME);
        FileOutputStream fileOutputStream = new FileOutputStream(DB_PATH + DB_NAME);
        byte[] arrby = new byte[1024];
        while ((n = inputStream.read(arrby)) > 0) {
            fileOutputStream.write(arrby, 0, n);
        }
        fileOutputStream.flush();
        fileOutputStream.close();
        inputStream.close();
    }

    public String[][] consultarProceso(String usuario, String empresa, String sucursal, String proceso){

        String string = "SELECT *  FROM tra_dcaptura WHERE cod_usuario = '" + usuario + "' AND cod_empresa = '" + empresa + "'" +
                " AND cod_proceso = " + proceso + " AND (ind_estado = 'A' OR ind_estado = 'I') AND cod_sucursal = '" + sucursal + "' ORDER BY num_orden,ind_estado DESC";
        System.out.println(string);
        Cursor cursor = this.myDataBase.rawQuery(string, null);
        String [][] general = new String[cursor.getCount()][cursor.getColumnCount()];
        System.out.println("CURSOR - " + cursor.getCount());
        //general[0][0] = cursor.getCount()+ "";
        int j = 0;
        if (cursor.moveToFirst()) {
            do {
                for (int i = 0; i < cursor.getColumnCount(); ++i) {
                    if (cursor.isNull(i)) {
                        general[j][i] = "";
                        continue;
                    }else {
                        general[j][i] = cursor.getString(i);
                    }
                }
                j++;
            } while (cursor.moveToNext());

        }


        return general;
    }

    public ArrayList consultarLocal(String usuario, String empresa, String sucursal) {
        String string2 = "SELECT * FROM tra_detalle WHERE cod_empresa = '" + empresa + "' AND cod_sucursal = " + sucursal + " " +
                "       AND cod_usuario = " + usuario +"";
        Cursor cursor = this.myDataBase.rawQuery(string2, null);
        ArrayList arrayList = new ArrayList();
        new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                for (int i = 0; i <= 46; ++i) {
                    if (cursor.isNull(i)) {
                        arrayList.add("");
                        continue;
                    }
                    arrayList.add(cursor.getString(i));
                }
            } while (cursor.moveToNext());
        }
        return arrayList;
    }

    public void borrarPLU(String string) {
        this.myDataBase.delete("des_invfisico_tmp", "cod_plu = '" + string + "'" + "", null);
    }

    public void borrarMovimiento() {
        this.myDataBase.delete("tra_movimiento", "", null);
    }

    public void borrarUbic(String string) {
        this.myDataBase.delete("des_invfisico_tmp", "cod_ubicacion = '" + string + "'" + "", null);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public boolean checkDataBase() {
        SQLiteDatabase sQLiteDatabase;
        try {
            sQLiteDatabase = SQLiteDatabase.openDatabase((String)(DB_PATH + DB_NAME), (SQLiteDatabase.CursorFactory)null, (int)0);
        }
        catch (SQLiteException var1_4) {
            return false;
        }
        if (sQLiteDatabase != null) {
            sQLiteDatabase.close();
        }
        boolean bl = false;
        if (sQLiteDatabase == null) return bl;
        return true;
    }

    public void close() {
        DataBaseHelper dataBaseHelper = this;
        synchronized (dataBaseHelper) {
            if (this.myDataBase != null) {
                this.myDataBase.close();
            }
            super.close();
            return;
        }
    }

    public void guardarProceso(String cod_empresa,String cod_sucursal,String cod_usuario,
                               String cod_proceso,String cod_atributo,String cod_valor,String num_orden){
        ContentValues contentValues = new ContentValues();
        contentValues.put("cod_empresa", cod_empresa);
        contentValues.put("cod_sucursal", cod_sucursal);
        contentValues.put("cod_usuario", cod_usuario);
        contentValues.put("cod_proceso", cod_proceso );
        contentValues.put("cod_atributo", cod_atributo );
        contentValues.put("cod_valor", cod_valor);
        contentValues.put("num_orden", num_orden);

        this.myDataBase.insert("tra_movimiento", null, contentValues);
    }

    public void guardarMovimiento(String atributo,String valor){
        ContentValues contentValues = new ContentValues();
        contentValues.put("cod_atributo", atributo);
        contentValues.put("cod_valor", valor);
        this.myDataBase.insertWithOnConflict("tra_movimiento", null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void guardarSql(String sql){
        System.out.println(sql);
        this.myDataBase.execSQL(sql);
    }

    public void guardarTraDetalle(String empresa,String sucursal,String usuario , String proceso , String ubicacion , String fecproceso
                                  ,String nit , String estado , String rombo , String placa , String tecnico , String vehiculo ,
                                  String consecutivo , String marca , String precio, String fpago , String notas , String costo , String descprecio,
                                  String descmodelo , String numitem , String numtotal , String codigo , String codubicacionbodega ){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        ContentValues contentValues = new ContentValues();
        contentValues.put("cod_empresa", empresa);
        contentValues.put("cod_sucursal", sucursal);
        contentValues.put("cod_proceso", proceso);
        contentValues.put("num_rombo", rombo);
        contentValues.put("cod_usuario", usuario);
        contentValues.put("cod_ubicacion", ubicacion);
        contentValues.put("fec_proceso", dateFormat.format(date));
        contentValues.put("num_nit", nit);
        contentValues.put("ind_estado", estado);
        contentValues.put("cod_placa", placa);
        contentValues.put("num_item", numitem);
        contentValues.put("num_total", numtotal);
        contentValues.put("cod_codigo", codigo);
        contentValues.put("cod_ubicacion_bodega", codubicacionbodega);
        contentValues.put("fec_lanzamiento", dateFormat.format(date));
        contentValues.put("fec_inicial", dateFormat.format(date));
        contentValues.put("cod_tecnico", tecnico);
        contentValues.put("cod_subgrupo", descmodelo);
        contentValues.put("ind_procesov", vehiculo);
        contentValues.put("val_unitario", precio);
        contentValues.put("val_total", precio);
        contentValues.put("cod_concepto", "51");
        contentValues.put("num_concepto", consecutivo);
        contentValues.put("nom_codigo", descprecio);
        contentValues.put("ano", "");
        contentValues.put("mes", "");
        contentValues.put("dia", "");
        contentValues.put("nom_obs", notas);
        contentValues.put("cod_marca", marca);
        contentValues.put("ind_fpago", fpago);
        contentValues.put("val_costo", costo);

        this.myDataBase.insertWithOnConflict("tra_detalle", null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    /*
     * Enabled aggressive block sorting
     */
    public ArrayList consultarPLU(String string) {
        String string2 = "SELECT des_invfisico_tmp._id, des_invfisico_tmp.cod_ubicacion,des_invfisico_tmp.cod_referencia,des_invfisico_tmp.cod_plu,plu.descripcion  FROM des_invfisico_tmp LEFT OUTER JOIN plu ON plu.cod_plu = des_invfisico_tmp.cod_plu WHERE des_invfisico_tmp.cod_plu = '" + string + "'" + "";
        Cursor cursor = this.myDataBase.rawQuery(string2, null);
        ArrayList arrayList = new ArrayList();
        new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                for (int i = 0; i <= 4; ++i) {
                    if (cursor.isNull(i)) {
                        arrayList.add("");
                        continue;
                    }
                    arrayList.add(cursor.getString(i));
                }
            } while (cursor.moveToNext());
        }
        return arrayList;
    }

    public ArrayList consultarValor() {
        String string2 = "SELECT cod_valor FROM tra_movimiento ORDER BY cod_atributo ";
        Cursor cursor = this.myDataBase.rawQuery(string2, null);
        ArrayList arrayList = new ArrayList();
        new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                for (int i = 0; i < 1; ++i) {
                    if (cursor.isNull(i)) {
                        arrayList.add("");
                        continue;
                    }
                    arrayList.add(cursor.getString(i));
                }
            } while (cursor.moveToNext());
        }
        return arrayList;
    }

    public ArrayList consultarAtributo() {
        String string2 = "SELECT cod_atributo FROM tra_movimiento ORDER BY cod_atributo";
        Cursor cursor = this.myDataBase.rawQuery(string2, null);
        ArrayList arrayList = new ArrayList();
        new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                for (int i = 0; i < 1; ++i) {
                    if (cursor.isNull(i)) {
                        arrayList.add("");
                        continue;
                    }
                    arrayList.add(cursor.getString(i));
                }
            } while (cursor.moveToNext());
        }

        for (int i = 0;i<arrayList.size();i++){
            System.out.println(arrayList.get(i));
        }
        return arrayList;
    }

    /*
     * Enabled aggressive block sorting
     */
    public ArrayList consultarTodo() {
        Cursor cursor = this.myDataBase.rawQuery("SELECT des_invfisico_tmp._id,des_invfisico_tmp.cod_ubicacion,des_invfisico_tmp.cod_referencia,des_invfisico_tmp.cod_plu,plu.descripcion  FROM des_invfisico_tmp LEFT OUTER JOIN plu ON plu.cod_plu = des_invfisico_tmp.cod_plu  ", null);
        ArrayList arrayList = new ArrayList();
        new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                for (int i = 0; i <= 4; ++i) {
                    if (cursor.isNull(i)) {
                        arrayList.add("");
                        continue;
                    }
                    arrayList.add(cursor.getString(i));
                }
            } while (cursor.moveToNext());
        }

        return arrayList;
    }
    /*
     * Enabled aggressive block sorting
     */
    public ArrayList consultarUbic(String string) {
        String string2 = "SELECT des_invfisico_tmp._id, des_invfisico_tmp.cod_ubicacion,des_invfisico_tmp.cod_referencia,des_invfisico_tmp.cod_plu,plu.descripcion  FROM des_invfisico_tmp LEFT OUTER JOIN plu ON plu.cod_plu = des_invfisico_tmp.cod_plu WHERE des_invfisico_tmp.cod_ubicacion  = '" + string + "'" + "";
        Cursor cursor = this.myDataBase.rawQuery(string2, null);
        ArrayList arrayList = new ArrayList();
        new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                for (int i = 0; i <= 4; ++i) {
                    if (cursor.isNull(i)) {
                        arrayList.add("");
                        continue;
                    }
                    arrayList.add(cursor.getString(i));
                }
            } while (cursor.moveToNext());
        }
        return arrayList;
    }
    public void createDataBase() throws IOException {
        if (this.checkDataBase()) {
            return;
        }
        this.getWritableDatabase();
        try {
            this.copyDataBase();
            return;
        }
        catch (IOException var2_1) {
            throw new Error("Error copiando Base de Datos");
        }
    }
    public ArrayList getConsultas(String string) {
        String string2 = "SELECT sysmod.desc_opc FROM security_groupings,security_users,sysmod,syspan,sysper  WHERE ( sysmod.codi_pan =syspan.codi_pan ) and  ( sysmod.codi_emp = sysper.codi_emp ) and  ( sysmod.codi_mod = sysper.codi_opc ) and   ( security_groupings.group_name = security_users.name ) and  ( sysper.codi_are = security_users.name)  and  ( security_users.name = '" + string + "'" + ")";
        Cursor cursor = this.myDataBase.rawQuery(string2, null);
        ArrayList arrayList = new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                arrayList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return arrayList;
    }
    public ArrayList getContraseñas() {
        Cursor cursor = this.myDataBase.rawQuery("SELECT password FROM security_users", null);
        ArrayList arrayList = new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                arrayList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return arrayList;
    }
    public ArrayList getContrasenasProcesos(String usuario) {
        Cursor cursor = this.myDataBase.rawQuery("SELECT cod_clave,cod_sucursal,cod_empresa,nom_usuario from tra_captura WHERE nom_usuario = '" + usuario + "'" , null);

        ArrayList arrayList = new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                arrayList.add(cursor.getString(0));
                arrayList.add(cursor.getString(1));
                arrayList.add(cursor.getString(2));
                arrayList.add(cursor.getString(3));
            } while (cursor.moveToNext());
        }

        for (int i = 0;i<arrayList.size();i++){
            System.out.println("ARRAY - " + i + " " + arrayList.get(i) );
        }
        return arrayList;
    }
    public ArrayList getProcesos(String usuario,String sucursal , String empresa) {
        Cursor cursor = this.myDataBase.rawQuery("SELECT cod_proceso from tra_captura WHERE nom_usuario = '" + usuario + "' AND cod_sucursal = '" + sucursal + "' AND cod_empresa = '" + empresa + "' AND ind_estado = 'A'" , null);
        System.out.println("cursor - " + cursor.getCount());
        ArrayList arrayList = new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                arrayList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        return arrayList;
    }

    public ArrayList getNomProcesos(String proceso) {
        Cursor cursor = this.myDataBase.rawQuery("SELECT ltrim(rtrim(nom_proceso)) as nom_proceso from tra_procesos WHERE cod_proceso = " + proceso + " ", null);
        ArrayList arrayList = new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                arrayList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        return arrayList;
    }

    public ArrayList getRecurso(String empresa,String sucursal, String atributo) {
        Cursor cursor = this.myDataBase.rawQuery("SELECT val_recurso from tra_recursos WHERE ind_estado = 'A' " +
                "AND cod_recurso ='" + atributo + "' AND cod_empresa = '" + empresa + "' AND  " +
                "cod_sucursal = '" + sucursal + "'" , null);

        System.out.println("CURSOR - " + cursor.getCount());
        ArrayList arrayList = new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                arrayList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        return arrayList;
    }

    public ArrayList getOpciones() {
        Cursor cursor = this.myDataBase.rawQuery("SELECT user_type FROM security_users", null);
        ArrayList arrayList = new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                arrayList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        for (int i = 0;i<arrayList.size();i++){
            System.out.println(arrayList.get(i));
        }
        return arrayList;
    }

    public void updateNit(String cedula, String nombre1, String nombre2, String apellido1, String apellido2){
        ContentValues contentValues = new ContentValues();
        contentValues.put("num_rombo", "");
        contentValues.put("cod_placa", "");
        contentValues.put("num_nit", cedula);
        contentValues.put("nom_apellido1", apellido1);
        contentValues.put("nom_apellido2", apellido2);
        contentValues.put("nom_nombre1", nombre1);
        contentValues.put("nom_nombre2", nombre2);
        contentValues.put("fec_nacimiento", "");
        contentValues.put("ind_sexo", "");

        this.myDataBase.insert("tra_nit", null, contentValues);
    }

    public ArrayList getUsuarios() {
        Cursor cursor = this.myDataBase.rawQuery("SELECT name FROM security_users", null);
        ArrayList arrayList = new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                arrayList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return arrayList;
    }
    public ArrayList getVista() {
        Cursor cursor = this.myDataBase.rawQuery("SELECT tipo_vista FROM security_users", null);
        ArrayList arrayList = new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                arrayList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return arrayList;
    }
    public ArrayList getVista2(String string) {
        String string2 = "SELECT sysmod.desc_mod FROM security_groupings,security_users,sysmod,syspan,sysper  WHERE ( sysmod.codi_pan =syspan.codi_pan ) and  ( sysmod.codi_emp = sysper.codi_emp ) and  ( sysmod.codi_mod = sysper.codi_mod ) and   ( security_groupings.group_name = security_users.name ) and  ( sysper.codi_are = security_users.name)  and  ( security_users.name = '" + string + "'" + ")";
        Cursor cursor = this.myDataBase.rawQuery(string2, null);
        ArrayList arrayList = new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                arrayList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return arrayList;
    }
    public void guardarProducto(String plu, int ubicacion, int ref, int cant, String string) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("cod_empresa", "");
        contentValues.put("ser_invcon", "");
        contentValues.put("cod_tercero", "");
        contentValues.put("tip_tercero", "");
        contentValues.put("cod_plu", plu);
        contentValues.put("cod_proveedoref", "");
        contentValues.put("cod_referencia", ref);
        contentValues.put("ind_calidad", "");
        contentValues.put("num_tiquete", "");
        contentValues.put("cod_ubicacion", ubicacion);
        contentValues.put("can_unidades", cant);
        contentValues.put("cod_usuario", "");
        contentValues.put("ind_estado", "");
        contentValues.put("descripcion", string);
        this.myDataBase.insert("des_invfisico_tmp", null, contentValues);
    }
    public void guardarProductoPLU(int plu, int ubic, int ref, int cant, String string, int costo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("empresa", "");
        contentValues.put("sucursal", "");
        contentValues.put("cod_plu", plu);
        contentValues.put("cod_referencia", ref);
        contentValues.put("cant", cant);
        contentValues.put("costo", costo);
        contentValues.put("precio", "");
        contentValues.put("estado", "");
        contentValues.put("cod_ubicacion", ubic);
        contentValues.put("descripcion", string);
        this.myDataBase.insert("plu", null, contentValues);

    }
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
    }
    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */

    public void openDataBase() throws SQLException {
        try {
            this.createDataBase();
        }
        catch (IOException var1_1) {
            throw new Error("Ha sido imposible crear la Base de Datos");
        }
        this.myDataBase = SQLiteDatabase.openDatabase((String)(DB_PATH + DB_NAME), (SQLiteDatabase.CursorFactory)null, (int)0);
    }

    public void update2(String string,String plu,String ref, String ubic) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("empresa", "");
        contentValues.put("sucursal", "");
        contentValues.put("cod_plu", plu);
        contentValues.put("cod_referencia", ref);
        contentValues.put("cant", "");
        contentValues.put("costo", "");
        contentValues.put("precio", "");
        contentValues.put("estado", "");
        contentValues.put("cod_ubicacion", ubic);
        contentValues.put("descripcion", string);
        this.myDataBase.insert("plu", null, contentValues);
    }

    public Cursor contar(String string) {
        Cursor cursor = this.myDataBase.rawQuery("SELECT " + string + "  FROM des_invfisico_tmp LEFT OUTER JOIN plu ON plu.cod_plu = des_invfisico_tmp.cod_plu WHERE not " + string + " = '' ", null);

        return cursor;
    }

    public Cursor contarPLU(String string , String plu) {
        Cursor cursor = this.myDataBase.rawQuery("SELECT  " + string + "  FROM des_invfisico_tmp LEFT OUTER JOIN plu ON plu.cod_plu = des_invfisico_tmp.cod_plu WHERE des_invfisico_tmp.cod_plu = " + plu + "", null);

        return cursor;
    }

    public Cursor contarUbic(String string , String ubic) {
        Cursor cursor = this.myDataBase.rawQuery("SELECT " + string + "  FROM des_invfisico_tmp LEFT OUTER JOIN plu ON plu.cod_plu = des_invfisico_tmp.cod_plu WHERE des_invfisico_tmp.cod_ubicacion  = " + ubic + "  AND not " + string + " = ''" , null);
        return cursor;
    }

    public void update(String string, int n, int n2) {
        ContentValues contentValues = new ContentValues();
        switch (n2) {
            default: {
                return;
            }
            case 1: {
                String string2 = "_id=" + n;
                contentValues.put("cod_ubicacion", string);
                this.myDataBase.update("des_invfisico_tmp", contentValues, string2, null);
                return;
            }
            case 2: {
                String string3 = "_id=" + n;
                contentValues.put("cod_referencia", string);
                this.myDataBase.update("des_invfisico_tmp", contentValues, string3, null);
                return;
            }
            case 3: {
                String string4 = "_id=" + n;
                contentValues.put("cod_plu", string);
                this.myDataBase.update("des_invfisico_tmp", contentValues, string4, null);
                return;
            }
            case 4:
        }
        String string5 = "_id=" + n;
        contentValues.put("descripcion", string);
        this.myDataBase.update("des_invfisico_tmp", contentValues, string5, null);
    }
}