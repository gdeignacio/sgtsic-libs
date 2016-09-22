package es.caib.sgtsic.docx;

import java.net.URI;

/**
 * Représente une relation d'une partie.
 * 
 * @author Julien Chable
 * @version 0.2
 */
public class PackageRelationship {

	public static final String ID_ATTRIBUTE_NAME = "Id";

	public static final String RELATIONSHIPS_NAMESPACE = "http://schemas.openxmlformats.org/package/2006/relationships";

	public static final String RELATIONSHIPS_TAG_NAME = "Relationships";

	public static final String RELATIONSHIP_TAG_NAME = "Relationship";

	public static final String TARGET_ATTRIBUTE_NAME = "Target";

	public static final String TARGET_MODE_ATTRIBUTE_NAME = "TargetMode";

	public static final String TYPE_ATTRIBUTE_NAME = "Type";

	/**
	 * L'ID de la relation.
	 */
	private String id;

	/**
	 * Référence vers le package.
	 */
	private Package container;

	/**
	 * Type de relation.
	 */
	private String relationshipType;

	/**
	 * Partie source de cette relation.
	 */
	private PackagePart source;

	/**
	 * Le mode de ciblage [Internal|External]
	 */
	private TargetMode targetMode;

	/**
	 * URI de la partie cible.
	 */
	private URI targetUri;

	public PackageRelationship(Package packageParent, PackagePart sourcePart,
			URI targetUri, TargetMode targetMode, String relationshipType,
			String id) {
		this.container = packageParent;
		this.source = sourcePart;
		this.targetUri = targetUri;
		this.targetMode = targetMode;
		this.relationshipType = relationshipType;
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PackageRelationship))
			return false;

		PackageRelationship rel = (PackageRelationship) obj;
		return (this.id == rel.id
				&& this.relationshipType == rel.relationshipType
				&& (rel.source != null ? rel.source.equals(this.source) : true)
				&& this.targetMode == rel.targetMode && this.targetUri
				.equals(rel.targetUri));
	}

	@Override
	public int hashCode() {
		return this.id.hashCode() + this.relationshipType.hashCode()
				+ this.source.hashCode() + this.targetMode.hashCode()
				+ this.targetUri.hashCode();
	}

	/* Accesseurs */

	public Package getContainer() {
		return container;
	}

	public String getId() {
		return id;
	}

	public String getRelationshipType() {
		return relationshipType;
	}

	public PackagePart getSource() {
		return source;
	}

	public TargetMode getTargetMode() {
		return targetMode;
	}

	public URI getTargetUri() {
		return targetUri;
	}

}