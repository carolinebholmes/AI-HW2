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
        }
        else {
			// call function to deal with certainty
        }
    }

    @Override
    public void addToWorld(World world) {
        isUncertain = world.getUncertain();
        this.world = world;
        super.addToWorld(world);
    }

    public static void main(String[] args) {
        try {
			World myWorld = new World("TestCases/myInputFile1.txt", true);
			
            MyRobot robot = new MyRobot();
            robot.addToWorld(myWorld);
			//myWorld.createGUI(400, 400, 200); // uncomment this and create a GUI; the last parameter is delay in msecs
			

			robot.travelToDestination();
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AStarNode[][] generateGraph(){
        if(this.world != null){
            int numCols = this.world.numCols();
            int numRows = this.world.numRows();
            Point goalPoint = this.world.getEndPos();
            Point startPoint = this.world.getStartPos();
            AStarNode[][] graph = new AStarNode[numRows][numCols];
            for(int r = 0; r < this.world.numRows(); r++){
                for(int c = 0; c < this.world.numCols(); c++){
                    String currTile = pingMap(new Point(c, r));
                    AStarNode anode = new AStarNode(new Point(c,r),Pattern.matches("[OFS]{1}", currTile));
                    anode.calculateHValue(goalPoint);
                    if(anode.point.equals(startPoint)) anode.setGValue(0);
                    graph[r][c] = anode;
                }
            }
            return graph;
        }else
            return null;
    }
}
