/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acars3.net;

import acars3.client.Pilot;
/**
 *
 * @author Michael
 */
public class LoginReturnMessage extends NetMessage
{
    public int getType()
    {
        return NetMessage.LOGIN_RETURN;
    }
    
    private Pilot pilot;
    private boolean version;
    private int scheduleVersion;
    
    public LoginReturnMessage(Pilot pilot, boolean hasVersion, int scheduleVersion)
    {
        this.pilot = pilot;
        this.version = hasVersion;
        this.scheduleVersion = scheduleVersion;
    }
    
    public int getScheduleVersion()
    {
        return scheduleVersion;
    }

    public boolean hasVersion()
    {
        return version;
    }
    
    public Pilot getPilot()
    {
        return pilot;
    }
}
