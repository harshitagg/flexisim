/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package flexisim.views;

/**
 *
 * @author harshit
 */
 public class CopyData {
        private int progress;
        private String fileName;
        private long totalKiloBytes;
        private long kiloBytesCopied;

        CopyData(int progress, String fileName, long totalKiloBytes, long kiloBytesCopied) {
            this.progress = progress;
            this.fileName = fileName;
            this.totalKiloBytes = totalKiloBytes;
            this.kiloBytesCopied = kiloBytesCopied;
        }

        int getProgress() {
            return progress;
        }

        String getFileName() {
            return fileName;
        }

        long getTotalKiloBytes() {
            return totalKiloBytes;
        }

        long getKiloBytesCopied() {
            return kiloBytesCopied;
        }
    }
