package acars3.client;

import java.awt.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import static graphicutils.GraphicUtils.*;

public class CompanyPanel extends JPanel
{
	private String name, hub, currentLoc, money, hours;
		
	public CompanyPanel(Pilot pilot)
	{
		setPilot(pilot);

		setLayout(new GridBagLayout());	
		setBackground(Global.BACKGROUND);
		
		constrain(this, new LogoPanel(), 0, 0, 1, 1, GridBagConstraints.EAST, 0, 0, 0, 0);
		constrain(this, innerPane(), 0, 1, 1, 1, 0, 0, 0, 0);
		
	}

	
	public void setPilot(Pilot p)
	{
            if(p != null && p.getName().length() > 0)
            {
                this.name = p.getRank()+" "+p.getName();
                
                if(p.getHub().equals("KTNG"))
                {
                    this.hub = "Hub: Training";  
                }
                else
                {
                    this.hub = "Hub: "+p.getHub();
                }
		
                if(!p.getCurrentLocation().equals("null"))
                {
                    this.currentLoc = "Currently at "+p.getCurrentLocation();
                }
                else if(p.getHub().equals("KTNG"))
                {
                    this.currentLoc = "No current location";
                }
                else
                {
                    this.currentLoc = "Currently at "+p.getHub();
                }
		
		this.money = "Money: $"+String.format("%.2f", p.getMoney());
		this.hours = "Career Hours: "+String.format("%.1f", p.getHours());
            }
            else
            {
                name = "";
                hub = "";
                currentLoc = "";
                money = "";
                hours = "";
            }
		
	}
	private JComponent innerPane()
	{
		
		JComponent innerPane = new JComponent()
		{
			public void paint(Graphics g)
			{
				
				g.setColor(Global.COMPANY_COLOR);
				g.setFont(Global.COMPANY_FONT);
				
				FontMetrics metrics = g.getFontMetrics();
				
				
				g.drawString(name, getWidth()-5-metrics.charsWidth(name.toCharArray(), 0, name.length()), 15);
				g.drawString(hub, getWidth()-5-metrics.charsWidth(hub.toCharArray(), 0, hub.length()), 35);
				g.drawString(currentLoc, getWidth()-5-metrics.charsWidth(currentLoc.toCharArray(), 0, currentLoc.length()), 55);
				g.drawString(hours, getWidth()-5-metrics.charsWidth(hours.toCharArray(), 0, hours.length()), 85);
				g.drawString(money, getWidth()-5-metrics.charsWidth(money.toCharArray(), 0, money.length()), 105);
			}
		};
		
		innerPane.setPreferredSize(new Dimension(430, 114));
		innerPane.setBackground(Global.BACKGROUND);
		
		
		return innerPane;
	}
	
	
}