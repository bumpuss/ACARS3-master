package acars3.client;

import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.awt.*;
import static graphicutils.GraphicUtils.*;
import java.security.MessageDigest;
import javax.imageio.ImageIO;


import javax.xml.soap.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;

public class Login extends JFrame
{
	private JTextField id;
	private JPasswordField password;
	
        private JButton ok;
	
        private ClientListener client;
        
        
        
	public Login(ClientListener client)
	{
                this.client = client;
                
                
                client.setLoginPanel(this);
                
		id = new JTextField(10);
		id.setText("USA");
		id.setBackground(Global.BACKGROUND);
		id.setCaretPosition(3);
		
		password = new JPasswordField(10);
		password.setBackground(Global.BACKGROUND);
		
		ok = new JButton("Login");
		final JButton cancel = new JButton("Cancel");
		
		cancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.exit(1);
			}
		});
		
		ActionListener al = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				login();
			}
		};
		
		ok.addActionListener(al);
		password.addActionListener(al);
		id.addActionListener(al);
		
		JPanel panel = new JPanel();
		panel.setBackground(Global.BACKGROUND);
		panel.setLayout(new GridBagLayout());
		
		constrain(panel, new LogoPanel(), 0, 0, 1, 1);
		
		JPanel panel2 = new JPanel();
		panel2.setBackground(Global.BACKGROUND);
		panel2.setLayout(new GridBagLayout());
                
                JPanel panel3 = new JPanel();
		panel3.setBackground(Global.BACKGROUND);
		panel3.setLayout(new GridBagLayout());
		
		JLabel label = new JLabel("Crew Login");
		label.setFont(Global.TITLE_FONT);
		
		constrain(panel2, label, 0, 0, 2, 1, GridBagConstraints.CENTER, 5, 10, 10, 10);
		constrain(panel2, new JLabel("Pilot ID: "), 0, 1, 1, 1);
		constrain(panel2, id, 1, 1, 1, 1);
		constrain(panel2, new JLabel("Password: "), 0, 2, 1, 1);
		constrain(panel2, password, 1, 2, 1, 1);
		constrain(panel3, ok, 0, 0, 1, 1);
		constrain(panel3, cancel, 1, 0, 1, 1);
		
		constrain(panel, panel2, 0, 1, 1, 1, GridBagConstraints.CENTER);
                constrain(panel, panel3, 0, 2, 1, 1, GridBagConstraints.CENTER);
		
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
                
		add(panel);
		pack();
		
		setResizable(false);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		setTitle("American Virtual Airlines ACARS "+Version.VERSION);
		
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(1);
			}
		});
		
		setVisible(true);
	}
	
	public void login()
	{
            id.setEditable(false);
            password.setEditable(false);
            
            id.setBackground(Global.TEXT_UNEDITABLE);
            password.setBackground(Global.TEXT_UNEDITABLE);
            ok.setEnabled(false);
            
            setVisible(false);
            
            client.login(id.getText(), md5(password.getText()));
        }
        
        private String md5(String password)
        {
            try
            {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(password.getBytes());

                byte byteData[] = md.digest();

                //convert the byte to hex format method 1
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < byteData.length; i++) {
                 sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
                }

                //convert the byte to hex format method 2
                StringBuffer hexString = new StringBuffer();
                for (int i=0;i<byteData.length;i++) {
                        String hex=Integer.toHexString(0xff & byteData[i]);
                        if(hex.length()==1) hexString.append('0');
                        hexString.append(hex);
                }

                return hexString.toString();
            }
            catch(Exception ex)
            {
                ex.printStackTrace(System.err);
                System.exit(1);
                return "";
            }
            
        }
        
        public void login_failed()
        {
            id.setEditable(true);
            password.setEditable(true);
            id.setBackground(Global.BACKGROUND);
            password.setBackground(Global.BACKGROUND);
            
            password.setText("");
            
            JOptionPane.showMessageDialog(this, "Your username or password are not correct.", "Invalid login", JOptionPane.WARNING_MESSAGE);
            
            ok.setEnabled(true);
        }
        
        public void bad_version()
        {
            setVisible(false);
            id.setEditable(true);
            password.setEditable(true);
            id.setBackground(Global.BACKGROUND);
            password.setBackground(Global.BACKGROUND);
            
            id.setText("USA");
            id.setCaretPosition(3);
            password.setText("");
            ok.setEnabled(true);
            
            JOptionPane.showMessageDialog(this, "Your ACARS is outdated. Please download the newest version from americanvirtual.org", "Old version", JOptionPane.WARNING_MESSAGE);
            System.exit(1);
            
        }
        
        public void login_success()
        {
            setVisible(false);
        }

}
