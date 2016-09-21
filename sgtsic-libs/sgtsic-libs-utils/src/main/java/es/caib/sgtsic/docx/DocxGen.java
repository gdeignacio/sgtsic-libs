package es.caib.sgtsic.docx;


import com.google.common.io.ByteStreams;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipFile;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.WordUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Paco Ros
 * @version 20090505
 *
 * Clase encargada de la generación de docuementos en formato DOCx
 *
 */
public class DocxGen {

    /**
     * Genera un informe a partir de su plantilla correspondiente y de los datos de la clase Serializable guardando el resultado en disco.
     *
     * La técnica debe basarse en la creación del documento a generar usando Word 2007.
     * Para ello es necesario activar la vista "Programador" y utilizar el campo "Texto enriquecido" para colocar los campos variables
     * en el informe. Estos campos deben tener como nombre el mismo que se usaría en JavaScript y deben corresponderse con la clase Serializable
     * pasada por parámetro.
     *
     * Ejemplo: la etiqueta de campo expediente.numero será reemplazada por el valor de data.getNumero() invocada mediante reflection.
     *
     * @param plantilla Documento en formato docx que contiene la base a generar.
     * @param data Datos a mapear en el documento.
     * @param atributo Nombre base para etiquetar los campos. Por ejemplo para el campo expediente.numero, atributo debería valer "expediente"
     * @return Apuntador a fichero con el resultado del informe generado.
     *
     * @throws es.bitel.error.ErrorGeneracionDOCX Si se produce un error generando el informe.
     */
    public static File generar(File plantilla, Serializable data, String atributo) throws IOException {
        
            ZipFile zipPla = null;
            Package pkg = null;
            WordDocument docx = null;
            File fileOut = null;
            zipPla = new ZipFile(plantilla);
            pkg = Package.open(zipPla, PackageAccess.ReadWrite);
            docx = new WordDocument(pkg);

            // Reemplazamos los datos del documento con la información del expediente
            modificarContenido(docx, data, atributo);

            // Creamos el documento de salida
            fileOut = File.createTempFile("docxgen", ".docx");
            docx.save(fileOut);

            return fileOut;
     
    }

    /**
     * Genera un informe a partir de su plantilla correspondiente y de los datos de la clase Serializable emitiendo el resultado a través del Stream de respuesta HTTP.
     *
     * La técnica debe basarse en la creación del documento a generar usando Word 2007.
     * Para ello es necesario activar la vista "Programador" y utilizar el campo "Texto enriquecido" para colocar los campos variables
     * en el informe. Estos campos deben tener como nombre el mismo que se usaría en JavaScript y deben corresponderse con la clase Serializable
     * pasada por parámetro.
     *
     * Ejemplo: la etiqueta de campo expediente.numero será reemplazada por el valor de data.DetNumero() invocada mediante reflection.
     *
     * Todos los ficheros intermedios generados (no así plantilla) son eliminados a la finalización del proceso.
     * Por otra parte, el nombre de fichero utilizado en la cabecera "Content-Disposition" enviada al navegador es el nombre real del fichero.
     *
     * @param plantilla Documento en formato docx que contiene la base a generar.
     * @param data Datos a mapear en el documento.
     * @param response Respuesta HTTP en un contexto de ejecución dentro del contenedor de Servlets.
     *
     * @throws es.bitel.error.ErrorGeneracionDOCX Si se produce un error generando el informe.
     */
    public static void generarYServir(File plantilla, Serializable data, String atributo, HttpServletResponse response) throws IOException  {
        File f = generar(plantilla, data, atributo);
        response.setHeader("Content-Type", ContentTypeConstant.WORD_MAIN_DOCUMENT);
        //response.setHeader("Cache-Control","cache");
        //response.setHeader("Pragma","Cache");
        response.setHeader("Content-Disposition", "attachment;filename=" + f.getName());

        OutputStream os = response.getOutputStream();
        InputStream is = new FileInputStream(f);

        ByteStreams.copy(is, os);

        is.close();
        os.flush();
        os.close();

        f.delete();
    }

    /**
     * Genera un informe a partir de su plantilla correspondiente y de los datos de la clase Serializable emitiendo el resultado a través del Stream de respuesta HTTP.
     *
     * La técnica debe basarse en la creación del documento a generar usando Word 2007.
     * Para ello es necesario activar la vista "Programador" y utilizar el campo "Texto enriquecido" para colocar los campos variables
     * en el informe. Estos campos deben tener como nombre el mismo que se usaría en JavaScript y deben corresponderse con la clase Serializable
     * pasada por parámetro.
     *
     * Ejemplo: la etiqueta de campo expediente.numero será reemplazada por el valor de data.DetNumero() invocada mediante reflection.
     *
     * Todos los ficheros intermedios generados (no así plantilla) son eliminados a la finalización del proceso.
     * Por otra parte, el nombre de fichero utilizado en la cabecera "Content-Disposition" enviada al navegador es el nombre indicado en fileName.
     *
     * @param plantilla Documento en formato docx que contiene la base a generar.
     * @param data Datos a mapear en el documento.
     * @param response Respuesta HTTP en un contexto de ejecución dentro del contenedor de Servlets.
     *
     * @throws es.bitel.error.ErrorGeneracionDOCX Si se produce un error generando el informe.
     */
    public static void generarYServir(File plantilla, Serializable data, String atributo, HttpServletResponse response, String fileName) throws IOException  {
        File f = generar(plantilla, data, atributo);

        response.setHeader("Content-Type", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        response.setHeader("Cache-Control", "cache");
        response.setHeader("Pragma", "Cache");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        OutputStream os = response.getOutputStream();
        InputStream is = new FileInputStream(f);

        ByteStreams.copy(is, os);

        is.close();
        os.flush();
        os.close();

        f.delete();
    }

    /**
     * Modifica el contenido del documento docx incluido en wd con los datos de la clase dta.
     *
     * @param wd Documento Word.
     * @param dta Clase data con la información a mapear.
     */
    private static void modificarContenido(WordDocument wd, Serializable dta, String atributo) {
        // Variables generales del método
        List<Document> xmlDocs = null;

        // Se recupera el contenido a través del content type
        xmlDocs = wd.getCoreDocuments();

        // Se tratan los nodos a partir de la etiqueta que los representa.
        for (Document xmlDoc : xmlDocs) {
            modificarContenidoXML(xmlDoc, dta, atributo);
        }

    }

    /**
     * Recorre un documento XML perteneciente al conjunto de documentos que forman el fochero docx y aplica
     * las sustituciones necesarias para reemplazar todas las ocurrencias de los campos aparecidos en el documento y mapeados en la case Serializable.
     *
     * @param xmlDoc Documento XML
     * @param objeto Datos a mapear
     * @param atributo Nombre del atributo base (atributo.getDato())
     */
    private static void modificarContenidoXML(Document xmlDoc, Object objeto, String atributo) {
        // Variables del método
        NodeList nl = null;
        Map<String, List<Node>> mn = null; // Mapa de nodos a suplantar por etiqueta

        nl = xmlDoc.getElementsByTagName("w:sdt");
        mn = new HashMap<String, List<Node>>();

        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            String etiqueta = obtenerEtiqueta(n);

            // Si la etiqueta pertenece al atributo actual seleccionamos sus nodos
            if (etiqueta.startsWith(atributo + ".")) {
                List<Node> ln = mn.get(etiqueta);
                if (ln == null) {
                    ln = new ArrayList<Node>();
                    mn.put(etiqueta, ln);
                }

                ln.add(n);
            }
        }

        // Analizamos las etiquetas una por una
        for (String etiqueta : mn.keySet()) {
            // Obtenemos por refection el objeto
            StringBuffer nombreAtributo = new StringBuffer();
            Object o = null;
            boolean existe = false;

            try {
                o = obtenerObjeto(objeto, etiqueta, nombreAtributo);
                existe = true;
            } catch (Exception e) {
                //BitLog.debug("DocxGen: No se encuentra el atributo " + nombreAtributo + " generando informe docx.");
            }

            // Las listas tienen un tratamiento especial.
            if (existe) {
                if (o instanceof List) {
                    remplazarLista(xmlDoc, mn.get(etiqueta), (List<Object>) o, nombreAtributo.toString());
                } else if (o != null) {
                    insertarValor(mn.get(etiqueta), o); // si tiene valor se asigna.
                } else {
                    eliminarCampo(mn.get(etiqueta)); // si no tiene valor se elimina el campo.
                }
            }
        }
    }

    /**
     * Recupera el valor de un atributo de un objeto usando reflection.
     *
     * @param o Objeto del que recuperar el valor.
     * @param etiqueta Nombre base utilizado para recuperar el atributo.
     * @param nombreAtributo Nombre del atributo.
     * @return Valor recuperado.
     *
     * @throws java.lang.Exception La ejecución de este método puede generar errores causados por problemas de acceso, seguridad, no existencia del atributo... etc.
     *							   Pero no es relevante más que saber si ha generado un error, típicamente causado porque no existe el atributo a recuperar del objeto.
     */
    private static Object obtenerObjeto(Object o, String etiqueta, StringBuffer nombreAtributo) throws Exception {
        // Variables generales del método
        String[] strFields = null; // ruta de atributos a seguir
        Object ot = null; // Objeto temporal, nos sirve para guardar los resultados intermedios y el final
        String strMethod = null; // Variable temporal para el nombre de los métodos
        Class c = null; // Variable temporal con la clase del objeto que tratamos actualmente
        Method m = null; // Variable temporal que contiene los métodos con los que obtenemos los atributos

        // Iniciamos las variables
        strFields = etiqueta.split("\\.");
        ot = o;

        // Mientras queden atributos los extraemos
        for (int i = 1; i < strFields.length; i++) {
            nombreAtributo.replace(0, nombreAtributo.length(), strFields[i]);
            strMethod = "get" + WordUtils.capitalize(strFields[i]); //BitCadena.capitalize(strFields[i]);
            c = o.getClass();
            m = c.getMethod(strMethod, (Class[]) null);

            ot = m.invoke(ot);
        }

        return ot;
    }

    /**
     * Reemplaza el valor de una lista de atributos especificada en el documento por una lista de valores.
     *
     * @param xmlDoc Documento XML que forma parte de un documento docx.
     * @param ln Lista de nodos XML
     * @param lo Lista de clases a usar para reemplazar valores
     * @param atributo Nombre del atributo indicado en el documento para referenciar al objeto.
     */
    private static void remplazarLista(Document xmlDoc, List<Node> ln, List<Object> lo, String atributo) {

        for (Node n : ln) {
            try {
                List<Node> lineas = null; // Líneas que contienen los valores de la lista
                List<Node> hijos = null; // Nodos hijos que contendrán los valores

                // Obtención de los nodos afectados
                hijos = obtenerHijos(n, atributo);

                // Obtención los padres de dichos nodos(líneas) a duplicar
                lineas = obtenerLineasADuplicar(n, hijos);

                // Duplicado de las lineas, cambiando los nombres de los atributos por atributos de la forma nombreAtributo[indice]
                for (int i = 0; i < lo.size(); i++) {

                    for (Node nd : lineas) {
                        Node nc = clonarNodo(nd, atributo, i);
                        nd.getParentNode().appendChild(nc);
                    }

                    // Modificación del contenido el contenido
                    modificarContenidoXML(xmlDoc, lo.get(i), atributo + "[" + i + "]");
                }

                // Eliminación de las lineas de referencia
                for (Node ne : lineas) {
                    ne.getParentNode().removeChild(ne);
                }
            } catch (Exception e) {
                //BitLog.debug("DocxGen: No se puede actualizar la lista del atributo " + atributo + "\n" + e.getMessage());
            }
        }
    }

    /**
     * Recupera los hijos de un nodo por nombre.
     *
     * @param n Nodo
     * @param nombre Nombre a partir del cual recuperarlos.
     * @return Lista de nodos con los hijos encontrados.
     */
    private static List<Node> obtenerHijos(Node n, String nombre) {

        List<Node> ln = new ArrayList<Node>();

        // Se buscan todos los nodos del tipo w:sdt que tengan un atributo que comience por el nombre indicados
        for (int i = 0; i < n.getChildNodes().getLength(); i++) {
            Node temp = n.getChildNodes().item(i);
            if (temp.getNodeName().equals("w:sdt")) {

                // Se obtiene la etiqueta
                String etiqueta = obtenerEtiqueta(temp);
                if (etiqueta != null && etiqueta.startsWith(nombre + ".")) {
                    ln.add(temp);
                }

            }

            // Se añaden también todos los subniveles del nodo actual
            ln.addAll(obtenerHijos(temp, nombre));
        }

        return ln;
    }

    /**
     * Dado un nodo renombra su etiqueta según el nombre pasado por parámetros
     *
     * @param node Nodo
     * @param atributo Atributo
     * @param indice Índice
     * @return
     */
    private static Node clonarNodo(Node node, String atributo, int indice) {
        Node nc = node.cloneNode(true);

        // Se obtienen los hijos del objeto clonado y se renombran
        List<Node> ltemp = obtenerHijos(nc, atributo);
        for (Node nt : ltemp) {
            Node temp = null;

            temp = obtenerNodoValorEtiqueta(nt);
            temp.setNodeValue(temp.getNodeValue().replace(atributo + ".", atributo + "[" + indice + "]."));

            temp = obtenerNodoValorAlias(nt);
            temp.setNodeValue(temp.getNodeValue().replace(atributo + ".", atributo + "[" + indice + "]."));

        }
        // Se elmininan los identificadores para no tenerlos duplicados para cada línea
        eliminarIds(nc);

        return nc;
    }

    /**
     * Recorre todos los nodos hijo del nodo nc y elimina los nodos w:id
     *
     * @param nc Nodo a recorrer.
     */
    private static void eliminarIds(Node nc) {
        for (int i = 0; i < nc.getChildNodes().getLength();) {
            Node temp = nc.getChildNodes().item(i);

            if (temp.getNodeName().equals("w:id")) {
                nc.removeChild(temp);
            } else {
                i++;
            }
        }
    }

    /**
     * Recupera una lista de nodos a duplicar.
     *
     * Dada una lista de nodos busca los padres tales que son del tipo línea, si no se encuentra la línea antes de llegar al nodo principal,
     * se usará el principal como nodo a duplicar.
     *
     * En la lista retornada ninguno de los nodos será hijo de otro elemento de la lista, se devolverá únicamente el de mayor nivel.
     *
     * @param nodoPrincipal Nodo proncipal
     * @param hijos Hijos del nodo.
     * @return Lista nunca nula de nodos duplicados.
     */
    private static List<Node> obtenerLineasADuplicar(Node nodoPrincipal, List<Node> hijos) {

        List<Node> lineasADuplicar = new ArrayList<Node>();

        for (Node h : hijos) {
            try {
                Node nodoActual = h.getParentNode();
                while (nodoActual != nodoPrincipal && !nodoActual.getNodeName().equals("w:tr")) {
                    nodoActual = nodoActual.getParentNode();
                    if (nodoActual == null) {
                        throw new Exception("ERROR EN EL RECORRIDO DEL NODO");
                    }
                }

                // Se añade el nodo comprobando que no sea hijo de otro nodo ni esté repetido.
                // Si es padre de otro nodo este reemplazará al anterior
                boolean anyadir = true;
                for (int i = 0; i < lineasADuplicar.size();) {
                    Node n = lineasADuplicar.get(i);

                    // Si está repetido no se añade
                    if (n == nodoActual) {
                        anyadir = false;
                    }

                    // Si es hijo de otro nodo no se añade
                    if (esHijoDe(nodoActual, n)) {
                        anyadir = false;
                    }

                    // Si es padre de otro nodo se elimina el hijo
                    if (esHijoDe(n, nodoActual)) {
                        lineasADuplicar.remove(i);
                    } else {
                        i++;
                    }
                }

                if (anyadir) {
                    lineasADuplicar.add(nodoActual);
                }

            } catch (Exception e) {
                //BitLog.debug("DocxGen: No se ha podido localizar el padre del nodo " + obtenerEtiqueta(h));
            }
        }

        return lineasADuplicar;
    }

    /**
     * Determina si un nodo es hijo de otro.
     *
     * @param hijo Nodo supuestamente hijo.
     * @param padre Supuesto padre del nodo.
     * @return verdadero si hijo es un nodo hijo de padre.
     */
    private static boolean esHijoDe(Node hijo, Node padre) {
        boolean esHijo = false;
        Node temp = hijo;

        while (!esHijo && temp.getParentNode() != null) {
            temp = temp.getParentNode();
            esHijo = temp == padre;
        }

        return esHijo;
    }

    /**
     * Recorre una lista de nodos e inserta un valor en todos ellos.
     *
     * @param ln Lista de nodos.
     * @param o Valor a insertar.
     */
    private static void insertarValor(List<Node> ln, Object o) {

        for (Node n : ln) {
            try {
                // Se busca la lista de nodos w:r, en el primero insertamos el texto, el resto los eliminamos
                List<Node> lnwr = buscarNodos(n, "w:r");
                boolean firstR = true;

                for (Node nr : lnwr) {
                    if (firstR) {
                        // Se buscan todos los nodos w:t al primero se le añade el contenido y el resto se elimina
                        List<Node> lnwt = buscarNodos(n, "w:t");
                        boolean firstT = true;
                        for (Node nt : lnwt) {
                            if (firstT) {
                                OpenXMLDocument.setTextContent(o.toString(), nt);
                                firstT = false;
                            } else {
                                nt.getParentNode().removeChild(nt);
                            }
                        }

                        firstR = false;
                    } else {
                        nr.getParentNode().removeChild(nr);// Se eliminan los nodos restantes
                    }
                }
            } catch (Exception e) {
                //BitLog.debug("DocxGen: No se ha podido insertar el valor \"" + o.toString() + "\" dentro de uno de los nodos.\n" + e.getMessage());
            }
        }
    }

    /**
     * Recupera la lista de nodos para un nombre dado.
     * @param root Nodo en el que buscar.
     * @param nombre Nombre a buscar.
     * @return Lista de nodos de root con el nombre "nombre".
     */
    private static List<Node> buscarNodos(Node root, String nombre) {
        ArrayList<Node> ln = new ArrayList<Node>();

        for (int i = 0; i < root.getChildNodes().getLength(); i++) {
            Node n = root.getChildNodes().item(i);

            if (n.getNodeName().equals(nombre)) {
                ln.add(n);
            } else {
                ln.addAll(buscarNodos(n, nombre));
            }
        }

        return ln;
    }

    /**
     * Elimina campos de una lista de nodos.
     *
     * @param ln Lista de nodos.
     */
    private static void eliminarCampo(List<Node> ln) {
        for (Node n : ln) {
            n.getParentNode().removeChild(n);
        }
    }

    /**
     * Dado un nodo de referencia obtiene el hijo que sigue la ruta indicada
     *
     * @param n Nodo
     * @param ruta Ruta
     * @return Nodo en ruta.
     */
    private static Node obtenerNodo(Node n, String ruta) {
        // Variables
        int indiceFinPrimer = 0; // Indice del separador
        int indiceInicioPendiente = 0; //Indice que marca el inicio de la cadena pendiente
        String primer = ""; // Nombre del primer elemento
        String pendiente = ""; // Ruta pendiente
        Node temp = null; // Nodo para los resultados temporales

        try {
            // Se obtiene el primer elemento de la ruta indicada
            indiceFinPrimer = ruta.indexOf("/");
            indiceFinPrimer = indiceFinPrimer <= 0 ? ruta.length() : indiceFinPrimer;
            indiceInicioPendiente = indiceFinPrimer == ruta.length() ? ruta.length() : indiceFinPrimer + 1;
            primer = ruta.substring(0, indiceFinPrimer);
            pendiente = ruta.substring(indiceInicioPendiente, ruta.length());

            // Se localiza el nodo
            for (int i = 0; i < n.getChildNodes().getLength(); i++) {
                if (n.getChildNodes().item(i).getNodeName().equals(primer)) {
                    temp = n.getChildNodes().item(i);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Si queda ruta por evaluar la realizamos, sino enviamos el nodo encontrado
        if (temp != null && pendiente.length() > 0) {
            return obtenerNodo(temp, pendiente);
        } else {
            return temp;
        }
    }

    // Batería de métodos auxiliares.
    /** Obtiene la etiqueta que identifica el nodo */
    private static String obtenerEtiqueta(Node n) {
        return obtenerValorDelAtributo(n, "w:tag");
    }

    /** Obtiene la etiqueta que identifica el nodo */
    private static Node obtenerNodoValorEtiqueta(Node n) {
        return obtenerNodoValorDelAtributo(n, "w:tag");
    }

    /** Obtiene la etiqueta que identifica el nodo */
    private static Node obtenerNodoValorAlias(Node n) {
        return obtenerNodoValorDelAtributo(n, "w:alias");
    }

    /** Obtiene el valor del atributo indicado para el nodo n */
    private static String obtenerValorDelAtributo(Node n, String atributo) {
        // Variables necesarias
        String value = "";
        Node ne = null;

        ne = obtenerNodoValorDelAtributo(n, atributo);
        value = ne != null ? ne.getNodeValue() : null;

        return value;
    }

    /** Obtiene nodo que contiene el atributo indicado para un nodo */
    private static Node obtenerNodoValorDelAtributo(Node n, String atributo) {
        Node temp = null;
        Node result = null;

        // Obtenemos el nodo de la etiqueta y de ella su atributo val
        temp = obtenerNodo(n, "w:sdtPr/" + atributo);

        for (int i = 0; i < temp.getAttributes().getLength() && result == null; i++) {
            Node att = temp.getAttributes().item(i);
            if (att.getNodeName().equals("w:val")) {
                result = att;
            }
        }

        return result;
    }
}
