package jtides;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;

public final class JTides
  extends JFrame
{
  boolean appInstalled = false;
  public static final String programName = "JTides 5.3";
  public static final String buildNumber = "368";
  private static final String requiredJavaVersion = "1.6.0";
  public static final String indexName = "JTidesData.index";
  public static final String dataDirName = "JTidesData";
  public static final String userDirName = ".JTides";
  public static final String helpBrowserName = "Help Browser";
  public static final String siteExplorerName = "Site Explorer";
  public static int updateIntervalMillis = 15000;
  public static int returnToCurrentTimeDelayMillis = 600000;
  String configPath;
  Dimension screenSize;
  Dimension appSize;
  Timer timer;
  public ConfigValues configValues;
  public InitFileHandler initFileHandler;
  public String appDir;
  public String userHome;
  public String basePath;
  public ImageIcon frameIcon;
  public TidesDoc currentFinder = null;
  public TidesDoc currentHelpBrowser = null;
  public TidesDoc currentSelectedFrame = null;
  public JPopupMenu activeMenu = null;
  TideComp tideComp;
  SunComp sunComp;
  RecentFileList recentFileList;
  Clipboard clipboard;
  TabbedPaneManager tabbedPaneManager;
  NonModalDialog installDialog = null;
  private JButton BackDayButton;
  private JButton BackMonthButton;
  private JButton BackYearButton;
  private JPanel Bottom_Panel;
  private JButton CalendarButton;
  private JMenuItem ClearSiteList;
  private JMenuItem Close;
  private JMenuItem CloseAll;
  private JButton Configure;
  private JMenuItem ConfigureMenuItem;
  private JButton Decrease;
  private JMenuItem Exit;
  private JButton FindNearestButton;
  private JButton ForwardDayButton;
  private JButton ForwardMonthButton;
  private JButton ForwardYearButton;
  private JButton HelpButton;
  private JButton HomeButton;
  private JButton Increase;
  private JMenuItem NearestSitesMenuItem;
  private JButton OneDay;
  private JButton OneMonth;
  private JButton OneMonthSmooth;
  private JMenuItem OpenMenuItem;
  private JButton OpenSelector;
  private JButton Print;
  private JMenuItem PrintMenuItem;
  private JToolBar Toolbar;
  private JButton aboutButton;
  private JMenuItem aboutMenuItem;
  private JPopupMenu filePopupMenu;
  private JMenuItem helpMenuItem1;
  private JProgressBar jProgressBar1;
  private JSeparator jSeparator3;
  private JSeparator jSeparator4;
  private JSeparator jSeparator5;
  private JSeparator jSeparator6;
  public JSeparator jSeparator7;
  private JSeparator jSeparator9;
  private JButton siteMenuButton;
  private JLabel statusBar;
  private JTabbedPane tabbedPane;
  
  public JTides()
  {
    testJavaVersion();
    this.userHome = System.getProperty("user.home");
    this.basePath = (this.userHome + TideConstants.SYSTEM_FILESEP + ".JTides");
    this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    this.tideComp = new TideComp(this);
    this.appDir = locateAppDir();
    this.sunComp = new SunComp(this);
    this.configValues = new ConfigValues();
    this.configPath = (this.basePath + TideConstants.SYSTEM_FILESEP + "JTides.ini");
    this.initFileHandler = new InitFileHandler(this.configPath);
    this.initFileHandler.read(this.configValues);
    setTitle("JTides 5.3");
    initComponents();
    this.tabbedPaneManager = new TabbedPaneManager(this.tabbedPane, this);
    String str = TideConstants.barPositions[this.configValues.toolBarPosition];
    getContentPane().add(this.Toolbar, str);
    this.Toolbar.setOrientation(this.configValues.toolBarPosition % 2 == 0 ? 0 : 1);
    pack();
    setSize(this.screenSize.width * 3 / 4, this.screenSize.height * 3 / 4);
    setLocation(this.screenSize.width / 8, this.screenSize.height / 8);
    setVisible(true);
    this.frameIcon = new ImageIcon(getClass().getResource("icons/JTides.png"));
    setIconImage(this.frameIcon.getImage());
    this.recentFileList = new RecentFileList(this, this.configValues.recentListSize, this.filePopupMenu);
    this.tideComp.setupLookAndFeel(this.configValues.LookAndFeel);
    this.statusBar.setFont(new Font("Monospaced", 0, this.statusBar.getFont().getSize()));
    unpackJarTest(this.appDir, "JTides.jar", "JTidesData", this.basePath);
    initPhase2();
  }
  
  public void initPhase2()
  {
    if (this.installDialog != null)
    {
      this.installDialog.setVisible(false);
      this.installDialog.dispose();
    }
    this.recentFileList.putAll(this.configValues.recentSiteList);
    if (this.tideComp.indexValid)
    {
      openFileList(this.configValues.openSites, true);
      this.tabbedPaneManager.setSelectedIndex(this.configValues.selectedSite);
      this.tabbedPaneManager.refocus();
    }
    else
    {
      regenerateIndex(true);
    }
    setupTimer();
  }
  
  private void testJavaVersion()
  {
    String str = System.getProperty("java.version");
    int i = convertVersionString(str) >= convertVersionString("1.6.0") ? 1 : 0;
    if (i == 0)
    {
      JOptionPane.showMessageDialog(this, "Very Sorry!\n\nJTides 5.3 requires a Java runtime engine (JRE)\nwith a version of 1.6.0 or better.\nThis machine has a version " + str + " JRE.\nPlease acquire the correct JRE at http://java.com.", "Wrong Java Runtime Version", 2);
      System.exit(0);
    }
  }
  
  private int convertVersionString(String paramString)
  {
    try
    {
      int i = paramString.length();
      for (int j = 0; (j < i) && (!Character.isDigit(paramString.charAt(j))); j++) {}
      StringBuffer localStringBuffer = new StringBuffer();
      char c = ' ';
      while (((j < i) && (Character.isDigit(c = paramString.charAt(j)))) || (c == '.'))
      {
        if (Character.isDigit(c)) {
          localStringBuffer.append(c);
        }
        j++;
      }
      int k = Integer.parseInt(localStringBuffer.toString());
      while ((k > 0) && (k < 100)) {
        k *= 10;
      }
      return k;
    }
    catch (Exception localException) {}
    return 0;
  }
  
  public void setActiveMenu(JPopupMenu paramJPopupMenu)
  {
    if (this.activeMenu != null) {
      this.activeMenu.setVisible(false);
    }
    this.activeMenu = paramJPopupMenu;
  }
  
  private void giveBackFocus(String paramString)
  {
    this.tabbedPaneManager.refocus();
  }
  
  private void giveBackFocus()
  {
    giveBackFocus("default");
  }
  
  private String locateAppDir()
  {
    URL localURL = getClass().getResource("JTides.class");
    File localFile = new File(localURL.getPath());
    String str1 = localFile.getPath();
    int i = str1.lastIndexOf(TideConstants.SYSTEM_FILESEP);
    if (i != -1)
    {
      str1 = str1.substring(0, i);
      i = str1.lastIndexOf(".jar");
      if (i != -1)
      {
        i = str1.lastIndexOf(TideConstants.SYSTEM_FILESEP, i);
        if (i != -1) {
          str1 = str1.substring(0, i);
        }
      }
    }
    String str2 = "file:";
    i = str1.indexOf(str2);
    if (i != -1) {
      str1 = str1.substring(i + str2.length());
    }
    str1 = this.tideComp.srchRplc(str1, "%20", " ");
    return str1;
  }
  
  public void regenerateIndex(boolean paramBoolean)
  {
    indexInit(this.configValues.openSites, paramBoolean);
  }
  
  private void setupTimer()
  {
    this.timer = new Timer(updateIntervalMillis, new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (JTides.this.isVisible()) {
          JTides.this.repaint();
        }
        if (JTides.this.appInstalled)
        {
          if (JTides.this.installDialog != null) {
            JTides.this.installDialog.dispose();
          }
          JTides.this.appInstalled = false;
        }
      }
    });
    this.timer.start();
  }
  
  public void unpackJarTest(final String paramString1, final String paramString2, final String paramString3, final String paramString4)
  {
    this.appInstalled = false;
    Thread local2 = new Thread()
    {
      public void run()
      {
        JTides.this.unpackJarTestThread(paramString1, paramString2, paramString3, paramString4);
      }
    };
    local2.start();
    try
    {
      local2.join();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    initPhase2();
  }
  
  public void unpackJarTestThread(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    try
    {
      File localFile = new File(paramString4);
      if (!localFile.exists())
      {
        this.installDialog = new NonModalDialog(this, "Preparing JTides 5.3 for use on\nyour system. Please stand by.", "JTides 5.3 Data File Preparation", false);
        Dimension localDimension = this.installDialog.getSize();
        this.installDialog.setLocation((this.screenSize.width - localDimension.width) / 2, (this.screenSize.height - localDimension.height) / 2);
        this.installDialog.setVisible(true);
        localFile.mkdir();
        String str1 = paramString1 + TideConstants.SYSTEM_FILESEP + paramString2;
        ZipFile localZipFile = new ZipFile(str1);
        Enumeration localEnumeration = localZipFile.entries();
        while (localEnumeration.hasMoreElements())
        {
          ZipEntry localZipEntry = (ZipEntry)localEnumeration.nextElement();
          String str2 = localZipEntry.toString();
          if (((str2.endsWith(".txt")) || (str2.endsWith(".xxx"))) && (str2.startsWith("JTidesData")))
          {
            int i = str2.lastIndexOf("/");
            str2 = str2.substring(i + 1);
            String str3 = paramString4 + TideConstants.SYSTEM_FILESEP + str2;
            copyFile(localZipFile, localZipEntry, str3);
          }
        }
        localZipFile.close();
        this.appInstalled = true;
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  public void copyFile(ZipFile paramZipFile, ZipEntry paramZipEntry, String paramString)
  {
    try
    {
      BufferedInputStream localBufferedInputStream = new BufferedInputStream(paramZipFile.getInputStream(paramZipEntry));
      int i = localBufferedInputStream.available();
      startProgressBar("Program Installation: processing " + paramString, 0L, i, 0L);
      BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(new FileOutputStream(new File(paramString)));
      int j = 2048;
      byte[] arrayOfByte = new byte[j];
      int k = 0;
      while ((i = localBufferedInputStream.read(arrayOfByte, 0, j)) != -1)
      {
        updateProgressBar(k);
        localBufferedOutputStream.write(arrayOfByte, 0, i);
        k += i;
      }
      localBufferedInputStream.close();
      localBufferedOutputStream.close();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    stopProgressBar();
  }
  
  public void openFileList(String paramString, boolean paramBoolean)
  {
    paramString = paramString.trim();
    if (!this.tideComp.indexReplaced)
    {
      if (paramString.length() > 0)
      {
        Vector localVector = this.tideComp.parseDelimLine(paramString, "|");
        for (int i = 0; i < localVector.size(); i++)
        {
          String str = (String)localVector.elementAt(i);
          openFile(str, false);
        }
      }
      else if (paramBoolean)
      {
        openFileOnPartialPath(this.configValues.defaultOpenSite);
      }
    }
    else
    {
      this.recentFileList.clear();
      this.configValues.openSites = "";
      openFileOnPartialPath(this.configValues.defaultOpenSite);
    }
    this.tideComp.indexReplaced = false;
  }
  
  public void openFileOnPartialPath(String paramString)
  {
    SortedSet localSortedSet = this.tideComp.siteIndex.tailSet(paramString);
    if (localSortedSet != null)
    {
      String str = (String)localSortedSet.first();
      openFile(str, true);
    }
  }
  
  public void updateStatusBar(String paramString)
  {
    this.statusBar.setText(paramString);
    if (this.statusBar.isVisible()) {
      this.statusBar.validate();
    }
  }
  
  public void startProgressBar(String paramString, long paramLong1, long paramLong2, long paramLong3)
  {
    updateStatusBar(paramString);
    this.jProgressBar1.setStringPainted(true);
    this.jProgressBar1.setMinimum((int)paramLong1);
    this.jProgressBar1.setMaximum((int)paramLong2);
    this.jProgressBar1.setValue((int)paramLong2);
  }
  
  public void updateProgressBar(long paramLong)
  {
    if (paramLong % 10L == 0L) {
      this.jProgressBar1.setValue((int)paramLong);
    }
  }
  
  public void stopProgressBar()
  {
    this.jProgressBar1.setValue(this.jProgressBar1.getMinimum());
    this.jProgressBar1.setStringPainted(false);
    updateStatusBar("Done (F1 = Help)");
  }
  
  private void lfActionPerformed(ActionEvent paramActionEvent)
  {
    String str = paramActionEvent.getActionCommand();
    try
    {
      UIManager.setLookAndFeel(str);
      SwingUtilities.updateComponentTreeUI(this);
    }
    catch (Exception localException) {}
  }
  
  private void showAboutDialog()
  {
    ImageIcon localImageIcon = new ImageIcon(getClass().getResource("icons/Me_nautical_tiny.jpg"));
    String str = "<HTML><BODY><FONT color=\"#000000\">JTides 5.3 build 368<p>is copyright 2011, P. Lutus.<p>Please visit the JTides Home Page<p>at <font color=#0000ff>www.arachnoid.com/JTides</font>.<p>Please read about CareWare<p>at <font color=#0000ff>www.arachnoid.com/careware</font>.</FONT></BODY></HTML>";
    JOptionPane.showMessageDialog(this, str, "About JTides 5.3", 1, localImageIcon);
  }
  
  private void initComponents()
  {
    this.filePopupMenu = new JPopupMenu();
    this.OpenMenuItem = new JMenuItem();
    this.Close = new JMenuItem();
    this.CloseAll = new JMenuItem();
    this.PrintMenuItem = new JMenuItem();
    this.NearestSitesMenuItem = new JMenuItem();
    this.ConfigureMenuItem = new JMenuItem();
    this.jSeparator9 = new JSeparator();
    this.helpMenuItem1 = new JMenuItem();
    this.aboutMenuItem = new JMenuItem();
    this.Exit = new JMenuItem();
    this.ClearSiteList = new JMenuItem();
    this.jSeparator7 = new JSeparator();
    this.Toolbar = new JToolBar();
    this.siteMenuButton = new MyJButton();
    this.jSeparator6 = new JSeparator();
    this.OpenSelector = new MyJButton();
    this.Print = new MyJButton();
    this.Configure = new MyJButton();
    this.FindNearestButton = new MyJButton();
    this.jSeparator3 = new JSeparator();
    this.BackYearButton = new MyJButton();
    this.BackMonthButton = new MyJButton();
    this.BackDayButton = new MyJButton();
    this.HomeButton = new MyJButton();
    this.ForwardDayButton = new MyJButton();
    this.ForwardMonthButton = new MyJButton();
    this.ForwardYearButton = new MyJButton();
    this.jSeparator4 = new JSeparator();
    this.OneDay = new MyJButton();
    this.Decrease = new MyJButton();
    this.Increase = new MyJButton();
    this.OneMonth = new MyJButton();
    this.OneMonthSmooth = new MyJButton();
    this.CalendarButton = new MyJButton();
    this.jSeparator5 = new JSeparator();
    this.HelpButton = new MyJButton();
    this.aboutButton = new MyJButton();
    this.Bottom_Panel = new JPanel();
    this.statusBar = new JLabel();
    this.jProgressBar1 = new JProgressBar();
    this.tabbedPane = new JTabbedPane();
    this.OpenMenuItem.setText("Open...");
    this.OpenMenuItem.setToolTipText("Open Site Explorer");
    this.OpenMenuItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JTides.this.OpenMenuItemActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.filePopupMenu.add(this.OpenMenuItem);
    this.Close.setText("Close");
    this.Close.setToolTipText("Close Displayed Site");
    this.Close.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JTides.this.CloseActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.filePopupMenu.add(this.Close);
    this.CloseAll.setText("Close All");
    this.CloseAll.setToolTipText("Close All Open Sites");
    this.CloseAll.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JTides.this.CloseAllActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.filePopupMenu.add(this.CloseAll);
    this.PrintMenuItem.setText("Print...");
    this.PrintMenuItem.setToolTipText("Print Displayed Site");
    this.PrintMenuItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JTides.this.PrintMenuItemActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.filePopupMenu.add(this.PrintMenuItem);
    this.NearestSitesMenuItem.setText("Nearest Sites...");
    this.NearestSitesMenuItem.setToolTipText("Launch nearest sites dialog");
    this.NearestSitesMenuItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JTides.this.NearestSitesMenuItemActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.filePopupMenu.add(this.NearestSitesMenuItem);
    this.ConfigureMenuItem.setText("Configure/Data...");
    this.ConfigureMenuItem.setToolTipText("Launch configuration dialog");
    this.ConfigureMenuItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JTides.this.ConfigureMenuItemActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.filePopupMenu.add(this.ConfigureMenuItem);
    this.filePopupMenu.add(this.jSeparator9);
    this.helpMenuItem1.setAccelerator(KeyStroke.getKeyStroke(112, 0));
    this.helpMenuItem1.setText("Help");
    this.helpMenuItem1.setToolTipText("Launch help browser");
    this.helpMenuItem1.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JTides.this.helpMenuItem1ActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.filePopupMenu.add(this.helpMenuItem1);
    this.aboutMenuItem.setText("About...");
    this.aboutMenuItem.setToolTipText("Information about JTides");
    this.aboutMenuItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JTides.this.aboutMenuItemActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.filePopupMenu.add(this.aboutMenuItem);
    this.Exit.setText("Exit");
    this.Exit.setToolTipText("Exit this program");
    this.Exit.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JTides.this.ExitActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.filePopupMenu.add(this.Exit);
    this.ClearSiteList.setText("Clear Site List");
    this.ClearSiteList.setToolTipText("Erase previously visited site list");
    this.ClearSiteList.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JTides.this.ClearSiteListActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.filePopupMenu.add(this.ClearSiteList);
    this.filePopupMenu.add(this.jSeparator7);
    addWindowListener(new WindowAdapter()
    {
      public void windowActivated(WindowEvent paramAnonymousWindowEvent)
      {
        JTides.this.formWindowActivated(paramAnonymousWindowEvent);
      }
      
      public void windowClosing(WindowEvent paramAnonymousWindowEvent)
      {
        JTides.this.exitForm(paramAnonymousWindowEvent);
      }
    });
    addComponentListener(new ComponentAdapter()
    {
      public void componentResized(ComponentEvent paramAnonymousComponentEvent)
      {
        JTides.this.formComponentResized(paramAnonymousComponentEvent);
      }
    });
    addContainerListener(new ContainerAdapter()
    {
      public void componentRemoved(ContainerEvent paramAnonymousContainerEvent)
      {
        JTides.this.formComponentRemoved(paramAnonymousContainerEvent);
      }
    });
    addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        JTides.this.formKeyPressed(paramAnonymousKeyEvent);
      }
    });
    this.Toolbar.setToolTipText("This toolbar can be floated and/or redocked");
    this.siteMenuButton.setText("Site Menu");
    this.siteMenuButton.setToolTipText("Site-related functions");
    this.siteMenuButton.setMargin(new Insets(2, 4, 2, 4));
    this.siteMenuButton.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
      {
        JTides.this.menuButtonMouseClicked(paramAnonymousMouseEvent);
      }
    });
    this.Toolbar.add(this.siteMenuButton);
    this.jSeparator6.setOrientation(1);
    this.Toolbar.add(this.jSeparator6);
    this.OpenSelector.setIcon(new ImageIcon(getClass().getResource("/jtides/icons/Folder.png")));
    this.OpenSelector.setToolTipText("Open site explorer");
    this.OpenSelector.setActionCommand("Open");
    this.OpenSelector.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JTides.this.OpenSelectorActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.Toolbar.add(this.OpenSelector);
    this.Print.setIcon(new ImageIcon(getClass().getResource("/jtides/icons/Print.png")));
    this.Print.setToolTipText("Print displayed chart");
    this.Print.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JTides.this.PrintActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.Toolbar.add(this.Print);
    this.Configure.setIcon(new ImageIcon(getClass().getResource("/jtides/icons/DocumentMag.png")));
    this.Configure.setToolTipText("Configuration/Database dialog");
    this.Configure.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JTides.this.ConfigureActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.Toolbar.add(this.Configure);
    this.FindNearestButton.setIcon(new ImageIcon(getClass().getResource("/jtides/icons/World2.png")));
    this.FindNearestButton.setToolTipText("Find nearest sites");
    this.FindNearestButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JTides.this.FindNearestButtonActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.Toolbar.add(this.FindNearestButton);
    this.jSeparator3.setOrientation(1);
    this.Toolbar.add(this.jSeparator3);
    this.BackYearButton.setIcon(new ImageIcon(getClass().getResource("/jtides/icons/Left.png")));
    this.BackYearButton.setToolTipText("Back 1 year (Page Up key)");
    this.BackYearButton.setFocusable(false);
    this.BackYearButton.setHorizontalTextPosition(0);
    this.BackYearButton.setVerticalTextPosition(3);
    this.BackYearButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JTides.this.BackYearButtonActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.Toolbar.add(this.BackYearButton);
    this.BackMonthButton.setIcon(new ImageIcon(getClass().getResource("/jtides/icons/VCRRewind.png")));
    this.BackMonthButton.setToolTipText("Back 1 month (Up Arrow key)");
    this.BackMonthButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JTides.this.BackMonthButtonActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.Toolbar.add(this.BackMonthButton);
    this.BackDayButton.setIcon(new ImageIcon(getClass().getResource("/jtides/icons/VCRBack.png")));
    this.BackDayButton.setToolTipText("Back 1 day (Left Arrow key)");
    this.BackDayButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JTides.this.BackDayButtonActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.Toolbar.add(this.BackDayButton);
    this.HomeButton.setIcon(new ImageIcon(getClass().getResource("/jtides/icons/VCRStop.png")));
    this.HomeButton.setToolTipText("Today (Home key)");
    this.HomeButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JTides.this.HomeButtonActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.Toolbar.add(this.HomeButton);
    this.ForwardDayButton.setIcon(new ImageIcon(getClass().getResource("/jtides/icons/VCRForward.png")));
    this.ForwardDayButton.setToolTipText("Forward 1 day (Right Arrow key)");
    this.ForwardDayButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JTides.this.ForwardDayButtonActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.Toolbar.add(this.ForwardDayButton);
    this.ForwardMonthButton.setIcon(new ImageIcon(getClass().getResource("/jtides/icons/VCRFastForward.png")));
    this.ForwardMonthButton.setToolTipText("Forward 1 month (Down Arrow key)");
    this.ForwardMonthButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JTides.this.ForwardMonthButtonActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.Toolbar.add(this.ForwardMonthButton);
    this.ForwardYearButton.setIcon(new ImageIcon(getClass().getResource("/jtides/icons/Right.png")));
    this.ForwardYearButton.setToolTipText("Forward 1 year (Page Down key)");
    this.ForwardYearButton.setFocusable(false);
    this.ForwardYearButton.setHorizontalTextPosition(0);
    this.ForwardYearButton.setVerticalTextPosition(3);
    this.ForwardYearButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JTides.this.ForwardYearButtonActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.Toolbar.add(this.ForwardYearButton);
    this.jSeparator4.setOrientation(1);
    this.Toolbar.add(this.jSeparator4);
    this.OneDay.setIcon(new ImageIcon(getClass().getResource("/jtides/icons/Magnify.png")));
    this.OneDay.setToolTipText("1 Day chart");
    this.OneDay.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JTides.this.OneDayActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.Toolbar.add(this.OneDay);
    this.Decrease.setIcon(new ImageIcon(getClass().getResource("/jtides/icons/MagnifyMinus.png")));
    this.Decrease.setToolTipText("Decrease chart time");
    this.Decrease.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JTides.this.DecreaseActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.Toolbar.add(this.Decrease);
    this.Increase.setIcon(new ImageIcon(getClass().getResource("/jtides/icons/MagnifyPlus.png")));
    this.Increase.setToolTipText("Increase chart time");
    this.Increase.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JTides.this.IncreaseActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.Toolbar.add(this.Increase);
    this.OneMonth.setIcon(new ImageIcon(getClass().getResource("/jtides/icons/InitProject.png")));
    this.OneMonth.setToolTipText("Clickable 30/60 day color chart");
    this.OneMonth.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JTides.this.OneMonthActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.Toolbar.add(this.OneMonth);
    this.OneMonthSmooth.setIcon(new ImageIcon(getClass().getResource("/jtides/icons/Palette.png")));
    this.OneMonthSmooth.setToolTipText("Clickable smooth 30/60 day chart (draws slower)");
    this.OneMonthSmooth.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JTides.this.OneMonthSmoothActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.Toolbar.add(this.OneMonthSmooth);
    this.CalendarButton.setIcon(new ImageIcon(getClass().getResource("/jtides/icons/Document.png")));
    this.CalendarButton.setToolTipText("Calendar for current month");
    this.CalendarButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JTides.this.CalendarActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.Toolbar.add(this.CalendarButton);
    this.jSeparator5.setOrientation(1);
    this.Toolbar.add(this.jSeparator5);
    this.HelpButton.setIcon(new ImageIcon(getClass().getResource("/jtides/icons/Help.png")));
    this.HelpButton.setToolTipText("Help Browser");
    this.HelpButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JTides.this.HelpButtonActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.Toolbar.add(this.HelpButton);
    this.aboutButton.setIcon(new ImageIcon(getClass().getResource("/jtides/icons/Options.png")));
    this.aboutButton.setToolTipText("About JTides");
    this.aboutButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JTides.this.aboutButtonActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.Toolbar.add(this.aboutButton);
    getContentPane().add(this.Toolbar, "North");
    this.Bottom_Panel.setFocusable(false);
    this.Bottom_Panel.setLayout(new BorderLayout());
    this.statusBar.setHorizontalAlignment(2);
    this.statusBar.setText("Status Area");
    this.statusBar.setToolTipText("Operation Status");
    this.Bottom_Panel.add(this.statusBar, "Center");
    this.jProgressBar1.setToolTipText("Event Progress");
    this.jProgressBar1.setMinimumSize(new Dimension(10, 22));
    this.jProgressBar1.setName("Progress Bar");
    this.jProgressBar1.setPreferredSize(new Dimension(148, 22));
    this.jProgressBar1.setStringPainted(true);
    this.Bottom_Panel.add(this.jProgressBar1, "East");
    getContentPane().add(this.Bottom_Panel, "South");
    this.tabbedPane.setTabPlacement(3);
    this.tabbedPane.setToolTipText("Click here for Site Explorer");
    this.tabbedPane.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
      {
        JTides.this.tabbedPaneMouseClicked(paramAnonymousMouseEvent);
      }
    });
    this.tabbedPane.addFocusListener(new FocusAdapter()
    {
      public void focusGained(FocusEvent paramAnonymousFocusEvent)
      {
        JTides.this.tabbedPaneFocusGained(paramAnonymousFocusEvent);
      }
    });
    getContentPane().add(this.tabbedPane, "Center");
  }
  
  private void aboutButtonActionPerformed(ActionEvent paramActionEvent)
  {
    showAboutDialog();
  }
  
  private void tabbedPaneMouseClicked(MouseEvent paramMouseEvent)
  {
    if (this.tabbedPaneManager.getTabCount() == 0) {
      openFile("Site Explorer", true);
    } else {
      this.tabbedPaneManager.refocus();
    }
  }
  
  private void tabbedPaneFocusGained(FocusEvent paramFocusEvent)
  {
    this.tabbedPaneManager.refocus();
  }
  
  private void formKeyPressed(KeyEvent paramKeyEvent) {}
  
  private void helpMenuItem1ActionPerformed(ActionEvent paramActionEvent)
  {
    openFile("Help Browser", true);
    giveBackFocus();
  }
  
  private void formComponentRemoved(ContainerEvent paramContainerEvent)
  {
    myExit();
  }
  
  private void aboutMenuItemActionPerformed(ActionEvent paramActionEvent)
  {
    showAboutDialog();
  }
  
  private void NearestSitesMenuItemActionPerformed(ActionEvent paramActionEvent)
  {
    TidesFindNearest localTidesFindNearest = new TidesFindNearest(this, false);
    giveBackFocus();
  }
  
  private void ConfigureMenuItemActionPerformed(ActionEvent paramActionEvent)
  {
    launchConfiguration();
    giveBackFocus();
  }
  
  private void OpenMenuItemActionPerformed(ActionEvent paramActionEvent)
  {
    openFile("Site Explorer", true);
    giveBackFocus();
  }
  
  private void menuButtonMouseClicked(MouseEvent paramMouseEvent)
  {
    Component localComponent = paramMouseEvent.getComponent();
    Point localPoint = localComponent.getLocation();
    this.filePopupMenu.show(this.Toolbar, localPoint.x, localPoint.y + localComponent.getSize().height);
    giveBackFocus();
  }
  
  private void PrintMenuItemActionPerformed(ActionEvent paramActionEvent)
  {
    TidesDoc localTidesDoc = this.currentSelectedFrame;
    if (localTidesDoc != null) {
      ((TidesDoc)localTidesDoc).panel.doPrint();
    }
    giveBackFocus();
  }
  
  private void OneMonthSmoothActionPerformed(ActionEvent paramActionEvent)
  {
    if ((isChartType(false)) || (getChartWidth() != 30)) {
      setChartWidth(30, true);
    } else {
      setChartWidth(60, true);
    }
  }
  
  private void HelpButtonActionPerformed(ActionEvent paramActionEvent)
  {
    openFile("Help Browser", true);
    giveBackFocus();
  }
  
  private void FindNearestButtonActionPerformed(ActionEvent paramActionEvent)
  {
    TidesFindNearest localTidesFindNearest = new TidesFindNearest(this, false);
    giveBackFocus();
  }
  
  private void CalendarActionPerformed(ActionEvent paramActionEvent)
  {
    setChartWidth(-2);
  }
  
  private int getChartWidth()
  {
    TidesDoc localTidesDoc = this.currentSelectedFrame;
    if ((localTidesDoc != null) && (localTidesDoc.type == 0)) {
      return localTidesDoc.panel.graphWidth;
    }
    return 0;
  }
  
  private boolean isChartType(boolean paramBoolean)
  {
    TidesDoc localTidesDoc = this.currentSelectedFrame;
    if ((localTidesDoc != null) && (localTidesDoc.type == 0)) {
      return localTidesDoc.panel.smoothGraph == paramBoolean;
    }
    return false;
  }
  
  private void setChartWidth(int paramInt)
  {
    TidesDoc localTidesDoc = this.currentSelectedFrame;
    if ((localTidesDoc != null) && (localTidesDoc.type == 0))
    {
      localTidesDoc.panel.setChartWidth(paramInt);
      localTidesDoc.newDisplay();
    }
    giveBackFocus();
  }
  
  private void setChartWidth(int paramInt, boolean paramBoolean)
  {
    TidesDoc localTidesDoc = this.currentSelectedFrame;
    if ((localTidesDoc != null) && (localTidesDoc.type == 0))
    {
      localTidesDoc.panel.setChartWidth(paramInt);
      localTidesDoc.panel.smoothGraph = paramBoolean;
      localTidesDoc.newDisplay();
    }
    giveBackFocus();
  }
  
  private void OneMonthActionPerformed(ActionEvent paramActionEvent)
  {
    if ((isChartType(true)) || (getChartWidth() != 30)) {
      setChartWidth(30, false);
    } else {
      setChartWidth(60, false);
    }
  }
  
  private void IncreaseActionPerformed(ActionEvent paramActionEvent)
  {
    setChartWidth(1);
  }
  
  private void DecreaseActionPerformed(ActionEvent paramActionEvent)
  {
    setChartWidth(-1);
  }
  
  private void OneDayActionPerformed(ActionEvent paramActionEvent)
  {
    setChartWidth(0);
  }
  
  private void ExitActionPerformed(ActionEvent paramActionEvent)
  {
    myExit();
  }
  
  private void ClearSiteListActionPerformed(ActionEvent paramActionEvent)
  {
    this.recentFileList.clear();
    giveBackFocus();
  }
  
  private void ForwardMonthButtonActionPerformed(ActionEvent paramActionEvent)
  {
    moveTime(2, 1);
  }
  
  private void ForwardDayButtonActionPerformed(ActionEvent paramActionEvent)
  {
    moveTime(5, 1);
  }
  
  private void HomeButtonActionPerformed(ActionEvent paramActionEvent)
  {
    moveTime(0, 0);
  }
  
  private void BackDayButtonActionPerformed(ActionEvent paramActionEvent)
  {
    moveTime(5, -1);
  }
  
  private void BackMonthButtonActionPerformed(ActionEvent paramActionEvent)
  {
    moveTime(2, -1);
  }
  
  private void moveTime(int paramInt1, int paramInt2)
  {
    TidesDoc localTidesDoc = this.currentSelectedFrame;
    if ((localTidesDoc != null) && (localTidesDoc.type == 0)) {
      localTidesDoc.panel.moveTime(paramInt1, paramInt2);
    }
    giveBackFocus();
  }
  
  private void PrintActionPerformed(ActionEvent paramActionEvent)
  {
    TidesDoc localTidesDoc = this.currentSelectedFrame;
    if ((localTidesDoc != null) && (((TidesDoc)localTidesDoc).type == 0)) {
      ((TidesDoc)localTidesDoc).panel.doPrint();
    }
    giveBackFocus();
  }
  
  private void ConfigureActionPerformed(ActionEvent paramActionEvent)
  {
    launchConfiguration();
    giveBackFocus();
  }
  
  public void launchConfiguration()
  {
    TideConfiguration localTideConfiguration = new TideConfiguration(this, false);
  }
  
  private void CloseAllActionPerformed(ActionEvent paramActionEvent)
  {
    this.tabbedPaneManager.closeAll();
  }
  
  private void OpenSelectorActionPerformed(ActionEvent paramActionEvent)
  {
    openFile("Site Explorer", true);
    giveBackFocus();
  }
  
  private void OpenSiteActionPerformed(ActionEvent paramActionEvent)
  {
    openFile("Site Explorer", true);
    giveBackFocus();
  }
  
  private void CloseActionPerformed(ActionEvent paramActionEvent)
  {
    this.tabbedPaneManager.close();
    giveBackFocus();
  }
  
  private void formComponentResized(ComponentEvent paramComponentEvent)
  {
    this.appSize = getSize();
  }
  
  public void openFile(String paramString, boolean paramBoolean)
  {
    this.tabbedPaneManager.addTab(paramString, paramBoolean);
  }
  
  private void indexInit(String paramString, boolean paramBoolean)
  {
    if (!this.tideComp.indexValid) {
      this.tideComp.verifyIndex("JTidesData.index", this.basePath, paramString, paramBoolean);
    }
  }
  
  public TidesDoc openSiteExplorer()
  {
    TidesDoc localTidesDoc = null;
    int i = 0;
    if (this.tideComp.indexValid)
    {
      if (this.currentFinder != null)
      {
        localTidesDoc = this.currentFinder;
        i = 1;
      }
      else
      {
        localTidesDoc = new TidesDoc(this, 1, "Site Explorer", true, true, true, true);
        this.currentFinder = localTidesDoc;
      }
    }
    else {
      Toolkit.getDefaultToolkit().beep();
    }
    return localTidesDoc;
  }
  
  public TidesDoc openHelpBrowser()
  {
    TidesDoc localTidesDoc = null;
    int i = 0;
    if (this.currentHelpBrowser != null)
    {
      localTidesDoc = this.currentHelpBrowser;
      i = 1;
    }
    else
    {
      localTidesDoc = new TidesDoc(this, 2, "Help Browser", true, true, true, true);
      this.currentHelpBrowser = localTidesDoc;
    }
    return localTidesDoc;
  }
  
  private void exitForm(WindowEvent paramWindowEvent)
  {
    myExit();
  }
  
  private void formWindowActivated(WindowEvent paramWindowEvent)
  {
    giveBackFocus();
  }
  
  private void BackYearButtonActionPerformed(ActionEvent paramActionEvent)
  {
    moveTime(1, -1);
  }
  
  private void ForwardYearButtonActionPerformed(ActionEvent paramActionEvent)
  {
    moveTime(1, 1);
  }
  
  public void myExit()
  {
    Dimension localDimension = this.Toolbar.getSize();
    Point localPoint = this.Toolbar.getLocation();
    if (localDimension.width > localDimension.height) {
      this.configValues.toolBarPosition = 0;
    } else {
      this.configValues.toolBarPosition = (localPoint.x > 0 ? 1 : 3);
    }
    Vector localVector = this.tabbedPaneManager.getList();
    this.configValues.openSites = this.tideComp.mergeDelimLine(localVector, "|");
    this.configValues.selectedSite = this.tabbedPaneManager.getSelectedIndex();
    this.configValues.recentSiteList = this.recentFileList.getString();
    this.initFileHandler.write(this.configValues);
    System.exit(0);
  }
  
  public static void main(String[] paramArrayOfString)
  {
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        new JTides().setVisible(true);
      }
    });
  }
}


/* Location:              C:\Users\02221224710\Downloads\JTides.jar!\jtides\JTides.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */