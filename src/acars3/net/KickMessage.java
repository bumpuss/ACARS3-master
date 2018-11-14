/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acars3.net;

/**
 *
 * @author Michael
 */
public class KickMessage extends NetMessage
{
    public int getType()
    {
        return NetMessage.KICK;
    }
    
    public String admin, user, admin_name, time, user_name;
    
    public KickMessage(String userid)
    {
        this.user = userid;
    }
    
    public void setAdminId(String adminid)
    {
        this.admin = adminid;
    }
    
    public void setAdminName(String name)
    {
        this.admin_name = name;
    }
    
    public void setUserName(String name)
    {
        this.user_name = name;
    }
    
    public void setTime(String time)
    {
        this.time = time;
    }
    
    public String getAdminName()
    {
        return admin_name;
    }
    
    public String getAdminId()
    {
        return admin;
    }
    
    public String getUserName()
    {
        return user_name;
    }
    
    public String getUserId()
    {
        return user;
    }
    
    public String getTime()
    {
        return time;
    }
}
