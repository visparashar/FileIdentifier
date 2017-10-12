package blueoptima;

import java.util.HashMap;
import java.util.List;

/**
 * Created by shivek on 3/4/17.
 */
public interface ITypeResolver{

    void execute();

    List<HashMap<String, String>> fetchResult();

}
