import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.parser.ParseException;

import csvHandling.CsvOutputCreator;
import csvHandling.csvFieldExtractor;
import freebaseQueryExecutor.FreeBaseObjectFieldsFinder;
import freebaseQueryExecutor.TopicInfoRetriever;
import freebaseQueryExecutor.TopicPropertiesFinder;


public class launchExecution {


	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException{

		// percentage initializer
		double percCounter = 0;
		String oldPerc = "";
		PercHandler perc = new PercHandler();

		FreeBaseObjectFieldsFinder finder = new FreeBaseObjectFieldsFinder();
		TopicPropertiesFinder propertiesFinder = new TopicPropertiesFinder();

		CsvOutputCreator outputCSVwriter = new CsvOutputCreator();
		csvFieldExtractor fieldExtractor = new csvFieldExtractor();
		Map<String, String> categories = new HashMap<String, String>();


		//initialize and run csv fields retriever for lastfm and ign dataset
		List<String> artistsName = new LinkedList<String>();
		artistsName = fieldExtractor.retrieveFieldFromCSV("lastFm.csv");

		List<String> gamesList = new LinkedList<String>();
		gamesList = fieldExtractor.retrieveFieldFromCSV("ign.csv");


		int listSize = artistsName.size()+gamesList.size();


		//start freebase fields extraction for last.fm dataset
		for(String artist: artistsName){

			//percentage stuff
			percCounter+=1.0;
			oldPerc = perc.printPerc(oldPerc, percCounter, listSize);

			//find category for main field with MQL API
			List<String> categoryList = finder.findFreeBaseCategory(1,artist);

			if(!categoryList.isEmpty()){

				//pick the first category and retrieve properties for the category
				String category = categoryList.get(0);
				categories = propertiesFinder.TopicCategoriesFinder(category);

				String id =finder.TopicIdFinder(artist, category);   //find id for topic API

				//retrieve fields and fill properties with topic API and write to csv.
				outputCSVwriter.csvOutputWrite(TopicInfoRetriever.retrieveTopicInfo(artist, id, categories));
			}
		}

		//start freebase fields extraction for ign dataset
		for(String game: gamesList){
			percCounter+=1.0;
			oldPerc = perc.printPerc(oldPerc, percCounter, listSize);

			List<String> categoryList = finder.findFreeBaseCategory(1,game);

			if(!categoryList.isEmpty()){
				String category = categoryList.get(0);
				categories = propertiesFinder.TopicCategoriesFinder(category);

				String id =finder.TopicIdFinder(game, category);


				outputCSVwriter.csvOutputWrite(TopicInfoRetriever.retrieveTopicInfo(game, id, categories));
			}
		}







	}

}
