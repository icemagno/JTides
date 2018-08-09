package jtides;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public final class TidesFindNearest
  extends JDialog
{
  JTides main;
  ImageIcon tideIcon;
  ImageIcon currentIcon;
  ImageIcon tideCurrentIcon;
  TreeMap proximityTree;
  String[] fullList = null;
  boolean searching = false;
  private JButton CloseButton;
  private JTextField LatField;
  private JTextField LongField;
  private JTable OutputList;
  private JButton SearchButton;
  private JComboBox SearchType;
  private JButton UseDisplayButton;
  private JPanel content;
  private JLabel jLabel1;
  private JLabel jLabel2;
  private JPanel jPanel1;
  private JProgressBar jProgressBar1;
  private JScrollPane jScrollPane1;
  
  public TidesFindNearest(Frame paramFrame, boolean paramBoolean)
  {
    super(paramFrame, paramBoolean);
    this.main = ((JTides)paramFrame);
    this.proximityTree = new TreeMap();
    this.tideIcon = new ImageIcon(this.main.getClass().getResource("icons/TideIcon.png"));
    this.currentIcon = new ImageIcon(this.main.getClass().getResource("icons/CurrentIcon.png"));
    this.tideCurrentIcon = new ImageIcon(this.main.getClass().getResource("icons/TideCurrentIcon.png"));
    initComponents();
    this.main.tideComp.tweakFont(this.OutputList);
    this.main.tideComp.tweakFont(this.LatField);
    this.main.tideComp.tweakFont(this.LongField);
    getDisplayPos();
    pack();
    this.SearchType.setRenderer(new MyDropDownListCellRenderer());
    setTitle("JTides 5.3" + " Find Nearest Sites");
    setupSearchTypes();
    setSize(600, 400);
    setVisible(true);
  }
  
  private void setupSearchTypes()
  {
    String[] arrayOfString = { "Tide & Current", "Tide only", "Current only" };
    ImageIcon[] arrayOfImageIcon = { this.tideCurrentIcon, this.tideIcon, this.currentIcon };
    for (int i = 0; i < arrayOfString.length; i++)
    {
      JLabel localJLabel = new JLabel(arrayOfString[i], arrayOfImageIcon[i], 2);
      this.SearchType.addItem(localJLabel);
    }
  }
  
  private void performSearch()
  {
    double d1 = getEntry(this.LatField);
    double d2 = getEntry(this.LongField);
    if ((d1 < 1000.0D) && (d2 < 1000.0D))
    {
      execSearch(d1, d2);
    }
    else
    {
      Toolkit.getDefaultToolkit().beep();
      JOptionPane.showMessageDialog(null, "Please use an entry format like \"dd.dd [mm.mm] [n/s/e/w]\"", "Numeric Entry Error", 0);
    }
  }
  
  private void execSearch(final double paramDouble1, double paramDouble2)
  {
    if (!this.searching)
    {
      this.searching = true;
      Thread local1 = new Thread()
      {
        public void run()
        {
          TidesFindNearest.this.execSearchThread(paramDouble1, this.val$lng);
          TidesFindNearest.this.searching = false;
        }
      };
      local1.start();
    }
  }
  
  private void execSearchThread(double paramDouble1, double paramDouble2)
  {
    try
    {
      int i = this.SearchType.getSelectedIndex();
      Complex localComplex1 = new Complex(paramDouble1, paramDouble2);
      this.proximityTree.clear();
      int j = this.main.tideComp.siteIndex.size();
      int k = 0;
      this.jProgressBar1.setMinimum(0);
      this.jProgressBar1.setMaximum(j);
      Iterator localIterator = this.main.tideComp.siteIndex.iterator();
      boolean bool = false;
      while (localIterator.hasNext())
      {
        localObject1 = (String)localIterator.next();
        localObject2 = this.main.tideComp.parseDelimLine((String)localObject1, "\t");
        m = 1;
        bool = ((String)((Vector)localObject2).elementAt(0)).equals("T");
        if (i != 0)
        {
          localObject3 = (String)((Vector)localObject2).elementAt(0);
          m = ((i == 1) && (bool)) || ((i == 2) && (!bool)) ? 1 : 0;
        }
        if (m != 0)
        {
          localObject3 = comp_pr_String((String)((Vector)localObject2).elementAt(5), (String)((Vector)localObject2).elementAt(4), localComplex1);
          while (this.proximityTree.containsKey(new Double(((Complex)localObject3).rad))) {
            localObject3.rad += 1.0E-10D;
          }
          this.proximityTree.put(new Double(((Complex)localObject3).rad), localObject1);
          k++;
          this.jProgressBar1.setValue(k);
        }
      }
      Object localObject1 = this.proximityTree.keySet();
      localIterator = ((Set)localObject1).iterator();
      k = 0;
      j = this.main.configValues.nearestSitesListSize;
      this.fullList = new String[j];
      Object localObject2 = { "Type", "Site Name", "Distance nm", "Bearing true" };
      int m = localObject2.length;
      Object localObject3 = new String[j][m];
      int[] arrayOfInt = new int[m];
      Graphics localGraphics = this.OutputList.getGraphics();
      FontMetrics localFontMetrics = localGraphics.getFontMetrics();
      while ((k < j) && (localIterator.hasNext()))
      {
        String str = (String)this.proximityTree.get(localIterator.next());
        this.fullList[k] = str;
        Vector localVector = this.main.tideComp.parseDelimLine(str, "\t");
        String[] arrayOfString = (String[])localVector.toArray(new String[0]);
        bool = arrayOfString[0].equals("T");
        Complex localComplex2 = comp_pr_String(arrayOfString[5], arrayOfString[4], localComplex1);
        localObject3[k][0] = (bool ? "*" : " ");
        localObject3[k][1] = arrayOfString[3];
        localObject3[k][2] = this.main.tideComp.padChar(this.main.tideComp.formatDouble(localComplex2.rad, 1, false), 5, " ");
        localObject3[k][3] = (this.main.tideComp.padChar(this.main.tideComp.formatDouble(localComplex2.lat * 57.29577951308232D, 1, false), 5, " ") + "°");
        for (int i3 = 1; i3 < m; i3++)
        {
          int i4 = localFontMetrics.stringWidth((String)localObject3[k][i3]);
          if (i4 > arrayOfInt[i3]) {
            arrayOfInt[i3] = i4;
          }
        }
        k++;
      }
      for (int n = 0; n < m; n++)
      {
        int i1 = localFontMetrics.stringWidth(localObject2[n]);
        if (i1 > arrayOfInt[n]) {
          arrayOfInt[n] = i1;
        }
      }
      localGraphics.dispose();
      DefaultTableModel local2 = new DefaultTableModel((Object[][])localObject3, (Object[])localObject2)
      {
        public boolean isCellEditable(int paramAnonymousInt1, int paramAnonymousInt2)
        {
          return false;
        }
      };
      this.OutputList.setModel(local2);
      this.OutputList.setSelectionMode(0);
      this.OutputList.getColumnModel().getColumn(0).setCellRenderer(new MyTableCellRenderer());
      TableColumn localTableColumn = null;
      for (int i2 = 0; i2 < 4; i2++)
      {
        localTableColumn = this.OutputList.getColumnModel().getColumn(i2);
        localTableColumn.setPreferredWidth(arrayOfInt[i2]);
      }
    }
    catch (Exception localException) {}
    this.jProgressBar1.setValue(0);
  }
  
  Complex comp_pr(Complex paramComplex1, Complex paramComplex2)
  {
    Complex localComplex = new Complex();
    double d1 = paramComplex1.lng - paramComplex2.lng;
    double d2 = Math.cos(d1);
    double d3 = Math.sin(paramComplex2.lat);
    double d4 = Math.cos(paramComplex2.lat);
    localComplex.rad = Math.acos(d3 * Math.sin(paramComplex1.lat) + d4 * Math.cos(paramComplex1.lat) * d2);
    localComplex.rad *= 3437.746770784939D;
    localComplex.lat = Math.atan2(Math.sin(d1), d4 * Math.tan(paramComplex1.lat) - d3 * d2);
    if (localComplex.lat < 0.0D) {
      localComplex.lat = (6.283185307179586D + localComplex.lat);
    }
    return localComplex;
  }
  
  Complex comp_pr_String(String paramString1, String paramString2, Complex paramComplex)
  {
    Complex localComplex = null;
    try
    {
      double d1 = Double.parseDouble(paramString1);
      double d2 = Double.parseDouble(paramString2);
      d2 *= 0.01745329251994329D;
      d1 *= 0.01745329251994329D;
      localComplex = new Complex(d1, d2);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return comp_pr(localComplex, paramComplex);
  }
  
  double getEntry(JTextField paramJTextField)
  {
    double d1 = 1000.0D;
    double d2 = 0.0D;
    double d3 = 0.0D;
    String str1 = "";
    try
    {
      String str2 = paramJTextField.getText();
      str2 = this.main.tideComp.srchRplc(0, str2, ",", ".");
      Vector localVector = this.main.tideComp.parseLine(str2);
      String[] arrayOfString = (String[])localVector.toArray(new String[0]);
      if (arrayOfString.length > 0)
      {
        d2 = Double.parseDouble(arrayOfString[0]);
        if (arrayOfString.length > 1)
        {
          if (Character.isDigit(arrayOfString[1].charAt(0))) {
            d3 = Double.parseDouble(arrayOfString[1]);
          } else {
            str1 = arrayOfString[1];
          }
          if (arrayOfString.length > 2) {
            str1 = arrayOfString[2];
          }
        }
        d1 = (d2 + d3 / 60.0D) * 0.01745329251994329D;
        if (str1.length() > 0)
        {
          str1 = str1.toLowerCase().substring(0, 1);
          if ((str1.equals("w")) || (str1.equals("s"))) {
            d1 = -d1;
          }
        }
      }
      else
      {
        throw new Exception();
      }
    }
    catch (Exception localException) {}
    return d1;
  }
  
  private void putEntry(JTextField paramJTextField, double paramDouble, String paramString1, String paramString2)
  {
    String str1 = paramDouble >= 0.0D ? paramString1 : paramString2;
    paramDouble = Math.abs(paramDouble);
    int i = (int)paramDouble;
    paramDouble -= i;
    paramDouble *= 60.0D;
    String str2 = i + " " + this.main.tideComp.formatDouble(paramDouble, 2, false) + " " + str1;
    paramJTextField.setText(str2);
  }
  
  public void getDisplayPos()
  {
    TidesDoc localTidesDoc = this.main.tabbedPaneManager.getSelectedDoc();
    if (localTidesDoc != null)
    {
      SiteSet localSiteSet = localTidesDoc.siteSet;
      putEntry(this.LatField, localSiteSet.lat, "n", "s");
      putEntry(this.LongField, localSiteSet.lng, "e", "w");
      repaint();
    }
    else
    {
      putEntry(this.LatField, 0.0D, "n", "s");
      putEntry(this.LongField, 0.0D, "e", "w");
    }
  }
  
  private void initComponents()
  {
    this.jPanel1 = new JPanel();
    this.jProgressBar1 = new JProgressBar();
    this.SearchButton = new MyJButton();
    this.CloseButton = new MyJButton();
    this.content = new JPanel();
    this.jLabel1 = new JLabel();
    this.jLabel2 = new JLabel();
    this.LatField = new JTextField();
    this.LongField = new JTextField();
    this.SearchType = new JComboBox();
    this.UseDisplayButton = new MyJButton();
    this.jScrollPane1 = new JScrollPane();
    this.OutputList = new JTable();
    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent paramAnonymousWindowEvent)
      {
        TidesFindNearest.this.closeDialog(paramAnonymousWindowEvent);
      }
    });
    this.jPanel1.setLayout(new BorderLayout());
    this.jProgressBar1.setToolTipText("Progress of the search");
    this.jProgressBar1.setStringPainted(true);
    this.jPanel1.add(this.jProgressBar1, "Center");
    this.SearchButton.setText("Search");
    this.SearchButton.setToolTipText("Refresh the list");
    this.SearchButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        TidesFindNearest.this.SearchButtonActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.jPanel1.add(this.SearchButton, "West");
    this.CloseButton.setText("Close");
    this.CloseButton.setToolTipText("Close this dialog");
    this.CloseButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        TidesFindNearest.this.CloseButtonActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.jPanel1.add(this.CloseButton, "East");
    getContentPane().add(this.jPanel1, "South");
    this.content.setToolTipText("Enter location data");
    this.content.setMinimumSize(new Dimension(490, 55));
    this.content.setPreferredSize(new Dimension(490, 55));
    this.content.setLayout(new GridBagLayout());
    this.jLabel1.setText("Lat.");
    GridBagConstraints localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 0;
    localGridBagConstraints.fill = 2;
    localGridBagConstraints.insets = new Insets(1, 1, 1, 1);
    this.content.add(this.jLabel1, localGridBagConstraints);
    this.jLabel2.setHorizontalAlignment(0);
    this.jLabel2.setText("Lng.");
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 2;
    localGridBagConstraints.gridy = 0;
    localGridBagConstraints.fill = 2;
    localGridBagConstraints.insets = new Insets(1, 1, 1, 1);
    this.content.add(this.jLabel2, localGridBagConstraints);
    this.LatField.setToolTipText("Use dd.dd [mm.mm] n/s/e/w format");
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 1;
    localGridBagConstraints.gridy = 0;
    localGridBagConstraints.fill = 2;
    localGridBagConstraints.weightx = 1.0D;
    localGridBagConstraints.insets = new Insets(1, 1, 1, 1);
    this.content.add(this.LatField, localGridBagConstraints);
    this.LongField.setToolTipText("Use dd.dd [mm.mm] n/s/e/w format");
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 3;
    localGridBagConstraints.gridy = 0;
    localGridBagConstraints.fill = 2;
    localGridBagConstraints.weightx = 1.0D;
    localGridBagConstraints.insets = new Insets(1, 1, 1, 1);
    this.content.add(this.LongField, localGridBagConstraints);
    this.SearchType.setToolTipText("Choose which entries to list");
    this.SearchType.addItemListener(new ItemListener()
    {
      public void itemStateChanged(ItemEvent paramAnonymousItemEvent)
      {
        TidesFindNearest.this.SearchTypeItemStateChanged(paramAnonymousItemEvent);
      }
    });
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 5;
    localGridBagConstraints.gridy = 0;
    localGridBagConstraints.fill = 2;
    localGridBagConstraints.weightx = 1.0D;
    localGridBagConstraints.insets = new Insets(1, 1, 1, 1);
    this.content.add(this.SearchType, localGridBagConstraints);
    this.UseDisplayButton.setText("Use display pos.");
    this.UseDisplayButton.setToolTipText("Use the position of the diplayed site");
    this.UseDisplayButton.setMaximumSize(new Dimension(32767, 11));
    this.UseDisplayButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        TidesFindNearest.this.UseDisplayButtonActionPerformed(paramAnonymousActionEvent);
      }
    });
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 4;
    localGridBagConstraints.gridy = 0;
    localGridBagConstraints.fill = 2;
    localGridBagConstraints.insets = new Insets(1, 1, 1, 1);
    this.content.add(this.UseDisplayButton, localGridBagConstraints);
    getContentPane().add(this.content, "North");
    this.OutputList.setToolTipText("Click a site to display it");
    this.OutputList.setAutoResizeMode(0);
    this.OutputList.setRowHeight(22);
    this.OutputList.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
      {
        TidesFindNearest.this.OutputListMouseClicked(paramAnonymousMouseEvent);
      }
    });
    this.jScrollPane1.setViewportView(this.OutputList);
    getContentPane().add(this.jScrollPane1, "Center");
  }
  
  private void OutputListMouseClicked(MouseEvent paramMouseEvent)
  {
    openSelection();
  }
  
  private void SearchTypeItemStateChanged(ItemEvent paramItemEvent)
  {
    performSearch();
  }
  
  private void openSelection()
  {
    int i = this.OutputList.getSelectedRow();
    if ((i != -1) && (this.fullList != null) && (this.fullList.length > i)) {
      this.main.openFile(this.fullList[i], true);
    }
  }
  
  private void UseDisplayButtonActionPerformed(ActionEvent paramActionEvent)
  {
    getDisplayPos();
  }
  
  private void SearchButtonActionPerformed(ActionEvent paramActionEvent)
  {
    performSearch();
  }
  
  private void CloseButtonActionPerformed(ActionEvent paramActionEvent)
  {
    quit();
  }
  
  private void closeDialog(WindowEvent paramWindowEvent)
  {
    quit();
  }
  
  private void quit()
  {
    this.main.tabbedPaneManager.refocus();
    setVisible(false);
    dispose();
  }
  
  public static void main(String[] paramArrayOfString)
  {
    new TidesFindNearest(new JFrame(), true).setVisible(true);
  }
  
  final class Complex
  {
    double lat = 0.0D;
    double lng = 0.0D;
    double rad = 0.0D;
    
    Complex(double paramDouble1, double paramDouble2)
    {
      this.lat = paramDouble1;
      this.lng = paramDouble2;
    }
    
    Complex(Complex paramComplex)
    {
      this.lat = paramComplex.lat;
      this.lng = paramComplex.lng;
      this.rad = paramComplex.rad;
    }
    
    Complex() {}
  }
  
  final class MyDropDownListCellRenderer
    extends JLabel
    implements ListCellRenderer
  {
    public MyDropDownListCellRenderer()
    {
      setOpaque(true);
      setVerticalAlignment(0);
    }
    
    public Component getListCellRendererComponent(JList paramJList, Object paramObject, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
    {
      JLabel localJLabel = (JLabel)paramObject;
      if (paramBoolean1)
      {
        setBackground(paramJList.getSelectionBackground());
        setForeground(paramJList.getSelectionForeground());
      }
      else
      {
        setBackground(paramJList.getBackground());
        setForeground(paramJList.getForeground());
      }
      setText(localJLabel.getText());
      setIcon(localJLabel.getIcon());
      return this;
    }
  }
  
  final class MyTableCellRenderer
    extends JLabel
    implements TableCellRenderer
  {
    public MyTableCellRenderer()
    {
      setOpaque(true);
      setVerticalAlignment(0);
    }
    
    public Component getTableCellRendererComponent(JTable paramJTable, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2)
    {
      String str = paramObject.toString();
      if (paramInt2 == 0)
      {
        boolean bool = str.substring(0, 1).equals("*");
        setText("");
        setIcon(bool ? TidesFindNearest.this.tideIcon : TidesFindNearest.this.currentIcon);
      }
      else
      {
        setText(str);
      }
      setEnabled(paramJTable.isEnabled());
      setFont(paramJTable.getFont());
      if (paramBoolean1)
      {
        setBackground(paramJTable.getSelectionBackground());
        setForeground(paramJTable.getSelectionForeground());
      }
      else
      {
        setBackground(paramJTable.getBackground());
        setForeground(paramJTable.getForeground());
      }
      return this;
    }
  }
}


/* Location:              C:\Users\02221224710\Downloads\JTides.jar!\jtides\TidesFindNearest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */