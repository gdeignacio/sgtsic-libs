package es.caib.sgtsic.docx;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

public final class MemoryPackagePart extends PackagePart {

	/**
	 * Les données de la partie.
	 */
	public byte[] data;

	/**
	 * La longueur des données.
	 */
	public int length;

	public MemoryPackagePart(Package pack, URI partURI, String contentType) {
		super(pack, partURI, contentType);
	}

	@Override
	protected InputStream getInputStreamImpl() {
		return new ByteArrayInputStream(data);
	}

	@Override
	protected OutputStream getOutputStreamImpl() {
		return new MemoryPackagePartOutputStream(this);
	}
	
	public void clear() {
		data = null;
		length = 0;
	}
	
	/**
	 * getContentImpl has to be added here, because the abstract was added in PackagePart
	 * Added by: Willy Ekasalim
	 */
	public PartDocument getContentImpl(){
		return null;
	}
	
	@Override
	public void save(OutputStream os) {
		new ZipPartMarshaller().marshall(this, os);
	}
	
	/**
	 * It seems that the object 'data' never get initialised
	 * So I create createArray() method to initialise it
	 * Added by: Willy Ekasalim - Allette Systems
	 * @param size the size of the byte array
	 */
	public void createArray(int size){
		data = new byte[size];
	}
}