package acars3.client;

import java.io.*;
import java.util.*;
import acars3.net.SendPIREPMessage;

public class PIREPManager
{
	private static final File SAVE_FILE = new File("pireps/PIREPs.dat");
	
	private List<PIREP> saved;
	
        private ClientListener client;
        
	public PIREPManager(ClientListener client)
	{
                this.client = client;
                
		saved = new ArrayList<PIREP>();
		load();
                
		
		if(saved.size() > 0)
		{
			int idx = 0;
			
			while(idx < saved.size())
			{
				if(transmit(saved.get(idx)))
				{
					saved.remove(idx);
				}
				else
				{
					idx++;
				}
			}
			
			save();
		}
		
	}
	
	private void load()
	{
		try
		{
			ObjectInputStream filein = new ObjectInputStream(new FileInputStream(SAVE_FILE));
			
			int size = (Integer)filein.readObject();
			
			for(; size > 0; size--)
			{
				saved.add((PIREP)filein.readObject());
			}
			
			filein.close();
		}
		catch(Exception ex)
		{
			
		}
		/*
		try
		{
			Scanner filein = new Scanner(SAVE_FILE);
			
			filein.nextLine();
			
			int size = filein.nextInt();
			
			for(; size > 0; size--)
			{
				saved.add(new PIREP(filein.nextInt(), filein.nextInt(), filein.next(), filein.next(), filein.next(), filein.nextLine().trim()));
			}
			
			filein.close();
		}
		catch(IOException ex)
		{
			
		}
		*/
		
	}
	
	private void save()
	{
		try
		{
			ObjectOutputStream fileout = new ObjectOutputStream(new FileOutputStream(SAVE_FILE));
			
			fileout.writeObject((Integer)saved.size());
			for(PIREP p : saved)
			{
				fileout.writeObject(p);
			}
			fileout.close();
		}
		catch(IOException ex)
		{
			
		}
		/*
		try
		{
			PrintWriter fileout = new PrintWriter(SAVE_FILE);
			fileout.println("WARNING: Do not modify this file.");
			fileout.println(saved.size());
			
			for(PIREP p : saved)
			{
				fileout.println(p.toString());
			}
			
			fileout.close();
		}
		catch(IOException ex)
		{
			
		}
		*/
	}
	
	
	public boolean submit(PIREP rep)
	{
		if(!transmit(rep))
                {
                    save(rep);
                    return false;
                }
                else
                {
                    return true;
                }
                
	}
        
        public void save(PIREP rep)
        {
            rep.save();
            saved.add(rep);
            save();
        }
        
        public void delete(PIREP rep)
        {
            rep.delete();
        }
	
	private boolean transmit(PIREP rep)
	{
		if(!rep.isValid())
		{
			return true;
		}
		
                if(client.send(new SendPIREPMessage(rep)))
                {
                    return client.sendFile(rep.getFile());
                }
                else
                {
                    return false;
                }
	}
}