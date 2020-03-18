package Lists;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SOLinkedList<E> implements List<E> {

    private int size;
    private Node firstNode, lastNode;
    
    public SOLinkedList() {
        size = 0;
    }
    
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    
    @Override
    public boolean add(E e) {
        Node newNode = new Node(e);
        
        if(firstNode == null) {
            firstNode = newNode;
            lastNode = newNode;
        }
        else {
            lastNode.next = newNode;
            newNode.previous = lastNode;
            lastNode = newNode;
        }
        ++size;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        Node curNode = firstNode;
        boolean found = false;
        
        while(!found && curNode != null) {
            if(curNode.element.equals(o)) {
                found = true;
                curNode.counter++;
                sortNode(curNode);
            }
            curNode = curNode.next;
        }
        
        return found;
    }

    @Override
    public Iterator<E> iterator() {
        Iterator iter = new Iterator() {
            private Node<E> curNode = firstNode;
            private boolean first = true;
            
            @Override
            public boolean hasNext() {
                return first || curNode.next != null;
            }

            @Override
            public Object next() {
                E element;
                if(first && curNode!=null) {
                    element=curNode.element;
                    curNode.counter++;
                    first = false;
                }
                else if(hasNext()) {
                    element=(E)curNode.next.element;
                    curNode.next.counter++;
                    curNode = curNode.next;
                }
                else throw new IndexOutOfBoundsException("No next element to return"); //To change body of generated methods, choose Tools | Templates.
                return element;
            }
            
        }; 
        return iter;
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size-1];
        Node tempNode = firstNode;
        
        for(int i =0; i < array.length; ++i) {
            array[i] = tempNode.element;
            tempNode = tempNode.next;
        }
        
        return array;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        Node tempNode = firstNode;
        if(a.length < this.size) {
            a = (T[]) Array.newInstance(a.getClass(), this.size);
        }
        
        for(int i =0; i < this.size; ++i) {
            a[i] = (T)tempNode.element;
            tempNode = tempNode.next;
        }
        
        return a;
    }


    @Override
    public boolean remove(Object o) {
        Node curNode = firstNode;
        boolean found = false;
        
        if(firstNode.element.equals(o) && firstNode == lastNode) {
            firstNode = null;
            lastNode = null;
            found = true;
        }
        else if(firstNode.element.equals(o)) {
            firstNode = firstNode.next;
            firstNode.previous.next = null;
            firstNode.previous = null;
            found = true;
        }
        else if(lastNode.element.equals(o)) {
            lastNode = lastNode.previous;
            lastNode.next.previous = null;
            lastNode.next = null;
            found = true;
        }
        else {
        while(curNode!=null && !found) {
            if(curNode.element.equals(o)) {
                curNode.next.previous = curNode.previous;
                curNode.previous.next = curNode.next;
                curNode.next = null;
                curNode.previous = null;
                found = true;
            }
            curNode = curNode.next;
        }}
        
        if(found) --size;
        
        return found;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        boolean contain = true;
        Node<E> curNode = firstNode;
        
        while(curNode!=null && contain) {
            if(!c.contains(curNode.element)) contain = false;
            break;
        }
        
        return contain;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        c.forEach(e -> add(e));
        
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        c.forEach(e -> remove(c));
        
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Node<E> curNode = firstNode;
        boolean didContainAny = false;
        
        while(curNode!= null) {
            if(!c.contains(curNode.element)) {
                Node temp;
                curNode.next.previous = curNode.previous;
                curNode.previous.next = curNode.next;
                temp = curNode;
                curNode = curNode.next;
                temp.previous = null;
                temp.next = null;
                this.size--;
                didContainAny = true;
            }
            else curNode = curNode.next;
        }
        return didContainAny;
    }

    @Override
    public void clear() {
        firstNode = null;
        lastNode = null;
        size = 0;
    }
    
    private void sortNode(Node<E> o) {
        Node<E> curNode = o, tempNode;
        boolean sorted = false;
        
        while(!sorted && curNode!=null) {
            if(o == firstNode) sorted =true;
            else if(curNode.counter > o.counter) {
                if(o == lastNode) {
                    o.previous.next = null;
                    lastNode = o.previous;
                }
                else {
                o.next.previous = o.previous;
                o.previous.next = o.next;
                }
                curNode.next.previous = o;
                o.next = curNode.next;
                o.previous = curNode;
                curNode.next = o;
                sorted = true;
            }
            else if(curNode == firstNode) {
                if(o == lastNode) {
                    o.previous.next = null;
                    lastNode = o.previous;
                }
                else {
                    o.previous.next = o.next;
                    o.next.previous = o.previous;
                }
                o.previous = null;
                o.next = firstNode;
                firstNode.previous = o;
                firstNode = o;
                sorted = true;
            }
            curNode = curNode.previous;
        }
    }
    


    @Override
    public E get(int index) {
        Node<E> tempNode = null;
        
        if(index < size && index >= 0) {
            int counter;
            if(index > size/2) {
                counter = size-1;
                tempNode = lastNode;
                while(counter!=index) {
                    tempNode = tempNode.previous;
                    counter--;
                }
            }
            else {
                tempNode = firstNode;
                counter = 0;
                while(counter!=index) {
                    tempNode = tempNode.next;
                    counter++;
                }
            }
        }
        else throw new IndexOutOfBoundsException("Index is outside the bounds of this list");
        if(tempNode == null) return null;
        else {
            tempNode.counter++;
            sortNode(tempNode);
            return tempNode.element;
        }
    }

    @Override
    public E set(int index, E element) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void add(int index, E element) {
        add(element);
    }

    @Override
    public E remove(int index) {
        int counter = 0;
        Node<E> curNode = firstNode;
        
        if(index < 0 || index > size -1) throw new IndexOutOfBoundsException("Index is outside list bounds"); //To change body of generated methods, choose Tools | Templates.
        
        while(counter != index) {
            curNode = curNode.next;
            counter++;
        }
        
        curNode.previous.next = curNode.next;
        curNode.next.previous = curNode.previous;
        curNode.next = null;
        curNode.previous = null;
        
        return curNode.element;
    }

    @Override
    public int indexOf(Object o) {
        int index = -1;
        Node<E> curNode = firstNode;
        int counter = 0;
        
        while(curNode != null && index == -1) {
            if(curNode.element.equals(o)) index = counter;
            curNode = curNode.next;
            counter++;
        }
        
        return index;
    }

    @Override
    public int lastIndexOf(Object o) {
        int index = -1;
        Node<E> curNode = lastNode;
        int counter = size -1;
        
        while(curNode != null && index == -1) {
            if(curNode.element.equals(o)) index = counter;
            curNode = curNode.previous;
            counter--;
        }
        
        return index;
    }

    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        Node<E> startNode;
        Node<E> tempNode = firstNode;
        if(index < 0 || index > size) throw new IndexOutOfBoundsException("Index is outside list bounds"); //To change body of generated methods, choose Tools | Templates.
        int counter = 0;
        while(index!=counter) {
            tempNode = tempNode.next;
        }
        startNode = tempNode;
        
        ListIterator iter = new ListIterator<E>() {
            
            boolean started = false;
            Node<E> curNode;
            int curIndex = counter;
            
            @Override
            public boolean hasNext() {
                return curNode.next!=null || (!started && startNode!=null);
            }

            @Override
            public E next() {
                if(!hasNext()) throw new IndexOutOfBoundsException("No next element");
                if(!started) {
                    curNode = startNode;
                    started = true;
                }
                else {
                    curNode = curNode.next;
                }
                ++curIndex;
                return curNode.element;
            }

            @Override
            public boolean hasPrevious() {
                if(curNode!=null) {
                    return curNode.previous != null;
                }
                else return false;
            }

            @Override
            public E previous() {
                if(hasPrevious()) {
                    curNode = curNode.previous;
                }
                else throw new IndexOutOfBoundsException("No previous element");
                --curIndex;
                return curNode.element;
            }

            @Override
            public int nextIndex() {
                return curIndex+1;
            }

            @Override
            public int previousIndex() {
                return curIndex-1;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void set(Object e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void add(Object e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
             
        };
        return iter;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        List list = new SOLinkedList();
        int index = 0;
        Node<E> curNode = firstNode;
        while(index <= toIndex) {
            if(index >= fromIndex) {
                list.add(curNode.element);
            }
            curNode = curNode.next;
            ++index;
        }
        return list;   
    }
    
    public String toStringSummary() {
        String str = "{";
        Node curNode = firstNode;
        
        while(curNode != null) {
            if(curNode.previous != null) {
                if(curNode.previous.counter > curNode.counter) {
                    str+="\n";
                }
            }
            str+= (curNode.element.toString() + "(" + curNode.counter + ")");
            if(curNode.next != null) str += ",";
            curNode = curNode.next;
        }
        str+="}";
        
        str+= " : FirstNode - "+firstNode.element.toString() + ", LastNode - "+lastNode.element.toString() + ", List Size: " + size();
        
        return str;
    }
    
    @Override
    public String toString() {
        String str = "{";
        Node curNode = firstNode;
        
        while(curNode != null) {
            str+= (curNode.element.toString() + "(" + curNode.counter + ")");
            if(curNode.next != null) str += ",";
            curNode = curNode.next;
        }
        str+="}";
        
        return str;
    }
    
        public static void main(String[] args) {
        SOLinkedList<String> list = new SOLinkedList();
        
        String a = "Apple";
        String b = "Beers";
        String c = "Carrot";
        String d = "Dennys";
        String e = "Eggs";
        
        list.add(a);
        list.add(b);
        list.add(c);
        list.add(d);
        list.add(e);
       
        
        System.out.println("Added: " + list.toString());
        
        list.contains(c);
        list.contains(c);
        System.out.println("Called contains twice for Carrot: " + list.toString());
        
        list.contains(b);
        list.contains(b);
        System.out.println("Called contains twice for Beers: " + list.toString());
        
        list.remove(b);
        System.out.println("Removed Beers: " + list.toString());
        
        list.contains(a);
        list.contains(a);
        System.out.println("Called contains twice for Apple: " + list.toString());
        
        list.contains(e);
        System.out.println("Called contains for Egg: " + list.toString());

        list.get(3);
        System.out.println("Get called for index 3: " + list.toString());       
        
        list.get(3);
        System.out.println("Get called for index 3: " + list.toString());

        System.out.println("Calling iterator:");
        Iterator iter = list.iterator();     
        iter.forEachRemaining(consm -> System.out.println("Iterator: " + consm.toString()));

        
    }
    
    private class Node<E> {
        E element;
        int counter;
        Node next, previous;
        
        public Node(E element) {
            this.element = element;
            this.counter = 1;
        }
    }
}