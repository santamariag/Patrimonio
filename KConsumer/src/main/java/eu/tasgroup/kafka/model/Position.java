package eu.tasgroup.kafka.model;

import java.math.BigDecimal;
import java.util.List;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Document(collection = "positions")
@Data
public class Position {
  @Id
  private String ndg;  
  private List<Asset> assets;
  private BigDecimal balance;

}