
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author JP
 */
public class Cliente extends javax.swing.JFrame {

    /**
     * Creates new form Cliente
     */
    public Cliente() {
        initComponents();
    }

    private static SSLSocket clienteSocket;
    static SSLSocketFactory socketFactory;
    static DataInputStream entrada;
    static DataOutputStream salida;

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtarea = new javax.swing.JTextArea();
        txtfield = new javax.swing.JTextField();
        bsend = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txtarea.setColumns(20);
        txtarea.setRows(5);
        jScrollPane1.setViewportView(txtarea);

        bsend.setText("ENVIAR");
        bsend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bsendActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtfield, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bsend, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtfield)
                    .addComponent(bsend, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bsendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bsendActionPerformed
        try {
            String msg = "";//creamos unha variable String chamada msg,na cal se gardan as mensaxes que se van a enviar

            msg = txtfield.getText().trim();//Recollo o que hai no textfield,formateo os posibles espazos en branco

            salida.writeUTF(msg);//Usei DataOutput Stream para a escritura,para asi poder escribir Strings dun tiron con writeUTF ,sen ter que recoller primeiro byte a byte

            txtarea.append("\n Cliente :" + txtfield.getText());//Envio o textarea a mensaxe que envio,co nome do que a envia
            
            salida.flush();//Limpio o fluxo de escritura
            txtfield.setText("");//Fago que o textfield volva a quedar limpo para enviar a proxima mensaxe

            if (msg.equalsIgnoreCase("exit")) {//Fago u nif para que si a mensaxe e igual a exit cerre o socket do cliente e asi non haxa comunicacion

                clienteSocket.close();

                JOptionPane.showMessageDialog(bsend, "cerrado");//Con un JOptionPane alerto de que se cerrou o socket do cliente

            }

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bsendActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Cliente().setVisible(true);
            }
        });

        /*
        javax.net.ssl.keyStore para indicar o almacén donde esta o certificado que nos identifica.
        javax.net.ssl.keyStorePassword A clave para acceder o almacen e para acceder o certificado dentro de ese almacen,Este e o motivo polo cal as claves deben ser iguais
        javax.net.ssl.trustStore para indicar o almacén onde estan os certificados.
        javax.net.ssl.trustStorePassword A clave para acceder ao almacén e certificados dentro deste.*/
        String msgin = "";
        System.setProperty("javax.net.ssl.keystore", "clientKey.jks");
        System.setProperty("javax.net.ssl.trustStore", "clientTrustedCerts.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "servpass");
        System.setProperty("javax.net.ssl.trustStorePassword", "servpass");
        
        JOptionPane.showMessageDialog(txtarea, "PARA CERRAR A CONEXION DENDE O CLIENTE, ESCRIBE A PALABRA exit ");

        try {
            socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            System.out.println("Creando socket cliente");
            clienteSocket = (SSLSocket) socketFactory.createSocket();//Crreo o socket cliente
            System.out.println("Estableciendo la conexion");

            InetSocketAddress direc = new InetSocketAddress("localhost", 6000);//Indico a dioreccion e o porto
            clienteSocket.connect(direc);

            //Creo os fluxos de entrada e saida de datos
            entrada = new DataInputStream(clienteSocket.getInputStream());
            salida = new DataOutputStream(clienteSocket.getOutputStream());

            while (true) {//dentro de un bucle while,e mentras a condicion sexa true,leo nunha variable String chamada msgin as mensaxes, e as almaceno

                msgin = entrada.readUTF();

                txtarea.setText(txtarea.getText().trim() + "\n Servidor :" + msgin);//Imprimo no textarea aas mensaxes,co usuario que a escribiu,e  formateo os posibles espazos en branco

            }

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bsend;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private static javax.swing.JTextArea txtarea;
    private javax.swing.JTextField txtfield;
    // End of variables declaration//GEN-END:variables
}
