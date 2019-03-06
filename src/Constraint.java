import java.io.IOException;
import java.util.Hashtable;

/*
 * Constraint
 * 
 * Constrains the values of two variables.
 * has a RightHandSide variable and LeftHandSide variable.
 * valid Operators are <, >, =, !
 */
public class Constraint {
	
	public String LeftHandSide;
	public String RightHandSide;
	public Operator Op;
	
	
	public Constraint(String lhs, String o, String rhs) throws IOException
	{
		LeftHandSide = lhs;
		RightHandSide = rhs;
		switch(o)
		{
		case ">":
			Op = Operator.GREATER_THAN;
			break;
		case "<":
			Op = Operator.LESS_THAN;
			break;
		case "=": 
			Op = Operator.EQUALS;
			break;
		case "!":
			Op = Operator.NOT_EQUALS;
			break;
		default:
			throw new IOException("OPERATOR NOT RECOGNIZED");
		}
	}
	
	
	
	//Checks to make sure the given assignment passes this constraint.
	//This version uses default value onMissingVariable=true
	public boolean CheckConstraint(Assignment assignment)
	{
		return CheckConstraint(assignment, true);
	}
	
	//Checks to make sure the given assignment passes this constraint.
	//If one or more variables are missing, it will return onMissingVariable
	public boolean CheckConstraint(Assignment assignment, boolean onMissingVariable)
	{
		if(!assignment.containsKey(LeftHandSide) || !assignment.containsKey(RightHandSide))
			return onMissingVariable;
		
		return Op.Evaluate(assignment.get(LeftHandSide), assignment.get(RightHandSide));
	}
	
	
	
	//Checks if two specific var-val pairs matches the constraints.
	//Doesn't matter what order the two variables are in this call, only the order in the constraint matters.
	//uses default value onMissingVariable=true
	public boolean CheckConstraint(String vara, int vala, String varb, int valb)
	{
		return CheckConstraint(vara, vala, varb, valb, true);
	}
	
	//Checks if two specific var-val pairs matches the constraints.
	//Doesn't matter what order the two variables are in this call, only the order in the constraint matters.
	//if one or more variables are missing, returns onMissingVariable
	public boolean CheckConstraint(String vara, int vala, String varb, int valb, boolean onMissingVariable)
	{
		if(vara.equals(RightHandSide) && varb.equals(LeftHandSide))
			return CheckConstraint(varb, valb, vara, vala, onMissingVariable);
		else if(vara.equals(LeftHandSide) && varb.equals(RightHandSide))
			return Op.Evaluate(vala, valb);
		else
			return onMissingVariable;
			
	}
	
	
	
	//Returns true if this constraint uses the given variable
	public boolean ContainsKey(String key)
	{
		return LeftHandSide.equals(key) || RightHandSide.equals(key);
	}
	
	
	//Given one key, this will return the other key.
	public String GetOtherKey(String in)
	{
		if(LeftHandSide.equals(in))
			return RightHandSide;
		else if(RightHandSide.equals(in))
			return LeftHandSide;
		else
			return null;
	}
	
	//Looks at a given assignment and returns the name of a key from this constraint that has been assigned.
	public String GetAssignedKey(Assignment assigned)
	{
		if(assigned.containsKey(LeftHandSide) && assigned.containsKey(RightHandSide))
			return null;
		else if(assigned.containsKey(LeftHandSide))
			return LeftHandSide;
		else if(assigned.containsKey(RightHandSide))
			return RightHandSide;
		else
			return null;
	}
	
	//Looks at a given assignment and returns the name of a key from this constraint that hasn't been assigned.
	public String GetUnassignedKey(Assignment assigned)
	{
		if(assigned.containsKey(LeftHandSide) && assigned.containsKey(RightHandSide))
			return null;
		else if(assigned.containsKey(LeftHandSide))
			return RightHandSide;
		else if(assigned.containsKey(RightHandSide))
			return LeftHandSide;
		else
			return null;
	}
	
	
	//For debug purposes only
	//Returns a printable string representation of the constraint
	public String toString()
	{
		return LeftHandSide + " " + Op + " " + RightHandSide;
	}
	
	
	
	//Enum that describes the different possible operations.
	//Has a Evaluate() function that is overridden based on which enum type it is.
	private enum Operator implements Operation
	{
		GREATER_THAN(">")
		{
			@Override public boolean Evaluate(int a, int b) { return a > b; }
		},
		
		LESS_THAN("<")
		{
			@Override public boolean Evaluate(int a, int b) {return a < b; }
		},
		
		EQUALS("=")
		{
			@Override public boolean Evaluate(int a, int b) {return a == b; }
		},
		
		NOT_EQUALS("!")
		{
			@Override public boolean Evaluate(int a, int b) {return a != b; }
		};
		
		
		//For debug purposes only.
		private String StringValue;
		Operator(String val)
		{
			StringValue = val;
		}
		public String toString() { return StringValue; }
	}
}
