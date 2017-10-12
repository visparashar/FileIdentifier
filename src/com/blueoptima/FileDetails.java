package blueoptima;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.*;

/**
 * Created by shivek on 9/4/17.
 */
@ToString
public class FileDetails {
    @SerializedName("File Details")
    @Getter @Setter
    public List<HashMap<String,String>> fileDetails;
    
}
