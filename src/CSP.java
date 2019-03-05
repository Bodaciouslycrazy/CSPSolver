import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class CSP {

	Hashtable<String, ArrayList<Integer>> VarDomain;
	ArrayList<Constraint> Constraints;
	
	public CSP(String varFileName, String constFileName) throws IOException
	{
		VarDomain = new Hashtable<String, ArrayList<Integer>>();
		
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
			ArrayList<Integer> values = new ArrayList<Integer>();
			for(int i = 0; i < stringValues.length; i++)
			{
				values.add(Integer.parseInt(stringValues[i]));
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
	
	
	//*****Select Variable*****
	//Finds the best variable to assign a value to.
	public String SelectVariable(Assignment assigned)
	{
		ArrayList<String> mostConstrained = new ArrayList<String>();
		int validValueCount = 99999;
		for(Entry<String, ArrayList<Integer>> entry : VarDomain.entrySet())
		{
			//Skip if this has already been assigned.
			if(assigned.containsKey(entry.getKey()))
				continue;
			
			if(entry.getValue().size() < validValueCount)
			{
				validValueCount = entry.getValue().size();
				mostConstrained.clear();
			}
			
			if(entry.getValue().size() == validValueCount)
			{
				mostConstrained.add(entry.getKey());
			}
		}
		
		//if there is only one element in mostConstrained, we don't need to tie break
		if(mostConstrained.size() == 1)
			return mostConstrained.get(0);
		
		//Tie break by finding most constraining variables
		ArrayList<String> mostConstraining = new ArrayList<String>();
		int constrainingValueCount = 99999;
		for(String var : mostConstrained)
		{
			int constrainValue = GetConstrainingValue(var, assigned);
			if(constrainValue < constrainingValueCount)
			{
				constrainingValueCount = constrainValue;
				mostConstraining.clear();
			}
			
			if(constrainValue == constrainingValueCount)
				mostConstraining.add(var);
		}
		
		//If there is only one element in the list, no more tie break
		if(mostConstraining.size() == 1)
			return mostConstraining.get(0);
		
		
		//Otherwise, break ties alphabetically
		String minLetter = "Z";
		for(String var : mostConstraining)
		{
			if(minLetter.compareTo(var) > 0)
				minLetter = var;
		}
		
		//No more tie breaking needed
		return minLetter;
	}
	
	
	
	//Returns the possible values for a variable in the domain.
	public ArrayList<Integer> OrderDomainValues(String variable)
	{
		System.out.println("Domain values not ordered correctly...");
		return VarDomain.get(variable);
	}
	
	
	//Returns an int that describes how much a variable constrains 
	private int GetConstrainingValue(String var, Assignment assigned)
	{
		int unassignedConstraints = 0;
		for(Constraint con : Constraints)
		{
			if(con.ContainsKey(var))
			{
				String ckey = con.GetOtherKey(var);
				if(!assigned.containsKey(ckey))
					unassignedConstraints++;
			}
		}
		
		return unassignedConstraints;
	}
	
	
	
	public boolean IsAssignmentComplete(Assignment assignment)
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
	
	
	public boolean IsAssignmentValid(Assignment assignment)
	{
		for(Constraint con : Constraints)
		{
			if(!con.CheckConstraint(assignment))
				return false;
		}
		
		return true;
	}
	
	public boolean CanAddAssignment(String variable, int value, Assignment assigned) 
	{
		for(Constraint con : Constraints)
		{
			if(con.ContainsKey(variable))
			{
				String ckey = con.GetOtherKey(variable);
				if(assigned.containsKey(ckey))
				{
					if(!con.CheckConstraint(variable,value,ckey,assigned.get(ckey).intValue()))
						return false;
				}
			}
		}
		
		//None of the constraints failed, so return true.
		return true;
	}
	
	
	/// FOR DEBUG ONLY
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		//Build Domain Lines
		for( Entry<String, ArrayList<Integer>> var : VarDomain.entrySet())
		{
			sb.append(var.getKey()).append(":");
			
			for(Integer val : var.getValue())
			{
				sb.append(" ").append(val);
			}
			sb.append("\n");
		}
		
		sb.append("\n");
		
		//Build constraint lines
		for(Constraint cons : Constraints)
		{
			sb.append(cons.toString()).append("\n");
		}
		
		return sb.toString();
	}


}
