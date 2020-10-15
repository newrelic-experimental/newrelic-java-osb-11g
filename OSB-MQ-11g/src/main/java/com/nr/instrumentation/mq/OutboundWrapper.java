package com.nr.instrumentation.mq;

import java.util.logging.Level;

import com.bea.wli.sb.transports.mq.MQOutboundMessageContext;
import com.newrelic.api.agent.HeaderType;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.OutboundHeaders;

public class OutboundWrapper implements OutboundHeaders {

	private final MQOutboundMessageContext ctx;

	public OutboundWrapper(MQOutboundMessageContext ctx) {
		this.ctx = ctx;
	}

	@Override
	public void setHeader(String name, String value) {
		try {
			ctx.getResponseMetaData().getHeaders().setHeader(name, value);
		} catch (Exception e) {
			NewRelic.getAgent().getLogger().log(Level.FINE, e, "Error setting property ({0}) on MQ message.", name);
		}
	}

	@Override
	public HeaderType getHeaderType() {
		return HeaderType.MESSAGE;
	}
}