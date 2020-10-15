package oracle.tip.adapter.fw.jca.messageinflow;

import javax.resource.cci.Record;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

@Weave
public abstract class MessageEndpointImpl {

	@Trace(dispatcher=true)
	public Record onMessage(Record message) {
		AgentBridge.publicApi.addCustomParameter("Record Name", message.getRecordName());
		return Weaver.callOriginal();
	}
}
