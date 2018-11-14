/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acars3.net;

/**
 *
 * @author Michael
 */
public abstract class NetMessage implements java.io.Serializable
{
    public abstract int getType();
    
    public static final int LOGIN = 1;
    public static final int LOGIN_RETURN = 2;
    public static final int CHAT = 3;
    public static final int STATUS = 4;
    public static final int DISCONNECT = 5;
    public static final int CONNECT = 6;
    public static final int PRIVATE = 7;
    public static final int REQUEST_BOOKED = 8;
    public static final int REQUEST_POSITION = 9;
    public static final int POSITION_RESPONSE = 10;
    public static final int REQUEST_ZULU_TIME = 11;
    public static final int ZULU_TIME = 12;
    public static final int BOOKED_FLIGHT = 13;
    public static final int TAKEOFF = 14;
    public static final int LANDED = 15; 
    public static final int SCHEDULE_KEY = 16;
    public static final int KICK = 17;
    public static final int UNMUTE = 18;
    public static final int FULL_STATUS = 19;
    public static final int FILE_RECEIVED = 20;
    public static final int REQUEST_SCHEDULE = 21;
    public static final int SEND_PIREP = 22;
    public static final int ANNOUNCE = 23;
    public static final int STAFF = 24;
}
