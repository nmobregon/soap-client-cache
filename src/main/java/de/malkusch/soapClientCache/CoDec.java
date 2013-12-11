package de.malkusch.soapClientCache;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

public class CoDec {

	public void write(SOAPMessage message, OutputStream stream) throws SOAPException, IOException {
		message.writeTo(stream);
	}
	
	public SOAPMessage read(InputStream stream) throws IOException, SOAPException {
		return MessageFactory.newInstance().createMessage(null, stream);
	}
	
}
