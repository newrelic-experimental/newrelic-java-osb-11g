package com.bea.wli.sb.transports;

import java.util.logging.Level;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.nr.instrumentation.transports.InboundResponseWrapper;

@SuppressWarnings({ "rawtypes", "deprecation" })
@Weave(type=MatchType.Interface)
public abstract class TransportSendListener {

	@NewField
	public Token token = null;
	
	@Trace(dispatcher=true)
	public void onReceiveResponse(OutboundTransportMessageContext outboundTransportMessageContext) {
		try {
			if(outboundTransportMessageContext != null) {
				ResponseMetaData responseMetadata = outboundTransportMessageContext.getResponseMetaData();
				if(responseMetadata != null) {
					ResponseHeaders headers = responseMetadata.getHeaders();
					if (headers != null) {
						AgentBridge.getAgent().getTransaction().provideHeaders(new InboundResponseWrapper(headers));
					}
				}
			}
		} catch (TransportException e) {
			NewRelic.getAgent().getLogger().log(Level.FINE, e, "Error getting inbound headers", new Object[] {});
		}
		Weaver.callOriginal();
		if(token != null) {
			token.linkAndExpire();
			token = null;
		}
	}
	
	@Trace(dispatcher=true)
	public void onError(OutboundTransportMessageContext outboundTransportMessageContext, String paramString1, String paramString2) {
		try {
			if(outboundTransportMessageContext != null) {
				ResponseMetaData responseMetadata = outboundTransportMessageContext.getResponseMetaData();
				if(responseMetadata != null) {
					ResponseHeaders headers = responseMetadata.getHeaders();
					if (headers != null) {
						AgentBridge.getAgent().getTransaction().provideHeaders(new InboundResponseWrapper(headers));
					}
				}
			}
		} catch (TransportException e) {
			NewRelic.getAgent().getLogger().log(Level.FINE, e, "Error getting inbound headers", new Object[] {});
		}
		Weaver.callOriginal();
		if(token != null) {
			token.linkAndExpire();
			token = null;
		}
	}

}
