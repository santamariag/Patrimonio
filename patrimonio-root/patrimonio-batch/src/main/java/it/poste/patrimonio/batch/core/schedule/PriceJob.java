package it.poste.patrimonio.batch.core.schedule;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.knowm.sundial.Job;
import org.knowm.sundial.SundialJobScheduler;
import org.knowm.sundial.exceptions.JobInterruptException;


import it.poste.patrimonio.batch.core.common.PriceFileAction;
import it.poste.patrimonio.bl.service.BatchService;
import it.poste.patrimonio.config.batch.PriceFileConfiguration;
import lombok.extern.slf4j.Slf4j;

//@CronTrigger(cron = "0/15 * * * * ?")
@Slf4j
public class PriceJob extends Job {
	
	private BatchService batchService;
	

	@Override
	public void doRun() throws JobInterruptException {
		
		
		
		log.info("Executing "+this.getClass().getSimpleName()+" "+LocalDateTime.now());
		
		PriceFileConfiguration config = PriceFileConfiguration.class.cast(SundialJobScheduler.getServletContext().getAttribute("priceFileConfig"));
		
		Map<String, List<String>> parameters=buildParameters(config);
				
		this.batchService = BatchService.class.cast(SundialJobScheduler.getServletContext().getAttribute("batchService"));
				
		getJobContext().put("params", parameters);
		
		new PriceFileAction(batchService).run();;
		
	}

	private Map<String, List<String>> buildParameters(PriceFileConfiguration config) {
		
		Map<String, List<String>> params=new HashMap<>();
		
		params.put("inputPath", Arrays.asList(config.getInputPath()));
		params.put("fileName", Arrays.asList(config.getFileName()));
		params.put("completedPath", Arrays.asList(config.getCompletedPath()));		
		params.put("discardedPath", Arrays.asList(config.getDiscardedPath()));
		
		return params;
		
	}

}
