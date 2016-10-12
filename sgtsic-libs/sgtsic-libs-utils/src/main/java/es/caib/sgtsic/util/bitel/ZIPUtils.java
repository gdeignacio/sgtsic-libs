package es.caib.sgtsic.util.bitel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZIPUtils {

    /**
     * Descomprime un fichero con formato ZIP en el directorio dir usando is para leer el fichero.
     * Ojo: Este m�todo no cierra is.
     *
     * @param dir Directorio de destino
     * @param is InputStream de donde obtener el ZIP.
     */
    public static void descomprimirZIP(File dir, InputStream is) throws IOException {
        HashSet createdDirs = new HashSet();
        // Descompresi�n
        ZipInputStream zis = new ZipInputStream(is);

        ZipEntry zen = zis.getNextEntry();
        while (zen != null) {
            String entryName = zen.getName();

            // tratamiento especial si es un directorio
            if (entryName.indexOf(File.separatorChar) >= 0) {
                File f = null;
                StringBuffer dirChain = new StringBuffer();

                String dirNames = entryName.substring(0, entryName.lastIndexOf(File.separatorChar));

                StringTokenizer stk = new StringTokenizer(dirNames, File.separator);
                while (stk.hasMoreTokens()) {
                    String dirName = stk.nextToken();
                    dirChain.append(File.separatorChar).append(dirName);
                    f = new File(dir.getAbsolutePath() + dirChain);
                    if (!f.exists()) {
                        f.mkdir();
                    }
                }
            }

            if (!(dir.getAbsolutePath() + File.separatorChar + entryName).endsWith(File.separator)) {
                int n;
                FileOutputStream fos = new FileOutputStream(dir.getAbsolutePath() + File.separatorChar + entryName);
                byte[] buf = new byte[1024];
                while ((n = zis.read(buf, 0, 1024)) > -1) {
                    fos.write(buf, 0, n);
                }


                fos.close();
            }
            zis.closeEntry();

            zen = zis.getNextEntry();
        }

        zis.close();
    }

    /**
     * Crea un fichero en formato ZIP en el descriptor "zip" a partir de los contenidos del directorio dir.
     *
     * @param dir Descriptor del directorio origen.
     * @param zip Descriptor del fichero zip destino.
     */
    public static void comprimirDirectorioFormatoZIP(File dir, File zip) throws FileNotFoundException, IOException {
        FileOutputStream fos = new FileOutputStream(zip);
        ZipOutputStream zos = new ZipOutputStream(fos);
        zos.setLevel(3);
        File[] files = dir.listFiles();
        addToZip("", files, zos);

        zos.close();
    }

    /**
     * A�ade recursivamente un �rbol de directorios a un ZipOutputStream.
     * Los directorios vac�os no se a�aden.
     *
     * @param files Lista de ficheros a a�adir
     * @param zos Stream de salida al ZIP.
     * @throws IOException
     */
    private static void addToZip(String parentPath, File[] files, ZipOutputStream zos) throws IOException {
        // Recorrido por los contenidos de dir para ir incluyendo ficheros en el ZIP.
        for (File file : files) {
            if (file.isDirectory()) {
                addToZip(parentPath + file.getName() + File.separator, file.listFiles(), zos); // recursividad para a�adir el contenido del directorio.
            } else {
                ZipEntry zen = new ZipEntry(parentPath + File.separatorChar + file.getName()); // ojo porque parentPath no puede empezar por "/"

                // Creaci�n de la entrada
                zos.putNextEntry(zen);
                
                // Escritura del fichero en el ZIP
                FileInputStream fis = new FileInputStream(file);

                int len;
                byte[] buf = new byte[1024];
                while ((len = fis.read(buf)) > 0) {
                    zos.write(buf, 0, len);
                }

                fis.close();

                // Cierre de la entrada.
                zos.closeEntry();
            }

            
        }
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        FileInputStream fis = new FileInputStream("/tmp/estadistica.ods");
        File f = new File("/tmp/testout.ods");

        if (f.exists()) {
            f.delete();
        }
        f.mkdir();

        descomprimirZIP(f, fis);

        File nuevoZip = new File("/tmp/nuevoZip.ods");
        comprimirDirectorioFormatoZIP(f, nuevoZip);
    }
}
