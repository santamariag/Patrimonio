package eu.tasgroup.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import eu.tasgroup.kafka.dto.PositionDTO;
import eu.tasgroup.kafka.service.IPositionService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public final class ConsumerService {

  private final IPositionService positionService;

  public ConsumerService(IPositionService positionService) {
    this.positionService = positionService;
  }

  //@KafkaListener(topics = "${kafka.topicName}", containerFactory = "kafkaListenerContainerFactory")
  public void consume(ConsumerRecord<String, Object> cr) {

	  log.info("----- Received message: {}", cr.toString());
	  
	  if (cr.value() instanceof PositionDTO) {
		  PositionDTO position = PositionDTO.class.cast(cr.value());
		  positionService.addPosition(position);
		  log.info("Fake Inserted in Mongo "+ position);
	  }
	  else {
		  
		  log.error("----- Message UNKNOWN!!!: {}", cr.toString());
	  }

  }
}