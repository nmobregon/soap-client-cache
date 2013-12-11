package de.malkusch.soapClientCache.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.soap.SOAPMessage;

import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.ECS.client.jax.ItemSearchRequest;
import com.ECS.client.jax.Items;

import de.malkusch.amazon.ecs.exception.RequestException;
import de.malkusch.soapClientCache.CacheHandler;
import de.malkusch.soapClientCache.cache.Cache;
import de.malkusch.soapClientCache.cache.MapCache;
import de.malkusch.soapClientCache.cache.Payload;
import de.malkusch.soapClientCache.cache.dataSource.DataSourceCache;
import de.malkusch.soapClientCache.cache.exception.CacheException;

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

		cases.add(new Object[] {
				new MapCache<String, SOAPMessage>(
						10, new ConcurrentHashMap<String, Payload<SOAPMessage>>()) });
		
		JdbcConnectionPool dataSource = JdbcConnectionPool.create(
				"jdbc:h2:mem;TRACE_LEVEL_FILE=0", "", "");
		cases.add(new Object[]{new DataSourceCache<String, SOAPMessage>(10, dataSource)});

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
