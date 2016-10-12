package es.caib.sgtsic.error.bitel;

import java.util.HashMap;

/**
 * La clase ErrorFicheroEscritura representa un error en la escritura de de un fichero.
 *
 * @author Miguel Angel Nieto
 * @author Paco Ros
 * @date 05/01/2007
 * @version 2.0
 */
public class ErrorGeneracionDOCX extends ErrorFichero {

  /**
   * Constructor de la clase BitError.
   * @param nombreFichero Nombre del fichero.
   * @param error Excepci�n/error asociado al error.
   */
  public ErrorGeneracionDOCX(Throwable error) {
    // Creamos el error con el c�digo del error y la excepci�n asociada
    super("BIT_GENERACION_DOCX", error);

    // Definimos la descripci�n del error
    HashMap descripcion = new HashMap();
    descripcion.put(BitError.IDIOMA_ESPANYOL, "Error al generar docx.");
    descripcion.put(BitError.IDIOMA_CATALAN, "Error generant docx.");

    this.setDescripcion(descripcion);

    // Definimos el debug del error
    this.setDebug((String)descripcion.get(BitError.IDIOMA_ESPANYOL));
  }

}
