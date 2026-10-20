package biblioteca;

import biblioteca.modelo.Libro;
import biblioteca.modelo.Prestamo;
import biblioteca.modelo.Socio;
import biblioteca.servicio.LibroService;
import biblioteca.servicio.PrestamoService;
import biblioteca.servicio.SocioService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final Scanner sc = new Scanner(System.in);

    private static SocioService socioService;
    private static LibroService libroService;
    private static PrestamoService prestamoService;

    public static void main(String[] args) {
        cargarDatosDePrueba();
        int opcion;
        do {
            System.out.println();
            System.out.println("===== SISTEMA DE GESTIÓN DE BIBLIOTECA =====");
            System.out.println("1. Registrar socio");
            System.out.println("2. Consultar socio");
            System.out.println("3. Activar / desactivar socio");
            System.out.println("4. Registrar libro");
            System.out.println("5. Consultar disponibilidad de libro");
            System.out.println("6. Activar / desactivar libro");
            System.out.println("7. Registrar préstamo");
            System.out.println("8. Registrar devolución");
            System.out.println("9. Listar todo (socios, libros y préstamos)");
            System.out.println("10. Reiniciar datos de prueba");
            System.out.println("0. Salir");
            opcion = leerEntero("Opción: ");
            try {
                switch (opcion) {
                    case 1 -> registrarSocio();
                    case 2 -> consultarSocio();
                    case 3 -> cambiarEstadoSocio();
                    case 4 -> registrarLibro();
                    case 5 -> consultarLibro();
                    case 6 -> cambiarEstadoLibro();
                    case 7 -> registrarPrestamo();
                    case 8 -> registrarDevolucion();
                    case 9 -> listarTodo();
                    case 10 -> {
                        cargarDatosDePrueba();
                        System.out.println("Datos de prueba reiniciados.");
                    }
                    case 0 -> System.out.println("Fin del programa.");
                    default -> System.out.println("Opción inválida.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    // Datos iniciales usados como precondición en los casos de prueba
    private static void cargarDatosDePrueba() {
        socioService = new SocioService();
        libroService = new LibroService();
        prestamoService = new PrestamoService(socioService, libroService);

        libroService.registrarLibro("9789500000001", "Ficciones", "Jorge Luis Borges", "Emecé", 3);
        libroService.registrarLibro("9789500000002", "Rayuela", "Julio Cortázar", "Sudamericana", 1);
        libroService.registrarLibro("9789500000003", "Martín Fierro", "José Hernández", "Kapelusz", 2);

        socioService.registrarSocio("33999000", "Pedro", "Ruiz", "pedro@mail.com", "2474-555666");
        socioService.cambiarEstado("33999000", false);
    }

    // ---------- Socios ----------

    private static void registrarSocio() {
        String dni = leerTexto("DNI: ");
        String nombre = leerTexto("Nombre: ");
        String apellido = leerTexto("Apellido: ");
        String correo = leerTexto("Correo: ");
        String telefono = leerTexto("Teléfono: ");
        Socio socio = socioService.registrarSocio(dni, nombre, apellido, correo, telefono);
        System.out.println("Socio registrado: " + socio);
    }

    private static void consultarSocio() {
        String dni = leerTexto("DNI del socio: ");
        Socio socio = socioService.buscarPorDni(dni);
        if (socio == null) {
            System.out.println("No existe un socio con ese DNI.");
            return;
        }
        System.out.println(socio);
        System.out.println("Préstamos activos: " + prestamoService.contarPrestamosActivos(dni));
        for (Prestamo p : prestamoService.prestamosActivosDe(dni)) {
            System.out.println("  " + p);
        }
    }

    private static void cambiarEstadoSocio() {
        String dni = leerTexto("DNI del socio: ");
        int opcion = leerEntero("1. Activar  2. Desactivar: ");
        socioService.cambiarEstado(dni, opcion == 1);
        System.out.println("Estado actualizado: " + socioService.buscarPorDni(dni));
    }

    // ---------- Libros ----------

    private static void registrarLibro() {
        String isbn = leerTexto("ISBN: ");
        String titulo = leerTexto("Título: ");
        String autor = leerTexto("Autor: ");
        String editorial = leerTexto("Editorial: ");
        int cantidad = leerEntero("Cantidad de ejemplares: ");
        Libro libro = libroService.registrarLibro(isbn, titulo, autor, editorial, cantidad);
        System.out.println("Libro registrado: " + libro);
    }

    private static void consultarLibro() {
        int tipo = leerEntero("Buscar por: 1. Título  2. Autor  3. ISBN: ");
        String texto = leerTexto("Texto a buscar: ");
        List<Libro> encontrados;
        if (tipo == 1) {
            encontrados = libroService.buscarPorTitulo(texto);
        } else if (tipo == 2) {
            encontrados = libroService.buscarPorAutor(texto);
        } else {
            Libro libro = libroService.buscarPorIsbn(texto);
            encontrados = (libro == null) ? List.of() : List.of(libro);
        }
        if (encontrados.isEmpty()) {
            System.out.println("No se encontraron libros.");
            return;
        }
        for (Libro l : encontrados) {
            System.out.println(l.getTitulo() + " - " + l.getAutor() + " (ISBN " + l.getIsbn() + "): "
                    + libroService.textoDisponibilidad(l)
                    + " | Ejemplares disponibles: " + l.getEjemplaresDisponibles());
        }
    }

    private static void cambiarEstadoLibro() {
        String isbn = leerTexto("ISBN del libro: ");
        int opcion = leerEntero("1. Activar  2. Desactivar: ");
        libroService.cambiarEstado(isbn, opcion == 1);
        System.out.println("Estado actualizado: " + libroService.buscarPorIsbn(isbn));
    }

    // ---------- Préstamos ----------

    private static void registrarPrestamo() {
        String dni = leerTexto("DNI del socio: ");
        String isbn = leerTexto("ISBN del libro: ");
        LocalDate desde = leerFecha("Fecha de préstamo (dd/mm/aaaa): ");
        LocalDate hasta = leerFecha("Fecha prevista de devolución (dd/mm/aaaa): ");
        Prestamo prestamo = prestamoService.registrarPrestamo(dni, isbn, desde, hasta);
        System.out.println("Préstamo registrado: " + prestamo);
        System.out.println("Ejemplares disponibles del libro: " + prestamo.getLibro().getEjemplaresDisponibles());
    }

    private static void registrarDevolucion() {
        String dni = leerTexto("DNI del socio: ");
        List<Prestamo> activos = prestamoService.prestamosActivosDe(dni);
        if (activos.isEmpty()) {
            System.out.println("El socio no tiene préstamos activos.");
        } else {
            System.out.println("Préstamos activos del socio:");
            for (Prestamo p : activos) {
                System.out.println("  " + p);
            }
        }
        int id = leerEntero("Número de préstamo a devolver: ");
        LocalDate fecha = leerFecha("Fecha de devolución (dd/mm/aaaa): ");
        Prestamo prestamo = prestamoService.registrarDevolucion(id, fecha);
        System.out.println("Devolución registrada: " + prestamo);
        System.out.println("Ejemplares disponibles del libro: " + prestamo.getLibro().getEjemplaresDisponibles());
    }

    private static void listarTodo() {
        System.out.println("--- Socios ---");
        socioService.listar().forEach(System.out::println);
        System.out.println("--- Libros ---");
        libroService.listar().forEach(System.out::println);
        System.out.println("--- Préstamos ---");
        prestamoService.listar().forEach(System.out::println);
    }

    // ---------- Lectura de datos ----------

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine();
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                return Integer.parseInt(leerTexto(mensaje).trim());
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número.");
            }
        }
    }

    private static LocalDate leerFecha(String mensaje) {
        while (true) {
            try {
                return LocalDate.parse(leerTexto(mensaje).trim(), FORMATO_FECHA);
            } catch (DateTimeParseException e) {
                System.out.println("Fecha inválida. Use el formato dd/mm/aaaa.");
            }
        }
    }
}
