/**
 * 
 */
package blueoptima.AttribMaster;

/**
 * @author vishal.parashar
 *
 */
public enum Source{
		FILEINFO ("fileinfo"),
		FILEEXTENSION("fileextension"),
		FILEEXT("fileext"),
		FILESUFFIX("filesuffix");
		
		private final String value;
		
		private Source(String i)
		{
			this.value =i;
		}
		private String getValue()
		{
			return this.value;
		}
	}
	
//	Sources source = new Sources();
	
	

