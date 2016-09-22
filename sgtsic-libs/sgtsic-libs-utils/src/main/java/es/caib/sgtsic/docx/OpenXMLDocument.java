package es.caib.sgtsic.docx;



import com.google.common.base.Strings;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
//import org.apache.xerces.dom.NodeImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;




/**
 * Représente un document OpenXML en général. Cette classe encapsule toutes les
 * méthodes qui permettent de manipuler un package de manière transparente.
 *
 * @author Julien Chable
 * @version 0.2
 *
 */
public class OpenXMLDocument {

	protected Package container;

	protected CorePropertiesHelper corePropertiesHelper;

	/**
	 * Constructeur.
	 *
	 * @param docPackage
	 *            Référence vers le package du document.
	 */
	public OpenXMLDocument(Package docPackage) {
		container = docPackage;
		corePropertiesHelper = new CorePropertiesHelper(container);
		// On associe le CorePropertiesHelper de cette classe comme étant
		// l'enregistreur des parties de type propriété du document.
		container.addMarshaller(ContentTypeConstant.CORE_PROPERTIES,
				corePropertiesHelper);
	}

	/**
	 * Extrait toutes les ressources du type spécifié et les place dans le
	 * répertoire cible.
	 *
	 * @param contentType
	 *            Le type de contenu.
	 * @param destFolder
	 *            Le répertoire cible.
	 */
	public void extractFiles(String contentType, File destFolder) {
		if (!destFolder.isDirectory())
			throw new IllegalArgumentException(
					"Le paramètre desFolder doit être un répertoire !");

		ArrayList<PackagePart> parts = new ArrayList<PackagePart>();
		for (PackagePart part : container.getPartByContentType(contentType))
			parts.add(part);
		extractParts(parts, destFolder);
	}

	/**
	 * Extrait le contenu des parties spécifiées dans le répertoire cible.
	 *
	 * @param parts
	 *            Les parties à extraire.
	 * @param destFolder
	 *            Le répertoire de destination.
	 */
	public void extractParts(ArrayList<PackagePart> parts, File destFolder) {
		for (PackagePart part : parts) {
			String filename = PackageURIHelper.getFilename(part.getUri());
			try {
				InputStream ins = part.getInputStream();
				FileOutputStream fw = new FileOutputStream(destFolder
						.getAbsolutePath()
						+ File.separator + filename);
				byte[] buff = new byte[512];
				while (ins.available() > 0) {
					ins.read(buff);
					fw.write(buff);
				}
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Obtenir toutes les parties étant des images d'aperçus du document.
	 */
	public ArrayList<PackagePart> getThumbnails() {
		return container
				.getPartByRelationshipType(PackageRelationshipConstants.NS_THUMBNAIL_PART);
	}

	/**
	 * Ouvre un document.
	 *
	 * @param zipFile
	 *            Le fichier Zip du document OpenXML.
	 * @param access
	 *            Le mode d'accès au document.
	 */
	public static OpenXMLDocument open(ZipFile zipFile, PackageAccess access) {
		return new OpenXMLDocument(Package.open(zipFile, access));
	}

	/**
	 * Sauvegarder l'intégralité du document dans le fichier de destination.
	 */
	public void save(File destFile) {
		container.save(destFile);
	}

	/**
	 * Close the package to free the zip file created in the temporary folder.
	 * Added by: Willy Ekasalim - Allette Systems
	 */
	public void close(){
		container.close();
	}

	public CoreProperties getCoreProperties() {
		return corePropertiesHelper.getCoreProperties();
	}

	final class CorePropertiesHelper implements PartMarshaller {

		private final static String NAMESPACE_DC_URI = "http://purl.org/dc/elements/1.1/";

		private final static String NAMESPACE_CP_URI = "http://schemas.openxmlformats.org/package/2006/metadata/core-properties";

		private final static String NAMESPACE_DCTERMS_URI = "http://purl.org/dc/terms/";

		/**
		 * Référence vers le package.
		 */
		private Package container;

		/**
		 * L'entrée Zip du fichier de propriétés du doccuments.
		 */
		private ZipEntry corePropertiesZipEntry;

		/**
		 * Le bean des propriétés du document.
		 */
		private CoreProperties coreProperties;

		/**
		 * L'arbre DOM des propriétés du document (sert à l'enregistrement)
		 */
		private Document xmlDoc;

		public CorePropertiesHelper(Package container) {
			this.container = container;
			coreProperties = parseCorePropertiesFile();
		}

		/**
		 * Parse le fichier de propriétés du document.
		 *
		 * @return
		 */
		private CoreProperties parseCorePropertiesFile() {
			CoreProperties coreProps = new CoreProperties();
			corePropertiesZipEntry = getCorePropertiesZipEntry();

			InputStream inStream = null;
			try {
				inStream = container.getArchive().getInputStream(
						corePropertiesZipEntry);
			} catch (IOException e) {
				throw new InvalidFormatException(
						"Impossible de lire le fichier de properiétés "
								+ corePropertiesZipEntry.getName());
			} catch (Exception e){
				//Willy: core properties not always present in the file?
				return null;
			}

			// Création du parser DOM
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
					.newInstance();
			documentBuilderFactory.setNamespaceAware(true);
			documentBuilderFactory.setIgnoringElementContentWhitespace(true);
			DocumentBuilder documentBuilder;
			try {
				documentBuilder = documentBuilderFactory.newDocumentBuilder();

				// On parse le document XML en arbre DOM
				xmlDoc = documentBuilder.parse(inStream);
				// Créateur
				NodeList creators = xmlDoc.getElementsByTagNameNS(NAMESPACE_DC_URI, "creator");
				if (creators != null && creators.item(0) != null)
					coreProps.setCreator(getTextContent(creators.item(0)));

				// Titre
				NodeList titles = xmlDoc.getElementsByTagNameNS(
						NAMESPACE_DC_URI, "title");
				if (titles != null && titles.item(0) != null)
					coreProps.setTitle(getTextContent(titles.item(0)));

				// Sujet
				NodeList subjects = xmlDoc.getElementsByTagNameNS(
						NAMESPACE_DC_URI, "subject");
				if (subjects != null && subjects.item(0) != null)
					coreProps.setSubject(getTextContent(subjects.item(0)));

				// Mots clé
				NodeList keywords = xmlDoc.getElementsByTagNameNS(
						NAMESPACE_CP_URI, "keywords");
				if (keywords != null & keywords.item(0) != null)
					coreProps.setKeywords(getTextContent(keywords.item(0)));

				// Description
				NodeList descriptions = xmlDoc.getElementsByTagNameNS(
						NAMESPACE_DC_URI, "description");
				if (descriptions != null && descriptions.item(0) != null)
					coreProps.setDescription(
							getTextContent(descriptions.item(0)));

				// Dernier personne à avoir modifié le document
				NodeList lastModicationPersons = xmlDoc.getElementsByTagNameNS(
						NAMESPACE_CP_URI, "lastModifiedBy");
				if (lastModicationPersons != null
						&& lastModicationPersons.item(0) != null)
					coreProps.setLastModifiedBy(getTextContent(lastModicationPersons.item(0)));

				// Revision
				NodeList revisions = xmlDoc.getElementsByTagNameNS(
						NAMESPACE_CP_URI, "revision");
				if (revisions != null && revisions.item(0) != null)
					coreProps.setRevision(getTextContent(revisions.item(0)));

				// Date de création
				NodeList created = xmlDoc.getElementsByTagNameNS(
						NAMESPACE_DCTERMS_URI, "created");
				if (created != null && created.item(0) != null)
					coreProps.setCreated(getTextContent(created.item(0)));

				// Date de modification
				NodeList modifies = xmlDoc.getElementsByTagNameNS(
						NAMESPACE_DCTERMS_URI, "modified");
				if (modifies != null && modifies.item(0) != null)
					coreProps.setModified(getTextContent(modifies.item(0)));

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return coreProps;
		}

		/**
		 * Sauvegarde le fichier de propriétés du document dans le flux
		 * spécifié.
		 *
		 * @param out
		 *            Flux de sortie.
		 */
		public void marshall(PackagePart part, OutputStream os) {
			if (!(os instanceof ZipOutputStream))
				throw new IllegalArgumentException(
						"Le flux doit être un ZipOutputSTream !");

			ZipOutputStream out = (ZipOutputStream) os;
			// Créateur
			setTextContent(coreProperties.getCreator(), xmlDoc.getElementsByTagNameNS(NAMESPACE_DC_URI, "creator").item(0));
			// Titre
			setTextContent(coreProperties.getTitle(), xmlDoc.getElementsByTagNameNS(NAMESPACE_DC_URI, "title").item(0));
			// Sujet
			setTextContent(coreProperties.getSubject(), xmlDoc.getElementsByTagNameNS(NAMESPACE_DC_URI, "subject").item(0));
			// Mots clé
			setTextContent(coreProperties.getKeywords(), xmlDoc.getElementsByTagNameNS(NAMESPACE_CP_URI, "keywords").item(0));
			// Description
			setTextContent(coreProperties.getDescription(), xmlDoc.getElementsByTagNameNS(NAMESPACE_DC_URI, "description").item(0));
			// Dernier personne à avoir modifié le document
			setTextContent(coreProperties.getLastModifiedBy(), xmlDoc.getElementsByTagNameNS(NAMESPACE_CP_URI, "lastModifiedBy").item(0));
			// Revision
			setTextContent(coreProperties.getRevision(), xmlDoc.getElementsByTagNameNS(NAMESPACE_CP_URI, "revision").item(0));
			// Date de création
			setTextContent(coreProperties.getCreated(), xmlDoc.getElementsByTagNameNS(NAMESPACE_DCTERMS_URI, "created").item(0));
			// Date de modification
			setTextContent(coreProperties.getModified(), xmlDoc.getElementsByTagNameNS(NAMESPACE_DCTERMS_URI, "modified").item(0));
			xmlDoc.normalize();

			// Enregistrement de la partie dans le zip
			ZipEntry ctEntry = new ZipEntry(corePropertiesZipEntry.getName());
			try {
				// Création de l'entrée dans le fichier ZIP
				out.putNextEntry(ctEntry);
				DOMSource source = new DOMSource(xmlDoc);
				StreamResult result = new StreamResult(out);
				TransformerFactory transFactory = TransformerFactory
						.newInstance();
				try {
					Transformer transformer = transFactory.newTransformer();
					transformer.setOutputProperty("indent", "yes");
					transformer.transform(source, result);
				} catch (TransformerException e) {
					System.err
							.println("Echec de l'enregistrement : impossible de créer le fichier "
									+ corePropertiesZipEntry.getName());
				}
				// Fermeture de l'entrée du fichier ZIP
				out.closeEntry();
			} catch (IOException e1) {
				System.err.println("");
			}
		}

		/* Accesseurs */

		public Package getContainer() {
			return container;
		}

		public CoreProperties getCoreProperties() {
			return coreProperties;
		}

		/**
		 * Récupérer l'entrée Zip du fichier de propriété du document.
		 */
		private ZipEntry getCorePropertiesZipEntry() {
			PackageRelationship corePropsRel = container
					.getRelationshipsByType(
							PackageRelationshipConstants.NS_CORE_PROPERTIES)
					.getRelationship(0);

			if (corePropsRel == null)
				return null;

			return new ZipEntry(corePropsRel.getTargetUri().getPath());
		}
	}



   // Para solucionar un problema de compatibilidad con JBOSS para el parseo de
   // XML hemos tenido que incluir la implementacion de los métodos que hay a
   // continuacion, ya que la libreria que hay en JBOSS está sobre escrita y
   // no los implementa.
   // Se ha tenido que modificar metodos de esta clase para que usen los añadi-
   // dos.
   //
   // Para compilar, se han añadido las librerias de JBOSS en el proyecto SICIE-registro-web
   //   - $JBOSS_HOME/lib/endorsed/xercesImpl.jar
   //   - $JBOSS_HOME/lib/endorsed//xml-apis.jar
   // indicando que no se empaqueten.

    // internal method returning whether to take the given node's text content
    private static final boolean hasTextContent(Node child) {
		String nodeValue = null;
		if (child != null) nodeValue = child.getNodeValue();
		if (nodeValue != null) nodeValue = nodeValue.trim();
		else nodeValue = ""; // BitCadena.CADENA_VACIA;

        return child.getNodeType() != Node.COMMENT_NODE &&
            child.getNodeType() != Node.PROCESSING_INSTRUCTION_NODE &&
			(child.getNodeType() != Node.TEXT_NODE || Strings.isNullOrEmpty(nodeValue)); 
       //  !BitCadena.esCadenaVacia(nodeValue));
             
            /*(child.getNodeType() != Node.TEXT_NODE ||
             ((TextImpl) child).isIgnorableWhitespace() == false);*/
    }

	private static String getChildTextContent(Node child){
		if (child.getClass().getName().equals("org.apache.xerces.dom.DeferredTextImpl")){
			try {
				java.lang.reflect.Method m = child.getClass().getMethod("getTextContent", (Class[]) null);
				Object textContent = m.invoke(child, (Object[])null);
				return hasTextContent(child) ? textContent.toString() : "";
			} catch (Exception ex) {
				Logger.getLogger(OpenXMLDocument.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		else return hasTextContent(child) ? child.getTextContent() : "";

		return "";
	}

    /*
     * Get Node text content
     * @since DOM Level 3
     */
    public static String getTextContent(Node node) throws DOMException {
		//org.apache.xerces.dom.DeferredTextImpl var;

        StringBuffer fBufferStr = null;
        Node child = node.getFirstChild();
        if (child != null) {
            Node next = child.getNextSibling();
            if (next == null) {
				return hasTextContent(child) ? getChildTextContent(child) : "";
				//return hasTextContent(child) ? ((NodeImpl) child).getTextContent() : "";
            }
            if (fBufferStr == null){
                fBufferStr = new StringBuffer();
            }
            else {
                fBufferStr.setLength(0);
            }
            getTextContent(fBufferStr, node);
            return fBufferStr.toString();
        }
        return "";
    }

    // internal method taking a StringBuffer in parameter
    private static void getTextContent(StringBuffer buf, Node node) throws DOMException {
        Node child = node.getFirstChild();

        while (child != null) {
            if (hasTextContent(child)) {
				getChildTextContent(child);
                //((NodeImpl) child).getTextContent();
            }
            child = child.getNextSibling();
        }
    }

    /*
     * Set Node text content
     * @since DOM Level 3
     */
    public static void setTextContent(String textContent, Node node)
        throws DOMException {
        // get rid of any existing children
        Node child;
        while ((child = node.getFirstChild()) != null) {
            node.removeChild(child);
        }
        // create a Text node to hold the given content
        if (textContent != null && textContent.length() != 0){
            node.appendChild(node.getOwnerDocument().createTextNode(textContent));
        }
    }
}