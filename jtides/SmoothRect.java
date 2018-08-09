package jtides;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public final class SmoothRect
{
  public void drawRect(BufferedImage paramBufferedImage, Rectangle paramRectangle, int paramInt1, int paramInt2, TideDatum[][] paramArrayOfTideDatum)
  {
    ColorIntBlender localColorIntBlender1 = new ColorIntBlender(paramArrayOfTideDatum[paramInt1][paramInt2].color, paramArrayOfTideDatum[(paramInt1 + 1)][paramInt2].color, paramRectangle.height);
    ColorIntBlender localColorIntBlender2 = new ColorIntBlender(paramArrayOfTideDatum[paramInt1][(paramInt2 + 1)].color, paramArrayOfTideDatum[(paramInt1 + 1)][(paramInt2 + 1)].color, paramRectangle.height);
    for (int i = paramRectangle.y; i < paramRectangle.y + paramRectangle.height; i++)
    {
      ColorIntBlender localColorIntBlender3 = new ColorIntBlender(localColorIntBlender1.getNext(), localColorIntBlender2.getNext(), paramRectangle.width);
      for (int j = paramRectangle.x; j < paramRectangle.x + paramRectangle.width; j++) {
        paramBufferedImage.setRGB(j, i, localColorIntBlender3.getNext());
      }
    }
  }
}


/* Location:              C:\Users\02221224710\Downloads\JTides.jar!\jtides\SmoothRect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */