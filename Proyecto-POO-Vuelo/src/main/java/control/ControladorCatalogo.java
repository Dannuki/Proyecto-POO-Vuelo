/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

import static com.sun.java.accessibility.util.AWTEventMonitor.addActionListener;
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
    private TableRowSorter<DefaultTableModel> sorter;

    public ControladorCatalogo(Catalogo vista) {
        this.vista = vista;
        this.inicializarModulo();
        this.cargarDatosDesdeArchivo();
    }

    private void inicializarModulo() {
        // Configuramos el sorter desde el inicio
        DefaultTableModel modelo = (DefaultTableModel) vista.getTblGestionVuelos().getModel();
        this.sorter = new TableRowSorter<>(modelo);
        vista.getTblGestionVuelos().setRowSorter(sorter);

        // 1. FILTROS EN TIEMPO REAL (Teclado)
        KeyAdapter filtroTeclado = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                aplicarFiltros();
            }
        };

        vista.getTxtCodigo().addKeyListener(filtroTeclado);
        vista.getTxtFiltroPrecio().addKeyListener(filtroTeclado);

        // 2. FILTROS POR COMBOS Y FECHAS
        vista.getCbxOrigen().addActionListener(e -> aplicarFiltros());
        vista.getCbxDestino().addActionListener(e -> aplicarFiltros());
        vista.getCbxTipoAvion().addActionListener(e -> aplicarFiltros());
        
        // Listeners para componentes de fecha y hora (LGoodDatePicker)
        vista.getDatesSalida().addDateChangeListener(e -> aplicarFiltros());
        vista.getTmPickSalida().addTimeChangeListener(e -> aplicarFiltros());

        // 3. BOTÓN LIMPIAR
        vista.getBtnLimpiar().addActionListener(e -> {
            int respuesta = JOptionPane.showConfirmDialog(vista, "¿Desea limpiar todos los filtros?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                limpiarTodo();
            }
        });

        // 4. BOTÓN CARGAR DATOS
        vista.getBtnCargarDatos().addActionListener(e -> {
            int confirmacion = JOptionPane.showConfirmDialog(vista, "¿Desea cargar los datos?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                String faltantes = verificarCamposFaltantes();
                if (faltantes.isEmpty()) {
                    cargarDatosDesdeArchivo();
                    // LLAMADA VITAL: Vuelve a aplicar los filtros sobre los datos nuevos
                    aplicarFiltros();
                    JOptionPane.showMessageDialog(vista, "Datos cargados y filtrados exitosamente.");
                } else {
                    JOptionPane.showMessageDialog(vista, "Faltan campos:\n" + faltantes, "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // 5. BOTÓN ELIMINAR VUELO
        vista.getBtnEliminarVuelo().addActionListener(e -> {
            int fila = vista.getTblGestionVuelos().getSelectedRow();

            if (fila == -1) {
                JOptionPane.showMessageDialog(vista, "Seleccione un vuelo para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirmar = JOptionPane.showConfirmDialog(vista, "¿Está seguro de eliminar el vuelo seleccionado?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmar == JOptionPane.YES_OPTION) {
                DefaultTableModel modeloTabla = (DefaultTableModel) vista.getTblGestionVuelos().getModel();
                // Convertimos el índice de la vista al índice del modelo por si hay filtros activos
                int modelRow = vista.getTblGestionVuelos().convertRowIndexToModel(fila);
                modeloTabla.removeRow(modelRow);

                JOptionPane.showMessageDialog(vista, "El vuelo ha sido eliminado correctamente.");
            }
        });
    } // AQUÍ CIERRA inicializarModulo

    // Método auxiliar para saber exactamente qué falta
    private String verificarCamposFaltantes() {
        StringBuilder sb = new StringBuilder();
        if (vista.getTxtCodigo().getText().trim().isEmpty()) {
            sb.append("- Código\n");
        }
        if (vista.getTxtFiltroPrecio().getText().trim().isEmpty()) {
            sb.append("- Presupuesto\n");
        }
        if (vista.getDatesSalida().getDate() == null) {
            sb.append("- Fecha de salida\n");
        }
        if (vista.getTmPickSalida().getTime() == null) {
            sb.append("- Hora de salida\n");
        }
        if (vista.getCbxOrigen().getSelectedIndex() <= 0) {
            sb.append("- Origen\n");
        }
        if (vista.getCbxDestino().getSelectedIndex() <= 0) {
            sb.append("- Destino\n");
        }
        return sb.toString();
    }

    private void aplicarFiltros() {
        List<RowFilter<Object, Object>> filtros = new ArrayList<>();

        /// 1. Código (Columna 0)
        if (!vista.getTxtCodigo().getText().trim().isEmpty()) {
                filtros.add(RowFilter.regexFilter("(?i)" + vista.getTxtCodigo().getText().trim(), 0));
        }

        // Origen (Col 2), Destino (Col 3), Avión (Col 1)
        agregarFiltroCombo(vista.getCbxOrigen(), 2, filtros);
        agregarFiltroCombo(vista.getCbxDestino(), 3, filtros);
        agregarFiltroCombo(vista.getCbxTipoAvion(), 1, filtros);
        
        
        // --- FILTRO DE PRESUPUESTO NUMÉRICO (Columna 8) ---
        String precioTexto = vista.getTxtFiltroPrecio().getText().trim();
        if (!precioTexto.isEmpty()) {
            try {
                double limite = Double.parseDouble(precioTexto);
                // Usamos BEFORE para incluir el valor exacto (limite + margen)
                filtros.add(RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE, limite + 0.01, 8));
            } catch (NumberFormatException e) { }
        }

        // FILTRO DE FECHA (Columna 4)
        if (vista.getDatesSalida().getDate() != null) {
            String fecha = vista.getDatesSalida().getDateStringOrEmptyString();
            filtros.add(RowFilter.regexFilter(fecha, 4));
        }

        // FILTRO DE HORA EXACTA (Columna 5)
        if (vista.getTmPickSalida().getTime() != null) {
            String hora = vista.getTmPickSalida().getTimeStringOrEmptyString();
            filtros.add(RowFilter.regexFilter(hora, 5));
        }
        // FILTRO DE HORA LLEGADA (Columna 6)
        if (vista.getTmPickLlegada().getTime() != null) { // Asegúrate que el getter sea getTmPickLlegada
            String horaLlegada = vista.getTmPickLlegada().getTimeStringOrEmptyString();
            filtros.add(RowFilter.regexFilter(horaLlegada, 6));
        }

        // FILTRO DE FECHA REGRESO (Columna 7)
        if(filtros.isEmpty()) {
            sorter.setRowFilter(null); // Muestra todo si no hay filtros
        }else {
        sorter.setRowFilter(RowFilter.andFilter(filtros)); // Aplica todos los filtros juntos
    }
    }

    private void agregarFiltroCombo(javax.swing.JComboBox combo, int columna, List<RowFilter<Object, Object>> lista) {
        if (combo.getSelectedIndex() > 0) {
            // Usamos (?i) para que no importe si dice "Airbus" o "airbus"
            lista.add(RowFilter.regexFilter("(?i)" + combo.getSelectedItem().toString(), columna));
        }
    }

    public void cargarDatosDesdeArchivo() {
        DefaultTableModel modelo = (DefaultTableModel) vista.getTblGestionVuelos().getModel();
        modelo.setRowCount(0);
        try (BufferedReader br = new BufferedReader(new FileReader("vuelos.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length >= 9) {
                    modelo.addRow(new Object[]{
                        datos[0], datos[1], datos[2], datos[3], datos[4], 
                        datos[5], datos[6], datos[7], 
                        Double.parseDouble(datos[8].trim())// VITAL: Cargar como Double para el filtro
                    });
                }
            }
        } catch (Exception e) {
            System.out.println("Error al cargar: " + e.getMessage());
        }
    }

    private void limpiarTodo() {
        // Limpieza de campos de texto
        vista.getTxtCodigo().setText("");
        vista.getTxtFiltroPrecio().setText("");

        // Limpieza de ComboBoxes
        vista.getCbxOrigen().setSelectedIndex(0);
        vista.getCbxDestino().setSelectedIndex(0);
        vista.getCbxTipoAvion().setSelectedIndex(0);

        // Limpieza de Fechas (DatePicker)
        vista.getDatesSalida().clear();
        vista.getDateRegreso().clear(); // Asegúrate de que este sea el nombre en tu Vista

        // Limpieza de Horas (TimePicker)
        vista.getTmPickSalida().clear();
        vista.getTmPickLlegada().clear(); // Asegúrate de que este sea el nombre en tu Vista

        // Resetear el resumen visual
        // vista.getLblResumen().setText("Seleccione un vuelo para ver el resumen.");
        // Actualizar la tabla para mostrar todos los datos de nuevo
        aplicarFiltros();
    }
}