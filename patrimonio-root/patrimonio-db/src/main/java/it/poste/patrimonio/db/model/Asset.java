package it.poste.patrimonio.db.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public final class Asset implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7977373736357587258L;
	private String isin;
	private Long quantity;
	private Double price;

}
