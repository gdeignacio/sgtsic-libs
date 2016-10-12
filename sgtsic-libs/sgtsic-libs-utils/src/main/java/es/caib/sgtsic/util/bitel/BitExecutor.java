package es.caib.sgtsic.util.bitel;

import es.caib.sgtsic.data.Data;
import javax.servlet.http.*;

import es.caib.sgtsic.error.bitel.BitError;


/**
 * Clase abstracta que representa una acci�n que se puede ejectuar
 * @author JBE
 * @author ELU
 * @version 2.0
 * @created 01-sep-2004
 */
public abstract class BitExecutor
{

  /**
   * Realiza las acciones necesarias para llevar a cabo la Acci�n.
   * @param response objeto HttpServletResponse
   * @param request objeto HttpServletRequest
   * @param accion c�digo de la acci�n
   * @param bean objeto Data que puede contener los datos recuperados del request.
   * Siempre que el objeto no sea null contendr� dichos datos
   * @param forward objeto BitForward que ya contiene la url donde tenemos que ir
   * cuando acabe la acci�n. Por regla general este m�todo solo tiene que invocar
   * al m�todo addParameter o addParameters del forward. Nunca debe invocar al m�todo
   * goUrl o goContextUrl.
   * @return retorna el objeto BitForward con la url completa.
   * @throws java.lang.Exception
   */
  public abstract BitForward execute(HttpServletResponse response, HttpServletRequest request, BitAction accion, Data bean, 
                                     BitForward forward) throws BitError;

}
