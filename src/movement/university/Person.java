package movement.university;

import movement.RandomDirection;
import java.util.*;

/**
 * Created by afedotov on 11/28/16.
 */
public class Person {

    private double speed;
    private Gender gender;
    private double organizedLevel;
    private TransportationType transportationType;

    public Person(double speed, Gender gender, double organizedLevel, TransportationType transportationType) {

        this.speed = speed;
        this.gender = gender;
        this.organizedLevel = organizedLevel;
        this.transportationType = transportationType;

    }

    public Person(PersonGenerator personGenerator) {

        Random random = new Random();
        this.gender = random.nextDouble() > 0.3 ? Gender.Male : Gender.Female;
        this.speed = personGenerator.generateSpeed();
        this.organizedLevel = personGenerator.generateOrganizationLevel();
        this.transportationType = personGenerator.generateTransportationType();

    }

    public double getSpeed() {
        return speed;
    }

    public Gender getGender() {
        return gender;
    }

    public double getOrganizedLevel() {
        return organizedLevel;
    }

    public TransportationType getTransportationType() {
        return transportationType;
    }
}


