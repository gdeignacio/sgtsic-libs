package es.caib.sgtsic.error.bitel;


import java.sql.SQLException;

import java.util.HashMap;


/**
 * La clase ErrorBaseDatosSelect representa un error producido por un SELECT de un elemento en base de datos.
 * $Id: ErrorBaseDatosSelect.java,v 1.3 2007/02/08 13:02:31 fros Exp $
 * 
 * @author Miguel Angel Nieto
 * @author Paco Ros
 * @author Felipe Benavente
 * @date 05/01/2007
 * @version 3.0
 */
public class ErrorBaseDatosSelect extends ErrorBaseDatos
{
  
  /**
   * Constructor de la clase BitError.
   * @param nombreElemento    Nombre del elemento afectado.
   * @param id     C�digo del elemento.
   * @param sql    Sentencia sql.
   * @param sqle    Excepci�n/error asociado al error.
   */
  public ErrorBaseDatosSelect(String nombreElemento, int id, String sql, SQLException sqle)
  {
    this(nombreElemento, String.valueOf(id), sql, sqle);
  }
  
  /**
   * Constructor de la clase BitError.
   * @param nombreElemento    Nombre del elemento afectado.
   * @param id     C�digo del elemento.
   * @param sql    Sentencia sql.
   * @param sqle    Excepci�n/error asociado al error.
   */
  public ErrorBaseDatosSelect(String nombreElemento, String id, String sql, SQLException sqle)
  {
    // Creamos el error con el c�digo del error y la excepci�n asociada
    super("BIT_BD_SELECT", sqle);
    
    // Definimos la descripci�n del error
    HashMap descripcion = new HashMap();
    descripcion.put(BitError.IDIOMA_ESPANYOL, "Error al consultar los datos de " + nombreElemento + " con el c�digo " + id + ".");
    descripcion.put(BitError.IDIOMA_CATALAN , "Error al consultar les dades de " + nombreElemento + " amb el codi " + id + ".");
    this.setDescripcion(descripcion);
    
    // Definimos el debug del error
    this.setDebug((String)descripcion.get(BitError.IDIOMA_ESPANYOL)
                  + "\nID: " + id
                  + "\nSQL: " + sql);
  }
  
}
