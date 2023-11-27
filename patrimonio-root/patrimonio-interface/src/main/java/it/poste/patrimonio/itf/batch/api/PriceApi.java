package it.poste.patrimonio.itf.batch.api;


import com.opencsv.bean.CsvBindByPosition;

import lombok.Data;

@Data
public class PriceApi {
	
	@CsvBindByPosition(position = 0)
	private String isin;
	@CsvBindByPosition(position = 1)
	private Double price;

}
