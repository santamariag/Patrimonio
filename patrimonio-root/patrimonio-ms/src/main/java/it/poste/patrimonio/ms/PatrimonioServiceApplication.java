package it.poste.patrimonio.ms;

import org.apache.cxf.ext.logging.LoggingInInterceptor;
import org.apache.cxf.ext.logging.LoggingOutInterceptor;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.roskart.dropwizard.jaxws.EndpointBuilder;
import com.roskart.dropwizard.jaxws.JAXWSBundle;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import it.poste.patrimonio.config.ms.PatrimonioServiceConfiguration;
import it.poste.patrimonio.db.configuration.PersistInitialiser;
import it.poste.patrimonio.ms.resources.PositionResource;
import it.poste.patrimonio.ms.resources.ws.PositionSoapService;

public class PatrimonioServiceApplication extends Application<PatrimonioServiceConfiguration> {

	private JAXWSBundle<Object> jaxWsBundle = new JAXWSBundle<>();
	
    public static void main(final String[] args) throws Exception {
        new PatrimonioServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "PatrimonioService";
    }

    @Override
    public void initialize(final Bootstrap<PatrimonioServiceConfiguration> bootstrap) {
        
    	 bootstrap.addBundle(new SwaggerBundle<PatrimonioServiceConfiguration>() {
             @Override
             protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(
                         final PatrimonioServiceConfiguration positionConfiguration) {
                 return positionConfiguration.getSwaggerBundleConfiguration();
             }
         });
    	 
    	 bootstrap.addBundle(jaxWsBundle);
    }

    @Override
    public void run(final PatrimonioServiceConfiguration configuration,
                    final Environment environment) {
    	
    	final MongoClient mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                        .build()
        );

       
    	environment.healthChecks().register("PositionMongoDBHealthCheck",
                new it.poste.patrimonio.db.model.health.DropwizardMongoDBHealthCheck(mongoClient));
    	
    	 final Injector injector = Guice.createInjector(new AppModule(configuration, environment));
         environment.jersey().register(injector.getInstance(PositionResource.class));
         injector.getInstance(PersistInitialiser.class);
         
         jaxWsBundle.publishEndpoint(new EndpointBuilder(configuration.getSoapConfig().getPositionEndPoint(), injector.getInstance(PositionSoapService.class))
	                .cxfInInterceptors(new LoggingInInterceptor())
	                .cxfOutInterceptors(new LoggingOutInterceptor()));
    }

}
