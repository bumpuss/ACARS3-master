/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acars3.net;

/**
 *
 * @author Michael
 */
public class ConnectMessage extends StatusMessage 
{
    public int getType()
    {
        return NetMessage.CONNECT;
    }
    
    private String message;
    
    public ConnectMessage(String dep, String arr, int status)
    {
        super(dep, arr, status);
    }
    
    public StatusMessage clone()
    {
        StatusMessage output = new StatusMessage(super.getDep(), super.getArr(), super.getStatusId());
        output.setIsStaff(isStaff());
        output.setName(getName());
        output.setRank(getRank());
        output.setId(getId());
        
        return output;
    }
    
    public void setMessage(String message)
    {
        this.message = message;
    }
    
    public String getMessage()
    {
        return message;
    }
}
