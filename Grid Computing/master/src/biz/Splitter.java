package biz;

import persist.WorkunitPersistance;

import java.beans.PropertyChangeEvent;
import java.io.*;
import java.nio.*;
import java.nio.channels.FileChannel;
import java.time.LocalDateTime;
import java.util.Properties;

/**
 * Created by Farima on 7/20/2016.
 */

/**
 * A singletone class because it works as a utility class
 * **/
public class Splitter {
    private static Splitter instance=null;
    private Properties masterProperties=new Properties();
    private Splitter(){};

    public static Splitter getInstance()
    {
        if (instance==null) instance=new Splitter();
        return instance;
    }

    /**
     * the method splits the given file into smaller files based on the workers count
     * **/
    public WorkUnits split(String fileAddress, int workersCount)
    {
        loadProperties();
        WorkUnits returningWorkUnit=WorkUnits.getInstance();
        File inputFile=new File(fileAddress);
        try (BufferedInputStream bufInputStream=new BufferedInputStream(new FileInputStream(inputFile))) {

            long fileLength=inputFile.length();
            long partLength=fileLength/workersCount;//get the length of each file
            if(fileLength%workersCount>0) partLength++;//if some bytes are left, the partLength is incremented to accommodate those bytes
            int pointerOnPart=0;//the pointer that works inside each file
            int partNum=0;//shows the part (file) being processed
            BufferedOutputStream bufOutputStream=null;


            int c = bufInputStream.read();
                    while (c != -1)  {//this loop work on an inter-files basis
                        partNum++;
                        String fileName=masterProperties.getProperty("splitsPath")+LocalDateTime.now().getYear()+
                                LocalDateTime.now().getMonth()+LocalDateTime.now().getDayOfMonth()+"-"+
                                LocalDateTime.now().getHour()+LocalDateTime.now().getMinute()+
                                LocalDateTime.now().getSecond()+"-"+"WorkUnit"+partNum+".txt";//in order to choose a unique name for each file
                        bufOutputStream=new BufferedOutputStream(new FileOutputStream(fileName));
                        while (c != -1 && pointerOnPart<partLength){//this loop work on an intra-files basis
                            bufOutputStream.write(c);
                            pointerOnPart++;
                            c = bufInputStream.read();
                        }
                        pointerOnPart=0;
                        bufOutputStream.flush();
                        WorkUnit workUnit=new WorkUnit(fileName, false, false);
                        returningWorkUnit.addWorkUnit(workUnit);
                        new WorkunitPersistance().addWorkunit(workUnit);
                    }
                    bufOutputStream.close();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return returningWorkUnit;
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


}
