package es.caib.sgtsic.util.bitel;

import es.caib.sgtsic.data.MensajeLogData;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.util.*;

import es.caib.sgtsic.error.bitel.*;

import java.sql.SQLException;


public class BitLogger
{

  /**
   * Inserta un mensaje de LOG en el sistema para la inserci�n de informaci�n en una tabla
   *
   * @param usuario Usuario que ha ejecutado la inserci�n
   * @param tabla Tabla de la base de datos donde se ha efectuado la inserci�n
   * @param clave c�digo o clave primaria del elemento insertado
   * @param datos Informaci�n que se ha insertado en la base de datos.
   *
   * @throws ErrorBaseDatosConexion, ErrorXMLLectura, ErrorBaseDatosInsert
   */
  public static void logInsert(String usuario, String tabla, String clave, String datos) throws ErrorBaseDatosConexion, ErrorXMLLectura, ErrorBaseDatosInsert
  {
    Connection con = BitEjb.getConnection();
    logInsert(con, usuario, tabla, clave, datos);
    try
    {
      BitEjb.closeConnection(con);
    }
    catch (Exception e)
    {
    }
  }

  /**
   * Inserta un mensaje de LOG en el sistema para la inserci�n de informaci�n en una tabla
   *
   * @param con Conexi�n con la base de datos que contiene la tabla de LOG
   * @param usuario Usuario que ha ejecutado la inserci�n
   * @param tabla Tabla de la base de datos donde se ha efectuado la inserci�n
   * @param clave c�digo o clave primaria del elemento insertado
   * @param datos Informaci�n que se ha insertado en la base de datos.
   *
   * @throws ErrorXMLLectura, ErrorBaseDatosInsert
   */
  public static void logInsert(Connection con, String usuario, String tabla, String clave, String datos) throws ErrorXMLLectura, ErrorBaseDatosInsert
  {
    if (BitLoggerConfig.doLogInsert(tabla))
    {
      MensajeLogData data = new MensajeLogData();

      data.setUsuario(usuario);
      data.setFecha(new Timestamp(System.currentTimeMillis()));
      data.setOperacion(MensajeLogData.INSERT);
      data.setTabla(tabla);
      data.setClave(clave);
      data.setDatos(datos);

      log(data, con);
    }
  }

  /**
   * Inserta un mensaje de LOG en el sistema para la actualizaci�n de informaci�n de una tabla
   *
   * @param usuario Usuario que ha ejecutado la actualizaci�n
   * @param tabla Tabla de la base de datos donde se ha efectuado la actualizaci�n
   * @param clave c�digo o clave primaria del elemento modificado
   * @param datos Nuevos datos del elemento modificado.
   *
   * @throws ErrorBaseDatosConexion, ErrorXMLLectura, ErrorBaseDatosInsert
   */
  public static void logUpdate(String usuario, String tabla, String clave, String datos) throws ErrorBaseDatosConexion, ErrorXMLLectura, ErrorBaseDatosInsert
  {
    Connection con = BitEjb.getConnection();
    logUpdate(con, usuario, tabla, clave, datos);
    try
    {
      BitEjb.closeConnection(con);
    }
    catch (Exception e)
    {
    }
  }

  /**
   * Inserta un mensaje de LOG en el sistema para la actualizaci�n de informaci�n de una tabla
   *
   * @param con Conexi�n con la base de datos que contiene la tabla de LOG
   * @param usuario Usuario que ha ejecutado la actualizaci�n
   * @param tabla Tabla de la base de datos donde se ha efectuado la actualizaci�n
   * @param clave c�digo o clave primaria del elemento modificado
   * @param datos Nuevos datos del elemento modificado.
   *
   * @throws ErrorXMLLectura, ErrorBaseDatosInsert
   */
  public static void logUpdate(Connection con, String usuario, String tabla, String clave, String datos) throws ErrorXMLLectura, ErrorBaseDatosInsert
  {
    if (BitLoggerConfig.doLogUpdate(tabla))
    {
      MensajeLogData data = new MensajeLogData();

      data.setUsuario(usuario);
      data.setFecha(new Timestamp(System.currentTimeMillis()));
      data.setOperacion(MensajeLogData.UPDATE);
      data.setTabla(tabla);
      data.setClave(clave);
      data.setDatos(datos);

      log(data, con);
    }
  }


  /**
   * Inserta un mensaje de LOG en el sistema para la eliminaci�n de informaci�n de una tabla
   *
   * @param usuario Usuario que elimina la informacion
   * @param tabla Tabla de la base de datos de donde se elimina la informaci�n
   * @param clave c�digo o clave primaria del elemento eliminado
   *
   * @throws ErrorBaseDatosConexion, ErrorXMLLectura, ErrorBaseDatosInsert
   */
  public static void logDelete(String usuario, String tabla, String clave) throws ErrorBaseDatosConexion, ErrorXMLLectura, ErrorBaseDatosInsert
  {
    Connection con = BitEjb.getConnection();
    logDelete(con, usuario, tabla, clave);
    try
    {
      BitEjb.closeConnection(con);
    }
    catch (Exception e)
    {
    }
  }

  /**
   * Inserta un mensaje de LOG en el sistema para la eliminaci�n de informaci�n de una tabla
   *
   * @param con Conexion a la base de datos donde se encuentra la tabla de LOG.
   * @param usuario Usuario que elimina la informacion
   * @param tabla Tabla de la base de datos de donde se elimina la informaci�n
   * @param clave c�digo o clave primaria del elemento eliminado
   *
   * @throws ErrorXMLLectura, ErrorBaseDatosInsert
   */
  public static void logDelete(Connection con, String usuario, String tabla, String clave) throws ErrorXMLLectura, ErrorBaseDatosInsert
  {
    if (BitLoggerConfig.doLogDelete(tabla))
    {
      MensajeLogData data = new MensajeLogData();

      data.setUsuario(usuario);
      data.setFecha(new Timestamp(System.currentTimeMillis()));
      data.setOperacion(MensajeLogData.DELETE);
      data.setTabla(tabla);
      data.setClave(clave);

      log(data, con);
    }
  }

  /**
   * Inserta un mensaje de LOG en el sistema para notificar que un usuario ha consultado
   * la informaci�n de una tabla.
   *
   * @param usuario Usuario que consulta la informaci�n
   * @param tabla Tabla de la base de datos de donde se consulta la informaci�n
   * @param clave c�digo o clave primaria del elemento consultado
   * @param datos informaci�n que ha obtenido el usuario en su consulta
   *
   * @throws ErrorBaseDatosConexion, ErrorXMLLectura, ErrorBaseDatosInsert
   */
  public static void logSelect(String usuario, String tabla, String clave, String datos) throws ErrorBaseDatosConexion, ErrorXMLLectura, ErrorBaseDatosInsert
  {
    Connection con = BitEjb.getConnection();
    logSelect(con, usuario, tabla, clave, datos);
    try
    {
      BitEjb.closeConnection(con);
    }
    catch (Exception e)
    {
    }
  }

  /**
   * Inserta un mensaje de LOG en el sistema para notificar que un usuario ha consultado
   * la informaci�n de una tabla.
   *
   * @param con Conexi�n a la base de datos que contiene la tabla de LOG
   * @param usuario Usuario que consulta la informaci�n
   * @param tabla Tabla de la base de datos de donde se consulta la informaci�n
   * @param clave c�digo o clave primaria del elemento consultado
   * @param datos informaci�n que ha obtenido el usuario en su consulta
   *
   * @throws ErrorXMLLectura, ErrorBaseDatosInsert
   */
  public static void logSelect(Connection con, String usuario, String tabla, String clave, String datos) throws ErrorXMLLectura, ErrorBaseDatosInsert
  {
    if (BitLoggerConfig.doLogSelect(tabla))
    {
      MensajeLogData data = new MensajeLogData();

      data.setUsuario(usuario);
      data.setFecha(new Timestamp(System.currentTimeMillis()));
      data.setOperacion(MensajeLogData.SELECT);
      data.setTabla(tabla);
      data.setClave(clave);
      data.setDatos(datos);

      log(data, con);
    }
  }

  /**
   * Inserta un mensaje de LOG en el sistema para notificar que un usuario ha consultado
   * una lista de elementos de una tabla.
   *
   * @param usuario Usuario que consulta la informaci�n
   * @param tabla Tabla de la base de datos de donde se consulta la informaci�n
   * @param datos informaci�n que ha obtenido el usuario en su consulta
   *
   * @throws ErrorBaseDatosConexion, ErrorXMLLectura, ErrorBaseDatosInsert
   */
  public static void logSelectLista(String usuario, String tabla, String datos) throws ErrorBaseDatosConexion, ErrorXMLLectura, ErrorBaseDatosInsert
  {
    Connection con = BitEjb.getConnection();
    logSelectLista(con, usuario, tabla, datos);
    try
    {
      BitEjb.closeConnection(con);
    }
    catch (Exception e)
    {
    }
  }

  /**
   * Inserta un mensaje de LOG en el sistema para notificar que un usuario ha consultado
   * una lista de elementos de una tabla.
   *
   * @param con Conexiona a la base de datos que contiene la tabla de LOG.
   * @param usuario Usuario que consulta la informaci�n
   * @param tabla Tabla de la base de datos de donde se consulta la informaci�n
   * @param datos informaci�n que ha obtenido el usuario en su consulta
   *
   * @throws ErrorXMLLectura, ErrorBaseDatosInsert
   */
  public static void logSelectLista(Connection con, String usuario, String tabla, String datos) throws ErrorXMLLectura, ErrorBaseDatosInsert
  {
    if (BitLoggerConfig.doLogSelectLista(tabla))
    {
      MensajeLogData data = new MensajeLogData();

      data.setUsuario(usuario);
      data.setFecha(new Timestamp(System.currentTimeMillis()));
      data.setOperacion(MensajeLogData.SELECT_LISTA);
      data.setTabla(tabla);

      data.setDatos(datos);

      log(data, con);
    }
  }

  private static void log(MensajeLogData data, Connection con) throws ErrorXMLLectura, ErrorBaseDatosInsert
  {
    // Declaramos variables
    List l = null;

    // Separamos el Mensaje de Log en mensajes de un tama�o adecuado
    l = splitLog(data);

    // Hacemos el Log de la lista de mensajes
    log(l, con);

    // Anulamos las variables para mejor gesti�n del GC
    l = null;
  }

  /**
   * Toma un Mensaje de LOG y, si sus datos superan los 2000 caracteres, lo divide en varios
   *
   * @param log Datos del mensaje de LOG
   * @return vector con los mensajes de LOG necesarios.
   */
  private static List splitLog(MensajeLogData log)
  {
    // Declaramos todas las variables al principio para optimizar la gesti�n de memoria de la VM.
    // Usamos una linked list que es la m�s r�pida en recorridos secuenciales.
    LinkedList ll = new LinkedList();
    MensajeLogData aux = null;
    int i, j;

    // Si no es necesario hacer el "split" devolvemos un solo elemento en la lista.
    if ((log.getDatos() == null) || (log.getDatos().length() < 2000))
    {
      ll.add(log);
      return ll;
    }

    for (i = 0; i < log.getDatos().length(); i += 2000)
    {
      // Hemos implementado clone() en la Data excepcionalmente para que esto sea lo m�s r�pido posible.
      aux = (MensajeLogData) log.clone();

      // Separamos el mensaje de LOG en mensajes de, como m�ximo, 2000 caracteres de descripcion.
      j = 2000;
      if (log.getDatos().substring(i).length() < 2000)
        j = log.getDatos().substring(i).length();
      aux.setDatos(log.getDatos().substring(i, i + j));

      // Metemos la data en la lsita.
      ll.add(aux);
    }

    // Anulamos para ayudar al GC
    aux = null;

    return ll;
  }

  /**
   * Inserta un mensaje de Log en la BD a partir de una Conexi�n a base de datos existente.
   *
   * @param l Lista de MensajeLogData con los mensajes a insertar en la BD.
   * @param con Conexi�n a la base de datos que contiene la tabla de LOG
   * @throws ErrorXMLLectura, ErrorBaseDatosInsert
   */
  private static void log(List l, Connection con) throws ErrorXMLLectura, ErrorBaseDatosInsert
  {
    // Declaramos e inicializamos todas las variables.
    MensajeLogData data = null;
    PreparedStatement ps = null;
    StringBuffer qry = new StringBuffer();
    String id = null;
    Iterator it = null;

    try
    {
      // Construimos el insert
      qry.append("insert into ");
      qry.append(BitLoggerConfig.getAcronimoAplicacion());
      qry.append("_LOG (LOG_CODIGO, LOG_USUARI, LOG_FECHA, LOG_OPERAC, LOG_TABLA, LOG_CLAVE, LOG_INFO) ");
      qry.append("values (");
      qry.append(BitLoggerConfig.getAcronimoAplicacion());
      qry.append("_SEQLOG.NEXTVAL, ?, ?, ?, ?, ?, ?)");

      // Preparamos el insert
      ps = con.prepareStatement(qry.toString());

      // Recuperamos un iterador para todos los mensajes
      it = l.iterator();

      // Ejecutamos el insert para todos los elmentos del iterador.
      while (it.hasNext())
      {
        // Recuperamos la clase Data
        data = (MensajeLogData) it.next();

        // Sustituimos los par�metros por los valores de la clase Data.
        ps.setString(1, data.getUsuario());
        ps.setTimestamp(2, data.getFecha());
        ps.setString(3, data.getOperacion());
        ps.setString(4, data.getTabla());
        ps.setString(5, data.getClave());
        ps.setString(6, data.getDatos());

        // Ejecutamos.
        ps.executeUpdate();
      }

      ps.close();
    }
    catch (SQLException sqle)
    {
      throw new ErrorBaseDatosInsert("el log", data, qry.toString(), sqle);
    }
    finally
    {
      // Anulamos las variables utilizadas para mejor gesti�n del GC
      ps = null;
      qry = null;
      id = null;
      it = null;
    }
  }
}
