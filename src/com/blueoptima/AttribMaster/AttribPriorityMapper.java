/**
 * 
 */
package blueoptima.AttribMaster;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import blueoptima.Common;

/**
 * @author vishal.parashar
 *
 */
/**
 * @author vishal.parashar
 *
 */
public class AttribPriorityMapper {
	
	  private static Map<String, ArrayList<String>> map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	
	  public static TreeMap<String, ArrayList<String>> getAttribMaster() {
	       
	            try {
	                BufferedReader br = new BufferedReader(new FileReader("AttribPriority.json"));
	                StringBuilder str = new StringBuilder("");
	                String line = null;
	                while ((line = br.readLine()) != null) {
	                    str.append(line);
	                }
	                map = Common.gson.fromJson(str.toString(), TreeMap.class);
	            } catch (FileNotFoundException e) {
	                e.printStackTrace();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        return (TreeMap<String, ArrayList<String>>) map;
	    }
	  
	  public static ArrayList<String> getDefaultAttribPriority(List<Source> executedSources){
		  List<String> keyPrior = new ArrayList<>();
		  for(int i=0;i<executedSources.size();i++)
		  {
			  keyPrior.add(executedSources.get(i).name());
		  }
		  return (ArrayList<String>) keyPrior;
	  }
	  public static ArrayList<String> getPriorityOf(String key){
		  return map.get(key);
	  }
	  
	  public static void main(String[] args) {
		System.out.println(getAttribMaster().toString());
	}
	
	
	

}
