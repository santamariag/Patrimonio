package it.poste.patrimonio.batch;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import it.poste.patrimonio.config.batch.PatrimonioBatchConfiguration;

public class PatrimonioBatchApplication extends Application<PatrimonioBatchConfiguration> {

    public static void main(final String[] args) throws Exception {
        new PatrimonioBatchApplication().run(args);
    }

    @Override
    public String getName() {
        return "PatrimonioBatch";
    }

    @Override
    public void initialize(final Bootstrap<PatrimonioBatchConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final PatrimonioBatchConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
