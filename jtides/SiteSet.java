package jtides;

public final class SiteSet
{
  public String fullName;
  public String name;
  public String shortName;
  int indexNumber = -1;
  int units;
  int currentDisplayUnits = -1;
  int currentYear = -1;
  boolean daylightInEffect = false;
  boolean current = false;
  boolean needRoot = false;
  boolean valid = false;
  double baseHeight;
  double tz;
  double lat;
  double lng;
  double gHiWater = 10.0D;
  double gLoWater = -2.0D;
  double mHiWater = 10.0D;
  double mLoWater = -1.0D;
  int constituentMax = 0;
  int startYear;
  int equMax;
  int nodeMax;
  int endYear;
  int currentLoadedFile = -1;
  long epochTime;
  String dataFileName;
  double[] constSpeeds;
  double[][] nodeFacts;
  double[][] equArgs;
  Harmonic[] harmBase;
  Harmonic[] harm;
}


/* Location:              C:\Users\02221224710\Downloads\JTides.jar!\jtides\SiteSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */