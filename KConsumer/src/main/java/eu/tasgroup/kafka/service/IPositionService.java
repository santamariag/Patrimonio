package eu.tasgroup.kafka.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import eu.tasgroup.kafka.dto.PositionDTO;

public interface IPositionService {
	
	public List<PositionDTO> findAllPosition();
	
	public PositionDTO addPosition(PositionDTO position);

	public void sendPosition(PositionDTO position);
	
	public Optional<PositionDTO> findByNdg(String ndg);
	
	public void updateByIsin(String isin, BigDecimal price);
	
	public long updatePrice(String isin, BigDecimal price);

}
