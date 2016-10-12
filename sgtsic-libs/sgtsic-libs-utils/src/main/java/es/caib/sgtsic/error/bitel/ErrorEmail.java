package es.caib.sgtsic.error.bitel;


/**
 * La clase ErrorEmail representa un error producido en la recepci�n, env�o o proceso de correos electr�nicos.
 * 
 * @author Miguel Angel Nieto
 * @date 09/02/2007
 * @version 2.0
 */
public abstract class ErrorEmail extends BitError
{
  
  /**
   * Constructor de la clase ErrorEmail.
   * @param id    C�digo del error.
   * @param error    Excepci�n/error asociado al error.
   */
  protected ErrorEmail(String id, Throwable error) {
    super(id, error);
  }
  
}
