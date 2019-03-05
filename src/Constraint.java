import java.io.IOException;
import java.util.Hashtable;

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
	
	
	
	public boolean CheckConstraint(Assignment assignment)
	{
		if(!assignment.containsKey(LeftHandSide) || !assignment.containsKey(RightHandSide))
			return true;
		
		return Op.Evaluate(assignment.get(LeftHandSide), assignment.get(RightHandSide));
	}
	
	
	
	public boolean CheckConstraint(String vara, int vala, String varb, int valb)
	{
		if(vara.equals(RightHandSide) && varb.equals(LeftHandSide))
			return CheckConstraint(varb, valb, vara, vala);
		else
		{
			return Op.Evaluate(vala, valb);
		}
	}
	
	
	
	public boolean ContainsKey(String key)
	{
		return LeftHandSide.equals(key) || RightHandSide.equals(key);
	}
	
	public String GetOtherKey(String in)
	{
		if(LeftHandSide.equals(in))
			return RightHandSide;
		else if(RightHandSide.equals(in))
			return LeftHandSide;
		else
			return null;
	}
	
	public String toString()
	{
		return LeftHandSide + " " + Op + " " + RightHandSide;
	}
	
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
		
		private String StringValue;
		Operator(String val)
		{
			StringValue = val;
		}
		public String toString() { return StringValue; }
	}
}
