package acars3.client;

import java.io.*;
import java.util.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.table.*;
import static graphicutils.GraphicUtils.*;
import javax.swing.event.*;

import javax.crypto.*;
import javax.crypto.spec.*;

public class SchedulePanel extends JPanel
{
	public static final File scheduleFile = new File("schedule/schedule.dat");
        public static final File keyFile = new File("schedule/schedule_key.dat");
	
	private Set<Flight> routes;
	
	private JComboBox carrier, aircraft, cat, dep, arr;
	
	private JTable table;
	
	private Flight[] results;
	
	private DispatchPanel dispatch;
	private GUI gui;
	
	private JButton select, search, clear;
        
        private char rank;
        
        private JTextField numFound;
        private JScrollPane scrollpane;
	
	public SchedulePanel(GUI gui_, String rank_, boolean loadSchedule)
	{
		this.gui = gui_;
                this.rank = Global.convertRank(rank_);
		
		routes = new HashSet<Flight>();
		
		results = new Flight[0];
		

                try
                {
                        readSchedule(scheduleFile, readKey(keyFile));
                }
                catch(IOException ex)
                {
                    ex.printStackTrace(System.err);
                        JOptionPane.showMessageDialog(this, "Unable to load schedule database.", "Error", JOptionPane.INFORMATION_MESSAGE);
                }
                catch(Exception ex2)
                {
                    ex2.printStackTrace(System.err);
                        JOptionPane.showMessageDialog(this, "Schedule database is corrupted.", "Error", JOptionPane.INFORMATION_MESSAGE);
                }
                
		
		
		
		String[][] lists = getLists();
		
		carrier = new JComboBox(lists[0]);
		aircraft = new JComboBox(lists[1]);
		cat = new JComboBox(lists[2]);
		dep = new JComboBox(lists[3]);
		arr = new JComboBox(lists[3]);
		
                numFound = new JTextField(20);
                numFound.setBorder(BorderFactory.createEmptyBorder());
                numFound.setBackground(Global.BACKGROUND);
                numFound.setEditable(false);
                
		if(routes.isEmpty())
		{
			dep.setEditable(false);
			dep.setBackground(Global.TEXT_UNEDITABLE);
			
			arr.setEditable(false);
			arr.setBackground(Global.TEXT_UNEDITABLE);
			
			carrier.setEnabled(false);
			aircraft.setEnabled(false);
			cat.setEnabled(false);
		}
		else
		{
			dep.setBackground(Global.BACKGROUND);
			arr.setBackground(Global.BACKGROUND);
			carrier.setBackground(Global.BACKGROUND);
			aircraft.setBackground(Global.BACKGROUND);
			cat.setBackground(Global.BACKGROUND);
		}
		
		dep.setFont(Global.DISPATCH_FONT);
		arr.setFont(Global.DISPATCH_FONT);
		cat.setFont(Global.DISPATCH_FONT);
		carrier.setFont(Global.DISPATCH_FONT);
		aircraft.setFont(Global.DISPATCH_FONT);
		
		table = new JTable(new SPTableModel())
		{
			public boolean getScrollableTracksViewportWidth()
			{
				return false;
			}	
			public boolean getScrollableTracksViewportHeight()
			{
				return false;
			}
		};
		
		table.setFont(Global.LIST_FONT);
		
		table.setPreferredScrollableViewportSize(new Dimension( 
            660,
            15*16
    	)); 
    	
		
		
		table.setShowGrid(false);
    	//table.setBackground(Global.BACKGROUND);
    	
    	table.getColumnModel().getColumn(0).setMaxWidth(50);
    	table.getColumnModel().getColumn(0).setMinWidth(50);
    	table.getColumnModel().getColumn(0).setPreferredWidth(50);
    	
    	table.getColumnModel().getColumn(1).setMinWidth(60);
    	table.getColumnModel().getColumn(1).setPreferredWidth(60);
    	table.getColumnModel().getColumn(1).setMaxWidth(60);
    	
    	table.getColumnModel().getColumn(2).setMinWidth(60);
    	table.getColumnModel().getColumn(2).setPreferredWidth(60);
    	table.getColumnModel().getColumn(2).setMaxWidth(60);
    	
    	table.getColumnModel().getColumn(3).setPreferredWidth(60);
    	table.getColumnModel().getColumn(3).setMaxWidth(60);
    	table.getColumnModel().getColumn(3).setMinWidth(60);
    	
    	table.getColumnModel().getColumn(4).setPreferredWidth(160);
    	table.getColumnModel().getColumn(4).setMaxWidth(160);
    	table.getColumnModel().getColumn(4).setMinWidth(160);
    	
    	table.getColumnModel().getColumn(5).setPreferredWidth(90);
    	table.getColumnModel().getColumn(5).setMaxWidth(90);
    	table.getColumnModel().getColumn(5).setMinWidth(90);
    	
    	table.getColumnModel().getColumn(6).setPreferredWidth(90);
    	table.getColumnModel().getColumn(6).setMaxWidth(90);
    	table.getColumnModel().getColumn(6).setMinWidth(90);
        
        table.getColumnModel().getColumn(7).setPreferredWidth(60);
    	table.getColumnModel().getColumn(7).setMaxWidth(60);
    	table.getColumnModel().getColumn(7).setMinWidth(60);
    	
    	table.getColumnModel().getColumn(8).setPreferredWidth(30);
    	table.getColumnModel().getColumn(8).setMaxWidth(30);
    	table.getColumnModel().getColumn(8).setMinWidth(30);
    	
    	table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	
    	table.setDefaultRenderer(Flight.class, new SPCellRenderer());
		
		setBackground(Global.BACKGROUND);
		
		search = new JButton("Search");
		clear = new JButton("Clear");
		select = new JButton("Select flight");
		
		select.setEnabled(false);
		
		scrollpane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollpane.getViewport().setBackground(Global.BACKGROUND);
		
		
		clear.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dep.setSelectedIndex(0);
				arr.setSelectedIndex(0);
				carrier.setSelectedIndex(0);
				cat.setSelectedIndex(0);
				aircraft.setSelectedIndex(0);
				results = new Flight[0];
				select.setEnabled(false);
                                numFound.setText("");
				
				table.clearSelection();
				table.revalidate();
				scrollpane.repaint();

			}
		});

		search.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				search.setEnabled(false);
				clear.setEnabled(false);
				dep.setEnabled(false);
				arr.setEnabled(false);
				carrier.setEnabled(false);
				cat.setEnabled(false);
				aircraft.setEnabled(false);
				select.setEnabled(false);
				
				table.clearSelection();
				
				results = search_help((String)dep.getSelectedItem(), (String)arr.getSelectedItem(), (String)carrier.getSelectedItem(), (String)aircraft.getSelectedItem(), (String)cat.getSelectedItem());
				numFound.setText(results.length+" flights found");
				table.revalidate();
				scrollpane.repaint();
				
				search.setEnabled(true);
				clear.setEnabled(true);
				dep.setEnabled(true);
				arr.setEnabled(true);
				carrier.setEnabled(true);
				cat.setEnabled(true);
				aircraft.setEnabled(true);
				select.setEnabled(true);
			}
		});
		
		table.setRowSelectionAllowed(true);
		table.setColumnSelectionAllowed(false);
		table.setCellSelectionEnabled(false);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e)
			{

				table.repaint();
				select.setEnabled(table.getSelectedRow() >= 0 && dispatch.getMode() == 0);
			}
		});
		
		select.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
			 	Flight flight = results[table.getSelectedRow()];
                                
                                
                                
                                if(rank == ' ')
                                {
                                    
                                    if(flight.getCAT() == 'A' || JOptionPane.showConfirmDialog(gui, "The selected flight is restricted to rank of "+Global.convertRank(flight.getCAT())+" and above.\n"+
                                            "Are you sure you want to select it?", "Are you sure?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                                    {
                                        selectFlight(flight);
                                    }
                                    
                                }
                                else if(flight.getCAT() > rank)
                                {
                                    JOptionPane.showMessageDialog(gui, "Your rank is not high enough to fly that route.", "Error", JOptionPane.WARNING_MESSAGE);
			
                                    return;
                                }
                                else
                                {
                                    selectFlight(flight);
                                }
			 	
			}
		});
		
		JPanel p = new JPanel();
		p.setBackground(Global.BACKGROUND);
		p.setLayout(new GridBagLayout());
		
		JPanel p1 = new JPanel();
		p1.setBackground(Global.BACKGROUND);
		p1.setLayout(new GridBagLayout());
		
                constrain(p1, new JLabel("Departure airport: "), 0, 1, 1, 1);
                constrain(p1, dep, 1, 1, 1, 1);
                constrain(p1, new JLabel("Arrival airport: "), 0, 2, 1, 1);
                constrain(p1, arr, 1, 2, 1, 1);
                constrain(p1, new JLabel("Carrier: "), 0, 3, 1, 1);
                constrain(p1, carrier, 1, 3, 1, 1);
                constrain(p1, new JLabel("Aircraft: "), 0, 4, 1, 1);
                constrain(p1, aircraft, 1, 4, 1, 1);
                constrain(p1, new JLabel("Category: "), 0, 5, 1, 1);
                constrain(p1, cat, 1, 5, 1, 1);


                constrain(p, p1, 0, 0, 3, 1);
                constrain(p, search, 0, 1, 1, 1);
                constrain(p, clear, 1, 1, 1, 1);
                constrain(p, numFound, 2, 1, 1, 1);
                
		

		constrain(p, scrollpane, 0, 2, 3, 1);
		constrain(p, select, 0, 3, 3, 1);
			
			
		add(p);
		
	}
        
        public void setSearchEnabled(boolean enabled)
        {
            clear.setEnabled(enabled);
            search.setEnabled(enabled);
            dep.setEnabled(enabled);
            arr.setEnabled(enabled);
            cat.setEnabled(enabled);
            carrier.setEnabled(enabled);
            aircraft.setEnabled(enabled);
        }
        
        public void setDispatchPanel(DispatchPanel dispatch)
        {
            this.dispatch = dispatch;
        }
	
        public synchronized void setResults(java.util.List<Flight> flights)
        {
            select.setEnabled(false);
            
            results = new Flight[flights.size()];
            
            for(int i = 0; i < results.length; i++)
            {
                results[i] = flights.get(i);
            }
            
            table.clearSelection();
            table.revalidate();
            scrollpane.repaint();
            select.setEnabled(true);
        }
        public void selectFlight(Flight flight)
        {
            table.clearSelection();
            select.setEnabled(false);

            if(dispatch.loadFlight(flight))
            {
                gui.switchTab(0);
            }
            
        }
        
	public void setSelectFlightEnabled(boolean e)
	{
		select.setEnabled(e && table.getSelectedRow() >= 0);
		table.setRowSelectionAllowed(e);
	}
	
        protected ScheduleKey readKey(File input) throws Exception
        {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(input));
            
            in.readObject(); // ignore version
            
            ScheduleKey output = (ScheduleKey)in.readObject();
            
            in.close();
            
            return output;
        }
        
	public void readSchedule(File input, ScheduleKey key) throws Exception
	{
                SecretKey aesKey = key.getKey();
                
                IvParameterSpec ivParameterSpec = null;
                Cipher decryptCipher = null;
            
                ivParameterSpec = new IvParameterSpec(aesKey.getEncoded());
                decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                decryptCipher.init(Cipher.DECRYPT_MODE, aesKey, ivParameterSpec);
                
		ObjectInputStream in = new ObjectInputStream(new CipherInputStream(new FileInputStream(input), decryptCipher));
		
		routes = (Set<Flight>)in.readObject();
		
		in.close();
	}
	
	private String[][] getLists()
	{
		Set<String> cats = new TreeSet<String>();
		Set<String> carriers = new TreeSet<String>();
		Set<String> aircrafts = new TreeSet<String>();
		Set<String> airports = new TreeSet<String>();
		
		for(Flight f : routes)
		{
			carriers.add(f.getCarrier());
			cats.add(""+f.getCAT());
			aircrafts.add(f.getAircraft());
			airports.add(f.getOriginDescription());
			airports.add(f.getDestDescription());
		}
		
		return new String[][]{toArray(carriers), toArray(aircrafts), toArray(cats), toArray(airports)};
	}
        
        public void setEventMode()
        {
            
        }
        
        public String[] getAirportList()
        {
            Set<String> output = new TreeSet<String>();
            
            for(Flight f : routes)
            {
                String temp = f.getOriginDescription();
                
                if(temp.length() > 0 && temp.indexOf('(') > 0 && f.getOriginICAO().length() > 0)
                {
                    temp = temp.substring(temp.indexOf('('));

                    output.add(f.getOriginICAO()+" "+temp);
                }

                
               
            }
            
            return toArray(output);
        }
	
	private String[] toArray(Set<String> input)
	{
		String[] output = new String[input.size()+1];
		
		output[0] = "";
		
		int idx = 1;
		
		for(String x : input)
		{
			output[idx++] = x;
		}
		
		return output;
	}

	private Flight[] search_help(String dep, String arr, String carrier, String aircraft, String cat)
	{
		if(dep.length() == 0)
		{
			dep = null;
		}
                else
                {
                    dep = dep.substring(0, 3);
                }
		
		if(arr.length() == 0)
		{
			arr = null;
		}
                else
                {
                    arr = arr.substring(0, 3);
                }
		
		if(carrier.length() == 0)
		{
			carrier = null;
		}
		
		if(aircraft.length() == 0)
		{
			aircraft = null;
		}
		
		if(cat.length() == 0)
		{
			cat = null;
		}
		
		return search(dep, arr, carrier, aircraft, cat);
	}
	
	public Flight[] search(String dep, String arr, String carrier, String aircraft, String cat)
	{
		Set<Flight> output = new TreeSet<Flight>();

		char cat1 = (cat != null)? cat.charAt(0) : ' ';
		
		for(Flight f : routes)
		{
			if((dep == null || f.getOriginIATA().equals(dep)) &&
				(arr == null || f.getDestIATA().equals(arr)) && 
				(carrier == null || f.getCarrier().equals(carrier)) &&
				(aircraft == null || f.getAircraft().equals(aircraft)) &&
				(cat == null || f.getCAT() == cat1) )
				{
					output.add(f);
				}
		}
		
		
		Flight[] temp = new Flight[output.size()];
		
		int idx = 0;
		
		for(Flight f : output)
		{
			temp[idx++] = f;
		}
		
		return temp;
	}
	
	public static final String[] COLUMN_NAMES = new String[]{"Carrier", "Num", "Orig", "Dest", "Aircraft", "Dep. time", "Arr. time", "Duration", "Cat"};
	
	private class SPTableModel implements TableModel
	{
		
		
		public SPTableModel()
		{
			
		}
		
		public void addTableModelListener(TableModelListener l){}		
		public void removeTableModelListener(TableModelListener l){}
		
		public Class getColumnClass(int columnIndex)
		{
			return Flight.class;
		}
		
		public int getColumnCount()
		{
			return COLUMN_NAMES.length;
		}
		
		public String getColumnName(int columnIndex)
		{
			return COLUMN_NAMES[columnIndex];
		}
		
		public int getRowCount()
		{
			return results.length;
		}
		
		public String getValueAt(int rowIndex, int columnIndex)
		{		
			if(results.length <= rowIndex)
			{
				return "";
			}
			switch(columnIndex)
			{
				case 0: return results[rowIndex].getCarrier();
				case 1: return ""+results[rowIndex].getNumber();
				case 2: return results[rowIndex].getOriginIATA();
				case 3: return results[rowIndex].getDestIATA();
				case 4: return results[rowIndex].getAircraft();
				case 5: return results[rowIndex].getDepTime();
				case 6: return results[rowIndex].getArrTime();
                                case 7: return Global.dispDuration(results[rowIndex].getDuration());
				case 8: return ""+results[rowIndex].getCAT();
				default: return "";
			}
		}
		
		public void setValueAt(Object value, int rowIndex, int columnIndex){}
		
		public boolean isCellEditable(int rowIndex, int columnIndex)
		{
			return false;
		}
	}
	
	private class SPCellRenderer extends DefaultTableCellRenderer
	{
		public SPCellRenderer(){}
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	    {
	        Component c = super.getTableCellRendererComponent(table, value, false, false, row, column);
	        
	        c.setForeground(Color.black);
	        
	        if(row == table.getSelectedRow())
	        {
	        	c.setBackground(Global.SELECTED);	
	        }
	        else
	        {
	        	c.setBackground(Global.BACKGROUND);
	        }

	        return c;
	    }
	}
}