package it.poste.patrimonio.kproducer;

import java.util.Collections;



import io.dropwizard.Application;
import io.dropwizard.kafka.KafkaProducerBundle;
import io.dropwizard.kafka.KafkaProducerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import it.poste.patrimonio.config.kproducer.PatrimonioKProducerConfiguration;
import it.poste.patrimonio.kproducer.core.KafkaMessageProducer;
import it.poste.patrimonio.kproducer.resources.PositionResource;

public class PatrimonioKProducerApplication extends Application<PatrimonioKProducerConfiguration> {

    public static void main(final String[] args) throws Exception {
        new PatrimonioKProducerApplication().run(args);
    }

    @Override
    public String getName() {
        return "PatrimonioKProducer";
    }
    
    private final KafkaProducerBundle<String, Object, PatrimonioKProducerConfiguration> kafkaProducer = new KafkaProducerBundle<String, Object, PatrimonioKProducerConfiguration>(Collections.emptyList()) {
        @Override
        public KafkaProducerFactory<String, Object> getKafkaProducerFactory(PatrimonioKProducerConfiguration configuration) {
            return configuration.getKafkaProducerFactory();
        }
    };

    @Override
    public void initialize(final Bootstrap<PatrimonioKProducerConfiguration> bootstrap) {
    	bootstrap.addBundle(kafkaProducer);
    }

    @Override
    public void run(final PatrimonioKProducerConfiguration configuration,
                    final Environment environment) {
    	
        
    	final KafkaMessageProducer kafkaMessageProducer = new KafkaMessageProducer(kafkaProducer.getProducer(), configuration.getKafkaTopicFactory().getName());
        
    	 environment.jersey().register(new PositionResource(kafkaMessageProducer));
    }

}
