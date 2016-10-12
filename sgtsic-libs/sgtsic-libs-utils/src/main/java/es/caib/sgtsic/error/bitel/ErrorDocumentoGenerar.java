package es.caib.sgtsic.error.bitel;

import java.util.HashMap;

/**
 * La clase ErrorDocumentoGenerar representa un problema en la generaci�n
 * autom�tica de documentos de salida.
 *
 * @author Alberto Bastos Vargas (ABV)
 * @version 14/01/2009
 */
public class ErrorDocumentoGenerar extends BitError
{
  /**
   * Constructor de la clase ErrorDocumentoGenerar.
   * 
   * @param  error     el error producido.
   */
  public ErrorDocumentoGenerar(Throwable error)
  {
    // Creamos el error con el c�digo del error y la excepci�n asociada
    super("BIT_GENERAR_DOCUMENTO", error);

    // Definimos la descripci�n del error
    HashMap descripcion = new HashMap();
    descripcion.put(BitError.IDIOMA_ESPANYOL, "Ha ocurrido un error durante la generaci�n del documento.\n" + error.getMessage());
    descripcion.put(BitError.IDIOMA_CATALAN , "Ha surgit un error durant la generaci� del document.\n" + error.getMessage());
    this.setDescripcion(descripcion);

    // Definimos el debug del error
    this.setDebug((String)descripcion.get(BitError.IDIOMA_ESPANYOL));
  }

}
