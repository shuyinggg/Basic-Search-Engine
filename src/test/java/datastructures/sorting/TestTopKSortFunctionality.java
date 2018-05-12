package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Searcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestTopKSortFunctionality extends BaseTest {
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testTopKSortBasics() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(1);
        list.add(7);
        list.add(8);
        list.add(56);
        list.add(2);
        list.add(0);
        list.add(91);
        list.add(15);
        list.add(42);
        list.add(33);
        list.add(12);
        
        IList<Integer> top = Searcher.topKSort(3, list);
        assertEquals(3, top.size());
        assertEquals(42, top.get(0));
        assertEquals(56, top.get(1));
        assertEquals(91, top.get(2));
    }
    
    @Test(timeout=SECOND)
    public void testIllegalInputException() {
        IList<Integer> list = new DoubleLinkedList<>();
        try {
            IList<Integer> top = Searcher.topKSort(-3, list);
        } catch (IllegalArgumentException ex) {
            //Do nothing
        }
    }
    
    @Test(timeout=SECOND)
    public void testSortBasics() {
        IList<Integer> ilist = new DoubleLinkedList<>();
        List<Integer> list = new ArrayList<>();
        for (int i = 50; i > 0; i--) {
            ilist.add(i);
            list.add(i);
        }

        IList<Integer> output = Searcher.topKSort(6, ilist);
        Collections.sort(list);
        for (int i = 0; i < 6; i++) {
            assertEquals(list.get(44 + i), output.get(i));
        }
    }
    
    
    
}
