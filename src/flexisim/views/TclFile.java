package flexisim.views;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;

/**
 *
 * @author harshit
 */
public class TclFile {

    public String nw, lw, tw;
    public String aw[] = new String[15];
    public String ew[] = new String[10];
    public String cw[] = new String[10];
    public String appw[] = new String[10];
    public String lew[] = new String[10];
    public String aew[] = new String[10];
    public String rew[] = new String[10];
    public String ncw[] = new String[200];
    public String npw[] = new String[200];
    public String nmw[] = new String[200];
    public String mnw[] = new String[200];
    FileInputStream fin;
    DataInputStream in;
    BufferedReader br;
    FileChannel fc = null;
    RandomAccessFile aFile = null;
    public long pos;
    private int i, j = 0;

    public TclFile() {
        try {
            if (!NewProject.obtwd.isFileCreated()) {
                aFile = new RandomAccessFile(NewProject.obpd.getFilename(), "rwd");
                fc = aFile.getChannel();
                pos = fc.position();
                NewProject.obtwd.setFileCreated(true);
            }
            String defbeg = "set ns [new Simulator]\n";
            String deftr = "set tracefile1 [open " + NewProject.obpd.getFile().toString() + ".tr w]\n$ns trace-all $tracefile1\n";
            String defnam = "set namfile [open " + NewProject.obpd.getFile().toString() + ".nam w]\n$ns namtrace-all $namfile\n";
            String deffin = "proc finish {} {\n\tglobal ns tracefile1 namfile\n\t$ns flush-trace\n\tclose $tracefile1\n\tclose $namfile\n\texec " + System.getenv("NAM") + "nam " + NewProject.obpd.getFile().toString() + ".nam &\n\texit 0\n}\n";
            String deftopo = "set topo [new Topography]\n";
            String settopo = "$topo load_flatgrid 500 500\n";
            ByteBuffer bufb = ByteBuffer.allocate(defbeg.length());
            bufb.clear();
            bufb.put(defbeg.getBytes());
            bufb.flip();
            while (bufb.hasRemaining()) {
                fc.write(bufb);
            }
            ByteBuffer buft = ByteBuffer.allocate(deftr.length());
            buft.clear();
            buft.put(deftr.getBytes());
            buft.flip();
            while (buft.hasRemaining()) {
                fc.write(buft);
            }
            ByteBuffer bufn = ByteBuffer.allocate(defnam.length());
            bufn.clear();
            bufn.put(defnam.getBytes());
            bufn.flip();
            while (bufn.hasRemaining()) {
                fc.write(bufn);
            }
            ByteBuffer buff = ByteBuffer.allocate(deffin.length());
            buff.clear();
            buff.put(deffin.getBytes());
            buff.flip();
            while (buff.hasRemaining()) {
                fc.write(buff);
            }
            if (NewProject.obpd.getNettype() == 1) {
                ByteBuffer buftopo = ByteBuffer.allocate(deftopo.length());
                buftopo.clear();
                buftopo.put(deftopo.getBytes());
                buftopo.flip();
                while (buftopo.hasRemaining()) {
                    fc.write(buftopo);
                }
                ByteBuffer bufstopo = ByteBuffer.allocate(settopo.length());
                bufstopo.clear();
                bufstopo.put(settopo.getBytes());
                bufstopo.flip();
                while (bufstopo.hasRemaining()) {
                    fc.write(bufstopo);
                }
                NewProject.obtwd.setTwpos(14);
                NewProject.obtwd.setLwpos(15);
                NewProject.obtwd.setNwpos(15);
                NewProject.obtwd.setAwpos(15);
                NewProject.obtwd.setEwpos(15);
                NewProject.obtwd.setNppos(15);
                NewProject.obtwd.setApppos(15);
                NewProject.obtwd.setLewpos(15);
                NewProject.obtwd.setCnpos(15);
                NewProject.obtwd.setAewpos(15);
                NewProject.obtwd.setRewpos(15);
                NewProject.obtwd.setNcwpos(15);
                NewProject.obtwd.setNmpos(15);
                NewProject.obtwd.setNmpos(15);
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void topoWrite(int x, int y) {
        try {
            tw = "$topo load_flatgrid " + x + " " + y + "\n";
            String tmpstr[] = new String[1000];
            fc.position(pos);
            fin = new FileInputStream(NewProject.obpd.getFilename());
            in = new DataInputStream(fin);
            br = new BufferedReader(new InputStreamReader(in));
            for (i = 0; i < NewProject.obtwd.getRewpos(); i++) {
                tmpstr[i] = br.readLine() + "\n";
            }
            NewProject.obpd.getFilename().delete();
            aFile = new RandomAccessFile(((NewProject.obpd.getFilename())), "rwd");
            fc = aFile.getChannel();
            pos = fc.position();
            for (i = 0; i < NewProject.obtwd.getTwpos(); i++) {
                ByteBuffer buf1 = ByteBuffer.allocate(tmpstr[i].length());
                buf1.clear();
                buf1.put(tmpstr[i].getBytes());
                buf1.flip();
                while (buf1.hasRemaining()) {
                    fc.write(buf1);
                }
            }
            tmpstr[NewProject.obtwd.getTwpos()] = tw;
            for (i = NewProject.obtwd.getTwpos(); i < NewProject.obtwd.getRewpos(); i++) {
                ByteBuffer buf2 = ByteBuffer.allocate(tmpstr[i].length());
                buf2.clear();
                buf2.put(tmpstr[i].getBytes());
                buf2.flip();
                while (buf2.hasRemaining()) {
                    fc.write(buf2);
                }
            }
        } catch (IOException ie) {
            System.out.println(ie);
        }
    }

    public void godWrite() {
        try {
            String defgod = ("create-god " + (NewProject.obnm.nodeNum()) + "\n");
            String tmpstr[] = new String[200];
            fin = new FileInputStream(NewProject.obpd.getFilename());
            in = new DataInputStream(fin);
            br = new BufferedReader(new InputStreamReader(in));
            fc.position(pos);
            for (i = 0; i < NewProject.obtwd.getGwpos(); i++) {
                tmpstr[i] = br.readLine() + "\n";
            }
            tmpstr[NewProject.obtwd.getGwpos()] = defgod;
            for (i = NewProject.obtwd.getGwpos() + 1; i <= NewProject.obtwd.getRewpos(); i++) {
                tmpstr[i] = br.readLine() + "\n";
            }
            for (i = 0; i <= NewProject.obtwd.getRewpos(); i++) {
                ByteBuffer buf = ByteBuffer.allocate(tmpstr[i].length());
                buf.clear();
                buf.put(tmpstr[i].getBytes());
                buf.flip();
                while (buf.hasRemaining()) {
                    fc.write(buf);
                }
            }
            NewProject.obtwd.incNwpos(1);
            NewProject.obtwd.incNppos(1);
            NewProject.obtwd.incLwpos(1);
            NewProject.obtwd.incAwpos(1);
            NewProject.obtwd.incEwpos(1);
            NewProject.obtwd.incAewpos(1);
            NewProject.obtwd.incLewpos(1);
            NewProject.obtwd.incCnpos(1);
            NewProject.obtwd.incApppos(1);
            NewProject.obtwd.incRewpos(1);
            NewProject.obtwd.incNmpos(1);
            NewProject.obtwd.incGwpos(1);
        } catch (IOException ie) {
            System.out.println(ie);
        }
    }

    public void clearGodWrite() {
        try {
            if (NewProject.obtwd.getGwpos() == 0) {
                NewProject.obtwd.setGwpos(15);
                NewProject.obtwd.decAwpos(1);
                NewProject.obtwd.decApppos(1);
                NewProject.obtwd.decEwpos(1);
                NewProject.obtwd.deccnpos(1);
                NewProject.obtwd.decAewpos(1);
                NewProject.obtwd.decLewpos(1);
                NewProject.obtwd.decNmpos(1);
                NewProject.obtwd.decNppos(1);
                NewProject.obtwd.decNwpos(1);
                String tmpstr[] = new String[200];
                fc.position(pos);
                fin = new FileInputStream(NewProject.obpd.getFilename());
                in = new DataInputStream(fin);
                br = new BufferedReader(new InputStreamReader(in));
                for (i = 0; i < NewProject.obtwd.getRewpos(); i++) {
                    tmpstr[i] = br.readLine() + "\n";
                }
                NewProject.obpd.getFilename().delete();
                aFile = new RandomAccessFile(((NewProject.obpd.getFilename())), "rwd");
                fc = aFile.getChannel();
                pos = fc.position();
                for (i = 0; i < NewProject.obtwd.getGwpos(); i++) {
                    ByteBuffer buf1 = ByteBuffer.allocate(tmpstr[i].length());
                    buf1.clear();
                    buf1.put(tmpstr[i].getBytes());
                    buf1.flip();
                    while (buf1.hasRemaining()) {
                        fc.write(buf1);
                    }
                }
                for (i = NewProject.obtwd.getGwpos() + 1; i < NewProject.obtwd.getRewpos(); i++) {
                    ByteBuffer buf2 = ByteBuffer.allocate(tmpstr[i].length());
                    buf2.clear();
                    buf2.put(tmpstr[i].getBytes());
                    buf2.flip();
                    while (buf2.hasRemaining()) {
                        fc.write(buf2);
                    }
                }
                NewProject.obtwd.decRewpos(1);
            }
        } catch (IOException ie) {
            System.out.println(ie);
        }
    }

    public void nodeWrite(String nname) {
        nw = ("set " + nname + " [$ns node]\n");
        String tmpstr[] = new String[200];
        try {
            fin = new FileInputStream(NewProject.obpd.getFilename());
            in = new DataInputStream(fin);
            br = new BufferedReader(new InputStreamReader(in));
            fc.position(pos);
            for (i = 0; i < NewProject.obtwd.getNwpos(); i++) {
                tmpstr[i] = br.readLine() + "\n";
            }
            tmpstr[NewProject.obtwd.getNwpos()] = nw;
            for (i = NewProject.obtwd.getNwpos() + 1; i <= NewProject.obtwd.getRewpos(); i++) {
                tmpstr[i] = br.readLine() + "\n";
            }
            for (i = 0; i <= NewProject.obtwd.getRewpos(); i++) {
                ByteBuffer buf = ByteBuffer.allocate(tmpstr[i].length());
                buf.clear();
                buf.put(tmpstr[i].getBytes());
                buf.flip();
                while (buf.hasRemaining()) {
                    fc.write(buf);
                }
            }
            NewProject.obtwd.incNwpos(1);
            NewProject.obtwd.incNppos(1);
            NewProject.obtwd.incLwpos(1);
            NewProject.obtwd.incAwpos(1);
            NewProject.obtwd.incEwpos(1);
            NewProject.obtwd.incAewpos(1);
            NewProject.obtwd.incLewpos(1);
            NewProject.obtwd.incCnpos(1);
            NewProject.obtwd.incApppos(1);
            NewProject.obtwd.incRewpos(1);
            NewProject.obtwd.incNmpos(1);
        } catch (IOException ie) {
            System.out.println(ie);
        }
    }

    public void nodeconfigwrite(String addtype, String adhocrouting, String lltype, String mactype, String proptype, String ifqtype, long ifqlen, String phytype, String anttype, String channeltype, String topoinstance, String wiredrouting, String mobileip, String energymodel, double initialenergy, double rxpower, double txpower, double idlepower, String agenttrace, String routertrace, String mactrace, String movementtrace) {
        if (addtype != null) {
            NewProject.obtwd.incNcwsize(1);
            ncw[NewProject.obtwd.getNcwsize()] = ("$ns node-config -addressingType " + addtype + "\n");
        }
        if (adhocrouting != null) {
            NewProject.obtwd.incNcwsize(1);
            ncw[NewProject.obtwd.getNcwsize()] = ("$ns node-config -adhocRouting " + adhocrouting + "\n");
        }
        if (lltype != null) {
            NewProject.obtwd.incNcwsize(1);
            ncw[NewProject.obtwd.getNcwsize()] = ("$ns node-config -llType " + lltype + "\n");
        }
        if (mactype != null) {
            NewProject.obtwd.incNcwsize(1);
            ncw[NewProject.obtwd.getNcwsize()] = ("$ns node-config -macType " + mactype + "\n");
        }
        if (proptype != null) {
            NewProject.obtwd.incNcwsize(1);
            ncw[NewProject.obtwd.getNcwsize()] = ("$ns node-config -propType " + proptype + "\n");
        }
        if (ifqtype != null) {
            NewProject.obtwd.incNcwsize(1);
            ncw[NewProject.obtwd.getNcwsize()] = ("$ns node-config -ifqType " + ifqtype + "\n");
        }
        if (ifqlen != 0) {
            NewProject.obtwd.incNcwsize(1);
            ncw[NewProject.obtwd.getNcwsize()] = ("$ns node-config -ifqLen " + ifqlen + "\n");
        }
        if (phytype != null) {
            NewProject.obtwd.incNcwsize(1);
            ncw[NewProject.obtwd.getNcwsize()] = ("$ns node-config -phyType " + phytype + "\n");
        }
        if (anttype != null) {
            NewProject.obtwd.incNcwsize(1);
            ncw[NewProject.obtwd.getNcwsize()] = ("$ns node-config -antType " + anttype + "\n");
        }
        if (channeltype != null) {
            NewProject.obtwd.incNcwsize(1);
            ncw[NewProject.obtwd.getNcwsize()] = ("$ns node-config -channelType " + channeltype + "\n");
        }
        if (topoinstance != null) {
            NewProject.obtwd.incNcwsize(1);
            ncw[NewProject.obtwd.getNcwsize()] = ("$ns node-config -topoInstance $" + topoinstance + "\n");
        }
        if (wiredrouting != null) {
            NewProject.obtwd.incNcwsize(1);
            ncw[NewProject.obtwd.getNcwsize()] = ("$ns node-config -wiredRouting " + wiredrouting + "\n");
        }
        if (mobileip != null) {
            NewProject.obtwd.incNcwsize(1);
            ncw[NewProject.obtwd.getNcwsize()] = ("$ns node-config -mobileIP " + mobileip + "\n");
        }
        if (energymodel != null && initialenergy != 0) {
            NewProject.obtwd.incNcwsize(1);
            ncw[NewProject.obtwd.getNcwsize()] = ("$ns node-config -energyModel " + energymodel + "\n");
        }
        if (initialenergy != 0) {
            NewProject.obtwd.incNcwsize(1);
            ncw[NewProject.obtwd.getNcwsize()] = ("$ns node-config -initialEnergy " + initialenergy + "\n");
        }
        if (rxpower != 0) {
            NewProject.obtwd.incNcwsize(1);
            ncw[NewProject.obtwd.getNcwsize()] = ("$ns node-config -rxPower " + rxpower + "\n");
        }
        if (txpower != 0) {
            NewProject.obtwd.incNcwsize(1);
            ncw[NewProject.obtwd.getNcwsize()] = ("$ns node-config -txPower " + txpower + "\n");
        }
        if (idlepower != 0) {
            NewProject.obtwd.incNcwsize(1);
            ncw[NewProject.obtwd.getNcwsize()] = ("$ns node-config -idlePower " + idlepower + "\n");
        }
        if (agenttrace != null) {
            NewProject.obtwd.incNcwsize(1);
            ncw[NewProject.obtwd.getNcwsize()] = ("$ns node-config -agentTrace " + agenttrace + "\n");
        }
        if (routertrace != null) {
            NewProject.obtwd.incNcwsize(1);
            ncw[NewProject.obtwd.getNcwsize()] = ("$ns node-config -routerTrace " + routertrace + "\n");
        }
        if (mactrace != null) {
            NewProject.obtwd.incNcwsize(1);
            ncw[NewProject.obtwd.getNcwsize()] = ("$ns node-config -macTrace " + mactrace + "\n");
        }
        if (movementtrace != null) {
            NewProject.obtwd.incNcwsize(1);
            ncw[NewProject.obtwd.getNcwsize()] = ("$ns node-config -movementTrace " + movementtrace + "\n");
        }
        String tmpstr[] = new String[1000];
        try {
            fc.position(pos);
            fin = new FileInputStream(NewProject.obpd.getFilename());
            in = new DataInputStream(fin);
            br = new BufferedReader(new InputStreamReader(in));
            for (i = 0; i < NewProject.obtwd.getNcwpos(); i++) {
                tmpstr[i] = br.readLine() + "\n";
            }
            for (i = 0; i <= NewProject.obtwd.getNcwsize(); i++) {
                tmpstr[NewProject.obtwd.getNcwpos() + i] = ncw[i];
            }
            NewProject.obtwd.incNwpos(NewProject.obtwd.getNcwsize());
            NewProject.obtwd.incNppos(NewProject.obtwd.getNcwsize());
            NewProject.obtwd.incNcwpos(NewProject.obtwd.getNcwsize());
            NewProject.obtwd.incAwpos(NewProject.obtwd.getNcwsize());
            NewProject.obtwd.incEwpos(NewProject.obtwd.getNcwsize());
            NewProject.obtwd.incAewpos(NewProject.obtwd.getNcwsize());
            NewProject.obtwd.incLewpos(NewProject.obtwd.getNcwsize());
            NewProject.obtwd.incCnpos(NewProject.obtwd.getNcwsize());
            NewProject.obtwd.incApppos(NewProject.obtwd.getNcwsize());
            NewProject.obtwd.incRewpos(NewProject.obtwd.getNcwsize());
            NewProject.obtwd.incNmpos(NewProject.obtwd.getNcwsize());
            for (i = NewProject.obtwd.getNcwpos() + 1; i <= NewProject.obtwd.getRewpos(); i++) {
                tmpstr[i] = br.readLine() + "\n";
            }
            for (i = 0; i <= NewProject.obtwd.getRewpos(); i++) {
                ByteBuffer buf = ByteBuffer.allocate(tmpstr[i].length());
                buf.clear();
                buf.put(tmpstr[i].getBytes());
                buf.flip();
                while (buf.hasRemaining()) {
                    fc.write(buf);
                }
            }
            NewProject.obtwd.incNwpos(1);
            NewProject.obtwd.incNppos(1);
            NewProject.obtwd.incAwpos(1);
            NewProject.obtwd.incEwpos(1);
            NewProject.obtwd.incAewpos(1);
            NewProject.obtwd.incLewpos(1);
            NewProject.obtwd.incCnpos(1);
            NewProject.obtwd.incApppos(1);
            NewProject.obtwd.incRewpos(1);
            NewProject.obtwd.incNmpos(1);
        } catch (IOException ie) {
            System.out.println(ie);
        }
    }

    public void clearNodeConfigWrite() {
        try {
            if (NewProject.obtwd.getNcwsize() != -1) {
                NewProject.obtwd.incNcwsize(1);
                NewProject.obtwd.setNcwpos(15);
                NewProject.obtwd.decNwpos(NewProject.obtwd.getNcwsize());
                NewProject.obtwd.decAwpos(NewProject.obtwd.getNcwsize());
                NewProject.obtwd.decApppos(NewProject.obtwd.getNcwsize());
                NewProject.obtwd.decEwpos(NewProject.obtwd.getNcwsize());
                NewProject.obtwd.deccnpos(NewProject.obtwd.getNcwsize());
                NewProject.obtwd.decAewpos(NewProject.obtwd.getNcwsize());
                NewProject.obtwd.decLewpos(NewProject.obtwd.getNcwsize());
                NewProject.obtwd.decNmpos(NewProject.obtwd.getNcwsize());
                NewProject.obtwd.decNppos(NewProject.obtwd.getNcwsize());
                String tmpstr[] = new String[1000];
                fc.position(pos);
                fin = new FileInputStream(NewProject.obpd.getFilename());
                in = new DataInputStream(fin);
                br = new BufferedReader(new InputStreamReader(in));
                for (i = 0; i < NewProject.obtwd.getRewpos(); i++) {
                    tmpstr[i] = br.readLine() + "\n";
                }
                NewProject.obpd.getFilename().delete();
                aFile = new RandomAccessFile(((NewProject.obpd.getFilename())), "rwd");
                fc = aFile.getChannel();
                pos = fc.position();
                for (i = 0; i < NewProject.obtwd.getNcwpos(); i++) {
                    ByteBuffer buf1 = ByteBuffer.allocate(tmpstr[i].length());
                    buf1.clear();
                    buf1.put(tmpstr[i].getBytes());
                    buf1.flip();
                    while (buf1.hasRemaining()) {
                        fc.write(buf1);
                    }
                }
                for (i = NewProject.obtwd.getNcwpos() + NewProject.obtwd.getNcwsize(); i < NewProject.obtwd.getRewpos(); i++) {
                    ByteBuffer buf2 = ByteBuffer.allocate(tmpstr[i].length());
                    buf2.clear();
                    buf2.put(tmpstr[i].getBytes());
                    buf2.flip();
                    while (buf2.hasRemaining()) {
                        fc.write(buf2);
                    }
                }
                NewProject.obtwd.decRewpos(NewProject.obtwd.getNcwsize());
                NewProject.obtwd.decNcwsize(1);
            }
        } catch (IOException ie) {
            System.out.println(ie);
        }
    }

    public void nodeposwrite(String node, double x, double y, double z, boolean random) {
        NewProject.obtwd.incNpwsize(3);
        int size = 2;
        if (!random) {
            npw[0] = ("$" + node + " set X_ " + x + "\n");
            npw[1] = ("$" + node + " set Y_ " + y + "\n");
            npw[2] = ("$" + node + " set Z_ " + z + "\n");
        } else if (random) {
            npw[0] = ("$" + node + " set X_ [ expr 10+round(rand()*" + (NewProject.obpd.getTopoX() - 20) + ") ]\n");
            npw[1] = ("$" + node + " set Y_ [ expr 10+round(rand()*" + (NewProject.obpd.getTopoY() - 20) + ") ]\n");
            npw[2] = ("$" + node + " set Z_ 0.0\n");
        }
        String tmpstr[] = new String[1000];
        try {
            fc.position(pos);
            fin = new FileInputStream(NewProject.obpd.getFilename());
            in = new DataInputStream(fin);
            br = new BufferedReader(new InputStreamReader(in));
            for (i = 0; i < NewProject.obtwd.getNppos(); i++) {
                tmpstr[i] = br.readLine() + "\n";
            }
            for (i = 0; i <= size; i++) {
                tmpstr[NewProject.obtwd.getNppos() + i] = npw[i];
            }
            NewProject.obtwd.incNppos(size);
            NewProject.obtwd.incAwpos(size);
            NewProject.obtwd.incEwpos(size);
            NewProject.obtwd.incAewpos(size);
            NewProject.obtwd.incLewpos(size);
            NewProject.obtwd.incCnpos(size);
            NewProject.obtwd.incApppos(size);
            NewProject.obtwd.incRewpos(size);
            NewProject.obtwd.incNmpos(size);
            for (i = NewProject.obtwd.getNppos() + 1; i <= NewProject.obtwd.getRewpos(); i++) {
                tmpstr[i] = br.readLine() + "\n";
            }
            for (i = 0; i <= NewProject.obtwd.getRewpos(); i++) {
                ByteBuffer buf = ByteBuffer.allocate(tmpstr[i].length());
                buf.clear();
                buf.put(tmpstr[i].getBytes());
                buf.flip();
                while (buf.hasRemaining()) {
                    fc.write(buf);
                }
            }
            NewProject.obtwd.incNppos(1);
            NewProject.obtwd.incAwpos(1);
            NewProject.obtwd.incEwpos(1);
            NewProject.obtwd.incAewpos(1);
            NewProject.obtwd.incLewpos(1);
            NewProject.obtwd.incCnpos(1);
            NewProject.obtwd.incApppos(1);
            NewProject.obtwd.incRewpos(1);
            NewProject.obtwd.incNmpos(1);
        } catch (IOException ie) {
            System.out.println(ie);
        }
    }

    public void clearNodePosWrite() {
        try {
            if (NewProject.obtwd.getNpwsize() != 0) {
                NewProject.obtwd.decAwpos(NewProject.obtwd.getNpwsize());
                NewProject.obtwd.decApppos(NewProject.obtwd.getNpwsize());
                NewProject.obtwd.decEwpos(NewProject.obtwd.getNpwsize());
                NewProject.obtwd.deccnpos(NewProject.obtwd.getNpwsize());
                NewProject.obtwd.decAewpos(NewProject.obtwd.getNpwsize());
                NewProject.obtwd.decLewpos(NewProject.obtwd.getNpwsize());
                NewProject.obtwd.decNmpos(NewProject.obtwd.getNpwsize());
                NewProject.obtwd.setNppos(NewProject.obtwd.getNwpos());
                String tmpstr[] = new String[1000];
                fc.position(pos);
                fin = new FileInputStream(NewProject.obpd.getFilename());
                in = new DataInputStream(fin);
                br = new BufferedReader(new InputStreamReader(in));
                for (i = 0; i < NewProject.obtwd.getRewpos(); i++) {
                    tmpstr[i] = br.readLine() + "\n";
                }
                NewProject.obpd.getFilename().delete();
                aFile = new RandomAccessFile(((NewProject.obpd.getFilename())), "rwd");
                fc = aFile.getChannel();
                pos = fc.position();
                for (i = 0; i < NewProject.obtwd.getNppos(); i++) {
                    ByteBuffer buf1 = ByteBuffer.allocate(tmpstr[i].length());
                    buf1.clear();
                    buf1.put(tmpstr[i].getBytes());
                    buf1.flip();
                    while (buf1.hasRemaining()) {
                        fc.write(buf1);
                    }
                }
                for (i = NewProject.obtwd.getNppos() + NewProject.obtwd.getNpwsize(); i < NewProject.obtwd.getRewpos(); i++) {
                    ByteBuffer buf2 = ByteBuffer.allocate(tmpstr[i].length());
                    buf2.clear();
                    buf2.put(tmpstr[i].getBytes());
                    buf2.flip();
                    while (buf2.hasRemaining()) {
                        fc.write(buf2);
                    }
                }
                NewProject.obtwd.decRewpos(NewProject.obtwd.getNpwsize());
                NewProject.obtwd.setNpwsize(0);
            }
        } catch (IOException ie) {
            System.out.println(ie);
        }
    }

    public void nodemovwrite(String node, double x, double y, double time, double speed, boolean random, boolean randomAll) {
        NewProject.obtwd.incNmwsize(1);
        if (!random && !randomAll) {
            nmw[0] = ("$ns at " + time + " \"$" + node + " setdest " + x + " " + y + " " + speed + "\"\n");
        } else if (random && !randomAll) {
            nmw[0] = ("$" + node + " random-motion 1\n");
        } else if (randomAll) {
            nmw[0] = "$ns at [ expr 15+round(rand()*60) ] \"$" + node + " setdest " + "[ expr 10+round(rand()*" + (NewProject.obpd.getTopoX() - 20) + ") ] [ expr 10+round(rand()*" + (NewProject.obpd.getTopoY() - 20) + ") ] [ expr 2+round(rand()*15) ]";
        }
        String tmpstr[] = new String[1000];
        try {
            fc.position(pos);
            fin = new FileInputStream(NewProject.obpd.getFilename());
            in = new DataInputStream(fin);
            br = new BufferedReader(new InputStreamReader(in));
            for (i = 0; i < NewProject.obtwd.getNmpos(); i++) {
                tmpstr[i] = br.readLine() + "\n";
            }
            for (i = 0; i <= 0; i++) {
                tmpstr[NewProject.obtwd.getNmpos() + i] = nmw[i];
            }
            for (i = NewProject.obtwd.getNmpos() + 1; i <= NewProject.obtwd.getRewpos(); i++) {
                tmpstr[i] = br.readLine() + "\n";
            }
            for (i = 0; i <= NewProject.obtwd.getRewpos(); i++) {
                ByteBuffer buf = ByteBuffer.allocate(tmpstr[i].length());
                buf.clear();
                buf.put(tmpstr[i].getBytes());
                buf.flip();
                while (buf.hasRemaining()) {
                    fc.write(buf);
                }
            }
            NewProject.obtwd.incAwpos(1);
            NewProject.obtwd.incEwpos(1);
            NewProject.obtwd.incAewpos(1);
            NewProject.obtwd.incLewpos(1);
            NewProject.obtwd.incCnpos(1);
            NewProject.obtwd.incApppos(1);
            NewProject.obtwd.incRewpos(1);
            NewProject.obtwd.incNmpos(1);
        } catch (IOException ie) {
            System.out.println(ie);
        }
    }

    public void clearNodeMovWrite() {
        try {
            if (NewProject.obtwd.getNmwsize() != 0) {
                NewProject.obtwd.decAwpos(NewProject.obtwd.getNmwsize());
                NewProject.obtwd.decApppos(NewProject.obtwd.getNmwsize());
                NewProject.obtwd.decEwpos(NewProject.obtwd.getNmwsize());
                NewProject.obtwd.deccnpos(NewProject.obtwd.getNmwsize());
                NewProject.obtwd.decAewpos(NewProject.obtwd.getNmwsize());
                NewProject.obtwd.decLewpos(NewProject.obtwd.getNmwsize());
                NewProject.obtwd.setNmpos(NewProject.obtwd.getNppos());
                String tmpstr[] = new String[1000];
                fc.position(pos);
                fin = new FileInputStream(NewProject.obpd.getFilename());
                in = new DataInputStream(fin);
                br = new BufferedReader(new InputStreamReader(in));
                for (i = 0; i < NewProject.obtwd.getRewpos(); i++) {
                    tmpstr[i] = br.readLine() + "\n";
                }
                NewProject.obpd.getFilename().delete();
                aFile = new RandomAccessFile(((NewProject.obpd.getFilename())), "rwd");
                fc = aFile.getChannel();
                pos = fc.position();
                for (i = 0; i < NewProject.obtwd.getNmpos(); i++) {
                    ByteBuffer buf1 = ByteBuffer.allocate(tmpstr[i].length());
                    buf1.clear();
                    buf1.put(tmpstr[i].getBytes());
                    buf1.flip();
                    while (buf1.hasRemaining()) {
                        fc.write(buf1);
                    }
                }
                for (i = NewProject.obtwd.getNmpos() + NewProject.obtwd.getNmwsize(); i < NewProject.obtwd.getRewpos(); i++) {
                    ByteBuffer buf2 = ByteBuffer.allocate(tmpstr[i].length());
                    buf2.clear();
                    buf2.put(tmpstr[i].getBytes());
                    buf2.flip();
                    while (buf2.hasRemaining()) {
                        fc.write(buf2);
                    }
                }
                NewProject.obtwd.decRewpos(NewProject.obtwd.getNmwsize());
                NewProject.obtwd.setNmwsize(0);
            }
        } catch (IOException ie) {
            System.out.println(ie);
        }
    }

    public void linkWrite(String ltype, String n1, String n2, double bdvalue, String bdunit, long dvalue, String dunit, String overflow) {
        lw = ("$ns " + ltype + " $" + n1 + " $" + n2 + " " + bdvalue + bdunit + " " + dvalue + dunit + " " + overflow + "\n");
        String tmpstr[] = new String[1000];
        try {
            fc.position(pos);
            fin = new FileInputStream(NewProject.obpd.getFilename());
            in = new DataInputStream(fin);
            br = new BufferedReader(new InputStreamReader(in));
            for (i = 0; i < NewProject.obtwd.getLwpos(); i++) {
                tmpstr[i] = br.readLine() + "\n";
            }
            tmpstr[NewProject.obtwd.getLwpos()] = lw;
            for (i = NewProject.obtwd.getLwpos() + 1; i <= NewProject.obtwd.getRewpos(); i++) {
                tmpstr[i] = br.readLine() + "\n";
            }
            for (i = 0; i <= NewProject.obtwd.getRewpos(); i++) {
                ByteBuffer buf = ByteBuffer.allocate(tmpstr[i].length());
                buf.clear();
                buf.put(tmpstr[i].getBytes());
                buf.flip();
                while (buf.hasRemaining()) {
                    fc.write(buf);
                }
            }
            NewProject.obtwd.incLwpos(1);
            NewProject.obtwd.incAwpos(1);
            NewProject.obtwd.incEwpos(1);
            NewProject.obtwd.incAewpos(1);
            NewProject.obtwd.incLewpos(1);
            NewProject.obtwd.incCnpos(1);
            NewProject.obtwd.incApppos(1);
            NewProject.obtwd.incRewpos(1);
        } catch (IOException ie) {
            System.out.println(ie);
        }
    }

    public void agentWrite(String aname, String atype, String anode) {
        int size = 2;
        aw[0] = "set " + aname + " [new Agent/" + atype + "]\n";
        aw[1] = "$ns attach-agent $" + anode + " $" + aname + "\n";
        aw[2] = "$" + aname + " set class_ " + j++ + "\n";
        String tmpstr[] = new String[1000];
        try {
            fc.position(pos);
            fin = new FileInputStream(NewProject.obpd.getFilename());
            in = new DataInputStream(fin);
            br = new BufferedReader(new InputStreamReader(in));
            for (i = 0; i < NewProject.obtwd.getAwpos(); i++) {
                tmpstr[i] = br.readLine() + "\n";
            }
            for (i = 0; i <= size; i++) {
                tmpstr[NewProject.obtwd.getAwpos() + i] = aw[i];
            }
            NewProject.obtwd.incAwpos(size);
            NewProject.obtwd.incEwpos(size);
            NewProject.obtwd.incAewpos(size);
            NewProject.obtwd.incLewpos(size);
            NewProject.obtwd.incCnpos(size);
            NewProject.obtwd.incApppos(size);
            NewProject.obtwd.incRewpos(size);
            for (i = NewProject.obtwd.getAwpos() + 1; i <= NewProject.obtwd.getRewpos(); i++) {
                tmpstr[i] = br.readLine() + "\n";
            }
            for (i = 0; i <= NewProject.obtwd.getRewpos(); i++) {
                ByteBuffer buf = ByteBuffer.allocate(tmpstr[i].length());
                buf.clear();
                buf.put(tmpstr[i].getBytes());
                buf.flip();
                while (buf.hasRemaining()) {
                    fc.write(buf);
                }
            }
            NewProject.obtwd.incAwpos(1);
            NewProject.obtwd.incEwpos(1);
            NewProject.obtwd.incAewpos(1);
            NewProject.obtwd.incLewpos(1);
            NewProject.obtwd.incCnpos(1);
            NewProject.obtwd.incApppos(1);
            NewProject.obtwd.incRewpos(1);
        } catch (IOException ie) {
            System.out.println(ie);
        }
    }

    public void appWrite(String appname, String apptype, String aname, long tpktsize, double traffic, boolean trafficRate, double noiseValue, int maxPack) {
        int size;
        if (apptype.equals("CBR")) {
            appw[0] = "set " + appname + " [new Application/Traffic/" + apptype + "]\n";
            appw[1] = "$" + appname + " attach-agent $" + aname + "\n";
            appw[2] = "$" + appname + " set packetSize_ " + tpktsize + "\n";
            if (trafficRate) {
                appw[3] = "$" + appname + " set rate_ " + traffic + "\n";
            } else {
                appw[3] = "$" + appname + " set interval_ " + traffic + "\n";
            }
            size = 3;
            if (noiseValue != -1) {
                appw[4] = "$" + appname + " set random_ " + noiseValue + "\n";
            }
            size = 4;
            if (maxPack != -1) {
                size++;
                appw[size] = "$" + appname + " set maxpkts_ " + maxPack + "\n";
            }
        } else {
            appw[0] = "set " + appname + " [new Application/" + apptype + "]\n";
            appw[1] = "$" + appname + " attach-agent $" + aname + "\n";
            size = 1;
        }
        String tmpstr[] = new String[1000];
        try {
            fc.position(pos);
            fin = new FileInputStream(NewProject.obpd.getFilename());
            in = new DataInputStream(fin);
            br = new BufferedReader(new InputStreamReader(in));
            for (i = 0; i < NewProject.obtwd.getApppos(); i++) {
                tmpstr[i] = br.readLine() + "\n";
            }
            for (i = 0; i <= size; i++) {
                tmpstr[NewProject.obtwd.getApppos() + i] = appw[i];
            }
            NewProject.obtwd.incEwpos(size);
            NewProject.obtwd.incAewpos(size);
            NewProject.obtwd.incLewpos(size);
            NewProject.obtwd.incCnpos(size);
            NewProject.obtwd.incApppos(size);
            NewProject.obtwd.incRewpos(size);
            for (i = NewProject.obtwd.getApppos() + 1; i <= NewProject.obtwd.getRewpos(); i++) {
                tmpstr[i] = br.readLine() + "\n";
            }
            for (i = 0; i <= NewProject.obtwd.getRewpos(); i++) {
                ByteBuffer buf = ByteBuffer.allocate(tmpstr[i].length());
                buf.clear();
                buf.put(tmpstr[i].getBytes());
                buf.flip();
                while (buf.hasRemaining()) {
                    fc.write(buf);
                }
            }
            NewProject.obtwd.incEwpos(1);
            NewProject.obtwd.incAewpos(1);
            NewProject.obtwd.incLewpos(1);
            NewProject.obtwd.incCnpos(1);
            NewProject.obtwd.incApppos(1);
            NewProject.obtwd.incRewpos(1);
        } catch (IOException ie) {
            System.out.println(ie);
        }
    }

    public void connectWrite(String agent1, String agent2) {
        int size = 0;
        cw[0] = "$ns connect $" + agent1 + " $" + agent2 + "\n";
        String tmpstr[] = new String[1000];
        try {
            fc.position(pos);
            fin = new FileInputStream(NewProject.obpd.getFilename());
            in = new DataInputStream(fin);
            br = new BufferedReader(new InputStreamReader(in));
            for (i = 0; i < NewProject.obtwd.getCnpos(); i++) {
                tmpstr[i] = br.readLine() + "\n";
            }
            for (i = 0; i <= size; i++) {
                tmpstr[NewProject.obtwd.getCnpos() + i] = cw[i];
            }
            NewProject.obtwd.incAewpos(size);
            NewProject.obtwd.incLewpos(size);
            NewProject.obtwd.incCnpos(size);
            NewProject.obtwd.incEwpos(size);
            NewProject.obtwd.incRewpos(size);
            for (i = NewProject.obtwd.getCnpos() + 1; i <= NewProject.obtwd.getRewpos(); i++) {
                tmpstr[i] = br.readLine() + "\n";
            }
            for (i = 0; i <= NewProject.obtwd.getRewpos(); i++) {
                ByteBuffer buf = ByteBuffer.allocate(tmpstr[i].length());
                buf.clear();
                buf.put(tmpstr[i].getBytes());
                buf.flip();
                while (buf.hasRemaining()) {
                    fc.write(buf);
                }
            }
            NewProject.obtwd.incEwpos(1);
            NewProject.obtwd.incAewpos(1);
            NewProject.obtwd.incLewpos(1);
            NewProject.obtwd.incCnpos(1);
            NewProject.obtwd.incRewpos(1);
        } catch (IOException ie) {
            System.out.println(ie);
        }
    }

    public void eventWrite(double startapp, double stopapp, String appname) {
        ew[0] = "$ns at " + startapp + " \"$" + appname + " start\"\n";
        int size = 0;
        if (stopapp > 0) {
            ew[1] = "$ns at " + stopapp + " \"$" + appname + " stop\"\n";
            size = 1;
        }
        String tmpstr[] = new String[1000];
        try {
            fc.position(pos);
            fin = new FileInputStream(NewProject.obpd.getFilename());
            in = new DataInputStream(fin);
            br = new BufferedReader(new InputStreamReader(in));
            for (i = 0; i < NewProject.obtwd.getEwpos(); i++) {
                tmpstr[i] = br.readLine() + "\n";
            }
            for (i = 0; i <= size; i++) {
                tmpstr[NewProject.obtwd.getEwpos() + i] = ew[i];
            }
            NewProject.obtwd.incAewpos(size);
            NewProject.obtwd.incLewpos(size);
            NewProject.obtwd.incEwpos(size);
            NewProject.obtwd.incRewpos(size);
            for (i = NewProject.obtwd.getEwpos() + 1; i <= NewProject.obtwd.getRewpos(); i++) {
                tmpstr[i] = br.readLine() + "\n";
            }
            for (i = 0; i <= NewProject.obtwd.getRewpos(); i++) {
                ByteBuffer buf = ByteBuffer.allocate(tmpstr[i].length());
                buf.clear();
                buf.put(tmpstr[i].getBytes());
                buf.flip();
                while (buf.hasRemaining()) {
                    fc.write(buf);
                }
            }
            NewProject.obtwd.incEwpos(1);
            NewProject.obtwd.incAewpos(1);
            NewProject.obtwd.incLewpos(1);
            NewProject.obtwd.incRewpos(1);
        } catch (IOException ie) {
            System.out.println(ie);
        }
    }

    public void linkEventWrite(String node1, String node2, double up, double down) {
        lew[0] = "$ns rtmodel-at " + down + " down $" + node1 + " $" + node2 + "\n";
        int size = 0;
        if (up > 0) {
            lew[1] = "$ns rtmodel-at " + up + " up $" + node1 + " $" + node2 + "\n";
            size = 1;
        }
        String tmpstr[] = new String[1000];
        try {
            fc.position(pos);
            fin = new FileInputStream(NewProject.obpd.getFilename());
            in = new DataInputStream(fin);
            br = new BufferedReader(new InputStreamReader(in));
            for (i = 0; i < NewProject.obtwd.getLewpos(); i++) {
                tmpstr[i] = br.readLine() + "\n";
            }
            for (i = 0; i <= size; i++) {
                tmpstr[NewProject.obtwd.getLewpos() + i] = lew[i];
            }
            NewProject.obtwd.incAewpos(size);
            NewProject.obtwd.incLewpos(size);
            NewProject.obtwd.incRewpos(size);
            for (i = NewProject.obtwd.getLewpos() + 1; i <= NewProject.obtwd.getRewpos(); i++) {
                tmpstr[i] = br.readLine() + "\n";
            }
            for (i = 0; i <= NewProject.obtwd.getRewpos(); i++) {
                ByteBuffer buf = ByteBuffer.allocate(tmpstr[i].length());
                buf.clear();
                buf.put(tmpstr[i].getBytes());
                buf.flip();
                while (buf.hasRemaining()) {
                    fc.write(buf);
                }
            }
            NewProject.obtwd.incAewpos(1);
            NewProject.obtwd.incLewpos(1);
            NewProject.obtwd.incRewpos(1);
        } catch (IOException ie) {
            System.out.println(ie);
        }
    }

    public void agentEventWrite(String agent, String node, double time) {
        aew[0] = "$ns at " + time + " \"$ns detach-agent $" + node + " $" + agent + "\"\n";
        int size = 0;
        String tmpstr[] = new String[1000];
        try {
            fc.position(pos);
            fin = new FileInputStream(NewProject.obpd.getFilename());
            in = new DataInputStream(fin);
            br = new BufferedReader(new InputStreamReader(in));
            for (i = 0; i < NewProject.obtwd.getAewpos(); i++) {
                tmpstr[i] = br.readLine() + "\n";
            }
            for (i = 0; i <= size; i++) {
                tmpstr[NewProject.obtwd.getAewpos() + i] = aew[i];
            }
            NewProject.obtwd.incAewpos(size);
            NewProject.obtwd.incRewpos(size);
            for (i = NewProject.obtwd.getAewpos() + 1; i <= NewProject.obtwd.getRewpos(); i++) {
                tmpstr[i] = br.readLine() + "\n";
            }
            for (i = 0; i <= NewProject.obtwd.getRewpos(); i++) {
                ByteBuffer buf = ByteBuffer.allocate(tmpstr[i].length());
                buf.clear();
                buf.put(tmpstr[i].getBytes());
                buf.flip();
                while (buf.hasRemaining()) {
                    fc.write(buf);
                }
            }
            NewProject.obtwd.incAewpos(1);
            NewProject.obtwd.incRewpos(1);
        } catch (IOException ie) {
            System.out.println(ie);
        }
    }

    public void clearEventWrite() {
        try {
            NewProject.obtwd.setAewpos(NewProject.obtwd.getEwpos());
            NewProject.obtwd.setLewpos(NewProject.obtwd.getEwpos());
            NewProject.obtwd.setRewpos(NewProject.obtwd.getEwpos());
            String tmpstr[] = new String[1000];
            fc.position(pos);
            fin = new FileInputStream(NewProject.obpd.getFilename());
            in = new DataInputStream(fin);
            br = new BufferedReader(new InputStreamReader(in));
            for (i = 0; i < NewProject.obtwd.getAewpos(); i++) {
                tmpstr[i] = br.readLine() + "\n";
            }
            NewProject.obpd.getFilename().delete();
            aFile = new RandomAccessFile(((NewProject.obpd.getFilename())), "rwd");
            fc = aFile.getChannel();
            pos = fc.position();
            for (i = 0; i < NewProject.obtwd.getAewpos(); i++) {
                ByteBuffer buf = ByteBuffer.allocate(tmpstr[i].length());
                buf.clear();
                buf.put(tmpstr[i].getBytes());
                buf.flip();
                while (buf.hasRemaining()) {
                    fc.write(buf);
                }
            }

        } catch (IOException ie) {
            System.out.println(ie);
        }
    }

    public void runWrite(double time) {
        rew[0] = "$ns at " + time + " \"finish\"\n";
        rew[1] = "$ns run\n";
        int size = 1;
        String tmpstr[] = new String[1000];
        try {
            fc.position(pos);
            fin = new FileInputStream(NewProject.obpd.getFilename());
            in = new DataInputStream(fin);
            br = new BufferedReader(new InputStreamReader(in));
            for (i = 0; i < NewProject.obtwd.getRewpos(); i++) {
                tmpstr[i] = br.readLine() + "\n";
            }
            for (i = 0; i <= size; i++) {
                tmpstr[NewProject.obtwd.getRewpos() + i] = rew[i];
            }
            NewProject.obtwd.incRewpos(size);
            for (i = 0; i <= NewProject.obtwd.getRewpos(); i++) {
                ByteBuffer buf = ByteBuffer.allocate(tmpstr[i].length());
                buf.clear();
                buf.put(tmpstr[i].getBytes());
                buf.flip();
                while (buf.hasRemaining()) {
                    fc.write(buf);
                }
            }
            NewProject.obtwd.incRewpos(1);
        } catch (IOException ie) {
            System.out.println(ie);
        }
    }

    public void clearRunWrite() {
        try {
            NewProject.obtwd.setRewpos(NewProject.obtwd.getAewpos());
            String tmpstr[] = new String[1000];
            fc.position(pos);
            fin = new FileInputStream(NewProject.obpd.getFilename());
            in = new DataInputStream(fin);
            br = new BufferedReader(new InputStreamReader(in));
            for (i = 0; i < NewProject.obtwd.getRewpos(); i++) {
                tmpstr[i] = br.readLine() + "\n";
            }
            NewProject.obpd.getFilename().delete();
            aFile = new RandomAccessFile(((NewProject.obpd.getFilename())), "rwd");
            fc = aFile.getChannel();
            pos = fc.position();
            for (i = 0; i < NewProject.obtwd.getRewpos(); i++) {
                ByteBuffer buf = ByteBuffer.allocate(tmpstr[i].length());
                buf.clear();
                buf.put(tmpstr[i].getBytes());
                buf.flip();
                while (buf.hasRemaining()) {
                    fc.write(buf);
                }
            }

        } catch (IOException ie) {
            System.out.println(ie);
        }
    }
}
