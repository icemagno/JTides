package jtides;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public final class NonModalDialog
  extends JDialog
{
  public NonModalDialog(Frame paramFrame, String paramString1, String paramString2, boolean paramBoolean)
  {
    super(paramFrame, paramBoolean);
    initComponents();
    setTitle(paramString2);
    JOptionPane localJOptionPane = new JOptionPane(paramString1, 1, -1);
    setContentPane(localJOptionPane);
    localJOptionPane.addPropertyChangeListener(new PropertyChangeListener()
    {
      public void propertyChange(PropertyChangeEvent paramAnonymousPropertyChangeEvent)
      {
        if (paramAnonymousPropertyChangeEvent.getPropertyName().equals("value")) {
          NonModalDialog.this.dispose();
        }
      }
    });
    pack();
    setSize(300, 150);
  }
  
  private void initComponents()
  {
    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent paramAnonymousWindowEvent)
      {
        NonModalDialog.this.closeDialog(paramAnonymousWindowEvent);
      }
    });
  }
  
  private void closeDialog(WindowEvent paramWindowEvent)
  {
    setVisible(false);
    dispose();
  }
}


/* Location:              C:\Users\02221224710\Downloads\JTides.jar!\jtides\NonModalDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */