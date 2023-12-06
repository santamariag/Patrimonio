package it.poste.patrimonio.batch;

import org.knowm.dropwizard.sundial.SundialBundle;
import org.knowm.dropwizard.sundial.SundialConfiguration;
import org.knowm.sundial.SundialJobScheduler;
import org.quartz.core.SchedulerFactory;

import com.google.common.base.CaseFormat;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import it.poste.patrimonio.batch.cli.BatchCommand;
import it.poste.patrimonio.batch.core.schedule.PriceJob;
import it.poste.patrimonio.batch.core.task.PriceTask;
import it.poste.patrimonio.bl.service.BatchService;
import it.poste.patrimonio.config.batch.PatrimonioBatchConfiguration;
import it.poste.patrimonio.db.configuration.PersistInitialiser;


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
        
    	bootstrap.addBundle(new SundialBundle<PatrimonioBatchConfiguration>() {

            @Override
            public SundialConfiguration getSundialConfiguration(final PatrimonioBatchConfiguration patrimonioBatchConfiguration) {
              return patrimonioBatchConfiguration.getSundialConfiguration();
            }
          });
    	    	
    	bootstrap.addCommand(new BatchCommand("pricebatch", "Price file acquisition"));
    }

    @Override
    public void run(final PatrimonioBatchConfiguration configuration,
                    final Environment environment) {
       
    	final MongoClient mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                        .build()
        );

       
    	environment.healthChecks().register("PositionMongoDBHealthCheck",
                new it.poste.patrimonio.db.model.health.DropwizardMongoDBHealthCheck(mongoClient));
    	
    	final Injector injector = Guice.createInjector(new AppModule(configuration, environment));
    	injector.getInstance(PersistInitialiser.class);
    	
    	  // tasks        
        environment.admin().addTask(new PriceTask(injector.getInstance(BatchService.class), configuration.getPageConfig()));
        
        // scheduled job
        environment.getApplicationContext().setAttribute("priceFileConfig", configuration.getPriceFileConfiguration());
        environment.getApplicationContext().setAttribute("batchService", injector.getInstance(BatchService.class));
        environment.getApplicationContext().setAttribute("pageConfig", configuration.getPageConfig());
        
    
        SundialJobScheduler.createScheduler(new SchedulerFactory());
        
        SundialJobScheduler.addJob(PriceJob.class.getSimpleName(), PriceJob.class.getName());
        SundialJobScheduler.addCronTrigger(PriceJob.class.getSimpleName()+"-Cron-Trigger", 
        									PriceJob.class.getSimpleName(), 
        									configuration.getJobsConfig().get(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL,  PriceJob.class.getSimpleName())));
        
    }

}
