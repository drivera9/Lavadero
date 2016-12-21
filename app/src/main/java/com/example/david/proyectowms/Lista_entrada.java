package com.example.david.proyectowms;

/*
 * Decompiled with CFR 0_92.
 *
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */

public class Lista_entrada {
    private int idImagen;
    private String textoEncima;

    public Lista_entrada(int n, String string) {
        this.idImagen = n;
        this.textoEncima = string;
    }

    public int get_idImagen() {
        return this.idImagen;
    }

    public String get_textoEncima() {
        return this.textoEncima;
    }
}
