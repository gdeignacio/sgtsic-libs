package es.caib.sgtsic.util.bitel;

import es.caib.sgtsic.error.bitel.ErrorXMLLectura;

import java.util.Hashtable;

import java.io.*;

import javax.servlet.ServletConfig;

import javax.xml.parsers.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

/**
 * Clase que carga los fichero BitAction.xml y BitSecurity.xml
 * Contiene una hashtable para cada XML. Esta clase ofrece funcionalidad para que el BitController
 * pueda obtener la informaci�n de acciones y seguridad sobre acciones.
 * Nuevo cambio por esquema de seguridad. El BitController si no encuentra el fichero BitSecurity.xml
 * parsaer� el BitAction.xml esperando encontrar la definici�n de seguridad para las acciones.
 */
public class BitControllerBean
{

  // Path del fichero de propiedades XML donde encontraremos la informaci�n de las acciones.
  private static String ficheroNavegacion = "BitAction.xml";

  // Path del fichero de propiedades XML donde encontraremos la informaci�n de la seguridad.
  private static String ficheroSeguridad = "BitSecurity.xml";

  // Estructura con los datos de las acciones
  private Hashtable BITACTION = null;

  // Estructura con los datos de la seguridad
  private Hashtable BITSECURITY = null;

  // Control para saber si existe el fichero BitSecurity
  private boolean existeBitSecurity = false;
  
  private ServletConfig config;

  /**
 * Funci�n que introduce los datos de las acciones en la estructura si esta esta
 * vacia o si se le indica de forma explicita con el parametro cargar.
 *
 * @param cargar indica si se debe realizar obligatoriamente la carga de la estructura BITACTION
 */
  public void cargarBITACTION(boolean cargar) throws ErrorXMLLectura
  {
    if (cargar || BITACTION == null)
    {

      BITACTION = new Hashtable();
      BITSECURITY = new Hashtable();
      ParseaHelper parseaHelper = new ParseaHelper();
      parseaHelper.parsea(BitControllerBean.ficheroNavegacion);

      /*
        Enumeration e = BITACTION.elements();
        String s;
        while(e.hasMoreElements()){
            BitAction a = (BitAction)e.nextElement();
            System.out.println("Name--> " + a.getName());
            System.out.println("Description--> " + a.getDescription());
            System.out.println("ClassName--> " + a.getClassName());
            System.out.println("Path--> " + a.getPath());
            System.out.println("=================================== ");
            System.out.println(" ");
        }
    */
    }
  }

  /**
 * Funci�n que obtiene los datos de una accion de la estructura BITACTION
 *
 * @param accion nombre de la accion
 */
  public BitAction getAccion(String accion) throws ErrorXMLLectura
  {
    //this.cargarBITACTION(false);
    BitAction ba = (BitAction) BITACTION.get(accion);
    if (ba == null)
    {
      if (BITACTION.isEmpty())
      {
        //BitLog.debug("La Hashtable de BitAction esta vac�a");
        this.cargarBITACTION(true);
        ba = (BitAction) BITACTION.get(accion);
      }
    }
    return ba;
  }

  /**
 * Funci�n que obtiene los datos de una security de la estructura BITACTION
 *
 * @param directorio nombre de la accion
 */
  public BitSecurity getSecurity(String directorio)
  {
    BitSecurity bs = (BitSecurity) BITSECURITY.get(directorio);
    if (bs == null)
    {
      if (BITSECURITY.isEmpty())
      {
        //BitLog.debug("La Hashtable de BitSecurity esta vacia");
        bs = (BitSecurity) BITSECURITY.get(directorio);
      }
    }
    return bs;
  }

  public void setConfig(ServletConfig config)
  {
    this.config = config;
  }

  public ServletConfig getConfig()
  {
    return config;
  }

  /**
 * Parser que obtiene los datos de las acciones a partir del fichero de propiedades XML
 */
  public class ParseaHelper extends DefaultHandler
  {
    XMLReader xmlReader;
    String data = "";
    BitAction accion = null;
    BitSecurity segGroup = null;
    BitSecurity segAction = null;
    boolean inicioSegAction = false; //Variable para controlar cuando estamos parseando el esquema de seguridad para el grupo o para la action

    public ParseaHelper() throws ErrorXMLLectura
    {
      SAXParserFactory spf = SAXParserFactory.newInstance();
      spf.setValidating(false);

      xmlReader = null;
      SAXParser saxParser;
      try
      {        
        saxParser = spf.newSAXParser();
        xmlReader = saxParser.getXMLReader();
      }
      catch (ParserConfigurationException e)
      {
        throw new ErrorXMLLectura(BitControllerBean.ficheroNavegacion,e);
      }      
      catch (SAXException e)
      {
         throw new ErrorXMLLectura(BitControllerBean.ficheroNavegacion,e);
      }

      xmlReader.setContentHandler(this);
      xmlReader.setErrorHandler(this);
    }

    protected void parsea(String ficheroConf) throws ErrorXMLLectura
    {
      InputStream input;
      input = config.getServletContext().getResourceAsStream("/WEB-INF/"+ficheroConf);
      if (input == null)
        BitLog.debug("No puedo encontrar el fichero BitAction.xml");

      try
      {
        xmlReader.parse(new InputSource(input));
      }
      catch (NullPointerException e)
      {
        throw new ErrorXMLLectura(BitControllerBean.ficheroNavegacion,e);        
      }
      catch (SAXException e)
      {
         throw new ErrorXMLLectura(BitControllerBean.ficheroNavegacion,e);
      }
      catch (IOException e)
      {
         throw new ErrorXMLLectura(BitControllerBean.ficheroNavegacion,e);
      }
      catch (Exception e)
      {
        throw new ErrorXMLLectura(BitControllerBean.ficheroNavegacion,e);        
      }

    }

    public void startDocument() throws SAXException
    {
    }

    public void endDocument() throws SAXException
    {
    }

    public void startElement(String namespaceURI, String name, String rawName, Attributes attrs) throws SAXException
    {
      if (rawName.equals("group")) //Detectamos un nuevo grupo
      {
        if (!existeBitSecurity) //Si no existe el XML BitSecurity
        {
          segGroup = new BitSecurity(); //Creamos el esquema de seguridad a nivel de grupo
          segGroup.setName(attrs.getValue(0));
          inicioSegAction = false;
          //BitLog.debug("Detectado el grupo " + segGroup.getName());
        }
      }
      else if (rawName.equals("bitaction")) //Detectamos una nueva action
      {
        if (!existeBitSecurity) //Si no existe el XML BitSecurity
        {
          segAction = new BitSecurity(); //Creamos el esquema de seguridad a nivel de action                    
          inicioSegAction = true;
          //BitLog.debug("Detectada una nueva action");
        }
      }
    }

    public void endElement(String namespaceURI, String name, String rawName) throws SAXException
    {
      if (rawName.equals("group")) //Detectamos un nuevo grupo
      {
        if (!existeBitSecurity) //Si no existe el XML BitSecurity
        {
          segGroup = null;
        }
      }
      else if (rawName.equals("name"))
      {
        // Creamos el elemento y comprobamos si ya estaba en la lista
        accion = new BitAction();
        accion.setName(data);
        //if (BITACTION.get(accion.getName()) != null)
        //  BitLog.error("La acci�n " + accion.getName() + " ya est� definida en la lista y ser� actualizada");
        // Introducimos los datos de la accion en la Hashtable
        BITACTION.put(accion.getName(), accion);

        if (!existeBitSecurity) //Si no existe el XML BitSecurity
        {
          segAction.setName(data); //creamos el bitsecurity con el nombre de la action
          accion.setSecurity(data); //la variable security tambien tendr� el nombre de la action
        }

        data = "";
      }
      // completamos la estructura cliente con cada uno de sus parametros
      else if (rawName.equals("description"))
      {
        accion.setDescription(data);
        data = "";
      }
      else if (rawName.equals("class-name"))
      {
        accion.setClassName(data);
        data = "";
      }
      else if (rawName.equals("operation"))
      {
        accion.setOperation(data);
        data = "";
      }
      else if (rawName.equals("path"))
      {
        accion.setPath(data);
        data = "";
      }
      else if (rawName.equals("pool"))
      {
        accion.setPool(new Boolean(data).booleanValue());
        data = "";
      }
      else if (rawName.equals("bean"))
      {
        accion.setBean(data);
        data = "";
      }
      else if (rawName.equals("validate"))
      {
        accion.setValidate(new Boolean(data).booleanValue());
        data = "";
      }
      else if (rawName.equals("failure"))
      {
        accion.setFailure(data);
        data = "";
      }
      else if (rawName.equals("history"))
      {
        accion.setHistory(new Boolean(data).booleanValue());
        data = "";
      }
      else if (rawName.equals("redirect"))
      {
        accion.setRedirect(new Boolean(data).booleanValue());
        data = "";
      }
      else if (rawName.equals("security"))
      {
        if (!existeBitSecurity) //No existe BitSecurity
        {
          if (inicioSegAction) //Si estamos en la etiqueta de seguridad de action               
          {
            if (segAction.getListaRoles().size() == 0) //la action no tiene definida la seguridad
            {
              if (segGroup.getListaRoles().size() == 0){ //Si la lista de roles del grupo es 0
                //BitLog.debug("La seguridad del grupo de la action " + accion.getName() + " no est� definida. La acci�n quedar� sin seguridad.");
              }
              else
              {
                //BitLog.debug("Copiamos la seguridad del grupo a la action " + accion.getName());
                segAction.setListaRoles(segGroup.getListaRoles()); //copiamos la lista de roles del grupo a la action
              }
            }            
          }
        }
        else
        {
          accion.setSecurity(data);
        }
        data = "";
      }
      else if (rawName.equals("role"))
      {
        if (!existeBitSecurity) //No existe BitSecurity
        {
          if (inicioSegAction) //Si estamos en la etiqueta de seguridad de action
          {
            segAction.addRole(data);
            //BitLog.debug("Definimos el role " + data + " para la action " + segAction.getName());
          }
          else //estamos en un grupo
          {
            segGroup.addRole(data); //A�adimos un role a nivel de grupo 
            //BitLog.debug("Definimos el role " + data + " para el grupo " + segGroup.getName());
          }
        }
        data = "";
      }
      else if (rawName.equals("bitaction"))
      { //A�adimos al BITSECURITY la nueva seguridad
        if (!existeBitSecurity) //No existe BitSecurity
        {
          //if (BITSECURITY.get(segAction.getName()) != null)
            //BitLog.error("La acci�n " + accion.getName() + " ya est� definida en la lista de seguridad y ser� actualizada");
          BITSECURITY.put(segAction.getName(), segAction);
        }
      }
      data = "";
    }


    public void characters(char[] buf, int offset, int len) throws SAXException
    {
      String aux = "";
      aux = new String(buf, offset, len); //data contendra el contenido del elemento
      if (!aux.trim().equals(""))
      {
        if (data.length() > 0)
        {
          data = data + aux;
        }
        else
          data = aux;
      }
    }

    public void warning(SAXParseException spe) throws SAXException
    {
    }

    public void error(SAXParseException spe) throws SAXException
    {
      String message = "Error: " + getParseExceptionInfo(spe);
      BitLog.error(message);
      throw new SAXException(message);
    }

    public void fatalError(SAXParseException spe) throws SAXException
    {
      String message = "Fatal Error: " + getParseExceptionInfo(spe);
      BitLog.error(message);
      throw new SAXException(message);
    }

    private String getParseExceptionInfo(SAXParseException spe)
    {
      String systemId = spe.getSystemId();
      if (systemId == null)
      {
        systemId = "null";
      }
      String info = "URI=" + systemId + " Line=" + spe.getLineNumber() + ": " + spe.getMessage();
      BitLog.error(info);
      return info;
    }
  }

}
