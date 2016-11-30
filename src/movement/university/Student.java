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

    public List<ScheduledEvent> getGeneratedEvents() {
        return generatedEvents;
    }

    public Student() {

        this.person = new Person(new PersonGenerator());
        this.classesGenerator = new ClassesGenerator();
        this.generatedEvents = generateEvents();

    }

    private List<ScheduledEvent> generateEvents() {

        List<ScheduledEvent> resultingEvents = new ArrayList<>();

        // 1. generate classes, sorted by time
        List<UniversityClass> classes = classesGenerator.generateClasses();

        // 2. based on organizedLevel and transportationType generate arrivalTime
        // TODO: include organizerLevel and transportationType in generation
        double arrivalTime = calculateArrivalDate(classes.get(0).getStartTime());

        double currentTime = arrivalTime;
        int currentClassIndex = 0;

        while(currentClassIndex < classes.size()) {

            UniversityClass nextClass = classes.get(currentClassIndex);

            // check for possibility to do long break activities
            while(canDoLongActivity(currentTime, nextClass.getStartTime())) {

                ScheduledEvent eventToPush = generateLongBreakEvent(currentTime);
                resultingEvents.add(eventToPush);
                currentTime += eventToPush.getDuration();

            }

            // check for possibility to do short break activities
            while(canDoShortActivity(currentTime, nextClass.getStartTime())) {

                ScheduledEvent eventToPush = generateShortBreakEvent(currentTime);
                resultingEvents.add(eventToPush);
                currentTime += eventToPush.getDuration();

            }

            // go to class
            UniversityClass currentClass = classes.get(currentClassIndex);
            resultingEvents.add(generateEventForClass(currentClass));

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

    private double calculateArrivalDate(double eventStartTime) {

        return eventStartTime - 600;

    }

    private ScheduledEvent generateShortBreakEvent(double startTime) {

        return new ScheduledEvent(this.generateShortBreakActivity(), startTime);

    }

    private ScheduledEvent generateLongBreakEvent(double startTime) {

        return new ScheduledEvent(this.generateLongBreakActivity(), startTime);

    }

    private Activity generateShortBreakActivity() {

        HashMap<shortBreakActivities, Activity> shortBreakActivitiesActivityMapping = new HashMap<>();

        shortBreakActivitiesActivityMapping.put(shortBreakActivities.Smoke, new Activity(300, new Coord(190.06, 379.66)));

        return shortBreakActivitiesActivityMapping.get(shortBreakActivities.Smoke);

    }

    private Activity generateLongBreakActivity() {

        HashMap<longBreakActivities, Activity> shortBreakActivitiesActivityMapping = new HashMap<>();

        shortBreakActivitiesActivityMapping.put(longBreakActivities.Library, new Activity(1800, new Coord(251.28, 194.90)));

        return shortBreakActivitiesActivityMapping.get(longBreakActivities.Library);

    }

    private ScheduledEvent generateEventForClass(UniversityClass uniClass) {

        Activity activity = new Activity(uniClass.getEndTime() - uniClass.getStartTime(), uniClass.getLocation());

        return new ScheduledEvent(activity, uniClass.getStartTime());

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
    MeetFriends
}
