package es.caib.sgtsic.error.bitel;

import java.util.HashMap;


/**
 * La clase ErrorConversion representa un error en la conversi�n de datos
 *
 * @author Esteban Luengo
 * @date 06/10/2009
 * @version 1.0
 */
public class ErrorConversion extends BitError
{

  /**
   * Constructor de la clase BitError.
   * @param error    Excepci�n/error asociado al error.
   */
  public ErrorConversion(Throwable error)
  {
    // Creamos el error con el c�digo del error y la excepci�n asociada
    super("BIT_CONVERSION", error);

    // Definimos la descripci�n del error
    HashMap descripcion = new HashMap();
    descripcion.put(BitError.IDIOMA_ESPANYOL, "Ha ocurrido un error durante la conversi�n de datos.\n"+error.getMessage());
    descripcion.put(BitError.IDIOMA_CATALAN , "Ha surgit un error durant la conversi� de dades.\n"+error.getMessage());
    this.setDescripcion(descripcion);

    // Definimos el debug del error
    this.setDebug((String)descripcion.get(BitError.IDIOMA_ESPANYOL));
  }

  /**
   * Constructor de la clase BitError.
   * @param error    Excepci�n/error asociado al error.
   */
  public ErrorConversion(String input, Throwable error)
  {
    // Creamos el error con el c�digo del error y la excepci�n asociada
    super("BIT_CONVERSION", error);

    // Definimos la descripci�n del error
    HashMap descripcion = new HashMap();
    descripcion.put(BitError.IDIOMA_ESPANYOL, "Ha ocurrido un error durante la conversi�n del dato: "+input+"\n"+error.getMessage());
    descripcion.put(BitError.IDIOMA_CATALAN , "Ha surgit un error durant la conversi� de la dada: "+input+"\n"+error.getMessage());
    this.setDescripcion(descripcion);

    // Definimos el debug del error
    this.setDebug((String)descripcion.get(BitError.IDIOMA_ESPANYOL));
  }

}
