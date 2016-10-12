package es.caib.sgtsic.data;


//~--- JDK imports ------------------------------------------------------------

import java.io.*;


/**
 * Clase que define los filtros de la aplicaci�n
 * @version 1.1 La clase ya no es abstracta. Se realiza por motivos de DWR
 * @created 12-ago-2004 9:47:01
 * @author Paco Ros
 */
public class FiltroData extends DataAdapter implements Serializable
{

  /**
     * Indica si el orden es ascendente (1...10 - a ... z) o descendente (10 ... 1 - z
     * ... a)
     */
  private boolean ordenAscendente = true;
  /**
     * En este atributo guardarmos el nombre de la clase que extienda de esta clase. Se utiliza por motivos de DWR para poder
     * recuperar los atributos de las clases hijas cuando viajen los datos de JavaScript a Java y viceversa
     */
  private String className;

  /**
     * N�mero de columna por la que se ordenar� el listado. -1 indica sin orden.
     */
  private int columnaOrden;


  public FiltroData()
  {
  }


  /**
     * N�mero de columna por la que se ordenar� el listado. -1 indica sin orden.
     */
  public int getColumnaOrden()
  {
    return columnaOrden;
  }

  /**
     * Indica si el orden es ascendente (1...10 - a ... z) o descendente (10 ... 1 - z
     * ... a)
     */
  public boolean isOrdenAscendente()
  {
    return ordenAscendente;
  }


  /**
     * N�mero de columna por la que se ordenar� el listado. -1 indica sin orden.
     * @param newVal
     *
     */
  public void setColumnaOrden(int newVal)
  {
    columnaOrden = newVal;
  }

  /**
     * Indica si el orden es ascendente (1...10 - a ... z) o descendente (10 ... 1 - z
     * ... a)
     * @param newVal
     *
     */
  public void setOrdenAscendente(boolean newVal)
  {
    ordenAscendente = newVal;
  }

    /**
     * @return the className
     */
    public String getClassName() {
        return className;
    }

    /**
     * @param className the className to set
     */
    public void setClassName(String className) {
        this.className = className;
    }
}
