/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acars3.client;

import acars3.net.*;
import java.io.IOException;
import javax.swing.JOptionPane;
import java.io.*;
import java.net.Socket;

/**
 *
 * @author Michael
 */
public class ClientListener extends MessageHandler
{
    private GUI gui;
    private Login loginPanel;
    private ChatPanel chat;
    
    private String id, password;
    
    private MessagePanel status;
    
    public ClientListener(String ip) throws IOException
    {
        super(ip, acars3.net.NetInfo.PORT); 
        
        status = new MessagePanel();
        
        start();
        
        //sendFile(new File("test.txt"));
        //receiveFile("test", "test2.txt");
    }
    
    public ClientListener()
    {
        super();
    }
    
    protected void setLoginPanel(Login loginPanel)
    {
        this.loginPanel = loginPanel;
    }
    
    public void login(String id, String password)
    {
        status.setText("Logging in...");
        
        this.id = id;
        this.password = password;
        send(new LoginMessage(id, password, Version.VERSION));
    }
    
    public void handleLoginReturn(LoginReturnMessage msg)
    {

        if(!msg.hasVersion())
        {
            status.setVisible(false);
            loginPanel.bad_version();
        }
        else if(msg.getPilot() == null)
        {
            status.setVisible(false);
            loginPanel.login_failed();
        }
        else
        {
            if( msg.getScheduleVersion() != checkScheduleVersion())
            {
                status.setText("Downloading schedule...");
                send(new RequestScheduleMessage());
                receiveFile("schedule", "schedule.dat");
                receiveFile("schedule", "schedule_key.dat");
                status.setVisible(false);
            }
     
            loginPanel.login_success();
            
            gui = new GUI(false, this, msg.getPilot());
            gui.setVisible(true);

            chat = gui.getChatPanel();

            send(new ConnectMessage("", "", StatusMessage.NOT_FLYING));

        }
    }
    
    public int checkScheduleVersion()
    {
        try
        {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File("schedule/schedule_key.dat")));
            int output = (Integer)in.readObject();
            in.close();
            return output;
        }
        catch(Exception ex)
        {
            return -1;
        }
        
        
    }
    
    public void disconnect_action()
    {
        if(gui != null)
        {
            gui.setOfflineMode(true);
        
            try_reconnect();
        }
        
    }
    
    public void try_reconnect()
    {
        while(true)
        {
            try
            {
                reconnect();
                
                login(id, password);

                if(isConnected())
                {
                    JOptionPane.showMessageDialog(gui, "You have reconnected to the server.", "Reconnected", JOptionPane.INFORMATION_MESSAGE);
			
                    start();
                    gui.setOfflineMode(false);
                    break;
                }
                
                
                Thread.sleep(60*1000);
            }
            catch(Exception ex)
            {

            }

        }
    }
    
    public void handleChat(ChatMessage msg)
    {
        String disp = disp = msg.getTime()+" <"+msg.getFromName()+">: "+msg.getMessage();
        chat.receive(disp, Global.STYLE_CHAT);
    }
    
    
    public void handleLanded(LandedMessage msg)
    {
        chat.receive(msg.getMessage(), Global.STYLE_TAKEOFF);
    }
    
    public void handleTakeoff(TakeoffMessage msg)
    {
        chat.receive(msg.getMessage(), Global.STYLE_TAKEOFF);
    }
    
    public void handleStatus(StatusMessage msg)
    {
        gui.getOnlineListPanel().update(msg.getOLRecord());
    }
    
    public void handleConnect(ConnectMessage msg)
    {
        handleStatus(msg);
        chat.receive(msg.getMessage(), Global.STYLE_CONNECT);
    }
    
    public void handleDisconnect(DisconnectMessage msg)
    {
        chat.receive(msg.getMessage(), Global.STYLE_CONNECT);
        chat.removeRecipient(msg.getId());
        gui.getOnlineListPanel().remove(msg.getId(), msg.getName());
    }
    
    public void handlePositionResponse(PositionResponseMessage msg)
    {
        String dest = gui.getDispatchPanel().getDest();
        String origin = gui.getDispatchPanel().getOrigin();
        

        if(dest != null && dest.equals(msg.getAirport()))
        {
            gui.getDispatchPanel().getFlight().setDestPosition(msg.getLatitude(), msg.getLongitude());
        }
        else if(origin != null && origin.equals(msg.getAirport()))
        {
            gui.getDispatchPanel().getFlight().setOriginPosition(msg.getLatitude(), msg.getLongitude());
        }
        
        
        
    }
    
    public void handlePrivate(PrivateMessage msg)
    {
        System.out.println(msg);
        
        String disp;
        
        if(msg.getTo().equals(gui.getPilot().getId()))
        {
            disp = msg.getTime()+" <"+msg.getFromName()+">: "+msg.getMessage();
            chat.pm_received(msg.getFrom());
            chat.receive(disp, Global.STYLE_PM);
        }
        else if(msg.getFrom().equals(gui.getPilot().getId()))
        {
            disp = msg.getTime()+" To <"+msg.getToName()+">: "+msg.getMessage();
            chat.receive(disp, Global.STYLE_PM);
        }
        
    }
    
    public void handleStaff(StaffMessage msg)
    {
        String disp = disp = msg.getTime()+" <"+msg.getFromName()+">: "+msg.getMessage();
        chat.receive(disp, Global.STYLE_PM);
    }
    
    public void handleAnnounce(AnnounceMessage msg)
    {
        String disp = disp = msg.getTime()+" <"+msg.getFromName()+">: "+msg.getMessage();
        chat.receive(disp, Global.STYLE_ANNOUNCE);
    }
    
    public void handleBookedFlight(BookedFlightMessage msg)
    {
        java.util.List<Flight> booked = msg.getFlights();
        
        if(booked.size() == 0)
        {
            gui.getDispatchPanel().loadFlight(null);
            JOptionPane.showMessageDialog(gui, "You do not have a flight booked. Please book a flight from the USAVA website.", "No booked flight", JOptionPane.INFORMATION_MESSAGE);
        }
        else
        {
            gui.getDispatchPanel().loadFlight(booked.get(0));
            gui.getSchedulePanel().setResults(booked);
        }
    }
    
    public void handleKick(KickMessage msg)
    {
        if(msg.getUserId().equals(gui.getPilot().getId()))
        {
            chat.receive(msg.getTime()+" <System> You have been muted by "+msg.getAdminId()+" "+msg.getAdminName(), Global.STYLE_ERROR);
        }
        else
        {
            chat.receive(msg.getTime()+" <System> "+msg.getUserId()+" "+msg.getUserName()+" was muted by "+msg.getAdminId()+" "+msg.getAdminName(), Global.STYLE_ERROR);
        }
    }
    
    public void handleUnmute(UnmuteMessage msg)
    {
        if(msg.getUserId().equals(gui.getPilot().getId()))
        {
            chat.receive(msg.getTime()+" <System> You have been unmuted by "+msg.getAdminId()+" "+msg.getAdminName(), Global.STYLE_ERROR);
        }
        else
        {
            chat.receive(msg.getTime()+" <System> "+msg.getUserId()+" "+msg.getUserName()+" was unmuted by "+msg.getAdminId()+" "+msg.getAdminName(), Global.STYLE_ERROR);
        }
    }
    
    public void handleFileReceived(FileReceivedMessage msg)
    {
        if(msg.getFileType() == FileReceivedMessage.PIREP)
        {
            if(gui.getFDR().getPIREP() != null)
            {
                if(msg.isSuccess())
                {

                    gui.getPIREPlist().delete(gui.getFDR().getPIREP());
                }
                else
                {
                    gui.getPIREPlist().save(gui.getFDR().getPIREP());
                }
                gui.getDispatchPanel().pirep_submitted();
            }
            
        }
    }
    
    
    public boolean sendFile(File file)
    {
        sleep(2000);
        
        
        try
        {
            Socket socket = new Socket(acars3.net.NetInfo.ADDRESS, acars3.net.NetInfo.FILE_PORT);
            System.out.println("Sending "+file.toString());
            
            InputStream netin = socket.getInputStream();
            OutputStream netout = new BufferedOutputStream(socket.getOutputStream());

            byte[] array = new byte[4];

            netin.read(array);

            array[0] += 10;

            netout.write(array);
            netout.flush();
            
            InputStream in = new BufferedInputStream(new FileInputStream(file));
            
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
            
            netout.close();
            
            socket.close();
            return true;
        }
        catch(Exception ex)
        {
            return false;
        }
    }
    
    public boolean receiveFile(String dir, String name)
    {
        sleep(2000);
        
        try
        {
            Socket socket = new Socket(acars3.net.NetInfo.ADDRESS, acars3.net.NetInfo.FILE_PORT);
            System.out.println("Receiving "+dir+"/"+name);
            
            InputStream netin = socket.getInputStream();
            OutputStream netout = new BufferedOutputStream(socket.getOutputStream());

            byte[] array = new byte[4];

            netin.read(array);

            array[0] += 10;

            netout.write(array);
            netout.flush();
            
            OutputStream out = new BufferedOutputStream(new FileOutputStream(new File(dir+"\\"+name)));          
            
                    
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
            
            return true;
        }
        catch(Exception ex)
        {
            return false;
        }
    }
    
    public void sleep(int time)
    {
        boolean slept = false;
        
        do
        {
            try
            {
                Thread.sleep(time);
                slept = true;
            }
            catch(Exception ex){}
        }
        while(!slept);
    }
}
