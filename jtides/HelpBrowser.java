package jtides;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Stack;
import java.util.TreeSet;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.Document;

public final class HelpBrowser
  extends JPanel
{
  JTides main;
  Stack outStack;
  Stack backStack;
  String initialURL;
  String initialPath;
  int searchPosition = 0;
  boolean needScroll = false;
  int scrollValue = 0;
  String currentFile = "";
  private JButton BackButton;
  private JButton FindButton;
  private JButton FindNextButton;
  private JButton ForwardButton;
  private JTextArea SearchString;
  public JEditorPane browserWindow;
  private JMenuItem closeMenuItem;
  private JLabel jLabel1;
  private JPanel jPanel1;
  private JPopupMenu popupMenu;
  public JScrollPane scrollPane;
  
  public HelpBrowser(JTides paramJTides)
  {
    this.main = paramJTides;
    this.initialURL = "index.html";
    this.outStack = new Stack();
    this.backStack = new Stack();
    setLayout(new BorderLayout());
    initComponents();
    setFocusTraversalKeys(0, new HashSet());
    setFocusTraversalKeys(1, new HashSet());
    this.browserWindow.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        HelpBrowser.this.browserKeyHandler(paramAnonymousKeyEvent);
      }
    });
    this.scrollPane.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        HelpBrowser.this.scrollKeyHandler(paramAnonymousKeyEvent);
      }
    });
    this.browserWindow.addHyperlinkListener(new LinkFollower(this));
    this.browserWindow.setEditable(false);
    enableButtons();
    setPage(this.initialURL);
  }
  
  public void paintComponents(Graphics paramGraphics)
  {
    super.paintComponents(paramGraphics);
    System.out.println(getSize());
  }
  
  public void setPage(String paramString)
  {
    try
    {
      this.currentFile = paramString;
      this.browserWindow.setPage(this.main.getClass().getResource("/docs/" + paramString));
      try
      {
        Thread.sleep(50L);
      }
      catch (Exception localException1) {}
      processTags(this.browserWindow);
    }
    catch (Exception localException2)
    {
      localException2.printStackTrace();
    }
  }
  
  public void toPage(String paramString)
  {
    if (!this.backStack.empty()) {
      this.backStack.pop();
    }
    pushStack(this.outStack);
    setPage(paramString);
  }
  
  private void pushStack(Stack paramStack)
  {
    BrowserData localBrowserData = new BrowserData(this.currentFile, this.scrollPane.getVerticalScrollBar().getValue());
    paramStack.push(localBrowserData);
    enableButtons();
  }
  
  private void popStack(Stack paramStack)
  {
    if (!paramStack.empty())
    {
      BrowserData localBrowserData = (BrowserData)paramStack.pop();
      enableButtons();
      setPage(localBrowserData.path);
      this.scrollValue = localBrowserData.scrollPos;
      this.needScroll = true;
      repaint();
      try
      {
        Thread.sleep(100L);
      }
      catch (Exception localException) {}
    }
  }
  
  private void goBack()
  {
    if (!this.outStack.empty())
    {
      pushStack(this.backStack);
      popStack(this.outStack);
    }
  }
  
  private void goForward()
  {
    if (!this.backStack.empty())
    {
      pushStack(this.outStack);
      popStack(this.backStack);
    }
  }
  
  private JPanel getContentPane()
  {
    return this;
  }
  
  public JEditorPane getBrowserWindow()
  {
    return this.browserWindow;
  }
  
  public void paintComponent(Graphics paramGraphics)
  {
    if (this.needScroll)
    {
      this.needScroll = false;
      this.scrollPane.getVerticalScrollBar().setValue(this.scrollValue);
    }
  }
  
  private void processTags(JEditorPane paramJEditorPane)
  {
    searchReplaceEditor(paramJEditorPane, "[basePath]", this.main.basePath);
    String str1 = System.getProperty("user.home");
    searchReplaceEditor(paramJEditorPane, "[user.home]", str1);
    String str2 = "" + this.main.tideComp.siteIndex.size();
    searchReplaceEditor(paramJEditorPane, "[totalSites]", str2);
  }
  
  private void replaceText(JEditorPane paramJEditorPane, int paramInt1, int paramInt2, String paramString)
  {
    paramJEditorPane.setSelectionStart(paramInt1);
    paramJEditorPane.setSelectionEnd(paramInt2);
    paramJEditorPane.setEditable(true);
    paramJEditorPane.replaceSelection(paramString);
    paramJEditorPane.setEditable(false);
  }
  
  private void searchReplaceEditor(JEditorPane paramJEditorPane, String paramString1, String paramString2)
  {
    int i = -1;
    int j = paramString1.length();
    int k = paramString2.length();
    int m = paramJEditorPane.getDocument().getLength();
    do
    {
      i++;
      try
      {
        String str = paramJEditorPane.getText(i, j);
        if (str.equals(paramString1))
        {
          replaceText(paramJEditorPane, i, i + j, paramString2);
          i += k - j;
        }
      }
      catch (Exception localException) {}
    } while (i < m);
    paramJEditorPane.setCaretPosition(0);
  }
  
  public void scrollKeyHandler(KeyEvent paramKeyEvent)
  {
    int i = paramKeyEvent.getKeyCode();
    if (i == 36) {
      this.scrollPane.getVerticalScrollBar().setValue(0);
    } else if (i == 35) {
      this.scrollPane.getVerticalScrollBar().setValue(this.scrollPane.getVerticalScrollBar().getMaximum());
    } else {
      this.main.tabbedPaneManager.handleKey(paramKeyEvent);
    }
  }
  
  public void browserKeyHandler(KeyEvent paramKeyEvent)
  {
    int i = paramKeyEvent.getKeyCode();
    if (i != 10) {
      this.scrollPane.requestFocusInWindow();
    } else {
      this.main.tabbedPaneManager.handleKey(paramKeyEvent);
    }
  }
  
  private void enableButtons()
  {
    this.BackButton.setEnabled(this.outStack.size() > 0);
    this.ForwardButton.setEnabled(this.backStack.size() > 0);
  }
  
  private void initComponents()
  {
    this.popupMenu = new JPopupMenu();
    this.closeMenuItem = new JMenuItem();
    this.scrollPane = new JScrollPane();
    this.browserWindow = new JEditorPane();
    this.jPanel1 = new JPanel();
    this.BackButton = new MyJButton();
    this.ForwardButton = new MyJButton();
    this.jLabel1 = new JLabel();
    this.SearchString = new JTextArea();
    this.FindButton = new MyJButton();
    this.FindNextButton = new MyJButton();
    this.closeMenuItem.setText("Close");
    this.closeMenuItem.setToolTipText("Close the help browser");
    this.closeMenuItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        HelpBrowser.this.closeMenuItemActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.popupMenu.add(this.closeMenuItem);
    addFocusListener(new FocusAdapter()
    {
      public void focusGained(FocusEvent paramAnonymousFocusEvent)
      {
        HelpBrowser.this.formFocusGained(paramAnonymousFocusEvent);
      }
      
      public void focusLost(FocusEvent paramAnonymousFocusEvent)
      {
        HelpBrowser.this.formFocusLost(paramAnonymousFocusEvent);
      }
    });
    addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        HelpBrowser.this.formKeyPressed(paramAnonymousKeyEvent);
      }
    });
    setLayout(new BorderLayout());
    this.browserWindow.addFocusListener(new FocusAdapter()
    {
      public void focusLost(FocusEvent paramAnonymousFocusEvent)
      {
        HelpBrowser.this.browserWindowFocusLost(paramAnonymousFocusEvent);
      }
    });
    this.browserWindow.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        HelpBrowser.this.browserWindowKeyPressed(paramAnonymousKeyEvent);
      }
    });
    this.browserWindow.addMouseListener(new MouseAdapter()
    {
      public void mousePressed(MouseEvent paramAnonymousMouseEvent)
      {
        HelpBrowser.this.browserWindowMousePressed(paramAnonymousMouseEvent);
      }
      
      public void mouseReleased(MouseEvent paramAnonymousMouseEvent)
      {
        HelpBrowser.this.browserWindowMouseReleased(paramAnonymousMouseEvent);
      }
    });
    this.scrollPane.setViewportView(this.browserWindow);
    add(this.scrollPane, "Center");
    this.jPanel1.setLayout(new GridBagLayout());
    this.BackButton.setText("Back");
    this.BackButton.setToolTipText("Go Back");
    this.BackButton.setHorizontalTextPosition(4);
    this.BackButton.setMaximumSize(new Dimension(120, 27));
    this.BackButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        HelpBrowser.this.BackButtonActionPerformed(paramAnonymousActionEvent);
      }
    });
    GridBagConstraints localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.fill = 3;
    localGridBagConstraints.insets = new Insets(2, 2, 2, 2);
    this.jPanel1.add(this.BackButton, localGridBagConstraints);
    this.ForwardButton.setText("Forward");
    this.ForwardButton.setToolTipText("Go Forward");
    this.ForwardButton.setHorizontalTextPosition(4);
    this.ForwardButton.setMaximumSize(new Dimension(35, 27));
    this.ForwardButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        HelpBrowser.this.ForwardButtonActionPerformed(paramAnonymousActionEvent);
      }
    });
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.fill = 3;
    localGridBagConstraints.insets = new Insets(2, 2, 2, 2);
    this.jPanel1.add(this.ForwardButton, localGridBagConstraints);
    this.jLabel1.setHorizontalAlignment(0);
    this.jLabel1.setText("Search:");
    this.jLabel1.setMinimumSize(new Dimension(60, 16));
    this.jLabel1.setPreferredSize(new Dimension(60, 16));
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.fill = 3;
    localGridBagConstraints.insets = new Insets(2, 2, 2, 2);
    this.jPanel1.add(this.jLabel1, localGridBagConstraints);
    this.SearchString.setBorder(BorderFactory.createEtchedBorder());
    this.SearchString.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        HelpBrowser.this.searchStringKeyPressed(paramAnonymousKeyEvent);
      }
    });
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.fill = 2;
    localGridBagConstraints.weightx = 1.0D;
    localGridBagConstraints.insets = new Insets(2, 2, 2, 2);
    this.jPanel1.add(this.SearchString, localGridBagConstraints);
    this.FindButton.setText("Find");
    this.FindButton.setToolTipText("Find first case");
    this.FindButton.setHorizontalTextPosition(4);
    this.FindButton.setMaximumSize(new Dimension(35, 27));
    this.FindButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        HelpBrowser.this.FindButtonActionPerformed(paramAnonymousActionEvent);
      }
    });
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.fill = 3;
    localGridBagConstraints.insets = new Insets(2, 2, 2, 2);
    this.jPanel1.add(this.FindButton, localGridBagConstraints);
    this.FindNextButton.setText("Find Next");
    this.FindNextButton.setToolTipText("Find next case");
    this.FindNextButton.setHorizontalTextPosition(4);
    this.FindNextButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        HelpBrowser.this.FindNextButtonActionPerformed(paramAnonymousActionEvent);
      }
    });
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.fill = 3;
    localGridBagConstraints.insets = new Insets(2, 2, 2, 2);
    this.jPanel1.add(this.FindNextButton, localGridBagConstraints);
    add(this.jPanel1, "North");
  }
  
  private void browserWindowFocusLost(FocusEvent paramFocusEvent) {}
  
  private void formFocusLost(FocusEvent paramFocusEvent) {}
  
  private void closeMenuItemActionPerformed(ActionEvent paramActionEvent)
  {
    this.main.tabbedPaneManager.close();
  }
  
  private void browserWindowMouseReleased(MouseEvent paramMouseEvent)
  {
    handleMouse(paramMouseEvent);
  }
  
  private void browserWindowMousePressed(MouseEvent paramMouseEvent)
  {
    handleMouse(paramMouseEvent);
  }
  
  private void formKeyPressed(KeyEvent paramKeyEvent)
  {
    this.main.tabbedPaneManager.handleKey(paramKeyEvent);
  }
  
  private void formFocusGained(FocusEvent paramFocusEvent)
  {
    this.browserWindow.requestFocusInWindow();
  }
  
  private void searchStringKeyPressed(KeyEvent paramKeyEvent)
  {
    int i = paramKeyEvent.getKeyCode();
    if (i == 10)
    {
      paramKeyEvent.consume();
      findText(true);
    }
    else if ((i == 40) || (i == 38) || (i == 33) || (i == 34))
    {
      this.scrollPane.grabFocus();
    }
    else
    {
      this.main.tabbedPaneManager.handleKey(paramKeyEvent);
    }
  }
  
  private void browserWindowKeyPressed(KeyEvent paramKeyEvent)
  {
    if (paramKeyEvent.getKeyCode() == 10)
    {
      findText(false);
      paramKeyEvent.consume();
    }
    else
    {
      this.main.tabbedPaneManager.handleKey(paramKeyEvent);
    }
  }
  
  private void FindNextButtonActionPerformed(ActionEvent paramActionEvent)
  {
    findText(false);
  }
  
  private void FindButtonActionPerformed(ActionEvent paramActionEvent)
  {
    findText(true);
  }
  
  private void ForwardButtonActionPerformed(ActionEvent paramActionEvent)
  {
    goForward();
  }
  
  private void BackButtonActionPerformed(ActionEvent paramActionEvent)
  {
    goBack();
  }
  
  private void handleMouse(MouseEvent paramMouseEvent)
  {
    if ((paramMouseEvent.isPopupTrigger()) && (isVisible())) {
      this.popupMenu.show(paramMouseEvent.getComponent(), paramMouseEvent.getX(), paramMouseEvent.getY());
    }
  }
  
  private void findText(boolean paramBoolean)
  {
    String str1 = this.browserWindow.getText();
    String str2 = this.SearchString.getText();
    if (str2.length() > 0)
    {
      if (paramBoolean == true) {
        this.searchPosition = 0;
      }
      int i = this.searchPosition - 1;
      int j = str2.length();
      int k = this.browserWindow.getDocument().getLength();
      boolean bool;
      do
      {
        i++;
        String str3 = "";
        try
        {
          str3 = this.browserWindow.getText(i, j);
          bool = str3.equals(str2);
        }
        catch (Exception localException)
        {
          bool = false;
        }
      } while ((!bool) && (i < k));
      if (bool)
      {
        int m = i + str2.length();
        this.browserWindow.setCaretPosition(i);
        this.browserWindow.moveCaretPosition(m);
        this.browserWindow.requestFocusInWindow();
        this.searchPosition = m;
      }
      else
      {
        this.searchPosition = 0;
        Toolkit.getDefaultToolkit().beep();
      }
    }
  }
}


/* Location:              C:\Users\02221224710\Downloads\JTides.jar!\jtides\HelpBrowser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */