/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flexisim.node.position;

import java.io.Serializable;

/**
 *
 * @author harshit
 */
public class NodePositionData implements Serializable {

    private double[] xp = new double[200];
    private double[] yp = new double[200];
    private double[] zp = new double[200];
    private boolean[] random = new boolean[200];

    public boolean getRandom(int n) {
        return random[n];
    }

    public void setRandom(boolean random, int n) {
        this.random[n] = random;
    }

    public boolean isRandomAll() {
        return randomAll;
    }

    public void setRandomAll(boolean randomAll) {
        this.randomAll = randomAll;
    }
    private boolean randomAll;

    public double getXp(int i) {
        return xp[i];
    }

    public void setXp(double xp, int i) {
        this.xp[i] = xp;
    }

    public double getYp(int i) {
        return yp[i];
    }

    public void setYp(double yp, int i) {
        this.yp[i] = yp;
    }

    public double getZp(int i) {
        return zp[i];
    }

    public void setZp(double zp, int i) {
        this.zp[i] = zp;
    }

    public void NodePositionData() {
        for (int i = 0; i < 200; i++) {
            xp[i] = yp[i] = zp[i] = 0;
        }
    }
}
