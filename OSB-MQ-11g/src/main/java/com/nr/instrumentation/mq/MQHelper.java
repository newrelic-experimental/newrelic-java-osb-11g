package com.nr.instrumentation.mq;

import java.util.logging.Level;

import com.bea.wli.sb.transports.mq.MQOutboundMessageContext;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.TracedMethod;

public class MQHelper {

    public static void processSendMessage(MQOutboundMessageContext ctx, TracedMethod tracer) {
        if (tracer == null) {
            NewRelic.getAgent().getLogger().log(Level.FINER, "processSendMessage(): no tracer");
        } else {
            	NewRelic.getAgent().getTracedMethod().addOutboundRequestHeaders(new OutboundWrapper(ctx));
        }
    }

}
