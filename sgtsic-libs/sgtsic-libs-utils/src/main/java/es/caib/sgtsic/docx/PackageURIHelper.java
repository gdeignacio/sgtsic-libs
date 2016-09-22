package es.caib.sgtsic.docx;

import java.net.URI;
import java.net.URISyntaxException;

public final class PackageURIHelper {

	public static final URI PACKAGE_RELATIONSHIPS_ROOT_URI;

	public static final URI PACKAGE_ROOT_URI;
	
	public static final URI DOCUMENT_RELATIONSHIP_ROOT_URI;

	public static final String RELATIONSHIP_PART_EXTENSION_NAME;

	public static final String RELATIONSHIP_PART_SEGMENT_NAME;
	
	/**
	 * Add similar URI for document relationship (word/_rels/document.xml.rels)
	 * Added by: Willy Ekasalim - Allette Systems
	 */
	public static final String DOCUMENT_RELATIONSHIP_PART_EXTENSION_NAME;

	public static final String DOCUMENT_RELATIONSHIP_PART_SEGMENT_NAME;

	public static final char FORWARD_SLASH_CHAR;

	static {
		RELATIONSHIP_PART_SEGMENT_NAME = "_rels";
		RELATIONSHIP_PART_EXTENSION_NAME = ".rels";
		DOCUMENT_RELATIONSHIP_PART_SEGMENT_NAME = "word/_rels";
		DOCUMENT_RELATIONSHIP_PART_EXTENSION_NAME = "document.xml.rels";
		FORWARD_SLASH_CHAR = '/';

		URI uriPACKAGE_ROOT_URI = null;
		URI uriPACKAGE_RELATIONSHIPS_ROOT_URI = null;
		URI uriDOCUMENT_RELATIONSHIPS_ROOT_URI = null;
		try {
			uriPACKAGE_ROOT_URI = new URI("/");
			uriPACKAGE_RELATIONSHIPS_ROOT_URI = new URI(
					RELATIONSHIP_PART_SEGMENT_NAME + FORWARD_SLASH_CHAR
							+ RELATIONSHIP_PART_EXTENSION_NAME);
			uriDOCUMENT_RELATIONSHIPS_ROOT_URI = new URI(DOCUMENT_RELATIONSHIP_PART_SEGMENT_NAME 
					+ FORWARD_SLASH_CHAR + DOCUMENT_RELATIONSHIP_PART_EXTENSION_NAME);
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		PACKAGE_ROOT_URI = uriPACKAGE_ROOT_URI;
		PACKAGE_RELATIONSHIPS_ROOT_URI = uriPACKAGE_RELATIONSHIPS_ROOT_URI;
		DOCUMENT_RELATIONSHIP_ROOT_URI = uriDOCUMENT_RELATIONSHIPS_ROOT_URI;
	}

	/**
	 * Récupérer l'extention du fichier désigné par l'URI spécifié.
	 * 
	 * @param partURI
	 *            L'URI à vérifier.
	 * @return L'extension du fichier désigné par l'URI.
	 */
	public static String getExtensionForPartURI(URI partURI) {
		String fragment = partURI.getPath();
		if (fragment.length() > 0)
			return fragment.substring(fragment.lastIndexOf(".") + 1);
		return fragment;

	}

	/**
	 * Savoir si l'URI spécifiée est celle d'une partie de relation.
	 * 
	 * @param partUri
	 *            La partie à vérifier.
	 * @return <i>true</i> si la parties est de type relation sinon <i>false</i>.
	 */
	public static boolean isRelationshipPartURI(URI partUri) {
		if (partUri == null)
			throw new NullPointerException("partUri");

		return partUri.getPath().matches(
				".*" + RELATIONSHIP_PART_SEGMENT_NAME + ".*"
						+ RELATIONSHIP_PART_EXTENSION_NAME + "$");
	}

	/**
	 * Obtenir le nom du fichier à partir de son URI.
	 */
	public static String getFilename(URI uri) {
		if (uri != null) {
			String path = uri.getPath();
			int len = path.length();
			int num2 = len;
			while (--num2 >= 0) {
				char ch1 = path.charAt(num2);
				if (ch1 == PackageURIHelper.FORWARD_SLASH_CHAR)
					return path.substring(num2 + 1, len);
			}
		}
		return "";
	}

	/**
	 * Récupérer le nom du fichier sans son extension.
	 * 
	 * @param uri
	 *            L'URI à partir duquel extraire le chemin du fichier
	 */
	public static String getFilenameWithoutExtension(URI uri) {
		String filename = getFilename(uri);
		int dotIndex = filename.lastIndexOf(".");
		if (dotIndex == -1)
			return filename;
		return filename.substring(0, dotIndex - 1);
	}

	/**
	 * Obtenir le chemin sans le nom du fichier à paritr d'un URI.
	 */
	public static URI getPath(URI uri) {
		if (uri != null) {
			String path = uri.getPath();
			int len = path.length();
			int num2 = len;
			while (--num2 >= 0) {
				char ch1 = path.charAt(num2);
				if (ch1 == PackageURIHelper.FORWARD_SLASH_CHAR) {
					try {
						return new URI(path.substring(0, num2));
					} catch (URISyntaxException e) {
						return null;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Combine les deux URI.
	 * 
	 * @param prefix
	 *            L'URI de préfixe.
	 * @param suffix
	 *            L'URI de suffixe.
	 * @return
	 */
	public static URI combine(URI prefix, URI suffix) {
		URI retUri = null;
		try {
			retUri = new URI(combine(prefix.getPath(), suffix.getPath()));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return retUri;
	}

	/**
	 * Combien deux chemin sous forme de string.
	 */
	public static String combine(String prefix, String suffix) {
		if (!prefix.endsWith("" + FORWARD_SLASH_CHAR)
				&& !suffix.startsWith("" + FORWARD_SLASH_CHAR))
			return prefix + FORWARD_SLASH_CHAR + suffix;
		else if ((!prefix.endsWith("" + FORWARD_SLASH_CHAR)
				&& suffix.startsWith("" + FORWARD_SLASH_CHAR) || (prefix
				.endsWith("" + FORWARD_SLASH_CHAR) && !suffix.startsWith(""
				+ FORWARD_SLASH_CHAR))))
			return prefix + suffix;
		else
			return "";
	}

	/**
	 * Obtenir une URI à partir d'un chemin
	 */
	public static URI getURIFromPath(String path) {
		URI retUri = null;
		try {
			retUri = new URI(path);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return retUri;
	}

	public static URI getSourcePartUriFromRelationshipPartUri(
			URI relationshipPartUri) {
		if (relationshipPartUri == null)
			throw new IllegalArgumentException(
					"Le paramètre relationshipPartUri ne doit pas être null !");

		if (!isRelationshipPartURI(relationshipPartUri))
			throw new IllegalArgumentException(
					"L'URI ne doit pas être celle d'une partie de type relation.");

		if (relationshipPartUri.compareTo(PACKAGE_RELATIONSHIPS_ROOT_URI) == 0)
			return PACKAGE_RELATIONSHIPS_ROOT_URI;

		String filename = relationshipPartUri.getPath();
		String filenameWithoutExtension = getFilenameWithoutExtension(relationshipPartUri);
		filename = filename
				.substring(
						0,
						((filename.length() - filenameWithoutExtension.length()) - RELATIONSHIP_PART_EXTENSION_NAME
								.length()) - 1);
		filename = filename.substring(0, filename.length()
				- RELATIONSHIP_PART_SEGMENT_NAME.length());
		filename = combine(filename, filenameWithoutExtension);
		return getURIFromPath(filename);
	}

	public static URI getRelationshipPartUri(URI partUri) {
		if (partUri == null)
			throw new IllegalArgumentException("partUri");

		if (PackageURIHelper.PACKAGE_ROOT_URI.equals(partUri))
			return PackageURIHelper.PACKAGE_RELATIONSHIPS_ROOT_URI;

		if (PackageURIHelper.isRelationshipPartURI(partUri))
			throw new IllegalArgumentException(
					"Ne doit pas être une partie de relations !");

		String fullPath = partUri.getPath();
		String filename = getFilename(partUri);
		fullPath = fullPath.substring(0, fullPath.length() - filename.length());
		fullPath = combine(fullPath,
				PackageURIHelper.RELATIONSHIP_PART_SEGMENT_NAME);
		fullPath = combine(fullPath, filename);
		fullPath = fullPath + PackageURIHelper.RELATIONSHIP_PART_EXTENSION_NAME;
		return getURIFromPath(fullPath);
	}
}
