package de.malkusch.soapClientCache;

import java.util.Set;

import javax.cache.Cache;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.malkusch.soapClientCache.exception.KeyException;
import de.malkusch.soapClientCache.key.KeyAdapter;
import de.malkusch.soapClientCache.key.SOAPMessageAdapter;

/**
 * Adds JCache (JSR-107) caching to a JAX-WS SOAP client.
 * 
 * @author Markus Malkusch <markus@malkusch.de>
 * @see Cache
 */
public class CacheHandler implements SOAPHandler<SOAPMessageContext> {

	final static public String CACHE_KEY = CacheHandler.class.getName() + ".cacheKey";
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private Cache<String, SOAPMessage> cache;
	
	private KeyAdapter keyAdapter;
	
	/**
	 * Initializes this object
	 */
	public CacheHandler(Cache<String, SOAPMessage> cache, KeyAdapter keyAdapter) {
		this.cache = cache;
		this.keyAdapter = keyAdapter;
	}
	
	/**
	 * Creates a CacheHandler with SoapMessageAdapter for generating cache keys
	 * 
	 * @see SOAPMessageAdapter
	 */
	public CacheHandler(Cache<String, SOAPMessage> cache) {
		this(cache, new SOAPMessageAdapter());
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		Boolean outbound = (Boolean) context
				.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		
		return outbound ? handleRequestMessage(context) : handleResponseMessage(context);
	}

	private boolean handleRequestMessage(SOAPMessageContext context) {
		try {
			String cacheKey = keyAdapter.adapt(context);
			
			SOAPMessage cachedMessage = cache.get(cacheKey);
			if (cachedMessage == null) {
				context.put(CACHE_KEY, cacheKey);
				return true;
	
			} else {
				context.setMessage(cachedMessage);
				return false;
	
			}
		} catch (KeyException e) {
			logger.warn("skip cache lookup", e);
			return true;
			
		}
	}

	private boolean handleResponseMessage(SOAPMessageContext context) {
		String cacheKey = (String) context.get(CACHE_KEY);
		if (cacheKey != null) {
			cache.put(cacheKey, new SerializableSOAPMessage(context.getMessage()));
			
		}
		return true;
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		return true;
	}

	@Override
	public void close(MessageContext context) {
	}

	@Override
	public Set<QName> getHeaders() {
		return null;
	}

}
