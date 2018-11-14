/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acars3.server;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 *
 * @author Michael
 */
public class EncryptedReceiveRequest extends ReceiveRequest
{
    private SecretKey aesKey;
    
    public EncryptedReceiveRequest(ServerListener client, String directory, String name, SecretKey aesKey)
    {
        super(client, directory, name);
        this.aesKey = aesKey;
    }
    
    public OutputStream createOutputStream() throws Exception
    {
        IvParameterSpec ivParameterSpec = new IvParameterSpec(aesKey.getEncoded());
        Cipher decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        decryptCipher.init(Cipher.DECRYPT_MODE, aesKey, ivParameterSpec);
            
        return new BufferedOutputStream(new CipherOutputStream(new FileOutputStream(new File(directory+"\\"+name)), decryptCipher));  
    }
}
