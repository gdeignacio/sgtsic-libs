package es.caib.sgtsic.error.bitel;


/**
 * La clase ErrorControlador representa un error producido en las clases de control.
 * 
 * @author Miguel Angel Nieto
 * @author Paco Ros
 * @date 05/01/2007
 * @version 2.0
 */
public abstract class ErrorControlador extends BitError
{
  
  /**
   * Constructor de la clase ErrorControlador.
   * @param id    C�digo del error.
   * @param error    Excepci�n/error asociado al error.
   */
  protected ErrorControlador(String id, Throwable error) {
    super(id, error);
  }
  
}
