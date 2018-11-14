/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acars3.net;

import acars3.client.Flight;
import java.util.List;

/**
 *
 * @author Michael
 */
public class BookedFlightMessage extends NetMessage
{
    public int getType()
    {
        return NetMessage.BOOKED_FLIGHT;
    }
    
    private List<Flight> flights;
    
    public BookedFlightMessage(List<Flight> flights)
    {
        this.flights = flights;
    }
    
    public List<Flight> getFlights()
    {
        return flights;
    }
}
