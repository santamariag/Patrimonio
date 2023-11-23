package it.poste.patrimonio.config.ms;

import io.dropwizard.Configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import it.poste.patrimonio.config.db.MongoConfiguration;
import it.poste.patrimonio.config.soap.SoapConfiguration;


import javax.validation.Valid;
import javax.validation.constraints.*;

public class PatrimonioServiceConfiguration extends Configuration {

	@Valid
	@JsonProperty("mongo")
	private MongoConfiguration databaseConfiguration;

	@JsonProperty("swagger")
	private SwaggerBundleConfiguration swaggerBundleConfiguration;
	
	@JsonProperty
	@NotNull
	private SoapConfiguration soapConfig = new SoapConfiguration();
	    

	public SwaggerBundleConfiguration getSwaggerBundleConfiguration() {
		return swaggerBundleConfiguration;
	}

	public MongoConfiguration getDatabaseConfiguration() {
		return databaseConfiguration;
	}

	public SoapConfiguration getSoapConfig() {
		return soapConfig;
	}

	public void setSoapConfig(SoapConfiguration soapConfig) {
		this.soapConfig = soapConfig;
	}
	
	
}
