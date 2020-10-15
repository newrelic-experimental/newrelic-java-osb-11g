package com.bea.wli.sb.pipeline;

import java.util.logging.Level;

import com.bea.wli.sb.context.MessageContext;
import com.bea.wli.sb.transports.OutboundTransportMessageContext;
import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.Logger;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.nr.instrumentation.transports.OutboundResponseWrapper;

@Weave
public abstract class MessageProcessor {

	@Trace(dispatcher=true)
	public static void processRequest(RouterContext routerContext) {
		Logger logger = AgentBridge.getAgent().getLogger();
		MessageContext messageCtx = routerContext != null ? routerContext.getMessageContext() : null;
		try {
			if (messageCtx != null) {

				AgentBridge.publicApi.addCustomParameter("Message ID", messageCtx.getMessageId());
			}
		} catch (Exception e) {
			logger.log(Level.FINE, e, "Error Message Context Inbound and Outbound Names");
		}
		if(routerContext.token == null) {
			routerContext.token = NewRelic.getAgent().getTransaction().getToken();
		}
		Weaver.callOriginal();
	}
	
	@Trace(dispatcher=true)
	public static void processResponse(RouterContext routerContext, OutboundTransportMessageContext tmc) {
		if(routerContext.token != null) {
			routerContext.token.linkAndExpire();
			routerContext.token = null;
		}
		if(OutboundResponseWrapper.valid(tmc)) {
			OutboundResponseWrapper wrapper = new OutboundResponseWrapper(tmc);
			NewRelic.getAgent().getTracedMethod().addOutboundRequestHeaders(wrapper);
		}
		Weaver.callOriginal();
		
	}
}
