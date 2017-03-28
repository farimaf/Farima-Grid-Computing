package network;

import algorithm.ProcessAlgorithm;
import biz.Result;
import biz.WorkUnit;
import biz.Worker;
import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Farima on 7/21/2016.
 */
public class SenderReceiver {

    private static SenderReceiver instance=null;

    private SenderReceiver(){loadProperties();};

    private Properties masterProperties=new Properties();
    public static SenderReceiver getInstance()
    {

        if (instance==null) instance=new SenderReceiver();
        return instance;
    }

    public Result setAndReceiveWorkUnitToFromWorker(Worker worker, WorkUnit workUnit, ProcessAlgorithm processAlgorithm)
    {
        Socket s;
        String ip="";
        int port=0;
        ip = worker.getIp();
        port = worker.getPort();
        byte[] classInBytes=fileToByteArray(processAlgorithm.getClass().getName());
        Result result=null;
        //System.out.println(System.getProperty("java.class.path"));
        try {
            s = new Socket(ip, port);
            OutputStream os = s.getOutputStream();
            System.out.println("Connected to Worker " + port);
            DataOutputStream dos=new DataOutputStream(os);
            dos.writeInt(workUnit.getName().length());
            dos.writeChars(workUnit.getName());
            dos.writeInt(processAlgorithm.getClass().getName().length());
            dos.writeChars(processAlgorithm.getClass().getName());
            dos.writeInt(classInBytes.length);
            dos.write(classInBytes);

            result=getResultFromWorker(s);

            dos.close();
            os.close();
            s.close();

        } catch (Exception e) {
            System.out.println("Not possible to connect to worker "+port);
        }
        return result;

    }

    private byte[] fileToByteArray(String classFileName)
    {

        byte[] result=null;
        String pureClassname=classFileName.split("\\.")[1];
        String fileAddress=masterProperties.getProperty("algorithmsClassFilePath")+pureClassname+".class";
        try {
            result = Files.readAllBytes(Paths.get(fileAddress));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return result;
    }

    private void loadProperties()
    {
        try(FileInputStream fis=new FileInputStream(new File("master.properties")))
        {
            masterProperties.load(fis);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public Result getResultFromWorker(Socket s)
    {
        Result finalResult=null;
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<String> result = executor.submit(new ThreadReceiver(s));
            System.out.println(result.get());
            finalResult= new Gson().fromJson(result.get(),Result.class);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return finalResult;
    }
}
