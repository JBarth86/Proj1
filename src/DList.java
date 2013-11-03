/* DList.java */

/**
 * This class manages Doubly Linked Lists
 * 
 * @author Jeremiah Barth
 *
 */
public class DList {
	DListNode head;
	DListNode tail;
	int size;
	
	DList() {
		head = tail = null;
		size = 0; 
	}
	
	public void insertFront(Object o) {
		if(size == 0) {
			head = tail = new DListNode(o);
		}
		else {
			head = new DListNode(o, head, null);
			head.next.prev = head;			
		}
		size++;
	}
	
	public void insertEnd(Object o) {
		if (size == 0) {
			head = tail = new DListNode(o);
		}
		else {
			tail = new DListNode(o, null, tail);
			tail.prev.next = tail;
		}
		size++;
		
	}
	
	class DListNode {
		Object item;
		DListNode next;
		DListNode prev;
		
		DListNode(Object o) {
			item = o;
			next = null;
			prev = null;
		}
		
		DListNode(Object o, DListNode n, DListNode p) {
			item = o;
			next = n;
			prev = p;
			
		}
	}
	
	public void iterateList() {
		DListNode x = head;
		
		while(x != null) {
			System.out.println(x.item);
			x = x.next;
		}
	}	
}
