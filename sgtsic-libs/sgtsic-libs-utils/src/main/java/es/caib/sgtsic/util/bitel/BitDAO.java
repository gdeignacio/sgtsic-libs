/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.caib.sgtsic.util.bitel;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;

//import oracle.sql.BLOB;
//import oracle.sql.CLOB;

/**
 * Contiene metodos de escritura de BLOBs y CLOBs y metodos de lectura de CLOBs.
 * Para leer BLOBs es suficiente con hacer rs.getBlob("...").getBytes(1, blob.length()).
 * Los m�todos tienen en cuenta si se ejecutan en un servidor Oracle, Glassfish o JBoss
 * para usar las clases apropiadas del driver.
 * @author manieto, abastos, eluengo
 * @version 1.0 12/01/2010
 */
public class BitDAO {
    
    
  /**
   * Escribe un BLOB en base de datos.
   *
   * @param  rs           el resultset correspondiente.
   * @param  nombreCampo  el nombre del campo en base de datos.
   * @param  documento    el documento que se desea almacenar.
   * @throws SQLException si se produce un error de base de datos.
   * @throws IOException  si se produce un error en la lectura del documento.
   */
    
  /*  
  public static void escribirBLOB(ResultSet rs, String nombreCampo, byte[] documento) throws SQLException, IOException
  {
    BitDAO.escribirBLOB(rs, nombreCampo, documento, (documento!=null)?documento.length:0);
  }*/


  /**
   * Escribe un BLOB en base de datos.
   *
   * @param  rs           el resultset correspondiente.
   * @param  nombreCampo  el nombre del campo en base de datos.
   * @param  documento    el documento que se desea almacenar.
   * @param  tamanyo      el tamanyo de los datos que se desea almacenar (si es cero, se coge el tama�o del documento).
   * @throws SQLException si se produce un error de base de datos.
   * @throws IOException  si se produce un error en la lectura del documento.
   */
    
   /* 
  public static void escribirBLOB(ResultSet rs, String nombreCampo, byte[] documento, int tamanyo) throws SQLException, IOException
  {
    // Declaramos los objetos principales del m�todo
    BLOB blob = null;
    OutputStream os = null;

    try {
      // PARTE ESPECIFICA QUE SOLO FUNCIONA CON OC4j
      if (BitSystem.isOracleContainersForJava()) {
        blob = ((oracle.jdbc.OracleResultSet)rs).getBLOB(nombreCampo);
      }
      // PARTE ESPECIFICA QUE SOLO FUNCIONA CON JBoss (incluir la librer�a /$JBOSS_HOME$/server/default/lib/jboss-common-jdbc-wrapper.jar)
      else if (BitSystem.isJBossServer()) {
        org.jboss.resource.adapter.jdbc.WrappedResultSet wrs = (org.jboss.resource.adapter.jdbc.WrappedResultSet) rs;
        oracle.jdbc.OracleResultSet ors = (oracle.jdbc.OracleResultSet) wrs.getUnderlyingResultSet();
        blob = ors.getBLOB(nombreCampo);
      // PARTE ESPECIFICA QUE SOLO FUNCIONA CON Glassfishv2
      } else if(BitSystem.isGlassfishV2()) {
        blob = ((oracle.jdbc.OracleResultSet)rs).getBLOB(nombreCampo);
      }

      // Obtenemos el OutputStream del BLOB
      os = blob.getBinaryOutputStream();

      // Escribimos los datos
      int i = 0;
      int bytesRestantes = tamanyo;
      int chunk = Math.min(bytesRestantes, blob.getChunkSize());
      while (bytesRestantes>0) {
        os.write(documento, i, chunk);
        i = i + chunk;
        bytesRestantes = bytesRestantes - chunk;
        chunk = Math.min(bytesRestantes, blob.getChunkSize());
      }

      // Finalizamos la escritura
      os.flush();
    }
    finally {
      if (os!=null) try { os.close(); } catch (Exception e) { BitLog.warn("El objeto OutputStream no se ha cerrado correctamente"); }
    }
  }

    */

  /**
   * Escribe un CLOB en base de datos.
   *
   * @param  rs           el resultset correspondiente.
   * @param  nombreCampo  el nombre del campo en base de datos.
   * @param  documento    el documento que se desea almacenar.
   * @throws SQLException si se produce un error de base de datos.
   * @throws IOException  si se produce un error en la lectura del documento.
   */
    
    /*
  public static void escribirCLOB(ResultSet rs, String nombreCampo, String documento) throws SQLException, IOException
  {
    BitDAO.escribirCLOB(rs, nombreCampo, documento, (documento!=null)?documento.length():0);
  }*/


  /**
   * Escribe un CLOB en base de datos.
   *
   * @param  rs           el resultset correspondiente.
   * @param  nombreCampo  el nombre del campo en base de datos.
   * @param  documento    el documento que se desea almacenar.
   * @param  tamanyo      el tamanyo de los datos que se desea almacenar (si es cero, se coge el tama�o del documento).
   * @throws SQLException si se produce un error de base de datos.
   * @throws IOException  si se produce un error en la lectura del documento.
   */
    /*
  public static void escribirCLOB(ResultSet rs, String nombreCampo, String documento, int tamanyo) throws SQLException, IOException
  {
    // Declaramos los objetos principales del m�todo
    CLOB clob = null;
    Reader reader = null;

    try {
      // PARTE ESPECIFICA QUE SOLO FUNCIONA CON OC4j
      if (BitSystem.isOracleContainersForJava()) {
        clob = ((oracle.jdbc.OracleResultSet)rs).getCLOB(nombreCampo);
      }
      // PARTE ESPECIFICA QUE SOLO FUNCIONA CON JBoss (incluir la librer�a /$JBOSS_HOME$/server/default/lib/jboss-common-jdbc-wrapper.jar)
      else if (BitSystem.isJBossServer()) {
        org.jboss.resource.adapter.jdbc.WrappedResultSet wrs = (org.jboss.resource.adapter.jdbc.WrappedResultSet) rs;
        oracle.jdbc.OracleResultSet ors = (oracle.jdbc.OracleResultSet) wrs.getUnderlyingResultSet();
        clob = ors.getCLOB(nombreCampo);
      // PARTE ESPECIFICA QUE SOLO FUNCIONA CON Glassfishv2
      } else if(BitSystem.isGlassfishV2()) {
        clob = ((oracle.jdbc.OracleResultSet)rs).getCLOB(nombreCampo);
      }

      // Escribimos los datos
      int position = 1;
      reader = new StringReader(documento);
      int chunk = clob.getChunkSize();
      char[] textBuffer = new char[chunk];
      int charsRead;
      while ((charsRead = reader.read(textBuffer)) != -1) {
          int charsWritten = clob.putChars(position, textBuffer, charsRead);
          position += charsRead;
          if (charsWritten!=charsRead) throw new SQLException("Error de escritura en CLOB");
      }

      // Finalizamos la escritura
      reader.close();
    }
    finally {
      if (reader!=null) try { reader.close(); } catch (Exception e) { BitLog.warn("El objeto OutputStream no se ha cerrado correctamente"); }
    }
  }
*/

  /**
   * Extrae el texto contenido en el Clob.
   *
   * @param clob el objeto Clob.
   * @return la informacion en forma de String.
   * @throws Exception si ocurre un error.
   */
    
    /*
  public static String leerClob(Clob clob) throws Exception
  {
    // Declaramos los objetos principales del metodo
    StringBuffer val = new StringBuffer();
    char[] buff = new char[512];
    int n = 0;

    // Leemos el texto
    if (clob != null) {
      Reader r = clob.getCharacterStream();
      while ((n = r.read(buff)) != -1) { val.append(buff, 0, n); }
      r.close();
    }
    return (val.length()>0) ? val.toString() : null;
  }
*/

}
