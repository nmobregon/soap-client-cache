package de.malkusch.soapClientCache.message;

import javax.xml.soap.SOAPMessage;

import de.malkusch.soapClientCache.cache.PayloadFactory;

public class SOAPMessagePayloadFactory extends PayloadFactory<SOAPMessage> {

	@Override
	public SOAPMessagePayload getInstance(SOAPMessage object) {
		return new SOAPMessagePayload(object);
	}
	
}
