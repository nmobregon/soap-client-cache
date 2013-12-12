package de.malkusch.soapClientCache;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Iterator;

import javax.activation.DataHandler;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

public class SerializableSOAPMessage extends SOAPMessage implements
		Serializable {

	private static final long serialVersionUID = -1514939541423262619L;

	private SOAPMessage message;

	public SerializableSOAPMessage(SOAPMessage message) {
		this.message = message;
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		try {
			message.writeTo(out);
			
		} catch (SOAPException e) {
			throw new IOException(e);
			
		}
	}

	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		try {
			message = MessageFactory.newInstance().createMessage(null, in);
			
		} catch (SOAPException e) {
			throw new IOException(e);
			
		}
	}
	
	@Override
	public SOAPBody getSOAPBody() throws SOAPException {
		return message.getSOAPBody();
	}
	
	@Override
	public SOAPHeader getSOAPHeader() throws SOAPException {
		return message.getSOAPHeader();
	}
	
	@Override
	public AttachmentPart createAttachmentPart(DataHandler dataHandler) {
		return message.createAttachmentPart(dataHandler);
	}
	
	@Override
	public void setProperty(String property, Object value) throws SOAPException {
		message.setProperty(property, value);
	}
	
	@Override
	public Object getProperty(String property) throws SOAPException {
		return message.getProperty(property);
	}
	
	@Override
	public AttachmentPart createAttachmentPart(Object content,
			String contentType) {
		return message.createAttachmentPart(content, contentType);
	}

	@Override
	public void addAttachmentPart(AttachmentPart AttachmentPart) {
		message.addAttachmentPart(AttachmentPart);
	}

	@Override
	public int countAttachments() {
		return message.countAttachments();
	}

	@Override
	public AttachmentPart createAttachmentPart() {
		return message.createAttachmentPart();
	}

	@Override
	public AttachmentPart getAttachment(SOAPElement element)
			throws SOAPException {
		return message.getAttachment(element);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Iterator getAttachments() {
		return message.getAttachments();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Iterator getAttachments(MimeHeaders headers) {
		return message.getAttachments(headers);
	}

	@Override
	public String getContentDescription() {
		return message.getContentDescription();
	}

	@Override
	public MimeHeaders getMimeHeaders() {
		return message.getMimeHeaders();
	}

	@Override
	public SOAPPart getSOAPPart() {
		return message.getSOAPPart();
	}

	@Override
	public void removeAllAttachments() {
		message.removeAllAttachments();
	}

	@Override
	public void removeAttachments(MimeHeaders headers) {
		message.removeAttachments(headers);
	}

	@Override
	public void saveChanges() throws SOAPException {
		message.saveChanges();
	}

	@Override
	public boolean saveRequired() {
		return message.saveRequired();
	}

	@Override
	public void setContentDescription(String description) {
		message.setContentDescription(description);
	}

	@Override
	public void writeTo(OutputStream out) throws SOAPException, IOException {
		message.writeTo(out);
	}

}
