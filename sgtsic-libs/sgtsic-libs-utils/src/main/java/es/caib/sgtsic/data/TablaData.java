package es.caib.sgtsic.data;

import es.caib.sgtsic.util.bitel.BitCadena;


public class TablaData extends DataAdapter implements java.io.Serializable
{
  private String id = BitCadena.CADENA_VACIA;
  private String nombre = BitCadena.CADENA_VACIA;


  public String getCodigoLOV()
  {
    return id;
  }

  public String getId()
  {
    return id;
  }

  public String getNombre()
  {
    return nombre;
  }

  public String getValorLOV()
  {
    return nombre;
  }


  public void setId(String s)
  {
    id = s;
  }

  public void setNombre(String s)
  {
    nombre = s;
  }
}
