package acars3.client;

import java.net.Socket;

public class Client
{
	public static void main(String[] args) throws Exception
	{
                ClientListener client = null;
                
                MessagePanel status = new MessagePanel();
                status.setText("Connecting to server...");
                status.setVisible(true);
        
                try
                {
                    //client = new ClientListener("0.0.0.0");
                    client = new ClientListener(acars3.net.NetInfo.ADDRESS);
                }
                catch(Exception ex)
                {
                    ex.printStackTrace(System.err);
                }
                
                status.setVisible(false);

		if(client != null && client.isConnected())
                {
                    new Login(client);
                }
                else
		{
			// offline mode
			GUI acars = new GUI(true, new OfflineClientListener(), new Pilot());
			acars.setVisible(true);
			acars.setOfflineMode(true);
			
		}
		
		
		
		
	}
}