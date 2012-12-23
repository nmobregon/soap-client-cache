package de.malkusch.soapClientCache.message;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamException;
import java.io.UnsupportedEncodingException;
import java.io.WriteAbortedException;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import de.malkusch.soapClientCache.cache.Payload;

/**
 * Helper class to deserialize to a SOAPMessagePayload
 * 
 * @author Markus Malkusch <markus@malkusch.de>
 */
public class SerializablePayload extends Payload<String> {

	private static final long serialVersionUID = -6805382434727948222L;

	public SerializablePayload(String payload) {
		super(payload);
	}

	private Object readResolve() throws ObjectStreamException {
		try {
			InputStream stream = new ByteArrayInputStream(getPayload()
					.getBytes("UTF-8"));
			SOAPMessage message = MessageFactory.newInstance().createMessage(
					null, stream);

			SOAPMessagePayload payload = new SOAPMessagePayload(message);
			payload.setExpiration(this.getExpiration());

			return payload;

		} catch (UnsupportedEncodingException e) {
			throw new WriteAbortedException(e.getMessage(), e);

		} catch (IOException e) {
			throw new WriteAbortedException(e.getMessage(), e);

		} catch (SOAPException e) {
			throw new WriteAbortedException(e.getMessage(), e);

		}
	}

}
