package de.malkusch.soapClientCache;

import java.util.Calendar;

/**
 * Cache.
 * 
 * The abstract methods must be implemented thread safe. You don't have to put
 * synchronization in. You just have to take care that the cache stays in a defined
 * state. 
 * 
 * @see CacheHandler
 * @author Markus Malkusch <markus@malkusch.de>
 */
abstract public class Cache<K, V> {
	
	/**
	 * Seconds after which entries expire
	 */
	private int expireSeconds;

	/**
	 * Returns a cached object or null.
	 */
	abstract protected Payload<V> getPayload(K key);
	
	/**
	 * Stores an object into the cache.
	 */
	abstract public void put(K key, Payload<V> payload);
	
	/**
	 * Removes a cached object.
	 */
	abstract protected void remove(K key);
	
	/**
	 * Set the seconds after which stored objects expire.
	 */
	public Cache(int expireSeconds) {
		this.expireSeconds = expireSeconds;
	}
	
	/**
	 * Set the seconds after which stored objects expire.
	 */
	public void setExpireSeconds(int expireSeconds) {
		this.expireSeconds = expireSeconds;
	}
	
	/**
	 * Returns the seconds after which stored objects expire.
	 */
	public int getExpireSeconds() {
		return expireSeconds;
	}
	
	/**
	 * Stores an object into the cache.
	 */
	public void put(K key, V object) {
		Payload<V> payload = new Payload<V>(object);
		update(payload);
		put(key, payload);
	}
	
	/**
	 * Returns a cached object.
	 * 
	 * If no object was cached, null is returned.
	 */
	public V get(K key) {
		Payload<V> payload = getPayload(key);
		if (payload == null) {
			return null;
			
		}
		if (payload.getExpiration().before(Calendar.getInstance())) {
			remove(key);
			return null;
			
		}
		return payload.getPayload();
	}
	
	/**
	 * Updates the expirations date.
	 */
	private void update(Payload<V> payload) {
		payload.getExpiration().add(Calendar.SECOND, expireSeconds);
	}

}
