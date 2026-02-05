/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author USER
 */
public class Ruta {
    private String origen;
    private String destino;

    public Ruta(String origen, String destino) {
        this.origen = origen;
        this.destino = destino;
    }

    public String getOrigen(){
        return origen; }
    public String getDestino() {
        return destino; }
}
