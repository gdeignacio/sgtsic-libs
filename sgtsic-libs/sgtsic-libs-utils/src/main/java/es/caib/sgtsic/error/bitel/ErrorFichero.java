package es.caib.sgtsic.error.bitel;


/**
 * La clase ErrorEJB representa un error producido en el acceso a ficheros.
 * 
 * @author Miguel Angel Nieto
 * @author Paco Ros
 * @date 05/01/2007
 * @version 2.0
 */
public abstract class ErrorFichero extends BitError
{
  
  /**
   * Constructor de la clase ErrorFichero.
   * @param id    C�digo del error.
   * @param error    Excepci�n/error asociado al error.
   */
  protected ErrorFichero(String id, Throwable error) {
    super(id, error);
  }
  
}
