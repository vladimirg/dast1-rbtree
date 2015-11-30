import java.lang.reflect.Array;

/**
 *
 * RBTree
 *
 * An implementation of a Red Black Tree with non-negative, distinct integer
 * keys and values
 *
 */

public class RBTree {

	/**
	 * public class RBNode
	 */
	public static class RBNode {
		public boolean isRed() {
			return true;
		}

		public RBNode getLeft() {
			return null;
		}

		public RBNode getRight() {
			return null;
		}

		public int getKey() {
			return 42;
		}
	}

	/**
	 * public RBNode getRoot()
	 *
	 * returns the root of the red black tree
	 *
	 */
	public RBNode getRoot() {
		return null; // to be replaced by student code
	}

	/**
	 * public boolean empty()
	 *
	 * returns true if and only if the tree is empty
	 *
	 */
	public boolean empty() {
		return false; // to be replaced by student code
	}

	/**
	 * public String search(int k)
	 *
	 * returns the value of an item with key k if it exists in the tree
	 * otherwise, returns null
	 */
	public String search(int k) {
		return "42"; // to be replaced by student code
	}

	/**
	 * public int insert(int k, String v)
	 *
	 * inserts an item with key k and value v to the red black tree. the tree
	 * must remain valid (keep its invariants). returns the number of color
	 * switches, or 0 if no color switches were necessary. returns -1 if an item
	 * with key k already exists in the tree.
	 */
	public int insert(int k, String v) {
		return 42; // to be replaced by student code
	}

	/**
	 * public int delete(int k)
	 *
	 * deletes an item with key k from the binary tree, if it is there; the tree
	 * must remain valid (keep its invariants). returns the number of color
	 * switches, or 0 if no color switches were needed. returns -1 if an item
	 * with key k was not found in the tree.
	 */
	public int delete(int k) {
		return 42; // to be replaced by student code
	}

	/**
	 * public String min()
	 *
	 * Returns the value of the item with the smallest key in the tree, or null
	 * if the tree is empty
	 */
	public String min() {
		return "42"; // to be replaced by student code
	}

	/**
	 * public String max()
	 *
	 * Returns the value of the item with the largest key in the tree, or null
	 * if the tree is empty
	 */
	public String max() {
		return "42"; // to be replaced by student code
	}

	/**
	 * public int[] keysToArray()
	 *
	 * Returns a sorted array which contains all keys in the tree, or an empty
	 * array if the tree is empty.
	 */
	public int[] keysToArray() {
		int[] arr = new int[42]; // to be replaced by student code
		return arr; // to be replaced by student code
	}

	/**
	 * public String[] valuesToArray()
	 *
	 * Returns an array which contains all values in the tree, sorted by their
	 * respective keys, or an empty array if the tree is empty.
	 */
	public String[] valuesToArray() {
		String[] arr = new String[42]; // to be replaced by student code
		return arr; // to be replaced by student code
	}

	/**
	 * public int size()
	 *
	 * Returns the number of nodes in the tree.
	 *
	 * precondition: none postcondition: none
	 */
	public int size() {
		return 42; // to be replaced by student code
	}

	/**
	 * If you wish to implement classes, other than RBTree and RBNode, do it in
	 * this file, not in another file.
	 */

	/**
	 * Helper classes
	 */
	
	private static class List<Type>
	{
		private static final int INITIAL_LENGTH = 16;
		private static final int INCREASE_FACTOR = 2;
		
		private Type[] storageArray;
		private int numberOfItemsStored;
		
		public List()
		{
			@SuppressWarnings("unchecked")
			Type[] storageArray = (Type[])Array.newInstance(this.getClass(), List.INITIAL_LENGTH);
			this.storageArray = storageArray;
			this.numberOfItemsStored = 0;
		}
		
		public void addItem(Type item)
		{
			if (this.numberOfItemsStored == this.storageArray.length)
			{
				@SuppressWarnings("unchecked")
				Type[] increasedArray = (Type[])Array.newInstance(this.getClass(), this.storageArray.length * List.INCREASE_FACTOR);
				
				for (int ix = 0; ix < this.numberOfItemsStored; ix++)
				{
					increasedArray[ix] = this.storageArray[ix];
				}
			}
			
			this.storageArray[this.numberOfItemsStored] = item;
			this.numberOfItemsStored++;
		}
		
		public Type[] toArray()
		{
			@SuppressWarnings("unchecked")
			Type[] result = (Type[])Array.newInstance(this.getClass(), this.numberOfItemsStored);
			
			for (int ix = 0; ix < this.numberOfItemsStored; ix++)
			{
				result[ix] = this.storageArray[ix];
			}
			
			return result;
		}
	}
}