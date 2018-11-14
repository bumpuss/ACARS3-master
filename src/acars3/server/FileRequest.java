/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acars3.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Michael
 */
public abstract class FileRequest 
{
    public abstract boolean isSending();
    private ServerListener client;
    
    public FileRequest(ServerListener client)
    {
        this.client = client;
    }
    
    public boolean isReceiving()
    {
        return !isSending();
    }
    
    public InputStream createInputStream() throws Exception
    {
        return null;
    }
    public OutputStream createOutputStream() throws Exception
    {
        return null;
    }
    
    public ServerListener getClient()
    {
        return client;
    }
}
