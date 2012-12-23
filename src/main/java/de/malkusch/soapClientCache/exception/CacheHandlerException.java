package de.malkusch.soapClientCache.exception;

public class CacheHandlerException extends Exception {

	private static final long serialVersionUID = -8557996723820492032L;

	public CacheHandlerException(String message) {
		super(message);
	}

	public CacheHandlerException(Throwable cause) {
		super(cause);
	}

	public CacheHandlerException(String message, Throwable cause) {
		super(message, cause);
	}

}
