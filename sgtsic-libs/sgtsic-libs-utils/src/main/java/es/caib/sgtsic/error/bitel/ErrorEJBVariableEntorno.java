package es.caib.sgtsic.error.bitel;

import java.util.HashMap;


/**
 * La clase ErrorEJBVariableEntorno representa un error producido al obtener una variable de entorno JNDI.
 * 
 * @author Miguel Angel Nieto
 * @author Paco Ros
 * @date 05/01/2007
 * @version 2.0
 */
public class ErrorEJBVariableEntorno extends ErrorEJB
{
  
  /**
   * Constructor de la clase BitError.
   * @param nombreVariable    Nombre de la variable.
   * @param error    Excepci�n/error asociado al error.
   */
  public ErrorEJBVariableEntorno(String nombreVariable, Throwable error)
  {
    // Creamos el error con el c�digo del error y la excepci�n asociada
    super("BIT_BD_VARIABLE_ENTORNO", error);
    
    // Definimos la descripci�n del error
    HashMap descripcion = new HashMap();
    descripcion.put(BitError.IDIOMA_ESPANYOL, "Error al buscar la variable de entorno " + nombreVariable + ".");
    descripcion.put(BitError.IDIOMA_CATALAN , "Error al cercar la variable d'entorn " + nombreVariable + ".");
    this.setDescripcion(descripcion);
    
    // Definimos el debug del error
    this.setDebug((String)descripcion.get(BitError.IDIOMA_ESPANYOL));
  }
  
}
