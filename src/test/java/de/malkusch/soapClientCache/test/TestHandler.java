package de.malkusch.soapClientCache.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Properties;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;
import javax.xml.soap.SOAPMessage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ECS.client.jax.AWSECommerceService;
import com.ECS.client.jax.ItemSearchRequest;
import com.ECS.client.jax.Items;

import de.malkusch.amazon.ecs.ProductAdvertisingAPI;
import de.malkusch.amazon.ecs.configuration.PropertiesConfiguration;
import de.malkusch.amazon.ecs.exception.RequestException;
import de.malkusch.soapClientCache.CacheHandler;
import de.malkusch.soapClientCache.test.helper.InvokationHandler;

public class TestHandler {

	private InvokationHandler invokationHandler;

	private ProductAdvertisingAPI api;

	private Cache<String, SOAPMessage> cache;

	@Before
	public void before() throws IOException {
		CachingProvider cachingProvider = Caching.getCachingProvider();
		CacheManager cacheManager = cachingProvider.getCacheManager();

		MutableConfiguration<String, SOAPMessage> config = new MutableConfiguration<String, SOAPMessage>()
				.setTypes(String.class, SOAPMessage.class).setStoreByValue(
						false);
		cache = cacheManager.createCache("simpleCache", config);
		
		PropertiesConfiguration configuration;

		Properties properties = new Properties();
		properties.load(getClass().getResourceAsStream("/amazon.properties"));
		configuration = new PropertiesConfiguration(properties);

		api = new ProductAdvertisingAPI(configuration,
				new AWSECommerceService().getAWSECommerceServicePortDE());
		
		api.prependHandler(new CacheHandler(cache));

		invokationHandler = new InvokationHandler();
		api.appendHandler(invokationHandler);
	}
	
	@After
	public void after() {
		cache.clear();
		Caching.getCachingProvider().getCacheManager().destroyCache(cache.getName());
		invokationHandler.reset();
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
