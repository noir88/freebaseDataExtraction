package csvHandling;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class csvFieldExtractor {

	private static final String PATH = "inputCSV/";

	public List<String> retrieveFieldFromCSV(String csvName) throws FileNotFoundException{

		List<String> fieldsList = new LinkedList<String>();

		//each scan next selects a different entry
		Scanner sc = new Scanner(new File(PATH+csvName));
		sc.useDelimiter("\n");

		//don't consider main titles
		sc.next();


		while(sc.hasNext()){
			String row = sc.next();
			String[] splitted = row.split(";");

			//retrieve and add to the list only the main field
			String field = splitted[1];
			fieldsList.add(field);
		}
		sc.close();


		return fieldsList;

	}

}

