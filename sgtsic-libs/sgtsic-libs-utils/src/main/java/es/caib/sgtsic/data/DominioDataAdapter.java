package es.caib.sgtsic.data;

/**
 * Extiende de DominioData. Se utiliza como comodin para las librerias internas
 * No se permite su uso en las aplicaciones
 * @author ELU
 * @version 1.0
 * @created 15-FEB-2005 11:12:00
 */
public class DominioDataAdapter extends DominioData
{
  public DominioDataAdapter()
  {
  }

  /**
     * Retorna getValorLOV()
     * @return String getValorLOV()
     */
  public String getDominio()
  {
    return getValorLOV();
  }

  /**
     * Retorna el literal "TraduccionData"
     * @return String "TraduccionData"
     */
  public String getNombreTabla()
  {
    return "TraduccionData";
  }
}
