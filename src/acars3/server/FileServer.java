/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acars3.server;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.net.InetAddress;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.HashMap;
import javax.crypto.SecretKey;

/**
 *
 * @author Michael
 */
public class FileServer implements Runnable
{
    private Map<InetAddress, FileRequest> requests;
    
    public FileServer()
    {
        requests = new HashMap<InetAddress, FileRequest>();
        
        Thread t = new Thread(this);
        t.start();
    }
    
    public void run()
    {
        ServerSocket server = null;
        try
        {
            server = new ServerSocket(acars3.net.NetInfo.FILE_PORT);
            System.out.println("File server open");
        }
        catch(Exception ex)
        {
            ex.printStackTrace(System.err);
            System.exit(1);
        }    
        
        while(true)
        {
            try
            {
                Socket socket = server.accept();
                System.out.println("File connected "+socket.getInetAddress());
                handleConnection(socket);
            }
            catch(Exception ex)
            {
                ex.printStackTrace(System.err);
            }
        }

    }
    
    public void handleConnection(Socket socket) throws Exception
    {
        FileRequest request = removeRequest(socket.getInetAddress());
        
        if(request != null)
        {
            Thread thread = new Thread(new FileServerThread(socket, request));
            thread.start();
        }
        else
        {
            System.out.println("No request");
            socket.close();
        }
    }
    
    
    public FileRequest removeRequest(InetAddress address)
    {
        synchronized(requests)
        {
            return requests.remove(address);
        }
    }
    
    public FileRequest getRequest(InetAddress address)
    {
        synchronized(requests)
        {
            return requests.get(address);
        }
    }
    
    public boolean hasActiveRequest(InetAddress address)
    {
        synchronized(requests)
        {
            return requests.containsKey(address);
        }
    }
    
    public void sendFile(ServerListener client, File file)
    {
        synchronized(requests)
        {
            requests.put(client.getInetAddress(), new SendRequest(client, file));
        }
    }
    
    public void receiveFile(ServerListener client, String directory, String name)
    {
        synchronized(requests)
        {
            requests.put(client.getInetAddress(), new ReceiveRequest(client, directory, name));
        }
    }
    
    public void receiveFile(ServerListener client, String directory, String name, SecretKey aesKey)
    {
        synchronized(requests)
        {
            requests.put(client.getInetAddress(), new EncryptedReceiveRequest(client, directory, name, aesKey));
        }
    }
}
