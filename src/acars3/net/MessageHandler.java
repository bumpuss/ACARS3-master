/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acars3.net;

import java.net.*;
import java.io.*;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 *
 * @author Michael
 */
public class MessageHandler implements Runnable
{
    public static final int TIMEOUT = 5000;
    
    public static final boolean DEBUG = true;
    
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private Socket socket;
    
    private String ip;
    private int port;
    

    public MessageHandler()
    {
        
    }
    public MessageHandler(Socket s)
    {
        this(s, false);
    }
    
    public MessageHandler(Socket s, boolean reversed)
    {
        connect(s, reversed);
        
    }
    public MessageHandler(String ip, int port) throws IOException
    {
        System.out.println(ip+" "+port);
        Socket socket = new Socket(ip, port);
        connect(socket, true);
        this.ip = ip;
        this.port = port;
    }
    
    public InetAddress getInetAddress()
    {
        return socket.getInetAddress();
    }
    
    public void reconnect()
    {
        try
        {
            connect(new Socket(ip, port), false);
        }
        catch(IOException ex){}
    }
    private void connect(Socket s, boolean reversed)
    {
        socket=s;

        if(reversed)
        {
                try
                {
                        out=new ObjectOutputStream(socket.getOutputStream());
                        in=new ObjectInputStream(socket.getInputStream());
                }
                catch(IOException e){}
        }
        else
        {
                try
                {
                        in=new ObjectInputStream(socket.getInputStream());
                        out=new ObjectOutputStream(socket.getOutputStream());
                }
                catch(IOException e){}
        }
    }
    
    public void start()
    {
        Thread t = new Thread(this, getClass().getName());
        t.start();
    }
    
    public void run()
    {
            if(DEBUG)
            {
                System.out.println("Running "+getClass().getName());
            }

            while(true)
            {
                    try
                    {
                            NetMessage msg=getNextMessage();

                            if(msg==null)
                            {
                                if(DEBUG)
                                {
                                    System.err.println("Msg is null.");
                                }
                                    
                                break;
                            }

                            //System.out.println("Received "+msg.getClass().getName());

                            if(!handleMessage(msg))
                            {
                                break;
                            }
                    }
                    catch(Exception e)
                    {
                            e.printStackTrace(System.err);
                    }

            }
            disconnect();
	}
    
        public void disconnect()
        {

                try
                {
                        out.close();
                        in.close();
                        socket.close();
                }
                catch(Exception e)
                {
                        e.printStackTrace(System.err);
                }

                if(DEBUG)
                {
                    System.out.println("[CONNECT] Disconnected "+getIP());
                }
                
                disconnect_action();
        }
        
        public void disconnect_action()
        {
            
        }

	public synchronized NetMessage getNextMessage()
	{
		try
		{
			return (NetMessage)in.readObject();
		}
		catch(Exception e)
		{
			return null;
		}
	}
        public int getPort()
	{
		return socket.getPort();
	}
	public String getIP()
	{
		String temp=socket.getInetAddress().toString();

		if(temp.indexOf("/")>=0)
		{
			temp=temp.substring(temp.indexOf("/")+1);
		}

		return temp;
	}

	public boolean send(NetMessage msg)
	{
            if(msg == null)
            {
                return false;
            }
            
		try
		{
			out.writeObject(msg);

			if(DEBUG)
                        {
                            System.out.println("Sent "+msg.getClass().getName()+" "+getClass().getName());
                        }

			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace(System.err);

			return false;
		}
                
            
	}
    
    public boolean isConnected()
    {
        return socket.isConnected();
    }
    public boolean handleMessage(NetMessage msg)
    {
        if(DEBUG)
        {
            System.out.println("Received "+msg.getClass().getName()+" "+getClass().getName());
        }
        
        try
        {
            
            switch(msg.getType())
            {
                case NetMessage.LOGIN: 
                    handleLogin((LoginMessage)msg); 
                    return true;
                case NetMessage.LOGIN_RETURN: 
                    handleLoginReturn((LoginReturnMessage)msg); 
                    return true;
                case NetMessage.CHAT:
                    handleChat((ChatMessage)msg);
                    return true;
                case NetMessage.STATUS:
                    handleStatus((StatusMessage)msg);
                    return true;
                case NetMessage.PRIVATE:
                    handlePrivate((PrivateMessage)msg);
                    return true;
                case NetMessage.CONNECT:
                    handleConnect((ConnectMessage)msg);
                    return true;
                case NetMessage.DISCONNECT:
                    handleDisconnect((DisconnectMessage)msg);
                    return true;
                case NetMessage.REQUEST_BOOKED:
                    handleRequestBooked((RequestBookedMessage)msg);
                    return true;
                case NetMessage.BOOKED_FLIGHT:
                    handleBookedFlight((BookedFlightMessage)msg);
                    return true;
                case NetMessage.REQUEST_POSITION:
                    handleRequestPosition((RequestPositionMessage)msg);
                    return true;
                case NetMessage.POSITION_RESPONSE:
                    handlePositionResponse((PositionResponseMessage)msg);
                    return true;
                case NetMessage.REQUEST_ZULU_TIME:
                    handleRequestZuluTime((RequestZuluTimeMessage)msg);
                    return true;
                case NetMessage.ZULU_TIME:
                    handleZuluTime((ZuluTimeMessage)msg);
                    return true;
                case NetMessage.TAKEOFF:
                    handleTakeoff((TakeoffMessage)msg);
                    return true;
                case NetMessage.LANDED:
                    handleLanded((LandedMessage)msg);
                    return true;
                case NetMessage.SCHEDULE_KEY:
                    handleScheduleKey((ScheduleKeyMessage)msg);
                    return true;
                case NetMessage.KICK:
                    handleKick((KickMessage)msg);
                    return true;
                case NetMessage.UNMUTE:
                    handleUnmute((UnmuteMessage)msg);
                    return true;
                case NetMessage.FULL_STATUS:
                    handleFullStatus((FullStatusMessage)msg);
                    return true;
                case NetMessage.FILE_RECEIVED:
                    handleFileReceived((FileReceivedMessage)msg);
                    return true;
                case NetMessage.REQUEST_SCHEDULE:
                    handleRequestSchedule((RequestScheduleMessage)msg);
                    return true;
                case NetMessage.SEND_PIREP:
                    handleSendPIREP((SendPIREPMessage)msg);
                    return true;
                case NetMessage.ANNOUNCE:
                    handleAnnounce((AnnounceMessage)msg);
                    return true;
                case NetMessage.STAFF:
                    handleStaff((StaffMessage)msg);
                    return true;
                // unrecognized message, do nothing
                default:
                    return true;
            }
        }
        catch(Exception ex)
        {
            // error, kill thread
            ex.printStackTrace(System.err);
            return false;
        }

    }
    
    public void handleLogin(LoginMessage msg) {}
    public void handleLoginReturn(LoginReturnMessage msg){}
    public void handleChat(ChatMessage msg){}
    public void handleStatus(StatusMessage msg){}
    public void handlePrivate(PrivateMessage msg){}
    public void handleConnect(ConnectMessage msg){}
    public void handleDisconnect(DisconnectMessage msg){}
    public void handleRequestBooked(RequestBookedMessage msg){}
    public void handleRequestPosition(RequestPositionMessage msg){}
    public void handlePositionResponse(PositionResponseMessage msg){}
    public void handleRequestZuluTime(RequestZuluTimeMessage msg){}
    public void handleZuluTime(ZuluTimeMessage msg){}
    public void handleBookedFlight(BookedFlightMessage msg){}
    public void handleTakeoff(TakeoffMessage msg){}
    public void handleLanded(LandedMessage msg){}
    public void handleScheduleKey(ScheduleKeyMessage msg){}
    public void handleKick(KickMessage msg){}
    public void handleUnmute(UnmuteMessage msg){}
    public void handleFullStatus(FullStatusMessage msg){}
    public void handleFileReceived(FileReceivedMessage msg){}
    public void handleRequestSchedule(RequestScheduleMessage msg){}
    public void handleSendPIREP(SendPIREPMessage msg){}
    public void handleAnnounce(AnnounceMessage msg){}
    public void handleStaff(StaffMessage msg){}
    
    /*
    public boolean receiveFile(String directory, String name, boolean server)
    {
        
        
        System.out.println("Receiving file "+name+" "+acars3.net.NetInfo.FILE_PORT);
        try
        {
            File file = new File(directory);
            if(!file.exists())
            {
                file.mkdir();
            }
            
            Socket socket2 = null;
            InputStream in = null;
            
            
            if(server)
            {
                ServerSocket serv_socket = new ServerSocket(acars3.net.NetInfo.FILE_PORT);
                System.out.println("Server open");
                
                boolean accept = false;
                
                do
                {
                    try
                    {             
                        socket2 = serv_socket.accept();
                        System.out.println("Connected receiving file");
                        
                        in = new BufferedInputStream(socket2.getInputStream());
                        OutputStream netout = socket2.getOutputStream();

                        byte[] array = new byte[4];

                        for(int i = 0; i < 4; i++)
                        {
                            array[i] = (byte)(Math.random()*255);
                        }
                        netout.write(array);
                        netout.flush();

                        array[0] += 10;

                        byte[] test = new byte[4];
                        in.read(test);

                        accept = true;

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
                            System.out.println("failed nonce");
                        }
                    }
                    catch(Exception ex)
                    {
                        ex.printStackTrace(System.err);
                    }
                }
                while(!accept);
                
                
                serv_socket.close();
                
                
            }
            else
            {
                socket2 = new Socket(getIP(), acars3.net.NetInfo.FILE_PORT);
                
                in = new BufferedInputStream(socket2.getInputStream());
                OutputStream netout = socket2.getOutputStream();
                
                byte[] array = new byte[4];
                
                in.read(array);
                
                array[0] += 10;
                
                netout.write(array);
                netout.flush();
            }
            
            OutputStream out = new BufferedOutputStream(new FileOutputStream(new File(directory+"\\"+name)));          
            
                    
            byte[] buffer = new byte[acars3.net.NetInfo.BUFFER_SIZE];
            
            int len = 0;
            
            do
            {
                len = in.read(buffer);  
                
                if(len > 0)
                {
                    out.write(buffer, 0, len);
                } 
            }
            while(len > 0);
            
            out.close();     
            socket2.close();
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace(System.err);
            return false;
        }
        
        return true;
        
    }
    
    public boolean receiveFile(String directory, String name, boolean server, SecretKey aesKey)
    {
        
        System.out.println("Receiving file"+name+" "+acars3.net.NetInfo.FILE_PORT);
        try
        {
            File file = new File(directory);
            if(!file.exists())
            {
                file.mkdir();
            }
            
            Socket socket2 = null;
            InputStream in = null;
            
            
            if(server)
            {
                ServerSocket serv_socket = new ServerSocket(acars3.net.NetInfo.FILE_PORT);
                System.out.println("Server open");
                
                boolean accept = false;
                
                do
                {
                    try
                    {             
                        socket2 = serv_socket.accept();
                        System.out.println("Connected file receive");
                        in = new BufferedInputStream(socket2.getInputStream());
                        OutputStream netout = socket2.getOutputStream();

                        byte[] array = new byte[4];

                        for(int i = 0; i < 4; i++)
                        {
                            array[i] = (byte)(Math.random()*255);
                        }
                        netout.write(array);
                        netout.flush();

                        array[0] += 10;

                        byte[] test = new byte[4];
                        in.read(test);

                        accept = true;

                        for(int i = 0; i < 4; i++)
                        {
                            if(test[i] != array[i])
                            {
                                accept = false;
                                break;
                            }
                        }
                    }
                    catch(Exception ex){}
                }
                while(!accept);
                
                
                serv_socket.close();
                
                
            }
            else
            {
                socket2 = new Socket(getIP(), acars3.net.NetInfo.FILE_PORT);
                
                in = new BufferedInputStream(socket2.getInputStream());
                OutputStream netout = socket2.getOutputStream();
                
                byte[] array = new byte[4];
                
                in.read(array);
                
                array[0] += 10;
                
                netout.write(array);
                netout.flush();
            }
            
            OutputStream out = new BufferedOutputStream(new FileOutputStream(new File(directory+"\\"+name)));    
            
            IvParameterSpec ivParameterSpec = new IvParameterSpec(aesKey.getEncoded());
            Cipher decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            decryptCipher.init(Cipher.DECRYPT_MODE, aesKey, ivParameterSpec);
            
            in = new BufferedInputStream(new CipherInputStream(socket2.getInputStream(), decryptCipher));
                    
            byte[] buffer = new byte[acars3.net.NetInfo.BUFFER_SIZE];
            
            int len = 0;
            
            do
            {
                len = in.read(buffer);  
                
                if(len > 0)
                {
                    out.write(buffer, 0, len);
                } 
            }
            while(len > 0);
            
            out.close();     
            socket2.close();
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace(System.err);
            return false;
        }
        
        
        return true;
        
    }
    
    
    public boolean sendFile(File file, boolean server)
    {
        
        try
        {
            System.out.println("Sending file "+file.toString()+" "+acars3.net.NetInfo.FILE_PORT);
            
            Socket socket2 = null;
            InputStream in = null;
            OutputStream out = null;
            
            if(server)
            {
                ServerSocket serv_socket = new ServerSocket(acars3.net.NetInfo.FILE_PORT);
                boolean accept = false;
                System.out.println("Server open");
                
                
                do
                {
                    try
                    {
                        socket2 = serv_socket.accept();
                        System.out.println("Connected file receive");
                        
                        InputStream netin = socket2.getInputStream();
                        out = new BufferedOutputStream(socket2.getOutputStream());

                        byte[] array = new byte[4];

                        for(int i = 0; i < 4; i++)
                        {
                            array[i] = (byte)(Math.random()*255);
                        }
                        out.write(array);
                        out.flush();

                        array[0] += 10;

                        byte[] test = new byte[4];
                        netin.read(test);

                        accept = true;

                        for(int i = 0; i < 4; i++)
                        {
                            if(test[i] != array[i])
                            {
                                accept = false;
                                break;
                            }
                        }
                    }
                    catch(Exception ex)
                    {
                        ex.printStackTrace(System.err);
                    }
                }
                while(!accept);
                
                
                serv_socket.close();
            }
            else
            {
                System.out.println(getIP()+" "+acars3.net.NetInfo.FILE_PORT);
                socket2 = new Socket(getIP(), acars3.net.NetInfo.FILE_PORT);
                
                InputStream netin = socket2.getInputStream();
                out = new BufferedOutputStream(socket2.getOutputStream());
                
                byte[] array = new byte[4];
                
                netin.read(array);
                
                array[0] += 10;
                
                out.write(array);
                out.flush();
            }
            
            in = new BufferedInputStream(new FileInputStream(file));
            
            byte[] buffer = new byte[acars3.net.NetInfo.BUFFER_SIZE];
            
            int len = 0;
            
            do
            {
                len = in.read(buffer);
                
                if(len > 0)
                {
                    out.write(buffer, 0, len);
                }
                
            }
            while(len > 0);
            out.close();
            in.close();
            socket2.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace(System.err);
            return false;
        }
        
        return true;
        
    }
    */
}
