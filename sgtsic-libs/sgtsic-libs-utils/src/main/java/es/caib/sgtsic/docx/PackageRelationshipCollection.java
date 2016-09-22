package es.caib.sgtsic.docx;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class PackageRelationshipCollection implements
		Iterable<PackageRelationship> {

	/**
	 * Les relations du paquet ordonnées par ID.
	 */
	private TreeMap<String, PackageRelationship> relationshipsByID;

	/**
	 * Les relations du paquet ordonnées par type.
	 */
	private TreeMap<String, PackageRelationship> relationshipsByType;

	/**
	 * PackagePart de cette partie.
	 */
	private PackagePart relationshipPart;

	/**
	 * La partie source à l'origine de cette relation.
	 */
	private PackagePart sourcePart;

	/**
	 * URI de cette partie.
	 */
	private URI uri;

	/**
	 * Référence vers le package parent.
	 */
	private Package container;

	/**
	 * Constructeur.
	 */
	private PackageRelationshipCollection() {
		relationshipsByID = new TreeMap<String, PackageRelationship>();
		relationshipsByType = new TreeMap<String, PackageRelationship>();
	}

	/**
	 * Constructeur de copie. Cette collection ne contiendra que les éléments de
	 * la collection passée en paramètre dont le type est compatible avec le
	 * filtre.
	 * 
	 * @param coll
	 *            La collection à importer.
	 * @param filter
	 *            Le filtre de la collection.
	 */
	public PackageRelationshipCollection(PackageRelationshipCollection coll,
			String filter) {
		this();
		for (PackageRelationship rel : coll.relationshipsByID.values())
			if (filter == null || rel.getRelationshipType().equals(filter))
				addRelationship(rel);
	}

	/**
	 * Constructeur.
	 */
	public PackageRelationshipCollection(Package container, PackageRelationshipType type) {
		this(container, null, type);
	}

	/**
	 * Constructeur.
	 */
	public PackageRelationshipCollection(PackagePart part) {
		this(part.container, part, null);
	}

	/**
	 * Constructeur.
	 * 
	 * @param container
	 *            La package parent.
	 * @param part
	 *            La partie dont dépendent les relations. Si null alors la
	 *            partie est la racine du package.
	 */
	public PackageRelationshipCollection(Package container, PackagePart part, PackageRelationshipType type) {
		this();
		this.container = container;
		this.sourcePart = part;
		uri = getRelationshipPartUri(sourcePart, type);
		if ((container.getPackageAccess() != PackageAccess.Write)
				&& container.partExists(uri)) {
			relationshipPart = container.getPart(uri);
			parseRelationshipsPart(relationshipPart);
		} else {
			throw new InvalidOperationException(
					"L'accès du package n'autorise pas cette opération ou la partie "
							+ uri + " n'existe pas !");
		}
	}

	/**
	 * Obtenir l'URI de la partie de relations dépendant de la partie spécifiée.
	 * 
	 * @param part
	 *            La partie source.
	 * @return L'URI de la partie de relation ou <i>null</i> si la partie ne
	 *         possède pas de partie de relations.
	 */
	private static URI getRelationshipPartUri(PackagePart part, PackageRelationshipType type) {
		URI retUri;
		if (part == null)
			retUri = PackageURIHelper.PACKAGE_ROOT_URI;
		else
			retUri = part.uri;
		// On cherche l'URI de la partie de relations
		if (retUri.getPath() == PackageURIHelper.PACKAGE_ROOT_URI.getPath()){
			if (type == PackageRelationshipType.PACKAGE)
				return PackageURIHelper.PACKAGE_RELATIONSHIPS_ROOT_URI;
			else if (type == PackageRelationshipType.DOCUMENT)
				return PackageURIHelper.DOCUMENT_RELATIONSHIP_ROOT_URI;
		}
		if (part.isRelationshipPart())
			throw new IllegalArgumentException(
					"Ne doit pas être une partie de relations !");

		String uriStr = retUri.getPath();
		String filename = PackageURIHelper.getFilename(retUri);

		uriStr = uriStr.substring(0, uriStr.length() - filename.length());
		uriStr += PackageURIHelper.RELATIONSHIP_PART_SEGMENT_NAME
				+ PackageURIHelper.FORWARD_SLASH_CHAR + filename
				+ PackageURIHelper.RELATIONSHIP_PART_EXTENSION_NAME;
		try {
			retUri = new URI(uriStr);
		} catch (URISyntaxException e) {
		}
		return retUri;
	}

	/**
	 * Ajoute la relation spécifiée à cette collection.
	 * 
	 * @param relPart
	 *            La relation à ajouter.
	 */
	public void addRelationship(PackageRelationship relPart) {
		relationshipsByID.put(relPart.getId(), relPart);
		relationshipsByType.put(relPart.getRelationshipType(), relPart);
	}

	/**
	 * Ajoute une relation à cette collection.
	 * 
	 * @param targetUri
	 *            L'URI de la cible
	 * @param targetMode
	 *            Le mode INTERNAL ou EXTERNAL (voir PackageAccess)
	 * @param relationshipType
	 *            Le type de relation
	 * @param id
	 *            L'ID de la relation
	 * @return La relation nouvellement créée et ajoutée.
	 */
	public PackageRelationship addRelationship(URI targetUri,
			TargetMode targetMode, String relationshipType, String id) {

		if (id == null) {
			// Génération d'un identifiant unique si le paramètre est null
			int i = 0;
			do {
				id = "rId" + ++i;
			} while (relationshipsByID.get(id) != null);
		}
		// Création de l'objet PackageRelationship
		PackageRelationship rel = new PackageRelationship(container,
				sourcePart, targetUri, targetMode, relationshipType, id);
		// Ajout de la relation aux collections
		relationshipsByID.put(rel.getId(), rel);
		relationshipsByType.put(rel.getRelationshipType(), rel);
		return rel;
	}

	/**
	 * Suppression de la relation à partir de son ID.
	 * 
	 * @param id
	 *            L'identifiant de la relation à supprimer.
	 */
	public void removeRelationship(String id) {
		if (relationshipsByID != null && relationshipsByType != null) {
			PackageRelationship rel = relationshipsByID.get(id);
			if (rel != null) {
				relationshipsByID.remove(rel.getId());
				relationshipsByType.values().remove(rel);
			}
		}
	}

	/**
	 * Suppression de la relation à partir de son ID.
	 * 
	 * @param rel
	 *            La relation à supprimer.
	 */
	public void removeRelationship(PackageRelationship rel) {
		if (rel == null)
			throw new IllegalArgumentException(
					"Le paramètre rel ne doit pas être nul !");

		relationshipsByID.values().remove(rel);
		relationshipsByType.values().remove(rel);
	}

	/**
	 * Récupérer la relation dont l'index est spécifié par rapport à sa position
	 * ordonnée des ID.
	 * 
	 * @param index
	 *            L'index de la relation compris 0 et <nombre de relations>-1
	 */
	public PackageRelationship getRelationship(int index) {
		if (index < 0 || index > relationshipsByID.values().size())
			throw new IllegalArgumentException(
					"L'index doit être compris entre 0 et <nombre de relations>-1");

		PackageRelationship retRel = null;
		int i = 0;
		for (PackageRelationship rel : relationshipsByID.values())
			if (index == i++)
				return rel;
		return retRel;
	}

	/**
	 * Récupérer le nombre de relations de la collection.
	 */
	public int size() {
		return relationshipsByID.values().size();
	}

	/**
	 * Parse le fichier de relation et charge les relations dans cette
	 * collection.
	 * 
	 * @param relPart
	 *            Le fichier de relation à parser.
	 */
	private void parseRelationshipsPart(PackagePart relPart) {
		// Création du parser DOM
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		documentBuilderFactory.setIgnoringElementContentWhitespace(true);
		DocumentBuilder documentBuilder;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();

			// On parse le document XML en arbre DOM
			Document xmlRelationshipsDoc = documentBuilder.parse(relPart
					.getInputStream());

			// On parcourt les types par défaut
			NodeList rels = xmlRelationshipsDoc
					.getElementsByTagName(PackageRelationship.RELATIONSHIP_TAG_NAME);
			for (int i = 0; i < rels.getLength(); ++i) {
				// On ajoute l'élément du type par défaut dans sa collection
				Node rel = rels.item(i);
				// L'identificant unique de la relation
				String id = rel.getAttributes().getNamedItem(
						PackageRelationship.ID_ATTRIBUTE_NAME).getNodeValue();
				// Le type de la relation
				String type = rel.getAttributes().getNamedItem(
						PackageRelationship.TYPE_ATTRIBUTE_NAME).getNodeValue();

				// L'élément Target converti en URI
				URI target;
				try {
					target = new URI(rel.getAttributes().getNamedItem(
							PackageRelationship.TARGET_ATTRIBUTE_NAME)
							.getNodeValue());
				} catch (URISyntaxException e) {
					continue;
				}

				// L'élément TargetMode (valeur par défaut "Internal")
				Node targetModeAttr = rel.getAttributes().getNamedItem(
						PackageRelationship.TARGET_MODE_ATTRIBUTE_NAME);
				TargetMode targetMode = TargetMode.INTERNAL;
				if (targetModeAttr != null)
					targetMode = targetModeAttr.getNodeValue().toLowerCase()
							.equals("internal") ? TargetMode.INTERNAL
							: TargetMode.EXTERNAL;
				addRelationship(target, targetMode, type, id);
			}
		} catch (ParserConfigurationException e) {

		} catch (IOException ioe) {

		} catch (SAXException saxe) {

		}
	}

	/**
	 * Récupérer toutes les relations correspondant au filtre.
	 * 
	 * @param filter
	 *            Le type de la relation à filtrer. Si <i>null</i> alors le
	 *            filtre est désactivé.
	 * @return Les relations validées par le filtre.
	 */
	public PackageRelationshipCollection getRelationships(String filter) {
		PackageRelationshipCollection coll = new PackageRelationshipCollection(
				this, filter);
		return coll;
	}

	public Iterator<PackageRelationship> iterator() {
		return relationshipsByID.values().iterator();
	}

	/**
	 * Obtenir les relations dont le type est celui spécifié.
	 * 
	 * @param typeFilter
	 *            Le type servant de filtre.
	 * @return Les relations dont le type est égal au filtre.
	 */
	public Iterator<PackageRelationship> iterator(String typeFilter) {
		ArrayList<PackageRelationship> retArr = new ArrayList<PackageRelationship>();
		for (PackageRelationship rel : relationshipsByID.values()) {
			if (rel.getRelationshipType().equals(typeFilter))
				retArr.add(rel);
		}
		return retArr.iterator();
	}

	/**
	 * Vide la collection de toutes les relations.
	 */
	public void clear() {
		relationshipsByID.clear();
		relationshipsByType.clear();
	}
}