package modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una reserva completa con vuelo, pasajeros, asientos y equipaje
 */
public class Reserva {
    private String codigoReserva;
    private Vuelo vuelo;
    private List<Pasajero> pasajeros;
    private List<String> asientosSeleccionados; // Ejemplo: ["1A", "1B", "2C"]
    private List<Equipaje> equipajes; // Un equipaje por pasajero
    private double precioTotal;
    private LocalDateTime fechaReserva;
    private String estado; // "PENDIENTE", "CONFIRMADA", "CANCELADA"

    public Reserva(String codigoReserva, Vuelo vuelo) {
        this.codigoReserva = codigoReserva;
        this.vuelo = vuelo;
        this.pasajeros = new ArrayList<>();
        this.asientosSeleccionados = new ArrayList<>();
        this.equipajes = new ArrayList<>();
        this.fechaReserva = LocalDateTime.now();
        this.estado = "PENDIENTE";
        this.precioTotal = 0.0;
    }

    // Agregar pasajero con su asiento y equipaje
    public void agregarPasajero(Pasajero pasajero, String asiento, Equipaje equipaje) {
        this.pasajeros.add(pasajero);
        this.asientosSeleccionados.add(asiento);
        this.equipajes.add(equipaje);
    }

    // Calcular precio total
    public void calcularPrecioTotal() {
        double total = 0.0;

        for (int i = 0; i < pasajeros.size(); i++) {
            Pasajero p = pasajeros.get(i);
            double precioBase = vuelo.getPrecioBase();

            // Si es niño (menor de 12), descuento del 50%
            if (p.esNino()) {
                precioBase = precioBase * 0.5;
            }

            // Agregar precio de equipaje
            double precioEquipaje = equipajes.get(i).getPrecio();

            total += precioBase + precioEquipaje;
        }

        this.precioTotal = total;
    }

    public void confirmarReserva() {
        this.estado = "CONFIRMADA";
        calcularPrecioTotal();
    }

    // Getters y Setters
    public String getCodigoReserva() {
        return codigoReserva;
    }

    public Vuelo getVuelo() {
        return vuelo;
    }

    public List<Pasajero> getPasajeros() {
        return pasajeros;
    }

    public List<String> getAsientosSeleccionados() {
        return asientosSeleccionados;
    }

    public List<Equipaje> getEquipajes() {
        return equipajes;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public LocalDateTime getFechaReserva() {
        return fechaReserva;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Método para generar el string que se guarda en archivo
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(codigoReserva).append("|");
        sb.append(vuelo.getCodigo()).append("|");
        sb.append(fechaReserva.toString()).append("|");
        sb.append(estado).append("|");
        sb.append(precioTotal).append("|");
        
        // Pasajeros (separados por ";")
        sb.append("[");
        for (int i = 0; i < pasajeros.size(); i++) {
            Pasajero p = pasajeros.get(i);
            sb.append(p.getNombre()).append(" ").append(p.getApellido());
            sb.append(":").append(asientosSeleccionados.get(i));
            sb.append(":").append(equipajes.get(i).getDescripcion());
            if (i < pasajeros.size() - 1) sb.append(";");
        }
        sb.append("]");
        
        return sb.toString();
    }

    // Generar un código único de reserva
    public static String generarCodigoReserva() {
        return "RES-" + System.currentTimeMillis();
    }
}
