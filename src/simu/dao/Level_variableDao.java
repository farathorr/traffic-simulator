package simu.dao;

import datasource.MariaDbJpaConnection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import simu.entity.Level_variables;

import java.util.List;

public class Level_variableDao {
    public void persist(Level_variables levelVariables) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(levelVariables);
        em.getTransaction().commit();
    }

    public Level_variables update(Level_variables levelVariables) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.merge(levelVariables);
        em.getTransaction().commit();
        return levelVariables;
    }

    public void delete(Level_variables levelVariables) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.remove(levelVariables);
        em.getTransaction().commit();
    }

    public List<Level_variables> getAll() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        TypedQuery<Level_variables> query = em.createQuery("SELECT c FROM Level_variables c", Level_variables.class);
        List<Level_variables> levelVariables = query.getResultList();
        return levelVariables;
    }

    public Level_variables find(int id) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        Level_variables levelVariables = em.find(Level_variables.class, id);
        return levelVariables;
    }

}
