import java.awt.*;

/**
 * Created by student on 10/1/2016.
 */
public class AStarNode {

    public Point point;
    public AStarNode prevNode;
    public int gValue; //points from start
    public int hValue; //distance from target
    public boolean isValid = false;

    public AStarNode(Point point, boolean isValid) {
        this.point = point;
        this.isValid = isValid;
    }

    public void setGValue(int amount) {
        this.gValue = amount;
    }

    public void calculateHValue(Point goalPoint) {
        this.hValue = (Math.abs(point.x - goalPoint.x) + Math.abs(point.y - goalPoint.y));
    }

    public void calculateGValue(AStarNode point) {//change this hardcore
        this.gValue = point.gValue;
    }

    public int getFValue() {
        return this.gValue + this.hValue;
    }
}