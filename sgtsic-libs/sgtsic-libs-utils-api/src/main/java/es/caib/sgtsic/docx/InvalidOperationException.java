package es.caib.sgtsic.docx;

@SuppressWarnings("serial")
public class InvalidOperationException extends RuntimeException{

	public InvalidOperationException(String message){
		super(message);
	}
}