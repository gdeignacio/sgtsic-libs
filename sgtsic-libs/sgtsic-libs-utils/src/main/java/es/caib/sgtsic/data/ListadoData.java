package es.caib.sgtsic.data;


//~--- JDK imports ------------------------------------------------------------

import java.io.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Clase abstracta que representa un listado
 * @version 1.0
 * @Author Paco Ros
 * @created 12-ago-2004 9:47:02
 */
public abstract class ListadoData extends DataAdapter implements Serializable
{

  /**
     * N�mero de columnas del listado
     */
  public static int numeroColumnas;

  /**
     * Opcionalmente, se puede inclu�r el texto de los t�tulos de las columnas del
     * listado.
     */
  public static String[] textoColumnas;

  /**
     * Lista de resultados del listado. Contiene necesariamente clases que implementan
     * Data
     */
  private ArrayList vRes = new ArrayList();

  /**
     * Posicion que ocupa el primer elemento del listado de este objeto
     * dentro del listado total.
     */
  private int elementoInicio = 1;

  /**
     * Indica si el listado ha recuperado m�s elementos de los permitidos.
     */
  private boolean demasiadosElementos;

  /**
     * Posicion que ocupa el ultimo elemento del listado de este objeto
     * dentro del listado total.
     */
  private int elementoFin;

  /**
     * Filtro (si procede) que gener� el listado
     */
  private FiltroData filtro;

  /**
     * N�mero de filas (elementos) del listado
     */
  private int numeroFilas;

  public ListadoData()
  {
  }


  /**
     * Elimina un elemento en la posici�n i de la Collection interna
     * @param pos    Posici�n (de 0 a N) que ocupa el elemento en la Collection
     * interna
     *
     */
  public void elimina(int pos)
  {
    vRes.remove(pos);
  }

  /**
     * A�ade una implementaci�n de es.bitel.data.Data a la Collection interna en la
     * �ltima posici�n
     * @param data    Clase que se quiere a�adir a la Collection interna
     *
     */
  public void pushData(Data data)
  {
    vRes.add(data);
  }

  /**
     * Vac�a la colecci�n de elementos
     */
  public void vacia()
  {
    vRes.clear();
  }

  /**
     * Devuelve la Collection completa de instancias de clases que implementan es.
     * bitel.data.Data que contiene el listado.
     * Especialmente �til para ejecutar bit:iterate
     * @return listado de elementos
     */
  public Collection getCollection()
  {
    return vRes;
  }

  /**
     * Devuelve un elemento situado en la posici�n pos de la collection interna
     * @param pos    Devuelve la instancia de es.bitel.data.Data que se encuentra en
     * la posici�n "pos" de la Collection interna
     *
     */
  public Data getData(int pos)
  {
    return (Data) vRes.get(pos);
  }

  /**
     * Retorna que posicion ocupa el ultimo elemento del listado de este objeto
     * dentro del listado total.
     * @return elemento final
     */
  public int getElementoFin()
  {
    return (vRes.size() - elementoInicio) - 1;
  }

  /**
     * Retorna que posicion ocupa el primer elemento del listado de este objeto
     * dentro del listado total.
     * @return elemento de inicio
     */
  public int getElementoInicio()
  {
    return elementoInicio;
  }

  /**
     * Devuelve el n�mero de elementos del listado
     */
  public int getNumeroFilas()
  {
    return vRes.size();
  }

  /**
     * Retorna true si el listado de este objeto no representa los elementos iniciales
     * del listado total. Retorna false en caso contrario. Este metodo determina si
     * podemos ir hacia atr�s en el listado total.
     * @return retorna un boolean
     */
  public boolean isAnterior()
  {
    return (elementoInicio > 1);
  }

  /**
     * Recupera el estado del exceso de elementos en el listado
     * @return
     */
  public boolean isDemasiadosElementos()
  {
    return demasiadosElementos;
  }

  /**
     * Retorna true si el listado paginado ha llegado a su fin. Retorna false en
     * caso contrario. Este metodo determina si podemos seguir hacia adelante
     * ene el listado total.
     * @return retorna un boolean
     */
  public boolean isSiguiente()
  {
    return demasiadosElementos;
  }

  /**
     * Especifica si el listado se ha recuperado con m�s elementos de los permitidos
     * @param demasiadosElementos
     */
  public void setDemasiadosElementos(boolean demasiadosElementos)
  {
    this.demasiadosElementos = demasiadosElementos;
  }

  /**
     * Establece que posicion ocupa el primer elemento del listado de este objeto
     * dentro del listado total.
     * @param elementoInicio
     */
  public void setElementoInicio(int elementoInicio)
  {
    this.elementoInicio = elementoInicio;
  }
}
