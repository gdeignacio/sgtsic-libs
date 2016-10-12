package es.caib.sgtsic.error.bitel;

import java.util.HashMap;


/**
 * La clase ErrorControladorInterno representa un error interno gen�rico producido en las clases de control.
 * 
 * @author Miguel Angel Nieto
 * @author Paco Ros
 * @date 05/01/2007
 * @version 2.0
 */
public class ErrorControladorInterno extends ErrorControlador
{
  
  /**
   * Constructor de la clase BitError.
   * @param error    Excepci�n/error asociado al error.
   */
  public ErrorControladorInterno(Throwable error)
  {
    // Creamos el error con el c�digo del error y la excepci�n asociada
    super("BIT_INTERNO_CONTROLADOR", error);
    
    // Definimos la descripci�n del error
    HashMap descripcion = new HashMap();
    descripcion.put(BitError.IDIOMA_ESPANYOL, "Error interno en el controlador.");
    descripcion.put(BitError.IDIOMA_CATALAN , "Error intern en el controlador.");
    this.setDescripcion(descripcion);
    
    // Definimos el debug del error
    this.setDebug((String)descripcion.get(BitError.IDIOMA_ESPANYOL));
  }
  
}
