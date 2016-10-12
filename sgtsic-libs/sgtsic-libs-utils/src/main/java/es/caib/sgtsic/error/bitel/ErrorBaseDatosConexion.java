package es.caib.sgtsic.error.bitel;

import java.util.HashMap;


/**
 * La clase ErrorBaseDatosConexion representa un error producido al obtener una conexi�n con la base de datos.
 * $Id: ErrorBaseDatosConexion.java,v 1.2 2007/02/08 13:02:30 fros Exp $
 * 
 * @author Miguel Angel Nieto
 * @author Paco Ros
 * @date 05/01/2007
 * @version 2.0
 */
public class ErrorBaseDatosConexion extends ErrorBaseDatos
{
  
  /**
   * Constructor de la clase BitError.
   * @param nombreDataSource    Nombre del DataSource.
   * @param error    Excepci�n/error asociado al error.
   */
  public ErrorBaseDatosConexion(String nombreDataSource, Throwable error)
  {
    // Creamos el error con el c�digo del error y la excepci�n asociada
    super("BIT_BD_CONEXION", error);
    
    // Definimos la descripci�n del error
    HashMap descripcion = new HashMap();
    descripcion.put(BitError.IDIOMA_ESPANYOL, "Error al establecer una conexi�n con el DataSource " + nombreDataSource + ".");
    descripcion.put(BitError.IDIOMA_CATALAN , "Error a l'establir una connexi� amb el DataSource " + nombreDataSource + ".");
    this.setDescripcion(descripcion);
    
    // Definimos el debug del error
    this.setDebug((String)descripcion.get(BitError.IDIOMA_ESPANYOL));
  }
  
}
