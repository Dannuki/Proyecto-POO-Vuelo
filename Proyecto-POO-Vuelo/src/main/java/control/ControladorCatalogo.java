/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import vista.Catalogo;

/**
 *
 * @author USER
 */
public class ControladorCatalogo {
    private Catalogo vista;

    public ControladorCatalogo(Catalogo vista) {
        this.vista = vista;
        this.cargarDatosDesdeArchivo();
        this.inicializarModulo();
    }

    private void inicializarModulo() {
        // Esto hace que al escribir o cambiar un combo, la tabla se filtre sola
        KeyAdapter filtroTeclado = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                aplicarFiltros();
            }
        };

        vista.getTxtCodigo().addKeyListener(filtroTeclado);
        vista.getTxtFiltroPrecio().addKeyListener(filtroTeclado);

        // Filtros para Combos
        vista.getCbxOrigen().addActionListener(e -> aplicarFiltros());
        vista.getCbxDestino().addActionListener(e -> aplicarFiltros());
        vista.getCbxTipoAvion().addActionListener(e -> aplicarFiltros());

        // 2. BOTÓN LIMPIAR
        vista.getBtnLimpiar().addActionListener(e -> {
            int respuesta = JOptionPane.showConfirmDialog(vista,
                    "¿Desea limpiar todos los filtros?", "Confirmar",
                        JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                vista.getTxtCodigo().setText("");
                vista.getTxtFiltroPrecio().setText("");
                vista.getCbxOrigen().setSelectedIndex(0);
                vista.getCbxDestino().setSelectedIndex(0);
                vista.getCbxTipoAvion().setSelectedIndex(0);
                vista.getDatesSalida().clear();
                vista.getDateRegreso().clear();
                vista.getTmPickSalida().clear();
                aplicarFiltros(); // Para mostrar todo de nuevo
            }
        });

        // 3. BOTÓN CARGAR DATOS
        vista.getBtnCargarDatos().addActionListener(e -> {
            // 1. Alerta de confirmación antes de procesar
            int confirmacion = JOptionPane.showConfirmDialog(vista,
                    "¿Desea cargar los datos desde el archivo y actualizar la lista?",
                    "Confirmar Carga", JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                // 2. Validación de seguridad: Todos los espacios deben estar llenos
                if (validarEspaciosCompletos()) {
                    cargarDatosDesdeArchivo(); // Ejecuta la lectura del archivo
                    JOptionPane.showMessageDialog(vista, "Datos cargados exitosamente.");
                } else {
                    // Alerta si faltan datos en los filtros/campos
                    JOptionPane.showMessageDialog(vista,
                            "Para realizar esta acción, debe llenar todos los espacios del formulario.",
                            "Datos Faltantes", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        

        // 4. BOTÓN ELIMINAR VUELO
        vista.getBtnEliminarVuelo().addActionListener(e -> {
            int fila = vista.getTblGestionVuelos().getSelectedRow();

            // 1. Alerta si no hay selección
            if (fila == -1) {
                JOptionPane.showMessageDialog(vista,
                        "No ha seleccionado ningún vuelo de la tabla.", "Aviso",
                            JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2. Alerta de seguridad si seleccionó algo
            int confirmar = JOptionPane.showConfirmDialog(vista,
                    "¿Está seguro de eliminar este vuelo?", "Confirmar",
                        JOptionPane.YES_NO_OPTION);
            if (confirmar == JOptionPane.YES_OPTION) {
                DefaultTableModel modelo = (DefaultTableModel)
                        vista.getTblGestionVuelos().getModel();
                modelo.removeRow(fila);

                // Mensaje en el resumen
                // vista.getLblResumen().setText("Vuelo eliminado correctamente.");
                JOptionPane.showMessageDialog(vista,
                        "El vuelo ha sido retirado del catálogo.");
            }
        });
    }
    
    private void aplicarFiltros() {
        DefaultTableModel modelo = (DefaultTableModel) vista.getTblGestionVuelos().getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        vista.getTblGestionVuelos().setRowSorter(sorter);

        List<RowFilter<Object, Object>> filtros = new ArrayList<>();

        // 1. Código (Columna 0)
        if (!vista.getTxtCodigo().getText().trim().isEmpty()) {
            filtros.add(RowFilter.regexFilter("(?i)" + vista.getTxtCodigo().getText().trim(), 0));
        }

        // 2. Origen (Col 2) y Destino (Col 3)
        if (vista.getCbxOrigen().getSelectedIndex() > 0) {
            filtros.add(RowFilter.regexFilter(vista.getCbxOrigen().getSelectedItem().toString(), 2));
        }
        if (vista.getCbxDestino().getSelectedIndex() > 0) {
            filtros.add(RowFilter.regexFilter(vista.getCbxDestino().getSelectedItem().toString(), 3));
        }

        // 3. Avión (Columna 1)
        if (vista.getCbxTipoAvion().getSelectedIndex() > 0) {
            filtros.add(RowFilter.regexFilter(vista.getCbxTipoAvion().getSelectedItem().toString(), 1));
        }

        /// --- FILTRO DE PRESUPUESTO (Columna 8) ---
    String precioTexto = vista.getTxtFiltroPrecio().getText().trim();
        if (!precioTexto.isEmpty()) {
            try {
                double limite = Double.parseDouble(precioTexto);
                // Comparamos numéricamente: Menor o igual a (limite + 0.01)
                filtros.add(RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE, limite + 0.01, 8));
            } catch (NumberFormatException e) {
            }
        }

        // --- FILTRO DE FECHA (Columna 4) ---
        // Usamos el formato ISO que usa tu tabla (AAAA-MM-DD)
        if (vista.getDatesSalida().getDate() != null) {
            String fechaBusqueda = vista.getDatesSalida().getDate().toString();
            filtros.add(RowFilter.regexFilter(fechaBusqueda, 4));
        }

        // --- FILTRO DE HORA (Columna 5) ---
        if (vista.getTmPickSalida().getTime() != null) {
            String horaBusqueda = vista.getTmPickSalida().getTime().toString();
            filtros.add(RowFilter.regexFilter(horaBusqueda, 5));
        }

        // Aplicar todos los filtros juntos
        if (!filtros.isEmpty()) {
            sorter.setRowFilter(RowFilter.andFilter(filtros));
        }
    }

    private void cargarDatosDesdeArchivo() {
        DefaultTableModel modelo = (DefaultTableModel) vista.getTblGestionVuelos().getModel();
        modelo.setRowCount(0);
        try (BufferedReader br = new BufferedReader(new FileReader("vuelos.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length >= 9) {
                    modelo.addRow(new Object[]{
                        datos[0], datos[1], datos[2], datos[3], datos[4], 
                        datos[5], datos[6], datos[7], datos[8]
                    });
                }
            }
        } catch (Exception e) {
            System.out.println("Error al cargar: " + e.getMessage());
        }
    }
    
    private boolean validarEspaciosCompletos() {
        return !vista.getTxtCodigo().getText().trim().isEmpty()
                && !vista.getTxtFiltroPrecio().getText().trim().isEmpty()
                && vista.getDatesSalida().getDate() != null
                && vista.getTmPickSalida().getTime() != null
                && vista.getCbxOrigen().getSelectedIndex() > 0
                && vista.getCbxDestino().getSelectedIndex() > 0;
    }
}