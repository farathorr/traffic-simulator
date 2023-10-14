package datasource;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * MariaDbJpaConnection-luokka, joka luo yhteyden tietokantaan.
 * Yhteyden luomiseen käytetään JPA:ta.
 * Yhteyden luomisen jälkeen luodaan EntityManager, jolla voidaan suorittaa kyselyitä tietokantaan.
 * Luokka on singleton, joten yhteyttä ei luoda uudestaan, jos se on jo olemassa.
 */
public class MariaDbJpaConnection {
    private static EntityManagerFactory emf = null;
    private static EntityManager em = null;

    public static EntityManager getInstance() {
        if (em==null) {
            if (emf==null) {
                emf = Persistence.createEntityManagerFactory("datasource");
            }
            em = emf.createEntityManager();
        }
        return em;
    }
}
