package es.caib.sgtsic.data;


import es.caib.sgtsic.util.bitel.BitCadena;

//~--- JDK imports ------------------------------------------------------------

import java.io.*;

/**
 * Clase abstracta DominioData. Se debe extender de esta subclase para cada Dominio de la aplicaci�n
 * @author Paco Ros
 * @version 2004.10.04
 */
public abstract class DominioData extends DataAdapter implements Serializable
{

  /*
     * Constante para indicar que las listas de dominios deben aparecer ordenadas por c�digo
     */
  public static String ORDEN_CODIGO = "dom_codigo";

  /*
     * Constante para indicar que las listas de dominios deben aparecer ordenadas por valor
     */
  public static String ORDEN_VALOR = "dom_valor";
  
 /*
  * Constantes para determinar los valores nulos que deebn tomar las variables num�ricas.
 */
 public static final String CODIGO_STRING_NULO = BitCadena.CADENA_VACIA;
 public static final Integer CODIGO_INTEGER_NULO = new Integer(-1);
 public static final Double CODIGO_DOUBLE_NULO = new Double(-1.0d);
 


  /*
     * C�digo del elemento del dominio
     */
  private String codigo = null;

  /*
     * Valor del elemento del dominio
     */
  private String valor = null;

  /*
     * Orden de recuperaci�n en las listas (ORDEN_CODIGO u ORDEN_VALOR). Por defecto el segundo
     */
  private String orden = null;


  public DominioData()
  {
    this.orden = ORDEN_VALOR;
  }

  public DominioData(String orden)
  {
    this.orden = orden;
  }


  /**
     * Recupera el c�digo del dominio
     * @return codigo del dominio
     */
  public final String getCodigoLOV()
  {
    if (codigo == null)
    {
      return BitCadena.CADENA_VACIA;
    }

    return codigo;
  }

  /**
     * Recupera el nombre del dominio representado por la subclase
     * @return  nombre del dominio
     */
  public abstract String getDominio();

  /**
     * Recupera el nombre de la tabla que contiene los dominios de la aplicaci�n
     * @return Nombre de la tabla que contiene los dominios de la aplicaci�n
     */
  public abstract String getNombreTabla();

  /**
     * Recupera el valor del orden
     * @return valor del orden
     */
  public String getOrden()
  {
    if (orden == null)
    {
      return BitCadena.CADENA_VACIA;
    }
    else
    {
      return orden;
    }
  }

  /**
     * Recupera el valor del dominio en el objeto
     * @return valor del dominio
     */
  public final String getValorLOV()
  {
    if (valor == null)
    {
      return BitCadena.CADENA_VACIA;
    }
    else
    {
      return valor;
    }
  }


  /**
     *  Pone el valor del c�digo del dominio
     * @param s nuevo c�digo
     */
  public final void setCodigoLOV(String s)
  {
    this.codigo = s;
  }

  /**
     * Asigna el valor del orden
     * @param s Valor del orden
     */
  public void setOrden(String s)
  {
    orden = s;
  }

  /**
     *  Asigna el valor del dominio al objeto
     * @param s Valor del dominio
     */
  public final void setValorLOV(String s)
  {
    valor = s;
  }
}
