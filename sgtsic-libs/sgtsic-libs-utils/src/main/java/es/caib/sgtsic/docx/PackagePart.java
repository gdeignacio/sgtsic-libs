package es.caib.sgtsic.docx;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

/**
 * 
 * @author Julien Chable
 * 
 */
public abstract class PackagePart {

	protected Package container;

	protected URI uri;

	protected String contentType;

	private boolean isRelationshipPart;

	private boolean isDeleted;
	
	/**
	 * Properties to flag whether this PackagePart's content has been modified
	 * Added by: Willy Ekasalim
	 */
	private boolean isModified;
	
	/**
	 * If the above isModified is true, then modifiedContent should not be null
	 * It contains the new XML content that this file should have
	 * Added by: Willy Ekasalim
	 */
	private PartDocument modifiedContent;

	private PackageRelationshipCollection relationships;

	/**
	 * Constructeur.
	 * 
	 * @param pack
	 *            Parent package.
	 * @param partURI
	 *            The part's Uniform Resource Identifier (URI), relative to the
	 *            parent Package root.
	 */
	public PackagePart(Package pack, URI partUri) {
		this.container = pack;
		this.uri = partUri;
		isRelationshipPart = PackageURIHelper.isRelationshipPartURI(partUri);
	}

	/**
	 * Constructor.
	 * 
	 * @param pack
	 *            Parent package.
	 * @param partURI
	 *            The part's Uniform Resource Identifier (URI), relative to the
	 *            parent Package root.
	 * @param contentType
	 *            The Multipurpose Internet Mail Extensions (MIME) content type
	 *            of the part's data stream.
	 */
	public PackagePart(Package pack, URI partURI, String contentType) {
		this(pack, partURI);
		this.contentType = contentType;
	}

	/**
	 * Ajout d'une relation à une partie (exclut les parties de type relation).
	 * 
	 * @param targetUri
	 *            URI de la partie cible, attention celle-ci doit être relative
	 *            par rapport au répertoire source de la partie.
	 * @param targetMode
	 *            Le mode [Internal|External].
	 * @param relationshipType
	 *            Le type de la relation.
	 * @return La relation qui a été ajoutée.
	 */
	public PackageRelationship addRelationship(URI targetUri,
			TargetMode targetMode, String relationshipType) {
		if (targetMode == null)
			targetMode = TargetMode.INTERNAL;

		// On vérifie que la partie existe
		if (!container.partExists(targetUri))
			throw new InvalidOperationException(
					"La partie cible n'existe pas dans le paquet !");

		return addRelationship(targetUri, targetMode, relationshipType, null);
	}

	/**
	 * Ajout d'une relation à une partie (exclut les parties de type relation).
	 * 
	 * @param targetUri
	 *            URI de la partie cible.
	 * @param targetMode
	 *            Le mode [Internal|External].
	 * @param relationshipType
	 *            Le type de la relation.
	 * @param id
	 *            L'identifiant unique de la relation, si <i>null</i> alors un
	 *            identifiant est généré automatiquement.
	 * @return La relation qui a été ajoutée.
	 */
	public PackageRelationship addRelationship(URI targetUri,
			TargetMode targetMode, String relationshipType, String id) {
		container.throwExceptionIfReadOnly();

		// On n'ajoute pas de relations à une partie de relation directement,
		// mais à la partie source
		if (isRelationshipPart)
			throw new InvalidOperationException(
					"Cette opération ne peut être exécutée sur une partie de type relation !");

		if (relationships == null)
			relationships = new PackageRelationshipCollection(this);

		return relationships.addRelationship(targetUri, targetMode,
				relationshipType, id);
	}

	/**
	 * Supprime toutes les relations de cette partie.
	 */
	public void clearRelationships() {
		relationships.clear();
	}

	/**
	 * Récupérer toutes relations de cette partie.
	 * 
	 * @return Toutes les relations de cette partie.
	 */
	public PackageRelationshipCollection getRelationships() {
		return getRelationshipsCore(null);
	}

	/**
	 * Récupérer toutes les relations dont le type correspond au filtre
	 * spécifié.
	 * 
	 * @param relationshipType
	 *            Le filtre de type de relation.
	 * @return Toutes les relations correspondant au filtre de type.
	 */
	public PackageRelationshipCollection getRelationshipsByType(
			String relationshipType) {
		if (container.getPackageAccess() == PackageAccess.Write)
			throw new IllegalAccessException(
					"Accès illégal, doit autoriser la lecture !");

		return getRelationshipsCore(relationshipType);
	}

	/**
	 * Implémentation de la recherche des relations d'une partie.
	 * 
	 * @param filter
	 *            Le filtre sur le type de relation. SI <i>null</i> alors le
	 *            filtre est désactivé et toutes les relations sont retournées.
	 * @return Toutes les relations dont le type est validé par le filtre.
	 */
	private PackageRelationshipCollection getRelationshipsCore(String filter) {
		if (container.getPackageAccess() == PackageAccess.Write)
			throw new IllegalAccessException(
					"Accès illégal, doit autoriser la lecture !");

		if (relationships == null) {
			if (isRelationshipPart)
				throw new IllegalArgumentException(
						"Ne doit pas être une partie de relations !");
			relationships = new PackageRelationshipCollection(this);
		}
		return new PackageRelationshipCollection(relationships, filter);
	}

	/**
	 * Savoir si la partie possède des relations.
	 * 
	 * @return <b>true</b> si la partie possède des relations sinon <b>false</b>.
	 */
	public boolean hasRelationships() {
		if (!(relationships != null && relationships.size() > 0))
			return container.partExists(PackageURIHelper
					.getRelationshipPartUri(uri));
		return true;
	}

	/**
	 * Obtenir le flux de lecture de la partie.
	 */
	public InputStream getInputStream() throws IOException {
		InputStream inStream = this.getInputStreamImpl();
		if (inStream == null)
			throw new IOException(
					"Ne peut obtenir le flux d'entrée de la partie " + uri);
		return inStream;
	}

	/**
	 * Obtenir le flux de sortie de la partie. Si la partie est contenue dans un
	 * ZIP, celle-ci est transformée en partie de type <i>MemoryPackagePart</i>
	 * afin de pouvoir écrire dedans, l'API Java ne permettant pas d'écrire
	 * directement dans le fichier.
	 */
	public OutputStream getOutputStream() throws IOException {
		OutputStream outStream;
		// Si l'instance est une partie de Zip (lecture uniquement) nous la
		// transformons en partie de type MemorypackagePart.
		if (this instanceof ZipPackagePart) {
			container.removePart(this.uri);
			PackagePart part = container.addPart(this.uri, this.contentType);
			outStream = part.getOutputStreamImpl();
		} else
			outStream = this.getOutputStreamImpl();

		if (outStream == null)
			throw new IOException(
					"Ne peut obtenir le flux de sortie de la partie " + uri);
		return outStream;
	}
	
	public PartDocument getContent(){
		return getContentImpl();
	}

	/*
	 * Accessors
	 */

	public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Package getParentPackage() {
		return container;
	}

	public boolean isRelationshipPart() {
		return isRelationshipPart;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	/**
	 * isModified()
	 * Added by: Willy Ekasalim
	 * @return isModified, which indicates whether this part content has been modified or not
	 */
	public boolean isModified() {
		return isModified;
	}
	
	/**
	 * Set the value of isModified properties of this part's content
	 * Added by: Willy Ekasalim
	 * @param isModified
	 */
	public void setModified(boolean isModified) {
		this.isModified = isModified;
	}
	
	/**
	 * If isModified is set to true, then the modifiedContent will not be null
	 * Added by: Willy Ekasalim
	 * @return PartDocument object, which contains the new content
	 */
	public PartDocument getModifiedContent(){
		return this.modifiedContent;
	}
	
	/**
	 * Method to save the new content of this PackagePart
	 * Added by: Willy Ekasalim
	 * @param modifiedContent the new content for this PackagePart
	 */
	public void setPartDoc(PartDocument modifiedContent){
		this.modifiedContent = modifiedContent;
	}
	
	/**
	 * Implémentation de la récupération du flux d'entrée du contenu de la
	 * partie.
	 */
	protected abstract InputStream getInputStreamImpl();

	/**
	 * Implémentation de la récupération du flux de sortie du contenu de la
	 * aprtie.
	 */
	protected abstract OutputStream getOutputStreamImpl();

	/**
	 * Enregistrement de la partie et de la partie de relations si la partie
	 * possède au moins une relation.
	 * 
	 * @param zos
	 *            Flux d'enregistrement
	 */
	public abstract void save(OutputStream zos);
	
	public abstract PartDocument getContentImpl();
}