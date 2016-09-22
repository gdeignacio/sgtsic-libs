/*
 * Copyright 2016 gdeignacio.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.caib.sgtsic.jooreports;

import es.caib.sgtsic.util.DataHandlers;
import es.caib.sgtsic.oo.pdfconverter.OOoStreamConverter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import net.sf.jooreports.templates.DocumentTemplate;
import net.sf.jooreports.templates.DocumentTemplateException;
import net.sf.jooreports.templates.DocumentTemplateFactory;

/**
 *
 * @author gdeignacio
 */
public class MailDocument {

    public static final String ODT = "application/vnd.oasis.opendocument.text";
    public static final String PDF = "application/pdf";
    
    
    
    public DataHandler convertDocumentToPdf(DataHandler document, Object data) throws IOException{
  
        return convertToPdf(convertDocument(document, data), new OOServerSettings());
        
    }
    
    
    public DataHandler convertDocumentToPdf(DataHandler document, Object data, OOServerSettings ooss) throws IOException{
  
        return convertToPdf(convertDocument(document, data), ooss);
        
    }
    
    public DataHandler convertDocument(DataHandler document, Object data) {
        try {
            return convertDocument(document, data, ODT);
        } catch (IOException ex) {
            Logger.getLogger(MailDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public DataHandler convertDocument(DataHandler document, Object data, String mimetype) throws IOException {

        InputStream is = new ByteArrayInputStream(DataHandlers.dataHandlerToByteArray(document));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DocumentTemplateFactory documentTemplateFactory = new DocumentTemplateFactory();
        DocumentTemplate template = documentTemplateFactory.getTemplate(is);
        try {
            template.createDocument(data, baos);
        } catch (DocumentTemplateException ex) {
            Logger.getLogger(MailDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
        is.close();
        baos.close();
        return DataHandlers.byteArrayToDataHandler(baos.toByteArray(), mimetype);

    }

    public DataHandler convertToPdf(DataHandler document) throws IOException {

        return convertToPdf(document, new OOServerSettings());

    }

    public DataHandler convertToPdf(DataHandler document, OOServerSettings ooss) throws IOException {

        OOoStreamConverter converter = new OOoStreamConverter(ooss.getHost(), Integer.valueOf(ooss.getPort()));
        byte[] toConvert = DataHandlers.dataHandlerToByteArray(document);
        return DataHandlers.byteArrayToDataHandler(converter.convertToPDF(toConvert), PDF);

    }
    
    

}
