package es.caib.sgtsic.error.bitel;

import es.caib.sgtsic.util.bitel.BitCadena;
import es.caib.sgtsic.utils.json.JSONException;
import es.caib.sgtsic.utils.json.JSONObject;
import java.util.HashMap;




/**
 * La clase BitError encapsula la informaci�n y la funcionalidad relacionada con las
 * excepciones y errores que pueden producirse en las aplicaciones de BITel.
 * $Id: BitError.java,v 1.4 2007/02/08 13:02:30 fros Exp $
 * 
 * @author Miguel Angel Nieto
 * @author Paco Ros
 * @date 05/01/2007
 * @version 2.0
 */
public class BitError extends Exception
{
  
  /** Idioma: Espa�ol. */
  public final static String IDIOMA_ESPANYOL = "es";
  
  /** Idioma: Catal�n. */
  public final static String IDIOMA_CATALAN = "ca";
  
  /** Idioma: Ingl�s. */
  public final static String IDIOMA_INGLES  = "en";
  
  /** Idioma: Alem�n. */
  public final static String IDIOMA_ALEMAN  = "de";
  
  
  
  /** Lista de idiomas soportados. */
  protected static String[] arrIdiomas = {BitError.IDIOMA_ESPANYOL, BitError.IDIOMA_CATALAN, BitError.IDIOMA_INGLES, BitError.IDIOMA_ALEMAN};
  
  
  /** C�digo del error. */
  private String id;
  
  /** Descripci�n multiidioma. */
  private HashMap descripcion;
  
  /** Informaci�n de debug. */
  private String debug;
  
  /** Excepci�n o error asociado. */
  private Throwable error;
  
  /** Traza de pila de la excepci�n o error asociado, generada a partir de la excepci�n o error asociado. */
  private String stackTraceAsString;
  
  
  
  /**
   * Constructor de la clase BitError.
   */
  protected BitError() {}
  
  
  /**
   * Constructor de la clase BitError.
   * @param id    C�digo del error.
   * @param error    Excepci�n/error asociado al error.
   */
  protected BitError(String id, Throwable error) {
    this(id, null, null, error);
  }
  
  
  /**
   * Constructor de la clase BitError.
   * @param id    C�digo del error.
   */
  protected BitError(String id) {
    this(id, null, null, null);
  }
  
  /**
   * Constructor de la clase BitError.
   * @param id    C�digo del error.
   * @param descripcion    Descripci�n del error.
   * @param debug    Descripci�n extendedida del error.
   * @param error    Excepci�n/error asociado al error.
   */
  protected BitError(String id, HashMap descripcion, String debug, Throwable error)
  {
    // Almacenamos la informaci�n b�sica del error
    this.id = id;
    this.descripcion = descripcion;
    this.debug = debug;
    this.error = error;
    
    // Si nos indican el error, almacenamos su traza de pila
    if (this.error!=null) {
      StackTraceElement[] ste = this.error.getStackTrace();
      if (ste!=null) {
        StringBuffer sb = new StringBuffer(512);
        for (int i=0; i<ste.length; i++) sb.append(ste[i].toString()).append("\n");
        this.stackTraceAsString = sb.toString();
      }
      else {
        this.stackTraceAsString = BitCadena.CADENA_VACIA;
      }
    }
  }
  
  
  /**
   * Recupera el c�digo del error.
   * @return    C�digo del error.
   */
  public String getId() {
    return this.id;
  }
  
  
  /**
   * Recupera la descripci�n de la excepci�n/error asociado.
   * @return    Descripci�n de la excepci�n/error asociado.
   */
  public HashMap getDescripcion() {
    return this.descripcion;
  }
  
  
  /**
   * Establece la descripci�n de la excepci�n/error asociado.
   * @param descripcion    Descripci�n de la excepci�n/error asociado.
   */
  public void setDescripcion(HashMap descripcion) {
    this.descripcion = descripcion;
  }
  
  
  /**
   * Recupera la informaci�n de debug de la excepci�n/error asociado.
   * @return    Informaci�n de debug de la excepci�n/error asociado.
   */
  public String getDebug() {
    return this.debug;
  }
  
  
  /**
   * Establece la informaci�n de debug de la excepci�n/error asociado.
   * @param debug    Informaci�n de debug de la excepci�n/error asociado.
   */
  public void setDebug(String debug) {
    this.debug = debug;
  }
  
  
  /**
   * Recupera la excepci�n/error asociado.
   * @return    Excepci�n/error asociado.
   */
  public Throwable getError() {
    return this.error;
  }
  
  
  /**
   * Recupera la traza de pila de la excepci�n/error asociado.
   * @return    Traza de pila de la excepci�n/error asociado.
   */
  public String getStackTraceAsString() {
    return this.stackTraceAsString;
  }
  
  
  /**
   * Recupera la descripci�n completa del error.
   * @return    Cadena de texto con la descripci�n completa del error.
   */
  public String toString() {
    return "id: " + this.getId()
    + "\n" + "descripcion: " + this.getDescripcion()
    + "\n" + "debug: " + this.getDebug()
    + "\n" + "stackTraceAsString: " + this.getStackTraceAsString();
  }
  
  
  /**
   * Recupera la informaci�n del objeto en formato JSON.
   * @return    Informaci�n del objeto en formato JSON.
   * @throws JSONException
   */
  public JSONObject getJSONObject() throws JSONException
  {
    JSONObject jsonObj = new JSONObject();
    jsonObj.put("id", this.getId());
    jsonObj.put("stackTraceAsString", this.stackTraceAsString);
    
    for (int i=0; i<arrIdiomas.length; i++) {
      String idioma = arrIdiomas[i];
      jsonObj.put("descripcion" + idioma.toUpperCase(), this.getDescripcion().get(idioma));
    }
    
    return jsonObj;
  }
}
