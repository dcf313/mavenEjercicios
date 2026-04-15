package com.biblioteca;

public class Libro extends Material {
    private String autor;
    private int numeroPaginas;

    public Libro(String id, String titulo, int anioPublicacion, String autor, int numeroPaginas) {
        super(id, titulo, anioPublicacion);
        this.autor = autor;
        this.numeroPaginas = numeroPaginas;
    }

    public String getAutor() { return autor; }
    public int getNumeroPaginas() { return numeroPaginas; }
}
