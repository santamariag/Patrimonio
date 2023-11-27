package it.poste.patrimonio.config.batch;

import lombok.Data;

@Data
public class PriceFileConfiguration {
	
	private String inputPath;
	private String fileName;
	private String completedPath;
	private String discardedPath;

}
