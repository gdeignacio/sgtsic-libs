package es.caib.sgtsic.docx;

@SuppressWarnings("serial")
public class InvalidFormatException extends RuntimeException{

	public InvalidFormatException(String message){
		super(message);
	}
}