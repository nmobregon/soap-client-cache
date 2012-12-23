package de.malkusch.soapClientCache.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.soap.SOAPMessage;

import org.junit.Test;

import com.ECS.client.jax.ItemSearchRequest;
import com.ECS.client.jax.Items;

import de.malkusch.amazon.ecs.exception.RequestException;
import de.malkusch.soapClientCache.cache.Cache;
import de.malkusch.soapClientCache.CacheHandler;
import de.malkusch.soapClientCache.cache.MapCache;
import de.malkusch.soapClientCache.cache.Payload;

public class TestHandler extends AbstractTest {

	private InvokationHandler invokationHandler;

	public TestHandler() throws IOException {
		super();

		Map<String, Payload<SOAPMessage>> map = new ConcurrentHashMap<String, Payload<SOAPMessage>>();
		Cache<String, SOAPMessage> cache = new MapCache<String, SOAPMessage>(60, map);
		CacheHandler cacheHandler = new CacheHandler(cache);
		api.prependHandler(cacheHandler);

		invokationHandler = new InvokationHandler();
		api.appendHandler(invokationHandler);
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
		assertTrue(! invokationHandler.isInbound());
		assertTrue(! invokationHandler.isOutbound());
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
