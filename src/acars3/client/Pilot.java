package acars3.client;

public class Pilot implements java.io.Serializable
{
	private String name, hub, currentLoc, id;
	private double hours, money;
        private boolean isAdmin;
        private char rank;
        
        public Pilot()
        {
            this("", ' ', "", "", "", 0, 0, false);
        }
	public Pilot(String id, char rank, String name, String hub, String currentLoc, double hours, double money, boolean isAdmin)
	{
		this.name = name;
		this.rank = rank;
		this.hub = hub;
		this.currentLoc = currentLoc;
		this.hours = hours;
		this.money = money;
                this.id = id;
                this.isAdmin = isAdmin;
	}
        
        public void setCurrentLoc(String currentLoc)
        {
            this.currentLoc = currentLoc;
        }
        
        public boolean isAdmin()
        {
            return isAdmin;
        }
	
        public String getId()
        {
            return id;
        }
	public String getName()
	{
		return name;
	}
	
	public String getRank()
	{
		return Global.convertRank(rank);
	}
        
        public String getAbbrevRank()
        {
            return Global.convertRankAbbrev(rank);
        }
        
        public char getCAT()
        {
            return rank;
        }
	
	public String getHub()
	{
		return hub;
	}
	
	public String getCurrentLocation()
	{
		return currentLoc;
	}
	
	public double getHours()
	{
		return hours;
	}
	
	public double getMoney()
	{
		return money;
	}
}