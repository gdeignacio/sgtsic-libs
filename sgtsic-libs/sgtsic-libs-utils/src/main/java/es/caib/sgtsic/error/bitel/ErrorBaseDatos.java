package es.caib.sgtsic.error.bitel;

import es.caib.sgtsic.util.bitel.BitCadena;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * La clase ErrorBaseDatos representa un error producido en la base de datos.
 * $Id: ErrorBaseDatos.java,v 1.2 2007/02/08 13:02:30 fros Exp $
 * 
 * @author Miguel Angel Nieto
 * @author Paco Ros
 * @author Felipe Benavente
 * @date 05/01/2007
 * @version 3.0
 */
public abstract class ErrorBaseDatos extends BitError {

    /** C�digos de errores definidos */
    public static DetalleCodigoErrorBaseDatos UNIQUE_CONSTRAINT_VIOLATED = new DetalleCodigoErrorBaseDatos("\\A00001\\z", "Ja existeix un element amb les mateixes caracter�stiques.", "Ya existe un elemento con las mismas caracter�sticas.");
    public static DetalleCodigoErrorBaseDatos PARENT_KEY_NOT_FOUND = new DetalleCodigoErrorBaseDatos("\\A02291\\z", "Hi ha refer�ncies a altres elements que no existeixen.", "Hay referencias a otros elementos que no existen.");
    public static DetalleCodigoErrorBaseDatos CHILL_RECORD_FOUND = new DetalleCodigoErrorBaseDatos("\\A02292\\z", "Existeixen depend�ncies cap a l'element o elements a eliminar. Esborri primer aquestes depend�ncies.", "Existen dependencias hacia el elemento o elementos a eliminar. Borre primero dichas dependencias.");
    public static DetalleCodigoErrorBaseDatos NOT_NULL_VALUE = new DetalleCodigoErrorBaseDatos("\\A01400\\z", "Hi ha dades buides que han d'emplenar-se.", "Hay datos vacios que deben rellenarse.");
    public static DetalleCodigoErrorBaseDatos MAX_LENGTH = new DetalleCodigoErrorBaseDatos("\\A12899\\z", "Una de les dades introdu�des sobrepassa la seva grand�ria m�xima.", "Uno de los datos introducidos sobrepasa su tama�o m�ximo.");

    /** Lista que contiene todos los c�digos de error definidos */
    private static List<DetalleCodigoErrorBaseDatos> detallesCodigoErrorBaseDatos = Collections.unmodifiableList(Arrays.asList(new DetalleCodigoErrorBaseDatos[]{
        UNIQUE_CONSTRAINT_VIOLATED,
        PARENT_KEY_NOT_FOUND,
        CHILL_RECORD_FOUND,
        NOT_NULL_VALUE,
        MAX_LENGTH
    }));

    /**
     * Constructor de la clase ErrorBaseDatos.
     * @param id    C�digo del error.
     * @param error    Excepci�n/error asociado al error.
     */
    protected ErrorBaseDatos(String id, Throwable error) {
        super(id, error);
        super.setDescripcion(getDescripcionCodigoErrorBaseDatos());
        super.setDebug(error instanceof SQLException ? "Error Oracle: " + error.getMessage() : null);
    }

    /**
     * Dada una descripci�n la complementa con el c�digo de error adecuado
     * @param descripcion
     */
    @Override
    public void setDescripcion(HashMap hd) {
        // Variables necesarias
        // Nueva descripci�n
        HashMap descripcion = new HashMap();
        // Descripci�n del c�digo de error
        HashMap descripcionCodigoError;

        // Primero obtenemos la descripci�n del c�digo de error y si existe establecemos la causa
        descripcionCodigoError = getDescripcionCodigoErrorBaseDatos();

        // Recorremos cada uno de los mensajes y a�adimos la causa tanto a la descripci�n como al debug
        for (Iterator it = hd.keySet().iterator(); it.hasNext();) {
            String key = (String) it.next();
            StringBuffer sb = new StringBuffer();
            sb.append(hd.get(key));
            if (descripcionCodigoError != null && descripcionCodigoError.containsKey(key)) {
                sb.append("\n");
                if (IDIOMA_ESPANYOL.equals(key)) {
                    sb.append("Detalles: ");
                } else if (IDIOMA_CATALAN.equals(key)) {
                    sb.append("Detalls: ");
                }
                sb.append(descripcionCodigoError.get(key));
            }
            descripcion.put(key, sb.toString());
        }

        super.setDescripcion(descripcion);
    }
    
    /**
     * A�ade al mensaje de debug el mensaje de oracle y lo establece
     * @param d
     */
    @Override
    public void setDebug(String d) {
        String debug = d;
        if (getError() != null && getError() instanceof SQLException){
            debug += "\nError Oracle: " + getError().getMessage();
        }
        super.setDebug(debug);
    }

    /**
     * Encuentra la descripci�n del error correspondiente para el error actual
     * @return
     */
    private HashMap getDescripcionCodigoErrorBaseDatos() {
        DetalleCodigoErrorBaseDatos dcebd = getDetalleCodigoErrorBaseDatos();
        // Si no hemos localizado ninguno devolvemos null
        return dcebd != null ? dcebd.descripcion : null;
    }

    public DetalleCodigoErrorBaseDatos getDetalleCodigoErrorBaseDatos (){
            // VAriables necesarias
        int codigoError = 0;

        // Obtenemos el c�digo de error
        if (getError() != null && getError() instanceof SQLException){
            codigoError = ((SQLException)getError()).getErrorCode();
        }

        if (codigoError > 0){
            // recorremos todos los errores hasta encontrar uno v�lido y devolvemos los mensajes
            for (DetalleCodigoErrorBaseDatos t : detallesCodigoErrorBaseDatos){
                if (t.esValida(codigoError)){
                    return t;
                }
            }
        }

        return null;
    }

    /**
     * Definici�n de la clase contenedora de los detalles de un c�digo de error
     */
    public static class DetalleCodigoErrorBaseDatos{

        private String expresionRegular = null;
        private HashMap descripcion = null;

        /**
         * Constructor de la calse
         * @param expresionRegular expresi�n regular que determinar� los c�digos de error v�lidos para su descripci�n
         * @param mensajeCatalan descripci�n del error en catalan
         * @param mensajeCastellano descripci�n del error en castellano
         */
        public DetalleCodigoErrorBaseDatos(String expresionRegular, String mensajeCatalan, String mensajeCastellano){
            descripcion = new HashMap();
            descripcion.put(IDIOMA_CATALAN, mensajeCatalan);
            descripcion.put(IDIOMA_ESPANYOL, mensajeCastellano);
            this.expresionRegular = expresionRegular;
        }

        /**
         * Dado un c�digo de error devuelve verdadero si la instancia es responsable de dicho c�digo
         * @param codigo
         * @return
         */
        public boolean esValida(int codigo){
            Pattern p = Pattern.compile(this.expresionRegular);
            Matcher m = p.matcher(format(codigo));
            return m.find();
        }

        private String format (int codigo){
            return BitCadena.lpad(String.valueOf(codigo), 5, '0');
        }

        public HashMap getDescripcion() {
            return descripcion;
        }
        
    }
}
