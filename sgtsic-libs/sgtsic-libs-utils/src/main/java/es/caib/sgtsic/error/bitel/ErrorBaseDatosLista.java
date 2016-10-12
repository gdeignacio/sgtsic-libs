package es.caib.sgtsic.error.bitel;


import es.caib.sgtsic.data.FiltroData;
import es.caib.sgtsic.util.bitel.BitCadena;

import java.sql.SQLException;

import java.util.HashMap;


/**
 * La clase ErrorBaseDatosLista representa un error producido por un SELECT de m�s d eun elemento en base de datos.
 * $Id: ErrorBaseDatosLista.java,v 1.4 2007/02/08 13:02:30 fros Exp $
 * 
 * @author Miguel Angel Nieto
 * @author Paco Ros
 * @author Felipe Benavente
 * @date 05/01/2007
 * @version 3.0
 */
public class ErrorBaseDatosLista extends ErrorBaseDatos
{
  
  /**
   * Constructor de la clase BitError.
   * @param nombreElemento    Nombre del elemento.
   * @param sql    Sentencia sql.
   * @param sqle    Excepci�n/error asociado al error.
   */
  public ErrorBaseDatosLista(String nombreElemento, String sql, SQLException sqle){
    this(nombreElemento, null, sql, sqle);
  }
  
  /**
   * Constructor de la clase BitError.
   * @param nombreElemento    Nombre del elemento.
   * @param filtro    Informaci�n del filtro.
   * @param sql    Sentencia sql.
   * @param sqle    Excepci�n/error asociado al error.
   */
  public ErrorBaseDatosLista(String nombreElemento, FiltroData filtro, String sql, SQLException sqle)
  {
    // Creamos el error con el c�digo del error y la excepci�n asociada
    super("BIT_BD_LIST", sqle);
    
    // Definimos la descripci�n del error
    HashMap descripcion = new HashMap();
    descripcion.put(BitError.IDIOMA_ESPANYOL, "Error al consultar la lista de " + nombreElemento + ".");
    descripcion.put(BitError.IDIOMA_CATALAN , "Error al consultar la llista de  " + nombreElemento + ".");
    this.setDescripcion(descripcion);
    
    // Definimos el debug del error
    this.setDebug((String)descripcion.get(BitError.IDIOMA_ESPANYOL)
                  + "\nSQL: " + sql
                  + "\nFiltro: " + (filtro == null ? BitCadena.CADENA_VACIA : filtro.toString()));
  }
  
  
  /**
   * Constructor de la clase BitError.
   * @param nombreElemento    Nombre del elemento.
   * @param nombreReferencia    Nombre del elemento propietario de la FK por la que se consulta.
   * @param idReferenciado    Valor de la FK por la que se consulta la lista
   * @param sql    Sentencia sql.
   * @param error    Excepci�n/error asociado al error.
   */
  public ErrorBaseDatosLista(String nombreElemento, String nombreReferencia, int idReferenciado, String sql, SQLException sqle)
  {
    this(nombreElemento, nombreReferencia, String.valueOf(idReferenciado), sql, sqle);
  }
  
  /**
   * Constructor de la clase BitError.
   * @param nombreElemento    Nombre del elemento.
   * @param nombreReferencia    Nombre del elemento propietario de la FK por la que se consulta.
   * @param idReferenciado    Valor de la FK por la que se consulta la lista
   * @param sql    Sentencia sql.
   * @param sqle    Excepci�n/error asociado al error.
   */
  public ErrorBaseDatosLista(String nombreElemento, String nombreReferencia, String idReferenciado, String sql, SQLException sqle)
  {
    // Creamos el error con el c�digo del error y la excepci�n asociada
    super("BIT_BD_LIST", sqle);
    
    // Definimos la descripci�n del error
    HashMap descripcion = new HashMap();
    descripcion.put(BitError.IDIOMA_ESPANYOL, "Error al consultar la lista de " + nombreElemento + " relacionados con " + nombreReferencia + " y c�digo " + idReferenciado + ".");
    descripcion.put(BitError.IDIOMA_ESPANYOL, "Error al consultar la llista de " + nombreElemento + " relacionats amb " + nombreReferencia + " i codi " + idReferenciado + ".");
    this.setDescripcion(descripcion);
    
    // Definimos el debug del error
    this.setDebug((String)descripcion.get(BitError.IDIOMA_ESPANYOL)
                  + "\nSQL: " + sql
                  + "\nId ref: " + idReferenciado);
  }
  
}
