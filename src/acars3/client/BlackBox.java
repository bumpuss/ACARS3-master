package acars3.client;

import java.awt.event.*;
import javax.swing.Timer;
import java.io.PrintStream;
import com.flightsim.fsuipc.FSUIPC;

public class BlackBox extends FSUIPC
{
	private boolean valid;
	protected EngineData eng1, eng2, eng3, eng4;
	
	private String aircraft;
	private byte engine_type;
	

	public BlackBox()
	{
		valid = false;
		
		// prop RPM
		
		eng1 = new EngineData()
		{
			public double getFuelFlow()
			{
				return getDouble(0x918);
			}
			public double getN1()
			{
				return getShort(0x898)/163.84;
			}	
			public double getN2()
			{
				if(engine_type == 1)
				{
					return getShort(0x896)/163.84;
				}
				else
				{
					return 0;
				}
			}
		};
		
		eng2 = new EngineData()
		{
			public double getFuelFlow()
			{
				return getDouble(0x9B0);
			}
			public double getN1()
			{
				return getShort(0x930)/163.84;
			}	
			public double getN2()
			{
				if(engine_type == 1)
				{
					return getShort(0x92E)/163.84;
				}
				else
				{
					return 0;
				}
			}
		};
		
		eng3 = new EngineData()
		{
			public double getFuelFlow()
			{
				return getDouble(0xA48);
			}
			public double getN1()
			{
				return getShort(0x9C8)/163.84;
			}	
			public double getN2()
			{
				if(engine_type == 1)
				{
					return getShort(0x9C6)/163.84;
				}
				else
				{
					return 0;
				}
			}
		};
		
		eng4 = new EngineData()
		{
			public double getFuelFlow()
			{
				return getDouble(0xAE0);
			}
			public double getN1()
			{
				return getShort(0xA60)/163.84;
			}	
			public double getN2()
			{
				if(engine_type == 1)
				{
					return getShort(0xA5E)/163.84;
				}
				else
				{
					return 0;
				}
			}
		};

	}

	public static double getDistance(double lat1, double lng1, double lat2, double lng2)
        {
            double earthRadius = 3958.75;
            double dLat = Math.toRadians(lat2-lat1);
            double dLng = Math.toRadians(lng2-lng1);
            double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                       Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                       Math.sin(dLng/2) * Math.sin(dLng/2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
            double dist = earthRadius * c;

            return dist / 1.15077945 ;
        }
	
	public void setActivated(boolean active)
	{
		if(active)
		{
			valid = open();
			aircraft = getAircraftTitleHelper();
			engine_type = getEngineType();
		}
		else
		{
			close();
			valid = false;
		}
	}	
	
	
	public boolean isValid()
	{
		return valid;
	}
	
	public void setValid(boolean valid)
	{
		this.valid = valid;
		
	}
	
	public String getAircraftTitle()
	{
		if(aircraft == null)
		{
			aircraft = getAircraftTitleHelper();
		}
		return aircraft;
	}
	
	private String getAircraftTitleHelper()
	{
		return getString(0x3D00, 256);
	}
	
	public EngineData getEng1()
	{
		return eng1;
	}
	
	public EngineData getEng2()
	{
		return eng2;
	}
	
	public EngineData getEng3()
	{
		return eng3;
	}
	
	public EngineData getEng4()
	{
		return eng4;
	}
	
	
	
	
	public double getLatitude()
	{
		return 90.0*(double)getLong(0x0560)/(10001750.0 * 65536.0 * 65536.0);
	}
	
	public double getLongitude()
	{
		return 360.0*(double)getLong(0x0568)/(65536.0 * 65536.0 * 65536.0 * 65536.0);
	}
	
	public double getTAS()
	{
		return getInt(0x02B8)/128.0;
	}
	
	public double getSimRate()
	{
		return getShort(0x0C1A)/256.0;
	}
	
	public double getTurnRate()
	{
		return getShort(0x037C)/256.0;
	}
	
	public double getBank()
	{
		return getDouble(0x2f78);
	}
	
	public double getPitch()
	{
		return -getDouble(0x2f70);
	}
	
	public String getAltimeter()
	{
		//return ""+(int)(getShort(0x0330)/16.0);
		return String.format("%.2f", getShort(0x0330)/33.86/16);
	}
	
	public double getWindDirection()
	{
		return (((((int)getShort(0x0E92))*360) >> 16) +360)%360;
	}
	
	public int getWindSpeed()
	{
		return getShort(0x0E90);
	}
	
	public int getWindGusts()
	{
		return getShort(0x0E94);
	}
	
	public double getVisibility()
	{
		return getShort(0x0E8A)/100.0;
	}
	
	public double getGForce()
	{
		return getShort(0x11BA)/624.0;
	}
	
	public String getCom1()
	{
		String temp = String.format("%x", getShort(0x034E));
		
		return "1"+temp.substring(0, 2)+"."+temp.substring(2, 4);
		
	}
	
	public int getHeading()
	{
		return 360-(((getInt(0x02A0)*360) >> 16) - ((360*(getInt(0x0580) >> 16)) >> 16) +360)%360;
	}
	
	public double getKIAS()
	{
		return getInt(0x02BC)/128.0;
	}
	
	public double getAltitude()
	{
		return getInt(0x0574)*3.28084;
	}
	
	public double getVS()
	{
		return getInt(0x02C8)*60.0*3.28084/256;
	}
	
	public double getLandingRate()
	{
		return getInt(0x030C)*60.0*3.28084/256;
	}
	
	public double getGS()
	{
		return getInt(0x02B4)/65536.0/1852*3600;
	}
	
	public double getEmptyWeight()
	{
		return (getInt(0x1330))/256.0;
	}
	public double getWeight()
	{
		return getDouble(0x30C0);
	}
		
	public int getFuel()
	{
		return getInt(0x126C);
	}
	
	public double getDistanceRemaining()
	{
		return 0;
	}
	
	public double getTotalDistance()
	{
		return 100;
	}
	
	public boolean isAutopilot()
	{
		return getInt(0x07BC) != 0;
	}
	
	public boolean isAutothrottle()
	{
		return getInt(0x810) != 0;
	}
	
	public boolean isOnGround()
	{
		return getShort(0x0366) != 0;
	}
	
	public boolean isCrashed()
	{
		return getShort(0x0840) != 0;
	}
	
	public boolean isStalled()
	{
		return getByte(0x036C) != 0;
	}
	
	public boolean isOverspeed()
	{
		return getByte(0x036D) != 0;
	}
	
	public boolean isLandingLights()
	{
		return (getShort(0x0D0C) & 0x4) != 0;
	}
	
	public boolean isStrobeLights()
	{
		return (getShort(0x0D0C) & 0x10) != 0;
	}
	
	public boolean isPitotHeat()
	{
		return getByte(0x029C) != 0;
	}
	
	public boolean isGearDown()
	{
		return (getInt(0xBE8) != 0) || (getInt(0xBEC) != 0) || (getInt(0xBF0) != 0);
	}
	
	public boolean isNavLights()
	{
		return (getShort(0x0D0C) & 0x1) != 0;
	}
	
	public boolean isParkingBrake()
	{
		return getShort(0x0BC8) != 0;
	}
	
	public String getTransponder()
	{
		return String.format("%04x", getShort(0x0354));
	}
	
	public String getTime()
	{
		return getByte(0x023B)+":"+getByte(0x023C)+":"+getByte(0x023A);
	}
	
	public String getFormattedTime()
	{
		return getByte(0x0238)+":"+String.format("%02d", getByte(0x0239))+":"+String.format("%02d", getByte(0x023A));
	}
	
	public int getFlaps()
	{
		return (int)(Math.min(getInt(0x0BE0), getInt(0x0BE4))/163.84);
	}
	
	public byte getEngineType()
	{
		return getByte(0x0609);
	}
	
	public boolean isPaused()
	{
		return getShort(0x0264) != 0;
	}
	
	public String getVersion()
	{
		short temp = getShort(0x3308);
		switch(temp)
		{
			case 1: return "FS98";
			case 2: return "FS2000";
			case 3: return "CFS2";
			case 4: return "CFS1";
			case 5: return "reserved";
			case 6: return "FS2002";
			case 7: return "FS2004";
			case 8: return "FSX";
			case 9: return "ESP";
			case 10: return "P3D";
			default: return ""+temp;
		}
	}
	
	public String getCurrentDate()
	{
		return "";
	}
	
	public int getNumEngines()
	{
		return getShort(0x0AEC);
	}
	
	
	
	
	
	
	
	
	
	public abstract class EngineData
	{

		
		public abstract double getN1();
		
		public abstract double getN2();
		
		public abstract double getFuelFlow();
		
		public boolean isValid()
		{
			return valid;
		}
		

	}
}