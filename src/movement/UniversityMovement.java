package movement;

import core.*;
import input.WKTMapReader;
import movement.map.MapNode;
import movement.map.SimMap;
import movement.university.ScheduledEvent;
import movement.university.Student;
import movement.university.UniversityClass;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by afedotov on 11/29/16.
 */
public class UniversityMovement extends MapBasedMovement {

    Student student;

    private int currentEventIndex = -1;

    public UniversityMovement(Settings settings) {
        super(settings);
        student = new Student();
    }

    /**
     * Creates a new MapBasedMovement based on a Settings object's settings
     * but with different SimMap
     * @param settings The Settings object where the settings are read from
     * @param newMap The SimMap to use
     * @param nrofMaps How many map "files" are in the map
     */
    public UniversityMovement(Settings settings, SimMap newMap, int nrofMaps) {
        super(settings, newMap, nrofMaps);
    }

    /**
     * Copyconstructor.
     * @param mbm The MapBasedMovement object to base the new object to
     */
    protected UniversityMovement(UniversityMovement mbm) {
        super(mbm);
        student = new Student();
    }

    /**
     * Returns a (random) coordinate that is between two adjacent MapNodes
     */
    @Override
    public Coord getInitialLocation() {

        return new Coord(592.68, -262.80+432.75);

    }

    @Override
    public boolean isActive() {

        boolean notStarted = currentEventIndex < -1;
        boolean alreadyFinished = currentEventIndex > student.getGeneratedEvents().size();

        return !notStarted && !alreadyFinished;

    }

    @Override
    protected double generateWaitTime() {

        ScheduledEvent currentEvent;

        if(currentEventIndex == -1) {

            currentEvent = student.getGeneratedEvents().get(0);

        } else if(currentEventIndex >= student.getGeneratedEvents().size()) {

            return 86401;

        } else {

            currentEvent = student.getGeneratedEvents().get(currentEventIndex);

        }

        return currentEvent.getEndTime() - SimClock.getTime();

    }

    @Override
    public Path getPath() {

        if(!this.isActive()) {
            return null;
        }

        Path p = new Path(generateSpeed());

        if(currentEventIndex == -1) {

            p.addWaypoint(this.getInitialLocation());

        } else {

            ScheduledEvent currentEvent = student.getGeneratedEvents().get(currentEventIndex);
            p.addWaypoint(currentEvent.getLocation());

        }

        currentEventIndex++;

        if(currentEventIndex == student.getGeneratedEvents().size()) {

            p.addWaypoint(this.getInitialLocation());

        } else {

            ScheduledEvent nextEvent = student.getGeneratedEvents().get(currentEventIndex);
            p.addWaypoint(nextEvent.getLocation());

        }

        return p;
    }

    @Override
    public UniversityMovement replicate() {
        return new UniversityMovement(this);
    }

}
