package it.poste.patrimonio.kproducer.core;


import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import it.poste.patrimonio.itf.api.PositionApi;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class KafkaMessageProducer {
	
	private Producer<String, Object> producer;
	
	private String topicName;

	
	public KafkaMessageProducer(Producer<String, Object> producer, String topicName) {
		this.producer = producer;
		this.topicName = topicName;
	}
	
	
	
	public void send(PositionApi position) {
		
		producer.send(new ProducerRecord<String, Object>(topicName, position.getNdg(), position), (recordMetadata, e) -> { 
           
            if (e == null) { 
                log.info("Message produced...received new metadata." + 
                        " Topic: " + recordMetadata.topic() + 
                        " Partition: " + recordMetadata.partition() + 
                        " Offset: " + recordMetadata.offset()); 
            } else { 
                log.error("Error while producing ", e); 
            } 
        }); 
		
	}

}
