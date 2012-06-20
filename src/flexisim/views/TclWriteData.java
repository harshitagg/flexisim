package flexisim.views;

import java.io.Serializable;

/**
 *
 * @author harshit
 */
public class TclWriteData implements Serializable {

    private boolean fileCreated;
    private int lwpos;
    private int nwpos;
    private int awpos;
    private int ewpos;
    private int apppos;
    private int lewpos;
    private int cnpos;
    private int aewpos;
    private int rewpos;
    private int nppos;
    private int ncwpos, nmpos, gwpos, ncwsize, npwsize, nmwsize, twpos;

    public TclWriteData() {
        lwpos = 13;
        nwpos = 13;
        awpos = 13;
        ewpos = 13;
        apppos = 13;
        lewpos = 13;
        cnpos = 13;
        aewpos = 13;
        rewpos = 13;
        ncwsize = -1;
        npwsize = 0;
        nmwsize = 0;
        fileCreated = false;
    }

    public int getTwpos() {
        return twpos;
    }

    public void setTwpos(int twpos) {
        this.twpos = twpos;
    }

    public int getNmwsize() {
        return nmwsize;
    }

    public void setNmwsize(int nmwsize) {
        this.nmwsize = nmwsize;
    }

    public void incNmwsize(int val) {
        nmwsize += val;
    }

    public int getNpwsize() {
        return npwsize;
    }

    public void setNpwsize(int npwsize) {
        this.npwsize = npwsize;
    }

    public void incNpwsize(int val) {
        npwsize += val;
    }

    public int getNcwsize() {
        return ncwsize;
    }

    public void setNcwsize(int ncwsize) {
        this.ncwsize = ncwsize;
    }

    public void incNcwsize(int val) {
        ncwsize += val;
    }

    public void decNcwsize(int val) {
        ncwsize -= val;
    }

    public int getGwpos() {
        return gwpos;
    }

    public void incGwpos(int val) {
        gwpos += val;
    }

    public void decGwpos(int val) {
        gwpos -= val;
    }

    public void incNmpos(int val) {
        nmpos += val;
    }

    public void decNmpos(int val) {
        nmpos -= val;
    }

    public void setGwpos(int gwpos) {
        this.gwpos = gwpos;
    }

    public int getNmpos() {
        return nmpos;
    }

    public void setNmpos(int nmpos) {
        this.nmpos = nmpos;
    }

    public int getNcwpos() {
        return ncwpos;
    }

    public void setNcwpos(int ncwpos) {
        this.ncwpos = ncwpos;
    }

    public void incNcwpos(int val) {
        ncwpos += val;
    }

    public void decNcwpos(int val) {
        ncwpos -= val;
    }

    public int getNppos() {
        return nppos;
    }

    public void setNppos(int nppos) {
        this.nppos = nppos;
    }

    public void incNppos(int val) {
        nppos += val;
    }

    public void decNppos(int val) {
        nppos -= val;
    }

    public int getAewpos() {
        return aewpos;
    }

    public void setAewpos(int aewpos) {
        this.aewpos = aewpos;
    }

    public void incAewpos(int val) {
        aewpos += val;
    }

    public void decAewpos(int val) {
        aewpos -= val;
    }

    public int getApppos() {
        return apppos;
    }

    public void setApppos(int apppos) {
        this.apppos = apppos;
    }

    public void incApppos(int val) {
        apppos += val;
    }

    public void decApppos(int val) {
        apppos -= val;
    }

    public int getAwpos() {
        return awpos;
    }

    public void setAwpos(int awpos) {
        this.awpos = awpos;
    }

    public void incAwpos(int val) {
        awpos += val;
    }

    public void decAwpos(int val) {
        awpos -= val;
    }

    public int getCnpos() {
        return cnpos;
    }

    public void setCnpos(int cnpos) {
        this.cnpos = cnpos;
    }

    public void incCnpos(int val) {
        cnpos += val;
    }

    public void deccnpos(int val) {
        cnpos -= val;
    }

    public int getEwpos() {
        return ewpos;
    }

    public void incEwpos(int val) {
        ewpos += val;
    }

    public void decEwpos(int val) {
        ewpos -= val;
    }

    public void setEwpos(int ewpos) {
        this.ewpos = ewpos;
    }

    public int getLewpos() {
        return lewpos;
    }

    public void incLewpos(int val) {
        lewpos += val;
    }

    public void decLewpos(int val) {
        lewpos -= val;
    }

    public void setLewpos(int lewpos) {
        this.lewpos = lewpos;
    }

    public int getLwpos() {
        return lwpos;
    }

    public void incLwpos(int val) {
        lwpos += val;
    }

    public void decLwpos(int val) {
        lwpos -= val;
    }

    public void setLwpos(int lwpos) {
        this.lwpos = lwpos;
    }

    public int getNwpos() {
        return nwpos;
    }

    public void setNwpos(int nwpos) {
        this.nwpos = nwpos;
    }

    public void incNwpos(int val) {
        nwpos += val;
    }

    public void decNwpos(int val) {
        nwpos -= val;
    }

    public int getRewpos() {
        return rewpos;
    }

    public void setRewpos(int rewpos) {
        this.rewpos = rewpos;
    }

    public void incRewpos(int val) {
        rewpos += val;
    }

    public void decRewpos(int val) {
        rewpos -= val;
    }

    public boolean isFileCreated() {
        return fileCreated;
    }

    public void setFileCreated(boolean fileCreated) {
        this.fileCreated = fileCreated;
    }
}
