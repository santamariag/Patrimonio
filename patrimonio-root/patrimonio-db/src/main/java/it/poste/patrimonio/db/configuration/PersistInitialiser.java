package it.poste.patrimonio.db.configuration;

import com.google.inject.Inject;
import com.google.inject.persist.PersistService;

public class PersistInitialiser {
	
	PersistService service;

    @Inject
    public PersistInitialiser(PersistService service) {
    	this.service=service;
        service.start();
    }
    
    public void stop() {
    	service.stop();
    }
}
