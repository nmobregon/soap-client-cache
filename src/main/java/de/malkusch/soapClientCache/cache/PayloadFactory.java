package de.malkusch.soapClientCache.cache;

public class PayloadFactory<T> {

	public Payload<T> getInstance(T object) {
		return new Payload<T>(object);
	}

}
