/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

import java.util.ArrayList;
import modelo.Vuelo;

import java.util.List;
/**
 *
 * @author RICHARD
 */
public class ControladorBusqueda {
    public List<Vuelo> buscar(List<Vuelo> base, String origen, String destino, double presupuesto) {
        List<Vuelo> resultado = new ArrayList<>();
        
        for (Vuelo v : base) {
            // 1. Acceder a la ruta y luego al origen/destino
            // En tu clase Vuelo usas getRuta(), y Ruta debe tener getOrigen()
            boolean coincideOrigen = v.getRuta().getOrigen().equals(origen);
            boolean coincideDestino = v.getRuta().getDestino().equals(destino);
            
            // 2. Usar el nombre exacto de tu atributo: getPrecioBase()
            boolean coincidePrecio = v.getPrecioBase() <= presupuesto;

            // Si cumple con la ruta completa y el presupuesto, lo agregamos
            if (coincideOrigen && coincideDestino && coincidePrecio) {
                resultado.add(v);
            }
        }
        return resultado;
    }
}