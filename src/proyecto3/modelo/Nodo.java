package proyecto3.modelo;

/**
 * Nodo del árbol binario de búsqueda.
 * Almacena una Tarjeta y referencias a los subárboles izquierdo y derecho.
 */
public class Nodo {

    // Acceso de paquete para que ArbolBinarioBusqueda pueda enlazar nodos directamente
    Tarjeta tarjeta;
    Nodo hijoIzquierdo;
    Nodo hijoDerecho;

    public Nodo(Tarjeta tarjeta) {
        this.tarjeta = tarjeta;
        this.hijoIzquierdo = null;
        this.hijoDerecho = null;
    }

    public Tarjeta getTarjeta() {
        return tarjeta;
    }

    public Nodo getHijoIzquierdo() {
        return hijoIzquierdo;
    }

    public Nodo getHijoDerecho() {
        return hijoDerecho;
    }
}
