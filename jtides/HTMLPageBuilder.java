package jtides;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

public class HTMLPageBuilder
{
  public static final String header = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
  public static final String[] unitName = { "Meters", "Feet", "Knots" };
  public static final String[] dayName = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
  public static final String[] monthShortName = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
  public static final String[] monthLongName = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
  String metaTags;
  JTides main;
  
  public HTMLPageBuilder(JTides paramJTides)
  {
    this.main = paramJTides;
    this.metaTags = ("<meta name=\"generator\" content=\"" + "JTides 5.3" + "\"/>\n<meta name=\"formatter\" content=\"" + "JTides 5.3" + "\"/>\n");
  }
  
  public String createMonthString(TidesDoc paramTidesDoc, String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar(paramInt1, paramInt2, 1, 0, 0, 0);
    int i = localGregorianCalendar.getActualMaximum(5);
    int j = localGregorianCalendar.get(7) - 1;
    int k = (j + i - 1) / 7 + 1;
    String[][] arrayOfString = new String[7][k];
    while (localGregorianCalendar.get(2) == paramInt2)
    {
      str1 = createDayData(paramTidesDoc, localGregorianCalendar, paramInt4, paramBoolean);
      int m = localGregorianCalendar.get(5);
      int n = m + j - 1;
      int i1 = n % 7;
      i2 = n / 7;
      arrayOfString[i1][i2] = str1;
      localGregorianCalendar.add(6, 1);
    }
    String str1 = "";
    if (paramBoolean)
    {
      str1 = "&nbsp;&nbsp;";
      if (paramInt2 > 0) {
        str1 = str1 + wrapTag("&lt;", "a", new StringBuilder().append("href=\"").append(monthLongName[(paramInt2 - 1)]).append(".html\"").toString());
      }
      if (paramInt2 < 11) {
        str1 = str1 + "&nbsp;" + wrapTag("&gt;", "a", new StringBuilder().append("href=\"").append(monthLongName[(paramInt2 + 1)]).append(".html\"").toString());
      }
    }
    StringBuffer localStringBuffer1 = new StringBuffer();
    StringBuffer localStringBuffer2 = new StringBuffer();
    for (int i2 = 0; i2 < 7; i2++)
    {
      String str2 = wrapTag(dayName[i2], "b");
      str2 = setFont(str2, paramInt4, paramBoolean);
      localStringBuffer2.append(wrapTag(str2, "td", "align=\"center\" bgcolor=\"" + this.main.configValues.htmlTitleColor + "\""));
    }
    localStringBuffer1.append(wrapTag(localStringBuffer2.toString(), "tr"));
    for (i2 = 0; i2 < k; i2++)
    {
      localStringBuffer2 = new StringBuffer();
      for (int i3 = 0; i3 < 7; i3++)
      {
        String str5;
        if (arrayOfString[i3][i2] != null) {
          str5 = wrapTag(arrayOfString[i3][i2], "td", "valign=\"top\" bgcolor=\"" + this.main.configValues.htmlCellColor + "\"");
        } else {
          str5 = wrapTag("&nbsp;", "td");
        }
        localStringBuffer2.append(str5);
      }
      localStringBuffer1.append(wrapTag(localStringBuffer2.toString(), "tr"));
    }
    String str3 = wrapTag(localStringBuffer1.toString(), "table", "bgcolor=\"" + this.main.configValues.htmlBgColor + "\" border=\"" + paramInt3 + "\" cellpadding=\"" + this.main.configValues.htmlCellPadding + "\" cellspacing=\"" + this.main.configValues.htmlCellSpacing + "\"");
    String str4 = "";
    if (this.main.tideComp.isDST(localGregorianCalendar)) {
      str4 = paramTidesDoc.siteSet.name + " (UTC" + plusSign((int)paramTidesDoc.siteSet.tz + 1) + ") (DT)";
    } else {
      str4 = paramTidesDoc.siteSet.name + " (UTC" + plusSign((int)paramTidesDoc.siteSet.tz) + ")";
    }
    int i4 = (int)paramTidesDoc.siteSet.tz;
    String str6 = (i4 > 0 ? "+" : "") + i4;
    str4 = str4 + ", " + monthLongName[paramInt2] + " " + paramInt1 + ". Units: " + this.main.tideComp.getUnitsTag(paramTidesDoc.siteSet);
    String str7 = str4 + str1;
    str7 = setFont(str7, paramInt4, paramBoolean);
    String str8 = wrapTag(str7, "b") + "<br/>";
    str8 = str8 + str3;
    if (paramBoolean) {
      str8 = str8 + paramString;
    }
    str8 = wrapTag(str8, "div", "align=\"center\"");
    String str9 = wrapTag(str8, "body");
    String str10 = wrapTag(str4, "title") + this.metaTags;
    str9 = wrapTag(str10, "head") + str9;
    str9 = wrapTag(str9, "html");
    str9 = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n\n" + str9;
    return str9;
  }
  
  public void createMonthPage(TidesDoc paramTidesDoc, String paramString, File paramFile, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    String str = createMonthString(paramTidesDoc, paramString, paramInt1, paramInt2, paramInt3, paramInt4, true);
    File localFile = new File(paramFile, "" + monthLongName[paramInt2] + ".html");
    try
    {
      FileWriter localFileWriter = new FileWriter(localFile);
      localFileWriter.write(str);
      localFileWriter.close();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  private String setFont(String paramString, int paramInt, boolean paramBoolean)
  {
    return paramBoolean ? paramString : wrapTag(paramString, "font", "style=\"font-size: " + paramInt + "pt;\"");
  }
  
  private String createDayData(TidesDoc paramTidesDoc, GregorianCalendar paramGregorianCalendar, int paramInt, boolean paramBoolean)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    int i = paramGregorianCalendar.get(5);
    String str1 = setFont("- " + i + " -", paramInt, paramBoolean);
    String str2 = wrapTag(wrapTag(str1, "b"), "a", "name=\"" + i + "\"");
    str2 = wrapTag(str2, "td", "align=\"center\" colspan=\"2\"");
    localStringBuffer.append(wrapTag(str2, "tr"));
    long l1 = paramGregorianCalendar.getTime().getTime() / 1000L;
    long l2 = l1 + 86400L;
    long l3 = this.main.tideComp.getNextEventTime(paramTidesDoc.siteSet, l1, false);
    long l4 = this.main.tideComp.getNextEventTime(paramTidesDoc.siteSet, l2, true);
    Vector localVector = this.main.tideComp.predictTideEvents(paramTidesDoc.siteSet, l3, l4, l1, l2, null, null);
    for (int j = 0; j < localVector.size(); j++)
    {
      String str4 = this.main.tideComp.formatDataString(paramTidesDoc, j, localVector, false, true, "", false, false, this.main.configValues.htmlIncludeHiLoText, "");
      if (str4.length() != 0)
      {
        str4 = str4.trim();
        int k = str4.lastIndexOf(' ');
        if (this.main.configValues.htmlIncludeHiLoText) {
          k = str4.lastIndexOf(' ', k - 1);
        }
        String str5 = str4.substring(0, k).trim();
        str5 = setFont(str5, paramInt, paramBoolean);
        String str6 = "&nbsp;" + str4.substring(k).trim();
        str6 = setFont(str6, paramInt, paramBoolean);
        String str7 = wrapTag(wrapTag(str5, "tt"), "td", "align=\"left\"");
        str7 = str7 + wrapTag(wrapTag(str6, "tt"), "td", "align=\"right\"");
        str7 = wrapTag(str7, "tr");
        localStringBuffer.append(str7);
      }
    }
    String str3 = wrapTag(localStringBuffer.toString(), "table", "border=\"0\" cellpadding=\"0\" cellspacing=\"0\"");
    return str3;
  }
  
  public String wrapTag(String paramString1, String paramString2)
  {
    return "<" + paramString2 + ">" + paramString1 + "</" + paramString2 + ">\n";
  }
  
  public String wrapTag(String paramString1, String paramString2, String paramString3)
  {
    return "<" + paramString2 + " " + paramString3 + ">" + paramString1 + "</" + paramString2 + ">\n";
  }
  
  private String plusSign(int paramInt)
  {
    String str = paramInt > 0 ? "+" : "";
    return str + paramInt;
  }
}


/* Location:              C:\Users\02221224710\Downloads\JTides.jar!\jtides\HTMLPageBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */