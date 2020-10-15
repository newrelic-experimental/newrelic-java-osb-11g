package com.bea.wli.sb.pipeline;

import com.bea.wli.sb.context.MessageContext;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;

@Weave
public abstract class RouterContext {

	@NewField
	public Token token;
	
	public abstract MessageContext getMessageContext();
	
}
