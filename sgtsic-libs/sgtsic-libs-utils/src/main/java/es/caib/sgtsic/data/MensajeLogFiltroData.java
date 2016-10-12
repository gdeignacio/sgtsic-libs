package es.caib.sgtsic.data;


//~--- JDK imports ------------------------------------------------------------

import es.caib.sgtsic.util.bitel.BitCadena;
import java.sql.Timestamp;


public class MensajeLogFiltroData implements java.io.Serializable
{
  private boolean mostrarInsert = true;
  private boolean mostrarUpdate = true;
  private boolean mostrarSelectLista = true;
  private boolean mostrarSelect = true;
  private boolean mostrarDelete = true;
  private String clave;
  private Timestamp fechaDesde;
  private Timestamp fechaHasta;
  private String nombreUsuario;
  private String tabla;
  private String texto;


  public String toString()
  {
    try
    {
      return BitCadena.doToString(this);
    }
    catch (Exception e)
    {
      return "[ERROR RECUPERANDO INFORMACION DE LA CLASE DATA]";
    }
  }


  public String getClave()
  {
    if (clave == null)
    {
      clave = BitCadena.CADENA_VACIA;
    }

    return clave;
  }

  public Timestamp getFechaDesde()
  {
    return fechaDesde;
  }

  public Timestamp getFechaHasta()
  {
    return fechaHasta;
  }

  public String getNombreUsuario()
  {
    if (nombreUsuario == null)
    {
      nombreUsuario = BitCadena.CADENA_VACIA;
    }

    return nombreUsuario;
  }

  public String getTabla()
  {
    if (tabla == null)
    {
      tabla = BitCadena.CADENA_VACIA;
    }

    return tabla;
  }

  public String getTexto()
  {
    if (texto == null)
    {
      texto = BitCadena.CADENA_VACIA;
    }

    return texto;
  }

  public boolean isMostrarDelete()
  {
    return mostrarDelete;
  }

  public boolean isMostrarInsert()
  {
    return mostrarInsert;
  }

  public boolean isMostrarSelect()
  {
    return mostrarSelect;
  }

  public boolean isMostrarSelectLista()
  {
    return mostrarSelectLista;
  }

  public boolean isMostrarUpdate()
  {
    return mostrarUpdate;
  }


  public void setClave(String s)
  {
    clave = s;
  }

  public void setFechaDesde(Timestamp newFechaDesde)
  {
    fechaDesde = newFechaDesde;
  }

  public void setFechaHasta(Timestamp newFechaHasta)
  {
    fechaHasta = newFechaHasta;
  }

  public void setMostrarDelete(boolean newMostrarDelete)
  {
    mostrarDelete = newMostrarDelete;
  }

  public void setMostrarInsert(boolean newMostrarInsert)
  {
    mostrarInsert = newMostrarInsert;
  }

  public void setMostrarSelect(boolean newMostrarSelect)
  {
    mostrarSelect = newMostrarSelect;
  }

  public void setMostrarSelectLista(boolean newMostrarSelectLista)
  {
    mostrarSelectLista = newMostrarSelectLista;
  }

  public void setMostrarUpdate(boolean newMostrarUpdate)
  {
    mostrarUpdate = newMostrarUpdate;
  }

  public void setNombreUsuario(String newNombreUsuario)
  {
    nombreUsuario = newNombreUsuario;
  }

  public void setTabla(String newTabla)
  {
    tabla = newTabla;
  }

  public void setTexto(String newTexto)
  {
    texto = newTexto;
  }
}
