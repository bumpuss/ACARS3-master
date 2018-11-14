/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acars3.net;

/**
 *
 * @author Michael
 */
public class LandedMessage extends ChatMessage
{
    public int getType()
    {
        return NetMessage.LANDED;
    }
    
    public LandedMessage(String message)
    {
        super(message);
    }
}
