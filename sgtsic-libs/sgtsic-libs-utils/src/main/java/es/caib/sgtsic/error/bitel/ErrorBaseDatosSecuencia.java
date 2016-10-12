package es.caib.sgtsic.error.bitel;

import java.util.HashMap;


/**
 * La clase ErrorBaseDatosSecuencia representa un error producido al acceder a una secuencia de base de datos.
 * $Id: ErrorBaseDatosSecuencia.java,v 1.2 2007/02/08 13:02:30 fros Exp $
 * 
 * @author Miguel Angel Nieto
 * @author Paco Ros
 * @author Felipe Benavente
 * @date 05/01/2007
 * @version 3.0
 */
public class ErrorBaseDatosSecuencia extends ErrorBaseDatos
{
  
  /**
   * Constructor de la clase BitError.
   * @param nombreSecuencia    Nombre de la secuencia.
   * @param error    Excepci�n/error asociado al error.
   */
  public ErrorBaseDatosSecuencia(String nombreSecuencia, Throwable error)
  {
    // Creamos el error con el c�digo del error y la excepci�n asociada
    super("BIT_BD_SECUENCIA", error);
    
    // Definimos la descripci�n del error
    HashMap descripcion = new HashMap();
    descripcion.put(BitError.IDIOMA_ESPANYOL, "Error en la secuencia " + nombreSecuencia + " de base de datos.");
    descripcion.put(BitError.IDIOMA_CATALAN , "Error en la seq��ncia " + nombreSecuencia + " de base de dades.");
    this.setDescripcion(descripcion);
    
    // Definimos el debug del error
    this.setDebug((String)descripcion.get(BitError.IDIOMA_ESPANYOL));
  }
  
}
