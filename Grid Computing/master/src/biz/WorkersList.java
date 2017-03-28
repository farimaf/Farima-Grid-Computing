package biz;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Farima on 7/16/2016.
 */
/**
 * A singletone class because one list of workers is needed and it cant be more than this
 * **/
public class WorkersList implements Iterable<Worker> {

    private static WorkersList instance=null;
    private ArrayList<Worker> freeWorkerList=new ArrayList<Worker>();
    private ArrayList<Worker> inUseWorkerList=new ArrayList<Worker>();

    private WorkersList(){};

    public static WorkersList getInstance()
    {
        if (instance==null) instance=new WorkersList();
        return instance;
    }

    public boolean hasFreeWorker()
    {
        return !freeWorkerList.isEmpty();
    }

    public boolean hasInUseWorker()
    {
        return !inUseWorkerList.isEmpty();
    }

    public void addToList(Worker w)
    {
        freeWorkerList.add(w);
    }

    public void removeWorker(Worker w)
    {
        if (freeWorkerList.contains(w))
            freeWorkerList.remove(w);
    }

    public void removeFromList(Worker w)
    {
        freeWorkerList.remove(w);
    }

    public int getTotalWorkersCount()
    {
        return freeWorkerList.size()+inUseWorkerList.size();
    }

    public int getFreeWorkersCount()
    {
        return freeWorkerList.size();
    }

    public int getInUseWorkersCount()
    {
        return inUseWorkerList.size();
    }

    public Worker useWorker(){
        Worker w=null;
        if(!freeWorkerList.isEmpty())
        {
            w=freeWorkerList.get(0);
            inUseWorkerList.add(w);
            freeWorkerList.remove(w);
        }
        return w;
    }

    public void freeWorkers(ArrayList<Worker> w)
    {
        for(Worker worker:w){
        if (inUseWorkerList.contains(worker)) {
            inUseWorkerList.remove(worker);
            freeWorkerList.add(worker);
        }
    }
    }

    public ArrayList<Worker> getInUseWorkerList()
    {
        if(!inUseWorkerList.isEmpty())
        return inUseWorkerList;
        else return null;
    }

    public ArrayList<Worker> getFreeWorkerList()
    {
        if(!freeWorkerList.isEmpty())
            return freeWorkerList;
        else return null;
    }

    public void fillFreeWorkers(ArrayList<Worker> workers)
    {
        freeWorkerList=workers;
    }

    @Override
    public Iterator<Worker> iterator() {
        return freeWorkerList.iterator();
    }

//    public Iterator<Worker> iteratorInUse() {
//        return inUseWorkerList.iterator();
//    }
}
