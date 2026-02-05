/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

import modelo.Pasajero;
import vista.VentanaRegistro;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;

public class ControladorRegistro implements ActionListener {

    private VentanaRegistro vista;

    // --- ESTE ES EL CONSTRUCTOR QUE TE FALTA ---
    public ControladorRegistro(int adultos, int ninos, int bebes) {
        // Inicializamos la vista pasando las cantidades
        this.vista = new VentanaRegistro(adultos, ninos, bebes);
        
        // Escuchamos el botón Continuar
        this.vista.getBtnContinuar().addActionListener(this);
    }

    public void iniciar() {
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        procesarRegistro();
    }

    private void procesarRegistro() {
        try {
            // 1. Obtener lista de pasajeros de la vista
            List<Pasajero> pasajeros = vista.obtenerPasajerosIngresados();
            
            // 2. Guardar en Sesión Global
            SesionGlobal.getInstancia().setListaPasajeros(pasajeros);
            
            // 3. Cerrar esta ventana y abrir PAGO
            vista.dispose();
            
            ControladorPago ctrlPago = new ControladorPago(); 
            ctrlPago.iniciar();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error: " + ex.getMessage());
        }
    }
}