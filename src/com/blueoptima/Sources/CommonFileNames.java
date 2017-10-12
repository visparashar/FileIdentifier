package blueoptima.Sources;

import blueoptima.Common;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by shivek on 11/4/17.
 */
public class CommonFileNames {

    private static HashMap<String, String> map = null;

    private CommonFileNames() {}

    public static HashMap<String,String> getCommonFileMap() {
        if (map == null) {
            try {
                BufferedReader br = new BufferedReader(new FileReader("CommonFileNames.json"));
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

    public static boolean  containsFileDetails(String filename){
        return (getCommonFileMap().containsKey(filename));

    }

    public static String getFiledetails(String filename){
        return getCommonFileMap().get(filename);
    }






}
