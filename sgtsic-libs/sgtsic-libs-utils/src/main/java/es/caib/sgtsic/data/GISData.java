package es.caib.sgtsic.data;

import java.io.Serializable;

import java.util.Collection;

/* Representa un elemento de localizaci�n global GIS de la base de datos
 *    Contiene informaci�n sobre los c�digos de la localizaci�n como son:
 *        idPais
 *        idComunidad
 *        idProvincia
 *        idIsla
 *        idMunicipio
 *        idLocalidad
 *        CP
 *    Contiene las listas de localizaciones tales como:
 *        listaPais
 *        listaComunidad
 *        listaProvincia
 *        listaIsla
 *        listaMunicipio
 *        listaLocalidad
 *        listaCP
 */
public class GISData implements Serializable
{
  private String idPais = null; /* Identificador del pais */
  private String nombrePais = null; /* Nombre del pais */
  private Collection listaPaises = null; /* Lista de paises */
  private String idProvincia = null; /* Identificador de la provincia */
  private String idComunidad = null; /* Identificador de la comunidad aut�noma */
  private String nombreProvincia = null; /* Nombre de la provincia */
  private String nombreMunicipio = null; /* Nombre del municipio */
  private String nombreLocalidad = null; /* Nombre de la localidad */
  private String nombreIsla = null; /* Nombre de la isla */
  private String nombreComunidad = null; /* Nombre de la comunidad aut�noma */
  private Collection listaProvincias = null; /* Lista de provincias */
  private Collection listaMunicipios = null; /* Lista de municipios */
  private Collection listaLocalidades = null; /* Lista de localidades */
  private Collection listaIslas = null; /* Lista de islas */
  private Collection listaComunidades = null; /* Lista de comunidades */
  private Collection listaCP = null; /* Lista de c�digos postales */
  private String idMunicipio = null; /* Identificador del municipio */
  private String idLocalidad = null; /* Identificador de la localidad */
  private String idIsla = null; /* Identificador de la isla */
  private String idCP = null; /* C�digo Postal */

  public GISData()
  {
  }

  public final String getIdCP()
  {
    return idCP;
  }

  public final String getIdComunidad()
  {
    return idComunidad;
  }

  public final String getIdIsla()
  {
    return idIsla;
  }

  public final String getIdLocalidad()
  {
    return idLocalidad;
  }

  public final String getIdMunicipio()
  {
    return idMunicipio;
  }

  public final String getIdPais()
  {
    return idPais;
  }

  public final String getIdProvincia()
  {
    return idProvincia;
  }

  public final Collection getListaCP()
  {
    return listaCP;
  }

  public final Collection getListaComunidades()
  {
    return listaComunidades;
  }

  public final Collection getListaIslas()
  {
    return listaIslas;
  }

  public final Collection getListaLocalidades()
  {
    return listaLocalidades;
  }

  public final Collection getListaMunicipios()
  {
    return listaMunicipios;
  }

  public final Collection getListaPaises()
  {
    return listaPaises;
  }

  public final Collection getListaProvincias()
  {
    return listaProvincias;
  }

  public final String getNombreComunidad()
  {
    return nombreComunidad;
  }

  public final String getNombreIsla()
  {
    return nombreIsla;
  }

  public final String getNombreLocalidad()
  {
    return nombreLocalidad;
  }

  public final String getNombreMunicipio()
  {
    return nombreMunicipio;
  }

  public final String getNombrePais()
  {
    return nombrePais;
  }

  public final String getNombreProvincia()
  {
    return nombreProvincia;
  }

  public final void setIdCP(String s)
  {
    this.idCP = s;
  }

  public final void setIdComunidad(String s)
  {
    this.idComunidad = s;
  }

  public final void setIdIsla(String s)
  {
    this.idIsla = s;
  }

  public final void setIdLocalidad(String s)
  {
    this.idLocalidad = s;
  }

  public final void setIdMunicipio(String s)
  {
    this.idMunicipio = s;
  }

  public final void setIdPais(String s)
  {
    this.idPais = s;
  }

  public final void setIdProvincia(String s)
  {
    this.idProvincia = s;
  }

  public final void setListaCP(Collection c)
  {
    this.listaCP = c;
  }

  public final void setListaComunidades(Collection c)
  {
    this.listaComunidades = c;
  }

  public final void setListaIslas(Collection c)
  {
    this.listaIslas = c;
  }

  public final void setListaLocalidades(Collection c)
  {
    this.listaLocalidades = c;
  }

  public final void setListaMunicipios(Collection c)
  {
    this.listaMunicipios = c;
  }

  public final void setListaPaises(Collection c)
  {
    this.listaPaises = c;
  }

  public final void setListaProvincias(Collection c)
  {
    this.listaProvincias = c;
  }

  public final void setNombreComunidad(String s)
  {
    this.nombreComunidad = s;
  }

  public final void setNombreIsla(String s)
  {
    this.nombreIsla = s;
  }

  public final void setNombreLocalidad(String s)
  {
    this.nombreLocalidad = s;
  }

  public final void setNombreMunicipio(String s)
  {
    this.nombreMunicipio = s;
  }

  public final void setNombrePais(String s)
  {
    this.nombrePais = s;
  }

  public final void setNombreProvincia(String s)
  {
    this.nombreProvincia = s;
  }
}
