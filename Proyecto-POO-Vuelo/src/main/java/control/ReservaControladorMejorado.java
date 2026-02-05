package control;

import vista.ReservaFrame;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import modelo.*;
import servicios.GestorReservas;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador mejorado de reserva que maneja asientos, equipaje y pago
 */
public class ReservaControladorMejorado {

    private ReservaFrame vista;
    private ControladorFlujoCompra flujoCompra;
    private GestorReservas gestorReservas;
    private List<String> asientosSeleccionados;
    private List<Equipaje> equipajesSeleccionados;
    private Equipaje equipajeActual;

    public ReservaControladorMejorado(ReservaFrame vista, ControladorFlujoCompra flujoCompra) {
        this.vista = vista;
        this.flujoCompra = flujoCompra;
        this.gestorReservas = new GestorReservas();
        this.asientosSeleccionados = new ArrayList<>();
        this.equipajesSeleccionados = new ArrayList<>();
        
        inicializarControlador();
    }

    private void inicializarControlador() {
        // Equipaje - Ahora guarda la selección para cada pasajero
        vista.getBtnMochila().addActionListener(e -> {
            vista.seleccionarEquipaje(vista.getBtnMochila());
            equipajeActual = new Equipaje(Equipaje.TipoEquipaje.MOCHILA_MANO);
        });
        
        vista.getBtnCarryOn().addActionListener(e -> {
            vista.seleccionarEquipaje(vista.getBtnCarryOn());
            equipajeActual = new Equipaje(Equipaje.TipoEquipaje.CARRY_ON);
        });
        
        vista.getBtnBodega().addActionListener(e -> {
            vista.seleccionarEquipaje(vista.getBtnBodega());
            equipajeActual = new Equipaje(Equipaje.TipoEquipaje.BODEGA_23KG);
        });

        // Asientos - Al hacer clic se marca/desmarca
        for (int fila = 0; fila < 3; fila++) {
            for (int col = 0; col < 4; col++) {
                JButton asiento = vista.getBotonAsiento(fila, col);
                String numeroAsiento = generarNumeroAsiento(fila, col);
                
                asiento.addActionListener(e -> {
                    if (asiento.isEnabled()) {
                        // Si ya está seleccionado, lo deselecciona
                        if (asientosSeleccionados.contains(numeroAsiento)) {
                            asientosSeleccionados.remove(numeroAsiento);
                            flujoCompra.liberarAsiento(numeroAsiento);
                            asiento.setBackground(null); // Color original
                        } else {
                            // Verificar que no exceda el número de pasajeros
                            if (asientosSeleccionados.size() >= flujoCompra.getTotalPasajeros()) {
                                JOptionPane.showMessageDialog(vista,
                                    "Ya seleccionó todos los asientos necesarios (" + 
                                    flujoCompra.getTotalPasajeros() + " pasajeros)",
                                    "Límite alcanzado", JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                            
                            asientosSeleccionados.add(numeroAsiento);
                            flujoCompra.ocuparAsiento(numeroAsiento, "PASAJERO");
                            asiento.setBackground(java.awt.Color.GREEN);
                        }
                        vista.actualizarTotal();
                    }
                });
            }
        }

        // Botón pagar - Ahora procesa toda la reserva
        vista.getBtnPagar().addActionListener(e -> procesarPago());
    }
    
    private String generarNumeroAsiento(int fila, int col) {
        String[] columnas = {"A", "B", "C", "D"};
        return (fila + 1) + columnas[col];
    }

    private void procesarPago() {
        // Validar que se haya seleccionado equipaje para cada pasajero
        if (equipajesSeleccionados.size() < flujoCompra.getTotalPasajeros()) {
            JOptionPane.showMessageDialog(vista,
                "Debe seleccionar equipaje para todos los pasajeros.",
                "Equipaje incompleto", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validar que se hayan seleccionado todos los asientos
        if (asientosSeleccionados.size() < flujoCompra.getTotalPasajeros()) {
            JOptionPane.showMessageDialog(vista,
                "Debe seleccionar asientos para todos los pasajeros (" + 
                flujoCompra.getTotalPasajeros() + " requeridos).",
                "Asientos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Solicitar método de pago
        String[] opciones = {"Tarjeta de Crédito", "Tarjeta de Débito", "Efectivo", "Transferencia"};
        int seleccion = JOptionPane.showOptionDialog(vista,
            "Seleccione el método de pago:",
            "Método de Pago",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[0]);

        if (seleccion == -1) return; // Canceló
        
        String metodoPago = opciones[seleccion];

        try {
            // Crear la reserva con asientos y equipajes
            flujoCompra.crearReserva(asientosSeleccionados, equipajesSeleccionados);
            
            // Confirmar pago y generar boleto
            Boleto boleto = flujoCompra.confirmarPago(metodoPago);
            
            // Guardar en archivos
            String rutaBoleto = gestorReservas.guardarReservaYBoleto(
                flujoCompra.getReservaActual(), 
                boleto
            );
            
            // Mostrar boleto
            JOptionPane.showMessageDialog(vista,
                boleto.generarBoletoTexto(),
                "Boleto Emitido",
                JOptionPane.INFORMATION_MESSAGE);
            
            JOptionPane.showMessageDialog(vista,
                "¡Pago procesado exitosamente!\n" +
                "Código de reserva: " + boleto.getReserva().getCodigoReserva() + "\n" +
                "Boleto guardado en: " + rutaBoleto,
                "Compra Exitosa",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Cerrar ventana y reiniciar flujo
            vista.dispose();
            flujoCompra.reiniciarFlujo();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista,
                "Error al procesar el pago: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Método para agregar equipaje del pasajero actual
     */
    public void confirmarEquipajePasajero() {
        if (equipajeActual == null) {
            JOptionPane.showMessageDialog(vista,
                "Debe seleccionar un tipo de equipaje.",
                "Equipaje no seleccionado",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (equipajesSeleccionados.size() >= flujoCompra.getTotalPasajeros()) {
            JOptionPane.showMessageDialog(vista,
                "Ya seleccionó equipaje para todos los pasajeros.",
                "Límite alcanzado",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        equipajesSeleccionados.add(equipajeActual);
        JOptionPane.showMessageDialog(vista,
            "Equipaje registrado para el pasajero " + equipajesSeleccionados.size() + ": " +
            equipajeActual.getDescripcion() + " - $" + equipajeActual.getPrecio(),
            "Equipaje Confirmado",
            JOptionPane.INFORMATION_MESSAGE);
        
        equipajeActual = null; // Resetear para el siguiente pasajero
    }
    
    public List<String> getAsientosSeleccionados() {
        return asientosSeleccionados;
    }
    
    public List<Equipaje> getEquipajesSeleccionados() {
        return equipajesSeleccionados;
    }
}
