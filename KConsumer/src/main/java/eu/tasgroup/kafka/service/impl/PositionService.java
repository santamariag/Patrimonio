package eu.tasgroup.kafka.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.tasgroup.kafka.dto.PositionDTO;
import eu.tasgroup.kafka.model.Asset;
import eu.tasgroup.kafka.model.Position;
import eu.tasgroup.kafka.producer.Producer;
import eu.tasgroup.kafka.repository.IPositionRepository;
import eu.tasgroup.kafka.service.IPositionService;
import eu.tasgroup.kafka.util.KeyExtractor;

@Service
public class PositionService implements IPositionService {
	
	private IPositionRepository repository;
	private ObjectMapper objectMapper;
	private final Producer<String, PositionDTO> producer;
	

	public PositionService(IPositionRepository repository, ObjectMapper objectMapper, Producer<String, PositionDTO> producer) {
		this.repository = repository;
		this.objectMapper = objectMapper;
		this.producer = producer;
	}

	@Override
	public List<PositionDTO> findAllPosition() {
		
		List<Position> positions= repository.findAll();
		
		return positions.stream()
			.map(p->objectMapper.convertValue(p, PositionDTO.class))
			.collect(Collectors.toList());
	
	}

	@Override
	public PositionDTO addPosition(PositionDTO position) {
			
		return objectMapper
				.convertValue(repository.save(objectMapper
						.convertValue(position, Position.class)), PositionDTO.class);
	}

	@Override
	public void sendPosition(PositionDTO position) {
			
		producer.sendMessage(new KeyExtractor<String, PositionDTO>() {
			
			@Override
			public String extractKey(PositionDTO event) 
			{
				return String.valueOf(event.getNdg()/*+"_"+System.currentTimeMillis()*/);
			}
		}.extractKey(position),  position);
		
	}

	@Override
	public Optional<PositionDTO> findByNdg(String ndg) {
		
		Optional<Position> positionOptional=repository.findById(ndg);
		if(positionOptional.isPresent()) {
			return Optional.of(objectMapper.convertValue(positionOptional.get(), PositionDTO.class));
		}
		 return Optional.empty();
	}

	@Override
	public void updateByIsin(String isin, BigDecimal price) {
		
		List<Position> positions= repository.findByIsin(isin);

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
			 repository.save(p);
		 }
		
	}

	@Override
	public long updatePrice(String isin, BigDecimal price) {
		
		return repository.updatePrice(isin, price);
		
	}
}
