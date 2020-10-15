package com.agent.instrumentation.oraclejms;

import java.util.logging.Level;

import javax.jms.JMSException;
import javax.jms.Message;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.HeaderType;
import com.newrelic.api.agent.OutboundHeaders;

public class OutboundWrapper implements OutboundHeaders {
	
	private Message message;
	
	public OutboundWrapper(Message msg) {
		message = msg;
	}

	@Override
	public HeaderType getHeaderType() {
		return HeaderType.MESSAGE;
	}

	@Override
	public void setHeader(String name, String value) {
		try {
			message.setStringProperty(name, value);
		} catch (JMSException e) {
			AgentBridge.getAgent().getLogger().log(Level.FINER, e, "Error setting string property {0} to {1} on JMS message", new Object[] {name,value});
		}
	}

}
