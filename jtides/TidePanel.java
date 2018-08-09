package jtides;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.TimeZone;
import java.util.Vector;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

public final class TidePanel
  extends JPanel
  implements Printable
{
  JTides main;
  TidesDoc theDoc;
  GregorianCalendar chartCal;
  long oldTime = 0L;
  HTMLPageBuilder pageBuilder;
  int siteOffset;
  int localOffset;
  int newscalex;
  int oldscalex = -1;
  double oldTimeZone = -100.0D;
  boolean isLocal;
  boolean calCalcBusy = false;
  int scalex;
  int scaley;
  int gstartx;
  int gstarty;
  long lastChangeTime = 0L;
  boolean mouseDown = false;
  int mouseX;
  int mouseY;
  static final String defaultTip = "Click the chart for specific information";
  long oldMatBase = -1L;
  int oldMatHeight = 0;
  int oldMatUnits = 0;
  int oldMatx = 0;
  int oldMaty = 0;
  BufferedImage offScreenImage = null;
  boolean oldSmoothGraph = false;
  boolean smoothGraph = false;
  String oldMatName = "";
  TideDatum[][] tideMat = (TideDatum[][])null;
  public int graphWidth = 1;
  Rts sunData;
  Rts twilightData;
  Color gridColor = Color.green.darker();
  Color lineColor = Color.blue;
  Color zeroColor = Color.black;
  Color currentColor = Color.red;
  Color backgroundColor = Color.white;
  Color textColor = Color.black;
  int divsx;
  int divsy;
  int startx;
  int endx;
  int starty;
  int endy;
  double rangex;
  double rangey;
  private JMenuItem ConfigureDataButton;
  private JMenuItem HelpButton;
  private JMenuItem NearestSitesButton;
  private JMenuItem PrintButton;
  private JMenuItem SiteData;
  private JMenuItem SiteExplorerButton;
  private JMenuItem closeMenuItem;
  private JPopupMenu popupMenu;
  
  public TidePanel(JTides paramJTides, TidesDoc paramTidesDoc)
  {
    this.main = paramJTides;
    this.theDoc = paramTidesDoc;
    this.divsx = 12;
    this.divsy = 8;
    initComponents();
    setFocusTraversalKeys(0, new HashSet());
    setFocusTraversalKeys(1, new HashSet());
    setToolTipText("Click the chart for specific information");
    setChartCalendar();
  }
  
  public GregorianCalendar getCurrentCal()
  {
    this.siteOffset = ((int)(this.theDoc.siteSet.tz * 3600000.0D));
    TimeZone localTimeZone = TimeZone.getDefault();
    if (this.main.configValues.timeZone == -14.0D) {
      this.localOffset = localTimeZone.getRawOffset();
    } else if (this.main.configValues.timeZone == -13.0D) {
      this.localOffset = this.siteOffset;
    } else {
      this.localOffset = ((int)(this.main.configValues.timeZone * 3600000.0D));
    }
    GregorianCalendar localGregorianCalendar1 = new GregorianCalendar();
    localGregorianCalendar1.setTimeZone(localTimeZone);
    localGregorianCalendar1.setTimeInMillis(new Date().getTime());
    GregorianCalendar localGregorianCalendar2 = new GregorianCalendar(localGregorianCalendar1.get(1), localGregorianCalendar1.get(2), localGregorianCalendar1.get(5));
    localGregorianCalendar2.setTimeZone(localTimeZone);
    return localGregorianCalendar2;
  }
  
  public GregorianCalendar getChartCalendar()
  {
    this.lastChangeTime = new Date().getTime();
    return this.chartCal;
  }
  
  private void setChartCalendar()
  {
    this.lastChangeTime = new Date().getTime();
    this.chartCal = getCurrentCal();
  }
  
  public void setChartWidth(int paramInt)
  {
    setToolTipText("Click the chart for specific information");
    if (paramInt == 1)
    {
      this.graphWidth += 1;
      if (this.graphWidth > 7) {
        this.graphWidth = 30;
      }
    }
    else if (paramInt == -1)
    {
      if (this.graphWidth == 30) {
        this.graphWidth = 8;
      }
      this.graphWidth -= 1;
      if (this.graphWidth == 0) {
        this.graphWidth = 1;
      }
    }
    else if (paramInt == 0)
    {
      this.graphWidth = 1;
    }
    else if (paramInt == 30)
    {
      this.graphWidth = 30;
    }
    else if (paramInt == 60)
    {
      this.graphWidth = 60;
    }
    else if (paramInt == -2)
    {
      this.graphWidth = -1;
      setToolTipText("");
    }
  }
  
  public void setPrintDims(PageFormat paramPageFormat)
  {
    this.main.configValues.printOrientation = paramPageFormat.getOrientation();
    PageFormat localPageFormat = new PageFormat();
    localPageFormat.setPaper(paramPageFormat.getPaper());
    localPageFormat.setOrientation(1);
    this.main.configValues.printWidth = localPageFormat.getWidth();
    this.main.configValues.printHeight = localPageFormat.getHeight();
    this.main.configValues.printIWidth = localPageFormat.getImageableWidth();
    this.main.configValues.printIHeight = localPageFormat.getImageableHeight();
    this.main.configValues.printIX = localPageFormat.getImageableX();
    this.main.configValues.printIY = localPageFormat.getImageableY();
  }
  
  public void getPrintDims(PageFormat paramPageFormat)
  {
    if (this.main.configValues.printWidth > 0.0D)
    {
      Paper localPaper = new Paper();
      localPaper.setSize(this.main.configValues.printWidth, this.main.configValues.printHeight);
      localPaper.setImageableArea(this.main.configValues.printIX, this.main.configValues.printIY, this.main.configValues.printIWidth, this.main.configValues.printIHeight);
      paramPageFormat.setPaper(localPaper);
      paramPageFormat.setOrientation(this.main.configValues.printOrientation);
    }
  }
  
  public void doPrint()
  {
    PrinterJob localPrinterJob = PrinterJob.getPrinterJob();
    PageFormat localPageFormat1 = localPrinterJob.defaultPage();
    localPageFormat1.setOrientation(0);
    getPrintDims(localPageFormat1);
    PageFormat localPageFormat2 = localPrinterJob.pageDialog(localPageFormat1);
    if (localPageFormat1 != localPageFormat2)
    {
      setPrintDims(localPageFormat2);
      Book localBook = new Book();
      localBook.append(this, localPageFormat2);
      localPrinterJob.setPageable(localBook);
      if (localPrinterJob.printDialog()) {
        try
        {
          localPrinterJob.print();
        }
        catch (Exception localException)
        {
          localException.printStackTrace();
        }
      }
    }
  }
  
  public int print(Graphics paramGraphics, PageFormat paramPageFormat, int paramInt)
    throws PrinterException
  {
    if (paramInt != 0) {
      return 1;
    }
    Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
    localGraphics2D.translate(paramPageFormat.getImageableX(), paramPageFormat.getImageableY());
    Rectangle localRectangle = localGraphics2D.getClipBounds();
    paintImage(localGraphics2D, localRectangle, true);
    return 0;
  }
  
  private void checkClock()
  {
    long l = new Date().getTime();
    GregorianCalendar localGregorianCalendar = new GregorianCalendar();
    int i = localGregorianCalendar.get(6);
    int j = this.chartCal.get(6);
    if ((i != j) && (l > this.lastChangeTime + JTides.returnToCurrentTimeDelayMillis))
    {
      setChartCalendar();
      this.theDoc.resetTitle();
    }
  }
  
  public void paintComponent(Graphics paramGraphics)
  {
    checkClock();
    Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
    Rectangle localRectangle = new Rectangle(0, 0, getSize().width, getSize().height);
    paintImage(localGraphics2D, localRectangle, false);
  }
  
  public void paintImage(Graphics2D paramGraphics2D, Rectangle paramRectangle, boolean paramBoolean)
  {
    RenderingHints localRenderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    paramGraphics2D.addRenderingHints(localRenderingHints);
    if (this.oldTimeZone != this.main.configValues.timeZone)
    {
      setChartCalendar();
      this.oldTimeZone = this.main.configValues.timeZone;
    }
    this.isLocal = (this.main.configValues.timeZone != -13.0D);
    if ((this.theDoc.siteSet.units < 2) && (this.theDoc.siteSet.currentDisplayUnits != this.main.configValues.displayUnits)) {
      this.main.tideComp.findHiLoWater(this.theDoc.siteSet, new GregorianCalendar(this.chartCal.get(1), 0, 1, 0, 0, 0).getTime(), 172800);
    }
    if ((paramBoolean) && (paramRectangle.height > paramRectangle.width)) {
      paramRectangle.height /= 2;
    }
    paramGraphics2D.setColor(Color.white);
    paramGraphics2D.fillRect(paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height);
    this.startx = 0;
    this.endx = (this.graphWidth * 24);
    this.starty = ((int)this.theDoc.siteSet.gLoWater);
    this.endy = ((int)this.theDoc.siteSet.gHiWater);
    if ((this.endy - this.starty) % 2 != 0) {
      this.endy += 1;
    }
    this.divsy = 8;
    int[] arrayOfInt = { 8, 10, 6, 12, 14, 7, 9, 11, 0 };
    for (int i = 0; (arrayOfInt[i] != 0) && ((this.starty - this.endy) % arrayOfInt[i] != 0); i++) {}
    if (arrayOfInt[i] != 0) {
      this.divsy = arrayOfInt[i];
    }
    this.rangex = (this.endx - this.startx);
    this.rangey = (this.endy - this.starty);
    int j = paramRectangle.width;
    int k = paramRectangle.height;
    this.gstartx = (paramRectangle.x + j / 16);
    this.gstarty = (paramRectangle.y + k / 32);
    this.scalex = (j - this.gstartx - j / 30);
    this.scaley = (k - this.gstarty - k / 20);
    if (!paramBoolean)
    {
      m = j / 64;
      this.gstartx += m;
      this.scalex -= m * 2;
      m = k / 64;
      this.gstarty += m;
      this.scaley -= m * 2;
    }
    int m = this.graphWidth == -1 ? this.scalex / 66 : this.scalex / 60;
    Font localFont = new Font("Monospaced", this.main.configValues.boldFont ? 1 : 0, m);
    paramGraphics2D.setFont(localFont);
    if (this.graphWidth == -1) {
      DrawCalendar(paramGraphics2D, paramRectangle, paramBoolean);
    } else if (this.graphWidth <= 7) {
      drawGraph(paramGraphics2D, paramBoolean);
    } else {
      DrawColorMat(paramGraphics2D, j, k);
    }
  }
  
  void drawTideCurve(Graphics2D paramGraphics2D, boolean paramBoolean)
  {
    paramGraphics2D.setColor(this.lineColor);
    long l = this.chartCal.getTime().getTime() / 1000L;
    if (this.main.configValues.thickLine) {
      paramGraphics2D.setStroke(new BasicStroke(2.0F));
    }
    GraphicsData localGraphicsData = new GraphicsData(paramGraphics2D);
    localGraphicsData.start = true;
    double d1 = (this.endx - this.startx) / this.scalex;
    double d2 = paramBoolean ? d1 : 0.25D;
    for (localGraphicsData.x = this.startx; localGraphicsData.x <= this.endx + 0.001D; localGraphicsData.x += d2)
    {
      localGraphicsData.y = this.main.tideComp.timeToTide(this.theDoc.siteSet, (int)(l + localGraphicsData.x * 3600.0D), true);
      localGraphicsData.y = this.main.tideComp.ConvertHeight(this.theDoc.siteSet, localGraphicsData.y);
      drawLine(localGraphicsData);
    }
    if (this.main.configValues.thickLine) {
      paramGraphics2D.setStroke(new BasicStroke(1.0F));
    }
  }
  
  void drawLine(GraphicsData paramGraphicsData)
  {
    int i = (int)(this.gstartx + (paramGraphicsData.x - this.startx) * this.scalex / (this.endx - this.startx));
    int j = (int)(this.gstarty + this.scaley - (paramGraphicsData.y - this.starty) * this.scaley / (this.endy - this.starty));
    if (!paramGraphicsData.start) {
      paramGraphicsData.g.drawLine(paramGraphicsData.ox, paramGraphicsData.oy, i, j);
    }
    paramGraphicsData.ox = i;
    paramGraphicsData.oy = j;
    paramGraphicsData.start = false;
  }
  
  void makeGrid(Graphics paramGraphics, boolean paramBoolean)
  {
    int i = this.gstarty;
    int j = this.gstarty + this.scaley;
    FontMetrics localFontMetrics = paramGraphics.getFontMetrics();
    Rectangle localRectangle = localFontMetrics.getStringBounds("00:00", paramGraphics).getBounds();
    int k = this.scalex / 96;
    int m = this.scaley / 24;
    int n = this.graphWidth == 1 ? this.divsx : this.graphWidth;
    if (paramBoolean) {
      n = 12;
    }
    paramGraphics.setColor(this.gridColor);
    int i3;
    Object localObject;
    String str;
    for (int i2 = 0; i2 <= n; i2++)
    {
      i3 = (int)(this.gstartx + i2 * this.scalex / n);
      if ((this.main.configValues.chartGrid) && (!paramBoolean))
      {
        paramGraphics.setColor(this.gridColor);
        paramGraphics.drawLine(i3, i, i3, j);
      }
      if ((this.main.configValues.gridNums) && ((this.graphWidth == 1) || (paramBoolean)))
      {
        int i1;
        if (paramBoolean) {
          i1 = i2 * 120;
        } else {
          i1 = (int)((this.endx - this.startx) * i2 / this.divsx + this.startx) * 60;
        }
        int i4 = i1 / 60 % 24;
        int i5 = i1 % 60;
        localObject = this.main.tideComp.hourAmPmFormat(i4, "a", "p");
        str = this.main.tideComp.hmsFormat(((TimeBundle)localObject).hour, i5) + ((TimeBundle)localObject).ampm;
        paramGraphics.setColor(this.textColor);
        paramGraphics.drawString(str, i3 - localRectangle.width / 2, j + m);
      }
    }
    if (!paramBoolean)
    {
      i = this.gstartx;
      j = this.gstartx + this.scalex;
      for (i2 = 0; i2 <= this.divsy; i2++)
      {
        i3 = (int)(this.scaley + this.gstarty - i2 * this.scaley / this.divsy);
        if (this.main.configValues.chartGrid)
        {
          paramGraphics.setColor(this.gridColor);
          paramGraphics.drawLine(i, i3, j, i3);
        }
        if (this.main.configValues.gridNums)
        {
          double d = (this.endy - this.starty) * i2 / this.divsy + this.starty;
          str = this.main.tideComp.formatDouble(d, 1, false);
          localObject = localFontMetrics.getStringBounds(str, paramGraphics).getBounds();
          paramGraphics.setColor(this.textColor);
          paramGraphics.drawString(str, this.gstartx - ((Rectangle)localObject).width - k, i3 + localRectangle.height / 4);
        }
      }
      if (this.main.configValues.chartGrid)
      {
        i2 = (int)(this.gstarty + this.scaley * ((this.rangey + this.starty) / this.rangey));
        if ((i2 >= this.gstarty) && (i2 <= this.gstarty + this.scaley))
        {
          paramGraphics.setColor(this.zeroColor);
          paramGraphics.drawLine(i, i2, j, i2);
        }
      }
    }
  }
  
  String createTimeLabel()
  {
    String str = "";
    int i = (int)this.main.configValues.timeZone;
    switch (i)
    {
    case -14: 
      str = "(system time)";
      break;
    case -13: 
      str = "(site time)";
      break;
    default: 
      str = "(GMT" + (i >= 0 ? "+" : "") + i + ")";
    }
    return str;
  }
  
  void drawCurrent(Graphics paramGraphics)
  {
    GregorianCalendar localGregorianCalendar1 = new GregorianCalendar();
    TimeZone localTimeZone = TimeZone.getDefault();
    localTimeZone.setRawOffset(this.siteOffset);
    localGregorianCalendar1.setTimeZone(localTimeZone);
    localGregorianCalendar1.setTimeInMillis(new Date().getTime() + this.siteOffset);
    double d1 = new Date().getTime();
    d1 = Math.IEEEremainder((d1 + this.siteOffset) / 3600000.0D, 24.0D);
    if (d1 < 0.0D) {
      d1 = 24.0D + d1;
    }
    Dimension localDimension = getSize();
    int i = localDimension.width;
    double d2;
    double d3;
    if (this.mouseDown)
    {
      d2 = this.mouseX;
      d2 = (this.mouseX - this.gstartx) / this.scalex;
      d2 = d2 < 0.0D ? 0.0D : d2;
      d2 = d2 > 1.0D ? 1.0D : d2;
      d3 = this.mouseY - this.scaley / 48;
      d3 = d3 < this.gstarty ? this.gstarty : d3;
      d3 = d3 > this.gstarty + this.scaley ? this.gstarty + this.scaley : d3;
    }
    else
    {
      d2 = (localGregorianCalendar1.getTime().getTime() - this.chartCal.getTime().getTime() - this.siteOffset) / 3600000.0D / this.rangex;
      d2 = Math.IEEEremainder(d2, 1.0D);
      if (d2 < 0.0D) {
        d2 = 1.0D + d2;
      }
      d3 = this.starty + this.scaley - this.scaley / 8;
      if ((d2 < 0.0D) || (d2 > 1.0D)) {
        return;
      }
    }
    double d4 = (d2 - this.startx) * this.rangex;
    long l = (this.chartCal.getTime().getTime() / 1000L + d2 * this.rangex * 3600.0D);
    double d5 = this.main.tideComp.timeToTide(this.theDoc.siteSet, l, true);
    double d6 = this.main.tideComp.ConvertHeight(this.theDoc.siteSet, d5);
    int j = (int)(this.gstartx + (d4 - this.startx) / this.rangex * this.scalex);
    paramGraphics.setColor(this.currentColor);
    paramGraphics.drawLine(j, this.gstarty, j, this.gstarty + this.scaley);
    boolean bool = this.graphWidth == 1;
    String str1 = this.main.tideComp.getUnitsTag(this.theDoc.siteSet);
    GregorianCalendar localGregorianCalendar2 = new GregorianCalendar();
    localTimeZone.setRawOffset(this.localOffset);
    localGregorianCalendar2.setTimeZone(localTimeZone);
    localGregorianCalendar2.setTimeInMillis(l * 1000L);
    String str2 = this.main.tideComp.formatDate(localGregorianCalendar2, false, bool, false, false, "") + " " + this.main.tideComp.formatDouble(d6, 1) + " " + str1.charAt(0);
    String str3 = createTimeLabel();
    FontMetrics localFontMetrics = paramGraphics.getFontMetrics();
    Rectangle localRectangle1 = localFontMetrics.getStringBounds(str2, paramGraphics).getBounds();
    Rectangle localRectangle2 = localFontMetrics.getStringBounds(str3, paramGraphics).getBounds();
    int k = localRectangle1.width + 8;
    int m = localRectangle2.width + 8;
    int n = k > m ? k : m;
    int i1 = localRectangle1.height * 2 + 4;
    int i2 = j - n / 2;
    int i3 = (int)(d3 + 2.0D);
    i2 = i2 < 0 ? 0 : i2;
    i2 = i2 + n > localDimension.width ? localDimension.width - n : i2;
    paramGraphics.setColor(this.backgroundColor);
    paramGraphics.fillRect(i2, i3 - i1, n, i1);
    paramGraphics.setColor(this.currentColor);
    paramGraphics.drawRect(i2, i3 - i1, n, i1);
    paramGraphics.setColor(this.textColor);
    int i4 = (k - m) / 2;
    i4 = i4 < 0 ? 0 : i4;
    paramGraphics.drawString(str2, i2 + 4, (int)d3 - localFontMetrics.getMaxDescent() - localRectangle1.height);
    paramGraphics.drawString(str3, i2 + 4 + i4, (int)d3 - localFontMetrics.getMaxDescent());
    if (this.mouseDown) {}
  }
  
  int ShowText(Graphics paramGraphics, int paramInt1, int paramInt2, String paramString, boolean paramBoolean)
  {
    FontMetrics localFontMetrics = paramGraphics.getFontMetrics();
    Rectangle localRectangle1 = localFontMetrics.getStringBounds(paramString, paramGraphics).getBounds();
    int i = paramInt1 - 4;
    int j = paramInt2;
    int k = localRectangle1.width + 8;
    int m = localRectangle1.height + 4;
    if ((paramBoolean) && (this.main.configValues.listBackground))
    {
      Rectangle localRectangle2 = new Rectangle(i, j, k, m);
      paramGraphics.setColor(this.backgroundColor);
      paramGraphics.fillRect(i, j, k, m);
    }
    paramGraphics.setColor(this.textColor);
    paramGraphics.drawString(paramString, paramInt1, paramInt2 + localRectangle1.height - localFontMetrics.getDescent());
    return paramInt2 + localRectangle1.height;
  }
  
  String plusSign(int paramInt)
  {
    String str = paramInt > 0 ? "+" : "";
    return str + paramInt;
  }
  
  int DrawSitePosText(Graphics paramGraphics, int paramInt)
  {
    int i = this.gstartx + this.scalex / 96;
    String str1 = "";
    if (this.main.tideComp.isDST(this.chartCal)) {
      str1 = this.theDoc.siteSet.name + " (UTC" + plusSign((int)this.theDoc.siteSet.tz + 1) + ") (Daylight Time)";
    } else {
      str1 = this.theDoc.siteSet.name + " (UTC" + plusSign((int)this.theDoc.siteSet.tz) + ")";
    }
    paramInt = ShowText(paramGraphics, i, paramInt, str1, true);
    str1 = this.main.tideComp.FormatLatLng(this.theDoc.siteSet.lat, this.theDoc.siteSet.lng);
    String str2 = ". Units: " + this.main.tideComp.getUnitsTag(this.theDoc.siteSet);
    int j;
    if ((j = str2.indexOf("^")) > 0) {
      str2 = str2.substring(0, j);
    }
    paramInt = ShowText(paramGraphics, i, paramInt, str1 + str2, true);
    return paramInt;
  }
  
  void CompSunData(long paramLong, int paramInt)
  {
    if (paramInt == 0) {
      this.sunData = this.main.sunComp.compRTS(this.theDoc.siteSet, paramLong, paramInt);
    } else {
      this.twilightData = this.main.sunComp.compRTS(this.theDoc.siteSet, paramLong, paramInt);
    }
  }
  
  void DrawTimeBox(Graphics paramGraphics, int paramInt1, int paramInt2, double paramDouble1, double paramDouble2, Color paramColor)
  {
    int i = (int)(this.gstartx + paramDouble1 / 24.0D * this.scalex);
    int j = (int)(this.gstartx + paramDouble2 / 24.0D * this.scalex);
    int k = paramInt1;
    int m = paramInt2;
    paramGraphics.setColor(paramColor);
    paramGraphics.fillRect(i, k, j - i, m - k);
  }
  
  void DrawSunLines(Graphics paramGraphics)
  {
    if (this.graphWidth == 1)
    {
      int i = this.gstarty;
      int j = i + this.scaley;
      Color localColor1;
      Color localColor2;
      Color localColor3;
      if (this.main.configValues.sunGraphicDark)
      {
        localColor1 = new Color(192, 192, 192);
        localColor2 = new Color(193, 193, 255);
        localColor3 = new Color(255, 255, 204);
      }
      else
      {
        localColor1 = new Color(220, 220, 220);
        localColor2 = new Color(220, 220, 255);
        localColor3 = new Color(255, 255, 220);
      }
      Rts localRts1 = this.twilightData;
      Rts localRts2 = this.sunData;
      if (localRts1.rise < 0.0D)
      {
        localRts1.rise = 12.0D;
        localRts1.set = 12.0D;
      }
      else if (localRts1.rise > 24.0D)
      {
        localRts1.rise = 0.0D;
        localRts1.set = 24.0D;
      }
      if (localRts2.rise < 0.0D)
      {
        localRts2.rise = 12.0D;
        localRts2.set = 12.0D;
      }
      else if (localRts2.rise > 24.0D)
      {
        localRts2.rise = 0.0D;
        localRts2.set = 24.0D;
      }
      if (localRts2.rise > localRts2.set)
      {
        localRts2.rise = 0.0D;
        localRts2.set = 24.0D;
      }
      if (localRts1.rise > localRts1.set)
      {
        localRts1.rise = 0.0D;
        localRts1.set = 24.0D;
      }
      DrawTimeBox(paramGraphics, i, j, 0.0D, localRts1.rise, localColor1);
      DrawTimeBox(paramGraphics, i, j, localRts1.set, 24.0D, localColor1);
      DrawTimeBox(paramGraphics, i, j, localRts1.rise, localRts2.rise, localColor2);
      DrawTimeBox(paramGraphics, i, j, localRts2.set, localRts1.set, localColor2);
      DrawTimeBox(paramGraphics, i, j, localRts2.rise, localRts2.set, localColor3);
    }
  }
  
  int DrawSunData(Graphics paramGraphics, int paramInt1, String paramString1, String paramString2, int paramInt2)
  {
    int i = this.gstartx + this.scalex / 96;
    String str2 = "";
    String str1;
    String str3;
    if (paramInt2 == 0)
    {
      str1 = this.main.tideComp.formatSunHMS(this.sunData.rise);
      str2 = " Transit " + this.main.tideComp.formatSunHMS(this.sunData.transit) + ",";
      str3 = this.main.tideComp.formatSunHMS(this.sunData.set);
    }
    else
    {
      str1 = this.main.tideComp.formatSunHMS(this.twilightData.rise);
      str3 = this.main.tideComp.formatSunHMS(this.twilightData.set);
    }
    String str4 = paramString1 + " " + str1 + " " + str2 + " " + paramString2 + " " + str3;
    paramInt1 = ShowText(paramGraphics, i, paramInt1, str4, true);
    return paramInt1;
  }
  
  int drawTideEventList(Graphics paramGraphics, int paramInt, Vector paramVector, boolean paramBoolean)
  {
    if (this.graphWidth <= 4)
    {
      int i = this.gstartx + this.scalex / 96;
      long l1 = this.chartCal.getTime().getTime() / 1000L;
      long l2 = l1 + (this.endx - this.startx) * 3600;
      boolean bool = (this.graphWidth == 1) && (!paramBoolean);
      for (int j = 0; j < paramVector.size(); j++)
      {
        if (paramInt > this.scaley)
        {
          paramInt = ShowText(paramGraphics, i, paramInt, " ... ", true);
          break;
        }
        String str = this.main.tideComp.formatDataString(this.theDoc, j, paramVector, false, bool, "", paramBoolean, false, true, "");
        if (str.length() > 0) {
          paramInt = ShowText(paramGraphics, i, paramInt, str, true);
        }
      }
    }
    return paramInt;
  }
  
  void drawGraph(Graphics2D paramGraphics2D, boolean paramBoolean)
  {
    if ((this.main.configValues.sunText) || (this.main.configValues.sunGraphic))
    {
      long l1 = this.main.tideComp.setDT(this.chartCal.getTime().getTime() / 1000L, this.theDoc);
      CompSunData(l1, 0);
      CompSunData(l1, 1);
    }
    if (this.main.configValues.sunGraphic) {
      DrawSunLines(paramGraphics2D);
    }
    makeGrid(paramGraphics2D, false);
    drawTideCurve(paramGraphics2D, paramBoolean);
    int i = this.gstarty + this.scaley / 64;
    if ((this.main.configValues.timeLine) && (!paramBoolean)) {
      drawCurrent(paramGraphics2D);
    }
    if (this.main.configValues.siteLabel) {
      i = DrawSitePosText(paramGraphics2D, i);
    }
    if (this.main.configValues.sunText)
    {
      i = DrawSunData(paramGraphics2D, i, "Sunrise", "Sunset", 0);
      i = DrawSunData(paramGraphics2D, i, "Twilight Begins", "Ends", 1);
    }
    if (this.main.configValues.tideList)
    {
      long l2 = this.chartCal.getTime().getTime() / 1000L;
      long l3 = l2 + (this.endx - this.startx) * 3600;
      long l4 = this.main.tideComp.getNextEventTime(this.theDoc.siteSet, l2, false);
      long l5 = this.main.tideComp.getNextEventTime(this.theDoc.siteSet, l3, true);
      Vector localVector = this.main.tideComp.predictTideEvents(this.theDoc.siteSet, l4, l5, l2, l3, null, null);
      i = drawTideEventList(paramGraphics2D, i, localVector, paramBoolean);
    }
    paramBoolean = false;
  }
  
  void calendarTideList(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, Date paramDate, boolean paramBoolean)
  {
    long l1 = paramDate.getTime() / 1000L;
    long l2 = l1 + 86400L;
    long l3 = this.main.tideComp.getNextEventTime(this.theDoc.siteSet, l1, false);
    long l4 = this.main.tideComp.getNextEventTime(this.theDoc.siteSet, l2, true);
    Vector localVector = this.main.tideComp.predictTideEvents(this.theDoc.siteSet, l3, l4, l1, l2, null, null);
    Font localFont1 = paramGraphics.getFont();
    Font localFont2 = new Font(localFont1.getName(), localFont1.getStyle(), localFont1.getSize() * 4 / 5);
    paramGraphics.setFont(localFont2);
    FontMetrics localFontMetrics = paramGraphics.getFontMetrics();
    Rectangle localRectangle = localFontMetrics.getStringBounds("XXX", paramGraphics).getBounds();
    int i = localVector.size();
    i = i < 5 ? 5 : i;
    int j = (int)(localFont2.getSize() * (paramInt3 - paramInt2) / (localRectangle.height * i));
    if (localFont1.getSize() < j) {
      j = localFont1.getSize();
    }
    paramGraphics.setFont(new Font(localFont2.getName(), localFont2.getStyle(), j));
    for (int k = 0; k < localVector.size(); k++)
    {
      String str = this.main.tideComp.formatDataString(this.theDoc, k, localVector, false, true, "", paramBoolean, false, true, "");
      if (str.length() != 0)
      {
        if (paramInt2 >= paramInt3) {
          break;
        }
        if (paramInt2 + localRectangle.height / 2 > paramInt3) {
          str = str + "...";
        }
        paramInt2 = ShowText(paramGraphics, paramInt1, paramInt2, str, false) - 1;
      }
    }
    paramGraphics.setFont(localFont1);
  }
  
  int colorStrToInt(String paramString)
  {
    paramString = paramString.replaceAll("#", "");
    return Integer.parseInt(paramString, 16);
  }
  
  void DrawCalendar(Graphics paramGraphics, Rectangle paramRectangle, boolean paramBoolean)
  {
    int i = 128;
    int j = paramRectangle.x + paramRectangle.width / i;
    int k = paramRectangle.width + paramRectangle.x - paramRectangle.width / i;
    int i1 = (k - j) / i;
    int i2 = (k - j) / 7;
    GregorianCalendar localGregorianCalendar1 = new GregorianCalendar();
    GregorianCalendar localGregorianCalendar2 = new GregorianCalendar(localGregorianCalendar1.get(1), localGregorianCalendar1.get(2), localGregorianCalendar1.get(5), 0, 0, 0);
    GregorianCalendar localGregorianCalendar3 = new GregorianCalendar(this.chartCal.get(1), this.chartCal.get(2), 1, 0, 0, 0);
    int i3 = localGregorianCalendar3.get(2);
    int i4 = localGregorianCalendar3.get(7) - 1;
    GregorianCalendar localGregorianCalendar4 = (GregorianCalendar)localGregorianCalendar3.clone();
    int i5 = localGregorianCalendar4.getActualMaximum(5);
    int i6 = (i4 + i5 - 1) / 7 + 1;
    String str1 = TideConstants.monthNames[i3] + " " + this.chartCal.get(1);
    StringBuffer localStringBuffer = new StringBuffer();
    if (this.main.tideComp.isDST(localGregorianCalendar4)) {
      localStringBuffer.append(this.theDoc.siteSet.name + " (GMT" + plusSign((int)this.theDoc.siteSet.tz + 1) + ") (Daylight Time)");
    } else {
      localStringBuffer.append(this.theDoc.siteSet.name + " (GMT" + plusSign((int)this.theDoc.siteSet.tz) + ")");
    }
    localStringBuffer.append(", " + str1);
    localStringBuffer.append(". Units: " + this.main.tideComp.getUnitsTag(this.theDoc.siteSet));
    String str2 = localStringBuffer.toString();
    int i7 = (j + k) / 2;
    int i8 = paramRectangle.y;
    int n = paramRectangle.height - (paramRectangle.height - paramRectangle.y) / i;
    Font localFont = paramGraphics.getFont();
    paramGraphics.setFont(new Font(localFont.getName(), 1, localFont.getSize()));
    FontMetrics localFontMetrics = paramGraphics.getFontMetrics();
    Rectangle localRectangle = new Rectangle();
    localRectangle = localFontMetrics.getStringBounds(str2, paramGraphics).getBounds();
    paramGraphics.setColor(this.textColor);
    paramGraphics.drawString(str2, i7 - localRectangle.width / 2, i8 + localRectangle.height - localFontMetrics.getDescent());
    paramGraphics.setFont(localFont);
    i8 += localRectangle.height + 2;
    int m = i8;
    n = paramRectangle.height - (paramRectangle.height - paramRectangle.y) / i;
    int i9 = colorStrToInt(this.main.configValues.htmlBgColor);
    Color localColor1 = new Color(i9);
    paramGraphics.setColor(localColor1);
    paramGraphics.fillRect(j, m, i2 * 7 + 1, localRectangle.height + 2);
    int i10 = colorStrToInt(this.main.configValues.htmlTitleColor);
    Color localColor2 = new Color(i10);
    for (int i11 = 0; i11 < 7; i11++)
    {
      i12 = j + i11 * i2;
      i13 = i12 + i2 / 2;
      paramGraphics.setColor(localColor2);
      paramGraphics.fillRect(i12 + 1, m + 1, i2 - 1, localRectangle.height + 1);
      str2 = TideConstants.dowNames[i11];
      localRectangle = localFontMetrics.getStringBounds(str2, paramGraphics).getBounds();
      paramGraphics.setColor(this.textColor);
      paramGraphics.drawString(str2, i13 - localRectangle.width / 2, i8 + localRectangle.height - localFontMetrics.getDescent());
    }
    i8 += localRectangle.height + 2;
    m = i8;
    i11 = (n - m) / i;
    int i12 = (n - m) / i6;
    paramGraphics.setColor(localColor1);
    paramGraphics.fillRect(j, m, i2 * 7 + 1, i12 * i6 + 1);
    int i13 = this.theDoc.siteSet.current ? 14 : 11;
    while (localGregorianCalendar3.get(2) == i3)
    {
      int i14 = localGregorianCalendar3.get(5);
      int i15 = (i14 + i4 - 1) % 7;
      int i16 = (i14 + i4 - 1) / 7;
      i15 = j + i15 * i2;
      i16 = m + i16 * i12;
      int i17 = colorStrToInt(this.main.configValues.htmlCellColor);
      paramGraphics.setColor(new Color(i17));
      paramGraphics.fillRect(i15, i16, i2, i12);
      paramGraphics.setColor(localColor1);
      paramGraphics.drawRect(i15, i16, i2, i12);
      i15 += i1;
      i16 += i11;
      int i18 = i16 - i11 + i12;
      if ((!paramBoolean) && (localGregorianCalendar3.getTime().compareTo(localGregorianCalendar2.getTime()) == 0)) {
        str2 = "*** " + i14 + " ***";
      } else {
        str2 = "- " + i14 + " -";
      }
      localRectangle = localFontMetrics.getStringBounds(str2, paramGraphics).getBounds();
      paramGraphics.setColor(this.textColor);
      paramGraphics.drawString(str2, i15 - i1 + (i2 - localRectangle.width) / 2, i16 + localRectangle.height - localFontMetrics.getDescent());
      i16 += localRectangle.height;
      calendarTideList(paramGraphics, i15, i16, i18, localGregorianCalendar3.getTime(), paramBoolean);
      localGregorianCalendar3.add(5, 1);
    }
  }
  
  int ChooseColor(double paramDouble)
  {
    int i = 255;
    int j = 255;
    int k = 255;
    if ((paramDouble >= -1.0D) && (paramDouble < 2.0D))
    {
      ColorMat[] arrayOfColorMat = { new ColorMat(-1.0D, 65, 45, 14), new ColorMat(0.0D, 65, 45, 14), new ColorMat(0.25D, 104, 104, 32), new ColorMat(0.5D, 0, 120, 96), new ColorMat(0.75D, 64, 128, 255), new ColorMat(1.0D, 255, 255, 255), new ColorMat(2.0D, 255, 255, 255) };
      int m = 0;
      i = 255;
      j = 255;
      k = 255;
      int n;
      do
      {
        double d1 = arrayOfColorMat[m].v;
        double d2 = arrayOfColorMat[(m + 1)].v;
        n = (paramDouble >= d1) && (paramDouble < d2) ? 1 : 0;
        if (n != 0)
        {
          double d4 = (paramDouble - d1) / (d2 - d1);
          double d3 = 1.0D - d4;
          i = (int)(arrayOfColorMat[m].r * d3 + arrayOfColorMat[(m + 1)].r * d4);
          j = (int)(arrayOfColorMat[m].g * d3 + arrayOfColorMat[(m + 1)].g * d4);
          k = (int)(arrayOfColorMat[m].b * d3 + arrayOfColorMat[(m + 1)].b * d4);
        }
        else
        {
          m++;
        }
      } while ((n == 0) && (m < arrayOfColorMat.length));
    }
    return i << 16 | j << 8 | k;
  }
  
  void DrawMatDate(Graphics paramGraphics, long paramLong, int paramInt1, int paramInt2, int paramInt3)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar();
    localGregorianCalendar.setTime(new Date(paramLong * 1000L));
    String str = this.main.tideComp.padChar(localGregorianCalendar.get(2) + 1, 2, "0") + "/" + this.main.tideComp.padChar(localGregorianCalendar.get(5), 2, "0");
    FontMetrics localFontMetrics = paramGraphics.getFontMetrics();
    Rectangle localRectangle = localFontMetrics.getStringBounds(str, paramGraphics).getBounds();
    int i = this.gstartx - localRectangle.width - this.scalex / 96;
    paramGraphics.setColor(this.textColor);
    paramGraphics.drawString(str, i, paramInt1 + localRectangle.height / 4);
  }
  
  void DrawColorMat(Graphics paramGraphics, int paramInt1, int paramInt2)
  {
    double d1 = this.graphWidth;
    double d2 = 48.0D;
    int i = 0;
    SmoothRect localSmoothRect = null;
    long l1 = this.chartCal.getTime().getTime() / 1000L;
    Rectangle localRectangle = new Rectangle();
    double d3 = this.theDoc.siteSet.mHiWater - this.theDoc.siteSet.mLoWater;
    int j = (this.oldMatx != paramInt1) || (this.oldMaty != paramInt2) || (this.oldSmoothGraph != this.smoothGraph) || (this.oldMatUnits != this.theDoc.siteSet.currentDisplayUnits) || (this.oldMatBase != l1) || (this.oldMatHeight != d1) || (!this.oldMatName.equals(this.theDoc.siteSet.fullName)) || (this.tideMat == null) || ((this.smoothGraph) && (this.offScreenImage == null)) ? 1 : 0;
    long l2;
    int m;
    if (j != 0)
    {
      this.oldMatx = paramInt1;
      this.oldMaty = paramInt2;
      this.oldMatBase = l1;
      this.oldMatHeight = ((int)d1);
      this.oldMatName = this.theDoc.siteSet.fullName;
      this.oldMatUnits = this.theDoc.siteSet.currentDisplayUnits;
      this.oldSmoothGraph = this.smoothGraph;
      if (this.smoothGraph)
      {
        localSmoothRect = new SmoothRect();
        i = 1;
        this.offScreenImage = new BufferedImage(this.scalex, this.scaley, 1);
      }
      this.tideMat = new TideDatum[(int)(d1 + i)][(int)(d2 + i)];
      for (k = 0; k < d1 + i; k++)
      {
        l2 = l1 + k * 86400;
        long l3 = (86400.0D / d2);
        for (int n = 0; n < d2 + i; n++)
        {
          double d4 = this.main.tideComp.timeToTide(this.theDoc.siteSet, l2, true);
          d4 = this.main.tideComp.ConvertHeight(this.theDoc.siteSet, d4);
          if (d3 != 0.0D) {
            d4 = (d4 - this.theDoc.siteSet.mLoWater) / d3;
          }
          this.tideMat[k][n] = new TideDatum(ChooseColor(d4), d4);
          l2 += l3;
        }
      }
      if (this.smoothGraph) {
        for (k = 0; k <= d1; k++)
        {
          localRectangle.y = ((int)(k / d1 * this.scaley));
          localRectangle.height = ((int)((k + 1) / d1 * this.scaley) - localRectangle.y);
          if (k < d1) {
            for (m = 0; m < d2; m++)
            {
              localRectangle.x = ((int)(m / d2 * this.scalex));
              localRectangle.width = ((int)((m + 1) / d2 * this.scalex) - localRectangle.x);
              localSmoothRect.drawRect(this.offScreenImage, localRectangle, k, m, this.tideMat);
            }
          }
        }
      }
    }
    for (int k = 0; k <= d1; k++)
    {
      localRectangle.y = ((int)(k / d1 * this.scaley));
      localRectangle.height = ((int)((k + 1) / d1 * this.scaley) - localRectangle.y);
      l2 = l1 + k * 86400;
      if (k % 3 == 0) {
        DrawMatDate(paramGraphics, l2, localRectangle.y + this.gstarty, paramInt1, paramInt2);
      }
      if ((!this.smoothGraph) && (k < d1)) {
        for (m = 0; m < d2; m++)
        {
          localRectangle.x = ((int)(m / d2 * this.scalex));
          localRectangle.width = ((int)((m + 1) / d2 * this.scalex) - localRectangle.x);
          paramGraphics.setColor(new Color(this.tideMat[k][m].color));
          paramGraphics.fillRect(localRectangle.x + this.gstartx, localRectangle.y + this.gstarty, localRectangle.width, localRectangle.height);
        }
      }
    }
    if (this.smoothGraph)
    {
      Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
      localGraphics2D.drawImage(this.offScreenImage, this.gstartx, this.gstarty, this);
    }
    makeGrid(paramGraphics, true);
    paramGraphics.setColor(Color.black);
    paramGraphics.drawRect(this.gstartx, this.gstarty, this.scalex, this.scaley);
    DrawMouseMatPos(paramGraphics, d1, d2, paramInt1, paramInt2);
  }
  
  void DrawMouseMatPos(Graphics paramGraphics, double paramDouble1, double paramDouble2, int paramInt1, int paramInt2)
  {
    if (this.mouseDown)
    {
      Rectangle localRectangle1 = paramGraphics.getClipBounds();
      double d1 = this.mouseX;
      d1 = d1 < this.gstartx ? this.gstartx : d1;
      d1 = d1 > this.gstartx + this.scalex ? this.gstartx + this.scalex : d1;
      double d2 = (d1 - this.gstartx) / this.scalex;
      double d3 = this.mouseY;
      d3 = d3 < this.gstarty ? this.gstarty : d3;
      d3 = d3 > this.gstarty + this.scaley ? this.gstarty + this.scaley : d3;
      double d4 = (d3 - this.gstarty) / this.scaley;
      long l1 = this.chartCal.getTime().getTime() / 1000L;
      long l2 = (d4 * paramDouble1);
      l2 *= 86400L;
      long l3 = (d2 * 86400.0D);
      l1 += l2 + l3;
      GregorianCalendar localGregorianCalendar = new GregorianCalendar();
      TimeZone localTimeZone = TimeZone.getDefault();
      localTimeZone.setRawOffset(0);
      localGregorianCalendar.setTimeZone(localTimeZone);
      localGregorianCalendar.setTimeInMillis(l1 * 1000L + this.localOffset);
      double d5 = this.main.tideComp.timeToTide(this.theDoc.siteSet, l1, true);
      double d6 = this.main.tideComp.ConvertHeight(this.theDoc.siteSet, d5);
      String str2 = this.main.tideComp.getUnitsTag(this.theDoc.siteSet);
      String str1 = this.main.tideComp.formatDate(localGregorianCalendar, false, false, true, false, "") + " " + this.main.tideComp.formatDouble(d6, 1, true) + " " + str2;
      this.main.updateStatusBar(str1);
      String str3 = this.main.tideComp.formatDate(localGregorianCalendar, false, false, false, false, "") + " " + this.main.tideComp.formatDouble(d6, 1, true) + " " + str2.charAt(0);
      String str4 = createTimeLabel();
      FontMetrics localFontMetrics = paramGraphics.getFontMetrics();
      Rectangle localRectangle2 = localFontMetrics.getStringBounds(str3, paramGraphics).getBounds();
      Rectangle localRectangle3 = localFontMetrics.getStringBounds(str4, paramGraphics).getBounds();
      int i = localRectangle2.width + 8;
      int j = localRectangle3.width + 8;
      int k = j > i ? j : i;
      int m = (i - j) / 2;
      m = m < 0 ? 0 : m;
      int n = localRectangle2.height * 2 + 4;
      int i1 = (int)(d1 - k / 2);
      d3 -= n + paramInt2 / 96;
      d3 = d3 < 2.0D ? 2.0D : d3;
      int i3 = (int)d3 + n * 2 / 3;
      int i2 = (int)(d3 - 2.0D);
      i1 = i1 < 0 ? 0 : i1;
      i1 = i1 + k > localRectangle1.width ? localRectangle1.width - k : i1;
      paramGraphics.setColor(Color.white);
      paramGraphics.fillRect(i1, i2, k, n);
      paramGraphics.setColor(Color.red);
      paramGraphics.drawRect(i1, i2, k, n);
      paramGraphics.setColor(this.textColor);
      paramGraphics.drawString(str3, i1 + 4, i3 + 4 - localRectangle2.height);
      paramGraphics.drawString(str4, i1 + 4 + m, i3 + 4);
    }
  }
  
  public void keyHandler(KeyEvent paramKeyEvent)
  {
    int i = 1;
    int j = paramKeyEvent.getKeyCode();
    if (j == 39) {
      getChartCalendar().add(5, 1);
    } else if (j == 37) {
      getChartCalendar().add(5, -1);
    } else if (j == 40) {
      getChartCalendar().add(2, 1);
    } else if (j == 38) {
      getChartCalendar().add(2, -1);
    } else if (j == 33) {
      getChartCalendar().add(1, -1);
    } else if (j == 34) {
      getChartCalendar().add(1, 1);
    } else if (j == 36) {
      setChartCalendar();
    } else {
      i = 0;
    }
    if (i != 0)
    {
      paramKeyEvent.consume();
      this.theDoc.newDisplay();
    }
  }
  
  private void initComponents()
  {
    this.popupMenu = new JPopupMenu();
    this.SiteExplorerButton = new JMenuItem();
    this.NearestSitesButton = new JMenuItem();
    this.ConfigureDataButton = new JMenuItem();
    this.PrintButton = new JMenuItem();
    this.SiteData = new JMenuItem();
    this.closeMenuItem = new JMenuItem();
    this.HelpButton = new JMenuItem();
    this.SiteExplorerButton.setText("Site Explorer");
    this.SiteExplorerButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        TidePanel.this.SiteExplorerButtonActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.popupMenu.add(this.SiteExplorerButton);
    this.NearestSitesButton.setText("Nearest Sites");
    this.NearestSitesButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        TidePanel.this.NearestSitesButtonActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.popupMenu.add(this.NearestSitesButton);
    this.ConfigureDataButton.setText("Configuration/Data");
    this.ConfigureDataButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        TidePanel.this.ConfigureDataButtonActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.popupMenu.add(this.ConfigureDataButton);
    this.PrintButton.setText("Print Chart");
    this.PrintButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        TidePanel.this.PrintButtonActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.popupMenu.add(this.PrintButton);
    this.SiteData.setText("Site Data");
    this.SiteData.setToolTipText("Information about this site");
    this.SiteData.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        TidePanel.this.SiteDataActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.popupMenu.add(this.SiteData);
    this.closeMenuItem.setText("Close");
    this.closeMenuItem.setToolTipText("Close this site");
    this.closeMenuItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        TidePanel.this.closeMenuItemActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.popupMenu.add(this.closeMenuItem);
    this.HelpButton.setText("Help");
    this.HelpButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        TidePanel.this.HelpButtonActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.popupMenu.add(this.HelpButton);
    addMouseListener(new MouseAdapter()
    {
      public void mousePressed(MouseEvent paramAnonymousMouseEvent)
      {
        TidePanel.this.formMousePressed(paramAnonymousMouseEvent);
      }
      
      public void mouseReleased(MouseEvent paramAnonymousMouseEvent)
      {
        TidePanel.this.formMouseReleased(paramAnonymousMouseEvent);
      }
    });
    addMouseMotionListener(new MouseMotionAdapter()
    {
      public void mouseDragged(MouseEvent paramAnonymousMouseEvent)
      {
        TidePanel.this.formMouseDragged(paramAnonymousMouseEvent);
      }
    });
    addFocusListener(new FocusAdapter()
    {
      public void focusLost(FocusEvent paramAnonymousFocusEvent)
      {
        TidePanel.this.formFocusLost(paramAnonymousFocusEvent);
      }
    });
    addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        TidePanel.this.formKeyPressed(paramAnonymousKeyEvent);
      }
    });
    setLayout(new BorderLayout());
  }
  
  private void formFocusLost(FocusEvent paramFocusEvent) {}
  
  private void closeMenuItemActionPerformed(ActionEvent paramActionEvent)
  {
    this.main.tabbedPaneManager.close();
  }
  
  private void formKeyPressed(KeyEvent paramKeyEvent)
  {
    this.theDoc.handleKey(paramKeyEvent);
  }
  
  private void HelpButtonActionPerformed(ActionEvent paramActionEvent)
  {
    this.main.openFile("Help Browser", true);
  }
  
  private void ConfigureDataButtonActionPerformed(ActionEvent paramActionEvent)
  {
    this.main.launchConfiguration();
  }
  
  private void PrintButtonActionPerformed(ActionEvent paramActionEvent)
  {
    doPrint();
  }
  
  private void SiteExplorerButtonActionPerformed(ActionEvent paramActionEvent)
  {
    this.main.openFile("Site Explorer", true);
  }
  
  private void NearestSitesButtonActionPerformed(ActionEvent paramActionEvent)
  {
    TidesFindNearest localTidesFindNearest = new TidesFindNearest(this.main, false);
  }
  
  private void SiteDataActionPerformed(ActionEvent paramActionEvent)
  {
    new SiteDataDialog(this.theDoc);
  }
  
  public void moveTime(int paramInt1, int paramInt2)
  {
    if (paramInt2 == 0) {
      setChartCalendar();
    } else {
      getChartCalendar().add(paramInt1, paramInt2);
    }
    this.theDoc.newDisplay();
  }
  
  private void formMouseDragged(MouseEvent paramMouseEvent)
  {
    formMousePressed(paramMouseEvent);
  }
  
  private void formMouseReleased(MouseEvent paramMouseEvent)
  {
    if (!testPopup(paramMouseEvent))
    {
      this.mouseDown = false;
      if (isVisible()) {
        repaint();
      }
      this.main.updateStatusBar("Done (F1 = Help)");
    }
    requestFocusInWindow();
  }
  
  private void formMousePressed(MouseEvent paramMouseEvent)
  {
    if ((!testPopup(paramMouseEvent)) && (SwingUtilities.isLeftMouseButton(paramMouseEvent)))
    {
      this.mouseDown = true;
      this.mouseX = paramMouseEvent.getX();
      this.mouseY = paramMouseEvent.getY();
      if (isVisible()) {
        repaint();
      }
    }
    requestFocusInWindow();
  }
  
  private boolean testPopup(MouseEvent paramMouseEvent)
  {
    if ((this.popupMenu.isPopupTrigger(paramMouseEvent)) && (isVisible()))
    {
      this.popupMenu.show(this, paramMouseEvent.getX(), paramMouseEvent.getY());
      return true;
    }
    return false;
  }
  
  final class ColorMat
  {
    public double v;
    public int r;
    public int g;
    public int b;
    
    ColorMat(double paramDouble, int paramInt1, int paramInt2, int paramInt3)
    {
      this.v = paramDouble;
      this.r = paramInt1;
      this.g = paramInt2;
      this.b = paramInt3;
    }
    
    public int getColor()
    {
      return this.r << 16 | this.g << 8 | this.b;
    }
  }
  
  final class GraphicsData
  {
    Graphics2D g;
    double x;
    double y;
    int ox;
    int oy;
    boolean start;
    
    GraphicsData(Graphics2D paramGraphics2D)
    {
      this.g = paramGraphics2D;
    }
  }
}


/* Location:              C:\Users\02221224710\Downloads\JTides.jar!\jtides\TidePanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */