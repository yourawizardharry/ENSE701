/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lists;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author harry
 */
public class DocumentReaderArrayList {
    public static void main(String[] args) {
        SOArrayList list = new SOArrayList();
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
