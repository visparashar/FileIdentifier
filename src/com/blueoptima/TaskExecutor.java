package blueoptima;

import blueoptima.AttribMaster.AttribPriorityManager;
import blueoptima.AttribMaster.Source;
import blueoptima.Sources.FileExtSource;

/**
 * Created by shivek on 4/4/17.
 */

import blueoptima.Sources.FileExtentionSource;
import blueoptima.Sources.FileInfoSource;
import blueoptima.Sources.FileSuffixSource;
import blueoptima.Sources.LoadLocalLanguagesData;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TaskExecutor {
//    @Getter @Setter
    private List<FileName> fileNameList;
//    @Getter @Setter 
    private List<FileDetails> result;
//    @Getter @Setter
    private String resultAsJson;
    
    public  List<Source> executedSources;

	/**
	 * @return the fileNameList
	 */
	public List<FileName> getFileNameList() {
		return fileNameList;
	}

	/**
	 * @param fileNameList the fileNameList to set
	 */
	public void setFileNameList(List<FileName> fileNameList) {
		this.fileNameList = fileNameList;
	}

	/**
	 * @return the result
	 */
	public List<FileDetails> getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(List<FileDetails> result) {
		this.result = result;
	}

	/**
	 * @return the resultAsJson
	 */
	public String getResultAsJson() {
		return resultAsJson;
	}

	/**
	 * @param resultAsJson the resultAsJson to set
	 */
	public void setResultAsJson(String resultAsJson) {
		this.resultAsJson = resultAsJson;
	}

	/**
	 * @return the executedSources
	 */
	public  List<Source> getExecutedSources() {
		return executedSources;
	}

	/**
	 * @param executedSources the executedSources to set
	 */
	public  void setExecutedSources(List<Source> executedSources) {
		this.executedSources = executedSources;
	}

	/**
	 * @return the error
	 */
	public static List<HashMap<String, String>> getError() {
		return error;
	}

	public static void setError(List<HashMap<String, String>> error) {
		TaskExecutor.error = error;
	}

	private static List<HashMap<String, String>> error = Arrays.asList(new HashMap<String, String>());

    static {
        error.get(0).put("Error", "File details not found");
    }

    public TaskExecutor(List<FileName> fileNameList) {
        this.fileNameList = fileNameList;
        this.result = new ArrayList<FileDetails>();
        this.executedSources= new ArrayList<>();
    }

    public void execute() {
    	result = (fileNameList).parallelStream().parallel().map(new Function<FileName, FileDetails>() {
    		@Override
    		public FileDetails apply(FileName fileName) {
    			AttribPriorityManager manager = new AttribPriorityManager();
    			ATypeResolverTask source = new FileSuffixSource(fileName);
    			ATypeResolverTask source1 = new FileInfoSource(fileName);
    			ATypeResolverTask source2 = new FileExtentionSource(fileName);
    			ATypeResolverTask source3 = new FileExtSource(fileName);
    			source.execute();
    			source1.execute();
    			source2.execute();
    			source3.execute();
//              Code Extended for 2 additional Sources
    			FileDetails fileDetails = new FileDetails();
    			List<HashMap<String,String>> finalResult =null;
    			if (source.getStatus() == Status.COMPLETED || source1.getStatus()==Status.COMPLETED ||
    					source2.getStatus()==Status.COMPLETED||  source3.getStatus()==Status.COMPLETED ) {
    				finalResult = new ArrayList<>();
    				if(source.getResult()!=null && !source.getResult().isEmpty())
    				{
    					finalResult.addAll(source.getResult());
    					executedSources.add(Source.FILESUFFIX);
    				}
    				if(source1.getResult()!=null && !source1.getResult().isEmpty())
    				{	
    					finalResult.addAll(source1.getResult());
    					executedSources.add(Source.FILEINFO);
    				}
    				if(source2.getResult()!=null && !source2.getResult().isEmpty())
    				{	finalResult.addAll(source2.getResult());
    					executedSources.add(Source.FILEEXTENSION);
    				}    				
    				if(source3.getResult()!=null && !source3.getResult().isEmpty())
    				{
    					finalResult.addAll(source3.getResult());
    					executedSources.add(Source.FILEEXT);
    				}
    			}
    		if(!finalResult.isEmpty()){
    			fileDetails.setFileDetails(manager.mergeFinalResult(finalResult ,executedSources));
    		}
    		else
    			fileDetails.setFileDetails(error);
    		return fileDetails;
    		}
    	}).collect(Collectors.toList());
    	resultAsJson = Common.gson.toJson(result);
    }
}
