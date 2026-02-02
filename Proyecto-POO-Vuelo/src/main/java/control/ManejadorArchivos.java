/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import modelo.Avion;
import modelo.Ruta;
import modelo.Vuelo;

/**
 *
 * @author USER
 */
public class ManejadorArchivos {
    private String nombreArchivo = "vuelos.txt";

    // Guarda todos los vuelos de la tabla al archivo
    public void guardarVuelos(ArrayList<Vuelo> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(nombreArchivo))) {
            for (Vuelo v : lista) {
                pw.println(v.toString()); // Usamos el toString que ya creamos
            }
        } catch (IOException e) {
            System.err.println("Hubo un error al guardar: " + e.getMessage());
        }
    }

    // Lee el archivo y devuelve una lista de objetos Vuelo
    public ArrayList<Vuelo> cargarVuelos() {
        ArrayList<Vuelo> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] d = linea.split(",");
                // Reconstruimos los objetos según el orden del toString
                Ruta rut = new Ruta(d[1], d[2]);
                Avion avio = new Avion(d[8]);
                Vuelo vue = new Vuelo(d[0], rut, avio, d[3], d[4], d[5], d[6], Double.parseDouble(d[7]));
                lista.add(vue);
            }
        } catch (IOException e) {
            System.out.println("Archivo no encontrado, se creará uno nuevo.");
        }
        return lista;
    }
}