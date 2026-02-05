package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase que representa el boleto final emitido al cliente
 */
public class Boleto {
    private Reserva reserva;
    private String metodoPago;
    private LocalDateTime fechaEmision;

    public Boleto(Reserva reserva, String metodoPago) {
        this.reserva = reserva;
        this.metodoPago = metodoPago;
        this.fechaEmision = LocalDateTime.now();
    }

    /**
     * Genera el boleto en formato texto legible
     */
    public String generarBoletoTexto() {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        sb.append("═══════════════════════════════════════════════════════════\n");
        sb.append("                   BOLETO ELECTRÓNICO                      \n");
        sb.append("═══════════════════════════════════════════════════════════\n\n");

        // Información de reserva
        sb.append("Código de Reserva: ").append(reserva.getCodigoReserva()).append("\n");
        sb.append("Fecha de Emisión: ").append(fechaEmision.format(formatter)).append("\n");
        sb.append("Estado: ").append(reserva.getEstado()).append("\n\n");

        // Información del vuelo
        Vuelo v = reserva.getVuelo();
        sb.append("───────────────── INFORMACIÓN DEL VUELO ─────────────────\n");
        sb.append("Código de Vuelo: ").append(v.getCodigo()).append("\n");
        sb.append("Ruta: ").append(v.getRuta().getOrigen()).append(" → ").append(v.getRuta().getDestino()).append("\n");
        sb.append("Avión: ").append(v.getAvion().getNombre()).append("\n");
        sb.append("Fecha de Salida: ").append(v.getFechaSalida()).append("\n");
        sb.append("Hora de Salida: ").append(v.getHoraSalida()).append("\n");
        sb.append("Hora de Llegada: ").append(v.getHoraLlegada()).append("\n");
        
        if (v.getFechaRegreso() != null && !v.getFechaRegreso().isEmpty()) {
            sb.append("Fecha de Regreso: ").append(v.getFechaRegreso()).append("\n");
        }
        sb.append("\n");

        // Información de pasajeros
        sb.append("────────────────── PASAJEROS Y ASIENTOS ─────────────────\n");
        for (int i = 0; i < reserva.getPasajeros().size(); i++) {
            Pasajero p = reserva.getPasajeros().get(i);
            String asiento = reserva.getAsientosSeleccionados().get(i);
            Equipaje eq = reserva.getEquipajes().get(i);

            sb.append("\nPasajero ").append(i + 1).append(":\n");
            sb.append("  Nombre: ").append(p.getNombre()).append(" ").append(p.getApellido()).append("\n");
            sb.append("  Documento: ").append(p.getDocumentoIdentidad()).append("\n");
            sb.append("  Tipo: ").append(p.esNino() ? "NIÑO (Descuento 50%)" : "ADULTO").append("\n");
            sb.append("  Asiento: ").append(asiento).append("\n");
            sb.append("  Equipaje: ").append(eq.getDescripcion()).append(" - $").append(String.format("%.2f", eq.getPrecio())).append("\n");
        }
        sb.append("\n");

        // Desglose de precios
        sb.append("──────────────── DESGLOSE DE PRECIOS ────────────────────\n");
        double subtotalVuelos = 0.0;
        double subtotalEquipajes = 0.0;

        for (int i = 0; i < reserva.getPasajeros().size(); i++) {
            Pasajero p = reserva.getPasajeros().get(i);
            double precioVuelo = v.getPrecioBase();
            
            if (p.esNino()) {
                precioVuelo = precioVuelo * 0.5;
            }

            subtotalVuelos += precioVuelo;
            subtotalEquipajes += reserva.getEquipajes().get(i).getPrecio();

            sb.append("Pasajero ").append(i + 1).append(": $").append(String.format("%.2f", precioVuelo));
            if (p.esNino()) sb.append(" (50% desc.)");
            sb.append("\n");
        }

        sb.append("\nSubtotal Vuelos: $").append(String.format("%.2f", subtotalVuelos)).append("\n");
        sb.append("Subtotal Equipajes: $").append(String.format("%.2f", subtotalEquipajes)).append("\n");
        sb.append("───────────────────────────────────────────────────────────\n");
        sb.append("TOTAL A PAGAR: $").append(String.format("%.2f", reserva.getPrecioTotal())).append("\n");
        sb.append("Método de Pago: ").append(metodoPago).append("\n");
        sb.append("═══════════════════════════════════════════════════════════\n");
        sb.append("        ¡Gracias por volar con nosotros!                   \n");
        sb.append("═══════════════════════════════════════════════════════════\n");

        return sb.toString();
    }

    // Getters
    public Reserva getReserva() {
        return reserva;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }
}
