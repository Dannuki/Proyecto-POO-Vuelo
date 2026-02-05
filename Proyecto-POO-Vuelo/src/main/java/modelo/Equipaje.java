/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author USUARIO
 */

//Se establecio precios para los tipos de equipaje
public class Equipaje {
    
    public enum TipoEquipaje {
        MOCHILA_MANO(0.0),
        CARRY_ON(15.0),
        BODEGA_23KG(29.0);

        private final double precio;
        TipoEquipaje(double precio) { this.precio = precio; }
        public double getPrecio() { return precio; }
    }

    private TipoEquipaje tipo;

    public Equipaje(TipoEquipaje tipo) {
        this.tipo = tipo;
    }

    public double getPrecio() {
        return tipo.getPrecio();
    }
    
    public String getDescripcion() {
        return tipo.name();
    }
}
