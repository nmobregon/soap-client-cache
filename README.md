# CacheHandler
CacheHandler is a SOAPHandler implementing a cache for JAX-WS SOAP clients.

The handler is backed by JCache (JSR-107). You have to provide that cache.
After configuring your objects you have to append the CacheHandler to your port:
```java
MutableConfiguration<String, SOAPMessage> config = new MutableConfiguration<>();
config.setTypes(String.class, SOAPMessage.class);
config.setStoreByValue(true);

try (
		CachingProvider cachingProvider = Caching.getCachingProvider();
		CacheManager cacheManager = cachingProvider.getCacheManager();
		Cache<String, SOAPMessage> cache = cacheManager.createCache("soap", config)) {
	
	CacheHandler cacheHandler = new CacheHandler(cache);
	
	Binding binding = ((BindingProvider) port).getBinding();
	List<Handler> handlerChain = binding.getHandlerChain();
	handlerChain.add(cacheHandler);
	binding.setHandlerChain(handlerChain);
}
```

# Maven
You find this package in Maven central:

```xml
<dependency>
    <groupId>de.malkusch.soap-client-cache</groupId>
    <artifactId>soap-client-cache</artifactId>
    <version>2.0.0</version>
</dependency>
```
