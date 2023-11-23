package it.poste.patrimonio.kconsumer;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import it.poste.patrimonio.config.kconsumer.PatrimonioKConsumerConfiguration;

public class PatrimonioKConsumerApplication extends Application<PatrimonioKConsumerConfiguration> {

    public static void main(final String[] args) throws Exception {
        new PatrimonioKConsumerApplication().run(args);
    }

    @Override
    public String getName() {
        return "PatrimonioKConsumer";
    }

    @Override
    public void initialize(final Bootstrap<PatrimonioKConsumerConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final PatrimonioKConsumerConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
