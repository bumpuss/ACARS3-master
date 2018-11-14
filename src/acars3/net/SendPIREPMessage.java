/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acars3.net;

import acars3.client.PIREP;
/**
 *
 * @author Michael
 */
public class SendPIREPMessage extends NetMessage
{
    public int getType()
    {
        return NetMessage.SEND_PIREP;
    }
    
    private PIREP pirep;
    
    public SendPIREPMessage(PIREP pirep)
    {
        this.pirep = pirep;
    }
    
    public PIREP getPIREP()
    {
        return pirep;
    }
}
