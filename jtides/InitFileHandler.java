package jtides;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.Properties;

public final class InitFileHandler
{
  Properties prop = new Properties();
  String path;
  
  public InitFileHandler(String paramString)
  {
    this.path = paramString;
  }
  
  public void write(ConfigValues paramConfigValues)
  {
    try
    {
      File localFile = new File(this.path);
      BufferedWriter localBufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(localFile)));
      Class localClass = paramConfigValues.getClass();
      Field[] arrayOfField = localClass.getFields();
      String str1 = "";
      String str2 = "";
      String str3 = "";
      for (int i = 0; i < arrayOfField.length; i++)
      {
        str1 = arrayOfField[i].getName();
        str2 = arrayOfField[i].getType().toString();
        if (str2.equals("int")) {
          str3 = "" + arrayOfField[i].getInt(paramConfigValues);
        } else if (str2.equals("long")) {
          str3 = "" + arrayOfField[i].getLong(paramConfigValues);
        } else if (str2.equals("double")) {
          str3 = "" + arrayOfField[i].getDouble(paramConfigValues);
        } else if (str2.equals("boolean")) {
          str3 = "" + arrayOfField[i].getBoolean(paramConfigValues);
        } else if (str2.indexOf("String") != -1) {
          str3 = arrayOfField[i].get(paramConfigValues).toString();
        }
        localBufferedWriter.write(str1 + "=" + str3 + TideConstants.SYSTEM_EOL);
      }
      localBufferedWriter.close();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  public void read(ConfigValues paramConfigValues)
  {
    try
    {
      File localFile = new File(this.path);
      if (localFile != null)
      {
        BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(localFile)));
        Class localClass = paramConfigValues.getClass();
        String str1 = "";
        String str2 = "";
        String str3 = "";
        String str4;
        while ((str4 = localBufferedReader.readLine()) != null)
        {
          int i = str4.indexOf("=");
          if (i != -1)
          {
            str1 = str4.substring(0, i);
            str3 = str4.substring(i + 1).trim();
            Field localField = localClass.getField(str1);
            str2 = localField.getType().toString();
            if (str2.equals("int")) {
              localField.setInt(paramConfigValues, Integer.parseInt(str3));
            } else if (str2.equals("long")) {
              localField.setLong(paramConfigValues, Long.parseLong(str3));
            } else if (str2.equals("double")) {
              localField.setDouble(paramConfigValues, Double.parseDouble(str3));
            } else if (str2.equals("boolean")) {
              localField.setBoolean(paramConfigValues, str3.equals("true"));
            } else if (str2.indexOf("String") != -1) {
              localField.set(paramConfigValues, str3);
            }
          }
        }
        localBufferedReader.close();
      }
    }
    catch (Exception localException) {}
  }
}


/* Location:              C:\Users\02221224710\Downloads\JTides.jar!\jtides\InitFileHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */