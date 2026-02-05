/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

import modelo.Vuelo;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author RICHARD
 */
public class BuscadorVuelosServicio {
    public List<Vuelo> filtrarVuelos(List<Vuelo> todos, String origen, String destino, double presupuesto) {
        List<Vuelo> filtrados = new ArrayList<>();
        
        for (Vuelo v : todos) {
            // 1. Validamos la ruta (Origen y Destino)
            boolean coincideRuta = v.getRuta().getOrigen().equalsIgnoreCase(origen) && 
                                   v.getRuta().getDestino().equalsIgnoreCase(destino);
            
            // 2. Validamos el presupuesto (PrecioBase)
            // Esto es lo que diferencia tu trabajo del de los demás
            boolean coincidePrecio = v.getPrecioBase() <= presupuesto;

            if (coincideRuta && coincidePrecio) {
                filtrados.add(v);
            }
        }
        return filtrados;
    }
}

