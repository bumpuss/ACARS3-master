package acars3.client;

import java.io.*;
import java.util.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import static graphicutils.GraphicUtils.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;
import javax.swing.event.TableModelListener;
import acars3.net.KickMessage;
import acars3.net.UnmuteMessage;

public class OnlineListPanel extends JPanel
{
	private static final String[] COLUMN_NAMES = new String[]{"ID", "Dep", "Arr", "Status"};
	
	private OnlineList list;
	private JTable table;
        
        private JButton pm, kick, unmute;
        private GUI gui;
	
	public OnlineListPanel(OnlineList list_, GUI gui_)
	{
                this.gui = gui_;
		this.list = list_;
		
		table = new JTable(new OLTableModel())
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
		table.setRowSelectionAllowed(false);
		table.setColumnSelectionAllowed(false);
		table.setCellSelectionEnabled(false);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setPreferredScrollableViewportSize(new Dimension( 
            402,
            15*16-1
    	)); 
    		
    
    	table.setDefaultRenderer(String.class, new OLCellRenderer());
    	table.setShowGrid(false);
    	table.setBackground(Global.BACKGROUND);
    	
    	table.getColumnModel().getColumn(0).setMaxWidth(500);
    	table.getColumnModel().getColumn(0).setMinWidth(50);
    	table.getColumnModel().getColumn(0).setPreferredWidth(240);
    	
    	table.getColumnModel().getColumn(1).setMinWidth(40);
    	table.getColumnModel().getColumn(1).setPreferredWidth(40);
    	table.getColumnModel().getColumn(1).setMaxWidth(80);
    	
    	table.getColumnModel().getColumn(2).setMinWidth(40);
    	table.getColumnModel().getColumn(2).setPreferredWidth(40);
    	table.getColumnModel().getColumn(2).setMaxWidth(80);
    	
    	table.getColumnModel().getColumn(3).setPreferredWidth(80);
    	table.getColumnModel().getColumn(3).setMaxWidth(120);
    	table.getColumnModel().getColumn(3).setMinWidth(60);
        
        pm = new JButton("PM");
        kick = new JButton("Mute");
        unmute = new JButton("Unmute");
        
        pm.setEnabled(false);
        kick.setEnabled(false);
        unmute.setEnabled(false);
        
        pm.addActionListener(new ActionListener()
        {
           public void actionPerformed(ActionEvent e)
           {
               gui.getChatPanel().pm(list.get(table.getSelectedRow()).getId());
               table.clearSelection();
           }
        });
        
        kick.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                gui.getClient().send(new KickMessage(list.get(table.getSelectedRow()).getId()));
                table.clearSelection();
            }
        });
        
        unmute.addActionListener(new ActionListener()
        {
           public void actionPerformed(ActionEvent e)
            {
                gui.getClient().send(new UnmuteMessage(list.get(table.getSelectedRow()).getId()));
                table.clearSelection();
            }
        });
        
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
        {
                public void valueChanged(ListSelectionEvent e)
                {

                        table.repaint();
                        pm.setEnabled(table.getSelectedRow() >= 0 && !list.get(table.getSelectedRow()).getId().equals(gui.getPilot().getId()));
                        kick.setEnabled(table.getSelectedRow() >= 0 && !list.get(table.getSelectedRow()).isStaff());
                        unmute.setEnabled(table.getSelectedRow() >= 0 && !list.get(table.getSelectedRow()).isStaff());
                }
        });
		
		setBackground(Global.BACKGROUND);
		setLayout(new GridBagLayout());
		
		JScrollPane scrollpane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollpane.getViewport().setBackground(Global.BACKGROUND);
		constrain(this, scrollpane, 0, 0, 3, 1);
		
		JLabel label = new JLabel("Pilots highlighted in red are staff members");
		label.setForeground(Color.red);
		label.setFont(Global.LIST_FONT);
		
                constrain(this, pm, 0, 1, 1, 1, 0, 0, 2, 5);
                
                if(gui.getPilot().isAdmin())
                {
                    constrain(this, kick, 1, 1, 1, 1, 0, 0, 2, 5);
                    constrain(this, unmute, 2, 1, 1, 1, 0, 0, 2, 5);
                }
                else
                {
                    constrain(this, label, 2, 1, 1, 1, GridBagConstraints.EAST, 0, 0, 2, 5);
                }
		
	}
	
        public void update(OnlineListRecord record)
        {
            list.update(record);
            table.revalidate();
            table.repaint();
        }
        
        public void remove(String id, String name)
        {
            list.remove(id, name);
            table.revalidate();
            table.repaint();
        }
        
	private class OLCellRenderer extends DefaultTableCellRenderer
	{
		public OLCellRenderer(){}
		
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	    {
	        Component c = super.getTableCellRendererComponent(table, value, false, false, row, column);
	        
	        
	        if(list.get(row).isStaff())
	        {
	        	c.setForeground(Color.red);
	        }
	        else
	        {
	        	c.setForeground(Color.black);
	        }
                
                
                if(table.getSelectedRow() == row)
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
	
	private class OLTableModel implements TableModel
	{
		public OLTableModel()
		{
			
		}
		
		public void addTableModelListener(TableModelListener l){}		
		public void removeTableModelListener(TableModelListener l){}
		
		public Class getColumnClass(int columnIndex)
		{
			return String.class;
		}
		
		public int getColumnCount()
		{
			return 4;
		}
		
		public String getColumnName(int columnIndex)
		{
			return COLUMN_NAMES[columnIndex];
		}
		
		public int getRowCount()
		{
			return list.size();
		}
		
		public String getValueAt(int rowIndex, int columnIndex)
		{
			OnlineListRecord item = list.get(rowIndex);
			
			switch(columnIndex)
			{
				case 0:
					return item.getId()+" "+item.getRank()+" "+item.getName();
				case 1:
					return item.getOrigin();
				case 2:
					return item.getDest();
				case 3:
					return item.getStatus();
				default:
					return "";
			}
		}
		
		public void setValueAt(Object value, int rowIndex, int columnIndex){}
		
		public boolean isCellEditable(int rowIndex, int columnIndex)
		{
			return false;
		}
	}
}