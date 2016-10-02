import java.awt.*;
import java.util.ArrayList;


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

    public int compareTo(Object o) {
        if(o instanceof AStarNode) {
            return this.getFValue() - ((AStarNode) o).getFValue();
        }
        else
            return -1; //This doesn't really make sense in the context of compareTo, we can probably work a simpler solution
    }
    public boolean equals(Object o){
        if (o instanceof AStarNode){
            if (this.point.equals(((AStarNode)o).point)) return true;
            else return false;
        }
        else{
            return false;
        }
    }

    public ArrayList<AStarNode> getNeighbors(AStarNode[][] grid){
        ArrayList<AStarNode> nbrs = new ArrayList<>();
        if(grid[this.point.y+1][this.point.x].isValid) nbrs.add(grid[this.point.y+1][this.point.x]);
        if(grid[this.point.y-1][this.point.x].isValid) nbrs.add(grid[this.point.y-1][this.point.x]);
        if(grid[this.point.y][this.point.x+1].isValid) nbrs.add(grid[this.point.y][this.point.x+1]);
        if(grid[this.point.y][this.point.x-1].isValid) nbrs.add(grid[this.point.y][this.point.x-1]);
        return nbrs;
    }

}