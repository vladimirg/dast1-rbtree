import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RuntimeMeasuerments {
	public static void main(String[] args) {
		for (int i = 1; i <= 10; i++) {
			List<Integer> items = new ArrayList<Integer>();
			RBTree tree = new RBTree();
			int insertColorChanges = 0;
			int deleteColorChanges = 0;
			int numberOfItems = i * 10000;
			
			for (int j = 1; j <= numberOfItems; j++) {
				items.add(j);
			}
			Collections.shuffle(items);
			
			for (int item : items) {
				insertColorChanges += tree.insert(item, Integer.toString(item));
			}
			
			for (int j = 1; j <= numberOfItems; j++) {
				deleteColorChanges += tree.delete(j);
			}
			
			System.out.printf("Run %d: avg insert color changes = %.2f; avg delete color changes = %.2f.\n",
					i, (float)insertColorChanges / numberOfItems, (float)deleteColorChanges / numberOfItems);
		}
	}
}
