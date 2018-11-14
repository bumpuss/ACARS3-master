/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acars3.net;

/**
 *
 * @author Michael
 */
public class TakeoffMessage extends ChatMessage
{
    public int getType()
    {
        return NetMessage.TAKEOFF;
    }
    
    public TakeoffMessage(String message)
    {
        super(message);
    }
}
