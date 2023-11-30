package eu.tasgroup.kafka.model;

import java.math.BigDecimal;


import lombok.Data;

@Data
public class Asset {
	
	private String isin;
	private Long quantity;
	private BigDecimal price;

}
