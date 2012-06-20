/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flexisim.views;

/**
 *
 * @author harshit
 */
import java.io.*;

public class CopyDirectory {

    public void copyDirectory(File srcPath, File dstPath)
            throws IOException {
        if (srcPath.isDirectory()) {
            if (!dstPath.exists()) {
                dstPath.mkdir();
            }
            String files[] = srcPath.list();
            for (int i = 0; i < files.length; i++) {
                copyDirectory(new File(srcPath, files[i]),
                        new File(dstPath, files[i]));
            }
        } else {
            if (!srcPath.exists()) {
                System.out.println("File or directory does not exist.");
            } else {
                if (accept(srcPath)) {
                    InputStream in = new FileInputStream(srcPath);
                    OutputStream out = new FileOutputStream(dstPath);
                    // Transfer bytes from in to out
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    NewProject.obpd.setModifyprotocoltab(true, NewProject.obpd.getI());
                    FlexiSimView.tabCreater(srcPath.getName());
                    NewProject.obpd.setProtocolFiles(srcPath.getName(), fileCount, true);
                    NewProject.obpd.setOpenTabsPath(dstPath.getPath(), fileCount);
                    FileReader fr = new FileReader(srcPath);
                    BufferedReader br = new BufferedReader(fr);
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        FlexiSimView.tabs[NewProject.obpd.getI() - 1].append(line + "\n");
                    }
                    if (fileCount == 0) {
                        FlexiSimView.fileTabbedPane.setSelectedIndex(1);
                    }
                    FlexiSimView.tabs[NewProject.obpd.getI() - 1].AddDocListener();
                    br.close();
                    in.close();
                    out.close();
                    fileCount++;
                }
            }
        }
    }
    String extensions[] = new String[]{"h", "cc","tcl"};

    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        } else {
            String path = file.getAbsolutePath().toLowerCase();
            for (int i = 0, n = extensions.length; i < n; i++) {
                String extension = extensions[i];
                if ((path.endsWith(extension) && (path.charAt(path.length() - extension.length() - 1)) == '.')) {
                    return true;
                }
            }
        }
        return false;
    }
    private int fileCount = 0;
}
