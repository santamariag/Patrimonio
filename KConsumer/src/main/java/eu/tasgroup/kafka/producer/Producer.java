package eu.tasgroup.kafka.producer;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import eu.tasgroup.kafka.config.KafkaConfig;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public final class Producer<TKey, TEvent> {


  private final KafkaTemplate<TKey, TEvent> kafkaTemplate;
  
  private KafkaConfig conf;

  public Producer(KafkaTemplate<TKey, TEvent> kafkaTemplate, KafkaConfig conf) {
    this.kafkaTemplate = kafkaTemplate;
    this.conf = conf;
  }

  public void sendMessage(TKey key, TEvent event){
   
	String topicName=conf.getTopicName();
	  
    ListenableFuture<SendResult<TKey, TEvent>> future = kafkaTemplate.send(topicName, key, event);

    //This will check producer result asynchronously to avoid thread blocking
    future.addCallback(new ListenableFutureCallback<SendResult<TKey, TEvent>>()   {
      @Override
      public void onFailure(Throwable throwable) {
		log.error("Unable to send message on topic \"{}\", message=[{}] due to : {}", topicName, event , throwable.getMessage(), throwable);
      }

      @Override
      public void onSuccess(SendResult<TKey, TEvent> result) {
    	  log.info("Sent message=[{}] with offset=[{}] in topic \"{}\" partition=[{}]", event , result.getRecordMetadata().offset(), result.getRecordMetadata().topic(), result.getRecordMetadata().partition());      }
    });
  }
}