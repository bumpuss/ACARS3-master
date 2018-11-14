package acars3.client;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import static graphicutils.GraphicUtils.*;

public class WeatherPanel extends JPanel
{
	public static void main(String[] args)
	{
		System.out.println(getMETAR("kdca"));
	}
	private JTextArea weather;
	private JTextField icao;
	
	public WeatherPanel()
	{
		weather = new JTextArea(8, 80);
		weather.setBorder(BorderFactory.createEmptyBorder());
		weather.setBackground(Global.BACKGROUND);
		weather.setEditable(false);
			
		icao = new JTextField(4);
		icao.setBackground(Global.BACKGROUND);
		
		final JButton getMetar = new JButton("Get weather report");
		
		ActionListener action = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				getMetar.setEnabled(false);
				
				weather.setText(getMETAR(icao.getText().trim())+"\n\n"+getTAF(icao.getText().trim()));
				icao.setText("");
				getMetar.setEnabled(true);
			}
		};
		
		icao.addActionListener(action);
		getMetar.addActionListener(action);
		
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBackground(Global.BACKGROUND);
		
		
		constrain(panel, new JLabel("ICAO: "), 0, 0, 1, 1);
		constrain(panel, icao, 1, 0, 1, 1);
		constrain(panel, getMetar, 2, 0, 1, 1);
		constrain(panel, weather, 0, 1, 4, 1);
		
		
		add(panel);
		
		
		setBackground(Global.BACKGROUND);
	}
	
	public static String getTAF(String icao)
	{
		if(icao.length() != 4)
		{
			return "";
		}
		
		icao = icao.toUpperCase();
		
		String output = "";
		
		String line = "";
		
		try 
		{
		    URL url = new URL("http://aviationweather.gov/adds/tafs/?station_ids="+icao.toLowerCase()+"&std_trans=standard&submit_taf=Get+TAFs");
		    DataInputStream dis = new DataInputStream(new BufferedInputStream(url.openStream()));
		
		    while ((line = dis.readLine()).indexOf(icao) < 0); 
		    
		    line = line.substring(line.indexOf(icao));
		    
		    output += line;
		    
		    while ((line = dis.readLine()).indexOf('<') < 0)
		    {
		    	output += "\n"+line;
		    }
		    
		    dis.close();
	
		} 
		catch (Exception ioe) 
		{
		     //ioe.printStackTrace();
		}
		
		return output;
	}
	
	public static String getMETAR(String icao)
	{
		if(icao.length() != 4)
		{
			return "";
		}
		
		icao = icao.toUpperCase();
		
		String line = "";
		
		try 
		{
		    URL url = new URL("http://aviationweather.gov/adds/metars/?station_ids="+icao.toLowerCase()+"&std_trans=standard&chk_metars=on&hoursStr=most+recent+only&submitmet=Submit");
		    DataInputStream dis = new DataInputStream(new BufferedInputStream(url.openStream()));
		
		    while ((line = dis.readLine()).indexOf(icao) < 0); 
		    
		    line = line.substring(line.indexOf(icao));
		    line = line.substring(0, line.indexOf('<'));
		    
		    dis.close();
	
		} 
		catch (Exception ioe) 
		{
		     //ioe.printStackTrace();
		}
		
		return line;
	}
	
	
}