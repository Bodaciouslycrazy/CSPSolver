import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

//Bodie Malik
//3-3-2019


public class Main {

	Dictionary<String, String> bleeh = new Hashtable<String, String>();
	
	public static void main(String[] args)
	{
		System.out.println("Remember, only 30 lines to stdout");
		//Make an assignment dictionary that is empty
		try 
		{
			CSP problem = new CSP(args[0],args[1]);
			
			Assignment emptyAssignment = new Assignment();
			Assignment finalAssignment = RecursiveBacktracking(emptyAssignment, problem);
			
			if(finalAssignment != null)
				PrintAssignment(finalAssignment, true);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		
		
	}
	
	private static Assignment RecursiveBacktracking(Assignment assignment, CSP problem)
	{
		if(problem.IsAssignmentComplete(assignment))
		{
			return assignment;
		}
		
		//Debug
		String assigningVariable = problem.SelectVariable(assignment);
		ArrayList<Integer> possibleValues = problem.OrderDomainValues(assigningVariable, assignment);
		
		System.out.println("Assigning variable: " + assigningVariable + ", possible values: " + possibleValues.size());
		
		for(int value : possibleValues)
		{
			assignment.put(assigningVariable, value);
			if(problem.IsAssignmentValid(assignment))
			{
				Assignment result = RecursiveBacktracking(assignment, problem);
				if(result != null)
					return result;
			}
			else
			{
				PrintAssignment(assignment, false);
			}
			assignment.remove(assigningVariable);
		}
		
		//For none of these values can we find a sucecessfull assignment.
		//Return failure (null).
		return null;
	}
	
	public static void PrintAssignment(Assignment assignment, boolean success)
	{
		if(success)
			System.out.println(assignment.toString() + "solution");
		else
			System.out.println(assignment.toString() + "failure");
	}
}
