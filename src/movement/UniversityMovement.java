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
public class UniversityMovement extends ShortestPathMapBasedMovement {

    Student student;

    private int currentEventIndex = -1;

    public UniversityMovement(Settings settings) {
        super(settings);
        student = new Student();
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
        Coord initialLocation;
        Coord destinationLocation;

        if(currentEventIndex == -1) {

            initialLocation = this.getInitialLocation();

        } else {

            ScheduledEvent currentEvent = student.getGeneratedEvents().get(currentEventIndex);
            initialLocation = currentEvent.getLocation();

        }

        currentEventIndex++;

        if(currentEventIndex == student.getGeneratedEvents().size()) {

            destinationLocation = this.getInitialLocation();

        } else {

            ScheduledEvent nextEvent = student.getGeneratedEvents().get(currentEventIndex);
            destinationLocation = nextEvent.getLocation();

        }

        MapNode from = super.getMap().getNodeByCoord(initialLocation);
        MapNode to = super.getMap().getNodeByCoord(destinationLocation);

        List<MapNode> nodePath = super.pathFinder.getShortestPath(from, to);

        // this assertion should never fire if the map is checked in read phase
        assert nodePath.size() > 0 : "No path from " + lastMapNode + " to " +
                to + ". The simulation map isn't fully connected";

        for (MapNode node : nodePath) { // create a Path from the shortest path
            p.addWaypoint(node.getLocation());
        }

        return p;
    }

    @Override
    public UniversityMovement replicate() {
        return new UniversityMovement(this);
    }

}
