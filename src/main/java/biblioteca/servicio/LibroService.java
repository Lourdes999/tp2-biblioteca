package biblioteca.servicio;

import biblioteca.modelo.Libro;

import java.util.ArrayList;
import java.util.List;

public class LibroService {
    private final List<Libro> libros = new ArrayList<>();
    private int proximoId = 1;

    public Libro registrarLibro(String isbn, String titulo, String autor, String editorial, int cantidadEjemplares) {
        // RN04: ISBN, título y autor son obligatorios
        if (estaVacio(isbn)) {
            throw new IllegalArgumentException("El ISBN es obligatorio");
        }
        if (estaVacio(titulo)) {
            throw new IllegalArgumentException("El título es obligatorio");
        }
        if (estaVacio(autor)) {
            throw new IllegalArgumentException("El autor es obligatorio");
        }
        // RN06: la cantidad de ejemplares no puede ser negativa
        if (cantidadEjemplares < 0) {
            throw new IllegalArgumentException("La cantidad de ejemplares no puede ser negativa");
        }
        // RN05: no pueden registrarse dos libros con el mismo ISBN
        if (buscarPorIsbn(isbn) != null) {
            throw new IllegalArgumentException("Ya existe un libro con ese ISBN");
        }
        Libro libro = new Libro(proximoId++, isbn.trim(), titulo.trim(), autor.trim(), editorial, cantidadEjemplares);
        libros.add(libro);
        return libro;
    }

    public Libro buscarPorIsbn(String isbn) {
        if (isbn == null) return null;
        for (Libro l : libros) {
            if (l.getIsbn().equals(isbn.trim())) {
                return l;
            }
        }
        return null;
    }

    // HU03: búsqueda por título o autor (coincidencia parcial, sin distinguir mayúsculas)
    public List<Libro> buscarPorTitulo(String texto) {
        List<Libro> resultado = new ArrayList<>();
        for (Libro l : libros) {
            if (l.getTitulo().toLowerCase().contains(texto.toLowerCase())) {
                resultado.add(l);
            }
        }
        return resultado;
    }

    public List<Libro> buscarPorAutor(String texto) {
        List<Libro> resultado = new ArrayList<>();
        for (Libro l : libros) {
            if (l.getAutor().toLowerCase().contains(texto.toLowerCase())) {
                resultado.add(l);
            }
        }
        return resultado;
    }

    public void cambiarEstado(String isbn, boolean activo) {
        Libro libro = buscarPorIsbn(isbn);
        if (libro == null) {
            throw new IllegalArgumentException("No existe un libro con ese ISBN");
        }
        libro.setActivo(activo);
    }

    // Caso 1 de JUnit - RN07: solo puede prestarse si hay al menos un ejemplar disponible
    public boolean estaDisponible(int ejemplaresDisponibles) {
        return ejemplaresDisponibles > 0;
    }

    public String textoDisponibilidad(Libro libro) {
        return estaDisponible(libro.getEjemplaresDisponibles()) ? "Disponible" : "No disponible";
    }

    public List<Libro> listar() {
        return libros;
    }

    private boolean estaVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}
