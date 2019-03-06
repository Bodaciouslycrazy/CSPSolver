import java.util.Comparator;

/*
 * Comparator that sorts ConflictionCount objects.
 * Lower conflicts are preferred, but if they are the same,
 * prefer the smaller value.
 */
public class SortByConflictions implements Comparator<ConflictionCount>
{

	@Override
	public int compare(ConflictionCount a, ConflictionCount b) {
		if(a.Count - b.Count != 0)
			return a.Count - b.Count;
		else
			return a.Value - b.Value;
		
	}

}
