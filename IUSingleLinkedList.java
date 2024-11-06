
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

/**
 * Single-linked node-based implementation of IndexedUnsortedList with a basic
 * Iterator including remove()
 *
 * @author Daylen Mathews cs221-3
 *
 * @param <T> The type of elements in this list
 */
public class IUSingleLinkedList<T> implements IndexedUnsortedList<T> {

    //Represents start of list
    private Node<T> head;

    //Represents end of list
    private Node<T> tail;

    //Keeps track of the size 
    private int size;

    //Assists the iterator in comparing changes
    private int versionNumber;

    public IUSingleLinkedList() {
        this.head = this.tail = null;
        this.size = 0;
        this.versionNumber = 0;

    }

    @Override
    public void addToFront(T element) {
        Node<T> newNode = new Node<T>(element);
        newNode.setNextNode(head);
        head = newNode;
        if (tail == null) { //or isEmpty() if it doesnt use head, or size == 0 or newNode.getNextNode()
            tail = newNode;
        }
        size++;
        versionNumber++;
    }

    @Override
    public void addToRear(T element) {
        Node<T> newNode = new Node<T>(element);
        if (isEmpty()) { //or size==0 or head == null
            head = newNode;
        }
        tail.setNextNode(newNode);
        tail = newNode;
        size++;
        versionNumber++;
    }

    @Override
    public void add(T element) {
        addToRear(element);
    }

    @Override
    public void addAfter(T element, T target) {
        Node<T> currentNode = head;
        while (currentNode != null && !currentNode.getElement().equals(target)) {
            currentNode = (Node<T>) currentNode.getNextNode();
        }

        if (currentNode == null) {
            throw new NoSuchElementException();
        }

        Node<T> newNode = new Node<T>(element);
        newNode.setNextNode(currentNode.getNextNode());
        currentNode.setNextNode(newNode);

        if (newNode.getNextNode() == null) {
            tail = newNode;
        }

        size++;
        versionNumber++;

    }

    @Override
    public void add(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        if (index == 0) {
            addToFront(element);
        } else {
            Node<T> curNode = head;
            for (int i = 0; i < index - 1; i++) {
                curNode = (Node<T>) curNode.getNextNode();
            }
        }
        Node<T> newNode = new Node<T>(element);
        newNode.setNextNode(curNode.getNextNode());
        curNode.setNextNode(newNode);
        if (newNode.getNextNode() == null) { //or curNode == tail
            tail = newNode;
        }
        size++;
        versionNumber++;
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        T returnValue;
        returnValue = head.getElement();

        if (size() == 1) {
            head = null;
            tail = null;
        } else {
            Node<T> currentNode = head;
            head = (Node<T>) currentNode.getNextNode();
        }

        size--;
        versionNumber++;
        return returnValue;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        T returnValue;

        if (size() == 1) {
            returnValue = head.getElement();
            head = null;
            tail = null;
        } else {
            Node<T> current = head;
            for (int i = 0; i < size() - 2; i++) {
                current = (Node<T>) current.getNextNode();
            }
            returnValue = tail.getElement();
            current.setNextNode(null);
            tail = current;;
        }

        @Override
        public T remove
        (T element
            
        ) {
        if (isEmpty()) {
                throw new NoSuchElementException();
            }

            T retVal;

            //Check if element is at head
            if (element.equals(head.getElement())) {
                retVal = head.getElement();
                head = (Node<T>) head.getNextNode();
                //update tail if the list becomes empty
                if (head == null) {
                    tail = null;
                }
                size--;
                versionNumber++;
                return retVal;
            }

            Node<T> currentNode = head;
            while (currentNode.getNextNode() != null //or currentNode == tail 
                    && !currentNode.getNextNode().getElement().equals(element)) {
                currentNode = (Node<T>) currentNode.getNextNode();
            }
            if (currentNode == tail) {
                throw new NoSuchElementException();
            }
            retVal = currentNode.getNextNode().getElement();
            currentNode.setNextNode(currentNode.getNextNode().getNextNode());

            size--;
            versionNumber++;

            return retVal;
        }
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        T returnValue;

        if (index == 0) { // first node
            returnValue = removeFirst();

        } else {
            Node<T> currentNode = head;
            Node<T> previousNode = null;

            for (int i = 0; i < index; i++) {
                previousNode = currentNode;
                currentNode = (Node<T>) currentNode.getNextNode();
            }

            returnValue = currentNode.getElement();
            previousNode.setNextNode(currentNode.getNextNode());
            if (currentNode.getNextNode() == null) {
                tail = previousNode;
            }
            size--;
            versionNumber++;
        }
        return returnValue;
    }

    @Override
    public void set(int index, T element) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        Node<T> currentNode = head;
        for (int i = 0; i < index; i++) {
            currentNode = (Node<T>) currentNode.getNextNode();
        }
        currentNode.setElement(element);
        versionNumber++;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        T returnValue;

        if (index == 0) { // first node
            returnValue = head.getElement();

        } else {
            Node<T> currentNode = head;
            for (int i = 0; i < index; i++) {
                currentNode = (Node<T>) currentNode.getNextNode();
            }

            returnValue = currentNode.getElement();

        }
        return returnValue;
    }

    @Override
    public int indexOf(T element) {
        Node<T> currentNode = head;
        int currentIndex = 0;
        while (currentNode != null && !currentNode.getElement().equals(element)) {

            currentNode = (Node<T>) currentNode.getNextNode();
            currentIndex++;
        }

        if (currentNode == null) {
            currentIndex = -1;
        }
        return currentIndex;
    }

    @Override
    public T first() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return head.getElement();
    }

    @Override
    public T last() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return tail.getElement();
    }

    @Override
    public boolean contains(T target) {
        return indexOf(target) > -1;
    }

    @Override
    public boolean isEmpty() {
        return size == 0; //return head = null
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");

        for (T element : this) {
            stringBuilder.append(element.toString());
            stringBuilder.append(", ");
        }

        if (size() > 0) {
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return new SLLIterator(); // Basic iterator, not to be confused with listIterator (for double linked
        // lists)
    }

    @Override
    public ListIterator<T> listIterator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
    }

    @Override
    public ListIterator<T> listIterator(int startingIndex) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
    }

    @Override
    public T remove(T element) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private static class curNode {

        private static Object getNextNode() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public curNode() {
        }
    }

    /**
     * Iterator class to use within IUSingleLinkedList, to avoid breaking
     * encapsulation. Do not need to include a generic <T> in the private class.
     * This would create a shadow generic.
     */
    private class SLLIterator implements Iterator<T> {

        private Node<T> iterNextNode;
        private boolean canRemove;
        private int iterVersionNumber;

        /**
         * Constructor to initialize the Iterator
         */
        public SLLIterator() {
            iterNextNode = head;
            canRemove = false;
            iterVersionNumber = versionNumber;
        }

        @Override
        public boolean hasNext() {
            if (iterVersionNumber != versionNumber) {
                throw new ConcurrentModificationException();
            }
            return iterNextNode != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            T returnValue = iterNextNode.getElement();
            iterNextNode = (Node<T>) iterNextNode.getNextNode();
            canRemove = true;
            return returnValue;
        }
    }

    public void remove(int iterVersionNumber, Object iterNextNode, boolean canRemove) {
        if (iterVersionNumber != versionNumber) {
            throw new ConcurrentModificationException();
        }

        if (!canRemove) {
            throw new IllegalStateException();
        }
        canRemove = false;
        Node<T> prevPrevNode = null;
        if (head.getNextNode() == iterNextNode) {
            head = (Node<T>) iterNextNode;
        } else {
            prevPrevNode = head;
            while (prevPrevNode.getNextNode().getNextNode() != iterNextNode) {
                prevPrevNode = (Node<T>) prevPrevNode.getNextNode();
            }
            prevPrevNode.setNextNode(iterNextNode);
        }
        if (iterNextNode == null) {
            tail = prevPrevNode;
        }

        versionNumber++;
        iterVersionNumber++;
        size--;
    }

}       // End of Iterator class
// End of IUSingleLinkedList class

