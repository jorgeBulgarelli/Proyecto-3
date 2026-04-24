package proyecto3.vista;

import proyecto3.modelo.Nodo;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 * Panel personalizado que dibuja el árbol binario de búsqueda.
 * Cada nodo se representa como un círculo amarillo con el Id en su interior.
 * Las aristas se representan como líneas negras entre nodos padre e hijo.
 */
public class PanelArbol extends JPanel {

    private static final int RADIO_NODO  = 20; // radio del círculo en píxeles
    private static final int ALTO_NIVEL  = 70; // distancia vertical entre niveles

    private Nodo raiz;

    public PanelArbol() {
        this.raiz = null;
        setBackground(Color.WHITE);
    }

    /** Actualiza la raíz que se dibujará. Llamar repaint() después. */
    public void setRaiz(Nodo raiz) {
        this.raiz = raiz;
    }

    @Override
    public Dimension getPreferredSize() {
        // Tamaño grande para habilitar scroll en árboles profundos
        return new Dimension(2000, 1500);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (raiz == null) {
            g.setColor(Color.GRAY);
            g.setFont(new Font("SansSerif", Font.ITALIC, 14));
            g.drawString("El árbol está vacío.", 20, 40);
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // La raíz se centra horizontalmente; cada nivel usa la mitad del espacio del padre
        dibujarNodo(g2d, raiz, getWidth() / 2, 50, getWidth() / 4);
    }

    /**
     * Dibuja un nodo y sus subárboles de forma recursiva.
     *
     * @param g      contexto gráfico
     * @param nodo   nodo a dibujar
     * @param x      posición horizontal del centro del nodo
     * @param y      posición vertical del centro del nodo
     * @param offset desplazamiento horizontal para los hijos
     */
    private void dibujarNodo(Graphics2D g, Nodo nodo, int x, int y, int offset) {
        if (nodo == null) return;

        // Dibujar arista y subárbol izquierdo
        if (nodo.getHijoIzquierdo() != null) {
            int xHijo = x - offset;
            int yHijo = y + ALTO_NIVEL;
            g.setColor(Color.BLACK);
            g.drawLine(x, y, xHijo, yHijo);
            dibujarNodo(g, nodo.getHijoIzquierdo(), xHijo, yHijo, offset / 2);
        }

        // Dibujar arista y subárbol derecho
        if (nodo.getHijoDerecho() != null) {
            int xHijo = x + offset;
            int yHijo = y + ALTO_NIVEL;
            g.setColor(Color.BLACK);
            g.drawLine(x, y, xHijo, yHijo);
            dibujarNodo(g, nodo.getHijoDerecho(), xHijo, yHijo, offset / 2);
        }

        // Dibujar círculo del nodo (relleno amarillo, borde negro)
        g.setColor(new Color(255, 215, 0));
        g.fillOval(x - RADIO_NODO, y - RADIO_NODO, RADIO_NODO * 2, RADIO_NODO * 2);
        g.setColor(Color.BLACK);
        g.drawOval(x - RADIO_NODO, y - RADIO_NODO, RADIO_NODO * 2, RADIO_NODO * 2);

        // Dibujar el Id centrado dentro del círculo
        String idStr  = String.valueOf(nodo.getTarjeta().getId());
        FontMetrics fm = g.getFontMetrics();
        int tx = x - fm.stringWidth(idStr) / 2;
        int ty = y + fm.getAscent() / 2 - 2;
        g.drawString(idStr, tx, ty);
    }
}
