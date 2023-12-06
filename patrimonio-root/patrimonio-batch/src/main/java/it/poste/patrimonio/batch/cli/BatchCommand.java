package it.poste.patrimonio.batch.cli;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;

import io.dropwizard.cli.ConfiguredCommand;
import io.dropwizard.setup.Bootstrap;
import it.poste.patrimonio.batch.BatchModule;
import it.poste.patrimonio.batch.PersistenceModule;
import it.poste.patrimonio.batch.core.common.PriceFileAction;
import it.poste.patrimonio.bl.service.BatchService;
import it.poste.patrimonio.config.batch.PatrimonioBatchConfiguration;
import it.poste.patrimonio.config.batch.PriceFileConfiguration;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

@Slf4j
public class BatchCommand extends ConfiguredCommand<PatrimonioBatchConfiguration>{
	
	
	private final String IP="inputPath";
	private final String FN="fileName";
	private final String CP="completedPath";
	private final String DP="discardedPath";
		
	public BatchCommand(String name, String description) {
		
		super(name, description);
	}
	
	
	@Override
    public void configure(Subparser subparser) {
        super.configure(subparser);
        
        subparser.addArgument("-ip")
        			.dest(IP)
        			.help("File input path");
        
        subparser.addArgument("-f")
					.dest(FN)
					.help("File name");
        
        subparser.addArgument("-cp")
					.dest(CP)
					.help("File completed path");
        
        subparser.addArgument("-dp")
					.dest(DP)
					.help("File discarded path");
        

    }

	@Override
	protected void run(Bootstrap<PatrimonioBatchConfiguration> bootstrap, Namespace namespace,
			PatrimonioBatchConfiguration configuration) throws Exception {
		
		log.info(this.getName()+" command launched");
		
		final Injector injector = Guice.createInjector(new PersistenceModule(configuration));
		
		PersistService persistService = injector.getInstance(PersistService.class);
		
		persistService.start();
		
		final Injector childInjector = injector.createChildInjector(new BatchModule(configuration));

		Map<String, Object> paramsFromInput=namespace.getAttrs();
		
		Map<String, List<String>> paramsFromConfig=buildParameters(configuration.getPriceFileConfiguration());
		
		Map<String, List<String>> mergedParams=mergeParameters(paramsFromInput, paramsFromConfig);
						
		new PriceFileAction(childInjector.getInstance(BatchService.class), configuration.getPageConfig())
						.elaborateFile(mergedParams);
		
		persistService.stop();
		
		log.info(this.getName()+" command completed");
	}
	
	private Map<String, List<String>> mergeParameters(Map<String, Object> paramsFromInput,
			Map<String, List<String>> paramsFromConfig) {
	
		Map<String, List<String>> merged=new HashMap<>();
		
		merged.put(IP, paramsFromInput.get(IP)!=null
				? Arrays.asList(paramsFromInput.get(IP).toString())
						: paramsFromConfig.get(IP));
		merged.put(FN, paramsFromInput.get(FN)!=null
				? Arrays.asList(paramsFromInput.get(FN).toString())
						: paramsFromConfig.get(FN));
		merged.put(CP, paramsFromInput.get(CP)!=null
				? Arrays.asList(paramsFromInput.get(CP).toString())
						: paramsFromConfig.get(CP));
		merged.put(DP, paramsFromInput.get(DP)!=null
				? Arrays.asList(paramsFromInput.get(DP).toString())
						: paramsFromConfig.get(DP));

		return merged;
	}


	private Map<String, List<String>> buildParameters(PriceFileConfiguration config) {
		
		Map<String, List<String>> params=new HashMap<>();
		
		params.put(IP, Arrays.asList(config.getInputPath()));
		params.put(FN, Arrays.asList(config.getFileName()));
		params.put(CP, Arrays.asList(config.getCompletedPath()));		
		params.put(DP, Arrays.asList(config.getDiscardedPath()));
		
		return params;
		
	}

}
