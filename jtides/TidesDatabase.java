package jtides;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;

public final class TidesDatabase
  extends JPanel
{
  JTides main;
  TidesDoc theDoc;
  GregorianCalendar startTime = null;
  GregorianCalendar endTime = null;
  boolean useInterval = false;
  boolean includeSun = false;
  boolean writingDatabase = false;
  long timeInterval = 1L;
  Thread workThread = null;
  ThreadStopper threadStopper;
  BoxData[] timeSpec = { new BoxData("1 Second", 1L), new BoxData("2 Seconds", 2L), new BoxData("5 Seconds", 5L), new BoxData("10 Seconds", 10L), new BoxData("30 Seconds", 30L), new BoxData("1 Minute", 60L), new BoxData("2 Minutes", 120L), new BoxData("5 Minutes", 300L), new BoxData("10 Minutes", 600L), new BoxData("15 Minutes", 900L), new BoxData("30 Minutes", 1800L), new BoxData("1 Hour", 3600L), new BoxData("2 Hours", 7200L), new BoxData("4 Hours", 14400L), new BoxData("8 Hours", 28800L), new BoxData("1 Day", 86400L), new BoxData("2 Days", 172800L), new BoxData("4 Days", 345600L), new BoxData("7 Days", 604800L), new BoxData("10 Days", 864000L) };
  private JButton CancelButton;
  private JButton CreateButton;
  private JComboBox EndDayBox;
  private JComboBox EndMonthBox;
  private JComboBox EndYearBox;
  private JCheckBox IncludeSunDataButton;
  private JComboBox IntervalBox;
  private JComboBox StartDayBox;
  private JComboBox StartMonthBox;
  private JComboBox StartYearBox;
  private JLabel StatusBar;
  private JRadioButton TideHighsLowsButton;
  private JRadioButton TideIntervalsButton;
  private JPanel controlPanel;
  private JPanel dataTypePanel;
  private JComboBox delimComboBox;
  private JLabel jLabel1;
  private JLabel jLabel2;
  private JLabel jLabel3;
  private JLabel jLabel4;
  private JLabel jLabel5;
  private JLabel jLabel6;
  private JLabel jLabel7;
  private JLabel jLabel8;
  private JProgressBar jProgressBar1;
  private JPanel timeIntervalPanel;
  
  public TidesDatabase(JTides paramJTides)
  {
    this.main = paramJTides;
    initComponents();
    this.threadStopper = new ThreadStopper();
    ButtonGroup localButtonGroup = new ButtonGroup();
    localButtonGroup.add(this.TideHighsLowsButton);
    localButtonGroup.add(this.TideIntervalsButton);
    this.theDoc = getCurrentDoc();
    initFields();
    presetFields();
  }
  
  public TidesDoc getCurrentDoc()
  {
    return this.main.tabbedPaneManager.getSelectedDoc();
  }
  
  private void initFields()
  {
    this.delimComboBox.addItem("Tab");
    this.delimComboBox.addItem("Comma");
    for (int i = 0; i < TideConstants.monthNames.length; i++)
    {
      this.StartMonthBox.addItem(TideConstants.monthNames[i]);
      this.EndMonthBox.addItem(TideConstants.monthNames[i]);
    }
    for (i = 1; i <= 31; i++)
    {
      this.StartDayBox.addItem("" + i);
      this.EndDayBox.addItem("" + i);
    }
    if ((this.theDoc != null) && (this.theDoc.type == 0)) {
      for (i = 0; i < this.theDoc.siteSet.equMax; i++)
      {
        this.StartYearBox.addItem("" + (this.theDoc.siteSet.startYear + i));
        this.EndYearBox.addItem("" + (this.theDoc.siteSet.startYear + i));
      }
    }
  }
  
  private void presetFields()
  {
    if ((this.theDoc != null) && (this.theDoc.type == 0))
    {
      GregorianCalendar localGregorianCalendar = new GregorianCalendar();
      localGregorianCalendar.setTime(this.theDoc.panel.getCurrentCal().getTime());
      this.StartMonthBox.setSelectedIndex(localGregorianCalendar.get(2));
      this.StartDayBox.setSelectedIndex(localGregorianCalendar.get(5) - 1);
      this.StartYearBox.setSelectedIndex(localGregorianCalendar.get(1) - this.theDoc.siteSet.startYear);
      localGregorianCalendar.add(2, 1);
      this.EndMonthBox.setSelectedIndex(localGregorianCalendar.get(2));
      this.EndDayBox.setSelectedIndex(localGregorianCalendar.get(5) - 1);
      this.EndYearBox.setSelectedIndex(localGregorianCalendar.get(1) - this.theDoc.siteSet.startYear);
    }
    for (int i = 0; i < this.timeSpec.length; i++) {
      this.IntervalBox.addItem(this.timeSpec[i].name);
    }
    this.IntervalBox.setSelectedIndex(10);
    this.TideHighsLowsButton.setSelected(true);
    this.IncludeSunDataButton.setSelected(false);
  }
  
  private void createDatabase()
  {
    String str1 = this.delimComboBox.getSelectedItem().equals("Tab") ? "\t" : ",";
    this.theDoc = getCurrentDoc();
    if ((this.theDoc != null) && (this.theDoc.type == 0))
    {
      this.startTime = new GregorianCalendar(this.StartYearBox.getSelectedIndex() + this.theDoc.siteSet.startYear, this.StartMonthBox.getSelectedIndex(), this.StartDayBox.getSelectedIndex() + 1);
      this.endTime = new GregorianCalendar(this.EndYearBox.getSelectedIndex() + this.theDoc.siteSet.startYear, this.EndMonthBox.getSelectedIndex(), this.EndDayBox.getSelectedIndex() + 1);
      this.endTime.add(6, 1);
      this.useInterval = this.TideIntervalsButton.isSelected();
      this.timeInterval = this.timeSpec[this.IntervalBox.getSelectedIndex()].value;
      this.includeSun = this.IncludeSunDataButton.isSelected();
      JFileChooser localJFileChooser = new JFileChooser();
      ExampleFileFilter localExampleFileFilter = new ExampleFileFilter();
      localExampleFileFilter.addExtension("csv");
      localExampleFileFilter.addExtension("tsv");
      localExampleFileFilter.addExtension("txt");
      localExampleFileFilter.setDescription("CSV database Files");
      localJFileChooser.setFileFilter(localExampleFileFilter);
      localJFileChooser.setDialogTitle("Save Database File");
      if (this.main.configValues.defaultDatabaseDirectory.length() > 0) {
        localJFileChooser.setCurrentDirectory(new File(this.main.configValues.defaultDatabaseDirectory));
      }
      String str2 = this.theDoc.siteSet.name;
      str2 = this.main.tideComp.srchRplc(str2, " ", "_");
      str2 = this.main.tideComp.srchRplc(str2, ",", "_");
      str2 = this.main.tideComp.srchRplc(str2, "__", "_");
      String str3 = str1.equals("\t") ? ".tsv" : ".csv";
      localJFileChooser.setSelectedFile(new File(str2 + str3));
      int i = localJFileChooser.showSaveDialog(this.main);
      if (i == 0)
      {
        try
        {
          this.main.configValues.defaultDatabaseDirectory = localJFileChooser.getCurrentDirectory().getCanonicalPath();
        }
        catch (Exception localException) {}
        File localFile = localJFileChooser.getSelectedFile();
        writeDatabase(this.theDoc, localFile.getPath(), str1);
      }
    }
  }
  
  private void writeDatabase(final TidesDoc paramTidesDoc, final String paramString1, final String paramString2)
  {
    if (!this.writingDatabase)
    {
      this.writingDatabase = true;
      this.threadStopper.stop = false;
      this.workThread = new Thread()
      {
        public void run()
        {
          TidesDatabase.this.writeDatabaseThread(paramTidesDoc, paramString1, paramString2);
        }
      };
      this.workThread.start();
    }
  }
  
  private void writeDatabaseThread(TidesDoc paramTidesDoc, String paramString1, String paramString2)
  {
    try
    {
      DataOutputStream localDataOutputStream = new DataOutputStream(new FileOutputStream(paramString1));
      Vector localVector = new Vector();
      long l1 = this.startTime.getTime().getTime() / 1000L;
      long l2 = this.endTime.getTime().getTime() / 1000L;
      String str1 = paramTidesDoc.siteSet.current ? "Current" : "Tide";
      String str2 = this.main.tideComp.getUnitsTag(paramTidesDoc.siteSet);
      StringBuffer localStringBuffer = new StringBuffer();
      localStringBuffer.append("UTCDate");
      localStringBuffer.append(paramString2);
      localStringBuffer.append("UTCTime");
      localStringBuffer.append(paramString2);
      localStringBuffer.append("Date");
      localStringBuffer.append(paramString2);
      localStringBuffer.append("Time");
      localStringBuffer.append(paramString2);
      localStringBuffer.append(str1);
      if (!this.useInterval)
      {
        localStringBuffer.append(paramString2);
        localStringBuffer.append("Type");
      }
      localStringBuffer.append(paramString2);
      localStringBuffer.append("Units");
      if (this.includeSun)
      {
        localStringBuffer.append(paramString2);
        localStringBuffer.append("TwilightBegin");
        localStringBuffer.append(paramString2);
        localStringBuffer.append("Sunrise");
        localStringBuffer.append(paramString2);
        localStringBuffer.append("Transit");
        localStringBuffer.append(paramString2);
        localStringBuffer.append("Sunset");
        localStringBuffer.append(paramString2);
        localStringBuffer.append("TwilightEnd");
        localStringBuffer.append(paramString2);
        localStringBuffer.append("DaylightTime");
      }
      writeLine(localStringBuffer.toString(), localDataOutputStream);
      startProgressBar(l1, l2, 0L, "Creating Tidal Data");
      long l3;
      if (!this.useInterval)
      {
        l3 = this.main.tideComp.getNextEventTime(paramTidesDoc.siteSet, l1, false);
        long l4 = this.main.tideComp.getNextEventTime(paramTidesDoc.siteSet, l2, true);
        localVector = this.main.tideComp.predictTideEvents(paramTidesDoc.siteSet, l3, l4, l1, l2, this.jProgressBar1, this.threadStopper);
      }
      else
      {
        for (l3 = l1; (l3 <= l2) && (!this.threadStopper.stop); l3 += this.timeInterval)
        {
          updateProgressBar(l3);
          TideEvent localTideEvent = new TideEvent();
          localTideEvent.height = this.main.tideComp.timeToTide(paramTidesDoc.siteSet, l3, true);
          localTideEvent.t = l3;
          localVector.add(localTideEvent);
        }
        stopProgressBar();
      }
      startProgressBar(0L, localVector.size(), 0L, "Formatting and Writing Database");
      for (int i = 0; (i < localVector.size()) && (!this.threadStopper.stop); i++)
      {
        updateProgressBar(i);
        String str3 = this.main.tideComp.formatDataString(paramTidesDoc, i, localVector, true, false, str2, true, this.includeSun, !this.useInterval, paramString2);
        writeLine(str3, localDataOutputStream);
      }
      localDataOutputStream.close();
      stopProgressBar();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    this.writingDatabase = false;
    this.threadStopper.stop = false;
    stopProgressBar();
  }
  
  public void writeLine(String paramString, DataOutputStream paramDataOutputStream)
  {
    try
    {
      paramDataOutputStream.writeBytes(paramString + TideConstants.SYSTEM_EOL);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  public void startProgressBar(long paramLong1, long paramLong2, long paramLong3, String paramString)
  {
    this.jProgressBar1.setMinimum((int)paramLong1);
    this.jProgressBar1.setMaximum((int)paramLong2);
    this.jProgressBar1.setValue((int)paramLong2);
    this.StatusBar.setText(paramString);
  }
  
  public void updateProgressBar(long paramLong)
  {
    this.jProgressBar1.setValue((int)paramLong);
  }
  
  public void stopProgressBar()
  {
    this.jProgressBar1.setValue(this.jProgressBar1.getMinimum());
    this.StatusBar.setText("Done");
  }
  
  private void initComponents()
  {
    this.timeIntervalPanel = new JPanel();
    this.jLabel1 = new JLabel();
    this.jLabel2 = new JLabel();
    this.jLabel3 = new JLabel();
    this.jLabel4 = new JLabel();
    this.jLabel5 = new JLabel();
    this.StartMonthBox = new JComboBox();
    this.StartDayBox = new JComboBox();
    this.StartYearBox = new JComboBox();
    this.jLabel6 = new JLabel();
    this.EndMonthBox = new JComboBox();
    this.EndDayBox = new JComboBox();
    this.EndYearBox = new JComboBox();
    this.jLabel8 = new JLabel();
    this.dataTypePanel = new JPanel();
    this.TideHighsLowsButton = new JRadioButton();
    this.TideIntervalsButton = new JRadioButton();
    this.IntervalBox = new JComboBox();
    this.IncludeSunDataButton = new JCheckBox();
    this.delimComboBox = new JComboBox();
    this.jLabel7 = new JLabel();
    this.controlPanel = new JPanel();
    this.CreateButton = new MyJButton();
    this.CancelButton = new MyJButton();
    this.jProgressBar1 = new JProgressBar();
    this.StatusBar = new JLabel();
    setLayout(new GridBagLayout());
    this.timeIntervalPanel.setBorder(BorderFactory.createTitledBorder("Choose Time Interval"));
    this.timeIntervalPanel.setLayout(new GridLayout(3, 4));
    this.timeIntervalPanel.add(this.jLabel1);
    this.jLabel2.setHorizontalAlignment(0);
    this.jLabel2.setText("Month");
    this.timeIntervalPanel.add(this.jLabel2);
    this.jLabel3.setHorizontalAlignment(0);
    this.jLabel3.setText("Day");
    this.timeIntervalPanel.add(this.jLabel3);
    this.jLabel4.setHorizontalAlignment(0);
    this.jLabel4.setText("Year");
    this.timeIntervalPanel.add(this.jLabel4);
    this.jLabel5.setText("Start");
    this.timeIntervalPanel.add(this.jLabel5);
    this.timeIntervalPanel.add(this.StartMonthBox);
    this.timeIntervalPanel.add(this.StartDayBox);
    this.timeIntervalPanel.add(this.StartYearBox);
    this.jLabel6.setText("End");
    this.timeIntervalPanel.add(this.jLabel6);
    this.timeIntervalPanel.add(this.EndMonthBox);
    this.timeIntervalPanel.add(this.EndDayBox);
    this.timeIntervalPanel.add(this.EndYearBox);
    GridBagConstraints localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 1;
    localGridBagConstraints.fill = 2;
    localGridBagConstraints.anchor = 11;
    localGridBagConstraints.weightx = 1.0D;
    localGridBagConstraints.insets = new Insets(4, 4, 4, 4);
    add(this.timeIntervalPanel, localGridBagConstraints);
    this.jLabel8.setFont(new Font("Dialog", 1, 11));
    this.jLabel8.setHorizontalAlignment(0);
    this.jLabel8.setText("Create Database");
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.fill = 2;
    add(this.jLabel8, localGridBagConstraints);
    this.dataTypePanel.setBorder(BorderFactory.createTitledBorder(null, "Choose Data Type", 1, 0));
    this.dataTypePanel.setLayout(new GridBagLayout());
    this.TideHighsLowsButton.setText("Tide/current maxima/minima/slacks only");
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 0;
    localGridBagConstraints.fill = 1;
    localGridBagConstraints.insets = new Insets(2, 2, 2, 2);
    this.dataTypePanel.add(this.TideHighsLowsButton, localGridBagConstraints);
    this.TideIntervalsButton.setText("Tide/current at intervals of:");
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 1;
    localGridBagConstraints.fill = 1;
    localGridBagConstraints.ipady = 5;
    localGridBagConstraints.insets = new Insets(2, 2, 2, 2);
    this.dataTypePanel.add(this.TideIntervalsButton, localGridBagConstraints);
    this.IntervalBox.addItemListener(new ItemListener()
    {
      public void itemStateChanged(ItemEvent paramAnonymousItemEvent)
      {
        TidesDatabase.this.IntervalBoxItemStateChanged(paramAnonymousItemEvent);
      }
    });
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 1;
    localGridBagConstraints.gridy = 1;
    localGridBagConstraints.fill = 1;
    localGridBagConstraints.anchor = 17;
    localGridBagConstraints.weightx = 1.0D;
    localGridBagConstraints.insets = new Insets(2, 2, 2, 2);
    this.dataTypePanel.add(this.IntervalBox, localGridBagConstraints);
    this.IncludeSunDataButton.setText("Include sun rise/set/twilight data");
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 2;
    localGridBagConstraints.fill = 1;
    localGridBagConstraints.insets = new Insets(2, 2, 2, 2);
    this.dataTypePanel.add(this.IncludeSunDataButton, localGridBagConstraints);
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 1;
    localGridBagConstraints.gridy = 3;
    localGridBagConstraints.fill = 1;
    localGridBagConstraints.anchor = 17;
    localGridBagConstraints.insets = new Insets(2, 2, 2, 2);
    this.dataTypePanel.add(this.delimComboBox, localGridBagConstraints);
    this.jLabel7.setHorizontalAlignment(0);
    this.jLabel7.setText("Choose database delimiter:");
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 3;
    localGridBagConstraints.fill = 1;
    localGridBagConstraints.anchor = 11;
    localGridBagConstraints.insets = new Insets(2, 2, 2, 2);
    this.dataTypePanel.add(this.jLabel7, localGridBagConstraints);
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 2;
    localGridBagConstraints.fill = 2;
    localGridBagConstraints.anchor = 11;
    localGridBagConstraints.weightx = 1.0D;
    localGridBagConstraints.insets = new Insets(4, 4, 4, 4);
    add(this.dataTypePanel, localGridBagConstraints);
    this.controlPanel.setLayout(new GridBagLayout());
    this.CreateButton.setText("Create");
    this.CreateButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        TidesDatabase.this.CreateButtonActionPerformed(paramAnonymousActionEvent);
      }
    });
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.fill = 3;
    localGridBagConstraints.anchor = 17;
    localGridBagConstraints.insets = new Insets(4, 4, 4, 4);
    this.controlPanel.add(this.CreateButton, localGridBagConstraints);
    this.CancelButton.setText("Cancel");
    this.CancelButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        TidesDatabase.this.CancelButtonActionPerformed(paramAnonymousActionEvent);
      }
    });
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.fill = 3;
    localGridBagConstraints.anchor = 17;
    localGridBagConstraints.insets = new Insets(4, 4, 4, 4);
    this.controlPanel.add(this.CancelButton, localGridBagConstraints);
    this.jProgressBar1.setStringPainted(true);
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.fill = 2;
    localGridBagConstraints.weightx = 1.0D;
    localGridBagConstraints.insets = new Insets(4, 4, 4, 4);
    this.controlPanel.add(this.jProgressBar1, localGridBagConstraints);
    this.StatusBar.setText("Done");
    this.StatusBar.setToolTipText("The current operation");
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 2;
    localGridBagConstraints.gridy = 1;
    localGridBagConstraints.fill = 2;
    localGridBagConstraints.insets = new Insets(4, 4, 4, 4);
    this.controlPanel.add(this.StatusBar, localGridBagConstraints);
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 3;
    localGridBagConstraints.fill = 2;
    localGridBagConstraints.anchor = 11;
    localGridBagConstraints.weightx = 1.0D;
    localGridBagConstraints.insets = new Insets(4, 4, 4, 4);
    add(this.controlPanel, localGridBagConstraints);
  }
  
  private void IntervalBoxItemStateChanged(ItemEvent paramItemEvent)
  {
    this.TideIntervalsButton.setSelected(true);
  }
  
  private void CancelButtonActionPerformed(ActionEvent paramActionEvent)
  {
    if (this.workThread != null) {
      this.threadStopper.stop = true;
    }
  }
  
  private void CreateButtonActionPerformed(ActionEvent paramActionEvent)
  {
    createDatabase();
  }
  
  final class BoxData
  {
    String name;
    long value;
    
    BoxData(String paramString, long paramLong)
    {
      this.name = paramString;
      this.value = paramLong;
    }
  }
}


/* Location:              C:\Users\02221224710\Downloads\JTides.jar!\jtides\TidesDatabase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */