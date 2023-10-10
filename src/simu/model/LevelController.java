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

    public Level getLevel(String levelKey) {
        return switch (levelKey) {
            case "DEBUG world" -> getDebugLevel();
            case "Level 1" -> getLevel1();
            case "Level 2" -> getLevel2();
            case "Level 3" -> getLevel3();
            case "Level 4" -> getLevel4();
            case "Level 5" -> getLevel5();
            default -> null;
        };
    }

    public Level getDebugLevel() {
        Level level = new Level("DEBUG world");
//        level.arrival(new ArrivalProcess(new Normal(5, 5), eventList, "ARR1", 0, 0), "road0_0");

        level.add(new Road(eventList, "road-intersection0_0"));
        level.add(new Road(eventList, "road-intersection0_2"));
        level.add(new Road(eventList, "road-intersection0_4"));
        level.add(new Road(eventList, "road-intersection0_6"));
        level.add(new Road(eventList, "road2_0"));
        level.add(new Road(eventList, "road2_2"));
        level.add(new Road(eventList, "road2_4"));
        level.add(new Road(eventList, "road2_6"));
        level.add(new Road(eventList, "road-turn4_0"));
        level.add(new Road(eventList, "road-turn4_2"));
        level.add(new Road(eventList, "road-turn4_4"));
        level.add(new Road(eventList, "road-turn4_6"));
        level.add(new Roundabout(10, 5, eventList, "roundabout0_8", 2));
        level.add(new Roundabout(10, 5, eventList, "roundabout0_10", 2));
        level.add(new Roundabout(10, 5, eventList, "roundabout0_12", 2));
        level.add(new Roundabout(10, 5, eventList, "roundabout0_14", 2));
        level.add(new Roundabout(10, 5, eventList, "roundabout-double2_8", 2));
        level.add(new Roundabout(10, 5, eventList, "roundabout-double2_10", 2));
        level.add(new Roundabout(10, 5, eventList, "roundabout-double2_12", 2));
        level.add(new Roundabout(10, 5, eventList, "roundabout-double2_14", 2));
        level.add(new Roundabout(10, 5, eventList, "roundabout-road4_8", 2));
        level.add(new Roundabout(10, 5, eventList, "roundabout-road4_10", 2));
        level.add(new Roundabout(10, 5, eventList, "roundabout-road4_12", 2));
        level.add(new Roundabout(10, 5, eventList, "roundabout-road4_14", 2));
        level.add(new TrafficLights(50, 1, eventList, "traffic-lights6_0"));
        level.add(new TrafficLights(50, 1, eventList, "traffic-lights6_2"));
        level.add(new TrafficLights(50, 1, eventList, "traffic-lights6_4"));
        level.add(new TrafficLights(50, 1, eventList, "traffic-lights6_6"));
        level.add(new Crosswalk(50, 5, eventList, "crosswalk8_0"));
        level.add(new Crosswalk(50, 5, eventList, "crosswalk8_2"));
        level.add(new Crosswalk(50, 5, eventList, "crosswalk8_4"));
        level.add(new Crosswalk(50, 5, eventList, "crosswalk8_6"));
        level.add(new Goal(eventList, "goal6_8"));


        controller.render(level, "road-intersection0_0", 0, 0, "t-intersection-right");
        controller.render(level, "road-intersection0_2", 0, 2, "t-intersection-left");
        controller.render(level, "road-intersection0_4", 0, 4, "t-intersection-top");
        controller.render(level, "road-intersection0_6", 0, 6, "t-intersection-bottom");
        controller.render(level, "road2_0", 2, 0, "right");
        controller.render(level, "road2_2", 2, 2, "left");
        controller.render(level, "road2_4", 2, 4, "top");
        controller.render(level, "road2_6", 2, 6, "bottom");
        controller.render(level, "road-turn4_0", 4, 0, "right-turn");
        controller.render(level, "road-turn4_2", 4, 2, "left-turn");
        controller.render(level, "road-turn4_4", 4, 4, "top-turn");
        controller.render(level, "road-turn4_6", 4, 6, "bottom-turn");
        controller.render(level, "roundabout0_8", 0, 8, "right");
        controller.render(level, "roundabout0_10", 0, 10, "left");
        controller.render(level, "roundabout0_12", 0, 12, "top");
        controller.render(level, "roundabout0_14", 0, 14, "bottom");
        controller.render(level, "roundabout-double2_8", 2, 8, "right-double");
        controller.render(level, "roundabout-double2_10", 2, 10, "left-double");
        controller.render(level, "roundabout-double2_12", 2, 12, "top-double");
        controller.render(level, "roundabout-double2_14", 2, 14, "bottom-double");
        controller.render(level, "roundabout-road4_8", 4, 8, "right-r-road");
        controller.render(level, "roundabout-road4_10", 4, 10, "left-r-road");
        controller.render(level, "roundabout-road4_12", 4, 12, "top-r-road");
        controller.render(level, "roundabout-road4_14", 4, 14, "bottom-r-road");
        controller.render(level, "traffic-lights6_0", 6, 0, "right");
        controller.render(level, "traffic-lights6_2", 6, 2, "left");
        controller.render(level, "traffic-lights6_4", 6, 4, "top");
        controller.render(level, "traffic-lights6_6", 6, 6, "bottom");
        controller.render(level, "crosswalk8_0", 8, 0, "right");
        controller.render(level, "crosswalk8_2", 8, 2, "left");
        controller.render(level, "crosswalk8_4", 8, 4, "top");
        controller.render(level, "crosswalk8_6", 8, 6, "bottom");
        controller.render(level, "goal6_8",6,8,"goal");

        return level;
    }
    public Level getLevel1() {
        Level level = new Level("Level 1");
        level.arrival(new ArrivalProcess(new Normal(5, 5), eventList, "ARR1", 0, 0), "road0_0");
        level.add(new Road(eventList, "road0_0"), "crosswalk1_0");
        level.add(new Crosswalk(50, 5, eventList, "crosswalk1_0"), "road1_1");
        level.add(new Road(eventList, "road1_1"), "trafficlights1_2");
        level.add(new TrafficLights(50, 1, eventList, "trafficlights1_2"), "road1_3");
        level.add(new Road(eventList, "road1_3"), new String[]{"road1_4", "road2_3"});
        level.add(new Road(eventList, "road2_3"), "crosswalk3_3");
        level.add(new Road(eventList, "road1_4"));
        level.add(new Crosswalk(10, 5, eventList, "crosswalk3_3"), "road4_3");
        level.add(new Road(eventList, "road4_3"), "roundabout_right4_2");

        level.add(new Roundabout(0, 3, eventList, "roundabout_right4_2", 3), "roundabout_top5_2");
        level.add(new Roundabout(0, 3, eventList, "roundabout_top5_2", 3), "roundabout_left5_1");
        level.add(new Roundabout(0, 3, eventList, "roundabout_left5_1", 3), "roundabout_bottom4_1");
        level.add(new Roundabout(0, 3, eventList, "roundabout_bottom4_1", 3), new String[]{"roundabout_right4_2", "goal3_1"});
        level.add(new Goal(eventList, "goal3_1"));

        controller.render(level, "road0_0", 0, 0, "right");
        controller.render(level, "crosswalk1_0", 1, 0, "right");
        controller.render(level, "road1_1", 1, 1, "bottom");
        controller.render(level, "trafficlights1_2", 1, 2, "bottom");
        controller.render(level, "road2_3", 2, 3, "right");
        controller.render(level, "road1_3", 1, 3, "t-intersection-right");
        controller.render(level, "road1_4", 1, 4, "bottom");
        controller.render(level, "crosswalk3_3", 3, 3, "right");
        controller.render(level, "road4_3", 4, 3, "right");
        controller.render(level, "roundabout_right4_2", 4, 2, "right-r-road");
        controller.render(level, "roundabout_top5_2", 5, 2, "top-r-road");
        controller.render(level, "roundabout_left5_1", 5, 1, "left-r-road");
        controller.render(level, "roundabout_bottom4_1", 4, 1, "bottom-r-road");
        controller.render(level, "goal3_1", 3, 1, "goal");

        return level;
    }

    public Level getLevel3() {
        Level level = new Level("Level 3");
        level.arrival(new ArrivalProcess(new Normal(5, 5), eventList, "ARR1", 0, 0), "road0_0");
        level.add(new Road(eventList, "road0_0"), "road0_1");
        level.add(new Road(eventList, "road0_1"), "road0_2");
        level.add(new Road(eventList, "road0_2"), "road0_3");
        level.add(new Road(eventList, "road0_3"), new String[]{"road0_4", "road1_3"});
        level.add(new Road(eventList, "road0_4"), "road0_5");
        level.add(new Road(eventList, "road0_5"));
        level.add(new Road(eventList, "road1_3"), "road2_3");
        level.add(new Road(eventList, "road2_3"), "roundabout3_3");
        level.add(new Roundabout(0, 3, eventList, "roundabout3_3", 3), "roundabout3_4");
        level.add(new Roundabout(0, 3, eventList, "roundabout3_4", 3), new String[]{"roundabout4_4", "road3_5"});
        level.add(new Road(eventList, "road3_5"), "road2_5");
        level.add(new Road(eventList, "road2_5"), "road2_4");
        level.add(new Road(eventList, "road2_4"), "roundabout3_4");
        level.add(new Roundabout(0, 3, eventList, "roundabout4_4", 3), new String[]{"roundabout4_3", "road5_4"});
        level.add(new Road(eventList, "road5_4"), "road5_5");
        level.add(new Road(eventList, "road5_5"), "road4_5");
        level.add(new Road(eventList, "road4_5"), "roundabout4_4");
        level.add(new Roundabout(0, 3, eventList, "roundabout4_3", 3), new String[]{"roundabout3_3", "road4_2"});
        level.add(new Road(eventList, "road4_2"), "goal4_1");
        level.add(new Goal(eventList, "goal4_1"));

        controller.render(level, "road0_0", 0, 0, "bottom");
        controller.render(level, "road0_1", 0, 1, "bottom");
        controller.render(level, "road0_2", 0, 2, "bottom");
        controller.render(level, "road0_3", 0, 3, "t-intersection-right");
        controller.render(level, "road0_4", 0, 4, "bottom");
        controller.render(level, "road0_5", 0, 5, "bottom");
        controller.render(level, "road1_3", 1, 3, "right");
        controller.render(level, "road2_3", 2, 3, "right");
        controller.render(level, "roundabout3_3", 3, 3, "bottom-r-road");
        controller.render(level, "roundabout3_4", 3, 4, "right-double");
        controller.render(level, "road3_5", 3, 5, "top-turn");
        controller.render(level, "road2_5", 2, 5, "right-turn");
        controller.render(level, "road2_4", 2, 4, "bottom-turn");
        controller.render(level, "roundabout4_4", 4, 4, "top-double");
        controller.render(level, "road5_4", 5, 4, "left-turn");
        controller.render(level, "road5_5", 5, 5, "top-turn");
        controller.render(level, "road4_5", 4, 5, "right-turn");
        controller.render(level, "roundabout4_3", 4, 3, "left-r-road");
        controller.render(level, "road4_2", 4, 2, "top");
        controller.render(level, "goal4_1", 4, 1, "goal");


        return level;
    }

    public Level getLevel4() {
        Level level = new Level("Level 4");
        level.arrival(new ArrivalProcess(new Normal(5, 5), eventList, "ARR1", 1, 3), "road1_3");
        level.add(new Road(eventList, "road1_3"), "road2_3");
        level.add(new Road(eventList, "road2_3"), "roundabout3_3");
        level.add(new Roundabout(0, 3, eventList, "roundabout3_3", 3), "roundabout3_4");
        level.add(new Roundabout(0, 3, eventList, "roundabout3_4", 3), new String[]{"roundabout4_4", "road3_5"});
        level.add(new Road(eventList, "road3_5"), "road2_5");
        level.add(new Road(eventList, "road2_5"), "road2_4");
        level.add(new Road(eventList, "road2_4"), "roundabout3_4");
        level.add(new Roundabout(0, 3, eventList, "roundabout4_4", 3), new String[]{"roundabout4_3", "road5_4"});
        level.add(new Road(eventList, "road5_4"), "road5_5");
        level.add(new Road(eventList, "road5_5"), "road4_5");
        level.add(new Road(eventList, "road4_5"), "roundabout4_4");
        level.add(new Roundabout(0, 3, eventList, "roundabout4_3", 3), new String[]{"roundabout3_3", "road4_2"});
        level.add(new Road(eventList, "road4_2"), "goal4_1");
        level.add(new Goal(eventList, "goal4_1"));

        controller.render(level, "road1_3", 1, 3, "right");
        controller.render(level, "road2_3", 2, 3, "right");
        controller.render(level, "roundabout3_3", 3, 3, "bottom-r-road");
        controller.render(level, "roundabout3_4", 3, 4, "right-double");
        controller.render(level, "road3_5", 3, 5, "top-turn");
        controller.render(level, "road2_5", 2, 5, "right-turn");
        controller.render(level, "road2_4", 2, 4, "bottom-turn");
        controller.render(level, "roundabout4_4", 4, 4, "top-double");
        controller.render(level, "road5_4", 5, 4, "left-turn");
        controller.render(level, "road5_5", 5, 5, "top-turn");
        controller.render(level, "road4_5", 4, 5, "right-turn");
        controller.render(level, "roundabout4_3", 4, 3, "left-r-road");
        controller.render(level, "road4_2", 4, 2, "top");
        controller.render(level, "goal4_1", 4, 1, "goal");


        return level;
    }

    public Level getLevel2(){
        Level level = new Level("Level 2");
        level.arrival(new ArrivalProcess(new Normal(5, 5), eventList, "ARR1", 0, 0), "road0_0");

        level.add(new Road(eventList, "road0_0"), "crosswalk1_0");

        level.add(new Crosswalk(50, 5, eventList, "crosswalk1_0"), "road2_0");

        level.add(new Road(eventList, "road2_0"), "trafficlights2_1");

        level.add(new TrafficLights(50, 1, eventList, "trafficlights2_1"), "road2_2");

        level.add(new Road(eventList, "road2_2"), "crosswalk1_2");

        level.add(new Crosswalk(10, 5, eventList, "crosswalk1_2"), "road0_2");

        level.add(new Road(eventList, "road0_2"), "trafficlights0_3");

        level.add(new TrafficLights(50, 1, eventList, "trafficlights0_3"), "road0_4");

        level.add(new Road(eventList, "road0_4"), "roundabout_bottom1_4");

        level.add(new Roundabout(0, 3, eventList, "roundabout_bottom1_4", 3), "roundabout_right1_5");
        level.add(new Roundabout(0, 3, eventList, "roundabout_right1_5", 3), "roundabout_top2_5");
        level.add(new Roundabout(0, 3, eventList, "roundabout_top2_5", 3), "roundabout_left2_4");
        level.add(new Roundabout(0, 3, eventList, "roundabout_left2_4", 3), new String[]{"roundabout_bottom1_4", "road3_4"});

        level.add(new Road(eventList, "road3_4"), "trafficlights4_4");

        level.add(new TrafficLights(50, 1, eventList, "trafficlights4_4"), "road5_4");

        level.add(new Road(eventList, "road5_4"), "crosswalk5_3");

        level.add(new Crosswalk(50, 5, eventList, "crosswalk5_3"), "road5_2");

        level.add(new Road(eventList, "road5_2"), "roundabout_right5_1");

        level.add(new Roundabout(0, 3, eventList, "roundabout_right5_1", 3), "roundabout_top6_1");
        level.add(new Roundabout(0, 3, eventList, "roundabout_top6_1", 3), "roundabout_left6_0");
        level.add(new Roundabout(0, 3, eventList, "roundabout_left6_0", 3), "roundabout_bottom5_0");
        level.add(new Roundabout(0, 3, eventList, "roundabout_bottom5_0", 3), new String[]{"roundabout_right5_1", "road4_0"});

        level.add(new Road(eventList, "road4_0"), "road3_0");

        level.add(new Road(eventList, "road3_0"), "road3_-1");

        level.add(new Road(eventList, "road3_-1"), "crosswalk2_-1");

        level.add(new Crosswalk(50, 5, eventList, "crosswalk2_-1"), "goal1_-1");

        level.add(new Goal(eventList, "goal1_-1"));


        controller.render(level, "road0_0", 0, 0, "right");
        controller.render(level, "crosswalk1_0", 1, 0, "right");
        controller.render(level, "road2_0", 2, 0, "left-turn");
        controller.render(level, "trafficlights2_1", 2, 1, "bottom");
        controller.render(level, "road2_2", 2, 2, "top-turn");
        controller.render(level, "crosswalk1_2", 1, 2, "left");
        controller.render(level, "road0_2", 0, 2, "bottom-turn");
        controller.render(level, "trafficlights0_3", 0, 3, "bottom");
        controller.render(level, "road0_4", 0, 4, "right-turn");
        controller.render(level, "roundabout_bottom1_4", 1, 4, "bottom-r-road");
        controller.render(level, "roundabout_right1_5", 1, 5, "right-r-road");
        controller.render(level, "roundabout_top2_5", 2, 5, "top-r-road");
        controller.render(level, "roundabout_left2_4", 2, 4, "left-r-road");
        controller.render(level, "road3_4", 3, 4, "right");
        controller.render(level, "trafficlights4_4", 4, 4, "right");
        controller.render(level, "road5_4", 5, 4, "top-turn");
        controller.render(level, "crosswalk5_3", 5, 3, "top");
        controller.render(level, "road5_2", 5, 2, "top");
        controller.render(level, "roundabout_right5_1", 5, 1, "right-r-road");
        controller.render(level, "roundabout_top6_1", 6, 1, "top-r-road");
        controller.render(level, "roundabout_left6_0", 6, 0, "left-r-road");
        controller.render(level, "roundabout_bottom5_0", 5, 0, "bottom-r-road");
        controller.render(level, "road4_0", 4, 0, "left");
        controller.render(level, "road3_0", 3, 0, "right-turn");
        controller.render(level, "road3_-1", 3, -1, "left-turn");
        controller.render(level, "crosswalk2_-1", 2, -1, "right");
        controller.render(level, "goal1_-1", 1, -1, "goal");

        return level;
    }

    public Level getLevel5(){
        Level level = new Level("Level 5");
        level.arrival(new ArrivalProcess(new Normal(5, 5), eventList, "ARR1", 0, 0), "road0_0");
        level.add(new Road(eventList, "road0_0"),"road1_0");
        level.add(new Road(eventList, "road1_0"),new String[]{"road1_-1","road1_1"});
        level.add(new Road(eventList, "road1_-1"),"road1_-2");
        level.add(new Road(eventList, "road1_-2"),"crosswalk2_-2");
        level.add(new Crosswalk(50,5, eventList, "crosswalk2_-2"),"road3_-2");
        level.add(new Road(eventList, "road3_-2"),"road3_-1");
        level.add(new Road(eventList, "road3_-1"),"roundabout_bottom4_-1");

        level.add(new Road(eventList, "road1_1"),"trafficlights2_1");
        level.add(new TrafficLights(50,1, eventList, "trafficlights2_1"),"road3_1");
        level.add(new Road(eventList,"road3_1"),"road4_1");
        level.add(new Road(eventList,"road4_1"),"roundabout_right4_0");

        level.add(new Roundabout(0, 3, eventList, "roundabout_right4_0", 3), "roundabout_top5_0");
        level.add(new Roundabout(0, 3, eventList, "roundabout_top5_0", 3), new String[]{"roundabout_left5_-1", "road6_0"});
        level.add(new Roundabout(0, 3, eventList, "roundabout_left5_-1", 3), new String[]{"roundabout_bottom4_-1", "road5_-2"});
        level.add(new Roundabout(0, 3, eventList, "roundabout_bottom4_-1", 3), "roundabout_right4_0");

        level.add(new Road(eventList,"road6_0"),"crosswalk7_0");
        level.add(new Crosswalk(50,5, eventList, "crosswalk7_0"),"goal8_0");
        level.add(new Goal(eventList,"goal8_0"));

        level.add(new Road(eventList,"road5_-2"),"trafficlights5_-3");
        level.add(new TrafficLights(50,1, eventList, "trafficlights5_-3"),"goal5_-4");
        level.add(new Goal(eventList,"goal5_-4"));


        controller.render(level, "road0_0", 0, 0, "right");
        controller.render(level, "road1_0", 1, 0, "t-intersection-left");

        controller.render(level, "road1_-1", 1, -1, "top");
        controller.render(level, "road1_-2", 1, -2, "bottom-turn");
        controller.render(level, "crosswalk2_-2", 2, -2, "right");
        controller.render(level, "road3_-2", 3, -2, "left-turn");
        controller.render(level,"road3_-1",3,-1,"right-turn");

        controller.render(level, "road1_1", 1, 1, "right-turn");
        controller.render(level, "trafficlights2_1", 2, 1, "right");
        controller.render(level, "road3_1",3,1,"right");
        controller.render(level, "road4_1",4,1,"top-turn");

        controller.render(level, "roundabout_right4_0", 4, 0, "right-r-road");
        controller.render(level, "roundabout_top5_0", 5, 0, "top-r-road");
        controller.render(level, "roundabout_left5_-1", 5, -1, "left-r-road");
        controller.render(level, "roundabout_bottom4_-1", 4, -1, "bottom-r-road");

        controller.render(level, "road6_0", 6, 0, "right");
        controller.render(level, "crosswalk7_0", 7, 0, "right");
        controller.render(level, "goal8_0", 8, 0, "goal");

        controller.render(level, "road5_-2", 5, -2, "top");
        controller.render(level, "trafficlights5_-3", 5, -3, "top");
        controller.render(level, "goal5_-4", 5, -4, "goal");



        return level;
    }
}
