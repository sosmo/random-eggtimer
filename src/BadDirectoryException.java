package munakello_public;

public class BadDirectoryException extends Exception {

	public BadDirectoryException() {
		super();
	}
	
	public BadDirectoryException(String message) {
		super(message);
	}
	
	public BadDirectoryException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
}
