
/**
 *
 * RBTree
 *
 * An implementation of a Red Black Tree with non-negative, distinct integer
 * keys and values
 *
 */

public class RBTree {
	
	private RBNode root;
	private RBNode min;
	private RBNode max;
	private int size;

	/**
	 * public class RBNode
	 */
	public static class RBNode {
		
		public enum Color {
			BLACK, RED;
		}
		
		private int key;
		private String value;
		private Color color;
		private RBNode parent;
		private RBNode leftChild;
		private RBNode rightChild;
		
		// empty constructor - creates a red node with no other properties
		// parents and children are null by default
		public RBNode() {
			this.color = Color.RED;
		}
		
		// full constructor - calls the empty one and adds key & value
		public RBNode(int key, String value) {
			this();
			this.key = key;
			this.value = value;
		}
		
		// returns true iff RBNode is red - O(1)
		public boolean isRed() {
			return this.color == Color.RED;
		}
		
		// returns true iff RBNode is black - O(1)
		public boolean isBlack() {
			return this.color == Color.BLACK;
		}
		
		// makes RBNode red - O(1)
		public void setToRed() {
			this.setColor(Color.RED);
		}
		
		// makes RBNode black - O(1)
		public void setToBlack() {
			this.setColor(Color.BLACK);
		}
		
		// sets the given color and returns 1 if color changed, 0 otherwise - O(1)
		public int setColor(Color newColor) {
			if (this.color == newColor) {
				return 0;
			}
			else {
				this.color = newColor;
				return 1;
			}
		}
		
		// getter for RBNode color (returns RBNode color) - O(1)
		public Color getColor() {
			return this.color;
		}

		// getter for RBNode left child - O(1)
		public RBNode getLeft() {
			return this.leftChild;
		}
		
		// setter for left child - sets left child and its parent - O(1)
		public void setLeft(RBNode leftChild) {
			this.leftChild = leftChild;
			
			if (leftChild != null) {
				leftChild.setParent(this);
			}
		}

		// getter for RBNode right child - O(1)
		public RBNode getRight() {
			return this.rightChild;
		}
		
		// setter for RBNode right child - O(1)
		public void setRight(RBNode rightChild) {
			this.rightChild = rightChild;
			
			if (rightChild != null) {
				rightChild.setParent(this);
			}
		}

		// getter for RBNode key - O(1)
		public int getKey() {
			return this.key;
		}
		
		// setter for RBNode key - O(1)
		public void setKey(int key) {
			this.key = key;
		}
		
		// getter for RBNode value - O(1)
		public String getValue() {
			return this.value;
		}
		
		// setter for RBNode value - O(1)
		public void setValue(String value) {
			this.value = value;
		}
		
		// getter for RBNode parent - O(1)
		public RBNode getParent() {
			return this.parent;
		}
		
		// setter for RBNode parent - O(1)
		public void setParent(RBNode node) {
			this.parent = node;
		}
		
		// replace old child by new child, including parent relations - O(1)
		public void replaceChild(RBNode oldChild, RBNode newChild) {
			if (this.leftChild == oldChild) {
				this.leftChild.setParent(null);
				this.setLeft(newChild);
				oldChild.setParent(null);
			}
			else if (this.rightChild == oldChild) {
				this.rightChild.setParent(null);
				this.setRight(newChild);
				oldChild.setParent(null);
			}
		}
		
		// returns RBNode sibling - O(1)
		public RBNode getSibling() {
			if (this.parent == null) {
				return null;
			}
			
			if (this == this.parent.getLeft()) {
				return this.parent.getRight();
			}
			else {
				return this.parent.getLeft();
			}
		}
	}

	/**
	 * public RBNode getRoot()
	 *
	 * returns the root of the red black tree
	 *
	 * in O(1)
	 */
	public RBNode getRoot() {
		return root;
	}

	/**
	 * public boolean empty()
	 *
	 * returns true if and only if the tree is empty
	 *
	 * in O(1)
	 */
	public boolean empty() {
		if (getRoot() == null) {
			return true;
		}
		return false;
	}

	/**
	 * public String search(int k)
	 *
	 * returns the value of an item with key k if it exists in the tree
	 * otherwise, returns null
	 * 
	 * in O(log(n)), because of the recursive inner method searchNode(k)
	 */
	public String search(int k) {
		if (searchNode(k) != null) {
			return searchNode(k).value;
		}
		return null;
	}

	/**
	 * public int insert(int k, String v)
	 *
	 * inserts an item with key k and value v to the red black tree. the tree
	 * must remain valid (keep its invariants). returns the number of color
	 * switches, or 0 if no color switches were necessary. returns -1 if an item
	 * with key k already exists in the tree.
	 * 
	 * in O(log(n)), because of the inner method insertFixup(node)
	 */
	public int insert(int k, String v) {
		RBNode myNode = new RBNode(k, v);
		
		// edge case - inserts new RBNode to root when RBTree is empty
		if (this.empty()) {
			this.root = myNode;
			this.min = myNode;
			this.max = myNode;
			size++;
			return myNode.setColor(RBNode.Color.BLACK);
		}
		
		// finds the place for insertion, RBNode with suitable null child
		RBNode myLeaf = searchLeaf(k);
		
		// inserts new RBNode
		if (myLeaf != null) {
			myNode.parent = myLeaf;
			if (myNode.key < myLeaf.key) {
				myLeaf.leftChild = myNode;
			} else {
				myLeaf.rightChild = myNode;
			}

			// fixes tree if necessary - colors, size, min/max, root
			return insertFixup(myNode);
		}
		return -1;
	}

	/**
	 * public int delete(int k)
	 *
	 * deletes an item with key k from the binary tree, if it is there; the tree
	 * must remain valid (keep its invariants). returns the number of color
	 * switches, or 0 if no color switches were needed. returns -1 if an item
	 * with key k was not found in the tree.
	 * 
	 * worst case - O(log n), because of the inner methods deleteNode(node), findSuccessor(node)
	 */
	public int delete(int k) {
		RBNode nodeToDelete = this.searchNode(k);
		if (nodeToDelete == null) {
			return -1;
		}
		
		if (nodeToDelete.getLeft() == null || nodeToDelete.getRight() == null) {
			// nodeToDelete has at most one child, physically delete it:
			return this.deleteNode(nodeToDelete);
		}
		else  {
			// nodeToDelete has two children, replace it with its successor:
			RBNode successor = this.findSuccessor(nodeToDelete);
			
			// We're going to delete the successor, but what if it's the max?
			boolean updateMax = (successor == this.max);
			
			nodeToDelete.setKey(successor.getKey());
			nodeToDelete.setValue(successor.getValue());
			
			int colorChanges = this.deleteNode(successor);
			if (updateMax) {
				this.max = nodeToDelete;
			}
			
			return colorChanges;
		}
	}

	/**
	 * public String min()
	 *
	 * Returns the value of the item with the smallest key in the tree, or null
	 * if the tree is empty
	 * 
	 * in O(1)
	 */
	public String min() {
		if (this.min != null) {
			return this.min.getValue();
		}
		return null;
	}

	/**
	 * public String max()
	 *
	 * Returns the value of the item with the largest key in the tree, or null
	 * if the tree is empty
	 * 
	 * in O(1)
	 */
	public String max() {
		if (this.max != null) {
			return max.getValue();
		}
		return null;
	}

	/**
	 * public int[] keysToArray()
	 *
	 * Returns a sorted array which contains all keys in the tree, or an empty
	 * array if the tree is empty.
	 * 
	 * worst case - O(n) (inorder traversal is O(n), collecting all keys into
	 * a list is O(n), copying the list into a result array is O(n))
	 */
	public int[] keysToArray() {
		List keys = new List();
		
		this.collectKeysInorder(this.getRoot(), keys);
		
		int[] result = new int[keys.length()];
		for (int ix = 0; ix < result.length; ix++)
		{
			result[ix] = (int)keys.getItem(ix);
		}
		
		return result;
	}

	/**
	 * public String[] valuesToArray()
	 *
	 * Returns an array which contains all values in the tree, sorted by their
	 * respective keys, or an empty array if the tree is empty.
	 * 
	 * worst case - O(n) (inorder traversal is O(n), collecting all values into
	 * a list is O(n), copying the list into a result array is O(n))
	 */
	public String[] valuesToArray() {
		List values = new List();
		
		this.collectValuesInorder(this.getRoot(), values);
		
		String[] result = new String[values.length()];
		for (int ix = 0; ix < result.length; ix++)
		{
			result[ix] = (String)values.getItem(ix);
		}
		
		return result;
	}

	/**
	 * public int size()
	 *
	 * Returns the number of nodes in the tree.
	 *
	 * precondition: none postcondition: none
	 */
	public int size() {
		return this.size;
	}

	/**
	 * If you wish to implement classes, other than RBTree and RBNode, do it in
	 * this file, not in another file.
	 */
	
	/**
	 * Private methods
	 */
	
	/**
	 * returns RBNode with matching key if exists, else returns null
	 * 
	 * starts searching on root
	 *
	 * O(log(n)) because of the inner method searchNode(key, node)
	 */
	private RBNode searchNode(int key) {
		return searchNode(key, this.root);
	}
	
	/**
	 * returns RBNode with matching key if exists, else returns null
	 * 
	 * starts searching on a given node
	 * 
	 * in O(log(n)), because it visits nodes on one branch only,
	 * the tree height is of course O(log(n))
	 */
	private RBNode searchNode(int key, RBNode node){
		if (node != null) {
			if (key == node.key) {
				return node;
			}
			if (key < node.key) {
				if (node.leftChild != null) {
					return searchNode(key, node.leftChild);
				}
				return null;
			}
			if (node.rightChild != null) {
				return searchNode(key, node.rightChild);
			}
		}
		return null;
	}
	
	/** 
	 * returns node for insertion of key; if key already exists returns null
	 * 
	 * starts searching on root
	 * 
	 * O(log(n)) because of the inner method searchLeaf(key, node)
	 */
	private RBNode searchLeaf(int key) {
		if (!this.empty()) {
			return searchLeaf(key, this.root);
		}
		return null;
	}
	
	/** 
	 * returns node for insertion of key; if key already exists returns null
	 * 
	 * starts searching on a given node
	 * 
	 * exactly the same path as searchNode - O(log(n))
	 */ 
	private RBNode searchLeaf(int key, RBNode node) {
		RBNode myNode = node;
		
		if (key == myNode.key) {
			return null;
		}
		if (key < myNode.key) {
			if (myNode.leftChild != null) {
				return searchLeaf(key, myNode.leftChild);
			}
			return myNode;
		}
		if (myNode.rightChild != null) {
			return searchLeaf(key, myNode.rightChild);
		}
		return myNode;
	}
	
	// replaces x's left child by y - O(1)
	private void toLeftChild(RBNode x, RBNode y) {
		x.leftChild = y;
		if (y != null) {
			y.parent = x;
		}
	}
	
	// replaces x's right child by y - O(1)
	private void toRightChild(RBNode x, RBNode y) {
		x.rightChild = y;
		if (y != null) {
			y.parent = x;
		}
	}
	
	// sets y as x's parent's child instead of x - O(1)
	private void transplate(RBNode x, RBNode y) {
		RBNode parent = x.parent;
		
		if (parent == null) {
			y.parent = null;
		} else {
			if (x == parent.leftChild) {
				toLeftChild(parent, y);
			} else {
				toRightChild(parent, y);
			}
		}
	}
	
	/**
	 * rotate a given node and its right child to the left - O(1)
	 */
	private void rotateLeft(RBNode x) {
		RBNode y = x.rightChild;
		
		transplate(x, y);
		toRightChild(x, y.leftChild);
		toLeftChild(y, x);
		
		if (x == this.root) {
			this.root = y;
		}
	}
	
	/**
	 * rotate a given node and its left child to the right - O(1)
	 */
	private void rotateRight(RBNode x) {
		RBNode y = x.leftChild;
		
		transplate(x, y);
		toLeftChild(x, y.rightChild);
		toRightChild(y, x);
		
		if (y.parent == null) {
			this.root = y;
		}
	}
	
	// returns node's successor - worst case O(log(n)), the tree height
	private RBNode findSuccessor(RBNode node) {
		// If we have a right child, the successor is the minimum of the right subtree:
		if (node.getRight() != null) {
			RBNode rightSubtreeMin = node.getRight();
			while (rightSubtreeMin.getLeft() != null) {
				rightSubtreeMin = rightSubtreeMin.getLeft();
			}
			
			return rightSubtreeMin;
		}
		
		// otherwise, it's the first ancestor that's bigger than us:
		RBNode ancestor = node.parent;
		while (ancestor != null && ancestor.getKey() < node.getKey()) {
			ancestor = ancestor.getParent();
		}
		
		return ancestor;
	}
	
	// returns node's predecessor - worst case O(log(n)), the tree height
	private RBNode findPredecessor(RBNode node) {
		// If the node has a left subtree, its predecessor is its max:
		if (node.getLeft() != null) {
			RBNode pred = node.getLeft();
			while (pred.getRight() != null) {
				pred = pred.getRight();
			}
			
			return pred;
		}
		
		// Otherwise, the predecessor is the first ancestor which is smaller than the node:
		RBNode parent = node.getParent();
		while (parent != null && parent.getKey() > node.getKey()) {
			parent = parent.getParent();
		}
		
		return parent;
	}
	
	/** 
	 * fixes tree after insertion of node
	 * 
	 * returns number of color changes
	 * 
	 * worst case in O(log(n)) - the max munber of loops is the tree height
	 */
	private int insertFixup(RBNode node) {
		
		// upgrades size
		size++;
		
		// upgrades min/max
		if (node.key < this.min.key) {
			this.min = node;
		}
		if (this.max.key < node.key) {
			this.max = node;
		}
		
		// upgrades colors and rotates if necessary
		int colorChanges = 0;
		while (node.parent.isRed()) {
			RBNode parent = node.parent;
			// always exists, cause otherwise parent is a black root
			RBNode granny = parent.parent;
			
			// parent is a left child
			if (parent == granny.leftChild) {
				// cases 1,2,3 by the school pseudo cod
				int[] leftResult = insertLeftCases(node);
				colorChanges += leftResult[0];
				// if case one
				if (leftResult[1] == 1) {
					node = granny;
				} else {
					break;
				}
			} else {
				// parent is a right child
				// cases 1,2,3 by the school pseudo cod
				int[] rightResult = insertRightCases(node);
				colorChanges += rightResult[0];
				// if case 1
				if (rightResult[1] == 1) {
					node = granny;
				} else {
					break;
				}
			}
		}
		return colorChanges;
	}
	
	/** 
	 * the one that's in the class slides - cases when parent is a left child
	 * 
	 * returns two ints - number of color changes & if it's case 1
	 *
	 * O(1) - no loops/recursion
	 */
	private int[] insertLeftCases(RBNode node) {
		RBNode parent = node.parent;
		RBNode granny = parent.parent;
		RBNode uncle = granny.rightChild;
		int colorChanges = 0;
		int isCase1 = 0;
		
		// case 1: red uncle
		if (uncle != null) {
			if (uncle.isRed()) {
				colorChanges += parent.setColor(RBNode.Color.BLACK);
				colorChanges += uncle.setColor(RBNode.Color.BLACK);
				if (this.root != granny) {
					colorChanges += granny.setColor(RBNode.Color.RED);
					isCase1 = 1;
				}
				return new int[] {colorChanges, isCase1};
			}
		}
		// case 2: black/null uncle & node is a right child
		if (node == parent.rightChild) {
			node = parent;
			rotateLeft(node);
			parent = node.parent;
			granny = parent.parent;
		}
		// case 3: black/null uncle & node is a left child
		colorChanges += parent.setColor(RBNode.Color.BLACK);
		colorChanges += granny.setColor(RBNode.Color.RED);
		rotateRight(granny);
		return new int[] {colorChanges, isCase1};
	}
	
	/**
	 * mirror case of the previous method
	 * 
	 * returns two ints - number of color changes & if it's case 1
	 * 
	 * O(1) - no loops/recursion
	 */
	private int[] insertRightCases(RBNode node) {
		RBNode parent = node.parent;
		RBNode granny = parent.parent;
		RBNode uncle = granny.leftChild;
		int colorChanges = 0;
		int isCase1 = 0;
		
		// case 1: red uncle
		if (uncle != null) {
			if (uncle.isRed()) {
				colorChanges += parent.setColor(RBNode.Color.BLACK);
				colorChanges += uncle.setColor(RBNode.Color.BLACK);
				if (this.root != granny) {
					colorChanges += granny.setColor(RBNode.Color.RED);
					isCase1 = 1;
				}
				return new int[] {colorChanges, isCase1};
			}
		}
		// case 2: black/null uncle & node is a left child
		if (node == parent.leftChild) {
			node = parent;
			rotateRight(node);
			parent = node.parent;
			granny = parent.parent;
		}
		// case 3: black/null uncle & node is a right child
		colorChanges += parent.setColor(RBNode.Color.BLACK);
		colorChanges += granny.setColor(RBNode.Color.RED);
		rotateLeft(granny);
		return new int[] {colorChanges, isCase1};
	}
	
	/**
	 * perform a physical deletion of a given node, update min and max poiners,
	 * and do the fixup of "double blackness" (if it arises)
	 * 
	 * returns the number of color changes that occured during the operation
	 * 
	 * the node to delete must have at most one child
	 * 
	 * O(log(n)) because of the inner method fixTreeAfterDeletion(child)
	 */
	private int deleteNode(RBNode nodeToDelete) {
		// we're going to delete this node for sure, so update size: 
		this.size -= 1;
		
		// if we're deleting the min or max nodes, we should update their pointers:
		if (nodeToDelete == this.min) {
			this.min = this.findSuccessor(nodeToDelete);
		}
		// not using "else" because in a one-node tree, the root is both the min and the max
		if (nodeToDelete == this.max) { 
			this.max = this.findPredecessor(nodeToDelete);
		}
		
		int colorChanges = 0;
		
		if (nodeToDelete.getLeft() == null && nodeToDelete.getRight() == null) {
			// nodeToDelete has no children, delete it immediately
			RBNode parent = nodeToDelete.getParent();
			
			// is this the last node in the tree?
			if (parent == null) {
				this.root = null;
			}
			else {
				if (nodeToDelete.isBlack()) {
					/* the node to delete is both black and has no real children.
					 * this is a problem, since we don't use instance placeholders for the
					 * terminal leaves. Our solution is to delay deleting this node
					 * until the tree fixing is complete, and only then remove it.
					 * this is OK, because fixing the tree should change neither the
					 * children of a double-black node nor its parent, including the
					 * node to delete
					 * */
					colorChanges += this.fixTreeAfterDeletion(nodeToDelete);
				}
				
				parent.replaceChild(nodeToDelete, null);
			}
		}
		else if (nodeToDelete.getLeft() == null || nodeToDelete.getRight() == null) {
			// nodeToDelete has only one child, replace it with its child.
			RBNode child;
			if (nodeToDelete.getLeft() != null) {
				child = nodeToDelete.getLeft();
			}
			else {
				child = nodeToDelete.getRight();
			}
			
			RBNode parent = nodeToDelete.getParent();
			
			// We're trying to delete the root, replace it with its child.
			if (parent == null) {
				child.parent = null;
				this.root = child;
			}
			else {
				parent.replaceChild(nodeToDelete, child);
			}
			
			// if the deleted node was black
			// we may be in trouble for violating the black height:
			if (nodeToDelete.isBlack()) {
				if (child.isRed()) {
					// The new child is red, we're cool
					child.setToBlack();
					colorChanges++;
				}
				else {
					// mark it and fix the tree
					colorChanges += this.fixTreeAfterDeletion(child);
				}
			}
		}
		
		return colorChanges;
	}
	
	/**
	 * recursively fix a tree that has a node afflicted with the dreaded "double blackness"
	 * 
	 * the input node is the node that is marked as double black
	 * (we do not store this information anywhere else)
	 * 
	 * returns the number of color changes that occurred during the fixup
	 * 
	 * O(log(n)) - the maximal depth of recursion is the tree height
	 */
	private int fixTreeAfterDeletion(RBNode doubleBlackNode) {
		// if we reached the root, do nothing:
		if (doubleBlackNode == this.root) {
			return 0;
		}
		
		// if the double black node is red, balance it by changing it to black:
		if (doubleBlackNode.isRed()) {
			doubleBlackNode.setToBlack();
			return 1;
		}
		
		RBNode parent = doubleBlackNode.getParent();
		RBNode sibling = doubleBlackNode.getSibling();
		
		if (sibling.isRed()) {
			// case 1: the sibling is red
			if (sibling == parent.getRight()) {
				this.rotateLeft(parent);
			}
			else {
				this.rotateRight(parent);
			}
			
			parent.setToRed();
			sibling.setToBlack();
			return 2 + this.fixTreeAfterDeletion(doubleBlackNode);
		}
		else {
			// the sibling is black
			
			RBNode proximalNephew; // the nephew closer to the double black node
			RBNode distalNephew; // the nephew farther from the double black node
			
			if (doubleBlackNode == parent.getLeft()) {
				proximalNephew = sibling.getLeft();
				distalNephew = sibling.getRight();
			}
			else {
				proximalNephew = sibling.getRight();
				distalNephew = sibling.getLeft();
			}
			
			if ((proximalNephew == null || proximalNephew.isBlack()) &&
				(distalNephew == null || distalNephew.isBlack())) {
				// Case 2: both nephews are black.
				sibling.setToRed();
				return 1 + this.fixTreeAfterDeletion(parent);
			}
			else {
				// At least one child is red.
				int colorChanges = 0;
				
				if (distalNephew == null || distalNephew.isBlack()) {
					// Case 3: the distal nephew is black (so the proximal nephew must be red).
					// Rotate as needed:
					RBNode newProximalNephew;
					if (proximalNephew == sibling.getLeft()) {
						this.rotateRight(sibling);
						newProximalNephew = proximalNephew.getLeft();
					}
					else {
						this.rotateLeft(sibling);
						newProximalNephew = proximalNephew.getRight();
					}
					
					// After rotation, update the old relation's colors:
					proximalNephew.setToBlack();
					sibling.setToRed();
					colorChanges += 2;
					
					// Update our relations:
					distalNephew = sibling; // Note that after this operation, the distal nephew is red.
					sibling = proximalNephew;
					proximalNephew = newProximalNephew;
				}
				
				// Case 4: the distal nephew is red (the proximal nephew can be either red or black).
				RBNode.Color parentColor = parent.getColor();
				if (sibling == parent.getRight()) {
					this.rotateLeft(parent);
				}
				else {
					this.rotateRight(parent);
				}
				
				colorChanges += sibling.setColor(parentColor);
				colorChanges += parent.setColor(RBNode.Color.BLACK);
				colorChanges += distalNephew.setColor(RBNode.Color.BLACK);
				
				return colorChanges;
			}
		}
	}
	
	// collect the node keys using inorder into a given list - O(n)
	private void collectKeysInorder(RBNode node, List keysList) {
		if (node == null) {
			return;
		}
		
		this.collectKeysInorder(node.getLeft(), keysList);
		keysList.addItem(node.getKey());
		this.collectKeysInorder(node.getRight(), keysList);
	}
	
	// collect the node values using inorder into a given list - O(n)
	private void collectValuesInorder(RBNode node, List keysList) {
		if (node == null) {
			return;
		}
		
		this.collectValuesInorder(node.getLeft(), keysList);
		keysList.addItem(node.getValue());
		this.collectValuesInorder(node.getRight(), keysList);
	}
	
	/**
	 * Helper classes
	 */
	
	// partial implementation of a list using dynamic (doubling) arrays
	private static class List {
		// The initial array length:
		private static final int INITIAL_LENGTH = 16;
		// The factor by which to increase the array once its full:
		private static final int INCREASE_FACTOR = 2;
		
		private Object[] storageArray;
		private int numberOfItemsStored;
		
		public List() {
			this.storageArray = new Object[INITIAL_LENGTH];
			this.numberOfItemsStored = 0;
		}
		
		// adds an item to the end of the list
		// worst case - O(n), amortized - O(1)
		public void addItem(Object item) {
			
			if (this.numberOfItemsStored == this.storageArray.length) {
				Object[] increasedArray = new Object[this.storageArray.length * List.INCREASE_FACTOR];
				
				for (int ix = 0; ix < this.numberOfItemsStored; ix++) {
					increasedArray[ix] = this.storageArray[ix];
				}
				
				this.storageArray = increasedArray;
			}
			
			this.storageArray[this.numberOfItemsStored] = item;
			this.numberOfItemsStored++;
		}
		
		// returns the size of the list - O(1)
		public int length() {
			return this.numberOfItemsStored;
		}
		
		// returns an item at a given index in the list - O(1)
		public Object getItem(int index) {
			return this.storageArray[index];
		}
	}
}