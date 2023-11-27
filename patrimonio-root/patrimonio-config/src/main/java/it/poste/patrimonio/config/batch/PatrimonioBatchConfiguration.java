package it.poste.patrimonio.config.batch;

import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.knowm.dropwizard.sundial.SundialConfiguration;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;
import it.poste.patrimonio.config.db.MongoConfiguration;

public class PatrimonioBatchConfiguration extends Configuration {
    
	@Valid
	@JsonProperty("mongo")
	private MongoConfiguration databaseConfiguration;
	

    @Valid
    @NotNull
    @JsonProperty("sundial")
    public SundialConfiguration sundialConfiguration = new SundialConfiguration();


    @JsonProperty("priceFileConfig")
    @NotNull
    private PriceFileConfiguration priceFileConfiguration = new PriceFileConfiguration();
    
    @JsonProperty("jobsConfig")
    private Map<String , String> jobsConfig;
    
    @JsonProperty("paging")
    private PageConfiguration pageConfig;

	public MongoConfiguration getDatabaseConfiguration() {
		return databaseConfiguration;
	}

	public SundialConfiguration getSundialConfiguration() {
		return sundialConfiguration;
	}

	public PriceFileConfiguration getPriceFileConfiguration() {
		return priceFileConfiguration;
	}

	public Map<String, String> getJobsConfig() {
		return jobsConfig;
	}

	public PageConfiguration getPageConfig() {
		return pageConfig;
	}
}
