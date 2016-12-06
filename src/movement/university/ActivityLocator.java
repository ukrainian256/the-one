package movement.university;

import core.Coord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by afedotov on 12/3/16.
 */
public class ActivityLocator {

    private static HashMap<shortBreakActivities, ArrayList<Coord>> shortBreakActivitiesLocations = new HashMap<>();
    private static HashMap<longBreakActivities, ArrayList<Coord>> longBreakActivitiesLocations = new HashMap<>();
    private static Random RANDOM = new Random();

    static {

        double magicNumber = 432.75;

        shortBreakActivitiesLocations.put(shortBreakActivities.Smoke, new ArrayList<>(Arrays.asList(
                new Coord(614.90, -254.13 + magicNumber),
                new Coord(622.48, -250.92 + magicNumber),
                new Coord(631.80, -240.81 + magicNumber),
                new Coord(606.00, -209.12 + magicNumber),
                new Coord(635.31, -226.03 + magicNumber),
                new Coord(631.16, -216.45 + magicNumber),
                new Coord(617.88, -211.52 + magicNumber)
        )));

        shortBreakActivitiesLocations.put(shortBreakActivities.SitInTheHall, new ArrayList<>(Arrays.asList(
                new Coord(366.89, -172.76 + magicNumber),
                new Coord(390.65, -169.99 + magicNumber),
                new Coord(454.79, -159.36 + magicNumber),
                new Coord(454.79, -159.36 + magicNumber),
                new Coord(506.02, -190.24 + magicNumber),
                new Coord(379.76, -210.79 + magicNumber),
                new Coord(402.21, -209.86 + magicNumber),
                new Coord(464.56, -200.49 + magicNumber),
                new Coord(483.47, -194.08 + magicNumber),
                new Coord(494.61, -145.84 + magicNumber)
        )));

        longBreakActivitiesLocations.put(longBreakActivities.Library, new ArrayList<>(Arrays.asList(
                new Coord(251.28, -194.90 + magicNumber),
                new Coord(251.32, -179.51 + magicNumber),
                new Coord(240.34, -165.71 + magicNumber),
                new Coord(230.15, -157.91 + magicNumber),
                new Coord(199.48, -208.87 + magicNumber),
                new Coord(193.00, -194.74 + magicNumber),
                new Coord(190.49, -180.75 + magicNumber),
                new Coord(200.45, -166.69 + magicNumber),
                new Coord(214.66, -159.23 + magicNumber)
        )));

        longBreakActivitiesLocations.put(longBreakActivities.SittingInTheHall, new ArrayList<>(Arrays.asList(
                new Coord(366.89, -172.76 + magicNumber),
                new Coord(390.65, -169.99 + magicNumber),
                new Coord(454.79, -159.36 + magicNumber),
                new Coord(454.79, -159.36 + magicNumber),
                new Coord(506.02, -190.24 + magicNumber),
                new Coord(379.76, -210.79 + magicNumber),
                new Coord(402.21, -209.86 + magicNumber),
                new Coord(464.56, -200.49 + magicNumber),
                new Coord(483.47, -194.08 + magicNumber),
                new Coord(494.61, -145.84 + magicNumber)
        )));

    }

    public static Coord getShortActivityLocation(shortBreakActivities activityType) {

        ArrayList<Coord> locations = shortBreakActivitiesLocations.get(activityType);

        return locations.get(RANDOM.nextInt(locations.size()));

    }

    public static Coord getLongActivityLocation(longBreakActivities activityType) {

        ArrayList<Coord> locations = longBreakActivitiesLocations.get(activityType);

        return locations.get(RANDOM.nextInt(locations.size()));

    }

}
