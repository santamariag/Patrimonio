package eu.tasgroup.kafka.producer.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import eu.tasgroup.kafka.config.KafkaConfig;

@Configuration
public class PConfig<TKey, TEvent> {

	@Autowired
	private KafkaConfig conf;


    @Bean
    ProducerFactory<TKey, TEvent> producerFactory() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, conf.getBootstrapAddress());
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        //Set these if using SASL authentication or Confluent Cloud
        /*properties.put("security.protocol", "SASL_SSL");
    properties.put("sasl.mechanism", "PLAIN");
    properties.put("sasl.jaas.config", jaas);
    properties.put("acks", "all");*/
        return new DefaultKafkaProducerFactory<>(properties);
    }

    @Bean
    KafkaTemplate<TKey, TEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}