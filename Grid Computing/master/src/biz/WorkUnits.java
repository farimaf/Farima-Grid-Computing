package biz;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Farima on 7/20/2016.
 */
public class WorkUnits implements Iterable<WorkUnit> {
    private static WorkUnits instance=null;

    private WorkUnits(){};

    public static WorkUnits getInstance()
    {
        if (instance==null) instance=new WorkUnits();
        return instance;
    }

    private ArrayList<WorkUnit> workUnits=new ArrayList<WorkUnit>();

    public void addWorkUnit(WorkUnit unit)
    {
        workUnits.add(unit);
    }

    public void removeWorkUnit(WorkUnit unit)
    {
        workUnits.remove(unit);
    }

    public int getLength(){return workUnits.size();}

    public boolean hasNotDoneUnit()
    {
        boolean out=false;
        for (WorkUnit wu:workUnits)
        {
            if (!wu.isDone())
            {
                out=true;
                break;
            }
        }
        return out;
    }

    @Override
    public Iterator<WorkUnit> iterator() {
        return workUnits.iterator();
    }
}
