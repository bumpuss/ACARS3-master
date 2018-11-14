package acars3.client;

import java.util.*;

public class OnlineList extends ArrayList<OnlineListRecord>
{
	public OnlineList()
	{
		
	}
        
        public void update(OnlineListRecord record)
        {

            for(OnlineListRecord r : this)
            {
                if(r.equals(record))
                {
                    r.copy(record);
                    return;
                }
            }
            
            add(record);
        }
        
        public void remove(String id, String name)
        {
            int rem_idx = -1;
            
            for(int i = 0; i < size(); i++)
            {
                OnlineListRecord r = get(i);
                if(r.getId().equals(id) && r.getName().equals(name))
                {
                    rem_idx = i;
                    break;
                }
            }
            
            if(rem_idx >= 0)
            {
                remove(rem_idx);
            }
        }
}