package es.caib.sgtsic.error.bitel;

import java.util.HashMap;


/**
 * La clase ErrorEJBInterfazHome representa un error producido al obtener la interfaz remota.
 * 
 * @author Miguel Angel Nieto
 * @author Paco Ros
 * @date 05/01/2007
 * @version 2.0
 */
public class ErrorEJBInterfazHome extends ErrorEJB
{
  
  /**
   * Constructor de la clase BitError.
   * @param nombreElemento    Nombre del elemento.
   * @param error    Excepci�n/error asociado al error.
   */
  public ErrorEJBInterfazHome(String nombreElemento, Throwable error)
  {
    // Creamos el error con el c�digo del error y la excepci�n asociada
    super("BIT_BD_INTERFAZ_REMOTA", error);
    
    // Definimos la descripci�n del error
    HashMap descripcion = new HashMap();
    descripcion.put(BitError.IDIOMA_ESPANYOL, "Error al buscar un EJB.");
    descripcion.put(BitError.IDIOMA_CATALAN , "Error al cercar un EJB.");
    this.setDescripcion(descripcion);
    
    // Definimos el debug del error
    this.setDebug((String)descripcion.get(BitError.IDIOMA_ESPANYOL) + "EJB: " + nombreElemento);
  }
  
}
