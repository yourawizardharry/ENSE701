/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lists;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 *
 * @author harry
 */
public class DocumentReaderLinkedList {
        public static void main(String[] args) {
        SOLinkedList list = new SOLinkedList();
        String line;
        try {
        BufferedReader reader = new BufferedReader(new FileReader("RandomText.txt"));
        while((line = reader.readLine()) != null) {
            String[] sarray = line.split(" ");
            for(String s : sarray) {
                s = s.toLowerCase();
                s = s.replace(".", "");
                if(s.equals(""));
                else if(!list.contains(s)) {
                    list.add(s);
                }
            }
        }
        }
        catch(Exception e) {
            System.out.println(e.toString());
        }
        System.out.println(list.toStringSummary());
    }
}
