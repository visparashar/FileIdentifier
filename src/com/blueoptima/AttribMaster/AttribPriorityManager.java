/**
 * 
 */
package blueoptima.AttribMaster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author vishal.parashar
 *
 */
public class AttribPriorityManager {

	public List<HashMap<String,String>> mergeFinalResult(List<HashMap<String,String>> finalResult ,List<Source> executedSources)
	{
		List<HashMap<String,String>> result =new ArrayList<>();
		if(finalResult!=null && !finalResult.isEmpty())
		{
			for(int i=0 ;i<finalResult.size();i++)
			{
				if(i==0){
					result.add(finalResult.get(0));
					continue;
				}
				HashMap<String,String> mapObj =finalResult.get(i);
				Source sourceName =executedSources.get(i);
				Set<String> keys = mapObj.keySet();
				for(String key :keys){
					if(isOverrideReq(key,executedSources ,i))
					{
						result.get(0).put(key, mapObj.get(key));
					}
				}
			}
		}
		return result;
	}

	public boolean isOverrideReq(String key, List<Source> executedSources ,int  index)
	{
		TreeMap<String, ArrayList<String>> attribMap =AttribPriorityMapper.getAttribMaster();
		if(attribMap.containsKey(key))
		{
			ArrayList<String> priority =AttribPriorityMapper.getPriorityOf(key);
			//			first check the index of current source in priority if greater 
			for(int i=0;i<priority.size();i++)
			{
				if(priority.get(i).equalsIgnoreCase(executedSources.get(index).name()))
				{
					for(int k=0;k<i;k++){
						if(priority.get(k).equalsIgnoreCase(executedSources.get(index-1).name())){
							return false;
						}
					}
				}
			}
			//			
		}else{
			attribMap.put(key, AttribPriorityMapper.getDefaultAttribPriority(executedSources));

		}
		return true;
	}

}
