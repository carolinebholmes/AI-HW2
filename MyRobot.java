// Feel free to use this java file as a template and extend it to write your solver.
// ---------------------------------------------------------------------------------

import world.Robot;
import world.World;

import java.awt.*;
import java.util.*;
import java.util.regex.Pattern;

public class MyRobot extends Robot {
    public static boolean uncertainty = false;
    public static boolean doTest = true; //even if true, only tests further under uncertain conditions
    public static int INITIAL_TESTS = 160;
    public static double DECISION_THRESHOLD = 1.9; //in stdevs     150 and 2.5 are the lower limit, don't do it

    boolean isUncertain;
    World world;
	
    @Override
    public void travelToDestination(){
        travelToDestination(this.world.getStartPos());
    }

    public void travelToDestination(Point start) {
        if (isUncertain) {
			// call function to deal with uncertainty
            AStarNode[][] grid;
            ArrayList<Point> path;
            do {
                grid = probGenGraph();
                path = aStarSearch(grid, start);
            }while(path.size() == 0);
            for(Point tile : path){
                int x = this.getPosition().x;
                int y = this.getPosition().y;
                this.move(tile);
                if(x == this.getPosition().x && y == this.getPosition().y){//a move was called and the robot didn't move, ie tried to move to a wall
                    System.out.println("Invalid path, recalculating");
                    this.travelToDestination(new Point(x,y));
                    break;
                }

            }
        }
        else {
            AStarNode[][] grid = generateGraph();
			ArrayList<Point> path = aStarSearch(grid, this.world.getStartPos());
            System.out.println(path);
            if(path.size() == 0) {
                System.out.println("There is no valid path on the board.");
            }
            for(Point tile : path){
                this.move(tile);
            }
        }
    }

    @Override
    public void addToWorld(World world) {
        isUncertain = world.getUncertain();
        this.world = world;
        super.addToWorld(world);
    }

    public ArrayList<Point> aStarSearch(AStarNode[][] grid, Point startPoint){
        AStarNode start = grid[startPoint.x][startPoint.y];
        AStarNode end = grid[this.world.getEndPos().x][this.world.getEndPos().y];
        PriorityQueue<AStarNode> openSet = new PriorityQueue<AStarNode>();
        Set<AStarNode> closedSet = new TreeSet<AStarNode>();
        openSet.add(start);
        while(openSet.size() > 0){
            AStarNode current = openSet.poll();
            if(current.equals(end)){
                return this.retraceSteps(end);
            }
            closedSet.add(current);
            ArrayList<AStarNode> neighbors = current.getNeighbors(grid);
            for (AStarNode neighbor : neighbors) {
                int neighborG = current.gValue + 1;
                if (closedSet.contains(neighbor)) {
                    if(neighborG < neighbor.gValue)
                        closedSet.remove(neighbor);
                    else
                        continue;
                }
                System.out.println(neighborG);
                if(!openSet.contains(neighbor))
                    openSet.add(neighbor);
                else if(neighborG >= neighbor.gValue)
                    continue;
                //if at this point, this path is the best or equal to the best path discovered so far
                neighbor.prevNode = current;
                neighbor.gValue = neighborG;
            }
        }
        return new ArrayList<Point>();
    }

    public ArrayList<Point> retraceSteps(AStarNode end){
        AStarNode curr = end;
        Stack<AStarNode> rstack = new Stack<>();
        rstack.push(curr);
        while(curr.prevNode != null){
            curr = curr.prevNode;
            rstack.push(curr);
        }
        ArrayList<Point> path = new ArrayList<>();
        while(!rstack.empty()){
            path.add(rstack.pop().point);
        }
        path.remove(0); //don't want starting block in path apparenntly
        return path;
    }

    private AStarNode[][] generateGraph(){
        if(this.world != null){
            int numCols = this.world.numCols();
            int numRows = this.world.numRows();
            Point goalPoint = this.world.getEndPos();
            Point startPoint = this.world.getStartPos();
            AStarNode[][] graph = new AStarNode[numRows][numCols];
            for(int r = 0; r < this.world.numRows(); r++){
                for(int c = 0; c < this.world.numCols(); c++){
                    String currTile = pingMap(new Point(r, c));
                    AStarNode anode = new AStarNode(new Point(r,c),Pattern.matches("[OFS]{1}", currTile));
                    anode.calculateHValue(goalPoint);
                    if(anode.point.equals(startPoint)) anode.setGValue(0);
                    graph[r][c] = anode;
                }
            }
            return graph;
        }else
            return null;
    }
    private AStarNode[][] probGenGraph(){
        if(this.world != null) {
            int numCols = this.world.numCols();
            int numRows = this.world.numRows();
            Point goalPoint = this.world.getEndPos();
            Point startPoint = this.getPosition();
            AStarNode[][] graph = new AStarNode[numRows][numCols];
            for(int r = 0; r < numRows; r++){
                for(int c = 0; c < numCols; c++){
                    int numX = 0;
                    int numO = 0;
                    for(int i = 0; i < INITIAL_TESTS; i++){
                        String res = pingMap(new Point(r,c));
                        if(res.equals("X")) numX++;
                        else if(res.equals("O")) numO++;
                    }
                    int numPings = INITIAL_TESTS;
                    while(doTest && !decisionTest(numPings, numX, numO)){
                        String res = pingMap(new Point(r,c));
                        if(res.equals("X")) numX++;
                        else if(res.equals("O")) numO++;
                        numPings++;
                    }
                    System.out.println(numPings + " , "+numX);
                    if(numX + numO == numPings && ((doTest && (numX >= (.6 * numPings) - DECISION_THRESHOLD*Math.sqrt(numPings * .6 *.4))) || (!doTest && (numX > numPings/2)))) graph[r][c] = new AStarNode(new Point(r,c), false); //If the tile is probably an X
                    else{//If the tile is either the start or end tile or probably a valid O
                        AStarNode anode = new AStarNode(new Point(r,c),true);
                        anode.calculateHValue(goalPoint);
                        if(anode.point.equals(startPoint)) anode.setGValue(0);
                        graph[r][c] = anode;
                    }
                }
            }
            return graph;
        }
        return null;
    }

    public boolean decisionTest(double numPings, double numX, double numO){
        if(numPings != numX + numO)
            return true;
        double stdev = Math.sqrt(numPings * .6 *.4);
        if((numX <= (.4 * numPings) + DECISION_THRESHOLD*stdev) || (numX >= (.6 * numPings) - DECISION_THRESHOLD*stdev))
            return true;
        return false;

    }

    public static void main(String[] args) {
        try {
			World myWorld = new World("TestCases/myInputFile4.txt", uncertainty);
            MyRobot robot = new MyRobot();
            robot.addToWorld(myWorld);
			myWorld.createGUI(400, 400, 200); // uncomment this and create a GUI; the last parameter is delay in msecs
			robot.travelToDestination();
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }


}
