/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acars3.net;

/**
 *
 * @author Michael
 */
public class ChatMessage extends NetMessage
{
    public int getType()
    {
        return NetMessage.CHAT;
    }
    
    private String msg;
    private String time, from_name;
    
    public ChatMessage(String msg)
    {
        this.msg = msg;
    }
    
    
    public void setTime(String time)
    {
        this.time = time;
    }
    
    public void setFromName(String name)
    {
        from_name = name;
    }
    public String getTime()
    {
        return time;
    }
    
    public String getFromName()
    {
        return from_name;
    }
    
    public String getMessage()
    {
        return msg;
    }
}
