package es.caib.sgtsic.util.bitel;


import es.caib.sgtsic.error.bitel.BitError;
import es.caib.sgtsic.error.bitel.ErrorConversion;
import java.util.*;

import java.text.*;

import java.lang.reflect.*;

public final class BitCadena {

    /** Variable para disponer de una cadena vacia*/
    public static String CADENA_VACIA = "";

    /* Palabras que no deben ser transformadas con may�sculas inicial. */
    public static List NO_CAPITALIZE = Arrays.asList(new String[]{"de", "del", "el", "la", "y", "a", "e", "i", "o", "u", "d", "l", "s"});

    /**
     * Funci�n que determina si una cadena es vacia o no. En el caso de que sea null
     * o la longitud sea cero es vacia. En caso contrario no es vacia
     *
     * @param s cadena a determinar si es vacia
     * @return true si la cadena es vacia, false en caso contrario
     **/
    public static boolean esCadenaVacia(String s) {
        return (s == null || s.length() == 0);
    }

    /**
     * Funci�n que resume la lomgitud una URL mediante puntos suspensivos a 50 caracteres
     *
     * @param s cadena a resumir
     * @return String
     **/
    public static String resumeURL(String s) {
        return resumeURL(s, 50);
    }

    /**
     * Funci�n que resume la lomgitud una URL mediante puntos suspensivos
     *
     * @param s cadena a resumir
     * @param l longitud de la cadena resumida
     * @return String
     **/
    public static String resumeURL(String s, int l) {
        if ((s == null) || (s.length() < l + 1)) {
            return s;
        }

        StringBuffer sb = new StringBuffer();
        sb.append(s.substring(0, 23));
        sb.append("...");
        sb.append(s.substring(s.length() - 23));

        return sb.toString();
    }

    /**
     * Funcion que formatea un double devolviendo un String con dos decimales
     *
     * @param numero n�mero a formatear
     * @return String
     **/
    public static String getFormatoPuntoFlotante(double numero) {
        boolean esNegativo = (numero < 0);
        if (esNegativo) {
            numero = (-1) * numero;
        }

        DecimalFormat formato = new DecimalFormat("############0.00");
        formato.setMaximumFractionDigits(2);
        formato.setMinimumFractionDigits(2);
        formato.setGroupingSize(12);
        formato.setMaximumIntegerDigits(12);
        formato.setMinimumIntegerDigits(1);

        if (esNegativo) {
            return "-" + formato.format(numero).replace(',', '.');
        } else {
            return formato.format(numero).replace(',', '.');
        }
    }

    /**
     * Funcion NVL que devuelve la cadena o el valor por defecto en caso de que la cadena sea nula
     * @param s cadena a comparar
     * @param valorPorDefecto
     * @return String
     **/
    public static String NVL(String s, String valorPorDefecto) {
        if (s == null) {
            return valorPorDefecto;
        } else {
            return s;
        }

    }

    /**
     * Pone la primera letra de un atributo en Mayusculas
     * @param s nombre del atributo a capitalizar
     * @return String
     **/
    public static String capitalize(String s) {
        if (s == null) {
            return null;
        }
        if (s.length() == 0) {
            return BitCadena.CADENA_VACIA;
        }
        if (s.length() == 1) {
            return s.toUpperCase();
        }
        if (s.length() > 1) {
            char primera = Character.toUpperCase(s.charAt(0));
            String capitalizado = primera + s.substring(1);
            return capitalizado;
        } else {
            return ("[error capitalizando]");
        }
    }

    /**
     * Transforma un texto de entrada pasando a may�scula la primera letra de cada palabra.
     * Ej.: "PRUEBAS DE TEXTO DE S'HOSPITAL DEL SR.P�REZ" --> "Pruebas de Texto de s'Hospital del Sr.P�rez"
     * @param in Texto de entrada
     * @return Texto de salida
     */
    public static String capitalizeWords(String in) {
        if (esCadenaVacia(in)) {
            return CADENA_VACIA;
        }
        StringBuffer out = new StringBuffer();
        in = in.toLowerCase();
        StringTokenizer st = new StringTokenizer(in, "[ \\.\\'\\\";-,\\(\\)]", true);

        while (st.hasMoreTokens()) {
            String s = st.nextToken();
            if (!esCadenaVacia(s)) {
                if (NO_CAPITALIZE.contains(s) && out.length() > 0) {
                    out.append(s);
                } else {
                    out.append(capitalize(s));
                }
            }
        }
        return out.toString();
    }

    /**
     * Devuelve la lista de atributos de un objeto con sus valores
     * @param Objeto
     * @return String
     **/
    public static String doToString(Object o) {
        if (o == null) {
            return null;
        }

        Class c = o.getClass();
        StringBuffer sb = new StringBuffer("");
        String nombreAtributo = null;

        Field[] fields = c.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];

            nombreAtributo = field.getName();
            if (!Modifier.isStatic(field.getModifiers())) {
                Method m = null;
                try {
                    m = c.getMethod("get" + capitalize(nombreAtributo));
                } catch (Exception e) {
                    try {
                        m = c.getMethod("is" + capitalize(nombreAtributo));
                    } catch (Exception ex) {
                        System.out.println("Error a l'atribut: " + nombreAtributo);
                        ex = null;
                    }
                    e = null;
                }

                sb.append(nombreAtributo);
                sb.append(": ");
                try {
                    sb.append(m.invoke(o));
                } catch (Exception ex) {
                    System.out.println("Error al Invocar el metode " + m.getName());
                    ex = null;
                }

                sb.append("\n");
            }
        }

        return sb.toString();
    }

    /** Convierte el n�mero pasado a formato euro en castellano,
     * siempre con dos decimales.
     * Param: recibe un double de dos decimales como mucho
     * Return: devuelve un string que representa un double formateado
     **/
    public static String getFormatoEuro(double numero) {

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();

        // Configuramos el separador de miles
        symbols.setGroupingSeparator('.');

        // Configuramos el separador de decimales
        symbols.setDecimalSeparator(',');

        // Configuramos el modelo que sigue el numero con dos decimales exactamente
        DecimalFormat formato = new DecimalFormat("###,##0.00", symbols);

        // Variable a la que se asigna el resultado
        StringBuffer num = new StringBuffer();

        formato.format(numero, num, new FieldPosition(0));

        return num.toString();
    }

    /** Convierte el String conteniendo un n�mero pasado a formato euro en castellano,
     * siempre con dos decimales, a double
     * @param n�mero a convertir
     * @return float
     **/
    public static double getFormatoEuroToDouble(String numero) {

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();

        // Configuramos el separador de miles
        symbols.setGroupingSeparator('.');

        // Configuramos el separador de decimales
        symbols.setDecimalSeparator(',');

        // Configuramos el modelo que sigue el numero con dos decimales exactamente
        DecimalFormat formato = new DecimalFormat("###,##0.00", symbols);

        try {
            Number num = formato.parse(numero);
            return num.doubleValue();
        } catch (Exception e) {
            e = null;
            return 0.0;
        }
    }

    /** Metodo que permite substituir todos los caracteres de ' de una
     *  cadena por \' para evitar los errores de javascript
     * @param cadenabd String que contiene al cadena con los caracteres a reemplazar.
     * @return String con los caracteres reeplazados.
     **/
    public static String replaceComilla(String cadena) {
        if (cadena == null || cadena.length() == 0) {
            return cadena;
        }

        StringTokenizer tokens = new StringTokenizer(cadena, "'");
        String nuevaCadena = "";
        while (tokens.hasMoreTokens()) {
            nuevaCadena = nuevaCadena + tokens.nextToken() + "\\'";
        }
        return nuevaCadena.substring(0, nuevaCadena.length() - 2);
    }

    /** Metodo que permite substituir todos los caracteres de fin de linea de una
     *  cadena por \\n
     * @param cadenabd String que contiene al cadena con los caracteres a reemplazar.
     * @return String con los caracteres reeplazados.
     **/
    public static String replaceFinLinea(String cadenabd) {
        if (cadenabd == null || cadenabd.length() == 0) {
            return cadenabd;
        }
        StringTokenizer tokens = new StringTokenizer(cadenabd, "\n");
        String cadena1 = "";
        while (tokens.hasMoreTokens()) {
            cadena1 = cadena1 + tokens.nextToken() + "\\n";
        }
        tokens = new StringTokenizer(cadena1, "\r");
        String cadena2 = "";
        while (tokens.hasMoreTokens()) {
            cadena2 = cadena2 + tokens.nextToken() + "";

        }
        return cadena2;
    }

    /** Metodo que permite substituir todos los caracteres de \n de una
     *  cadena por <br> para evitar los errores de javascript
     * @param cadena String que contiene al cadena con los caracteres a reemplazar.
     * @return String con los caracteres reeplazados.
     **/
    public static String replaceReturn(String cadena) {
        if (cadena == null || cadena.length() == 0) {
            return cadena;
        }
        StringTokenizer tokens = new StringTokenizer(cadena, "\n");
        String nuevaCadena = "";
        while (tokens.hasMoreTokens()) {
            nuevaCadena = nuevaCadena + tokens.nextToken() + "<br>";
        }
        return nuevaCadena.substring(0, nuevaCadena.length());
    }

    /**
     * M�todo para convertir una cadena a un entero de forma segura.
     * @param cad cadena que queremos convertir a int
     * @param num valor que retornaremos si se produce una Exception
     * @return retorna la conversi�n a entero de cad o num si se produce una
     * excepcion
     * @author ELU
     */
    public static int getInt(String cad, int num) {
        try {
            return Integer.parseInt(cad);
        } catch (Exception e) {
            return num;
        }
    }

    /**
     * M�todo para convertir una cadena a un entero de forma segura.
     * @param cad cadena que queremos convertir a int
     * @return retorna la conversi�n a entero de cad o 0 si se produce una
     * excepcion
     * @author ELU
     */
    public static int getInt(String cad) {
        return BitCadena.getInt(cad, 0);
    }

    /**
     * Rellena una cadena de texto con un caracter por la derecha hasta completar
     * una determinada longitud
     *
     * @param cadena Texto original
     * @param longitud Longitud que tendr� la cadena de texto despu�s de aplicar la funci�n
     * @param relleno Caracter de relleno a utilizar en caso de que cadena no tenga la longitud suficiente
     */
    public static String rpad(String cadena, int longitud, char relleno) {
        String s = (cadena != null) ? cadena : "";
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < longitud; i++) {
            if (i < s.length()) {
                sb.append(s.charAt(i));
            } else {
                sb.append(relleno);
            }
        }

        return sb.toString();
    }

    /**
     * Rellena una cadena de texto con un caracter por la izquierda hasta completar
     * una determinada longitud
     *
     * @param cadena Texto original
     * @param longitud Longitud que tendr� la cadena de texto despu�s de aplicar la funci�n
     * @param relleno Caracter de relleno a utilizar en caso de que cadena no tenga la longitud suficiente
     */
    public static String lpad(String cadena, int longitud, char relleno) {
        String s = (cadena != null) ? cadena : "";
        StringBuffer sb = new StringBuffer(s);
        sb.reverse();

        sb = new StringBuffer(rpad(sb.toString(), longitud, relleno));

        return sb.reverse().toString();
    }

    /**
     * Devuelve la representaci�n textual en catal�n del n�mero especificado por par�metro
     * @param numero representa un double con el n�mero del que queremos representar en catal�n
     * @return retorna un String con la representaci�n textual del n�mero especificado
     * @throws BitError, la funci�n lanza un ErrorConversion si se produce cualquier excepcion del tipo que sea
     */
    public static String getFormatoTextual(double numero) throws BitError {

        try{
            String parteEntera = "";
            String parteDecimal = "";
            String numeroConvertir = BitCadena.getFormatoEuro(numero);
            numeroConvertir = numeroConvertir.replace(".", "");
            String decimales = "";

            int index = numeroConvertir.indexOf(',');

            if (index != -1) {
                parteEntera = numeroConvertir.substring(0, index);
                parteDecimal = numeroConvertir.substring(index + 1, numeroConvertir.length());
            } else {
                parteEntera = numeroConvertir;
                parteDecimal = "0";
            }

            String resultadoEntero = getNumberLiteral(Long.parseLong(parteEntera)) + " euros";

            if (parteDecimal.equals("00") || parteDecimal.equals("0") || parteDecimal.equals("")) {
                decimales = "";
            } else {
                String resultadoDecimal = getNumberLiteral(Long.parseLong(parteDecimal));
                decimales = " amb " + resultadoDecimal + " c�ntims";
            }
            return resultadoEntero + decimales;
        }catch(Exception e){
            throw new ErrorConversion(""+numero,e);
        }
    }

    /**
     * Calcula los literales de un numero a partir de unidades, decenas y centenas
     *
     * @return la cadena de texto resultante.
     */
    private static String letras(String c, String d, String u) {

        int centenas, decenas, decom;
        String lc = "";
        String ld = "";
        String lu = "";

        centenas = Integer.parseInt(c);
        decenas = Integer.parseInt(d);
        decom = Integer.parseInt(u);
        switch (centenas) {
            case 0:
                lc = "";
                break;
            case 1:
                 {
                    if (decenas == 0 && decom == 0) {
                        lc = "Cent";
                    } else {
                        lc = "Cent ";
                    }
                }
                break;
            case 2:
                lc = "Dos-cents ";
                break;
            case 3:
                lc = "Tres-cents ";
                break;
            case 4:
                lc = "Quatre-cents ";
                break;
            case 5:
                lc = "Cinc-cents ";
                break;
            case 6:
                lc = "Sis-cents ";
                break;
            case 7:
                lc = "Set-cents ";
                break;
            case 8:
                lc = "Vuit-cents ";
                break;
            case 9:
                lc = "Nou-cents ";
                break;
        }
        switch (decenas) {
            case 0:
                ld = "";
                break;
            case 1:
                 {
                    switch (decom) {
                        case 0:
                            ld = "Deu";
                            break;
                        case 1:
                            ld = "Onze";
                            break;
                        case 2:
                            ld = "Dotze";
                            break;
                        case 3:
                            ld = "Treze";
                            break;
                        case 4:
                            ld = "Catorze";
                            break;
                        case 5:
                            ld = "Quinze";
                            break;
                        case 6:
                            ld = "Setze";
                            break;
                        case 7:
                            ld = "Decet";
                            break;
                        case 8:
                            ld = "Divuit";
                            break;
                        case 9:
                            ld = "Dinou";
                            break;
                    }
                }
                break;
            case 2:
                ld = "Vint";
                break;
            case 3:
                ld = "Trenta";
                break;
            case 4:
                ld = "Quaranta";
                break;
            case 5:
                ld = "Cinquanta";
                break;
            case 6:
                ld = "Seixanta";
                break;
            case 7:
                ld = "Setanta";
                break;
            case 8:
                ld = "Vuitanta";
                break;
            case 9:
                ld = "Noranta";
                break;
        }
        switch (decom) {
            case 0:
                lu = "";
                break;
            case 1:
                lu = "Un";
                break;
            case 2:
                lu = "Dos";
                break;
            case 3:
                lu = "Tres";
                break;
            case 4:
                lu = "Quatre";
                break;
            case 5:
                lu = "Cinc";
                break;
            case 6:
                lu = "Sis";
                break;
            case 7:
                lu = "Set";
                break;
            case 8:
                lu = "Vuit";
                break;
            case 9:
                lu = "Nou";
                break;
        }

        if (decenas == 1) {
            return lc + ld;
        }
        if (decenas == 0 || decom == 0) {
            return lc + " " + ld + lu;
        } else {
            if (decenas == 2) {
                ld = "Vint i ";
                return lc + ld + lu.toLowerCase();
            } else {
                return lc + ld + " " + lu;
            }
        }

    }

    private static String getNumberLiteral(long n) {

        long m0 = n / 1000000000000L;
        long rm0 = n % 1000000000000L;
        long m1 = rm0 / 100000000000L;
        long rm1 = rm0 % 100000000000L;
        long m2 = rm1 / 10000000000L;
        long rm2 = rm1 % 10000000000L;
        long m3 = rm2 / 1000000000L;
        long rm3 = rm2 % 1000000000L;
        long cm = rm3 / 100000000L;
        long r1 = rm3 % 100000000L;
        long dm = r1 / 10000000L;
        long r2 = r1 % 10000000L;
        long um = r2 / 1000000L;
        long r3 = r2 % 1000000L;
        long cmi = r3 / 100000L;
        long r4 = r3 % 100000L;
        long dmi = r4 / 10000L;
        long r5 = r4 % 10000L;
        long umi = r5 / 1000L;
        long r6 = r5 % 1000L;

        long ce = r6 / 100L;
        long r7 = r6 % 100L;
        long de = r7 / 10L;
        long r8 = r7 % 10L;
        long un = r8 / 1L;

        if (n < 1000000000000L && n >= 1000000000L) {
            String tmp = "" + n;
            int s = tmp.length();
            String tmp1 = tmp.substring(0, s - 9);
            String tmp2 = tmp.substring(s - 9, s);

            String tmpn1 = getNumberLiteral(Long.parseLong(tmp1));
            String tmpn2 = getNumberLiteral(Long.parseLong(tmp2));

            String pred;
            if (tmpn1.indexOf("Un") >= 0) {
                pred = " Bili� ";
            } else {
                pred = " Bilions ";
            }

            return tmpn1 + pred + tmpn2;
        }

        if (n < 10000000000L && n >= 1000000L) {
            String mldata = letras(String.valueOf(cm), String.valueOf(dm), String.valueOf(um));

            String hlp = mldata.replace("Un", "*");

            if (hlp.indexOf("*") < 0 || hlp.indexOf("*") > 3) {
                mldata = mldata.replace("Un", "un");
                mldata += " Milions ";
            } else {
                mldata = "Un Mili� ";
            }
            String mdata = letras(String.valueOf(cmi), String.valueOf(dmi), String.valueOf(umi));
            String cdata = letras(String.valueOf(ce), String.valueOf(de), String.valueOf(un));

            if (!mldata.equals("	")) {
                if (n == 1000000) {
                    mdata = mdata.replace("Un", "un") + "de";
                } else {
                    mdata = mdata.replace("Un", "un") + " mil ";
                }
            }

            return (mldata + mdata + cdata);
        }
        if (n < 1000000 && n >= 1000) {
            String mdata = letras(String.valueOf(cmi), String.valueOf(dmi), String.valueOf(umi));
            String cdata = letras(String.valueOf(ce), String.valueOf(de), String.valueOf(un));

            String hlp = mdata.replace("Un", "*");

            if (hlp.indexOf("*") < 0 || hlp.indexOf("*") > 3) {
                mdata = mdata.replace("Uno", "un");
                return (mdata + " mil " + cdata);
            } else {
                return ("Mil " + cdata);
            }
        }
        if (n < 1000 && n >= 1) {
            return (letras(String.valueOf(ce), String.valueOf(de), String.valueOf(un)));
        }
        if (n == 0) {
            return " Zero";
        }
        return "No disponible";
    }
}
