/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Lists.SOArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author harry
 */
public class ArrayListTest {
    
    public SOArrayList aList;
    
   @Test
   public void initalSize() {
       aList = new SOArrayList();
       assertEquals(aList.size(),0);
   }
   
   @Test
   public void addObjectSize() {
       aList = new SOArrayList();
       aList.add("test");
       assertEquals(aList.size(),1);
   }
   
   @Test
   public void removeObjectSize() {
       aList = new SOArrayList();
       String str = "test";
       aList.add(str);
       aList.remove(str);
       assertEquals(aList.size(), 0);
   }
   

    
}
