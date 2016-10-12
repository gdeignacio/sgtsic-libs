package es.caib.sgtsic.util.bitel;

import java.util.HashMap;

/**
 * Representa una Hashtable
 * @author ELU
 * @version 1.0
 * @created 01-sep-2004 12:00:00
 */
public class BitHash
{
  /**
   * Representa la Hashtable
   */
  HashMap lista;

  /**
   * Constructor que crea la hashtable con la capacidad pasada por par�metro
   * @param capacity Capacidad que se desea que tenga la Hashtable
   */
  public BitHash(int capacity)
  {
    lista = new HashMap(capacity);
  }

  /**
   * Constructor que crea la hashtable con la capacidad pasada por par�metro y
   * el factor de carga especificado
   * @param capacity Capacidad que se desea que tenga la Hashtable
   * @param loadFactor factor de carga que puede ir de 0 a 1
   */
  public BitHash(int capacity, float loadFactor)
  {
    lista = new HashMap(capacity, loadFactor);
  }

  /**
   * Construye una hashtable con los par�metros por defecto
   */
  public BitHash()
  {
    lista = new HashMap();
  }

  /**
   * Retorna el objeto que corresponda con la clave pasada por par�metro. Si no
   * existe retorna null.
   * @param key Clave del objeto que se quiere recuperar
   * @return Retorna el objeto asociado a la clave.
   */
  public Object get(String key)
  {
    if (lista == null)
      return null;
    return lista.get(key);
  }

  /**
   * Introduce un nuevo objeto asociado a la clave aunque previamente ya exisitiera.
   * El m�todo es sincronizado
   * @param key Clave del objeto
   * @param obj Objeto que se quiere introducir
   */
  public synchronized void putEver(String key, Object obj)
  {
    if (lista == null)
      lista = new HashMap();
    lista.put(key, obj);
  }

  /**
   * Introduce un nuevo objeto asociado a la clave solo si no exist�a previamente.
   * El m�todo es sincronizado
   * @param key Clave del objeto
   * @param obj Objeto que se quiere introducir
   */
  public synchronized void put(String key, Object obj)
  {
    if (lista == null) //si la hash es null no hacemos nada
      lista = new HashMap();
    if (!lista.containsKey(key)) //si la hash no contiene la clave la metemos
      lista.put(key, obj);
  }

  /**
   * Retorna true si existe un objeto asociado a la clave
   * @param clave Clave que se quiere buscar
   * @return Retorna true si el existe un objeto asociado a la clave
   */
  public boolean containsKey(String clave)
  {
    if (lista == null)
      return false;
    return lista.containsKey(clave);
  }

  /**
   * Retorna la hashmap del objeto
   * @return HashMap
   */
  public HashMap getLista()
  {
    return lista;
  }

}
