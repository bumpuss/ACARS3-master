package acars3.client;

import java.io.*;
import java.util.*;
import javax.crypto.*;
import javax.crypto.spec.*;

public class ConvertSchedule
{
	public static void main(String[] args) throws Exception
	{
		
		Set<Flight> routes = new HashSet<Flight>();
		
		
		Scanner filein = new Scanner(new File("output.csv"));
		
		while(filein.hasNext())
		{
			Scanner chopper = new Scanner(filein.nextLine().replaceAll("[\\\"]", ""));
			chopper.useDelimiter(",");
			
			if(!chopper.hasNextInt())
			{
				System.out.println(chopper.next());
			}
			int id = chopper.nextInt(); // id
			
			int num = chopper.nextInt();
			String start_date = chopper.next();
			String end_date = chopper.next();
			
			for(int i = 0; i < 7; i++)
			{
				chopper.next();
			}
			
			String origin_iata = chopper.next();
			String origin_icao = chopper.next();
			
			String origin_descrip = chopper.next()+","+chopper.next();
                        
                        if(origin_descrip.length() > 4)
                        {
                            origin_descrip = origin_descrip.substring(4);
                        }
                        
			chopper.next();
			
			String dep_time = chopper.next();
			
			chopper.next();
			
			String dest_iata = chopper.next();
			String dest_icao = chopper.next();
			
                        String dest_descrip = chopper.next();
                        
                        if(dest_descrip.length() > 4)
                        {
                            dest_descrip = dest_descrip.substring(4);
                        }
                        
			if(dest_icao.length() > 0)		
                        {
                            dest_descrip += ","+chopper.next();
                        }
			chopper.next();
			
			String arr_time = chopper.next();
			
			chopper.next();
			
			String aircraft = chopper.next();
			
			char cat = chopper.next().charAt(0);
			
			chopper.next();
			
			int duration = chopper.nextInt();
			
			chopper.next();
			chopper.next();
			chopper.next();
			chopper.next();
			
			String carrier = chopper.next();
			
			if(aircraft.equals("B77"))
			{
				aircraft = "B777";
			}
			else if(aircraft.equals("340"))
			{
				aircraft = "A340";
			}			
			
			if(!Character.isLetter(cat))
			{
				System.out.println(id);
				System.out.println(num+" "+origin_iata+" "+origin_icao+" "+dep_time+" "+dest_iata+" "+dest_icao+" "+arr_time+" "+aircraft+" "+cat+" "+duration+" "+carrier);
				System.exit(1);
			}
			
			routes.add(new Flight(num, origin_iata, origin_icao, origin_descrip, dest_iata, dest_icao, dest_descrip, aircraft, dep_time, arr_time, duration, cat, carrier));
		}
                
                SecretKey aesKey = null;
                IvParameterSpec ivParameterSpec = null;
                Cipher encryptCipher = null;
		
                KeyGenerator kgen = KeyGenerator.getInstance("AES");
                kgen.init(128);
                aesKey = kgen.generateKey();
                ivParameterSpec = new IvParameterSpec(aesKey.getEncoded());
                encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                encryptCipher.init(Cipher.ENCRYPT_MODE, aesKey, ivParameterSpec);
                
		ObjectOutputStream out = new ObjectOutputStream(new CipherOutputStream(new FileOutputStream(new File("schedule.dat")), encryptCipher));
		
		out.writeObject(routes);
		
		out.close();
                
                out = new ObjectOutputStream(new FileOutputStream(new File("schedule_key.dat")));
                out.writeObject(new Integer((int)(Math.random()*Integer.MAX_VALUE)));
                out.writeObject(new ScheduleKey(aesKey));
                out.close();
                
	}
}