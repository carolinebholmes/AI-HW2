// Feel free to use this java file as a template and extend it to write your solver.
// ---------------------------------------------------------------------------------

import world.Robot;
import world.World;

import java.awt.*;
import java.util.*;
import java.util.regex.Pattern;

public class MyRobot extends Robot {
    boolean isUncertain;
    World world;
	
    @Override
    public void travelToDestination() {
        if (isUncertain) {
			// call function to deal with uncertainty
            AStarNode[][] grid;
            ArrayList<Point> path;
            do {
                grid = probGenGraph();
                path = aStarSearch(grid);
            }while(path.size() == 0);
            for(Point tile : path){
                this.move(tile);
            }
        }
        else {
            AStarNode[][] grid = generateGraph();
			ArrayList<Point> path = aStarSearch(grid);
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

    public ArrayList<Point> aStarSearch(AStarNode[][] grid){
        AStarNode start = grid[this.world.getStartPos().x][this.world.getStartPos().y];
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
                if (closedSet.contains(neighbor)) {
                    continue;
                }
                int neighborG = current.gValue + 1;
                if(!openSet.contains(neighbor))
                    openSet.add(neighbor);
                else if(neighborG >= neighbor.gValue)
                    continue;
                //if at this point, this path is the best until now
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
        path.remove(0);
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
            Point startPoint = this.world.getStartPos();
            AStarNode[][] graph = new AStarNode[numRows][numCols];
            for(int r = 0; r < numRows; r++){
                for(int c = 0; c < numCols; c++){
                    int numX = 0;
                    int numO = 0;
                    for(int i = 0; i < 100; i++){
                        String res = pingMap(new Point(r,c));
                        if(res.equals("X")) numX++;
                        else if(res.equals("O")) numO++;
                    }
                    if(numX + numO == 100 && numX >= 50) graph[r][c] = new AStarNode(new Point(r,c), false); //If the tile is probably an X
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

    public static void main(String[] args) {
        try {
			World myWorld = new World("TestCases/myInputFile4.txt", true);
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
