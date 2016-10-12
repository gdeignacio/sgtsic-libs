package es.caib.sgtsic.data;


import es.caib.sgtsic.util.bitel.BitCadena;
import java.sql.Timestamp;

public class MensajeLogData extends DataAdapter
{
  public static String INSERT = "INSERT";
  public static String UPDATE = "UPDATE";
  public static String SELECT_LISTA = "SELECT LISTA";
  public static String SELECT = "SELECT";
  public static String DELETE = "DELETE";


  private String id = BitCadena.CADENA_VACIA;
  private String clave;
  private String datos;
  private Timestamp fecha;
  private String operacion;
  private String tabla;
  private String usuario;


  public Object clone()
  {
    MensajeLogData clon = new MensajeLogData();

    clon.setId(id);
    clon.setUsuario(usuario);
    clon.setFecha(fecha);
    clon.setOperacion(operacion);
    clon.setTabla(tabla);
    clon.setClave(clave);
    clon.setDatos(datos);

    return clon;
  }

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
      return BitCadena.CADENA_VACIA;
    }
    else
    {
      return clave;
    }
  }

  public String getDatos()
  {
    if (datos == null)
    {
      return BitCadena.CADENA_VACIA;
    }
    else
    {
      return datos;
    }
  }

  public Timestamp getFecha()
  {
    return fecha;
  }

  public String getId()
  {
    if (id == null)
    {
      return BitCadena.CADENA_VACIA;
    }
    else
    {
      return id;
    }
  }

  public String getOperacion()
  {
    if (operacion == null)
    {
      return BitCadena.CADENA_VACIA;
    }
    else
    {
      return operacion;
    }
  }

  public String getTabla()
  {
    if (tabla == null)
    {
      return BitCadena.CADENA_VACIA;
    }
    else
    {
      return tabla;
    }
  }

  public String getUsuario()
  {
    if (usuario == null)
    {
      return BitCadena.CADENA_VACIA;
    }
    else
    {
      return usuario;
    }
  }


  public void setClave(String newClave)
  {
    clave = newClave;
  }

  public void setDatos(String newDatos)
  {
    datos = newDatos;
  }

  public void setFecha(Timestamp newFecha)
  {
    fecha = newFecha;
  }

  public void setId(String newId)
  {
    id = newId;
  }

  public void setOperacion(String newOperacion)
  {
    operacion = newOperacion;
  }

  public void setTabla(String newTabla)
  {
    tabla = newTabla;
  }

  public void setUsuario(String newUsuario)
  {
    usuario = newUsuario;
  }
}
