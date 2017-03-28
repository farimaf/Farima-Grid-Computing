package logging;

import org.eclipse.persistence.logging.LogFormatter;
import sun.net.www.protocol.http.logging.HttpLogFormatter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.*;

/**
 * Created by Farima on 7/25/2016.
 */
public class MasterLogger {

    private static MasterLogger instance=null;
    private Logger logger;
    private FileHandler file = null;
    private MasterLogger(){};

    public static MasterLogger getInstance()
    {
        if (instance==null) instance=new MasterLogger();
        return instance;
    }

    public void saveLog(String logMsg,Level severityLevel)
    {
        try {
        if (logger==null)
        {
            logger=Logger.getLogger("MasterEvents");
            logger.setLevel(Level.INFO);
            logger.setUseParentHandlers(false);
            file = new FileHandler("master.log",true);
            file.setFormatter(new SimpleFormatter());
            logger.addHandler(file);
        }
            logger.log(severityLevel,logMsg);
        }        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
