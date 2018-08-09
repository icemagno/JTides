package jtides;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public final class TideConfiguration
  extends JDialog
{
  ConfigValues configValues;
  TidesDatabase dataBase;
  HTMLDatabase htmlBase;
  JTides main;
  Timer timer;
  boolean allowWrites = false;
  private JRadioButton AMPMButton;
  private JCheckBox BackgroundCheckBox;
  private JCheckBox BoldFontCheckBox;
  private JButton CloseButton;
  private JCheckBox DarkColorCheckBox;
  private JPanel DataOptions;
  private JComboBox DaylightHandling;
  private JPanel DispayOptions;
  private JRadioButton FeetButton;
  private JCheckBox GridCheckBox;
  private JCheckBox GridNumbersCheckBox;
  private JCheckBox LabelCheckBox;
  private JComboBox LookAndFeel;
  private JRadioButton MetersButton;
  private JRadioButton MilitaryTimeButton;
  private JCheckBox RiseSetDataCheckbox;
  private JCheckBox SunTwilightCheckBox;
  private JCheckBox ThickLineCheckBox;
  private JCheckBox TideCurrentListCheckBox;
  private JCheckBox TimeTideCheckBox;
  private JComboBox TimeZoneBox;
  private JPanel htmlPanel;
  private JLabel jLabel1;
  private JLabel jLabel2;
  private JLabel jLabel3;
  private JLabel jLabel4;
  private JLabel jLabel5;
  private JLabel jLabel6;
  private JPanel jPanel9;
  private JTabbedPane jTabbedPane1;
  
  public TideConfiguration(JTides paramJTides, boolean paramBoolean)
  {
    super(paramJTides, paramBoolean);
    setDefaultCloseOperation(0);
    this.main = paramJTides;
    this.configValues = this.main.configValues;
    initComponents();
    this.dataBase = new TidesDatabase(this.main);
    this.DataOptions.add(this.dataBase, "Center");
    this.htmlBase = new HTMLDatabase(this.main);
    this.htmlPanel.add(this.htmlBase, "Center");
    setupComponentData();
    setupComponentValues();
    pack();
    setVisible(true);
    this.allowWrites = true;
  }
  
  private void setupComponentData()
  {
    setTitle("JTides 5.3" + " Configuration/Database Dialog");
    this.jTabbedPane1.setTitleAt(0, "Configuration");
    this.jTabbedPane1.setTitleAt(1, "Text Database");
    this.jTabbedPane1.setTitleAt(2, "HTML Pages");
    String[] arrayOfString = { "Always use standard time", "Compute daylight time", "Always use daylight time" };
    for (int i = 0; i < arrayOfString.length; i++) {
      this.DaylightHandling.addItem(arrayOfString[i]);
    }
    this.TimeZoneBox.addItem("Use System Time Zone");
    this.TimeZoneBox.addItem("Use Site   Time Zone");
    for (i = -12; i <= 12; i++)
    {
      String str = "GMT";
      if (i >= 0) {
        str = str + "+";
      }
      str = str + i;
      this.TimeZoneBox.addItem(str);
    }
    UIManager.LookAndFeelInfo[] arrayOfLookAndFeelInfo = UIManager.getInstalledLookAndFeels();
    for (int j = 0; j < arrayOfLookAndFeelInfo.length; j++) {
      this.LookAndFeel.addItem(arrayOfLookAndFeelInfo[j].getName());
    }
    ButtonGroup localButtonGroup1 = new ButtonGroup();
    localButtonGroup1.add(this.MetersButton);
    localButtonGroup1.add(this.FeetButton);
    ButtonGroup localButtonGroup2 = new ButtonGroup();
    localButtonGroup2.add(this.AMPMButton);
    localButtonGroup2.add(this.MilitaryTimeButton);
  }
  
  private void setupComponentValues()
  {
    setupComponent(this.AMPMButton, this.main.configValues.ampmFlag);
    setupComponent(this.MilitaryTimeButton, !this.main.configValues.ampmFlag);
    setupComponent(this.MetersButton, this.main.configValues.displayUnits != 1);
    setupComponent(this.FeetButton, this.main.configValues.displayUnits == 1);
    this.DaylightHandling.setSelectedIndex(this.main.configValues.daylightTime);
    this.TimeZoneBox.setSelectedIndex((int)(this.main.configValues.timeZone + 14.0D));
    if (this.main.configValues.LookAndFeel == -1)
    {
      for (int i = 0; (i < this.LookAndFeel.getComponentCount()) && (this.LookAndFeel.getItemAt(i).toString().indexOf("Metal") == -1); i++) {}
      if (i < this.LookAndFeel.getComponentCount()) {
        this.LookAndFeel.setSelectedIndex(i);
      }
    }
    else
    {
      this.LookAndFeel.setSelectedIndex(this.main.configValues.LookAndFeel);
    }
    setupComponent(this.TimeTideCheckBox, this.main.configValues.timeLine);
    setupComponent(this.GridCheckBox, this.main.configValues.chartGrid);
    setupComponent(this.GridNumbersCheckBox, this.main.configValues.gridNums);
    setupComponent(this.BackgroundCheckBox, this.main.configValues.listBackground);
    setupComponent(this.SunTwilightCheckBox, this.main.configValues.sunGraphic);
    setupComponent(this.DarkColorCheckBox, this.main.configValues.sunGraphicDark);
    setupComponent(this.LabelCheckBox, this.main.configValues.siteLabel);
    setupComponent(this.RiseSetDataCheckbox, this.main.configValues.sunText);
    setupComponent(this.TideCurrentListCheckBox, this.main.configValues.tideList);
    setupComponent(this.ThickLineCheckBox, this.main.configValues.thickLine);
    setupComponent(this.BoldFontCheckBox, this.main.configValues.boldFont);
  }
  
  private void setupComponent(JRadioButton paramJRadioButton, boolean paramBoolean)
  {
    paramJRadioButton.setSelected(paramBoolean);
    paramJRadioButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        TideConfiguration.this.dataActionPerformed(paramAnonymousActionEvent);
      }
    });
  }
  
  private void setupComponent(JCheckBox paramJCheckBox, boolean paramBoolean)
  {
    paramJCheckBox.setSelected(paramBoolean);
    paramJCheckBox.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        TideConfiguration.this.dataActionPerformed(paramAnonymousActionEvent);
      }
    });
  }
  
  private void changeTimeZone()
  {
    int i = this.TimeZoneBox.getSelectedIndex();
    this.main.configValues.timeZone = (i - 14);
  }
  
  private void dataActionPerformed(ActionEvent paramActionEvent)
  {
    if (this.allowWrites)
    {
      Object localObject = paramActionEvent.getSource();
      if (localObject == this.AMPMButton) {
        this.main.configValues.ampmFlag = true;
      } else if (localObject == this.MilitaryTimeButton) {
        this.main.configValues.ampmFlag = false;
      } else if (localObject == this.FeetButton) {
        this.main.configValues.displayUnits = 1;
      } else if (localObject == this.MetersButton) {
        this.main.configValues.displayUnits = 0;
      } else if (localObject == this.TimeTideCheckBox) {
        this.main.configValues.timeLine = ((JCheckBox)localObject).isSelected();
      } else if (localObject == this.GridCheckBox) {
        this.main.configValues.chartGrid = ((JCheckBox)localObject).isSelected();
      } else if (localObject == this.GridNumbersCheckBox) {
        this.main.configValues.gridNums = ((JCheckBox)localObject).isSelected();
      } else if (localObject == this.BackgroundCheckBox) {
        this.main.configValues.listBackground = ((JCheckBox)localObject).isSelected();
      } else if (localObject == this.SunTwilightCheckBox) {
        this.main.configValues.sunGraphic = ((JCheckBox)localObject).isSelected();
      } else if (localObject == this.DarkColorCheckBox) {
        this.main.configValues.sunGraphicDark = ((JCheckBox)localObject).isSelected();
      } else if (localObject == this.LabelCheckBox) {
        this.main.configValues.siteLabel = ((JCheckBox)localObject).isSelected();
      } else if (localObject == this.RiseSetDataCheckbox) {
        this.main.configValues.sunText = ((JCheckBox)localObject).isSelected();
      } else if (localObject == this.TideCurrentListCheckBox) {
        this.main.configValues.tideList = ((JCheckBox)localObject).isSelected();
      } else if (localObject == this.ThickLineCheckBox) {
        this.main.configValues.thickLine = ((JCheckBox)localObject).isSelected();
      } else if (localObject == this.BoldFontCheckBox) {
        this.main.configValues.boldFont = ((JCheckBox)localObject).isSelected();
      }
      this.main.repaint();
    }
  }
  
  private void initComponents()
  {
    this.jPanel9 = new JPanel();
    this.CloseButton = new MyJButton();
    this.jTabbedPane1 = new JTabbedPane();
    this.DispayOptions = new JPanel();
    this.jLabel1 = new JLabel();
    this.AMPMButton = new JRadioButton();
    this.MilitaryTimeButton = new JRadioButton();
    this.jLabel2 = new JLabel();
    this.DaylightHandling = new JComboBox();
    this.jLabel3 = new JLabel();
    this.MetersButton = new JRadioButton();
    this.FeetButton = new JRadioButton();
    this.TimeTideCheckBox = new JCheckBox();
    this.GridCheckBox = new JCheckBox();
    this.GridNumbersCheckBox = new JCheckBox();
    this.BackgroundCheckBox = new JCheckBox();
    this.SunTwilightCheckBox = new JCheckBox();
    this.LabelCheckBox = new JCheckBox();
    this.RiseSetDataCheckbox = new JCheckBox();
    this.TideCurrentListCheckBox = new JCheckBox();
    this.jLabel5 = new JLabel();
    this.jLabel4 = new JLabel();
    this.LookAndFeel = new JComboBox();
    this.DarkColorCheckBox = new JCheckBox();
    this.ThickLineCheckBox = new JCheckBox();
    this.BoldFontCheckBox = new JCheckBox();
    this.jLabel6 = new JLabel();
    this.TimeZoneBox = new JComboBox();
    this.DataOptions = new JPanel();
    this.htmlPanel = new JPanel();
    setTitle("");
    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent paramAnonymousWindowEvent)
      {
        TideConfiguration.this.closeDialog(paramAnonymousWindowEvent);
      }
    });
    this.CloseButton.setText("Close Options Dialog");
    this.CloseButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        TideConfiguration.this.OKButtonActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.jPanel9.add(this.CloseButton);
    getContentPane().add(this.jPanel9, "South");
    this.DispayOptions.setToolTipText("Chosen options take effect immediately");
    this.DispayOptions.setName("");
    this.DispayOptions.setLayout(new GridBagLayout());
    this.jLabel1.setText("Display Units");
    GridBagConstraints localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 3;
    localGridBagConstraints.fill = 1;
    localGridBagConstraints.insets = new Insets(2, 4, 2, 2);
    this.DispayOptions.add(this.jLabel1, localGridBagConstraints);
    this.AMPMButton.setText("AM/PM");
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 1;
    localGridBagConstraints.gridy = 1;
    localGridBagConstraints.fill = 1;
    localGridBagConstraints.insets = new Insets(2, 2, 2, 2);
    this.DispayOptions.add(this.AMPMButton, localGridBagConstraints);
    this.MilitaryTimeButton.setText("24 Hour");
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 2;
    localGridBagConstraints.gridy = 1;
    localGridBagConstraints.fill = 1;
    localGridBagConstraints.insets = new Insets(2, 2, 2, 2);
    this.DispayOptions.add(this.MilitaryTimeButton, localGridBagConstraints);
    this.jLabel2.setText("Display Time");
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 1;
    localGridBagConstraints.fill = 1;
    localGridBagConstraints.insets = new Insets(2, 4, 2, 2);
    this.DispayOptions.add(this.jLabel2, localGridBagConstraints);
    this.DaylightHandling.addItemListener(new ItemListener()
    {
      public void itemStateChanged(ItemEvent paramAnonymousItemEvent)
      {
        TideConfiguration.this.DaylightHandlingItemStateChanged(paramAnonymousItemEvent);
      }
    });
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 1;
    localGridBagConstraints.gridy = 2;
    localGridBagConstraints.gridwidth = 2;
    localGridBagConstraints.fill = 1;
    localGridBagConstraints.insets = new Insets(2, 2, 2, 2);
    this.DispayOptions.add(this.DaylightHandling, localGridBagConstraints);
    this.jLabel3.setText("Daylight Time Handling");
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 2;
    localGridBagConstraints.fill = 1;
    localGridBagConstraints.insets = new Insets(2, 4, 2, 2);
    this.DispayOptions.add(this.jLabel3, localGridBagConstraints);
    this.MetersButton.setText("Meters");
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 2;
    localGridBagConstraints.gridy = 3;
    localGridBagConstraints.fill = 1;
    localGridBagConstraints.insets = new Insets(2, 2, 2, 2);
    this.DispayOptions.add(this.MetersButton, localGridBagConstraints);
    this.FeetButton.setText("Feet");
    this.FeetButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        TideConfiguration.this.jRadioButton4ActionPerformed(paramAnonymousActionEvent);
      }
    });
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 1;
    localGridBagConstraints.gridy = 3;
    localGridBagConstraints.fill = 1;
    localGridBagConstraints.insets = new Insets(2, 2, 2, 2);
    this.DispayOptions.add(this.FeetButton, localGridBagConstraints);
    this.TimeTideCheckBox.setText("Current Time/Tide Line");
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 1;
    localGridBagConstraints.gridy = 5;
    localGridBagConstraints.gridwidth = 2;
    localGridBagConstraints.fill = 1;
    localGridBagConstraints.insets = new Insets(2, 2, 2, 2);
    this.DispayOptions.add(this.TimeTideCheckBox, localGridBagConstraints);
    this.GridCheckBox.setText("Overlay Time/Height Grid");
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 1;
    localGridBagConstraints.gridy = 6;
    localGridBagConstraints.gridwidth = 2;
    localGridBagConstraints.fill = 1;
    this.DispayOptions.add(this.GridCheckBox, localGridBagConstraints);
    this.GridNumbersCheckBox.setText("Grid Numbers");
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 1;
    localGridBagConstraints.gridy = 7;
    localGridBagConstraints.gridwidth = 2;
    localGridBagConstraints.fill = 1;
    this.DispayOptions.add(this.GridNumbersCheckBox, localGridBagConstraints);
    this.BackgroundCheckBox.setText("White Background for Data ");
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 1;
    localGridBagConstraints.gridy = 8;
    localGridBagConstraints.gridwidth = 2;
    localGridBagConstraints.fill = 1;
    this.DispayOptions.add(this.BackgroundCheckBox, localGridBagConstraints);
    this.SunTwilightCheckBox.setText("Sun/Twilight Color Fill");
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 5;
    localGridBagConstraints.fill = 1;
    localGridBagConstraints.insets = new Insets(0, 4, 0, 0);
    this.DispayOptions.add(this.SunTwilightCheckBox, localGridBagConstraints);
    this.LabelCheckBox.setText("Site Data ");
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 6;
    localGridBagConstraints.fill = 1;
    localGridBagConstraints.insets = new Insets(0, 4, 0, 0);
    this.DispayOptions.add(this.LabelCheckBox, localGridBagConstraints);
    this.RiseSetDataCheckbox.setText("Sunrise/set Data");
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 7;
    localGridBagConstraints.fill = 1;
    localGridBagConstraints.insets = new Insets(0, 4, 0, 0);
    this.DispayOptions.add(this.RiseSetDataCheckbox, localGridBagConstraints);
    this.TideCurrentListCheckBox.setText("Tide/Current Event List");
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 8;
    localGridBagConstraints.fill = 1;
    localGridBagConstraints.insets = new Insets(0, 4, 0, 0);
    this.DispayOptions.add(this.TideCurrentListCheckBox, localGridBagConstraints);
    this.jLabel5.setText("Other Options");
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 4;
    localGridBagConstraints.fill = 1;
    localGridBagConstraints.insets = new Insets(2, 4, 2, 2);
    this.DispayOptions.add(this.jLabel5, localGridBagConstraints);
    this.jLabel4.setText("Application Look & Feel");
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 11;
    localGridBagConstraints.fill = 1;
    localGridBagConstraints.weightx = 1.0D;
    localGridBagConstraints.insets = new Insets(2, 4, 2, 2);
    this.DispayOptions.add(this.jLabel4, localGridBagConstraints);
    this.LookAndFeel.addItemListener(new ItemListener()
    {
      public void itemStateChanged(ItemEvent paramAnonymousItemEvent)
      {
        TideConfiguration.this.LookAndFeelItemStateChanged(paramAnonymousItemEvent);
      }
    });
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 1;
    localGridBagConstraints.gridy = 11;
    localGridBagConstraints.gridwidth = 2;
    localGridBagConstraints.fill = 1;
    localGridBagConstraints.weightx = 1.0D;
    localGridBagConstraints.insets = new Insets(2, 2, 2, 2);
    this.DispayOptions.add(this.LookAndFeel, localGridBagConstraints);
    this.DarkColorCheckBox.setText("Darker Sun Data Colors");
    this.DarkColorCheckBox.setToolTipText("Choose color shade for sun data");
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 1;
    localGridBagConstraints.gridy = 9;
    localGridBagConstraints.gridwidth = 2;
    localGridBagConstraints.fill = 1;
    this.DispayOptions.add(this.DarkColorCheckBox, localGridBagConstraints);
    this.ThickLineCheckBox.setText("Thick Graphic Line");
    this.ThickLineCheckBox.setToolTipText("Thich tide/current curve line");
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 9;
    localGridBagConstraints.fill = 1;
    localGridBagConstraints.insets = new Insets(0, 4, 0, 0);
    this.DispayOptions.add(this.ThickLineCheckBox, localGridBagConstraints);
    this.BoldFontCheckBox.setText("Bold Chart Text");
    this.BoldFontCheckBox.setToolTipText("For more contrast in display and printing");
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 10;
    localGridBagConstraints.fill = 1;
    localGridBagConstraints.insets = new Insets(0, 4, 0, 0);
    this.DispayOptions.add(this.BoldFontCheckBox, localGridBagConstraints);
    this.jLabel6.setText("Time Zone");
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.fill = 1;
    localGridBagConstraints.insets = new Insets(2, 4, 2, 2);
    this.DispayOptions.add(this.jLabel6, localGridBagConstraints);
    this.TimeZoneBox.setMaximumRowCount(14);
    this.TimeZoneBox.addItemListener(new ItemListener()
    {
      public void itemStateChanged(ItemEvent paramAnonymousItemEvent)
      {
        TideConfiguration.this.TimeZoneBoxItemStateChanged(paramAnonymousItemEvent);
      }
    });
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridwidth = 2;
    localGridBagConstraints.fill = 1;
    localGridBagConstraints.insets = new Insets(2, 2, 2, 2);
    this.DispayOptions.add(this.TimeZoneBox, localGridBagConstraints);
    this.jTabbedPane1.addTab("jPanel4", null, this.DispayOptions, "");
    this.DataOptions.setLayout(new BorderLayout());
    this.jTabbedPane1.addTab("jPanel6", null, this.DataOptions, "");
    this.htmlPanel.setLayout(new BorderLayout());
    this.jTabbedPane1.addTab("tab3", this.htmlPanel);
    getContentPane().add(this.jTabbedPane1, "Center");
  }
  
  private void TimeZoneBoxItemStateChanged(ItemEvent paramItemEvent)
  {
    if (this.allowWrites)
    {
      changeTimeZone();
      this.main.repaint();
    }
  }
  
  private void LookAndFeelItemStateChanged(ItemEvent paramItemEvent)
  {
    if (this.allowWrites) {
      this.main.tideComp.setupLookAndFeel(this.LookAndFeel.getSelectedIndex());
    }
  }
  
  private void OKButtonActionPerformed(ActionEvent paramActionEvent)
  {
    testCloseDialog();
  }
  
  private void DaylightHandlingItemStateChanged(ItemEvent paramItemEvent)
  {
    if (this.allowWrites)
    {
      this.main.configValues.daylightTime = this.DaylightHandling.getSelectedIndex();
      this.main.repaint();
    }
  }
  
  private void jRadioButton4ActionPerformed(ActionEvent paramActionEvent) {}
  
  private void closeDialog(WindowEvent paramWindowEvent)
  {
    testCloseDialog();
  }
  
  private void testCloseDialog()
  {
    if (!this.dataBase.writingDatabase)
    {
      setVisible(false);
      dispose();
    }
    else
    {
      Toolkit.getDefaultToolkit().beep();
      JOptionPane.showMessageDialog(null, "Cannot close this dialog while\nwriting database. You must\ncancel the operation first.", "Cannot Close Dialog", 0);
      this.jTabbedPane1.setSelectedIndex(1);
    }
  }
}


/* Location:              C:\Users\02221224710\Downloads\JTides.jar!\jtides\TideConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */