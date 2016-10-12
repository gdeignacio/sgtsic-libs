package es.caib.sgtsic.util.bitel;

/**
 * Representa una URL.
 * @author MNI
 * @author ELU
 * @version 1.0
 * @created 01-sep-2004 12:00:00
 */
public class BitURL implements java.io.Serializable
{
  private String accion;
  private String href;
  private String parametros;

  /**
     * Constructor por defecto de la clase BitURL
     */
  public BitURL()
  {
  }

  /**
     * Constructor de la clase BitURL
     * @param href url base
     * @param parametros parametros de la url
     */
  public BitURL(String href, String parametros)
  {
    this.setHref(href);
    this.setParametros(parametros);
  }

  /**
     * Constructor de la clase BitURL
     * @param href url base
     * @param parametros parametros de la url
     * @param accion accion del Controlador
     */
  public BitURL(String href, String parametros, String accion)
  {
    this.setHref(href);
    this.setParametros(parametros);
    this.setAccion(accion);
  }

  /**
     * Sobrecarga del metodo equals.
     * Analiza inicamente la propiedad accion de las URLs.
     * @param bitURL objeto BitURL a comparar
     * @return retorna true si la propiedad accion del objeto bitURL
     * pasado por parametro es igual a la propiedad acccion del objeto al
     * que se invoca el metodo.
     */
  public boolean equals(BitURL bitURL)
  {
    if (bitURL == null)
    {
      return false;
    }

    return this.getAccion().equals(bitURL.getAccion());
  }

  /**
     *
     * @return Retorna en una cadena String el valor de las propiedades del objeto
     */
  public String toString()
  {
    StringBuffer sb = new StringBuffer();

    sb.append(this.getHref());
    sb.append(this.getParametros());

    return sb.toString();
  }

  /**
     * Retorna la propiedad accion
     * @return  String
     */
  public String getAccion()
  {
    if (accion == null)
    {
      return BitCadena.CADENA_VACIA;
    }
    else
    {
      return accion;
    }
  }

  /**
     * @return Retorna la propiedad href del objeto
     */
  public String getHref()
  {
    if (href == null)
    {
      return BitCadena.CADENA_VACIA;
    }
    else
    {
      return href;
    }
  }

  /**
     * @return Retorna la propiedad parametros del objeto
     */
  public String getParametros()
  {
    if (parametros == null)
    {
      return BitCadena.CADENA_VACIA;
    }
    else
    {
      return parametros;
    }
  }

  /**
     * Establece el valor del atributo accion
     * @param accion Accion que se utiliza en el controlador
     */
  public void setAccion(String accion)
  {
    this.accion = accion;
  }

  /**
     * Establece el valor del atributo href.
     * @param newVal valor que queremos que tenga la propiedad href del objeto
     */
  public void setHref(String newVal)
  {
    href = newVal;
  }

  /**
     * Establece el valor del atributo parametros.
     * @param newVal valor que queremos que tenga la propiedad parametros del objeto
     */
  public void setParametros(String newVal)
  {
    parametros = newVal;
  }

  /**
     * Descompone la url pasada por parametro metiendo la direccion del recurso en
     * la propiedad href y los parametros en la propiedad parametros
     * @param url URL que queremos descomponer
     */
  public void setURL(String url)
  {
    int pos;

    if (url != null)
    {
      pos = url.lastIndexOf('?');

      if (pos != -1) // la url tiene parametros
      {
        this.setHref(url.substring(0, pos));
        this.setParametros(url.substring(pos, url.length()));
      }
      else // la url no tiene parametros
      {
        this.setHref(url);
        this.setParametros("");
      }
    }
  }
}
