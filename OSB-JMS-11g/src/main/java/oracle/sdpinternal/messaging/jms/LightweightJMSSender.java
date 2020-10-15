package oracle.sdpinternal.messaging.jms;

import java.util.logging.Level;

import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Topic;

import com.agent.instrumentation.oraclejms.JmsMetricUtil;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.TracedMethod;
import com.newrelic.api.agent.weaver.Weaver;

import oracle.sdpinternal.messaging.TransportException;

public class LightweightJMSSender {

    @Trace
    public void send(final Message message, final int n, final int n2, final long n3) throws TransportException {
        try {
            Destination dest = message.getJMSDestination();
            
            if (dest != null && (dest instanceof Queue || dest instanceof Topic)) {
            	TracedMethod tracer = NewRelic.getAgent().getTracedMethod();
            	JmsMetricUtil.processSendMessage(message, dest, tracer);
            } else {
                NewRelic.getAgent().getLogger().log(Level.FINE,
                        "Error processing JMS Message: Invalid Message Type: {0}", message.getClass().getName());
            }
        } catch (Exception e) {
            //
        }
        Weaver.callOriginal();
    }

}
