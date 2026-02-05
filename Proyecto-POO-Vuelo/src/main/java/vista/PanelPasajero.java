/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vista;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import java.time.Period;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
 *
 * @author USUARIO
 */
public class PanelPasajero extends javax.swing.JPanel {

    private javax.swing.border.Border bordeOriginal;
    
    private String tipoPasajeroActual = "ADULTO";
    
    public PanelPasajero() {
    initComponents();
    Color grisFondo = new Color(153, 153, 153);
    txtNombre.setText("Nombre:");     txtNombre.setForeground(grisFondo);
    txtApellido.setText("Apellido:"); txtApellido.setForeground(grisFondo);
    txtCorreo.setText("Correo Electrónico."); txtCorreo.setForeground(grisFondo);

    // --- CREACIÓN DE TU BORDE BONITO ---
    javax.swing.border.Border bordeLinea = javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200), 1, true);
    javax.swing.border.Border margen = javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10);
    javax.swing.border.Border bordeFinal = javax.swing.BorderFactory.createCompoundBorder(bordeLinea, margen);
    
    // Aplicamos el borde bonito
    txtNombre.setBorder(bordeFinal);
    txtApellido.setBorder(bordeFinal);
    txtFechaNac.setBorder(bordeFinal);
    txtCorreo.setBorder(bordeFinal);
    txtDocumento.setBorder(bordeFinal); // No olvides este

    // --- CORRECCIÓN AQUÍ ---
    // Guardamos TU borde bonito como el "original" para restaurarlo después
    this.bordeOriginal = bordeFinal;
    
}
    //Para que el sistema diferencia entre adulto, niño y bebe, creamos un metodo que muestre y oculte ciertas pestañas dependiendo la funconalidad
    public void configurarTipo(String tipo, int numero) {
    this.tipoPasajeroActual = tipo;
    
    if (tipo.equals("ADULTO")) {
        lblTituloTipo.setText("DATOS DEL ADULTO " + numero);
        txtCorreo.setVisible(true);
        
    } else if (tipo.equals("NIÑO")) {
        lblTituloTipo.setText("DATOS DEL NIÑO " + numero + " (2-11 Años)");
        // Los niños no tienen correo propio, se oculta
        txtCorreo.setVisible(false); 
        jLabel2.setVisible(false);
        
    } else if (tipo.equals("BEBÉ")) {
        lblTituloTipo.setText("DATOS DEL BEBÉ " + numero + " (0-23 Meses)");
        // Los bebés no tienen correo, se oculta
        txtCorreo.setVisible(false);
        jLabel2.setVisible(false);
    }
}
    
    public String getTipoPasajero() {
    return tipoPasajeroActual;
    }
    
    // Asegúrate de tener estos imports arriba

public modelo.Pasajero obtenerDatos() throws Exception {
    
    // 1. LIMPIEZA VISUAL (Reseteamos bordes)
    javax.swing.border.Border bordeRojo = javax.swing.BorderFactory.createLineBorder(java.awt.Color.RED, 2);
    
    txtNombre.setBorder(bordeOriginal);
    txtApellido.setBorder(bordeOriginal);
    txtFechaNac.setBorder(bordeOriginal);
    txtDocumento.setBorder(bordeOriginal);

    // 2. VALIDACIONES DE TEXTO BÁSICAS
    if (txtNombre.getText().trim().isEmpty() || txtNombre.getText().equals("Nombre:")) {
        txtNombre.setBorder(bordeRojo);
        throw new Exception("El campo 'Nombre' es obligatorio."); 
    }
    if (txtApellido.getText().trim().isEmpty() || txtApellido.getText().equals("Apellido:")) {
        txtApellido.setBorder(bordeRojo);
        throw new Exception("El campo 'Apellido' es obligatorio.");
    }
    
    // 3. VALIDACIÓN DE COMBOS (Género, Nacionalidad y Documento)
    if (cmbGenero1.getSelectedIndex() == 0) throw new Exception("Selecciona un Género.");
    if (cmbNacionalidad.getSelectedIndex() == 0) throw new Exception("Selecciona una Nacionalidad.");
    if (cmbDocumento.getSelectedIndex() == 0) {
        throw new Exception("Selecciona el Tipo de Documento.");
    }
    // Validar que escribió el número
    String docNumero = txtDocumento.getText().trim();
    if (docNumero.isEmpty() || docNumero.equals("")) { // Agrega tu placeholder si tienes
        txtDocumento.setBorder(bordeRojo);
        throw new Exception("El número de documento es obligatorio.");
    }
    
    if (txtCorreo.isVisible()) {
        String correo = txtCorreo.getText().trim();
        
        // Si está vacío 
        if (correo.isEmpty() || correo.equals("Correo Electrónico.")) {
            txtCorreo.setBorder(bordeRojo);
            throw new Exception("El correo electrónico es obligatorio.");
        }
        
        // (Opcional) Validación básica de que tenga una arroba
        if (!correo.contains("@") || !correo.contains(".")) {
             txtCorreo.setBorder(bordeRojo);
             throw new Exception("Ingresa un correo válido (ej: usuario@mail.com).");
        }
       
    }

    //FECHAS Y EDADES
    String textoFecha = txtFechaNac.getText();
    java.time.LocalDate fechaNacimiento = null;
    
    if (textoFecha.trim().isEmpty() || textoFecha.equals("- -")) {
        txtFechaNac.setBorder(bordeRojo);
        throw new Exception("La fecha es obligatoria.");
    }
    
    try {
        java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy");
        fechaNacimiento = java.time.LocalDate.parse(textoFecha, fmt);
        
        if (fechaNacimiento.isAfter(java.time.LocalDate.now())) {
             txtFechaNac.setBorder(bordeRojo);
             throw new Exception("La fecha no puede ser futura.");
        }

        int edad = java.time.Period.between(fechaNacimiento, java.time.LocalDate.now()).getYears();
        String titulo = lblTituloTipo.getText().toUpperCase(); 

        // Reglas de edad (Ajustadas a tu gusto)
        if (titulo.contains("BEBÉ") && edad > 2) {
            txtFechaNac.setBorder(bordeRojo);
            throw new Exception("Error en BEBÉ: Debe tener máximo 2 años.");
        } 
        else if (titulo.contains("NIÑO") && (edad < 2 || edad >= 12)) {
             txtFechaNac.setBorder(bordeRojo);
             throw new Exception("Error en NIÑO: Debe tener entre 2 y 11 años.");
        } 
        else if (titulo.contains("ADULTO") && edad < 12) {
             txtFechaNac.setBorder(bordeRojo);
             throw new Exception("Error en ADULTO: Debe tener 12 años o más.");
        }

    } catch (Exception e) {
        if (e.getMessage().startsWith("Error") || e.getMessage().startsWith("La fecha")) throw e; 
        txtFechaNac.setBorder(bordeRojo);
        throw new Exception("Fecha inválida (Formato: dd-mm-aaaa).");
    }

    String documentoFinal = cmbDocumento.getSelectedItem().toString() + ": " + docNumero;

    // --- LÓGICA PARA CORREO DE MENORES ---
    String correoFinal = "";

    // Preguntamos: ¿La cajita de correo se ve en la pantalla?
    if (txtCorreo.isVisible()) {
        // Si se ve (es Adulto), guardamos lo que escribió
        correoFinal = txtCorreo.getText();
    } else {
        // Si NO se ve (es Niño o Bebé), guardamos "N/A"
        correoFinal = "N/A";
    }

    // RETORNO CON EL DATO CORREGIDO
    return new modelo.Pasajero(
            txtNombre.getText(), 
            txtApellido.getText(), 
            fechaNacimiento,
            cmbGenero1.getSelectedItem().toString(), 
            cmbNacionalidad.getSelectedItem().toString(),
            documentoFinal,
            correoFinal
    );
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtUsuario = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        JPanelGen = new javax.swing.JPanel();
        txtApellido = new javax.swing.JTextField();
        cmbDocumento = new javax.swing.JComboBox<>();
        cmbNacionalidad = new javax.swing.JComboBox<>();
        txtFechaNac = new javax.swing.JFormattedTextField();
        jLabel1 = new javax.swing.JLabel();
        cmbGenero1 = new javax.swing.JComboBox<>();
        txtDocumento = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        txtCorreo = new javax.swing.JTextField();
        lblTituloTipo = new javax.swing.JLabel();

        txtUsuario.setFont(new java.awt.Font("Open Sauce One Light", 0, 12)); // NOI18N
        txtUsuario.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtUsuario.setText("Ingrese su nombre de usuario");
        txtUsuario.setBorder(null);
        txtUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtUsuarioMousePressed(evt);
            }
        });
        txtUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsuarioActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane3.setViewportView(jTextArea1);

        JPanelGen.setBackground(new java.awt.Color(255, 255, 255));

        txtApellido.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtApellido.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        txtApellido.setFont(new java.awt.Font("Open Sauce One Light", 0, 12)); // NOI18N
        txtApellido.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtApellidoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtApellidoFocusLost(evt);
            }
        });
        txtApellido.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtApellidoMousePressed(evt);
            }
        });
        txtApellido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtApellidoActionPerformed(evt);
            }
        });

        cmbDocumento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tipo de Documento...", "Cédula de Identidad", "Pasaporte", " " }));
        cmbDocumento.setFont(new java.awt.Font("Open Sauce One Light", 0, 12)); // NOI18N

        cmbNacionalidad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nacionalidad...", "Argentino", "Boliviano", "Brasileño", "Chileno", "Colombiano", "Costarricense", "Cubano", "Ecuatoriano", "Salvadoreño", "Hondureño", "Mexicano", "Nicaragüense", "Panameño" }));
        cmbNacionalidad.setFont(new java.awt.Font("Open Sauce One Light", 0, 12)); // NOI18N

        try {
            txtFechaNac.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##-##-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtFechaNac.setFont(new java.awt.Font("Open Sauce One Light", 0, 14)); // NOI18N
        txtFechaNac.setToolTipText("");
        txtFechaNac.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFechaNacActionPerformed(evt);
            }
        });

        jLabel1.setText("Fecha de Nacimiento en el formato dd-mm-aaaa");
        jLabel1.setFont(new java.awt.Font("Open Sauce One Light", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(38, 61, 141));

        cmbGenero1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Genero...", "Masculino", "Femenino", " " }));
        cmbGenero1.setFont(new java.awt.Font("Open Sauce One Light", 0, 12)); // NOI18N

        txtDocumento.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtDocumento.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        txtDocumento.setFont(new java.awt.Font("Open Sauce One Light", 0, 12)); // NOI18N
        txtDocumento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtDocumentoMousePressed(evt);
            }
        });
        txtDocumento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDocumentoActionPerformed(evt);
            }
        });

        jLabel2.setText("Información de Contacto:");
        jLabel2.setFont(new java.awt.Font("Open Sauce One Light", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(38, 61, 141));

        txtNombre.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtNombre.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        txtNombre.setFont(new java.awt.Font("Open Sauce One Light", 0, 12)); // NOI18N
        txtNombre.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNombreFocusLost(evt);
            }
        });
        txtNombre.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtNombreMousePressed(evt);
            }
        });
        txtNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreActionPerformed(evt);
            }
        });

        txtCorreo.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtCorreo.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        txtCorreo.setFont(new java.awt.Font("Open Sauce One Light", 0, 12)); // NOI18N
        txtCorreo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCorreoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCorreoFocusLost(evt);
            }
        });
        txtCorreo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtCorreoMousePressed(evt);
            }
        });
        txtCorreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCorreoActionPerformed(evt);
            }
        });

        lblTituloTipo.setText("Datos del Pasajero");
        lblTituloTipo.setFont(new java.awt.Font("Open Sauce One ExtraBold", 0, 24)); // NOI18N
        lblTituloTipo.setForeground(new java.awt.Color(38, 61, 141));

        javax.swing.GroupLayout JPanelGenLayout = new javax.swing.GroupLayout(JPanelGen);
        JPanelGen.setLayout(JPanelGenLayout);
        JPanelGenLayout.setHorizontalGroup(
            JPanelGenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelGenLayout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addGroup(JPanelGenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, JPanelGenLayout.createSequentialGroup()
                        .addGroup(JPanelGenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblTituloTipo, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, JPanelGenLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(JPanelGenLayout.createSequentialGroup()
                        .addGroup(JPanelGenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtNombre)
                            .addComponent(cmbGenero1, 0, 179, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(JPanelGenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtApellido)
                            .addComponent(cmbNacionalidad, 0, 179, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, JPanelGenLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(JPanelGenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtCorreo, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, JPanelGenLayout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(JPanelGenLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(cmbDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtFechaNac, javax.swing.GroupLayout.Alignment.LEADING))))
                .addGap(67, 67, 67))
        );
        JPanelGenLayout.setVerticalGroup(
            JPanelGenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelGenLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(lblTituloTipo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(JPanelGenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(JPanelGenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbGenero1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbNacionalidad, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFechaNac, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(JPanelGenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmbDocumento, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                    .addComponent(txtDocumento))
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(109, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(JPanelGen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(JPanelGen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtUsuarioMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtUsuarioMousePressed
       
    }//GEN-LAST:event_txtUsuarioMousePressed

    private void txtUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsuarioActionPerformed

    private void txtApellidoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtApellidoMousePressed
        
    }//GEN-LAST:event_txtApellidoMousePressed

    private void txtApellidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtApellidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtApellidoActionPerformed

    private void txtDocumentoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDocumentoMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDocumentoMousePressed

    private void txtDocumentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDocumentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDocumentoActionPerformed

    private void txtNombreMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNombreMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreMousePressed

    private void txtNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreActionPerformed

    private void txtCorreoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCorreoMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreoMousePressed

    private void txtCorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCorreoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreoActionPerformed

    private void txtNombreFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreFocusGained
       if (txtNombre.getText().equals("Nombre:")) {
        txtNombre.setText("");
        txtNombre.setForeground(java.awt.Color.BLACK); 
    }
    }//GEN-LAST:event_txtNombreFocusGained

    private void txtNombreFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreFocusLost
        if (txtNombre.getText().isEmpty()) {
        txtNombre.setForeground(new java.awt.Color(153, 153, 153));
        txtNombre.setText("Nombre:");
    }
    }//GEN-LAST:event_txtNombreFocusLost

    private void txtApellidoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtApellidoFocusGained
    if (txtApellido.getText().equals("Apellido:")) {
        txtApellido.setText("");
        txtApellido.setForeground(java.awt.Color.BLACK);
    }
    }//GEN-LAST:event_txtApellidoFocusGained

    private void txtApellidoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtApellidoFocusLost
        if (txtApellido.getText().isEmpty()) {
        txtApellido.setForeground(new java.awt.Color(153, 153, 153));
        txtApellido.setText("Apellido:");
    }
    }//GEN-LAST:event_txtApellidoFocusLost

    private void txtCorreoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCorreoFocusGained
        if (txtCorreo.getText().equals("Correo Electrónico.")) {
        txtCorreo.setText("");
        txtCorreo.setForeground(java.awt.Color.BLACK);
        }
    }//GEN-LAST:event_txtCorreoFocusGained

    private void txtCorreoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCorreoFocusLost
        if (txtCorreo.getText().isEmpty()) {
        txtCorreo.setForeground(new java.awt.Color(153, 153, 153)); // Gris
        txtCorreo.setText("Correo Electrónico."); // Restaura el texto
        }
    }//GEN-LAST:event_txtCorreoFocusLost

    private void txtFechaNacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFechaNacActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFechaNacActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel JPanelGen;
    private javax.swing.JComboBox<String> cmbDocumento;
    private javax.swing.JComboBox<String> cmbGenero1;
    private javax.swing.JComboBox<String> cmbNacionalidad;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel lblTituloTipo;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtDocumento;
    private javax.swing.JFormattedTextField txtFechaNac;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
