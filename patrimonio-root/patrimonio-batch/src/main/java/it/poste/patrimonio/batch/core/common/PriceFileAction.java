package it.poste.patrimonio.batch.core.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.knowm.sundial.JobAction;

import com.opencsv.bean.CsvToBeanBuilder;

import it.poste.patrimonio.bl.service.BatchService;
import it.poste.patrimonio.bl.util.Mode;
import it.poste.patrimonio.config.batch.PageConfiguration;
import it.poste.patrimonio.itf.batch.api.PriceApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class PriceFileAction extends JobAction {

	private final BatchService batchService;
	private final PageConfiguration paging;
	
	private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

	@Override
	public void doRun() {

		elaborateFile(getJobContext().get("params"));
		
	}
	
	public boolean elaborateFile(Map<String, List<String>> parameters) {

		String input=new StringBuilder()
				.append(parameters.get("inputPath").get(0))
				.append(System.getProperty("file.separator"))
				.append(parameters.get("fileName").get(0))
				.toString();


		File file = new File(input);
		if(file.exists()) {
			FileReader reader=null;
			try {
				reader = (new FileReader(file));
			} catch (FileNotFoundException e) {
				log.error(e.getMessage());
			}
			List<PriceApi> prices = new CsvToBeanBuilder<PriceApi>(reader)
					.withType(PriceApi.class)
					.build()
					.parse();

			try {
				reader.close();
			} catch (IOException e) {
				log.error(e.getMessage());
			}

			boolean success=managePersistence(prices);

			return success ? moveToCompleted(file, parameters.get("completedPath").get(0))
					: moveToDiscarded(file, parameters.get("discardedPath").get(0));
		}
		log.info("No new Price file present");
		return true;

	}

	private boolean managePersistence(List<PriceApi> prices) {

		boolean success=true;
		try {
			prices.forEach(p->{
				log.info("Elaborating "+p);

				Optional<PriceApi> priceOpt=batchService.getOne(p.getIsin());
				if(priceOpt.isPresent()) {
					PriceApi findPrice=priceOpt.get();
					if(p.getPrice().compareTo(findPrice.getPrice())!=0) {
						log.info("Price present with different value...updating");
						batchService.managePriceAndPosition(p, Mode.UPDATE, paging.getPageSize());

					} else {
						log.info("Price present with same value...nothing to do");
					}

				} else {
					log.info("Price not present...inserting");
					batchService.managePriceAndPosition(p, Mode.INSERT, paging.getPageSize());
				}

			});
		} catch (Exception e) {
			log.error(e.getMessage());
			success=false;
		}

		return success;

	}

	private boolean moveToDiscarded(File file, String discardedPath) {

		log.info("Move to discarded "+ file.getAbsolutePath());
		String outputPath=new StringBuilder()
				.append(discardedPath)
				.append(System.getProperty("file.separator"))
				.append(file.getName()+".discarded_"+LocalDateTime.now().format(dtf))
				.toString();
		return moveFile(file.getAbsolutePath(), outputPath);

	}

	private boolean moveToCompleted(File file, String completedPath) {

		log.info("Move to completed "+ file.getAbsolutePath());
		String targetPath=new StringBuilder()
				.append(completedPath)
				.append(System.getProperty("file.separator"))
				.append(file.getName()+".completed_"+LocalDateTime.now().format(dtf))
				.toString();
		return moveFile(file.getAbsolutePath(), targetPath);
	}

	private boolean moveFile(String sourcePath, String targetPath) {

		boolean succes=true;
		try {
			Path path= Files.copy(Paths.get(sourcePath), Paths.get(targetPath), StandardCopyOption.REPLACE_EXISTING);
			log.info("Destination file "+path.toAbsolutePath().toString());
			Files.delete(Paths.get(sourcePath));
		} catch (IOException e) {
			succes=false;
			log.error(e.getMessage());
		}

		return succes;
	}

}
