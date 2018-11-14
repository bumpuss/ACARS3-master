package acars3.client;

public class OnlineListRecord implements Comparable<OnlineListRecord>
{
	private String callsign, rank, name, origin, dest, status;
	private boolean isStaff;
	private int id;
	
	public OnlineListRecord(String id, String rank, String name, boolean isStaff, String origin, String dest, String status)
	{
		this.callsign = id.substring(0, 3);
		this.id = Integer.parseInt(id.substring(3));
		this.name = name;
		this.rank = rank;
		this.origin = origin;
		this.dest = dest;
		this.status = status;
		this.isStaff = isStaff;
	}
        
        public boolean equals(Object o)
        {
            OnlineListRecord rhs = (OnlineListRecord)o;
            
            return id == rhs.id && callsign.equals(rhs.callsign) && name.equals(rhs.name);
        }
        
        public void copy(OnlineListRecord rhs)
        {
            origin = rhs.origin;
            rank = rhs.rank;
            dest = rhs.dest;
            status = rhs.status;
            isStaff = rhs.isStaff;
        }
	
	public void setOrigin(String origin)
	{
		this.origin = origin;
	}
	
	public void setDest(String dest)
	{
		this.dest = dest;
	}
	
	public void setStatus(String status)
	{
		this.status = status;
	}
	
	
	
	
	public String getId()
	{
		return callsign+id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getRank()
	{
		return rank;
	}
	
	public boolean isStaff()
	{
		return isStaff;
	}
	
	public String getOrigin()
	{
		return origin;
	}
	
	public String getDest()
	{
		return dest;
	}
	
	public String getStatus()
	{
		return status;
	}
	
	public int compareTo(OnlineListRecord rhs)
	{
		return id-rhs.id;
	}
}