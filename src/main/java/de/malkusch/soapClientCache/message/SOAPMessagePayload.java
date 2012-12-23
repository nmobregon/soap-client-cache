package de.malkusch.soapClientCache.message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.WriteAbortedException;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import de.malkusch.soapClientCache.cache.Payload;

/**
 * Serializable Payload<SOAPMessage>
 * 
 * Serialization is accomplished by storing the soap message as XML. The replaced object is
 * capable of deserializing the XML to SOAPMessage.
 * 
 * @author Markus Malkusch <markus@malkusch.de>
 */
public class SOAPMessagePayload extends Payload<SOAPMessage> {

	private static final long serialVersionUID = 6943363486488696747L;

	public SOAPMessagePayload(SOAPMessage payload) {
		super(payload);
	}
	
	private Object writeReplace() throws ObjectStreamException {
		try {
			ByteArrayOutputStream xmlBytes = new ByteArrayOutputStream();
			getPayload().writeTo(xmlBytes);
			String xml = new String(xmlBytes.toByteArray(), "UTF-8");
		
			SerializablePayload stringPayload = new SerializablePayload(xml);
			stringPayload.setExpiration(getExpiration());
			
			return stringPayload;
			
		} catch (SOAPException e) {
			throw new WriteAbortedException(e.getMessage(), e);
			
		} catch (IOException e) {
			throw new WriteAbortedException(e.getMessage(), e);
			
		}
	}

}
