/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acars3.net;

import javax.crypto.SecretKey;
/**
 *
 * @author Michael
 */
public class ScheduleKeyMessage extends NetMessage
{
    public int getType()
    {
        return NetMessage.SCHEDULE_KEY;
    }
    
    private SecretKey key;
    
    public ScheduleKeyMessage(SecretKey key)
    {
        this.key = key;
    }
    
    public SecretKey getKey()
    {
        return key;
    }
        
}
