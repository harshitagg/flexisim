/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flexisim.views;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import javax.swing.SwingWorker;

/**
 *
 * @author harshit
 */
public class MakeNS extends SwingWorker<Void, CopyData> {

    public void makeClean() {
        String path = NewProject.obpd.getNsInstallationPath();
        try {
            String[] command = new String[]{"/usr/bin/make", "-f", path + "/Makefile", "-k", "clean"};
            Process child = Runtime.getRuntime().exec(command, null, new File(NewProject.obpd.getNsInstallationPath()));
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
            FlexiSimView.runTextArea.append("Cleaning Make" + " \n");
            if (t.equals("")) {
                s += "\nNormal Termination.";
                FlexiSimView.runTextArea.append(s + "\n");
            } else {
                FlexiSimView.runTextArea.append(t + "\n");
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void make() {
        String path = NewProject.obpd.getNsInstallationPath();
        try {
            String[] command = new String[]{"/usr/bin/make", "-f", path + "/Makefile", "-k"};
            Process child = Runtime.getRuntime().exec(command, null, new File(NewProject.obpd.getNsInstallationPath()));
            DataInputStream data = new DataInputStream(child.getInputStream());
            DataInputStream data_data = new DataInputStream(child.getErrorStream());
            String s = "", t = "";
            int ch;
            while ((ch = data.read()) != -1) {
                s = s + (char) ch;
                FlexiSimView.runTextArea.append(s);
                s="";
            }
            data.close();
            while ((ch = data_data.read()) != -1) {
                t = t + (char) ch;
            }
            data_data.close();
            FlexiSimView.statusTabbedPane.setSelectedIndex(1);
            FlexiSimView.runTextArea.append("Making NS" + " \n");
            if (t.equals("")) {
                s += "\nNormal Termination.";
                FlexiSimView.runTextArea.append(s + "\n");
            } else {
                FlexiSimView.runTextArea.append(t + "\n");
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    @Override
    protected Void doInBackground() throws Exception {
        int progress = 0;
        setProgress(0);
        makeClean();
        make();
        return null;
    }

    @Override
    public void done() {
        FlexiSimView.statusTextArea.append("Making & Cleaning Complete\n");
        CopyFiles.makensdialog.dispose();
    }
}
