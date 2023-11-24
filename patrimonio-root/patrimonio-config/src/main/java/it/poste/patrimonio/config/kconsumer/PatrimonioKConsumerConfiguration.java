package it.poste.patrimonio.config.kconsumer;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;
import it.poste.patrimonio.config.db.MongoConfiguration;


public class PatrimonioKConsumerConfiguration extends Configuration {
    

	@Valid
	@JsonProperty("mongo")
	private MongoConfiguration databaseConfiguration;
	
	@JsonProperty
    @NotNull
    private MessageHandlerConfiguration messageHandlerConfig = new MessageHandlerConfiguration();
	
	@JsonProperty
    @NotNull
    private KafkaConfiguration kafkaConfig = new KafkaConfiguration();

	public MongoConfiguration getDatabaseConfiguration() {
		return databaseConfiguration;
	}

	public MessageHandlerConfiguration getMessageHandlerConfig() {
		return messageHandlerConfig;
	}

	public KafkaConfiguration getKafkaConfig() {
		return kafkaConfig;
	}
	
	
    
}
