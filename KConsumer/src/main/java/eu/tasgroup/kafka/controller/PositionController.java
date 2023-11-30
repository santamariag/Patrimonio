package eu.tasgroup.kafka.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.tasgroup.kafka.dto.PositionDTO;
import eu.tasgroup.kafka.service.IPositionService;

@RestController
@RequestMapping("/api")
public class PositionController {

  private final IPositionService positionService;
 

  public PositionController(IPositionService positionService) {
    this.positionService = positionService;
  }

  @GetMapping(value = "/positions")
  public List<PositionDTO> getPositions() {
    return positionService.findAllPosition();
  }

  @PostMapping(value = "/position")
  public void insertPosition(@RequestBody PositionDTO position) {
	  
	  positionService.sendPosition(position);
	  
  }
  
  @GetMapping(value = "/position/{ndg}")
  public ResponseEntity<PositionDTO> getPositionByNdg(@PathVariable String ndg) {
	  
	  Optional<PositionDTO> positionOpt=positionService.findByNdg(ndg);
	  
	  return positionOpt.isPresent()
			  ? ResponseEntity.ok(positionOpt.get())
					  :ResponseEntity.notFound().build();
	  
  }
  
  @PutMapping(value = "/positions/updateByIsin")
  public void updateByIsin(@RequestParam String isin, @RequestParam BigDecimal price) {
	  
	  positionService.updateByIsin(isin, price);
	  
	  
  }
  
  @PutMapping(value = "/positions/updatePrice")
  public long updatePrice(@RequestParam String isin, @RequestParam BigDecimal price) {
	  
	   return positionService.updatePrice(isin, price);
	  
	  
  }
}