package es.caib.sgtsic.util.bitel;

/**
 * Clase de utilidades para configuraciones de sistema, carga de propiedades, etc.
 *
 * @author dept desarrollo
 * @version 1.0 23/04/2008
 */
public class BitSystem {

    public BitSystem() {
    }

    /**
     * Comprueba las propiedades del sistema y determina si se est� ejecutando un servidor JBoss
     *
     * @return true si el servidor en ejecuci�n es JBoss. False en caso contrario
     */
    public static boolean isJBossServer() {
        boolean evidencia1 = "org.jnp.interfaces.NamingContextFactory".equals(System.getProperty("java.naming.factory.initial"));
        boolean evidencia2 = System.getProperty("jboss.server.name") != null;

        if (!evidencia1 && evidencia2 || evidencia1 && !evidencia2) {
            System.out.println("Alerta! Se podr�a estar confunciendo el servidor en ejecuci�n");
        }

        return evidencia1 || evidencia2;
    }

    /**
     * Comprueba las propiedades del sistema y determina si se est� ejecutando un servidor OC4J
     *
     * @return true si el servidor en ejecuci�n es OC4J. False en caso contrario
     */
    public static boolean isOracleContainersForJava() {
        boolean evidencia1 = "com.evermind.server.ApplicationInitialContextFactory".equals(System.getProperty("java.naming.factory.initial"));
        boolean evidencia2 = System.getProperty("oracle.j2ee.home") != null;

        if (!evidencia1 && evidencia2 || evidencia1 && !evidencia2) {
            System.out.println("Alerta! Se podr�a estar confunciendo el servidor en ejecuci�n");
        }

        return evidencia1 || evidencia2;
    }

    /**
     * Comprueba las propiedades del sistema y determina si se est�
     * ejecutando un servidor Glassfish v2.
     *
     * @return true si el servidor en ejecuci�n es Glassfish v2, false en
     * caso contrario.
     */
    public static boolean isGlassfishV2() {
        boolean evidencia1 = System.getProperty("com.sun.jbi.home") != null;
        boolean evidencia2 = System.getProperty("com.sun.updatecenter.home") != null;

        if (!evidencia1 && evidencia2 || evidencia1 && !evidencia2) {
            System.out.println("Alerta! Se podr�a estar confunciendo el servidor en ejecucion.");
        }
        return evidencia1 || evidencia2;

    }
}
