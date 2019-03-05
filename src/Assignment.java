import java.util.Hashtable;
import java.util.Map.Entry;

public class Assignment extends Hashtable<String, Integer>{
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for(Entry<String, Integer> entry : entrySet())
		{
			sb.append(entry.getKey()).append("=").append(entry.getValue()).append(", ");
		}
		
		return sb.toString();
	}
}
