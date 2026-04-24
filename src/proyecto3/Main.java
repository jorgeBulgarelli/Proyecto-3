package proyecto3;

import proyecto3.vista.VentanaPrincipal;
import javax.swing.SwingUtilities;

/**
 * Punto de entrada de la aplicación.
 * Inicia la interfaz gráfica en el hilo de despacho de eventos de Swing.
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}
