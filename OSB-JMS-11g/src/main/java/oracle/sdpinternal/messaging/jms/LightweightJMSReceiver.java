package oracle.sdpinternal.messaging.jms;

import javax.jms.Message;
import javax.jms.Queue;

import oracle.sdp.messaging.MessagingException;

import com.agent.instrumentation.oraclejms.JmsMetricUtil;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.TracedMethod;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

@Weave
public class LightweightJMSReceiver {

	protected final SharedJMSHandle mQueueConnection = Weaver.callOriginal();
	
    @Trace(dispatcher = true)
    public Message receive(final boolean b) throws MessagingException {
        Message message = Weaver.callOriginal();

        TracedMethod tracer = NewRelic.getAgent().getTracedMethod();
        
        Queue dest = mQueueConnection.mQueue;
        JmsMetricUtil.processConsume(message, dest, tracer);;

        return message;
    }

}
