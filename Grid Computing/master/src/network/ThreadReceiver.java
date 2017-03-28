package network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.io.*;
/**
 * Created by Farima on 7/23/2016.
 */
public class ThreadReceiver implements Callable {
    private Socket socket;
    private int result;

    public ThreadReceiver(Socket socket) {
        this.socket=socket;

    }
    @Override
    public Object call() throws Exception {
        String myResultInJson="";

        try (DataInputStream dis=new DataInputStream(socket.getInputStream())){

            int length=dis.readInt();
            for (int i = 0; i <length ; i++) {
                myResultInJson+=dis.readChar();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return myResultInJson;
    }
}
