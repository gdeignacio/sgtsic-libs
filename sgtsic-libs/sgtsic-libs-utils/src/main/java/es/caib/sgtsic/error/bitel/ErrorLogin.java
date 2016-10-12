package es.caib.sgtsic.error.bitel;

import java.util.HashMap;


/**
 * La clase ErrorLogin representa un error producido al registrar un log de operaci�n en el sistema.
 * 
 * @author  Miguel Angel Nieto
 * @author  Paco Ros
 * @version 1.0, 16/07/2008
 */
public class ErrorLogin extends BitError
{
  
  /**
   * Constructor de la clase ErrorLogin.
   * 
   */
  public ErrorLogin()
  {
    // Creamos el error con el c�digo del error y la excepci�n asociada
    super("BIT_LOGIN");
    
    // Definimos la descripci�n del error
    HashMap descripcion = new HashMap();
    descripcion.put(BitError.IDIOMA_ESPANYOL, "Disculpe, no es posible encontrar sus datos en la aplicaci�n. Por favor, contacte con un administrador.");
    descripcion.put(BitError.IDIOMA_CATALAN , "Disculpi, no �s possible trobar les seves dades a l'aplicaci�. Per favor, contacti amb un administrador.");
    this.setDescripcion(descripcion);
    
    // Definimos el debug del error
    this.setDebug((String)descripcion.get(BitError.IDIOMA_ESPANYOL));
  }
  
}
