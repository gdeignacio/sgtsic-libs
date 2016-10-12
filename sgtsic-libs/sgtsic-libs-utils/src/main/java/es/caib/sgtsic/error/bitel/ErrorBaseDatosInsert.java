package es.caib.sgtsic.error.bitel;

import es.caib.sgtsic.data.Data;
import java.sql.SQLException;

import java.util.HashMap;


/**
 * La clase ErrorBaseDatosInsert representa un error producido por un INSERT en base de datos.
 * $Id: ErrorBaseDatosInsert.java,v 1.2 2007/02/08 13:02:30 fros Exp $
 * 
 * @author Miguel Angel Nieto
 * @author Paco Ros
 * @author Felipe Benavente
 * @date 05/01/2007
 * @version 3.0
 */
public class ErrorBaseDatosInsert extends ErrorBaseDatos
{
  
  /**
   * Constructor de la clase BitError.
   * @param nombreElemento    Nombre del elemento.
   * @param data    Informaci�n de la clase Data.
   * @param sql    Sentencia sql.
   * @param sqle    Excepci�n/error asociado al error.
   */
  public ErrorBaseDatosInsert(String nombreElemento, Data data, String sql, SQLException  sqle)
  {
    // Creamos el error con el c�digo del error y la excepci�n asociada
    super("BIT_BD_INSERT", sqle);
    
    // Definimos la descripci�n del error
    HashMap descripcion = new HashMap();
    descripcion.put(BitError.IDIOMA_ESPANYOL, "Error al insertar " + nombreElemento + ".");
    descripcion.put(BitError.IDIOMA_CATALAN , "Error a l'insertar " + nombreElemento + ".");
    this.setDescripcion(descripcion);
    
    // Definimos el debug del error
    this.setDebug((String)descripcion.get(BitError.IDIOMA_ESPANYOL)
                  + "\nSQL: " + sql
                  + "\nData: " + data);
  }
  
}
