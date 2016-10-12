package es.caib.sgtsic.error.bitel;

import java.util.HashMap;


/**
 * La clase ErrorReflexion representa un error producido al utilizar la API Reflection para manipular clases.
 * $Id: ErrorReflexion.java,v 1.1 2007/02/08 13:02:47 fros Exp $
 * 
 * @author Paco Ros
 * @date 05/01/2007
 * @version 2.0
 */
public class ErrorReflexion extends BitError
{
  
 
  /**
   * Constructor de la clase ErrorFicheroEscritura.
   * @param nombreClase    Nombre del fichero.
   * @param error    Excepci�n/error asociado al error.
   */
  public ErrorReflexion(String nombreClase, Throwable error)
  {
    // Creamos el error con el c�digo del error y la excepci�n asociada
    super("BIT_REFLECTION", error);
    
    // Definimos la descripci�n del error
    HashMap descripcion = new HashMap();
    descripcion.put(BitError.IDIOMA_ESPANYOL, "Error al acceder a la descripci�n intenrna de la clase " + nombreClase + ".");
    descripcion.put(BitError.IDIOMA_CATALAN , "Error a l'accedir a la descripci� interna de la classe " + nombreClase + ".");
    this.setDescripcion(descripcion);
    
    // Definimos el debug del error
    this.setDebug((String)descripcion.get(BitError.IDIOMA_ESPANYOL));
  }
  
}
