package de.malkusch.soapClientCache.test.rule;

import java.util.Properties;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;
import javax.xml.soap.SOAPMessage;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import com.ECS.client.jax.AWSECommerceService;

import de.malkusch.amazon.ecs.ProductAdvertisingAPI;
import de.malkusch.amazon.ecs.configuration.PropertiesConfiguration;
import de.malkusch.soapClientCache.CacheHandler;

public class CacheRule implements MethodRule {
	
	private ProductAdvertisingAPI api;
	
	private Cache<String, SOAPMessage> cache;
	
	private MutableConfiguration<String, SOAPMessage> cacheConfiguration = new MutableConfiguration<>(); 
	
	public CacheRule(boolean isStoreByValue) {
		cacheConfiguration.setTypes(String.class, SOAPMessage.class);
		cacheConfiguration.setStoreByValue(isStoreByValue);
	}
	
	public ProductAdvertisingAPI getProductAdvertisingAPI() {
		return api;
	}
	
	public Cache<String, SOAPMessage> getCache() {
		return cache;
	}
	
	@Override
	public Statement apply(final Statement statement, FrameworkMethod method, Object target) {
		return new Statement() {

			@Override
			public void evaluate() throws Throwable {
				try (	
						CachingProvider cachingProvider = Caching.getCachingProvider();
						CacheManager cacheManager = cachingProvider.getCacheManager();
						Cache<String, SOAPMessage> cacheResource = cacheManager.createCache("simpleCache", cacheConfiguration)) {
					
					cache = cacheResource;
					
					PropertiesConfiguration configuration;
					Properties properties = new Properties();
					properties.load(getClass().getResourceAsStream("/amazon.properties"));
					configuration = new PropertiesConfiguration(properties);

					api = new ProductAdvertisingAPI(configuration,
							new AWSECommerceService().getAWSECommerceServicePortDE());
					api.prependHandler(new CacheHandler(cache));
					
					statement.evaluate();
					
					cacheManager.destroyCache(cache.getName());
					
				}
			}
		};
	}


}
