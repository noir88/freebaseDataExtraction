package csvHandling;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class CsvOutputCreator {

	private static final String PATH = "outputCSV/";

	public CsvOutputCreator() {
		// TODO Auto-generated constructor stub
	}

	public void csvOutputWrite(Map<String, String[]> input){

		FileWriter fw;
		try {
			fw = new FileWriter(PATH+"output.csv",true);
			PrintWriter out = new PrintWriter(fw);

			//for each pair put in the csv the couple categoyname : value
			for(Map.Entry<String, String[]> entry: input.entrySet()){
				out.print(entry.getValue()[0]+": "+entry.getValue()[1]);
				out.print(";");
			}


			out.println("");
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}



}
