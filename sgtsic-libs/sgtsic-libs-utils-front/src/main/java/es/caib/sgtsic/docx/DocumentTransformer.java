package es.caib.sgtsic.docx;

import java.io.InputStream;

/**
 * Définition de l'interace de transformation des documents.
 * 
 * @author Julien Chable
 */
public interface DocumentTransformer {

	public InputStream transform(OpenXMLDocument doc);
}