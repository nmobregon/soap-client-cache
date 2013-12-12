package de.malkusch.soapClientCache.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.ECS.client.jax.ItemSearchRequest;
import com.ECS.client.jax.Items;

import de.malkusch.amazon.ecs.ProductAdvertisingAPI;
import de.malkusch.amazon.ecs.exception.RequestException;
import de.malkusch.soapClientCache.test.helper.InvokationHandler;
import de.malkusch.soapClientCache.test.rule.CacheRule;

public class TestHandler {

	private InvokationHandler invokationHandler;

	private ProductAdvertisingAPI api;
	
	@Rule
	public CacheRule cacheRule = new CacheRule(false);

	@Before
	public void api() throws IOException {
		api = cacheRule.getProductAdvertisingAPI();
		invokationHandler = new InvokationHandler();
		api.appendHandler(invokationHandler);
	}
	
	@After
	public void after() {
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
