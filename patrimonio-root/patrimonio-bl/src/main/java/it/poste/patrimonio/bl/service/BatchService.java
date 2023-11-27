package it.poste.patrimonio.bl.service;

import java.util.Optional;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

import it.poste.patrimonio.bl.util.Mode;
import it.poste.patrimonio.itf.batch.api.PriceApi;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BatchService {
	
	private PositionService positionService;
	private PriceService priceService;
	
	@Inject
	public BatchService(final PositionService positionService, final PriceService priceService) {
		this.positionService = positionService;
		this.priceService = priceService;
	}
	
	public Optional<PriceApi> getOne(String isin){
		return priceService.getOne(isin);
	}
	
	
	@Transactional
	public void managePriceAndPosition(PriceApi price, Mode mode, Long pageSize) {
		
		switch (mode) {
		case INSERT:
			
			priceService.save(price);
			break;
			
		case UPDATE:
			
			priceService.update(price);
			break;

		default:
			log.warn("Not supported operation "+ mode);
			return;
		}
		
		positionService.updateByIsinPaged(price.getIsin(), price.getPrice(), pageSize);
		
	}
	
	

}
