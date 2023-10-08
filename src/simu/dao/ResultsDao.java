package simu.dao;

import datasource.MariaDbJpaConnection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import simu.entity.Results;

import java.util.List;

public class ResultsDao {

    public void persist(Results results) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(results);
        em.getTransaction().commit();
    }

    public Results update(Results results) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.merge(results);
        em.getTransaction().commit();
        return results;
    }

    public void delete(Results results) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.remove(results);
        em.getTransaction().commit();
    }

    public List<Results> getAll() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        TypedQuery<Results> query = em.createQuery("SELECT c FROM Results c", Results.class);
        List<Results> results = query.getResultList();
        return results;
    }

    public Results find(int id) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        Results results = em.find(Results.class, id);
        return results;
    }
}
