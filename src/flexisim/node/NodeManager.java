/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flexisim.node;

import java.io.Serializable;
import flexisim.views.FlexiSimView;
import flexisim.views.NewProject;

/**
 *
 * @author harshit
 */
public class NodeManager implements Serializable {

    private int i;

    public NodeManager() {
        i = -1;
    }
    private String nodename[] = new String[200];

    public String getNodename(int i) {
        return nodename[i];
    }

    public void setNodename(String nodename, int i) {
        this.nodename[i] = nodename;
    }

    public int nodeNum() {
        return i + 1;
    }

    public void addNode(String node) {
        i += 1;
        if (i == 1 && NewProject.obpd.getNettype() == 0) {
            FlexiSimView.newLinkMenuItem.setEnabled(true);
            FlexiSimView.nodeMenuItem.setEnabled(true);
        }
        if (i == 0 && NewProject.obpd.getNettype() == 1) {
            FlexiSimView.nodeMenuItem.setEnabled(true);
            FlexiSimView.nodePositionMenuItem.setEnabled(true);
            FlexiSimView.newAgentMenuItem.setEnabled(true);
            FlexiSimView.runMenuItem.setEnabled(true);
            FlexiSimView.topographyMenuItem.setEnabled(true);
        }
        nodename[i] = node;
    }

    public int checkNode(String node) {
        for (int j = i; j >= 0; j--) {
            if (nodename[j].equals(node)) {
                return 1;
            }
        }
        return 0;
    }
}
