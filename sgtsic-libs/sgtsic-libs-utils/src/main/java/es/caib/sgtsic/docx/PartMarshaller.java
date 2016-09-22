package es.caib.sgtsic.docx;

import java.io.OutputStream;

public interface PartMarshaller {

	public void marshall(PackagePart part, OutputStream out);
}