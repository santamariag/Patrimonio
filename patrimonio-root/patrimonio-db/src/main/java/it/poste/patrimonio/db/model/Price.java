package it.poste.patrimonio.db.model;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Entity
@Table(name = "prices")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Price {
	
	@Id
	private String isin;
	private Double price;

}
