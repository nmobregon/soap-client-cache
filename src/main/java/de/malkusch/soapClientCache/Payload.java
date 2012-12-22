package de.malkusch.soapClientCache;

import java.util.Calendar;

/**
 * Cached Data.
 * 
 * The cached data is decorated with the date.
 * 
 * @author Markus Malkusch <markus@malkusch.de>
 */
public class Payload<T> {
	
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
