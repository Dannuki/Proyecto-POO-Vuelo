package modelo;

/**
 * Clase que representa un asiento en el avión
 */
public class Asiento {
    private String numero; // Ejemplo: "1A", "2B", "3C"
    private boolean ocupado;
    private String tipoPasajero; // "ADULTO", "NIÑO", "BEBE" (opcional)

    public Asiento(String numero) {
        this.numero = numero;
        this.ocupado = false;
        this.tipoPasajero = "";
    }

    public void ocupar(String tipoPasajero) {
        this.ocupado = true;
        this.tipoPasajero = tipoPasajero;
    }

    public void liberar() {
        this.ocupado = false;
        this.tipoPasajero = "";
    }

    // Getters y Setters
    public String getNumero() {
        return numero;
    }

    public boolean isOcupado() {
        return ocupado;
    }

    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }

    public String getTipoPasajero() {
        return tipoPasajero;
    }

    public void setTipoPasajero(String tipoPasajero) {
        this.tipoPasajero = tipoPasajero;
    }

    @Override
    public String toString() {
        return numero + (ocupado ? " [X]" : " [ ]");
    }
}
