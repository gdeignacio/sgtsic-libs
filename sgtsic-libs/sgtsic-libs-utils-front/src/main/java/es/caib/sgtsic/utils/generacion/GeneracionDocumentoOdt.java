/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.caib.sgtsic.utils.generacion;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import net.sf.jooreports.templates.DocumentTemplate;
import net.sf.jooreports.templates.DocumentTemplateException;
import net.sf.jooreports.templates.DocumentTemplateFactory;

/**
 * Generación de documentos a partir de plantilla.
 * Versión para documentos OpenOffice 3 (.odt).
 * @author abastos
 */
public class GeneracionDocumentoOdt {

    /**
     * Transforma un .odt de entrada a partir de los datos proporcionados.
     * @param sourceOdt Array de bytes que representan un fichero .odt.
     * @param data Contenedor de datos.
     * @return Array de bytes que representan un fichero .odt transformado.
     * @throws es.caib.sifarma.error.ErrorDocumentoGenerar Ha ocurrido un error en la transformación.
     */
    public static byte[] process(byte[] sourceOdt, Map<String, Object> data) throws IOException, DocumentTemplateException  {
        return process(sourceOdt, data, new TransformadorOdt());
    }

    /**
     * Transforma un .odt de entrada a partir de los datos proporcionados.
     * @param sourceOdt Array de bytes que representan un fichero .odt.
     * @param data Contenedor de datos.
     * @param trans Transformador a aplicar al documento (hereda del transformador base).
     * @return Array de bytes que representan un fichero .odt transformado.
     * @throws es.caib.sifarma.error.ErrorDocumentoGenerar Ha ocurrido un error en la transformación.
     */
    public static byte[] process(byte[] sourceOdt, Map<String, Object> data, TransformadorOdt trans) throws IOException, DocumentTemplateException  {
        byte[] res = null;
        ByteArrayOutputStream bos = null;
        ByteArrayInputStream bais = null;
        try {
            DocumentTemplateFactory factory = new DocumentTemplateFactory();
            bais = new ByteArrayInputStream(sourceOdt);
            DocumentTemplate tmpl = factory.getTemplate(bais);
            // Utilizamos un transformador para convertir los saltos de línea en formato \n
            tmpl.setContentWrapper(trans);
            bos = new ByteArrayOutputStream();
            // Con la actualización a jodreports2.3.0, por defecto no se procesan los
            // input field que incluyen la variable en la descripción.
            // Modificamos el parámetro para que continúe teniéndolos en cuenta.
            tmpl.getConfigurations().put("process_jooscript_only", Boolean.FALSE);
            tmpl.createDocument(data, bos);
            res = bos.toByteArray();
            return res;
        } catch (IOException e) {
            throw new IOException(e);
        } catch (DocumentTemplateException e) {
            throw new DocumentTemplateException(e);
        } finally {
            if (bais != null) {
                try {
                    bais.close();
                } catch (IOException ex) {
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    /**
     * Revisa un ODT que contiene valores para  sus variables y define en el mapa su valor a "" si no se han definido.
     * @param map Mapa de valores
     * @param tipoPlantilla array de bytes con el tipo de plantilla
     */
    public void normalizarMapa(Map map, byte[] tipoPlantilla) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(tipoPlantilla);
            ZipInputStream zis = new ZipInputStream(bais);
            String contentXML = null;
            ZipEntry zen = null;
            while (contentXML == null && (zen = zis.getNextEntry()) != null) {
                if (zen.getName().equals("content.xml")) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] buff = new byte[1024];
                    int n = 0;
                    while ((n = zis.read(buff, 0, 1024)) > 0) {
                        bos.write(buff, 0, n);
                    }

                    contentXML = new String(bos.toByteArray(), "UTF-8");
                    bos.close();
                }
            }

            zis.close();

            if (contentXML != null) {
                String variable;
                int start = 0, end = 0;

                ArrayList<String> al = new ArrayList<String>();
                do {
                    start = contentXML.indexOf("${", end);
                    end = contentXML.indexOf('}', start);
                    if (start >= 0 && end >= 0) {
                        variable = contentXML.substring(start + 2, end);
                        al.add(variable);
                    }
                } while (start >= 0 && end >= 0);

                for (String aux : al) {
                    if (!map.containsKey(aux)) {
                        map.put(aux, " ");
                    }
                }
            }

        } catch (IOException ex) {
            // tratar al gusto.
        }
    }
}
