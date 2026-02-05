/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import modelo.Pasajero;

/**
 *
 * @author USUARIO
 */


public class GestorArchivos {
    
    public static void guardarDatos(List<Pasajero> lista, String ruta) throws IOException {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ruta, false))) {
            
            writer.write("--- REGISTRO DE VUELO ---");
            writer.newLine();
            
            for (Pasajero p : lista) {
                // Formato: Nombre | Apellido | Fecha | Edad
                String linea = p.getNombre() + " " + p.getApellido() + 
                               " | Nacimiento: " + p.getFechaNacimiento() + 
                               " | Nacionalidad: " + p.getNacionalidad() +
                               " | Correo: " + p.getCorreoElectronico();
                
                writer.write(linea);
                writer.newLine();
            }
            writer.write("-------------------------");
            writer.newLine();
            System.out.println("Archivo guardado en: " + ruta);
        }
    }
    
    public static void guardarVueloSeleccionado(String nombreArchivo, String contenido) {
        try (PrintWriter out = new PrintWriter(new FileWriter(nombreArchivo))) {
            out.println(contenido);
        } catch (IOException e) {
            System.out.println("Error al guardar: " + e.getMessage());
        }
    }
}
