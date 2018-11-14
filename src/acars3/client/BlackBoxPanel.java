package acars3.client;

import java.awt.*;
import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import static graphicutils.GraphicUtils.*;


public class BlackBoxPanel extends JPanel
{
	private BlackBox fdr;
	
	private JTextField aircraft, hdg, kias, distance, altitude, vs, gs, fuel;
	private EnginePanel eng1Pane, eng2Pane;
	
	private boolean validSwitch;
        
        private double dest_lat, dest_long, origin_lat, origin_long;
        
        private GUI gui;
	
	public BlackBoxPanel(BlackBox fdr, GUI gui)
	{	
                this.gui = gui;
                
		validSwitch = false;
		
		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new GridBagLayout());
		innerPanel.setBackground(Global.BACKGROUND);
		
		aircraft = new JTextField(21);
		aircraft.setEditable(false);
		aircraft.setBackground(Global.BACKGROUND);
		aircraft.setBorder(BorderFactory.createEmptyBorder());
		aircraft.setFont(Global.BLACKBOX_FONT);
		aircraft.setHighlighter(null);
		
		hdg = new JTextField(4);
		hdg.setEditable(false);
		hdg.setBackground(Global.BACKGROUND);
		hdg.setBorder(BorderFactory.createEmptyBorder());
		hdg.setFont(Global.BLACKBOX_FONT);
		hdg.setHighlighter(null);
		
		kias = new JTextField(4);
		kias.setEditable(false);
		kias.setBackground(Global.BACKGROUND);
		kias.setBorder(BorderFactory.createEmptyBorder());
		kias.setFont(Global.BLACKBOX_FONT);
		kias.setHighlighter(null);
		
		distance = new JTextField(10);
		distance.setEditable(false);
		distance.setBackground(Global.BACKGROUND);
		distance.setBorder(BorderFactory.createEmptyBorder());
		distance.setFont(Global.BLACKBOX_FONT);
		distance.setHighlighter(null);
		
		altitude = new JTextField(8);
		altitude.setEditable(false);
		altitude.setBackground(Global.BACKGROUND);
		altitude.setBorder(BorderFactory.createEmptyBorder());
		altitude.setFont(Global.BLACKBOX_FONT);
		altitude.setHighlighter(null);
		
		vs = new JTextField(8);
		vs.setEditable(false);
		vs.setBackground(Global.BACKGROUND);
		vs.setBorder(BorderFactory.createEmptyBorder());
		vs.setFont(Global.BLACKBOX_FONT);
		vs.setHighlighter(null);
		
		gs = new JTextField(8);
		gs.setEditable(false);
		gs.setBackground(Global.BACKGROUND);
		gs.setBorder(BorderFactory.createEmptyBorder());
		gs.setFont(Global.BLACKBOX_FONT);
		gs.setHighlighter(null);
		
		fuel = new JTextField(8);
		fuel.setEditable(false);
		fuel.setBackground(Global.BACKGROUND);
		fuel.setBorder(BorderFactory.createEmptyBorder());
		fuel.setFont(Global.BLACKBOX_FONT);
		fuel.setHighlighter(null);
		
		JLabel label = new JLabel("Aircraft: ");
		label.setFont(Global.BLACKBOX_FONT);
		constrain(innerPanel, label, 0, 0, 1, 1, 0, 5, 0, 0);
		constrain(innerPanel, aircraft, 1, 0, 2, 1, 0, 6, 0, 0);
		
		label = new JLabel("HDG: ");
		label.setFont(Global.BLACKBOX_FONT);
		constrain(innerPanel, label, 0, 1, 1, 1, 0, 5, 0, 0);
		constrain(innerPanel, hdg, 1, 1, 1, 1, 0, 0, 0, 0);
		
		label = new JLabel("KIAS: ");
		label.setFont(Global.BLACKBOX_FONT);
		constrain(innerPanel, label, 0, 2, 1, 1, 0, 5, 0, 0);
		constrain(innerPanel, kias, 1, 2, 1, 1, 0, 0, 0, 0);
		
		label = new JLabel("Distance (to go): ");
		label.setFont(Global.BLACKBOX_FONT);
		constrain(innerPanel, label, 0, 3, 2, 1, 0, 5, 0, 0);
		constrain(innerPanel, distance, 2, 3, 1, 1, 0, 0, 0, 0);
		
		label = new JLabel("Altitude: ");
		label.setFont(Global.BLACKBOX_FONT);
		constrain(innerPanel, label, 3, 0, 1, 1, 0, 0, 0, 0);
		constrain(innerPanel, altitude, 4, 0, 1, 1, 0, 0, 0, 5);
		
		label = new JLabel("VS: ");
		label.setFont(Global.BLACKBOX_FONT);
		constrain(innerPanel, label, 3, 1, 1, 1, 0, 0, 0, 0);
		constrain(innerPanel, vs, 4, 1, 1, 1, 0, 0, 0, 5);
		
		label = new JLabel("GS: ");
		label.setFont(Global.BLACKBOX_FONT);
		constrain(innerPanel, label, 3, 2, 1, 1, 0, 0, 0, 0);
		constrain(innerPanel, gs, 4, 2, 1, 1, 0, 0, 0, 5);
		
		label = new JLabel("Fuel: ");
		label.setFont(Global.BLACKBOX_FONT);
		constrain(innerPanel, label, 3, 3, 1, 1, 0, 0, 0, 0);
		constrain(innerPanel, fuel, 4, 3, 1, 1, 0, 0, 0, 5);
			
			
			
			
		setLayout(new GridBagLayout());
		constrain(this, innerPanel, 0, 0, 2, 1, 0, 0, 0, 0);
		
		eng1Pane = new EnginePanel(fdr.getEng1());
		eng1Pane.setBorder(BorderFactory.createTitledBorder("Engine 1"));
		
		eng2Pane = new EnginePanel(fdr.getEng2());
		eng2Pane.setBorder(BorderFactory.createTitledBorder("Engine 2"));
	
		
		constrain(this, eng1Pane, 0, 1, 1, 1, GridBagConstraints.CENTER, 2, 5, 1, 5);
		constrain(this, eng2Pane, 1, 1, 1, 1, GridBagConstraints.CENTER, 2, 5, 1, 5);
		
		this.fdr = fdr;
		
		
		setBackground(Global.BACKGROUND);
		
		Timer timer = new Timer(10*1000, new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				repaint();
			}
		});
		timer.start();
	}
        
        
        
        
        
        
	
	public void setActivated(boolean active)
	{
		if(active)
		{
			aircraft.setText(fdr.getAircraftTitle());
		}
		else
		{
			aircraft.setText("");
		}
	}
	public void repaint()
	{
		if(fdr != null)
		{
			if(fdr.isValid() != validSwitch)
			{
				validSwitch = !validSwitch;
				
				if(validSwitch)
				{
					aircraft.setText(fdr.getAircraftTitle());
					aircraft.setCaretPosition(0);
				}
				else
				{
					aircraft.setText("");
					hdg.setText("");
					kias.setText("");
					altitude.setText("");
					distance.setText("");
					gs.setText("");
					vs.setText("");
					fuel.setText("");
				}
					
			}
				
			if(fdr.isValid())
			{	
				hdg.setText(""+(int)(fdr.getHeading()));
				kias.setText(""+(int)Math.round(fdr.getKIAS()));
				altitude.setText(""+(int)Math.round(fdr.getAltitude())+" ft");
                                
                                Flight flight = gui.getDispatchPanel().getFlight();
                                
                                double dest_lat = flight.getDestLatitude();
                                double dest_long = flight.getDestLongitude();
                                double origin_lat = flight.getOriginLatitude();
                                double origin_long = flight.getOriginLongitude();
                                
                                if((dest_lat != 0 || dest_long != 0) && (origin_lat != 0 || origin_long != 0))
                                {
                                    
                                    
                                    double curr_lat = fdr.getLatitude();
                                    double curr_long = fdr.getLongitude();
                                    
                                    double dist1 = fdr.getDistance(dest_lat, dest_long, curr_lat, curr_long);
                                    double dist2 = fdr.getDistance(dest_lat, dest_long, origin_lat, origin_long);
 
                                    distance.setText((int)Math.round(dist1)+" nm ("+(int)(100*(1-dist1/dist2))+" %)");
                                }
                                else
                                {
                                    distance.setText("unavailable");
                                }
                                
				gs.setText(""+(int)Math.round(fdr.getGS())+" kt");
				vs.setText(""+(int)Math.round(fdr.getVS())+" ft/min");
				fuel.setText(""+(int)(fdr.getFuel())+" lb");
                                
                                
                                
                                
			}
		}

		
		super.repaint();
		
		if(eng2Pane != null)
		{
			eng1Pane.repaint();
			eng2Pane.repaint();
		}
		

	}
	
}