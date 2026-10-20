package biblioteca.modelo;

import java.time.LocalDate;

public class Prestamo {
    public static final String ACTIVO = "Activo";
    public static final String DEVUELTO = "Devuelto";

    private final int id;
    private final Socio socio;
    private final Libro libro;
    private final LocalDate fechaPrestamo;
    private final LocalDate fechaPrevistaDevolucion;
    private LocalDate fechaDevolucion;
    private String estado;

    public Prestamo(int id, Socio socio, Libro libro, LocalDate fechaPrestamo, LocalDate fechaPrevistaDevolucion) {
        this.id = id;
        this.socio = socio;
        this.libro = libro;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaPrevistaDevolucion = fechaPrevistaDevolucion;
        this.estado = ACTIVO;
    }

    public int getId() { return id; }
    public Socio getSocio() { return socio; }
    public Libro getLibro() { return libro; }
    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
    public LocalDate getFechaPrevistaDevolucion() { return fechaPrevistaDevolucion; }
    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    public String getEstado() { return estado; }
    public boolean isActivo() { return ACTIVO.equals(estado); }

    public void registrarDevolucion(LocalDate fecha) {
        this.fechaDevolucion = fecha;
        this.estado = DEVUELTO;
    }

    @Override
    public String toString() {
        return "Préstamo #" + id + " | Socio: " + socio.getDni() + " (" + socio.getApellido() + ")"
                + " | Libro: " + libro.getTitulo()
                + " | Desde: " + fechaPrestamo + " | Hasta: " + fechaPrevistaDevolucion
                + (fechaDevolucion != null ? " | Devuelto el: " + fechaDevolucion : "")
                + " | Estado: " + estado;
    }
}
