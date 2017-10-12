package blueoptima;

import com.google.common.base.Preconditions;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by shivek on 4/4/17.
 */
@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
public class FileName {

     private String name;
     private String ext;
     
     public String getName(){
    	 return name;
     }
     public String getExt(){
    	 return ext;
     }

    FileName(String fileName) {
        Preconditions.checkNotNull(fileName, "Invalid FileName");
        Preconditions.checkArgument(fileName.contains("."), "File name must contain extention");
        String[] str = fileName.split("\\.");
        if (str.length != 2)
            str = parseFileName(fileName);
        this.name = str[0];
//        this.ext = str[1].toUpperCase();
        this.ext = str[1].toLowerCase();
    }

    private String[] parseFileName(String fileName) {
        String[] str = new String[2];
        int pos = fileName.lastIndexOf('.');
        str[0] = fileName.substring(0, pos - 1);
        str[1] = fileName.substring(pos + 1, fileName.length() - 1);
        return str;
    }

    public String getFullFileName(){
        return getName()+"."+getExt().toLowerCase();
    }
    
}
