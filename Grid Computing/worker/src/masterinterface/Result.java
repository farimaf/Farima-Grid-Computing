package masterinterface;

/**
 * Created by Farima on 7/24/2016.
 */
public class Result {
    private int finalResult;
    private long processTime;
    private String workerName;

    public Result(int finalResult, long processTime, String workerName) {
        this.finalResult = finalResult;
        this.processTime = processTime;
        this.workerName = workerName;
    }

    public int getFinalResult() {
        return finalResult;
    }

    public void setFinalResult(int finalResult) {
        this.finalResult = finalResult;
    }

    public double getProcessTime() {
        return processTime;
    }

    public void setProcessTime(long processTime) {
        this.processTime = processTime;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }
}
