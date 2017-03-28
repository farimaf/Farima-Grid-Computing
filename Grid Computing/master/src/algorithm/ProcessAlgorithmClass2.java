package algorithm;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Farima on 7/22/2016.
 */
public class ProcessAlgorithmClass2 implements ProcessAlgorithm{
    @Override
    public int doProcess(String workUnit) {
            int count=0;
            try(InputStream in = new FileInputStream(workUnit))
            {

                int c = 0;
                while ( (c = in.read()) != -1 )
                    if ((char)c!='\n'&&(char)c!='\r')
                        count++;

                in.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        System.out.println("I'm Algorithm 2!");
        return count;
    }
}

