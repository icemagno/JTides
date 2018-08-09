package jtides;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.GregorianCalendar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;

public final class TidesDoc
  extends JPanel
{
  TidePanel panel = null;
  Component clientPanel = null;
  JScrollPane scrollPane;
  JTides main;
  public SiteSet siteSet;
  boolean needInit = true;
  String title;
  int type;
  
  public TidesDoc(JTides paramJTides, int paramInt, String paramString, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4)
  {
    this.main = paramJTides;
    boolean bool = this.main.configValues.maximized;
    setLayout(new BorderLayout());
    this.type = paramInt;
    this.title = paramString;
    this.siteSet = new SiteSet();
    this.siteSet.fullName = paramString;
    this.siteSet.name = paramString;
    init();
    if (this.type == 0) {
      processSiteData(paramString);
    }
    setVisible(true);
  }
  
  private void init()
  {
    initComponents();
    if (this.type == 0)
    {
      this.panel = new TidePanel(this.main, this);
      add(this.panel, "Center");
      this.clientPanel = this.panel;
    }
    else
    {
      Object localObject;
      if (this.type == 1)
      {
        localObject = new SiteTree(this.main);
        add((Component)localObject, "Center");
        ((SiteTree)localObject).getTree().expandRow(0);
        this.siteSet.shortName = "Sites";
        if (((SiteTree)localObject).isVisible()) {
          ((SiteTree)localObject).repaint();
        }
        this.main.currentFinder = this;
        this.clientPanel = ((Component)localObject);
      }
      else if (this.type == 2)
      {
        localObject = new HelpBrowser(this.main);
        add((Component)localObject, "Center");
        this.siteSet.shortName = "Help";
        this.main.currentHelpBrowser = this;
        this.clientPanel = ((Component)localObject);
      }
    }
  }
  
  private void processSiteData(String paramString)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar();
    this.main.tideComp.readSite(this.siteSet, paramString, localGregorianCalendar.get(1));
    resetTitle();
  }
  
  void resetTitle()
  {
    if (this.type == 0) {
      this.main.tabbedPaneManager.refreshTitle();
    }
  }
  
  private String formatTitleDate(GregorianCalendar paramGregorianCalendar)
  {
    String str = TideConstants.dowNames[(paramGregorianCalendar.get(7) - 1)] + " " + this.main.tideComp.padChar(paramGregorianCalendar.get(2) + 1, 2, "0") + "/" + this.main.tideComp.padChar(paramGregorianCalendar.get(5), 2, "0") + "/" + paramGregorianCalendar.get(1);
    return str;
  }
  
  public String getTitleString()
  {
    return this.siteSet.name + " " + formatTitleDate(this.panel.chartCal);
  }
  
  private void initComponents()
  {
    addFocusListener(new FocusAdapter()
    {
      public void focusGained(FocusEvent paramAnonymousFocusEvent)
      {
        TidesDoc.this.formFocusGained(paramAnonymousFocusEvent);
      }
      
      public void focusLost(FocusEvent paramAnonymousFocusEvent)
      {
        TidesDoc.this.formFocusLost(paramAnonymousFocusEvent);
      }
    });
    addKeyListener(new KeyAdapter()
    {
      public void keyReleased(KeyEvent paramAnonymousKeyEvent)
      {
        TidesDoc.this.formKeyReleased(paramAnonymousKeyEvent);
      }
    });
    setLayout(new BorderLayout());
  }
  
  private void formFocusGained(FocusEvent paramFocusEvent)
  {
    this.main.currentSelectedFrame = this;
  }
  
  private void formFocusLost(FocusEvent paramFocusEvent) {}
  
  private void formKeyReleased(KeyEvent paramKeyEvent)
  {
    handleKey(paramKeyEvent);
  }
  
  public void handleKey(KeyEvent paramKeyEvent)
  {
    int i = paramKeyEvent.getKeyCode();
    if (i == 9)
    {
      paramKeyEvent.consume();
      this.main.tabbedPaneManager.moveToNextWindow();
    }
    else if (i == 112)
    {
      this.main.openFile("Help Browser", true);
    }
    else if (this.type == 0)
    {
      ((TidePanel)this.clientPanel).keyHandler(paramKeyEvent);
    }
  }
  
  public void newDisplay()
  {
    resetTitle();
    if (isVisible()) {
      repaint();
    }
  }
}


/* Location:              C:\Users\02221224710\Downloads\JTides.jar!\jtides\TidesDoc.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */