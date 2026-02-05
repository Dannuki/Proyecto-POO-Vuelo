/*
 * Controlador actualizado del Catálogo que inicia el flujo de compra
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
import modelo.Vuelo;
import modelo.Ruta;
import modelo.Avion;

public class ControladorCatalogoMejorado {
    private Catalogo vista;
    private TableRowSorter<DefaultTableModel> sorter;
    private ControladorFlujoCompra flujoCompra;

    public ControladorCatalogoMejorado(Catalogo vista) {
        this.vista = vista;
        this.flujoCompra = new ControladorFlujoCompra();
        this.inicializarModulo();
        this.cargarDatosDesdeArchivo();
    }

    // Método auxiliar para validar que solo ingresen enteros
private int pedirNumero(String mensaje) {
    while (true) {
        String input = JOptionPane.showInputDialog(vista, mensaje);
        if (input == null) return -1; // Botón Cancelar
        if (input.trim().isEmpty()) return 0; // Si lo deja vacío cuenta como 0
        try {
            int valor = Integer.parseInt(input);
            if (valor < 0) {
                JOptionPane.showMessageDialog(vista, "No puede ingresar números negativos.");
            } else {
                return valor; // ¡Es válido!
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "Error: Ingrese solo números enteros (sin decimales ni letras).");
        }
    }
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
            int confirmacion = JOptionPane.showConfirmDialog(vista,
                    "¿Desea cargar los datos desde el archivo y actualizar la lista?",
                    "Confirmar Carga", JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                String faltantes = verificarCamposFaltantes();

                if (faltantes.isEmpty()) {
                    cargarDatosDesdeArchivo();
                    limpiarTodo();
                    JOptionPane.showMessageDialog(vista, "Datos cargados exitosamente.");
                } else {
                    JOptionPane.showMessageDialog(vista,
                            "Faltan los siguientes campos para proceder:\n" + faltantes,
                            "Datos Incompletos", JOptionPane.WARNING_MESSAGE);
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
                int modelRow = vista.getTblGestionVuelos().convertRowIndexToModel(fila);
                modeloTabla.removeRow(modelRow);

                JOptionPane.showMessageDialog(vista, "El vuelo ha sido eliminado correctamente.");
            }
        });
        
        // 6. BOTÓN NUEVO VUELO
        vista.getBtnNuevoVuelo().addActionListener(e -> {
            int respuesta = JOptionPane.showConfirmDialog(vista,
                    "¿Está seguro de iniciar un nuevo proceso? Se borrarán todos los datos actuales.",
                    "Confirmar Nuevo Vuelo", JOptionPane.YES_NO_OPTION);

            if (respuesta == JOptionPane.YES_OPTION) {
                limpiarTodo();
                vista.getTxtCodigo().requestFocus();
            }
        });
    
    
        
    // BOTÓN CONTINUAR (CON VALIDACIÓN DE ENTEROS)
vista.getBtnContinuar().addActionListener(evt -> {
    int fila = vista.getTblGestionVuelos().getSelectedRow();
    
    if (fila != -1) {
        // 1. Recuperar datos del vuelo
        int modelRow = vista.getTblGestionVuelos().convertRowIndexToModel(fila);
        javax.swing.table.DefaultTableModel modeloTabla = (javax.swing.table.DefaultTableModel) vista.getTblGestionVuelos().getModel();
        
        String codigo = modeloTabla.getValueAt(modelRow, 0).toString();
        String avion = modeloTabla.getValueAt(modelRow, 1).toString();
        String origen = modeloTabla.getValueAt(modelRow, 2).toString();
        String destino = modeloTabla.getValueAt(modelRow, 3).toString();
        String fecha = modeloTabla.getValueAt(modelRow, 4).toString();
        String horaS = modeloTabla.getValueAt(modelRow, 5).toString();
        String horaL = modeloTabla.getValueAt(modelRow, 6).toString();
        double precio = Double.parseDouble(modeloTabla.getValueAt(modelRow, 8).toString());

        modelo.Ruta ruta = new modelo.Ruta(origen, destino);
        modelo.Avion avionObj = new modelo.Avion(avion);
        modelo.Vuelo v = new modelo.Vuelo(codigo, ruta, avionObj, fecha, "", horaS, horaL, precio);
        SesionGlobal.getInstancia().setVueloSeleccionado(v);

        // 2. PEDIR PASAJEROS (CON VALIDACIÓN)
        int adultos = pedirNumero("¿Cuántos ADULTOS viajan?");
        if (adultos == -1) return; // Si canceló, salimos
        
        if (adultos > 0) {
            int ninos = pedirNumero("¿Cuántos NIÑOS viajan? (2-11 años)");
            if (ninos == -1) return;
            
            int bebes = pedirNumero("¿Cuántos BEBÉS viajan? (0-23 meses)");
            if (bebes == -1) return;

            // Guardar
            SesionGlobal.getInstancia().setCantAdultos(adultos);
            SesionGlobal.getInstancia().setCantNinos(ninos);
            SesionGlobal.getInstancia().setCantBebes(bebes);
            
            // Preguntar Equipaje
            int resp = JOptionPane.showConfirmDialog(vista, "¿Desea equipaje de bodega (23kg) por $29?", "Equipaje", JOptionPane.YES_NO_OPTION);
            if (resp == JOptionPane.YES_OPTION) {
                // Asumimos 1 maleta por adulto+niño
                SesionGlobal.getInstancia().setCostoTotalEquipaje(29.0 * (adultos + ninos));
            } else {
                SesionGlobal.getInstancia().setCostoTotalEquipaje(0.0);
            }

            // Cambio de ventana
            vista.dispose();
            new ControladorRegistro(adultos, ninos, bebes).iniciar(); 
        } else {
             JOptionPane.showMessageDialog(vista, "Debe viajar al menos 1 adulto.");
        }
    } else {
        JOptionPane.showMessageDialog(vista, "Seleccione un vuelo.");
    }
});
        
    } 

    /**
     * NUEVO MÉTODO: Inicia el proceso de compra con el vuelo seleccionado
     */
    private void iniciarProcesoCompra(int filaSeleccionada) {
        try {
            // Convertir índice de vista a modelo (por si hay filtros)
            int modelRow = vista.getTblGestionVuelos().convertRowIndexToModel(filaSeleccionada);
            DefaultTableModel modelo = (DefaultTableModel) vista.getTblGestionVuelos().getModel();
            
            // Extraer datos del vuelo seleccionado
            String codigo = modelo.getValueAt(modelRow, 0).toString();
            String avionNombre = modelo.getValueAt(modelRow, 1).toString();
            String origen = modelo.getValueAt(modelRow, 2).toString();
            String destino = modelo.getValueAt(modelRow, 3).toString();
            String fechaSalida = modelo.getValueAt(modelRow, 4).toString();
            String horaSalida = modelo.getValueAt(modelRow, 5).toString();
            String horaLlegada = modelo.getValueAt(modelRow, 6).toString();
            String fechaRegreso = modelo.getValueAt(modelRow, 7).toString();
            double precio = Double.parseDouble(modelo.getValueAt(modelRow, 8).toString());
            
            // Crear objetos del modelo
            Ruta ruta = new Ruta(origen, destino);
            Avion avion = new Avion(avionNombre);
            Vuelo vueloSeleccionado = new Vuelo(codigo, ruta, avion, fechaSalida, 
                                                fechaRegreso, horaSalida, horaLlegada, precio);
            
            // Iniciar el flujo de compra
            flujoCompra.seleccionarVuelo(vueloSeleccionado);
            
            // Mostrar resumen del vuelo
            String resumen = String.format(
                "Vuelo seleccionado:\n" +
                "Código: %s\n" +
                "Ruta: %s → %s\n" +
                "Fecha: %s - Hora: %s\n" +
                "Precio base: $%.2f",
                codigo, origen, destino, fechaSalida, horaSalida, precio
            );
            
            JOptionPane.showMessageDialog(vista, resumen, 
                "Vuelo Seleccionado", JOptionPane.INFORMATION_MESSAGE);
            
            // Aquí deberías abrir la siguiente ventana del flujo
            // Por ejemplo: abrirVentanaSeleccionPasajeros();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, 
                "Error al procesar el vuelo: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

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

        // 1. Código (Columna 0)
        if (!vista.getTxtCodigo().getText().trim().isEmpty()) {
                filtros.add(RowFilter.regexFilter("(?i)" + vista.getTxtCodigo().getText().trim(), 0));
        }

        // Origen (Col 2), Destino (Col 3), Avión (Col 1)
        agregarFiltroCombo(vista.getCbxOrigen(), 2, filtros);
        agregarFiltroCombo(vista.getCbxDestino(), 3, filtros);
        agregarFiltroCombo(vista.getCbxTipoAvion(), 1, filtros);
        
        // FILTRO DE PRESUPUESTO NUMÉRICO (Columna 8)
        String precioTexto = vista.getTxtFiltroPrecio().getText().trim();
        if (!precioTexto.isEmpty()) {
            try {
                double limite = Double.parseDouble(precioTexto);
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
        if (vista.getTmPickLlegada().getTime() != null) {
            String horaLlegada = vista.getTmPickLlegada().getTimeStringOrEmptyString();
            filtros.add(RowFilter.regexFilter(horaLlegada, 6));
        }

        if(filtros.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.andFilter(filtros));
        }
    }

    private void agregarFiltroCombo(javax.swing.JComboBox combo, int columna, List<RowFilter<Object, Object>> lista) {
        if (combo.getSelectedIndex() > 0) {
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
                        Double.parseDouble(datos[8].trim())
                    });
                }
            }
        } catch (Exception e) {
            System.out.println("Error al cargar: " + e.getMessage());
        }
    }

    private void limpiarTodo() {
        vista.getTxtCodigo().setText("");
        vista.getTxtFiltroPrecio().setText("");
        vista.getCbxOrigen().setSelectedIndex(0);
        vista.getCbxDestino().setSelectedIndex(0);
        vista.getCbxTipoAvion().setSelectedIndex(0);
        vista.getDatesSalida().clear();
        vista.getDateRegreso().clear();
        vista.getTmPickSalida().clear();
        vista.getTmPickLlegada().clear();
        vista.getLblResumen().setText("Seleccione un vuelo para ver el resumen.");
        aplicarFiltros();
    }
    
    public ControladorFlujoCompra getFlujoCompra() {
        return flujoCompra;
    }
}
