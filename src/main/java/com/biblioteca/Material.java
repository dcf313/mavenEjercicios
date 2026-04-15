package com.biblioteca;

public abstract class Material {
    private String id;
    private String titulo;
    private int anioPublicacion;
    private EstadoMaterial estado;

    public Material(String id, String titulo, int anioPublicacion) {
        this.id = id;
        this.titulo = titulo;
        this.anioPublicacion = anioPublicacion;
        this.estado = EstadoMaterial.DISPONIBLE;
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public int getAnioPublicacion() { return anioPublicacion; }
    public EstadoMaterial getEstado() { return estado; }

    public void setEstado(EstadoMaterial estado) {
        this.estado = estado;
    }
}
