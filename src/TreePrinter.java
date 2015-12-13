/**
* 
* @pre - notice that in line 15 i'm sending to the treper method a pointer of the type RBTree.RBNode node called root.
* you should put there an RBTree.RBNode pointer to your tree root
*/

import java.util.ArrayList;

public class TreePrinter {
	public boolean bykey = true;

	public static void printree(RBTree t, boolean bykey) {
		// Print a textual representation of t
		// bykey=True: show keys instead of values
		ArrayList<String> tree = trepr(t, t.getRoot(), bykey);
		for (String row : tree) {
			System.out.println(row);
		}
	}

	private static ArrayList<String> trepr(RBTree t, RBTree.RBNode node, boolean bykey) {
		// Return a list of textual representations of the levels in t
		// bykey=True: show keys instead of values
		ArrayList<String> a = new ArrayList<String>();
		String thistr;
		if (node == null) {
			a.add("#");
			return a;
		}
		if (bykey == true) {
			// String thistr = Integer.toString(node.key);
			// thistr = Integer.toString(node.key);
			thistr = Integer.toString(node.getKey()) + node.getColor();
		} else { // (bykey == false)
			thistr = node.getValue();
		}
		return conc(trepr(t, node.getLeft(), bykey), thistr, trepr(t, node.getRight(), bykey));
	}

	private static ArrayList<String> conc(ArrayList<String> left, String root, ArrayList<String> right) {
		// Return a concatenation of textual representations of a root node, its left node, and its right node
		// root is a string, and left and right are lists of strings

		int lwid = left.get(left.size() - 1).length();
		int rwid = right.get(right.size() - 1).length();
		int rootwid = root.length();

		String temp = "";
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i <= (lwid + 1); i++) {
			temp += " ";
		}
		temp += root;
		for (int i = 0; i <= (rwid + 1); i++) {
			temp += " ";
		}
		result.add(temp);
		temp = "";

		int ls = leftspace(left.get(0));
		int rs = rightspace(right.get(0));

		for (int j = 0; j <= ls; j++) {
			temp += " ";
		}
		for (int j = 0; j <= lwid - ls; j++) {
			temp += "_";
		}
		temp += "/";
		for (int j = 0; j <= rootwid; j++) {
			temp += " ";
		}
		temp += "\\";
		for (int j = 0; j <= rs; j++) {
			temp += "_";
		}
		for (int j = 0; j <= rwid - rs; j++) {
			temp += " ";
		}
		result.add(temp);
		temp = "";

		for (int i = 0; i < Math.max(left.size(), right.size()); i++) {
			String row = "";
			if (i < left.size()) {
				row += left.get(i);
			} else {
				for (int k = 0; k <= lwid; k++) {
					row += " ";
					// row += lwid*" ";
				}
			}
			for (int j = 0; j <= rootwid + 2; j++) {
				row += " ";
			}
			if (i < right.size()) {
				row += right.get(i);
			} else {
				for (int k = 0; k <= rwid; k++) {
					row += " ";
				}
			}
			result.add(row);
		}
		return result;
	}

	private static int leftspace(String row) {
		// helper for conc
		// row is the first row of a left node
		// returns the index of where the second whitespace starts
		int i = row.length() - 1;
		while (i >= 0 && row.charAt(i) == ' ') {
			i -= 1;
		}
		return i + 1;
	}

	private static int rightspace(String row) {
		// helper for conc
		// row is the first row of a right node
		// returns the index of where the first whitespace ends
		int i = 0;
		while (i < row.length() && row.charAt(i) == ' ') {
			i += 1;
		}
		return i;
	}
}
