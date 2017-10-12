package blueoptima.Sources;

import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by shivek on 7/4/17.
 */
@Deprecated
public class ImportLanguages {
    String url = "https://en.wikipedia.org/wiki/List_of_programming_languages";
    List<String> urls = new ArrayList<>();
    List<String> extensions = new ArrayList<>();


    public ImportLanguages() throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements h2 = doc.getElementsByTag("li");
        for (Element h : h2) {
            if (h.children().size() > 0) {
                String str = h.child(0).attr("href");
                if (str.contains("wiki") && !str.contains("https") && h.child(0).hasAttr("title")) urls.add(str);
            }
        }
        List<HashMap<String, String>> result = urls.parallelStream().parallel().distinct().map(new Function<String, HashMap<String, String>>() {
            String baseURL = "https://en.wikipedia.org";

            @Override
            public HashMap<String, String> apply(String s) {
                HashMap<String, String> hash = new HashMap<String, String>();
                try {
                    Document doc = Jsoup.connect(baseURL + s).get();
                    Elements ele = doc.getElementsByClass("infobox");
                    Elements names = doc.getElementsByClass("summary");
                    if (names.size() > 0) {
                        hash.put("Name", names.get(0).text().toUpperCase());
                        if (ele.size() > 0) {
                            Element e = ele.get(0);
                            Elements ele1 = e.getElementsByTag("tr");
                            boolean cont = false;
                            Elements th=null,td=null;
                            for (Element tr : ele1) {
                                if(cont){
                                    cont = false;
                                    td = tr.getElementsByTag("td");
                                }
                                else if(tr.children().size()==1){
                                    cont= true;
                                    th = tr.getElementsByTag("th");
                                    continue;
                                }
                                else {
                                    th = tr.getElementsByTag("th");
                                    td = tr.getElementsByTag("td");
                                }
                                if (th.text() != null && th.text().length()!=0) {
                                    if (th.text().toLowerCase().contains("extension")) {
                                         hash.put("EXTENSION",td.text());
                                        extensions.addAll(Arrays.asList(td.text().split(",")));
                                        
                                    } else {
                                        hash.put(th.text(), td.text());
                                    }
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage() + baseURL + s );
                }
                return hash;
            }
        }).

                filter(new Predicate<HashMap<String, String>>() {
                    @Override
                    public boolean test(HashMap<String, String> stringStringHashMap) {
                        if (stringStringHashMap.size() == 0) {
                            return false;
                        }
                        if (!stringStringHashMap.containsKey("Name")) {
                            return false;
                        }
                        return true;
                    }
                }).collect(Collectors.toList());

        HashMap<String,HashMap<String,String>> map = new HashMap<>();
        for(HashMap entry : result){
            map.put((String) entry.get("Name"),entry);
        }
        String str = new Gson().toJson(map);
        System.out.println(str);
    }
}