package jtides;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

public final class SiteTree
  extends JPanel
{
  JTides main;
  public JScrollPane scrollPane;
  Enumeration searchEnum;
  private JButton CollapseButton;
  private JButton ExpandButton;
  private JButton FindButton;
  private JButton FindNextButton;
  private JTextArea SearchEntry;
  private JMenuItem closeMenuitem;
  private JLabel jLabel1;
  private JScrollPane jScrollPane1;
  private JToolBar jToolBar1;
  private JPopupMenu popupMenu;
  public JTree tree;
  
  public SiteTree(JTides paramJTides)
  {
    this.main = paramJTides;
    this.searchEnum = this.main.tideComp.root.depthFirstEnumeration();
    initComponents();
    setFocusTraversalKeys(0, new HashSet());
    setFocusTraversalKeys(1, new HashSet());
    this.tree.setFocusTraversalKeys(0, new HashSet());
    this.tree.setFocusTraversalKeys(1, new HashSet());
    this.tree.setShowsRootHandles(true);
    this.tree.setScrollsOnExpand(true);
    this.tree.setModel(null);
    setupIcons(this.tree);
    initWhenReady();
  }
  
  private void setupIcons(JTree paramJTree)
  {
    DefaultTreeCellRenderer localDefaultTreeCellRenderer = (DefaultTreeCellRenderer)paramJTree.getCellRenderer();
    localDefaultTreeCellRenderer.setClosedIcon(new ImageIcon(this.main.getClass().getResource("icons/Folder.png")));
    localDefaultTreeCellRenderer.setOpenIcon(new ImageIcon(this.main.getClass().getResource("icons/Open.png")));
    localDefaultTreeCellRenderer.setLeafIcon(new ImageIcon(this.main.getClass().getResource("icons/Document.png")));
  }
  
  JTree getTree()
  {
    return this.tree;
  }
  
  private void initWhenReady()
  {
    this.tree.setModel(this.main.tideComp.dtm);
    this.tree.expandRow(0);
  }
  
  private void searchTest(boolean paramBoolean)
  {
    String str = this.SearchEntry.getText();
    if (str.length() > 0) {
      search(str, paramBoolean);
    }
  }
  
  public void search(String paramString, boolean paramBoolean)
  {
    paramString = paramString.toLowerCase();
    if (paramBoolean) {
      this.searchEnum = this.main.tideComp.root.depthFirstEnumeration();
    }
    int i = 0;
    while ((this.searchEnum.hasMoreElements()) && (i == 0))
    {
      Object localObject = this.searchEnum.nextElement();
      String str = localObject.toString().toLowerCase();
      if (str.indexOf(paramString) != -1)
      {
        i = 1;
        TreePath localTreePath = new TreePath(((DefaultMutableTreeNode)localObject).getPath());
        this.tree.scrollPathToVisible(localTreePath);
        this.tree.setSelectionRow(this.tree.getRowForPath(localTreePath));
        this.tree.makeVisible(localTreePath);
      }
    }
    if (i == 0)
    {
      Toolkit.getDefaultToolkit().beep();
      this.searchEnum = this.main.tideComp.root.depthFirstEnumeration();
    }
  }
  
  public void expand(boolean paramBoolean)
  {
    int i;
    if (paramBoolean)
    {
      for (i = 0; i < this.tree.getRowCount(); i++) {
        this.tree.expandRow(i);
      }
    }
    else
    {
      for (i = this.tree.getRowCount(); i >= 0; i--) {
        this.tree.collapseRow(i);
      }
      this.tree.expandRow(0);
    }
  }
  
  private void initComponents()
  {
    this.popupMenu = new JPopupMenu();
    this.closeMenuitem = new JMenuItem();
    this.jScrollPane1 = new JScrollPane();
    this.tree = new JTree();
    this.jToolBar1 = new JToolBar();
    this.CollapseButton = new MyJButton();
    this.ExpandButton = new MyJButton();
    this.jLabel1 = new JLabel();
    this.SearchEntry = new JTextArea();
    this.FindButton = new MyJButton();
    this.FindNextButton = new MyJButton();
    this.closeMenuitem.setText("Close");
    this.closeMenuitem.setToolTipText("Close the site explorer");
    this.closeMenuitem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        SiteTree.this.closeMenuitemActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.popupMenu.add(this.closeMenuitem);
    addFocusListener(new FocusAdapter()
    {
      public void focusGained(FocusEvent paramAnonymousFocusEvent)
      {
        SiteTree.this.formFocusGained(paramAnonymousFocusEvent);
      }
      
      public void focusLost(FocusEvent paramAnonymousFocusEvent)
      {
        SiteTree.this.formFocusLost(paramAnonymousFocusEvent);
      }
    });
    addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        SiteTree.this.formKeyPressed(paramAnonymousKeyEvent);
      }
    });
    addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
      {
        SiteTree.this.formMouseClicked(paramAnonymousMouseEvent);
      }
    });
    setLayout(new BorderLayout());
    this.tree.addFocusListener(new FocusAdapter()
    {
      public void focusGained(FocusEvent paramAnonymousFocusEvent)
      {
        SiteTree.this.treeFocusGained(paramAnonymousFocusEvent);
      }
      
      public void focusLost(FocusEvent paramAnonymousFocusEvent)
      {
        SiteTree.this.treeFocusLost(paramAnonymousFocusEvent);
      }
    });
    this.tree.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        SiteTree.this.treeKeyPressed(paramAnonymousKeyEvent);
      }
    });
    this.tree.addMouseListener(new MouseAdapter()
    {
      public void mousePressed(MouseEvent paramAnonymousMouseEvent)
      {
        SiteTree.this.treeMousePressed(paramAnonymousMouseEvent);
      }
      
      public void mouseReleased(MouseEvent paramAnonymousMouseEvent)
      {
        SiteTree.this.treeMouseReleased(paramAnonymousMouseEvent);
      }
      
      public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
      {
        SiteTree.this.treeMouseClicked(paramAnonymousMouseEvent);
      }
    });
    this.jScrollPane1.setViewportView(this.tree);
    add(this.jScrollPane1, "Center");
    this.CollapseButton.setText("Collapse");
    this.CollapseButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        SiteTree.this.CollapseButtonActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.jToolBar1.add(this.CollapseButton);
    this.ExpandButton.setText("Expand");
    this.ExpandButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        SiteTree.this.ExpandButtonActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.jToolBar1.add(this.ExpandButton);
    this.jLabel1.setHorizontalAlignment(0);
    this.jLabel1.setText("Search:");
    this.jLabel1.setMinimumSize(new Dimension(60, 16));
    this.jLabel1.setPreferredSize(new Dimension(60, 16));
    this.jToolBar1.add(this.jLabel1);
    this.SearchEntry.setRows(1);
    this.SearchEntry.setBorder(BorderFactory.createEtchedBorder());
    this.SearchEntry.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        SiteTree.this.SearchEntryKeyPressed(paramAnonymousKeyEvent);
      }
    });
    this.jToolBar1.add(this.SearchEntry);
    this.FindButton.setText("Find");
    this.FindButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        SiteTree.this.FindButtonActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.jToolBar1.add(this.FindButton);
    this.FindNextButton.setText("Find Next");
    this.FindNextButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        SiteTree.this.FindNextButtonActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.jToolBar1.add(this.FindNextButton);
    add(this.jToolBar1, "North");
  }
  
  private void treeFocusLost(FocusEvent paramFocusEvent) {}
  
  private void formFocusLost(FocusEvent paramFocusEvent) {}
  
  private void treeMouseReleased(MouseEvent paramMouseEvent)
  {
    handleMouse(paramMouseEvent);
  }
  
  private void treeMousePressed(MouseEvent paramMouseEvent)
  {
    handleMouse(paramMouseEvent);
  }
  
  private void closeMenuitemActionPerformed(ActionEvent paramActionEvent)
  {
    this.main.tabbedPaneManager.close();
  }
  
  private void treeFocusGained(FocusEvent paramFocusEvent) {}
  
  private void formFocusGained(FocusEvent paramFocusEvent)
  {
    this.tree.requestFocusInWindow();
  }
  
  private void formKeyPressed(KeyEvent paramKeyEvent)
  {
    this.main.tabbedPaneManager.handleKey(paramKeyEvent);
  }
  
  private void treeKeyPressed(KeyEvent paramKeyEvent)
  {
    int i = paramKeyEvent.getKeyCode();
    if (i == 10)
    {
      paramKeyEvent.consume();
      processMouseSelection();
    }
    else
    {
      this.main.tabbedPaneManager.handleKey(paramKeyEvent);
    }
  }
  
  private void SearchEntryKeyPressed(KeyEvent paramKeyEvent)
  {
    int i = paramKeyEvent.getKeyCode();
    if (i == 10)
    {
      paramKeyEvent.consume();
      searchTest(false);
      this.SearchEntry.requestFocusInWindow();
    }
    else
    {
      this.main.tabbedPaneManager.handleKey(paramKeyEvent);
    }
  }
  
  private void CollapseButtonActionPerformed(ActionEvent paramActionEvent)
  {
    expand(false);
  }
  
  private void treeMouseClicked(MouseEvent paramMouseEvent)
  {
    processMouseSelection();
  }
  
  private void FindNextButtonActionPerformed(ActionEvent paramActionEvent)
  {
    searchTest(false);
  }
  
  private void ExpandButtonActionPerformed(ActionEvent paramActionEvent)
  {
    expand(true);
  }
  
  private void FindButtonActionPerformed(ActionEvent paramActionEvent)
  {
    searchTest(true);
  }
  
  private void formMouseClicked(MouseEvent paramMouseEvent)
  {
    if (paramMouseEvent.getClickCount() == 2) {
      processMouseSelection();
    }
  }
  
  private void handleMouse(MouseEvent paramMouseEvent)
  {
    if ((paramMouseEvent.isPopupTrigger()) && (isVisible()))
    {
      this.popupMenu.show(paramMouseEvent.getComponent(), paramMouseEvent.getX(), paramMouseEvent.getY());
      this.main.setActiveMenu(this.popupMenu);
    }
  }
  
  public void processMouseSelection()
  {
    TreePath localTreePath = this.tree.getSelectionPath();
    if (localTreePath != null)
    {
      String str1 = "";
      int i = localTreePath.getPathCount();
      if (i == 5)
      {
        for (int j = 1; j < i; j++) {
          str1 = str1 + localTreePath.getPathComponent(j).toString() + (j < i - 1 ? "\t" : "");
        }
        Vector localVector = this.main.tideComp.parseDelimLine(str1, "\t");
        String str2 = (String)localVector.elementAt(0);
        str2 = str2.indexOf("Current") != -1 ? "C" : "T";
        localVector.setElementAt(str2, 0);
        str1 = this.main.tideComp.mergeDelimLine(localVector, "\t");
        this.main.openFileOnPartialPath(str1);
      }
    }
  }
}


/* Location:              C:\Users\02221224710\Downloads\JTides.jar!\jtides\SiteTree.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */