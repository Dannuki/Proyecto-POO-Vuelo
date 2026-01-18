/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;
import java.time.LocalDate;
import java.time.Period;

/**
 *
 * @author Danny Ponce
 */

public class Pasajero {
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private String genero;
    private String nacionalidad;
    private String documentoIdentidad;

    public Pasajero(String nombres, String apellidos, LocalDate fechaNacimiento, 
                    String genero, String nacionalidad, String documentoIdentidad) {
        this.nombre = nombres;
        this.apellido = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.genero = genero;
        this.nacionalidad = nacionalidad;
        this.documentoIdentidad = documentoIdentidad;
    }

    //Comprobar si el pasajero es adulto o niño
    //niño <12 años
    //adultom >= 13 años
    public boolean esNino() {
        int edad = Period.between(this.fechaNacimiento, LocalDate.now()).getYears();
        return edad < 12;
    }

    public String getNombreCompleto() { return nombre + " " + apellido; }
    public String getDocumentoIdentidad() { return documentoIdentidad; }
}
