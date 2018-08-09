package jtides;

import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.Vector;
import javax.swing.JTabbedPane;

public class TabbedPaneManager
{
  JTabbedPane tabbedPane;
  JTides main;
  
  public TabbedPaneManager(JTabbedPane paramJTabbedPane, JTides paramJTides)
  {
    this.tabbedPane = paramJTabbedPane;
    this.main = paramJTides;
  }
  
  public void addTab(String paramString, boolean paramBoolean)
  {
    TidesDoc localTidesDoc1 = null;
    int i = 0;
    int k = this.tabbedPane.getTabCount();
    for (int j = 0; j < k; j++)
    {
      TidesDoc localTidesDoc2 = (TidesDoc)this.tabbedPane.getComponentAt(j);
      if (paramString.equals(localTidesDoc2.siteSet.fullName))
      {
        i = 1;
        break;
      }
    }
    if (i != 0)
    {
      localTidesDoc1 = (TidesDoc)this.tabbedPane.getComponentAt(j);
    }
    else
    {
      if (paramString.equals("Site Explorer")) {
        localTidesDoc1 = this.main.openSiteExplorer();
      } else if (paramString.equals("Help Browser")) {
        localTidesDoc1 = this.main.openHelpBrowser();
      } else {
        localTidesDoc1 = new TidesDoc(this.main, 0, paramString, true, true, true, true);
      }
      this.tabbedPane.addTab(localTidesDoc1.siteSet.shortName, localTidesDoc1);
      j = this.tabbedPane.indexOfComponent(localTidesDoc1);
      if (j != -1) {
        this.tabbedPane.setToolTipTextAt(j, localTidesDoc1.siteSet.name);
      }
    }
    if (paramBoolean)
    {
      this.tabbedPane.setSelectedComponent(localTidesDoc1);
      localTidesDoc1.requestFocusInWindow();
    }
    refreshTitle();
    this.main.recentFileList.put(new Date().getTime() / 1000L, localTidesDoc1.siteSet.name, localTidesDoc1.siteSet.fullName);
  }
  
  public void refocus()
  {
    TidesDoc localTidesDoc = (TidesDoc)this.tabbedPane.getSelectedComponent();
    if (localTidesDoc != null)
    {
      refreshTitle();
      this.tabbedPane.setSelectedComponent(localTidesDoc);
      localTidesDoc.requestFocusInWindow();
    }
  }
  
  public void moveToNextWindow()
  {
    this.main.setActiveMenu(null);
    int i = this.tabbedPane.getTabCount();
    int j = this.tabbedPane.getSelectedIndex();
    if ((i > 0) && (j >= 0))
    {
      j = (j + 1) % i;
      this.tabbedPane.setSelectedIndex(j);
      refocus();
    }
  }
  
  public void close()
  {
    TidesDoc localTidesDoc = (TidesDoc)this.tabbedPane.getSelectedComponent();
    if (localTidesDoc != null) {
      this.tabbedPane.remove(localTidesDoc);
    }
    refreshTitle();
  }
  
  public void closeAll()
  {
    int i = this.tabbedPane.getTabCount();
    for (int j = i - 1; j >= 0; j--)
    {
      TidesDoc localTidesDoc = (TidesDoc)this.tabbedPane.getComponentAt(j);
      if (localTidesDoc != null) {
        this.tabbedPane.remove(localTidesDoc);
      }
    }
    refreshTitle();
  }
  
  public Vector getList()
  {
    Vector localVector = new Vector();
    int i = this.tabbedPane.getTabCount();
    for (int j = 0; j < i; j++)
    {
      TidesDoc localTidesDoc = (TidesDoc)this.tabbedPane.getComponentAt(j);
      String str = localTidesDoc.siteSet.fullName;
      localVector.add(str);
    }
    return localVector;
  }
  
  public TidesDoc getSelectedDoc()
  {
    return (TidesDoc)this.tabbedPane.getSelectedComponent();
  }
  
  public int getSelectedIndex()
  {
    return this.tabbedPane.getSelectedIndex();
  }
  
  public void setSelectedIndex(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < this.tabbedPane.getTabCount())) {
      this.tabbedPane.setSelectedIndex(paramInt);
    }
  }
  
  public void handleKey(KeyEvent paramKeyEvent)
  {
    TidesDoc localTidesDoc = getSelectedDoc();
    if (localTidesDoc != null) {
      localTidesDoc.handleKey(paramKeyEvent);
    }
  }
  
  public int getTabCount()
  {
    return this.tabbedPane.getTabCount();
  }
  
  public void refreshTitle()
  {
    String str = "JTides 5.3";
    TidesDoc localTidesDoc = getSelectedDoc();
    if ((localTidesDoc != null) && (localTidesDoc.type == 0)) {
      str = str + " / " + localTidesDoc.getTitleString();
    }
    this.main.setTitle(str);
  }
}


/* Location:              C:\Users\02221224710\Downloads\JTides.jar!\jtides\TabbedPaneManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */