package jtides;

public final class Harmonic
{
  double amplitude = 0.0D;
  double epoch = 0.0D;
  
  Harmonic() {}
  
  Harmonic(double paramDouble1, double paramDouble2)
  {
    this.amplitude = paramDouble1;
    this.epoch = paramDouble2;
  }
  
  Harmonic(Harmonic paramHarmonic)
  {
    this.amplitude = paramHarmonic.amplitude;
    this.epoch = paramHarmonic.epoch;
  }
  
  public String toString()
  {
    return "{" + this.amplitude + "," + this.epoch + "}";
  }
}


/* Location:              C:\Users\02221224710\Downloads\JTides.jar!\jtides\Harmonic.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */