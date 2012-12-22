This is a SoapHandler implementing a cache for JAX-WS SOAP clients.

First you have to define a Cache implementation. If you don't implement your own you
could use the MapCache. MapCache is backed by a map. You must provide the map to MapCache.
Please take care that the map is thread safe! You could use ConcurrentHashMap for that.

    Map<String, Payload<SOAPMessage>> map = new ConcurrentHashMap<String, Payload<SOAPMessage>>();
    Cache<String, SOAPMessage> cache = new MapCache<String, SOAPMessage>(60, map);

A generic approach would be a wrapped map by  Collections.synchronizedMap(). If you expect a
too big amount of cached objects you should use a limited Map. Apache Common's LRUMap
in conjunction with Collections.synchronizedMap() would be a good choice.

Now you can create the CacheHandler and append it to your SOAP port.

    CacheHandler cacheHandler = new CacheHandler(cache);
    Binding binding = ((BindingProvider) port).getBinding();
    List<Handler> handlerList = binding.getHandlerChain();
    handlerList.add(cacheHandler);
    binding.setHandlerChain(handlerList);

# Maven
You find this package in my maven repository: http://mvn.malkusch.de

    <repositories>
        <repository>
            <id>malkusch.de</id>
            <url>http://mvn.malkusch.de/</url>
        </repository>
    </repositories>

Add the following dependency to your pom.xml

    <dependency>
        <groupId>de.malkusch</groupId>
        <artifactId>soap-client-cache</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
