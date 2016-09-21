package es.caib.sgtsic.oo.pdfconverter;

import com.sun.star.beans.PropertyValue;
import com.sun.star.comp.helper.BootstrapException;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XStorable;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.XCloseable;

import es.caib.sgtsic.oo.pdfconverter.connector.BootstrapSocketConnector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Conversor de documentos utilizando el servidor de OpenOffice.
 * La clase conecta con el servidor y puerto especificado en el constructor.
 *
 * @author fros
 * @author gdeignacio
 * @version 2.0 16/04/2016
 */
public class OOoStreamConverter {

    //servidor donde est� instalado el servidor de OpenOffice a utilizar para
    //la conversión
    private String host;
    //Puerto donde está instalado el servidor de OpenOffice a utilizar para la
    //conversión
    private int port;

    public OOoStreamConverter(String host, int port) {
        //this.xComponentContext = BootstrapSocketConnector.bootstrap(host, port);
        this.host = host;
        this.port = port;
    }

    public byte[] convertToPDF(byte[] input) {
        OOoInputStream inputStream = new OOoInputStream(input);

        return convert(inputStream, "writer_pdf_Export");
    }

    /**
     * Metodo privado que realiza la conversión del documento
     * @param input objeto de la clase OOoInputStream que representa el documento
     * a convertir
     * @param filterName nombre del filtro que indica a que tipo de documento
     * tenemos que convertir
     * @return el método retorna un array de bytes con el documento convertido
     * @throws ErrorConversion en caso de producirse un error en la conversion
     */
    private byte[] convert(OOoInputStream input, String filterName)  {
        
            // Vars.
            OOoOutputStream output = null;
            XMultiComponentFactory xMultiComponentFactory = null;
            Object desktopService = null;
            XComponentLoader xComponentLoader = null;
            PropertyValue[] conversionProperties = null;
            XComponent document = null;
            XStorable xstorable = null;
            XCloseable xclosable = null;
            
        try {    
            // Create OOoOutputStream
            output = new OOoOutputStream();
            //XComponentContext xComponentContext = BootstrapSocketConnector.bootstrap(this.host, this.port);
            BootstrapSocketConnector bootstrapSocketConnector = new BootstrapSocketConnector();
            XComponentContext xComponentContext = bootstrapSocketConnector.connect(host, port);

            xMultiComponentFactory = xComponentContext.getServiceManager();
            desktopService = xMultiComponentFactory.createInstanceWithContext("com.sun.star.frame.Desktop", xComponentContext);
            xComponentLoader = (XComponentLoader) UnoRuntime.queryInterface(XComponentLoader.class, desktopService);

            conversionProperties = new PropertyValue[2];
            conversionProperties[0] = new PropertyValue();
            conversionProperties[1] = new PropertyValue();

            conversionProperties[1].Name = "InputStream";
            conversionProperties[1].Value = input;
            document = xComponentLoader.loadComponentFromURL("private:stream", "_blank", 0, conversionProperties);
            conversionProperties = new PropertyValue[3];
            // OutputStream
            conversionProperties[0] = new PropertyValue();
            conversionProperties[0].Name = "OutputStream";
            conversionProperties[0].Value = output;

            // Nombre del filtro (normalmente writer_pdf_Export)
            conversionProperties[1] = new PropertyValue();
            conversionProperties[1].Name = "FilterName";
            conversionProperties[1].Value = filterName;

            // Propiedades de filtrado (ojo, que es una property "FilterData" cuyo valor es un array)
            conversionProperties[2] = new PropertyValue();
            PropertyValue[] filterDataProperties = new PropertyValue[1];

            // defaults PDF/A for pdf conversions
            filterDataProperties[0] = new PropertyValue();
            filterDataProperties[0].Name = "SelectPdfVersion";
            filterDataProperties[0].Value = new Integer(1);

            conversionProperties[2].Name = "FilterData";
            conversionProperties[2].Value = filterDataProperties;

            xstorable = (XStorable) UnoRuntime.queryInterface(XStorable.class, document);
            xstorable.storeToURL("private:stream", conversionProperties);

            xclosable = (XCloseable) UnoRuntime.queryInterface(XCloseable.class, document);
            xclosable.close(true);

            output.closeOutput();

            bootstrapSocketConnector.disconnect();

            return output.toByteArray();
        } catch (BootstrapException | Exception ex) {
            Logger.getLogger(OOoStreamConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
    }
}
