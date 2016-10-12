package es.caib.sgtsic.data;

import es.caib.sgtsic.util.bitel.BitFecha;

//~--- JDK imports ------------------------------------------------------------

import java.sql.Connection;

public class CtrlConData
{
  Connection conexion;
  Exception excepcion;
  String fecha;

  public CtrlConData(Connection con, Exception e)
  {
    this.conexion = con;
    this.excepcion = e;
    fecha = BitFecha.getFechaActualString() + " " + BitFecha.getHoraActualString();
  }

  public Connection getConexion()
  {
    return conexion;
  }

  public Exception getExcepcion()
  {
    return excepcion;
  }

  public String getFecha()
  {
    return fecha;
  }

  public void setConexion(Connection conexion)
  {
    this.conexion = conexion;
  }

  public void setExcepcion(Exception excepcion)
  {
    this.excepcion = excepcion;
  }

  public void setFecha(String fecha)
  {
    this.fecha = fecha;
  }
}
