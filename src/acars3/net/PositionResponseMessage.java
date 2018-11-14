/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acars3.net;

/**
 *
 * @author Michael
 */
public class PositionResponseMessage extends NetMessage
{
    public int getType()
    {
        return NetMessage.POSITION_RESPONSE;
    }
    
    private String icao;
    private double latitude, longitude;
    
    public PositionResponseMessage(String icao, double latitude, double longitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;
        this.icao = icao;
    }
    
    public String getAirport()
    {
        return icao;
    }
    
    public double getLatitude()
    {
        return latitude;
    }
    
    public double getLongitude()
    {
        return longitude;
    }
    
}
