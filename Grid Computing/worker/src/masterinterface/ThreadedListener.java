package masterinterface;

import algorithm.ProcessAlgorithm;
import biz.Processor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Farima on 7/22/2016.
 */
public class ThreadedListener implements Runnable {

    private int myPort;

    public ThreadedListener(int myPort) {
        this.myPort=myPort;
    }

    @Override
    public void run() {
        ServerSocket listener=null;
        Socket socket=null;
        try {
            long myProcessTime=0;

            listener = new ServerSocket(myPort);
            System.out.println("Worker "+myPort+" is waiting for request...");
            System.out.println("----------------");
            socket = listener.accept();

            long startingTime=System.currentTimeMillis();

            InputStream is=socket.getInputStream();
            DataInputStream dis=new DataInputStream(is);
            int length=dis.readInt();
            String myWorkUnitName="";
            String classFileName="";
            byte[] classFile=null;
            int myResult=0;

            if(length>0)
            {
                for (int i = 0; i <length ; i++) {
                    myWorkUnitName+=dis.readChar();
                }
            }

            System.out.println("Worker " + myPort + " received " + myWorkUnitName);
            System.out.println("----------------");

            int lengthClassFileName=dis.readInt();
            if(lengthClassFileName>0)
            {
                for (int i = 0; i <lengthClassFileName ; i++) {
                    classFileName+=dis.readChar();
                }
            }

            int lengthClassFile=dis.readInt();
            if(lengthClassFile>0)
            {
                classFile=new byte[lengthClassFile];
                for (int i = 0; i <lengthClassFile ; i++) {
                    classFile[i]=dis.readByte();
                }
            }

            myResult=new Processor().doProcess(classFile,classFileName,myWorkUnitName);
            long finishingTime=System.currentTimeMillis();
            myProcessTime=finishingTime-startingTime;

            Thread t1=new Thread(new ThreadSender(socket,myResult,Integer.toString(myPort),myWorkUnitName,myProcessTime));
            t1.start();
            t1.join();

            dis.close();
            is.close();


        } catch (Exception e) {

            e.printStackTrace();
        }
        finally {
            try {
                socket.close();
            }
            catch (IOException e)
            {
                System.out.println("could not close");
            }
        }

    }
}
