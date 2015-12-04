import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import org.junit.Test;

public class Tester {
	@Test
	public static void main(String[] args)
	{
		int[] insertions;
		int[] deletions;
		
		// Run the simple test:
		insertions = new int[] {6, 7, 8, 9, 10, 5, 4, 3, 2, 1};
		deletions = new int[]  {2, 9, 1, 10, 5, 4, 3, 8, 7, 6};
		runFullTestSuite(insertions, deletions);
		
		// Run the big dataset test:
		insertions = getKeysFromFile("test data/test_data_insert_1.txt");
		deletions = getKeysFromFile("test data/test_data_delete_1.txt");
		runFullTestSuite(insertions, deletions);
	}
	
	private static void runFullTestSuite(int[] insertions, int[] deletions)
	{
		List<Integer> keysInTree = new ArrayList<Integer>();
		RBTree tree = new RBTree();
		
		verifyEmptyTree(tree);
		
		for (int ix = 0; ix < insertions.length; ix++)
		{
			int keyToInsert = insertions[ix];
			keysInTree.add(keyToInsert);
			String valueToInsert = Integer.toString(keyToInsert);
			
			int previousRedCount = countReds(tree.getRoot());
			int colorChanges = tree.insert(keyToInsert, valueToInsert);
			int newRedCount = countReds(tree.getRoot());
			
			// -1 because a newly inserted node is red, but doesn't count as a color change:
			assert colorChanges >= (Math.abs(newRedCount - previousRedCount) - 1);
			
			verifyTreeMatchesArray(tree, listToArray(keysInTree));
			verifyRBTreeInvariants(tree);
		}
		
		for (int ix = 0; ix < deletions.length; ix++)
		{
			int keyToDelete = deletions[ix];
			keysInTree.remove(new Integer(keyToDelete));
			
			int previousRedCount = countReds(tree.getRoot());
			int colorChanges = tree.delete(keyToDelete);
			int newRedCount = countReds(tree.getRoot());
			
			// -1 because a deleted node may be red, and that doesn't count towards a color change.
			assert colorChanges >= (Math.abs(newRedCount - previousRedCount) - 1);
			
			verifyTreeMatchesArray(tree, listToArray(keysInTree));
			verifyRBTreeInvariants(tree);
		}
		
		verifyEmptyTree(tree);
	}
	
	private static int countReds(RBTree.RBNode node)
	{
		if (node == null)
		{
			return 0;
		}
		
		return (node.isRed() ? 1 : 0) + countReds(node.getLeft()) + countReds(node.getRight());
	}
	
	private static void testRandomDataset()
	{
		RBTree tree = new RBTree();
		verifyEmptyTree(tree);
		
		List<Integer> itemList = createItemList(1000);
		insertArrayIntoTree(tree, listToArray(itemList));
		verifyTreeMatchesArray(tree, listToArray(itemList));
		
		for (int ix = 0; ix < itemList.size(); ix += 10)
		{
			tree.delete(itemList.get(ix));
			itemList.remove(ix);
		}
		verifyTreeMatchesArray(tree, listToArray(itemList));
		
		for (int i : itemList)
		{
			tree.delete(i);
		}
		verifyEmptyTree(tree);
	}
	
	private static void verifyEmptyTree(RBTree tree) {
		assert tree.empty() == true;
		assert tree.size() == 0;
		assert tree.getRoot() == null; 
		assert Arrays.equals(new int[] {}, tree.keysToArray());
		assert Arrays.equals(new String[] {}, tree.valuesToArray());
		assert tree.min() == null;
		assert tree.max() == null;
		assert tree.search(42) == null;
	}
	
	private static void verifyTreeMatchesArray(RBTree tree, int[] array)
	{
		int[] sortedArray = new int[array.length];
		System.arraycopy(array, 0, sortedArray, 0, array.length);
		Arrays.sort(sortedArray);
		String[] valuesSortedArray = stringifyArray(sortedArray);
		
		assert tree.size() == array.length;
		assert tree.empty() == (array.length == 0);
		assert Arrays.equals(tree.keysToArray(), sortedArray);
		assert Arrays.equals(tree.valuesToArray(), valuesSortedArray);
		assert tree.min().equals(valuesSortedArray[0]);
		assert tree.max().equals(valuesSortedArray[valuesSortedArray.length-1]);
		for (int i : array)
		{
			assert tree.search(i).equals(Integer.toString(i));
		}
	}
	
	private static void verifyRBTreeInvariants(RBTree tree)
	{
		RBTree.RBNode treeRoot = tree.getRoot();
		assert treeRoot == null || (!treeRoot.isRed() && treeRoot.isBlack());
		
		verifyRBTreeInvariants(treeRoot, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}
	
	private static int verifyRBTreeInvariants(RBTree.RBNode node, int minKey, int maxKey)
	{
		if (node == null)
		{
			return 1;
		}
		
		// Verify key properties:
		assert node.getKey() > minKey && node.getKey() < maxKey;
		
		// Verify correct coloring:
		assert (node.isRed() && !node.isBlack()) || (!node.isRed() && node.isBlack());
		RBTree.RBNode left = node.getLeft();
		RBTree.RBNode right = node.getRight();
		if (node.isRed())
		{
			assert left == null || left.isBlack();
			assert right == null || right.isBlack();
		}
		
		// Verify black height:
		int leftBlackHeight = verifyRBTreeInvariants(left, minKey, node.getKey());
		int rightBlackHeight = verifyRBTreeInvariants(right, node.getKey(), maxKey);
		
		assert leftBlackHeight == rightBlackHeight;
		
		return leftBlackHeight + (node.isBlack() ? 1 : 0);
	}
	
	private static void insertArrayIntoTree(RBTree tree, int[] array)
	{
		for (int i : array)
		{
			tree.insert(i, Integer.toString(i));
		}
	}
	
	private static List<Integer> createItemList(int size)
	{
		List<Integer> result = new ArrayList<Integer>();
		
		for (int i = 1; i <= size; i++)
		{
			result.add(i);
		}
		
		Collections.shuffle(result);
		
		return result;
	}
	
	private static String[] stringifyArray(int[] array)
	{
		String[] result = new String[array.length];
		
		for (int ix = 0; ix < array.length; ix++)
		{
			result[ix] = Integer.toString(array[ix]);
		}
		
		return result;
	}
	
	private static int[] listToArray(List<Integer> list)
	{
		int[] result = new int[list.size()];
		
		for (int ix = 0; ix < result.length; ix++)
		{
			result[ix] = list.get(ix);
		}
		
		return result;
	}
	
	private static int[] getKeysFromFile(String filename)
	{
		List<Integer> result = new ArrayList<Integer>();
		try
		{
			Scanner scanner = new Scanner(new File(filename));
			
			while (scanner.hasNextLine())
			{
				result.add( Integer.parseInt(scanner.nextLine()) );
			}
			
			scanner.close();
		}
		catch (FileNotFoundException e)
		{
			assert false;
		}
		
		return listToArray(result);
	}
}
