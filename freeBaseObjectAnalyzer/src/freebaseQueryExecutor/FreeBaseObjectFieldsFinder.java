package freebaseQueryExecutor;
import java.util.LinkedList;
import java.util.List;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.jayway.jsonpath.JsonPath;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



public class FreeBaseObjectFieldsFinder {

	public static Properties properties = new Properties();

	
	/*This method makes an MQL query to freebase to retrieve the most significative categories for the search Terms and returns them */
	public List<String> findFreeBaseCategory(int nOfResults, String termToCategorize){
		List<String> categories = new LinkedList<String>();


		try {

			properties.load(new FileInputStream("config/freebase.properties"));
			HttpTransport httpTransport = new NetHttpTransport();
			HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
			JSONParser parser = new JSONParser();
			String query = "[{"									//this query ask for the type of the result, given the terms to search
					+ "\"name\":\""+termToCategorize+"\","
					+ "\"type\":[]"
					+ "}]";

			JsonDataResultExtractor categoryExtractor = new JsonDataResultExtractor();


			GenericUrl url = new GenericUrl("https://www.googleapis.com/freebase/v1/mqlread");
			url.put("query", query);
			url.put("key", properties.get("API_KEY"));
			HttpRequest request = requestFactory.buildGetRequest(url);
			HttpResponse httpResponse = request.execute();
			JSONObject response = (JSONObject)parser.parse(httpResponse.parseAsString());


			//if the query result is not empty, retrieve the category with JSONPath

			JSONArray results = (JSONArray)response.get("result");
			if(!results.isEmpty()){

				String type = JsonPath.read(results.get(0),"$.type").toString();
				categories = categoryExtractor.extractTypeFromJson(type,nOfResults);

			}


		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return categories;
	}

	/*This method makes an MQL query to retrieve the unique topic ID for the given couple name, category */
	
	public String TopicIdFinder(String name, String category) throws FileNotFoundException, IOException, ParseException{
		String id="";


		properties.load(new FileInputStream("config/freebase.properties"));
		HttpTransport httpTransport = new NetHttpTransport();
		HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
		JSONParser parser = new JSONParser();

		String query1 = "[{"
				+ "\"name\":\""+name+"\","
				+ "\"type\":\""+category+"\","
				+"\"id\": null"
				+ "}]";

		GenericUrl url = new GenericUrl("https://www.googleapis.com/freebase/v1/mqlread");
		url.put("query", query1);
		url.put("key", properties.get("API_KEY"));
		HttpRequest request = requestFactory.buildGetRequest(url);
		HttpResponse httpResponse = request.execute();
		JSONObject response = (JSONObject)parser.parse(httpResponse.parseAsString());

		JSONArray results = (JSONArray)response.get("result");
		if(!results.isEmpty()){

			id = JsonPath.read(results.get(0),"$.id").toString();

		}
		return id;
	}
}

