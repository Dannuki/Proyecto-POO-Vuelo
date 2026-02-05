/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


/**
 *
 * @author USUARIO
 */

public class ReservaFrame extends JFrame {

    private AccesoLogin accesoLogin; // referencia al login
    private JButton[][] asientos = new JButton[3][4];
    private Set<String> asientosSeleccionados = new HashSet<>();
    private JButton btnMochila = new JButton("Mochila Mano");
    private JButton btnCarryOn = new JButton("Carry-On $15");
    private JButton btnBodega = new JButton("Bodega 23kg $29");
    private JButton btnPagar = new JButton("Pagar");
    private JLabel lblEquipaje = new JLabel("Equipaje: -");
    private JLabel lblTotal = new JLabel("Total: $0");
    private Color colorNormal = new Color(69, 130, 177);
    private Color colorHover = new Color(83, 155, 212);
    private String codigoVuelo, avion, origen, destino, fechaSalida, horaSalida, horaLlegada, fechaRetorno;
    private double precioBase;
    private String equipajeSeleccionado = "-";

    public ReservaFrame(AccesoLogin accesoLogin, String codigoVuelo, String avion, String origen,
                        String destino, String fechaSalida, String horaSalida,
                        String horaLlegada, String fechaRetorno, double precioBase) {

        this.accesoLogin = accesoLogin;
        this.codigoVuelo = codigoVuelo;
        this.avion = avion;
        this.origen = origen;
        this.destino = destino;
        this.fechaSalida = fechaSalida;
        this.horaSalida = horaSalida;
        this.horaLlegada = horaLlegada;
        this.fechaRetorno = fechaRetorno;
        this.precioBase = precioBase;

        initComponents();
    }

    private void initComponents() {
        setTitle("Reserva de Asientos y Equipaje");
        setSize(700, 500);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Información vuelo
        JLabel lblInfo = new JLabel("<html>Vuelo: " + codigoVuelo + "<br>"
                                    + "Avión: " + avion + "<br>"
                                    + "Origen: " + origen + "<br>"
                                    + "Destino: " + destino + "<br>"
                                    + "Fecha Salida: " + fechaSalida + " " + horaSalida + "<br>"
                                    + "Hora Llegada: " + horaLlegada + "<br>"
                                    + "Fecha Retorno: " + fechaRetorno + "<br>"
                                    + "Precio Base: $" + precioBase + "</html>");
        lblInfo.setBounds(20, 10, 400, 150);
        add(lblInfo);

        // Asientos
        char[] letras = {'A','B','C'};
        int x=50, y=170, ancho=80, alto=40, espacio=90;
        for (int fila=0; fila<3; fila++){
            for (int col=0; col<4; col++){
                String codigoAsiento = letras[fila] + String.valueOf(col+1);
                JButton btn = new JButton(codigoAsiento);
                btn.setBounds(x + col*espacio, y + fila*espacio, ancho, alto);
                btn.setBackground(Color.GREEN);
                btn.setOpaque(true);
                btn.setBorderPainted(false);
                btn.setForeground(Color.WHITE);

                btn.addActionListener(e -> {
                    if(btn.getBackground()==Color.GREEN){
                        btn.setBackground(Color.YELLOW);
                        asientosSeleccionados.add(codigoAsiento);
                    } else {
                        btn.setBackground(Color.GREEN);
                        asientosSeleccionados.remove(codigoAsiento);
                    }
                    actualizarTotal();
                });

                asientos[fila][col]=btn;
                add(btn);
            }
        }

        // Botones equipaje
        btnMochila.setBounds(450,50,180,40);
        btnCarryOn.setBounds(450,100,180,40);
        btnBodega.setBounds(450,150,180,40);

        for(JButton btn: new JButton[]{btnMochila,btnCarryOn,btnBodega}){
            btn.setBackground(colorNormal);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            addHoverEffect(btn);
            add(btn);
            btn.addActionListener(e -> seleccionarEquipaje(btn));
        }

        lblEquipaje.setBounds(450,210,200,30);
        add(lblEquipaje);
        lblTotal.setBounds(450,250,200,30);
        add(lblTotal);

        btnPagar.setBounds(450,300,180,50);
        btnPagar.setBackground(new Color(109,106,220));
        btnPagar.setForeground(Color.WHITE);
        btnPagar.setFocusPainted(false);
        add(btnPagar);
        btnPagar.addActionListener(e -> pagar());

        setVisible(true);
    }

    private void addHoverEffect(JButton btn){
        btn.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseEntered(java.awt.event.MouseEvent e){ btn.setBackground(colorHover);}
            public void mouseExited(java.awt.event.MouseEvent e){ btn.setBackground(colorNormal);}
        });
    }

    public void seleccionarEquipaje(JButton btn){
        if(btn==btnMochila) equipajeSeleccionado="Mochila Mano";
        else if(btn==btnCarryOn) equipajeSeleccionado="Carry-On";
        else if(btn==btnBodega) equipajeSeleccionado="Bodega 23kg";

        lblEquipaje.setText("Equipaje: " + equipajeSeleccionado);
        actualizarTotal();
    }

    public void actualizarTotal(){
        double total = precioBase;
        double precioEquipaje = 0;
        if(equipajeSeleccionado.equals("Carry-On")) precioEquipaje=15;
        else if(equipajeSeleccionado.equals("Bodega 23kg")) precioEquipaje=29;

        total += precioEquipaje * asientosSeleccionados.size();
        lblTotal.setText("Total: $" + total);
    }

    public void pagar(){
        if(asientosSeleccionados.isEmpty()){
            JOptionPane.showMessageDialog(this,"Seleccione al menos un asiento","Aviso",JOptionPane.WARNING_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,"Pago realizado!\nAsientos: " + asientosSeleccionados
                                      + "\n" + lblEquipaje.getText()
                                      + "\n" + lblTotal.getText());
        dispose(); // cerramos reserva
        if(accesoLogin!=null) accesoLogin.setVisible(true); // regresamos al login
    }
    
    // --- GETTERS para el controlador ---
public JButton getBtnMochila() { return btnMochila; }
public JButton getBtnCarryOn() { return btnCarryOn; }
public JButton getBtnBodega() { return btnBodega; }
public JButton getBtnPagar() { return btnPagar; }
public Set<String> getAsientosSeleccionados() { return asientosSeleccionados; }
public JButton getBotonAsiento(int fila, int col) { return asientos[fila][col]; }
}