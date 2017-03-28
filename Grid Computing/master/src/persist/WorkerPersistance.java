package persist;

import biz.Worker;
import biz.WorkersList;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;

/**
 * Created by Farima on 7/24/2016.
 */
public class WorkerPersistance {
    EntityManager entityManager;
    Worker worker;

    public WorkerPersistance() {

        Properties props = new Properties();
        props.put("eclipselink.persistencexml","META-INF/persistence.xml");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myJPA", props);
        this.entityManager = emf.createEntityManager();

    }

    public void saveWorker(Worker w)
    {

        if(!isWorkerExisting(w)) {
            entityManager.getTransaction().begin();
            entityManager.persist(w);
            entityManager.flush();
            entityManager.getTransaction().commit();
        }

    }

    public void removeWorker(Worker w)
    {
        if (isWorkerExisting(w))
        {
            entityManager.getTransaction().begin();
            if(!entityManager.contains(w)) w=entityManager.merge(w);
            entityManager.remove(w);
            entityManager.getTransaction().commit();
        }
    }

    private boolean isWorkerExisting(Worker w)
    {
        Worker worker = null;
        boolean result=true;
        String jpql = "select wr from Worker wr where wr.port = :port and wr.ip=:ip";
        Query q = entityManager.createQuery(jpql);
        q.setParameter("ip", w.getIp());
        q.setParameter("port", w.getPort());
        try {
            worker = (Worker) q.getSingleResult();
        } catch (Exception e) {
            result=false;
        }
        return result;
    }

    public void loadWorkers()
    {
        ArrayList<Worker> resultInArrayList=new ArrayList<>();
        Query query = entityManager.createNativeQuery("Select * FROM workers",Worker.class);
        Vector<Worker> entities= (Vector<Worker>)query.getResultList();
        for (Worker worker:entities)
        resultInArrayList.add(worker);
        WorkersList.getInstance().fillFreeWorkers(resultInArrayList);
    }

}
