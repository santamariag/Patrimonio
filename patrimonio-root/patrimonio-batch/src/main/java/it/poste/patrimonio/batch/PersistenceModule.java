package it.poste.patrimonio.batch;

import java.util.Properties;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.persist.jpa.JpaPersistModule;

import it.poste.patrimonio.config.batch.PatrimonioBatchConfiguration;
import it.poste.patrimonio.config.db.MongoConfiguration;

public class PersistenceModule extends AbstractModule {
	
private PatrimonioBatchConfiguration configuration;
	
    public PersistenceModule(final PatrimonioBatchConfiguration configuration) {
        this.configuration = configuration;
    }
	
	@Override
    protected void configure() {
		
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
