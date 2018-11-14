/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acars3.server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author Michael
 */
public class FileServerThread implements Runnable
{
    private Socket socket;
    private FileRequest request;
    
    public FileServerThread(Socket socket, FileRequest request)
    {
        this.socket = socket;
        this.request = request;
    }
    
    public void run()
    {
        try
        {
            InputStream netin = new BufferedInputStream(socket.getInputStream());
            OutputStream netout = socket.getOutputStream();

            byte[] array = new byte[4];

            for(int i = 0; i < 4; i++)
            {
                array[i] = (byte)(Math.random()*255);
            }
            netout.write(array);
            netout.flush();

            array[0] += 10;

            byte[] test = new byte[4];
            netin.read(test);

            boolean accept = true;

            for(int i = 0; i < 4; i++)
            {
                if(test[i] != array[i])
                {
                    accept = false;
                    break;
                }
            }

            if(!accept)
            {
                System.out.println("Failed nonce");
                socket.close();
                return;
            }

            try
            {
                if(request.isSending())
                {
                    InputStream in = request.createInputStream();
            
                    byte[] buffer = new byte[acars3.net.NetInfo.BUFFER_SIZE];

                    int len = 0;

                    do
                    {
                        len = in.read(buffer);

                        if(len > 0)
                        {
                            netout.write(buffer, 0, len);
                        }

                    }
                    while(len > 0);
                    
                    in.close();
                    System.out.println("Finished sending");
                }
                else
                {
                    OutputStream out = request.createOutputStream();

                    byte[] buffer = new byte[acars3.net.NetInfo.BUFFER_SIZE];
            
                    int len = 0;

                    do
                    {
                        len = netin.read(buffer);  

                        if(len > 0)
                        {
                            out.write(buffer, 0, len);
                        } 
                        
                    }
                    while(len > 0);

                    out.close();     
                    request.getClient().fileReceived();
                    System.out.println("Finished receiving");
                }
            }
            catch(Exception ex)
            {
            }
            
            socket.close();
            
        }
        catch(IOException ex)
        {
            ex.printStackTrace(System.err);
            
            if(!request.isSending())
            {
                request.getClient().fileNotReceived();
            }
        }
    }
}
