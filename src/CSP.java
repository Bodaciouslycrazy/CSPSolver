import java.io.*;
import java.util.*;

public class CSP {

	Hashtable<String, Integer[]> VarDomain;
	
	public void CSP(String varFileName, String constFileName) throws IOException
	{
		VarDomain = new Hashtable<String, Integer[]>();
		
		File varFile = new File(varFileName);
		
		BufferedReader reader = new BufferedReader(new FileReader(varFile));
		String line;
		while((line = reader.readLine()) != null)
		{
			line = line.trim();
			
			
		}
	}
	
	public boolean IsAssignmentComplete(Hashtable assignment)
	{
		Enumeration<String> keys = VarDomain.keys();
		while(keys.hasMoreElements())
		{
			String var = keys.nextElement();
			if(!assignment.containsKey(var))
				return false;
		}
		
		return true;
	}
	
}
