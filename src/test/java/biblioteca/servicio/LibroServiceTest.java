package biblioteca.servicio;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Caso 1 – Verificar disponibilidad de un libro (RN07)
class LibroServiceTest {
    private LibroService servicio;

    @BeforeEach
    void setUp() {
        servicio = new LibroService();
    }

    @AfterEach
    void tearDown() {
        servicio = null;
    }

    @Test
    void libroConEjemplaresEstaDisponible() {
        boolean resultado = servicio.estaDisponible(2);
        assertTrue(resultado);
    }

    @Test
    void libroSinEjemplaresNoEstaDisponible() { // caso límite
        boolean resultado = servicio.estaDisponible(0);
        assertFalse(resultado);
    }
}
