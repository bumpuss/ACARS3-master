/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acars3.net;

import acars3.client.Global;
/**
 *
 * @author Michael
 */
public class RequestZuluTimeMessage extends NetMessage
{
    public int getType()
    {
        return NetMessage.REQUEST_ZULU_TIME;
    }
    
    private String icao;
    private int time;
    
    public RequestZuluTimeMessage(String icao, String localtime)
    {
        this.icao = icao;
        
        // convert time to 24 hours, in minutes
        time = Global.convertTime(localtime);
    }
    
    public int getLocalTime()
    {
        return time;
    }
    
    public String getICAO()
    {
        return icao;
    }
}
