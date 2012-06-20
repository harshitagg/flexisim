/*
 * FlexiSimView.java
 */
package flexisim.views;

import flexisim.node.movement.NodeMovement;
import flexisim.node.position.NodePosition;
import flexisim.event.NewEvent;
import flexisim.node.NewNode;
import flexisim.link.NewLink;
import flexisim.agent.ConnectAgents;
import flexisim.agent.NewAgent;
import java.beans.PropertyChangeEvent;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import java.io.*;
import javax.swing.JFileChooser;
import flexisim.agent.AgentManager;
import flexisim.application.ApplicationManager;
import flexisim.event.EventData;
import flexisim.link.LinkManager;
import flexisim.node.NodeManager;
import flexisim.node.movement.NodeMovementData;
import flexisim.node.position.NodePositionData;
import flexisim.routing.protocols.NewProtocol;
import java.beans.PropertyChangeListener;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;

/**
 * The application's main frame.
 */
public class FlexiSimView extends FrameView implements PropertyChangeListener {

    public static File file;
    private File openProjectDirectory;
    FileOutputStream fos = null;
    ObjectOutputStream oos = null;
    FileInputStream fin = null;
    ObjectInputStream ois = null;
    public static File nsFiles[] = new File[9];

    public FlexiSimView(SingleFrameApplication app) {
        super(app);
        i = 0;
        initComponents();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int l = 0; l < busyIcons.length; l++) {
            busyIcons[l] = resourceMap.getIcon("StatusBar.busyIcons[" + l + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    public static void codeTextAreaWrite() {
        try {
            FileReader f = new FileReader(NewProject.obpd.getFilename());
            BufferedReader br = new BufferedReader(f);
            try {
                String line = null;
                codeTextArea.setText("");
                while ((line = br.readLine()) != null) {
                    codeTextArea.append(line + "\n");
                }
                NewProject.obpd.setSaved(false);
            } finally {
                br.close();
            }
        } catch (FileNotFoundException fe) {
            JFrame mainFrame = FlexiSimApp.getApplication().getMainFrame();
            JOptionPane.showMessageDialog(mainFrame, "File not found.", "File Not Found Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static void tabCreater(String tabName) {
        tabs[i] = new FilePanel();
        spane[i] = new javax.swing.JScrollPane();
        NewProject.obpd.setI(i);
        NewProject.obpd.setOpentabs(true, i);
        NewProject.obpd.setOpentabsname(tabName, i);
        tabs[i].setColumns(20);
        tabs[i].setRows(5);
        tabs[i].setName(tabName);
        spane[i].setViewportView(tabs[i]);
        fileTabbedPane.addTab(tabName, spane[i]);
        i++;
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = FlexiSimApp.getApplication().getMainFrame();
            aboutBox = new FlexiSimAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        FlexiSimApp.getApplication().show(aboutBox);
    }

    @Action
    public void showNewNodeBox() {
        JFrame mainFrame = FlexiSimApp.getApplication().getMainFrame();
        newNode = new NewNode(mainFrame, true);
        newNode.setLocationRelativeTo(mainFrame);
        FlexiSimApp.getApplication().show(newNode);
    }

    @Action
    public void showNewLinkBox() {
        JFrame mainFrame = FlexiSimApp.getApplication().getMainFrame();
        newLink = new NewLink(mainFrame);
        newLink.setLocationRelativeTo(mainFrame);
        FlexiSimApp.getApplication().show(newLink);
    }

    @Action
    public void showNewAgentBox() {
        JFrame mainFrame = FlexiSimApp.getApplication().getMainFrame();
        newAgent = new NewAgent(mainFrame);
        newAgent.setLocationRelativeTo(mainFrame);
        FlexiSimApp.getApplication().show(newAgent);
    }

    @Action
    public void showConnectAgentsBox() {
        JFrame mainFrame = FlexiSimApp.getApplication().getMainFrame();
        newConnection = new ConnectAgents(mainFrame);
        newConnection.setLocationRelativeTo(mainFrame);
        FlexiSimApp.getApplication().show(newConnection);
    }

    @Action
    public void showNewEventBox() {
        JFrame mainFrame = FlexiSimApp.getApplication().getMainFrame();
        newevent = new NewEvent(mainFrame);
        newevent.setLocationRelativeTo(mainFrame);
        FlexiSimApp.getApplication().show(newevent);
    }

    @Action
    public void showRunBox() {
        JFrame mainFrame = FlexiSimApp.getApplication().getMainFrame();
        run = new Run(mainFrame);
        run.setLocationRelativeTo(mainFrame);
        FlexiSimApp.getApplication().show(run);
    }

    @Action
    public void showNodeConfigBox() {
        JFrame mainFrame = FlexiSimApp.getApplication().getMainFrame();
        nodeconfiguration = new NodeConfig(mainFrame);
        nodeconfiguration.setLocationRelativeTo(mainFrame);
        FlexiSimApp.getApplication().show(nodeconfiguration);
    }

    @Action
    public void showNodePostionBox() {
        JFrame mainFrame = FlexiSimApp.getApplication().getMainFrame();
        nodeposition = new NodePosition(mainFrame);
        nodeposition.setLocationRelativeTo(mainFrame);
        FlexiSimApp.getApplication().show(nodeposition);
    }

    @Action
    public void showNodeMovementBox() {
        JFrame mainFrame = FlexiSimApp.getApplication().getMainFrame();
        nodemovement = new NodeMovement(mainFrame);
        nodemovement.setLocationRelativeTo(mainFrame);
        FlexiSimApp.getApplication().show(nodemovement);
    }

    @Action
    public void showNewProtocolBox() {
        JFrame mainFrame = FlexiSimApp.getApplication().getMainFrame();
        newprotocol = new NewProtocol(mainFrame);
        newprotocol.setLocationRelativeTo(mainFrame);
        FlexiSimApp.getApplication().show(newprotocol);
    }

    @Action
    public void showNewProtocolFileBox() {
        JFrame mainFrame = FlexiSimApp.getApplication().getMainFrame();
        newprotocolfile = new NewFile(mainFrame);
        newprotocol.setLocationRelativeTo(mainFrame);
        FlexiSimApp.getApplication().show(newprotocolfile);
    }

    @Action
    public void showConfigureTopographyBox() {
        JFrame mainFrame = FlexiSimApp.getApplication().getMainFrame();
        configuretopography = new ConfigureTopography(mainFrame, true);
        configuretopography.setLocationRelativeTo(mainFrame);
        FlexiSimApp.getApplication().show(configuretopography);
    }

    @Action
    public void showCompileFileBox() {
        if (NewProject.obpd.isSaved()) {
            JFrame mainFrame = FlexiSimApp.getApplication().getMainFrame();
            compilefile = new CompileFile(mainFrame);
            compilefile.setLocationRelativeTo(mainFrame);
            FlexiSimApp.getApplication().show(compilefile);
        } else {
            JFrame mainFrame = FlexiSimApp.getApplication().getMainFrame();
            JOptionPane.showMessageDialog(mainFrame, "Project not saved.", "Compile Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        statusTabbedPane = new javax.swing.JTabbedPane();
        statPanel = new javax.swing.JPanel();
        statusScrollPane = new javax.swing.JScrollPane();
        statusTextArea = new javax.swing.JTextArea();
        runScrollPane = new javax.swing.JScrollPane();
        runTextArea = new javax.swing.JTextArea();
        fileTabbedPane = new ClosableTabbedPane();
        filePanel = new javax.swing.JPanel();
        codeScrollPane = new javax.swing.JScrollPane();
        codeTextArea = new javax.swing.JTextArea();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        openMenuItem = new javax.swing.JMenuItem();
        newFileMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        toolsMenu = new javax.swing.JMenu();
        newNodeMenuItem = new javax.swing.JMenuItem();
        newLinkMenuItem = new javax.swing.JMenuItem();
        newAgentMenuItem = new javax.swing.JMenuItem();
        newConnectionMenuItem = new javax.swing.JMenuItem();
        eventsMenuItem = new javax.swing.JMenuItem();
        newProtocol = new javax.swing.JMenu();
        createNewProtocolMenuItem = new javax.swing.JMenuItem();
        modifyExistingProtocolMenuItem = new javax.swing.JMenuItem();
        configureMenu = new javax.swing.JMenu();
        nodeMenuItem = new javax.swing.JMenuItem();
        nodePositionMenuItem = new javax.swing.JMenuItem();
        nodeMovementMenuItem = new javax.swing.JMenuItem();
        topographyMenuItem = new javax.swing.JMenuItem();
        runMenu = new javax.swing.JMenu();
        runMenuItem = new javax.swing.JMenuItem();
        compileMenuItem = new javax.swing.JMenuItem();
        makeMenuItem = new javax.swing.JMenuItem();
        tracegraphMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        newFileChooser = new javax.swing.JFileChooser();

        mainPanel.setName("mainPanel"); // NOI18N

        statusTabbedPane.setName("statusTabbedPane"); // NOI18N
        statusTabbedPane.setFocusable(false);

        statPanel.setName("statPanel"); // NOI18N

        statusScrollPane.setName("statusScrollPane"); // NOI18N

        statusTextArea.setColumns(20);
        statusTextArea.setRows(5);
        statusTextArea.setName("statusTextArea"); // NOI18N
        statusScrollPane.setViewportView(statusTextArea);
        statusTextArea.setEditable(false);

        javax.swing.GroupLayout statPanelLayout = new javax.swing.GroupLayout(statPanel);
        statPanel.setLayout(statPanelLayout);
        statPanelLayout.setHorizontalGroup(
            statPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 491, Short.MAX_VALUE)
            .addGroup(statPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(statusScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE))
        );
        statPanelLayout.setVerticalGroup(
            statPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 157, Short.MAX_VALUE)
            .addGroup(statPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(statusScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE))
        );

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(flexisim.views.FlexiSimApp.class).getContext().getResourceMap(FlexiSimView.class);
        statusTabbedPane.addTab(resourceMap.getString("statPanel.TabConstraints.tabTitle"), statPanel); // NOI18N

        runScrollPane.setName("runScrollPane"); // NOI18N

        runTextArea.setColumns(20);
        runTextArea.setRows(5);
        runTextArea.setName("runTextArea"); // NOI18N
        runScrollPane.setViewportView(runTextArea);
        runTextArea.setEditable(false);

        statusTabbedPane.addTab(resourceMap.getString("runScrollPane.TabConstraints.tabTitle"), runScrollPane); // NOI18N

        fileTabbedPane.setFocusable(false);
        fileTabbedPane.setName("fileTabbedPane"); // NOI18N

        filePanel.setName("filePanel"); // NOI18N

        codeScrollPane.setName("codeScrollPane"); // NOI18N

        codeTextArea.setColumns(20);
        codeTextArea.setRows(5);
        codeTextArea.setName("codeTextArea"); // NOI18N
        codeScrollPane.setViewportView(codeTextArea);
        codeTextArea.setEditable(false);
        codeTextArea.setSize(mainPanel.getWidth()-statusTabbedPane.getWidth(), mainPanel.getHeight()-statusTabbedPane.getHeight());

        javax.swing.GroupLayout filePanelLayout = new javax.swing.GroupLayout(filePanel);
        filePanel.setLayout(filePanelLayout);
        filePanelLayout.setHorizontalGroup(
            filePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(codeScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
        );
        filePanelLayout.setVerticalGroup(
            filePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(codeScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
        );

        codeScrollPane.setSize(mainPanel.getWidth()-statusTabbedPane.getWidth(), mainPanel.getHeight()-statusTabbedPane.getHeight());

        fileTabbedPane.addTab(resourceMap.getString("filePanel.TabConstraints.tabTitle"), filePanel); // NOI18N
        filePanel.setSize(mainPanel.getWidth()-statusTabbedPane.getWidth(), mainPanel.getHeight()-statusTabbedPane.getHeight());

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(fileTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE)
            .addComponent(statusTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addComponent(fileTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        fileTabbedPane.setSize(mainPanel.getWidth()-statusTabbedPane.getWidth(), mainPanel.getHeight()-statusTabbedPane.getHeight());
        fileTabbedPane.getAccessibleContext().setAccessibleName(resourceMap.getString("fileTabbedPane.AccessibleContext.accessibleName")); // NOI18N

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setMnemonic('F');
        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        newMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newMenuItem.setMnemonic('N');
        newMenuItem.setText(resourceMap.getString("newMenuItem.text")); // NOI18N
        newMenuItem.setToolTipText(resourceMap.getString("newMenuItem.toolTipText")); // NOI18N
        newMenuItem.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        newMenuItem.setName("newMenuItem"); // NOI18N
        newMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(newMenuItem);
        newMenuItem.getAccessibleContext().setAccessibleDescription(resourceMap.getString("newMenuItem.AccessibleContext.accessibleDescription")); // NOI18N

        openMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        openMenuItem.setMnemonic('O');
        openMenuItem.setText(resourceMap.getString("openMenuItem.text")); // NOI18N
        openMenuItem.setToolTipText(resourceMap.getString("openMenuItem.toolTipText")); // NOI18N
        openMenuItem.setName("openMenuItem"); // NOI18N
        openMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(openMenuItem);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(flexisim.views.FlexiSimApp.class).getContext().getActionMap(FlexiSimView.class, this);
        newFileMenuItem.setAction(actionMap.get("showNewProtocolFileBox")); // NOI18N
        newFileMenuItem.setText(resourceMap.getString("newFileMenuItem.text")); // NOI18N
        newFileMenuItem.setToolTipText(resourceMap.getString("newFileMenuItem.toolTipText")); // NOI18N
        newFileMenuItem.setName("newFileMenuItem"); // NOI18N
        newFileMenuItem.setEnabled(false);
        fileMenu.add(newFileMenuItem);

        saveMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveMenuItem.setMnemonic('S');
        saveMenuItem.setText(resourceMap.getString("saveMenuItem.text")); // NOI18N
        saveMenuItem.setToolTipText(resourceMap.getString("saveMenuItem.toolTipText")); // NOI18N
        saveMenuItem.setName("saveMenuItem"); // NOI18N
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveMenuItem);
        saveMenuItem.setEnabled(false);

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);
        fileMenu.getAccessibleContext().setAccessibleDescription(resourceMap.getString("fileMenu.AccessibleContext.accessibleDescription")); // NOI18N

        toolsMenu.setMnemonic('T');
        toolsMenu.setText(resourceMap.getString("toolsMenu.text")); // NOI18N

        newNodeMenuItem.setAction(actionMap.get("showNewNodeBox")); // NOI18N
        newNodeMenuItem.setText(resourceMap.getString("newNodeMenuItem.text")); // NOI18N
        newNodeMenuItem.setName("newNodeMenuItem"); // NOI18N
        toolsMenu.add(newNodeMenuItem);
        newNodeMenuItem.setEnabled(false);

        newLinkMenuItem.setAction(actionMap.get("showNewLinkBox")); // NOI18N
        newLinkMenuItem.setText(resourceMap.getString("newLinkMenuItem.text")); // NOI18N
        newLinkMenuItem.setName("newLinkMenuItem"); // NOI18N
        toolsMenu.add(newLinkMenuItem);
        newLinkMenuItem.setEnabled(false);

        newAgentMenuItem.setAction(actionMap.get("showNewAgentBox")); // NOI18N
        newAgentMenuItem.setText(resourceMap.getString("newAgentMenuItem.text")); // NOI18N
        newAgentMenuItem.setName("newAgentMenuItem");
        toolsMenu.add(newAgentMenuItem);
        newAgentMenuItem.setEnabled(false);

        newConnectionMenuItem.setAction(actionMap.get("showConnectAgentsBox")); // NOI18N
        newConnectionMenuItem.setText(resourceMap.getString("newConnectionMenuItem.text")); // NOI18N
        newConnectionMenuItem.setName("newConnectionMenuItem"); // NOI18N
        toolsMenu.add(newConnectionMenuItem);
        newConnectionMenuItem.setEnabled(false);

        eventsMenuItem.setAction(actionMap.get("showNewEventBox")); // NOI18N
        eventsMenuItem.setText(resourceMap.getString("eventsMenuItem.text")); // NOI18N
        eventsMenuItem.setName("eventsMenuItem"); // NOI18N
        toolsMenu.add(eventsMenuItem);
        eventsMenuItem.setEnabled(false);

        newProtocol.setText(resourceMap.getString("newProtocol.text")); // NOI18N
        newProtocol.setName("newProtocol"); // NOI18N

        createNewProtocolMenuItem.setAction(actionMap.get("showNewProtocolBox")); // NOI18N
        createNewProtocolMenuItem.setText(resourceMap.getString("createNewProtocolMenuItem.text")); // NOI18N
        createNewProtocolMenuItem.setName("createNewProtocolMenuItem"); // NOI18N
        newProtocol.add(createNewProtocolMenuItem);
        createNewProtocolMenuItem.setEnabled(false);

        modifyExistingProtocolMenuItem.setText(resourceMap.getString("modifyExistingProtocolMenuItem.text")); // NOI18N
        modifyExistingProtocolMenuItem.setName("modifyExistingProtocolMenuItem"); // NOI18N
        modifyExistingProtocolMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modifyExistingProtocolMenuItemActionPerformed(evt);
            }
        });
        modifyExistingProtocolMenuItem.setEnabled(false);
        newProtocol.add(modifyExistingProtocolMenuItem);

        toolsMenu.add(newProtocol);

        menuBar.add(toolsMenu);

        configureMenu.setMnemonic('C');
        configureMenu.setText(resourceMap.getString("configureMenu.text")); // NOI18N
        configureMenu.setName("configureMenu"); // NOI18N

        nodeMenuItem.setAction(actionMap.get("showNodeConfigBox")); // NOI18N
        nodeMenuItem.setText(resourceMap.getString("nodeMenuItem.text")); // NOI18N
        nodeMenuItem.setName("nodeMenuItem"); // NOI18N
        nodeMenuItem.setEnabled(false);
        configureMenu.add(nodeMenuItem);

        nodePositionMenuItem.setAction(actionMap.get("showNodePostionBox")); // NOI18N
        nodePositionMenuItem.setText(resourceMap.getString("nodePositionMenuItem.text")); // NOI18N
        nodePositionMenuItem.setName("nodePositionMenuItem"); // NOI18N
        nodePositionMenuItem.setEnabled(false);
        configureMenu.add(nodePositionMenuItem);

        nodeMovementMenuItem.setAction(actionMap.get("showNodeMovementBox")); // NOI18N
        nodeMovementMenuItem.setText(resourceMap.getString("nodeMovementMenuItem.text")); // NOI18N
        nodeMovementMenuItem.setName("nodeMovementMenuItem"); // NOI18N
        nodeMovementMenuItem.setEnabled(false);
        configureMenu.add(nodeMovementMenuItem);

        topographyMenuItem.setAction(actionMap.get("showConfigureTopographyBox")); // NOI18N
        topographyMenuItem.setText(resourceMap.getString("topographyMenuItem.text")); // NOI18N
        topographyMenuItem.setName("topographyMenuItem"); // NOI18N
        topographyMenuItem.setEnabled(false);
        configureMenu.add(topographyMenuItem);

        menuBar.add(configureMenu);

        runMenu.setMnemonic('R');
        runMenu.setText(resourceMap.getString("runMenu.text")); // NOI18N
        runMenu.setName("runMenu"); // NOI18N

        runMenuItem.setAction(actionMap.get("showRunBox")); // NOI18N
        runMenuItem.setText(resourceMap.getString("runMenuItem.text")); // NOI18N
        runMenuItem.setName("runMenuItem"); // NOI18N
        runMenuItem.setEnabled(false);
        runMenu.add(runMenuItem);

        compileMenuItem.setAction(actionMap.get("showCompileFileBox")); // NOI18N
        compileMenuItem.setText(resourceMap.getString("compileMenuItem.text")); // NOI18N
        compileMenuItem.setName("compileMenuItem"); // NOI18N
        compileMenuItem.setEnabled(false);
        runMenu.add(compileMenuItem);

        makeMenuItem.setText(resourceMap.getString("makeMenuItem.text")); // NOI18N
        makeMenuItem.setName("makeMenuItem"); // NOI18N
        makeMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                makeMenuItemActionPerformed(evt);
            }
        });
        makeMenuItem.setEnabled(false);
        runMenu.add(makeMenuItem);

        tracegraphMenuItem.setText(resourceMap.getString("tracegraphMenuItem.text")); // NOI18N
        tracegraphMenuItem.setName("tracegraphMenuItem"); // NOI18N
        tracegraphMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tracegraphMenuItemActionPerformed(evt);
            }
        });
        tracegraphMenuItem.setEnabled(false);
        runMenu.add(tracegraphMenuItem);

        menuBar.add(runMenu);

        helpMenu.setMnemonic('H');
        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 319, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        newFileChooser.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
        newFileChooser.setName("newFileChooser"); // NOI18N

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void newMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMenuItemActionPerformed
        // TODO add your handling code here:

        JFrame mainFrame = FlexiSimApp.getApplication().getMainFrame();
        newproject = new NewProject(mainFrame);
        newproject.setLocationRelativeTo(mainFrame);
        FlexiSimApp.getApplication().show(newproject);
    }//GEN-LAST:event_newMenuItemActionPerformed

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItemActionPerformed
        // TODO add your handling code here:
        try {
            fos = new FileOutputStream(file + ".oblm");
            oos = new ObjectOutputStream(fos);
            oos.writeObject(NewProject.oblm);
            oos.close();
            fos = new FileOutputStream(file + ".obam");
            oos = new ObjectOutputStream(fos);
            oos.writeObject(NewProject.obam);
            oos.close();
            fos = new FileOutputStream(file + ".obed");
            oos = new ObjectOutputStream(fos);
            oos.writeObject(NewProject.obed);
            oos.close();
            fos = new FileOutputStream(file + ".obnm");
            oos = new ObjectOutputStream(fos);
            oos.writeObject(NewProject.obnm);
            oos.close();
            fos = new FileOutputStream(file + ".obnmd");
            oos = new ObjectOutputStream(fos);
            oos.writeObject(NewProject.obnmd);
            oos.close();
            fos = new FileOutputStream(file + ".obnpd");
            oos = new ObjectOutputStream(fos);
            oos.writeObject(NewProject.obnpd);
            oos.close();
            fos = new FileOutputStream(file + ".obappm");
            oos = new ObjectOutputStream(fos);
            oos.writeObject(NewProject.obappm);
            oos.close();
            fos = new FileOutputStream(file + ".obtcl");
            oos = new ObjectOutputStream(fos);
            oos.writeObject(NewProject.obtwd);
            oos.close();
            if (NewProject.obpd.isProtocol()) {
                fos = new FileOutputStream(file + ".obprd");
                oos = new ObjectOutputStream(fos);
                oos.writeObject(NewProject.obprd);
                oos.close();
                File file1;
                for (int j = 1; j < fileTabbedPane.getTabCount(); j++) {
                    for (k = 0; k < i; k++) {
                        if (fileTabbedPane.getTitleAt(j).equals(NewProject.obpd.getOpentabsname(k)) && NewProject.obpd.isOpentabs(k)) {
                            try {
                                if (NewProject.obpd.getModifyprotocoltab(k)) {
                                    file1 = new File(file.getParent() + "//" + NewProject.obpd.getProtocolFile().getName() + "//" + NewProject.obpd.getOpentabsname(k));
                                } else {
                                    file1 = new File(NewProject.obprd.getProtocolFile().getParent() + "//" + NewProject.obpd.getOpentabsname(k));
                                }
                                FileOutputStream fop = new FileOutputStream(file1);
                                if (file1.createNewFile()) {
                                    String temp;
                                    temp = tabs[k].getText();
                                    byte bufn[] = new byte[temp.length()];
                                    bufn = temp.getBytes();
                                    fop.write(bufn);
                                    fop.close();
                                } else {
                                    FileOutputStream erasor = new FileOutputStream(file1);
                                    erasor.write((new String()).getBytes());
                                    erasor.close();
                                    String temp;
                                    temp = tabs[k].getText();
                                    byte bufn[] = new byte[temp.length()];
                                    bufn = temp.getBytes();
                                    fop.write(bufn);
                                    fop.close();
                                }
                            } catch (IOException e) {
                                System.out.println(e);
                            }
                            NewProject.obpd.setProtocolFiles(fileTabbedPane.getTitleAt(j), NewProject.obpd.getProtocolFileCounter(), NewProject.obpd.getModifyprotocoltab(k));
                            tabModified[k] = false;
                        }
                    }
                }
            }
            fos = new FileOutputStream(file + ".obpd");
            oos = new ObjectOutputStream(fos);
            oos.writeObject(NewProject.obpd);
            oos.close();
            statusTextArea.append("Project Successfully saved.\n");
            NewProject.obpd.setSaved(true);
        } catch (IOException ie) {
            System.out.println(ie);
        }
    }//GEN-LAST:event_saveMenuItemActionPerformed

    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItemActionPerformed
        // TODO add your handling code here:
        openProject = new JFileChooser();
        openProject.setDialogTitle("Select Project Location");
        openProject.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        openProject.setAcceptAllFileFilterUsed(false);
        if (openProject.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            openProjectDirectory = openProject.getSelectedFile();
            try {
                fin = new FileInputStream(openProjectDirectory + "/" + openProjectDirectory.getName() + ".obnm");
                ois = new ObjectInputStream(fin);
                NewProject.obnm = (NodeManager) ois.readObject();
                ois.close();
                fin = new FileInputStream(openProjectDirectory + "/" + openProjectDirectory.getName() + ".oblm");
                ois = new ObjectInputStream(fin);
                NewProject.oblm = (LinkManager) ois.readObject();
                ois.close();
                fin = new FileInputStream(openProjectDirectory + "/" + openProjectDirectory.getName() + ".obam");
                ois = new ObjectInputStream(fin);
                NewProject.obam = (AgentManager) ois.readObject();
                ois.close();
                fin = new FileInputStream(openProjectDirectory + "/" + openProjectDirectory.getName() + ".obed");
                ois = new ObjectInputStream(fin);
                NewProject.obed = (EventData) ois.readObject();
                ois.close();
                fin = new FileInputStream(openProjectDirectory + "/" + openProjectDirectory.getName() + ".obnmd");
                ois = new ObjectInputStream(fin);
                NewProject.obnmd = (NodeMovementData) ois.readObject();
                ois.close();
                fin = new FileInputStream(openProjectDirectory + "/" + openProjectDirectory.getName() + ".obnpd");
                ois = new ObjectInputStream(fin);
                NewProject.obnpd = (NodePositionData) ois.readObject();
                ois.close();
                fin = new FileInputStream(openProjectDirectory + "/" + openProjectDirectory.getName() + ".obappm");
                ois = new ObjectInputStream(fin);
                NewProject.obappm = (ApplicationManager) ois.readObject();
                ois.close();
                fin = new FileInputStream(openProjectDirectory + "/" + openProjectDirectory.getName() + ".obtcl");
                ois = new ObjectInputStream(fin);
                NewProject.obtwd = (TclWriteData) ois.readObject();
                ois.close();
                fin = new FileInputStream(openProjectDirectory + "/" + openProjectDirectory.getName() + ".obpd");
                ois = new ObjectInputStream(fin);
                NewProject.obpd = (ProjectData) ois.readObject();
                ois.close();
                if (NewProject.obtwd.isFileCreated()) {
                    NewProject.obpd.setFilename(new File(openProjectDirectory + "/" + openProjectDirectory.getName() + ".tcl"));
                    NewProject.obpd.setFile(new File(openProjectDirectory + "/" + openProjectDirectory.getName()));
                }
                newNodeMenuItem.setEnabled(true);
                saveMenuItem.setEnabled(true);
                if (NewProject.obnm.nodeNum() >= 1 && NewProject.obpd.getNettype() == 1) {
                    nodeMenuItem.setEnabled(true);
                    nodePositionMenuItem.setEnabled(true);
                    newAgentMenuItem.setEnabled(true);
                    runMenuItem.setEnabled(true);
                }
                if (NewProject.obnm.nodeNum() >= 2 && NewProject.obpd.getNettype() == 0) {
                    newLinkMenuItem.setEnabled(true);
                    newAgentMenuItem.setEnabled(true);
                    runMenuItem.setEnabled(true);
                    if (NewProject.oblm.linkNum() > 0) {
                        eventsMenuItem.setEnabled(true);
                    }
                }
                if (NewProject.obam.getAgentNum() > 1) {
                    newConnectionMenuItem.setEnabled(true);
                }
                statusTextArea.append("Project Successfully Opened.\n");
                int p = 0;
                while (NewProject.obpd.getLog(p) != null) {
                    statusTextArea.append(NewProject.obpd.getLog(p));
                    p++;
                }
            } catch (FileNotFoundException fe) {
                statusTextArea.append("Files Not Found.\n");
            } catch (Exception ie) {
                System.err.println(ie);
            }
            codeTextAreaWrite();
        }
    }//GEN-LAST:event_openMenuItemActionPerformed

    private void modifyExistingProtocolMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modifyExistingProtocolMenuItemActionPerformed
        nsFiles[0] = new File(System.getenv("NS") + "common/packet.h");
        nsFiles[1] = new File(System.getenv("NS") + "tcl/lib/ns-default.tcl");
        nsFiles[2] = new File(System.getenv("NS") + "tcl/lib/ns-agent.tcl");
        nsFiles[3] = new File(System.getenv("NS") + "tcl/lib/ns-mobilenode.tcl");
        nsFiles[4] = new File(System.getenv("NS") + "tcl/lib/ns-packet.tcl");
        nsFiles[5] = new File(System.getenv("NS") + "tcl/lib/ns-lib.tcl");
        nsFiles[6] = new File(System.getenv("NS") + "trace/cmu-trace.cc");
        nsFiles[7] = new File(System.getenv("NS") + "trace/cmu-trace.h");
        nsFiles[8] = new File(System.getenv("NS") + "queue/priqueue.cc");
        modifyprotocol = new JFileChooser();
        //directoryChooser.setCurrentDirectory(current);
        modifyprotocol.setCurrentDirectory(new File(NewProject.obpd.getNsInstallationPath()));
        //ExtensionFileFilter filter1 = new ExtensionFileFilter(".h and .cc", new String[]{"h", "cc"});
        //modifyprotocol.setFileFilter(filter1);
        modifyprotocol.setDialogTitle("Select Protocol Folder");
        modifyprotocol.setAcceptAllFileFilterUsed(false);
        modifyprotocol.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (modifyprotocol.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            NewProject.obpd.setProtocolFile(modifyprotocol.getSelectedFile());
            //File f = new File(file.getParent() + "//" + NewProject.obpd.getProtocolFile().getName());
            CopyDirectory cd = new CopyDirectory();
            try {
                File f = new File(NewProject.obpd.getFile().getParent() + "/" + modifyprotocol.getSelectedFile().getName());
                cd.copyDirectory(NewProject.obpd.getProtocolFile(), f);
                for (int i = 0; i < 9; i++) {
                    cd.copyDirectory(nsFiles[i], new File(NewProject.obpd.getFile().getParent() + "/" + nsFiles[i].getName()));
                }
                /* if (f.createNewFile()) {
                FileInputStream fIn;
                FileOutputStream fOut;
                FileChannel fIChan, fOChan;
                long fSize;
                MappedByteBuffer mBuf;
                fIn = new FileInputStream(NewProject.obpd.getProtocolFile());
                fOut = new FileOutputStream(f);
                fIChan = fIn.getChannel();
                fOChan = fOut.getChannel();
                fSize = fIChan.size();
                mBuf = fIChan.map(FileChannel.MapMode.READ_ONLY, 0, fSize);
                fOChan.write(mBuf);
                fIChan.close();
                fIn.close();
                fOChan.close();
                fOut.close();
                NewProject.obpd.setModifyprotocoltab(true, i);
                tabCreater(NewProject.obpd.getProtocolFile().getName());
                FileReader fr = new FileReader(f);
                BufferedReader br = new BufferedReader(fr);
                String line = null;
                while ((line = br.readLine()) != null) {
                tabs[NewProject.obpd.getI() - 1].append(line + "\n");
                }
                br.close();
                statusTextArea.append("File Successfully Opened.\n");
                NewProject.obpd.setProtocol(true);
                }*/

            } catch (FileNotFoundException fe) {
                JFrame mainFrame = FlexiSimApp.getApplication().getMainFrame();
                JOptionPane.showMessageDialog(mainFrame, "File Not Found.", "File Not Found Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                System.out.println(e);
            }
            statusTextArea.append("File Successfully Opened.\n");
            makeMenuItem.setEnabled(true);
            compileMenuItem.setEnabled(true);
            NewProject.obpd.setProtocol(true);
            FlexiSimView.modifyExistingProtocolMenuItem.setEnabled(false);
        }

    }//GEN-LAST:event_modifyExistingProtocolMenuItemActionPerformed

    private void makeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_makeMenuItemActionPerformed
      if(!NewProject.obpd.isSaved()){
         JFrame mainFrame = FlexiSimApp.getApplication().getMainFrame();
            JOptionPane.showMessageDialog(mainFrame, "Save the project before make.", "Project Not Saved", JOptionPane.ERROR_MESSAGE);
      }
        File src = new File(NewProject.obpd.getNsInstallationPath());
        File dest = new File(NewProject.obpd.getFile().getParent() + "/" + "backup");
        // create the progress monitor
        progressMonitor = new ProgressMonitor(makeMenuItem, "Operation in progress...", "", 0, 100);
        progressMonitor.setProgress(0);
        // schedule the copy files operation for execution on a background thread
        operation = new CopyFiles(src, dest);
        // add ProgressMonitorExample as a listener on CopyFiles;
        // of specific interest is the bound property progress
        operation.addPropertyChangeListener(this);
        operation.execute();
       
        mk = new MakeNS();
        mk.addPropertyChangeListener(this);
    }//GEN-LAST:event_makeMenuItemActionPerformed

    private void tracegraphMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tracegraphMenuItemActionPerformed
        opentgraph = new OpenTraceGraph();
        opentgraph.execute();
    }//GEN-LAST:event_tracegraphMenuItemActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane codeScrollPane;
    public static javax.swing.JTextArea codeTextArea;
    public static javax.swing.JMenuItem compileMenuItem;
    private javax.swing.JMenu configureMenu;
    private javax.swing.JMenuItem createNewProtocolMenuItem;
    public static javax.swing.JMenuItem eventsMenuItem;
    private javax.swing.JPanel filePanel;
    public static javax.swing.JTabbedPane fileTabbedPane;
    private javax.swing.JPanel mainPanel;
    public static javax.swing.JMenuItem makeMenuItem;
    private javax.swing.JMenuBar menuBar;
    public static javax.swing.JMenuItem modifyExistingProtocolMenuItem;
    public static javax.swing.JMenuItem newAgentMenuItem;
    public static javax.swing.JMenuItem newConnectionMenuItem;
    private javax.swing.JFileChooser newFileChooser;
    private javax.swing.JMenuItem newFileMenuItem;
    public static javax.swing.JMenuItem newLinkMenuItem;
    private javax.swing.JMenuItem newMenuItem;
    public static javax.swing.JMenuItem newNodeMenuItem;
    private javax.swing.JMenu newProtocol;
    public static javax.swing.JMenuItem nodeMenuItem;
    public static javax.swing.JMenuItem nodeMovementMenuItem;
    public static javax.swing.JMenuItem nodePositionMenuItem;
    public static javax.swing.JMenuItem openMenuItem;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JMenu runMenu;
    public static javax.swing.JMenuItem runMenuItem;
    private javax.swing.JScrollPane runScrollPane;
    public static javax.swing.JTextArea runTextArea;
    public static javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JPanel statPanel;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JScrollPane statusScrollPane;
    public static javax.swing.JTabbedPane statusTabbedPane;
    public static javax.swing.JTextArea statusTextArea;
    private javax.swing.JMenu toolsMenu;
    public static javax.swing.JMenuItem topographyMenuItem;
    public static javax.swing.JMenuItem tracegraphMenuItem;
    // End of variables declaration//GEN-END:variables
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
    private JDialog newNode;
    private JDialog newLink;
    private JDialog newAgent;
    private JDialog newConnection;
    private JDialog newevent;
    private JDialog run;
    private JFileChooser openProject;
    private JDialog nodeconfiguration;
    private JDialog nodeposition;
    private JDialog nodemovement;
    private JDialog newprotocol;
    private JDialog newproject;
    private JDialog newprotocolfile;
    private JDialog compilefile;
    private JDialog configuretopography;
    public static FilePanel tabs[] = new FilePanel[20];
    private static javax.swing.JScrollPane spane[] = new javax.swing.JScrollPane[20];
    static int i = 0;
    static int k = 0;
    private javax.swing.JFileChooser modifyprotocol;
    public static boolean tabModified[] = new boolean[20];
    public static int TabClosed[] = new int[20];
    public static int noOfTabsClosed;
    public static ProgressMonitor progressMonitor;
    private CopyFiles operation;
    public static MakeNS mk;
    private OpenTraceGraph opentgraph;

    public void propertyChange(PropertyChangeEvent evt) {
        // if the operation is finished or has been canceled by
        // the user, take appropriate action
        if (progressMonitor.isCanceled()) {
            operation.cancel(true);
        } else if (evt.getPropertyName().equals("progress")) {
            // get the % complete from the progress event
            // and set it on the progress monitor
            int progress = ((Integer) evt.getNewValue()).intValue();
            progressMonitor.setProgress(progress);
        }
    }
}
