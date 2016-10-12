package es.caib.sgtsic.util.bitel;


import java.util.*;


/**
 * Representa la pila de direcciones del sistema de navegacon.
 * @author MNI
 * @author ELU
 * @version 1.0
 * @created 02-sep-2004 12:00:00
 */
public class BitNavegacion
{
  private static final int TAMANYO_POR_DEFECTO = 32;

  private Stack pila;
  private int tamanyo;
  private int tamanyoMaximo;

  /**
     * Constructor ampliado de la clase. Inicializa la pila de navegacion y su tamanyo.
     * @param tm    Tamanyo maximo de la pila.
     */
  public BitNavegacion(int tm)
  {
    this.tamanyoMaximo = tm;
    this.tamanyo = 0;
    this.pila = new Stack();
  }

  /**
     * <p>Inserta una URL en la pila de navegacion.</p>
     * <p>- Si la nueva URL se considera equivalente a la que se encuentra en la parte
     * superior de la pila de navegacion, no se anyade sino que silo se modifican sus
     * parametros. Equivalente significa que las acciones son iguales.</p>
     * <p>- Si la nueva URL es nula, no se inserta.
     * @param url    URL que se desea insertar.
     */
  public void anayadirURL(BitURL url)
  {
    BitLog.debug("Se anyade la url " + url.getHref() + " de la accion " + url.getAccion());

    if (url != null)
    {
      BitURL ultimaURL = null;

      if (!pila.isEmpty())
      {
        ultimaURL = (BitURL) pila.peek();
      }

      // Si lo insertamos URLs cuyo href sea distinto a la URL superior en la pila de navegacion
      if (!url.equals(ultimaURL))
      {
        pila.push(url);
        aumentaTamanyo();

        // Si excedemos el tamanyo de la pila, quitamos el elemento mas antiguo
        if (getTamanyo() > tamanyoMaximo)
        {

          // Aprovechamos el hecho de que la clase Stack extiende Vector
          pila.remove(0);
          disminuyeTamanyo();
        }
      }

      // Si los href coinciden, modificamos la parte de parametros
      else if (ultimaURL != null)
      {
        ultimaURL.setParametros(url.getParametros());
      }
    } // fin if (url!=null)
  }

  /**
     * Aumenta en uno el numero de elementos de la pila
     */
  private void aumentaTamanyo()
  {
    this.tamanyo++;
  }

  /**
     * Disminuye en uno el numero de elementos de la pila
     */
  private void disminuyeTamanyo()
  {
    this.tamanyo--;
  }

  /**
     * Extrae una URL de la pila de navegacion y devuelve su valor.
     * @return Devuelve null si la pila esta vacia.
     */
  public BitURL extraerURL()
  {
    BitURL url = null;

    if (!pila.isEmpty())
    {
      url = (BitURL) pila.pop();

      // Decrementamos el tamanyo actual
      disminuyeTamanyo();
    }

    return url;
  }

  /**
     * Metodo toString() de la clase.
     */
  public String toString()
  {
    StringBuffer sb = new StringBuffer();

    for (int i = 0; i < pila.size(); i++)
    {
      BitURL url = (BitURL) pila.get(i);

      sb.append(i + ": " + url + "\n");
    }

    return sb.toString();
  }

  /**
     * Recupera el numero de elementos de la pila
     * @return numero de elementos de la pila
     */
  private int getTamanyo()
  {
    return this.tamanyo;
  }

  /**
     * Retorna la URL de la cima pero sin sacarla de la pila
     * @return retorna la URL de la cima pero sin sacarla. Si no hay URL entonces
     * retorna null
     */
  public BitURL getUrl()
  {
    if (pila != null) // el stack contiene elementos
    {
      if (pila.isEmpty())
      { // la pila esta vacia
        return null;
      }
      else
      { // la pila no esta vacia
        return (BitURL) pila.peek();
      }
    }
    else
    {
      return null;
    }
  }
}