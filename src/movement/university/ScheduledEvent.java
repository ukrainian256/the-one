package movement.university;

import core.Coord;

/**
 * Created by afedotov on 11/28/16.
 */
public class ScheduledEvent {

    private Activity activity;
    private double startTime;

    public ScheduledEvent(Activity activity, double startTime) {
        this.activity = activity;
        this.startTime = startTime;
    }

    public double getStartTime() {
        return startTime;
    }

    public Coord getLocation() {
        return activity.getLocation();
    }

    public double getEndTime() {
        return startTime + activity.getDuration();
    }

    public double getDuration() {
        return activity.getDuration();
    }


}
