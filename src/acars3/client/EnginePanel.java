package acars3.client;

import java.awt.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import static graphicutils.GraphicUtils.*;
import acars3.client.BlackBox.EngineData;

public class EnginePanel extends JPanel
{
	private JTextField n1, n2, flow;
	private EngineData engdata;
	
	public EnginePanel(EngineData engdata)
	{
		setLayout(new GridBagLayout());
		
		n1 = new JTextField(4);
		n1.setEditable(false);
		n1.setBackground(Global.BACKGROUND);
		n1.setBorder(BorderFactory.createEmptyBorder());
		n1.setFont(Global.BLACKBOX_FONT);
		n1.setHighlighter(null);
		
		n2 = new JTextField(4);
		n2.setEditable(false);
		n2.setBackground(Global.BACKGROUND);
		n2.setBorder(BorderFactory.createEmptyBorder());
		n2.setFont(Global.BLACKBOX_FONT);
		n2.setHighlighter(null);
		
		flow = new JTextField(7);
		flow.setEditable(false);
		flow.setBackground(Global.BACKGROUND);
		flow.setBorder(BorderFactory.createEmptyBorder());
		flow.setFont(Global.BLACKBOX_FONT);
		flow.setHighlighter(null);
		
		JLabel temp = new JLabel("N1: ");
		temp.setFont(Global.BLACKBOX_FONT);
		constrain(this, temp, 0, 0, 1, 1, 0, 0, 0, 0);
		constrain(this, n1, 1, 0, 1, 1, 0, 0, 0, 0);
		
		temp = new JLabel("N2: ");
		temp.setFont(Global.BLACKBOX_FONT);
		constrain(this, temp, 0, 1, 1, 1, 0, 0, 0, 0);
		constrain(this, n2, 1, 1, 1, 1, 0, 0, 0, 0);
		
		temp = new JLabel("Flow: ");
		temp.setFont(Global.BLACKBOX_FONT);
		constrain(this, temp, 0, 2, 1, 1, 0, 0, 0, 0);
		constrain(this, flow, 1, 2, 1, 1, 0, 0, 0, 0);
		
		this.engdata = engdata;
		
		setBackground(Global.BACKGROUND);
	}
	
	public void repaint()
	{
		if(engdata != null && engdata.isValid())
		{
			if(engdata.isValid())
			{
				n1.setText(""+(int)Math.round(engdata.getN1())+" %");
				n2.setText(""+(int)Math.round(engdata.getN2())+" %");
				flow.setText(""+(int)Math.round(engdata.getFuelFlow())+" lb/hr");
			}
			else
			{
				n1.setText("");
				n2.setText("");
				flow.setText("");
			}
		}

		
		super.repaint();
	}
}