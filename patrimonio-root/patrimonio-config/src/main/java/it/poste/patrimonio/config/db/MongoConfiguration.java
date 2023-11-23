package it.poste.patrimonio.config.db;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.poste.patrimonio.config.ms.PatrimonioServiceConfiguration;

import javax.validation.constraints.NotNull;

public class MongoConfiguration extends PatrimonioServiceConfiguration {

    @NotNull
    private String host;

    @NotNull
    private String provider;

    @NotNull
    private String database;

    @NotNull
    @JsonProperty("jta.platform")
    private String jtaPlatform;

    @NotNull
    @JsonProperty("create.database")
    private boolean createDatabase;

    public String getHost() {
        return host;
    }

    public String getProvider() {
        return provider;
    }

    public String getDatabase() {
        return database;
    }

    public String getJtaPlatform() {
        return jtaPlatform;
    }

    public boolean isCreateDatabase() {
        return createDatabase;
    }
}
