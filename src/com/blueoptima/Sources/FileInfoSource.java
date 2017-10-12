package blueoptima.Sources;

import blueoptima.ATypeResolverTask;
import blueoptima.AWebPageParser;
import blueoptima.FileName;
import blueoptima.ParsingException;
import com.google.common.base.Joiner;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by shivek on 8/4/17.
 */
public class FileInfoSource extends ATypeResolverTask {

    private static String baseURL = "https://fileinfo.com/extension/%s";


    public FileInfoSource(FileName fileName) {
        super(baseURL, new FileInfoParser(), fileName);
    }

}

class FileInfoParser extends AWebPageParser {
    @Override
    public void parse(Document doc, String filename) throws ParsingException {
        try {
            Elements type = doc.getElementsByClass("ext");
            for (int a = 0; a < type.size(); a++) {
                Elements elements = type.get(a).getElementsByClass("headerInfo");
                for (Element element : elements) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("File Name", filename);
                    if (a > 0) {
                        map.put("Name", doc.getElementsByClass("fileType").get(a).nextSibling().toString());
                    } else map.put("Name", doc.getElementsByClass("fileType").get(a).nextElementSibling().text());
                    LoadLocalLanguagesData.fillMapwithLanguageDetails(map);
                    String description = Joiner.on(" ").join(type.get(a).getElementsByClass("infoBox").stream().filter(new Predicate<Element>() {
                        @Override
                        public boolean test(Element element) {
                            return element.getElementsByClass("programs").size() == 0;

                        }
                    }).map(new Function<Element, String>() {
                        @Override
                        public String apply(Element element) {
                            return element.text();
                        }
                    }).collect(Collectors.toList()));
                    map.put("Description", description);
                    for (Element tr : element.getElementsByTag("tr")) {
                        if (tr.getElementsByTag("td").get(1).getElementsByTag("p").size() != 0) {
                            map.put(tr.getElementsByTag("td").get(0).text(), tr.getElementsByTag("td").get(1).getElementsByTag("p").get(0).text());
                        } else
                            map.put(tr.getElementsByTag("td").get(0).text(), tr.getElementsByTag("td").get(1).text());
                    }
                    if(CommonFileNames.containsFileDetails(filename.toUpperCase())){
                        map.put("Usual usage",CommonFileNames.getFiledetails(filename.toUpperCase()));
                    }
                    getResult().add(map);
                }
            }
        }catch(Exception e){
            throw  new ParsingException("Parsing Error");
        }
    }
}
