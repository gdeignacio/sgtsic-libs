package es.caib.sgtsic.util.bitel;


import java.util.*;


public class LocaleBean
{
  private Map taulaMestre;


  public LocaleBean()
  {
    taulaMestre = new HashMap(4);
  }


  private boolean loadLanguage(String s)
  {
    boolean flag = false;

    try
    {
      java.io.InputStream inputstream = Thread.currentThread().getContextClassLoader().getResourceAsStream("locale." + s);
      Properties properties = new Properties();

      properties.load(inputstream);
      taulaMestre.put(s, properties);
      //System.out.println("LocaleBean: S'ha carregat el locale '" + s + "'");
      flag = true;
    }
    catch (Exception exception)
    {
      System.out.println("LocaleBean: Excepcio " + exception.getMessage());
    }

    return flag;
  }


  public String get(String s, String s1)
  {
    if (taulaMestre.containsKey(s) || loadLanguage(s))
    {
      Properties properties = (Properties) taulaMestre.get(s);

      return properties.getProperty(s1, "&lt;" + s + "|" + s1 + "&gt;");
    }
    else
    {
      return s + " no soportat";
    }
  }
}
