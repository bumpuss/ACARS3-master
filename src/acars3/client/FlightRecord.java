/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acars3.client;

/**
 *
 * @author Michael
 */
public class FlightRecord implements java.io.Serializable
{
    public double KIAS, GS, TAS, latitude, longitude, altitude, heading, eng1_n1, eng1_n2, eng2_n1, eng2_n2, eng3_n1, eng3_n2, eng4_n1, eng4_n2, VS, Gforce, eng1_ff,
            eng2_ff, eng3_ff, eng4_ff, sim_rate, turnRate, bank, pitch, totalfuel, wind_dir, wind_spd, wind_gusts, vis;
    public String altimeter, com1, time, transponder;
    public int flaps;
    public boolean isOnGround, isAutopilot, isAutothrottle, isCrashed, isStalled, isOverspeed, isGearDown, isLandingLights, isNavLights, isStrobeLights, isPitotHeat;
    
    public FlightRecord(double KIAS, 
            double GS, 
            double TAS, 
            boolean isOnGround, 
            double latitude, 
            double longitude, 
            double altitude,
            double heading, 
            double eng1_n1, 
            double eng1_n2, 
            double eng2_n1, 
            double eng2_n2, 
            double eng3_n1,
            double eng3_n2, 
            double eng4_n1, 
            double eng4_n2, 
            boolean isAutopilot, 
            boolean isAutothrottle, 
            double VS, 
            boolean isCrashed,
            boolean isStalled, 
            boolean isOverspeed, 
            boolean isGearDown, 
            double Gforce, 
            int flaps, 
            double eng1_ff, 
            double eng2_ff, 
            double eng3_ff,
            double eng4_ff, 
            double sim_rate, 
            boolean isLandingLights, 
            boolean isNavLights, 
            boolean isStrobeLights, 
            boolean isPitotHeat, 
            double turnRate,
            double bank, 
            double pitch, 
            String transponder, 
            String altimeter, 
            String com1, 
            double totalfuel, 
            double wind_dir, 
            double wind_spd, 
            double wind_gusts,
            double vis, 
            String time)
    {
        this.KIAS = KIAS;
        this.GS = GS;
        this.TAS = TAS;
        this.isOnGround = isOnGround;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.heading = heading;
        this.eng1_n1 = eng1_n1;
        this.eng1_n2 = eng1_n2;
        this.eng2_n1 = eng2_n1;
        this.eng2_n2 = eng2_n2;
        this.eng3_n1 = eng3_n1;
        this.eng3_n2 = eng3_n2;
        this.eng4_n1 = eng4_n1;
        this.eng4_n2 = eng4_n2;
        this.isAutopilot = isAutopilot;
        this.isAutothrottle = isAutothrottle;
        this.VS = VS;
        this.isCrashed = isCrashed;
        this.isStalled = isStalled;
        this.isOverspeed = isOverspeed;
        this.isGearDown = isGearDown;
        this.Gforce = Gforce;
        this.flaps = flaps;
        this.eng1_ff = eng1_ff;
        this.eng2_ff = eng2_ff;
        this.eng3_ff = eng3_ff;
        this.eng4_ff = eng4_ff;
        this.sim_rate = sim_rate;
        this.isLandingLights = isLandingLights;
        this.isNavLights = isNavLights;
        this.isStrobeLights = isStrobeLights;
        this.isPitotHeat = isPitotHeat;
        this.turnRate = turnRate;
        this.bank = bank;
        this.pitch = pitch;
        this.transponder = transponder;
        this.altimeter = altimeter;
        this.com1 = com1;
        this.totalfuel = totalfuel;
        this.wind_dir = wind_dir;
        this.wind_spd = wind_spd;
        this.wind_gusts = wind_gusts;
        this.vis = vis;
        this.time = time;
    }
            
    public String toString()
    {
        return 
			"<airspeed>"+KIAS+"</airspeed>"+
			"<gairspeed>"+String.format("%.0f", GS)+"</gairspeed>"+
			"<tairspeed>"+String.format("%.0f", TAS)+"</tairspeed>"+
			"<ground>"+(isOnGround?1:0)+"</ground>"+
			"<latitude>"+longitude+"</latitude>"+
			"<longitude>"+latitude+"</longitude>"+
			"<altitude>"+String.format("%.0f", altitude)+"</altitude>"+
			"<heading>"+String.format("%.0f", heading)+"</heading>"+
			"<e1n1>"+String.format("%.0f", eng1_n1)+"</e1n1>"+
			"<e1n2>"+String.format("%.0f", eng1_n2)+"</e1n2>"+
			"<e2n1>"+String.format("%.0f", eng2_n1)+"</e2n1>"+
			"<e2n2>"+String.format("%.0f", eng2_n2)+"</e2n2>"+
			"<e3n1>"+String.format("%.0f", eng3_n1)+"</e3n1>"+
			"<e3n2>"+String.format("%.0f", eng3_n2)+"</e3n2>"+
			"<e4n1>"+String.format("%.0f", eng4_n1)+"</e4n1>"+
			"<e4n2>"+String.format("%.0f", eng4_n2)+"</e4n2>"+
			"<autopilot>"+(isAutopilot?1:0)+"</autopilot>"+
			"<autothrottle>"+(isAutothrottle?1:0)+"</autothrottle>"+
			"<verticalspeed>"+String.format("%.2f", VS)+"</verticalspeed>"+
			"<crash>"+(isCrashed?1:0)+"</crash>"+
			"<stall>"+(isStalled?1:0)+"</stall>"+
			"<overspeed>"+(isOverspeed?1:0)+"</overspeed>"+
			"<geardown>"+(isGearDown?1:0)+"</geardown>"+
			"<gforce>"+String.format("%.1f", Gforce)+":f</gforce>"+
			"<flaps>"+flaps+"</flaps>"+
			"<e1fuelflow>"+String.format("%.2f", eng1_ff)+"</e1fuelflow>"+
			"<e2fuelflow>"+String.format("%.2f", eng2_ff)+"</e2fuelflow>"+
                        "<e3fuelflow>"+String.format("%.2f", eng3_ff)+"</e3fuelflow>"+
                        "<e4fuelflow>"+String.format("%.2f", eng4_ff)+"</e4fuelflow>"+
			"<simrate>"+sim_rate+"</simrate>"+
			"<landinglights>"+(isLandingLights?1:0)+"</landinglights>"+
			"<navlights>"+(isNavLights?1:0)+"</navlights>"+
			"<strobelights>"+(isStrobeLights?1:0)+"</strobelights>"+
			"<pitotheat>"+(isPitotHeat?1:0)+"</pitotheat>"+
			"<turnrate>"+String.format("%.2f", turnRate)+"</turnrate>"+
			"<bank>"+String.format("%.2f", bank)+"</bank>"+
			"<pitch>"+String.format("%.2f", pitch)+"</pitch>"+
			"<transponder>"+transponder+"</transponder>"+
			"<altimeter>"+altimeter+"</altimeter>"+
			"<com1>"+com1+"</com1>"+
			"<totalfuel>"+String.format("%.2f", totalfuel)+"</totalfuel>"+
			"<winddirection>"+String.format("%.0f", wind_dir)+"</winddirection>"+
			"<windspeed>"+String.format("%.0f", wind_spd)+"</windspeed>"+
			"<windgusts>"+String.format("%.0f", wind_gusts)+"</windgusts>"+
			"<visibility>"+String.format("%.1f", vis)+"</visibility>"+
			"<currenttime>"+time+"</currenttime>";
    }
}
