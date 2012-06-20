/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flexisim.routing.protocols;

import java.io.File;
import java.io.Serializable;

/**
 *
 * @author harshit
 */
public class NewProtocolData implements Serializable {

    String protocolName;
    String protocolFileName[] = new String[10];
    int fileCount = 0;

    public int getFileCount() {
        return fileCount;
    }

    public void setFileCount(int fileCount) {
        this.fileCount = fileCount;
    }

    public String getProtocolFileName(int n) {
        return protocolFileName[n];
    }

    public void setProtocolFileName(String protocolFileName, int n) {
        this.protocolFileName[n] = protocolFileName;
        if (fileCount <= n) {
            fileCount++;
        }
    }

    public File getProtocolFile() {
        return protocolFile;
    }

    public void setProtocolFile(File protocolFile) {
        this.protocolFile = protocolFile;
    }
    File protocolFile;

    public String getProtocolName() {
        return protocolName;
    }

    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }
}
