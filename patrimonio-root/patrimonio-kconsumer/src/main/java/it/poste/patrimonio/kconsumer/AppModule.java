package it.poste.patrimonio.kconsumer;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.persist.jpa.JpaPersistModule;
import io.dropwizard.setup.Environment;
import it.poste.patrimonio.config.db.MongoConfiguration;
import it.poste.patrimonio.config.kconsumer.PatrimonioKConsumerConfiguration;

import java.util.Properties;

public class AppModule extends AbstractModule {

    private PatrimonioKConsumerConfiguration configuration;
    private Environment environment;

    public AppModule(final PatrimonioKConsumerConfiguration configuration, final Environment environment) {
        this.configuration = configuration;
        this.environment = environment;
    }

    @Override
    protected void configure() {
        bind(PatrimonioKConsumerConfiguration.class).toInstance(configuration);
        bind(Environment.class).toInstance(environment);

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
