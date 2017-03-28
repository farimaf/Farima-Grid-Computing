package biz;

import javax.persistence.*;
/**
 * Created by Farima on 7/20/2016.
 */
@Entity
@Table(name = "workunits")
public class WorkUnit {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="wu_id")
    private int id;
    @Column(name = "wu_name")
    private String name;
    @Column(name = "wu_isDone")
    private boolean isDone;
    @Transient
    private boolean isInProcess;
    @Column(name = "wu_processTime")
    private long processTime;
    @JoinColumn(name = "wu_w_id")
    @OneToOne
    private Worker worker;

    public int getId() {
        return id;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public WorkUnit(){}

    public WorkUnit(String name, boolean isDone, boolean isInProcess) {
        this.name = name;
        this.isDone = isDone;
        this.isInProcess=isInProcess;
    }

    public long getProcessTime() {
        return processTime;
    }

    public void setProcessTime(long processTime) {
        this.processTime = processTime;
    }

    public boolean isInProcess() {
        return isInProcess;
    }

    public void setIsInProcess(boolean isInProcess) {
        this.isInProcess = isInProcess;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    @Override
    public boolean equals(Object obj) {
        return ((WorkUnit)obj).getName().equals(this.getName());
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }
}
