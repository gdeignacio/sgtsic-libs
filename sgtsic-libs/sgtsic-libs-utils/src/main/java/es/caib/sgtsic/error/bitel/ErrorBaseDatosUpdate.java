package es.caib.sgtsic.error.bitel;

import es.caib.sgtsic.data.Data;
import es.caib.sgtsic.util.bitel.BitCadena;

import java.sql.SQLException;

import java.util.HashMap;


/**
 * La clase ErrorBaseDatosUpdate representa un error producido por un UPDATE en base de datos.
 * $Id: ErrorBaseDatosUpdate.java,v 1.3 2007/02/08 13:02:31 fros Exp $
 * @author Miguel Angel Nieto
 * @author Paco Ros
 * @author Felipe Benavente
 * @date 05/01/2007
 * @version 3.0
 */
public class ErrorBaseDatosUpdate extends ErrorBaseDatos
{
  
  /**
   * Constructor de la clase BitError.
   * @param nombreElemento    Nombre del elemento.
   * @param data    Informaci�n de la clase Data.
   * @param sql    Sentencia sql.
   * @param error    Excepci�n/error asociado al error.
   */
  public ErrorBaseDatosUpdate(String nombreElemento, Data data, String sql, SQLException sqle)
  {
    this(nombreElemento, null, data, sql, sqle);
  }
  
  /**
   * Constructor de la clase BitError.
   * @param nombreElemento    Nombre del elemento.
   * @param data    Informaci�n de la clase Data.
   * @param sql    Sentencia sql.
   * @param id    C�digo del elemento afectado.
   * @param error    Excepci�n/error asociado al error.
   */
  public ErrorBaseDatosUpdate(String nombreElemento, int id, Data data, String sql, SQLException sqle)
  {
    this(nombreElemento, String.valueOf(id), data, sql, sqle);
  }
  
  /**
   * Constructor de la clase BitError.
   * @param nombreElemento    Nombre del elemento.
   * @param data    Informaci�n de la clase Data.
   * @param sql    Sentencia sql.
   * @param id    C�digo del elemento afectado.
   * @param error    Excepci�n/error asociado al error.
   */
  public ErrorBaseDatosUpdate(String nombreElemento, String id, Data data, String sql, SQLException sqle)
  {
    // Creamos el error con el c�digo del error y la excepci�n asociada
    super("BIT_BD_UPDATE", sqle);
    
    // Definimos la descripci�n del error
    HashMap descripcion = new HashMap();
    if (id != null){
      descripcion.put(BitError.IDIOMA_ESPANYOL, "Error al actualizar " + nombreElemento + " con c�digo " + id + ".");
      descripcion.put(BitError.IDIOMA_CATALAN , "Error a l'actualitzar " + nombreElemento + " amb codi " + id + ".");
    }
    else{
      descripcion.put(BitError.IDIOMA_ESPANYOL, "Error al actualizar " + nombreElemento + ".");
      descripcion.put(BitError.IDIOMA_CATALAN , "Error a l'actualitzar " + nombreElemento + ".");
    }
    this.setDescripcion(descripcion);
    
    // Definimos el debug del error
    this.setDebug((String)descripcion.get(BitError.IDIOMA_ESPANYOL)
                  + "\nSQL: " + sql
                  + "\nData: " + data
                  + (id == null ? BitCadena.CADENA_VACIA : ("\nId: " + id)));
  }
  
}
