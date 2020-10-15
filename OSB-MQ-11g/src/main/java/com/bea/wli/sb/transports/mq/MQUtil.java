package com.bea.wli.sb.transports.mq;

import com.bea.wli.config.Ref;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

@Weave
public abstract class MQUtil {
	
	@Trace
	public static void sendMessage(Ref ref, MQMessage message, MQQueue queue, int options)  {
		String queueName = queue.name;
		NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","MQUtil","sendMessage",queueName});
		Weaver.callOriginal();
	}

	@Trace
	public static void sendMessage(MQMessage message, MQQueueManager qManager, String qName, int putOptions, String remoteQM) {
		NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","MQUtil","sendMessage",qName});
		Weaver.callOriginal();
	}

}
