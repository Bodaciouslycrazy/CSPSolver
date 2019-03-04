import java.io.*;
import java.util.*;

public class CSP {

	Hashtable<String, Integer[]> VarDomain;
	ArrayList<Constraint> Constraints;
	
	public void CSP(String varFileName, String constFileName) throws IOException
	{
		VarDomain = new Hashtable<String, Integer[]>();
		
		//********************************************************
		//				READ VARIABLE DOMAIN FILE
		//********************************************************
		File varFile = new File(varFileName);
		
		BufferedReader reader = new BufferedReader(new FileReader(varFile));
		String line;
		while((line = reader.readLine()) != null)
		{
			//Separate the variable name from the values
			line = line.trim();
			String[] split1 = line.split(":");
			String varName = split1[0];
			
			if(VarDomain.containsKey(varName))
				throw new IOException("KEY ALREADY EXISTS");
			
			
			//Separate the values from eachother
			split1[1] = split1[1].trim();
			String[] stringValues = split1[1].split(" ");
			
			//Convert values to Integers
			Integer[] values = new Integer[stringValues.length];
			for(int i = 0; i < stringValues.length; i++)
			{
				values[i] = Integer.parseInt(stringValues[i]);
			}
			
			//Add data to VarDomain hashtable
			VarDomain.put(varName, values);
		}
		
		//********************************************************
		//				READ CONSTRAINT FILE
		//********************************************************
		Constraints = new ArrayList<Constraint>();
		
		File constFile = new File(constFileName);
		
		reader = new BufferedReader(new FileReader(constFile));
		while((line = reader.readLine()) != null)
		{
			//split the string
			String[] split = line.split(" ");
			
			//Create the constraint
			Constraints.add( new Constraint(split[0], split[1], split[2]));
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
	
	public String ToString()
	{
		StringBuilder sb = new StringBuilder();
		
		for(Constraint cons : Constraints)
		{
			sb.append(cons.ToString()).append("\n");
		}
		
		return sb.toString();
	}
}
