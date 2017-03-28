package biz;

/**
 * Created by Farima on 7/16/2016.
 */

import algorithm.ProcessAlgorithm;
import algorithm.ProcessAlgorithmClass1;
import algorithm.ProcessAlgorithmClass2;
import logging.MasterLogger;
import network.Reporter;
import persist.WorkerPersistance;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;

/**
 * Singletone class because one obj is needed for as a facade
 * **/
public class BizFacade {
    private static BizFacade instance=null;
    private WorkerPersistance workerPersistance=new WorkerPersistance();

    private BizFacade(){};

    public static BizFacade getInstance()
    {
        if (instance==null) instance=new BizFacade();
        return instance;
    }
    /**
     * This method adds a worker to the list of the workers
     * **/
    public void setWorker(String worker)
    {
        WorkersList workersList=WorkersList.getInstance();
        String[] tempWorker=worker.split(":");
        Worker w=new Worker(tempWorker[0],Integer.parseInt(tempWorker[1]));
        workersList.addToList(w);
        workerPersistance.saveWorker(w);
    }

    public void removeWorker(String workerName) {
        WorkersList workersList = WorkersList.getInstance();
        String[] tempWorker = workerName.split(":");
        Worker targetWorker = null;
        for (Worker worker : WorkersList.getInstance()) {
            if (worker.getIp().equals(tempWorker[0]) && Integer.toString(worker.getPort()).equals(tempWorker[1])) {
                targetWorker = worker;
                break;
            }
        }
        if (targetWorker != null) {
            workersList.removeWorker(targetWorker);
            workerPersistance.removeWorker(targetWorker);
        }
    }

    public ArrayList<Worker> loadWorkers()
    {
        ArrayList<Worker> result=null;
        new WorkerPersistance().loadWorkers();
        if(WorkersList.getInstance().hasFreeWorker()) result=WorkersList.getInstance().getFreeWorkerList();
        return result;
    }

    /**
     * This method sends the file selected by the user to the splitter so that it is splitted based on the number of workers
     * **/
    private WorkUnits splitMainFile(String fileAddress)
    {
        return Splitter.getInstance().split(fileAddress, WorkersList.getInstance().getTotalWorkersCount());
    }
    public ArrayList<String> reportAvailableWorkers()
    {
        ArrayList<String> result=new ArrayList<>();
        String[] report=Reporter.getInstance().getAvailableWorkers().split(",");
        for (String s:report)
        result.add(s);
        return result;
    }

    private HashMap<WorkUnit,Result> assignToWorkers(WorkUnits wu, ProcessAlgorithm processAlgorithm)
    {
       WorkersList wl=WorkersList.getInstance();
        HashMap<WorkUnit,Result> resultHashMap=PublisherReporter.getInstance().sendToWorkers(wl,wu,processAlgorithm);
        return resultHashMap;
    }

    public HashMap<WorkUnit,Result> startWorking(String fileAddress, String algorithmName)
    {
        ProcessAlgorithm algorithm=null;

        if (algorithmName.equals("ProcessAlgorithmClass1.class"))
            algorithm=new ProcessAlgorithmClass1();
        else if (algorithmName.equals("ProcessAlgorithmClass2.class"))
            algorithm=new ProcessAlgorithmClass2();
        else
            algorithm=new ProcessAlgorithmClass1();

        MasterLogger.getInstance().saveLog("Start distributing work units at " + LocalDateTime.now(), Level.INFO);
        configureMasterProperties();
        HashMap<WorkUnit,Result> resultHashMap=assignToWorkers(splitMainFile(fileAddress), algorithm);
        return resultHashMap;
    }
    public void configureMasterProperties()
    {
        Properties properties=new Properties();
        properties.setProperty("algorithmsClassFilePath","D:\\Java-109\\Assignments\\master\\out\\production\\master\\algorithm\\");
        properties.setProperty("splitsPath","D:\\WorkUnits\\");
        properties.setProperty("fileHome","D:\\");
        File f=new File("master.properties");
        try(FileOutputStream fos=new FileOutputStream(f))
        {
            properties.store(fos,"MasterProperties");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public int doIntegration(HashMap<WorkUnit,Result> resultHashMap)
    {
        ArrayList<Integer> numList=new ArrayList<>();
        int finalSum=0;
        for (Result r:resultHashMap.values())
        {
            numList.add(r.getFinalResult());
        }
        finalSum=Integrator.getInstance().integrate(numList);
        return finalSum;
    }
}
