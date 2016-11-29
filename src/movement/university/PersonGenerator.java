package movement.university;

import java.util.*;

/**
 * Created by afedotov on 11/28/16.
 */

public class PersonGenerator {

    private final List<TransportationType> VALUES;
    private final int SIZE;
    private static final Random RANDOM = new Random();


    public PersonGenerator() {

        VALUES = Collections.unmodifiableList(Arrays.asList(PersonGenerator.getTransportationTypes()));
        SIZE = VALUES.size();

    }

    public double generateOrganizationLevel() {

        return RANDOM.nextDouble();

    }

    public TransportationType generateTransportationType() {

        return VALUES.get(RANDOM.nextInt(SIZE));

    }

    public double generateSpeed() {

        return RANDOM.nextDouble();

    }

    private static TransportationType[] getTransportationTypes() {

        return TransportationType.values();

    }

}
