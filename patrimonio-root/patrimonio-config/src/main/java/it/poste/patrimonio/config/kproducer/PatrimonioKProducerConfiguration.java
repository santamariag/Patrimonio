package it.poste.patrimonio.config.kproducer;

import io.dropwizard.Configuration;
import io.dropwizard.kafka.KafkaProducerFactory;
import io.dropwizard.kafka.KafkaTopicFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.*;

public class PatrimonioKProducerConfiguration extends Configuration {
    
	@Valid
	@NotNull
	@JsonProperty("producer")
	private KafkaProducerFactory<String, Object> kafkaProducerFactory;
	

	@Valid
	@NotNull
	@JsonProperty("topic")
	private KafkaTopicFactory kafkaTopicFactory;

	public KafkaProducerFactory<String, Object> getKafkaProducerFactory() {
		return kafkaProducerFactory;
	}

	public KafkaTopicFactory getKafkaTopicFactory() {
		return kafkaTopicFactory;
	}
	
}
