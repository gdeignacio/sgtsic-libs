package es.caib.sgtsic.docx;

//import es.bitel.utils.//BitLog;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Classe permettant d'enregistrement des parties dans un flux Zip
 * 
 * @author Julien Chable
 */
public class ZipPartMarshaller implements PartMarshaller {

	/**
	 * Enregistre la partie spécifiée.
	 */
	public void marshall(PackagePart part, OutputStream os) {
		//add some modfication to check whether the content has been modified or not
		if(part.isModified()){
			//BitLog.debug("PackagePart has been modified");
		}
		
		if (!(os instanceof ZipOutputStream))
			throw new IllegalArgumentException(
					"Le flux doit être un ZipOutputStream !");

		ZipOutputStream zos = (ZipOutputStream) os;
		ZipEntry partEntry = new ZipEntry(part.uri.getPath());
		try {

			//Création de l'entrée du ZIP
			zos.putNextEntry(partEntry);
			//if the content is modified then take then one from PackagePart->PartDocument
			if(part.isModified()){
				Document doc = part.getModifiedContent().getContent();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(zos);
				TransformerFactory transFactory = TransformerFactory
						.newInstance();
				//Willy: taken from WordDOcument mechanism for saving
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
			}
			else{
	
				// Enregistrement des données dans le fichier ZIP
				InputStream ins = part.getInputStream();
				byte[] buff = new byte[512];
				int c;
				while (ins.available() > 0) {
					c = ins.read(buff);
					zos.write(buff, 0, c);
				}
			}
			zos.closeEntry();
		} catch (IOException ioe) {
			System.err.println("Echec enregistrement : la partie " + part.uri
					+ " n'a pas été enregistré !");
		}

		// Enregistrement des relations
		if (part.hasRelationships())
			marshallRelationshipPart(part.getRelationships(), PackageURIHelper
					.getRelationshipPartUri(part.getUri()), zos);
	}

	/**
	 * Enregistrement des relations de la partie.
	 */
	protected void marshallRelationshipPart(PackageRelationshipCollection rels,
			URI relPartURI, ZipOutputStream zos) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);

		// Schema
		try {
			SchemaFactory sf = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = sf.newSchema(new File(System
					.getProperty("user.dir")
					+ File.separator
					+ "schemas"
					+ File.separator
					+ "opc-relationships.xsd"));
			dbf.setSchema(schema);
			dbf.setValidating(true);
		} catch (SAXException e) {
			System.err.println("Le fichier XML " + relPartURI
					+ " ne comportera pas de namespace !");
		}

		// Création de la sortie XML
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			System.err
					.println("Echec de l'enregistrement : impossibilité de créer le fichier XML !");
		}

		Document xmlOutDoc = db.newDocument();
		// L'élément racine Relationships
		Element relsElem = xmlOutDoc
				.createElement(PackageRelationship.RELATIONSHIPS_TAG_NAME);
		// Ajout du namespace
		relsElem.setAttribute("xmlns",
				PackageRelationship.RELATIONSHIPS_NAMESPACE);

		// On ajoute toutes les relations
		for (PackageRelationship rel : rels) {
			// L'élément de la relation
			Element relElem = xmlOutDoc
					.createElement(PackageRelationship.RELATIONSHIP_TAG_NAME);

			// L'attribut ID
			Attr idAttr = xmlOutDoc
					.createAttribute(PackageRelationship.ID_ATTRIBUTE_NAME);
			idAttr.setNodeValue(rel.getId());

			// L'attribut Type
			Attr typeAttr = xmlOutDoc
					.createAttribute(PackageRelationship.TYPE_ATTRIBUTE_NAME);
			typeAttr.setNodeValue(rel.getRelationshipType());

			// L'attribut Target
			Attr targetAttr = xmlOutDoc
					.createAttribute(PackageRelationship.TARGET_ATTRIBUTE_NAME);
			// Willy: modified below to use toString(), because getPath did not return full path
			//targetAttr.setNodeValue(rel.getTargetUri().getPath());
			targetAttr.setNodeValue(rel.getTargetUri().toString());

			// L'attribut TargetMode (si mis en external)
			if (rel.getTargetMode() == TargetMode.EXTERNAL) {
				Attr targetModeAttr = xmlOutDoc
						.createAttribute(PackageRelationship.TARGET_MODE_ATTRIBUTE_NAME);
				targetModeAttr.setNodeValue("External");
				relElem.setAttributeNode(targetModeAttr);
			}

			relElem.setAttributeNode(idAttr);
			relElem.setAttributeNode(typeAttr);
			relElem.setAttributeNode(targetAttr);
			relsElem.appendChild(relElem);
		}

		xmlOutDoc.appendChild(relsElem);
		xmlOutDoc.normalize();

		// Enregistrement de la partie dans le zip
		ZipEntry ctEntry = new ZipEntry(relPartURI.getPath());
		try {
			// Création de l'entrée dans le fichier ZIP
			zos.putNextEntry(ctEntry);
			DOMSource source = new DOMSource(xmlOutDoc);
			StreamResult result = new StreamResult(zos);
			TransformerFactory transFactory = TransformerFactory.newInstance();
			try {
				Transformer transformer = transFactory.newTransformer();
				transformer.setOutputProperty("indent", "yes");
				transformer.transform(source, result);
			} catch (TransformerException e) {
				System.err
						.println("Echec de l'enregistrement : impossible de créer le fichier "
								+ relPartURI);
			}
			// Fermeture de l'entrée du fichier ZIP
			zos.closeEntry();
		} catch (IOException e1) {
			System.err.println("");
		}
	}
}