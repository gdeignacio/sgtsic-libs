package es.caib.sgtsic.util.bitel;

import es.caib.sgtsic.util.bitel.BitLog;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.*;
import java.sql.*;
import javax.naming.*;
import javax.sql.*;

import es.caib.sgtsic.data.*;
import es.caib.sgtsic.error.bitel.*;

/**
* Clase de utilidades para los EJB's
*
* @author JBE,ELU
*/
public class BitEjb
{
  /**
   * Constante que indica que el nombre JNDI del data source: <BR>
   * java:comp/env/jdbc/bitelDS
   */
  private static String DATASOURCE = "java:comp/env/jdbc/bitelDS";
  private static Hashtable poolConexiones;
  private static boolean ctrlPool;
  private static DataSource ds;
  private static boolean cargadoDataSource = false;


  public static void cargaDataSource()
  {
    if (!cargadoDataSource)
    {
      try
      {
        InitialContext ic = new InitialContext();
        ds = (DataSource) ic.lookup(DATASOURCE);
        cargadoDataSource = true;
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }

  /**
   * M�todo que accede a la tabla de dominios y obtiene una lista de valores del
   * dominio indicado bajo el orden indicado.
   *
   * @param dominio Subclase de DominioData con la informaci�n del dominio
   * @return Collection de DominioData donde cada instancia es del mismo tipo que "dominio"
   * @throws ErrorBaseDatosSelect,ErrorBaseDatosConexion
   */
  public static Collection getDominio(DominioData dominio) throws ErrorReflexion, ErrorBaseDatosSelect, ErrorBaseDatosConexion
  {
    PreparedStatement ps = null;
    ResultSet rs = null;
    Connection con = null;

    ArrayList v = new ArrayList();
    StringBuffer qry = new StringBuffer();

    // Contruimos la sentenia de BD
    qry = new StringBuffer("SELECT dom_codigo, dom_valor, dom_domini FROM ");
    qry.append(dominio.getNombreTabla());
    qry.append(" WHERE dom_domini = ? ORDER BY ");
    qry.append(dominio.getOrden());

    try
    {
      con = BitEjb.getConnection();
      ps = con.prepareStatement(qry.toString());
      ps.setString(1, dominio.getDominio());

      // Ejecutamos la sentencia de BD
      ps.executeQuery();
      rs = ps.getResultSet();
      DominioData dom = null;
      Class clase = dominio.getClass();
      String orden = dominio.getOrden();
      while (rs.next())
      {
        dom = (DominioData) clase.newInstance();
        dom.setCodigoLOV(rs.getString("dom_codigo"));
        dom.setValorLOV(rs.getString("dom_valor"));
        dom.setOrden(orden);
        v.add(dom);
      }
    }
    catch (SQLException e)
    {
      throw new ErrorBaseDatosSelect(dominio.getNombreTabla(), dominio.getDominio(), qry.toString(), e);       
    }
    catch(InstantiationException e) 
    {
      throw new ErrorReflexion(dominio.getClass().getName(), e);
    }
    catch(IllegalAccessException e) 
    {
      throw new ErrorReflexion(dominio.getClass().getName(), e);
    }                                                                 
    finally
    {
      BitEjb.closeConnection(con, ps);
    }
    return v;
  }


  /**
   * Recupera el nombre de un usuario que est� ejecutando un m�todo de un EJB.
   *
   * Es muy importante que la variable ctx que se pasa por par�metro haya sido
   * asignada en el m�todo setSessionContext. Ejemplo de implementaci�n en un
   * sessionbean:
   *
   *  private SessionContext ctx;
   *  public void setSessionContext(SessionContext ctx){
   *    this.ctx = ctx;
   *  }
   *
   * @param ctx Contexto en que se est� ejecutando el m�todo.
   * @return Login del usuario que est� ejecutando el m�todo o
   *         "[desconegut]" si el ususario es an�nimo
   *
   **/
  public static String getUsuario(SessionContext ctx)
  {
    java.security.Principal principal = ctx.getCallerPrincipal();
    String usuari = "[desconegut]";
    if (principal != null)
      usuari = principal.getName();

    return usuari;
  }

  /**
    * Metodo para obtener el valor de una variable de entorno
    *
    * @param nombre representa el nombre de la variable de entorno
    * @return Valor de la variable
    */
  public static String getVariableEntorno(String nombre) throws ErrorEJBVariableEntorno
  {
    InitialContext ic = null;
    try
    {
      ic = new InitialContext();
      return (String) ic.lookup("java:comp/env/" + nombre);
    }
    catch (NamingException ex)
    {
      throw new ErrorEJBVariableEntorno(nombre, ex);
    }finally{
       if (ic != null){
            try {
                ic.close();
            } catch (NamingException ex) {
                BitLog.debug("No se pudo cerrar el objeto InitialContext:"+ex.toString());
            }
       }
    }
  }

  /**
    * Metodo para obtener la conexi�n a BD para las consultas en JDBC
    *
    * @return jdbc connection to database
    */
  public static Connection getConnection() throws ErrorBaseDatosConexion
  {
    try
    {
      BitEjb.cargaDataSource();
      Connection con = ds.getConnection();
      addConexion(con);
      return con;
    }
    catch (SQLException ex)
    {
      throw new ErrorBaseDatosConexion(DATASOURCE, ex);
    }
  }

  /**
    * Metodo para cerrar la conexi�n a BD
    * @param con Conexion a Base de datos
    */
  public static void closeConnection(Connection con)
  {
    try
    {
      if (con != null)
      {
        con.close();
        removeConexion(con);
      }
    }
    catch (SQLException e)
    {
      BitLog.debug("No se pudo cerrar la conexi�n: " + con.hashCode());
    }
  }

  /**
    * Metodo para cerrar un preparedStatement a BD
    * @param ps Prepared Statement
    */
  public static void closePreparedStatement(PreparedStatement ps)
  {
    try
    {
      if (ps != null)
        ps.close();
    }
    catch (SQLException e)
    {
      BitLog.debug("No se pudo cerrar el preparedStatement");
    }
  }

  /**
    * Metodo para cerrar la conexi�n a BD y un preparedStatement
    * @param con Conexion a Base de datos
    * @param ps Prepared Statement
    */
  public static void closeConnection(Connection con, PreparedStatement ps)
  {
    closePreparedStatement(ps);
    closeConnection(con);
  }

  /**
   * Metodo para cerrar un conjunto de preparedStatements. 
   * @param listPs Lista de preparedStatements
   */
  public static void closePreparedStatements(PreparedStatement... listPs)
  {
    for (PreparedStatement ps: listPs){
        closePreparedStatement(ps);
    }
        
  }

  /**
    * Metodo para obtener la interficie local de un EJB
    *
    * @param nombre JNDI del EJB
    * @return EJBHome
    */
  public static EJBHome getHome(String nombre) throws ErrorEJBInterfazHome
  {
    InitialContext ic = null;
    try
    {
      //Devuelve el EJB Home al cual tendremos que hacer el cast  
      ic = new InitialContext();
      return (EJBHome) ic.lookup("java:comp/env/ejb/" + nombre);
    } 
    catch (NamingException e) 
    {
      throw new ErrorEJBInterfazHome(nombre, e);
    }finally{
       if (ic != null){
            try {
                ic.close();
            } catch (NamingException ex) {
                BitLog.debug("No se pudo cerrar el objeto InitialContext:"+ex.toString());
            }
       }
    }
  }

  /**
   * Modifica la sentencia de consulta, a�adiendo las instrucciones necesarias para
   * realizar la acotaci�n tal y como indican los par�metros. La consulta, si es
   * modificada, lee siempre un elemento m�s de los solicitados para poder asi
   * controlar si existen elementos posteriores sin recuperar.
   *
   * @param qryOld    Especifica la consulta original de la base de datos.
   * @param maxResultadosTotal    Especifica el n�mero m�ximo de resultados del listado total. El valor neutro es el cero.
   * @param maxResultadosPagina    Especifica el n�mero m�ximo de resultados de una p�gina del listado. El valor neutro es el cero.
   * @param elementoInicio    Especifica el elemento inicial. El valor neutro es el cero.
   * @return    Devuelve la consulta modificada convenientemente.
   */
  public static final StringBuffer acotarConsulta(StringBuffer qryOld, int maxResultadosTotal, int maxResultadosPagina, 
                                                  int elementoInicio)
  {
    // Definimos los objetos principales del m�todo
    StringBuffer qry;

    // Si hay que modificar la consulta, a�adimos lo necesario para realizar la/s acotacione/s
    if (elementoInicio != 0 || maxResultadosTotal != 0 || (elementoInicio != 0 && maxResultadosPagina != 0))
    {
      // Obtenemos la parte del texto que indican los campos que se recuperan
      int selIni = 7;
      int selFin = qryOld.indexOf(" from ");

      // Reservamos la memoria para la nueva sentencia
      qry = new StringBuffer(qryOld.length() + (selFin - selIni) + 100);

      // Construimos la nueva sentencia
      qry.append("select * from ( select rownum as ROW_NUM, ");
      qry.append(qryOld.substring(selIni, selFin));
      qry.append(" from ( ");
      qry.append(qryOld);
      qry.append(" )) ");
      if (elementoInicio != 0)
      {
        qry.append("where ROW_NUM >= " + elementoInicio);
        if (maxResultadosPagina != 0)
          qry.append(" and ROW_NUM <= " + (elementoInicio + maxResultadosPagina));
        else if (maxResultadosTotal != 0)
          qry.append(" and ROW_NUM <= " + (elementoInicio + maxResultadosTotal));
      }
      else
      {
        if (maxResultadosTotal != 0)
          qry.append("where ROW_NUM <= " + (maxResultadosTotal + 1));
      }
    }
    else
    {
      // Si no hay que modificar la consulta, devolvemos la original
      qry = qryOld;
    }

    // Devolvemos la consulta
    return qry;
  }


  /**
   * A�ade una conexion en la hashtable de conexiones
   * @param con Conexion a a�adir en la hashtable
   */
  private static void addConexion(Connection con)
  {
    if (getCtrlPool() && poolConexiones != null) //Si tenemos que controlar el pool de conexiones
    {
      Exception e = new Exception();
      poolConexiones.put("" + con.hashCode(), new CtrlConData(con, e));
    }
  }

  /**
   * Metodo para obtener el identificador del elemento a insertar en la BD
   * creando el metodo la conexi�n a la base de datos y cerr�ndola
   * @param seq nombre de la secuencia
   * @return identificador
   */
  public static String getSecuencia(String seq) throws ErrorBaseDatosSecuencia, ErrorBaseDatosConexion
  {
    String sec = BitEjb.getSecuencia(null, seq);
    return sec;
  }
  
  /**
   * Metodo para obtener el identificador del elemento a insertar en la BD
   * creando el metodo la conexi�n a la base de datos y cerr�ndola
   * @param seq nombre de la secuencia
   * @return identificador
   */
  public static int getSecuenciaInt(String seq) throws ErrorBaseDatosSecuencia, ErrorBaseDatosConexion
  {
    int sec = BitEjb.getSecuenciaInt(null, seq);
    return sec;
  }

  /**
    * Metodo para obtener el identificador del elemento a insertar en la BD
    * reutilizando la conexi�n pasada por par�metro. Es responsabilidad del m�todo
    * llamante cerrar la conexi�n. En el caso de que la conexi�n pasada por par�metro
    * sea null, el m�todo crea una conexi�n interna y la cierra.
    * @param seq nombre de la secuencia
    * @param conPri conexion de BD. El m�todo no la cierra
    * @return identificador
    */
  public static String getSecuencia(Connection conPri, String seq) throws ErrorBaseDatosSecuencia, ErrorBaseDatosConexion {
    PreparedStatement ps = null;
    ResultSet rs = null;
    String val = "0";
    Connection con = null;
    StringBuffer qry = new StringBuffer();
    // Construimos la sentencia de BD para el secuenciador 
    qry = new StringBuffer("select ");
    qry.append(seq);
    qry.append(".nextval from dual");
    try
    {
      if (conPri == null)
        con = BitEjb.getConnection();
      else
        con = conPri;

      ps = con.prepareStatement(qry.toString());
      ps.executeQuery();
      rs = ps.getResultSet();
      if (rs.next())
      {
        val = rs.getString(1);
      }
      return val;
    }
    catch (SQLException ex)
    {
      throw new ErrorBaseDatosSecuencia(seq, ex);
    }
    finally
    {
      BitEjb.closePreparedStatement(ps);
      if (conPri == null)
        BitEjb.closeConnection(con);
    }
  }
  
  /**
    * Metodo para obtener el identificador del elemento a insertar en la BD
    * reutilizando la conexi�n pasada por par�metro. Es responsabilidad del m�todo
    * llamante cerrar la conexi�n. En el caso de que la conexi�n pasada por par�metro
    * sea null, el m�todo crea una conexi�n interna y la cierra.
    * @param seq nombre de la secuencia
    * @param conPri conexion de BD. El m�todo no la cierra
    * @return identificador
    */
  public static int getSecuenciaInt(Connection conPri, String seq) throws ErrorBaseDatosSecuencia, ErrorBaseDatosConexion
  {
    PreparedStatement ps = null;
    ResultSet rs = null;
    int val = 0;
    Connection con = null;
    StringBuffer qry = new StringBuffer();
    // Construimos la sentencia de BD para el secuenciador 
    qry = new StringBuffer("select ");
    qry.append(seq);
    qry.append(".nextval from dual");
    try
    {
      if (conPri == null)
        con = BitEjb.getConnection();
      else
        con = conPri;
      
      ps = con.prepareStatement(qry.toString());
      ps.executeQuery();
      rs = ps.getResultSet();
      if (rs.next())
      {
        val = rs.getInt(1);
      }
      return val;
    }
    catch (SQLException ex)
    {
      throw new ErrorBaseDatosSecuencia(seq, ex);
    }
    finally
    {
      BitEjb.closePreparedStatement(ps);
      if (conPri == null)
        BitEjb.closeConnection(con);
    }
  }

  /**
   * Elimina una conexion de la hashtable de conexiones
   * @param con Conexion a eliminar de la hashtable
   */
  private static void removeConexion(Connection con)
  {
    if (getCtrlPool() && poolConexiones != null) //Si tenemos que controlar el pool de conexiones
    {
      poolConexiones.remove("" + con.hashCode());
    }
  }

  /**
   * Crea el pool de control de conexiones con un tama�o especificado por parametro
   * @param size tama�o del pool
   */
  public static void setPoolConexiones(int size)
  {
    poolConexiones = new Hashtable(size);
    BitLog.debug("Establecemos el pool de conexiones con el tamanyo:" + size);
  }

  /**
   * Establece el valor para saber si controlamos o no el pool de conexiones
   * @param newVal true o false
   */
  public static void setCtrlPool(boolean newVal)
  {
    ctrlPool = newVal;
  }

  /**
   * Recupera el valor del control de conexiones
   * @return true o false
   */
  public static boolean getCtrlPool()
  {
    return ctrlPool;
  }

  /**
   * Retorna el pool de conexiones
   * @return Hashtable
   */
  public static Hashtable getPoolConexiones()
  {
    return poolConexiones;
  }
  
    /**
     * Realiza un lookup de un EJB retornando la interface remota correspondiente al bean solicitado. Es responsabilidad del
     * m�todo que llama a este m�todo el realiza un cast apropiado al objeto invocado. Un ejemplo de invocaci�n de este m�todo ser�a
     * BolsaPlazas ejb = (BolsaPlazas)EJBUtils.lookup(BolsaPlazasHome.class, BolsaPlazas.class, "BolsaPlazasBean");
     * @param homeClass representa un objeto Class que identifica al home del EJB
     * @param remoteClass representa un objeto Class que identifica al remote del EJB
     * @param beanName representa el nombre del EJB que se define en el ejb-jar.xml
     * @return el m�todo retorna la interface remota que identifica el EJB solicitado
     */
    public static Object lookup(Class homeClass, Class remoteClass,
                                String beanName) {
        InitialContext ic = null;
        try {
            ic = new InitialContext();
            Object remote = ic.lookup("java:comp/env/" + beanName);
            Object narrowed =
                javax.rmi.PortableRemoteObject.narrow(remote, homeClass);
            Object invokedRemote =
                homeClass.getMethod("create", new Class[0]).invoke(narrowed,
                                                                   (Object[])null);

            return remoteClass.cast(invokedRemote);
        } catch (Exception e) {
            BitLog.fatal("Error localizando el EJB " + beanName);
            throw new RuntimeException(e);
        }finally{
            if (ic != null){
                try {
                    ic.close();
                }catch (NamingException ex) {
                   BitLog.debug("No se puedo cerrar el objeto InitialContext:"+ex.toString());
                }
            }
        }
    }


}
