package biblioteca.servicio;

import biblioteca.modelo.Libro;
import biblioteca.modelo.Prestamo;
import biblioteca.modelo.Socio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrestamoService {
    public static final int MAXIMO_PRESTAMOS_ACTIVOS = 3;

    private final List<Prestamo> prestamos = new ArrayList<>();
    private final SocioService socioService;
    private final LibroService libroService;
    private int proximoId = 1;

    public PrestamoService(SocioService socioService, LibroService libroService) {
        this.socioService = socioService;
        this.libroService = libroService;
    }

    // ---------- Métodos simples usados por las pruebas JUnit ----------

    // Caso 2 - RN08: un socio puede tener como máximo 3 préstamos activos
    public boolean puedePedirPrestamo(int prestamosActivos) {
        return prestamosActivos < MAXIMO_PRESTAMOS_ACTIVOS;
    }

    // Caso 3 - RN11: al prestar disminuye la cantidad disponible
    public int prestarEjemplar(int disponibles) {
        if (disponibles <= 0) {
            throw new IllegalArgumentException("No hay ejemplares disponibles");
        }
        return disponibles - 1;
    }

    // Caso 4 - RN12: al devolver aumenta la cantidad disponible,
    // sin superar la cantidad total de ejemplares del libro
    public int devolverEjemplar(int disponibles, int cantidadTotal) {
        if (disponibles < 0 || disponibles >= cantidadTotal) {
            throw new IllegalArgumentException("No hay ejemplares prestados para devolver");
        }
        return disponibles + 1;
    }

    // ---------- Operaciones del sistema ----------

    public Prestamo registrarPrestamo(String dni, String isbn, LocalDate fechaPrestamo, LocalDate fechaPrevista) {
        Socio socio = socioService.buscarPorDni(dni);
        if (socio == null) {
            throw new IllegalArgumentException("El socio no existe");
        }
        // RN03: un socio inactivo no puede realizar préstamos
        if (!socio.isActivo()) {
            throw new IllegalArgumentException("El socio está inactivo");
        }
        Libro libro = libroService.buscarPorIsbn(isbn);
        if (libro == null) {
            throw new IllegalArgumentException("El libro no existe");
        }
        if (!libro.isActivo()) {
            throw new IllegalArgumentException("El libro está inactivo");
        }
        // RN07 / RN09: solo se presta si hay disponibilidad
        if (!libroService.estaDisponible(libro.getEjemplaresDisponibles())) {
            throw new IllegalArgumentException("El libro no tiene ejemplares disponibles");
        }
        // RN08: máximo 3 préstamos activos
        if (!puedePedirPrestamo(contarPrestamosActivos(dni))) {
            throw new IllegalArgumentException("El socio ya tiene " + MAXIMO_PRESTAMOS_ACTIVOS + " préstamos activos");
        }
        if (fechaPrevista.isBefore(fechaPrestamo)) {
            throw new IllegalArgumentException("La fecha de devolución no puede ser anterior a la del préstamo");
        }
        // RN11: disminuye la disponibilidad
        libro.setEjemplaresDisponibles(prestarEjemplar(libro.getEjemplaresDisponibles()));

        Prestamo prestamo = new Prestamo(proximoId++, socio, libro, fechaPrestamo, fechaPrevista);
        prestamos.add(prestamo);
        return prestamo;
    }

    public Prestamo registrarDevolucion(int idPrestamo, LocalDate fechaDevolucion) {
        Prestamo prestamo = buscarPorId(idPrestamo);
        if (prestamo == null) {
            throw new IllegalArgumentException("El préstamo no existe");
        }
        // RN10: solo se devuelve un préstamo activo
        if (!prestamo.isActivo()) {
            throw new IllegalArgumentException("El préstamo no está activo");
        }
        Libro libro = prestamo.getLibro();
        // RN12: aumenta la disponibilidad
        libro.setEjemplaresDisponibles(
                devolverEjemplar(libro.getEjemplaresDisponibles(), libro.getCantidadEjemplares()));
        prestamo.registrarDevolucion(fechaDevolucion);
        return prestamo;
    }

    public int contarPrestamosActivos(String dni) {
        int cantidad = 0;
        for (Prestamo p : prestamos) {
            if (p.isActivo() && p.getSocio().getDni().equals(dni)) {
                cantidad++;
            }
        }
        return cantidad;
    }

    public List<Prestamo> prestamosActivosDe(String dni) {
        List<Prestamo> resultado = new ArrayList<>();
        for (Prestamo p : prestamos) {
            if (p.isActivo() && p.getSocio().getDni().equals(dni)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public Prestamo buscarPorId(int id) {
        for (Prestamo p : prestamos) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public List<Prestamo> listar() {
        return prestamos;
    }
}
