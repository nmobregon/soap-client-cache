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
import de.malkusch.soapClientCache.Cache;
import de.malkusch.soapClientCache.CacheHandler;
import de.malkusch.soapClientCache.MapCache;
import de.malkusch.soapClientCache.Payload;

public class TestHandler extends AbstractTest {

	public TestHandler() throws IOException {
		super();
		
		Map<String, Payload<SOAPMessage>> map = new ConcurrentHashMap<String, Payload<SOAPMessage>>();
		Cache<String, SOAPMessage> cache = new MapCache<String, SOAPMessage>(60, map);
		CacheHandler cacheHandler = new CacheHandler(cache);
		api.appendHandler(cacheHandler);
	}

	@Test
	public void testCachedCall() throws RequestException {
		{
			ItemSearchRequest itemSearchRequest = new ItemSearchRequest();
			itemSearchRequest.setSearchIndex("Books");
			itemSearchRequest.setKeywords("Star Wars");
	
			Items items = api.getItemSearch().call(itemSearchRequest);
			assertTrue(items.getItem().size() > 0);
		}
		{
			ItemSearchRequest itemSearchRequest = new ItemSearchRequest();
			itemSearchRequest.setSearchIndex("Books");
			itemSearchRequest.setKeywords("Star Wars");
	
			Items items = api.getItemSearch().call(itemSearchRequest);
			assertTrue(items.getItem().size() > 0);
		}
	}

}
