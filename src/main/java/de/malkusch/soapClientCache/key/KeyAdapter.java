package de.malkusch.soapClientCache.key;

import javax.xml.ws.handler.soap.SOAPMessageContext;

import de.malkusch.soapClientCache.cache.exception.KeyException;

/**
 * Converts T to String which can be used as a cache key
 * 
 * @author Markus Malkusch <markus@malkusch.de>
 */
public interface KeyAdapter {

	public String adapt(SOAPMessageContext context) throws KeyException;

}
