/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acars3.net;

/**
 *
 * @author Michael
 */
public class StaffMessage extends ChatMessage
{
    public int getType()
    {
        return STAFF;
    }
    
    public StaffMessage(String msg)
    {
        super(msg);
    }
}
