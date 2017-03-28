package persist;

import biz.WorkUnit;
import biz.Worker;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Properties;

/**
 * Created by Farima on 7/25/2016.
 */
public class WorkunitPersistance {
    EntityManager entityManager;
    WorkUnit workUnit;

    public WorkunitPersistance() {

        Properties props = new Properties();
        props.put("eclipselink.persistencexml", "META-INF/persistence.xml");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myJPA", props);
        this.entityManager = emf.createEntityManager();
    }

    public void addWorkunit(WorkUnit w) {

        entityManager.getTransaction().begin();
        entityManager.persist(w);
        entityManager.flush();

        entityManager.getTransaction().commit();
    }

    public void updateProcessTime(WorkUnit workunit, long processTime) {
        entityManager.getTransaction().begin();
        WorkUnit workUnit = (WorkUnit) entityManager.find(WorkUnit.class, workunit.getId());
        workUnit.setProcessTime(processTime);
        entityManager.getTransaction().commit();
    }

    public void updateIsDone(WorkUnit workunit, boolean isDone) {
        entityManager.getTransaction().begin();
        WorkUnit workUnit = (WorkUnit) entityManager.find(WorkUnit.class, workunit.getId());
        workUnit.setIsDone(isDone);
        entityManager.getTransaction().commit();
    }

    public void updateAssignedWorker(WorkUnit workunit, Worker worker)
    {
        entityManager.getTransaction().begin();
        WorkUnit workUnit = (WorkUnit) entityManager.find(WorkUnit.class, workunit.getId());
        workUnit.setWorker(worker);
        entityManager.flush();
        entityManager.getTransaction().commit();

    }
}
