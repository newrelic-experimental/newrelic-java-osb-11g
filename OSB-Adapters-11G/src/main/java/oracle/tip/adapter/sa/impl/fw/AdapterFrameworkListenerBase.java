package oracle.tip.adapter.sa.impl.fw;


import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

@Weave(type=MatchType.BaseClass)
public abstract class AdapterFrameworkListenerBase {
	public abstract String getServiceName();

	public abstract String getOperationName();

	public abstract String getAdapterName();

	@Trace(dispatcher=true)
	public Record onMessage(Record record) {
		String adapterName = getAdapterName();
		if(adapterName == null) {
			adapterName = "Unknown_Adapter";
		}
		NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","OSB","Adapter",adapterName,"onMessage",getServiceName(),getOperationName()});
		return Weaver.callOriginal();
	}
	
	@Weave
	public static class Record {
		
	}
}
