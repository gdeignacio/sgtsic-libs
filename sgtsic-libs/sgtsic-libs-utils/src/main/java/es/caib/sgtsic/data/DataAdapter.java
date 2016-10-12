package es.caib.sgtsic.data;

import es.caib.sgtsic.util.bitel.BitCadena;
import es.caib.sgtsic.util.bitel.BitFecha;
import es.caib.sgtsic.util.bitel.BitForward;
import es.caib.sgtsic.util.bitel.BitLog;

//~--- JDK imports ------------------------------------------------------------

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Implementa una clase Data.
 * Se utiliza cuando queremos crear una Data pero no queremos implementar todos
 * los m�todos de Data. En ese caso extenderemos de la clase DataAdapter.
 *
 * @author ELU
 * @version 1.0
 * @created 01-sep-2004 12:00:00
 */
public class DataAdapter implements Data
{

  /**
     * Constructor por defecto.
     */
  public DataAdapter()
  {
  }

  /**
     * Recupera todos los par�metros de filtro que llegan a trav�s de una petici�n
     * HTTP.
     * @param request    Objeto request de la p�gina.
     * @param c    Clase de clase Data en la que se desea recuperar la informaci�n.
     *
     */
  public static Data recogerParametros(HttpServletRequest request, Class c) throws IllegalAccessException, 
                                                                                   InstantiationException
  {

    // Recuperamos la clase y su lista de atributos
    Data dta = (Data) c.newInstance();
    Field[] atributos = c.getDeclaredFields();

    // Recorremos los atributos del request
    for (int i = 0; i < atributos.length; i++)
    {

      // Recuperamos el atributo y su informaci�n principal
      Field atributo = atributos[i];
      String nombreAtributo = atributo.getName();
      Class tipoAtributo = atributo.getType();
      boolean esEstatico = Modifier.isStatic(atributo.getModifiers());
      String parametro = request.getParameter(nombreAtributo);

      if (parametro != null)
      {

        // S�lo recogemos los par�metros propios de la clase, no aquellos est�ticos (constantes, principalmente)
        if (!esEstatico)
        {
          Method metodo = null;

          try
          {

            // Definimos el m�todo set
            Class[] paramDefs = new Class[1];

            paramDefs[0] = atributo.getType();
            metodo = c.getMethod("set" + BitCadena.capitalize(nombreAtributo), paramDefs);
          }
          catch (Exception e)
          {
            BitLog.error("No puedo hacer set de " + nombreAtributo);
          }

          try
          {

            // Definimos los par�metros que usaremos para llamar al m�todo
            boolean invocarMetodo = false;
            Object params[] = new Object[1];

            // S�lo recogemos los campos de clase b�sica
            if ("java.lang.String".equals(tipoAtributo.getName()))
            {
              params[0] = new String(parametro);
              invocarMetodo = true;
            }
            else if ("int".equals(tipoAtributo.getName()) || "java.lang.Integer".equals(tipoAtributo.getName()))
            {
              String valor = parametro;

              if ((valor != null) && (valor.length() > 0))
              {
                params[0] = new Integer(valor);
                invocarMetodo = true;
              }
            }
            else if ("double".equals(tipoAtributo.getName()) || "java.lang.Double".equals(tipoAtributo.getName()))
            {
              String valor = parametro;

              if ((valor != null) && (valor.length() > 0))
              {
                params[0] = new Double(valor);
                invocarMetodo = true;
              }
            }
            else if ("boolean".equals(tipoAtributo.getName()))
            {
              params[0] = new Boolean("S".equals(new String(parametro)));
              invocarMetodo = true;
            }
            else if ("java.lang.Boolean".equals(tipoAtributo.getName()))
            {
              if ("S".equals(new String(parametro)))
              {
                params[0] = new Boolean(true);
              }
              else if ("N".equals(new String(parametro)))
              {
                params[0] = new Boolean(false);
              }
              else
              {
                params[0] = null;
              }

              invocarMetodo = true;
            }
            else if ("java.sql.Timestamp".equals(tipoAtributo.getName()))
            {
              params[0] = BitFecha.creaFecha(new String(parametro));
              invocarMetodo = true;
            }
            else if ("java.util.Collection".equals(tipoAtributo.getName()) || 
                     "java.util.List".equals(tipoAtributo.getName()) || 
                     "java.util.ArrayList".equals(tipoAtributo.getName()) || 
                     "java.util.Vector".equals(tipoAtributo.getName()) || "java.util.Set".equals(tipoAtributo.getName()) || 
                     "java.util.HashSet".equals(tipoAtributo.getName()) || 
                     "java.util.LinkedList".equals(tipoAtributo.getName()))
            {
              String[] valores = request.getParameterValues(nombreAtributo);

              if (valores != null)
              {

                // Controlamos el hecho de que nos hayan indicado una interfaz y no una clase
                String nombreClase;

                if ("java.util.Collection".equals(tipoAtributo.getName()) || "java.util.List".equals(tipoAtributo.getName()))
                {
                  nombreClase = "java.util.ArrayList";
                }
                else if ("java.util.Set".equals(tipoAtributo.getName()))
                {
                  nombreClase = "java.util.HashSet";
                }
                else
                {
                  nombreClase = tipoAtributo.getName();
                }

                Collection col = (Collection) Class.forName(nombreClase).getConstructor().newInstance();

                col.addAll(Arrays.asList(valores));
                params[0] = col;
                invocarMetodo = true;
              }
            }

            // Invocamos el m�todo (todos los casos correctos excepto con las listas)
            if (invocarMetodo && (metodo != null))
            {
              metodo.invoke(dta, params);
            }
          }
          catch (Exception e)
          {
            e.printStackTrace();
            BitLog.error("No puedo invocar el m�todo " + metodo.getName());
          }
        } // fin if (!esEstatico)
      } // fin if(parametro != null)
    }

    // Devolvemos la nueva clase Data
    return dta;
  }

  /**
     * Funci�n que devuelve el valor de cada uno de los atributos de la clase Data.
     * @return Devuelve el resultado de BitCadena.doToString(this).
     */
  public String toString()
  {
    return BitCadena.doToString(this);
  }

  /**
     * Funcion que devuelve true si el resultado de validar los datos de la clase ha
     * sido satisfactorio. En caso contrario devuelve false.
     * @param forward    Objeto BitForward con la URL a la que se redireccionar� la petici�n
     * en caso de que la funci�n retorne false.
     * @return En este caso devuelve true.
     */
  public boolean validate(BitForward forward)
  {
    return true;
  }

  /**
     * Funci�n que devuelve el c�digo de la clase data en formato string para las
     * listas de valores.
     * @return En este caso devuelve un String vac�o.
     */
  public String getCodigoLOV()
  {
    return BitCadena.CADENA_VACIA;
  }

  /**
     * Funci�n que devuelve la descripci�n que deseamos que aparezca en las
     * listas de valores.
     * @return En este caso devuelve un String vac�o.
     */
  public String getValorLOV()
  {
    return BitCadena.CADENA_VACIA;
  }

  /**
   * Se a�ade para que DWR no de por culo
   * @param codigo
   */
  public void setCodigoLOV(String codigo)
  {

  }

  /**
   * Idem que setCodigoLOV.
   * @param valor
   */
  public void setValorLOV(String valor)
  {
     
  }


}
