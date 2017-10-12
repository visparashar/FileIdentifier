package blueoptima;

import org.jsoup.nodes.Document;

/**
 * Created by shivek on 4/4/17.
 */
public interface IWebPageParser {

    void parse(Document doc ,String filename) throws ParsingException;

}
