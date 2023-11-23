package it.poste.patrimonio.ms.resources.ws;

import java.util.List;
import java.util.Optional;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.codahale.metrics.annotation.Metered;
import com.google.inject.Inject;

import it.poste.patrimonio.bl.service.PositionService;
import it.poste.patrimonio.itf.api.PositionApi;
import lombok.extern.slf4j.Slf4j;

@Metered
@WebService(serviceName = "PositionService")
@Slf4j
public class PositionSoapService {

	private PositionService positionService;

	@Inject
	public PositionSoapService(PositionService positionService) {
		this.positionService = positionService;
	}

	@WebMethod
	@XmlElementWrapper(name="positions") 
    @XmlElement(name="position")
	public List<PositionApi> getPositions() {

		log.info("List all Positions.");
		return positionService.getAll();

	}

	@WebMethod
	public @WebResult(name = "position") PositionApi getPosition(@WebParam(name = "ndg") @XmlElement(required=true,nillable=false) String ndg) {

		log.info("Find the position by identifier : " + ndg);
		Optional<PositionApi> positionOpt=positionService.getOne(ndg);
		return positionOpt.isPresent()
				? positionOpt.get()
						:null;
		
	}

}
