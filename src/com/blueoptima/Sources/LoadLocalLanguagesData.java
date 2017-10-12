package blueoptima.Sources;

import blueoptima.Common;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by shivek on 8/4/17.
 */
public class LoadLocalLanguagesData {
    private static HashMap<String, HashMap<String, String>> map = null;

    private LoadLocalLanguagesData() {}

    public static HashMap<String, HashMap<String, String>> getLanguageMap() {
        if (map == null) {
            try {
                BufferedReader br = new BufferedReader(new FileReader("languageData.json"));
                StringBuilder str = new StringBuilder("");
                String line = null;
                while ((line = br.readLine()) != null) {
                    str.append(line);
                }
                map = Common.gson.fromJson(str.toString(), HashMap.class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public static synchronized HashMap<String, String> getDetailsAboutLanguage(String name) {
        List<String> names = new ArrayList<>();
        names = getNamesTokens(name);
        if (names.size() == 0) return null;
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        for (String str : names) {

            if (getLanguageMap().containsKey(str.toUpperCase()))
                list.add(new HashMap<String, String>(getLanguageMap().get(str.toUpperCase())));

        }
        if (list.size() == 0) {
            return null;
        }
        if (list.size() == 1) {
            return list.get(0);
        }
        HashMap<String, String> map = new HashMap<>();
        StringBuilder str = new StringBuilder("");
        for (int a = 0; a < list.size(); a++) {
            if (list.get(a).containsKey("Name"))
                str.append(list.get(a).get("Name") + "/");
        }
        map.put("Language Family(s)", str.toString());
        return map;
    }

    private static List<String> getNamesTokens(String name) {
        StringTokenizer stringtokenizer = new StringTokenizer(name, "/ ");
        List<String> names = new ArrayList<>();
        while (stringtokenizer.hasMoreElements()) {
            names.add(stringtokenizer.nextToken());
        }
        return names;
    }

    public static void fillMapwithLanguageDetails(HashMap<String,String> map){
        try {
            HashMap<String, String> languageDetials = LoadLocalLanguagesData.getDetailsAboutLanguage(map.get("Name"));
            for (Map.Entry<String, String> entry : languageDetials.entrySet()) {
                if (entry.getKey().equals("Name")) {
                    map.put("Language Family", entry.getValue());
                } else {
                    map.put(entry.getKey(), entry.getValue());
                }
            }
        } catch (Exception e) {

        }
    }
}




