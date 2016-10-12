package es.caib.sgtsic.error.bitel;

import java.util.HashMap;


/**
 * La clase ErrorDatosNoEncontrado representa un error producido al tratar de recuperar un elemento que no existe.
 * 
 * @author  Miguel Angel Nieto
 * @author  Paco Ros
 * @version 1.0, 16/07/2008
 */
public class ErrorDatosNoEncontrado extends BitError
{
  
  /**
   * Constructor de la clase ErrorDatosNoEncontrado.
   * 
   * @param elemento Nombre del tipo de elemento no encontrado.
   * @param id C�digo del elemento no encontrado.
   */
  public ErrorDatosNoEncontrado(String elemento, String id)
  {
    // Creamos el error con el c�digo del error y la excepci�n asociada
    super("BIT_DATOS_NO_ENCONTRADO");
    
    // Definimos la descripci�n del error
    HashMap descripcion = new HashMap();
    descripcion.put(BitError.IDIOMA_ESPANYOL, "No se ha encontrado ning�n elemento del tipo " + elemento + " con c�digo " + id + ".");
    descripcion.put(BitError.IDIOMA_CATALAN , "No s'ha trobat cap element del tipus " + elemento + " amb codi " + id + ".");
    this.setDescripcion(descripcion);
    
    // Definimos el debug del error
    this.setDebug((String)descripcion.get(BitError.IDIOMA_ESPANYOL));
  }
  
}
