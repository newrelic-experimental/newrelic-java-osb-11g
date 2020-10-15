package com.bea.wli.sb.transports;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

@Weave(type=MatchType.BaseClass)
public class CoLocatedMessageContext {

	@Trace
	public void send(TransportSendListener listener) {
		if(listener.token == null) {
			listener.token = NewRelic.getAgent().getTransaction().getToken();
		}
		Weaver.callOriginal();
	}
	
	@Trace
	public void close(TransportOptions options) {
		Weaver.callOriginal();
	}
}
