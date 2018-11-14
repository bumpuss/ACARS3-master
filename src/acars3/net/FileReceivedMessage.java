/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acars3.net;

/**
 *
 * @author Michael
 */
public class FileReceivedMessage extends NetMessage
{
    public int getType()
    {
        return NetMessage.FILE_RECEIVED;
    }
    
    public static final int PIREP = 0;
    public static final int SCHEDULE = 1;
    public static final int SCHEDULE_KEY = 2;
    
    private int type;
    private String name;
    private boolean success;
    
    public FileReceivedMessage(String name, int type)
    {
        this(name, type, true);
    }
    public FileReceivedMessage(String name, int type, boolean success)
    {
        this.name = name;
        this.type = type;
        this.success = success;
    }
    
    public boolean isSuccess()
    {
        return success;
    }
    
    public int getFileType()
    {
        return type;
    }
    
    public String getName()
    {
        return name;
    }
}
