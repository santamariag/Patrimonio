package eu.tasgroup.kafka.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class AssetDTO {
	
	private String isin;
	private Long quantity;
	private BigDecimal price;

}
