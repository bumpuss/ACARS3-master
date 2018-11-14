/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acars3.server;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author Michael
 */
public class ReceiveRequest extends FileRequest
{
    protected String directory, name;
    public ReceiveRequest(ServerListener client, String directory, String name)
    {
        super(client);
        this.directory = directory;
        this.name = name;
    }
    
    public OutputStream createOutputStream() throws Exception
    {
        return new BufferedOutputStream(new FileOutputStream(new File(directory+"\\"+name))); 
    }
    
    public boolean isSending()
    {
        return false;
    }
}
