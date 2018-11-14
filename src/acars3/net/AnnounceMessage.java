/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acars3.net;

/**
 *
 * @author Michael
 */
public class AnnounceMessage extends ChatMessage
{
    public int getType()
    {
        return ANNOUNCE;
    }
    
    public AnnounceMessage(String msg)
    {
        super(msg);
    }
}
