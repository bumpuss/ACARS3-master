package acars3.client;

import java.io.*;
import java.util.Scanner;
import javax.crypto.*;


public class PIREP implements java.io.Serializable
{	
	
	private File file;
	private String origin, dest, aircraft;
	private int flightnum;
        private String route, version, comments, deptime, arrtime, date, carrier;
        private double beginfuel, endfuel, takeoffrate, landingrate, duration, distance;
        private int pax;
        private boolean online;
        private char era;
        
	private SecretKey aesKey;
	
	public PIREP(int flightnum, String origin, String dest, String aircraft, String route, String version, String deptime, 
                double beginfuel, int pax, boolean online, String date, char era, String carrier, SecretKey aesKey)
	{
		this.origin = origin;
		this.dest = dest;
		this.aircraft = aircraft;
		this.flightnum = flightnum;
                this.route = route;
                this.version = version;
                this.deptime = deptime;
                this.beginfuel = beginfuel;
                this.pax = pax;
                this.online = online;
                this.date = date;
                this.carrier = carrier;
                
                this.aesKey = aesKey;
                
		file = new File("pireps/"+String.format("%x", (origin+dest+System.nanoTime()).hashCode())+".xml");
	}
        
        public double getDistance()
        {
            return distance;
        }
        
        public void setDistance(double d)
        {
            distance = d;
        }
        
        public String getCarrier()
        {
            return carrier;
        }
        public SecretKey getKey()
        {
            return aesKey;
        }
        
        public char getEra()
        {
            return era;
        }
        
        public double getFuelUsed()
        {
            return endfuel - beginfuel;
        }
        
        public double getDuration()
        {
            return duration;
        }
        
        public void setFile(File file)
        {
            this.file = file;
        }
        
        public double getEndFuel()
        {
            return endfuel;
        }
        
        public double getTakeoffRate()
        {
            return takeoffrate;
        }
        
        public double getLandingRate()
        {
            return landingrate;
        }
        
        public int getPax()
        {
            return pax;
        }
        
        
        public boolean isOnline()
        {
            return online;
        }
        
        public String getArrTime()
        {
            return arrtime;
        }
        
        public String getDate()
        {
            return date;
        }
        
        public double getBeginFuel()
        {
            return beginfuel;
        }
        
        public String getVersion()
        {
            return version;
        }
        
        public String getComments()
        {
            return comments;
        }
        
        public String getDepTime()
        {
            return deptime;
        }
        
        public void setComments(String comments)
        {
            this.comments = comments.replaceAll("'", "");    
        }
        
        public void setArrTime(String arrtime)
        {
            this.arrtime = arrtime;
        }
        
        public void setEndFuel(double endfuel)
        {
            this.endfuel = endfuel;
        }
        
        public void setDuration(double duration)
        {
            this.duration = duration;
        }
        
        public void setTakeoffRate(double takeoffrate)
        {
            this.takeoffrate = takeoffrate;
        }
        
        public void setLandingRate(double landingrate)
        {
            this.landingrate = landingrate;
        }

        public String getRoute()
        {
            return route;
        }
	

	
	public void save()
	{
		file.setWritable(false);
	}
	
	public String getOrigin()
	{
		return origin;
	}
	
	public String getDest()
	{
		return dest;
	}
	
	public int getFlightNum()
	{
		return flightnum;
	}
	
	public String getAircraft()
	{
		return aircraft;
	}
	
	public boolean isValid()
	{
		return file.exists();
	}
	
	public File getFile()
	{
		return file;
	}
	
	public String toString()
	{
		return flightnum+" "+origin+" "+dest+" "+aircraft;
	}

	
	public void delete()
	{
		file.delete();
	}
        
        public static final String[] KEYWORDS = new String[]{ "319", "320", "321", "330", "A340", 
            "380", "KKER100", "F100", "J31", "1-11", 
            "S-55", "A300", "-340", "580", "262", 
            "DC-3", "340", "FS US", "717", "727", 
            "737", "732", "741", "742", "743", 
            "744", "747", "80", "MD83", "MD82", 
            "MD81", "MD-83", "MD-82", "MD-81", "MCDONNELL", 
            "SUPER", "9-50", "C99", "1900", "EECH", 
            "757", "767", "777", "200", "700", 
            "900", "120", "145", "170", "175", 
            "190", "195", "DHC-7", "ASH", "JC8Q400", 
            "HC-8", "HC8", "8-300", "ATR", "OTTER", 
            "DHC-6", "146", "RJ100", "DC9-30", "DC-10", 
            "DC10", "L1049", "L1011", "L-188", "CONC"};
            
        public static final String[] AIRCRAFT_TYPES = new String[]{"A319", "A320", "A321", "A330", "A340", 
            "A380", "F100", "F100", "J31", "BA11", 
            "S55", "A300", "CV34", "CV58", "N262",
            "DC3", "S340", "S340", "B717", "B727", 
            "B737", "B732", "B747", "B747", "B747", 
            "B747", "B747", "MD80", "MD80", "MD80", 
            "MD80", "MD80", "MD80", "MD80", "MD80",
            "MD80", "MD95", "B99", "B19D", "B19D", 
            "B757", "B767", "B777", "CRJ2", "CRJ7",
            "CRJ9", "E120", "E145", "E170", "E170", 
            "E190", "E190", "DH7", "DH7", "DH8", 
            "DH8", "DH8", "DH8", "AT72", "DHC6", 
            "DHC6", "B462", "ARJ","DC93", "DC10", 
            "DC10", "L49", "L101", "L188", "CONC"};
        
        public String getAircraftType()
        {
            String temp = aircraft.toUpperCase();
            
            for(int i = 0; i < KEYWORDS.length; i++)
            {
                if(temp.indexOf(KEYWORDS[i]) >= 0)
                {
                    return AIRCRAFT_TYPES[i];
                }
            }
            
            return "A300";
        }
}