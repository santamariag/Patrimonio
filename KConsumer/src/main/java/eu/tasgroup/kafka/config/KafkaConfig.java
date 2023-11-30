package eu.tasgroup.kafka.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "kafka")
@Data
public class KafkaConfig {
	
	private String bootstrapAddress;
	private String groupId;
	private String jaas;
	private String topicName;

}
