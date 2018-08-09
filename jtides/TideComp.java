package jtides;

import java.awt.Component;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.SimpleTimeZone;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public final class TideComp
{
  String[] unitsStr = { "Meters", "Feet", "Knots" };
  JTides main;
  public Vector fileList = new Vector();
  public TreeSet siteIndex = new TreeSet();
  boolean indexFileValid = false;
  boolean siteIndexValid = false;
  boolean indexValid = false;
  boolean generatingIndex = false;
  boolean indexReplaced = false;
  String[] lookAndFeelClassNames;
  DefaultMutableTreeNode root = new DefaultMutableTreeNode("Tide/Current Selection");
  DefaultTreeModel dtm = new DefaultTreeModel(this.root);
  String tidePath = "";
  
  TideComp(JTides paramJTides)
  {
    this.main = paramJTides;
    getLookAndFeelNames();
  }
  
  private void getLookAndFeelNames()
  {
    UIManager.LookAndFeelInfo[] arrayOfLookAndFeelInfo = UIManager.getInstalledLookAndFeels();
    this.lookAndFeelClassNames = new String[arrayOfLookAndFeelInfo.length];
    for (int i = 0; i < arrayOfLookAndFeelInfo.length; i++) {
      this.lookAndFeelClassNames[i] = arrayOfLookAndFeelInfo[i].getClassName();
    }
  }
  
  public void setupLookAndFeel(int paramInt)
  {
    this.main.configValues.LookAndFeel = paramInt;
    try
    {
      UIManager.setLookAndFeel(this.lookAndFeelClassNames[paramInt]);
      SwingUtilities.updateComponentTreeUI(this.main);
    }
    catch (Exception localException) {}
  }
  
  public void startProgressBar(JProgressBar paramJProgressBar, long paramLong1, long paramLong2, long paramLong3)
  {
    if (paramJProgressBar != null)
    {
      paramJProgressBar.setMinimum((int)paramLong1);
      paramJProgressBar.setMaximum((int)paramLong2);
      paramJProgressBar.setValue((int)paramLong2);
    }
  }
  
  public void updateProgressBar(JProgressBar paramJProgressBar, long paramLong)
  {
    if (paramJProgressBar != null) {
      paramJProgressBar.setValue((int)paramLong);
    }
  }
  
  public void stopProgressBar(JProgressBar paramJProgressBar)
  {
    if (paramJProgressBar != null) {
      paramJProgressBar.setValue(paramJProgressBar.getMinimum());
    }
  }
  
  public String readFile(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    try
    {
      FileInputStream localFileInputStream = new FileInputStream(paramString);
      int i;
      while ((i = localFileInputStream.read()) != -1) {
        localStringBuffer.append((char)i);
      }
      localFileInputStream.close();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return localStringBuffer.toString();
  }
  
  public String readStream(InputStream paramInputStream)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    try
    {
      int i;
      while ((i = paramInputStream.read()) != -1) {
        localStringBuffer.append((char)i);
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return localStringBuffer.toString();
  }
  
  public String srchRplc(int paramInt, String paramString1, String paramString2, String paramString3)
  {
    if (paramString2.compareTo(paramString3) != 0)
    {
      StringBuffer localStringBuffer = new StringBuffer();
      char[] arrayOfChar = paramString1.toCharArray();
      if (paramInt > 0) {
        localStringBuffer.append(arrayOfChar, 0, paramInt);
      }
      int j = paramString2.length();
      int i;
      while ((i = paramString1.indexOf(paramString2, paramInt)) != -1)
      {
        localStringBuffer.append(arrayOfChar, paramInt, i - paramInt);
        localStringBuffer.append(paramString3);
        paramInt = i + j;
      }
      if (paramInt < paramString1.length()) {
        localStringBuffer.append(arrayOfChar, paramInt, paramString1.length() - paramInt);
      }
      return localStringBuffer.toString();
    }
    return paramString1;
  }
  
  public String srchRplc(String paramString1, String paramString2, String paramString3)
  {
    return srchRplc(0, paramString1, paramString2, paramString3);
  }
  
  long setDT(long paramLong, TidesDoc paramTidesDoc)
  {
    paramLong = (paramLong + paramTidesDoc.siteSet.tz * 3600.0D);
    paramLong -= paramLong % 86400L;
    GregorianCalendar localGregorianCalendar = new GregorianCalendar();
    localGregorianCalendar.setTime(new Date((paramLong + 43200L) * 1000L));
    boolean bool = this.main.tideComp.isDST(localGregorianCalendar);
    paramTidesDoc.siteSet.daylightInEffect = (this.main.configValues.daylightTime == 2 | this.main.configValues.daylightTime == 1 & bool);
    return paramLong;
  }
  
  String hmsFormat(int paramInt1, int paramInt2)
  {
    return "" + padChar(paramInt1, 2, "0") + ":" + padChar(paramInt2, 2, "0");
  }
  
  String hmsFormat(int paramInt1, int paramInt2, int paramInt3)
  {
    return hmsFormat(paramInt1, paramInt2) + ":" + padChar(paramInt3, 2, "0");
  }
  
  String formatSunHMS(double paramDouble, boolean paramBoolean)
  {
    String str = "";
    if (paramDouble < 0.0D)
    {
      str = "[Below]";
    }
    else if (paramDouble > 24.0D)
    {
      str = "[Above]";
    }
    else
    {
      int i = (int)paramDouble;
      int j = (int)(paramDouble * 60.0D);
      j %= 60;
      int k = (int)(paramDouble * 3600.0D);
      k %= 60;
      TimeBundle localTimeBundle = hourAmPmFormat(i, " AM", " PM");
      if (paramBoolean) {
        str = padChar(localTimeBundle.hour, 2, "0") + ":" + padChar(j, 2, "0") + ":" + padChar(k, 2, "0") + localTimeBundle.ampm;
      } else {
        str = padChar(localTimeBundle.hour, 2, "0") + ":" + padChar(j, 2, "0") + localTimeBundle.ampm;
      }
    }
    return str;
  }
  
  String formatSunHMS(double paramDouble)
  {
    return formatSunHMS(paramDouble, false);
  }
  
  String padChar(String paramString1, int paramInt, String paramString2)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    paramInt -= paramString1.length();
    for (int i = 0; i < paramInt; i++) {
      localStringBuffer.append(paramString2);
    }
    localStringBuffer.append(paramString1);
    return localStringBuffer.toString();
  }
  
  String padChar(int paramInt1, int paramInt2, String paramString)
  {
    return padChar("" + paramInt1, paramInt2, paramString);
  }
  
  TimeBundle hourAmPmFormat(int paramInt, String paramString1, String paramString2)
  {
    TimeBundle localTimeBundle = new TimeBundle(paramInt);
    if (this.main.configValues.ampmFlag)
    {
      localTimeBundle.ampm = (localTimeBundle.hour >= 12 ? paramString2 : paramString1);
      localTimeBundle.hour %= 12;
      localTimeBundle.hour = (localTimeBundle.hour < 1 ? 12 + localTimeBundle.hour : localTimeBundle.hour);
    }
    return localTimeBundle;
  }
  
  String formatDate(long paramLong, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, String paramString)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar();
    localGregorianCalendar.setTime(new Date(paramLong));
    return formatDate(localGregorianCalendar, paramBoolean1, paramBoolean2, paramBoolean3, paramBoolean4, paramString);
  }
  
  String formatDate(GregorianCalendar paramGregorianCalendar, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, String paramString)
  {
    String str1 = "";
    String str2 = "";
    if (paramBoolean3) {
      str2 = "/" + paramGregorianCalendar.get(1);
    }
    if (paramBoolean1)
    {
      StringBuffer localStringBuffer = new StringBuffer();
      int j = paramGregorianCalendar.get(11);
      localStringBuffer.append(paramGregorianCalendar.get(1));
      localStringBuffer.append("-");
      localStringBuffer.append(paramGregorianCalendar.get(2) + 1);
      localStringBuffer.append("-");
      localStringBuffer.append(paramGregorianCalendar.get(5));
      localStringBuffer.append(paramString);
      localStringBuffer.append(padChar(paramGregorianCalendar.get(11), 2, "0"));
      localStringBuffer.append(":");
      localStringBuffer.append(padChar(paramGregorianCalendar.get(12), 2, "0"));
      localStringBuffer.append(":");
      localStringBuffer.append(padChar(paramGregorianCalendar.get(13), 2, "0"));
      str1 = localStringBuffer.toString();
    }
    else
    {
      if (paramBoolean4) {
        paramGregorianCalendar.add(13, 30);
      }
      int i = paramGregorianCalendar.get(11);
      TimeBundle localTimeBundle = hourAmPmFormat(i, " AM", " PM");
      if (paramBoolean2) {
        str1 = "" + padChar(localTimeBundle.hour, 2, "0") + ":" + padChar(paramGregorianCalendar.get(12), 2, "0") + localTimeBundle.ampm;
      } else {
        str1 = TideConstants.dowNames[(paramGregorianCalendar.get(7) - 1)] + " " + padChar(paramGregorianCalendar.get(2) + 1, 2, "0") + "/" + padChar(paramGregorianCalendar.get(5), 2, "0") + str2 + " " + padChar(localTimeBundle.hour, 2, "0") + ":" + padChar(paramGregorianCalendar.get(12), 2, "0") + ":" + padChar(paramGregorianCalendar.get(13), 2, "0") + localTimeBundle.ampm;
      }
    }
    return str1;
  }
  
  String old_formatDataString(TidesDoc paramTidesDoc, int paramInt, Vector paramVector, boolean paramBoolean1, boolean paramBoolean2, String paramString1, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5, String paramString2)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    Object localObject = { 'L', 'H' };
    char[] arrayOfChar = { 'E', 'F' };
    if (paramTidesDoc.siteSet.current) {
      localObject = arrayOfChar;
    }
    long l = ((TideEvent)paramVector.elementAt(paramInt)).t;
    double d = this.main.tideComp.ConvertHeight(paramTidesDoc.siteSet, ((TideEvent)paramVector.elementAt(paramInt)).height);
    GregorianCalendar localGregorianCalendar = new GregorianCalendar();
    localGregorianCalendar.setTime(new Date(l * 1000L));
    String str1 = formatDate(localGregorianCalendar, paramBoolean1, paramBoolean2, paramBoolean3, true, paramString2);
    String str2;
    if (((TideEvent)paramVector.elementAt(paramInt)).slack)
    {
      if (!paramBoolean1) {
        str2 = "Slack  ";
      } else {
        str2 = "0.0" + paramString2 + "S";
      }
    }
    else
    {
      if (paramBoolean1) {
        str2 = "" + formatDouble(d, 2);
      } else {
        str2 = "" + formatDouble(d, 1);
      }
      if (paramBoolean5) {
        str2 = str2 + (paramBoolean1 ? paramString2 : " ") + (((TideEvent)paramVector.elementAt(paramInt)).high ? localObject[1] : localObject[0]);
      }
    }
    if (paramBoolean1)
    {
      if (paramBoolean4)
      {
        setDT(l, paramTidesDoc);
        Rts localRts2 = this.main.sunComp.compRTS(paramTidesDoc.siteSet, l, 0);
        Rts localRts1 = this.main.sunComp.compRTS(paramTidesDoc.siteSet, l, 1);
        localStringBuffer.append(str1);
        localStringBuffer.append(paramString2);
        localStringBuffer.append(str2);
        localStringBuffer.append(paramString2);
        localStringBuffer.append(paramString1);
        localStringBuffer.append(paramString2);
        localStringBuffer.append(formatSunHMS(localRts1.rise, true));
        localStringBuffer.append(paramString2);
        localStringBuffer.append(formatSunHMS(localRts2.rise, true));
        localStringBuffer.append(paramString2);
        localStringBuffer.append(formatSunHMS(localRts2.transit, true));
        localStringBuffer.append(paramString2);
        localStringBuffer.append(formatSunHMS(localRts2.set, true));
        localStringBuffer.append(paramString2);
        localStringBuffer.append(formatSunHMS(localRts1.set, true));
        localStringBuffer.append(paramString2);
        localStringBuffer.append(paramTidesDoc.siteSet.daylightInEffect ? "1" : "0");
      }
      else
      {
        localStringBuffer.append(str1);
        localStringBuffer.append(paramString2);
        localStringBuffer.append(str2);
        localStringBuffer.append(paramString2);
        localStringBuffer.append(paramString1);
      }
    }
    else {
      localStringBuffer.append(str1 + " " + padString(str2, 7, 2));
    }
    return localStringBuffer.toString();
  }
  
  String formatDataString(TidesDoc paramTidesDoc, int paramInt, Vector paramVector, boolean paramBoolean1, boolean paramBoolean2, String paramString1, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5, String paramString2)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    Object localObject = { 'L', 'H' };
    char[] arrayOfChar = { 'E', 'F' };
    if (paramTidesDoc.siteSet.current) {
      localObject = arrayOfChar;
    }
    long l1 = ((TideEvent)paramVector.elementAt(paramInt)).t;
    double d = this.main.tideComp.ConvertHeight(paramTidesDoc.siteSet, ((TideEvent)paramVector.elementAt(paramInt)).height);
    GregorianCalendar localGregorianCalendar1 = new GregorianCalendar();
    int i = localGregorianCalendar1.getTimeZone().getRawOffset();
    SimpleTimeZone localSimpleTimeZone = new SimpleTimeZone(0, "");
    localSimpleTimeZone.setRawOffset(i);
    localGregorianCalendar1.setTimeZone(localSimpleTimeZone);
    GregorianCalendar localGregorianCalendar2 = new GregorianCalendar();
    localGregorianCalendar2.setTime(new Date(l1 * 1000L));
    long l2 = l1 - (paramTidesDoc.siteSet.tz * 3600.0D);
    localGregorianCalendar1.setTime(new Date(l2 * 1000L));
    String str1 = formatDate(localGregorianCalendar1, paramBoolean1, paramBoolean2, paramBoolean3, true, paramString2);
    String str2 = formatDate(localGregorianCalendar2, paramBoolean1, paramBoolean2, paramBoolean3, true, paramString2);
    String str3;
    if (((TideEvent)paramVector.elementAt(paramInt)).slack)
    {
      if (!paramBoolean1) {
        str3 = "Slack  ";
      } else {
        str3 = "0.0" + paramString2 + "S";
      }
    }
    else
    {
      if (paramBoolean1) {
        str3 = "" + formatDouble(d, 2);
      } else {
        str3 = "" + formatDouble(d, 1);
      }
      if (paramBoolean5) {
        str3 = str3 + (paramBoolean1 ? paramString2 : " ") + (((TideEvent)paramVector.elementAt(paramInt)).high ? localObject[1] : localObject[0]);
      }
    }
    if (paramBoolean1)
    {
      if (paramBoolean4)
      {
        setDT(l1, paramTidesDoc);
        Rts localRts2 = this.main.sunComp.compRTS(paramTidesDoc.siteSet, l1, 0);
        Rts localRts1 = this.main.sunComp.compRTS(paramTidesDoc.siteSet, l1, 1);
        localStringBuffer.append(str1);
        localStringBuffer.append(paramString2);
        localStringBuffer.append(str2);
        localStringBuffer.append(paramString2);
        localStringBuffer.append(str3);
        localStringBuffer.append(paramString2);
        localStringBuffer.append(paramString1);
        localStringBuffer.append(paramString2);
        localStringBuffer.append(formatSunHMS(localRts1.rise, true));
        localStringBuffer.append(paramString2);
        localStringBuffer.append(formatSunHMS(localRts2.rise, true));
        localStringBuffer.append(paramString2);
        localStringBuffer.append(formatSunHMS(localRts2.transit, true));
        localStringBuffer.append(paramString2);
        localStringBuffer.append(formatSunHMS(localRts2.set, true));
        localStringBuffer.append(paramString2);
        localStringBuffer.append(formatSunHMS(localRts1.set, true));
        localStringBuffer.append(paramString2);
        localStringBuffer.append(paramTidesDoc.siteSet.daylightInEffect ? "1" : "0");
      }
      else
      {
        localStringBuffer.append(str1);
        localStringBuffer.append(paramString2);
        localStringBuffer.append(str2);
        localStringBuffer.append(paramString2);
        localStringBuffer.append(str3);
        localStringBuffer.append(paramString2);
        localStringBuffer.append(paramString1);
      }
    }
    else
    {
      int j = paramBoolean5 ? 8 : 6;
      localStringBuffer.append(padString(str2, 8, 0) + padString(str3, j, 2));
    }
    return localStringBuffer.toString();
  }
  
  String padString(String paramString, int paramInt1, int paramInt2)
  {
    int i = paramString.length();
    StringBuffer localStringBuffer = new StringBuffer();
    for (int j = 0; j < paramInt1; j++) {
      localStringBuffer.append(' ');
    }
    j = paramInt2 == 1 ? (paramInt1 - i) / 2 : paramInt2 == 0 ? 0 : paramInt1 - i;
    j = j < 0 ? 0 : j;
    localStringBuffer.replace(j, j + i, paramString);
    return localStringBuffer.toString();
  }
  
  public void tweakFont(Component paramComponent)
  {
    paramComponent.setFont(new Font("Monospaced", paramComponent.getFont().getStyle(), paramComponent.getFont().getSize()));
  }
  
  public void verifyIndex(String paramString1, String paramString2, String paramString3, boolean paramBoolean)
  {
    boolean bool = !readFileList(paramString1, paramString2, this.fileList);
    bool = testIndexFiles(paramString1, paramString2, bool, this.fileList);
    if (bool)
    {
      this.indexReplaced = true;
      buildNewIndex(paramString1, paramString2, this.fileList);
    }
    if (!this.indexFileValid) {
      readFileList(paramString1, paramString2, this.fileList);
    }
    setupIndex1(paramString1, paramString2, paramString3, paramBoolean);
  }
  
  public void setupIndex1(String paramString1, String paramString2, String paramString3, boolean paramBoolean)
  {
    if (!this.siteIndexValid) {
      readIndex(paramString1, paramString2);
    }
    setupIndex2(paramString1, paramString2, paramString3, paramBoolean);
  }
  
  public void setupIndex2(String paramString1, String paramString2, String paramString3, boolean paramBoolean)
  {
    if (!this.indexValid) {
      setupIndex3(paramString1, paramString2);
    }
    if (paramBoolean) {
      this.main.openFileList(paramString3, true);
    }
  }
  
  public void setupIndex3(final String paramString1, final String paramString2)
  {
    if (!this.indexValid)
    {
      Thread local1 = new Thread()
      {
        public void run()
        {
          TideComp.this.setupIndexThread(paramString1, paramString2);
        }
      };
      local1.start();
      try
      {
        local1.join();
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
    }
  }
  
  public void setupIndexThread(String paramString1, String paramString2)
  {
    if (this.root.getChildCount() < 2)
    {
      this.root.removeAllChildren();
      Vector localVector = new Vector();
      int i = this.siteIndex.size();
      int j = 0;
      this.main.startProgressBar("Setting up Index", 0L, j, 0L);
      Iterator localIterator = this.siteIndex.iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        localVector = parseDelimLine(str, "\t");
        recurseTree(this.root, localVector, 0);
        j++;
        if (j % 100 == 0) {
          this.main.updateProgressBar(j);
        }
      }
      this.main.stopProgressBar();
    }
    this.indexValid = true;
  }
  
  private void recurseTree(DefaultMutableTreeNode paramDefaultMutableTreeNode, Vector paramVector, int paramInt)
  {
    DefaultMutableTreeNode localDefaultMutableTreeNode = new DefaultMutableTreeNode("");
    if ((paramInt < 4) && (paramInt < paramVector.size()))
    {
      String str = (String)paramVector.elementAt(paramInt);
      if (paramInt == 0)
      {
        if (str.equals("C")) {
          str = "Current Stations";
        }
        if (str.equals("T")) {
          str = "Tide Stations";
        }
      }
      int i = matchNodes(paramDefaultMutableTreeNode, str);
      if (i < 0)
      {
        localDefaultMutableTreeNode = new DefaultMutableTreeNode(str);
        this.dtm.insertNodeInto(localDefaultMutableTreeNode, paramDefaultMutableTreeNode, paramDefaultMutableTreeNode.getChildCount());
      }
      else
      {
        localDefaultMutableTreeNode = (DefaultMutableTreeNode)paramDefaultMutableTreeNode.getChildAt(i);
      }
      recurseTree(localDefaultMutableTreeNode, paramVector, paramInt + 1);
    }
  }
  
  int matchNodes(DefaultMutableTreeNode paramDefaultMutableTreeNode, String paramString)
  {
    int i = paramDefaultMutableTreeNode.getChildCount();
    int j = 0;
    boolean bool = false;
    while ((j < i) && (!bool))
    {
      DefaultMutableTreeNode localDefaultMutableTreeNode = (DefaultMutableTreeNode)paramDefaultMutableTreeNode.getChildAt(j);
      String str = localDefaultMutableTreeNode.toString();
      bool = paramString.equals(str);
      if (!bool) {
        j++;
      }
    }
    if (!bool) {
      j = -1;
    }
    return j;
  }
  
  String doubleToStr(double paramDouble)
  {
    String str = new Double(paramDouble).toString();
    return str;
  }
  
  double strToDouble(String paramString, boolean paramBoolean)
  {
    double d = new Double(paramString).doubleValue();
    if (paramBoolean) {
      d = Math.abs(d);
    }
    return d;
  }
  
  boolean isDST(GregorianCalendar paramGregorianCalendar)
  {
    boolean bool = false;
    if (this.main.configValues.daylightTime == 1)
    {
      TimeZone localTimeZone = paramGregorianCalendar.getTimeZone();
      bool = localTimeZone.inDaylightTime(paramGregorianCalendar.getTime());
    }
    else
    {
      bool = this.main.configValues.daylightTime == 2;
    }
    return bool;
  }
  
  String FormatDegMin(double paramDouble, char paramChar1, char paramChar2)
  {
    long l2 = (Math.abs(paramDouble) * 6000.0D);
    long l1 = l2 / 6000L;
    l2 %= 6000L;
    String str = "" + l1 + '°' + " " + formatDouble(l2 / 100.0D, 2, false) + "' " + (paramDouble < 0.0D ? paramChar2 : paramChar1);
    return str;
  }
  
  String formatDouble(double paramDouble, int paramInt, boolean paramBoolean)
  {
    String str1 = paramBoolean ? "+" : "";
    String str2 = paramBoolean ? "-" : "";
    String str3 = "000000000000";
    String str4 = "###0." + str3.substring(0, paramInt);
    DecimalFormat localDecimalFormat = new DecimalFormat(str1 + str4 + ";" + str2 + str4);
    return localDecimalFormat.format(paramDouble);
  }
  
  String formatDouble(double paramDouble, int paramInt)
  {
    return formatDouble(paramDouble, paramInt, true);
  }
  
  String FormatLatLng(double paramDouble1, double paramDouble2)
  {
    String str = "Lat. " + FormatDegMin(paramDouble1, 'N', 'S') + " Lng. " + FormatDegMin(paramDouble2, 'E', 'W');
    return str;
  }
  
  int skipWS(String paramString, int paramInt)
  {
    while ((Character.isWhitespace(paramString.charAt(paramInt))) && (paramInt < paramString.length())) {
      paramInt++;
    }
    return paramInt;
  }
  
  FieldData getWSField(String paramString, int paramInt)
  {
    paramInt = skipWS(paramString, paramInt);
    int i = paramInt;
    while ((paramInt < paramString.length()) && (!Character.isWhitespace(paramString.charAt(paramInt)))) {
      paramInt++;
    }
    String str = paramString.substring(i, paramInt);
    return new FieldData(str, paramInt);
  }
  
  String mergeDelimLine(Vector paramVector, String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    int i = paramVector.size();
    for (int j = 0; j < i; j++) {
      localStringBuffer.append((String)paramVector.elementAt(j) + (j < i - 1 ? paramString : ""));
    }
    return localStringBuffer.toString();
  }
  
  Vector parseLine(String paramString)
  {
    paramString = paramString.trim();
    Vector localVector = new Vector();
    int i = 0;
    while (i < paramString.length())
    {
      FieldData localFieldData = getWSField(paramString, i);
      i = localFieldData.pos;
      localVector.add(localFieldData.field);
    }
    return localVector;
  }
  
  Vector parseDelimLine(String paramString1, String paramString2)
  {
    Vector localVector = new Vector();
    int i = 0;
    int k = paramString2.length();
    int j;
    while ((j = paramString1.indexOf(paramString2, i)) != -1)
    {
      String str = paramString1.substring(i, j);
      localVector.addElement(str);
      i = j + k;
    }
    if (i <= paramString1.length()) {
      localVector.addElement(paramString1.substring(i));
    }
    return localVector;
  }
  
  String shortForm(String paramString, int paramInt)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    Vector localVector = parseDelimLine(paramString, "\t");
    for (int i = 0; (i < paramInt) && (i < localVector.size()); i++) {
      localStringBuffer.append((String)localVector.elementAt(i) + "\t");
    }
    return localStringBuffer.toString();
  }
  
  boolean readFileList(String paramString1, String paramString2, Vector paramVector)
  {
    boolean bool = true;
    try
    {
      File localFile = new File(paramString2 + TideConstants.SYSTEM_FILESEP + paramString1);
      if (localFile != null)
      {
        RandomAccessFile localRandomAccessFile = new RandomAccessFile(localFile, "r");
        paramVector.clear();
        int i = 1;
        while ((i != 0) && (bool))
        {
          String str = localRandomAccessFile.readLine();
          if (str.charAt(0) == '-')
          {
            i = 0;
          }
          else if (i != 0)
          {
            Vector localVector = parseDelimLine(str, "\t");
            if (localVector.size() > 1)
            {
              paramVector.add(localVector.elementAt(1));
              if (new File(this.tidePath + TideConstants.SYSTEM_FILESEP + (String)localVector.elementAt(1)) == null) {
                bool = false;
              }
            }
          }
        }
        localRandomAccessFile.close();
      }
      else
      {
        bool = false;
      }
    }
    catch (Exception localException) {}
    return bool;
  }
  
  void readIndex(final String paramString1, final String paramString2)
  {
    Thread local2 = new Thread()
    {
      public void run()
      {
        TideComp.this.readIndexThread(paramString1, paramString2);
      }
    };
    local2.start();
    try
    {
      local2.join();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  void readIndexThread(String paramString1, String paramString2)
  {
    String str1 = paramString2 + TideConstants.SYSTEM_FILESEP + paramString1;
    long l1 = fileSize(str1);
    this.main.startProgressBar("Reading Site Index", 0L, l1, 0L);
    if (this.indexFileValid)
    {
      try
      {
        File localFile = new File(paramString2 + TideConstants.SYSTEM_FILESEP + paramString1);
        if (localFile != null)
        {
          this.siteIndexValid = false;
          this.siteIndex.clear();
          RandomAccessFile localRandomAccessFile = new RandomAccessFile(localFile, "r");
          int i = 0;
          long l2 = 0L;
          String str2 = "";
          this.siteIndex.clear();
          for (int j = 1; (str2 = localRandomAccessFile.readLine()) != null; j = 0)
          {
            label135:
            l2 = localRandomAccessFile.getFilePointer();
            this.main.updateProgressBar(l2);
            if (j == 0) {
              this.siteIndex.add(str2);
            }
            if ((j == 0) || (str2.charAt(0) != '-')) {
              break label135;
            }
          }
          localRandomAccessFile.close();
        }
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
      this.main.stopProgressBar();
      this.siteIndexValid = true;
    }
  }
  
  String getUnitsTag(SiteSet paramSiteSet)
  {
    int i = paramSiteSet.units < 2 ? this.main.configValues.displayUnits : paramSiteSet.units;
    return this.unitsStr[i];
  }
  
  double ConvertHeight(SiteSet paramSiteSet, double paramDouble)
  {
    if (paramSiteSet.units < 2)
    {
      int i = this.main.configValues.displayUnits - paramSiteSet.units;
      if (i == -1) {
        paramDouble *= 0.3048D;
      } else if (i == 1) {
        paramDouble *= 3.280839895013123D;
      }
    }
    return paramDouble;
  }
  
  boolean findStr(FindData paramFindData)
  {
    boolean bool = (paramFindData.index = paramFindData.data.indexOf(paramFindData.srch)) != -1;
    if (bool) {
      paramFindData.index += paramFindData.srch.length();
    }
    return bool;
  }
  
  int checkYear(SiteSet paramSiteSet, int paramInt)
  {
    paramInt = paramInt < paramSiteSet.startYear ? paramSiteSet.startYear : paramInt;
    paramInt = paramInt >= paramSiteSet.endYear ? paramSiteSet.endYear - 1 : paramInt;
    return paramInt;
  }
  
  int useCalendar(Date paramDate, int paramInt)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar();
    localGregorianCalendar.setTime(paramDate);
    return localGregorianCalendar.get(paramInt);
  }
  
  Date checkYear(SiteSet paramSiteSet, Date paramDate)
  {
    int i = useCalendar(paramDate, 1);
    i = checkYear(paramSiteSet, i);
    GregorianCalendar localGregorianCalendar = new GregorianCalendar(i, useCalendar(paramDate, 2), useCalendar(paramDate, 5), 0, 0, 0);
    Date localDate = localGregorianCalendar.getTime();
    return localDate;
  }
  
  void yearCorrect(SiteSet paramSiteSet, int paramInt)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar(paramInt, 0, 1, 0, 0, 0);
    Date localDate = localGregorianCalendar.getTime();
    paramSiteSet.epochTime = (localDate.getTime() / 1000L);
    int i = paramInt - paramSiteSet.startYear;
    i = i < 0 ? 0 : i;
    i = i >= paramSiteSet.equMax ? paramSiteSet.equMax - 1 : i;
    if (paramSiteSet.harmBase.length >= paramSiteSet.constituentMax) {
      for (int j = 0; j < paramSiteSet.constituentMax; j++)
      {
        Harmonic localHarmonic = new Harmonic();
        localHarmonic.epoch = (paramSiteSet.harmBase[j].epoch - paramSiteSet.equArgs[j][i]);
        localHarmonic.amplitude = (paramSiteSet.harmBase[j].amplitude * paramSiteSet.nodeFacts[j][i]);
        paramSiteSet.harm[j] = localHarmonic;
      }
    }
    paramSiteSet.currentYear = paramInt;
  }
  
  void testYear(SiteSet paramSiteSet, long paramLong)
  {
    Date localDate = new Date(paramLong * 1000L);
    int i = useCalendar(localDate, 1);
    checkYear(paramSiteSet, i);
    if (paramSiteSet.currentYear != i) {
      yearCorrect(paramSiteSet, i);
    }
  }
  
  double performSignedRoot(double paramDouble)
  {
    return paramDouble < 0.0D ? -Math.sqrt(-paramDouble) : Math.sqrt(paramDouble);
  }
  
  double timeToTide(SiteSet paramSiteSet, long paramLong, boolean paramBoolean)
  {
    double d1 = paramSiteSet.baseHeight;
    if (paramBoolean) {
      testYear(paramSiteSet, paramLong);
    }
    paramLong -= paramSiteSet.epochTime;
    double d2 = paramLong * 2.777777777777778E-4D;
    for (int i = 0; i < paramSiteSet.constituentMax; i++) {
      d1 += paramSiteSet.harm[i].amplitude * Math.cos(paramSiteSet.constSpeeds[i] * d2 - paramSiteSet.harm[i].epoch);
    }
    if (paramSiteSet.needRoot) {
      d1 = performSignedRoot(d1);
    }
    return d1;
  }
  
  void findHiLoWater(SiteSet paramSiteSet, Date paramDate, int paramInt)
  {
    long l1 = paramDate.getTime() / 1000L;
    long l2 = l1 + 31536000L;
    double d2 = 1.0D;
    double d3 = -1.0D;
    while (l1 < l2)
    {
      double d1 = timeToTide(paramSiteSet, l1, true);
      d3 = d3 < d1 ? d1 : d3;
      d2 = d2 > d1 ? d1 : d2;
      l1 += 7200L;
    }
    paramSiteSet.mLoWater = ConvertHeight(paramSiteSet, d2);
    paramSiteSet.mHiWater = ConvertHeight(paramSiteSet, d3);
    paramSiteSet.gLoWater = ((int)paramSiteSet.mLoWater - 1);
    paramSiteSet.gHiWater = ((int)paramSiteSet.mHiWater + 1);
    paramSiteSet.currentDisplayUnits = this.main.configValues.displayUnits;
  }
  
  double timeToTideDeriv(SiteSet paramSiteSet, long paramLong, int paramInt, boolean paramBoolean)
  {
    double d1 = 0.0D;
    double d3 = 1.570796326794897D * paramInt;
    if (paramBoolean) {
      testYear(paramSiteSet, paramLong);
    }
    paramLong -= paramSiteSet.epochTime;
    double d4 = paramLong * 2.777777777777778E-4D;
    for (int i = 0; i < paramSiteSet.constituentMax; i++)
    {
      double d2 = paramSiteSet.harm[i].amplitude * Math.cos(d3 + paramSiteSet.constSpeeds[i] * d4 - paramSiteSet.harm[i].epoch);
      for (int j = 0; j < paramInt; j++) {
        d2 *= paramSiteSet.constSpeeds[i];
      }
      d1 += d2;
    }
    if (paramSiteSet.needRoot) {
      d1 = performSignedRoot(d1);
    }
    return d1;
  }
  
  long findRoot(SiteSet paramSiteSet, long paramLong1, long paramLong2, double paramDouble)
  {
    long l = paramLong1 - paramLong2;
    int i = 0;
    int k;
    for (int j = paramDouble > 0.0D ? 1 : 0; (i++ < 50) && (Math.abs(l) > 1L); j = k)
    {
      paramLong2 += l;
      double d = timeToTideDeriv(paramSiteSet, paramLong2, 1, false);
      k = d > 0.0D ? 1 : 0;
      d = Math.abs(d);
      if ((paramDouble < d) || (j != k)) {
        l = -l / 2L;
      }
      paramDouble = d;
    }
    return paramLong2;
  }
  
  long findSlackRoot(SiteSet paramSiteSet, long paramLong1, long paramLong2, double paramDouble)
  {
    long l = paramLong1 - paramLong2;
    int i = 0;
    int k;
    for (int j = paramDouble > 0.0D ? 1 : 0; (i++ < 50) && (Math.abs(l) > 1L); j = k)
    {
      paramLong2 += l;
      double d = timeToTide(paramSiteSet, paramLong2, false);
      k = d > 0.0D ? 1 : 0;
      d = Math.abs(d);
      if ((paramDouble < d) || (j != k)) {
        l = -l / 2L;
      }
      paramDouble = d;
    }
    return paramLong2;
  }
  
  long getNextEventTime(SiteSet paramSiteSet, long paramLong, boolean paramBoolean)
  {
    int j = timeToTideDeriv(paramSiteSet, paramLong, 1, false) > 0.0D ? 1 : 0;
    int k = 0;
    int i;
    do
    {
      paramLong += (paramBoolean ? 960L : -960L);
      i = timeToTideDeriv(paramSiteSet, paramLong, 1, false) > 0.0D ? 1 : 0;
    } while ((k++ < 90) && (i == j));
    return paramLong;
  }
  
  void predictSlackEvents(SiteSet paramSiteSet, long paramLong1, long paramLong2, long paramLong3, long paramLong4, Vector paramVector, JProgressBar paramJProgressBar, ThreadStopper paramThreadStopper)
  {
    long l1 = paramLong1;
    int j = timeToTide(paramSiteSet, paramLong1, true) > 0.0D ? 1 : 0;
    int k = 0;
    startProgressBar(paramJProgressBar, paramLong1, paramLong2, 0L);
    for (long l2 = paramLong1; (l2 <= paramLong2) && ((paramThreadStopper == null) || (!paramThreadStopper.stop)); l2 += 960L)
    {
      updateProgressBar(paramJProgressBar, l2);
      double d = timeToTide(paramSiteSet, l2, true);
      int i;
      if ((i = d > 0.0D ? 1 : 0) != j)
      {
        long l3 = findSlackRoot(paramSiteSet, l1, l2, d);
        TideEvent localTideEvent = new TideEvent();
        localTideEvent.t = (l3 + 30L);
        localTideEvent.height = 0.0D;
        localTideEvent.slack = true;
        if ((localTideEvent.t >= paramLong3) && (localTideEvent.t <= paramLong4)) {
          paramVector.add(localTideEvent);
        }
      }
      j = i;
      l1 = l2;
    }
    stopProgressBar(paramJProgressBar);
  }
  
  Vector predictTideEvents(SiteSet paramSiteSet, long paramLong1, long paramLong2, long paramLong3, long paramLong4, JProgressBar paramJProgressBar, ThreadStopper paramThreadStopper)
  {
    startProgressBar(paramJProgressBar, paramLong1, paramLong2, 0L);
    Vector localVector = new Vector();
    long l1 = paramLong1;
    int j = timeToTideDeriv(paramSiteSet, paramLong1, 1, true) > 0.0D ? 1 : 0;
    int k = 0;
    for (long l2 = paramLong1; (l2 <= paramLong2) && ((paramThreadStopper == null) || (!paramThreadStopper.stop)); l2 += 960L)
    {
      updateProgressBar(paramJProgressBar, l2);
      TideEvent localTideEvent = new TideEvent();
      double d = timeToTideDeriv(paramSiteSet, l2, 1, true);
      int i;
      if ((i = d > 0.0D ? 1 : 0) != j)
      {
        long l3 = findRoot(paramSiteSet, l1, l2, d);
        d = timeToTide(paramSiteSet, l3, true);
        localTideEvent.t = l3;
        localTideEvent.height = d;
        localTideEvent.high = (timeToTideDeriv(paramSiteSet, l3, 2, false) < 0.0D);
        if ((localTideEvent.t >= paramLong3) && (localTideEvent.t <= paramLong4)) {
          localVector.add(localTideEvent);
        }
      }
      j = i;
      l1 = l2;
    }
    stopProgressBar(paramJProgressBar);
    if (paramSiteSet.current) {
      predictSlackEvents(paramSiteSet, paramLong1, paramLong2, paramLong3, paramLong4, localVector, paramJProgressBar, paramThreadStopper);
    }
    Collections.sort(localVector, new CompareTC());
    return localVector;
  }
  
  Date IncDecCTime(SiteSet paramSiteSet, Date paramDate, int paramInt1, int paramInt2, int paramInt3)
  {
    Object localObject = new GregorianCalendar(useCalendar(paramDate, 1), useCalendar(paramDate, 2), useCalendar(paramDate, 5), 0, 0, 0);
    if (paramInt3 > 0) {
      ((GregorianCalendar)localObject).add(5, 1);
    } else if (paramInt3 < 0) {
      ((GregorianCalendar)localObject).add(5, -1);
    }
    GregorianCalendar localGregorianCalendar1 = new GregorianCalendar(paramSiteSet.startYear, 0, 1, 0, 0, 0);
    if (((GregorianCalendar)localObject).getTime().getTime() < localGregorianCalendar1.getTime().getTime()) {
      localObject = localGregorianCalendar1;
    }
    int i = ((GregorianCalendar)localObject).get(1);
    int j = ((GregorianCalendar)localObject).get(2);
    int k = ((GregorianCalendar)localObject).get(5);
    j += paramInt2;
    if (j < 1)
    {
      j = 12;
      i--;
    }
    if (j > 12)
    {
      j = 1;
      i++;
    }
    i += paramInt1;
    i = i < paramSiteSet.startYear ? paramSiteSet.startYear : i;
    i = i > paramSiteSet.startYear + paramSiteSet.equMax - 1 ? paramSiteSet.startYear + paramSiteSet.equMax - 1 : i;
    checkYear(paramSiteSet, i);
    GregorianCalendar localGregorianCalendar2 = new GregorianCalendar(i, j - 1, k, 0, 0, 0);
    return localGregorianCalendar2.getTime();
  }
  
  String getFullEntry(String paramString)
  {
    String str = "";
    SortedSet localSortedSet = this.siteIndex.tailSet(paramString);
    if (localSortedSet != null) {
      str = (String)localSortedSet.first();
    }
    return str;
  }
  
  double stringToDouble(String paramString)
  {
    double d = 0.0D;
    try
    {
      d = new Double(paramString).doubleValue();
    }
    catch (Exception localException) {}
    return d;
  }
  
  int stringToInt(String paramString)
  {
    int i = 0;
    try
    {
      i = new Integer(paramString).intValue();
    }
    catch (Exception localException) {}
    return i;
  }
  
  private String readNonBlankLine(RandomAccessFile paramRandomAccessFile)
  {
    String str = null;
    try
    {
      do
      {
        str = paramRandomAccessFile.readLine();
        if (str != null) {
          str = str.trim();
        }
        if (str == null) {
          break;
        }
      } while (str.length() == 0);
    }
    catch (IOException localIOException) {}
    return str;
  }
  
  void readSite(SiteSet paramSiteSet, String paramString, int paramInt)
  {
    paramSiteSet.currentYear = -1;
    paramSiteSet.currentDisplayUnits = -1;
    paramSiteSet.needRoot = false;
    paramSiteSet.current = false;
    paramSiteSet.valid = false;
    long l2 = 0L;
    if (paramString.length() > 0)
    {
      Vector localVector1 = parseDelimLine(paramString, "\t");
      if (localVector1.size() >= 9)
      {
        paramSiteSet.current = ((String)localVector1.elementAt(0)).equals("C");
        paramSiteSet.name = ((String)localVector1.elementAt(3));
        paramSiteSet.shortName = paramSiteSet.name;
        int j = paramSiteSet.name.indexOf(",");
        if (j != -1) {
          paramSiteSet.shortName = paramSiteSet.name.substring(0, j);
        }
        paramSiteSet.lng = stringToDouble((String)localVector1.elementAt(4));
        paramSiteSet.lat = stringToDouble((String)localVector1.elementAt(5));
        Vector localVector2 = parseDelimLine((String)localVector1.elementAt(6), ":");
        int k = stringToInt((String)localVector2.elementAt(0));
        int m = stringToInt((String)localVector2.elementAt(1));
        paramSiteSet.tz = (k + m / 60.0D);
        int i = stringToInt((String)localVector1.elementAt(7));
        long l1 = stringToInt((String)localVector1.elementAt(8));
        paramSiteSet.indexNumber = ((int)l1);
        if (i != paramSiteSet.currentLoadedFile) {
          ReadDataFile(paramSiteSet, i);
        }
        String str2 = this.main.basePath + TideConstants.SYSTEM_FILESEP + paramSiteSet.dataFileName;
        File localFile = new File(str2);
        if (localFile == null)
        {
          errorMessage("Cannot read file ", str2);
          return;
        }
        try
        {
          RandomAccessFile localRandomAccessFile = new RandomAccessFile(localFile, "r");
          localRandomAccessFile.seek(l1);
          String str1 = readNonBlankLine(localRandomAccessFile);
          localVector1 = parseDelimLine(str1, ":");
          double d1 = stringToDouble((String)localVector1.elementAt(0));
          double d2 = stringToDouble((String)localVector1.elementAt(1));
          d1 += (d1 < 0.0D ? -d2 / 60.0D : d2 / 60.0D);
          if (k != 0)
          {
            paramSiteSet.tz = d1;
          }
          else if (localVector1.size() > 2)
          {
            str3 = (String)localVector1.get(2);
            System.out.println("Time zone name: " + str3);
            TimeZone localTimeZone = TimeZone.getTimeZone(str3);
            paramSiteSet.tz = (localTimeZone.getRawOffset() / 3600000);
          }
          str1 = readNonBlankLine(localRandomAccessFile);
          localVector1 = parseLine(str1);
          paramSiteSet.baseHeight = stringToDouble((String)localVector1.elementAt(0));
          String str3 = (String)localVector1.elementAt(1);
          paramSiteSet.units = (str3.indexOf("meters") != -1 ? 0 : 1);
          if (str3.indexOf("knots") != -1) {
            paramSiteSet.units = 2;
          }
          if (str3.indexOf("^2") != -1) {
            paramSiteSet.needRoot = true;
          }
          paramSiteSet.harmBase = new Harmonic[paramSiteSet.constituentMax];
          for (int n = 0; (n < paramSiteSet.constituentMax) && ((str1 = readNonBlankLine(localRandomAccessFile)) != null); n++)
          {
            localVector1 = parseLine(str1);
            if (localVector1.size() > 2)
            {
              Harmonic localHarmonic = new Harmonic();
              localHarmonic.amplitude = stringToDouble((String)localVector1.elementAt(1));
              localHarmonic.epoch = stringToDouble((String)localVector1.elementAt(2));
              localHarmonic.epoch *= 0.01745329251994329D;
              paramSiteSet.harmBase[n] = localHarmonic;
            }
            else
            {
              localRandomAccessFile.close();
              errorMessage("parse error in readSite", str2);
            }
          }
          localRandomAccessFile.close();
        }
        catch (Exception localException)
        {
          localException.printStackTrace();
        }
      }
      paramSiteSet.valid = true;
      paramSiteSet.fullName = paramString;
      yearCorrect(paramSiteSet, paramInt);
      findHiLoWater(paramSiteSet, new GregorianCalendar(paramInt, 0, 1, 0, 0, 0).getTime(), 172800);
    }
  }
  
  String getComDatLine(RandomAccessFile paramRandomAccessFile)
  {
    String str = "";
    try
    {
      while ((str = paramRandomAccessFile.readLine()) != null) {
        if ((str.charAt(0) == '#') && (str.indexOf('!') != -1)) {
          break;
        }
      }
    }
    catch (Exception localException) {}
    if (str != null) {
      str = str.trim();
    }
    return str;
  }
  
  void getNonCommentLine(RandomAccessFile paramRandomAccessFile, ScanData paramScanData)
  {
    paramScanData.line = "";
    try
    {
      while ((paramScanData.line = paramRandomAccessFile.readLine()) != null) {
        if ((paramScanData.line.length() > 0) && (paramScanData.line.charAt(0) != '#')) {
          break;
        }
      }
      if (paramScanData.line != null) {
        paramScanData.line = paramScanData.line.trim();
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  void getNonCommentLinePos(RandomAccessFile paramRandomAccessFile, ScanData paramScanData)
  {
    paramScanData.line = "";
    paramScanData.pos = 0L;
    try
    {
      do
      {
        paramScanData.pos = paramRandomAccessFile.getFilePointer();
        paramScanData.line = paramRandomAccessFile.readLine();
        if (paramScanData.line != null) {
          paramScanData.line = paramScanData.line.trim();
        }
        if (paramScanData.line == null) {
          break;
        }
      } while (paramScanData.line.charAt(0) == '#');
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  String getCommentLine(RandomAccessFile paramRandomAccessFile)
  {
    String str = "";
    try
    {
      while (((str = paramRandomAccessFile.readLine()) != null) && (str.charAt(0) != '#')) {}
    }
    catch (Exception localException) {}
    return str;
  }
  
  long fileDate(String paramString)
  {
    long l = 0L;
    File localFile = new File(paramString);
    if (localFile != null) {
      l = localFile.lastModified();
    }
    return l;
  }
  
  long fileSize(String paramString)
  {
    long l = 0L;
    File localFile = new File(paramString);
    if (localFile != null) {
      l = localFile.length();
    }
    return l;
  }
  
  boolean testIndexFiles(String paramString1, String paramString2, boolean paramBoolean, Vector paramVector)
  {
    int i = paramVector.size();
    paramVector.clear();
    String str1 = paramString2 + TideConstants.SYSTEM_FILESEP + paramString1;
    long l1 = fileDate(str1);
    if (l1 == 0L) {
      paramBoolean = true;
    }
    int j = 0;
    String str2 = ".txt";
    File localFile = new File(paramString2);
    String[] arrayOfString = localFile.list();
    if (arrayOfString != null)
    {
      for (int k = 0; k < arrayOfString.length; k++)
      {
        String str3 = arrayOfString[k].toLowerCase();
        if (str3.lastIndexOf(str2) == str3.length() - str2.length())
        {
          paramString1 = paramString2 + TideConstants.SYSTEM_FILESEP + arrayOfString[k];
          paramVector.add(arrayOfString[k]);
          j++;
          long l2 = fileDate(paramString1);
          if ((l2 == 0L) || (l2 > l1)) {
            paramBoolean = true;
          }
        }
      }
      if (j != i) {
        paramBoolean = true;
      }
    }
    if (!paramBoolean) {
      this.indexFileValid = true;
    }
    return paramBoolean;
  }
  
  void buildNewIndex(final String paramString1, final String paramString2, final Vector paramVector)
  {
    Thread local3 = new Thread()
    {
      public void run()
      {
        TideComp.this.buildNewIndexThread(paramString1, paramString2, paramVector);
      }
    };
    local3.start();
    try
    {
      local3.join();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  void buildNewIndexThread(String paramString1, String paramString2, Vector paramVector)
  {
    this.indexFileValid = false;
    Vector localVector = new Vector();
    try
    {
      String str1 = paramString2 + TideConstants.SYSTEM_FILESEP + paramString1;
      File localFile = new File(str1);
      RandomAccessFile localRandomAccessFile = new RandomAccessFile(localFile, "rw");
      localRandomAccessFile.setLength(0L);
      for (int i = 0; i < paramVector.size(); i++)
      {
        localRandomAccessFile.writeBytes("" + i + "\t" + (String)paramVector.elementAt(i) + TideConstants.SYSTEM_EOL);
        writeSingleIndex(localVector, paramString2, (String)paramVector.elementAt(i), i);
      }
      localRandomAccessFile.writeBytes("--------" + TideConstants.SYSTEM_EOL);
      Collections.sort(localVector);
      i = 0;
      int j = 0;
      int k = localVector.size();
      for (int m = 0; m < localVector.size(); m++)
      {
        String str2 = (String)localVector.elementAt(m);
        j += str2.length();
        localRandomAccessFile.writeBytes(str2 + TideConstants.SYSTEM_EOL);
      }
      localRandomAccessFile.close();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    this.indexFileValid = true;
  }
  
  void writeSingleIndex(Vector paramVector, String paramString1, String paramString2, int paramInt)
  {
    String str1 = paramString1 + TideConstants.SYSTEM_FILESEP + paramString2;
    long l1 = fileSize(str1);
    File localFile = new File(str1);
    try
    {
      RandomAccessFile localRandomAccessFile = new RandomAccessFile(localFile, "r");
      this.main.startProgressBar("Rebuilding Index -- Reading file \"" + paramString2 + "\"", 0L, l1, 0L);
      int j = 0;
      String str2 = "";
      int k = 0;
      long l3 = 0L;
      while (str2 != null)
      {
        String str5 = "";
        String str4 = "";
        double d1 = 0.0D;
        double d2 = 0.0D;
        String str7 = "T";
        int i = 0;
        if ((str2 = getComDatLine(localRandomAccessFile)) != null)
        {
          l3 = localRandomAccessFile.getFilePointer();
          this.main.updateProgressBar(l3);
          int m = 1;
          Object localObject;
          do
          {
            localObject = new FindData(str2, "!longitude:", j);
            if (findStr((FindData)localObject))
            {
              i++;
              str2 = str2.substring(((FindData)localObject).index);
              d2 = stringToDouble(str2);
            }
            else
            {
              ((FindData)localObject).srch = "!latitude:";
              if (findStr((FindData)localObject))
              {
                i++;
                str2 = str2.substring(((FindData)localObject).index);
                d1 = stringToDouble(str2);
              }
            }
            str2 = localRandomAccessFile.readLine();
            if (str2 != null) {
              m = (str2.charAt(0) == '#') && (str2.indexOf('!') != -1) ? 1 : 0;
            } else {
              m = 0;
            }
          } while (m != 0);
          if ((str2 != null) && (str2.charAt(0) != '#'))
          {
            i++;
            str2 = str2.trim();
            String str3 = str2;
            String str6 = "";
            if ((j = str3.lastIndexOf(',')) != -1)
            {
              str6 = str3.substring(j + 1);
              if ((j = str6.indexOf('(')) != -1) {
                str6 = str6.substring(0, j);
              }
            }
            if (str6.indexOf(')') != -1) {
              str6 = "";
            }
            if ((j = str6.indexOf(" Current")) != -1)
            {
              str6 = str6.substring(0, j);
              str7 = "C";
            }
            str6 = str6.trim();
            if (str6.length() == 0) {
              str6 = "Other";
            }
            localObject = new ScanData();
            getNonCommentLinePos(localRandomAccessFile, (ScanData)localObject);
            long l2 = ((ScanData)localObject).pos;
            Vector localVector = parseLine(((ScanData)localObject).line);
            if (localVector.size() > 1)
            {
              str4 = (String)localVector.elementAt(0);
              str5 = ((String)localVector.elementAt(1)).substring(1);
              if ((j = str5.indexOf('/')) != -1) {
                str5 = str5.substring(0, j);
              }
              i += 2;
            }
            if (i == 5)
            {
              String str8 = str7 + "\t" + str5 + "\t" + str6 + "\t" + str3 + "\t" + d2 + "\t" + d1 + "\t" + str4 + "\t" + paramInt + "\t" + l2;
              paramVector.add(str8);
            }
          }
        }
      }
      localRandomAccessFile.close();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    this.main.stopProgressBar();
  }
  
  void errorMessage(String paramString1, String paramString2)
  {
    String str1 = this.main.basePath + "/" + "JTidesData.index";
    String str2 = "Data Format Error: " + paramString1 + ", file " + paramString2 + "\n\nThis error may result from an index file that is not\nsynchronized with the installed data files. The remedy\nis to delete the index file. The file is located at\n" + str1 + " on this system.\nThis file will be automatically regenerated\nteh next time you run JTides.\n\nPress OK to delete the file, Cancel to preserve it.";
    int i = JOptionPane.showConfirmDialog(this.main, str2, "Error reading file", 2);
    if (i == 0)
    {
      File localFile = new File(str1);
      boolean bool = localFile.delete();
      if (bool) {
        JOptionPane.showConfirmDialog(this.main, "Please exit and restart JTides.", "Index File Deleted", -1);
      }
    }
  }
  
  private String readNonWSChars(RandomAccessFile paramRandomAccessFile)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    try
    {
      char c;
      do
      {
        c = (char)paramRandomAccessFile.readByte();
      } while (Character.isWhitespace(c));
      while (!Character.isWhitespace(c))
      {
        localStringBuffer.append(c);
        c = (char)paramRandomAccessFile.readByte();
      }
    }
    catch (Exception localException) {}
    return localStringBuffer.toString();
  }
  
  boolean ReadDataFile(SiteSet paramSiteSet, int paramInt)
  {
    if (paramInt >= this.fileList.size())
    {
      errorMessage("Index file not synchronized", "");
      return false;
    }
    paramSiteSet.dataFileName = ((String)this.fileList.elementAt(paramInt));
    paramSiteSet.equMax = -1;
    paramSiteSet.nodeMax = -1;
    paramSiteSet.constituentMax = -1;
    String str1 = this.main.basePath + TideConstants.SYSTEM_FILESEP + paramSiteSet.dataFileName;
    try
    {
      RandomAccessFile localRandomAccessFile = new RandomAccessFile(new File(str1), "r");
      String str2 = "";
      ScanData localScanData = new ScanData();
      getNonCommentLine(localRandomAccessFile, localScanData);
      if (localScanData.line != null)
      {
        paramSiteSet.constituentMax = stringToInt(localScanData.line);
      }
      else
      {
        localRandomAccessFile.close();
        errorMessage("Cannot read argument count", paramSiteSet.dataFileName);
        return false;
      }
      paramSiteSet.harm = new Harmonic[paramSiteSet.constituentMax];
      paramSiteSet.constSpeeds = new double[paramSiteSet.constituentMax];
      for (int i = 0; (i < paramSiteSet.constituentMax) && (localScanData.line != null); i++)
      {
        getNonCommentLine(localRandomAccessFile, localScanData);
        if (localScanData.line != null)
        {
          Vector localVector = parseLine(localScanData.line);
          double d2 = stringToDouble((String)localVector.elementAt(1));
          d2 *= 0.01745329251994329D;
          paramSiteSet.constSpeeds[i] = d2;
        }
        else
        {
          localRandomAccessFile.close();
          errorMessage("Premature end of constituent data", paramSiteSet.dataFileName);
          return false;
        }
      }
      getNonCommentLine(localRandomAccessFile, localScanData);
      if (localScanData.line != null)
      {
        paramSiteSet.startYear = stringToInt(localScanData.line);
      }
      else
      {
        localRandomAccessFile.close();
        errorMessage("Cannot read base year", paramSiteSet.dataFileName);
        return false;
      }
      getNonCommentLine(localRandomAccessFile, localScanData);
      if (localScanData.line != null)
      {
        paramSiteSet.equMax = stringToInt(localScanData.line);
        paramSiteSet.endYear = (paramSiteSet.startYear + paramSiteSet.equMax);
      }
      else
      {
        localRandomAccessFile.close();
        errorMessage("Cannot read equ count", paramSiteSet.dataFileName);
        return false;
      }
      paramSiteSet.equArgs = new double[paramSiteSet.constituentMax][paramSiteSet.equMax];
      int j;
      double d1;
      for (i = 0; i < paramSiteSet.constituentMax; i++)
      {
        readNonWSChars(localRandomAccessFile);
        for (j = 0; j < paramSiteSet.equMax; j++)
        {
          str2 = readNonWSChars(localRandomAccessFile);
          d1 = stringToDouble(str2);
          d1 *= 0.01745329251994329D;
          paramSiteSet.equArgs[i][j] = d1;
        }
        if (str2 == null)
        {
          localRandomAccessFile.close();
          errorMessage("Premature end of equ data", paramSiteSet.dataFileName);
          return false;
        }
      }
      str2 = readNonWSChars(localRandomAccessFile);
      if (!str2.equals("*END*"))
      {
        localRandomAccessFile.close();
        errorMessage("Missing equ end mark", paramSiteSet.dataFileName);
        return false;
      }
      getNonCommentLine(localRandomAccessFile, localScanData);
      if (localScanData.line != null)
      {
        paramSiteSet.nodeMax = stringToInt(localScanData.line);
      }
      else
      {
        localRandomAccessFile.close();
        errorMessage("Cannot read node count", paramSiteSet.dataFileName);
        return false;
      }
      paramSiteSet.nodeFacts = new double[paramSiteSet.constituentMax][paramSiteSet.nodeMax];
      for (i = 0; i < paramSiteSet.constituentMax; i++)
      {
        readNonWSChars(localRandomAccessFile);
        for (j = 0; j < paramSiteSet.nodeMax; j++)
        {
          str2 = readNonWSChars(localRandomAccessFile);
          d1 = stringToDouble(str2);
          paramSiteSet.nodeFacts[i][j] = d1;
        }
        if (str2 == null)
        {
          localRandomAccessFile.close();
          errorMessage("Premature end of node data", paramSiteSet.dataFileName);
          return false;
        }
      }
      str2 = readNonWSChars(localRandomAccessFile);
      if (!str2.equals("*END*"))
      {
        localRandomAccessFile.close();
        errorMessage("Missing node end mark", paramSiteSet.dataFileName);
        return false;
      }
      localRandomAccessFile.close();
    }
    catch (Exception localException)
    {
      errorMessage("Cannot open file", paramSiteSet.dataFileName);
      return false;
    }
    paramSiteSet.currentLoadedFile = paramInt;
    return true;
  }
  
  final class CompareTC
    implements Comparator
  {
    CompareTC() {}
    
    public int compare(Object paramObject1, Object paramObject2)
    {
      return (int)(((TideEvent)paramObject1).t - ((TideEvent)paramObject2).t);
    }
  }
  
  final class FindData
  {
    String data;
    String srch;
    int index;
    
    public FindData(String paramString1, String paramString2, int paramInt)
    {
      this.data = paramString1;
      this.srch = paramString2;
      this.index = paramInt;
    }
  }
  
  final class ScanData
  {
    public String line = "";
    public long pos = 0L;
    
    ScanData(String paramString, long paramLong)
    {
      this.pos = paramLong;
      this.line = paramString;
    }
    
    ScanData() {}
  }
  
  final class FieldData
  {
    public String field;
    public int pos;
    
    FieldData(String paramString, int paramInt)
    {
      this.pos = paramInt;
      this.field = paramString;
    }
  }
}


/* Location:              C:\Users\02221224710\Downloads\JTides.jar!\jtides\TideComp.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */