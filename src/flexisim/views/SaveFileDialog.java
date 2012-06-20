/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SaveFileDialog.java
 *
 * Created on Jan 29, 2011, 2:42:18 AM
 */
package flexisim.views;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author harshit
 */
public class SaveFileDialog extends javax.swing.JDialog {

    /** Creates new form SaveFileDialog */
    public SaveFileDialog(java.awt.Frame parent, int tabIndex, String tabName) {
        super(parent);
        initComponents();
        tabName = this.tabName;
        tabIndex = this.tabIndex;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileLabel = new javax.swing.JLabel();
        cancelButton = new javax.swing.JButton();
        dontSaveButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(flexisim.views.FlexiSimApp.class).getContext().getResourceMap(SaveFileDialog.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N
        setResizable(false);

        fileLabel.setText("File "+tabName+" has been modified.");
        fileLabel.setName("fileLabel"); // NOI18N

        cancelButton.setText(resourceMap.getString("cancelButton.text")); // NOI18N
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        dontSaveButton.setText(resourceMap.getString("dontSaveButton.text")); // NOI18N
        dontSaveButton.setName("dontSaveButton"); // NOI18N
        dontSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dontSaveButtonActionPerformed(evt);
            }
        });

        saveButton.setText(resourceMap.getString("saveButton.text")); // NOI18N
        saveButton.setName("saveButton"); // NOI18N
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileLabel)
                .addContainerGap(262, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(87, Short.MAX_VALUE)
                .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(dontSaveButton)
                .addGap(12, 12, 12)
                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveButton)
                    .addComponent(cancelButton)
                    .addComponent(dontSaveButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        ClosableTabbedPane.closeTab = false;
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        String content = FlexiSimView.tabs[tabIndex - 1].getText();
        File file1;
        try {
            if (NewProject.obpd.getModifyprotocoltab(tabIndex - 1)) {
                file1 = new File(NewProject.obpd.getFile().getParent() + "//" + NewProject.obpd.getProtocolFile().getName() + "//" + NewProject.obpd.getOpentabsname(tabIndex - 1));
            } else {
                file1 = new File(NewProject.obprd.getProtocolFile().getParent() + "//" + NewProject.obpd.getOpentabsname(tabIndex - 1));
            }
            FileOutputStream fop = new FileOutputStream(file1);
            if (file1.createNewFile()) {
                byte bufn[] = new byte[content.length()];
                bufn = content.getBytes();
                fop.write(bufn);
                fop.close();
            } else {
                FileOutputStream erasor = new FileOutputStream(file1);
                erasor.write((new String()).getBytes());
                erasor.close();
                byte bufn[] = new byte[content.length()];
                bufn = content.getBytes();
                fop.write(bufn);
                fop.close();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        ClosableTabbedPane.closeTab = true;
        dispose();
    }//GEN-LAST:event_saveButtonActionPerformed

    private void dontSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dontSaveButtonActionPerformed
        ClosableTabbedPane.closeTab = true;
        dispose();
    }//GEN-LAST:event_dontSaveButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                SaveFileDialog dialog = new SaveFileDialog(new javax.swing.JFrame(), -1, "0");
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton dontSaveButton;
    private javax.swing.JLabel fileLabel;
    private javax.swing.JButton saveButton;
    // End of variables declaration//GEN-END:variables
    private String tabName;
    private int tabIndex;
}
