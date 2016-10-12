package es.caib.sgtsic.error.bitel;

import java.util.HashMap;


/**
 * La clase ErrorControladorResponseNoDefinido representa un error interno producido en las clases de control.
 * 
 * @author Miguel Angel Nieto
 * @author Paco Ros
 * @date 05/01/2007
 * @version 2.0
 */
public class ErrorControladorResponseNoDefinido extends ErrorControlador
{
  
  /**
   * Constructor de la clase BitError.
   */
  public ErrorControladorResponseNoDefinido()
  {
    // Creamos el error con el c�digo del error y la excepci�n asociada
    super("BIT_INTERNO_RESPONSE_NO_DEFINIDO", null);
    
    // Definimos la descripci�n del error
    HashMap descripcion = new HashMap();
    descripcion.put(BitError.IDIOMA_ESPANYOL, "Error interno en el controlador: El objeto 'response' no est� definido.");
    descripcion.put(BitError.IDIOMA_CATALAN , "Error intern en el controlador: L'objecte 'response' no est� definit.");
    this.setDescripcion(descripcion);
    
    // Definimos el debug del error
    this.setDebug((String)descripcion.get(BitError.IDIOMA_ESPANYOL));
  }
  
}
