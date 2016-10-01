import java.awt.*;


public class AStarNode {

    public Point point;
    public AStarNode prevNode;
    public int gValue; //points from start
    public int hValue; //distance from target
    public boolean isValid = false;

    public AStarNode(Point point, boolean isValid) {
        this.point = point;
        this.isValid = isValid;
        this.gValue = Integer.MAX_VALUE;
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