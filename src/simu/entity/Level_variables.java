package simu.entity;


import jakarta.persistence.*;

/**
 * Tämä on Level_variables luokka joka sisältää Level_variables olion ominaisuudet ja metodit.
 */
@Entity
public class Level_variables {
    /**
     * @param id on Level_variables olion id.
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    /**
     * @param levelId on Results luokan olio joka on Level_variables olion yksi ominaisuus.
     *                Tämä ominaisuus on yhteydessä Results luokan olioon sillä tavalla että,
     *                Results olion kanssa voi olla moni Level_variables olio sidottuna.
     */
    @ManyToOne
    private Results levelId;

    /**
     * @param servicePointName on Level_variables olion yksi ominaisuus tähän laitetaan palvelupisteen nimi.
     */
    private String servicePointName;

    /**
     * @param mean1 on Level_variables olion yksi ominaisuus joka ottaa keskiarvon itselleen.
     */
    private double mean1;

    /**
     * @param mean2 on Level_variables olion yksi ominaisuus joka ottaa keskiarvon itelleen.
     */
    private double mean2;

    /**
     * @param variance1 on Level_variables olion yksi ominaisuus joka ottaa vaihtelevuuden itelleen.
     */
    private double variance1;

    /**
     * @param variance2 on Level_variables olion yksi ominaisuus joka ottaa vaihtelevuuden itelleen.
     */
    private double variance2;

    /**
     * @param carCount on Level_variables olion yksi ominaisuus joka ottaa autojen määrän.
     */
    private int carCount;

    /**
     * @param averageTime on Level_variables olion yksi ominaisuus joka ottaa keskimääräisen ajan.
     */
    private double averageTime;

    /**
     * @param maxQueueSize on Level_variables olion yksi ominaisuus joka ottaa jonon maksimikoon.
     */
    private int maxQueueSize;

    /**
     * @param levelVariablesModel on LevelVariablesModel luokan olio joka on Level_variables olion yksi ominaisuus.
     */
    @Transient
    private LevelVariablesModel levelVariablesModel;

    /**
     * Tätä konstruktoria käytetään palvelupisteen luomisen yhteydessä.
     * @param levelId Results olion id joka on Level_variables olion yksi ominaisuus.
     * @param servicePointName palvelupisteen nimi.
     * @param mean1 keskiarvo.
     * @param mean2 keskiarvo.
     * @param variance1 vaihtelevuus.
     * @param variance2 vaihtelevuus.
     * @param carCount autojen määrä.
     * @param maxQueueSize jonon maksimikoko.
     */
    public Level_variables(Results levelId, String servicePointName, double mean1, double mean2, double variance1, double variance2, int carCount, int maxQueueSize) {
        this.carCount = carCount;
        this.levelId = levelId;
        this.servicePointName = servicePointName;
        this.mean1 = mean1;
        this.mean2 = mean2;
        this.variance1 = variance1;
        this.variance2 = variance2;
        this.maxQueueSize = maxQueueSize;
        this.levelVariablesModel = new LevelVariablesModel(this);
    }

    /**
     * Tätä konstruktoria käytetään maalin ja lähtöpisteen luomiseen.
     * @param levelId Results olion id joka on Level_variables olion yksi ominaisuus.
     * @param servicePointName palvelupisteen nimi.
     * @param carCount autojen määrä.
     * @param averageTime keskimääräinen aika.
     */
    public Level_variables(Results levelId, String servicePointName, int carCount, double averageTime) {
        this.averageTime = averageTime;
        this.carCount = carCount;
        this.levelId = levelId;
        this.servicePointName = servicePointName;
        this.levelVariablesModel = new LevelVariablesModel(this);
    }

    /**
     * Vaadittu konstruktori jotta Level_variables luokka toimisi Entity luokkana.
     */
    public Level_variables() {
    }

    /**
     * @return palauttaa Level_variables olion id:n.
     */
    public Results getLevelId() {
        return levelId;
    }

    /**
     * @param levelId asettaa Level_variables olion id:n.
     */
    public void setLevelId(Results levelId) {
        this.levelId = levelId;
    }

    /**
     * @return palauttaa palvelupisteen nimen.
     */
    public String getServicePointName() {
        return servicePointName;
    }

    /**
     * @param servicePointName asettaa palvelupisteen nimen.
     */
    public void setServicePointName(String servicePointName) {
        this.servicePointName = servicePointName;
    }

    /**
     * @return palauttaa ensimmäisen keskiarvon.
     */
    public double getMean1() {
        return mean1;
    }

    /**
     * @param mean asettaa ensimmäisen keskiarvon.
     */
    public void setMean1(double mean) {
        this.mean1 = mean;
    }

    /**
     * @return palauttaa toisen keskiarvon.
     */
    public double getVariance1() {
        return variance1;
    }

    /**
     * @param variance asettaa toisen keskiarvon.
     */
    public void setVariance1(double variance) {
        this.variance1 = variance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LevelVariablesModel getLevelVariablesModel() {
        return levelVariablesModel;
    }

    public int getCarCount() {
        return carCount;
    }

    public void setCarCount(int carCount) {
        this.carCount = carCount;
    }

    public double getAverageTime() {
        return averageTime;
    }

    public void setAverageTime(double averageTime) {
        this.averageTime = averageTime;
    }

    public double getMean2() {
        return mean2;
    }

    public void setMean2(double mean2) {
        this.mean2 = mean2;
    }

    public double getVariance2() {
        return variance2;
    }

    public void setVariance2(double variance2) {
        this.variance2 = variance2;
    }

    public int getMaxQueueSize() {
        return maxQueueSize;
    }

    public void setMaxQueueSize(int maxQueueSize) {
        this.maxQueueSize = maxQueueSize;
    }
}
