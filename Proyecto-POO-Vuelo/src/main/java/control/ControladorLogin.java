
package control;

import modelo.IUsuarioRepositorio;
import modelo.Usuario;
import vista.AccesoLogin; // Ajustado según tu imagen de paquetes
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class ControladorLogin {
    
  private AccesoLogin vista;
  private IUsuarioRepositorio repositorio;

    public ControladorLogin(AccesoLogin vista, IUsuarioRepositorio repositorio) {
        this.vista = vista;
        this.repositorio = repositorio;

        // Le decimos al botón de la vista que nosotros manejaremos el clic
        this.vista.getBtnEntrar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ejecutarLogin();
            }
        });
    }

    private void ejecutarLogin() {
        // 1. Obtenemos los datos de la GUI
        String usuario = vista.gettxtUsuario().getText();
        String contraseña = new String(vista.gettxtContra().getPassword());

        // 2. Validamos usando el modelo (SOLID)
        Usuario usuarioValidado = repositorio.validar(usuario, contraseña);

        // 3. Tomamos decisiones según el resultado y el ROL
// Dentro de tu método ejecutarLogin() en ControladorLogin.java

    if (usuarioValidado != null) {
    // 1. Guardamos al usuario en la mochila (SesionGlobal)
    // Esto permite que la ventana de "Buscador" sepa quién compró el boleto.
    SesionGlobal.get().setUsuario(usuarioValidado); 

    // 2. Continuamos con el flujo normal
    if (usuarioValidado.getRol().equalsIgnoreCase("Admin")) {
        abrirMenuAdmin();
    } else {
        abrirMenuCliente();
    }
}
    }

    private void abrirMenuAdmin() {
        System.out.println("Abriendo interfaz de Administrador...");
        // Aquí instanciarías el JFrame de Admin
    }

    private void abrirMenuCliente() {
        System.out.println("Abriendo interfaz de Cliente...");
        // Aquí instanciarías el JFrame de Cliente
    }  

}
