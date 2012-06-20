/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flexisim.views;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingWorker;

/**
 *
 * @author harshit
 */
public class CopyFiles extends SwingWorker<Void, CopyData> {

    private static final int PROGRESS_CHECKPOINT = 100;
    private File srcDir;
    private File destDir;

    CopyFiles(File src, File dest) {
        this.srcDir = src;
        this.destDir = dest;
    }

    // perform time-consuming copy task in the worker thread
    public void copy(File srcDir, File destDir) {
        int progress = 0;
        // initialize bound property progress (inherited from SwingWorker)
        setProgress(0);
        // get the files to be copied from the source directory
        File[] files = srcDir.listFiles();
        // determine the scope of the task
        long totalBytes = calcTotalBytes(files);
        long bytesCopied = 0;

        // while (progress < 100 && !isCancelled()) {
        // copy the files to the destination directory
        for (File f : files) {
            if (f.isDirectory()) {
                if (!new File(destDir, f.getName()).exists()) {
                    new File(destDir, f.getName()).mkdir();
                }
                copy(f, new File(destDir, f.getName()));
                continue;
            }
            File destFile = new File(destDir, f.getName());
            long previousLen = 0;
            try {
                InputStream in = new FileInputStream(f);
                OutputStream out = new FileOutputStream(destFile);
                byte[] buf = new byte[1024];
                int counter = 0;
                int len;

                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                    counter += len;
                    bytesCopied += (destFile.length() - previousLen);
                    previousLen = destFile.length();
                    if (counter > PROGRESS_CHECKPOINT || bytesCopied == totalBytes) {
                        // get % complete for the task
                        progress = (int) ((100 * bytesCopied) / totalBytes);
                        counter = 0;
                        CopyData current = new CopyData(progress, f.getName(),
                                getTotalKiloBytes(totalBytes),
                                getKiloBytesCopied(bytesCopied));

                        // set new value on bound property
                        // progress and fire property change event
                        setProgress(progress);

                        // publish current progress data for copy task
                        publish(current);
                    }
                }
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
//    }

    @Override
    public Void doInBackground() {
        if (srcDir.isDirectory()) {
            if (!destDir.exists()) {
                destDir.mkdir();
            }
        }
        copy(srcDir, destDir);


        return null;
    }

    // process copy task progress data in the event dispatch thread
    @Override
    public void process(List<CopyData> data) {
        if (isCancelled()) {
            return;
        }
        CopyData update = new CopyData(0, "", 0, 0);
        for (CopyData d : data) {
            // progress updates may be batched, so get the most recent
            if (d.getKiloBytesCopied() > update.getKiloBytesCopied()) {
                update = d;
            }
        }

        // update the progress monitor's status note with the
        // latest progress data from the copy operation, and
        // additionally append the note to the FlexiSimView.statusTextPane
        String progressNote = update.getKiloBytesCopied() + " of "
                + update.getTotalKiloBytes() + " kb copied.";
        String fileNameNote = "Now copying " + update.getFileName();

        if (update.getProgress() < 100) {
            FlexiSimView.progressMonitor.setNote(progressNote + " " + fileNameNote);
        } else {
            FlexiSimView.progressMonitor.setNote(progressNote);
        }
    }

    // perform final updates in the event dispatch thread
    @Override
    public void done() {
        try {
            // call get() to tell us whether the operation completed or
            // was canceled; we don't do anything with this result
            Void result = get();
            FlexiSimView.statusTextArea.append("Copy operation completed.\n");
        } catch (InterruptedException e) {
        } catch (CancellationException e) {
            // get() throws CancellationException if background task was canceled
            FlexiSimView.statusTextArea.append("Copy operation canceled.\n");
        } catch (ExecutionException e) {
            FlexiSimView.statusTextArea.append("Exception occurred: " + e.getCause());
        }
        // reset the example app
        FlexiSimView.progressMonitor.close();
        FlexiSimView.statusTextArea.append("Protocol Updated\n");
        CopyNSDirectory replaceProtocol = new CopyNSDirectory();
        CopyNSDirectory replaceNSFiles = new CopyNSDirectory();
        File src = new File(NewProject.obpd.getFile().getParent() + "/" + NewProject.obpd.getProtocolFile().getName());
        File dest = new File(NewProject.obpd.getProtocolFile().getPath());
        try {
            replaceProtocol.copyNSDirectory(src, dest);
        } catch (IOException e) {
        }
        for (int i = 0; i < 9; i++) {
            try {
                replaceNSFiles.copyNSDirectory(new File(NewProject.obpd.getFile().getParent() + "/" + FlexiSimView.nsFiles[i].getName()), FlexiSimView.nsFiles[i]);
            } catch (Exception e) {
            }
        }
        FlexiSimView.mk.execute();
        FlexiSimView.statusTextArea.append("Cleaning NS folder\n");
        JFrame mainFrame = FlexiSimApp.getApplication().getMainFrame();
        makensdialog = new MakeNSDialog(mainFrame);
        makensdialog.setLocationRelativeTo(mainFrame);
        FlexiSimApp.getApplication().show(makensdialog);
        FlexiSimView.statusTextArea.append("Making NS\n");

    }

    private long calcTotalBytes(File[] files) {
        long tmpCount = 0;
        for (File f : files) {
            tmpCount += f.length();
        }
        return tmpCount;
    }

    private long getTotalKiloBytes(long totalBytes) {
        return Math.round(totalBytes / 1024);
    }

    private long getKiloBytesCopied(long bytesCopied) {
        return Math.round(bytesCopied / 1024);
    }
    public static JDialog makensdialog;
}
