package acars3.client;

import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import static graphicutils.GraphicUtils.*;
import javax.swing.text.Highlighter;
import acars3.net.*;

public class DispatchPanel extends JPanel
{
	private JTextField flightnum, origin, dest, aircraft;
        private JComboBox alt;
	private JTextArea route, comments;
	private JCheckBox vatsim, event;
	private JButton submit, refresh;
	
	private int mode;
	private Border defaultBorder, emptyBorder;
	private Highlighter defaultHighlighter;
	
	private boolean offlineMode;
	
	private PIREPManager pireplist;
	private ACARS fdr;
	private GUI gui;
        
        
        
        private Flight flight;
	
	public DispatchPanel(ACARS fdr, PIREPManager pireplist, GUI gui)
	{
		this.fdr = fdr;
		this.pireplist = pireplist;
		this.gui = gui;
		
		mode = 0;
		
		route = new JTextArea(3, 46);
		route.setFont(Global.DISPATCH_FONT);
		route.setBackground(Global.BACKGROUND);
		
		comments = new JTextArea(3, 46);
		comments.setFont(Global.DISPATCH_FONT);
		comments.setBackground(Global.BACKGROUND);
		
		flightnum = new JTextField(15);
		flightnum.setEditable(false);
		flightnum.setBackground(Global.BACKGROUND);
		flightnum.setFont(Global.DISPATCH_FONT);
		
		defaultBorder = flightnum.getBorder();
		defaultHighlighter = flightnum.getHighlighter();
		
		flightnum.setBorder(BorderFactory.createEmptyBorder());
		flightnum.setHighlighter(null);
		
		
		origin = new JTextField(30);
		origin.setEditable(false);
		origin.setBackground(Global.BACKGROUND);
		emptyBorder = BorderFactory.createEmptyBorder();
		origin.setBorder(emptyBorder);
		origin.setHighlighter(null);
		origin.setFont(Global.DISPATCH_FONT);
		
		dest = new JTextField(46);
		dest.setEditable(false);
		dest.setBackground(Global.BACKGROUND);
		dest.setBorder(emptyBorder);
		dest.setHighlighter(null);
		dest.setFont(Global.DISPATCH_FONT);
	
		
		alt = new JComboBox(gui.getSchedulePanel().getAirportList());
		alt.setBackground(Global.BACKGROUND);
		alt.setFont(Global.DISPATCH_FONT);
		
		aircraft = new JTextField(46);
		aircraft.setEditable(false);
		aircraft.setBackground(Global.BACKGROUND);
		aircraft.setBorder(emptyBorder);
		aircraft.setHighlighter(null);
		aircraft.setFont(Global.DISPATCH_FONT);
		
		refresh = new JButton("Refresh");
		
		refresh.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				refresh();
			}
		});
		
		
		vatsim = new JCheckBox("Will fly on VATSIM");
		vatsim.setBackground(Global.BACKGROUND);
		
		event = new JCheckBox("This is an Event Flight");
		event.setBackground(Global.BACKGROUND);
		
		
		

		submit = new JButton("Preflight");
		
		submit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				submit();
			}
		});
		
		setLayout(new GridBagLayout());
		constrain(this, new JLabel("Flight Number"), 0, 0, 1, 1, 3, 5, 3, 20);
		constrain(this, flightnum, 1, 0, 3, 1, 3, 5, 3, 0);
		constrain(this, refresh, 3, 0, 1, 2, GridBagConstraints.EAST, 3, 5, 3, 5);
		
		constrain(this, new JLabel("Departing"), 0, 1, 1, 1, 3, 5, 3, 0);
		constrain(this, origin, 1, 1, 3, 1, 3, 5, 3, 0);
		
		constrain(this, new JLabel("Arriving"), 0, 2, 1, 1, 3, 5, 3, 0);
		constrain(this, dest, 1, 2, 3, 1, 3, 5, 3, 0);
		
		constrain(this, new JLabel("Alternate"), 0, 3, 1, 1, 3, 5, 3, 0);
		constrain(this, alt, 1, 3, 3, 1, 3, 5, 3, 0);
		
		constrain(this, new JLabel("Aircraft"), 0, 4, 1, 1, 3, 5, 3, 5);
		constrain(this, aircraft, 1, 4, 3, 1, 3, 5, 3, 0);
		
		constrain(this, new JLabel("Route"), 0, 5, 1, 1, 3, 5, 3, 0);
		constrain(this, new JScrollPane(route, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER), 1, 5, 3, 1, 3, 5, 3, 5);
		
		constrain(this, new JLabel("Comments"), 0, 6, 1, 1, 0, 5, 0, 0);
		constrain(this, new JScrollPane(comments, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER), 1, 6, 3, 1, 3, 5, 3, 5);
		
		constrain(this, vatsim, 0, 7, 2, 1, 3, 5, 3, 0);
		constrain(this, event, 2, 7, 1, 1, 3, 5, 3, 0);
		constrain(this, submit, 3, 7, 1, 1, GridBagConstraints.EAST, 3, 5, 3, 5);
		
		setBackground(Global.BACKGROUND);

		loadFlight(null);

                refresh();
	}
	
	public int getMode()
	{
		return mode;
	}
        
        public Flight getFlight()
        {
            return flight;
        }
	
	public boolean loadFlight(Flight f)
	{
		if(mode == 0)
		{
                    
                    if(f != null && (f.getOriginICAO().equals(gui.getPilot().getCurrentLocation()) || 
                            JOptionPane.showConfirmDialog(gui, "Your booked flight may not depart from your last location.\nIf you fly this flight, you may be charged a jumpseat fee.\nDo you wish to continue?", "Possible jumpseat fee", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION
                            ))
                    {
                        flight = f;
                        
                        alt.setBackground(Global.BACKGROUND);
                        alt.setEnabled(true);
                        route.setBackground(Global.BACKGROUND);
                        route.setEditable(true);
                        comments.setBackground(Global.BACKGROUND);
                        comments.setEditable(true);
                        event.setEnabled(true);
                        vatsim.setEnabled(true);
                        submit.setEnabled(true);
                        
			origin.setText(f.getOriginICAO()+" @ "+f.getDepTime()+(f.getDepTimeZulu() != null && f.getDepTimeZulu().length() > 0? " / "+f.getDepTimeZulu() : ""));
			dest.setText(f.getDestICAO()+" @ "+f.getArrTime()+(f.getArrTimeZulu() != null && f.getArrTimeZulu().length() > 0? " / "+f.getArrTimeZulu() : "")+" (Estimated time en-route: "+toHours(f.getDuration())+")");
			flightnum.setText(f.getCarrier()+" "+f.getNumber());
			aircraft.setText(f.getAircraft());
                        
                        // update status
                        gui.getClient().send(new StatusMessage(flight.getOriginICAO(), flight.getDestICAO(), StatusMessage.NOT_FLYING));
                        
                        // request airport position if not available
                        if(flight.getDestLatitude() == 0 && flight.getDestLongitude() == 0)
                        {
                            gui.getClient().send(new RequestPositionMessage(flight.getDestICAO()));
                        }
                        if(flight.getOriginLatitude() == 0 && flight.getOriginLongitude() == 0)
                        {
                            gui.getClient().send(new RequestPositionMessage(flight.getOriginICAO()));
                        }
                        
                        return true;
                    }
                    else
                    {
                        flight = null;
                        
                        origin.setText("");
                        dest.setText("");
                        flightnum.setText("");
                        aircraft.setText("");
                        
                        alt.setBackground(Global.TEXT_UNEDITABLE);
                        alt.setEnabled(false);
                        route.setBackground(Global.TEXT_UNEDITABLE);
                        route.setEditable(false);
                        comments.setBackground(Global.TEXT_UNEDITABLE);
                        comments.setEditable(false);
                        event.setEnabled(false);
                        vatsim.setEnabled(false);
                        submit.setEnabled(false);
                        
                        
                    }
		}
                return false;
	}
	
	public void setOfflineMode(boolean m)
	{
		offlineMode = m;
		
		refresh.setEnabled(mode == 0 && !offlineMode);
		
		
	}
	
	public String toHours(int mins)
	{
		String temp = ""+(mins/60);
		int min = mins % 60;
		
		if(min < 10)
		{
			temp += ":0"+min;
		}
		else
		{
			temp += ":"+min;
		}
		return temp;
	}
        
        
	
	public void refresh()
	{
            gui.getClient().send(new RequestBookedMessage());
	}
	
	public void submit()
	{
		switch(mode)
		{
			case 0: 
				preflight();
				break;
			case 1:
				beginFlight();
				break;
			case 2:
				endFlight();
				break;
		}
	}
	
        public String getOrigin()
        {
            if(flight != null)
            {
                return flight.getOriginICAO();
            }
            else
            {
                return null;
            }
        }
        
        public String getDest()
        {
            if(flight != null)
            {
                return flight.getDestICAO();
            }
            else
            {
                return null;
            }
        }
        
	public void preflight()
	{
		if(route.getText().trim().length() == 0)
		{
			JOptionPane.showMessageDialog(this, "Please provide the route you are going to fly in the Route box and try again.", "Cannot Start Flight", JOptionPane. ERROR_MESSAGE);			
			return;
		}
		
		if(!fdr.isValid())
		{
			fdr.setActivated(true);
		}
		
		if(!fdr.isValid())
		{
			JOptionPane.showMessageDialog(this, "Could not connect to Flight Sim.", "Cannot Start Flight", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if(!fdr.isParkingBrake() || !fdr.isOnGround())
		{
			JOptionPane.showMessageDialog(this, "Your aircraft must be at the gate with your parking brake on before starting a flight.", "Cannot Start Flight", JOptionPane. ERROR_MESSAGE);	
			return;	
		}
		
		
		mode++;
		gui.getSchedulePanel().setSelectFlightEnabled(false);
                
                gui.getClient().send(new StatusMessage(flight.getOriginICAO(), flight.getDestICAO(), StatusMessage.BRIEFING));
                
                gui.getBlackBoxPanel().repaint();
		
		submit.setText("Begin Flight");
		refresh.setEnabled(false);
		route.setEditable(false);
		alt.setEnabled(false);
		vatsim.setEnabled(false);
		event.setEnabled(false);
		
		route.setBackground(Global.TEXT_UNEDITABLE);
		
		if(offlineMode)
		{
			flightnum.setEditable(false);
			origin.setEditable(false);
			dest.setEditable(false);
			aircraft.setEditable(false);
			

		}
		
		
	}
	
	public void beginFlight()
	{
		if(!fdr.isOnGround())
		{
			JOptionPane.showMessageDialog(this, "Your aircraft must be on the ground before starting a flight.", "Cannot Start Flight", JOptionPane. ERROR_MESSAGE);	
		}
		
		if(JOptionPane.showConfirmDialog(this, "Before clicking OK, check:\n\n"+
			"You have an appropriate amount of fuel\n"+
			"You are parked at the gate\n"+
			"You are ready to pushback", "Ready to start?", JOptionPane.OK_CANCEL_OPTION,
			JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION)
		{
			

                        String alternate = "None provided";
                        String alternatefull = "No alternate";
                        
			try
			{
				alternate = alt.getSelectedItem().toString().trim().substring(0, 4);
                                alternatefull = alt.getSelectedItem().toString();
                                
                                if(alternatefull.length() > 4)
                                {
                                    alternatefull = alternatefull.substring(alternatefull.indexOf('(')+1, alternatefull.indexOf(')'));
                                }
			}
			catch(Exception ex)
			{

			}
			
			if( !fdr.beginFlight(flight, alternate, alternatefull, route.getText().trim(), vatsim.isSelected()) )
			{
				JOptionPane.showMessageDialog(this, "Could not create PIREP file.", "Cannot Start Flight", JOptionPane.ERROR_MESSAGE);
				return;	
			}
		
			mode++;
			gui.getSchedulePanel().setSelectFlightEnabled(false);
                        
                        gui.getClient().send(new StatusMessage(flight.getOriginICAO(), flight.getDestICAO(), StatusMessage.AT_GATE));
			
			submit.setText("End Flight");
		}	
		
	}
	
	public void endFlight()
	{
		if(JOptionPane.showConfirmDialog(this, "Are you sure you wish to end this flight?", 
			"Confirm Flight End", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)
			== JOptionPane.NO_OPTION)
		{
			return;
		}

		LogbookEntry report = fdr.endFlight(comments.getText().trim(), event.isSelected());
		
		if(JOptionPane.showConfirmDialog(this, report.getOrigin()+" to "+report.getDest()+"\nDeparted "+report.getBeginTime()+"   Arrived "+report.getEndTime()+
			"   Duration "+report.getFormattedDuration()+"\nLanding weight: "+(int)report.getWeight()+" lb\nFuel loaded: "+report.getFuelLoad()+" lb   Used: "+report.getFuelUsed()+" lb\n"+
			"Landing rate: "+report.getLandingRate()+" ft/min\n\n"+
			"Do you wish to file a PIREP?", 
			"File PIREP?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)
			== JOptionPane.YES_OPTION)
		{
                        comments.setBackground(Global.TEXT_UNEDITABLE);
                        comments.setEditable(false);
                        submit.setEnabled(true);
                        alt.setEnabled(false);
                        vatsim.setEnabled(false);
                        event.setEnabled(false);
                        
			if(!pireplist.submit(fdr.getPIREP()))
                        {
                            pirep_submitted();
                        }
		}		
		else
		{
			fdr.getPIREP().delete();
                        pirep_submitted();
		}
		
        }
        public void pirep_submitted()
        {
                gui.getPilot().setCurrentLoc(fdr.getPIREP().getDest());
                gui.getCompanyPanel().repaint();
                
		flightnum.setText("");
		origin.setText("");
		dest.setText("");
		alt.setSelectedIndex(0);
		aircraft.setText("");
		route.setText("");
		comments.setText("");
		vatsim.setSelected(false);
		event.setSelected(false);
	
		
		
		mode = 0;
		gui.getSchedulePanel().setSelectFlightEnabled(true);
		fdr.setActivated(false);
		
		submit.setText("Preflight");
		
		if(!offlineMode)
		{
			refresh.setEnabled(true);
			refresh();
		}
	}

	public boolean isInFlight()
	{
		return mode != 0;
	}
	
}