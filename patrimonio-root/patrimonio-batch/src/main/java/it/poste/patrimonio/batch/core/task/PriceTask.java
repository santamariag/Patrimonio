package it.poste.patrimonio.batch.core.task;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;

import io.dropwizard.servlets.tasks.Task;
import it.poste.patrimonio.batch.core.common.PriceFileAction;
import it.poste.patrimonio.bl.service.BatchService;
import it.poste.patrimonio.config.batch.PageConfiguration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PriceTask extends Task {
	
	private BatchService batchService;	
	private PageConfiguration paging;

	@Inject
	public PriceTask(BatchService batchService, PageConfiguration paging) {
		super("pricetask");
		this.batchService = batchService;
		this.paging = paging;
	}

	@Override
	public void execute(Map<String, List<String>> parameters, PrintWriter output) throws Exception {
		
		log.info("Executing "+this.getName());
		
		parameters.entrySet().forEach(e->log.info(e.getKey()+" "+ e.getValue()));
				
		new PriceFileAction(batchService, paging).elaborateFile(parameters);
		
		output.print(this.getName()+ " executed");
		
	}

	

}
