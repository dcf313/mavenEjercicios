package com.biblioteca;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    private Usuario usuario;
    private Libro libroPrueba;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("U100", "Juan Perez");
        libroPrueba = new Libro("L100", "Testing Book", 2020, "Author", 100);
    }

    @Test
    void testCreacionUsuario() {
        assertEquals("U100", usuario.getIdUsuario());
        assertEquals("Juan Perez", usuario.getNombre());
        assertTrue(usuario.getMaterialesPrestados().isEmpty());
    }

    @Test
    void testDevolverMaterialNoPrestado() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            usuario.devolverMaterial(libroPrueba);
        });
        assertEquals("El usuario no tiene prestado este material.", exception.getMessage());
    }
}
