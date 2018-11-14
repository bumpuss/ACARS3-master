/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acars3.net;

/**
 *
 * @author Michael
 */
public class DisconnectMessage extends ChatMessage
{
    public int getType()
    {
        return NetMessage.DISCONNECT;
    }
    
    private String id, name;
    
    public DisconnectMessage(String message, String id, String name)
    {
        super(message);
        
        this.id = id;
        this.name = name;
    }
    
    public String getId()
    {
        return id;
    }
    
    public String getName()
    {
        return name;
    }

}
