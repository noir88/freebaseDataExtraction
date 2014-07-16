package freebaseQueryExecutor;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.jayway.jsonpath.JsonPath;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class TopicInfoRetriever {

	public static Properties properties = new Properties();

	/*Given the topic ID, a list of categories and the main term, this method make a query to Topic API to search which of these
	 * categories are defined for the current entity and fills these fields. */
	
	public static Map<String, String[]> retrieveTopicInfo(String searchTerm, String topicId, Map<String,String> categoriesList) {

		Map<String, String[]> values = new HashMap<String, String[]>();

		try {

			properties.load(new FileInputStream("config/freebase.properties"));
			HttpTransport httpTransport = new NetHttpTransport();
			HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
			JSONParser parser = new JSONParser();
			GenericUrl url = new GenericUrl("https://www.googleapis.com/freebase/v1/topic" + topicId);
			url.put("key", properties.get("API_KEY"));
			HttpRequest request = requestFactory.buildGetRequest(url);
			HttpResponse httpResponse = request.execute();
			JSONObject topic = (JSONObject)parser.parse(httpResponse.parseAsString());
			
			System.out.println("--------------------------------------");
			String[] topicArray = new String[2];
			topicArray[0] = "topic ID";
			topicArray[1] = topicId;
			values.put(topicId, topicArray);
			System.out.println("TOPIC ID: "+topicId);
			
			
			String[] search = new String[2];
			search[0] = "Entity";
			search[1] = searchTerm;
			values.put("name", search);
			System.out.println("NAME: "+search[1]);
			
			if((JsonPath.read(topic,"$.property")).toString().contains("notable_types")){
				String[] notable_type = new String[2];
				notable_type[0] = "Category";
				notable_type[1] = JsonPath.read(topic,"$.property./common/topic/notable_types.values[0].text").toString();
				values.put("type", notable_type);
				System.out.println("CATEGORY: "+notable_type[1]);
			}
			
			/*for each category, if it's defined for the current topic, extract it and place in a map both the 
			 * normal category name, the notable name and the value retrieved. */
			for(Map.Entry<String, String> entry: categoriesList.entrySet()){
				String s = "$.property.";
				String matchingString = entry.getKey().replace("/","\\/");
				if((JsonPath.read(topic,s)).toString().contains(matchingString)){
					String[] couples = new String[2];

					couples[0] = entry.getValue();
					couples[1] = (JsonPath.read(topic,s+entry.getKey()+".values[*].text")).toString();
					values.put(entry.getKey(), couples);
					System.out.println(couples[0].toUpperCase()+": "+couples[1]);
				}
						
			
			}


		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return values;
	}
}