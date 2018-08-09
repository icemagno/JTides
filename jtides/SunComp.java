package jtides;

public final class SunComp
{
  long oldTime;
  double oldLat;
  double oldLng;
  public Pos compPos;
  double compSid;
  double compJD;
  JTides main;
  
  SunComp(JTides paramJTides)
  {
    this.main = paramJTides;
    this.oldTime = -1L;
    this.oldLat = -1.0D;
    this.oldLng = -1.0D;
  }
  
  double tf_to_jd(double paramDouble)
  {
    return paramDouble + 2440587.5D;
  }
  
  double jd_to_tf(double paramDouble)
  {
    return paramDouble - 2440587.5D;
  }
  
  Pos calcsun(double paramDouble)
  {
    Pos localPos = new Pos();
    double d1 = (paramDouble - 2451545.0D) * 2.7378507871321E-5D;
    double d2 = 280.46645D + 36000.76983D * d1 + 3.032E-4D * d1 * d1;
    double d3 = 357.5291D + 35999.0503D * d1 - 1.559E-4D * d1 * d1 - 4.8E-7D * d1 * d1 * d1;
    double d4 = d3 * 0.01745329251994329D;
    double d5 = 0.016708617D - 4.2037E-5D * d1 - 1.236E-7D * d1 * d1;
    double d6 = (1.9146D - 0.004817D * d1 - 1.4E-5D * d1 * d1) * Math.sin(d4);
    d6 += (0.019993D - 1.01E-4D * d1) * Math.sin(2.0D * d4);
    d6 += 2.9E-4D * Math.sin(3.0D * d4);
    double d7 = d2 + d6;
    double d8 = d3 + d6;
    double d9 = 1.000001018D * (1.0D - d5 * d5) / (1.0D + d5 * Math.cos(d8 * 0.01745329251994329D));
    double d10 = 125.04D - 1934.136D * d1;
    double d11 = 0.01745329251994329D * (d7 - 0.00569D - 0.00478D * Math.sin(0.01745329251994329D * d10));
    double d12 = 23.4391666666667D - 0.0130041666666666D * d1 - 1.63888888E-7D * d1 * d1 + 5.03611111111E-8D * d1 * d1 * d1;
    d12 = 0.01745329251994329D * (d12 + 0.00256D * Math.cos(0.01745329251994329D * d10));
    double d13 = 57.29577951308232D * Math.atan2(Math.cos(d12) * Math.sin(d11), Math.cos(d11));
    if (d13 < 0.0D) {
      d13 = 360.0D + d13;
    }
    double d14 = 57.29577951308232D * Math.asin(Math.sin(d12) * Math.sin(d11));
    localPos.lat = d14;
    localPos.lng = d13;
    return localPos;
  }
  
  double sidtime(double paramDouble1, double paramDouble2)
  {
    double d1 = (Math.floor(paramDouble1) + 0.5D - 2451545.0D) * 2.7378507871321E-5D;
    double d2 = 280.46061837D;
    d2 += 360.98564736629D * (paramDouble1 - 2451545.0D);
    d2 += 3.87933E-4D * d1 * d1;
    d2 -= d1 * d1 * d1 * 2.58331180573495E-8D;
    d2 -= paramDouble2;
    d2 *= 0.00277777777777778D;
    d2 -= Math.floor(d2);
    d2 *= 360.0D;
    return d2;
  }
  
  double mod1(double paramDouble)
  {
    double d = paramDouble;
    paramDouble = Math.abs(paramDouble);
    paramDouble -= Math.floor(paramDouble);
    if (d < 0.0D) {
      paramDouble = 1.0D - paramDouble;
    }
    return paramDouble;
  }
  
  Rts rmsTime(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7)
  {
    Rts localRts = new Rts();
    paramDouble4 *= 0.0416666666666667D;
    localRts.transit = ((paramDouble6 - paramDouble3 - paramDouble1) / 360.0D);
    paramDouble5 = 0.01745329251994329D * paramDouble5;
    paramDouble2 = 0.01745329251994329D * paramDouble2;
    paramDouble7 = 0.01745329251994329D * paramDouble7;
    double d = Math.sin(paramDouble5) - Math.sin(paramDouble2) * Math.sin(paramDouble7);
    d /= Math.cos(paramDouble2) * Math.cos(paramDouble7);
    if ((d < 1.0D) && (d > -1.0D))
    {
      d = 57.29577951308232D * Math.acos(d) * 0.00277777777777778D;
      localRts.rise = (mod1(localRts.transit - d + paramDouble4) * 24.0D);
      localRts.set = (mod1(localRts.transit + d + paramDouble4) * 24.0D);
      localRts.transit = (mod1(localRts.transit + paramDouble4) * 24.0D);
    }
    else
    {
      d = d >= 0.0D ? -100.0D : 100.0D;
      localRts.rise = d;
      localRts.transit = d;
      localRts.set = d;
    }
    return localRts;
  }
  
  Rts compRTS(SiteSet paramSiteSet, long paramLong, int paramInt)
  {
    paramLong = (paramLong + paramSiteSet.tz * 3600.0D);
    paramLong -= paramLong % 86400L;
    paramLong = (paramLong - paramSiteSet.tz * 3600.0D);
    double[] arrayOfDouble = { -0.833333333333333D, -6.0D, -12.0D, -18.0D };
    Rts localRts = new Rts();
    if ((paramLong != this.oldTime) || (paramSiteSet.lat != this.oldLat) || (paramSiteSet.lng != this.oldLng))
    {
      this.oldLat = paramSiteSet.lat;
      this.oldLng = paramSiteSet.lng;
      this.oldTime = paramLong;
      d = paramLong / 3600.0D;
      d /= 24.0D;
      d = tf_to_jd(d);
      this.compSid = sidtime(Math.floor(d) + 0.5D, 0.0D);
      this.compJD = d;
      this.compPos = calcsun(d);
    }
    double d = arrayOfDouble[paramInt];
    localRts = rmsTime(this.compSid, paramSiteSet.lat, paramSiteSet.lng, paramSiteSet.tz, d, this.compPos.lng, this.compPos.lat);
    if (paramSiteSet.daylightInEffect)
    {
      localRts.rise += 1.0D;
      localRts.transit += 1.0D;
      localRts.set += 1.0D;
    }
    return localRts;
  }
}


/* Location:              C:\Users\02221224710\Downloads\JTides.jar!\jtides\SunComp.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */