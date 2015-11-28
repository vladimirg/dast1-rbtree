import java.util.*;

import org.junit.Test;

public class Tester {
	@Test
	public static void main(String[] args)
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
}
