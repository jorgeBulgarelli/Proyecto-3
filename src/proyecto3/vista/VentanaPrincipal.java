package proyecto3.vista;

import proyecto3.modelo.ArbolBinarioBusqueda;
import proyecto3.modelo.Tarjeta;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Ventana principal de la aplicación.
 * Contiene todas las funcionalidades del árbol BST en una única interfaz con pestañas.
 * No utiliza ventanas independientes para ningún proceso.
 */
public class VentanaPrincipal extends JFrame {

    private ArbolBinarioBusqueda arbol;
    private PanelArbol panelArbol;

    // Categorías definidas en el enunciado
    private static final String[] CATEGORIAS = {
        "Civiles",
        "Equipos",
        "Súper héroes",
        "Objetos",
        "Súper villanos",
        "Paneles",
        "Cara a cara",
        "Frases icónicas",
        "Película especial"
    };

    public VentanaPrincipal() {
        arbol      = new ArbolBinarioBusqueda();
        panelArbol = new PanelArbol();
        configurarVentana();
    }

    private void configurarVentana() {
        setTitle("Gestión de Tarjetas DC Comics — Árbol Binario de Búsqueda");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setResizable(true);

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Inserción",      crearPanelInsercion());
        pestanas.addTab("Eliminación",    crearPanelEliminacion());
        pestanas.addTab("Búsqueda",       crearPanelBusqueda());
        pestanas.addTab("Recorridos",     crearPanelRecorridos());
        pestanas.addTab("Consultas",      crearPanelConsultas());
        pestanas.addTab("Graficar Árbol", crearPanelGraficar());

        add(pestanas, BorderLayout.CENTER);
    }

    // -----------------------------------------------------------------------
    // TAB 1 — Inserción
    // -----------------------------------------------------------------------
    private JPanel crearPanelInsercion() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Insertar nueva tarjeta DC Comics", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        panel.add(titulo, BorderLayout.NORTH);

        // Formulario de entrada con GridBagLayout para alineación ordenada
        JPanel formulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(8, 8, 8, 8);
        gbc.anchor  = GridBagConstraints.WEST;
        gbc.fill    = GridBagConstraints.HORIZONTAL;

        JTextField txtId          = new JTextField(10);
        JTextField txtDescripcion = new JTextField(30);
        JComboBox<String> cmbCategoria = new JComboBox<>(CATEGORIAS);

        gbc.gridx = 0; gbc.gridy = 0; gbc.fill = GridBagConstraints.NONE;
        formulario.add(new JLabel("ID de la tarjeta:"), gbc);
        gbc.gridx = 1;
        formulario.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formulario.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formulario.add(txtDescripcion, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        formulario.add(new JLabel("Categoría:"), gbc);
        gbc.gridx = 1;
        formulario.add(cmbCategoria, gbc);

        JButton btnInsertar = new JButton("Insertar Tarjeta");
        btnInsertar.setFont(new Font("SansSerif", Font.BOLD, 13));
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        formulario.add(btnInsertar, gbc);

        panel.add(formulario, BorderLayout.CENTER);

        // Etiqueta de estado — muestra resultado sin JOptionPane
        JLabel lblEstado = new JLabel(" ", SwingConstants.CENTER);
        lblEstado.setFont(new Font("SansSerif", Font.PLAIN, 13));
        panel.add(lblEstado, BorderLayout.SOUTH);

        btnInsertar.addActionListener((ActionEvent e) -> {
            String idTexto     = txtId.getText().trim();
            String descripcion = txtDescripcion.getText().trim();

            if (idTexto.isEmpty()) {
                mostrarError(lblEstado, "Error: el campo ID no puede estar vacío.");
                return;
            }

            int id;
            try {
                id = Integer.parseInt(idTexto);
            } catch (NumberFormatException ex) {
                mostrarError(lblEstado, "Error: el ID debe ser un número entero positivo.");
                return;
            }

            if (id <= 0) {
                mostrarError(lblEstado, "Error: el ID debe ser un número entero positivo.");
                return;
            }

            if (descripcion.isEmpty()) {
                mostrarError(lblEstado, "Error: el campo Descripción no puede estar vacío.");
                return;
            }

            String categoria = (String) cmbCategoria.getSelectedItem();
            String errorBST  = arbol.insertar(id, descripcion, categoria);

            if (errorBST != null) {
                mostrarError(lblEstado, errorBST);
            } else {
                mostrarExito(lblEstado, "Tarjeta con ID " + id + " insertada exitosamente.");
                txtId.setText("");
                txtDescripcion.setText("");
                cmbCategoria.setSelectedIndex(0);
                actualizarGrafica();
            }
        });

        return panel;
    }

    // -----------------------------------------------------------------------
    // TAB 2 — Eliminación
    // -----------------------------------------------------------------------
    private JPanel crearPanelEliminacion() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Eliminar tarjeta del árbol", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        panel.add(titulo, BorderLayout.NORTH);

        JPanel entrada = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        entrada.add(new JLabel("ID a eliminar:"));
        JTextField txtId = new JTextField(10);
        entrada.add(txtId);
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setFont(new Font("SansSerif", Font.BOLD, 13));
        entrada.add(btnEliminar);
        panel.add(entrada, BorderLayout.CENTER);

        // Área de reglas de eliminación como referencia visual
        JTextArea areaReglas = new JTextArea(
            "Reglas de eliminación:\n" +
            "1. Nodo hoja → se elimina.\n" +
            "2. Solo subárbol derecho → NO se elimina.\n" +
            "3. Solo subárbol izquierdo → se elimina; su subárbol ocupa su lugar.\n" +
            "4. Dos subárboles → NO se elimina.\n" +
            "5. Categoría 'Civiles' → NUNCA se elimina (sin excepción)."
        );
        areaReglas.setEditable(false);
        areaReglas.setFont(new Font("SansSerif", Font.PLAIN, 11));
        areaReglas.setBackground(new Color(255, 255, 220));
        areaReglas.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        areaReglas.setMargin(new Insets(6, 6, 6, 6));

        JLabel lblEstado = new JLabel(" ", SwingConstants.CENTER);
        lblEstado.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JPanel sur = new JPanel(new BorderLayout(5, 8));
        sur.add(areaReglas, BorderLayout.CENTER);
        sur.add(lblEstado,  BorderLayout.SOUTH);
        panel.add(sur, BorderLayout.SOUTH);

        btnEliminar.addActionListener((ActionEvent e) -> {
            String idTexto = txtId.getText().trim();
            if (idTexto.isEmpty()) {
                mostrarError(lblEstado, "Error: el campo ID no puede estar vacío.");
                return;
            }
            int id;
            try {
                id = Integer.parseInt(idTexto);
            } catch (NumberFormatException ex) {
                mostrarError(lblEstado, "Error: el ID debe ser un número entero positivo.");
                return;
            }
            if (id <= 0) {
                mostrarError(lblEstado, "Error: el ID debe ser un número entero positivo.");
                return;
            }

            String resultado = arbol.eliminar(id);
            // Los mensajes de éxito comienzan con "Tarjeta con ID"
            if (resultado.startsWith("Tarjeta con ID")) {
                mostrarExito(lblEstado, resultado);
            } else {
                mostrarError(lblEstado, resultado);
            }
            actualizarGrafica();
        });

        return panel;
    }

    // -----------------------------------------------------------------------
    // TAB 3 — Búsqueda
    // -----------------------------------------------------------------------
    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Búsqueda de tarjeta por ID", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        panel.add(titulo, BorderLayout.NORTH);

        JPanel entrada = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        entrada.add(new JLabel("ID a buscar:"));
        JTextField txtId = new JTextField(10);
        entrada.add(txtId);
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setFont(new Font("SansSerif", Font.BOLD, 13));
        entrada.add(btnBuscar);

        JTextArea areaResultado = crearAreaResultado(6, 40);
        JScrollPane scroll      = new JScrollPane(areaResultado);

        JPanel centro = new JPanel(new BorderLayout(5, 10));
        centro.add(entrada, BorderLayout.NORTH);
        centro.add(scroll,  BorderLayout.CENTER);
        panel.add(centro, BorderLayout.CENTER);

        btnBuscar.addActionListener((ActionEvent e) -> {
            String idTexto = txtId.getText().trim();
            if (idTexto.isEmpty()) {
                areaResultado.setText("Error: el campo ID no puede estar vacío.");
                return;
            }
            int id;
            try {
                id = Integer.parseInt(idTexto);
            } catch (NumberFormatException ex) {
                areaResultado.setText("Error: el ID debe ser un número entero positivo.");
                return;
            }
            if (id <= 0) {
                areaResultado.setText("Error: el ID debe ser un número entero positivo.");
                return;
            }

            Tarjeta tarjeta = arbol.buscar(id);
            if (tarjeta == null) {
                areaResultado.setText("No se encontró ninguna tarjeta con ID " + id + ".");
            } else {
                areaResultado.setText(tarjeta.toString());
            }
        });

        return panel;
    }

    // -----------------------------------------------------------------------
    // TAB 4 — Recorridos
    // -----------------------------------------------------------------------
    private JPanel crearPanelRecorridos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Recorridos del árbol (IDs separados por guiones)", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        panel.add(titulo, BorderLayout.NORTH);

        JTextArea areaResultado   = crearAreaResultado(6, 50);
        JLabel    lblTipoRecorrido = new JLabel(" ", SwingConstants.CENTER);
        lblTipoRecorrido.setFont(new Font("SansSerif", Font.ITALIC, 12));
        JScrollPane scroll = new JScrollPane(areaResultado);

        JButton btnPreOrden  = new JButton("Pre-Orden");
        JButton btnInOrden   = new JButton("In-Orden");
        JButton btnPostOrden = new JButton("Post-Orden");

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        botones.add(btnPreOrden);
        botones.add(btnInOrden);
        botones.add(btnPostOrden);

        JPanel centro = new JPanel(new BorderLayout(5, 10));
        centro.add(botones,          BorderLayout.NORTH);
        centro.add(scroll,           BorderLayout.CENTER);
        centro.add(lblTipoRecorrido, BorderLayout.SOUTH);
        panel.add(centro, BorderLayout.CENTER);

        btnPreOrden.addActionListener((ActionEvent e) -> {
            String resultado = arbol.recorridoPreOrden();
            lblTipoRecorrido.setText("Pre-Orden: raíz → izquierda → derecha");
            areaResultado.setText(resultado.isEmpty() ? "El árbol está vacío." : resultado);
        });

        btnInOrden.addActionListener((ActionEvent e) -> {
            String resultado = arbol.recorridoInOrden();
            lblTipoRecorrido.setText("In-Orden: izquierda → raíz → derecha");
            areaResultado.setText(resultado.isEmpty() ? "El árbol está vacío." : resultado);
        });

        btnPostOrden.addActionListener((ActionEvent e) -> {
            String resultado = arbol.recorridoPostOrden();
            lblTipoRecorrido.setText("Post-Orden: izquierda → derecha → raíz");
            areaResultado.setText(resultado.isEmpty() ? "El árbol está vacío." : resultado);
        });

        return panel;
    }

    // -----------------------------------------------------------------------
    // TAB 5 — Consultas adicionales
    // -----------------------------------------------------------------------
    private JPanel crearPanelConsultas() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Consultas adicionales sobre el árbol", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        panel.add(titulo, BorderLayout.NORTH);

        JTextArea areaResultado = crearAreaResultado(10, 50);
        JScrollPane scroll      = new JScrollPane(areaResultado);

        JButton btnContarHeroes = new JButton("Cantidad de Súper héroes y Súper villanos");
        JButton btnFrasesHoja   = new JButton("Frases icónicas en nodos hoja");
        JButton btnMinMax       = new JButton("ID mínimo y máximo");

        JPanel botones = new JPanel(new GridLayout(3, 1, 5, 8));
        botones.add(btnContarHeroes);
        botones.add(btnFrasesHoja);
        botones.add(btnMinMax);

        JPanel centro = new JPanel(new BorderLayout(5, 10));
        centro.add(botones, BorderLayout.NORTH);
        centro.add(scroll,  BorderLayout.CENTER);
        panel.add(centro, BorderLayout.CENTER);

        btnContarHeroes.addActionListener((ActionEvent e) -> {
            int cantidad = arbol.contarHeroesVillanos();
            areaResultado.setText(
                "Total de tarjetas de categoría 'Súper héroes' o 'Súper villanos': " + cantidad
            );
        });

        btnFrasesHoja.addActionListener((ActionEvent e) -> {
            String resultado = arbol.frasesIconicasHojas();
            if (resultado.isEmpty()) {
                areaResultado.setText("No hay nodos de categoría 'Frases icónicas' que sean hojas.");
            } else {
                areaResultado.setText("Descripciones de 'Frases icónicas' en nodos hoja:\n\n" + resultado);
            }
        });

        btnMinMax.addActionListener((ActionEvent e) -> {
            Tarjeta[] resultado = arbol.minimoYMaximo();
            if (resultado == null) {
                areaResultado.setText("El árbol está vacío.");
            } else {
                areaResultado.setText(
                    "Tarjeta con el MENOR ID:\n" + resultado[0].toString() +
                    "\n\nTarjeta con el MAYOR ID:\n" + resultado[1].toString()
                );
            }
        });

        return panel;
    }

    // -----------------------------------------------------------------------
    // TAB 6 — Graficar árbol
    // -----------------------------------------------------------------------
    private JPanel crearPanelGraficar() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Representación gráfica del árbol", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        panel.add(titulo, BorderLayout.NORTH);

        panelArbol.setPreferredSize(new Dimension(2000, 1500));
        JScrollPane scroll = new JScrollPane(panelArbol);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(scroll, BorderLayout.CENTER);

        JButton btnActualizar = new JButton("Actualizar gráfica");
        btnActualizar.addActionListener((ActionEvent e) -> actualizarGrafica());
        JPanel sur = new JPanel(new FlowLayout(FlowLayout.CENTER));
        sur.add(btnActualizar);
        panel.add(sur, BorderLayout.SOUTH);

        return panel;
    }

    // -----------------------------------------------------------------------
    // Utilidades
    // -----------------------------------------------------------------------

    /** Actualiza la referencia al árbol en el panel gráfico y redibuja. */
    private void actualizarGrafica() {
        panelArbol.setRaiz(arbol.getRaiz());
        panelArbol.repaint();
    }

    /** Muestra un mensaje de error en rojo sobre la etiqueta indicada. */
    private void mostrarError(JLabel etiqueta, String mensaje) {
        etiqueta.setForeground(Color.RED);
        etiqueta.setText(mensaje);
    }

    /** Muestra un mensaje de éxito en verde sobre la etiqueta indicada. */
    private void mostrarExito(JLabel etiqueta, String mensaje) {
        etiqueta.setForeground(new Color(0, 128, 0));
        etiqueta.setText(mensaje);
    }

    /** Crea un JTextArea de solo lectura con el estilo estándar de resultados. */
    private JTextArea crearAreaResultado(int filas, int columnas) {
        JTextArea area = new JTextArea(filas, columnas);
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
        area.setBackground(new Color(245, 245, 245));
        area.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        area.setMargin(new Insets(6, 6, 6, 6));
        return area;
    }
}
