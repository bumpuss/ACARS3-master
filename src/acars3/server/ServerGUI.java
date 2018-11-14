/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acars3.server;

import acars3.net.NetInfo;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import static graphicutils.GraphicUtils.*;

/**
 *
 * @author Michael
 */
public class ServerGUI extends JFrame
{
    public ServerGUI()
    {
        JButton stop = new JButton("Close ACARS server engine");
        
        stop.addActionListener(new ActionListener()
        {
           public void actionPerformed(ActionEvent e)
           {
               System.exit(1);
           }
        });
        
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                System.exit(1);
            }
        });
        
        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        constrain(p, stop, 0, 0, 1, 1);
        constrain(p, new JLabel("Ports: "+NetInfo.PORT+", "+NetInfo.FILE_PORT), 0, 1, 1, 1);
        
        add(p);
        pack();
        setResizable(false);
        
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        
        setTitle("ACARS3 Server");
        
        setVisible(true);
    }
}
