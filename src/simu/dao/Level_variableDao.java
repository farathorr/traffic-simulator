package simu.dao;

import datasource.MariaDbJpaConnection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import simu.entity.Level_variables;

import java.util.List;


/**
 * Tämä luokka on Level_variables luokkaa varten tehty Dao ja tässä luokassa on metodeja joilla tallennetaan
 * Level_variables luokan olioita titokantaan sekä haetaan titokannasta.
 */
public class Level_variableDao {

    /**
     * @param levelVariables on Level_variables luokan olio joka tallennetaan tietokantaan.
     */
    public void persist(Level_variables levelVariables) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(levelVariables);
        em.getTransaction().commit();
    }

    /**
     * @param levelVariables on Level_variables luokan olio joka päivitetään tietokantaan.
     * @return palauttaa päivitetyn Level_variables olion.
     */
    public Level_variables update(Level_variables levelVariables) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.merge(levelVariables);
        em.getTransaction().commit();
        return levelVariables;
    }

    /**
     * @param levelVariables on Level_variables luokan olio joka poistetaan tietokannasta.
     */
    public void delete(Level_variables levelVariables) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.remove(levelVariables);
        em.getTransaction().commit();
    }

    /**
     * @return palauttaa listan kaikista Level_variables olioista.
     */
    public List<Level_variables> getAll() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        TypedQuery<Level_variables> query = em.createQuery("SELECT c FROM Level_variables c", Level_variables.class);
        List<Level_variables> levelVariables = query.getResultList();
        return levelVariables;
    }

    /**
     * @param id Etsii tietokannasta Level_variables olion id:n perusteella.
     * @return
     */
    public Level_variables find(int id) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        Level_variables levelVariables = em.find(Level_variables.class, id);
        return levelVariables;
    }

}
