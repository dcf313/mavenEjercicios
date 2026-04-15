package com.biblioteca;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Biblioteca {
    private List<Material> catalogo;
    private List<Usuario> usuarios;

    public Biblioteca() {
        this.catalogo = new ArrayList<>();
        this.usuarios = new ArrayList<>();
    }

    public void registrarUsuario(Usuario usuario) {
        if (buscarUsuarioPorId(usuario.getIdUsuario()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario con este ID.");
        }
        usuarios.add(usuario);
    }

    public void agregarMaterial(Material material) {
        if (buscarMaterialPorId(material.getId()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un material con este ID en el catálogo.");
        }
        catalogo.add(material);
    }

    public Optional<Usuario> buscarUsuarioPorId(String idUsuario) {
        return usuarios.stream().filter(u -> u.getIdUsuario().equals(idUsuario)).findFirst();
    }

    public Optional<Material> buscarMaterialPorId(String idMaterial) {
        return catalogo.stream().filter(m -> m.getId().equals(idMaterial)).findFirst();
    }

    public List<Material> buscarMaterialesPorTitulo(String titulo) {
        return catalogo.stream()
                .filter(m -> m.getTitulo().toLowerCase().contains(titulo.toLowerCase()))
                .collect(Collectors.toList());
    }

    public void prestarMaterial(String idMaterial, String idUsuario) {
        Usuario usuario = buscarUsuarioPorId(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
        Material material = buscarMaterialPorId(idMaterial)
                .orElseThrow(() -> new IllegalArgumentException("Material no encontrado."));

        if (material.getEstado() != EstadoMaterial.DISPONIBLE) {
            throw new IllegalStateException("El material no está disponible para préstamo.");
        }

        usuario.prestarMaterial(material);
        material.setEstado(EstadoMaterial.PRESTADO);
    }

    public void devolverMaterial(String idMaterial, String idUsuario) {
        Usuario usuario = buscarUsuarioPorId(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
        Material material = buscarMaterialPorId(idMaterial)
                .orElseThrow(() -> new IllegalArgumentException("Material no encontrado."));

        usuario.devolverMaterial(material);
        material.setEstado(EstadoMaterial.DISPONIBLE);
    }

    public void ponerEnReparacion(String idMaterial) {
        Material material = buscarMaterialPorId(idMaterial)
                .orElseThrow(() -> new IllegalArgumentException("Material no encontrado."));
        
        if (material.getEstado() == EstadoMaterial.PRESTADO) {
            throw new IllegalStateException("No se puede reparar un material prestado.");
        }
        material.setEstado(EstadoMaterial.EN_REPARACION);
    }
}
