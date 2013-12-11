package de.malkusch.soapClientCache.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;
import javax.xml.soap.SOAPMessage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.ECS.client.jax.ItemSearchRequest;
import com.ECS.client.jax.Items;

import de.malkusch.amazon.ecs.exception.RequestException;
import de.malkusch.soapClientCache.CacheHandler;

@RunWith(Parameterized.class)
public class TestHandler extends AbstractTest {

	private InvokationHandler invokationHandler;

	private Cache<String, SOAPMessage> cache;

	public TestHandler(Cache<String, SOAPMessage> cache) throws IOException {
		super();

		this.cache = cache;
		api.prependHandler(new CacheHandler(cache));

		invokationHandler = new InvokationHandler();
		api.appendHandler(invokationHandler);
	}

	@Before
	public void resetInvokationHandler() {
		invokationHandler.reset();
	}

	@Before
	public void clearCache() throws CacheException {
		cache.clear();
	}

	@Parameters
	public static Collection<Object[]> getParameters() throws IOException, ClassNotFoundException {
		Collection<Object[]> cases = new ArrayList<Object[]>();

		CachingProvider cachingProvider = Caching.getCachingProvider();
		CacheManager cacheManager = cachingProvider.getCacheManager();
		
		MutableConfiguration<String, SOAPMessage> config1 =
				new MutableConfiguration<String, SOAPMessage>()
				.setTypes(String.class, SOAPMessage.class)
				.setStoreByValue(false);
		cases.add(new Object[] { cacheManager.createCache("simpleCache", config1) });
		
		MutableConfiguration<String, SOAPMessage> config2 =
				new MutableConfiguration<String, SOAPMessage>()
				.setTypes(String.class, SOAPMessage.class)
				.setStoreByValue(true);
		cases.add(new Object[] { cacheManager.createCache("simpleCache2", config2) });
		
		return cases;
	}

	@Test
	public void testCachedCall() throws RequestException {
		ItemSearchRequest itemSearchRequest = new ItemSearchRequest();
		itemSearchRequest.setSearchIndex("Books");
		itemSearchRequest.setKeywords("Star Wars");

		Items items = api.getItemSearch().call(itemSearchRequest);
		assertTrue(items.getItem().size() > 0);
		assertTrue(invokationHandler.isInbound());
		assertTrue(invokationHandler.isOutbound());

		invokationHandler.reset();
		items = api.getItemSearch().call(itemSearchRequest);
		assertTrue(items.getItem().size() > 0);
		assertTrue(!invokationHandler.isInbound());
		assertTrue(!invokationHandler.isOutbound());
	}

	@Test
	public void testUncachedCall() throws RequestException {
		ItemSearchRequest itemSearchRequest = new ItemSearchRequest();
		itemSearchRequest.setSearchIndex("Books");
		itemSearchRequest.setKeywords("Star Wars");

		Items items = api.getItemSearch().call(itemSearchRequest);
		assertTrue(items.getItem().size() > 0);
		assertTrue(invokationHandler.isInbound());
		assertTrue(invokationHandler.isOutbound());

		invokationHandler.reset();
		itemSearchRequest.setKeywords("Harry Potter");
		items = api.getItemSearch().call(itemSearchRequest);
		assertTrue(items.getItem().size() > 0);
		assertTrue(invokationHandler.isInbound());
		assertTrue(invokationHandler.isOutbound());
	}

}
