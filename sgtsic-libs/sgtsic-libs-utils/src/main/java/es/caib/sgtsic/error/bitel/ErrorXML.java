package es.caib.sgtsic.error.bitel;


/**
 * La clase ErrorXML representa un error producido en la obtenci�n o escritura de datos en formato XML.
 * 
 * @author Miguel Angel Nieto
 * @author Paco Ros
 * @date 05/01/2007
 * @version 2.0
 */
public abstract class ErrorXML extends BitError
{
  
  /**
   * Constructor de la clase ErrorXML.
   * @param id    C�digo del error.
   * @param error    Excepci�n/error asociado al error.
   */
  protected ErrorXML(String id, Throwable error) {
    super(id, error);
  }
  
}
