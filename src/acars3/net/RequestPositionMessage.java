/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acars3.net;

/**
 *
 * @author Michael
 */
public class RequestPositionMessage extends NetMessage
{
    public int getType()
    {
        return NetMessage.REQUEST_POSITION;
    }
    
    private String icao;
    
    public RequestPositionMessage(String icao)
    {
        this.icao = icao;
    }
    
    public String getAirport()
    {
        return icao;
    }
}
