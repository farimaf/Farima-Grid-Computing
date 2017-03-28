package biz;

import algorithm.ProcessAlgorithm;
import masterinterface.ByteArrayClassLoader;

/**
 * Created by Farima on 7/22/2016.
 */
public class Processor {

    public int doProcess(byte[] classFile, String classFileName,String workUnitName)
    {
        int result=0;
        try {

            ProcessAlgorithm pa=(ProcessAlgorithm)new ByteArrayClassLoader().getClassFromBytes(classFile,classFileName).newInstance();
            result=pa.doProcess(workUnitName);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }
}
