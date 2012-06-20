/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flexisim.event;

import java.io.Serializable;

/**
 *
 * @author harshit
 */
public class EventData implements Serializable {

    private String linkEntered[] = new String[50];
    private String agentEntered[] = new String[50];
    private String event[] = new String[50];
    private Number upValue[] = new Number[50];
    private Number downValue[] = new Number[50];
    private Number deattach[] = new Number[50];
    private int index[] = new int[50];
    private int i = -1, k = -1, te = 0;

    public String getAgentEntered(int n) {
        return agentEntered[n];
    }

    public void setAgentEntered(String agentEntered, int n) {
        this.agentEntered[n] = agentEntered;
    }

    public Number getDeattach(int n) {
        return deattach[n];
    }

    public void setDeattach(Number deattach, int n) {
        this.deattach[n] = deattach;
    }

    public Number getDownValue(int n) {
        return downValue[n];
    }

    public void setDownValue(Number downValue, int n) {
        this.downValue[n] = downValue;
    }

    public int[] getIndex() {
        return index;
    }

    public void setIndex(int index, int n) {
        this.index[n] = index;
    }

    public String getLinkEntered(int n) {
        return linkEntered[n];
    }

    public void setLinkEntered(String linkEntered, int n) {
        this.linkEntered[n] = linkEntered;
    }

    public String[] getEvent() {
        return event;
    }

    public String getEvent(int n) {
        return event[n];
    }

    public void setEvent(String event) {
        this.event[te] = event;
        this.te++;
    }

    public Number getUpValue(int n) {
        return upValue[n];
    }

    public void setUpValue(Number upValue, int n) {
        this.upValue[n] = upValue;
    }

    public void increamentI() {
        i++;
    }

    public int getI() {
        return i;
    }

    public void increamentK() {
        k++;
    }

    public int getK() {
        return k;
    }
}
