/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;
import modelo.Usuario;
import modelo.Vuelo;
import modelo.Asiento;
import java.util.List;
/**
 *
 * @author USER
 */
public class SesionGlobal {
    // Instancia única (Singleton)
    private static SesionGlobal instancia;

    // --- VARIABLES DE MEMORIA (Aquí guardan sus cosas) ---
    private Usuario usuarioLogueado;      // Persona 1 guarda aquí
    private Vuelo vueloSeleccionado;      // Persona 2/3 guarda aquí
    private List<Asiento> asientosSeleccionados; // Persona 4 guarda aquí
    // Yo (Persona 5) leo todo esto al final.

    private SesionGlobal() {}

    public static SesionGlobal get() {
        if (instancia == null) { instancia = new SesionGlobal(); }
        return instancia;
    }

    // --- MÉTODOS PARA GUARDAR (Setters) ---
    public void setUsuario(Usuario u) { this.usuarioLogueado = u; }
    public void setVuelo(Vuelo v) { this.vueloSeleccionado = v; }
    public void setAsientos(List<Asiento> a) { this.asientosSeleccionados = a; }

    // --- MÉTODOS PARA LEER (Getters) ---
    public Usuario getUsuario() { return usuarioLogueado; }
    public Vuelo getVuelo() { return vueloSeleccionado; }
    public List<Asiento> getAsientos() { return asientosSeleccionados; }
}
    
    
    
    
    
    
    
    
    
    
    
    
}
