/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lists;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author harry
 * @param <E>
 */
public class SOArrayList<E> implements List<E> {

    private Node<E>[] array;
    private int size;
    private final int START_CAPACITY = 10;

    public SOArrayList() {
        size = 0;
        array = new Node[START_CAPACITY];
    }

    private void expandCapacity() {
        Node[] temp = new Node[array.length * 2];

        for (int i = 0; i < size; ++i) {
            temp[i] = array[i];
        }

        array = temp;
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
        if (size == array.length) {
            expandCapacity();
        }
        array[size] = newNode;
        ++size;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    private void sort(int index) {

        for (int i = index - 1; i >= 0; --i) {
         if(array[i].counter >= array[index].counter) {
             Node<E> temp = array[i+1];
             array[i+1] = array[index];
             array[index] = temp;
             break;
         }   
         else if(i == 0) {
             Node<E> tempNode = array[i];
             array[i] = array[index];
             array[index] = tempNode;
         }
        }
    }

    public int indexOf(Object o) {
        int index = -1;

        for (int i = 0; i < size; ++i) {
            if (array[i].element.equals(o)) {
                array[i].counter++;
                sort(i);
                index = i;
                break;
            }
        }

        return index;
    }

    @Override
    public boolean remove(Object o) {
        boolean found = false;

        for (int i = 0; i < size; ++i) {
            if (array[i].element == o) {
                for(int j = i; j < size; ++j) {
                    array[j] = array[j+1];
                }
                found = true;
                size--;
                break;
            }
        }
        return found;
    }
    

    @Override
    public E get(int index) {
        if (index > size || index < 0) {
            throw new ArrayIndexOutOfBoundsException("Requested index is outside the list bounds");
        }
        
        array[index].counter++;
        E temp = array[index].element;
        
        sort(index);
        
        /*
        if(index != 0) {
            for (int j = index; j >= 0; --j) {
                if (array[j].counter > array[index].counter) {
                    Node temp = array[index];
                    array[index] = array[j + 1];
                    array[j + 1] = temp;
                    index = j + 1;
                    break;
                }
            }
        }
        return array[index].element;
*/
        return temp;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int curNo = 0;

            @Override
            public boolean hasNext() {
                return curNo < size();
            }

            @Override
            public E next() {
                if (hasNext()) {
                    return array[curNo++].element;
                } else {
                    throw new ArrayIndexOutOfBoundsException("No next element in list");
                }
            }
        };
    }
    
    public String toStringSummary() {
        String str = "{";
        for (int i = 0; i < size; ++i) {
            if(i > 0) {
                if(array[i].counter < array[i-1].counter) {
                    str += "\n";
                }
            }
            str += (array[i].element.toString() + "(" + array[i].counter + ")");
            if (i != size - 1) {
                str += ",";
            }
        }
        str += "}";
        
        if(size > 0) {
            str+= "\n" + "First Element - " + array[0].element + ", Last Element - " + array[size-1].element + ", List Size: " + size;
        }

        return str;
    }

    @Override
    public String toString() {
        String str = "{";
        for (int i = 0; i < size; ++i) {
            
            str += (array[i].element.toString() + "(" + array[i].counter + ")");
            if (i != size - 1) {
                str += ",";
            }
        }
        str += "}";

        return str;
    }

    @Override
    public Object[] toArray() {
        Object[] tempArray = new Object[this.size];

        for (int i = 0; i < this.size; i++) {
            tempArray[i] = array[i].element;
        }

        return tempArray;
    }

    @Override
    public void clear() {
        array = new Node[START_CAPACITY];
        size = 0;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index > size - 1) {
            throw new ArrayIndexOutOfBoundsException("Requested index is outside the list bounds");
        }

        E temp = array[index].element;
        for (int i = index + 1; i < size; ++i) {
            array[i - 1] = array[i];
        }

        size--;
        array[size] = null;

        return temp;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        c.forEach(e -> this.add(e));

        return true;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size) {
            throw new ArrayIndexOutOfBoundsException("Requested index is outside the list bounds");
        }

        List list = new SOArrayList();
        for (int i = fromIndex; fromIndex < toIndex; ++i) {
            list.add(array[i].element);
        }

        return list;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        if (index < 0 || index > size - 1) {
            throw new ArrayIndexOutOfBoundsException("Requested index is outside the list bounds");
        }

        ListIterator iter = new ListIterator() {

            int curNo = index;

            @Override
            public boolean hasNext() {
                return curNo < size();
            }

            @Override
            public E next() {
                if (hasNext()) {
                    return array[curNo++].element;
                } else {
                    throw new ArrayIndexOutOfBoundsException("No next element in list");
                }
            }

            @Override
            public boolean hasPrevious() {
                return curNo > 0;
            }

            @Override
            public Object previous() {
                if (hasPrevious()) {
                    curNo--;
                    return array[curNo].element;
                } else {
                    throw new ArrayIndexOutOfBoundsException("No previous element in list");
                }
            }

            @Override
            public int nextIndex() {
                if (hasNext()) {
                    return curNo + 1;
                } else {
                    throw new ArrayIndexOutOfBoundsException("No next index in list");
                }
            }

            @Override
            public int previousIndex() {
                if (hasPrevious()) {
                    return curNo - 1;
                } else {
                    throw new ArrayIndexOutOfBoundsException("No previous index in list");
                }
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
    public boolean containsAll(Collection<?> c) {
        for (int i = 0; i < size; ++i) {
            if (!c.contains(array[i].element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        c.forEach(e -> this.add(e));
        return true;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if(a.length < this.size) {
            a = (T[]) Array.newInstance(a.getClass(), this.size);
        }
        
        for (int i = 0; i < this.size; i++) {
            a[i] = (T)array[i].element;
        }
        
        return a;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        c.forEach(e -> remove(e));
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        int newArraySize = 0;
        boolean didContainAny = false;
        Node[] tempArray = new Node[size];
        
        for(int i = 0; i < size; ++i) {
            if(c.contains(array[i].element)) {
                tempArray[newArraySize] = array[i];
                ++newArraySize;
                didContainAny = true;
            }
        }
        array = tempArray;
        this.size = newArraySize;
        
        return didContainAny;
    }

    @Override
    public E set(int index, E element) {
        throw new UnsupportedOperationException("Cannot set elements in a self organising list"); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void add(int index, E element) {
        this.add(element);
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size; i > 0; --i) {
            if (array[i].equals(o)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public ListIterator<E> listIterator() {
        return this.listIterator(0);
    }

    private class Node<E> {

        E element;
        int counter;

        public Node(E element) {
            this.element = element;
            this.counter = 1;
        }
    }

    public static void main(String[] args) {
        SOArrayList<String> list = new SOArrayList();

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
        System.out.println("Get called again for index 3: " + list.toString());

        System.out.println("Calling iterator:");
        Iterator iter = list.iterator();     
        iter.forEachRemaining(consm -> System.out.println("Iterator: " + consm.toString()));
    }

}
