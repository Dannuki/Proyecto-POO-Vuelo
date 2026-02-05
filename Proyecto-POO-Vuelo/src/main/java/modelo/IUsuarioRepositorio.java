/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author USER
 */
public interface IUsuarioRepositorio {
    /**
     * Valida las credenciales y retorna un objeto Usuario con su respectivo Rol.
     * @param usuario El nombre de usuario ingresado.
     * @param contraseña La contraseña ingresada.
     * @return Objeto Usuario si es válido, null en caso contrario.
     */
    Usuario validar(String usuario, String contraseña);
    
    // Aquí podrías añadir más métodos en el futuro, como:
    // void guardar(Usuario usuario);
    
}
