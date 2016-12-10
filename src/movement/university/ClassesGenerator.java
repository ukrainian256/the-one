/*
 * Copyright 2010 Aalto University, ComNet
 * Released under GPLv3. See LICENSE.txt for details.
 */
package movement.university;

import core.Coord;

import java.util.*;

public class ClassesGenerator {

    private static List<UniversityClass> uniClasses;
    private static List<Coord> horsaalCoordinates;
    private static List<Coord> smallRoomCoordinates;
    private static Random r = new Random();
    private static double horsaalClassPopularity = 0.8;

    static {

        double magicNumber = 432.75;

        horsaalCoordinates = Arrays.asList(
                new Coord(625.14, -188.92 + magicNumber),
                new Coord(640.29, -182.99 + magicNumber),
                new Coord(647.04, -175.28 + magicNumber),
                new Coord(637.93, -150.62 + magicNumber),
                new Coord(635.67, -186.80 + magicNumber),
                new Coord(655.39, -164.14 + magicNumber),
                new Coord(647.23, -155.42 + magicNumber),
                new Coord(618.04, -149.43 + magicNumber),
                new Coord(632.95, -150.45 + magicNumber)
        );

        smallRoomCoordinates = Arrays.asList(

                new Coord(190.06, -379.66 + magicNumber),
                new Coord(184.78, -350.64 + magicNumber),
                new Coord(182.75, -323.82 + magicNumber),
                new Coord(177.85, -293.68 + magicNumber),
                new Coord(201.40, -289.78 + magicNumber),
                new Coord(210.58, -376.90 + magicNumber),
                new Coord(207.68, -347.52 + magicNumber),
                new Coord(205.67, -321.16 + magicNumber),

                new Coord(277.85, -369.18 + magicNumber),
                new Coord(299.34, -367.44 + magicNumber),
                new Coord(293.74, -330.41 + magicNumber),
                new Coord(272.46, -334.41 + magicNumber),
                new Coord(265.84, -292.14 + magicNumber),
                new Coord(287.38, -288.67 + magicNumber),

                new Coord(361.80, -354.96 + magicNumber),
                new Coord(385.05, -352.54 + magicNumber),
                new Coord(379.68, -320.69 + magicNumber),
                new Coord(356.54, -325.13 + magicNumber),
                new Coord(351.15, -281.57 + magicNumber),
                new Coord(374.22, -277.16 + magicNumber),

                new Coord(448.08, -344.92 + magicNumber),
                new Coord(471.20, -339.27 + magicNumber),
                new Coord(467.45, -310.95 + magicNumber),
                new Coord(444.14, -316.85 + magicNumber),
                new Coord(437.40, -265.20 + magicNumber),
                new Coord(458.96, -261.48 + magicNumber),

                new Coord(533.84, -335.86 + magicNumber),
                new Coord(556.17, -331.17 + magicNumber),
                new Coord(551.03, -292.87 + magicNumber),
                new Coord(528.45, -295.72 + magicNumber),
                new Coord(522.89, -249.55 + magicNumber),
                new Coord(544.74, -244.60 + magicNumber),

                new Coord(283.75, -77.81 + magicNumber),
                new Coord(294.02, -126.35 + magicNumber),
                new Coord(317.17, -119.46 + magicNumber),
                new Coord(306.88, -71.66 + magicNumber),

                new Coord(360.35, -59.86 + magicNumber),
                new Coord(383.03, -52.21 + magicNumber),
                new Coord(374.31, -109.71 + magicNumber),
                new Coord(396.22, -102.77 + magicNumber),

                new Coord(438.26, -39.97 + magicNumber),
                new Coord(462.35, -34.85 + magicNumber),
                new Coord(452.89, -92.01 + magicNumber),
                new Coord(472.52, -86.24 + magicNumber),

                new Coord(519.78, -23.87 + magicNumber),
                new Coord(541.92, -17.68 + magicNumber),
                new Coord(530.33, -71.49 + magicNumber),
                new Coord(552.95, -65.82 + magicNumber),

                new Coord(595.86, -6.65 + magicNumber),
                new Coord(607.94, -45.87 + magicNumber),
                new Coord(628.27, -41.61 + magicNumber),
                new Coord(619.01, -0.00 + magicNumber)

        );

        uniClasses = generateWholeSchedule();

    }

    // generates classes for a particular student, based on pregenerated schedule for the day
    public List<UniversityClass> generateClasses() {

        List<UniversityClass> generatedClasses = new ArrayList<>();

        // create random classes
        for (UniversityClass uniClass : uniClasses) {

            // change location for horsaal
            if (uniClass.getPopularity() > horsaalClassPopularity) {

                Coord randomHorsaalLocation = horsaalCoordinates.get(r.nextInt(horsaalCoordinates.size()));
                generatedClasses.add(UniversityClass.replicateWithDifferentLocation(uniClass, randomHorsaalLocation));

            } else {

                generatedClasses.add(uniClass);

            }

        }

        // check for conflicts, remove random class if one exists
        while (conflictExists(generatedClasses)) {

            // TODO: don't remove popular classes with a certain probability
            int randomRemoval = r.nextInt(generatedClasses.size());
            generatedClasses.remove(randomRemoval);

        }

        conflictExists(generatedClasses);

        return generatedClasses;
    }

    private boolean conflictExists(List<UniversityClass> classes) {

        for (int i = 0; i < classes.size(); ++i) {

            double endA = classes.get(i).getEndTime();

            for (int j = i + 1; j < classes.size(); ++j) {

                double startB = classes.get(j).getStartTime();

                if(startB <= endA) {
                    return true;
                }
                // no conflict iff startB > endA OR startA > endB
//                if (((startA <= endB) && (endA >=  startB)) || ((startB <= endA) && (endB >= startA))) {
//                    return true;
//                }
            }
        }

        return false;
    }

    private List<UniversityClass> removeClassesIntersection(List<UniversityClass> classes) {

        List<UniversityClass> resultClasses = new ArrayList();

        if (classes.size() == 1) {
            resultClasses = classes;
        } else {

            for (int i = 0; i < classes.size(); i++) {

                for (int j = i + 1; j < classes.size(); j++) {

                    UniversityClass class1 = classes.get(i);
                    UniversityClass class2 = classes.get(j);

                    if (class1.getStartTime() < class2.getStartTime() && class2.getStartTime() < class1.getEndTime()) {
                        // do nothing
                    } else {
                        resultClasses.add(class1);
                    }

                }

            }

        }

        return resultClasses;

    }

    private static List<UniversityClass> generateWholeSchedule() {

        List<UniversityClass> result = new ArrayList<>();
        boolean horsaalUsed = false;

        for (int i = getSecondsFromHours(1); i < getSecondsFromHours(10); i += 7200) {

            if (i % 7200 == 0) {
                horsaalUsed = false;
            }

            int maxNumberOfClasses = i < getSecondsFromHours(2) ? 10 : r.nextInt(15) + 30;

            for (int j = 0; j < maxNumberOfClasses; j++) {

                double popularity = r.nextDouble();
                int startTime = i;
                // to make endTime of the class to be not standard
                int endTime = i + (int) (getSecondsFromHours(2) * (r.nextDouble() * 0.3 + 0.7));
                String name = "class at " + (i / 3600);
                Coord location;

                if (popularity > horsaalClassPopularity && !horsaalUsed) {

                    location = horsaalCoordinates.get(0);
                    horsaalUsed = true;

                } else {

                    location = smallRoomCoordinates.get(r.nextInt(smallRoomCoordinates.size()));

                }

                result.add(new UniversityClass(name, startTime, endTime, location, popularity));

            }

        }

        return result;

    }

    private static int getSecondsFromHours(int hours) {

        return hours * 3600;

    }

}