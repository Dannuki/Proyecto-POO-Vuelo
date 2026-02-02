/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package main;

import modelo.ArchivoUsuarioRepositorio;
import modelo.IUsuarioRepositorio;
import vista.AccesoLogin;
import control.ControladorLogin;


public class Aerolinea {
    

    public static void main(String[] args) {
      // 1. Instanciamos la persistencia (El Modelo)
        // Usamos la interfaz para cumplir con SOLID
        IUsuarioRepositorio modeloRepo = new ArchivoUsuarioRepositorio();

        // 2. Instanciamos la Ventana de Login (La Vista)
        AccesoLogin vistaLogin = new AccesoLogin();
      

        // 3. Instanciamos el Controlador (El que une a ambos)
        // Le pasamos la vista y el modelo para que los gestione
        ControladorLogin ctrl = new ControladorLogin(vistaLogin, modeloRepo);

        // 4. Configuraciones finales de la ventana
        vistaLogin.setTitle("Sistema de Aerolíneas - Login");
        vistaLogin.setLocationRelativeTo(null); // Centra la ventana
        vistaLogin.setVisible(true); // Hace que la ventana aparezca  
        
        
    }    

        
}

