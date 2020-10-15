package com.agent.instrumentation.oraclejms;

import java.util.logging.Level;

import javax.jms.JMSException;
import javax.jms.Message;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.HeaderType;
import com.newrelic.api.agent.InboundHeaders;

public class InboundWrapper implements InboundHeaders {
	
	private Message message;
	
	public InboundWrapper(Message msg) {
		message = msg;
	}

	@Override
	public HeaderType getHeaderType() {
		return HeaderType.MESSAGE;
	}

	@Override
	public String getHeader(String name) {
		try {
			return message.getStringProperty(name);
		} catch (JMSException e) {
			AgentBridge.getAgent().getLogger().log(Level.FINER, e, "Error getting string property {0} from JMS message", new Object[] {name});
			return null;
		}
	}

}
