package es.caib.sgtsic.error.bitel;

import java.util.HashMap;


/**
 * La clase ErrorDesconocido representa un error en la lectura de informaci�n en formato XML.
 * 
 * @author Miguel Angel Nieto
 * @author Paco Ros
 * @date 05/01/2007
 * @version 2.0
 */
public class ErrorDesconocido extends BitError
{
  
  /**
   * Constructor de la clase BitError.
   * @param error    Excepci�n/error asociado al error.
   */
  public ErrorDesconocido(Throwable error)
  {
    // Creamos el error con el c�digo del error y la excepci�n asociada
    super("BIT_DESCONOCIDO", error);
    
    // Definimos la descripci�n del error
    HashMap descripcion = new HashMap();
    descripcion.put(BitError.IDIOMA_ESPANYOL, "Error desconocido.");
    descripcion.put(BitError.IDIOMA_CATALAN , "Error desconegut.");
    this.setDescripcion(descripcion);
    
    // Definimos el debug del error
    this.setDebug((String)descripcion.get(BitError.IDIOMA_ESPANYOL));
  }
  
}
