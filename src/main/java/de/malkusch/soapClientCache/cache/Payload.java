package de.malkusch.soapClientCache.cache;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Cached Data.
 * 
 * The cached data is decorated with the date.
 * 
 * @author Markus Malkusch <markus@malkusch.de>
 */
public class Payload<T> implements Serializable {
	
	private static final long serialVersionUID = 6235483024218533135L;

	private T payload;
	
	private Calendar expiration = Calendar.getInstance();

	public Payload(T payload) {
		this.payload = payload;
	}
	
	public T getPayload() {
		return payload;
	}

	public Calendar getExpiration() {
		return expiration;
	}

	public void setExpiration(Calendar expiration) {
		this.expiration = expiration;
	}

}
