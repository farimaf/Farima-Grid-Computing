package network;

import biz.Result;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Farima on 7/28/2016.
 */
public class Reporter {
    private static Reporter instance=null;

    private Reporter(){};

    public static Reporter getInstance()
    {
        if (instance==null) instance=new Reporter();
        return instance;
    }

    public String getAvailableWorkers()
    {
        Socket s;
        String ip="127.0.0.1";
        int port=1230;
        String result="";

        try {
            s = new Socket(ip, port);
            InputStream is = s.getInputStream();
            System.out.println("Connected to " + port);
            DataInputStream dis=new DataInputStream(is);
            int length=dis.readInt();
            for (int i = 0; i <length ; i++) {
                result+=dis.readChar();
            }
            dis.close();
            is.close();
            s.close();

        } catch (Exception e) {
            System.out.println("Not possible to connect to "+port);
        }
        return result;

    }
}
