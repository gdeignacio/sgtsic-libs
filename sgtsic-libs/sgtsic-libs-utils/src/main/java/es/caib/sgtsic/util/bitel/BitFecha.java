package es.caib.sgtsic.util.bitel;

import es.caib.sgtsic.data.TiempoData;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;
import java.text.DateFormat;

import java.util.*;

import java.sql.Timestamp;

public class BitFecha {

    private final static SimpleDateFormat sdf1 = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("CA"));
    private final static SimpleDateFormat sdf2 = new SimpleDateFormat("d 'd''\'MMMM 'de' yyyy", new Locale("CA"));

/**
     * Devuelve la fecha formateada del tipo "dd [de|d'] mmmm de yyyy".
     * @param d Fecha
     * @return Fecha formateada
     */
    public static String formateaFechaCatalan(Date d) {
        if (d == null) {
            return null;
        }
        String s;
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        switch (c.get(Calendar.MONTH)) {
            case Calendar.APRIL:
                s = sdf2.format(c.getTime());
                break;
            case Calendar.AUGUST:
                s = sdf2.format(c.getTime());
                break;
            case Calendar.OCTOBER:
                s = sdf2.format(c.getTime());
                break;
            default:
                s = sdf1.format(c.getTime());
        }
        return s;
    }

    /**
     * M�todo que trunca una fecha
     *
     * @param  Timestamp fecha a truncar
     * @return Timestamp (fecha final)
     */
    public static Timestamp truncarFecha(Timestamp fecha) {
        return truncarFecha(fecha, 0);
    }

    /**
     * M�todo que trunca una fecha y le suma un n�mero de dias
     *
     * @param  Timestamp fecha a truncar
     * @param  int n�mero de dias a sumar
     * @return Timestamp (fecha final)
     */
    public static Timestamp truncarFecha(Timestamp fecha, int sumarDias) {
        if (fecha == null) {
            return null;
        }

        GregorianCalendar gc = new GregorianCalendar();

        gc.setTime(new java.sql.Date(fecha.getTime()));
        gc.set(Calendar.HOUR_OF_DAY, 0);
        gc.set(Calendar.MINUTE, 0);
        gc.set(Calendar.SECOND, 0);
        gc.set(Calendar.MILLISECOND, 0);
        gc.add(GregorianCalendar.DAY_OF_YEAR, sumarDias);

        return new Timestamp(gc.getTime().getTime());
    }

    /**
     * M�todo que calcula la diferencia entre dos fechas, y devuelve un objeto TiempoData con la informaci�n
     *
     * @param  Timestamp desde (fecha inicial)
     * @param  Timestamp hasta (fecha final)
     * @return Timestamp (fecha final)
     */
    TiempoData diferenciaEntreFechas(Timestamp desde, Timestamp hasta) {

        long diferencia = hasta.getTime() - desde.getTime();

        TiempoData tie = new TiempoData();
        int dias = (int) (diferencia / 86400000);
        tie.setDias(dias);
        int resto = (int) (diferencia % 86400000);
        int horas = resto / 3600000;
        tie.setHoras(horas);
        resto = resto % 3600000;
        int minutos = resto / 60000;
        tie.setMinutos(minutos);
        resto = resto % 60000;
        int segundos = (resto % 60000) / 1000;
        tie.setSegundos(segundos);
        int milisegundos = resto % 1000;
        tie.setMilisegundos(milisegundos);

        return tie;
    }

    public static boolean esFechaCorrecta(String dateString) {
        if (dateString == null) {
            return false;
        }
        if (dateString.length() != 10) {
            return false;
        }
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            ParsePosition pos = new ParsePosition(0);
            Date currentTime_2 = formatter.parse(dateString, pos);
            Timestamp fechaSql = new java.sql.Timestamp(currentTime_2.getTime());
            fechaSql = null;
            return true;
        } catch (Exception e) {
            e = null;
            return false;
        }
    }

    public static java.util.Date getFecha(String fecha) {
        try {
            return DateFormat.getDateInstance().parse(fecha);
        } catch (Exception e) {
            e = null;
            return null;
        }
    }

    public static java.sql.Timestamp getFechaActual() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date currentTime_1 = new java.util.Date();
            String dateString = formatter.format(currentTime_1);

            ParsePosition pos = new ParsePosition(0);
            java.util.Date currentTime_2 = formatter.parse(dateString, pos);
            java.sql.Timestamp fechaSql = new java.sql.Timestamp(currentTime_2.getTime());
            return fechaSql;
        } catch (Exception e) {
            e = null;
            return null;
        }
    }

    public static java.sql.Timestamp getFechaCompletaActual() {
        // A diferencia de la anterior esta devuelve tambien hh:mm:ss:ms
        return new Timestamp(System.currentTimeMillis());
    }

    public static String getFechaActualString() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date currentTime_1 = new java.util.Date();
            String dateString = formatter.format(currentTime_1);
            return dateString;
        } catch (Exception e) {
            e = null;
            return null;
        }
    }

    public static String getHoraActualString() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            java.util.Date currentTime_1 = new java.util.Date();
            String dateString = formatter.format(currentTime_1);
            return dateString;
        } catch (Exception e) {
            e = null;
            return null;
        }
    }

    public static java.sql.Timestamp creaFecha(String dateString, String timeString) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            ParsePosition pos = new ParsePosition(0);
            java.util.Date currentTime_2 = formatter.parse(dateString + " " + timeString, pos);
            java.sql.Timestamp fechaSql = new java.sql.Timestamp(currentTime_2.getTime());

            return fechaSql;
        } catch (Exception e) {
            e = null;
            return null;
        }
    }

    public static java.sql.Timestamp creaFechaConSegundos(String dateString, String timeString) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            ParsePosition pos = new ParsePosition(0);
            java.util.Date currentTime_2 = formatter.parse(dateString + " " + timeString, pos);
            java.sql.Timestamp fechaSql = new java.sql.Timestamp(currentTime_2.getTime());

            return fechaSql;
        } catch (Exception e) {
            e = null;
            return null;
        }
    }

    public static java.sql.Timestamp creaFecha(String dateString) {
        return creaFecha(dateString, "00:00");
    }

    public static String getFechaFormateada(java.sql.Timestamp fecha, String formato) {
        try {
            if (fecha == null) {
                return "";
            }
            SimpleDateFormat formatter = new SimpleDateFormat(formato);
            return formatter.format(fecha);
        } catch (Exception e) {
            e = null;
            return null;
        }
    }

    public static String getHoraFormateadaEstandar(java.sql.Timestamp fecha) {
        try {
            return getFechaFormateada(fecha, "HH:mm");
        } catch (Exception e) {
            e = null;
            return null;
        }
    }

    public static String getFechaFormateadaEstandar(java.sql.Timestamp fecha) {
        try {
            return getFechaFormateada(fecha, "dd/MM/yyyy");
        } catch (Exception e) {
            e = null;
            return null;
        }
    }

    public static String getDiaActual() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd");
            return formatter.format(new java.util.Date());
        } catch (Exception e) {
            e = null;
            return null;
        }
    }

    public static String getAnyoActual() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
            return formatter.format(new java.util.Date());
        } catch (Exception e) {
            e = null;
            return null;
        }
    }

    public static String getMesActual() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("MM");
            return formatter.format(new java.util.Date());
        } catch (Exception e) {
            e = null;
            return null;
        }
    }

    public static String getDia(String dateString) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date fechaSql = formatter.parse(dateString);
            SimpleDateFormat formatter2 = new SimpleDateFormat("dd");
            return formatter2.format(fechaSql);
        } catch (Exception e) {
            e = null;
            return null;
        }
    }

    public static String getAno(String dateString) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date fechaSql = formatter.parse(dateString);
            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy");
            return formatter2.format(fechaSql);
        } catch (Exception e) {
            e = null;
            return null;
        }
    }

    public static String getMes(String dateString) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date fechaSql = formatter.parse(dateString);
            SimpleDateFormat formatter2 = new SimpleDateFormat("MM");
            return formatter2.format(fechaSql);
        } catch (Exception e) {
            e = null;
            return null;
        }
    }

    public static String getNombreMes(int mes) {
        switch (mes) {
            case 1:
                return "de Gener";
            case 2:
                return "de Febrer";
            case 3:
                return "de Mar�";
            case 4:
                return "d'Abril";
            case 5:
                return "de Maig";
            case 6:
                return "de Juny";
            case 7:
                return "de Juliol";
            case 8:
                return "d'Agost";
            case 9:
                return "de Setembre";
            case 10:
                return "d'Octubre";
            case 11:
                return "de Novembre";
            case 12:
                return "de Desembre";
            default:
                return "Mes Desconegut";
        }
    }

    public static int diferenciaEnDias(Timestamp desde, Timestamp hasta) {
        GregorianCalendar gcDesde = new GregorianCalendar();
        GregorianCalendar gcHasta = new GregorianCalendar();

        gcDesde.setTime(new java.sql.Date(desde.getTime()));
        gcHasta.setTime(new java.sql.Date(hasta.getTime()));

        int dias = 0;
        while (gcDesde.before(gcHasta)) {
            dias++;
            gcDesde.add(Calendar.DAY_OF_YEAR, 1);
        }

        return dias;
    }

    /**
     * M�todo que recibe un Timestamp y un c�digo de idioma y devuelve la fecha completa
     * en el texto del idioma indicado
     *
     * @param  fecha fecha a convertir
     * @param  idioma c�digo de idioma ('ca' para catal�n y 'es' para espa�ol)
     * @return String texto de la fecha
     */
    public static String getFechaCompleta(Timestamp fecha, String idioma) {
        if (fecha == null) {
            return null;
        }

        GregorianCalendar gcFecha = new GregorianCalendar();
        gcFecha.setTime(new java.util.Date(fecha.getTime()));
        int mes = gcFecha.get(Calendar.MONTH);
        String deMes;
        if ("ca".equals(idioma) && (mes == Calendar.APRIL || mes == Calendar.AUGUST || mes == Calendar.OCTOBER)) {
            deMes = "d'";
        } else {
            deMes = "de ";
        }
        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("EEEEE dd '" + deMes + "'MMMMM 'de' yyyy", new Locale(idioma));

        return BitCadena.capitalize(sdf.format(gcFecha.getTime()));

    }
}
