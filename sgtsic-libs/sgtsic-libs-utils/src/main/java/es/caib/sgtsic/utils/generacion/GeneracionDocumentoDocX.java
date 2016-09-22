/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.caib.sgtsic.utils.generacion;

import com.google.common.base.Strings;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Generación de documentos a partir de plantilla.
 * Versión para documentos Office 2007 (.docx).
 * @author abastos
 */
public class GeneracionDocumentoDocX {

    // Fichero interno del .docx que incluye el contenido del documento.
    private static final String[] DYNAMIC_FILES = {"^word/document\\.xml$", "^word/header.*\\.xml$", "^word/footer.*\\.xml$"};

    /**
     * Transforma un .docx de entrada a partir de los datos proporcionados.
     * @param sourceDocX Array de bytes que representan un fichero .docx.
     * @param data Contenedor de datos.
     * @return Array de bytes que representan un fichero .docx transformado.
     * @throws es.caib.sifarma.error.Exception Ha ocurrido un error en la transformación.
     */
    public static byte[] process(byte[] sourceDocX, GeneracionDocumentoData data) throws IOException {
        ByteArrayInputStream bis = null;
        ZipInputStream zis = null;
        ByteArrayOutputStream bos = null;
        ZipOutputStream zos = null;

        try {
            bis = new ByteArrayInputStream(sourceDocX);
            zis = new ZipInputStream(bis);
            bos = new ByteArrayOutputStream();
            zos = new ZipOutputStream(bos);

            ZipEntry entryIn;
            while ((entryIn = zis.getNextEntry()) != null) {
                if (esFicheroDinamico(entryIn.getName())) {
                    processEntry(zis, zos, entryIn, data);
                } else {
                    copyEntry(zis, zos, entryIn);
                }
                zis.closeEntry();
            }

            zis.close();
            zos.flush();

            zos.close();

            byte[] out = bos.toByteArray();
            return out;
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    /**
     * Copia el archivo del ZIP de entrada sobre el ZIP de salida.
     * @param zis Stream del ZIP de entrada
     * @param zos Stream del ZIP de salida
     * @param entry Archivo de entrada
     * @throws java.io.IOException Ha ocurrido un error en la copia de datos.
     */
    private static void copyEntry(ZipInputStream zis, ZipOutputStream zos, ZipEntry entry) throws IOException {
        ZipEntry newEntry = new ZipEntry(entry.getName());
        zos.putNextEntry(newEntry);
        byte[] buff = new byte[1024];
        int n = 0;
        while ((n = zis.read(buff, 0, 1024)) > 0) {
            zos.write(buff, 0, n);
        }
        zos.closeEntry();
    }

    private static boolean esFicheroDinamico(String file) {
        for (int i = 0; i < DYNAMIC_FILES.length; i++) {
            if (file.matches(DYNAMIC_FILES[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Transforma un archivo del ZIP de entrada y lo vuelca sobre el ZIP de salida.
     * @param zis Stream del ZIP de entrada
     * @param zos Stream del ZIP de salida
     * @param entry Archivo de entrada
     * @param data Contenedor de datos para la transformación
     * @throws java.io.IOException Ha ocurrido un error en la copia de datos.
     * @throws es.caib.sifarma.error.Exception Ha ocurrido un error en la transformación.
     */
    private static void processEntry(ZipInputStream zis, ZipOutputStream zos, ZipEntry entry, GeneracionDocumentoData data) throws IOException  {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        // Copiamos el archivo de entrada sobre un String
        // (suponemos que NO es binario).
        byte[] buff = new byte[1024];
        int n = 0;
        while ((n = zis.read(buff, 0, 1024)) > 0) {
            bos.write(buff, 0, n);
        }

        String in = new String(bos.toByteArray(), "UTF-8");
        bos.close();

        // Transformamos el archivo
        String out = null;
        try {
            out = GeneracionDocumento.generarDocumento(in, data);
        } catch (Exception ex) {
            Logger.getLogger(GeneracionDocumentoDocX.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Es necesario limpiar las tabulaciones, saltos de línea
        // y espacios entre tags para que OpenOffice abra el fichero.
        out = out.replaceAll("[\r\n\t]*", "");
        out = out.replaceAll(">( )*<w:", "><w:");
        out = out.replaceAll(">( )*</w:", "></w:");

        // Hacemos un tratamiento de los ampersand (&).
        // No basta con un simple replace, ya que algunos
        // carácteres & pueden ser ya parte de entidades html (p. ej: &gt;).
        out = tratamientoAmpersand(out);
        // Volvamos el archivo transformado sobre la salida.
        ZipEntry newEntry = new ZipEntry(entry.getName());
        zos.putNextEntry(newEntry);
        ByteArrayInputStream bis = new ByteArrayInputStream(out.toString().getBytes("UTF-8"));
        n = 0;
        while ((n = bis.read(buff, 0, 1024)) > 0) {
            zos.write(buff, 0, n);
        }
        zos.closeEntry();
    }

    /**
     * Transforma un texto con saltos de línea (\n) en una colección de textos, uno para cada línea.
     * @param in Texto con saltos de línea.
     * @return Colección de líneas de texto.
     */
    public static List<GeneracionDocumentoData> generarEstructuraParrafos(String in) {
        List<GeneracionDocumentoData> parrafos = new ArrayList<GeneracionDocumentoData>();
        if (Strings.isNullOrEmpty(in)) {
            return parrafos;
        }
        GeneracionDocumentoData gen = null;
        String[] in_p = in.split("\n");
        for (String p : in_p) {
            gen = new GeneracionDocumentoData();
            gen.addParametro("TEXTO", p);
            parrafos.add(gen);
        }
        return parrafos;
    }

    /**
     * Hace un tratamiento a los carácteres & que pueden provocar errores
     * en la sintaxis XML.
     *
     * Tiene en cuenta que algunos & pueden no ser carácteres ampersand,
     * si no marcas de inicio de entidades XML que ya se introdujeron correctamente.
     *
     * @param in Contenido original
     * @return Contenido sin carácteres & que rompan la sintaxis XML.
     */
    private static String tratamientoAmpersand(String in) {
        StringBuffer out = new StringBuffer();

        // Primero sustituimos todos los & por &amp;,
        // sin saber si los & son el carácter ampersand
        // o bien el inicio de entidades xml.

        in = in.replaceAll("&", "&amp;");

        // Encontramos coincidencias de sustituciones de & por &amp;
        // que no deberíamos haber hecho, ya que el & original
        // ya formaba parte de una entidad xml.

        Pattern p = Pattern.compile("(&)(amp;)(([a-z]+;))");
        Matcher m = p.matcher(in);

        int lastCopiedPosition = 0;

        while (m.find()) {

            // Primero copiamos a la salida todo el contenido entre la última
            // copia y el nuevo resultado, ya que lo conservamos intacto.
            out.append(in.substring(lastCopiedPosition, m.start()));

            // El grupo (2) es el fragmento "amp;" que queremos eliminar.
            // Copiamos todo lo que hay antes y después.

            // El grupo (1) siempre es un &, no hace falta leerlo en el origen
            out.append("&");

            // Copiamos el grupo (3) que es el resto de la entidad xml original
            out.append(m.group(3));

            // Colocamos el cursor al final de la coincidencia.
            lastCopiedPosition = m.end();
        }

        if (lastCopiedPosition < in.length()) {
            out.append(in.substring(lastCopiedPosition, in.length()));
        }

        return out.toString();
    }

    public static void main(String arg[]){
        System.out.println(tratamientoAmpersand("jhonson&amp;jhonson &lt; hola &"));
        
    }
}
