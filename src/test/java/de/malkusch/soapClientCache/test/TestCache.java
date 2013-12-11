package de.malkusch.soapClientCache.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.malkusch.soapClientCache.cache.Cache;
import de.malkusch.soapClientCache.cache.MapCache;
import de.malkusch.soapClientCache.cache.Payload;
import de.malkusch.soapClientCache.cache.dataSource.DataSourceCache;
import de.malkusch.soapClientCache.cache.exception.CacheException;

@RunWith(Parameterized.class)
public class TestCache {

	private Cache<String, String> cache;

	@Parameters
	public static Collection<Object[]> getParameters() throws IOException, ClassNotFoundException {
		Collection<Object[]> cases = new ArrayList<Object[]>();

		cases.add(new Object[] {
				new MapCache<String, String>(
						1, new ConcurrentHashMap<String, Payload<String>>()) });
		
		JdbcConnectionPool dataSource = JdbcConnectionPool.create(
				"jdbc:h2:mem;TRACE_LEVEL_FILE=0", "", "");
		cases.add(new Object[]{new DataSourceCache<String, String>(1, dataSource)});

		return cases;
	}
	
	public TestCache(Cache<String, String> cache) {
		this.cache = cache;
	}
	
	@Before
	public void clearCache() throws CacheException {
		cache.clear();
	}

	/**
	 * Tests get() and put()
	 * @throws CacheException 
	 */
	@Test
	public void putAndGetTest() throws CacheException {
		String key = "putAndGetTest";
		assertNull(cache.get(key));
		
		String object = "object";
		cache.put(key, object);
		for (int i = 0; i < 10; i++) {
			assertEquals(object, cache.get(key));
			
		}
	}
	
	/**
	 * Tests that get() doesn't read always the same object
	 */
	@Test
	public void getDifferentTest() throws CacheException {
		String key1    = "getDifferentTest1";
		String object1 = "object1";

		String key2    = "getDifferentTest2";
		String object2 = "object2";
		
		cache.put(key1, object1);
		cache.put(key2, object2);
		
		assertFalse(cache.get(key1).equals(cache.get(key2)));
	}
	
	/**
	 * Tests expiring values.
	 * @throws CacheException 
	 */
	@Test
	public void expireTest() throws InterruptedException, CacheException {
		String key = "expireTest";
		cache.put(key, "value");
		Thread.sleep((cache.getExpireSeconds() + 1) * 1000);
		assertNull(cache.get(key));
	}

}
