import java.util.Comparator;

public class SortByConflictions implements Comparator<ConflictionCount>
{

	@Override
	public int compare(ConflictionCount a, ConflictionCount b) {
		
		return a.Count - b.Count;
	}

}
