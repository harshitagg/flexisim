/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flexisim.views;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.SwingWorker;

/**
 *
 * @author harshit
 */
public class OpenTraceGraph extends SwingWorker<Void, CopyData> {

    @Override
    protected Void doInBackground() throws Exception {
        File current = new File(NewProject.obpd.getFile().getParent());
        openTraceFile = new JFileChooser();
        openTraceFile.setCurrentDirectory(current);
        ExtensionFileFilter filterTr = new ExtensionFileFilter(".tr", new String[]{"tr"});
        openTraceFile.setFileFilter(filterTr);
        openTraceFile.setDialogTitle("Select trace file");
        openTraceFile.setAcceptAllFileFilterUsed(false);
        openTraceFile.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (openTraceFile.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File f = openTraceFile.getSelectedFile();
            String envp[] = new String[]{System.getenv("LD_LIBRARY_PATH"),"DISPLAY=:0.0"};
            try {
                String[] command = new String[]{System.getenv("TRGRAPH") + "trgraph", f.getPath()};
                Process child = Runtime.getRuntime().exec(command, envp,new File(System.getenv("TRGRAPH")));
                DataInputStream data = new DataInputStream(child.getInputStream());
                System.out.println("After exec");
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
                FlexiSimView.runTextArea.append("Running TraceGraph" + " \n");
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
        return null;
    }
    private JFileChooser openTraceFile;
}
