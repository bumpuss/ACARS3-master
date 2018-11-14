/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acars3.server;

import acars3.net.*;
import java.sql.*;
import java.io.*;
import java.net.*;
import acars3.client.*;
import java.util.*;

import java.text.SimpleDateFormat;

/**
 *
 * @author Michael
 */
public class ACARSserver implements Runnable
{
    public static final File SCHEDULE_KEY_FILE = new File("schedule_database/schedule_key.dat");
    public static final File SCHEDULE_FILE = new File("schedule_database/schedule.dat");
    public static final String PIREP_DIRECTORY = "pirep_database";
    //public static final String PIREP_DIRECTORY = "c:\\inetpub\\wwwroot\\usava\\acarsengine\\flights";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception
    {
        new ACARSserver(true);  
    }
    
    private Connection connect;
    
    private HashMap<String, ServerListener> clients;
    
    private Calendar calendar;
    private SimpleDateFormat sdf;
    
    private int scheduleVersion;
    
    private String ip;
    private FileServer fileserver;
    
    public ACARSserver(boolean logfile)
    {
        clients = new HashMap<String, ServerListener>();
        
        calendar = Calendar.getInstance();
        sdf = new SimpleDateFormat("hh:mm a");
        
        readScheduleVersion(SCHEDULE_KEY_FILE);
        
        if(logfile)
        {
            try
            {
                System.setOut(new PrintStream(new FileOutputStream(new File("log_out.txt")), true));
                System.setErr(new PrintStream(new FileOutputStream(new File("log_error.txt")), true));
            }
            catch(IOException ex)
            {
                ex.printStackTrace(System.err);
            }
        }
        
        fileserver = new FileServer();
        
        Thread t = new Thread(this);
        t.start();
    }
    
    public FileServer getFileServer()
    {
        return fileserver;
    }
    
    public String getIP()
    {
        return ip;
    }
    
    public void readScheduleVersion(File file)
    {
        try
        {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            scheduleVersion = (Integer)in.readObject();
            in.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }
    
    public int getScheduleVersion()
    {
        return scheduleVersion;
    }
    
    public void run()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            //connect = DriverManager.getConnection("jdbc:mysql://localhost/usa_web?"+ "user=root&password=adfasfdas");
            //connect = DriverManager.getConnection("jdbc:mysql://173.16.52.85:3306/usa_web", "root", "SLb4103#!steve");
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/usa_web?"+"user=root&password=SLb4103#!steve");
            
            System.out.println("Connected to mySQL");
        }
        catch(Exception ex)
        {
            //ex.printStackTrace(System.err);
            
            //System.exit(1);
            //return;
        }
        
        initGUI();
        
        ServerSocket socket = null;
        
        try
        {
            socket=new ServerSocket(NetInfo.PORT);
            System.out.println("Server running");
        }
        catch(IOException ex)
        {
            ex.printStackTrace(System.err);
            System.exit(1);
            return;
        }
        
        try
        {
            
            
            while(true)
            {
                Socket clientSocket = socket.accept();
                System.out.println("Connected "+clientSocket.getInetAddress());
                new ServerListener(clientSocket, this);
            }
        }
        catch(IOException e)
        {
            e.printStackTrace(System.err);
        }
    }
    
    public void initGUI()
    {
        new ServerGUI();
    }
    
    public String getTime()
    {
    	
    	return sdf.format(calendar.getTime());
    }
    
    public void addClient(ServerListener client)
    {
        for(String x : clients.keySet())
        {
            client.send(clients.get(x).getStatus());
        }
        
        synchronized(clients)
        {
           clients.put(client.getPilot().getId(), client); 
        }
    }
    
    public void removeClient(ServerListener client)
    {
        synchronized(clients)
        {
            clients.remove(client.getPilot().getId());
        }
    }
    
    public void sendPrivateMessage(PrivateMessage msg, ServerListener client)
    {
        if(clients.containsKey(msg.getTo()))
        {
            ServerListener recipient = clients.get(msg.getTo());
            msg.setToName(Global.convertRankAbbrev(recipient.getPilot().getCAT())+" "+recipient.getPilot().getName());
            msg.setFromName(Global.convertRankAbbrev(client.getPilot().getCAT())+" "+client.getPilot().getName());
            msg.setFrom(client.getPilot().getId());
            msg.setTime(getTime());
            
            recipient.send(msg);
            client.send(msg);
            
        }
        else
        {
            msg = new PrivateMessage("System", client.getPilot().getId(), msg.getTo()+" is not online.");
            msg.setTime(getTime());
            client.send(msg);
        }
    }
    
    public void sendToAll(NetMessage msg)
    {
        for(String x  : clients.keySet())
        {
            clients.get(x).send(msg);
        }     
    }
    
    public void sendToStaff(StaffMessage msg, ServerListener sender)
    {
        for(String x : clients.keySet())
        {
            ServerListener client = clients.get(x);
            
            if(client.isAdmin())
            {
                client.send(msg);
            }
        }
    }
    
    public Pilot authenticatePilot(String login, String password, ServerListener client)
    {
        try
        {
            PreparedStatement preparedStatement = connect.prepareStatement("Select * from members where login = '"+login+"' and password = '"+password+"' && status NOT IN (2,3,5) LIMIT 1");
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next() && !clients.containsKey(resultSet.getString("login")))
            {
                PreparedStatement preparedStatement2 = connect.prepareStatement("select arr_icao from reports where status=0 or status=4 and mid="+resultSet.getString("id")+
                        " and type!=2 order by timestamp desc limit 1;");
                ResultSet currentLocResults = preparedStatement2.executeQuery();
                
                String currentLoc;
                
                if(currentLocResults.next())
                {
                    currentLoc = currentLocResults.getString("arr_icao");
                }
                else
                {
                    currentLoc = "null";
                }
                
                client.setId(resultSet.getInt("id"));
                
                return new Pilot(resultSet.getString("login"), Global.convertRank(resultSet.getString("rank")), resultSet.getString("firstName")+" "+resultSet.getString("lastName"), 
                        resultSet.getString("hub"), currentLoc, resultSet.getDouble("transHours")+resultSet.getDouble("compHours")+resultSet.getDouble("bonusHours"), 
                        resultSet.getDouble("money"), Integer.parseInt(resultSet.getString("admin")) > 0);
            }
            else
            {
                return null;
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace(System.err);
            return null;
        }
        
    }
    
    public double[] getLatLong(String icao)
    {
        try
        {
            PreparedStatement preparedStatement = connect.prepareStatement("select latitude, longitude from airports where icao = '"+icao+"'");
            ResultSet results = preparedStatement.executeQuery();

            if(results.next())
            {
                return new double[]{results.getDouble("latitude"), results.getDouble("longitude")};
            }
            else
            {
                return new double[2];
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace(System.err);
            return new double[2];
        }
    }
    
    public void handleRequestPosition(String icao, ServerListener client)
    {
        try
        {
            PreparedStatement preparedStatement = connect.prepareStatement("select latitude, longitude from airports where icao = '"+icao+"'");
            ResultSet results = preparedStatement.executeQuery();

            if(results.next())
            {
                client.send(new PositionResponseMessage(icao, results.getDouble("latitude"), results.getDouble("longitude")));
            }
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace(System.err);
        }
        
    }
    
    public List<Flight> getBookedFlights(int database_id)
    {
        try
        {
            PreparedStatement preparedStatement = connect.prepareStatement("select * from reports where mid = "+database_id+" and type = 2 order by timestamp asc");  
            ResultSet results = preparedStatement.executeQuery();
            
            List<Flight> output = new ArrayList<Flight>();
            
            while(results.next())
            {
                String dep_icao = results.getString("dep_icao");
                String arr_icao = results.getString("arr_icao");
               
                String dep_time = results.getString("dep_time");
                String dtime_zulu = getZuluTime(dep_icao, dep_time);
                String arr_time = results.getString("arr_time");
                String etime_zulu = getZuluTime(arr_icao, arr_time);
                
                int duration = getDuration(dtime_zulu = getZuluTime(dep_icao, dep_time), etime_zulu = getZuluTime(arr_icao, arr_time));
                
                double[] origin_pos = getLatLong(dep_icao);
                double[] dest_pos = getLatLong(arr_icao);
                
                output.add(new Flight(results.getInt("flightNumber"), dep_icao, dep_icao, "", origin_pos[0], origin_pos[1],
                        arr_icao, arr_icao, "", dest_pos[0], dest_pos[1], results.getString("aircraft"), dep_time, arr_time, dtime_zulu, etime_zulu,
                        duration, ' ', results.getString("carrier"), results.getString("era").charAt(0), results.getInt("passengers")));
            }

            return output;
        }
        catch(Exception ex)
        {
            ex.printStackTrace(System.err);
            return new ArrayList<Flight>();
        }
        
    }
    
    public int getDuration(String dtime, String etime)
    {
        int dmins = Integer.parseInt(dtime.substring(0,2))*60 + Integer.parseInt(dtime.substring(2,4));
        int emins = Integer.parseInt(etime.substring(0,2))*60 + Integer.parseInt(etime.substring(2,4));
        
        if(dmins >= emins)
        {
            emins += 24*60;
        }
        
        return emins - dmins;
    }
    
    public String getZuluTime(String icao, String time)
    {
        try
        {
            int idx = time.indexOf(':');
            
            int mins = Integer.parseInt(time.substring(0, idx))*60 + Integer.parseInt(time.substring(idx+1, idx+3));
        
            if(time.indexOf("PM")>0 && mins < 12*60)
            {
                mins += 12*60;
            }

            PreparedStatement preparedStatement = connect.prepareStatement("select timezone from airports where icao='"+icao+"'");
            ResultSet results = preparedStatement.executeQuery();
            
            results.next();
            
            TimeZone tz = TimeZone.getTimeZone(results.getString("timezone"));
            
            int offset = tz.getOffset(System.nanoTime()/1000000)/1000/60;
            
            mins -= offset;
            
            if(mins < 0)
            {
                mins += 24 * 60;
            }
            else if(mins > 24*60)
            {
                mins -= 24*60;
            }

            
            return (mins/60 < 10?"0":"")+(mins/60)+(mins%60<10?"0":"")+(mins%60)+"Z";
        }
        catch(Exception ex)
        {
            ex.printStackTrace(System.err);
            return "0000Z";
        }
    }
    
    public void mute(KickMessage msg, ServerListener admin)
    {
        
        ServerListener user = clients.get(msg.getUserId());

        
        if(user != null && !user.isAdmin() && !user.isMuted())
        {
            msg.setUserName(user.getPilot().getName());
            msg.setAdminName(admin.getPilot().getName());
            msg.setAdminId(admin.getPilot().getId());
            msg.setTime(getTime());
            
            user.setMuted(true);
            
            sendToAll(msg);
        }
    }
    
    public void unmute(UnmuteMessage msg, ServerListener admin)
    {
        
        ServerListener user = clients.get(msg.getUserId());

        
        if(user != null && !user.isAdmin() && user.isMuted())
        {
            msg.setUserName(user.getPilot().getName());
            msg.setAdminName(admin.getPilot().getName());
            msg.setAdminId(admin.getPilot().getId());
            msg.setTime(getTime());
            
            user.setMuted(false);
            
            sendToAll(msg);
        }
    }
    
    public void updatePosition(FullStatusMessage msg, ServerListener client)
    {
        try
        {
            PreparedStatement preparedStatement = connect.prepareStatement("select mid from acars_online where mid = "+client.getId());
            ResultSet results = preparedStatement.executeQuery();

            FlightRecord record = msg.getFlightRecord();
            
            if(results.next())
            {
                preparedStatement = connect.prepareStatement("update acars_online set depicao='"+msg.getDep()+"',busy=0,arricao='"+msg.getArr()+"',abbrev_ac='"+msg.getAircraft()+"',"+
                        "route='"+msg.getRoute()+"',heading="+String.format("%.0f", record.heading)+",altitude="+String.format("%.0f", record.altitude)+",latitude="+String.format("%10f", record.latitude)+",longitude="+String.format("%10f", record.longitude)+","+
                        "gs="+String.format("%.0f", record.GS)+",ias="+String.format("%.0f", record.KIAS)+",vs="+String.format("%.2f", record.VS)+",e1n1="+String.format("%.2f", record.eng1_n1)+",e1n2="+String.format("%.2f", record.eng1_n2)+",e2n1="+String.format("%.2f", record.eng2_n1)+",e2n2="+String.format("%.2f", record.eng2_n2)+
                        ",e3n1="+String.format("%.2f", record.eng3_n1)+",e3n2="+String.format("%.2f", record.eng3_n2)+",e4n1="+String.format("%.2f", record.eng4_n1)+",e4n2="+String.format("%.2f", record.eng4_n2)+",fuel="+String.format("%.2f", record.totalfuel)+",e1fuelflow="+String.format("%.2f", record.eng1_ff)+
                        ",e2fuelflow="+String.format("%.2f", record.eng2_ff)+",e3fuelflow="+String.format("%.2f", record.eng3_ff)+",e4fuelflow="+String.format("%.2f", record.eng4_ff)+",winddegree="+String.format("%.2f", record.wind_dir)+",windspeed="+String.format("%.2f", record.wind_spd)+
                        ",windgusts="+String.format("%.2f", record.wind_gusts)+",ap="+(record.isAutopilot?"1":"0")+",apat="+(record.isAutothrottle?"1":"0")+",landinglights="+(record.isLandingLights?"1":"0")+",strobelights="+(record.isStrobeLights?"1":"0")+
                        ",navlights="+(record.isNavLights?"1":"0")+",pitotheat="+(record.isPitotHeat?"1":"0")+",simrate="+String.format("%.3f", record.sim_rate)+",turnrate="+String.format("%.2f", record.turnRate)+",bank="+record.bank+",pitch="+String.format("%.2f", record.pitch)+
                        ",gforce="+String.format("%.2f", record.Gforce)+",lastupdate='"+getTime()+"',status='"+msg.getStatusId()+"'"+
                        ",ac='',aphdg=0,apspeed=0,apmach=0,apapproach=0,apnav=0,ip='"+client.getIP()+"',version='"+msg.getVersion()+"'"+
                        " where mid="+client.getId());
            }
            else
            {
                
                preparedStatement = connect.prepareStatement( "insert into acars_online set depicao='"+msg.getDep()+"',busy=0,arricao='"+msg.getArr()+"',abbrev_ac='"+msg.getAircraft()+"',"+
                        "route='"+msg.getRoute()+"',heading="+String.format("%.0f", record.heading)+",altitude="+String.format("%.0f", record.altitude)+",latitude="+String.format("%10f", record.latitude)+",longitude="+String.format("%10f", record.longitude)+","+
                        "gs="+String.format("%.0f", record.GS)+",ias="+String.format("%.0f", record.KIAS)+",vs="+String.format("%.2f", record.VS)+",e1n1="+String.format("%.2f", record.eng1_n1)+",e1n2="+String.format("%.2f", record.eng1_n2)+",e2n1="+String.format("%.2f", record.eng2_n1)+",e2n2="+String.format("%.2f", record.eng2_n2)+
                        ",e3n1="+String.format("%.2f", record.eng3_n1)+",e3n2="+String.format("%.2f", record.eng3_n2)+",e4n1="+String.format("%.2f", record.eng4_n1)+",e4n2="+String.format("%.2f", record.eng4_n2)+",fuel="+String.format("%.2f", record.totalfuel)+",e1fuelflow="+String.format("%.2f", record.eng1_ff)+
                        ",e2fuelflow="+String.format("%.2f", record.eng2_ff)+",e3fuelflow="+String.format("%.2f", record.eng3_ff)+",e4fuelflow="+String.format("%.2f", record.eng4_ff)+",winddegree="+String.format("%.2f", record.wind_dir)+",windspeed="+String.format("%.2f", record.wind_spd)+
                        ",windgusts="+String.format("%.2f", record.wind_gusts)+",ap="+(record.isAutopilot?"1":"0")+",apat="+(record.isAutothrottle?"1":"0")+",landinglights="+(record.isLandingLights?"1":"0")+",strobelights="+(record.isStrobeLights?"1":"0")+
                        ",navlights="+(record.isNavLights?"1":"0")+",pitotheat="+(record.isPitotHeat?"1":"0")+",simrate="+String.format("%.3f", record.sim_rate)+",turnrate="+String.format("%.2f", record.turnRate)+",bank="+record.bank+",pitch="+String.format("%.2f", record.pitch)+
                        ",gforce="+String.format("%.2f", record.Gforce)+",lastupdate='"+getTime()+"',status='"+msg.getStatusId()+"',mid="+client.getId()+
                        ",ac='',aphdg=0,apspeed=0,apmach=0,apapproach=0,apnav=0,ip='"+client.getIP()+"',version='"+msg.getVersion()+"'");
            }
            
            preparedStatement.executeUpdate();
        }
        catch(Exception ex)
        {
            ex.printStackTrace(System.err);
        }
    }
    
    
    public boolean savePIREP(PIREP pirep, ServerListener client)
    {
        try
        {
            PreparedStatement preparedStatement = connect.prepareStatement("select * from reports where mid="+client.getId()+" and type=2 and dep_icao='"+pirep.getOrigin()+
                    "' and arr_icao='"+pirep.getDest()+"' order by id limit 1");
            ResultSet results = preparedStatement.executeQuery();

            int rep_id;
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            
            String timestamp = ""+calendar.getTimeInMillis() / 1000L;
            String now = getTime();

            if(results.next())
            {
                rep_id = results.getInt("id");
                
                preparedStatement = connect.prepareStatement("update reports set mid="+client.getId()+",mhub='"+client.getPilot().getHub()+"',flightnumber="+pirep.getFlightNum()+
                    ",dep_icao='"+pirep.getOrigin()+"',arr_icao='"+pirep.getDest()+"',dep_time='"+pirep.getDepTime()+"',arr_time='"+pirep.getArrTime()+"',aircraft='"+pirep.getAircraft()+"',"+
                    "duration="+String.format("%.1f", pirep.getDuration())+",fuelused="+pirep.getFuelUsed()+",passengers="+pirep.getPax()+",online="+(pirep.isOnline()?"1":"0")+","+
                    "takeoffrate="+pirep.getTakeoffRate()+",era='"+pirep.getEra()+"',comments='"+pirep.getComments()+"',landingrate='"+pirep.getLandingRate()+"',"+
                    "acarsfile='"+pirep.getFile().getCanonicalPath()+"',type=5,timestamp='"+timestamp+"',sip='"+client.getIP()+"',status='4',ac_code='"+pirep.getAircraftType()+"',"+
                    "manifest='',reason='',aip='"+getIP()+"',aid=1,tt_id='',carrier='"+pirep.getCarrier()+"',"+
                    "distance='"+String.format("%.1f", pirep.getDistance())+"' where id="+rep_id);
            }
            else
            {
                // else find latest id and add flights above it
                preparedStatement = connect.prepareStatement("select id from reports order by id desc limit 1");
                results = preparedStatement.executeQuery();
                
                if(results.next())
                {
                    rep_id = results.getInt("id");
                }
                else
                {
                    rep_id = 1;
                }
                
                preparedStatement = connect.prepareStatement("insert into reports set id="+rep_id+", mid="+client.getId()+",mhub='"+client.getPilot().getHub()+"',flightnumber="+pirep.getFlightNum()+
                    ",dep_icao='"+pirep.getOrigin()+"',arr_icao='"+pirep.getDest()+"',dep_time='"+pirep.getDepTime()+"',arr_time='"+pirep.getArrTime()+"',aircraft='"+pirep.getAircraft()+"',"+
                    "duration="+String.format("%.1f", pirep.getDuration())+",fuelused="+pirep.getFuelUsed()+",passengers="+pirep.getPax()+",online="+(pirep.isOnline()?"1":"0")+","+
                    "takeoffrate="+pirep.getTakeoffRate()+",era='"+pirep.getEra()+"',comments='"+pirep.getComments()+"',landingrate='"+pirep.getLandingRate()+"',"+
                    "acarsfile='"+pirep.getFile().getCanonicalPath()+"',type=5,timestamp='"+timestamp+"',sip='"+client.getIP()+"',status='4',ac_code='"+pirep.getAircraftType()+"',"+
                    "manifest='',reason='',aip='"+getIP()+"',aid=1,tt_id='',carrier='"+pirep.getCarrier()+"',"+
                    "distance='"+String.format("%.1f", pirep.getDistance())+"'");
                // ,expenses=0,pilotpay=0,cargorevenue=0,paxrevenue=0,revenue=0,bonushours=0
            }
            preparedStatement.executeUpdate();
            
            
            
            preparedStatement = connect.prepareStatement("update members set lastreport = '"+now+"' where id="+client.getId());
            preparedStatement.executeUpdate();
            
            

            

            
            return true;
        }
        catch(Exception ex)
        {
            ex.printStackTrace(System.err);
            return false;
        }
    }
    
    public String createPIREPfile()
    {
        String legalchars = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        
        File file;
        String name;
        
        do
        {
            name = "";
            
            for(int i = 0; i < 16; i++)
            {
                name += legalchars.charAt((int)(Math.random()*legalchars.length()));
            }
            
            name += ".xml";
            
            file = new File(PIREP_DIRECTORY+"\\"+name);
        }
        while(file.exists());
        
        return name;
    }
}
