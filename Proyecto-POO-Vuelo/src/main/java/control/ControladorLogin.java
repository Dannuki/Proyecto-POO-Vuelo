
package control;

import modelo.IUsuarioRepositorio;
import modelo.Usuario;
import vista.AccesoLogin;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import vista.Catalogo;

public class ControladorLogin {
    private AccesoLogin vista;
    private IUsuarioRepositorio repositorio;

    public ControladorLogin(AccesoLogin vista, IUsuarioRepositorio repositorio) {
        this.vista = vista;
        this.repositorio = repositorio;

        // --- PRUEBA DE CABLEADO Y CONEXIÓN ÚNICA ---
        if (this.vista.getBtnEntrar() == null) {
            System.out.println("ERROR: El controlador no encuentra el botón en la vista. Revisa el Getter.");
        } else {
            System.out.println("CONEXIÓN EXITOSA: El controlador detectó el botón.");
            
            // Registramos el evento una sola vez de forma limpia
            this.vista.getBtnEntrar().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("EVENTO DETECTADO: El botón fue presionado.");
                    ejecutarLogin();
                }
            });
        }
    }

    public void ejecutarLogin() {
        // 1. Obtenemos los datos usando los getters de la vista
        String usuario = vista.gettxtUsuario().getText().trim();
        String contraseña = new String(vista.gettxtContra().getPassword()).trim();

        // 2. VALIDACIÓN DE CAMPOS VACÍOS (BLOQUEO REAL)
        if (usuario.isEmpty() || usuario.equals("Ingrese su nombre de usuario") || contraseña.isEmpty()) {
            JOptionPane.showMessageDialog(vista,
                    "Debe ingresar usuario y contraseña para continuar.",
                    "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return; // Detiene la ejecución aquí
        }

        // 3. Validación con el repositorio
        Usuario usuarioValidado = repositorio.validar(usuario, contraseña);

        if (usuarioValidado != null) {
            abrirModuloCatalogo();  
            vista.dispose(); // Cierra el login
        } else {
            JOptionPane.showMessageDialog(vista, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void abrirModuloCatalogo() {
        // Instanciamos la Vista y el Controlador del Catálogo
        Catalogo miCatalogo = new Catalogo();
        new ControladorCatalogo(miCatalogo); // Activa filtros y carga de datos
        
        miCatalogo.setVisible(true);
        miCatalogo.setLocationRelativeTo(null);
    }
}