/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acars3.net;

/**
 *
 * @author Michael
 */
public class PrivateMessage extends ChatMessage
{
    public int getType()
    {
        return NetMessage.PRIVATE;
    }
    
    private String to, from;
    private String to_name, from_name, time;
    
    public PrivateMessage(String to, String msg)
    {
        super(msg);
        this.to = to;
    }
    
    public PrivateMessage(String from, String to, String msg)
    {
        super(msg);
        this.to = to;
        this.from = from;
    }
    
    public void setTime(String t)
    {
        this.time = t;
    }
    
    public void setFromName(String f)
    {
        from_name = f;
    }
    
    public void setToName(String t)
    {
        to_name = t;
    }
    
    public void setFrom(String f)
    {
        from = f;
    }
    
    
    public String getToName()
    {
        return to_name;
    }
    
    public String getFromName()
    {
        return from_name;
    }
    
    public String getTo()
    {
        return to;
    }
    
    public String getFrom()
    {
        return from;
    }
    
    public String getTime()
    {
        return time;
    }
}
