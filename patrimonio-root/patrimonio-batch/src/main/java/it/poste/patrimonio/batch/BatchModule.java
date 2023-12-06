package it.poste.patrimonio.batch;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import it.poste.patrimonio.bl.service.BatchService;
import it.poste.patrimonio.config.batch.PatrimonioBatchConfiguration;

public class BatchModule extends AbstractModule {
		
	private PatrimonioBatchConfiguration configuration;
	
    public BatchModule(final PatrimonioBatchConfiguration configuration) {
        this.configuration = configuration;
    }


    @Override
    protected void configure() {
          
    	bind(BatchService.class).in(Singleton.class);;
    	bind(PatrimonioBatchConfiguration.class).toInstance(configuration);
    	
    }
}
