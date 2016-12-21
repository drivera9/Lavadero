package com.example.david.proyectowms;

/**
 * Created by HP on 12/03/2016.
 */
public class Objeto {

    private String codigo;
    private  String concepto;
    private int id;

    public Objeto(String codigo,String concepto, int id) {
        this.codigo = codigo;
        this.concepto = concepto;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConcepto(){
        return concepto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }


}
