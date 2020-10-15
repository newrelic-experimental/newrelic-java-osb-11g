package com.bea.wli.sb.transports;

import java.net.URI;

import com.bea.wli.config.Ref;
import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.TransactionNamePriority;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.nr.instrumentation.transports.InboundWrapper;

@Weave(type = MatchType.Interface)
public abstract class TransportManager {
	
	@SuppressWarnings("deprecation")
	@Trace(dispatcher = true)
	public void receiveMessage(InboundTransportMessageContext inboundCtx, TransportOptions transportOptions) throws TransportException {
		if (inboundCtx != null) {
			URI uri = inboundCtx.getURI();
			TransportEndPoint endpoint = inboundCtx.getEndPoint();
			if (uri != null && endpoint != null) {
				Ref serviceRef = endpoint.getServiceRef();
				if (serviceRef != null) {
					String serviceName = serviceRef.getFullName();
					if (serviceName != null && !serviceName.isEmpty()) {
						// Name the traced method something like Custom/OSB/MQInboundMessageContext/receiveMessage
						NewRelic.getAgent().getTracedMethod().setMetricName("Custom", "OSB", serviceName, "receiveMessage");
					}
					NewRelic.getAgent().getTransaction().setTransactionName(TransactionNamePriority.FRAMEWORK_LOW, true, "OSB",
							new String[] { "Custom", "TransportManager", inboundCtx.getClass().getSimpleName(), uri.getPath() });
				}
			}
			// Capture incoming headers for CAT purposes
			RequestHeaders requestHeaders = inboundCtx.getRequestMetaData().getHeaders();
			AgentBridge.getAgent().getTransaction().provideHeaders(new InboundWrapper(requestHeaders));
		}
		Weaver.callOriginal();
	}
}
