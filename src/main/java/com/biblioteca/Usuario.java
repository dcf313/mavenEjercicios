package com.biblioteca;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String idUsuario;
    private String nombre;
    private List<Material> materialesPrestados;
    private static final int MAX_PRESTAMOS = 3;

    public Usuario(String idUsuario, String nombre) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.materialesPrestados = new ArrayList<>();
    }

    public String getIdUsuario() { return idUsuario; }
    public String getNombre() { return nombre; }
    public List<Material> getMaterialesPrestados() { return new ArrayList<>(materialesPrestados); }

    public void prestarMaterial(Material material) {
        if (materialesPrestados.size() >= MAX_PRESTAMOS) {
            throw new IllegalStateException("El usuario ya ha alcanzado el límite de préstamos (" + MAX_PRESTAMOS + ").");
        }
        materialesPrestados.add(material);
    }

    public void devolverMaterial(Material material) {
        if (!materialesPrestados.contains(material)) {
            throw new IllegalArgumentException("El usuario no tiene prestado este material.");
        }
        materialesPrestados.remove(material);
    }
}
