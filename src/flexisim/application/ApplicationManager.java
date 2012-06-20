/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package flexisim.application;

import java.io.Serializable;

/**
 *
 * @author harshit
 */
public class ApplicationManager implements Serializable {
    int k;

    public ApplicationManager() {
        k=-1;
    }

    private String applname[] = new String[50];
    private String apptype[]=new String[50];

    public String[] getApptype() {
        return apptype;
    }

    public String[] getApplname() {
        return applname;
    }

    public void addApp(String appname,String apptype) {
        k += 1;
        this.applname[k] = appname;
        this.apptype[k]=apptype;
    }
}
