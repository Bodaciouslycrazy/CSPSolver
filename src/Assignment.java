import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map.Entry;

public class Assignment {// extends Hashtable<String, Integer>{
	
	private ArrayList<String> Variables;
	private ArrayList<Integer> Values;
	
	public Assignment()
	{
		Variables = new ArrayList<String>();
		Values = new ArrayList<Integer>();
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < Variables.size(); i++)
		{
			sb.append(Variables.get(i)).append("=").append(Values.get(i)).append(", ");
		}
		
		/*
		for(Entry<String, Integer> entry : entrySet())
		{
			sb.append(entry.getKey()).append("=").append(entry.getValue()).append(", ");
		}
		*/
		
		return sb.toString();
	}
	
	public void put(String var, int val) throws Exception
	{
		if(Variables.contains(var))
			throw new Exception("VARIABLE ASSIGNMENT ALREADY EXISTS");
		
		Variables.add(var);
		Values.add(val);
	}
	
	public void remove(String var)
	{
		int i = Variables.indexOf(var);
		
		if(i >= 0)
		{
			Variables.remove(i);
			Values.remove(i);
		}
		else
		{
			System.out.println("Stupid error");
		}
	}
	
	public boolean containsKey(String key)
	{
		return Variables.contains(key);
	}
	
	public int GetAssignedValue(String var)
	{
		int i = Variables.indexOf(var);
		return Values.get(i);
	}
}
