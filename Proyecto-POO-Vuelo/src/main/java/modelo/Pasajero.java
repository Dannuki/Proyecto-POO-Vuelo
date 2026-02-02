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
    private String correoElectronico;

    public Pasajero(String nombres, String apellidos, LocalDate fechaNacimiento, 
                    String genero, String nacionalidad, String documentoIdentidad, String correoElectronico) {
        this.nombre = nombres;
        this.apellido = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.genero = genero;
        this.nacionalidad = nacionalidad;
        this.documentoIdentidad = documentoIdentidad;
        this.correoElectronico = correoElectronico;
    }

    //Comprobar si el pasajero es adulto o niño
    //niño <12 años
    //adultom >= 13 años
    public boolean esNino() {
        int edad = Period.between(this.fechaNacimiento, LocalDate.now()).getYears();
        return edad < 12;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getDocumentoIdentidad() {
        return documentoIdentidad;
    }

    public void setDocumentoIdentidad(String documentoIdentidad) {
        this.documentoIdentidad = documentoIdentidad;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    
    
}
