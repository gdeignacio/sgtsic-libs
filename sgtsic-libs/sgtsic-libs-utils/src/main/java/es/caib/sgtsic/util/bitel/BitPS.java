package es.caib.sgtsic.util.bitel;

import es.caib.sgtsic.data.DataAdapter;
import java.sql.*;

/**
 *
 * Implementa un PreparedStatement con algunas funcionalidades adicionales con las
 * que no cuenta la clase java.sql.PreparedStatement.
 *
 * En pariticular:
 * <ul>
 * <li>Permite tener informaci�n de debug precisa sobre cada par�metro que se asigna a una query.</li>
 * <li>Permite no tener que controlar el �ndice de los par�metros: basta con colocarlos ordenadamente.</li>
 * <li>Permite visualizar la query final a ejecutar con los par�metros ya sustituidos: es mejor para debug d econsultas complejas.</li>
 * </ul>
 *
 * Como contrapartida necesita un espacio adicional de memoria para guardar la query a ejecutar con los par�metros.
 * <br/><br/>
 * Ejempo de funcionamiento:
 * <pre>
 * StringBuffer sql = new StringBuffer();
 * sql.append("select columna1, columna2 from tabla where texto like ? and numero < ? and fecha > ?");
 * 
 * BitPS bps = new BitPS(con); // Se entiende que con es una instancia de Connection.
 * bps.setVerbose(true); // Provoca que se muestre por consola un mensaje cada vez que se asigna un par�metro.
 * 
 * bps.setString("txt");
 * bps.setInt(3);
 * pbs.setTimestamp(fecha);
 * 
 * ResultSet rs = bps.executeQuery();
 * (...)
 * // Si hubiera que usar BitEjb.closeConnection(Connection, PreparedStatement) se puede obtener el PreparedStatement real con bps.getPS();
 * bps.close(); 
 * </pre>
 *
 * @author Paco Ros
 * @version 20081128
 *
 */
public class BitPS extends DataAdapter {

    private PreparedStatement ps;
    String qryOrig;
    String qryDebug;
    int psCounter = 1;
    boolean verbose = false;

    /**
     * Provoca que se publiquen mensajes de debug por la consola con m�s frecuencia si p = true.
     *
     * @param b Par�metro que permite activar o desactivar el modo verbose.
     */
    public void setVerbose(boolean b) {
        this.verbose = b;
    }

    private void constructor(Connection con, String query) throws SQLException {
        ps = con.prepareStatement(query);
        qryOrig = new String(query);
        qryDebug = query.replaceAll("select", "\nselect").replaceAll("SELECT", "\nSELECT").replaceAll("from", "\nfrom").replaceAll("FROM", "\nFROM").replaceAll("where", "\nwhere").replaceAll("WHERE", "\nWHERE").replaceAll(" and ", "\n  and ").replaceAll(" AND ", "\n AND ").replaceAll(" or ", "\n   or ").replaceAll(" OR ", "\n OR ").replaceAll("order by", "\norder by").replaceAll("ORDER BY", "\nORDER BY").replaceAll("group by", "\ngroup by").replaceAll("GROUP BY", "\nGROUP BY");
    }

    /**
     * Consutruye un BitPS a partir de una conexi�n a BD y un StringBuffer. Internamente llama a con.prepareStatement()
     *
     * @param con Conexi�n abierta a la Base de Datos
     * @param sb Consulta SQL a ejecutar
     */
    public BitPS(Connection con, StringBuffer sb) throws SQLException {
        constructor(con, sb.toString());
    }

    /**
     * Consutruye un BitPS a partir de una conexi�n a BD y un StringBuffer. Internamente llama a con.prepareStatement()
     *
     * @param con Conexi�n abierta a la Base de Datos
     * @param query Consulta SQL a ejecutar
     */
    public BitPS(Connection con, String query) throws SQLException {
        constructor(con, query);
    }

    /**
     * Asigna un par�metro de tipo String al PreparedStatement
     *
     * @param str par�metro a asignar.
     */
    public void setString(String str) throws SQLException {
        ps.setString(psCounter++, str);
        qryReplaceFirst("?", "'" + str + "'");
        if (verbose) {
            BitLog.debug("[BitPS]: Ubicado par�metro de tipo String en la posici�n " + (psCounter - 1) + " con valor '" + str + "'");
        }
    }

    /**
     * Asigna un par�metro de tipo Timestamp al PreparedStatement
     *
     * @param t par�metro a asignar.
     */
    public void setTimestamp(Timestamp t) throws SQLException {
        ps.setTimestamp(psCounter++, t);
        qryReplaceFirst("?", "to_date('" + BitFecha.getFechaFormateadaEstandar(t) + "', 'dd/mm/yyyy')");
        if (verbose) {
            BitLog.debug("[BitPS]: Ubicado par�metro de tipo Timestamp en la posici�n " + (psCounter - 1) + " con valor '" + t + "'");
        }
    }

    /**
     * Asigna un par�metro de tipo Int al PreparedStatement
     *
     * @param i par�metro a asignar.
     */
    public void setInt(int i) throws SQLException {
        ps.setInt(psCounter++, i);
        qryReplaceFirst("?", String.valueOf(i));
        if (verbose) {
            BitLog.debug("[BitPS]: Ubicado par�metro de tipo int en la posici�n " + (psCounter - 1) + " con valor " + i + "");
        }
    }

    /**
     * Asigna un par�metro de tipo Double al PreparedStatement
     *
     * @param d par�metro a asignar.
     */
    public void setDouble(double d) throws SQLException {
        ps.setDouble(psCounter++, d);
        qryReplaceFirst("?", String.valueOf(d));
        if (verbose) {
            BitLog.debug("[BitPS]: Ubicado par�metro de tipo double en la posici�n " + (psCounter - 1) + " con valor " + d + "");
        }
    }

    /**
     * Asigna un par�metro de tipo Object al PreparedStatement
     * Las clases aceptadas son las siguientes
     *
     * String
     * Integer
     * Double
     * Float
     * java.sql.Date, java.sql.Timestamp java.util.Date
     *
     * @param o par�metro a asignar.
     */
    public void setObject(Object o) throws SQLException {

        if( o instanceof String){
            this.setString((String)o);
        }else if (o instanceof Integer){
            this.setInt(((Integer)o).intValue());
        }else if (o instanceof Double){
            this.setDouble(((Double)o).doubleValue());
        }else if (o instanceof java.util.Date){
            Timestamp t = new Timestamp(((java.util.Date)o).getTime());
            this.setTimestamp(t);
        }else{
            throw new SQLException("Parametro err�neo");
        }
    }

    /**
     * Cierra el objecto PreparedStatement subyacente.
     *
     * @throws SQLException si no es posible cerrarlo.
     */
    public void close() throws SQLException {
        ps.close();
    }

    /**
     * Recuera el sql original con el que se construy� el objeto BitPS
     *
     * @return SQL original
     */
    public String getOriginalQuery() {
        return qryOrig;
    }

    /**
     * Recupera el SQL con los par�metros modificados a punto para ser lanzado.
     *
     * @return Sentencia SQL parametrizada con unos valores conocidos.
     */
    public String getParametrizedQuery() {
        return qryDebug;
    }

    /**
     * Recupera el objeto PreparedStatement subyacente.
     *
     * @return El PreparedStatement real que se usa.
     * @see java.sql.PreparedStatement
     */
    public PreparedStatement getPS() {
        return ps;
    }

    /**
     * Ejecuta la consulta mediante la ejecucuci�n de ps.executeQuery()
     *
     * @return Resultados obtenidos
     * @see java.sql.ResultSet
     * @see java.sql.PreparedStatement
     */
    public ResultSet executeQuery() throws SQLException {
        return ps.executeQuery();
    }

    /**
     * Ejecuta el sql de tipo dml preparado.
     *
     * @return n�mero de filas afectadas por la actualizaci�n
     * @throws SQLException Si se produce alg�n error en el proceso de ejecuci�n.
     * @see java.sql.PreparedStatement
     */
    public int executeUpdate() throws SQLException {
        return ps.executeUpdate();
    }

    /**
     * Resetea el contador de par�metros para ejecutar por segunda vez un mismo PS con diferentes par�metros.
     */
    public void resetPsCounter() {
        psCounter = 1;
        qryDebug = new String(qryOrig);
    }

    /**
     * Vuelve a abrir el objeto PreparedStatement subyacente contra la conexi�n a base de datos indicada
     * cerrando el objeto anterior si �ste estuviera abierto.
     *
     * @throws SQLExeption si no es posbile preparar el PreparedStatement con el sql indicado en el constructor.
     * @see java.sql.PreparedStatement
     */
    public void reopen(Connection con) throws SQLException {
        if (ps != null) {
            ps.close();
            ps = con.prepareStatement(qryOrig);
            resetPsCounter();
        }
    }

    private void qryReplaceFirst(String s1, String s2) {
        if (qryDebug != null) {
            StringBuffer sb = new StringBuffer();

            int i = qryDebug.indexOf(s1);

            if (i >= 0) {
                sb.append(qryDebug.substring(0, i));
                sb.append(s2);
                if (i < qryDebug.length() - 1) {
                    sb.append(qryDebug.substring(i + 1));
                }

                qryDebug = sb.toString();
            }
        }
    }
}