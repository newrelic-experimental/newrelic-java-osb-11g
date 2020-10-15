package com.nr.instrumentation.osb.http;

import java.net.URI;

import com.bea.wli.sb.transports.TransportSendListener;
import com.newrelic.api.agent.HttpParameters;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

@Weave(originalName="com.bea.wli.sb.transports.http.HttpOutboundMessageContext",type=MatchType.BaseClass)
public abstract class HttpOutboundMessageContext_instrumentation {
	
	public abstract URI getURI();

	@Trace(leaf=true)
	public void send(TransportSendListener listener) {
		HttpParameters params = HttpParameters.library("OSB-Http").uri(getURI()).procedure("send").noInboundHeaders().build();
		NewRelic.getAgent().getTracedMethod().reportAsExternal(params);
		Weaver.callOriginal();
	}
}
