package es.caib.sgtsic.data;

import java.io.*;

/* Representa un elemento de localizaci�n de la base de datos
 * Por ejemplo: pais
 *              Provincia
 *              Municipio
 *              Isla
 */
public class LocalizacionData extends DataAdapter implements Serializable
{
  public static final String ESPANYA = "34"; // C�digo de pais de Espa�a
  public static final String COMUNIDAD_BALEARES = "4"; // C�digo de Comunidad autonoma de Baleares
  public static final String BALEARES = "7"; // C�digo de Provincia de Baleares
  public static final String MALLORCA = "1"; // C�digo de la isla mallorca

  private String id = null; /* Identificador del elemento */
  private String nombre = null; /* Nombre del elemento */

  public LocalizacionData()
  {
  }

  public final String getCodigoLOV()
  {
    return this.id;
  }

  public final String getId()
  {
    return id;
  }

  public final String getNombre()
  {
    return nombre;
  }

  public final String getValorLOV()
  {
    return this.nombre;
  }

  public final void setId(String s)
  {
    this.id = s;
  }

  public final void setNombre(String s)
  {
    this.nombre = s;
  }
}
