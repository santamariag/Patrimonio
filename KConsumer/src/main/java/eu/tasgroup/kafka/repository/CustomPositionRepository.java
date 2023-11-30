package eu.tasgroup.kafka.repository;

import java.math.BigDecimal;

public interface CustomPositionRepository {
	
	public long updatePrice(String Isin, BigDecimal price);

}
