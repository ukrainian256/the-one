package movement.university;

import core.Coord;

import java.util.*;

/**
 * Created by afedotov on 11/28/16.
 */
public class Student {

    private ClassesGenerator classesGenerator;
    private Person person;
    private List<ScheduledEvent> generatedEvents;
    private HashMap<shortBreakActivities, Activity> shortBreakActivitiesActivityMapping;
    private HashMap<longBreakActivities, Activity> longBreakActivitiesActivityHashMap;


    public List<ScheduledEvent> getGeneratedEvents() {
        return generatedEvents;
    }

    public Student() {

        this.person = new Person(new PersonGenerator());
        this.classesGenerator = new ClassesGenerator();

        double magicNumber = 432.75;

        shortBreakActivitiesActivityMapping = new HashMap<>();
        shortBreakActivitiesActivityMapping.put(shortBreakActivities.Smoke, new Activity(300, new Coord(614.90, -254.13 + magicNumber)));

        longBreakActivitiesActivityHashMap = new HashMap<>();
        longBreakActivitiesActivityHashMap.put(longBreakActivities.Library, new Activity(1800, new Coord(251.28, -194.90 + magicNumber)));
        longBreakActivitiesActivityHashMap.put(longBreakActivities.SittingInTheHall, new Activity(1800, new Coord(366.89, -172.76 + magicNumber)));

        this.generatedEvents = generateEvents();

    }

    private List<ScheduledEvent> generateEvents() {

        List<ScheduledEvent> resultingEvents = new ArrayList<>();

        // 1. generate classes, sorted by time
        List<UniversityClass> classes = classesGenerator.generateClasses();

        // 2. based on organizedLevel and transportationType generate arrivalTime
        // TODO: include organizerLevel and transportationType in generation
        double arrivalTime = calculateArrivalDate(this.person.getOrganizedLevel(), classes.get(0).getStartTime());

        double currentTime = arrivalTime;
        int currentClassIndex = 0;

        while (currentClassIndex < classes.size()) {

            UniversityClass nextClass = classes.get(currentClassIndex);

            // check for possibility to do long break activities
            while (canDoLongActivity(currentTime, nextClass.getStartTime())) {

                ScheduledEvent eventToPush = generateLongBreakEvent(currentTime);
                resultingEvents.add(eventToPush);
                currentTime += eventToPush.getDuration();

            }

            // check for possibility to do short break activities
            while (canDoShortActivity(currentTime, nextClass.getStartTime())) {

                ScheduledEvent eventToPush = generateShortBreakEvent(currentTime);
                resultingEvents.add(eventToPush);
                currentTime += eventToPush.getDuration();

            }

            // go to class
            UniversityClass currentClass = classes.get(currentClassIndex);
            resultingEvents.add(generateEventForClass(currentClass, currentTime));

            // class finished, update index for next class + update currentTime
            currentTime = currentClass.getEndTime();
            currentClassIndex++;

        }

        // go home - nothing should be added

        return resultingEvents;

    }

    private boolean canDoShortActivity(double currentTime, double nextActivityTime) {

        return nextActivityTime - currentTime > 400;

    }

    private boolean canDoLongActivity(double currentTime, double nextActivityTime) {

        return nextActivityTime - currentTime > 2000;

    }

    private double calculateArrivalDate(double organizedLevel, double eventStartTime) {

//        return eventStartTime - (organizedLevel - 0.5) * 1200 - 100;

        return eventStartTime - 600;

    }

    private ScheduledEvent generateShortBreakEvent(double startTime) {

        return new ScheduledEvent(this.generateShortBreakActivity(), startTime);

    }

    private ScheduledEvent generateLongBreakEvent(double startTime) {

        return new ScheduledEvent(this.generateLongBreakActivity(), startTime);

    }

    private Activity generateShortBreakActivity() {

        return shortBreakActivitiesActivityMapping.get(shortBreakActivities.Smoke);

    }

    private Activity generateLongBreakActivity() {

        Random r = new Random();
        longBreakActivities activityValue = longBreakActivities.values()[r.nextInt(2)];
        return longBreakActivitiesActivityHashMap.get(activityValue);

    }

    private ScheduledEvent generateEventForClass(UniversityClass uniClass, double currentTime) {

        Activity activity = new Activity(uniClass.getEndTime() - currentTime, uniClass.getLocation());

        return new ScheduledEvent(activity, currentTime);

    }

}

enum shortBreakActivities {
    Smoke,
    Coffee,
    Toilet
}

enum longBreakActivities {
    Library,
    Mensa,
    SittingInTheHall
}
