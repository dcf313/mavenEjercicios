package com.biblioteca;

public class Revista extends Material {
    private int numeroEdicion;
    private String tematica;

    public Revista(String id, String titulo, int anioPublicacion, int numeroEdicion, String tematica) {
        super(id, titulo, anioPublicacion);
        this.numeroEdicion = numeroEdicion;
        this.tematica = tematica;
    }

    public int getNumeroEdicion() { return numeroEdicion; }
    public String getTematica() { return tematica; }
}
