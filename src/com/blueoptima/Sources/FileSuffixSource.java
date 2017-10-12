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
public class FileSuffixSource  extends ATypeResolverTask{

	/**
	 * @param baseURL
	 * @param parser
	 * @param fileName
	 */
	private static String baseURL = "https://www.filesuffix.com/en/extension/%s";


	public FileSuffixSource(FileName fileName){
		super(baseURL,new FileSuffixParser(),fileName);
	}
}

class FileSuffixParser extends AWebPageParser{

	/* (non-Javadoc)
	 * @see blueoptima.IWebPageParser#parse(org.jsoup.nodes.Document, java.lang.String)
	 */
	@Override
	public void parse(Document doc, String filename) throws ParsingException {
		try {
			Elements type = doc.getElementsByClass("exttab");
//			System.out.println(type);
			for (int a = 0; a < type.size(); a++) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("File Name", filename);
				map.put("Name", doc.getElementById("ext"+a).text().toString());
				LoadLocalLanguagesData.fillMapwithLanguageDetails(map);
				Elements elements = type.get(a).getElementsByClass("el");
				for(Element element :elements){
					if(element!=null&& element.hasText())
					{
						String keyValue=element.text();
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
			}
		}catch(Exception e){
			throw  new ParsingException("Parsing Error");
		}
	}

}

