/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acars3.server;

import acars3.net.*;
import java.net.Socket;
import acars3.client.Pilot;
import acars3.client.Global;
import acars3.client.Version;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import acars3.client.PIREP;
import javax.crypto.SecretKey;

/**
 *
 * @author Michael
 */
public class ServerListener extends MessageHandler
{
    private ACARSserver server;
    
    private Pilot pilot;
    private StatusMessage status;
    
    private int id; // database id
    
    private boolean muted;
    
    public ServerListener(Socket s, ACARSserver server)
    {
        super(s, true);
        this.server = server;
        
        //receiveFile("test", "test3.txt");
        //sendFile(new File("test.txt"));
        start();
        
        
    }
    
    public void handleLogin(LoginMessage msg)
    {
        if(!msg.getVersion().equals(Version.VERSION))
        {
            send(new LoginReturnMessage(null, false, 0));
        }
        
        pilot = server.authenticatePilot(msg.getUsername(), msg.getPassword(), this);
        
        send(new LoginReturnMessage(pilot, true, server.getScheduleVersion()));
        
        if(pilot != null)
        {
            server.addClient(this);
        }
    }
    
    public void setMuted(boolean m)
    {
        muted = m;
    }
    
    public boolean isMuted()
    {
        return muted;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getId()
    {
        return id;
    }
    
    public Pilot getPilot()
    {
        return pilot;
    }
    
    public boolean isAdmin()
    {
        return pilot.isAdmin();
    }
    
    public StatusMessage getStatus()
    {
        return status;
    }
    
    public void disconnect_action()
    {
        if(pilot != null)
        {
            server.removeClient(this);
            server.sendToAll(new DisconnectMessage(server.getTime()+" <System> "+pilot.getRank()+" "+pilot.getName()+" has disconnected.", pilot.getId(), pilot.getName()));
        }
    }
    
    public boolean handleMessage(NetMessage msg)
    {
        // ignore all messages except login messages if not authenticated
        if(pilot != null || msg.getType() == NetMessage.LOGIN)
        {
            return super.handleMessage(msg);
        }
        
        return false;
    }
    
    public void handleStatus(StatusMessage msg)
    {
        msg.setId(pilot.getId());
        msg.setRank(pilot.getCAT());
        msg.setName(pilot.getName());
        msg.setIsStaff(isAdmin());
        
        status = msg;
        
        server.sendToAll(msg);
    }
    
    public void handleChat(ChatMessage msg)
    {
        if(pilot != null)
        {
            if(!muted)
            {
                msg.setTime(server.getTime());
                msg.setFromName(pilot.getAbbrevRank()+" "+pilot.getName());
                server.sendToAll(msg); 
            }
            else
            {
                PrivateMessage msg2 = new PrivateMessage("System", "You are muted. Email a staff member for support.");
                msg2.setTime(server.getTime());
                send(msg2);
            }
        }
    }
    
    public void handleStaff(StaffMessage msg)
    {
        if(pilot != null)
        {
            if(!muted)
            {
                msg.setTime(server.getTime());
                msg.setFromName(pilot.getAbbrevRank()+" "+pilot.getName());
                server.sendToStaff(msg, this); 
            }
            else
            {
                PrivateMessage msg2 = new PrivateMessage("System", "You are muted. Email a staff member for support.");
                msg2.setTime(server.getTime());
                send(msg2);
            }
        }
    }
    
    public void handleAnnounce(AnnounceMessage msg)
    {
        if(pilot != null && pilot.isAdmin())
        {
            msg.setTime(server.getTime());
            msg.setFromName(pilot.getAbbrevRank()+" "+pilot.getName());
            server.sendToAll(msg); 
        }
    }
    
    
    public void handlePrivate(PrivateMessage msg)
    {
        if(pilot != null)
        {
            if(!muted)
            {
                server.sendPrivateMessage(msg, this);
            }
            else
            {
                PrivateMessage msg2 = new PrivateMessage("System", "You are muted. Email a staff member for support.");
                msg2.setTime(server.getTime());
                send(msg2);
            }
        }
    }
    
    public void handleKick(KickMessage msg)
    {
        if(isAdmin())
        {
            server.mute(msg, this);
        }
    }
    
    public void handleUnmute(UnmuteMessage msg)
    {
        if(isAdmin())
        {
            server.unmute(msg, this);
        }
    }
    
    public void handleConnect(ConnectMessage msg)
    {
        msg.setMessage(server.getTime()+" <System> "+pilot.getRank()+" "+pilot.getName()+" has connected to the company.");
        msg.setId(pilot.getId());
        msg.setIsStaff(isAdmin());
        msg.setName(pilot.getName());
        msg.setRank(pilot.getCAT());
        
        status = msg.clone();
        
        server.sendToAll(msg);
    }
    
    public void handleRequestPosition(RequestPositionMessage msg)
    {
        String icao = msg.getAirport();
        double[] output = server.getLatLong(icao);
        send(new PositionResponseMessage(icao, output[0], output[1]));
    }
    
    public void handleRequestBooked(RequestBookedMessage msg)
    {
        send(new BookedFlightMessage(server.getBookedFlights(id)));
    }
    
    public void handleFullStatus(FullStatusMessage msg)
    {
        server.updatePosition(msg, this);
    }
    

    public void handleRequestSchedule(RequestScheduleMessage msg)
    {
        sendFile(ACARSserver.SCHEDULE_FILE);  
        
        while(server.getFileServer().hasActiveRequest(getInetAddress()))
        {
            try
            {
                Thread.sleep(1000);
            }
            catch(Exception ex){}
        }
        sendFile(ACARSserver.SCHEDULE_KEY_FILE);
        
    }
    
    public void handleTakeoff(TakeoffMessage msg)
    {
        server.sendToAll(new TakeoffMessage(server.getTime()+" <System> "+pilot.getAbbrevRank()+" "+pilot.getName()+msg.getMessage()));
    }
    public void handleLanded(LandedMessage msg)
    {
        server.sendToAll(new LandedMessage(server.getTime()+" <System> "+pilot.getAbbrevRank()+" "+pilot.getName()+msg.getMessage()));
    }
    
    private PIREP pirep_pending;
    
    public void handleSendPIREP(SendPIREPMessage msg)
    {
        pirep_pending = msg.getPIREP();
        String name;
        receiveFile(ACARSserver.PIREP_DIRECTORY, name = server.createPIREPfile(), pirep_pending.getKey());
        pirep_pending.setFile(new File(ACARSserver.PIREP_DIRECTORY+"\\"+name));
        
        /*
        String name;
        if(receiveFile(ACARSserver.PIREP_DIRECTORY, name = server.createPIREPfile(), false, pirep.getKey()))
        {
            pirep.setFile(new File(ACARSserver.PIREP_DIRECTORY+"\\"+name));
            boolean success = server.savePIREP(pirep, this);
            
            if(!success)
            {
                pirep.getFile().delete();
            }
            send(new FileReceivedMessage("PIREP", FileReceivedMessage.PIREP, success));
        }
        else
        {
            send(new FileReceivedMessage("PIREP", FileReceivedMessage.PIREP, false));
        }
        */
    }
    
    public void fileReceived()
    {
        if(pirep_pending == null)
        {
            return;
        }
        
        boolean success = server.savePIREP(pirep_pending, this);

        if(!success)
        {
            pirep_pending.getFile().delete();
        }
        send(new FileReceivedMessage("PIREP", FileReceivedMessage.PIREP, success));
    }
    
    public void fileNotReceived()
    {
        pirep_pending = null;
        send(new FileReceivedMessage("PIREP", FileReceivedMessage.PIREP, false));
    }
    
    public void sendFile(File file)
    {
        System.out.println("Sending "+file.toString());
        server.getFileServer().sendFile(this, file); 
        
    }
    
    public void receiveFile(String directory, String name)
    {
        server.getFileServer().receiveFile(this, directory, name);
        System.out.println("Receiving "+directory+"/"+name);
    }
    
    public void receiveFile(String directory, String name, SecretKey key)
    {
        server.getFileServer().receiveFile(this, directory, name, key);
        System.out.println("Receiving "+directory+"/"+name);
    }
}
