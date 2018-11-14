package acars3.client;

import javax.swing.Timer;
import java.io.*;
import java.awt.event.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import acars3.net.StatusMessage;
import acars3.net.TakeoffMessage;
import acars3.net.LandedMessage;
import acars3.net.FullStatusMessage;

public class ACARS extends BlackBox
{
	private PrintStream out;
	
	private Timer timer;
	
	private int count;
	
	private FlightRecord tempRecord, tempRecord2;
	private int countdown;
	private boolean onGround;
	private boolean cruising;
        private boolean takeoffRecorded;
	private int lastAlt;
	
	private int time;
	private int pauseTime;
	
	private int beginfuel;
	private int recordCount;
	private String begintime;
	

	private PIREP pirep;
	private boolean isEvent, isOnline;
	
	private SecretKey aesKey;
	
        private DispatchPanel panel;
        
        private int curr_status;
        private ClientListener client;
        
        private Flight flight;
        
        private long lastPosUpdate;
        
        private double begin_lat, begin_long, end_lat, end_long;
        
	public ACARS(ClientListener client)
	{
                this.client = client;
		timer = new Timer(1000, new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				update();
			}
		});
		
	}
        
        public void setPanel(DispatchPanel panel)
        {
            this.panel = panel;
        }
	
	public void setActivated(boolean active)
	{
		super.setActivated(active);
		
		if(active)
		{
			onGround = true;
			count = 0;
			cruising = false;
		}
	}
	
	public boolean beginFlight(Flight flight, String alt, String altfull, String route, boolean isOnline)
	{
            this.flight = flight;
            lastPosUpdate = 0;
            curr_status = StatusMessage.AT_GATE;
            
            SecretKey aesKey = null;
            IvParameterSpec ivParameterSpec = null;
            Cipher encryptCipher = null;
            
            
            try
            {
                KeyGenerator kgen = KeyGenerator.getInstance("AES");
                kgen.init(128);
                aesKey = kgen.generateKey();
                ivParameterSpec = new IvParameterSpec(aesKey.getEncoded());
                encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                encryptCipher.init(Cipher.ENCRYPT_MODE, aesKey, ivParameterSpec);
            }
            catch(Exception ex)
            {
                ex.printStackTrace(System.err);
                System.exit(1);
            }
            
            
                begintime = getTime();
                
 
                
		beginfuel = getFuel();
                String begindate = getCurrentDate();
                        
		pirep = new PIREP(flight.getNumber(), flight.getOriginICAO(), flight.getDestICAO(), getAircraftTitle(), route, Version.VERSION,
                        begintime, beginfuel, flight.getPax(), isOnline, begindate, flight.getEra(), flight.getCarrier(), aesKey);

		
		File dir = new File("pireps\\");
                
                if(!dir.exists())
                {
                    dir.mkdir();
                }
		
		try
		{
			//out = new PrintStream(new CipherOutputStream(new BufferedOutputStream(new FileOutputStream(pirep.getFile())), encryptCipher));
                        out = new PrintStream(new BufferedOutputStream(new FileOutputStream(pirep.getFile())));
		}
		catch(IOException ex)
		{
                    ex.printStackTrace(System.err);
			return false;
		}
		
		
		
		time = 0;
		recordCount = 0;
		pauseTime = 0;
		count = 2;
		
                begin_lat = getLatitude();
                begin_long = getLongitude();
                
		out.print("<?xml version=\"1.0\"?><flight><beginflight><acarsbuild>"+Version.VERSION+"</acarsbuild><fsversion>"+
			getVersion()+"</fsversion><flightnumber>"+flight.getNumber()+"</flightnumber><depicao>"+flight.getOriginICAO()+"</depicao><depfull>"+
			flight.getOriginDescription()+"</depfull><arricao>"+flight.getDestICAO()+"</arricao><arrfull>"+flight.getDestDescription()+"</arrfull><alticao>"+alt+"</alticao><altfull>"+
			altfull+"</altfull><filedaircraft>"+flight.getAircraft()+"</filedaircraft><currentaircraft>"+getAircraftTitle()+"</currentaircraft><route>"+
			route+"</route><comments /><weight>"+getWeight()+"</weight><fuel>"+getFuel()+"</fuel><online>"+isOnline+"</online><begintime>"+
			begintime+"</begintime><currentdate>"+begindate+"</currentdate><nengines>"+getNumEngines()+"</nengines></beginflight>");
		out.flush();

		
		timer.start();
		
		return true;
	}
	
	public LogbookEntry endFlight(String comments, boolean isEvent)
	{
		timer.stop();
		
		if(isEvent)
		{
			comments += "\nATTN: PIREP APPROVERS FROM ACARS: The pilot indicated this flight was an official USAVA event flight.";
		}
		
                double endfuel = getFuel();
                double duration = time/3600.0;
                String endtime = getTime();
                
                end_lat = getLatitude();
                end_long = getLongitude();
                
                pirep.setDistance(getDistance(begin_lat, begin_long, end_lat, end_long));
                
		out.print("<endflight><positions>"+recordCount+"</positions><duration>"+String.format("%.1f", duration)+"</duration><weight>"+
			String.format("%.1f", getWeight())+"</weight><fuel>"+endfuel+"</fuel><comments>"+
			comments+"</comments><currenttime>"+endtime+"</currenttime></endflight></flight>");
		
		out.close();
                
                pirep.setComments(comments);
                pirep.setEndFuel(endfuel);
                pirep.setArrTime(endtime);
                pirep.setDuration(duration);
                
		
		return new LogbookEntry(pirep.getOrigin(), pirep.getDest(), begintime, endtime, beginfuel, getFuel(), time, getWeight(), getLandingRate());
	}
	
	
	public PIREP getPIREP()
	{
		return pirep;
	}
	
	public int getDuration()
	{
		return time;
	}
	
	public int getPauseTime()
	{
		return pauseTime;
	}
	
	public void landingRecorded(FlightRecord record, double landingRate)
        {
            pirep.setLandingRate(landingRate);
                    
            client.send(new LandedMessage(" has landed in a "+getAircraftTitle()+". Touchdown speed was "+String.format("%.0f", record.KIAS)+" KIAS, "+
                    "descent rate was "+String.format("%.2f", landingRate)+" fpm with "+String.format("%.2f", record.totalfuel)+" lbs of fuel onboard."));
        }
        
	public void takeoffRecorded(FlightRecord record)
        {
            takeoffRecorded = true;
            pirep.setTakeoffRate(record.KIAS);
            
            client.send(new TakeoffMessage(" has taken off in a "+getAircraftTitle()+". Takeoff speed was "+String.format("%.0f", record.KIAS)+" KIAS."));
        }
        
        public void updatePosition(FlightRecord record)
        {
            client.send(new FullStatusMessage(pirep.getOrigin(), pirep.getDest(), curr_status, record, pirep.getAircraft(), pirep.getRoute(), Version.VERSION));
        }
        
        public void stageChanged(int stage)
        {
            client.send(new StatusMessage(pirep.getOrigin(), pirep.getDest(), stage));
        }
        
        
        
        public void updated(FlightRecord record)
        {
            long time = System.nanoTime();
            
            
            
            int newStage = 0;
            
            double dist = getDistance(record.latitude, record.longitude, flight.getDestLatitude(), flight.getDestLongitude());
            
            if(isOnGround())
            {
                if(record.GS > 30)
                {
                    if(takeoffRecorded)
                    {
                        newStage = StatusMessage.LANDING;
                    }
                    else
                    {
                        newStage = StatusMessage.TAKING_OFF;
                    }
                    
                }
                else if(record.GS != 0)
                {
                    newStage = StatusMessage.TAXI;
                }
                else
                {
                    newStage = StatusMessage.AT_GATE;
                }
            }
            else
            {
                if(dist < 3)
                {
                    newStage = StatusMessage.LANDING;
                }
                else if(dist < 10)
                {
                    newStage = StatusMessage.APPROACH;
                }
                else if(record.VS > 800)
                {
                    newStage = StatusMessage.CLIMBING;
                }
                else if(record.VS < -800)
                {
                    newStage = StatusMessage.DESCENDING;
                }
                else if(record.altitude > 10000)
                {
                    newStage = StatusMessage.CRUISING;
                }
                else
                {
                    newStage = StatusMessage.LEVEL_FLIGHT;
                }
            }
            
            if( (time - lastPosUpdate) / 1.0e9 > Global.POS_UPDATE_INTERVAL)
            {
                lastPosUpdate = time;
                curr_status = newStage;
                updatePosition(record);
            }
            
            if(newStage != curr_status)
            {
                curr_status = newStage;
                stageChanged(newStage);
            }
        }
        
        public void update(FlightRecord record, String type)
        {
            out.print("<"+type+">"+record+"</"+type+">");
            out.flush();
            updated(record);
        }
	
	
	public void update()
	{
		if(isPaused())
		{
			pauseTime++;
			return;
		}
		time++;
		
		
		if(onGround != isOnGround())
		{
			onGround = !onGround;
			
			if(countdown >= 0)
			{
				countdown = -1;
				
				if(--count < 0)
				{
					count += 15;
					
					update(tempRecord, "flightdata");
					recordCount++;
				}
				else if(count == 0)
				{
					update(record(), "flightdata");
					recordCount++;
				}
			}
			else
			{
				countdown = 5;
				
				if(--count == 0)
				{
					tempRecord = tempRecord2 = record();
				}
				else
				{
					tempRecord2 = record();
				}
			}	
		}
		else if(--countdown == 0)
		{
			--countdown;
			
			if(onGround)
			{
                                update(tempRecord2, "landing");
				out.print("<landingrate>"+getLandingRate()+"</landingrate>");
				out.flush();
				landingRecorded(tempRecord2, getLandingRate());
				recordCount++;
			}
			else
			{
                                update(tempRecord2, "takeoff");
				takeoffRecorded(tempRecord2);
				recordCount++;
			}
			
			count = 9;
		}
		else if(--count == 0)
		{
			int alt = (int)getAltitude();
			
			if(Math.abs(alt-lastAlt) < 100 && alt > 10000 && !isOnGround())
			{
				
				cruising = true;
				
                                update(record(), "flightdata");
                                recordCount++;
                                count += 15;
			}
			else
			{
				update(record(), "flightdata");
				recordCount++;
			}
			
			lastAlt = alt;

			count += 15;	
		}
		
		//System.out.println(count);
	}
	
	public FlightRecord record()
	{
            return new FlightRecord(
                    getKIAS(),
                    getGS(),
                    getTAS(),
                    isOnGround(),
                    getLatitude(),
                    getLongitude(),
                    getAltitude(),
                    getHeading(),
                    eng1.getN1(),
                    eng1.getN2(),
                    eng2.getN1(),
                    eng2.getN2(),
                    eng3.getN1(),
                    eng3.getN2(),
                    eng4.getN1(),
                    eng4.getN2(), 
                    isAutopilot(),
                    isAutothrottle(),
                    getVS(),
                    isCrashed(),
                    isStalled(),
                    isOverspeed(),
                    isGearDown(),
                    getGForce(),
                    getFlaps(),
                    eng1.getFuelFlow(),
                    eng2.getFuelFlow(),
                    eng3.getFuelFlow(),
                    eng4.getFuelFlow(),
                    getSimRate(),
                    isLandingLights(),
                    isNavLights(),
                    isStrobeLights(),
                    isPitotHeat(),
                    getTurnRate(),
                    getBank(),
                    getPitch(),
                    getTransponder(),
                    getAltimeter(),
                    getCom1(),
                    getFuel(),
                    getWindDirection(),
                    getWindSpeed(),
                    getWindGusts(),
                    getVisibility(),
                    getTime()
                    );


	}
}