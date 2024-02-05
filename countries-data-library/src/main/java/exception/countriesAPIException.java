package exception;


// A class made to handle exceptions and inform the user about the error cause without the app's disruption 
public class countriesAPIException extends Exception{
	
	
	public countriesAPIException() {
		super();
	}
	
	public countriesAPIException(String message) {
		super(message);
	}
	
	public countriesAPIException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public countriesAPIException(Throwable cause) {
		super(cause);
	}
	
	
	protected countriesAPIException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
	
	
	
	
}
