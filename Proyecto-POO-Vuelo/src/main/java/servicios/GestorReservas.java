package servicios;

import modelo.Boleto;
import modelo.Reserva;
import java.io.*;
import java.time.format.DateTimeFormatter;

/**
 * Clase que maneja la persistencia de reservas y boletos
 */
public class GestorReservas {
    
    private static final String ARCHIVO_RESERVAS = "reservas.txt";
    private static final String CARPETA_BOLETOS = "boletos/";
    
    public GestorReservas() {
        // Crear carpeta de boletos si no existe
        File carpeta = new File(CARPETA_BOLETOS);
        if (!carpeta.exists()) {
            carpeta.mkdir();
        }
    }
    
    /**
     * Guarda una reserva en el archivo de reservas
     */
    public void guardarReserva(Reserva reserva) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_RESERVAS, true))) {
            pw.println(reserva.toString());
            System.out.println("Reserva guardada: " + reserva.getCodigoReserva());
        } catch (IOException e) {
            System.err.println("Error al guardar reserva: " + e.getMessage());
        }
    }
    
    /**
     * Guarda el boleto en un archivo de texto individual
     */
    public String guardarBoleto(Boleto boleto) {
        String nombreArchivo = CARPETA_BOLETOS + boleto.getReserva().getCodigoReserva() + ".txt";
        
        try (PrintWriter pw = new PrintWriter(new FileWriter(nombreArchivo))) {
            pw.print(boleto.generarBoletoTexto());
            System.out.println("Boleto guardado en: " + nombreArchivo);
            return nombreArchivo;
        } catch (IOException e) {
            System.err.println("Error al guardar boleto: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Guarda tanto la reserva como el boleto
     */
    public String guardarReservaYBoleto(Reserva reserva, Boleto boleto) {
        guardarReserva(reserva);
        return guardarBoleto(boleto);
    }
    
    /**
     * Lee el contenido de un boleto guardado
     */
    public String leerBoleto(String codigoReserva) {
        String nombreArchivo = CARPETA_BOLETOS + codigoReserva + ".txt";
        StringBuilder contenido = new StringBuilder();
        
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                contenido.append(linea).append("\n");
            }
            return contenido.toString();
        } catch (IOException e) {
            System.err.println("Error al leer boleto: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Verifica si existe un boleto con ese código
     */
    public boolean existeBoleto(String codigoReserva) {
        String nombreArchivo = CARPETA_BOLETOS + codigoReserva + ".txt";
        File archivo = new File(nombreArchivo);
        return archivo.exists();
    }
}
