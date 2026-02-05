/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

import modelo.Usuario;
import modelo.Vuelo;
import modelo.Pasajero;
import java.util.List;

/**
 *
 * @author USUARIO
 */

public class SesionGlobal {
    private static SesionGlobal instancia;
    
    // Variables de viaje
    private Usuario usuarioLogueado;
    private Vuelo vueloSeleccionado;
    private List<Pasajero> listaPasajeros;
    
    // --- VARIABLES DE EMERGENCIA PARA CONECTAR VENTANAS ---
    private int cantAdultos = 0;
    private int cantNinos = 0;
    private int cantBebes = 0;
    private double costoTotalEquipaje = 0.0;

    private SesionGlobal() {}

    public static SesionGlobal getInstancia() {
        if (instancia == null) { instancia = new SesionGlobal(); }
        return instancia;
    }

    // Getters y Setters Nuevos
    public int getCantAdultos() { return cantAdultos; }
    public void setCantAdultos(int a) { this.cantAdultos = a; }
    
    public int getCantNinos() { return cantNinos; }
    public void setCantNinos(int n) { this.cantNinos = n; }
    
    public int getCantBebes() { return cantBebes; }
    public void setCantBebes(int b) { this.cantBebes = b; }
    
    public double getCostoTotalEquipaje() { return costoTotalEquipaje; }
    public void setCostoTotalEquipaje(double c) { this.costoTotalEquipaje = c; }
    
    // Getters y Setters Originales
    public Vuelo getVueloSeleccionado() { return vueloSeleccionado; }
    public void setVueloSeleccionado(Vuelo v) { this.vueloSeleccionado = v; }
    
    public Usuario getUsuarioLogueado() { return usuarioLogueado; }
    public void setUsuarioLogueado(Usuario u) { this.usuarioLogueado = u; }
    
    public List<Pasajero> getListaPasajeros() { return listaPasajeros; }
    public void setListaPasajeros(List<Pasajero> l) { this.listaPasajeros = l; }
}
