package freebaseQueryExecutor;
import java.util.ArrayList;


public class JsonDataResultExtractor {

	public JsonDataResultExtractor() {
	}
	public  ArrayList<String> extractTypeFromJson(String json, int parameter){
			
			
			ArrayList<String> result = new ArrayList<String>();
			int count = 0;
			
			
			//parse and edit the output to retrieve the correct field 
			json = json.replace("\\", "");
			json = json.replace("[", "");
			json = json.replace("]", "");
			json = json.replace("\"", "");
			
			String[] pieces = json.split(",");
			
			while(count < parameter){
				result.add(pieces[count]);
				count++;
			
			}
			
			
			return result;
				}
			
			
			
			
	}
	
	
	
	

