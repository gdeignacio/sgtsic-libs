package es.caib.sgtsic.error.bitel;

import java.util.HashMap;


/**
 * La clase ErrorDatosIncorrecto representa un error producido al recibir datos incorrectos.
 * 
 * @author  Miguel Angel Nieto
 * @author  Paco Ros
 * @version 1.0, 16/07/2008
 */
public class ErrorDatosIncorrecto extends BitError
{
  
  /**
   * Constructor de la clase ErrorDatosIncorrectos.
   * 
   */
  public ErrorDatosIncorrecto()
  {
    // Creamos el error con el c�digo del error y la excepci�n asociada
    super("BIT_DATOS_INCORRECTO");
    
    // Definimos la descripci�n del error
    HashMap descripcion = new HashMap();
    descripcion.put(BitError.IDIOMA_ESPANYOL, "La informaci�n enviada contiene datos incorrectos y el proceso no puede continuar.");
    descripcion.put(BitError.IDIOMA_CATALAN , "la informaci� enviada cont� dades incorrectes i el proc�s no pot continuar.");
    this.setDescripcion(descripcion);
    
    // Definimos el debug del error
    this.setDebug((String)descripcion.get(BitError.IDIOMA_ESPANYOL));
  }
  
  /**
   * Constructor de la clase ErrorDatosIncorrectos.
   * 
   * @param  datos Datos que han provocado el error.
   */
  public ErrorDatosIncorrecto(String datos)
  {
    // Creamos el error con el c�digo del error y la excepci�n asociada
    super("BIT_DATOS_INVALIDOS");
    
    // Definimos la descripci�n del error
    HashMap descripcion = new HashMap();
    descripcion.put(BitError.IDIOMA_ESPANYOL, "La informaci�n enviada no es consistente y el proceso no puede continuar."+datos);
    descripcion.put(BitError.IDIOMA_CATALAN , "la informaci� enviada no �s consistent i el proc�s no pot continuar."+datos);
    this.setDescripcion(descripcion);
    
    // Definimos el debug del error
    this.setDebug((String)descripcion.get(BitError.IDIOMA_ESPANYOL));
  }
  
}
