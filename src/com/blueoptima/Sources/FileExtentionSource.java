package blueoptima.Sources;

import blueoptima.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by shivek on 5/4/17.
 */
public class FileExtentionSource extends ATypeResolverTask {

    private final static String baseURL = "https://www.file-extensions.org/%s-file-extension";
    private final static String searchURL = "https://www.file-extensions.org/search/?searchstring=%s&searchtype=1000";
    private List<String> listOfUrls = new ArrayList<String>();
    private int index = 0;

    public FileExtentionSource(FileName fileName) {
        super(baseURL, new FileExtentionParser(), fileName);
    }

    @Override
    protected void connect() throws ConnectionException {
        if (index == 0) fillListOfUrls();
        super.connect();
    }

    private void fillListOfUrls() {
        try {
            String base = "https://www.file-extensions.org";
            Document doc = Jsoup.connect(String.format(searchURL, getFileName().getExt())).get();
            Elements elements = doc.getElementsByTag("tclear" +
                    "body").get(0).getElementsByAttribute("href");
            for (int a = 0; a < elements.size(); a++) {
                if (a > 1 && elements.get(a).getElementsByTag("strong").text().equals(getFileName().getExt().toLowerCase())) {
                    listOfUrls.add(base + elements.get(a).attr("href"));
                }
            }
        } catch (IOException e) {
            System.out.println("Cannot fetch list details");
        }
    }

    @Override
    protected String getUrl() {
        if (listOfUrls.size() == 0) {
            return super.getUrl();
        }
        return listOfUrls.get(index++);
    }

    @Override
    protected boolean isfinished() {
        if (index == listOfUrls.size()) {
            return true;
        } else return false;
    }
}

class FileExtentionParser extends AWebPageParser {
    @Override
    public synchronized void parse(Document doc, String filename) throws ParsingException {
        try {
            HashMap<String, String> map = new HashMap<>();
            Elements elements = doc.getElementsByClass("smselected");
            map.put("Category", elements.stream().map(Element::text).collect(Collectors.joining(",")));
            Element element = doc.getElementsByClass("b335mid").get(0).child(0).child(0);
            map.put("Description", element.text());
            elements = doc.getElementsByAttributeValue("itemtype", "name");
            map.put("Name", elements.get(0).text());
            LoadLocalLanguagesData.fillMapwithLanguageDetails(map);
            if (CommonFileNames.containsFileDetails(filename.toUpperCase())) {
                map.put("Usual usage", CommonFileNames.getFiledetails(filename.toUpperCase()));
            }
            getResult().add(map);
        } catch (Exception e) {
            throw new ParsingException("Parsing error");
        }
    }

}
