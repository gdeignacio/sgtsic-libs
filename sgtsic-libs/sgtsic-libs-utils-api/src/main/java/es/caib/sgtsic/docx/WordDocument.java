package es.caib.sgtsic.docx;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import org.w3c.dom.Document;



/**
 * Classe de manipulation du contenu d'un document Word 2007.
 * 
 * @author Julien Chable
 */
public final class WordDocument extends OpenXMLDocument {

	/**
	 * Le contenu du document.
	 */
	private List<Document> contents;

	/**
	 * Aide à la gestion du contenu du document.
	 */
	private MainDocumentHelper mainDocumentHelper;

	public WordDocument(Package pack) {
		super(pack);
		mainDocumentHelper = new MainDocumentHelper();
		mainDocumentHelper.parseDocumentContent();
        // Añadimos uno por cada tipo que aceptamos editar
		container.addMarshaller(ContentTypeConstant.WORD_MAIN_DOCUMENT,mainDocumentHelper);
		container.addMarshaller(ContentTypeConstant.WORD_HEADER_DOCUMENT,mainDocumentHelper);
		container.addMarshaller(ContentTypeConstant.WORD_FOOTER_DOCUMENT,mainDocumentHelper);
	}

	/**
	 * Obtenir le contenu du document.
	 */
	public List<PackagePart> getCoreParts() {
		List<PackagePart> lp = container.getPartByContentType(ContentTypeConstant.WORD_MAIN_DOCUMENT);
        lp.addAll(container.getPartByContentType(ContentTypeConstant.WORD_HEADER_DOCUMENT));
        lp.addAll(container.getPartByContentType(ContentTypeConstant.WORD_FOOTER_DOCUMENT));

        return lp;
	}

	/**
	 * Permet d'obtenir l'arbre DOM du contenu du document.
	 */
	public List<Document> getCoreDocuments() {
		return contents;
	}
    
	/**
	 * Sauvegarde du document.
	 */
	public void save(File destFile) {
		super.save(destFile);
	}
	
	/**
	 * Classe d'aide pour gérer la partie principale du contenu du document.
	 * 
	 * @author Julien Chable
	 */
	class MainDocumentHelper implements PartMarshaller {

        // Mapeamos las partes para obtener de cada una el documento creado
        private Map<PackagePart,Document> mpd = null;

		/**
		 * Parse le contenu du document.
		 */
		private void parseDocumentContent() {
            List<PackagePart> contentParts = getCoreParts();
			if (contentParts == null)
				throw new InvalidFormatException(
						"Le document ne possède pas de contenu !");

            mpd = new HashMap<PackagePart,Document>();
            contents = new ArrayList<Document>();
            for(PackagePart contentPart : contentParts){
                // Obtention du flux de lecture
                InputStream inStream = null;
                try {
                    inStream = contentPart.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

                // Création du parser DOM
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                        .newInstance();
                documentBuilderFactory.setNamespaceAware(true);
                DocumentBuilder documentBuilder;
                try {
                    documentBuilder = documentBuilderFactory.newDocumentBuilder();

                    // On parse le document XML en arbre DOM
                    Document d = documentBuilder.parse(inStream);
                    contents.add(d);
                    mpd.put(contentPart, d);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
		}

		/**
		 * Enregistre le contenu principal du document dans le flux spécifié.
		 */
		public void marshall(PackagePart part, OutputStream os) {
			if (!(os instanceof ZipOutputStream))
				throw new IllegalArgumentException(
						"Le flux doit être un ZipOutputSTream !");

			ZipOutputStream out = (ZipOutputStream) os;

			// Enregistrement de la partie dans le zip
			ZipEntry ctEntry = new ZipEntry(part.getUri().getPath());
			try {
				// Création de l'entrée dans le fichier ZIP
				out.putNextEntry(ctEntry);
                Document d = mpd.get(part);
				DOMSource source = new DOMSource(d);
				StreamResult result = new StreamResult(out);
				TransformerFactory transFactory = TransformerFactory
						.newInstance();
				try {
					Transformer transformer = transFactory.newTransformer();
					// Si désactivé permet de gagner de réduire la taille du
					// fichier
					transformer.setOutputProperty(OutputKeys.INDENT, "yes");
					transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
					transformer.transform(source, result);
				} catch (TransformerException e) {
					System.err
							.println("Echec de l'enregistrement : impossible de créer le fichier "
									+ part.getUri());
				}
				// Fermeture de l'entrée du fichier ZIP
				out.closeEntry();
			} catch (IOException e1) {
				System.err.println("");
			}
		}
	}
}