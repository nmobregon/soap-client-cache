package de.malkusch.soapClientCache;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adds caching to a SOAP client.
 * 
 * @author Markus Malkusch <markus@malkusch.de>
 * @see Cache
 */
public class CacheHandler implements SOAPHandler<SOAPMessageContext> {

	final static public String CACHE_KEY = CacheHandler.class.getName() + ".cacheKey";
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	private Cache<String, SOAPMessage> cache;
	
	public CacheHandler(Cache<String, SOAPMessage> cache) {
		this.cache = cache;
	}

	public boolean handleMessage(SOAPMessageContext context) {
		Boolean outbound = (Boolean) context
				.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		
		return outbound ? handleRequestMessage(context) : handleResponseMessage(context);
	}

	private boolean handleRequestMessage(SOAPMessageContext context) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			context.getMessage().writeTo(out);
			String cacheKey = new String(out.toByteArray(), "UTF-8");
			
			SOAPMessage cachedMessage = cache.get(cacheKey);
			if (cachedMessage == null) {
				context.put(CACHE_KEY, cacheKey);
				return true;
	
			} else {
				context.setMessage(cachedMessage);
				return false;
	
			}
		} catch (SOAPException e) {
			logger.warn("skip cache lookup", e);
			return true;
			
		} catch (IOException e) {
			logger.warn("skip cache lookup", e);
			return true;
			
		}
	}

	private boolean handleResponseMessage(SOAPMessageContext context) {
		String cacheKey = (String) context.get(CACHE_KEY);
		if (cacheKey != null) {
			cache.put(cacheKey, context.getMessage());
			
		}
		return true;
	}

	public boolean handleFault(SOAPMessageContext context) {
		return true;
	}

	public void close(MessageContext context) {
	}

	public Set<QName> getHeaders() {
		return null;
	}

}
