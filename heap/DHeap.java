// Klassen i denna fil måste döpas om till DHeap för att testerna ska fungera. 
package alda.heap;

import java.util.NoSuchElementException;

//DHeap class
//
//CONSTRUCTION: with optional capacity (that defaults to 100)
//            or an array containing initial items
//
//******************PUBLIC OPERATIONS*********************
//void insert( x )       --> Insert x
//Comparable deleteMin( )--> Return and remove smallest item
//Comparable findMin( )  --> Return smallest item
//boolean isEmpty( )     --> Return true if empty; else false
//void makeEmpty( )      --> Remove all items
//******************ERRORS********************************
//Throws UnderflowException as appropriate

/**
 * Implements a d heap based on Marl Allen Weiss original binary heap.
 * 
 * 
 * @author Mark Allen Weiss
 * @author Johan Ekh, joek7107
 * @author Alexanderi Malmgren, alma1060
 */
public class DHeap<AnyType extends Comparable<? super AnyType>> {
	private static final int DEFAULT_CAPACITY = 10;
	private static final int DEFAULT_ARITY = 2;

	private int currentSize; // Number of elements in heap
	private AnyType[] array; // The heap array
	private int d = 2;

	/**
	 * Construct the heap.
	 */
	public DHeap() {
		this(DEFAULT_ARITY);
	}

	public DHeap(int d) {
		if (d < 2) {
			throw new IllegalArgumentException();
		}
		this.d = d;
		currentSize = 0;
		array = (AnyType[]) new Comparable[DEFAULT_CAPACITY + 1];
	}

	public int parentIndex(int childIndex) {
		if (childIndex < 2) {
			throw new IllegalArgumentException();
		}
		return (childIndex - 2) / d + 1;
	}

	public int firstChildIndex(int parentIndex) {
		if (parentIndex < 1) {
			throw new IllegalArgumentException();
		}
		return d * (parentIndex - 1) + 2;
	}

	public int size() {
		return currentSize;
	}

	public AnyType get(int index) {
		return array[index];
	}

	/**
	 * Insert into the priority queue, maintaining heap order. Duplicates are
	 * allowed.
	 * 
	 * @param x
	 *            the item to insert.
	 */
	public void insert(AnyType x) {
		if (currentSize == array.length - 1)
			enlargeArray(array.length * 2 + 1);

		// Percolate up
		int hole = ++currentSize;

		array[hole] = x;
		
		for (; (hole > 1 && x.compareTo(array[parentIndex(hole)]) < 0); hole = parentIndex(hole)) {
			array[hole] = array[parentIndex(hole)];
		}
		array[hole] = x;
	}

	private void enlargeArray(int newSize) {
		AnyType[] old = array;
		array = (AnyType[]) new Comparable[newSize];
		for (int i = 0; i < old.length; i++)
			array[i] = old[i];
	}

	/**
	 * Find the smallest item in the priority queue.
	 * 
	 * @return the smallest item, or throw an UnderflowException if empty.
	 */
	public AnyType findMin() {
		if (isEmpty())
			throw new NoSuchElementException("Underflow Exception");
		return array[1];
	}

	/**
	 * Remove the smallest item from the priority queue.
	 * 
	 * @return the smallest item, or throw an UnderflowException if empty.
	 */
	public AnyType deleteMin() {
		if (isEmpty())
			throw new NoSuchElementException("Underflow Exception");

		AnyType minItem = findMin();
		array[1] = array[currentSize--];
		percolateDown(1);

		return minItem;
	}
	
	/**
	 * Test if the priority queue is logically empty.
	 * 
	 * @return true if empty, false otherwise.
	 */
	public boolean isEmpty() {
		return currentSize == 0;
	}

	/**
	 * Make the priority queue logically empty.
	 */
	public void makeEmpty() {
		currentSize = 0;
		array = (AnyType[]) new Comparable[DEFAULT_CAPACITY + 1];
	}

	/**
	 * Internal method to percolate down in the heap.
	 * 
	 * @param hole
	 *            the index at which the percolate begins.
	 */
	private void percolateDown(int hole) {
		int child = firstChildIndex(hole);
		AnyType tmp = array[hole];

		for (; firstChildIndex(hole) <= currentSize; hole = child) {
			int tmpChild = firstChildIndex(hole);
			child = firstChildIndex(hole);

			for (int i = 0; i < d && tmpChild != currentSize; i++) {
				if (array[tmpChild + 1].compareTo(array[child]) < 0) {
					child = tmpChild + 1;
				}
				tmpChild++;
			}
			if (array[child].compareTo(tmp) < 0) {
				array[hole] = array[child];
				array[child] = tmp;
			} else {
				break;
			}
		}
	}

	public String toString() {
		String output = "[";
		if (size() > 1) {
			output += array[1].toString();
		}

		for (int i = 2; i <= size(); i++)
			output += ", " + array[i].toString();
		return output + "]";
	}

	public static void main(String[] args) {
		int numItems = 10000;
		DHeap<Integer> h = new DHeap<>();
		int i = 37;

		for (i = 37; i != 0; i = (i + 37) % numItems)
			h.insert(i);
		for (i = 1; i < numItems; i++)
			if (h.deleteMin() != i)
				System.out.println("Oops! " + i);
	}
}
