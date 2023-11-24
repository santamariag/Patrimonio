package it.poste.patrimonio.kconsumer.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

import it.poste.patrimonio.bl.service.PositionService;
import it.poste.patrimonio.config.kconsumer.KafkaConfiguration;
import it.poste.patrimonio.itf.api.PositionApi;
import it.poste.patrimonio.kconsumer.handler.MessageHandler;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KafkaMessageConsumer {

    private ConsumerConnector consumerConnector;
    private KafkaConfiguration kafkaConfiguration;
    private ExecutorService executor;
    private List<MessageHandler> messageHandlers;
  
    private PositionService positionService;
    
    private ObjectMapper mapper;

    @Inject
    public KafkaMessageConsumer(ConsumerConnector consumerConnector, KafkaConfiguration kafkaConfiguration, List<MessageHandler> messageHandlers, PositionService positionService, ObjectMapper mapper) {
        this.consumerConnector = consumerConnector;
        this.kafkaConfiguration = kafkaConfiguration;
        this.messageHandlers = messageHandlers;
        this.positionService = positionService;
        this.mapper = mapper;
        log.info("new Kafka ConsumerConnector {} is created with the {}", consumerConnector, kafkaConfiguration);
    }

    public void start() {
        int threadCount = messageHandlers.size();
        String topic = kafkaConfiguration.getTopic();
        Map<String, Integer> topicCount = new HashMap<>();
        topicCount.put(topic, new Integer(threadCount));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerStreams = consumerConnector.createMessageStreams(topicCount);
        List<KafkaStream<byte[], byte[]>> streams = consumerStreams.get(topic);
        executor = Executors.newFixedThreadPool(threadCount);
        int threadNumber = 0;
        for (final KafkaStream<byte[], byte[]> stream : streams) {
            executor.submit(new Worker(stream, messageHandlers.get(threadNumber)));
            threadNumber++;
          log.info("New thread has been created for the consumerConnector " + consumerConnector + " to the topic " + topic);
        }
        log.info("New consumerConnector " + consumerConnector + " for topic " + topic + " is running.");
    }

    public void stop() {
        if (consumerConnector != null) {
            consumerConnector.shutdown();
            log.info("ConsumerConnector {} has been shut down", consumerConnector );
        }
        if (executor != null) {
            executor.shutdown();
        }
    }

    class Worker implements Runnable {

        private KafkaStream<byte[], byte[]> stream;
        private int threadNumber;
        private MessageHandler messageHandler;

        Worker(KafkaStream<byte[], byte[]> stream, int threadNumber, MessageHandler messageHandler) {
            this.threadNumber = threadNumber;
            this.stream = stream;
            this.messageHandler = messageHandler;
        }

        Worker(KafkaStream<byte[], byte[]> stream, MessageHandler messageHandler) {
            this.stream = stream;
            this.messageHandler = messageHandler;
        }

        @Override
        public void run() {
            ConsumerIterator<byte[], byte[]> consumerIte = stream.iterator();

            while (consumerIte.hasNext()) {
            	MessageAndMetadata<byte[], byte[]> message=consumerIte.next();
            	byte[] key = message.key();
            	byte[] msg = message.message();
            	long offset=message.offset();
            	int partition=message.partition();
            	String strKey=new String(key);
            	String jsonMsg=new String(msg);
            	log.debug("Thread number "+threadNumber+" consume message: key ["+strKey+"] value [" + jsonMsg+"] partition ["+partition+"] offset ["+offset+"]");

            	Object obj=null;
            	if((obj=isA(jsonMsg, PositionApi.class)) instanceof PositionApi) {
            		PositionApi position = PositionApi.class.cast(obj);
            		positionService.saveOrUpdate(position);
            		
            	} 
            	
            	  else if((obj=isA(jsonMsg, Boolean.class)) instanceof Boolean) {
            		log.warn("message deserialization is not manageable "+obj);
            	} else {
            		log.warn("cannot deserialize message "+obj);
            	}
            	


            	messageHandler.dispose(msg);
            }
        }
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Object isA(String json, Class expected) {

    	try {
    		return mapper.readValue(json, expected);
    	} catch (JsonProcessingException e) {
    		log.error(e.getMessage());
    		return Boolean.FALSE;
    	}
    }
}