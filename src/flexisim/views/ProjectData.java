package flexisim.views;

import java.io.File;
import java.io.Serializable;

/**
 *
 * @author harshit
 */
public class ProjectData implements Serializable {

    public File getProtocolFile() {
        return protocolFile;
    }

    public void setProtocolFile(File protocolFile) {
        this.protocolFile = protocolFile;
    }
    private File filename;
    private File file;
    private int nettype, logindex = 0;
    private String log[] = new String[100];
    private File protocolFile;
    private boolean modifyprotocoltab[] = new boolean[50];
    private String nsInstallationPath = System.getenv("NS");
    private String protocolFiles[] = new String[50];
    private int protocolFileCounter = 0;
    boolean modifiedProtocol[] = new boolean[50];
    private boolean saved;
    private boolean openModified;
    private int topoX=500,topoY=500;

    public int getTopoX() {
        return topoX;
    }

    public void setTopoX(int topoX) {
        this.topoX = topoX;
    }

    public int getTopoY() {
        return topoY;
    }

    public void setTopoY(int topoY) {
        this.topoY = topoY;
    }

    public boolean isOpenModified() {
        return openModified;
    }

    public void setOpenModified(boolean openModified) {
        this.openModified = openModified;
    }


    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public boolean getModifiedProtocol(int n) {
        return modifiedProtocol[n];
    }

    public int getProtocolFileCounter() {
        return protocolFileCounter;
    }

    public String getProtocolFiles(int n) {
        return protocolFiles[n];
    }

    public void setProtocolFiles(String protocolFiles, int n, boolean modified) {
        this.protocolFiles[n] = protocolFiles;
        protocolFileCounter++;
        modifiedProtocol[n] = modified;
    }

    public String getNsInstallationPath() {
        return nsInstallationPath;
    }

    public boolean getModifyprotocoltab(int n) {
        return modifyprotocoltab[n];
    }

    public void setModifyprotocoltab(boolean modifyprotocoltab, int n) {
        this.modifyprotocoltab[n] = modifyprotocoltab;
    }

    public boolean isOpentabs(int n) {
        return opentabs[n];
    }

    public void setOpentabs(boolean opentabs, int n) {
        this.opentabs[n] = opentabs;
    }
    private boolean protocol = false;
    private String opentabsname[] = new String[50];
    private int opentabsid[] = new int[50];
    private boolean opentabs[] = new boolean[50];
    private String openTabsPath[]=new String[50];

    public String getOpenTabsPath(int n) {
        return openTabsPath[n];
    }

    public void setOpenTabsPath(String openTabsPath,int n) {
        this.openTabsPath[n] = openTabsPath;
    }

    public int getOpentabsid(int n) {
        return opentabsid[n];
    }

    public void setOpentabsid(int opentabsid, int n) {
        this.opentabsid[n] = opentabsid;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }
    private int i = 0;

    public String getOpentabsname(int n) {
        return opentabsname[n];
    }

    public void setOpentabsname(String opentabsname, int n) {
        this.opentabsname[n] = opentabsname;
        i++;
    }

    public boolean isProtocol() {
        return protocol;
    }

    public void setProtocol(boolean protocol) {
        this.protocol = protocol;
    }

    public String[] getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log[logindex] = log;
        logindex++;
    }

    public String getLog(int i) {
        return log[i];
    }

    public void setLog(String[] log) {
        this.log = log;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getNettype() {
        return nettype;
    }

    public void setNettype(int nettype) {
        this.nettype = nettype;
    }

    public File getFilename() {
        return filename;
    }

    public void setFilename(File filename) {
        this.filename = filename;
    }
}
