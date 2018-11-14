package acars3.client;

public class Flight implements Comparable<Flight>, java.io.Serializable
{
	private char cat;
	
	private String origin_iata, origin_icao, dest_iata, dest_icao, origin_descrip, dest_descrip, aircraft, dtime, etime, carrier;
	
	private int duration, number;
        
        private String dtime_zulu, etime_zulu;
        private double origin_lat, origin_long, dest_lat, dest_long;

        private char era;
        private int pax;
        
	public Flight(int number, String origin_iata, String origin_icao, String origin_descrip, String dest_iata, String dest_icao, String dest_descrip, 
                String aircraft, String dtime, String etime, int duration, char cat, String carrier)
	{
		this.cat = cat;
		this.origin_iata = origin_iata;
		this.origin_icao = origin_icao;
		this.dest_iata = dest_iata;
		this.dest_icao = dest_icao;
		this.aircraft = aircraft;
		this.dtime = dtime;
		this.etime = etime;
		this.duration = duration;
		this.number = number;
		this.carrier = carrier;
                this.origin_descrip = origin_descrip;
                this.dest_descrip = dest_descrip;
                era = 'C';
	}
        
        public Flight(int number, String origin_iata, String origin_icao, String origin_descrip, double origin_lat, double origin_long, String dest_iata, String dest_icao, String dest_descrip, 
                double dest_lat, double dest_long, String aircraft, String dtime, String etime, String dtimezulu, String etimezulu, int duration, char cat, String carrier, char era, int pax)
	{
		this.cat = cat;
		this.origin_iata = origin_iata;
		this.origin_icao = origin_icao;
		this.dest_iata = dest_iata;
		this.dest_icao = dest_icao;
		this.aircraft = aircraft;
		this.dtime = dtime;
		this.etime = etime;
                this.dtime_zulu = dtimezulu;
                this.etime_zulu = etimezulu;
		this.duration = duration;
		this.number = number;
		this.carrier = carrier;
                this.origin_descrip = origin_descrip;
                this.dest_descrip = dest_descrip;
                this.era = era;
                this.pax = pax;
	}
        
        public Flight(int number, String origin_iata, String origin_icao, String origin_descrip, double origin_lat, double origin_long,
                String dest_iata, String dest_icao, String dest_descrip, double dest_lat, double dest_long,
                String aircraft, String dtime, String etime, String dtime_zulu, String etime_zulu, int duration, char cat, String carrier)
	{
		this.cat = cat;
		this.origin_iata = origin_iata;
		this.origin_icao = origin_icao;
		this.dest_iata = dest_iata;
		this.dest_icao = dest_icao;
		this.aircraft = aircraft;
		this.dtime = dtime;
		this.etime = etime;
		this.duration = duration;
		this.number = number;
		this.carrier = carrier;
                this.origin_descrip = origin_descrip;
                this.dest_descrip = dest_descrip;
                
                this.origin_lat = origin_lat;
                this.origin_long = origin_long;
                this.dest_lat = dest_lat;
                this.dest_long = dest_long;
                
                this.dtime_zulu = dtime_zulu;
                this.etime_zulu = etime_zulu;
                era = 'C';
  
	}
        
        public void setPax(int pax)
        {
            this.pax = pax;
        }
        
        public int getPax()
        {
            return pax;
        }
        
        public char getEra()
        {
            return era;
        }
        
        public void setEra(char era)
        {
            this.era = era;
        }
        
        public String getDepTimeZulu()
        {
            return dtime_zulu;
        }
        
        public String getArrTimeZulu()
        {
            return etime_zulu;
        }
        
        public double getDestLatitude()
        {
            return dest_lat;
        }
        
        public double getDestLongitude()
        {
            return dest_long;
        }
        
        public double getOriginLatitude()
        {
            return origin_lat;
        }
        
        public double getOriginLongitude()
        {
            return origin_long;       
        }
        
        public void setDestPosition(double dest_lat, double dest_long)
        {
            this.dest_lat = dest_lat;
            this.dest_long = dest_long;
        }
        
        public void setOriginPosition(double origin_lat, double origin_long)
        {
            this.origin_lat = origin_lat;
            this.origin_long = origin_long;
        }
	
	public int hashCode()
	{
		return number;
	}
	
	public boolean equals(Object o)
	{
		Flight rhs = (Flight)o;
		
		return number == rhs.number && origin_iata.equals(rhs.origin_iata) && dest_iata.equals(rhs.dest_iata) && carrier.equals(rhs.carrier) && cat == rhs.cat;
	}
	
	public int compareTo(Flight rhs)
	{
		if(rhs.cat != cat)
		{
			return cat - rhs.cat;
		}
		else if(!origin_iata.equals(rhs.origin_iata))
		{
			return origin_iata.compareTo(rhs.origin_iata);
		}
		else if(!dest_iata.equals(rhs.dest_iata))
		{
			return dest_iata.compareTo(rhs.dest_iata);
		}
		else
		{
			return number - rhs.number;
		}
	}
        
        public String getOriginDescription()
        {
            return origin_iata+" "+origin_descrip;
        }
        
        public String getDestDescription()
        {
            return dest_iata+" "+dest_descrip;
        }
	
	public String getCarrier()
	{
		return carrier;
	}
	
	public int getNumber()
	{
		return number;
	}
	
	public int getDuration()
	{
		return duration;
	}
	
	public String getDepTime()
	{
		return dtime;
	}
	
	public String getArrTime()
	{
		return etime;
	}
	
	public String getOriginIATA()
	{
		return origin_iata;
	}
	
	public String getOriginICAO()
	{
		return origin_icao;
	}
	
	public String getDestIATA()
	{
		return dest_iata;
	}
	
	public String getDestICAO()
	{
		return dest_icao;
	}
	
	public String getAircraft()
	{
		return aircraft;
	}
	
	public char getCAT()
	{
		return cat;
	}
}