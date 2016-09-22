package es.caib.sgtsic.docx;

//import es.bitel.utils.//BitLog;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import java.util.*;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Représente un paquet suivant les spécifications de l'Open packaging
 * Convention.
 * 
 * @author Julien Chable
 * @version 0.1
 */
public class Package implements PartMarshaller {

	/**
	 * Mode d'accès au package.
	 */
	private PackageAccess packageAccess;

	/**
	 * Partie contenant les types de contenu de ce package.
	 */
	private final ContentTypeHelper contentTypeHelper;

	/**
	 * La liste des parties de ce package.
	 */
	private TreeMap<URI, PackagePart> partList;

	/**
	 * Partie contenant les relations de ce package.
	 */
	private PackageRelationshipCollection relationships;
	
	/**
	 * Package relationship object that stores document relationship
	 * word/_rels/document.xml.rels
	 * Added by: Willy Ekasalim - Allette Systems
	 */

	private PackageRelationshipCollection documentrelationships;
	
	/**
	 * Référence vers l'archive ZIP du document.
	 */
	private ZipFile archive;

	/**
	 * Les PartMarshaller en fonction du type de contenu.
	 */
	protected Hashtable<String, PartMarshaller> partMarshallers;

	/**
	 * Le PartMarshaller par défaut.
	 */
	protected PartMarshaller defaultPartMarshaller;

	/**
	 * Constructeur appelé par la méthode <i>open</i>. Initialise le
	 * ContentTypeHelper.
	 * 
	 * @param archive
	 *            L'archive ZIP du document.
	 * @param access
	 *            Mode d'accès au document (lecture seule, écriture seule,
	 *            lecture/écriture)
	 */
	private Package(ZipFile archive, PackageAccess access) {
		init();
		this.archive = archive;
		this.contentTypeHelper = new ContentTypeHelper(archive);
		this.packageAccess = access;
	}

	/**
	 * Initialisation des variables de classe.
	 */
	private void init() {
		defaultPartMarshaller = this;
		partMarshallers = new Hashtable<String, PartMarshaller>(5);
	}

	/**
	 * Ouvre ou crée une archive OPC
	 * 
	 * @param archive
	 *            L'archive ZIP du document.
	 * @param access
	 *            Mode d'accès au document (lecture seule, écriture seule,
	 *            lecture/écriture)
	 * @return Le document résultant de l'ouverture ou de la création.
	 */
	public static Package open(ZipFile archive, PackageAccess access) {
		Package pack = new Package(archive, access);
		if (pack.partList == null && access != PackageAccess.Write)
			pack.getParts();
		return pack;
	}
	
	/**
	 * Create a blank docx file
	 * Added by: Willy Ekasalim - Allette Systems
	 */
	public static Package create(){
		try{
			//create temp file on the local machine and delete it after it being closed (close() being called on the package)
			File temp = File.createTempFile("OpenXML4J-temp",".zip");
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(temp));
			String[] toc = {"_rels/.rels",
					"word/document.xml",
					"[Content_Types].xml",
					"word/_rels/document.xml.rels"};
			temp.deleteOnExit();
	
			//init the content of .rels
		    ByteArrayOutputStream in0 = new ByteArrayOutputStream();
			PrintStream p0 = new PrintStream(in0);
			p0.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
					"<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">" + 
					"<Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument\" Target=\"word/document.xml\"/>" +
					"</Relationships>");
		    out.putNextEntry(new ZipEntry(toc[0]));
			byte[] b = in0.toByteArray();
		        out.write(b, 0, b.length);
		    out.closeEntry();
		    in0.close();
		    
			//init the content of empty document.xml
			ByteArrayOutputStream in1 = new ByteArrayOutputStream();
			PrintStream p1 = new PrintStream(in1);
			p1.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
					"<w:document xmlns:ve=\"http://schemas.openxmlformats.org/markup-compatibility/2006\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" xmlns:w10=\"urn:schemas-microsoft-com:office:word\" xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\">" +
					"<w:body/>" +
					"</w:document>");
		    out.putNextEntry(new ZipEntry(toc[1]));
			byte[] b1 = in1.toByteArray();
		        out.write(b1, 0, b1.length);
		    out.closeEntry();
		    in1.close();
			
			//init the content of [Content_Types].xml
			ByteArrayOutputStream in2 = new ByteArrayOutputStream();
			PrintStream p2 = new PrintStream(in2);
			p2.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
					"<Types xmlns=\"http://schemas.openxmlformats.org/package/2006/content-types\">" +
					"<Default Extension=\"rels\" ContentType=\"application/vnd.openxmlformats-package.relationships+xml\"/>" +
					"<Default Extension=\"xml\" ContentType=\"application/xml\"/><Override PartName=\"/word/document.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml\"/>" +
					"</Types>");
		    out.putNextEntry(new ZipEntry(toc[2]));
			byte[] b2 = in2.toByteArray();
		        out.write(b2, 0, b2.length);
		    out.closeEntry();
		    in2.close();
			
		    //init the content of word/_rels/document.xml.rels
			ByteArrayOutputStream in3 = new ByteArrayOutputStream();
			PrintStream p3 = new PrintStream(in3);
			p3.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
					"<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">" +
					"<Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument\" Target=\"word/document.xml\"/>" +
					"</Relationships>");
		    out.putNextEntry(new ZipEntry(toc[3]));
			byte[] b3 = in3.toByteArray();
		        out.write(b3, 0, b3.length);
		    out.closeEntry();
		    in3.close();
			
		    // Complete the ZIP file
		    out.close();
			
			//load the temp file and create Package object with the file
			ZipFile zipFile = new ZipFile(temp);
			
			PackageAccess access = PackageAccess.ReadWrite;
			Package pack = new Package(zipFile, access);
			if (pack.partList == null && access != PackageAccess.Write)
				pack.getParts();
			return pack;
		}
		catch(IOException e){
			System.err.println("Error when trying to create new/blank docx file" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Liste l'ensemble des parties contenues dans un package. Le fichier de
	 * type de contenu [Content_Types].xml n'est pas listé.
	 * 
	 * @param zipfile
	 *            Le fichier de paquet compressé à l'aide de l'algorithme ZIP.
	 * @return La liste des URIs de l'ensemble des parties du paquet.
	 */
	public static ArrayList<URI> listPackageParts(ZipFile zipfile) {
		ArrayList<URI> retArr = new ArrayList<URI>();
		Enumeration entries = zipfile.entries();

		// On parcourt toutes les entrées du fichier ZIP
		while (entries.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			// On transforme le nom de l'entrée en URI
			URI entryURI = null;
			try {
				entryURI = new URI(entry.getName());
			} catch (URISyntaxException e) {
				continue;
			}
			// On ajoute l'URI de l'entrée à la liste des noms des parties
			retArr.add(entryURI);
		}
		return retArr;
	}

	/**
	 * Lance une exception si le mode d'accès est ouvert/créé en lecture
	 * seulement (PackageAccess.Read).
	 */
	public void throwExceptionIfReadOnly() {
		if (packageAccess == PackageAccess.Read)
			throw new InvalidOperationException(
					"L'opération n'est pas permise, le document est ouvert en lecture seulement !");
	}

	/**
	 * Lance une exception si le mode d'accès est en écriture seule
	 * (PackageAccess.Write). Cette méthode est appellée par les méthodes
	 * nécessitant un droit d'écriture sur le document.
	 */
	public void throwExceptionIfWriteOnly() {
		if (packageAccess == PackageAccess.Write)
			throw new InvalidOperationException(
					"L'opération n'est pas permise, le document est ouvert en écriture seulement !");
	}

	/**
	 * Récupérer une partie spécifique de ce package.
	 * 
	 * @param parturi
	 *            L'URI de la partie
	 * @return La partie correspondant à l'URI spécifié
	 */
	public PackagePart getPart(URI partUri) {
		if (partUri == null)
			throw new IllegalArgumentException("partUri ne doit pas être nul !");

		if (partList == null)
			getParts();
		return partList.get(partUri);
	}

	/**
	 * Récupérer les parties dont le type de contenu est celui spécifié en
	 * argument.
	 * 
	 * @param contentType
	 *            Le type de contenu à chercher.
	 * @return Toutes le sparties dont le type de contenu est celui spécifié.
	 */
	public ArrayList<PackagePart> getPartByContentType(String contentType) {
		ArrayList<PackagePart> retArr = new ArrayList<PackagePart>();
		for (PackagePart part : partList.values())
			if (part.getContentType().equals(contentType))
				retArr.add(part);
		return retArr;
	}

	/**
	 * Récupérer les parties en fontion du type de relation.
	 * 
	 * @param relationshipType
	 *            Le type de relation.
	 * @return Toutes les parties ayant le type de relation spécifié.
	 */
	public ArrayList<PackagePart> getPartByRelationshipType(
			String relationshipType) {
		if (relationshipType == null)
			throw new IllegalArgumentException(
					"Le type de la relation ne peut être null.");

		ArrayList<PackagePart> retArr = new ArrayList<PackagePart>();
		for (PackageRelationship rel : getRelationshipsByType(relationshipType))
			retArr.add(getPart(rel));

		return retArr;
	}

	/**
	 * Obtenir la partie référencée par la relation spécifiée.
	 * 
	 * @param partRel
	 *            La relation de partie
	 */
	public PackagePart getPart(PackageRelationship partRel) {
		PackagePart retPart = null;
		for (PackageRelationship rel : relationships)
			if (rel.getRelationshipType().equals(partRel.getRelationshipType())) {
				retPart = getPart(rel.getTargetUri());
				break;
			}
		return retPart;
	}

	/**
	 * Obtenir les parties de l'archive. Si celles-ci n'ont pas encore été
	 * chargé depuis le document, alors cela est réalisé par cette méthode.
	 * 
	 * @return Les parties de ce package.
	 */
	public ArrayList<PackagePart> getParts() {
		throwExceptionIfWriteOnly();
		if (partList != null)
			return new ArrayList<PackagePart>(partList.values());
		else {
			partList = new TreeMap<URI, PackagePart>();
			Enumeration entries = archive.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				URI entryUri = null;
				try {
					entryUri = new URI(entry.getName()).normalize();
				} catch (URISyntaxException e) {
					continue;
				}
				String contentType = contentTypeHelper.getContentType(entryUri);
				if (contentType != null)
					partList.put(entryUri, new ZipPackagePart(this, entry,
							entryUri, contentType));
			}
			return new ArrayList<PackagePart>(partList.values());
		}
	}

	/**
	 * Ajouter une partie à ce package.
	 * 
	 * @param partUri
	 *            L'URI de la partie
	 * @param contentType
	 *            Le type de contenu de la partie.
	 * @return L'objet PackagePart représentant la partie nouvellement ajoutée.
	 */
	public PackagePart addPart(URI partUri, String contentType) {
		throwExceptionIfReadOnly();
		if (partUri == null)
			throw new IllegalArgumentException("partUri est null");

		if (contentType == null)
			throw new IllegalArgumentException("contentType est null");

		URI normUri = partUri.normalize();
		if (partList.containsKey(normUri) && !partList.get(normUri).isDeleted())
			throw new InvalidOperationException("La parties existe déjà");

		// On ajoute le type de contenu (si nécessaire)
		// Willy: remove the following for now, dont know what it is for
		//contentTypeHelper.addContentType(partUri, contentType);
		MemoryPackagePart retPart = new MemoryPackagePart(this, partUri,
				contentType);
		this.partList.put(normUri, retPart);
		return retPart;
	}

	/**
	 * Ajouter une partie à ce package à partir du contenu d'un fichier.
	 * 
	 * @param partUri
	 *            L'URI de destination de la partie dans le paquet
	 * @param contentType
	 *            Le type de contenu du paquet
	 * @param content
	 *            Le fichier d'origine dont le contenu sera lu et ajouté dans la
	 *            partie ajoutée
	 * @return La partie nouvellement ajoutée.
	 */
	public PackagePart addPart(URI partUri, String contentType, File content)
			throws IOException {
		PackagePart addedPart = addPart(partUri, contentType);

		// On extrait le contenu du fichier et l'injecte dans la partie
		if (content != null) {
			try {
				OutputStream partOutput = addedPart.getOutputStream();
				FileInputStream fis = new FileInputStream(content);
				byte[] buff = new byte[512];
				int c = 0;
				while (fis.available() > 0) {
					c = fis.read(buff);
					partOutput.write(buff, 0, c);
				}
				partOutput.close();
				fis.close();
			} catch (IOException ioe) {
				System.err
						.println("Le contenu de la partie ajoutée peut être altéré ... :(");
			}
		}
		return addedPart;
	}

	public void removePart(URI partUri) {
		throwExceptionIfReadOnly();
		if (partUri == null)
			throw new IllegalArgumentException("partUri ne doit pas être nul");

		// Suppression de la partie dans le paquet
		if (partList.containsKey(partUri)) {
			partList.get(partUri).setDeleted(true);
			partList.remove(partUri);
		}

		// Suppression du type de contenu si nécessaire
		contentTypeHelper.deleteContentType(partUri);

		if (PackageURIHelper.isRelationshipPartURI(partUri)) {
			// Suppression des relations dépendants de la partie source
			URI sourceUri = PackageURIHelper
					.getSourcePartUriFromRelationshipPartUri(partUri);
			if (sourceUri.equals(PackageURIHelper.PACKAGE_ROOT_URI))
				clearRelationships();
			else if (partExists(sourceUri))
				getPart(sourceUri).clearRelationships();
		} else {
			removePart(PackageURIHelper.getRelationshipPartUri(partUri));
		}
	}

	/**
	 * Savoir si une partie est bien présente dans le paquet.
	 * 
	 * @param partUri
	 *            L'URI de la partie à vérifier.
	 * @return <i>true</i> si la partie existe logiquement dans le paquet sinon
	 *         <i>false</i>.
	 */
	public boolean partExists(URI partUri) {
		return (this.getPart(partUri) != null);
	}

	/**
	 * Ajouter une relation au niveau du package.
	 * 
	 * @param targetUri
	 *            L'URI de la partie cible.
	 * @param targetMode
	 *            La méthode de ciblage (Internal|External).
	 * @param relationshipType
	 *            Le type de la relation.
	 */
	public void addRelationship(URI targetUri, TargetMode targetMode,
			String relationshipType) {
		if (relationships == null)
			relationships = new PackageRelationshipCollection(this, PackageRelationshipType.PACKAGE);
		relationships.addRelationship(targetUri, targetMode, relationshipType,
				null);
	}

	/**
	 * Supprimer une relation du package.
	 * 
	 * @param id
	 *            L'identifiant de la relation à supprimer.
	 */
	public void removeRelationship(String id) {
		if (relationships != null)
			relationships.removeRelationship(id);
	}

	/**
	 * Récupérer toutes les relations de ce package.
	 * 
	 * @return Les relations de niveau paquet de ce package.
	 */
	public PackageRelationshipCollection getRelationships() {
		return getRelationshipsHelper(null);
	}

	/**
	 * Récupérer les relations possédant le type spécifié
	 * 
	 * @param relationshipType
	 *            Le filtre spécifiant le type de relation des relations à
	 *            retourner.
	 * @return Les relations dont le type correspond au filtre.
	 */
	public PackageRelationshipCollection getRelationshipsByType(
			String relationshipType) {
		throwExceptionIfWriteOnly();
		if (relationshipType == null)
			throw new IllegalArgumentException(
					"relationshipType ne doit pas être null !");
		return getRelationshipsHelper(relationshipType);
	}

	/**
	 * Récupérer les relations possédant le type spécifié.
	 * 
	 * @param id
	 *            Le type des relations désirées.
	 */
	private PackageRelationshipCollection getRelationshipsHelper(String id) {
		throwExceptionIfWriteOnly();
		if (relationships == null)
			relationships = new PackageRelationshipCollection(this, PackageRelationshipType.PACKAGE);
		return relationships.getRelationships(id);
	}

	/**
	 * Supprime toutes les relations de package.
	 */
	private void clearRelationships() {
		if (relationships != null)
			relationships.clear();
	}

	/**
	 * Enregistre le document dans le fichier spécifié.
	 * 
	 * @param destFile
	 *            Le fichier de destination
	 */
	public void save(File destFile) {
		// On vérifie que le document a été ouvert en écriture
		throwExceptionIfReadOnly();

		try {
			// Création d'un fichier de destination temporaire pour le document
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(
					destFile));

			//BitLog.debug("Save content type part");
			// Enregistrement du fichier de type de contenu
			contentTypeHelper.save(zos);

			//BitLog.debug("Save package relationships part");
			// Enregistrement des relations du package
			ZipPackageMarshaller packageMarshaller = new ZipPackageMarshaller(
					this);
			packageMarshaller.marshall(null, zos);

			// Enregistrement des parties
			for (PackagePart part : getParts()) {
				if (part.isDeleted())
					throw new InvalidOperationException(
							"Erreur, la partie ne devrait pas exister !");

				// Si la partie est une relation, on ne l'enregistre pas, c'est
				// la partie source qui le fera.
				if (part.isRelationshipPart())
					continue;

				//BitLog.debug("Save " + part.getUri().getPath() + " part");
				PartMarshaller marshaller = partMarshallers
						.get(part.contentType);
				if (marshaller != null)
					marshaller.marshall(part, zos);
				else
					defaultPartMarshaller.marshall(part, zos);
			}
			zos.close();
		} catch (IOException e) {
			System.err
					.println("Echec de l'enregistrement : une erreur est survenue pendant l'enregistrement !");
		}
	}

	/**
	 * Ajoute un marshaller.
	 * 
	 * @param contentType
	 *            Le type de contenu associé à ce marshaller.
	 * @param marshaller
	 *            Le marshaller à utilsier pour enregistrer les parties du type
	 *            de contenu spécifié.
	 */
	public void addMarshaller(String contentType, PartMarshaller marshaller) {
		partMarshallers.put(contentType, marshaller);
	}

	/**
	 * Supprimer un marshaller en fonction de son type de contenu.
	 * 
	 * @param contentType
	 *            Le type de contenu de la partie à supprimer.
	 */
	public void removeMarshaller(String contentType) {
		partMarshallers.remove(contentType);
	}

	/**
	 * Enregistre les parties dans le flux compressé Zip
	 */
	public void marshall(PackagePart part, OutputStream out) {
		ZipOutputStream zos = (ZipOutputStream) out;
		part.save(zos);
	}

	/* Accesseurs */

	public ZipFile getArchive() {
		return archive;
	}

	public ContentTypeHelper getContentTypeHelper() {
		return contentTypeHelper;
	}

	public PackageAccess getPackageAccess() {
		return packageAccess;
	}
	
	/**
	 * Free up/close the temp zip file
	 * Added by: Willy Ekasalim - Allette Systems
	 */
	public void close(){
		try{
			archive.close();
		}
		catch(Exception e){
			e.printStackTrace();
			//BitLog.debug("Error while trying to close temp zip file" + e.getMessage());
		}
	}
	
	public void addDefaultContentType(String extension, String contentType) {
		contentTypeHelper.addDefaultContentType(extension, contentType);
	}
	
	/**
	 * The following is added Methods for documentrelationship, its similar to package relationship method
	 * Added by: Willy Ekasalim - Allette Systems
	 */
	
	/**
	 * Add new relationship to documentrelationship
	 * Added by: Willy Ekasalim - Allette Systems
	 */
	public void addDocumentRelationship(URI targetUri, TargetMode targetMode,
			String relationshipType) {
		if (documentrelationships == null)
			documentrelationships = new PackageRelationshipCollection(this, PackageRelationshipType.DOCUMENT);
		documentrelationships.addRelationship(targetUri, targetMode, relationshipType,
				null);
	}
	
	/**
	 * getDocumentRelationships() is identical to getRelationships(), except that it works with documentrelationship
	 * @return documentrelationship
	 * Added by: Willy Ekasalim - Allette Systems
	 */
	public PackageRelationshipCollection getDocumentRelationships() {
		return getDocumentRelationshipsHelper(null);
	}
	
	/**
	 * Helper function for documentrelationship
	 * Added by: Willy Ekasalim - Allette Systems
	 */
	private PackageRelationshipCollection getDocumentRelationshipsHelper(String id) {
		throwExceptionIfWriteOnly();
		if (documentrelationships == null)
			documentrelationships = new PackageRelationshipCollection(this, PackageRelationshipType.DOCUMENT);
		return documentrelationships.getRelationships(id);
	}
	
	/**
	 * Classe d'aide qui gère les types de contenu des parties.
	 * 
	 * @author Julien Chable
	 */
	final class ContentTypeHelper {

		/**
		 * Le nom du fichier de contenu
		 */
		public static final String CONTENT_TYPES_FILENAME = "[Content_Types].xml";

		public static final String TYPES_NAMESPACE_URI = "http://schemas.openxmlformats.org/package/2006/content-types";

		private static final String TYPES_TAG_NAME = "Types";

		private static final String DEFAULT_TAG_NAME = "Default";

		private static final String EXTENSION_ATTRIBUTE_NAME = "Extension";

		private static final String CONTENT_TYPE_ATTRIBUTE_NAME = "ContentType";

		private static final String OVERRIDE_TAG_NAME = "Override";

		private static final String PART_NAME_ATTRIBUTE_NAME = "PartName";

		private ZipEntry contentTypeZipEntry;

		private TreeMap<String, String> defaultContentType;

		private TreeMap<URI, String> overrideContentType;

		private ZipFile zipArchive;

		/**
		 * Constructeur.
		 * 
		 * @param archive
		 *            L'archive du document, si différent de <i>null</i> alors
		 *            le fichier de types de contenu de l'archive est parsé.
		 */
		public ContentTypeHelper(ZipFile archive) {
			this.zipArchive = archive;
			this.defaultContentType = new TreeMap<String, String>();
			if (archive != null)
				parseContentTypesFile();
		}

		/**
		 * Parse le contenu du fichier de types de l'archive.
		 */
		private void parseContentTypesFile() {
			contentTypeZipEntry = getContentTypeZipEntry();
			InputStream inStream = null;
			try {
				inStream = zipArchive.getInputStream(contentTypeZipEntry);
			} catch (IOException e) {
				throw new InvalidFormatException(
						"Impossible de lire le fichier "
								+ CONTENT_TYPES_FILENAME);
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
				Document xmlContentTypetDoc = documentBuilder.parse(inStream);

				// On parcourt les types par défaut
				NodeList defaultTypes = xmlContentTypetDoc
						.getElementsByTagName(DEFAULT_TAG_NAME);
				for (int i = 0; i < defaultTypes.getLength(); ++i) {
					// On ajoute l'élément du type par défaut dans sa collection
					Node type = defaultTypes.item(i);
					String extension = type.getAttributes().getNamedItem(
							EXTENSION_ATTRIBUTE_NAME).getNodeValue();
					String contentType = type.getAttributes().getNamedItem(
							CONTENT_TYPE_ATTRIBUTE_NAME).getNodeValue();
					addDefaultContentType(extension, contentType);
				}

				// On parcourt les types surdéfinis
				NodeList overrideTypes = xmlContentTypetDoc
						.getElementsByTagName(OVERRIDE_TAG_NAME);
				for (int i = 0; i < overrideTypes.getLength(); ++i) {
					// On ajoute l'élément du type par défaut dans sa collection
					Node type = overrideTypes.item(i);
					URI uri = new URI(type.getAttributes().getNamedItem(
							PART_NAME_ATTRIBUTE_NAME).getNodeValue());
					String contentType = type.getAttributes().getNamedItem(
							CONTENT_TYPE_ATTRIBUTE_NAME).getNodeValue();
					addOverrideContentType(uri, contentType);
				}
			} catch (ParserConfigurationException e) {

			} catch (IOException ioe) {

			} catch (SAXException saxe) {

			} catch (URISyntaxException urie) {

			}
		}

		public void addContentType(URI partUri, String contentType) {
			boolean defaultCTExists = false;
			String extension = PackageURIHelper.getExtensionForPartURI(partUri);
			if ((extension.length() == 0)
					|| (this.defaultContentType.containsKey(extension) && !(defaultCTExists = this.defaultContentType
							.containsValue(contentType))))
				this.addOverrideContentType(partUri, contentType);
			else if (!defaultCTExists)
				this.addDefaultContentType(extension, contentType);
		}

		private void addOverrideContentType(URI partUri, String contentType) {
			if (overrideContentType == null)
				overrideContentType = new TreeMap<URI, String>();
			overrideContentType.put(partUri.normalize(), contentType);
		}

		private void addDefaultContentType(String extension, String contentType) {
			defaultContentType.put(extension, contentType);
		}

		public void deleteContentType(URI partUri) {
			if (this.overrideContentType != null) {
				this.overrideContentType.remove(partUri.normalize());
			}
		}

		public String getContentType(URI partUri) {
			URI normPartUri;
			try {
				normPartUri = new URI(PackageURIHelper.FORWARD_SLASH_CHAR
						+ partUri.normalize().getPath());
			} catch (URISyntaxException e) {
				return null;
			}

			if ((this.overrideContentType != null)
					&& this.overrideContentType.containsKey(normPartUri))
				return this.overrideContentType.get(normPartUri);

			String extension = PackageURIHelper.getExtensionForPartURI(partUri);
			if (this.defaultContentType.containsKey(extension))
				return this.defaultContentType.get(extension);

			return null;
		}

		/**
		 * On récupérer l'entrée du fichier Zip qui contient le contenu du
		 * fichier de type de contenu.
		 */
		private ZipEntry getContentTypeZipEntry() {
			Enumeration entries = zipArchive.entries();
			// On énumère toutes les entrées de l'archive Zip jusqu'à trouver
			// celle du fichier [Content_Types].xml
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				if (entry.getName().equals(CONTENT_TYPES_FILENAME))
					return entry;
			}
			return null;
		}

		public void save(ZipOutputStream outStream) {
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
						+ "opc-contenttypes.xsd"));
				dbf.setSchema(schema);
				dbf.setValidating(true);
			} catch (SAXException e) {
				System.err.println("Le fichier XML " + CONTENT_TYPES_FILENAME
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
			// L'élément racine Types
			Element typesElem = xmlOutDoc.createElement(TYPES_TAG_NAME);
			// Ajout du namespace
			typesElem.setAttribute("xmlns", TYPES_NAMESPACE_URI);

			// On ajoute tous les types par défaut
			for (Entry<String, String> entry : defaultContentType.entrySet()) {
				// L'élément du type par défaut
				Element defaultElem = xmlOutDoc.createElement(DEFAULT_TAG_NAME);
				// L'attribut Extension
				Attr extAttr = xmlOutDoc
						.createAttribute(EXTENSION_ATTRIBUTE_NAME);
				extAttr.setNodeValue(entry.getKey());
				// L'attribut ContentType
				Attr ctAttr = xmlOutDoc
						.createAttribute(CONTENT_TYPE_ATTRIBUTE_NAME);
				ctAttr.setNodeValue(entry.getValue());
				defaultElem.setAttributeNode(ctAttr);
				defaultElem.setAttributeNode(extAttr);
				typesElem.appendChild(defaultElem);
			}

			// On ajoute tous les types spécifiques
			for (Entry<URI, String> entry : overrideContentType.entrySet()) {
				// L'élément du type par défaut
				Element overrideElem = xmlOutDoc
						.createElement(OVERRIDE_TAG_NAME);
				// L'attribut Extension
				Attr partNameAttr = xmlOutDoc
						.createAttribute(PART_NAME_ATTRIBUTE_NAME);
				partNameAttr.setNodeValue(entry.getKey().getPath());
				// L'attribut ContentType
				Attr ctAttr = xmlOutDoc
						.createAttribute(CONTENT_TYPE_ATTRIBUTE_NAME);
				ctAttr.setNodeValue(entry.getValue());
				overrideElem.setAttributeNode(ctAttr);
				overrideElem.setAttributeNode(partNameAttr);
				typesElem.appendChild(overrideElem);
			}

			xmlOutDoc.appendChild(typesElem);
			xmlOutDoc.normalize();

			// Enregistrement de la partie dans le zip
			ZipEntry ctEntry = new ZipEntry(CONTENT_TYPES_FILENAME);
			try {
				// Création de l'entrée dans le fichier ZIP
				outStream.putNextEntry(ctEntry);
				DOMSource source = new DOMSource(xmlOutDoc);
				StreamResult result = new StreamResult(outStream);
				TransformerFactory transFactory = TransformerFactory
						.newInstance();
				try {
					Transformer transformer = transFactory.newTransformer();
					transformer.setOutputProperty("indent", "yes");
					transformer.transform(source, result);
				} catch (TransformerException e) {
					System.err
							.println("Echec de l'enregistrement : impossible de créer le fichier "
									+ CONTENT_TYPES_FILENAME);
				}
				// Fermeture de l'entrée du fichier ZIP
				outStream.closeEntry();
			} catch (IOException e1) {
				System.err.println("");
			}
		}
	}
}