package com.biblioteca;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class BibliotecaTest {

    private Biblioteca biblioteca;
    private Libro libro1;
    private Revista revista1;
    private Usuario usuario1;

    @BeforeEach
    void setUp() {
        biblioteca = new Biblioteca();
        libro1 = new Libro("L1", "El Señor de los Anillos", 1954, "J.R.R. Tolkien", 1200);
        revista1 = new Revista("R1", "National Geographic", 2023, 150, "Ciencia y Naturaleza");
        usuario1 = new Usuario("U1", "Ana Garcia");
        
        biblioteca.agregarMaterial(libro1);
        biblioteca.agregarMaterial(revista1);
        biblioteca.registrarUsuario(usuario1);
    }

    @Test
    void testAgregarMaterialDuplicado() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            biblioteca.agregarMaterial(new Libro("L1", "Otro título", 2020, "Autor", 100));
        });
        assertTrue(exception.getMessage().contains("Ya existe un material con este ID"));
    }

    @Test
    void testRegistrarUsuarioDuplicado() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            biblioteca.registrarUsuario(new Usuario("U1", "Carlos"));
        });
        assertTrue(exception.getMessage().contains("Ya existe un usuario con este ID"));
    }

    @Test
    void testBuscarMaterialPorTitulo() {
        List<Material> resultados = biblioteca.buscarMaterialesPorTitulo("Señor");
        assertEquals(1, resultados.size());
        assertEquals("L1", resultados.get(0).getId());
    }

    @Test
    void testPrestarMaterialExitoso() {
        biblioteca.prestarMaterial("L1", "U1");
        
        assertEquals(EstadoMaterial.PRESTADO, libro1.getEstado());
        assertEquals(1, usuario1.getMaterialesPrestados().size());
        assertTrue(usuario1.getMaterialesPrestados().contains(libro1));
    }

    @Test
    void testPrestarMaterialNoDisponible() {
        biblioteca.prestarMaterial("L1", "U1");
        
        Usuario usuario2 = new Usuario("U2", "Pedro");
        biblioteca.registrarUsuario(usuario2);
        
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            biblioteca.prestarMaterial("L1", "U2");
        });
        assertTrue(exception.getMessage().contains("no está disponible para préstamo"));
    }

    @Test
    void testPrestarMasDelLimitePermitido() {
        biblioteca.agregarMaterial(new Libro("L2", "Libro 2", 2000, "Autor 2", 200));
        biblioteca.agregarMaterial(new Libro("L3", "Libro 3", 2001, "Autor 3", 300));
        biblioteca.agregarMaterial(new Libro("L4", "Libro 4", 2002, "Autor 4", 400));
        
        biblioteca.prestarMaterial("L1", "U1");
        biblioteca.prestarMaterial("R1", "U1");
        biblioteca.prestarMaterial("L2", "U1");
        
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            biblioteca.prestarMaterial("L3", "U1"); // El limite es 3
        });
        assertTrue(exception.getMessage().contains("límite de préstamos"));
    }

    @Test
    void testDevolverMaterialExitoso() {
        biblioteca.prestarMaterial("L1", "U1");
        biblioteca.devolverMaterial("L1", "U1");
        
        assertEquals(EstadoMaterial.DISPONIBLE, libro1.getEstado());
        assertEquals(0, usuario1.getMaterialesPrestados().size());
    }

    @Test
    void testUnUsuarioDevuelveMaterialDeOtro() {
        Usuario usuario2 = new Usuario("U2", "Luis");
        biblioteca.registrarUsuario(usuario2);
        biblioteca.prestarMaterial("L1", "U1");
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            biblioteca.devolverMaterial("L1", "U2");
        });
        assertTrue(exception.getMessage().contains("no tiene prestado este material"));
    }

    @Test
    void testPonerEnReparacion() {
        biblioteca.ponerEnReparacion("R1");
        assertEquals(EstadoMaterial.EN_REPARACION, revista1.getEstado());
    }

    @Test
    void testMandarMaterialACambiarEstadoSiEstaPrestadoFalla() {
        biblioteca.prestarMaterial("L1", "U1");
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            biblioteca.ponerEnReparacion("L1");
        });
        assertTrue(exception.getMessage().contains("No se puede reparar un material prestado"));
    }
}
