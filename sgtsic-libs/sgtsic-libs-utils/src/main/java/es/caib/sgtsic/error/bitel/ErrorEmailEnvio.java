package es.caib.sgtsic.error.bitel;

import java.util.HashMap;
import java.util.ArrayList;


/**
 * La clase ErrorEmailEnvio representa un error producido al realizar el env�o de un correo electr�nico.
 * 
 * @author Miguel Angel Nieto
 * @date 08/02/2007
 * @version 2.0
 */
public class ErrorEmailEnvio extends ErrorEmail
{
  
  /**
   * Constructor de la clase ErrorEmailEnvio.
   * @param from    Especifica la direcci�n de correo del remitente.
   * @param remitente    Especifica el nombre del remitente.
   * @param alTo    Especifica la lista de direcciones de correo de los destinatarios.
   * @param alDestinatarios    Especifica la lista de nombres de los destinatarios.
   * @param alCC    Especifica la lista de direcciones de correo de los destinatarios con copia.
   * @param alDestCopia    Especifica la lista de nombres de los destinatarios con copia.
   * @param asunto    Especifica el asunto (subject) del e-mail.
   * @param error    Excepci�n/error asociado al error.
   */
  public ErrorEmailEnvio(String from, String remitente, ArrayList alTo, ArrayList alDestinatarios, ArrayList alCC, ArrayList alDestCopia, String asunto, Throwable error)
  {
    // Creamos el error con el c�digo del error y la excepci�n asociada
    super("BIT_EMAIL_ENVIO", error);
    
    // Definimos la descripci�n del error
    HashMap descripcion = new HashMap();
    descripcion.put(BitError.IDIOMA_ESPANYOL, "Error al enviar un e-mail con el asunto '" + asunto + "'.");
    descripcion.put(BitError.IDIOMA_CATALAN , "Error a l'enviar un e-mail amb l'assumpte '" + asunto + "'.");
    this.setDescripcion(descripcion);
    
    // Definimos el debug del error
    this.setDebug((String)descripcion.get(BitError.IDIOMA_ESPANYOL)
                  + "\nfrom: " + from
                  + "\nremitente: " + remitente
                  + "\nalTo: " + alTo
                  + "\nalDestinatarios: " + alDestinatarios
                  + "\nalCC: " + alCC
                  + "\nalDestCopia: " + alDestCopia
                  + "\nasunto: " + asunto);
  }
  
}
