/*
 * Run.java
 *
 * Created on Sep 26, 2010, 4:12:30 AM
 */
package flexisim.views;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import flexisim.node.NewNode;
import java.text.DecimalFormat;

/**
 *
 * @author harshit
 */
public class Run extends javax.swing.JDialog {

    private String runOutput;

    /** Creates new form Run */
    public Run(java.awt.Frame parent) {
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

        fTimeLabel = new javax.swing.JLabel();
        fTimeFormattedTextField = new javax.swing.JFormattedTextField(new DecimalFormat("#.00"));
        jLabel2 = new javax.swing.JLabel();
        runButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(flexisim.views.FlexiSimApp.class).getContext().getResourceMap(Run.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N
        setResizable(false);

        fTimeLabel.setText(resourceMap.getString("fTimeLabel.text")); // NOI18N
        fTimeLabel.setName("fTimeLabel"); // NOI18N

        fTimeFormattedTextField.setText(resourceMap.getString("fTimeFormattedTextField.text")); // NOI18N
        fTimeFormattedTextField.setName("fTimeFormattedTextField"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        runButton.setText(resourceMap.getString("runButton.text")); // NOI18N
        runButton.setName("runButton"); // NOI18N
        getRootPane().setDefaultButton(runButton);
        runButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fTimeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fTimeFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel2)
                .addContainerGap(26, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(140, Short.MAX_VALUE)
                .addComponent(runButton)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fTimeLabel)
                    .addComponent(fTimeFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addComponent(runButton)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void runButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runButtonActionPerformed
        // TODO add your handling code here:
        NewNode.obj.clearRunWrite();
        if (NewProject.obpd.getNettype() == 1) {
            NewNode.obj.clearGodWrite();
            NewNode.obj.godWrite();
        }
        Number temp;
        temp = (Number) fTimeFormattedTextField.getValue();
        NewNode.obj.runWrite(temp.doubleValue());
        try {
            String[] command = new String[]{System.getenv("NS")+"ns", NewProject.obpd.getFilename().toString()};
            System.out.printf(command[0]);
            Process child = Runtime.getRuntime().exec(command, null);
            InputStream in = child.getInputStream();

            if (in != null) {
                Writer writer = new StringWriter();
                char[] buffer = new char[1024];

                Reader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
                runOutput = writer.toString();
                FlexiSimView.statusTabbedPane.setSelectedIndex(1);
                FlexiSimView.runTextArea.append(runOutput + "\n");
            }
            in.close();
        } catch (IOException e) {
            System.err.println(e);
        }
        FlexiSimView.codeTextAreaWrite();
        dispose();
    }//GEN-LAST:event_runButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                Run dialog = new Run(new javax.swing.JFrame());
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
    private javax.swing.JFormattedTextField fTimeFormattedTextField;
    private javax.swing.JLabel fTimeLabel;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton runButton;
    // End of variables declaration//GEN-END:variables
}