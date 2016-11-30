/*
 * Copyright 2010 Aalto University, ComNet
 * Released under GPLv3. See LICENSE.txt for details.
 */
package movement.university;

import core.Coord;

import java.util.*;

public class ClassesGenerator {

	private List<UniversityClass> uniClasses;

	public ClassesGenerator() {

		uniClasses = Arrays.asList(
				new UniversityClass("Machine Learning", 43200, 48600, new Coord(625.14, 188.92), 0.6),
				new UniversityClass("Connected Mobility", 36000, 39600, new Coord(618.04, 149.43), 0.2),
				new UniversityClass("Patterns in Software Engineering", 57600, 63000, new Coord(635.67, 186.80), 0.6),
				new UniversityClass("Project Organization Management", 68400, 72000, new Coord(190.06, 379.66), 0.4)
		);

	}

	public List<UniversityClass> generateClasses() {

		return uniClasses;

	}

}
