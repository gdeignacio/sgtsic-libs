package es.caib.sgtsic.error.bitel;

import java.util.HashMap;


/**
 * La clase ErrorFicheroLectura representa un error en la lectura de de un fichero.
 * 
 * @author Miguel Angel Nieto
 * @author Paco Ros
 * @date 05/01/2007
 * @version 2.0
 */
public class ErrorFicheroLectura extends ErrorFichero
{
  
  /**
   * Constructor de la clase BitError.
   * @param nombreFichero    Nombre del fichero.
   * @param error    Excepci�n/error asociado al error.
   */
  public ErrorFicheroLectura(String nombreFichero, Throwable error)
  {
    // Creamos el error con el c�digo del error y la excepci�n asociada
    super("BIT_FICHERO_LECTURA", error);
    
    // Definimos la descripci�n del error
    HashMap descripcion = new HashMap();
    descripcion.put(BitError.IDIOMA_ESPANYOL, "Error al leer el archivo " + nombreFichero + ".");
    descripcion.put(BitError.IDIOMA_CATALAN , "Error al llegir l'arxiu " + nombreFichero + ".");
    this.setDescripcion(descripcion);
    
    // Definimos el debug del error
    this.setDebug((String)descripcion.get(BitError.IDIOMA_ESPANYOL) + "Fichero: " + nombreFichero);
  }
  
}
