package application;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sondes.scenarios.ScenarioCaller;

public final class Application {
	
	/**
	 * Application instance de l'application
	 */
	private static Application application;

	private Options options;

	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	
	/**
	 * Application Constructor
	 */
	public Application() {
		this.options = new Options();
		Option logfile   = OptionBuilder.isRequired(false)
				.hasArg()
				.withDescription("use given file for log")
				.create("logfile");
		
		Option outputdir   = OptionBuilder.isRequired(false)
				.hasArg()
				.withDescription("Store output file in this directory")
				.create("outputdir");
		
		Option help   = OptionBuilder.isRequired(false)
				.withDescription("print this help")
				.create("help");
			 
		Option scenariofile   = OptionBuilder.isRequired(true)
				.hasArg()
				.withDescription("use given file for execute scenario")
				.create("scenariofile");
			 
		options.addOption(logfile);
		options.addOption(outputdir);
		options.addOption(scenariofile);
		options.addOption(help);
	}
	
	/**
	 * Applique le DP singleton sur la classe application
	 * @return
	 */
	public static Application getInstance() {
		if (application == null) {
			application = new Application();
		}

		return application;
	}

	/**
	 * Point d'entrée du programme
	 * @param args
	 */
	public static void main(String[] args) {
		Application.getInstance().lancer(args);
	}
	
	public void lancer(String[] args) {
		
	    CommandLineParser parser = new GnuParser();
	    
	    try {
	        // Create logger
	        CommandLine line = parser.parse( options, args );

	        if(line.hasOption("help")) {
		        HelpFormatter formatter = new HelpFormatter();
		        formatter.printHelp( "check_scenario", options );
	        	System.exit(255);
	        }
	        
	        String scenarioFilePath = line.getOptionValue("scenariofile");
	       this.logEvent("Application", "Load scenario from : " + scenarioFilePath);
		    ScenarioCaller caller = new ScenarioCaller(scenarioFilePath, "UTF-8");
		    if(line.hasOption("outputdir")) {
		    	caller.setOutputDir(line.getOptionValue("outputdir"));
		    }
		    int returnCode = caller.launchTest();
		    System.exit(returnCode);
	        
	    }
	    catch( ParseException exp ) {
	        // oops, something went wrong
	        System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );

	        HelpFormatter formatter = new HelpFormatter();
	        formatter.printHelp( "check_scenario", options );
	        System.exit(255);
	    }

	}

	private void logEvent(String string, String string2) {
		logger.info(string + " : " + string2);
	}
}
