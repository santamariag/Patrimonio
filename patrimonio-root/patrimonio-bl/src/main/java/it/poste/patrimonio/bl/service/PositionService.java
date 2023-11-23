package it.poste.patrimonio.bl.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

import it.poste.patrimonio.db.dao.PositionDAO;
import it.poste.patrimonio.db.model.Position;
import it.poste.patrimonio.itf.api.PositionApi;
import it.poste.patrimonio.itf.mapper.PositionMapper;

public class PositionService {



	/**
	 * DAO position.
	 */
	private PositionDAO positionDAO;
	
	/**
	 * DAO price.
	 */
	//private PriceDAO priceDAO;

	/**
	 * Constructor.
	 *
	 * @param positionDAO the dao position.
	 */
	@Inject
	public PositionService(final PositionDAO positionDAO/*, final PriceDAO priceDAO*/) {
		this.positionDAO = positionDAO;
		//this.priceDAO = priceDAO;
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<PositionApi> getAll() {
		
		Optional<List> positionsOpt=positionDAO.find(Position.class);
    
		return positionsOpt.isPresent()
					? ((List<Position>) positionsOpt.get()).stream()
			              .map(p -> PositionMapper.INSTANCE.positionToPositionApi(p))
			              .collect(Collectors.toList())
							: new ArrayList<PositionApi>();
	}

	public Optional<PositionApi> getOne(String ndg) {

		Optional<Position> positionOpt=positionDAO.findById(ndg);
		return positionOpt.isPresent()
					? Optional.of(PositionMapper.INSTANCE.positionToPositionApi(positionOpt.get()))
						: Optional.empty();
	}

	public void save(PositionApi position) {

		
		positionDAO.persist(PositionMapper.INSTANCE.positionApiToPosition(position));
	}

	public void update(PositionApi position) {

		positionDAO.merge(PositionMapper.INSTANCE.positionApiToPosition(position));

	}

	@Transactional
	public boolean delete(String ndg) {

		Optional<Position> positionOpt= positionDAO.findById(ndg);
	    
		if (positionOpt.isPresent()) {
			positionDAO.remove(positionOpt.get());
			return true;
		}
           
		return false;

	}

	/*public void insertPrice(Price price) {

		priceDAO.insertPrice(price);

	}

	public Price findPrice(String isin) {

		return priceDAO.findPrice(isin);

	}


	public void updatePrice(String isin, BigDecimal price) {

		priceDAO.updatePrice(isin, price);

	}*/
	
	/*public void updateByIsin(String isin, BigDecimal price) {
		
		List<Position> positions= positionDAO.findByIsin(isin);

		 for (Position p : positions) {
			 BigDecimal newBalance=BigDecimal.ZERO;
			 List<Asset> assets=p.getAssets();
			 for (Asset a : assets) {
				 if(a.getIsin().equals(isin)) {
					 a.setPrice(price);
				 }
				newBalance=newBalance.add(a.getPrice().multiply(new BigDecimal(a.getQuantity())));	    			
			 }
			 p.setBalance(newBalance);
			 positionDAO.save(p);
		 }
		
	}*/

}
