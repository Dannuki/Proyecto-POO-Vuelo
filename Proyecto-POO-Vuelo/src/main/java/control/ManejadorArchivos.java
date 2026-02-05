/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

import java.io.BufferedReader;
import java.io.File;
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

    public void guardarVuelo(Vuelo v) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(nombreArchivo, true))) {
            pw.println(v.toString()); 
        } catch (IOException e) {
            System.err.println("Error al guardar: " + e.getMessage());
        }
    }

    // Lee el archivo y devuelve la lista de los 20 vuelos (o más)
    public ArrayList<Vuelo> cargarVuelos() {
        ArrayList<Vuelo> lista = new ArrayList<>();
        File archivo = new File(nombreArchivo);
        if (!archivo.exists()) return lista;
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] d = linea.split(",");
                if (d.length >= 9) { // Validamos que la linea esté completa
                    Ruta rut = new Ruta(d[2], d[3]); // Origen y Destino según tu tabla
                    Avion avio = new Avion(d[1]);    // Nombre del Avión
                    // Constructor: Codigo, Ruta, Avion, FechaS, FechaR, HoraS, HoraL, Precio
                    Vuelo vue = new Vuelo(d[0], rut, avio, d[4], d[7], d[5],
                            d[6], Double.parseDouble(d[8]));
                    lista.add(vue);
                }
            }
        } catch (Exception e) {
            System.out.println("Error al cargar datos: " + e.getMessage());
        }
        return lista;
    }
}