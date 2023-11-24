package it.poste.patrimonio.kconsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import it.poste.patrimonio.bl.service.PositionService;
import it.poste.patrimonio.config.kconsumer.PatrimonioKConsumerConfiguration;
import it.poste.patrimonio.db.configuration.PersistInitialiser;
import it.poste.patrimonio.kconsumer.core.KafkaMessageConsumer;
import it.poste.patrimonio.kconsumer.core.KafkaMessageConsumerManager;
import it.poste.patrimonio.kconsumer.core.MessageHandlerManager;
import it.poste.patrimonio.kconsumer.handler.LogMessageHandler;
import it.poste.patrimonio.kconsumer.handler.MessageHandler;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.javaapi.consumer.ConsumerConnector;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class PatrimonioKConsumerApplication extends Application<PatrimonioKConsumerConfiguration> {

    public static void main(final String[] args) throws Exception {
        new PatrimonioKConsumerApplication().run(args);
    }

    @Override
    public String getName() {
        return "PatrimonioKConsumer";
    }
    
    protected MessageHandler createMessageHandler() {
        return new LogMessageHandler();
    }

    @Override
    public void initialize(final Bootstrap<PatrimonioKConsumerConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final PatrimonioKConsumerConfiguration configuration,
                    final Environment environment) {
        
    	final MongoClient mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                        .build()
        );

       
    	environment.healthChecks().register("PositionMongoDBHealthCheck",
                new it.poste.patrimonio.db.model.health.DropwizardMongoDBHealthCheck(mongoClient));
    	
    	final Injector injector = Guice.createInjector(new AppModule(configuration, environment));
    	injector.getInstance(PersistInitialiser.class);
    	
        List<MessageHandler> messageHandlers= new ArrayList<>(configuration.getMessageHandlerConfig().getNumber());
        for (int i = 0; i < configuration.getMessageHandlerConfig().getNumber(); i++) {
            messageHandlers.add(createMessageHandler());
        }
        MessageHandlerManager messageHandlerManager = new MessageHandlerManager(messageHandlers);
        environment.lifecycle().manage(messageHandlerManager);
        log.info("new MessageHandlerManager with {} MessageHandlers has been created " +
                "and managed by the dropwizard environment.", messageHandlers.size());

        KafkaMessageConsumer consumer = createKafkaConsumer(configuration.getKafkaConfig(), messageHandlers, injector.getInstance(PositionService.class));
        KafkaMessageConsumerManager kafkaMessageConsumerManager = new KafkaMessageConsumerManager(consumer);
                
        environment.lifecycle().manage(kafkaMessageConsumerManager);
        log.info("new KafkaMessageConsumerManager with a KafkaMessageConsumer has been created " +
                "and managed by the dropwizard environment.");
    }
    
    private KafkaMessageConsumer createKafkaConsumer(it.poste.patrimonio.config.kconsumer.KafkaConfiguration kafkaConfiguration, List<MessageHandler> messageHandlers, PositionService positionService) {
        Properties props = new Properties();
        props.put("zookeeper.connect", kafkaConfiguration.getZookeeperConnect());
        props.put("group.id", kafkaConfiguration.getGroupId());
        props.put("zookeeper.session.timeout.ms", Integer.toString(kafkaConfiguration.getZookeeperSessionTimeout()));
        props.put("zookeeper.sync.time.ms", Integer.toString(kafkaConfiguration.getZookeeperSyncTime()));
        props.put("auto.commit.interval.ms", Integer.toString(kafkaConfiguration.getAutoCommitInterval()));
        ConsumerConnector consumerConnector = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
        log.info("new Kafka ConsumerConnector {} is created with the {}", consumerConnector, kafkaConfiguration);
        final ObjectMapper mapper = new ObjectMapper();
        
        return new KafkaMessageConsumer(consumerConnector, kafkaConfiguration, messageHandlers, positionService, mapper);
    }

}
