/**
 * 
 */
package temp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author vishal.parashar
 *
 */
public class Parenthises {
	
	public static void main(String[] args) throws IOException {
		 BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		 String s=br.readLine();
	     boolean result=check(s);
	     System.out.println(result);
	}
	public static boolean check(String s) {
	    int counter = 0;
	    for (int i = 0; i < s.length(); i++) {
	        if (s.charAt(i) == '(') {
	            counter++;
	        } else if (s.charAt(i) == ')') {
	            if (counter == 0) {
	                return false;
	            }
	            counter--;
	        }
	    }
	    return counter == 0;
	}
	

}
