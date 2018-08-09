package jtides;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public final class RecentFileList
{
  public TreeMap list = new TreeMap();
  private int max;
  private int insertPoint;
  private JTides main;
  JPopupMenu menu;
  
  public RecentFileList(JTides paramJTides, int paramInt, JPopupMenu paramJPopupMenu)
  {
    this.main = paramJTides;
    this.max = paramInt;
    this.menu = paramJPopupMenu;
    this.insertPoint = paramJPopupMenu.getComponentCount();
  }
  
  public void clear()
  {
    this.list.clear();
    while (this.menu.getComponentCount() - this.insertPoint > 0) {
      this.menu.remove(this.insertPoint);
    }
  }
  
  public void put(long paramLong, String paramString1, String paramString2)
  {
    if (this.list.containsKey(paramString1))
    {
      localObject = (SiteDat)this.list.get(paramString1);
      ((SiteDat)localObject).time = paramLong;
    }
    else
    {
      this.list.put(paramString1, new SiteDat(paramLong, paramString1, paramString2));
    }
    Object localObject = this.list.keySet().iterator();
    for (int i = 0; i < this.list.size(); i++)
    {
      SiteDat localSiteDat = (SiteDat)this.list.get(((Iterator)localObject).next());
      JMenuItem localJMenuItem;
      if (i < this.menu.getComponentCount() - this.insertPoint)
      {
        localJMenuItem = (JMenuItem)this.menu.getComponent(i + this.insertPoint);
        localJMenuItem.setText(localSiteDat.name);
      }
      else
      {
        localJMenuItem = new JMenuItem(localSiteDat.name);
        this.menu.add(localJMenuItem);
        addListener(localJMenuItem);
      }
    }
    if (this.list.size() > this.max) {
      deleteOldestMember();
    }
  }
  
  public Vector getVec()
  {
    Vector localVector = new Vector();
    Iterator localIterator = this.list.keySet().iterator();
    while (localIterator.hasNext())
    {
      SiteDat localSiteDat = (SiteDat)this.list.get(localIterator.next());
      String str = localSiteDat.fullName + "\t" + localSiteDat.time;
      localVector.add(str);
    }
    return localVector;
  }
  
  public String getString()
  {
    Vector localVector = getVec();
    String str = this.main.tideComp.mergeDelimLine(localVector, "|");
    return str;
  }
  
  public void putAll(String paramString)
  {
    Vector localVector1 = this.main.tideComp.parseDelimLine(paramString, "|");
    for (int i = 0; i < localVector1.size(); i++)
    {
      Vector localVector2 = this.main.tideComp.parseDelimLine((String)localVector1.elementAt(i), "\t");
      if (localVector2.size() > 9)
      {
        long l = Long.parseLong((String)localVector2.elementAt(9));
        localVector2.removeElementAt(9);
        String str1 = this.main.tideComp.mergeDelimLine(localVector2, "\t");
        String str2 = (String)localVector2.elementAt(3);
        put(l, str2, str1);
      }
    }
  }
  
  private void deleteOldestMember()
  {
    Iterator localIterator = this.list.keySet().iterator();
    String str = null;
    long l = Long.MAX_VALUE;
    while (localIterator.hasNext())
    {
      SiteDat localSiteDat = (SiteDat)this.list.get(localIterator.next());
      if (localSiteDat.time < l)
      {
        l = localSiteDat.time;
        str = localSiteDat.name;
      }
    }
    if (str != null) {
      this.list.remove(str);
    }
  }
  
  private void addListener(JMenuItem paramJMenuItem)
  {
    paramJMenuItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        RecentFileList.this.menuActionPerformed(paramAnonymousActionEvent);
      }
    });
  }
  
  private void menuActionPerformed(ActionEvent paramActionEvent)
  {
    JMenuItem localJMenuItem = (JMenuItem)paramActionEvent.getSource();
    SiteDat localSiteDat = (SiteDat)this.list.get(localJMenuItem.getText());
    this.main.openFile(localSiteDat.fullName, true);
  }
  
  final class SiteDat
  {
    public long time;
    public String name;
    public String fullName;
    
    SiteDat(long paramLong, String paramString1, String paramString2)
    {
      this.time = paramLong;
      this.name = paramString1;
      this.fullName = paramString2;
    }
  }
}


/* Location:              C:\Users\02221224710\Downloads\JTides.jar!\jtides\RecentFileList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */