package blueoptima;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by shivek on 11/4/17.
 */
public class Common {

    public static Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .enableComplexMapKeySerialization()
            .create();



}
