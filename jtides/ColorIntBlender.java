package jtides;

final class ColorIntBlender
{
  Blender r;
  Blender g;
  Blender b;
  
  ColorIntBlender(int paramInt1, int paramInt2, int paramInt3)
  {
    this.r = new Blender(paramInt1 >>> 16 & 0xFF, paramInt2 >>> 16 & 0xFF, paramInt3);
    this.g = new Blender(paramInt1 >>> 8 & 0xFF, paramInt2 >>> 8 & 0xFF, paramInt3);
    this.b = new Blender(paramInt1 & 0xFF, paramInt2 & 0xFF, paramInt3);
  }
  
  public int getNext()
  {
    return this.r.getNext() << 16 | this.g.getNext() << 8 | this.b.getNext();
  }
}


/* Location:              C:\Users\02221224710\Downloads\JTides.jar!\jtides\ColorIntBlender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */