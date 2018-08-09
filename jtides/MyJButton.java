package jtides;

import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

public final class MyJButton
  extends JButton
{
  Border activeBorder;
  Border passiveBorder;
  
  public MyJButton()
  {
    myInit();
  }
  
  public MyJButton(Icon paramIcon)
  {
    super(paramIcon);
    myInit();
  }
  
  public MyJButton(String paramString)
  {
    super(paramString);
    myInit();
  }
  
  public MyJButton(String paramString, Icon paramIcon)
  {
    super(paramString, paramIcon);
    myInit();
  }
  
  private void myInit()
  {
    this.activeBorder = new BevelBorder(0);
    this.passiveBorder = getBorder();
    addMouseListener(new MouseAdapter()
    {
      public void mouseEntered(MouseEvent paramAnonymousMouseEvent)
      {
        MyJButton.this.formMouseEntered(paramAnonymousMouseEvent);
      }
      
      public void mouseExited(MouseEvent paramAnonymousMouseEvent)
      {
        MyJButton.this.formMouseExited(paramAnonymousMouseEvent);
      }
    });
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    super.setEnabled(paramBoolean);
    if (!paramBoolean) {
      setDefaultBorder();
    }
  }
  
  private Insets getDiffInsets(Insets paramInsets)
  {
    Insets localInsets = getInsets();
    return new Insets(localInsets.top - paramInsets.top, localInsets.left - paramInsets.left, localInsets.bottom - paramInsets.bottom, localInsets.right - paramInsets.right);
  }
  
  private void formMouseEntered(MouseEvent paramMouseEvent)
  {
    if (isEnabled())
    {
      Insets localInsets = getDiffInsets(this.activeBorder.getBorderInsets(this));
      super.setBorder(new CompoundBorder(this.activeBorder, new EmptyBorder(localInsets)));
    }
  }
  
  private void formMouseExited(MouseEvent paramMouseEvent)
  {
    setDefaultBorder();
  }
  
  private void setDefaultBorder()
  {
    Insets localInsets = getDiffInsets(this.passiveBorder.getBorderInsets(this));
    super.setBorder(new CompoundBorder(this.passiveBorder, new EmptyBorder(localInsets)));
  }
}


/* Location:              C:\Users\02221224710\Downloads\JTides.jar!\jtides\MyJButton.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */