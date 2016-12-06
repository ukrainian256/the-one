package movement.university;

import core.Coord;
import movement.UniversityMovement;

/**
 * Created by afedotov on 11/28/16.
 */
public class UniversityClass {

    private int startTime;
    private int endTime;
    private Coord location;
    private String name;
    private double popularity;

    public UniversityClass(String name, int startTime, int endTime, Coord location, double popularity) {

        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.popularity = popularity;

    }

    public int getStartTime() {
        return this.startTime;
    }

    public int getEndTime() {
        return this.endTime;
    }

    public Coord getLocation() {
        return this.location;
    }

    public String getName() {
        return this.name;
    }

    public double getPopularity() { return this.popularity; }

    public static UniversityClass replicateWithDifferentLocation(UniversityClass other, Coord location) {

        return new UniversityClass(other.getName(), other.getStartTime(), other.getEndTime(), location, other.getPopularity());

    }

}
