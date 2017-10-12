/**
 * 
 */
package blueoptima.Sources;

import java.util.HashMap;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import blueoptima.ATypeResolverTask;
import blueoptima.AWebPageParser;
import blueoptima.FileName;
import blueoptima.ParsingException;

/**
 * @author vishal.parashar
 *
 */
public class FileExtSource extends ATypeResolverTask{

	private static String baseURL = "http://filext.com/file-extension/%s";

	public FileExtSource(FileName fileName){
		super(baseURL,new FileExtParser(),fileName);
	}
}

class FileExtParser extends AWebPageParser{

	/* (non-Javadoc)
	 * @see blueoptima.IWebPageParser#parse(org.jsoup.nodes.Document, java.lang.String)
	 */
	@Override
	public void parse(Document doc, String filename) throws ParsingException {
		try {
			Element type = doc.getElementById("main");
			Element fileTypes = doc.getElementById("h-info-left-txt");
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("File Name", filename);
			LoadLocalLanguagesData.fillMapwithLanguageDetails(map);
			Elements elements =fileTypes.getElementsByTag("div");
			if(elements!=null&& elements.hasText())
			{
				for(int i=1;i<elements.size();i++)
				{
					String keyValue=elements.get(i).text();
					String[] keyValueArr =keyValue.split(":");
					if(keyValueArr.length>=2){
						map.put(keyValueArr[0], keyValueArr[1]);
					}
				}
			}
			Element ele =doc.getElementById("leftcolumn");
			Elements elem =ele.getElementsByTag("div");
			for(Element elemm : elem){
				if(!elemm.hasAttr("id")&&!elemm.hasAttr("class")){
					String keyValue=elemm.text();
					String[] keyValueArr =keyValue.split(":");
					if(keyValueArr.length>=2){
						map.put(keyValueArr[0], keyValueArr[1]);
					}
				}
			}
			if(CommonFileNames.containsFileDetails(filename.toUpperCase())){
				map.put("Usual usage",CommonFileNames.getFiledetails(filename.toUpperCase()));
			}
			getResult().add(map);
		}catch(Exception e){
			throw  new ParsingException("Parsing Error");
		}
	}
}
