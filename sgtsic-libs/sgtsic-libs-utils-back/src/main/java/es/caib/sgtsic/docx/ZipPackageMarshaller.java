package es.caib.sgtsic.docx;

import java.io.OutputStream;
import java.util.zip.ZipOutputStream;

public final class ZipPackageMarshaller extends ZipPartMarshaller {

	private Package pack;

	public ZipPackageMarshaller(Package pack) {
		this.pack = pack;
	}

	@Override
	public void marshall(PackagePart part, OutputStream os) {
		if (!(os instanceof ZipOutputStream))
			throw new IllegalArgumentException(
					"Le flux doit être un ZipOutputStream !");

		marshallRelationshipPart(pack.getRelationships(),
				PackageURIHelper.PACKAGE_RELATIONSHIPS_ROOT_URI,
				(ZipOutputStream) os);
		
		marshallRelationshipPart(pack.getDocumentRelationships(),
				PackageURIHelper.DOCUMENT_RELATIONSHIP_ROOT_URI,
				(ZipOutputStream) os);
	}
}