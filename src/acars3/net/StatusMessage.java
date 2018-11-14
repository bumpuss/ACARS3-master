/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acars3.net;

import acars3.client.OnlineListRecord;
import acars3.client.Global;
/**
 *
 * @author Michael
 */
public class StatusMessage extends NetMessage
{
    public int getType()
    {
        return NetMessage.STATUS;
    }
    
    public static final int NOT_FLYING = 0;
    public static final int BRIEFING = 1;
    public static final int AT_GATE = 2;
    public static final int TAXI = 3;
    public static final int TAKING_OFF = 4;
    public static final int CLIMBING = 5;
    public static final int LEVEL_FLIGHT = 6;
    public static final int DESCENDING = 7;
    public static final int CRUISING = 8;
    public static final int APPROACH = 9;
    public static final int LANDING = 10;
    
    private String dep, arr, name, id;
    private char rank;
    private int status;
    private boolean isStaff;
    
    public StatusMessage(String dep, String arr, int status)
    {
        this.dep = dep;
        this.arr = arr;
        this.status = status;
    }
    
    public StatusMessage clone()
    {
        return this;
    }
    
    public String getDep()
    {
        return dep;    
    }
    
    public String getArr()
    {
        return arr;
    }
    
    public void setIsStaff(boolean s)
    {
        isStaff = s;
    }
    
    public boolean isStaff()
    {
        return isStaff;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public String getId()
    {
        return id;
    }
    
    public void setRank(char rank)
    {
        this.rank = rank;
    }
    
    public char getRank()
    {
        return rank;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getName()
    {
        return name;
    }
    
    public String getStatus()
    {
        switch(status)
        {
            case NOT_FLYING: return "Not Flying";
            case BRIEFING: return "Briefing";
            case AT_GATE: return "At Gate";
            case TAXI: return "Taxi";
            case TAKING_OFF: return "Taking Off";
            case CLIMBING: return "Climbing";
            case LEVEL_FLIGHT: return "Level Flight";
            case DESCENDING: return "Descending";
            case CRUISING: return "Cruising";
            case APPROACH: return "Approach";
            case LANDING: return "Landing";
            default: return "";
        }
    }
    
    public int getStatusId()
    {
        return status;
    }
    
    public OnlineListRecord getOLRecord()
    {
        return new OnlineListRecord(id, Global.convertRankAbbrev(rank), name, isStaff, dep, arr, getStatus());
    }
}
