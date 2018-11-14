/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acars3.net;

/**
 *
 * @author Michael
 */
public class LoginMessage extends NetMessage
{
    public int getType()
    {
        return NetMessage.LOGIN;
    }
    
    private String username, password, version;
    
    public LoginMessage(String username, String password, String version)
    {
        this.username = username;
        this.password = password;
        this.version = version;
    }
    
    public String getVersion()
    {
        return version;
    }
    
    public String getUsername()
    {
        return username;
    }
    
    public String getPassword()
    {
        return password;
    }
}
