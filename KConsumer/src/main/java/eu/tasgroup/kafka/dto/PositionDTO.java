package eu.tasgroup.kafka.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class PositionDTO {
  
  private String ndg;  
  private List<AssetDTO> assets;
  private BigDecimal balance;

}