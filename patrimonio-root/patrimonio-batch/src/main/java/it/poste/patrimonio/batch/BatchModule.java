package it.poste.patrimonio.batch;

import java.util.Properties;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.google.inject.persist.jpa.JpaPersistModule;

import it.poste.patrimonio.bl.service.BatchService;
import it.poste.patrimonio.config.batch.PatrimonioBatchConfiguration;
import it.poste.patrimonio.config.db.MongoConfiguration;

public class BatchModule extends AbstractModule {
		
	private PatrimonioBatchConfiguration configuration;
	
    public BatchModule(final PatrimonioBatchConfiguration configuration) {
        this.configuration = configuration;
    }


    @Override
    protected void configure() {
          
    	bind(BatchService.class).in(Singleton.class);;
    	bind(PatrimonioBatchConfiguration.class).toInstance(configuration);
    	install(jpaModule(configuration.getDatabaseConfiguration()));
	}
	
	
	 private Module jpaModule(MongoConfiguration databaseConfiguration ) {
	    	final Properties properties = new Properties();
	    	properties.put("hibernate.transaction.jta.platform", databaseConfiguration.getJtaPlatform());
	    	properties.put("hibernate.ogm.datastore.provider", databaseConfiguration.getProvider());
	    	properties.put("hibernate.ogm.datastore.host", databaseConfiguration.getHost());
	    	properties.put("hibernate.ogm.datastore.database", databaseConfiguration.getDatabase());
	    	properties.put("hibernate.ogm.datastore.create_database", databaseConfiguration.isCreateDatabase());

	    	final JpaPersistModule jpaModule = new JpaPersistModule("DefaultUnit");
	    	jpaModule.properties(properties);

	    	return jpaModule;
	    }
}
