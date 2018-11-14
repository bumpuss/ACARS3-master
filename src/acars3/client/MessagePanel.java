/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acars3.client;

import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import static graphicutils.GraphicUtils.*;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JLabel;

/**
 *
 * @author Michael
 */
public class MessagePanel extends JFrame
{
    private JTextField msg;
    
    public MessagePanel()
    {
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
        
        msg = new JTextField(30);
        msg.setEditable(false);
        msg.setBackground(Global.BACKGROUND);
        Border emptyBorder = BorderFactory.createEmptyBorder();
        msg.setBorder(emptyBorder);
        msg.setHighlighter(null);
        msg.setFont(Global.DISPATCH_FONT);
        
        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        constrain(p, new LogoPanel(), 0, 0, 1, 1);
        constrain(p, new JLabel(""), 0, 1, 1, 1);
        constrain(p, msg, 0, 2, 1, 1);
        constrain(p, new JLabel(""), 0, 3, 1, 1);
        p.setBackground(Global.BACKGROUND);
        setBackground(Global.BACKGROUND);
        
        add(p);
        pack();
        setResizable(false);
        
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        
        addWindowListener(new WindowAdapter()
        {
                public void windowClosing(WindowEvent e)
                {
                        System.exit(1);
                }
        });
        
        setTitle("US Airways Virtual Airlines ACARS "+Version.VERSION);
    }
    public void setText(String txt)
    {
        msg.setText(txt);
    }
}
