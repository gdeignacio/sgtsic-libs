package es.caib.sgtsic.oo.pdfconverter.connector;

import com.sun.star.comp.helper.BootstrapException;
import com.sun.star.uno.XComponentContext;
/**
 * A Bootstrap Connector which uses a socket to connect to an OOo server.
 */
public class BootstrapSocketConnector extends BootstrapConnector {

    

    /**
     * Connects to an OOo server using a default socket and returns a
     * component context for using the connection to the OOo server.
     * 
     * @return             The component context
     */
    public XComponentContext connect() throws BootstrapException {

        // create random pipe name
        String host = "localhost";
        int    port = 8100;

        return connect(host,port);
    }

    /**
     * Connects to an OOo server using the specified host and port for the
     * socket and returns a component context for using the connection to the
     * OOo server.
     * 
     * @param   host   The host
     * @param   port   The port
     * @return         The component context
     */
    public XComponentContext connect(String host, int port) throws BootstrapException {

        // host and port
        String hostAndPort = "host="+host+",port="+port;

        // accept option
        String oooAcceptOption = "-accept=socket,"+hostAndPort+";urp;";

        // connection string
        //String unoConnectString = "uno:socket,"+hostAndPort+";urp;StarOffice.ComponentContext";
        // connection string
        String unoConnectString = "uno:socket,"+hostAndPort+";urp;StarOffice.ComponentContext";

        return connect(unoConnectString);
    }

    /**
     * Bootstraps a connection to an OOo server in the specified soffice
     * executable folder of the OOo installation using a default socket and
     * returns a component context for using the connection to the OOo server.
     * 
     * @param   oooExecFolder      The folder of the OOo installation containing the soffice executable
     * @return                     The component context
     */
    public static final XComponentContext bootstrap() throws BootstrapException {

        BootstrapSocketConnector bootstrapSocketConnector = new BootstrapSocketConnector();
        return bootstrapSocketConnector.connect();
    }

    /**
     * Bootstraps a connection to an OOo server in the specified soffice
     * executable folder of the OOo installation using the specified host and
     * port for the socket and returns a component context for using the
     * connection to the OOo server.
     * 
     * @param   oooExecFolder      The folder of the OOo installation containing the soffice executable
     * @param   host               The host
     * @param   port               The port
     * @return                     The component context
     */
    public static final XComponentContext bootstrap(String host, int port) throws BootstrapException {

        BootstrapSocketConnector bootstrapSocketConnector = new BootstrapSocketConnector();
        return bootstrapSocketConnector.connect(host,port);
    }
}
