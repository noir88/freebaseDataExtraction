
public class PercHandler {

	
	public String printPerc(String oldPerc, double count, int listSize){
		
		String newPerc = " Progress: "+String.format("%.2f",(count*100/listSize))+"%";
		if(!newPerc.equals(oldPerc))
			System.out.println(newPerc);
		
		return newPerc;
		
	}
}
