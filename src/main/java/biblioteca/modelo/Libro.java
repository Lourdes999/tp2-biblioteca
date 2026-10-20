package biblioteca.modelo;

public class Libro {
    private final int id;
    private final String isbn;
    private final String titulo;
    private final String autor;
    private final String editorial;
    private final int cantidadEjemplares;
    private int ejemplaresDisponibles;
    private boolean activo;

    public Libro(int id, String isbn, String titulo, String autor, String editorial, int cantidadEjemplares) {
        this.id = id;
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.cantidadEjemplares = cantidadEjemplares;
        this.ejemplaresDisponibles = cantidadEjemplares;
        this.activo = true;
    }

    public int getId() { return id; }
    public String getIsbn() { return isbn; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getEditorial() { return editorial; }
    public int getCantidadEjemplares() { return cantidadEjemplares; }
    public int getEjemplaresDisponibles() { return ejemplaresDisponibles; }
    public void setEjemplaresDisponibles(int ejemplaresDisponibles) { this.ejemplaresDisponibles = ejemplaresDisponibles; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return "Libro #" + id + " | ISBN: " + isbn + " | " + titulo + " - " + autor
                + " | Editorial: " + editorial
                + " | Ejemplares: " + cantidadEjemplares
                + " | Disponibles: " + ejemplaresDisponibles
                + " | Estado: " + (activo ? "Activo" : "Inactivo");
    }
}
