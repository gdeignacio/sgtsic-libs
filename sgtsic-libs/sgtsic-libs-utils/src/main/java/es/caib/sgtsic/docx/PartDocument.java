package es.caib.sgtsic.docx;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * PartDocument contains Document object of a Part.
 * And it provides easy interface for appending new element on the content
 * @author Willy Ekasalim, Allette Systems
 *
 */
public class PartDocument {
	
	/**
	 * The document XML content
	 */
	private Document content;
	
	/**
	 * Keep reference to its ZipPackagePart
	 */
	private ZipPackagePart zipPackagePart;
	
	/**
	 * Constructor of PartDocument
	 * @param content, the XML content
	 * @param zipPackagePart, reference to ZipPackagePart object
	 */
	public PartDocument(Document content, ZipPackagePart zipPackagePart){
		this.content = content;
		this.zipPackagePart = zipPackagePart;
	}
	
	/**
	 * Append new child to the current content, 
	 * and then flag it to zipPackagePart that it has been modified (isModified)
	 * and set the reference to the content back to ZipPackagePart for saving purpose later.
	 * @param newContent, the new content to appended
	 */
	public void appendChild(Document newContent){
		//I use namespace and it did not work
		Node styleBody = content.getElementsByTagName("w:styles").item(0);
		Node newStyle =  newContent.getElementsByTagName("w:style").item(0);
		styleBody.appendChild(content.importNode(newStyle, true));
		zipPackagePart.setModified(true);
		zipPackagePart.setPartDoc(this);
	}
	
	/**
	 * Replace the content of this document
	 * and then flag it to zipPackagePart that it has been modified (isModified)
	 * and set the reference to the content back to ZipPackagePart for saving purpose later.
	 * @param newContent, the newContent to replace the current content.
	 */
	public void replaceContent(Document newContent){
		this.content = newContent;
		zipPackagePart.setModified(true);
		zipPackagePart.setPartDoc(this);
	}
	
	
	public Document getContent(){
		return content;
	}
}
