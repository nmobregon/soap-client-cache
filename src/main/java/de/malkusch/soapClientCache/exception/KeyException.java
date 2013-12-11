package de.malkusch.soapClientCache.exception;


public class KeyException extends CacheHandlerException {

	private static final long serialVersionUID = -4247365240199753670L;

	public KeyException(String message) {
		super(message);
	}

	public KeyException(Throwable cause) {
		super(cause);
	}

	public KeyException(String message, Throwable cause) {
		super(message, cause);
	}

}
