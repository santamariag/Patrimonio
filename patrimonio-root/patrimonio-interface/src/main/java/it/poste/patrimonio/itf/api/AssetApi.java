package it.poste.patrimonio.itf.api;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlType(name = "Asset", propOrder = {"isin", "price", "quantity"})
public final class AssetApi implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7977373736357587258L;
	private String isin;
	private Long quantity;
	private BigDecimal price;

}
