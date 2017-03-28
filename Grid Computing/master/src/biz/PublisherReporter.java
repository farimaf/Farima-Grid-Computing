package biz;

/**
 * Created by Farima on 7/20/2016.
 */

import algorithm.ProcessAlgorithm;
import com.sun.corba.se.impl.orbutil.concurrent.Sync;
import network.SenderReceiver;
import persist.WorkunitPersistance;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A singletone class because it works as a utility class
 * **/
public class PublisherReporter {

    private WorkunitPersistance workunitPersistance=new WorkunitPersistance();
    private static PublisherReporter instance=null;

    private PublisherReporter(){};

    public static PublisherReporter getInstance()
    {
        if (instance==null) instance=new PublisherReporter();
        return instance;
    }

    public HashMap<WorkUnit,Result> sendToWorkers(WorkersList workersList,WorkUnits workUnits, ProcessAlgorithm processAlgorithm)
    {
        HashMap<WorkUnit,Result> resultSet=new HashMap<>();
        while (workUnits.hasNotDoneUnit())
        {
            for (WorkUnit workUnit:workUnits)
            {
                if (!workUnit.isDone()&&!workUnit.isInProcess())
                {
                    if(workersList.hasFreeWorker())
                    {
                        Worker w=workersList.useWorker();
                        workunitPersistance.updateAssignedWorker(workUnit, w);
                        Thread t=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                resultSet.put(workUnit,SenderReceiver.getInstance().setAndReceiveWorkUnitToFromWorker(w, workUnit, processAlgorithm));
                            }
                        });
                       t.start();
                        workUnit.setIsInProcess(true);
                    }

                }
                if(((Result)resultSet.get(workUnit))!=null && workUnits.hasNotDoneUnit())
                {
                    workUnit.setIsDone(true);
                    workunitPersistance.updateIsDone(workUnit, true);
                    workunitPersistance.updateProcessTime(workUnit,((Result)resultSet.get(workUnit)).getProcessTime());
                    workUnit.setIsInProcess(false);
                    //System.out.println(workUnit.getName());
                    if(workersList.hasInUseWorker()) {
                    ArrayList<Worker> toBeRemoved=new ArrayList<>();

                        for (Worker worker : workersList.getInUseWorkerList()) {
                            if (Integer.toString(worker.getPort()).equals(((Result) resultSet.get(workUnit)).getWorkerName())) {
                                toBeRemoved.add(worker);
                            }
                        }
                        workersList.freeWorkers(toBeRemoved);
                    }
                }
            }
        }

    return resultSet;
    }


}
