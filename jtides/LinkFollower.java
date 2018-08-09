package jtides;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;

public final class LinkFollower
  implements HyperlinkListener
{
  private HelpBrowser browser;
  
  public LinkFollower(HelpBrowser paramHelpBrowser)
  {
    this.browser = paramHelpBrowser;
  }
  
  public void hyperlinkUpdate(HyperlinkEvent paramHyperlinkEvent)
  {
    if (paramHyperlinkEvent.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
    {
      JEditorPane localJEditorPane = (JEditorPane)paramHyperlinkEvent.getSource();
      try
      {
        this.browser.toPage(paramHyperlinkEvent.getDescription());
      }
      catch (Throwable localThrowable)
      {
        localThrowable.printStackTrace();
      }
    }
  }
}


/* Location:              C:\Users\02221224710\Downloads\JTides.jar!\jtides\LinkFollower.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */