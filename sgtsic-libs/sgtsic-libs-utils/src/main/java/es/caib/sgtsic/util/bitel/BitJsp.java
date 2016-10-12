package es.caib.sgtsic.util.bitel;

import javax.ejb.*;

import javax.naming.*;

import javax.servlet.http.*;

import es.caib.sgtsic.data.AutenticacionData;
import es.caib.sgtsic.error.bitel.ErrorEJBInterfazHome;



/**
 * Clase de utilidades del proyecto regentbs
 *
 * @author EVA
 */
public class BitJsp {
    /**
     * Recupera el Home de un EJB a partir de su nombre JNDI
     * @param nombre Nombre JNDI
     * @return Instancia del EJB
     * @author JBE
     **/
    public static EJBHome getHome(String nombre) throws ErrorEJBInterfazHome {
        try {
            //Devuelve el EJB Home al cual tendremos que hacer el cast
            InitialContext ic = new InitialContext();
            return (EJBHome)ic.lookup("java:comp/env/ejb/" + nombre);
        } catch (NamingException e) {
            throw new ErrorEJBInterfazHome(nombre, e);
        }

    }

    /**
     * Recupera la informaci�n de autentificaci�n que viaja en la petici�n HTTP
     *
     * @param req instancia de Request que ha llegado al componente web
     * @return instancia con el nombre de usuario y la contrase�a utilizados para acceder al recurso en la petici�n
     * @author FRO
     **/
    public static AutenticacionData getAutenticacion(HttpServletRequest req) {
        AutenticacionData data = null;
        // Recogemos la cabecera "authorization"
        // esta cabecera tiene la forma: "Basic XXXXXXXX" donde XXXXXXXX es una cadena de texto codificada en base64
        String s = req.getHeader("authorization");

        // Si la cabecera aparece en la petici�n HTTP, miramos si mide m�s de 6 para quitarle la palabra "Basic"
        if ((s != null) && (s.length() > 6)) {
            // Tomamos la cabecera, le quitamos la palabra "Basic" y la decodificamos.
            // El resultado ser� usuario:password
            String up = new String(Base64.decode(s.substring(6)));

            // Nos aseguramos de que, efectivamente, la cadena contiene el caracter ':'
            int dospuntos;
            if ((up != null) && ((dospuntos = up.indexOf(":")) >= 0)) {
                data = new AutenticacionData();
                data.setUsuario(up.substring(0, dospuntos));
                data.setContrasenya(up.substring(dospuntos + 1));
            }
        }

        return data;
    }

    /**
     * Realiza un lookup de un EJB retornando la interface remota correspondiente al bean solicitado. Es responsabilidad del
     * m�todo que llama a este m�todo el realiza un cast apropiado al objeto invocado. Un ejemplo de invocaci�n de este m�todo ser�a
     * BolsaPlazas ejb = (BolsaPlazas)EJBUtils.lookup(BolsaPlazasHome.class, BolsaPlazas.class, "BolsaPlazasBean");
     * @param homeClass representa un objeto Class que identifica al home del EJB
     * @param remoteClass representa un objeto Class que identifica al remote del EJB
     * @param beanName representa el nombre del EJB que se define en el ejb-jar.xml
     * @return el m�todo retorna la interface remota que identifica el EJB solicitado
     */
    public static Object lookup(Class homeClass, Class remoteClass,
                                String beanName) {
        try {
            Context c = new InitialContext();
            Object remote = c.lookup("java:comp/env/" + beanName);
            Object narrowed =
                javax.rmi.PortableRemoteObject.narrow(remote, homeClass);
            Object invokedRemote =
                homeClass.getMethod("create", new Class[0]).invoke(narrowed,
                                                                   (Object[])null);

            return remoteClass.cast(invokedRemote);
        } catch (Exception e) {
            BitLog.fatal("Error localizando el EJB " + beanName);
            throw new RuntimeException(e);
        }
    }
}
