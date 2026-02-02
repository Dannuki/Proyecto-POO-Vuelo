/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

import vista.PanelPasajero;
import modelo.Pasajero;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author USUARIO
 */

public class ControladorPasajero {
    
    private PanelPasajero vista;


    public ControladorPasajero() {
        this.vista = new PanelPasajero();
        initController();
    }
    
    private void initController() {
    }


    // Metodo que perimite que la ventana principal ponga el panel
    public PanelPasajero getVista() {
        return this.vista;
    }

    public void configurarTipo(String tipo, int numero) {
        vista.configurarTipo(tipo, numero);
    }

    //Método para pedir los datos
    public Pasajero obtenerDatosControlados() throws Exception {
        return vista.obtenerDatos();
    }
}
