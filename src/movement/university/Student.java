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
    private HashMap<longBreakActivities, Activity> longBreakActivitiesActivityHashMap;
    private Random RANDOM = new Random();
    private Class longBreakActivitiesClass = longBreakActivities.class;
    private Class shortBreakActivitiesClass = shortBreakActivities.class;
    private ActivityLocator activityLocator;


    public List<ScheduledEvent> getGeneratedEvents() {
        return generatedEvents;
    }

    public Student() {

        this.person = new Person(new PersonAttributesGenerator());
        this.classesGenerator = new ClassesGenerator();

        double magicNumber = 432.75;

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
            double timeUntilNextClass = nextClass.getStartTime() - currentTime;

            // check for possibility to do long break activities
            while (canDoLongActivity(timeUntilNextClass)) {

                double duration = RANDOM.nextDouble() * timeUntilNextClass + 1200;
                ScheduledEvent eventToPush = generateLongBreakEvent(currentTime, duration);
                resultingEvents.add(eventToPush);
                currentTime += duration;
                timeUntilNextClass = nextClass.getStartTime() - currentTime;

            }

            // check for possibility to do short break activities
            while (canDoShortActivity(timeUntilNextClass)) {

                double duration = RANDOM.nextDouble() * 300 + 200;
                ScheduledEvent eventToPush = generateShortBreakEvent(currentTime, duration);
                resultingEvents.add(eventToPush);
                currentTime += duration;
                timeUntilNextClass = nextClass.getStartTime() - currentTime;

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

    private boolean canDoShortActivity(double duration) {

        return duration > 500;

    }

    private boolean canDoLongActivity(double duration) {

        return duration > 2500;

    }

    private double calculateArrivalDate(double organizedLevel, double eventStartTime) {

        return eventStartTime - (organizedLevel - 0.5) * 1200 - 100;

    }

    private ScheduledEvent generateShortBreakEvent(double startTime, double duration) {

        return new ScheduledEvent(this.generateShortBreakActivity(duration), startTime);

    }

    private ScheduledEvent generateLongBreakEvent(double startTime, double duration) {

        return new ScheduledEvent(this.generateLongBreakActivity(duration), startTime);

    }

    private Activity generateShortBreakActivity(double duration) {

        shortBreakActivities activityValue = (shortBreakActivities) (shortBreakActivitiesClass.getEnumConstants()[RANDOM.nextInt(shortBreakActivitiesClass.getEnumConstants().length)]);
        return new Activity(duration, activityLocator.getShortActivityLocation(activityValue));

    }

    private Activity generateLongBreakActivity(double duration) {

        longBreakActivities activityValue = (longBreakActivities) (longBreakActivitiesClass.getEnumConstants()[RANDOM.nextInt(longBreakActivitiesClass.getEnumConstants().length)]);
        return new Activity(duration, activityLocator.getLongActivityLocation(activityValue));

    }

    private ScheduledEvent generateEventForClass(UniversityClass uniClass, double currentTime) {

        Activity activity = new Activity(uniClass.getEndTime() - currentTime, uniClass.getLocation());

        return new ScheduledEvent(activity, currentTime);

    }

}
