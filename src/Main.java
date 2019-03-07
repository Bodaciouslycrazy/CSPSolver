import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

//Bodie Malik
//3-3-2019


public class Main {

	private static int BranchesVisited = 0;
	private static final int MaxBranches = 30;
	private static boolean ForwardCheckingEnabled = false;
	
	public static void main(String[] args)
	{
		try 
		{
			switch(args[2])
			{
			case "none": ForwardCheckingEnabled = false;
				break;
			case "fc": ForwardCheckingEnabled = true;
				break;
			default:
				throw new Exception("Argument not recognized: must be either \"none\" or \"fc\".");
			}
			
			CSP problem = new CSP(args[0],args[1]);
			
			//Make an assignment dictionary that is empty
			Assignment emptyAssignment = new Assignment();
			Assignment finalAssignment = RecursiveBacktracking(emptyAssignment, problem);
			
			//print final assignment if it wasn't a failure
			if(finalAssignment != null)
				PrintAssignment(finalAssignment, true);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	
	
	//Main recursive backtracking algorithm.
	//Parameters:
	//	assignment - specifies which variables have been assigned what values.
	//	problem - contains the problem domain and constraints.
	//Returns:
	//	Assignment - final accepted assignment (null if failure)
	private static Assignment RecursiveBacktracking(Assignment assignment, CSP problem) throws Exception
	{
		if(problem.IsAssignmentComplete(assignment))
		{
			return assignment;
		}
		
		
		//This limits to 30 branches. returns failure
		if(BranchesVisited >= MaxBranches)
			return null;

		
		String assigningVariable = problem.SelectVariable(assignment);
		ArrayList<Integer> possibleValues = problem.OrderDomainValues(assigningVariable, assignment);
	
		
		//Attempt to assign every possible value
		for(int value : possibleValues)
		{
			//System.out.println("Adding assignment: " + assigningVariable + " = " + value);
			assignment.put(assigningVariable, value);
			
			//Default values for no forward checking
			CSP nextProblem = problem;
			boolean fcPass = true;
			if(ForwardCheckingEnabled) 
			{
				nextProblem = problem.clone();
				fcPass = nextProblem.ForwardCheck(assignment);
			}
			
			//In order to accept assignment, ForwardCheck must pass, and the assignment must be accepted by constraints.
			if(fcPass && problem.IsAssignmentValid(assignment))
			{
				Assignment result = RecursiveBacktracking(assignment, nextProblem);
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
	
	
	//Prints an assignment object along with "solution" or "failure"
	//Limits to max 30 prints
	public static void PrintAssignment(Assignment assignment, boolean success)
	{
		if(BranchesVisited >= MaxBranches)
			return;
		
		if(success)
			System.out.println((BranchesVisited + 1) + ". " + assignment.toString() + "solution");
		else
			System.out.println((BranchesVisited + 1) + ". " + assignment.toString() + "failure");
		
		BranchesVisited++;
	}
}
