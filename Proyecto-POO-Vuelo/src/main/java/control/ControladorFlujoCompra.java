package control;

import modelo.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador principal que maneja el flujo completo de compra
 * Catálogo → Pasajeros → Asientos → Equipaje → Pago → Boleto
 */
public class ControladorFlujoCompra {
    
    private Vuelo vueloSeleccionado;
    private Reserva reservaActual;
    private List<Pasajero> pasajerosTemporales;
    private List<Asiento> asientosDisponibles;
    private int cantidadAdultos;
    private int cantidadNinos;
    private int cantidadBebes;
    
    public ControladorFlujoCompra() {
        this.pasajerosTemporales = new ArrayList<>();
        inicializarAsientos();
    }
    
    // ============ PASO 1: SELECCIÓN DE VUELO DESDE CATÁLOGO ============
    
    public void seleccionarVuelo(Vuelo vuelo) {
        this.vueloSeleccionado = vuelo;
        System.out.println("Vuelo seleccionado: " + vuelo.getCodigo());
    }
    
    // ============ PASO 2: DEFINIR CANTIDAD DE PASAJEROS ============
    
    public void establecerCantidadPasajeros(int adultos, int ninos, int bebes) {
        this.cantidadAdultos = adultos;
        this.cantidadNinos = ninos;
        this.cantidadBebes = bebes;
        
        int total = adultos + ninos + bebes;
        if (total > 12) {
            throw new IllegalArgumentException("El avión solo tiene capacidad para 12 pasajeros");
        }
        
        System.out.println("Cantidad de pasajeros: " + adultos + " adultos, " + 
                         ninos + " niños, " + bebes + " bebés");
    }
    
    public int getTotalPasajeros() {
        return cantidadAdultos + cantidadNinos + cantidadBebes;
    }
    
    // ============ PASO 3: CAPTURAR DATOS DE PASAJEROS ============
    
    public void agregarPasajero(Pasajero pasajero) {
        if (pasajerosTemporales.size() >= getTotalPasajeros()) {
            throw new IllegalStateException("Ya se registraron todos los pasajeros");
        }
        pasajerosTemporales.add(pasajero);
        System.out.println("Pasajero agregado: " + pasajero.getNombre());
    }
    
    public boolean todosLosPasajerosRegistrados() {
        return pasajerosTemporales.size() == getTotalPasajeros();
    }
    
    public int getPasajerosRegistrados() {
        return pasajerosTemporales.size();
    }
    
    // ============ PASO 4: GESTIÓN DE ASIENTOS ============
    
    private void inicializarAsientos() {
        asientosDisponibles = new ArrayList<>();
        String[] columnas = {"A", "B", "C", "D"};
        
        // 3 filas x 4 columnas = 12 asientos
        for (int fila = 1; fila <= 3; fila++) {
            for (String col : columnas) {
                asientosDisponibles.add(new Asiento(fila + col));
            }
        }
    }
    
    public List<Asiento> getAsientosDisponibles() {
        return asientosDisponibles;
    }
    
    public boolean asientoEstaDisponible(String numeroAsiento) {
        for (Asiento a : asientosDisponibles) {
            if (a.getNumero().equals(numeroAsiento)) {
                return !a.isOcupado();
            }
        }
        return false;
    }
    
    public void ocuparAsiento(String numeroAsiento, String tipoPasajero) {
        for (Asiento a : asientosDisponibles) {
            if (a.getNumero().equals(numeroAsiento)) {
                a.ocupar(tipoPasajero);
                System.out.println("Asiento " + numeroAsiento + " ocupado por " + tipoPasajero);
                return;
            }
        }
    }
    
    public void liberarAsiento(String numeroAsiento) {
        for (Asiento a : asientosDisponibles) {
            if (a.getNumero().equals(numeroAsiento)) {
                a.liberar();
                System.out.println("Asiento " + numeroAsiento + " liberado");
                return;
            }
        }
    }
    
    // ============ PASO 5: CREAR RESERVA CON ASIENTOS Y EQUIPAJE ============
    
    public void crearReserva(List<String> asientosSeleccionados, List<Equipaje> equipajes) {
        if (pasajerosTemporales.size() != asientosSeleccionados.size() || 
            pasajerosTemporales.size() != equipajes.size()) {
            throw new IllegalArgumentException("Debe haber un asiento y equipaje por cada pasajero");
        }
        
        String codigoReserva = Reserva.generarCodigoReserva();
        reservaActual = new Reserva(codigoReserva, vueloSeleccionado);
        
        for (int i = 0; i < pasajerosTemporales.size(); i++) {
            reservaActual.agregarPasajero(
                pasajerosTemporales.get(i),
                asientosSeleccionados.get(i),
                equipajes.get(i)
            );
        }
        
        reservaActual.calcularPrecioTotal();
        System.out.println("Reserva creada: " + codigoReserva);
    }
    
    // ============ PASO 6: CONFIRMAR PAGO Y GENERAR BOLETO ============
    
    public Boleto confirmarPago(String metodoPago) {
        if (reservaActual == null) {
            throw new IllegalStateException("No hay una reserva activa");
        }
        
        reservaActual.confirmarReserva();
        Boleto boleto = new Boleto(reservaActual, metodoPago);
        
        System.out.println("Pago confirmado. Boleto generado.");
        return boleto;
    }
    
    // ============ MÉTODOS AUXILIARES ============
    
    public Reserva getReservaActual() {
        return reservaActual;
    }
    
    public Vuelo getVueloSeleccionado() {
        return vueloSeleccionado;
    }
    
    public List<Pasajero> getPasajerosTemporales() {
        return pasajerosTemporales;
    }
    
    public void reiniciarFlujo() {
        this.vueloSeleccionado = null;
        this.reservaActual = null;
        this.pasajerosTemporales.clear();
        this.cantidadAdultos = 0;
        this.cantidadNinos = 0;
        this.cantidadBebes = 0;
        inicializarAsientos();
        System.out.println("Flujo de compra reiniciado");
    }
    
    public int getCantidadAdultos() {
        return cantidadAdultos;
    }
    
    public int getCantidadNinos() {
        return cantidadNinos;
    }
    
    public int getCantidadBebes() {
        return cantidadBebes;
    }
}
