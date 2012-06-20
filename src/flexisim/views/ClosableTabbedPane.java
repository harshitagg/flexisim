/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flexisim.views;

/**
 *
 * @author harshit
 */
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.*;

public class ClosableTabbedPane extends JTabbedPane {

    private TabCloseUI closeUI = new TabCloseUI(this);

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        closeUI.paint(g);
    }

    @Override
    public void addTab(String title, Component component) {
        super.addTab(title + "  ", component);
    }

    public String getTabTitleAt(int index) {
        return super.getTitleAt(index).trim();
    }

    private class TabCloseUI implements MouseListener, MouseMotionListener {

        private ClosableTabbedPane tabbedPane;
        private int closeX = 0, closeY = 0, meX = 0, meY = 0;
        private int selectedTab;
        private final int width = 8, height = 8;
        private Rectangle rectangle = new Rectangle(0, 0, width, height);

        private TabCloseUI() {
        }

        public TabCloseUI(ClosableTabbedPane pane) {
            tabbedPane = pane;
            tabbedPane.addMouseMotionListener(this);
            tabbedPane.addMouseListener(this);
        }

        public void mouseEntered(MouseEvent me) {
        }

        public void mouseExited(MouseEvent me) {
        }

        public void mousePressed(MouseEvent me) {
        }

        public void mouseClicked(MouseEvent me) {
        }

        public void mouseDragged(MouseEvent me) {
        }

        public void mouseReleased(MouseEvent me) {
            if (closeUnderMouse(me.getX(), me.getY())) {
                boolean isToCloseTab = tabAboutToClose(selectedTab);
                if (isToCloseTab && selectedTab > -1) {
                    int tabIndex;
                    int less = 0;
                    for (int i = 0; i < FlexiSimView.noOfTabsClosed; i++) {
                        if (selectedTab >= FlexiSimView.TabClosed[i]) {
                            less++;
                        }
                    }
                    tabIndex = selectedTab - 1 + less;
                    if (FlexiSimView.tabs[tabIndex].isChanged()) {
                        String tabName = tabbedPane.getTabTitleAt(selectedTab);
                        JFrame mainFrame = FlexiSimApp.getApplication().getMainFrame();
                        Object[] options = {"Cancel", "Don't Save", "Save"};
                        int n = JOptionPane.showOptionDialog(mainFrame, "Files " + tabName + " is modified.", "FlexiSim", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
                        if (n == 0) {
                            closeTab = false;
                        } else if (n == 1) {
                            closeTab = true;
                        } else if (n == 2) {
                            String content = FlexiSimView.tabs[tabIndex].getText();
                            File file1;
                            try {
                                file1 = new File(NewProject.obpd.getOpenTabsPath(tabIndex));
                                FileOutputStream fop = new FileOutputStream(file1);
                                if (file1.createNewFile()) {
                                    byte bufn[] = new byte[content.length()];
                                    bufn = content.getBytes();
                                    fop.write(bufn);
                                    fop.close();
                                } else {
                                    FileOutputStream erasor = new FileOutputStream(file1);
                                    erasor.write((new String()).getBytes());
                                    erasor.close();
                                    byte bufn[] = new byte[content.length()];
                                    bufn = content.getBytes();
                                    fop.write(bufn);
                                    fop.close();
                                }
                            } catch (IOException e) {
                                System.out.println(e);
                            }
                            closeTab = true;
                        }
                    }
                    if (closeTab) {
                        FlexiSimView.TabClosed[FlexiSimView.noOfTabsClosed] = selectedTab;
                        tabbedPane.removeTabAt(selectedTab);
                        FlexiSimView.noOfTabsClosed++;
                    }
                }
                selectedTab = tabbedPane.getSelectedIndex();
            }
        }

        public void mouseMoved(MouseEvent me) {
            meX = me.getX();
            meY = me.getY();
            if (mouseOverTab(meX, meY)) {
                controlCursor();
                tabbedPane.repaint();
            }
        }

        private void controlCursor() {
            if (tabbedPane.getTabCount() > 0) {
                if (closeUnderMouse(meX, meY)) {
                    tabbedPane.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    if (selectedTab > -1) {
                        tabbedPane.setToolTipTextAt(selectedTab, "Close " + tabbedPane.getTitleAt(selectedTab));
                    }
                } else {
                    tabbedPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    if (selectedTab > -1) {
                        tabbedPane.setToolTipTextAt(selectedTab, "");
                    }
                }
            }
        }

        private boolean closeUnderMouse(int x, int y) {
            rectangle.x = closeX;
            rectangle.y = closeY;
            return rectangle.contains(x, y);
        }

        public void paint(Graphics g) {

            int tabCount = tabbedPane.getTabCount();
            for (int j = 1; j < tabCount; j++) {
                if (tabbedPane.getComponent(j).isShowing()) {
                    int x = tabbedPane.getBoundsAt(j).x + tabbedPane.getBoundsAt(j).width - width - 665;
                    int y = tabbedPane.getBoundsAt(j).y + 5;
                    drawClose(g, x, y);
                    break;
                }
            }
            if (mouseOverTab(meX, meY)) {
                drawClose(g, closeX, closeY);
            }
        }

        private void drawClose(Graphics g, int x, int y) {
            if (tabbedPane != null && tabbedPane.getTabCount() > 0) {
                Graphics2D g2 = (Graphics2D) g;
                drawColored(g2, isUnderMouse(x, y) ? Color.BLACK : Color.WHITE, x, y);
            }
        }

        private void drawColored(Graphics2D g2, Color color, int x, int y) {
            g2.setStroke(new BasicStroke(5, BasicStroke.JOIN_ROUND, BasicStroke.CAP_ROUND));
            g2.setColor(Color.BLACK);
            g2.drawLine(x, y, x + width, y + height);
            g2.drawLine(x + width, y, x, y + height);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(3, BasicStroke.JOIN_ROUND, BasicStroke.CAP_ROUND));
            g2.drawLine(x, y, x + width, y + height);
            g2.drawLine(x + width, y, x, y + height);

        }

        private boolean isUnderMouse(int x, int y) {
            if (Math.abs(x - meX) < width && Math.abs(y - meY) < height) {
                return true;
            }
            return false;
        }

        private boolean mouseOverTab(int x, int y) {
            int tabCount = tabbedPane.getTabCount();
            for (int j = 1; j < tabCount; j++) {
                if (tabbedPane.getBoundsAt(j).contains(meX, meY)) {
                    selectedTab = j;
                    closeX = tabbedPane.getBoundsAt(j).x + tabbedPane.getBoundsAt(j).width - width - 5;
                    closeY = tabbedPane.getBoundsAt(j).y + 5;
                    return true;
                }
            }
            return false;
        }
    }

    public boolean tabAboutToClose(int tabIndex) {
        return true;
    }
    public static boolean closeTab = true;
}
