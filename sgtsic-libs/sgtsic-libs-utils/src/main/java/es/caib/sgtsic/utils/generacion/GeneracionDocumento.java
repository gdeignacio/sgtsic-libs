/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.caib.sgtsic.utils.generacion;


import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clase de generación de documentos a partir de plantilla.
 *
 * Transforma una plantilla de entrada a partir de los datos
 * recibidos a través del contenedor GeneracionDocumentoData.
 *
 * La plantilla soporta fragmentos de asignación, condición y iteración.
 * @author ABV
 */
public class GeneracionDocumento {

  // Marcas de inicio y fin de un bloque de asignación (ej.: ##exp.metaExpedienteNombre##)
  public static final String VALOR_INICIO = "##";
  public static final String VALOR_FIN = "##";

  // Marcas de inicio de condición, fin de condición,
  // separación if-else y fin de bloque para condicionales.
  // ej.:
  // #IF#estaCerrado#
  // <p>El expediente está cerrado.</p>
  // #ELSE#
  // <p>El expediente está abierto.</p>
  // #ENDIF#
  public static final String CONDICION_IF_INICIO = "#IF#";
  public static final String CONDICION_IF_FIN = "#";
  public static final String CONDICION_ELSE = "#ELSE#";
  public static final String CONDICION_IF_END = "#ENDIF#";

  // Marcas de inicio de colección, fin de colección
  // y fin de bloque para iteraciones.
  // ej.:
  // #FOR#registros#
  // <p>Registro num. ##numero##.</p>
  // #ENDFOR#
  public static final String BUCLE_INICIO = "#FOR#";
  public static final String BUCLE_FININICIO = "#";
  public static final String BUCLE_FIN = "#ENDFOR#";

  /**
   * Procesa el texto de entrada transformándolo a partir de los datos del contenedor.
   * @param plantilla Texto de entrada
   * @param data Contenedor de datos
   * @return Texto transformado con los datos del contenedor.
   * @throws es.caib.sifarma.error.ErrorDocumentoGenerar Ha ocurrido algún error al analizar la estructura de entrada.
   */
  public static String generarDocumento(String plantilla, GeneracionDocumentoData data) throws Exception {
    StringBuffer out = new StringBuffer();

    int recorrido = 0; // Última posición procesada

    // Obtenemos los listados de ocurrencias que nos serán útiles durante el recorrido
    Matcher mGen = obtenerMarcasGeneral(plantilla); // Inicios de bloque, para el recorrido general
    Matcher mCond = obtenerMarcasCondicion(plantilla); // Marcas de bloque de condición, para encontrar el cierre de una condición
    Matcher mBucle = obtenerMarcasBucle(plantilla); // Marcas de bloque de iteración, para encontrar el cierre de un bucle

    // Avanzamos por el texto de entrada hasta la siguiente marca de bloque.
    while(mGen.find(recorrido)) {
      // Copiamos a la salida todo el fragmento previo a la nueva ocurrencia.
      int posIni = mGen.start();
      String marca = mGen.group();
      out.append(plantilla.substring(recorrido, posIni));

      // Determinamos si es una asignación, condición o iteración.
      if(VALOR_INICIO.equals(marca)) {
        // Para una asignación, acotamos la expresión y la resolvemos contra el contenedor de datos.
        int posFin = plantilla.indexOf(VALOR_FIN, mGen.end()); // posición de la marca de fin
        String expresion = plantilla.substring(mGen.end(), posFin); // expresion a sustituir
        String valor = encontrarValor(expresion, data); // valor a sustituir
        
        // Si no hemos conseguido resolver el valor (null), conservamos la expresion original
        if(valor != null) { out.append(valor); }
        else { out.append(VALOR_INICIO + expresion + VALOR_FIN); }

        // Avanzamos la marca de última posición procesada.
        recorrido = posFin + VALOR_FIN.length();
      } else if(CONDICION_IF_INICIO.equals(marca)) {
        // Para una condición, buscamos las marcas de separación if-else (opcional) y de fin de bloque.
        // Para ello, mantenemos un contador de bloques anidados que nos permita saber
        // cuándo estamos tratando una marca al mismo nivel de anidación que la del bloque actual.
        int contadorAnidados = 0;
        int posFin = -1, posElse = -1, tmp = mGen.end();

        // Mientras no hayamos encontrado el cierre del bloque
        while(posFin < 0) {
          if(mCond.find(tmp)) {
            tmp = mCond.start();
            // Bloque de inicio: se trata de una condición anidada, no nos interesa y incrementamos la anidación.
            if(CONDICION_IF_INICIO.equals(mCond.group())) {
              contadorAnidados++;
              tmp = mCond.end();
            }
            // Bloque de separación if-else: si estamos al nivel de nuestro bloque, lo tenemos en cuenta.
            else if(CONDICION_ELSE.equals(mCond.group())) {
              if(contadorAnidados == 0) { posElse = tmp; }
              tmp = mCond.end();
            }
            // Bloque de cierre: si estamos al nivel de nuestro bloque, es la marca que buscamos.
            else if(CONDICION_IF_END.equals(mCond.group())) {
              contadorAnidados--;
              if(contadorAnidados < 0) { posFin = tmp; }
              tmp = mCond.end();
            }
          } else {
            // Si acabamos con las marcas antes de obtener el cierre, el texto de entrada está mal formado.
            throw new Exception("Error de sintaxis procesando condicion.");
          }
        }

        // Obtenemos la condicion y los bloques de condicion y else (si existe).
        int posFinIni = plantilla.indexOf(CONDICION_IF_FIN, mGen.end());
        String condicion = plantilla.substring(mGen.end(), posFinIni);
        String cuerpoCondicion = null;
        String cuerpoElse = null;
        if(posElse > 0) {
          cuerpoCondicion = plantilla.substring(posFinIni + CONDICION_IF_FIN.length(), posElse);
          cuerpoElse = plantilla.substring(posElse + CONDICION_ELSE.length(), posFin);
        } else {
          cuerpoCondicion = plantilla.substring(posFinIni + CONDICION_IF_FIN.length(), posFin);
        }

        // Procesamos un fragmento u otro según la evaluación de la condición.
        // El bloque a procesar puede ser dinámico, por lo que llamamos
        // recursivamente a la transformación con el mismo contenedor de datos.
        if(evaluarCondicion(condicion, data)) {
          out.append(GeneracionDocumento.generarDocumento(cuerpoCondicion, data));
        } else {
          if(cuerpoElse != null) {
            out.append(GeneracionDocumento.generarDocumento(cuerpoElse, data));
          }
        }

        // Avanzamos la marca de última posición procesada.
        recorrido = posFin + CONDICION_IF_END.length();
      } else if(BUCLE_INICIO.equals(marca)) {
        // Para un bucle, buscamos la marca de cierre de bucle que nos corresponde.
        // Para ello mantenemos un contador de bloques anidados que nos permita descartar
        // marcas de iteraciones anidadas dentro de la actual.
        int contadorAnidados = 0;
        int posFin = -1, tmp = mGen.end();

        while(posFin < 0) {
          if(mBucle.find(tmp)) {
            tmp = mBucle.start();
            // Inicio de bucle: se trata de un bucle anidados, no nos interesa y sumamos un nivel.
            if(BUCLE_INICIO.equals(mBucle.group())) {
              contadorAnidados++;
              tmp = mBucle.end();
            }
            // Fin de bucle: si estamos en el nivel de nuestro bloque, es la marca que buscamos.
            else if(BUCLE_FIN.equals(mBucle.group())) {
              contadorAnidados--;
              if(contadorAnidados < 0) { posFin = tmp; }
              tmp = mBucle.end();
            }
          } else {
            // Si se nos acaban las marcas antes de encontrar el cierre, el texto de entrada está mal formado.
            throw new Exception("Error de sintaxis procesando bucle.");
          }
        }

        // Extraremos la colección a recorrer y el cuerpo del bucle.
        int posFinIni = plantilla.indexOf(BUCLE_FININICIO, mGen.end());
        String coleccion = plantilla.substring(mGen.end(), posFinIni);
        String cuerpoBucle = plantilla.substring(posFinIni + BUCLE_FININICIO.length(), posFin);
        List<GeneracionDocumentoData> listColeccion = data.getColeccion(coleccion);

        // Procesamos recursivamente el cuerpo del bucle para cada elemento de la colección.
        for(GeneracionDocumentoData dataRec: listColeccion) {
          out.append(GeneracionDocumento.generarDocumento(cuerpoBucle, dataRec));
        }

        // Avanzamos la marca de última posición procesada.
        recorrido = posFin + BUCLE_FIN.length();
      }
    }

    // Copiamos a la salida todo el fragmento desde la última ocurrencia hasta el final.
    out.append(plantilla.substring(recorrido));

    return out.toString();
  }

  /**
   * Evalúa una condición buscándola en el contenedor de datos.
   * Si no se encuentra la condición, busca recursivamente en niveles superiores.
   * @param condicion Nombre de la condición
   * @param data Contenedor de datos
   * @return Evaluación de la condición, false si no se encuentra.
   */
  private static boolean evaluarCondicion(String condicion, GeneracionDocumentoData data) {
      boolean ret = false;
      if(condicion == null || "".equals(condicion) || data == null) { return false; }

      if(data.existsCondicion(condicion)) {
          ret = data.getCondicion(condicion);
      } else if(data.getSuperior() != null) {
          ret = evaluarCondicion(condicion, data.getSuperior());
      } else {
          ret = false;
      }
      return ret;
  }

  /**
   * Busca en el contenedor de datos el valor para la expresión indicada.
   * Si no se encuentra un valor para la expresión, busca recursivamente en niveles superiores.
   * @param expresion Expresión a sustituir (nombre de parámetro, atributo de objeto, prefijo y atributo de objeto)
   * @param data Contenedor de datos
   * @return Valor por el que sustituir la expresión.
   */
  private static String encontrarValor(String expresion, GeneracionDocumentoData data) {
    if(expresion == null || "".equals(expresion)) { return null; }
    String objeto = null; String propiedad = null;
    // Si existe un separador (punto) en la expresión, debemos
    // contemplar la posibilidad de que el primer fragmento sea
    // el identificador del objeto.
    if(expresion.indexOf(".") >= 0) {
      objeto = expresion.substring(0, expresion.indexOf("."));
      propiedad = expresion.substring(expresion.indexOf(".") + 1);
    } else {
      objeto = null;
      propiedad = expresion;
    }

    // Buscamos por las tres opciones posibles, por este orden:
    // - Que la expresion sea un parámetro
    // - Que la expresion sea un atributo (con prefijo para el objeto)
    // - Que la expresion sea un atributo (sin prefijo para el objeto)
    Object valor = data.getParametro(expresion);
    if(valor == null) { valor = data.getPropiedadObjeto(objeto, propiedad); }
    if(valor == null && objeto != null) { valor = data.getPropiedadObjeto(null, expresion); }

    // Si no hemos encontrado un valor y el contenedor está anidado en una colección, subimos a su padre.
    if(valor == null && data.getSuperior() != null) {
        valor = encontrarValor(expresion, data.getSuperior());
    }
    return valor != null ? valor.toString() : null;
  }

  /**
   * Devuelve la colección de ocurrencias en el texto de entrada para
   * los bloques de inicio de asignación, condición e iteración.
   * @param plantilla Texto de entrada
   * @return Instancia de Matcher con todas las ocurrencias iniciales.
   */
  private static Matcher obtenerMarcasGeneral(String plantilla) {
    Pattern pattern = Pattern.compile("("+VALOR_INICIO+")|("+CONDICION_IF_INICIO+")|("+BUCLE_INICIO+")");
    return pattern.matcher(plantilla);
  }

  /**
   * Devuelve la colección de ocurrencias en el texto de entrada para
   * los bloques de inicio de condición, separación if-else y fin de bloque de condición.
   * @param plantilla Texto de entrada
   * @return Instancia de Matcher con todas las ocurrencias encontradas.
   */
  private static Matcher obtenerMarcasCondicion(String plantilla) {
    Pattern pattern = Pattern.compile("("+CONDICION_IF_INICIO+")|("+CONDICION_ELSE+")|("+CONDICION_IF_END+")");
    return pattern.matcher(plantilla);
  }

  /**
   * Devuelve la colección de ocurrencias en el texto de entrada para
   * los bloques de inicio y fin de bucle.
   * @param plantilla Texto de entrada
   * @return Instancia de Matcher con todas las ocurrencias encontradas.
   */
  private static Matcher obtenerMarcasBucle(String plantilla) {
    Pattern pattern = Pattern.compile("("+BUCLE_INICIO+")|("+BUCLE_FIN+")");
    return pattern.matcher(plantilla);
  }

}
