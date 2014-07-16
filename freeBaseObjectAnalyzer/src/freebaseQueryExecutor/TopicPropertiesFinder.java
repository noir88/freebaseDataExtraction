package freebaseQueryExecutor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.jayway.jsonpath.JsonPath;


public class TopicPropertiesFinder {
	public static Properties properties = new Properties();

	/*This method, given the exact name of the category we found, retrieves all the properties defined for the category. For example,
	 * if we found our field to be an artist name, we've found the category /music/artist, and this method tells us artist have the properties albums
	 * concerts and so on. We then will make a topic API query to fill these fields.
	 */
	public Map<String, String> TopicCategoriesFinder(String category) throws FileNotFoundException, IOException, ParseException{
		String id="";
		String notable="";
		Map<String, String> categories = new HashMap<String, String>();

		properties.load(new FileInputStream("config/freebase.properties"));
		HttpTransport httpTransport = new NetHttpTransport();
		HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
		JSONParser parser = new JSONParser();

		String query1 = "[{"
				+ "\"type\":\"/type/property\","
				+ "\"schema\": {"
				+"\"id\": \""+category+"\""
				+" }, "
				+" \"id\": null,"
				+" \"name\": null"
				+ "}]";


		GenericUrl url = new GenericUrl("https://www.googleapis.com/freebase/v1/mqlread");
		url.put("query", query1);
		url.put("key", properties.get("API_KEY"));
		HttpRequest request = requestFactory.buildGetRequest(url);
		HttpResponse httpResponse = request.execute();
		JSONObject response = (JSONObject)parser.parse(httpResponse.parseAsString());

		JSONArray results = (JSONArray)response.get("result");
		if(!results.isEmpty()){
			for(int i = 0; i < results.size(); i++){
				id = JsonPath.read(results.get(i),"$.id").toString();
				notable = JsonPath.read(results.get(i),"$.name").toString();
				categories.put(id, notable);
			}

		}
		return categories;
	}

}
