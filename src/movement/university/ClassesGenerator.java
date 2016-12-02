/*
 * Copyright 2010 Aalto University, ComNet
 * Released under GPLv3. See LICENSE.txt for details.
 */
package movement.university;

import core.Coord;

import java.util.*;

public class ClassesGenerator {

    private List<UniversityClass> uniClasses;
    private Random r = new Random();

    public ClassesGenerator() {

        double magicNumber = 432.75;

        uniClasses = Arrays.asList(
                new UniversityClass("Connected Mobility", 36000, 39600, new Coord(277.85, -369.18 + magicNumber), 0.2),
                new UniversityClass("Machine Learning", 43200, 48600, new Coord(190.06, -379.66 + magicNumber), 0.6),
                new UniversityClass("Poker for Dummies", 50000, 55000, new Coord(551.03, -292.87 + magicNumber), 0.4),
                new UniversityClass("Economics for Dummies", 55000, 56000, new Coord(551.03, -292.87 + magicNumber), 0.4),
                new UniversityClass("Patterns in Software Engineering", 57600, 63000, new Coord(361.80, -354.96 + magicNumber), 0.6),
                new UniversityClass("Distributed Systems", 57600, 57600 + 10800, new Coord(458.96, -261.48 + magicNumber), 0.4),
                new UniversityClass("Databases for Dummies", 62000, 66000, new Coord(551.03, -292.87 + magicNumber), 0.4),
                new UniversityClass("Project Organization Management", 68400, 72000, new Coord(448.08, -344.92 + magicNumber), 0.4)
        );

    }

    /* Should return classes sorted by start time
     * Classes should not intersect */
    public List<UniversityClass> generateClasses() {

        int numberOfClasses = r.nextInt(3) + 1;

        List<UniversityClass> generatedClasses = getRandomClasses(numberOfClasses);

        while(generatedClasses.size() == 0) {

            generatedClasses = getRandomClasses(numberOfClasses);

        }

        generatedClasses = removeClassesIntersection(generatedClasses);

        return generatedClasses;

    }

    private List<UniversityClass> getRandomClasses(int numberOfClasses) {

        List<UniversityClass> result = new ArrayList();

        for (UniversityClass uniClass: uniClasses) {

            if(r.nextBoolean()) {

                result.add(uniClass);

            }

            if(result.size() == numberOfClasses) {
                break;
            }

        }

        return result;

    }

    private List<UniversityClass> removeClassesIntersection(List<UniversityClass> classes) {

        List<UniversityClass> resultClasses = new ArrayList();

        if(classes.size() == 1) {
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

}
