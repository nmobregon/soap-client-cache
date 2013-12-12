package de.malkusch.soapClientCache.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.ECS.client.jax.ItemSearchRequest;

import de.malkusch.amazon.ecs.ProductAdvertisingAPI;
import de.malkusch.amazon.ecs.exception.RequestException;
import de.malkusch.soapClientCache.test.rule.CacheRule;

public class TestSerialization {
	
	private ProductAdvertisingAPI api;
	
	@Rule
	public CacheRule cacheRule = new CacheRule(true);

	@Before
	public void api() throws IOException {
		api = cacheRule.getProductAdvertisingAPI();
	}
	
	@Test
	public void test() throws RequestException {
		ItemSearchRequest itemSearchRequest = new ItemSearchRequest();
		itemSearchRequest.setSearchIndex("Books");
		itemSearchRequest.setKeywords("Star Wars");

		api.getItemSearch().call(itemSearchRequest);
		api.getItemSearch().call(itemSearchRequest);
		
		assertTrue(true);
	}

}
