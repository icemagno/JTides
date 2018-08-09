package jtides;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.TreeSet;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

public final class SiteDataDialog
  extends JDialog
{
  TidesDoc parent;
  private JButton OKBUtton;
  private JPanel jPanel1;
  private JTextArea outputArea;
  
  public SiteDataDialog(TidesDoc paramTidesDoc)
  {
    super(paramTidesDoc.main, "Site Information", false);
    this.parent = paramTidesDoc;
    initComponents();
    pack();
    setSize(500, 300);
    paramTidesDoc.main.tideComp.tweakFont(this.outputArea);
    Dimension localDimension1 = paramTidesDoc.main.getSize();
    Point localPoint = paramTidesDoc.main.getLocation();
    Dimension localDimension2 = getSize();
    setLocation(localPoint.x + (localDimension1.width - localDimension2.width) / 2, localPoint.y + (localDimension1.height - localDimension2.height) / 2);
    setVisible(true);
    createData(paramTidesDoc.siteSet);
  }
  
  private void createData(SiteSet paramSiteSet)
  {
    int i = 26;
    StringBuffer localStringBuffer = new StringBuffer();
    Vector localVector = this.parent.main.tideComp.parseDelimLine(paramSiteSet.fullName, "\t");
    String[] arrayOfString = (String[])localVector.toArray(new String[0]);
    insertPair(localStringBuffer, "Site Name", paramSiteSet.name, i, 0);
    insertPair(localStringBuffer, "Data Type", arrayOfString[0].equals("T") ? "Tide" : "Current", i, 0);
    insertPair(localStringBuffer, "Region", arrayOfString[1], i, 0);
    insertPair(localStringBuffer, "State or Territory", arrayOfString[2], i, 0);
    insertPair(localStringBuffer, "Position", this.parent.main.tideComp.FormatLatLng(paramSiteSet.lat, paramSiteSet.lng), i, 0);
    insertPair(localStringBuffer, "Time Zone", "UTC" + this.parent.panel.plusSign((int)paramSiteSet.tz), i, 0);
    insertPair(localStringBuffer, "Data File", paramSiteSet.dataFileName, i, 0);
    insertPair(localStringBuffer, "Byte Position", arrayOfString[8], i, 0);
    insertPair(localStringBuffer, "Harmonic Constants", "" + paramSiteSet.constituentMax, i, 0);
    insertPair(localStringBuffer, "Valid Prediction years", "" + paramSiteSet.startYear + "-" + paramSiteSet.endYear, i, 0);
    insertPair(localStringBuffer, "Total indexed sites", "" + this.parent.main.tideComp.siteIndex.size(), i, 0);
    this.outputArea.setText(localStringBuffer.toString());
  }
  
  private void insertPair(StringBuffer paramStringBuffer, String paramString1, String paramString2, int paramInt1, int paramInt2)
  {
    paramStringBuffer.append(this.parent.main.tideComp.padString(paramString1, paramInt1, paramInt2) + this.parent.main.tideComp.padString(paramString2, paramInt1, paramInt2) + "\n");
  }
  
  private void initComponents()
  {
    this.jPanel1 = new JPanel();
    this.OKBUtton = new MyJButton();
    this.outputArea = new JTextArea();
    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent paramAnonymousWindowEvent)
      {
        SiteDataDialog.this.closeDialog(paramAnonymousWindowEvent);
      }
    });
    this.OKBUtton.setText("OK");
    this.OKBUtton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        SiteDataDialog.this.OKBUttonActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.jPanel1.add(this.OKBUtton);
    getContentPane().add(this.jPanel1, "South");
    this.outputArea.setBackground(UIManager.getDefaults().getColor("CheckBox.background"));
    this.outputArea.setEditable(false);
    this.outputArea.setMargin(new Insets(8, 8, 8, 8));
    getContentPane().add(this.outputArea, "Center");
  }
  
  private void OKBUttonActionPerformed(ActionEvent paramActionEvent)
  {
    setVisible(false);
    dispose();
  }
  
  private void closeDialog(WindowEvent paramWindowEvent)
  {
    setVisible(false);
    dispose();
  }
}


/* Location:              C:\Users\02221224710\Downloads\JTides.jar!\jtides\SiteDataDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */