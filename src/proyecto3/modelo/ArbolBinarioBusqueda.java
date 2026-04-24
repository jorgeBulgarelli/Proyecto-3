package proyecto3.modelo;

/**
 * Árbol binario de búsqueda que gestiona una colección de tarjetas DC Comics.
 * Ordenado por el campo Id de cada tarjeta.
 * No utiliza clases de colección preconstruidas de Java.
 */
public class ArbolBinarioBusqueda {

    private Nodo raiz;

    public ArbolBinarioBusqueda() {
        raiz = null;
    }

    // -----------------------------------------------------------------------
    // INSERCIÓN
    // -----------------------------------------------------------------------

    /**
     * Inserta una tarjeta en el árbol.
     * Retorna null si la inserción fue exitosa, o un mensaje de error si el Id ya existe.
     */
    public String insertar(int id, String descripcion, String categoria) {
        String[] mensajeError = new String[1]; // canal de salida para error de duplicado
        raiz = insertarRec(raiz, new Tarjeta(id, descripcion, categoria), mensajeError);
        return mensajeError[0];
    }

    private Nodo insertarRec(Nodo nodo, Tarjeta tarjeta, String[] mensajeError) {
        if (nodo == null) {
            return new Nodo(tarjeta);
        }
        if (tarjeta.getId() == nodo.tarjeta.getId()) {
            mensajeError[0] = "Ya existe una tarjeta con ID " + tarjeta.getId() + ". No se insertó el elemento.";
            return nodo;
        }
        if (tarjeta.getId() < nodo.tarjeta.getId()) {
            nodo.hijoIzquierdo = insertarRec(nodo.hijoIzquierdo, tarjeta, mensajeError);
        } else {
            nodo.hijoDerecho = insertarRec(nodo.hijoDerecho, tarjeta, mensajeError);
        }
        return nodo;
    }

    // -----------------------------------------------------------------------
    // ELIMINACIÓN
    // -----------------------------------------------------------------------

    /**
     * Elimina el nodo con el Id dado aplicando las reglas del enunciado.
     * Retorna un mensaje con el resultado de la operación.
     */
    public String eliminar(int id) {
        String[] mensaje = new String[1];
        raiz = eliminarRec(raiz, id, mensaje);
        if (mensaje[0] == null) {
            mensaje[0] = "No se encontró ninguna tarjeta con ID " + id + ".";
        }
        return mensaje[0];
    }

    private Nodo eliminarRec(Nodo nodo, int id, String[] mensaje) {
        if (nodo == null) {
            // Nodo no encontrado — el mensaje "no encontrado" lo asigna eliminar()
            return null;
        }

        if (id < nodo.tarjeta.getId()) {
            nodo.hijoIzquierdo = eliminarRec(nodo.hijoIzquierdo, id, mensaje);
            return nodo;
        }

        if (id > nodo.tarjeta.getId()) {
            nodo.hijoDerecho = eliminarRec(nodo.hijoDerecho, id, mensaje);
            return nodo;
        }

        // Nodo encontrado — aplicar reglas en el orden del enunciado

        // Regla 5 (se verifica primero): categoría "Civiles" nunca se puede eliminar
        if (nodo.tarjeta.getCategoria().equals("Civiles")) {
            mensaje[0] = "No se puede eliminar: las tarjetas de categoría 'Civiles' no pueden ser eliminadas.";
            return nodo;
        }

        boolean tieneIzquierdo = nodo.hijoIzquierdo != null;
        boolean tieneDerecho   = nodo.hijoDerecho   != null;

        if (!tieneIzquierdo && !tieneDerecho) {
            // Regla 1: nodo hoja — se elimina
            mensaje[0] = "Tarjeta con ID " + id + " eliminada exitosamente (era nodo hoja).";
            return null;
        }

        if (!tieneIzquierdo && tieneDerecho) {
            // Regla 2: solo subárbol derecho — NO se elimina
            mensaje[0] = "No se puede eliminar: el nodo con ID " + id + " tiene únicamente subárbol derecho.";
            return nodo;
        }

        if (tieneIzquierdo && !tieneDerecho) {
            // Regla 3: solo subárbol izquierdo — se elimina, el subárbol ocupa su lugar
            mensaje[0] = "Tarjeta con ID " + id + " eliminada. Su subárbol izquierdo ocupa su lugar.";
            return nodo.hijoIzquierdo;
        }

        // Regla 4: dos subárboles — NO se elimina
        mensaje[0] = "No se puede eliminar: el nodo con ID " + id + " tiene dos subárboles (dos hijos).";
        return nodo;
    }

    // -----------------------------------------------------------------------
    // BÚSQUEDA
    // -----------------------------------------------------------------------

    /**
     * Busca una tarjeta por Id recorriendo el árbol.
     * Retorna la Tarjeta si existe, null si no.
     */
    public Tarjeta buscar(int id) {
        return buscarRec(raiz, id);
    }

    private Tarjeta buscarRec(Nodo nodo, int id) {
        if (nodo == null) {
            return null;
        }
        if (id == nodo.tarjeta.getId()) {
            return nodo.tarjeta;
        }
        if (id < nodo.tarjeta.getId()) {
            return buscarRec(nodo.hijoIzquierdo, id);
        }
        return buscarRec(nodo.hijoDerecho, id);
    }

    // -----------------------------------------------------------------------
    // RECORRIDOS
    // -----------------------------------------------------------------------

    /**
     * Retorna los Ids en recorrido pre-orden separados por guiones.
     */
    public String recorridoPreOrden() {
        return preOrdenRec(raiz);
    }

    // Visita: raíz → izquierda → derecha
    private String preOrdenRec(Nodo nodo) {
        if (nodo == null) {
            return "";
        }
        String resultado = String.valueOf(nodo.tarjeta.getId());
        String izq = preOrdenRec(nodo.hijoIzquierdo);
        String der = preOrdenRec(nodo.hijoDerecho);
        if (!izq.isEmpty()) resultado += "-" + izq;
        if (!der.isEmpty()) resultado += "-" + der;
        return resultado;
    }

    /**
     * Retorna los Ids en recorrido in-orden separados por guiones.
     */
    public String recorridoInOrden() {
        return inOrdenRec(raiz);
    }

    // Visita: izquierda → raíz → derecha
    private String inOrdenRec(Nodo nodo) {
        if (nodo == null) {
            return "";
        }
        String izq    = inOrdenRec(nodo.hijoIzquierdo);
        String actual = String.valueOf(nodo.tarjeta.getId());
        String der    = inOrdenRec(nodo.hijoDerecho);

        String resultado = "";
        if (!izq.isEmpty()) resultado = izq + "-";
        resultado += actual;
        if (!der.isEmpty()) resultado += "-" + der;
        return resultado;
    }

    /**
     * Retorna los Ids en recorrido post-orden separados por guiones.
     */
    public String recorridoPostOrden() {
        return postOrdenRec(raiz);
    }

    // Visita: izquierda → derecha → raíz
    private String postOrdenRec(Nodo nodo) {
        if (nodo == null) {
            return "";
        }
        String izq    = postOrdenRec(nodo.hijoIzquierdo);
        String der    = postOrdenRec(nodo.hijoDerecho);
        String actual = String.valueOf(nodo.tarjeta.getId());

        String resultado = "";
        if (!izq.isEmpty()) resultado = izq;
        if (!der.isEmpty()) resultado = resultado.isEmpty() ? der : resultado + "-" + der;
        resultado = resultado.isEmpty() ? actual : resultado + "-" + actual;
        return resultado;
    }

    // -----------------------------------------------------------------------
    // CONSULTAS ADICIONALES
    // -----------------------------------------------------------------------

    /**
     * Retorna la cantidad de nodos con categoría "Súper héroes" o "Súper villanos".
     */
    public int contarHeroesVillanos() {
        return contarHeroesVillanosRec(raiz);
    }

    private int contarHeroesVillanosRec(Nodo nodo) {
        if (nodo == null) {
            return 0;
        }
        String cat = nodo.tarjeta.getCategoria();
        int esCuenta = (cat.equals("Súper héroes") || cat.equals("Súper villanos")) ? 1 : 0;
        return esCuenta
             + contarHeroesVillanosRec(nodo.hijoIzquierdo)
             + contarHeroesVillanosRec(nodo.hijoDerecho);
    }

    /**
     * Retorna las descripciones de tarjetas de categoría "Frases icónicas" que sean nodos hoja,
     * separadas por saltos de línea. Retorna cadena vacía si no hay ninguna.
     */
    public String frasesIconicasHojas() {
        return frasesIconicasHojasRec(raiz);
    }

    private String frasesIconicasHojasRec(Nodo nodo) {
        if (nodo == null) {
            return "";
        }
        boolean esHoja  = nodo.hijoIzquierdo == null && nodo.hijoDerecho == null;
        boolean esFrase = nodo.tarjeta.getCategoria().equals("Frases icónicas");
        String resultado = (esHoja && esFrase) ? nodo.tarjeta.getDescripcion() : "";

        String izq = frasesIconicasHojasRec(nodo.hijoIzquierdo);
        String der = frasesIconicasHojasRec(nodo.hijoDerecho);
        if (!izq.isEmpty()) resultado = resultado.isEmpty() ? izq : resultado + "\n" + izq;
        if (!der.isEmpty()) resultado = resultado.isEmpty() ? der : resultado + "\n" + der;
        return resultado;
    }

    /**
     * Retorna un arreglo [tarjetaMenorId, tarjetaMayorId] obtenido por recorrido.
     * Retorna null si el árbol está vacío.
     */
    public Tarjeta[] minimoYMaximo() {
        if (raiz == null) {
            return null;
        }
        return new Tarjeta[]{buscarMinimoRec(raiz), buscarMaximoRec(raiz)};
    }

    // El mínimo siempre es el nodo más a la izquierda
    private Tarjeta buscarMinimoRec(Nodo nodo) {
        if (nodo.hijoIzquierdo == null) {
            return nodo.tarjeta;
        }
        return buscarMinimoRec(nodo.hijoIzquierdo);
    }

    // El máximo siempre es el nodo más a la derecha
    private Tarjeta buscarMaximoRec(Nodo nodo) {
        if (nodo.hijoDerecho == null) {
            return nodo.tarjeta;
        }
        return buscarMaximoRec(nodo.hijoDerecho);
    }

    /**
     * Retorna la raíz del árbol. Usado por PanelArbol para graficar.
     */
    public Nodo getRaiz() {
        return raiz;
    }
}
