package es.caib.sgtsic.error.bitel;


import java.sql.SQLException;

import java.util.HashMap;


/**
 * La clase ErrorBaseDatosDelete representa un error producido por un DELETE en base de datos.
 * $Id: ErrorBaseDatosDelete.java,v 1.3 2007/02/08 13:02:30 fros Exp $
 * 
 * @author Miguel Angel Nieto
 * @author Paco Ros
 * @author Felipe Benavente
 * @date 05/01/2007
 * @version 3.0
 */
public class ErrorBaseDatosDelete extends ErrorBaseDatos
{
  
  /**
   * Constructor de la clase BitError.
   * @param nombreElementoEliminar    Nombre del elemento del que se hace el delete.
   * @param nombreElementoRelacionado    Nombre del elemento cuya relaci�n motiva la eliminaci�n del primero
   * @param idRelacionado    C�digo del elemento relacionado por el que se est� eliminando
   * @param sql    Sentencia sql.
   * @param sqle    Excepci�n/error asociado al error.
   */
  public ErrorBaseDatosDelete(String nombreElementoEliminar, String nombreElementoRelacionado, int idRelacionado, String sql, SQLException sqle)
  {
    this(nombreElementoEliminar, nombreElementoRelacionado, String.valueOf(idRelacionado), sql, sqle);
  }
  /**
   * Constructor de la clase BitError.
   * @param nombreElementoEliminar    Nombre del elemento del que se hace el delete.
   * @param nombreElementoRelacionado    Nombre del elemento cuya relaci�n motiva la eliminaci�n del primero
   * @param idRelacionado    C�digo del elemento relacionado por el que se est� eliminando
   * @param sql    Sentencia sql.
   * @param sqle    Excepci�n/error asociado al error.
   */
  public ErrorBaseDatosDelete(String nombreElementoEliminar, String nombreElementoRelacionado, String idRelacionado, String sql, SQLException sqle)
  {
    // Creamos el error con el c�digo del error y la excepci�n asociada
    super("BIT_BD_DELETE", sqle);
    
    // Definimos la descripci�n del error
    HashMap descripcion = new HashMap();
    descripcion.put(BitError.IDIOMA_ESPANYOL, "Error al eliminar " + nombreElementoEliminar + " relacionados con " + nombreElementoRelacionado + " con c�digo " + idRelacionado + ".");
    descripcion.put(BitError.IDIOMA_CATALAN , "Error a l'eliminar " + nombreElementoEliminar + " relacionats/ades amb " + nombreElementoRelacionado + " amb codi " + idRelacionado + ".");
    this.setDescripcion(descripcion);
    
    // Definimos el debug del error
    this.setDebug((String)descripcion.get(BitError.IDIOMA_ESPANYOL)
                  + "\nSQL: " + sql
                  + "\nFk: " + idRelacionado);
  }
  
  /**
   * Constructor de la clase BitError.
   * @param nombreElemento    Nombre del elemento.
   * @param id    C�digo por el que se est� eliminando.
   * @param sql    Sentencia sql.
   * @param sqle    Excepci�n/error asociado al error.
   */
  public ErrorBaseDatosDelete(String nombreElemento, int  id, String sql, SQLException sqle)
  {
    this(nombreElemento, String.valueOf(id), sql, sqle);
  }
    
  /**
   * Constructor de la clase BitError.
   * @param nombreElemento    Nombre del elemento.
   * @param id    C�digo por el que se est� eliminando.
   * @param sql    Sentencia sql.
   * @param sqle    Excepci�n/error asociado al error.
   */
  public ErrorBaseDatosDelete(String nombreElemento, String  id, String sql, SQLException sqle)
  {
    // Creamos el error con el c�digo del error y la excepci�n asociada
    super("BIT_BD_DELETE", sqle);
    
    // Definimos la descripci�n del error
    HashMap descripcion = new HashMap();
    descripcion.put(BitError.IDIOMA_ESPANYOL, "Error al eliminar " + nombreElemento + " con c�digo " + id + ".");
    descripcion.put(BitError.IDIOMA_CATALAN , "Error a l'eliminar " + nombreElemento + " amb codi " + id + ".");
    this.setDescripcion(descripcion);
    
    // Definimos el debug del error
    this.setDebug((String)descripcion.get(BitError.IDIOMA_ESPANYOL)
                  + "\nSQL: " + sql
                  + "\nId: " + id);
  }
  
}
