package simu.dao;

import datasource.MariaDbJpaConnection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import simu.entity.Results;

import java.util.List;

/**
 * Tämä luokka on Results luokkaa varten tehty Dao ja tässä luokassa on metodeja joilla tallennetaan
 * Results luokan olioita titokantaan sekä haetaan titokannasta.
 */
public class ResultsDao {

    /**
     * @param results on Results luokan olio joka tallennetaan tietokantaan.
     */
    public void persist(Results results) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(results);
        em.getTransaction().commit();
    }

    /**
     * @param results on Results luokan olio joka päivitetään tietokantaan.
     * @return
     */
    public Results update(Results results) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.merge(results);
        em.getTransaction().commit();
        return results;
    }

    /**
     * @param results on Results luokan olio joka poistetaan tietokannasta.
     */
    public void delete(Results results) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.remove(results);
        em.getTransaction().commit();
    }

    /**
     * @return palauttaa listan kaikista Results olioista.
     */
    public List<Results> getAll() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        TypedQuery<Results> query = em.createQuery("SELECT c FROM Results c", Results.class);
        List<Results> results = query.getResultList();
        return results;
    }

    /**
     * @param id Etsii tietokannasta Results olion id:n perusteella.
     * @return
     */
    public Results find(int id) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        Results results = em.find(Results.class, id);
        return results;
    }
}
