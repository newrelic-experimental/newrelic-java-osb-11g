package com.agent.instrumentation.oraclejms;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.TemporaryQueue;
import javax.jms.TemporaryTopic;
import javax.jms.Topic;

import com.newrelic.api.agent.DestinationType;
import com.newrelic.api.agent.InboundHeaders;
import com.newrelic.api.agent.MessageConsumeParameters;
import com.newrelic.api.agent.MessageProduceParameters;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.OutboundHeaders;
import com.newrelic.api.agent.TracedMethod;
import com.newrelic.api.agent.TransactionNamePriority;

public abstract class JmsMetricUtil {

    private static final String CATEGORY = "Message";

    public static Message nameTransaction(Message msg) {
        if (msg != null) {
            try {
                Destination dest = msg.getJMSDestination();
                if (dest instanceof Queue) {
                    Queue queue = (Queue) dest;
                    if (queue instanceof TemporaryQueue) {
                        NewRelic.getAgent().getTransaction().setTransactionName(TransactionNamePriority.FRAMEWORK_LOW,
                                false, CATEGORY, "JMS/Queue/Temp");
                    } else {
                        NewRelic.getAgent().getTransaction().setTransactionName(TransactionNamePriority.FRAMEWORK_HIGH,
                                false, CATEGORY, "JMS/Queue/Named", queue.getQueueName());
                    }
                } else if (dest instanceof Topic) {
                    Topic topic = (Topic) dest;
                    if (topic instanceof TemporaryTopic) {
                        NewRelic.getAgent().getTransaction().setTransactionName(TransactionNamePriority.FRAMEWORK_LOW,
                                false, CATEGORY, "JMS/Topic/Temp");
                    } else {
                        NewRelic.getAgent().getTransaction().setTransactionName(TransactionNamePriority.FRAMEWORK_HIGH,
                                false, CATEGORY, "JMS/Topic/Named", topic.getTopicName());
                    }
                } else {
                    NewRelic.getAgent().getLogger().log(Level.FINE,
                            "Error naming JMS transaction: Invalid Message Type.");
                }
            } catch (JMSException e) {
                NewRelic.getAgent().getLogger().log(Level.FINE, e, "Error naming JMS transaction");
            }
        } else {
            // Not a useful transaction.
            NewRelic.getAgent().getTransaction().ignore();
        }
        return msg;
    }

    public static void saveMessageParameters(Message msg) {
        if (msg != null) {
        	Map<String, String> msgParams = getMessageParameters(msg);
        	for(String key : msgParams.keySet()) {
        		String value = msgParams.get(key);
        		NewRelic.addCustomParameter(key, value);
        	}
        }
    }

    /**
     * Get the message properties as a map of String to Object
     * 
     * @param msg the message holding 0 or more properties
     * @return the map, may be empty, never null
     */
    public static Map<String, String> getMessageParameters(Message msg) {

        Map<String, String> result = new LinkedHashMap<String, String>(1);

        try {
            Enumeration<?> parameterEnum = msg.getPropertyNames();
            if (parameterEnum == null || !parameterEnum.hasMoreElements()) {
                return Collections.emptyMap();
            }

            while (parameterEnum.hasMoreElements()) {
                String key = (String) parameterEnum.nextElement();
                Object val = msg.getObjectProperty(key);
                result.put(key, ((val == null) ? null : val.toString()));
            }
        } catch (JMSException e) {
            NewRelic.getAgent().getLogger().log(Level.FINE, e, "Unable to capture JMS message property");
        }

        return result;
    }

    public static void processSendMessage(Message message, Destination dest, TracedMethod tracer) {
        if (message == null) {
            NewRelic.getAgent().getLogger().log(Level.FINER, "JMS processSendMessage(): message is null");
            return;
        }

        try {
        	
            DestinationType destinationType = getDestinationType(message.getJMSDestination());
            String destinationName = getDestinationName(message.getJMSDestination());
            OutboundHeaders wrapper = new OutboundWrapper(message);
            MessageProduceParameters params = MessageProduceParameters.library("OSB-JMS").destinationType(destinationType).destinationName(destinationName).outboundHeaders(wrapper).build();
            tracer.reportAsExternal(params);
        } catch (JMSException exception) {
            NewRelic.getAgent().getLogger().log(Level.FINE, exception,
                    "Unable to record metrics for JMS message produce.");
        }
    }

    public static void processConsume(Message message, Destination dest, TracedMethod tracer) {
        if (message == null) {
            NewRelic.getAgent().getLogger().log(Level.FINER, "JMS processConsume: message is null");
            return;
        }

        try {
            DestinationType  destinationType = getDestinationType(dest);
            String destinationName = getDestinationName(dest == null ? message.getJMSDestination() : dest);
        	InboundHeaders inboundHeaders = new InboundWrapper(message);
			MessageConsumeParameters params = MessageConsumeParameters.library("OSB-JMS").destinationType(destinationType).destinationName(destinationName).inboundHeaders(inboundHeaders).build();
            tracer.reportAsExternal(params);
        } catch (JMSException exception) {
            NewRelic.getAgent().getLogger().log(Level.FINE, exception,
                    "Unable to record metrics for JMS message consume.");
        }
    }

    private static String getDestinationName(Destination destination) throws JMSException {

        if (destination instanceof TemporaryQueue || destination instanceof TemporaryTopic) {
            return "Temp";
        }

        if (destination instanceof Queue) {
            Queue queue = (Queue) destination;
            return queue.getQueueName();
        }

        if (destination instanceof Topic) {
            Topic topic = (Topic) destination;
            topic.getTopicName();
        }

        return "Unknown";
    }

    private static DestinationType getDestinationType(Destination destination) {
        if (destination instanceof TemporaryQueue) {
            return DestinationType.TEMP_QUEUE;
        } else if (destination instanceof TemporaryTopic) {
            return DestinationType.TEMP_TOPIC;
        } else if (destination instanceof Queue) {
            return DestinationType.NAMED_QUEUE;
        } else {
            return DestinationType.NAMED_TOPIC;
        }
    }

}