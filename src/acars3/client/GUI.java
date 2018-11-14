package acars3.client;

import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import static graphicutils.GraphicUtils.*;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GUI extends JFrame
{
	private JTextArea chatlog;

	private CompanyPanel companyPane;
	private DispatchPanel dispatchPane;
	private BlackBoxPanel blackboxPane;
	private OnlineListPanel onlineListPane;
	private ChatPanel chatPane;
	
	private WeatherPanel weatherPane;
	private SchedulePanel schedulePane;

	private OnlineList onlineList;
	
	private ACARS fdr;
	
	private JTabbedPane tabbedPane;
	
	private PIREPManager pireplist;
	
        private ClientListener client;
        
        private Pilot pilot;
        
        private JMenuItem eventMode;

	public GUI(boolean offlineMode, ClientListener client, Pilot pilot)
	{
		this.client = client;
                this.pilot = pilot;
                
                schedulePane = new SchedulePanel(this, (pilot == null)? " " : pilot.getRank(), offlineMode);
                
                
		fdr = new ACARS(client);
		
		pireplist = new PIREPManager(client);
		
		
		chatPane = new ChatPanel(this);

                
		companyPane = new CompanyPanel(pilot);
		
		dispatchPane = new DispatchPanel(fdr, pireplist, this);
                schedulePane.setDispatchPanel(dispatchPane);
		
		if(offlineMode)
		{
			dispatchPane.setOfflineMode(true);
		}
		
		blackboxPane = new BlackBoxPanel(fdr, this);
		
		onlineList = new OnlineList();
		//onlineList.add(new OnlineListRecord("USA4644", "Exec Capt", "Michael Levin", true, "", "", "Not Flying"));
		//onlineList.add(new OnlineListRecord("USA1257", "Exec Capt", "Brian Wilks", false, "KCLT", "KTPA", "Cruising"));
		
	
		
		onlineListPane = new OnlineListPanel(onlineList, this);
		
		chatPane.setBorder(BorderFactory.createTitledBorder("Company Chat"));
		companyPane.setBorder(BorderFactory.createTitledBorder("Company ID"));
		dispatchPane.setBorder(BorderFactory.createTitledBorder("Dispatch"));
		blackboxPane.setBorder(BorderFactory.createTitledBorder("Flight data"));
		onlineListPane.setBorder(BorderFactory.createTitledBorder("Online Members"));
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		constrain(panel, dispatchPane, 0, 0, 1, 2, 1, 1, 1, 1);
		constrain(panel, companyPane, 1, 0, 1, 1, 1, 1, 1, 1);
		constrain(panel, blackboxPane, 1, 1, 1, 2, 1, 1, 1, 1);
		constrain(panel, chatPane, 0, 2, 1, 2, 1, 1, 1, 1);
		constrain(panel, onlineListPane, 1, 3, 1, 1, 1, 1, 1, 1);

		panel.setBackground(Global.BACKGROUND);
                
                setBackground(Global.BACKGROUND);
		
		//panel.setPreferredSize(new Dimension(990, 680));
		
		
		tabbedPane = new JTabbedPane();
                tabbedPane.setBackground(Global.BACKGROUND);
		tabbedPane.add("Main", panel);
		
		
		
		weatherPane = new WeatherPanel();
		tabbedPane.add("Weather", weatherPane);
		
		
                if(offlineMode)
                {
                    tabbedPane.add("Schedule", schedulePane);
                }
		else
                {
                    tabbedPane.add("Itinerary", schedulePane);
                }
		
	
		
		add(tabbedPane);
                
                
		
		
		JMenuBar menubar = new JMenuBar();
		
		JMenu m1 = new JMenu("File");
		
		JMenuItem mi3 = new JMenuItem("Save chat log");
		m1.add(mi3);
		
		mi3.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				writeChatLog();
			}
		});
		
		JMenuItem mi1 = new JMenuItem("Exit");
		m1.add(mi1);
		
		mi1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				exit();
			}
		});
		
		menubar.add(m1);

		m1 = new JMenu("Options");
		
		eventMode = new JMenuItem("Event mode");
		m1.add(eventMode);
		
		eventMode.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				schedulePane.setEventMode();
			}
		});
                
                menubar.add(m1);
		
		JMenu m2 = new JMenu("Help");
                
                JMenuItem mi2 = new JMenuItem("Help");
		m2.add(mi2);
		
		mi2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				showHelp();
			}
		});
                
		mi2 = new JMenuItem("About");
		m2.add(mi2);
		
		mi2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				showAbout();
			}
		});
		
		menubar.add(m2);
		
		setJMenuBar(menubar);
		
		
		setTitle("US Airways Virtual Airlines ACARS "+Version.VERSION);
		
		Image icon;
		try 
		{
		    icon = ImageIO.read(new File("icon.gif"));
		} 
		catch (IOException e) 
		{
			icon = null;
		}

		
		if(icon != null)
		{
			setIconImage(icon);
		}
		
		
		
		
		
		pack();
		setResizable(false);
		
                setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				exit();
			}
                        
		});
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		

	}
        
        public ChatPanel getChatPanel()
        {
            return chatPane;
        }
	
	public SchedulePanel getSchedulePanel()
	{
		return schedulePane;
	}
	
        public BlackBoxPanel getBlackBoxPanel()
        {
            return blackboxPane;
        }
        
        public CompanyPanel getCompanyPanel()
        {
            return companyPane;
        }
        
        public DispatchPanel getDispatchPanel()
        {
            return dispatchPane;
        }
        
	public void switchTab(int tab)
	{
		tabbedPane.setSelectedIndex(tab);
	}
	
	public void writeChatLog()
	{
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showSaveDialog(this);
		
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			chatPane.writeChatLog(chooser.getSelectedFile());
		}
	}
	
	public void setOfflineMode(boolean offlineMode)
	{
		dispatchPane.setOfflineMode(offlineMode);
		chatPane.setOfflineMode(offlineMode);
                
                eventMode.setEnabled(!offlineMode);
            
		//pack();
		
		if(offlineMode)
		{
			JOptionPane.showMessageDialog(this, "Offline mode activated.  PIREPs will automatically be submitted when the server is available.  \nConsult the schedule tab for valid flights.", "Offline mode", JOptionPane.INFORMATION_MESSAGE);
			
		}
	}
	
	public void exit()
	{
            
		if(!dispatchPane.isInFlight() || JOptionPane.showConfirmDialog(this, "Flight in progress.  Exit ACARS?", "Exit ACARS?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == 
			JOptionPane.OK_OPTION)
		{
			System.exit(1);
		}
	}
        
        public OnlineListPanel getOnlineListPanel()
        {
            return onlineListPane;
        }
        
        public ClientListener getClient()
        {
            return client;
        }
        
        public Pilot getPilot()
        {
            return pilot;
        }
        
        public ACARS getFDR()
        {
            return fdr;
        }
        
        public PIREPManager getPIREPlist()
        {
            return pireplist;
        }
        
        
	private JFrame aboutFrame;
        
        
        public void showHelp()
        {
            JOptionPane.showMessageDialog(this, "Please refer to the ACARS3 manual, found in your ACARS3 installation directory.", "Help", JOptionPane.INFORMATION_MESSAGE);
			
        }
	
	public void showAbout()
	{
		aboutFrame = new JFrame("About ACARS");
		
		JPanel panel = new JPanel();
		panel.setBackground(Global.BACKGROUND);
		
		
		
		BufferedImage logo;
		try 
		{
		    logo = ImageIO.read(new File("Logo.jpg"));
		} 
		catch (IOException e) 
		{
		}
		
		JButton ok = new JButton("OK");
		
		ok.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				aboutFrame.setVisible(false);
				aboutFrame = null;
			}
		});

		
		panel.setLayout(new GridBagLayout());
		constrain(panel, new LogoPanel(), 0, 0, 2, 1);
		
		constrain(panel, new JLabel("US Airways VA ACARS "+Version.VERSION), 0, 1, 2, 1);
		constrain(panel, new JLabel("©2014 US Airways Virtual Airlines"), 0, 2, 1, 1);
		constrain(panel, new JLabel("usairwaysva.org"), 1, 2, 2, 2);
		constrain(panel, new JLabel("Developed by USA4644 Michael Levin (michael.levin@usairwaysva.org)"), 0, 3, 2, 1);
		
		
		JTextArea eula = new JTextArea(8, 35);
		eula.setEditable(false);
		eula.setLineWrap(true);
		eula.setWrapStyleWord(true);
		eula.setBackground(Global.TEXT_UNEDITABLE);
		
		
		eula.setText("Warning: This computer program is protected by copyright law and international treaties.  Unauthorized reproduction, distribution or reverse engineering of this program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law.");
		
		constrain(panel, new JScrollPane(eula, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER), 0, 4, 2, 1);
		
		constrain(panel, ok, 0, 5, 2, 1, GridBagConstraints.CENTER);
		
		aboutFrame.add(panel);
		aboutFrame.pack();
		aboutFrame.setResizable(false);
		
		aboutFrame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				aboutFrame.setVisible(false);
				aboutFrame = null;
			}
		});
                
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		aboutFrame.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
                
		aboutFrame.setVisible(true);
		
	}
}