/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acars3.net;

import acars3.client.FlightRecord;
/**
 *
 * @author Michael
 */
public class FullStatusMessage extends StatusMessage
{
    public int getType()
    {
        return NetMessage.FULL_STATUS;
    }
    
    private FlightRecord record;
    private String route, aircraft, version;
    
    public FullStatusMessage(String dep, String arr, int status, FlightRecord record, String aircraft, String route, String version)
    {
        super(dep, arr, status);
        this.record = record;
        this.route = route;
        this.aircraft = aircraft;
        this.version = version;
    }
    
    public String getVersion()
    {
        return version;
    }
    public String getAircraft()
    {
        return aircraft;
    }
    public String getRoute()
    {
        return route;
    }
    
    public FlightRecord getFlightRecord()
    {
        return record;
    }
}
