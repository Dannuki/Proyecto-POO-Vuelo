/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author USER
 */
public class Vuelo {
    // Atributos básicos
    private String codigo;
    private double precioBase;
    private String fechaSalida;
    private String fechaRegreso;
    private String horaSalida;
    private String horaLlegada;

    private Ruta ruta; 
    private Avion avion;

    // Constructor
    public Vuelo(String codigo, Ruta ruta, Avion avion, String fSalida,
            String fRegreso, String hSalida, String hLlegada, double precio){
        this.codigo = codigo;
        this.ruta = ruta;
        this.avion = avion;
        this.fechaSalida = fSalida;
        this.fechaRegreso = fRegreso;
        this.horaSalida = hSalida;
        this.horaLlegada = hLlegada;
        this.precioBase = precio;
    }

    // Getters y Setters
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

    public String getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(String fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public String getFechaRegreso() {
        return fechaRegreso;
    }

    public void setFechaRegreso(String fechaRegreso) {
        this.fechaRegreso = fechaRegreso;
    }

    public String getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(String horaSalida) {
        this.horaSalida = horaSalida;
    }

    public String getHoraLlegada() {
        return horaLlegada;
    }

    public void setHoraLlegada(String horaLlegada) {
        this.horaLlegada = horaLlegada;
    }

    public Ruta getRuta() {
        return ruta;
    }

    public void setRuta(Ruta ruta) {
        this.ruta = ruta;
    }

    public Avion getAvion() {
        return avion;
    }

    public void setAvion(Avion avion) {
        this.avion = avion;
    }
    
    /**
     * Método especial para guardar en archivo de texto.
     * Convierte el objeto en una línea separada por comas.
     */
    
    @Override
    public String toString() {
        return codigo + "," + ruta.getOrigen() + "," + ruta.getDestino() + "," + 
               fechaSalida + "," + fechaRegreso + "," + horaSalida + "," + 
               horaLlegada + "," + precioBase + "," + avion.getNombre();
    }
}
