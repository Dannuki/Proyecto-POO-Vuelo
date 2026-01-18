
package modelo;

public class Usuario {
    private String nombre;
    private String rol;

    public Usuario(String nombre, String contraseña, String rol) {
        this.nombre = nombre;
        this.rol = rol;
    }

    // Getters (Necesarios para que el Controlador sepa qué mostrar)
    public String getNombre() { return nombre; }
    public String getRol() { return rol; }
    
 
    
    
}
