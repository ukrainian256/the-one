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
public class UniversityMovement extends MovementModel {

    Student student;
    /** sim map for the model */
    private SimMap map = null;

    /** the indexes of the OK map files or null if all maps are OK */
    private int [] okMapNodeTypes;

    /** how many map files are read */
    private int nrofMapFilesRead = 0;
    /** map cache -- in case last mm read the same map, use it without loading*/
    private static SimMap cachedMap = null;
    /** names of the previously cached map's files (for hit comparison) */
    private static List<String> cachedMapFiles = null;
    /** map based movement model's settings namespace ({@value})*/
    public static final String MAP_BASE_MOVEMENT_NS = "UniversityMovement";
    /** number of map files -setting id ({@value})*/
    public static final String NROF_FILES_S = "nrofMapFiles";
    /** map file -setting id ({@value})*/
    public static final String FILE_S = "mapFile";
    private int currentEventIndex = -1;

    public UniversityMovement(Settings settings) {
        super(settings);
        map = readMap();
        student = new Student();
    }

    /**
     * Copyconstructor.
     * @param mbm The MapBasedMovement object to base the new object to
     */
    protected UniversityMovement(UniversityMovement mbm) {
        super(mbm);
        this.okMapNodeTypes = mbm.okMapNodeTypes;
        this.map = mbm.map;
        student = new Student();
    }

    /**
     * Returns a (random) coordinate that is between two adjacent MapNodes
     */
    @Override
    public Coord getInitialLocation() {

        return new Coord(614.90, 254.13);

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

        if(currentEventIndex == -1) {

            p.addWaypoint(this.getInitialLocation());

        } else {

            ScheduledEvent currentEvent = student.getGeneratedEvents().get(currentEventIndex);
            p.addWaypoint(currentEvent.getLocation());

        }

        currentEventIndex++;

        if(currentEventIndex == student.getGeneratedEvents().size()) {

            p.addWaypoint(this.getInitialLocation());

        } else {

            ScheduledEvent nextEvent = student.getGeneratedEvents().get(currentEventIndex);
            p.addWaypoint(nextEvent.getLocation());

        }

        return p;
    }

    /**
     * Reads a sim map from location set to the settings, mirrors the map and
     * moves its upper left corner to origo.
     * @return A new SimMap based on the settings
     */
    private SimMap readMap() {
        SimMap simMap;
        Settings settings = new Settings(MAP_BASE_MOVEMENT_NS);
        WKTMapReader r = new WKTMapReader(true);

        if (cachedMap == null) {
            cachedMapFiles = new ArrayList<String>(); // no cache present
        }
        else { // something in cache
            // check out if previously asked map was asked again
            SimMap cached = checkCache(settings);
            if (cached != null) {
                nrofMapFilesRead = cachedMapFiles.size();
                return cached; // we had right map cached -> return it
            }
            else { // no hit -> reset cache
                cachedMapFiles = new ArrayList<String>();
                cachedMap = null;
            }
        }

        try {
            int nrofMapFiles = settings.getInt(NROF_FILES_S);

            for (int i = 1; i <= nrofMapFiles; i++ ) {
                String pathFile = settings.getSetting(FILE_S + i);
                cachedMapFiles.add(pathFile);
                r.addPaths(new File(pathFile), i);
            }

            nrofMapFilesRead = nrofMapFiles;
        } catch (IOException e) {
            throw new SimError(e.toString(),e);
        }

        simMap = r.getMap();
        checkMapConnectedness(simMap.getNodes());
        // mirrors the map (y' = -y) and moves its upper left corner to origo
        simMap.mirror();
        Coord offset = simMap.getMinBound().clone();
        simMap.translate(-offset.getX(), -offset.getY());
        checkCoordValidity(simMap.getNodes());

        cachedMap = simMap;
        return simMap;
    }

    /**
     * Checks map cache if the requested map file(s) match to the cached
     * sim map
     * @param settings The Settings where map file names are found
     * @return A cached map or null if the cached map didn't match
     */
    private SimMap checkCache(Settings settings) {
        int nrofMapFiles = settings.getInt(NROF_FILES_S);

        if (nrofMapFiles != cachedMapFiles.size() || cachedMap == null) {
            return null; // wrong number of files
        }

        for (int i = 1; i <= nrofMapFiles; i++ ) {
            String pathFile = settings.getSetting(FILE_S + i);
            if (!pathFile.equals(cachedMapFiles.get(i-1))) {
                return null;	// found wrong file name
            }
        }

        // all files matched -> return cached map
        return cachedMap;
    }

    /**
     * Checks that all map nodes can be reached from all other map nodes
     * @param nodes The list of nodes to check
     * @throws SettingsError if all map nodes are not connected
     */
    private void checkMapConnectedness(List<MapNode> nodes) {
        Set<MapNode> visited = new HashSet<MapNode>();
        Queue<MapNode> unvisited = new LinkedList<MapNode>();
        MapNode firstNode;
        MapNode next = null;

        if (nodes.size() == 0) {
            throw new SimError("No map nodes in the given map");
        }

        firstNode = nodes.get(0);

        visited.add(firstNode);
        unvisited.addAll(firstNode.getNeighbors());

        while ((next = unvisited.poll()) != null) {
            visited.add(next);
            for (MapNode n: next.getNeighbors()) {
                if (!visited.contains(n) && ! unvisited.contains(n)) {
                    unvisited.add(n);
                }
            }
        }

        if (visited.size() != nodes.size()) { // some node couldn't be reached
            MapNode disconnected = null;
            for (MapNode n : nodes) { // find an example node
                if (!visited.contains(n)) {
                    disconnected = n;
                    break;
                }
            }
            throw new SettingsError("SimMap is not fully connected. Only " +
                    visited.size() + " out of " + nodes.size() + " map nodes " +
                    "can be reached from " + firstNode + ". E.g. " +
                    disconnected + " can't be reached");
        }
    }

    /**
     * Checks that all coordinates of map nodes are within the min&max limits
     * of the movement model
     * @param nodes The list of nodes to check
     * @throws SettingsError if some map node is out of bounds
     */
    private void checkCoordValidity(List<MapNode> nodes) {
        // Check that all map nodes are within world limits
        for (MapNode n : nodes) {
            double x = n.getLocation().getX();
            double y = n.getLocation().getY();
            if (x < 0 || x > getMaxX() || y < 0 || y > getMaxY()) {
                throw new SettingsError("Map node " + n.getLocation() +
                        " is out of world  bounds "+
                        "(x: 0..." + getMaxX() + " y: 0..." + getMaxY() + ")");
            }
        }
    }

    /**
     * Returns the SimMap this movement model uses
     * @return The SimMap this movement model uses
     */
    public SimMap getMap() {
        return map;
    }

    public UniversityMovement replicate() {
        return new UniversityMovement(this);
    }

}
