package jtides;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Calendar;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class HTMLDatabase
  extends JPanel
{
  boolean writingDatabase = false;
  Thread workThread = null;
  String copynote;
  HTMLPageBuilder pageBuilder;
  String javaScriptBlock = "<script type=\"text/javascript\" language=\"JavaScript\">\n<!-- ;\nmon = new Array(\"January\",\"February\",\"March\",\"April\",\"May\",\n\"June\",\"July\",\"August\",\"September\",\"October\",\"November\",\"December\");\nfunction today(path)\n{\n   path = \"./\" + path + \"/\";\n   date = new Date();\n   yr = date.getYear();\n   if(yr < 50) { // JavaScript isn't Y2K compatible, so this hack is necessary\n      yr += 2000;\n   }\n   else if(yr < 1900) {\n      yr += 1900;\n   }\n   month = date.getMonth();\n   mday = date.getDate();\n   location.href = path + yr + \"/\" + mon[month] + \".html#\" + mday;\n}\n//-->\n</script>\n";
  String todayTag = "<li><a href=\"javascript:today('')\">Today</a></li>";
  JTides main;
  TidesDoc theDoc;
  private JPanel TimePanel;
  private JButton createButton;
  private JComboBox endYearComboBox;
  private JLabel jLabel1;
  private JLabel jLabel2;
  private JScrollPane jScrollPane1;
  private JTextArea messageTextArea;
  private JProgressBar progressBar;
  private JComboBox startYearComboBox;
  private JLabel titleLabel;
  
  public HTMLDatabase(JTides paramJTides)
  {
    this.main = paramJTides;
    this.theDoc = getCurrentDoc();
    initComponents();
    this.pageBuilder = new HTMLPageBuilder(paramJTides);
    this.progressBar.setStringPainted(true);
    this.titleLabel.setText("Create HTML page set for " + this.theDoc.siteSet.name);
    setup();
    this.copynote = ("<p></p><font size=\"-2\">Copyright &copy; 2011, P. Lutus " + this.pageBuilder.wrapTag("http://www.arachnoid.com/JTides", "a", "href=\"http://www.arachnoid.com/JTides\"") + "</font>");
  }
  
  private void setup()
  {
    for (int i = this.theDoc.siteSet.startYear; i < this.theDoc.siteSet.endYear; i++)
    {
      str = "" + i;
      this.startYearComboBox.addItem(str);
      this.endYearComboBox.addItem(str);
    }
    i = Calendar.getInstance().get(1);
    this.startYearComboBox.setSelectedItem("" + i);
    this.endYearComboBox.setSelectedItem("" + i);
    String str = "This dialog will create a set of Web pages for the currently displayed site (" + this.theDoc.siteSet.name + " ). The pages will be placed on your system under the directory \"" + this.main.basePath + "/HTMLPages\".\n\nYou may choose to move the pages from that location onto your Web site or elsewhere. Any number of site data sets may be created this way and they will be indexed for you under this directory.\n\n1. Select the desired site in the main JTides display.\n2. Open this dialog.\n3. Choose the desired years above.\n4. Click \"Create HTML page set\" below.\n\nNote: If you want to change the appearance of the generated Web pages, feel free to edit the values in \"" + this.main.configPath + "\", in particular those values beginning with \"html\".";
    this.messageTextArea.setText(str);
  }
  
  public TidesDoc getCurrentDoc()
  {
    return this.main.tabbedPaneManager.getSelectedDoc();
  }
  
  private void createPages()
  {
    if (!this.writingDatabase)
    {
      this.writingDatabase = true;
      this.createButton.setEnabled(false);
      this.workThread = new Thread()
      {
        public void run()
        {
          HTMLDatabase.this.createPagesThread();
        }
      };
      this.workThread.start();
    }
    else
    {
      Toolkit.getDefaultToolkit().beep();
    }
  }
  
  private void createPagesThread()
  {
    String str1 = this.main.basePath + "/HTMLPages";
    File localFile1 = new File(str1);
    String str2 = this.theDoc.siteSet.name;
    String str3 = str2.replaceAll("[^\\w]", "_");
    File localFile2 = new File(localFile1, str3);
    localFile2.mkdirs();
    int i = Integer.parseInt((String)this.startYearComboBox.getSelectedItem());
    int j = Integer.parseInt((String)this.endYearComboBox.getSelectedItem());
    int k = (j + 1 - i) * 12;
    int m = 0;
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(this.todayTag);
    for (int n = i; n <= j; n++)
    {
      m = createYearPage(localFile2, n, str2 + ", " + n, m, k);
      str5 = this.pageBuilder.wrapTag("" + n, "a", "href=\"" + n + "/index.html\"");
      localStringBuffer.append(this.pageBuilder.wrapTag(str5, "li"));
    }
    String str4 = this.pageBuilder.wrapTag(localStringBuffer.toString(), "ul");
    str4 = this.pageBuilder.wrapTag(str2, "b") + "<p></p>" + str4 + this.copynote;
    str4 = this.pageBuilder.wrapTag(str4, "body");
    String str5 = this.pageBuilder.wrapTag(str2, "title");
    String str6 = str5 + this.pageBuilder.metaTags + this.javaScriptBlock;
    str4 = this.pageBuilder.wrapTag(str6, "head") + str4;
    str4 = this.pageBuilder.wrapTag(str4, "html");
    str4 = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" + "\n\n" + str4;
    File localFile3 = new File(localFile2, "index.html");
    try
    {
      FileWriter localFileWriter = new FileWriter(localFile3);
      localFileWriter.write(str4);
      localFileWriter.close();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    refreshMasterIndex(localFile1);
    this.writingDatabase = false;
    this.createButton.setEnabled(true);
  }
  
  private void refreshMasterIndex(File paramFile)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    File[] arrayOfFile = paramFile.listFiles();
    Arrays.sort(arrayOfFile);
    for (int i = 0; i < arrayOfFile.length; i++) {
      if (arrayOfFile[i].isDirectory())
      {
        localObject = new File(arrayOfFile[i], "index.html");
        str2 = readFile((File)localObject);
        str2 = str2.replaceFirst("([\\w|\\W]+<title>\\s*)([^<|\\n]+)(\\s*</title>[\\w|\\W]+)", "$2");
        str3 = this.pageBuilder.wrapTag(str2, "a", "href=\"" + arrayOfFile[i].getName() + "/index.html\"");
        str3 = str3 + "&nbsp;" + this.pageBuilder.wrapTag("Today", "a", new StringBuilder().append("href=\"javascript:today('").append(arrayOfFile[i].getName()).append("')\"").toString());
        localStringBuffer.append(this.pageBuilder.wrapTag(str3, "li"));
      }
    }
    String str1 = this.pageBuilder.wrapTag(localStringBuffer.toString(), "ul");
    Object localObject = "JTides Tide/Current Data Pages";
    String str2 = this.pageBuilder.wrapTag(str1 + this.copynote, "body");
    String str3 = this.pageBuilder.wrapTag((String)localObject, "title") + this.pageBuilder.metaTags + this.javaScriptBlock;
    str2 = this.pageBuilder.wrapTag(str3, "head") + str2;
    str2 = this.pageBuilder.wrapTag(str2, "html");
    str2 = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" + "\n\n" + str2;
    File localFile = new File(paramFile, "index.html");
    try
    {
      FileWriter localFileWriter = new FileWriter(localFile);
      localFileWriter.write(str2);
      localFileWriter.close();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  public int createYearPage(File paramFile, int paramInt1, String paramString, int paramInt2, int paramInt3)
  {
    File localFile = new File(paramFile, "" + paramInt1);
    localFile.mkdirs();
    StringBuffer localStringBuffer = new StringBuffer();
    for (int i = 0; i < 12; i++)
    {
      updateProgressBar(0, ++paramInt2, paramInt3);
      this.pageBuilder.createMonthPage(this.theDoc, this.copynote, localFile, paramInt1, i, 0, 12);
      str2 = HTMLPageBuilder.monthLongName[i];
      localObject = this.pageBuilder.wrapTag(str2, "a", "href=\"" + HTMLPageBuilder.monthLongName[i] + ".html\"");
      localStringBuffer.append(this.pageBuilder.wrapTag((String)localObject, "li"));
    }
    String str1 = this.pageBuilder.wrapTag(localStringBuffer.toString(), "ul");
    str1 = this.pageBuilder.wrapTag(str1 + this.copynote, "body");
    String str2 = this.pageBuilder.wrapTag(paramString, "title") + this.pageBuilder.metaTags;
    str1 = this.pageBuilder.wrapTag(str2, "head") + str1;
    str1 = this.pageBuilder.wrapTag(str1, "html");
    str1 = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" + "\n\n" + str1;
    Object localObject = new File(localFile, "index.html");
    try
    {
      FileWriter localFileWriter = new FileWriter((File)localObject);
      localFileWriter.write(str1);
      localFileWriter.close();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return paramInt2;
  }
  
  private String readFile(File paramFile)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    try
    {
      BufferedReader localBufferedReader = new BufferedReader(new FileReader(paramFile));
      char[] arrayOfChar = new char['䀀'];
      int i;
      while ((i = localBufferedReader.read(arrayOfChar)) > 0) {
        localStringBuffer.append(arrayOfChar, 0, i);
      }
      localBufferedReader.close();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return localStringBuffer.toString();
  }
  
  private void updateProgressBar(int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt3 > paramInt1)
    {
      this.progressBar.setMinimum(paramInt1);
      this.progressBar.setMaximum(paramInt3);
      this.progressBar.setValue(paramInt2);
    }
  }
  
  private void initComponents()
  {
    this.titleLabel = new JLabel();
    this.TimePanel = new JPanel();
    this.jLabel1 = new JLabel();
    this.startYearComboBox = new JComboBox();
    this.jLabel2 = new JLabel();
    this.endYearComboBox = new JComboBox();
    this.createButton = new MyJButton();
    this.jScrollPane1 = new JScrollPane();
    this.messageTextArea = new JTextArea();
    this.progressBar = new JProgressBar();
    setLayout(new GridBagLayout());
    this.titleLabel.setHorizontalAlignment(0);
    this.titleLabel.setText("***");
    GridBagConstraints localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.fill = 2;
    localGridBagConstraints.anchor = 11;
    localGridBagConstraints.insets = new Insets(2, 2, 2, 2);
    add(this.titleLabel, localGridBagConstraints);
    this.TimePanel.setBorder(BorderFactory.createTitledBorder("Time Interval"));
    this.TimePanel.setLayout(new GridBagLayout());
    this.jLabel1.setText("Start");
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.insets = new Insets(2, 2, 2, 2);
    this.TimePanel.add(this.jLabel1, localGridBagConstraints);
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.fill = 2;
    localGridBagConstraints.weightx = 1.0D;
    localGridBagConstraints.insets = new Insets(2, 2, 2, 2);
    this.TimePanel.add(this.startYearComboBox, localGridBagConstraints);
    this.jLabel2.setText("End");
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.insets = new Insets(2, 2, 2, 2);
    this.TimePanel.add(this.jLabel2, localGridBagConstraints);
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.fill = 2;
    localGridBagConstraints.weightx = 1.0D;
    localGridBagConstraints.insets = new Insets(2, 2, 2, 2);
    this.TimePanel.add(this.endYearComboBox, localGridBagConstraints);
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 1;
    localGridBagConstraints.fill = 2;
    localGridBagConstraints.anchor = 11;
    localGridBagConstraints.weightx = 1.0D;
    localGridBagConstraints.insets = new Insets(2, 2, 2, 2);
    add(this.TimePanel, localGridBagConstraints);
    this.createButton.setText("Create HTML page set");
    this.createButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        HTMLDatabase.this.createButtonActionPerformed(paramAnonymousActionEvent);
      }
    });
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 4;
    localGridBagConstraints.anchor = 15;
    add(this.createButton, localGridBagConstraints);
    this.messageTextArea.setEditable(false);
    this.messageTextArea.setLineWrap(true);
    this.messageTextArea.setRows(16);
    this.messageTextArea.setWrapStyleWord(true);
    this.jScrollPane1.setViewportView(this.messageTextArea);
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 2;
    localGridBagConstraints.fill = 1;
    localGridBagConstraints.weightx = 1.0D;
    localGridBagConstraints.weighty = 1.0D;
    localGridBagConstraints.insets = new Insets(2, 2, 2, 2);
    add(this.jScrollPane1, localGridBagConstraints);
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 3;
    localGridBagConstraints.fill = 2;
    localGridBagConstraints.anchor = 15;
    localGridBagConstraints.weightx = 1.0D;
    localGridBagConstraints.insets = new Insets(2, 2, 2, 2);
    add(this.progressBar, localGridBagConstraints);
  }
  
  private void createButtonActionPerformed(ActionEvent paramActionEvent)
  {
    createPages();
  }
}


/* Location:              C:\Users\02221224710\Downloads\JTides.jar!\jtides\HTMLDatabase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */