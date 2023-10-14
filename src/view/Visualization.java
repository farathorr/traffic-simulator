package view;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import simu.model.*;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;

/**
 * Visualization-luokka, joka sisältää visualisointiin liittyvät metodit.
 * Perii Canvas-luokan ja toteuttaa IVisualizationForV- ja IVisualizationForM-rajapinnat.
 */
public class Visualization extends Canvas implements IVisualizationForV, IVisualizationForM {

    /**
     * GraphicsContext-olio, joka sisältää metodeja, joita käytetään piirtämiseen.
     */
    private final GraphicsContext gc;
    /**
     * x- ja y-koordinaatit.
     */
    double x = 0, y = 0;
    /**
     * Näyttää kursorin x- ja y-kordinaatit.
     */
    private int previewX = 0, previewY = 0;
    /**
     * Näyttää minkälaista palikkaa ollaan asettamassa.
     */
    private String placeTileType = "road", placeRotation = "right";
    /**
     * Ruudun leveys ja korkeus.
     */
    private int width, height;
    /**
     * Yhden gridin koko pikseleissä.
     */
    private final int gridSize = 128;
    /**
     * Oletus-zoom taso.
     */
    private double zoomLevel = 1.0;
    /**
     * Lista ServicePoint-olioista
     */
    private List<ServicePoint> servicePoints = new ArrayList<>();
    /**
     * Lista Customer-olioista
     */
    private LinkedList<Customer> customers = new LinkedList<>();
    /**
     * Lista Customer-olioista, jotka poistetaan seuraavassa renderöinnissä.
     */
    private List<Customer> removeCustomers = new ArrayList<>();
    /**
     * Kuvat, joita käytetään visualisoinnissa.
     */
    private Image roundaboutTurn = new Image("roundabout.png");
    private Image roundaboutRoad = new Image("roundabout-with-road.png");
    private Image roundaboutDouble = new Image("roundabout-double.png");
    private Image roundaboutRoad2 = new Image("roundabout-with-road2.png");
    private Image roundaboutEntrance = new Image("roundabout-entrance.png");
    private Image roundaboutEntrance2 = new Image("roundabout-entrance2.png");
    private Image trafficLightGreen = new Image("trafficlight-green.png");
    private Image trafficLightGreen2 = new Image("trafficlight-green2.png");
    private Image trafficLightRed = new Image("trafficlight-red.png");
    private Image trafficLightRed2 = new Image("trafficlight-red2.png");
    private Image crosswalkImage = new Image("crosswalk.png");
    private Image crosswalkImage2 = new Image("crosswalk2.png");
    private Image crosswalkCrossingImage = new Image("crosswalkCrossing.png");
    private Image crosswalk2CrossingImage = new Image("crosswalk2Crossing.png");
    private Image roadImage = new Image("road.png");
    private Image roadImage2 = new Image("road2.png");
    private Image roadTurn = new Image("road-turn.png");
    private Image tIntersection = new Image("t-intersection.png");
    private Image tIntersection2 = new Image("t-intersection2.png");
    private Image goal = new Image("goal.png");
    private Image start = new Image("start.png");
    private Image arrow = new Image("arrow.png");
    private Image arrow2 = new Image("arrow2.png");
    /**
     * Level-olio, jota visualisoidaan.
     */
    private Level level;


    /**
     * @param w Ruudun leveys.
     * @param h Ruudun korkeus.
     * Konstruktori, joka luo uuden Visualization-olion.
     * Konstruktori myös luo GraphicsContext-olion ja asettaa kuvan venytyksen pikselöidyksi.
     * Konstruktori myös tyhjentää ruudun.
     */
    public Visualization(int w, int h) {
        super(w, h);
        this.width = w;
        this.height = h;
        gc = this.getGraphicsContext2D();
        gc.setImageSmoothing(false);
        clearScreen();
    }


    public void reset() {
        servicePoints.clear();
        customers.clear();
        clearScreen();
    }

    /**
     * Tyhjentää ruudun.
     */
    public void clearScreen() {
        gc.clearRect(0, 0, this.getWidth(), this.getHeight());
    }

    /**
     * @param servicePoint ServicePoint-olio, joka piirretään.
     * Lisää ServicePoint-olion renderöintijonoon.
     */
    public void addToRenderQueue(ServicePoint servicePoint) {
        servicePoints.add(servicePoint);
    }
    /**
     * @param customer Customer-olio, joka lisätään jonoon.
     * Lisää Customer-olion jonoon.
     */
    public void addToCustomerQueue(Customer customer) {
        customers.add(customer);
    }

    /**
     * Metodi, joka renderöi ruutuun palvelupisteen kuvan.
     * Metodi myös renderöi ruutuun jonossa olevat asiakkaat.
     * Metodi myös poistaa jonossa olevat asiakkaat, jotka ovat valmiita poistettavaksi.
     * Metodi myös piirtää canvakselle ruudukon, jos debug-tila on päällä.
     * Metodi myös piirtää canvakselle palvelupisteiden väliset yhteydet, jos debug-tila on päällä ja käyttäjä piirtää yhdistysnuolia.
     */
    public void render() {
        Platform.runLater(() -> {
            gc.clearRect(0, 0, this.getWidth(), this.getHeight());
            servicePoints.forEach(this::renderServicePoint);

            try {
                customers.forEach(this::drawQueue);
            } catch (ConcurrentModificationException e) {
                System.out.println("Working as intended");
            }

            removeCustomers.forEach(customers::remove);
            removeCustomers.clear();

            if(Debug.getInstance().isGrid()) drawGrid();
            if(Debug.getInstance().isDebug()) {
                if(!placeTileType.equals("air")) drawPreviewServicePoint();
                if (placeTileType.equals("arrow")) renderRoadConnectionsArrows();
            }
        });
    }

    /**
     * @param servicePoint ServicePoint-olio, jota ollaan piirtämässä.
     * Metodi, joka piirtää palvelupisteen kuvan.
     * Metodi lukee palvelupisteen nimen ja rotaation ja piirtää sen mukaisen kuvan.
     * Metodi myös piirtää palvelupisteen ympärille punaisen reunuksen, jos palvelupisteellä on yhteysvirhe.
     */
    public void renderServicePoint(ServicePoint servicePoint) {
        if (servicePoint.isConnectionError()) drawErrorConnectionBorder(servicePoint.getX() * gridSize, servicePoint.getY() * gridSize);
        if (servicePoint.getClass() == Road.class) {
            switch (servicePoint.getRotation()) {
                case "right", "left" -> drawImage(roadImage, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                case "top", "bottom" -> drawImage(roadImage2, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                case "right-turn" -> drawImage(roadTurn, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                case "top-turn" -> drawImage(roadTurn, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize, -gridSize, gridSize);
                case "left-turn" -> drawImage(roadTurn, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize + gridSize, -gridSize, -gridSize);
                case "bottom-turn" -> drawImage(roadTurn, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize + gridSize, gridSize, -gridSize);
                case "t-intersection-right" -> drawImage(tIntersection2, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize, -gridSize, gridSize);
                case "t-intersection-top" -> drawImage(tIntersection, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize + gridSize, gridSize, -gridSize);
                case "t-intersection-left" -> drawImage(tIntersection2, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                case "t-intersection-bottom" -> drawImage(tIntersection, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                case "start" -> drawImage(start, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
            }
        }else if (servicePoint.getClass() == Goal.class) {
            switch(servicePoint.getRotation()){
                case "goal" -> drawImage(goal, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
            }
        }
        else if (servicePoint.getClass() == Crosswalk.class) {
                if (((Crosswalk) servicePoint).isCrossable()) {
                    switch (servicePoint.getRotation()) {
                        case "right" -> drawImage(crosswalkImage, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize, -gridSize, gridSize);
                        case "left" -> drawImage(crosswalkImage, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                        case "top" -> drawImage(crosswalkImage2, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize + gridSize, gridSize, -gridSize);
                        case "bottom" -> drawImage(crosswalkImage2, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                    }
                } else {
                    switch (servicePoint.getRotation()) {
                        case "right" -> drawImage(crosswalkCrossingImage, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize, -gridSize, gridSize);
                        case "left" -> drawImage(crosswalkCrossingImage, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                        case "top" -> drawImage(crosswalk2CrossingImage, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize + gridSize, gridSize, -gridSize);
                        case "bottom" -> drawImage(crosswalk2CrossingImage, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                    }
                }
            }


        else if (servicePoint.getClass() == Roundabout.class) {
            switch (servicePoint.getRotation()) {
                case "right" -> drawImage(roundaboutTurn, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                case "top" -> drawImage(roundaboutTurn, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize, -gridSize, gridSize);
                case "left" -> drawImage(roundaboutTurn, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize + gridSize, -gridSize, -gridSize);
                case "bottom" -> drawImage(roundaboutTurn, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize + gridSize, gridSize, -gridSize);
                case "right-double" -> drawImage(roundaboutDouble, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                case "top-double" -> drawImage(roundaboutDouble, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize, -gridSize, gridSize);
                case "left-double" -> drawImage(roundaboutDouble, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize + gridSize, -gridSize, -gridSize);
                case "bottom-double" -> drawImage(roundaboutDouble, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize + gridSize, gridSize, -gridSize);
                case "right-r-road" -> drawImage(roundaboutRoad, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                case "top-r-road" -> drawImage(roundaboutRoad2, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                case "left-r-road" -> drawImage(roundaboutRoad, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize + gridSize, -gridSize, -gridSize);
                case "bottom-r-road" -> drawImage(roundaboutRoad2, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize + gridSize, -gridSize, -gridSize);
                case "right-r-entrance" -> drawImage(roundaboutEntrance, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                case "top-r-entrance" -> drawImage(roundaboutEntrance2, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                case "left-r-entrance" -> drawImage(roundaboutEntrance, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize + gridSize, -gridSize, -gridSize);
                case "bottom-r-entrance" -> drawImage(roundaboutEntrance2, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize + gridSize, -gridSize, -gridSize);
            }
        }
        else if (servicePoint.getClass() == TrafficLights.class) {
            if(((TrafficLights) servicePoint).isGreenLight()){
                switch (servicePoint.getRotation()) {
                    case "bottom" -> drawImage(trafficLightGreen2, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                    case "top" -> drawImage(trafficLightGreen2, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize + gridSize, -gridSize, -gridSize);
                    case "left" -> drawImage(trafficLightGreen, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize, -gridSize, gridSize);
                    case "right" -> drawImage(trafficLightGreen, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize , gridSize, gridSize);
                }
            }
            else {
                switch (servicePoint.getRotation()) {
                    case "bottom" -> drawImage(trafficLightRed2, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                    case "top" -> drawImage(trafficLightRed2, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize + gridSize, -gridSize, -gridSize);
                    case "left" -> drawImage(trafficLightRed, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize, -gridSize, gridSize);
                    case "right" -> drawImage(trafficLightRed, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                }
            }
        }
    };

    /**
     * @param customer Customer-olio, joka piirretään.
     * Metodi, joka piirtää jonossa olevan asiakkaan.
     * Metodi myös poistaa jonossa olevan asiakkaan, jos se on valmis poistettavaksi.
     */
    public void drawQueue(Customer customer) {
        if (customer != null) {
            if (customer.canDelete()) removeCustomers.add(customer);
            double size = 20 * zoomLevel;
            customer.moveCustomer();
            if (customer.isFirstInQueue()) gc.setFill(Color.web("#32a852"));
            else gc.setFill(Color.web("#eb4034"));

            gc.fillOval(x + (customer.getX() * gridSize + gridSize / 2) * zoomLevel - size / 2, y + (customer.getY() * gridSize + gridSize / 2) * zoomLevel - size / 2, size, size);
        }
    }

    /**
     * Metodi yhdistysnuolien piirtämiselle.
     * Yhdistysnuolia käytetään debug-modessa levelien rakentamiseen.
     * Yhdistysnuolet kertovat servicePointeille mitkä ovat niiden seuraavat servicePointit.
     * Nuolet piirretään siihen suuntaan mihin käyttäjä on kääntänyt valitun nuolen.
     * Nuolet piirretään vain jos servicePointillä on seuraavia servicePointteja.
     */
    private void renderRoadConnectionsArrows() {
        servicePoints.forEach(point -> {
            if (!level.hasNextServicePoint(point)) return;
            ArrayList<String> nextPoints = level.getAllNextServicePoints(point);
            nextPoints.forEach((String key) -> {
                ServicePoint nextPoint = level.getServicePoint(key);
                if(point.getX() - nextPoint.getX() < 0) drawImage(arrow, point.getX() * gridSize + gridSize, point.getY() * gridSize, -gridSize, gridSize);
                else if(point.getX() - nextPoint.getX() > 0) drawImage(arrow, point.getX() * gridSize, point.getY() * gridSize, gridSize, gridSize);
                else if(point.getY() - nextPoint.getY() > 0) drawImage(arrow2, point.getX() * gridSize, point.getY() * gridSize + gridSize, gridSize, -gridSize);
                else if(point.getY() - nextPoint.getY() < 0) drawImage(arrow2, point.getX() * gridSize, point.getY() * gridSize, gridSize, gridSize);
            });
        });

        switch (placeRotation) {
            case "right" -> drawImage(arrow, previewX * gridSize + gridSize, previewY * gridSize, -gridSize, gridSize);
            case "left" -> drawImage(arrow, previewX * gridSize, previewY * gridSize, gridSize, gridSize);
            case "top" -> drawImage(arrow2, previewX * gridSize, previewY * gridSize + gridSize, gridSize, -gridSize);
            case "bottom" -> drawImage(arrow2, previewX * gridSize, previewY * gridSize, gridSize, gridSize);
        }
    }

    /**
     * @param x Palvelupisteen x-koordinaatti.
     * @param y Palvelupisteen y-koordinaatti.
     * Metodi, joka piirtää punaisen reunuksen palvelupisteen ympärille.
     * Metodi piirtää reunuksen vain jos palvelupisteellä on yhteysvirhe.
     */
    public void testServicePointForConnectionErrors(int x, int y) {
        ServicePoint servicePoint = getServicePointByCordinates(x, y);
        if (servicePoint != null) servicePoint.setConnectionError(false);
        if (level.hasNextServicePoint(servicePoint)) {
            ArrayList<String> points = level.getAllNextServicePoints(servicePoint);
            for (String point : points) {
                ServicePoint nextPoint = level.getServicePoint(point);
                if (nextPoint == null) {
                    servicePoint.setConnectionError(true);
                    break;
                }
            }
        }
    }

    /**
     * @param img Kuva, joka piirretään.
     * @param x Kuvan x-koordinaatti.
     * @param y Kuvan y-koordinaatti.
     * @param w Kuvan leveys.
     * @param h Kuvan korkeus.
     * Metodi, joka piirtää kuvan canvakselle.
     */
    public void drawImage(Image img, double x, double y, double w, double h) {
        gc.drawImage(img, this.x + x * zoomLevel, this.y + y * zoomLevel, w * zoomLevel, h * zoomLevel);
    }

    /**
     * Metodi joka piirtää kordinaatiston koko canvakselle.
     * Metodi myös piirtää kordinaatiston numerot, jos zoom-taso on tarpeeksi lähellä.
     */
    public void drawGrid() {
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1.0);
        gc.setFont(gc.getFont().font(16));

        double grid = gridSize * zoomLevel;
        int width = (int)(this.width / grid) + 4;
        int height = (int)(this.height / grid) + 4;

        for(int i = -1; i < width; i+=2) {
            double x = this.x % grid + i * grid;
            double y = this.y % grid - grid;
            gc.strokeRect(x, y, grid, grid * height);
        }
        for(int j = -1; j < height; j+=2) {
            double x = this.x % grid - grid;
            double y = this.y % grid + j * grid;
            gc.strokeRect(x, y, grid * width, grid);
        }

        if (zoomLevel > 0.35) {
            gc.setLineWidth(2.0);
            for(int i = -1; i < width; i++) {
                for(int j = -1; j < height; j++) {
                    double x = this.x % grid + i * grid;
                    double y = this.y % grid + j * grid;
                    gc.setStroke(Color.BLACK);
                    String text = String.format("%d, %d", i - (int)((this.x - this.x % grid) / grid), j - (int)((this.y - this.y % grid) / grid));
                    gc.setStroke(Color.WHITE);
                    gc.strokeText(text, x + 2, y + grid - 2);
                    gc.fillText(text, x + 2, y + grid - 2);
                }
            }
        }
    }

    /**
     * Metodi joka piirtää debug-modessa valitun servicePointin kuvan kursorin kohdalle.
     */
    public void drawPreviewServicePoint() {
        if (placeTileType.equals("arrow")) return;
        ServicePoint servicePoint = generateNewServicePoint(previewX, previewY, placeTileType, placeRotation);
        renderServicePoint(servicePoint);
    }

    /**
     * @param x Kursorin x-koordinaatti.
     * @param y Kursorin y-koordinaatti.
     * @param tileType Palvelupisteen tyyppi.
     * @param rotation Palvelupisteen rotaatio.
     * Metodi, joka luo uuden palvelupisteen kursorin kohdalle.
     * Metodi myös tarkistaa onko palvelupisteellä yhteysvirheitä sen ympärillä oleviin palvelupisteisiin.
     */
    public void createNewServicePoint(int x, int y, String tileType, String rotation) {
        for(int i = 0; i < servicePoints.size(); i++) {
            if(servicePoints.get(i).getX() == x && servicePoints.get(i).getY() == y) {
                ServicePoint servicePoint = generateNewServicePoint(x, y, tileType, rotation);
                level.remove(servicePoints.get(i));
                level.add(servicePoint);
                servicePoints.set(i, servicePoint);
                testServicePointForConnectionErrors(x - 1, y);
                testServicePointForConnectionErrors(x + 1, y);
                testServicePointForConnectionErrors(x, y - 1);
                testServicePointForConnectionErrors(x, y + 1);
                return;
            }
        }

        ServicePoint servicePoint = generateNewServicePoint(x, y, tileType, rotation);
        level.add(servicePoint);
        servicePoints.add(servicePoint);
    }

    /**
     * Metodi jolla voi valita valmiiksi piirretystä palvelupisteestä samansuuntaisen ja näköisen palvelupisteen.
     * Jos kordinaatissa ei ole mitään, valitaan ilma-ruutu.
     */
    public void pickATileInfo() {
        ServicePoint servicePoint = getServicePointByCordinates(previewX, previewY);
        if (servicePoint != null) {
            // Regex to remove all the numbers and underscores from the string
            placeTileType = servicePoint.getScheduledEventType().replaceAll("[0-9_]", "");
            System.out.println(placeTileType);
            if (servicePoint.getRotation().contains("right")) placeRotation = "right";
            else if (servicePoint.getRotation().contains("left")) placeRotation = "left";
            else if (servicePoint.getRotation().contains("top")) placeRotation = "top";
            else if (servicePoint.getRotation().contains("bottom")) placeRotation = "bottom";
            else placeRotation = "right";
        } else placeTileType = "air";
    }

    /**
     * @param x Kursorin x-koordinaatti.
     * @param y Kursorin y-koordinaatti.
     * @param rotate Yhdistysnuolen suunta.
     * Metodi, joka luo yhdistysnuolen kursorin kohdalle.
     * Metodi päivittää palvelupisteen seuraavan pisteen nuolen osoittamaan suuntaan.
     */
    public void createServicePointConnection(int x, int y, String rotate) {
        ServicePoint servicePoint = getServicePointByCordinates(x, y);
        ServicePoint nextServicePoint = getServicePointByCordinates(x + (rotate.equals("right") ? 1 : rotate.equals("left") ? -1 : 0), y + (rotate.equals("top") ? -1 : rotate.equals("bottom") ? 1 : 0));
        if(servicePoint != null && nextServicePoint != null) {
            ArrayList<String> points = new ArrayList<>();
            if (level.hasNextServicePoint(servicePoint)) {
                points = level.getAllNextServicePoints(servicePoint);
                if (points.contains(nextServicePoint.getScheduledEventType())) return;
                points.add(nextServicePoint.getScheduledEventType());
            } else points.add(nextServicePoint.getScheduledEventType());

            level.getNextPoints().put(servicePoint, points);
        }
    }

    /**
     * @param x Kursorin x-koordinaatti.
     * @param y Kursorin y-koordinaatti.
     * @return Palauttaa ServicePoint-olion, jos sellainen löytyy kordinaateista.
     * Metodi, joka palauttaa ServicePoint-olion kursorin kohdalta.
     * Metodi palauttaa null, jos kordinaateissa ei ole mitään.
     */
    public ServicePoint getServicePointByCordinates(int x, int y) {
        for(int i = 0; i < servicePoints.size(); i++) {
            if(servicePoints.get(i).getX() == x && servicePoints.get(i).getY() == y) {
                return servicePoints.get(i);
            }
        }
        return null;
    }

    /**
     * @param x Kursorin x-koordinaatti.
     * @param y Kursorin y-koordinaatti.
     * @param tileType Palvelupisteen tyyppi.
     * @param rotation Palvelupisteen rotaatio.
     * @return Palauttaa uuden ServicePoint-olion.
     * Metodi, joka luo uuden ServicePoint-olion joka noudattaa debug-modessa valittuja parametrejä.
     * Metodi palauttaa oletuksena Road-olion, jos parametrit eivät täsmää.
     */
    private ServicePoint generateNewServicePoint(int x, int y, String tileType, String rotation) {
        return switch(tileType) {
            case "road" -> {
                Road road = new Road(null, "road" + x + "_" + y);
                road.render(x, y, rotation);
                yield road;
            }
            case "road-turn" -> {
                Road road = new Road(null, "road-turn" + x + "_" +y);
                road.render(x,y,rotation+"-turn");
                yield road;
            }
            case "t-intersection" -> {
                Road road = new Road(null, "t-intersection" + x + "_" +y);
                road.render(x, y, "t-intersection-"+rotation);
                yield road;
            }
            case "crosswalk" -> {
                Crosswalk crosswalk = new Crosswalk(5, 5, 10, 10,null, "crosswalk" + x + "_" + y);
                crosswalk.render(x, y, rotation);
                yield crosswalk;
            }
            case "traffic-lights" -> {
                TrafficLights trafficLights = new TrafficLights(20, 5,10, 5, null, "traffic-lights" + x + "_" + y);
                trafficLights.render(x, y, rotation);
                yield trafficLights;
            }
            case "roundabout" -> {
                Roundabout roundabout = new Roundabout(null, "roundabout" + x + "_" + y);
                roundabout.render(x, y, rotation);
                yield roundabout;
            }
            case "roundabout-entrance" -> {
                Roundabout roundabout = new Roundabout(null, "roundabout-entrance" + x + "_" + y);
                roundabout.render(x, y, rotation+"-r-entrance");
                yield roundabout;
            }
            case "roundabout-road" -> {
                Roundabout roundabout = new Roundabout(null,"roundabout-road" + x + "_" + y);
                roundabout.render(x,y,rotation+"-r-road");
                yield roundabout;
            }
            case "roundabout-double" -> {
                Roundabout roundabout = new Roundabout(null,"roundabout-double" + x + "_" + y);
                roundabout.render(x,y,rotation+"-double");
                yield roundabout;
            }
            case "goal" -> {
                Goal goal = new Goal(null, "goal" + x + "_" + y);
                goal.render(x, y, "goal");
                yield goal;
            }
            case "start" -> {
                Road start = new Road(null, "start" + x + "_" + y);
                start.render(x, y, "start");
                yield start;
            }
            default -> {
                Road road = new Road(null, "road" + x + "_" + y);
                road.render(x, y, rotation);
                yield road;
            }
        };
    }

    /**
     * Metodi joka lukee canvakselta kaikki palvelupisteet ja niiden yhteydet.
     * Metodi tulostaa sitten kaikki palvelupisteet ja niiden yhteydet koodina konsoliin.
     */
    public void exportSelectedLevel() {
        for(ServicePoint servicePoint : servicePoints) {
            if (!servicePoint.getRotation().equals("start")) continue;

            System.out.printf("level.arrival(new ArrivalProcess(new Normal(5, 5), eventList, \"%s\", %.0f, %.0f), \"%s\");\n",
                    "ARR-" + servicePoint.getScheduledEventType(),
                    servicePoint.getX(),
                    servicePoint.getY(),
                    servicePoint.getScheduledEventType()
            );
        }

        System.out.println();

        servicePoints.forEach(ServicePoint::displayClass);
        System.out.println();
        servicePoints.forEach(ServicePoint::displayClassRender);
    }

    /**
     * @param scaleX Palvelupisteen x-koordinaatti.
     * @param scaleY Palvelupisteen y-koordinaatti.
     * Metodi, joka poistaa palvelupisteen parametreissä määriteltyjen kordinaattien kohdalta.
     */
    public void deleteServicePoint(int scaleX, int scaleY) {
        ServicePoint servicePoint = getServicePointByCordinates(scaleX, scaleY);
        if(servicePoint != null) {
            level.remove(servicePoint);
            servicePoints.remove(servicePoint);
            testServicePointForConnectionErrors(scaleX - 1, scaleY);
            testServicePointForConnectionErrors(scaleX + 1, scaleY);
            testServicePointForConnectionErrors(scaleX, scaleY - 1);
            testServicePointForConnectionErrors(scaleX, scaleY + 1);
        }
    }

    /**
     * @param x X-koordinaatti.
     * @param y Y-koordinaatti.
     * Metodi, joka piirtää punaisen reunuksen valittuun kordinaattiin.
     */
    private void drawErrorConnectionBorder(double x, double y) {
        gc.setStroke(Color.RED);
        gc.setLineWidth(5.0);
        double grid = gridSize * zoomLevel;
        gc.strokeRect(this.x + x * zoomLevel, this.y + y * zoomLevel, grid, grid);
    }
    public void setLevel(Level level) {
        this.level = level;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(double zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

    public String getPlaceTileType() {
        return placeTileType;
    }

    public void setPlaceTileType(String placeTileType) {
        this.placeTileType = placeTileType;
    }

    public String getPlaceRotation() {
        return placeRotation;
    }

    public void setPlaceRotation(String placeRotation) {
        this.placeRotation = placeRotation;
    }

    public void setPreviewX(int previewX) {
        this.previewX = previewX;
    }

    public void setPreviewY(int previewY) {
        this.previewY = previewY;
    }


    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getGridSize() {
        return gridSize;
    }
}
