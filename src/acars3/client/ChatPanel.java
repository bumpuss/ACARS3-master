package acars3.client;

import java.io.*;
import java.util.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.text.*;
import java.awt.*;
import static graphicutils.GraphicUtils.*;
import acars3.net.PrivateMessage;
import acars3.net.ChatMessage;

public class ChatPanel extends JPanel
{
	private JTextPane chatlog;
	private JTextField message;
	private JButton transmit;
	
	private GUI gui;
        
        private JComboBox recipient;
        
        private SimpleAttributeSet[] styles;
        
        private int pm_size;

	
	public ChatPanel(GUI gui)
	{
                this.gui = gui;
                
                styles = new SimpleAttributeSet[Global.STYLE_COLORS.length];
                
                for(int i = 0; i < styles.length; i++)
                {
                    styles[i] = new SimpleAttributeSet();
                    StyleConstants.setForeground(styles[i], Global.STYLE_COLORS[i]);
                }
                
		//chatlog = new JTextPane(16, 43);
		//chatlog.setLineWrap(true);
		//chatlog.setWrapStyleWord(true);
                chatlog = new JTextPane()
                {
                    public Dimension getPreferredScrollableViewportSize()
                    {
                        return new Dimension(595, 297);
                    }
                    
                    public boolean getScrollableTracksViewportWidth()
                    {
                        return true;
                    }
                };
   
                StyledDocument doc = chatlog.getStyledDocument();
                
                chatlog.setEditable(false);
		chatlog.setFont(Global.CHAT_FONT);
		chatlog.setBackground(Global.TEXT_UNEDITABLE);
		
		message = new JTextField(37);
		message.setFont(Global.CHAT_FONT);
		
		transmit = new JButton("Transmit");
		
		ActionListener al = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
                                
				String msg = message.getText().trim();
                                
                                
				
				if(msg.length() == 0)
				{
					message.setText("");
				}
				else if(send((String)recipient.getSelectedItem(), msg))
				{
					message.setText("");
				}
				else
				{
					receive("<System> Could not transmit.", Global.STYLE_ERROR);
				}
			}
		};
		
		transmit.addActionListener(al);
		message.addActionListener(al);
                
                pm_size = 1;
                recipient = new JComboBox(new Vector<String>());
                recipient.addItem("Send to all");
                recipient.addItem("Staff");
		
		setLayout(new GridBagLayout());
		constrain(this, new JScrollPane(chatlog, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER), 0, 0, 3, 1);
                constrain(this, recipient, 0, 1, 1, 1, 3, 3, 3, 3);
		constrain(this, message, 1, 1, 1, 1, GridBagConstraints.CENTER, 3, 5, 3, 5);
		constrain(this, transmit, 2, 1, 1, 1, 3, 5, 3, 3);
		
		setBackground(Global.BACKGROUND);
	}
        
        public boolean send(String recipient, String msg)
        {
            if(recipient.equals("Send to all"))
            {
                return gui.getClient().send(new ChatMessage(msg));
            }
            else
            {
                return gui.getClient().send(new PrivateMessage(recipient, msg));
            }
        }
        
        public int addRecipient(String x)
        {
 
            for(int i = 0; i < pm_size; i++)
            {
                if(recipient.getItemAt(i).equals(x))
                {
                    return i;
                }
            }
            
            recipient.addItem(x);
            pm_size++;
            return pm_size-1;
            
            
           
        }
        
        public void pm(String x)
        {
            recipient.setSelectedIndex(addRecipient(x));
            message.requestFocus();
        }
        
        public void pm_received(String x)
        {
            int idx = addRecipient(x);
            
            if(message.getText().equals(""))
            {
                recipient.setSelectedIndex(idx);
                message.requestFocus();
            }
        }
        
        public void removeRecipient(String x)
        {
            int idx = -1;
            for(int i = 0; i < pm_size; i++)
            {
                if(recipient.getItemAt(i).equals(x))
                {
                    idx = i;
                    break;
                }
            }
            
            if(idx > 0)
            {
                if(recipient.getSelectedIndex() == idx)
                {
                     recipient.setSelectedIndex(0);
                }
                recipient.removeItem(x);
                pm_size--;
            }
            
            
            
        }
	
	public void setOfflineMode(boolean offline)
	{
		transmit.setEnabled(!offline);
		message.setEditable(!offline);
		recipient.setEnabled(!offline);
	}
	
	public void receive(String msg, int style)
	{
		synchronized(chatlog)
		{
                    try
                    {
                        chatlog.getDocument().insertString(chatlog.getDocument().getLength(), msg+"\n", styles[style]);
                    }
                    catch(Exception ex)
                    {
                        ex.printStackTrace(System.err);
                        System.exit(1);
                    }
		}
	}
	
	public void writeChatLog(File output) 
	{
		try
		{
		
			PrintWriter fileout = new PrintWriter(output);
			
			synchronized(chatlog)
			{
				fileout.println(chatlog.getText());
			}
			fileout.close();
		}
		catch(IOException ex){}
		
	}
	
	
}