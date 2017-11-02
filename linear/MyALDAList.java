package alda.linear;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 
 * @author Johan Ekh, johan.ekh@hotmail.com
 * @author Alexanderi Malmgren, alma1060@student.su.se
 *
 */
public class MyALDAList<T> implements ALDAList<T> {
	private Node<T> first;
	private int size;

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {

			private Node<T> currentNode = first;
			private Node<T> previousNode = null;
			private Node<T> previousNode2 = null;
			boolean removed = false;

			@Override
			public boolean hasNext() {
				return currentNode != null;
			}

			@Override
			public T next() {
				if (currentNode == null) {
					throw new NoSuchElementException();
				}
				T toReturn = currentNode.data;
				previousNode2 = previousNode;
				previousNode = currentNode;
				currentNode = currentNode.next;
				removed = false;
				return toReturn;
			}

			@Override
			public void remove() {
				if (previousNode == null || removed) {
					throw new IllegalStateException();
				}

				if (previousNode2 == null) {
					first = currentNode;
					previousNode = null;
				} else {
					previousNode2.next = currentNode;
					previousNode = previousNode2;
				}
				size--;
				removed = true;

			}
		};
	}

	private boolean isEmpty() {
		return first == null;
	}

	@Override
	public void add(T element) {
		if (first == null) {
			first = new Node<>(element);
			size++;
		} else {
			for (Node<T> temp = first; temp != null; temp = temp.next) {
				if (temp.next == null || temp.next.equals(null)) {
					temp.next = new Node<>(element);
					size++;
					return;
				}
			}
		}
	}

	@Override
	public void add(int index, T element) {
		int nodecounter = 0;

		if (index == 0) {
			Node<T> newNode = new Node<>(element);
			newNode.next = first;
			first = newNode;
			size++;
			return;
		}

		for (Node<T> temp = first; temp != null; temp = temp.next) {

			if (index < 0 || index > size) {
				// för lågt eller högt index
				throw new IndexOutOfBoundsException();
			}

			if (temp.next == null || temp.next.equals(null) || (index == size && index == nodecounter + 1)) {
				// sist i listan
				temp.next = new Node<>(element);
				size++;
				return;
			}

			if (nodecounter + 1 == index) {
				// i mitten
				Node<T> newNode = new Node<>(element);
				newNode.next = temp.next;
				temp.next = newNode;
				size++;
				return;
			}

			nodecounter++;
		}
		throw new IndexOutOfBoundsException();
	}

	@Override
	public T remove(int index) {
		if (isEmpty() || index > size - 1 || index < 0) {
			throw new IndexOutOfBoundsException();
		}

		int nodecounter = 0;
		Node<T> prevNode = null;
		T nodeDataToReturn;

		for (Node<T> temp = first; temp != null; temp = temp.next) {
			if (index == 0) {
				// först i listan
				nodeDataToReturn = first.data;
				first = first.next;
				size--;
				return nodeDataToReturn;
			}
			if (nodecounter == index && temp.next == null || temp.next.equals(null)) {
				// sist i listan
				nodeDataToReturn = temp.data;
				prevNode.next = null;
				size--;
				return nodeDataToReturn;
			}
			if (index > nodecounter && temp.next == null || temp.next.equals(null)) {
				// för högt index
				throw new IndexOutOfBoundsException();
			}

			if (prevNode != null && nodecounter == index) {
				// i mitten
				prevNode.next = temp.next;
				size--;
				return temp.data;
			}

			prevNode = temp;
			nodecounter++;
		}

		return null;
	}

	@Override
	public boolean remove(T element) {
		int nodecounter = 0;
		Node<T> prevNode = null;

		for (Node<T> temp = first; temp != null; temp = temp.next) {
			if (nodecounter == 0 && (temp.data == element || temp.data.equals(element))) {
				// först i listan
				first = first.next;
				size--;
				return true;
			}
			
			if ((temp.data == element || temp.data.equals(element)) && (temp.next == null || temp.next.equals(null))) {
				// sist i listan
				prevNode.next = null;
				size--;
				return true;
			}
			
			if (temp.next == null || temp.next.equals(null)) {
				// inte funnen
				return false;
			}

			if (prevNode != null && (element == null || element.equals(temp.data))) {
				prevNode.next = temp.next;
				size--;
				return true;
			}

			prevNode = temp;
			nodecounter++;
		}

		return false;
	}

	@Override
	public T get(int index) {
		int nodecounter = 0;

		for (Node<T> temp = first; temp != null; temp = temp.next) {
			if (nodecounter == index) {
				return temp.data;
			}
			nodecounter++;
		}

		throw new IndexOutOfBoundsException();
	}

	@Override
	public boolean contains(T element) {
		for (Node<T> temp = first; temp != null; temp = temp.next) {
			if (element == null && temp.data == null) {
				return true;
			} else if (temp.data != null && (temp.data == element || temp.data.equals(element))) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int indexOf(T element) {
		int nodecounter = 0;
		for (Node<T> temp = first; temp != null; temp = temp.next) {
			if (element == null && temp.data == null) {
				return nodecounter;
			} else if (temp.data != null && (temp.data == element || temp.data.equals(element))) {
				return nodecounter;
			}
			nodecounter++;
		}
		return -1;
	}

	@Override
	public void clear() {
		size = 0;
		first = null;
	}

	@Override
	public int size() {
		return size;
	}

	private static class Node<T> {
		T data;
		Node<T> next;

		public Node(T data) {
			this.data = data;
		}

		@Override
		public String toString() {
			return "" + data;
		}
	}

	@Override
	public String toString() {
		String string = "";
		if (first != null) {
			string = first.toString();
			for (Node<T> temp = first.next; temp != null; temp = temp.next) {
				if (temp.data == null) {
					string += ", null";
				} else {
					string += ", " + temp.data.toString();
				}
			}
		}

		return "[" + string + "]";
	}
}
