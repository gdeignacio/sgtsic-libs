package es.caib.sgtsic.util.bitel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Clase de utilidades para trabajar con inputsstreams y outputstreams. Se incluye un conjunto de funcionalidades para trabajar con ficheros
 * @author dept. Desarrollo
 * @version 1.0 29/02/2008
 */
public class BitIO
{
  public BitIO()
  {
  }
  
  /**
   * Copia completamente el contenido de un InputStream en un OutputStream con un buffer de 8024
   * El m�todo no cierra ni abre los streams, queda a responsabilidad del programador cerrarlos despu�s de la llamada.
   * 
   * @param input Stream de entrada
   * @param output Stream de salida
   * @throws IOException Si se produce un error en la lectura o la escritura de uno de los dos
   **/
  public static void streamCopy(InputStream input,  OutputStream output) throws IOException
  {
      streamCopy(input, output, 8024);
  }
  
  /**
   * Copia completamente el contenido de un InputStream en un OutputStream con un buffer determinado por pa�metro
   * El m�todo no cierra ni abre los streams, queda a responsabilidad del programador cerrarlos despu�s de la llamada.
   * 
   * @param input Stream de entrada
   * @param output Stream de salida
   * @param sizeBuffer tama�o que tendr� el buffer
   * @throws IOException Si se produce un error en la lectura o la escritura de uno de los dos
   **/
  public static void streamCopy(InputStream input,  OutputStream output, int sizeBuffer) throws IOException
  {
      final byte[] buffer = new byte[ sizeBuffer ];
      int n = 0;
      while( -1 != ( n = input.read( buffer ) ) ) output.write( buffer, 0, n );
  }

}
