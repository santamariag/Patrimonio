package it.poste.patrimonio.bl.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

import it.poste.patrimonio.db.dao.PriceDAO;
import it.poste.patrimonio.db.model.Price;
import it.poste.patrimonio.itf.batch.api.PriceApi;
import it.poste.patrimonio.itf.mapper.PriceMapper;

public class PriceService {
	
	/**
	 * DAO price.
	 */
	private PriceDAO priceDAO;

	@Inject
	public PriceService(PriceDAO priceDAO) {
		this.priceDAO = priceDAO;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<PriceApi> getAll() {
		
		Optional<List> pricesOpt=priceDAO.find(Price.class);
    
		return pricesOpt.isPresent()
					? ((List<Price>) pricesOpt.get()).stream()
			              .map(p -> PriceMapper.INSTANCE.priceToPriceApi(p))
			              .collect(Collectors.toList())
							: new ArrayList<PriceApi>();
	}
	
	public Optional<PriceApi> getOne(String isin) {

		Optional<Price> priceOpt=priceDAO.findById(isin);
		return priceOpt.isPresent()
					? Optional.of(PriceMapper.INSTANCE.priceToPriceApi(priceOpt.get()))
						: Optional.empty();

	}

	public void save(PriceApi price) {

		
		priceDAO.persist(PriceMapper.INSTANCE.priceApiToPrice(price));
	}

	public void update(PriceApi price) {

		priceDAO.merge(PriceMapper.INSTANCE.priceApiToPrice(price));
	}
	
	public void saveOrUpdate(PriceApi price) {

		Optional<Price> priceOpt=priceDAO.findById(price.getIsin());
		
		if(priceOpt.isPresent()) 
			priceDAO.merge(PriceMapper.INSTANCE.priceApiToPrice(price));
		else
			priceDAO.persist(PriceMapper.INSTANCE.priceApiToPrice(price));
	}

	@Transactional
	public boolean delete(String isin) {

		Optional<Price> priceOpt=priceDAO.findById(isin);
	    
		if (priceOpt.isPresent()) {
			priceDAO.remove(priceOpt.get());
			return true;
		}           
		return false;

	}

}
