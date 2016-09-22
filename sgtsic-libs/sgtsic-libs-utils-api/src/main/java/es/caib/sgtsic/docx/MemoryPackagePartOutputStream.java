package es.caib.sgtsic.docx;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Créé un flux de sortie pour les parties de type MemoryPackagePart.
 * 
 * @author Julien Chable
 */
public final class MemoryPackagePartOutputStream extends OutputStream {

	private MemoryPackagePart part;

	private ByteArrayOutputStream buff;

	public MemoryPackagePartOutputStream(MemoryPackagePart part) {
		this.part = part;
		buff = new ByteArrayOutputStream();
	}

	@Override
	public void write(int b) throws IOException {
		buff.write(b);
	}

	@Override
	public void close() throws IOException {
		this.flush();
	}

	@Override
	public void flush() throws IOException {
		buff.flush();
		//Willy: i dont know why the following cause an error 
		//so i replace it with temporary solution that writes the data to part.data
		
		//byte[] newArray = new byte[part.data.length + buff.size()];
		//System.arraycopy(part.data, 0, newArray, 0, part.data.length);
		//byte[] buffArr = buff.toByteArray();
		//System
		//		.arraycopy(buffArr, 0, newArray, part.data.length,
		//				buffArr.length);
		part.createArray(buff.size());
		byte[] buffArr = buff.toByteArray();
		System
				.arraycopy(buffArr, 0, part.data, 0,
						buffArr.length);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		buff.write(b, off, len);
	}

	@Override
	public void write(byte[] b) throws IOException {
		buff.write(b);
	}
}