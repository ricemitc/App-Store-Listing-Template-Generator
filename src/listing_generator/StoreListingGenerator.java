package listing_generator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import pojos.ListingTemplate;
import pojos.State;

public class StoreListingGenerator {

	private final String REPLACE_ABBREVIATION = "{st}";
	private final String REPLACE_STATE_NAME = "{state}";
	private final String CONFIGURATION_STATE_LIST = "src/Configuration/StateList.txt";
		
	
	
	public StoreListingGenerator() {
		
	}
	
	
	public void generateLotteryStoreLisitngs() {
		System.out.println("Beginning to Generate Store Listings.");
		
		// Open the StateList file and create an array of States to iterate 
		ArrayList<State> stateList = loadStates(CONFIGURATION_STATE_LIST);
		
		// Open up all the templates
		ArrayList<ListingTemplate> templateList = loadAllTemplates();		
		
		for(ListingTemplate listingTemplate : templateList) {
			
			// Open the current template file and read in all the text
			List<String> currentTemplateText = loadTemplateText(listingTemplate.getFileUrl());
			
			// For each State, generate a store listing for the current template and save that to file 
			for(State state : stateList) {
				List<String> generatedListing = generateListingForState(currentTemplateText, state);
				saveListingToFile(listingTemplate, generatedListing, state);
			}
		}
		
		System.out.println("\nFinished processing all store listing templates for each State...");
	}
	
	
	
	
	private List<String> generateListingForState(List<String> templateText, State state) {

		List<String> generatedListing = new ArrayList<String>();
		
		for(String line : templateText) {
			line = line.replace(REPLACE_ABBREVIATION, state.getAbbreviation());
			line = line.replace(REPLACE_STATE_NAME, state.getFullName());
			generatedListing.add(line);
		}
		
		return generatedListing;
	}
	
	
	
	
	private void saveListingToFile(ListingTemplate template, List<String> listing, State state) {
		
		// Generate file name based on State + template details
		String fileName = template.getName() +  " " +
				"v" + template.getVersionNumber() + 
				" " + state.getAbbreviation() + 
				" Store Listing Info.txt";
		
		String fullPath = "src/Results/" + fileName;
		System.out.println("\t\tProcessing file: " + fileName);
		
		// Write to new file
		try {
			Files.write(new File(fullPath).toPath(), listing, Charset.defaultCharset());
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private List<String> loadTemplateText(String templateUrl) {
		List<String> template = new ArrayList<String>();
		
		// Open up the Template text file
		try {

            File f = new File(templateUrl);
            BufferedReader b = new BufferedReader(new FileReader(f));
            String readLine = "";

            while ((readLine = b.readLine()) != null) {
                template.add(readLine);
            }
            
            b.close();
        } 
		catch (IOException e) {
            e.printStackTrace();
        }
		
		return template;
	}
	
	
	private ArrayList<State> loadStates(String stateFileUrl) {
		ArrayList<State> stateList = new ArrayList<State>();
		
		// Open up the StateList text file
		File file = new File(stateFileUrl);  

		// Line by line, read in and create a State
		try (Stream<String> stream = Files.lines(file.toPath())) {			
			stateList = stream
					.map(line -> new State(line))
					.collect(Collectors.toCollection(ArrayList::new));
			
			System.out.println("\tStates to Process: " + stateList.size() + ". State list loaded.");
		} 
		catch (IOException e) {
			e.printStackTrace();
		}		
		
		return stateList;
	}
	
	
	private ArrayList<ListingTemplate> loadAllTemplates() {
		
		// For now just hardcoding, dynamic load coming later
		ArrayList<ListingTemplate> templateList = new ArrayList<ListingTemplate>();
		
		ListingTemplate amazonTemplate = new ListingTemplate();
		amazonTemplate.setFileUrl("src/Templates/AmazonAppStoreTemplate.txt");
		amazonTemplate.setName("Amazon App Store");
		amazonTemplate.setVersionNumber(1);
		templateList.add(amazonTemplate);
		
		ListingTemplate playTemplate = new ListingTemplate();
		playTemplate.setFileUrl("src/Templates/GooglePlayStoreTemplate.txt");
		playTemplate.setName("Google Play Store");
		playTemplate.setVersionNumber(1);
		templateList.add(playTemplate);
				
		System.out.println("\tTemplate list loaded.");
		return templateList;
	}
	
	
}
