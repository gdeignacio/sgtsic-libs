package es.caib.sgtsic.error.bitel;

import java.util.HashMap;

/**
 * La clase ErrorDocumentoGenerar representa un problema en la generaci�n
 * autom�tica de documentos de salida.
 *
 * @author Alberto Bastos Vargas (ABV)
 * @version 14/01/2009
 */
public class ErrorDocumentoGenerarPlantilla extends BitError
{
  /**
   * Constructor de la clase ErrorDocumentoGenerar.
   */
  public ErrorDocumentoGenerarPlantilla(String tipoSalida, String tramiteNombre)
  {
    // Creamos el error con el c�digo del error y la excepci�n asociada
    super("BIT_GENERAR_DOCUMENTO_PLANTILLA");

    // Definimos la descripci�n del error
    HashMap descripcion = new HashMap();
    descripcion.put(BitError.IDIOMA_ESPANYOL, "Ha ocurrido un error durante la generaci�n del documento.\nNo se ha encontrado la plantilla del documento '" + tipoSalida + "' en el tr�mite '" + tramiteNombre + ".");
    descripcion.put(BitError.IDIOMA_CATALAN , "Ha surgit un error durant la generaci� del document.\nNo s'ha trobat la plantilla del document '" + tipoSalida + "' en el tr�mit '" + tramiteNombre + "'.");
    this.setDescripcion(descripcion);

    // Definimos el debug del error
    this.setDebug((String)descripcion.get(BitError.IDIOMA_ESPANYOL));
  }

  public ErrorDocumentoGenerarPlantilla(String tipoSalida, String tipoPlantilla, String tramiteNombre)
  {
    // Creamos el error con el c�digo del error y la excepci�n asociada
    super("BIT_GENERAR_DOCUMENTO_PLANTILLA");

    // Definimos la descripci�n del error
    HashMap descripcion = new HashMap();
    descripcion.put(BitError.IDIOMA_ESPANYOL, "Ha ocurrido un error durante la generaci�n del documento.\nNo se ha encontrado la plantilla del tipo '" + tipoPlantilla + "' para el documento '" + tipoSalida + "' en el tr�mite '" + tramiteNombre + ".");
    descripcion.put(BitError.IDIOMA_CATALAN , "Ha surgit un error durant la generaci� del document.\nNo s'ha trobat la plantilla del tipus '" + tipoPlantilla + "' per al document '" + tipoSalida + "' en el tr�mit '" + tramiteNombre + "'.");
    this.setDescripcion(descripcion);

    // Definimos el debug del error
    this.setDebug((String)descripcion.get(BitError.IDIOMA_ESPANYOL));
  }

}
