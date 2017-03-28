package masterinterface;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Farima on 7/16/2016.
 */
public class InputFacade {

    private static int port=1231;
    private ArrayList<String> availableWorkers=new ArrayList<>();

    public static void main(String[] args) {
        new InputFacade().receiveWorkUnit();
    }


    public void receiveWorkUnit()

    {
        Thread t1=new Thread(new ThreadedListener(port));
        availableWorkers.add("127.0.0.1:"+Integer.toString(port));
        port++;
        Thread t2=new Thread(new ThreadedListener(port));
        availableWorkers.add("127.0.0.1:"+Integer.toString(port));
        port++;
        Thread t3=new Thread(new ThreadedListener(port));
        availableWorkers.add("127.0.0.1:"+Integer.toString(port));
        port++;
        Thread t4=new Thread(new ThreadedListener(port));
        availableWorkers.add("127.0.0.1:"+Integer.toString(port));

        reportAvailableWorkers();

        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }

    public void reportAvailableWorkers()
    {
        ServerSocket listener=null;
        Socket socket=null;
        try {

            String report="";
            for (String s:availableWorkers)
            {
                report=report+s+",";
            }
            report=report.substring(0,report.length()-1);
            listener = new ServerSocket(1230);
            System.out.println("Port 1230 is waiting...");
            System.out.println("----------------");
            socket = listener.accept();
            OutputStream os = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeInt(report.length());
            dos.writeChars(report);
            dos.close();
            os.close();
            socket.close();
            listener.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
