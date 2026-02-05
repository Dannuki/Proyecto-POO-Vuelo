package control;

import vista.VentanaPago;
import modelo.*; 
import servicios.GestorReservas;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;

public class ControladorPago implements ActionListener {
    
    private VentanaPago vista;

    public ControladorPago() {
        this.vista = new VentanaPago();
        
        // CONEXIÓN SEGURA: Usamos el getter que acabas de arreglar
        if (this.vista.getBtnContinuar() != null) {
            this.vista.getBtnContinuar().addActionListener(this);
        } else {
            System.out.println("¡ERROR! El botón 'continuar1' no fue encontrado por el Getter.");
        }
        
        cargarResumenVisual();
        this.iniciar();
    }

    public void iniciar() {
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
    }

    // Muestra los datos en la pantalla antes de pagar
    private void cargarResumenVisual() {
        try {
            SesionGlobal sesion = SesionGlobal.getInstancia();
            Vuelo vuelo = sesion.getVueloSeleccionado();
            
            double precioBase = (vuelo != null) ? vuelo.getPrecioBase() : 0.0;
            double total = (sesion.getCantAdultos() * precioBase) + 
                           (sesion.getCantNinos() * precioBase * 0.70) + 
                           sesion.getCostoTotalEquipaje();
            
            // IVA del 15%
            double totalConImpuestos = total * 1.15; 

            // Mostrar en el cuadro de texto
            StringBuilder sb = new StringBuilder();
            sb.append("VUELO: ").append(vuelo != null ? vuelo.getCodigo() : "N/A").append("\n");
            sb.append("PASAJEROS: ").append(sesion.getCantAdultos() + sesion.getCantNinos()).append("\n");
            
            if (vista.getTxtResumen() != null) {
                vista.getTxtResumen().setText(sb.toString());
            }
            
            // Mostrar precio
            if (vista.getTxtMontoTotal() != null) {
                // Probamos poner el texto (si es JLabel o TextField)
                try {
                    vista.getTxtMontoTotal().setText("$ " + String.format("%.2f", totalConImpuestos));
                } catch(Exception e) {
                    // Si es JFormattedTextField a veces requiere setValue
                    // vista.getLblMontoTotal().setValue(totalConImpuestos);
                }
            }
        } catch(Exception e) {
            System.out.println("Error visual menor: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        // 1. VALIDACIÓN: ¿Eligió método de pago?
        if (vista.getCbxMetodoPago().getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(vista, "Por favor seleccione un método de pago.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String metodoPago = vista.getCbxMetodoPago().getSelectedItem().toString();

        try {
            // 2. RECUPERAR DATOS DE LA SESIÓN
            SesionGlobal sesion = SesionGlobal.getInstancia();
            Vuelo vuelo = sesion.getVueloSeleccionado();
            List<Pasajero> pasajeros = sesion.getListaPasajeros();
            
            // 3. LÓGICA DE NEGOCIO: CREAR RESERVA
            Reserva nuevaReserva = new Reserva(Reserva.generarCodigoReserva(), vuelo);
            
            // Determinamos el equipaje (Venta rápida: todos llevan lo mismo)
            Equipaje tipoEquipaje = (sesion.getCostoTotalEquipaje() > 0) 
                    ? new Equipaje(Equipaje.TipoEquipaje.BODEGA_23KG) 
                    : new Equipaje(Equipaje.TipoEquipaje.MOCHILA_MANO);

            // Agregamos cada pasajero a la reserva
            for (Pasajero p : pasajeros) {
                nuevaReserva.agregarPasajero(p, "GEN", tipoEquipaje);
            }
            
            // Calculamos totales internos
            nuevaReserva.confirmarReserva();
            
            // 4. GENERAR EL BOLETO
            Boleto boletoFinal = new Boleto(nuevaReserva, metodoPago);
            
            // 5. GUARDAR EN ARCHIVO (PERSISTENCIA)
            GestorReservas gestor = new GestorReservas();
            String ruta = gestor.guardarReservaYBoleto(nuevaReserva, boletoFinal);
            
            System.out.println("Boleto guardado en: " + ruta);
            
            // 6. MENSAJE FINAL Y CIERRE
            // Al usuario darle "OK" a este mensaje, el código continúa abajo
            JOptionPane.showMessageDialog(vista, 
                    "¡PAGO EXITOSO!\n\nSu boleto ha sido generado correctamente.\nGracias por volar con nosotros.", 
                    "Confirmación", 
                    JOptionPane.INFORMATION_MESSAGE);
            
            // CERRAR EL SISTEMA
            System.exit(0);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error al procesar el pago: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}