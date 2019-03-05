import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

//Bodie Malik
//3-3-2019


public class Main {

	Dictionary<String, String> bleeh = new Hashtable<String, String>();
	private static int BranchesVisited = 0;
	private static final int MaxBranches = 30;
	
	
	public static void main(String[] args)
	{
		try 
		{
			CSP problem = new CSP(args[0],args[1]);
			
			//Make an assignment dictionary that is empty
			Assignment emptyAssignment = new Assignment();
			Assignment finalAssignment = RecursiveBacktracking(emptyAssignment, problem);
			
			if(finalAssignment != null)
				PrintAssignment(finalAssignment, true);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	private static Assignment RecursiveBacktracking(Assignment assignment, CSP problem) throws Exception
	{
		if(problem.IsAssignmentComplete(assignment))
		{
			return assignment;
		}
		
		//This limits to 30 branches
		if(BranchesVisited >= MaxBranches)
			return null;
		else
			BranchesVisited++;

		
		String assigningVariable = problem.SelectVariable(assignment);
		ArrayList<Integer> possibleValues = problem.OrderDomainValues(assigningVariable, assignment);
	
		
		for(int value : possibleValues)
		{
			//System.out.println("Adding assignment: " + assigningVariable + " = " + value);
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
		
		//For none of these values can we find a successful assignment.
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
