package it.poste.patrimonio.ms.resources;

import javax.validation.Valid;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;


import it.poste.patrimonio.specs.api.DettaglioPatrimonioApi;
import it.poste.patrimonio.specs.model.DettaglioPatrimonioInput;
import it.poste.patrimonio.specs.model.DettaglioPatrimonioTypeTypeNs2;
import it.poste.patrimonio.specs.model.EsitoTypeTypeNs2;
import it.poste.patrimonio.specs.model.PatrimonioClienteOutputElementNs1;
import it.poste.patrimonio.specs.model.Ww0aOTabpatTabCpTypeNs2;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DettaglioPatrimonioResource implements DettaglioPatrimonioApi {

	@Override
	public Response dettaglioPatrimonioPost(
			@Valid DettaglioPatrimonioInput dettaglioPatrimonioInputDTO, String source, String requestID,
			String routingRole, String creationTime) {
		
		
		log.info("SOURCE "+source);
		log.info("REQUEST ID "+requestID);
		log.info("ROUTING ROLE "+routingRole);
		log.info("CREATION TIME "+creationTime);
		
		log.info("REQUEST "+dettaglioPatrimonioInputDTO);
		
		
		PatrimonioClienteOutputElementNs1 output=new PatrimonioClienteOutputElementNs1();
		
		EsitoTypeTypeNs2 esito=new EsitoTypeTypeNs2();
		esito.setEsito("OK");
		
		output.setEsito(esito);
		
		DettaglioPatrimonioTypeTypeNs2 dettaglioP=new DettaglioPatrimonioTypeTypeNs2();
		dettaglioP.setNdg("12345");
		
		output.getDettaglioPatrimonio().add(dettaglioP);
		
		
		Ww0aOTabpatTabCpTypeNs2 dettaglioC=new Ww0aOTabpatTabCpTypeNs2();
		dettaglioC.setIdFamigliaProdotto("1");
		dettaglioC.setNdg("123456");
		
		output.getDettaglioCards().add(dettaglioC);
		
		if(source.equals("Postman"))
			throw new WebApplicationException("Test");
		
		return Response.ok(output).build();
	}

}
