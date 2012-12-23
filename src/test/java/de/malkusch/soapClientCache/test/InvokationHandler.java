package de.malkusch.soapClientCache.test;

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class InvokationHandler implements SOAPHandler<SOAPMessageContext> {
	
	private boolean outbound;
	private boolean inbound;
	
	public void reset() {
		outbound = false;
		inbound = false;
	}
	
	public boolean isOutbound() {
		return outbound;
	}
	
	public boolean isInbound() {
		return inbound;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		boolean outbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (outbound) {
			this.outbound = true;
			
		} else {
			inbound = true;
			
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
