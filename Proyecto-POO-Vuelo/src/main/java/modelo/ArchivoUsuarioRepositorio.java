/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author USER
 */
public class ArchivoUsuarioRepositorio implements IUsuarioRepositorio {
    
    private Persistencia persistencia;

    public ArchivoUsuarioRepositorio() {
        // Instanciamos tu clase de manejo de archivos
        this.persistencia = new Persistencia();
    }

    @Override
    public Usuario validar(String usuario, String contraseña) {
        // Llamamos al método que ya tienes en tu clase Persistencia
        Usuario userValidado = persistencia.validarCredenciales(usuario, contraseña);
        
        if (userValidado != null) {
            // Si el rol existe, creamos el objeto Usuario para el sistema
            return userValidado;
        }
        
        return null; // Credenciales incorrectas
    }
}
    
 
    
    
    
    

