package blueoptima;

import lombok.Getter;
import lombok.Setter;
import org.jsoup.nodes.Document;

import java.util.*;

/**
 * Created by shivek on 5/4/17.
 */
public abstract  class AWebPageParser implements IWebPageParser {

    @Getter @Setter
    protected List<HashMap<String,String>> result = new ArrayList<HashMap<String,String>>();

	}
