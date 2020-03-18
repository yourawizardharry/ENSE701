
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Harrison.Cloake
 */

//This is it's own class and not an inner class, so that other classes can log errors to file using the static method.
public class Logger {
    public static void errorLog(Exception e) { 
        try {
        PrintWriter writer = new PrintWriter("src\\log.txt");
        e.printStackTrace(writer);
        writer.close();
        }
        catch(FileNotFoundException ee) {
            System.out.println("There was an issue writing an error to the logs. Please try restarting the applicatoin.");
        }
    }
}
