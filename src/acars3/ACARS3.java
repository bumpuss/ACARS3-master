/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acars3;

import acars3.server.*;
import acars3.client.*;
import com.flightsim.fsuipc.FSUIPC;
import java.io.*;

/**
 *
 * @author Michael
 */
public class ACARS3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        
        boolean client = args.length == 0 || !args[0].equals("server");
        

        //ConvertSchedule.main(args);
        //System.exit(1);
        
        
        
        if(!client)
        {
            ACARSserver.main(args);

            Thread.sleep(3000);
        }
        else
        {
            
            //System.setOut(new PrintStream(new FileOutputStream(new File("log_out.txt")), true));
            //System.setErr(new PrintStream(new FileOutputStream(new File("log_error.txt")), true));
            
            Client.main(args);
        }
        
        
        

    }
}
