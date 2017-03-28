package masterinterface;

import com.sun.imageio.spi.OutputStreamImageOutputStreamSpi;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Farima on 7/23/2016.
 */
public class ThreadSender implements Runnable {

    private Socket socket;
    private int result;
    private String workUnitName;
    private String workerName;
    private long processTime;

    public ThreadSender(Socket socket, int result,String workerName,String workUnitName,long processTime) {
        this.socket=socket;
        this.result=result;
        this.workerName=workerName;
        this.workUnitName=workUnitName;
        this.processTime=processTime;
    }
    @Override
    public void run() {
        try(OutputStream os=socket.getOutputStream();DataOutputStream dos=new DataOutputStream(os))
        {
            Result toBeSentResult=new Result(result,processTime,workerName);
            String resultInString=new outputBuilder().buildJsonOutput(toBeSentResult);
            dos.writeInt(resultInString.length());
            dos.writeChars(resultInString);
            System.out.println(resultInString);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
