package jtides;

final class Blender
{
  int from;
  int to;
  int step;
  
  Blender(double paramDouble1, double paramDouble2, int paramInt)
  {
    this.from = ((int)paramDouble1 << 16);
    this.to = ((int)paramDouble2 << 16);
    this.step = ((this.to - this.from) / paramInt);
  }
  
  Blender(int paramInt1, int paramInt2, int paramInt3)
  {
    this.from = (paramInt1 << 16);
    this.to = (paramInt2 << 16);
    this.step = ((this.to - this.from) / paramInt3);
  }
  
  public int getNext()
  {
    int i = this.from;
    this.from += this.step;
    return i >>> 16;
  }
}


/* Location:              C:\Users\02221224710\Downloads\JTides.jar!\jtides\Blender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */