package de.malkusch.soapClientCache.cache;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.malkusch.soapClientCache.CacheHandler;
import de.malkusch.soapClientCache.cache.exception.CacheException;

/**
 * Cache backed by a map.
 * 
 * The backing map must be thread safe. If not use {@link Collections#synchronizedMap(Map)}
 * to get a thread safe map.
 * 
 * {@link ConcurrentHashMap} can be used if the result set is limited. Otherwise 
 * you might have a look at Apache Common's LRUMap. 
 * 
 * @see CacheHandler
 * @see Collections#synchronizedMap(Map)
 * @author Markus Malkusch <markus@malkusch.de>
 */
public class MapCache<K, V> extends Cache<K, V> {
	
	private Map<K, Payload<V>> map;

	/**
	 * Set the backing map and the seconds after which stored objects expire.
	 */
	public MapCache(int expireSeconds, Map<K, Payload<V>> map) {
		super(expireSeconds);
		this.map = map;
	}

	@Override
	public Payload<V> getPayload(K key) {
		return map.get(key);
	}

	@Override
	public void put(K key, Payload<V> payload) {
		map.put(key, payload);
	}

	@Override
	protected void remove(K key) {
		map.remove(key);
	}

	@Override
	public void clear() throws CacheException {
		map.clear();
	}

}
