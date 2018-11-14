/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acars3.server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Michael
 */
public class SendRequest extends FileRequest
{
    private File file;
    
    public SendRequest(ServerListener client, File file)
    {
        super(client);
        this.file = file;
    }
    
    public InputStream createInputStream() throws Exception
    {
       return new BufferedInputStream(new FileInputStream(file));
    }
    
    public boolean isSending()
    {
        return true;
    }
}
