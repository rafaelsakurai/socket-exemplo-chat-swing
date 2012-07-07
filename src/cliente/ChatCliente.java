package cliente;



import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JFrame;

/**
 *
 * @author Rafael Guimarães Sakurai
 */
public class ChatCliente extends JFrame implements Runnable {
    private static final long serialVersionUID = 7807451284291881701L;

    public ChatCliente(Socket socket) throws IOException {
        initComponents();

        this.socket = socket;
        this.out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        this.in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        this.listener = new Thread(this);
        this.listener.start();
        this.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnEnviar = new javax.swing.JButton();
        entrada = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        saida = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chat Socket");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        btnEnviar.setText("Enviar");
        btnEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarActionPerformed(evt);
            }
        });

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setAutoscrolls(true);

        saida.setColumns(20);
        saida.setLineWrap(true);
        saida.setRows(5);
        jScrollPane1.setViewportView(saida);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(entrada, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEnviar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEnviar)
                    .addComponent(entrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarActionPerformed
        try {
            if(this.entrada.getText().trim().length() > 0) {
                out.writeUTF(this.entrada.getText());
                out.flush();
                this.entrada.setText(null);
            }

            this.entrada.requestFocus();
        } catch (Exception ex) {
            ex.printStackTrace();
            sair();
        }
}//GEN-LAST:event_btnEnviarActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        sair();
    }//GEN-LAST:event_formWindowClosing

    public void run() {
        try {
            while(true) {
                String msg = this.in.readUTF();
                this.saida.append(msg);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void sair() {
        try {
            out.writeUTF("SAIR");
            out.flush();
            if(!this.socket.isClosed()) {
                this.socket.close();
            }

            if(listener != null) {
                listener.interrupt();
                listener = null;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Não conseguiu fechar o socket.");
        }
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        Socket socket = null;
        try {
            //TODO - TROCAR localhost por outro IP da rede.
            socket = new Socket("localhost", 9876);
            new ChatCliente(socket);
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
            System.out.println("Não encontrou o host servidor.");
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Não conseguiu abrir conexão com o host.");
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEnviar;
    private javax.swing.JTextField entrada;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea saida;
    // End of variables declaration//GEN-END:variables

    private DataInputStream in;
    private DataOutputStream out;
    private Socket socket;
    private Thread listener;
}