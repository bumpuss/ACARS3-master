/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acars3.client;

import javax.crypto.SecretKey;
/**
 *
 * @author Michael
 */
public class ScheduleKey implements java.io.Serializable
{
    private SecretKey key;
    
    protected ScheduleKey(SecretKey key)
    {
        this.key = key;
    }
    
    protected SecretKey getKey()
    {
        return key;
    }
}
