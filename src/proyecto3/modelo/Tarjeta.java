package proyecto3.modelo;

/**
 * Representa una tarjeta de la colección DC Comics.
 * Es inmutable después de su creación para mantener el orden del ABB.
 */
public class Tarjeta {

    private int id;
    private String descripcion;
    private String categoria;

    public Tarjeta(int id, String descripcion, String categoria) {
        this.id = id;
        this.descripcion = descripcion;
        this.categoria = categoria;
    }

    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    @Override
    public String toString() {
        return "ID: " + id + "\nCategoría: " + categoria + "\nDescripción: " + descripcion;
    }
}
