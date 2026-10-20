package biblioteca.servicio;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrestamoServiceTest {
    private PrestamoService servicio;

    // Antes de cada prueba se crea un servicio nuevo, para que ninguna prueba dependa de otra
    @BeforeEach
    void setUp() {
        servicio = new PrestamoService(new SocioService(), new LibroService());
    }

    // Después de cada prueba se libera el servicio
    @AfterEach
    void tearDown() {
        servicio = null;
    }

    // ---------- Caso 2 – Cantidad máxima de préstamos (RN08) ----------

    @Test
    void socioConUnPrestamoPuedePedirOtro() { // prueba normal
        assertTrue(servicio.puedePedirPrestamo(1));
    }

    @Test
    void socioConTresPrestamosNoPuedePedirOtro() { // caso límite
        assertFalse(servicio.puedePedirPrestamo(3));
    }

    // ---------- Caso 3 – Disminuir disponibilidad al prestar (RN11) ----------

    @Test
    void prestarDisminuyeLaDisponibilidad() { // prueba correcta
        assertEquals(2, servicio.prestarEjemplar(3));
    }

    @Test
    void prestarSinDisponiblesLanzaExcepcion() { // caso inválido
        assertThrows(IllegalArgumentException.class, () -> servicio.prestarEjemplar(0));
    }

    // ---------- Caso 4 – Devolución de libro (RN12) ----------

    @Test
    void devolverAumentaLaDisponibilidad() { // prueba normal
        assertEquals(2, servicio.devolverEjemplar(1, 3));
    }

    @Test
    void devolverConTodosLosEjemplaresDisponiblesLanzaExcepcion() { // caso inválido
        assertThrows(IllegalArgumentException.class, () -> servicio.devolverEjemplar(3, 3));
    }
}
