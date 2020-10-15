package com.nr.instrumentation.transports;

import java.util.logging.Level;

import com.bea.wli.sb.transports.OutboundTransportMessageContext;
import com.bea.wli.sb.transports.ResponseHeaders;
import com.bea.wli.sb.transports.ResponseMetaData;
import com.bea.wli.sb.transports.TransportException;
import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.HeaderType;
import com.newrelic.api.agent.OutboundHeaders;

public class OutboundResponseWrapper implements OutboundHeaders {
	private OutboundTransportMessageContext outboundCtx;
	
	public OutboundResponseWrapper(OutboundTransportMessageContext ctx) {
		outboundCtx = ctx;
	}

	@Override
	public HeaderType getHeaderType() {
		return HeaderType.HTTP;
	}

	@Override
	public void setHeader(String name, String value) {
		try {
			if (outboundCtx != null) {
				ResponseMetaData<?> metadata = outboundCtx.getResponseMetaData();
				if (metadata != null) {
					ResponseHeaders headers = metadata.getHeaders();
					if (headers != null) {
						headers.setHeader(name, value);
					}
				}
			}
		} catch (TransportException e) {
			AgentBridge.getAgent().getLogger().log(Level.FINER, e, "Error setting headers", new Object[0]);
		}
	}

	public static boolean valid(OutboundTransportMessageContext outboundCtx) {
		if(outboundCtx == null) return false;
		ResponseMetaData<?> metadata = null;
		try {
			metadata = outboundCtx.getResponseMetaData();
		} catch (TransportException e) {
		}
		if(metadata == null) return false;
		if(metadata.getHeaders() == null) return false;
		return true;
	}
}
