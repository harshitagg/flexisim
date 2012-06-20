/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flexisim.link;

import java.io.Serializable;
import flexisim.views.FlexiSimView;

/**
 *
 * @author harshit
 */
public class LinkManager implements Serializable{

    private String node1[] = new String[50];
    private String node2[] = new String[50];
    private String link[] = new String[50];
    private int i;

    public LinkManager() {
        i=-1;
    }

    public String getLink(int n){
        return link[n];
    }

    public int linkNum(){
        return i+1;
    }
    
    public String getNode1(int n) {
        return node1[n];
    }

    public void setNode1(String node1,int n) {
        this.node1[n] = node1;
    }

    public String getNode2(int n) {
        return node2[n];
    }

    public void setNode2(String node2,int n) {
        this.node2[n] = node2;
    }

    public void addLink(String node1, String node2) {
        i += 1;
        if (i == 0) {
            FlexiSimView.newAgentMenuItem.setEnabled(true);
            FlexiSimView.eventsMenuItem.setEnabled(true);
            FlexiSimView.runMenuItem.setEnabled(true);
        }
        this.node1[i] = node1;
        this.node2[i] = node2;
    }

    public int checkRepeat(String n1, String n2) {
        for (int j = i - 1; j >= 0 && i > 0; j--) {
            if ((n1.equals(node1[j]) && n2.equals(node2[j])) || (n1.equals(node2[j]) && n2.equals(node1[j]))) {
                return 1;
            }
        }

        return 0;
    }

    public int checkLink(String asnode, String adnode) {
        for (int j = i; j >= 0; j--) {
            if ((asnode.equals(node1[j]) && adnode.equals(node2[j])) || (asnode.equals(node2[j]) && adnode.equals(node1[j]))) {
                return 1;
            }
        }
        return 0;
    }

    public String[] showLink() {
        for (int j = i; j >= 0; j--) {
            link[j] = (node1[j] + "-" + node2[j]);
        }
        return link;
    }
}
