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
public class ZuluTimeMessage extends NetMessage
{
    public int getType()
    {
        return NetMessage.ZULU_TIME;
    }
    
    private String icao;
    private int time, zulu_time;
    
    public ZuluTimeMessage(String icao, int localtime, int zulu_time)
    {
        this.icao = icao;
        this.time = localtime;
        this.zulu_time = zulu_time;
    }
    
    public String getICAO()
    {
        return icao;
    }
    
    public String getLocalTime()
    {
        return Global.dispLocalTime(time);
    }
    
    public String getZuluTime()
    {
        return Global.dispZuluTime(zulu_time);
    }
}
