/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * CompileFile.java
 *
 * Created on Jan 21, 2011, 6:38:18 AM
 */
package flexisim.views;

import java.io.DataInputStream;
import java.io.IOException;

/**
 *
 * @author harshit
 */
public class CompileFile extends javax.swing.JDialog {

    /** Creates new form CompileFile */
    public CompileFile(java.awt.Frame parent) {
        super(parent);
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileLabel = new javax.swing.JLabel();
        fileSelectComboBox = new javax.swing.JComboBox();
        compileButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(flexisim.views.FlexiSimApp.class).getContext().getResourceMap(CompileFile.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N
        setResizable(false);

        fileLabel.setText(resourceMap.getString("fileLabel.text")); // NOI18N
        fileLabel.setName("fileLabel"); // NOI18N

        fileSelectComboBox.setModel(new javax.swing.DefaultComboBoxModel());
        for(int i=0;i<NewProject.obpd.getProtocolFileCounter();i++)
        {
            if(NewProject.obpd.getProtocolFiles(i)!=null)
            fileSelectComboBox.addItem(NewProject.obpd.getProtocolFiles(i));
        }
        fileSelectComboBox.setName("fileSelectComboBox"); // NOI18N

        compileButton.setText(resourceMap.getString("compileButton.text")); // NOI18N
        compileButton.setName("compileButton"); // NOI18N
        compileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compileButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileLabel)
                .addGap(18, 18, 18)
                .addComponent(fileSelectComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(86, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(201, Short.MAX_VALUE)
                .addComponent(compileButton)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fileLabel)
                    .addComponent(fileSelectComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addComponent(compileButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void compileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_compileButtonActionPerformed
        String file;
        String path;
        file = (String) fileSelectComboBox.getSelectedItem();
        if (NewProject.obpd.getModifiedProtocol(fileSelectComboBox.getSelectedIndex())) {
            path = NewProject.obpd.getFile().getParent() + "/" + NewProject.obpd.getProtocolFile().getName() + "/" + file;
        } else {
            path = NewProject.obprd.getProtocolFile().getParent() + "/" + file;
        }
        try {
            String[] command = new String[]{"/usr/bin/g++", path};
            Process child = Runtime.getRuntime().exec(command, null);
            DataInputStream data = new DataInputStream(child.getInputStream());
            DataInputStream data_data = new DataInputStream(child.getErrorStream());
            String s = "", t = "";
            int ch;
            while ((ch = data.read()) != -1) {
                s = s + (char) ch;
            }
            data.close();
            while ((ch = data_data.read()) != -1) {
                t = t + (char) ch;
            }
            data_data.close();
            FlexiSimView.statusTabbedPane.setSelectedIndex(1);
            FlexiSimView.runTextArea.append("Comipiling File " + file + " --------------------" + " \n");
            if (t.equals("")) {
                s += "\nNormal Termination.";
                FlexiSimView.runTextArea.append(s + "\n");
            } else {
                FlexiSimView.runTextArea.append(t + "\n");
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        dispose();
    }//GEN-LAST:event_compileButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                CompileFile dialog = new CompileFile(new javax.swing.JFrame());
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton compileButton;
    private javax.swing.JLabel fileLabel;
    private javax.swing.JComboBox fileSelectComboBox;
    // End of variables declaration//GEN-END:variables
}
