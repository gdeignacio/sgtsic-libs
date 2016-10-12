package es.caib.sgtsic.error.bitel;

import java.util.HashMap;


/**
 * La clase ErrorXMLLectura representa un error en la lectura de informaci�n en formato XML.
 * 
 * @author Miguel Angel Nieto
 * @author Paco Ros
 * @date 05/01/2007
 * @version 2.0
 */
public class ErrorXMLLectura extends ErrorXML
{
  
  /**
   * Constructor de la clase BitError.
   * @param nombreElemento    Nombre del elemento.
   * @param error    Excepci�n/error asociado al error.
   */
  public ErrorXMLLectura(String nombreElemento, Throwable error)
  {
    // Creamos el error con el c�digo del error y la excepci�n asociada
    super("BIT_XML_LECTURA", error);
    
    // Definimos la descripci�n del error
    HashMap descripcion = new HashMap();
    descripcion.put(BitError.IDIOMA_ESPANYOL, "Error al leer un contenido en formato XML.");
    descripcion.put(BitError.IDIOMA_CATALAN , "Error al llegir un contingut en format XML.");
    this.setDescripcion(descripcion);
    
    // Definimos el debug del error
    this.setDebug((String)descripcion.get(BitError.IDIOMA_ESPANYOL) + "Fichero: " + nombreElemento);
  }
  
}
