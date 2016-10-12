package es.caib.sgtsic.util.bitel;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;

import es.caib.sgtsic.error.bitel.ErrorXMLLectura;


public class BitLoggerConfig
{
  private static String ficheroXML = "BitLogger.xml";
  private static boolean isLoaded = false;
  private static String acronimoAplicacion;
  public static HashMap logDelete;
  public static HashMap logInsert;
  public static HashMap logSelect;
  public static HashMap logSelectLista;
  public static HashMap logUpdate;


  public static boolean doLogDelete(String tabla) throws ErrorXMLLectura
  {
    loadConfig();

    return (logDelete.containsKey(tabla));
  }

  public static boolean doLogInsert(String tabla) throws ErrorXMLLectura
  {
    loadConfig();

    return (logInsert.containsKey(tabla));
  }

  public static boolean doLogSelect(String tabla) throws ErrorXMLLectura
  {
    loadConfig();

    return (logSelect.containsKey(tabla));
  }

  public static boolean doLogSelectLista(String tabla) throws ErrorXMLLectura
  {
    loadConfig();

    return (logSelectLista.containsKey(tabla));
  }

  public static boolean doLogUpdate(String tabla) throws ErrorXMLLectura
  {
    loadConfig();

    return (logUpdate.containsKey(tabla));
  }

  private static void loadConfig() throws ErrorXMLLectura
  {
    if (isLoaded)
    {
      return;
    }
    else
    {
      isLoaded = true;
      acronimoAplicacion = "";
      logInsert = new HashMap();
      logUpdate = new HashMap();
      logDelete = new HashMap();
      logSelect = new HashMap();
      logSelectLista = new HashMap();

      try
      {
        ParseaHelper parseaHelper = new ParseaHelper();
        parseaHelper.parsea(ficheroXML);
      }
      catch (Exception e)
      {
        acronimoAplicacion = null;
        logInsert = null;
        logUpdate = null;
        logDelete = null;
        logSelect = null;
        logSelectLista = null;
        
        throw new ErrorXMLLectura(ficheroXML, e);
      }
    }
  }

  public static String getAcronimoAplicacion() throws ErrorXMLLectura
  {
    loadConfig();
    return acronimoAplicacion;
  }

  /**
   *  Parser que obtiene los datos del BitLogger a partir del fichero XML
   */
  public static class ParseaHelper extends DefaultHandler
  {
    String operacionActual;
    String tablaActual;
    XMLReader xmlReader;

    public ParseaHelper() throws Exception
    {
      SAXParserFactory spf = SAXParserFactory.newInstance();

      spf.setValidating(false);
      xmlReader = null;

      try
      {
        SAXParser saxParser = spf.newSAXParser();

        xmlReader = saxParser.getXMLReader();
      }
      catch (Exception e)
      {
        throw new Exception(e.toString());
      }

      xmlReader.setContentHandler(this);
      xmlReader.setErrorHandler(this);
    }

    public void characters(char[] buf, int offset, int len) throws SAXException
    {
      String aux = "";

      aux = new String(buf, offset, len); // Data contendra el contenido del elemento

      if ("true".equals(aux))
      {
        if ("INSERT".equals(operacionActual))
        {
          logInsert.put(tablaActual, "");
        }
        else if ("UPDATE".equals(operacionActual))
        {
          logUpdate.put(tablaActual, "");
        }
        else if ("DELETE".equals(operacionActual))
        {
          logDelete.put(tablaActual, "");
        }
        else if ("SELECT".equals(operacionActual))
        {
          logSelect.put(tablaActual, "");
        }
        else if ("SELECT_LISTA".equals(operacionActual))
        {
          logSelectLista.put(tablaActual, "");
        }
      }
    }

    public void endDocument() throws SAXException
    {
    }

    public void endElement(String namespaceURI, String name, String rawName) throws SAXException
    {
    }

    public void error(SAXParseException spe) throws SAXException
    {
      String message = "Error: " + getParseExceptionInfo(spe);

      throw new SAXException(message);
    }

    public void fatalError(SAXParseException spe) throws SAXException
    {
      String message = "Fatal Error: " + getParseExceptionInfo(spe);

      throw new SAXException(message);
    }

    protected void parsea(String ficheroConf) throws Exception
    {
      InputStream input;

      try
      {
        input = Thread.currentThread().getContextClassLoader().getResourceAsStream(ficheroConf);
      }
      catch (Exception e)
      {
        throw new Exception(e.toString());
      }

      try
      {
        xmlReader.parse(new InputSource(input));
      }
      catch (Exception e)
      {
        throw new Exception(e.toString());
      }
    }

    public void startDocument() throws SAXException
    {
    }

    public void startElement(String namespaceURI, String name, String rawName, Attributes attrs) throws SAXException
    {
      if ("aplicacion".equals(rawName))
      {
        acronimoAplicacion = attrs.getValue("acronimo");
      }
      else if ("tabla".equals(rawName))
      {
        tablaActual = attrs.getValue("nombre");
      }
      else if ("log-insert".equals(rawName))
      {
        operacionActual = "INSERT";
      }
      else if ("log-update".equals(rawName))
      {
        operacionActual = "UPDATE";
      }
      else if ("log-delete".equals(rawName))
      {
        operacionActual = "DELETE";
      }
      else if ("log-select".equals(rawName))
      {
        operacionActual = "SELECT";
      }
      else if ("log-select-lista".equals(rawName))
      {
        operacionActual = "SELECT_LISTA";
      }
    }

    public void warning(SAXParseException spe) throws SAXException
    {
    }

    private String getParseExceptionInfo(SAXParseException spe)
    {
      String systemId = spe.getSystemId();

      if (systemId == null)
      {
        systemId = "null";
      }

      String info = "URI=" + systemId + " Line=" + spe.getLineNumber() + ": " + spe.getMessage();

      return info;
    }
  }
}