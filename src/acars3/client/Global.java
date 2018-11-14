package acars3.client;

import java.awt.*;

public final class Global
{
    public static final int POS_UPDATE_INTERVAL = 5*60; // 5 minutes
    
	public static final Font BLACKBOX_FONT = new Font("Arial", 0, 12);
	
	public static final Font COMPANY_FONT = new Font("Arial", Font.BOLD, 15);
	public static final Color COMPANY_COLOR = Color.black;
	
	public static final Color BACKGROUND = Color.white;
	public static final Color TEXT_UNEDITABLE = new Color(245, 245, 245);
	public static final Color SELECTED = new Color(230, 230, 230);
	
	public static final Font CHAT_FONT = new Font("Arial", 0, 12);
	
	public static final Font LIST_FONT = new Font("Arial", 0, 12);
	public static final Font DISPATCH_FONT = new Font("Arial", 0, 12);
	
	public static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 16);
        
        
        
        public static final int STYLE_CHAT = 0;
        public static final int STYLE_CONNECT = 1;
        public static final int STYLE_TAKEOFF = 2;
        public static final int STYLE_ERROR = 3;
        public static final int STYLE_PM = 4;
        public static final int STYLE_ANNOUNCE = 5;
        
        public static final Color[] STYLE_COLORS = new Color[]{Color.black, Color.blue, new Color(34, 177, 76), Color.red, new Color(230, 91, 0), Color.red};
        
        public static final char convertRank(String rank)
        {
            if(rank.equals("Probationary First Officer"))
            {
                return 'A';
            }
            else if(rank.equals("First Officer"))
            {
                return 'B';
            }
            else if(rank.equals("Captain"))
            {
                return 'C';
            }
            else if(rank.equals("Senior Captain"))
            {
                return 'D';
            }
            else if(rank.equals("Executive Captain"))
            {
                return 'E';
            }
            else
            {
                return ' ';
            }
        }
        
        public static final String convertRank(char rank)
        {
            switch(rank)
            {
                case 'A': return "Probationary First Officer";
                case 'B': return "First Officer";
                case 'C': return "Captain";
                case 'D': return "Senior Captain";
                case 'E': return "Executive Captain";
                default: return "";
            }
        }
        
        public static final String convertRankAbbrev(char rank)
        {
            switch(rank)
            {
                case 'A': return "PFO";
                case 'B': return "FO";
                case 'C': return "Capt";
                case 'D': return "Snr Capt";
                case 'E': return "Exec Capt";
                default: return "";
            }
        }
        
        public static final int convertTime(String localtime)
        {
            int time = 60 * Integer.parseInt(localtime.substring(0, localtime.indexOf(':')));
            localtime = localtime.substring(localtime.indexOf(':')+1);
            time += Integer.parseInt(localtime.substring(localtime.indexOf(' ')));
            localtime = localtime.substring(localtime.indexOf(' ')+1);

            if(localtime.equals("PM"))
            {
                time += 12*60;
            }
            
            return time;
        }
        
        public static final String dispLocalTime(int time)
        {
            String output = "" + ( ( (time/60) == 12)? "12" : ((time / 60)%12));
            output += ":"+(time%60);
            output += (time >= 12*60? " PM" : " AM");
            return output;
        }

        public static final String dispZuluTime(int time)
        {
            return ""+ (time/60)+""+(time%60)+"z";
        }
        
        public static final String dispDuration(int mins)
        {
            return ""+(mins/60)+":"+ (mins%60 < 10? "0" : "")+(mins%60);
        }
}