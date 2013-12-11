package de.malkusch.soapClientCache.key;

import java.io.StringWriter;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.w3c.dom.Document;

import de.malkusch.soapClientCache.exception.KeyException;

/**
 * Generates a key from the SOAP body.
 * 
 * @author Markus Malkusch <markus@malkusch.de>
 */
public class SOAPBodyAdapter implements KeyAdapter {

	@Override
	public String adapt(SOAPMessageContext context) throws KeyException {
		try {
			SOAPBody body = context.getMessage().getSOAPBody();

			// TODO Hier wird firstChild auf NULL gesetzt :(
			Document doc = body.extractContentAsDocument();
			
			Source source = new DOMSource(doc);
			StringWriter stringWriter = new StringWriter();
			Result result = new StreamResult(stringWriter);
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();
			transformer.transform(source, result);
			String string = stringWriter.getBuffer().toString();
			return string;
			
		} catch (SOAPException e) {
			throw new KeyException(e);
			
		} catch (TransformerException e) {
			throw new KeyException(e);
			
		}
	}

}
