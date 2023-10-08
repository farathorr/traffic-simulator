package simu.model;


import controller.Controller;
import controller.IControllerForM;
import eduni.distributions.Normal;
import simu.framework.ArrivalProcess;
import simu.framework.EventList;

public class LevelController {
    private IControllerForM controller;
    private EventList eventList;

    public LevelController(IControllerForM controller, EventList eventList) {
        this.controller = controller;
        this.eventList = eventList;
    }

    public Level getLevel(int num) {
        return switch (num) {
            case -123 -> getDebugLevel();
            case 1 -> getLevel1();
            case 2 -> getLevel2();
            default -> null;
        };
    }

    public Level getDebugLevel() {
        Level level = new Level();
//        level.arrival(new ArrivalProcess(new Normal(5, 5), eventList, "ARR1", 0, 0), "road0_0");

        level.add(new Road(new Normal(5, 3), eventList, "road0_0"), "road0_1");
        level.add(new Road(new Normal(5, 3), eventList, "road0_1"), "road0_2");
        level.add(new Road(new Normal(5, 3), eventList, "road0_2"), "road0_3");
        level.add(new Road(new Normal(5, 3), eventList, "road0_3"), "road0_4");
        level.add(new Road(new Normal(5, 3), eventList, "road0_4"), "crosswalk1_1");

        controller.render(level, "road0_0", 0, 0, "t-intersection-right");
        controller.render(level, "road0_1", 0, 1, "t-intersection-left");
        controller.render(level, "road0_2", 0, 2, "t-intersection-top");
        controller.render(level, "road0_3", 0, 3, "t-intersection-bottom");

        level.add(new Crosswalk(new Normal(5, 2), new Normal(10, 5), eventList, "crosswalk1_0"), "crosswalk1_1");
        level.add(new Crosswalk(new Normal(5, 2), new Normal(10, 5), eventList, "crosswalk1_1"), "crosswalk1_2");
        level.add(new Crosswalk(new Normal(5, 2), new Normal(10, 5), eventList, "crosswalk1_2"), "crosswalk1_3");
        level.add(new Crosswalk(new Normal(5, 2), new Normal(10, 5), eventList, "crosswalk1_3"));

        controller.render(level, "crosswalk1_0", 1, 0, "right");
        controller.render(level, "crosswalk1_1", 1, 1, "left");
        controller.render(level, "crosswalk1_2", 1, 2, "top");
        controller.render(level, "crosswalk1_3", 1, 3, "bottom");

        return level;
    }
    public Level getLevel1() {
        Level level = new Level();
        level.arrival(new ArrivalProcess(new Normal(5, 5), eventList, "ARR1", 0, 0), "road0_0");
        level.add(new Road(new Normal(5, 3), eventList, "road0_0"), "crosswalk1_0");
        level.add(new Crosswalk(new Normal(1, 2), new Normal(50, 5), eventList, "crosswalk1_0"), "road1_1");
        level.add(new Road(new Normal(5, 3), eventList, "road1_1"), "trafficlights1_2");
        level.add(new TrafficLights(new Normal(2, 1), new Normal(50, 1), eventList, "trafficlights1_2"), "road1_3");
        level.add(new Road(new Normal(5, 3), eventList, "road1_3"), new String[]{"road1_4", "road2_3"});
        level.add(new Road(new Normal(5, 3), eventList, "road2_3"), "crosswalk3_3");
        level.add(new Road(new Normal(5, 3), eventList, "road1_4"));
        level.add(new Crosswalk(new Normal(5, 2), new Normal(10, 5), eventList, "crosswalk3_3"), "road4_3");
        level.add(new Road(new Normal(5, 3), eventList, "road4_3"), "roundabout_right4_2");

        level.add(new Roundabout(new Normal(5, 1), new Normal(0, 3), eventList, "roundabout_right4_2", 3), "roundabout_top5_2");
        level.add(new Roundabout(new Normal(5, 1), new Normal(0, 3), eventList, "roundabout_top5_2", 3), "roundabout_left5_1");
        level.add(new Roundabout(new Normal(5, 1), new Normal(0, 3), eventList, "roundabout_left5_1", 3), "roundabout_bottom4_1");
        level.add(new Roundabout(new Normal(5, 1), new Normal(0, 3), eventList, "roundabout_bottom4_1", 3), "roundabout_right4_2");


        controller.render(level, "road0_0", 0, 0, "right");
        controller.render(level, "crosswalk1_0", 1, 0, "right");
        controller.render(level, "road1_1", 1, 1, "bottom");
        controller.render(level, "trafficlights1_2", 1, 2, "bottom");
        controller.render(level, "road2_3", 2, 3, "right");
        controller.render(level, "road1_3", 1, 3, "t-intersection-right");
        controller.render(level, "road1_4", 1, 4, "bottom");
        controller.render(level, "crosswalk3_3", 3, 3, "right");
        controller.render(level, "road4_3", 4, 3, "right");
        controller.render(level, "roundabout_right4_2", 4, 2, "right-road");
        controller.render(level, "roundabout_top5_2", 5, 2, "top-road");
        controller.render(level, "roundabout_left5_1", 5, 1, "left-road");
        controller.render(level, "roundabout_bottom4_1", 4, 1, "bottom-road");

        return level;
    }

    public Level getLevel2(){
        Level level = new Level();
        level.arrival(new ArrivalProcess(new Normal(5, 5), eventList, "ARR1", 0, 0), "road0_0");

        level.add(new Road(new Normal(5, 3), eventList, "road0_0"), "crosswalk1_0");

        level.add(new Crosswalk(new Normal(1, 2), new Normal(50, 5), eventList, "crosswalk1_0"), "road2_0");

        level.add(new Road(new Normal(5, 3), eventList, "road2_0"), "trafficlights2_1");

        level.add(new TrafficLights(new Normal(2, 1), new Normal(50, 1), eventList, "trafficlights2_1"), "road2_2");

        level.add(new Road(new Normal(5, 3), eventList, "road2_2"), "crosswalk1_2");

        level.add(new Crosswalk(new Normal(5, 2), new Normal(10, 5), eventList, "crosswalk1_2"), "road0_2");

        level.add(new Road(new Normal(5, 3), eventList, "road0_2"), "trafficlights0_3");

        level.add(new TrafficLights(new Normal(2, 1), new Normal(50, 1), eventList, "trafficlights0_3"), "road0_4");

        level.add(new Road(new Normal(5, 3), eventList, "road0_4"), "roundabout_bottom1_4");

        level.add(new Roundabout(new Normal(5, 1), new Normal(0, 3), eventList, "roundabout_bottom1_4", 3), "roundabout_right1_5");
        level.add(new Roundabout(new Normal(5, 1), new Normal(0, 3), eventList, "roundabout_right1_5", 3), "roundabout_top2_5");
        level.add(new Roundabout(new Normal(5, 1), new Normal(0, 3), eventList, "roundabout_top2_5", 3), "roundabout_left2_4");
        level.add(new Roundabout(new Normal(5, 1), new Normal(0, 3), eventList, "roundabout_left2_4", 3), new String[]{"roundabout_bottom1_4", "road3_4"});

        level.add(new Road(new Normal(5, 3), eventList, "road3_4"), "trafficlights4_4");

        level.add(new TrafficLights(new Normal(2, 1), new Normal(50, 1), eventList, "trafficlights4_4"), "road5_4");

        level.add(new Road(new Normal(5, 3), eventList, "road5_4")); //"trafficlights4_4"


        controller.render(level, "road0_0", 0, 0, "right");
        controller.render(level, "crosswalk1_0", 1, 0, "right");
        controller.render(level, "road2_0", 2, 0, "t-intersection-left");
        controller.render(level, "trafficlights2_1", 2, 1, "bottom");
        controller.render(level, "road2_2", 2, 2, "t-intersection-top");
        controller.render(level, "crosswalk1_2", 1, 2, "left");
        controller.render(level, "road0_2", 0, 2, "t-intersection-right");
        controller.render(level, "trafficlights0_3", 0, 3, "bottom");
        controller.render(level, "road0_4", 0, 4, "t-intersection-top");
        controller.render(level, "roundabout_bottom1_4", 1, 4, "bottom-road");
        controller.render(level, "roundabout_right1_5", 1, 5, "right-road");
        controller.render(level, "roundabout_top2_5", 2, 5, "top-road");
        controller.render(level, "roundabout_left2_4", 2, 4, "left-road");
        controller.render(level, "road3_4", 3, 4, "right");
        controller.render(level, "trafficlights4_4", 4, 4, "right");
        controller.render(level, "road5_4", 5, 4, "t-intersection-top");

        return level;
    }
}
