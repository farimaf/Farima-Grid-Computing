package biz;

import java.util.ArrayList;

/**
 * Created by Farima on 7/24/2016.
 */
public class Integrator {
    public static Integrator instance=null;

    private Integrator(){};

    public static Integrator getInstance()
    {
        if (instance==null) instance=new Integrator();
        return instance;
    }

    public int integrate(ArrayList<Integer> inputNums)
    {
        int sum=0;
        for (Integer i:inputNums)
        {
            sum+=i;
        }
        return sum;
    }
}
