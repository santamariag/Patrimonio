package eu.tasgroup.kafka.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import eu.tasgroup.kafka.model.Position;

public interface IPositionRepository extends MongoRepository<Position, String>, CustomPositionRepository {
	
	@Query(value = "{ 'assets.isin' : ?0 }")
	public List<Position> findByIsin(String isin);
}
