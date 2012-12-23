package de.malkusch.soapClientCache.cache.exception;

public class CacheException extends Exception {

	private static final long serialVersionUID = 6052634441204775813L;

	public CacheException() {
	}

	public CacheException(String message) {
		super(message);
	}

	public CacheException(Throwable cause) {
		super(cause);
	}

	public CacheException(String message, Throwable cause) {
		super(message, cause);
	}

}
