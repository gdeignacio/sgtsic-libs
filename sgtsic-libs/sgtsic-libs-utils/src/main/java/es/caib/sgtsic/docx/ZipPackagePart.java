package es.caib.sgtsic.docx;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.zip.ZipEntry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class ZipPackagePart extends PackagePart {

	private ZipEntry zipEntry;

	public ZipPackagePart(Package parent, URI partURI, String contentType) {
		super(parent, partURI, contentType);
	}

	public ZipPackagePart(Package parent, ZipEntry zipEntry, URI partURI,
			String contentType) {
		super(parent, partURI, contentType);
		this.zipEntry = zipEntry;
	}

	public ZipEntry getZipArchive() {
		return zipEntry;
	}
	
	public PartDocument getContentImpl(){
		Document doc = null;
		try{
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	        doc = docBuilder.parse(this.getInputStream());
		}
		catch(Exception e){
			System.err.println("Error on returning this part content as Document object: " + e.getMessage());
			e.printStackTrace();
		}
		PartDocument partDoc = new PartDocument(doc, this);
		return partDoc;
	}

	@Override
	protected InputStream getInputStreamImpl() {
		try {
			// On utilise la méthode getInputStream() de la classe
			// java.util.zip.ZipFile qui renvoie un InputStream vers le contenu
			// de lentrée donnée en paramètre.
			return (container).getArchive().getInputStream(zipEntry);
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	protected OutputStream getOutputStreamImpl() {
		return null;
	}

	@Override
	public void save(OutputStream os) {
		new ZipPartMarshaller().marshall(this, os);
	}
}