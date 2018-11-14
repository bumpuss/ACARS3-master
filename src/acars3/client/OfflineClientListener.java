/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acars3.client;

import acars3.net.NetMessage;
/**
 *
 * @author Michael
 */
public class OfflineClientListener extends ClientListener
{
    public OfflineClientListener()
    {
    }
    
    public boolean send(NetMessage msg){return false;}
    public void run(){}
    public boolean handleMessage(NetMessage msg){return false;}
    public NetMessage getNextMessage(){return null;}
}
