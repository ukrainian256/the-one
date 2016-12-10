package movement.university;

import java.util.*;

/**
 * Created by afedotov on 11/28/16.
 */

public class PersonAttributesGenerator {

    private final List<TransportationType> VALUES;
    private final int SIZE;
    private static final Random RANDOM = new Random();


    public PersonAttributesGenerator() {

        VALUES = Collections.unmodifiableList(Arrays.asList(PersonAttributesGenerator.getTransportationTypes()));
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
