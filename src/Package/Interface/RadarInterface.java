/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Package.Interface;

import Package.Sockets.Radar;
import Package.Commun.Fonction;

/**
 *
 * @author Cynthia Abou Maroun
 */

public class RadarInterface extends javax.swing.JFrame 
{

    /**
     * Creates new form RadarInterface
     */
    Radar _Radar = null;
    Fonction _Fonction;
    
    public RadarInterface() 
    {
        initComponents();
        this.setTitle("Interface: Radar");
        this.setSize(1800, 1200);
        this.setLocationRelativeTo(null);
        _Radar = new Radar(this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ScrollPane1 = new javax.swing.JScrollPane();
        TextArea = new javax.swing.JTextArea();
        Separator1 = new javax.swing.JSeparator();
        ScrollPane2 = new javax.swing.JScrollPane();
        ListeVol = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        TextArea.setEditable(false);
        TextArea.setColumns(21);
        TextArea.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        TextArea.setForeground(new java.awt.Color(204, 204, 255));
        TextArea.setRows(6);
        TextArea.setTabSize(9);
        TextArea.setToolTipText(" ");
        ScrollPane1.setViewportView(TextArea);

        ListeVol.setForeground(new java.awt.Color(204, 204, 255));
        ListeVol.setToolTipText(" ");
        ListeVol.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                ListeVolValueChanged(evt);
            }
        });
        ScrollPane2.setViewportView(ListeVol);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1050, Short.MAX_VALUE)
                    .addComponent(Separator1)
                    .addComponent(ScrollPane2))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Separator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        if (_Radar != null)
        {
            _Fonction.CloseConnexion();
        }
    }//GEN-LAST:event_formWindowClosing

    private void ListeVolValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_ListeVolValueChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_ListeVolValueChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) 
    {
     /* Set the Nimbus look and feel */
     //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
     /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
      * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
      */
        try 
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) 
            {
                if ("Nimbus".equals(info.getName())) 
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } 
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException e) 
        {
            System.out.println(e);
            System.out.println("Java L&F: Look and Feel Exceptiom error!!!");
            System.out.println(ControleurInterface.class.getName());
        }
        //</editor-fold>

        /* Create and display the form */
        
        java.awt.EventQueue.invokeLater(() -> {new RadarInterface().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JList<String> ListeVol;
    private javax.swing.JScrollPane ScrollPane1;
    private javax.swing.JScrollPane ScrollPane2;
    private javax.swing.JSeparator Separator1;
    public javax.swing.JTextArea TextArea;
    // End of variables declaration//GEN-END:variables
}
