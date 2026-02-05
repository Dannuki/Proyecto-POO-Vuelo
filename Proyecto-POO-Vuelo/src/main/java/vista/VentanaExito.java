/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package vista;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class VentanaExito extends JFrame {

    public VentanaExito(String codigoVuelo, String avion, int cantPasajeros, double total) {
        // Configuración de la Ventana
        setTitle("Confirmación de Compra");
        setSize(400, 500);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true); // Diseño moderno sin bordes

        // Panel Principal (Blanco)
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(null);
        panel.setBounds(0, 0, 400, 500);
        panel.setBorder(new LineBorder(new Color(69, 130, 177), 2));
        add(panel);

        // Título
        JLabel lblTitulo = new JLabel("¡PAGO EXITOSO!", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(34, 139, 34)); // Verde
        lblTitulo.setBounds(0, 30, 400, 40);
        panel.add(lblTitulo);

        // Subtítulo
        JLabel lblSub = new JLabel("Su boleto ha sido generado correctamente.", SwingConstants.CENTER);
        lblSub.setFont(new Font("Arial", Font.PLAIN, 12));
        lblSub.setForeground(Color.GRAY);
        lblSub.setBounds(0, 70, 400, 20);
        panel.add(lblSub);

        // --- CAJA DE DATOS ---
        JPanel panelDatos = new JPanel();
        panelDatos.setBackground(new Color(245, 245, 245));
        panelDatos.setLayout(null);
        panelDatos.setBounds(40, 110, 320, 250);
        panel.add(panelDatos);

        // Datos
        crearDato(panelDatos, "CÓDIGO DE VUELO:", codigoVuelo, 20);
        crearDato(panelDatos, "AVIÓN:", avion, 80);
        crearDato(panelDatos, "PASAJEROS:", cantPasajeros + " Personas", 140);
        
        // Total
        JLabel lblTotalTitulo = new JLabel("TOTAL PAGADO:");
        lblTotalTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotalTitulo.setBounds(20, 200, 150, 30);
        panelDatos.add(lblTotalTitulo);

        JLabel lblTotalValor = new JLabel("$ " + String.format("%.2f", total));
        lblTotalValor.setFont(new Font("Arial", Font.BOLD, 22));
        lblTotalValor.setForeground(new Color(69, 130, 177));
        lblTotalValor.setBounds(150, 200, 150, 30);
        panelDatos.add(lblTotalValor);

        // Botón Finalizar
        JButton btnFinalizar = new JButton("FINALIZAR");
        btnFinalizar.setBounds(100, 400, 200, 40);
        btnFinalizar.setBackground(new Color(69, 130, 177));
        btnFinalizar.setForeground(Color.WHITE);
        btnFinalizar.setFont(new Font("Arial", Font.BOLD, 14));
        btnFinalizar.setFocusPainted(false);
        
        btnFinalizar.addActionListener(e -> System.exit(0));
        panel.add(btnFinalizar);
    }

    private void crearDato(JPanel p, String titulo, String valor, int y) {
        JLabel lblT = new JLabel(titulo);
        lblT.setFont(new Font("Arial", Font.BOLD, 12));
        lblT.setForeground(Color.GRAY);
        lblT.setBounds(20, y, 200, 20);
        p.add(lblT);

        JLabel lblV = new JLabel(valor);
        lblV.setFont(new Font("Arial", Font.BOLD, 16));
        lblV.setForeground(Color.BLACK);
        lblV.setBounds(20, y + 25, 280, 20);
        p.add(lblV);
    }
}