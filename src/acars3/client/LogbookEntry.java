package acars3.client;


public class LogbookEntry implements java.io.Serializable
{
	private String origin, dest, begintime, endtime;
	private int beginfuel, endfuel, time;
	private double weight, landingRate;
	
	
	
	public LogbookEntry(String origin, String dest, String begintime, String endtime, int beginfuel, int endfuel, int time, double weight, double landingRate)
	{
		this.origin = origin;
		this.dest = dest;
		this.begintime = begintime;
		this.endtime = endtime;
		this.beginfuel = beginfuel;
		this.endfuel = endfuel;
		this.time = time;
		this.weight = weight;
		this.landingRate = landingRate;
		
	}
	
	
	
	public double getLandingRate()
	{
		return landingRate;
	}
	public String getOrigin()
	{
		return origin;
	}
	
	public String getDest()
	{
		return dest;
	}
	
	public double getWeight()
	{
		return weight;
	}
	
	public String getBeginTime()
	{
		return begintime;
	}
	
	public String getEndTime()
	{
		return endtime;
	}
	
	public int getFuelLoad()
	{
		return beginfuel;
	}
	
	public int getFuelUsed()
	{
		return beginfuel - endfuel;
	}
	
	public int getFuelRemaining()
	{
		return endfuel;
	}
	
	public String getFormattedDuration()
	{
		int hours = time/3600;
		int minutes = (time%3600)/60;
		int seconds = time%60;
		
		return hours+":"+String.format("%02d", minutes)+":"+String.format("%02d", seconds);
	}
}