
package modelo;
import java.io.*;

public class Persistencia {
    private final String rutaUsuarios = "usuarios.txt";
    private final String rutaVuelos = "vuelos.txt";
    
    public Persistencia(){
       inicializarBaseDeDatos();  
    }
    
    private void inicializarBaseDeDatos() {
        File f = new File(rutaUsuarios);
        if (!f.exists()) {
            crearArchivo(rutaUsuarios);
            // IMPORTANTE: Agregamos el ROL al final (admin,clave,rol)
            escribirArchivo(rutaUsuarios, "admin,admin123,Admin");
            escribirArchivo(rutaUsuarios, "cliente,123,Cliente");
        }
        
        File fv = new File(rutaVuelos);
        if (!fv.exists()) {
            crearArchivo(rutaVuelos);
            escribirArchivo(rutaVuelos, "Vuelo 001: Quito-Guayaquil | 10:00 AM | $50");
        }
    }
    
    public void crearArchivo(String nombreArchivo) {
        try {
            File archivo = new File(nombreArchivo);
            if (archivo.createNewFile()) {
                System.out.println("Se creó el archivo: " + nombreArchivo);
            }
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
    }

    public void escribirArchivo(String nombreArchivo, String contenido) {
        // 'true' permite adjuntar sin borrar lo anterior
        try (PrintWriter salida = new PrintWriter(new FileWriter(nombreArchivo, true))) {
            salida.println(contenido);
            System.out.println("Dato guardado en: " + nombreArchivo);
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
    }

    public Usuario validarCredenciales(String usuario, String contraseña) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaUsuarios))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length >= 3 && datos[0].equals(usuario) && datos[1].equals(contraseña)) {
                    return new Usuario(datos[0], datos[1], datos[2]); // Retorna "Admin" o "Cliente"
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer usuarios.");
        }
        return null;
    }
}
    

    
    
    
