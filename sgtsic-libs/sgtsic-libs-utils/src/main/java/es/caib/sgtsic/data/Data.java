package es.caib.sgtsic.data;

import es.caib.sgtsic.util.bitel.BitForward;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

public interface Data extends Serializable
{

  /**
     * Funci�n que devuelve el valor de cada uno los atributos de la clase Data
     * @return el valor de cada uno los atributos de la clase Data
     */
  public String toString();

  /**
     * Funcion que devuelve true si el resultado de validar los datos de la clase ha
     * sido satisfactorio. En caso contrario devuelve false
     * @param forward URL a la que se redireccionar� la petici�n en caso de que falle
     * la validaci�n. El propio m�todo puede a�adir par�metros a esta url o incluso
     * modificar la URL
     * @return true si la validaci�n fue correcta, false en caso contrario
     */
  public boolean validate(BitForward forward);

  /**
     * Funci�n que devuelve el c�digo de la clase data en formato string para las
     * listas de valores
     * @return c�digo de la clase data en formato string para las listas de valores
     */
  public String getCodigoLOV();

  /**
     * Funci�n que devuelve la descripci�n que deseamos que aprarezca en las
     * listas de valores
     * @return Descripci�n que deseamos que aparezca en las listas de valores
     */
  public String getValorLOV();
}
