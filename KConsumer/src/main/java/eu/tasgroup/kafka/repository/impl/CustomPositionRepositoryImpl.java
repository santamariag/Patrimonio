package eu.tasgroup.kafka.repository.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.client.result.UpdateResult;

import eu.tasgroup.kafka.model.Position;
import eu.tasgroup.kafka.repository.CustomPositionRepository;

public class CustomPositionRepositoryImpl<T, ID> implements CustomPositionRepository{
	
	@Autowired
	private MongoTemplate template;

	@Override
	public long updatePrice(String isin, BigDecimal price) {
	
		Query query=new Query().addCriteria(Criteria.where("assets.isin").is(isin));
		Update updateDefinition=new Update().set("assets.$[e].price", price).set("assets.$[e].isin", isin).filterArray("e.isin", isin);
			
		UpdateResult result= template.updateMulti(query, updateDefinition, Position.class);
		
		return result.getModifiedCount();
		
	}

}
