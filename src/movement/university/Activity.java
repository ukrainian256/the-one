package movement.university;

import core.Coord;

/**
 * Created by afedotov on 11/28/16.
 */
public class Activity {

    private double duration;
    private Coord location;

    public Activity(double duration, Coord location) {

        this.duration = duration;
        this.location = location;

    }

    public double getDuration() {
        return duration;
    }

    public Coord getLocation() {
        return location;
    }
}
