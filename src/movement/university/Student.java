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

        // 3. depending on quantity of time before the activity generate shortBreakActivities -> state shortActivity
        double firstClassTime = classes.get(0).getStartTime();
        while(firstClassTime - currentTime > 400) {

            ScheduledEvent eventToAdd = generateShortBreakEvent(currentTime);
            resultingEvents.add(eventToAdd);

            currentTime += eventToAdd.getDuration();

        }

        // 4. go to next class -> state sittingInTheClass
        UniversityClass currentClass = classes.get(0);
        resultingEvents.add(generateEventForClass(currentClass));
        currentTime += currentClass.getEndTime();
        currentClassIndex++;

        // 5. class finished - check for next class, if no -> go home, else -> 5.
        // 6. based on quantity of time generate shortBreakActivities or longBreakActivities -> shortActivity || longActivity
        // 7. move to point 4

        return resultingEvents;

    }

    private double calculateArrivalDate(double eventStartTime) {

        return eventStartTime - 600;

    }

    private ScheduledEvent generateShortBreakEvent(double startTime) {

        return new ScheduledEvent(this.generateShortBreakActivity(), startTime);

    }

    private Activity generateShortBreakActivity() {

        HashMap<shortBreakActivities, Activity> shortBreakActivitiesActivityMapping = new HashMap<>();

        shortBreakActivitiesActivityMapping.put(shortBreakActivities.Smoke, new Activity(300, new Coord(215.93, 155.12)));

        return shortBreakActivitiesActivityMapping.get(shortBreakActivities.Smoke);

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
