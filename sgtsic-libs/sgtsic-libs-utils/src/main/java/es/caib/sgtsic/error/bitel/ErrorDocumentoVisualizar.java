package es.caib.sgtsic.error.bitel;

import java.util.HashMap;

/**
 * La clase ErrorDocumentoVisualizar representa un problema en la
 * visualizaci�n de documentos almacenados del sistema.
 *
 * @author Alberto Bastos Vargas (ABV)
 * @version 14/01/2009
 */
public class ErrorDocumentoVisualizar extends BitError {

  /**
   * Constructor de la clase ErrorDocumentoVisualizar.
   *
   * @param  error     el error producido.
   */
  public ErrorDocumentoVisualizar(Throwable error)
  {
    // Creamos el error con el c�digo del error y la excepci�n asociada
    super("BIT_VER_DOCUMENTO", error);

    // Definimos la descripci�n del error
    HashMap descripcion = new HashMap();
    descripcion.put(BitError.IDIOMA_ESPANYOL, "Ha ocurrido un error durante la carga del documento.\n" + error.getMessage());
    descripcion.put(BitError.IDIOMA_CATALAN , "Ha surgit un error durant la c�rrega del document.\n" + error.getMessage());
    this.setDescripcion(descripcion);

    // Definimos el debug del error
    this.setDebug((String)descripcion.get(BitError.IDIOMA_ESPANYOL));
  }

}
