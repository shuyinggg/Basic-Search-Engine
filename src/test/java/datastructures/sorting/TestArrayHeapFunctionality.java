package datastructures.sorting;

import static org.junit.Assert.assertTrue;

import misc.BaseTest;
import misc.exceptions.EmptyContainerException;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapFunctionality extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
        assertTrue(!heap.isEmpty());
    }
    
    @Test(timeout=SECOND)
    public void testRemoveNullException() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.removeMin();
        } catch (EmptyContainerException ex){
            //do nothing
        }
    }
    
    @Test(timeout=SECOND)
    public void testPeekNullException() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.peekMin();
        } catch (EmptyContainerException ex){
            //do nothing
        }
    }
    
    @Test(timeout=SECOND)
    public void testRemoveMinBasic() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        try {
            heap.removeMin();
        } catch (EmptyContainerException ex){
            //do nothing
        }
        assertEquals(0, heap.size());
        assertTrue(heap.isEmpty());
        try {
            heap.removeMin();
        } catch (EmptyContainerException ex){
            //do nothing
        }  
    }
    
    @Test(timeout=SECOND)
    public void testPeekMinBasic() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        try {
            heap.peekMin();
        } catch (EmptyContainerException ex){
            //do nothing
        }
        assertEquals(1, heap.size());
        assertTrue(!heap.isEmpty());
        heap.removeMin();
        try {
            heap.peekMin();
        } catch (EmptyContainerException ex){
            //do nothing
        }  
    }
    
    @Test(timeout=SECOND)
    public void testInsertThrowsException() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.insert(null);
        } catch (IllegalArgumentException ex){
            //do nothing
        }
    }
    
    @Test(timeout=SECOND)
    public void testInsertandRemoveMin() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        heap.insert(6);
        heap.insert(2);
        heap.insert(8);
        heap.insert(0);
        heap.insert(5);
        heap.insert(7);
        heap.insert(9);
        heap.insert(3);
        assertEquals(0,heap.removeMin());
        assertEquals(2,heap.removeMin());
        assertEquals(3,heap.removeMin());
        assertEquals(3,heap.removeMin());
        assertEquals(5,heap.removeMin());
        assertEquals(6,heap.removeMin());
        assertEquals(7,heap.removeMin());
        assertEquals(8,heap.removeMin());
        assertEquals(9,heap.removeMin());
        assertEquals(0,heap.size());    
    }
    
    
}
