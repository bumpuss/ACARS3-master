package acars3.client;

import java.awt.image.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.awt.*;

public class LogoPanel extends JComponent
{
	private Image logo;
	
	public LogoPanel()
	{
		try 
		{
		    logo = ImageIO.read(new File("logo.jpg"));
		} 
		catch (IOException e) 
		{
		}
		
		setPreferredSize(new Dimension(logo.getWidth(null), logo.getHeight(null)));
		
	}
	
	public void paint(Graphics g)
	{
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.drawImage(logo, (getWidth()-logo.getWidth(null))/2, (getHeight()-logo.getHeight(null))/2, null);
	}
}