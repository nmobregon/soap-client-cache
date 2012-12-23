package de.malkusch.soapClientCache.cache.exception;

public class KeyException extends CacheException {

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
