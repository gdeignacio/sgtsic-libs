package es.caib.sgtsic.error.bitel;


/**
 * La clase ErrorEJB representa un error producido en el acceso o ejecuci�n de un Enterprise Java Bean.
 * 
 * @author Miguel Angel Nieto
 * @author Paco Ros
 * @date 05/01/2007
 * @version 2.0
 */
public abstract class ErrorEJB extends BitError
{
  
  /**
   * Constructor de la clase ErrorEJB.
   * @param id    C�digo del error.
   * @param error    Excepci�n/error asociado al error.
   */
  protected ErrorEJB(String id, Throwable error) {
    super(id, error);
  }
  
}
