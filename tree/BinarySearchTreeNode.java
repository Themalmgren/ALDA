package alda.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Detta är den enda av de tre klasserna ni ska göra några ändringar i. (Om ni
 * inte vill lägga till fler testfall.) De ändringar som är tillåtna är dock
 * begränsade av följande:
 * <ul>
 * <li>Ni får INTE lägga till några fler instansvariabler.
 * <li>Ni får INTE lägga till några statiska variabler.
 * <li>Ni får INTE använda några loopar någonstans.
 * <li>Ni FÅR lägga till fler metoder, dessa ska då vara privata.
 * </ul>
 * 
 * @author henrikbe
 * @author Johan Ekh, joek7107
 * @author Alexanderi Malmgren, alma1060
 * 
 * @param <T>
 */
public class BinarySearchTreeNode<T extends Comparable<T>> {

	private T data;
	private BinarySearchTreeNode<T> left;
	private BinarySearchTreeNode<T> right;

	public BinarySearchTreeNode(T data) {
		this.data = data;
	}

	public boolean add(T data) {
		if (data == null) {
			return false;
		}

		if (data.compareTo(this.data) > 0) {
			if (right == null) {
				right = new BinarySearchTreeNode<>(data);
				return true;
			}
			return right.add(data);
		} else if (data.compareTo(this.data) < 0) {
			if (left == null) {
				left = new BinarySearchTreeNode<>(data);
				return true;
			}
			return left.add(data);
		}
		// ger false vid dubbletter
		return false;
	}

	public BinarySearchTreeNode<T> remove(T data) {
		BinarySearchTreeNode<T> root = this;
		if (root.data == data) {
			if (right == null) {
				// rot har inget högerbarn
				// spelar ingen roll om rot har vänsterbarn eller ej
				return left;
			}
			if (left == null) {
				// rot har inget vänsterbarn
				return right;
			}
			if (right.left == null) {
				// right har inget vänsterbarn
				// right flyttas till rot
				// spelar ingen roll om rot har vänsterbarn eller ej
				right.left = left;
				return right;
			}

			List<BinarySearchTreeNode<T>> nodes = findMinNode(right);
			BinarySearchTreeNode<T> parentOfNodeToRemove = nodes.get(0);
			BinarySearchTreeNode<T> nodeToRemove = nodes.get(1);

			root.data = nodeToRemove.data;
			if (nodeToRemove.right == null) {
				parentOfNodeToRemove.left = null;
			} else {
				parentOfNodeToRemove.left = nodeToRemove.right;
			}

			return root;
		}

		removeHelper(data);
		return root;
	}

	private void removeHelper(T data) {
		if (right == null && left == null) {
			// nod inte hittad
			return;
		}

		if (data.compareTo(this.data) > 0) {
			// kollar högerbarn
			if (right != null) {
				if (right.data.equals(data)) {
					// högerbarn är sökta datat
					removeRightChildren();
				} else {
					right.removeHelper(data);
				}
			} else {
				// datat är större än fokusnoden och högerbarn finns ej
				return;
			}
		} else if (data.compareTo(this.data) < 0) {
			// kollar vänsterbarn
			if (left != null) {
				if (left.data.equals(data)) {
					// vänsterbarn är sökta datat
					removeLeftChildren();
				} else {
					left.removeHelper(data);
				}
			} else {
				// datat är mindre än fokusnoden och vänsterbarn finns ej
				return;
			}
		}
	}

	private void removeLeftChildren() {
		if (left.left == null && left.right == null) {
			// vänsterbarn är löv och kan således tas bort direkt
			left = null;
		} else if (left.left != null && left.right == null) {
			// noden har vänsterbarn, men inte högerbarn
			left = left.left;
		} else if (left.left == null && left.right != null) {
			// noden har högerbarn, men inte vänsterbarn
			left = left.right;
		} else if (left.left != null && left.right != null) {
			// noden har två barn
			removeLeftWithChildren();
		}
	}

	private void removeRightChildren() {
		if (right.right == null && right.left == null) {
			// högerbarn är löv och kan således tas bort direkt
			right = null;
		} else if (right.right != null && right.left == null) {
			// noden har högerbarn, men inte vänsterbarn
			right = right.right;
		} else if (right.right == null && right.left != null) {
			// noden har vänsterbarn, men inte högerbarn
			right = right.left;
		} else if (right.right != null && right.left != null) {
			// noden har två barn
			removeRightWithChildren();
		}
	}

	private BinarySearchTreeNode<T> removeRightWithChildren() {
		if (right.left.right != null) {
			// right är noden som ska få nytt värde
			List<BinarySearchTreeNode<T>> nodes = findMaxNode(right.left);
			BinarySearchTreeNode<T> parentOfNodeToRemove = nodes.get(0);
			BinarySearchTreeNode<T> nodeToRemove = nodes.get(1);

			right.data = nodeToRemove.data;
			if (nodeToRemove.right == null) {
				if (nodeToRemove.left == null) {
					parentOfNodeToRemove.right = null;
				} else {
					parentOfNodeToRemove.right = nodeToRemove.left;
				}
			} else {
				parentOfNodeToRemove.right = nodeToRemove.left;
			}
		} else {
			// flyttar om referenserna från objektet som sedan tas bort
			right.left.right = right.right;
			right = right.left;
		}
		return null;
	}

	private BinarySearchTreeNode<T> removeLeftWithChildren() {
		if (left.right.left != null) {
			// left är noden som ska få nytt värde
			List<BinarySearchTreeNode<T>> nodes = findMinNode(left.right);
			BinarySearchTreeNode<T> parentOfNodeToRemove = nodes.get(0);
			BinarySearchTreeNode<T> nodeToRemove = nodes.get(1);

			left.data = nodeToRemove.data;
			if (nodeToRemove.right == null) {
				if (nodeToRemove.left == null) {
					parentOfNodeToRemove.left = null;
				} else {
					parentOfNodeToRemove.left = nodeToRemove.right;
				}
			} else {
				parentOfNodeToRemove.left = nodeToRemove.right;
			}
		} else {
			// flyttar om referenserna från objektet som sedan tas bort
			left.right.left = left.left;
			left = left.right;
		}
		return null;
	}

	private List<BinarySearchTreeNode<T>> findMaxNode(BinarySearchTreeNode<T> node) {
		if (node.right != null && node.right.right != null) {
			// node.right kan inte vara null i första varvet
			return findMaxNode(node.right);
		}
		List<BinarySearchTreeNode<T>> nodes = new ArrayList<>();
		nodes.add(node);
		nodes.add(node.right);
		return nodes;
	}

	private List<BinarySearchTreeNode<T>> findMinNode(BinarySearchTreeNode<T> node) {
		if (node.left != null && node.left.left != null) {
			// node.left kan inte vara null i första varvet
			return findMinNode(node.left);
		}
		List<BinarySearchTreeNode<T>> nodes = new ArrayList<>();
		nodes.add(node);
		nodes.add(node.left);
		return nodes;
	}

	public boolean contains(T data) {
		if (this.data.equals(data)) {
			return true;
		}
		if (left != null && this.data.compareTo(data) > 0) {
			return left.contains(data);
		}
		if (right != null && this.data.compareTo(data) < 0) {
			return right.contains(data);
		}
		// noden fanns ej
		return false;
	}

	public int size() {
		return (size(this));
	}

	private int size(BinarySearchTreeNode<T> node) {
		if (node == null) {
			return (0);
		}
		return (size(node.left) + size(node.right) + 1);
	}

	public int depth() {
		return (depth(this) - 1);
	}

	private int depth(BinarySearchTreeNode<T> node) {
		if (node == null) {
			return (0);
		}
		int depthLeft = depth(node.left) + 1;
		int depthRight = depth(node.right) + 1;

		// använder den djupaste
		return (Math.max(depthLeft, depthRight));
	}

	@SuppressWarnings("unused")
	private T findMin() {
		// vi använder findMinNode
		if (left != null) {
			left.findMin();
		} else {
			return this.data;
		}
		return null;
	}

	public String toString() {
		String result = "";
		if (left != null) {
			result = result + left.toString() + ", ";
		}
		result = result + data;

		if (right != null) {
			result = result + ", " + right.toString();
		}
		return result;
	}
}