/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flexisim.views;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author harshit
 */
public class FilePanel extends JTextArea implements DocumentListener {

    private boolean changed=false;

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public boolean isChanged() {
        return changed;
    }

    public void AddDocListener() {
        this.getDocument().addDocumentListener(this);
    }

    public void insertUpdate(DocumentEvent e) {
        changed = true;
        NewProject.obpd.setSaved(false);
    }

    public void removeUpdate(DocumentEvent e) {
        changed = true;
        NewProject.obpd.setSaved(false);
    }

    public void changedUpdate(DocumentEvent e) {
        changed = true;
        NewProject.obpd.setSaved(false);
    }
}
