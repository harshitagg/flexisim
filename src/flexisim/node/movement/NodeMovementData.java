/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flexisim.node.movement;

import java.io.Serializable;

/**
 *
 * @author harshit
 */
public class NodeMovementData implements Serializable {

    private double[] xp = new double[200];
    private double[] yp = new double[200];
    private double[] timep = new double[200];
    private double[] speedp = new double[200];
    private boolean[] randomp = new boolean[200];
    private boolean randomAll;

    public boolean isRandomAll() {
        return randomAll;
    }

    public void setRandomAll(boolean randomAll) {
        this.randomAll = randomAll;
    }

    public boolean getRandomp(int i) {
        return randomp[i];
    }

    public void setRandomp(boolean randomp,int i) {
        this.randomp[i] = randomp;
    }

    public double getSpeedp(int i) {
        return speedp[i];
    }

    public void setSpeedp(double speedp,int i) {
        this.speedp[i] = speedp;
    }

    public double getTimep(int i) {
        return timep[i];
    }

    public void setTimep(double timep,int i) {
        this.timep[i] = timep;
    }

    public double getXp(int i) {
        return xp[i];
    }

    public void setXp(double xp,int i) {
        this.xp[i] = xp;
    }

    public double getYp(int i) {
        return yp[i];
    }

    public void setYp(double yp,int i) {
        this.yp[i] = yp;
    }

    public void NodeMovementData() {
        for (int i = 0; i < 200; i++) {
            xp[i] = yp[i] = timep[i] = speedp[i] = 0;
            randomp[i] = false;
        }
    }
}
