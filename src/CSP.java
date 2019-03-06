import java.io.*;
import java.util.*;
import java.util.Map.Entry;


/*
 * Contains all the data and logic necessary to store/solve a ConstraintSatisfactionProblem.
 * VariableDomain is stored as a hashtable with the variable name as a key and values as an ArrayList of Integer objects.
 * Also stores an ArrayList of Constraints.
 */
public class CSP {

	Hashtable<String, ArrayList<Integer>> VarDomain;
	ArrayList<Constraint> Constraints;
	//private boolean ForwardCheckingEnabled = false;
	
	//Given .var and .con files, this constructor will automatically populate the CSP data.
	public CSP(String varFileName, String constFileName) throws Exception
	{
		/*
		if(fc.equals("fc"))
			ForwardCheckingEnabled = true;
		else if(fc.equals("none"))
			ForwardCheckingEnabled = false;
		else
			throw new Exception("NON VALID ARGUMENT: must enter either \"none\" or \"fc\".");
		*/
		
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
	
	
	
	//Constructor used for cloning.
	public CSP(Hashtable<String, ArrayList<Integer>> newDomain, ArrayList<Constraint> newConst)
	{
		Constraints = newConst;
		VarDomain = newDomain;
	}
	
	
	
	//*****Select Variable*****
	//Finds the best variable to assign a value to.
	//prioritizes most contrained -> then most contraining -> then alphabetical.
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
		int constrainingValueCount = 0;
		for(String var : mostConstrained)
		{
			int constrainValue = GetConstrainingValue(var, assigned);
			if(constrainValue > constrainingValueCount)
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
	//Prioritizes least constraining -> then lowest value
	public ArrayList<Integer> OrderDomainValues(String variable, Assignment assigned)
	{
		ArrayList<ConflictionCount> DomainConflictions = new ArrayList<ConflictionCount>();
		
		for(Integer domainValue : VarDomain.get(variable))
		{
			//find how many values this value constrains
			ConflictionCount cc = new ConflictionCount();
			cc.Value = domainValue;
			cc.Count = GetConflictionCount(variable, domainValue, assigned);
			DomainConflictions.add(cc);
		}
		
		
		//Finally, sort the list and return the variable names
		DomainConflictions.sort(new SortByConflictions());
		
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(int i = 0; i < DomainConflictions.size(); i++)
		{
			result.add(DomainConflictions.get(i).Value);
		}
		
		return result;
	}
	
	
	//Returns the number of conflictions a new var-val assignment has with a given assignment
	public int GetConflictionCount(String variable, int value, Assignment assigned )
	{
		ArrayList<String> foundConflictions = new ArrayList<String>();
		
		
		for(Constraint con : Constraints)
		{
			if(!con.ContainsKey(variable))
				continue;
			else if(assigned.containsKey(con.GetOtherKey(variable)))
				continue;
			
			String checkingVariable = con.GetOtherKey(variable);
			ArrayList<Integer> otherValues = VarDomain.get(con.GetOtherKey(variable));
			
			for(Integer otherValue : otherValues)
			{
				if(!con.CheckConstraint(variable,  value,  checkingVariable,  otherValue))
				{
					String foundConfliction = checkingVariable + ":" + otherValue;
					//Add to foundConflictions if not already there
					
					if(!foundConflictions.contains(foundConfliction))
						foundConflictions.add(foundConfliction);
				}
			}
		}
		
		return foundConflictions.size();
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
	
	
	//Returns true if the assignment accounts for every variable in the domain.
	//The assignment doesn't have to be accepted by the constraints...
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
	
	
	
	//Returns true if the assignment is accepted by all the constraints.
	public boolean IsAssignmentValid(Assignment assignment)
	{
		for(Constraint con : Constraints)
		{
			if(!con.CheckConstraint(assignment))
				return false;
		}
		
		/*
		if(ForwardCheckingEnabled)
			return ForwardCheck(assignment);
		else
			return true;
		*/
		return true;
	}
	
	
	
	public CSP clone()
	{
		Hashtable<String, ArrayList<Integer>> newDomain = (Hashtable<String, ArrayList<Integer>>)VarDomain.clone();
		
		for(Entry<String, ArrayList<Integer>> entry : newDomain.entrySet())
		{
			entry.setValue( (ArrayList<Integer>)entry.getValue().clone() );
		}
		
		
		return new CSP(newDomain, Constraints);
	}
	
	
	//Removes values from our domain based on the given assignment.
	//returns true is procedure can continue, false otherwise.
	public boolean ForwardCheck(Assignment assignment)
	{
		//System.out.println("*****BEFORE CHECK*****");
		//System.out.println(DomainToString());
		
		for(Constraint con : Constraints)
		{
			//If both variables are assigned already, skip
			//If both variables are unassigned, also skip
			if(assignment.containsKey(con.LeftHandSide) == assignment.containsKey(con.RightHandSide))
				continue;
			
			String assignedVar = con.GetAssignedKey(assignment);
			String unassignedVar = con.GetUnassignedKey(assignment);
			
			ArrayList<Integer> possibleValues = VarDomain.get(unassignedVar);
			
			for(int i = 0; i < possibleValues.size(); i++)
			{
				//If value doesn't pass constraint, remove it from domain.
				if(!con.CheckConstraint(assignedVar, assignment.get(assignedVar), unassignedVar, possibleValues.get(i)))
				{
					possibleValues.remove(i);
					i--;
					//if we removed all possible values, return false.
					//This branch may not continue.
					if(possibleValues.size() == 0)
						return false;
				}
			}
		}
		
		//System.out.println("*****AFTER CHECK*****");
		//System.out.println(DomainToString());
		
		return true;
	}
	
	
	/*
	public boolean CanAddAssignment(String variable, int value, Assignment assigned) 
	{
		for(Constraint con : Constraints)
		{
			if(con.ContainsKey(variable))
			{
				String ckey = con.GetOtherKey(variable);
				if(assigned.containsKey(ckey))
				{
					if(!con.CheckConstraint(variable,value,ckey,assigned.GetAssignedValue(ckey)))
						return false;
				}
			}
		}
		
		//None of the constraints failed, so return true.
		return true;
	}
	*/
	
	
	/// FOR DEBUG ONLY
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(DomainToString()).append("\n");
		
		//Build constraint lines
		for(Constraint cons : Constraints)
		{
			sb.append(cons.toString()).append("\n");
		}
		
		return sb.toString();
	}
	
	public String DomainToString()
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
		
		return sb.toString();
	}
	
	
}
