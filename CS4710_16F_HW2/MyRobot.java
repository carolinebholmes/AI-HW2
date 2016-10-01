// Feel free to use this java file as a template and extend it to write your solver.
// ---------------------------------------------------------------------------------

import world.Robot;
import world.World;

import java.awt.*;
import java.util.*;

public class MyRobot extends Robot {
    boolean isUncertain;
	
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
        super.addToWorld(world);
    }

    public void move(Point postion){
        /*
        Attempts to move the robot to the given position. 
        Position must be adjacent to current position and a legal position. 
        Returns the robot’s new position a er the move.
        */

    }

    public String pingMap(Point position){
        /*
        Robot a empts to view the map directly at the given position. 
        Returns the string associ- ated with that position. 
        If uncertainty is true, this may return the incorrect result 
            (more likely to be wrong if further away from robot).  
        is position can be any place on the map.
        */


    }

    public Point getPosition() {
        return 
    }

    public int getX() {

    }

    public int getY() {

    }

    public int getNumPings() {

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
}
