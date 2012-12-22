package de.malkusch.soapClientCache.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.malkusch.soapClientCache.Cache;
import de.malkusch.soapClientCache.MapCache;
import de.malkusch.soapClientCache.Payload;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class TestCache {

	private Cache<String, String> cache;

	@Parameters
	public static Collection<Object[]> getParameters() {
		Collection<Object[]> cases = new ArrayList<Object[]>();

		cases.add(new Object[] {
				new MapCache<String, String>(
						1, new ConcurrentHashMap<String, Payload<String>>()) });

		return cases;
	}
	
	public TestCache(Cache<String, String> cache) {
		this.cache = cache;
	}

	/**
	 * Tests get() and put()
	 */
	@Test
	public void putAndGetTest() {
		String key = "putAndGetTest";
		assertNull(cache.get(key));
		
		String object = "object";
		cache.put(key, object);
		for (int i = 0; i < 100; i++) {
			assertSame(object, cache.get(key));
			
		}
	}
	
	/**
	 * Tests expiring values.
	 */
	@Test
	public void expireTest() throws InterruptedException {
		String key = "expireTest";
		cache.put(key, "value");
		Thread.sleep((cache.getExpireSeconds() + 1) * 1000);
		assertNull(cache.get(key));
	}

}
