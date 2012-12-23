package de.malkusch.soapClientCache.key;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.soap.SOAPException;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import de.malkusch.soapClientCache.exception.KeyException;

/**
 * Generates a key from the complete SOAP message (including headers).
 * 
 * @author Markus Malkusch <markus@malkusch.de>
 */
public class SoapMessageAdapter implements KeyAdapter {

	@Override
	public String adapt(SOAPMessageContext context) throws KeyException {
		
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			context.getMessage().writeTo(out);
			return new String(out.toByteArray(), "UTF-8");
			
		} catch (SOAPException e) {
			throw new KeyException(e);
			
		} catch (IOException e) {
			throw new KeyException(e);
			
		}
	}

}
